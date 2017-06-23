package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;



/**
 * <p>收付款单凭证制作服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月8日
 */
@Service("ops.PaymentVoucherMakeService")
public class PaymentVoucherMakeService extends VoucherMakeAbstractService{
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentBillService;
	
	/**
	 * 财务账套服务类
	 */
	@Autowired
	private FiscalAccountService fiscalAccountService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 凭证明细服务类
	 */
	@Autowired
	private VoucherDetailService voucherDetailService;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService fiscalPeriodService;
	
	/**
	 * 单据、会计科目关联模板服务类
	 */
	@Autowired
	private BillSubjectService billSubjectService;
	
	/**
	 * 单据关联会计科目模板明细服务类
	 */
	@Autowired
	private BillSubjectDetailService billSubjectDetailService;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 制作收付款单的凭证
	 * @param vo
	 * @return
	 */
	public RequestResult makeVoucher(VoucherMakeVo vo){
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		
		//会计期间
		FiscalPeriod fiscalPeriod = null;
		RequestResult periodCheck = checkFiscalPeriod(SecurityUtil.getFiscalAccountId(), vo.getVoucherDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		else{
			fiscalPeriod = (FiscalPeriod) periodCheck.getData();
		}
		
		if(StringUtils.isBlank(vo.getBillIds())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "请选择单据!");
		}
		
		//单据、会计科目关联模板
		BillSubject template = billSubjectService.get(vo.getTemplateId());
		if(template == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "模板不存在或已被删除!");
		}
		if(template.getBillType() != vo.getBillType()){
			return new RequestResult(RequestResult.RETURN_FAILURE, "模板选择错误!!");
		}
		
		//单据关联会计科目模板明细
		List<BillSubjectDetail> templateDetails = billSubjectDetailService.getByTemplateId(template.getFid());
		if(CollectionUtils.isEmpty(templateDetails)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "模板内容为空!");
		}
		
		//校验借方、贷方科目
		Integer billType = vo.getBillType();
		String[] billIds = vo.getBillIds().split(",");
		for(int i=0; i<billIds.length; i++){
			PaymentBill bill = paymentBillService.get(billIds[i]);
			for(BillSubjectDetail templateDetail : templateDetails){
				boolean isBankSubject = false; //是否为银行科目
				FiscalAccountingSubject subject = null;
				if(templateDetail.getSubjectSource() == BillSubjectDetail.SUBJECT_SOURCE_BANK){
					isBankSubject = true;
					Bank bank = bill.getBank();
					if(bank == null){
						return new RequestResult(RequestResult.RETURN_FAILURE, 
								String.format("模板设置错误，单据(%s)的银行账号为空，找不到对应的银行科目!", bill.getCode()));
					}
					subject = subjectService.getBankSubject(SecurityUtil.getFiscalAccountId(), bank.getFid());
					if(subject == null){
						return new RequestResult(RequestResult.RETURN_FAILURE, String.format("%s对应的银行科目不存在!", bank.getName()));
					}
				}
				else {
					subject = templateDetail.getSubject();
					if(subject == null){
						return new RequestResult(RequestResult.RETURN_FAILURE, "模板明细缺少科目!");
					}
				}
				RequestResult subjectCheck = check(billType, subject, bill, isBankSubject);
				if(subjectCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
					return subjectCheck;
				}
			}
		}
		
		//添加凭证
		AuxiliaryAttr voucherWord = template.getVoucherWord(); //凭证字
		int number = voucherService.getMaxNumber(fiscalPeriod.getFid()) + 1; //凭证顺序号
		int voucherNumber = voucherService.getMaxVoucherNumber(fiscalAccount.getFid(), fiscalPeriod.getFid(), voucherWord.getFid()) + 1; //凭证号
		Voucher voucher = new Voucher();
		voucher.setNumber(number);
		voucher.setOrg(SecurityUtil.getCurrentOrg());
		voucher.setCreator(SecurityUtil.getCurrentUser());
		voucher.setVoucherWord(voucherWord);
		voucher.setVoucherNumber(voucherNumber);
		voucher.setVoucherDate(vo.getVoucherDate());
		voucher.setAccessoryNumber(vo.getAccessoryNumber());
		voucher.setFiscalPeriod(fiscalPeriod);
		voucher.setFiscalAccount(fiscalAccount);
		voucher.setCreateTime(Calendar.getInstance().getTime());
		voucher.setUpdateTime(Calendar.getInstance().getTime());
		voucher.setVoucherWordNumber(voucherWord.getName() + "-" + voucherNumber);
		voucherService.save(voucher);
		
		for(int i=0; i<billIds.length; i++){
			PaymentBill bill = paymentBillService.get(billIds[i]);
			
			//如果单据之前已经生成过凭证，则不能再次生成
			VoucherBill record = voucherBillService.getRecord(bill.getFid(), bill.getBillType(), fiscalAccount.getFid());
			if(record != null){
				continue;
			}
			
			//添加凭证明细
			for(BillSubjectDetail templateDetail : templateDetails){
				FiscalAccountingSubject subject = null;
				if(templateDetail.getSubjectSource() == BillSubjectDetail.SUBJECT_SOURCE_BANK){
					subject = subjectService.getBankSubject(SecurityUtil.getFiscalAccountId(), bill.getBank().getFid());
				}
				else{
					subject = getSubject(bill, templateDetail.getSubject());
				}
				saveVoucherDetail(voucher, bill, fiscalAccount, subject, templateDetail.getResume(), templateDetail.getDirection(), templateDetail.getAmountSource());
			}
			
			//添加单据、凭证关联
			VoucherBill voucherBill = new VoucherBill();
			voucherBill.setVoucher(voucher);
			voucherBill.setBillType(billType);
			voucherBill.setBillId(bill.getFid());
			voucherBill.setFiscalAccount(fiscalAccount);
			voucherBill.setOrg(SecurityUtil.getCurrentOrg());
			voucherBill.setCreator(SecurityUtil.getCurrentUser());
			voucherBill.setCreateTime(Calendar.getInstance().getTime());
			voucherBillService.save(voucherBill);
		}
		
		RequestResult requestResult = new RequestResult();
		requestResult.setData(voucher.getFid());
		return requestResult;
	}
	
	/**
	 * 添加凭证明细信息
	 * @param voucher 凭证
	 * @param bill 单据
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param voucherResume 摘要
	 * @param direction 方向
	 * @param amountSource 金额来源
	 * @return
	 */
	private void saveVoucherDetail(Voucher voucher, PaymentBill bill, FiscalAccount fiscalAccount, 
			FiscalAccountingSubject subject, String voucherResume, int direction, int amountSource){
		VoucherDetail voucherDetail = new VoucherDetail();
		voucherDetail.setVoucher(voucher);
		voucherDetail.setResume(voucherResume);
		voucherDetail.setAccountingSubject(subject);
		voucherDetail.setFiscalAccount(fiscalAccount);
		voucherDetail.setOrg(SecurityUtil.getCurrentOrg());
		voucherDetail.setCreator(SecurityUtil.getCurrentUser());
		voucherDetail.setCreateTime(Calendar.getInstance().getTime());
		voucherDetail.setUpdateTime(Calendar.getInstance().getTime());
		
		BigDecimal amount = amountSource != BillSubjectDetail.AMOUNT_SOURCE_BILL ? BigDecimal.ZERO : bill.getAmount();
		
		//借方金额、贷方金额
		if(direction == FiscalAccountingSubject.DIRECTION_BORROW){
			voucherDetail.setDebitAmount(amount);
		}
		else{
			voucherDetail.setCreditAmount(amount);
		}
		//核算数量
		if(subject.getQuantitySign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setQuantity(BigDecimal.ZERO);
		}
		//核算销售商
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setCustomer(bill.getCustomer());
		}
		//核算供应商
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setSupplier(bill.getSupplier());
		}
		//核算业务员
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setMember(bill.getMember());
		}
		//核算部门
		if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setDepartment(bill.getMember().getDept());
		}
		//核算外币
		AuxiliaryAttr rmbCurrency = getRmbCurrency(fiscalAccount);
		if(subject.getCurrencySign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setCurrency(rmbCurrency);
			voucherDetail.setExchangeRate(BigDecimal.ONE);
			voucherDetail.setCurrencyAmount(bill.getAmount());
		}
		voucherDetailService.save(voucherDetail);
	}
	
	/**
	 * 根据约定规则，获取科目
	 * @param bill
	 * @param subject
	 * @return
	 */
	private FiscalAccountingSubject getSubject(PaymentBill bill, final FiscalAccountingSubject subject){
		if(subject.getFlag() == FiscalAccountingSubject.FLAG_CHILD){
			return subject;
		}
		
		if(specialTypes.contains(bill.getBillType())){
			return null;
		}
		
		Customer customer = bill.getCustomer();
		Supplier supplier = bill.getSupplier();
		FiscalAccountingSubject resultSubject = null;
		Set<FiscalAccountingSubject> childs = subject.getChilds();
		if(CollectionUtils.isNotEmpty(childs)){
			for(FiscalAccountingSubject childSubject : childs){
				if(customer != null && childSubject.getRelationType() != null && StringUtils.isNotBlank(childSubject.getRelationId())){
					if(childSubject.getRelationType() == FiscalAccountingSubject.RELATION_CUSTOMER && customer.getFid().equals(childSubject.getRelationId())){
						resultSubject = childSubject;
						break;
					}
				}
				if(supplier != null && childSubject.getRelationType() != null && StringUtils.isNotBlank(childSubject.getRelationId())){
					if(childSubject.getRelationType() == FiscalAccountingSubject.RELATION_SUPPLIER && supplier.getFid().equals(childSubject.getRelationId())){
						resultSubject = childSubject;
						break;
					}
				}
			}
		}
		if(resultSubject == null || isSigned(resultSubject)){
			return null;
		}
		else{
			return resultSubject;
		}
	}
	
	/**
	 * 校验规则
	 * @param billType 单据类型
	 * @param subject 科目
	 * @return
	 */
	@Override
	public RequestResult check(Integer billType, FiscalAccountingSubject subject, Object ... args){
		PaymentBill paymentBill = (PaymentBill) args[0]; //单据
		boolean isBankSubject = (Boolean) args[1]; //是否为银行科目
		
		if(isBankSubject){
			if(isSigned(subject)){
				return new RequestResult(RequestResult.RETURN_FAILURE, String.format("银行科目(%s)勾选了核算项目!", subject.getName()));
			}
			else{
				return new RequestResult();
			}
		}
		
		if(subject.getFlag() == FiscalAccountingSubject.FLAG_PARENT){
			FiscalAccountingSubject resultSubject = getSubject(paymentBill, subject);
			if(resultSubject == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目设置错误!");
			}
			else{
				return new RequestResult();
			}
		}
		
		String subjectName = subject.getName();
		switch (billType) {
			case WarehouseBuilderCodeHelper.fkd:
			case WarehouseBuilderCodeHelper.cgfld:
			{
				//付款单、采购返利单
				if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算销售商!");
				}
				if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算仓库!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算货品!");
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.skd:
			case WarehouseBuilderCodeHelper.xsfld:
			{
				//收款单、销售返利单
				if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算供应商!");
				}
				if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算仓库!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算货品!");
				}
				break;
			}
			
			default:{
				throw new RuntimeException("无法处理未知单据!");
			}
		}
		return new RequestResult();
	}
}
