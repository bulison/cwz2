package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;



/**
 * <p>仓库单据凭证制作服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月8日
 */
@Service
public class BillVoucherMakeService extends VoucherMakeAbstractService{
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentBillService;
	
	/**
	 * 仓库单据服务类
	 */
	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService warehouseBillService;
	
	/**
	 * 仓库单据明细服务类
	 */
	@Autowired
	private WarehouseBillDetailService warehouseBillDetailService;
	
	/**
	 * 仓库单据记录明细服务类
	 */
	@Autowired
	private WarehouseBillDetailService billDetailService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;

	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 凭证明细服务类
	 */
	@Autowired
	private VoucherDetailService voucherDetailService;
	
	@Autowired
	private VoucherRepository voucherRepo;
	
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
	 * 制作仓库单据的凭证
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult makeVoucher(VoucherMakeVo vo){
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		User user = SecurityUtil.getCurrentUser();
		
		//会计期间
		FiscalPeriod fiscalPeriod = null;
		RequestResult periodCheck = checkFiscalPeriod(fiscalAccount.getFid(), vo.getVoucherDate());
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
			WarehouseBill bill = warehouseBillService.get(billIds[i]);
			for(BillSubjectDetail templateDetail : templateDetails){
				FiscalAccountingSubject subject = templateDetail.getSubject();
				if(templateDetail.getSubjectSource() == BillSubjectDetail.SUBJECT_SOURCE_BANK){
					return new RequestResult(RequestResult.RETURN_FAILURE, "模板设置错误，找不到对应的银行科目!");
				}
				if(subject == null){
					return new RequestResult(RequestResult.RETURN_FAILURE, "模板明细缺少科目!");
				}
				RequestResult subjectCheck = check(billType, subject, bill);
				if(subjectCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
					return subjectCheck;
				}
			}
		}
		
		//添加凭证
		AuxiliaryAttr voucherWord = template.getVoucherWord(); //凭证字
		Integer number = voucherService.getMaxNumber(fiscalPeriod.getFid())+1; //凭证顺序号
		Integer voucherNumber = voucherRepo.getMaxVoucherNumberAuto(fiscalPeriod.getFid(), voucherWord.getFid()) + 1; //凭证号

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
		voucherRepo.save(voucher);
		
		for(int i=0; i<billIds.length; i++){
			WarehouseBill bill = warehouseBillService.get(billIds[i]);
			
			//如果单据之前已经生成过凭证，则不能再次生成
			VoucherBill record = voucherBillService.getRecord(bill.getFid(), bill.getBillType(), fiscalAccount.getFid());
			if(record != null){
				continue;
			}
			
			//添加凭证明细
			for(BillSubjectDetail templateDetail : templateDetails){
				FiscalAccountingSubject subject = getSubject(bill, templateDetail.getSubject());
				int hedge = templateDetail.getHedge()==null?BillSubjectDetail.HEDGE_BLUE:templateDetail.getHedge();
				saveVoucherDetail(voucher, bill, fiscalAccount, subject, templateDetail.getResume(), templateDetail.getDirection(), 
						templateDetail.getAmountSource(),hedge);
			}
			
			//添加单据、凭证关联
			VoucherBill voucherBill = new VoucherBill();
			voucherBill.setVoucher(voucher);
			voucherBill.setBillType(billType);
			voucherBill.setBillId(bill.getFid());
			voucherBill.setFiscalAccount(fiscalAccount);
			voucherBill.setOrg(org);
			voucherBill.setCreator(user);
			voucherBill.setCreateTime(Calendar.getInstance().getTime());
			voucherBillService.save(voucherBill);
		}
		
		RequestResult result = new RequestResult();
		result.setData(voucher.getFid());
		return result;
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
	private void saveVoucherDetail(Voucher voucher, WarehouseBill bill, FiscalAccount fiscalAccount, 
			FiscalAccountingSubject subject, String voucherResume, int direction, int amountSource, int hedge){
		AuxiliaryAttr rmbCurrency = getRmbCurrency(fiscalAccount);
		
		if(subject.getQuantitySign() == FiscalAccountingSubject.ACCOUNT){
			//核算数量
			List<WarehouseBillDetail> billDetails = billDetailService.getBillDetails(bill.getFid());
			for(WarehouseBillDetail billDetail : billDetails){
				BigDecimal amount = billDetailService.getAmount(billDetail, amountSource);
				amount = NumberUtil.multiply(amount, new BigDecimal(hedge));
				
				VoucherDetail voucherDetail = getVoucherDetail(voucher, bill, fiscalAccount, subject, rmbCurrency, voucherResume, amount, direction);
				voucherDetail.setQuantity(billDetail.getAccountQuentity());
				if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
					//核算货品
					voucherDetail.setUnit(billDetail.getAccountUint());
					voucherDetail.setGoods(billDetail.getGoods());
				}
				if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
					//核算仓库
					voucherDetail.setWarehouse(billDetail.getInWareHouse());
				}
				voucherDetailService.save(voucherDetail);
			}
		}
		else if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT && subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			//核算货品、仓库
			List<Object[]> datas = billDetailService.getDetailsByGroup(bill.getFid(), 2, amountSource);
			for(Object[] data : datas){
				WarehouseBillDetail billDetail = warehouseBillDetailService.get((String) data[0]);
				BigDecimal accountAmount = (BigDecimal) data[1];
				accountAmount = NumberUtil.multiply(accountAmount, new BigDecimal(hedge));
				VoucherDetail voucherDetail = getVoucherDetail(voucher, bill, fiscalAccount, subject, rmbCurrency, voucherResume, accountAmount, direction);
				voucherDetail.setUnit(billDetail.getAccountUint());
				voucherDetail.setGoods(billDetail.getGoods());
				voucherDetail.setWarehouse(billDetail.getInWareHouse());
				voucherDetailService.save(voucherDetail);
			}
		}
		else if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			//核算仓库
			List<Object[]> datas = billDetailService.getDetailsByGroup(bill.getFid(), 1, amountSource);
			for(Object[] data : datas){
				WarehouseBillDetail billDetail = warehouseBillDetailService.get((String) data[0]);
				BigDecimal accountAmount = (BigDecimal) data[1];
				accountAmount = NumberUtil.multiply(accountAmount, new BigDecimal(hedge));
				VoucherDetail voucherDetail = getVoucherDetail(voucher, bill, fiscalAccount, subject, rmbCurrency, voucherResume, accountAmount, direction);
				voucherDetail.setWarehouse(billDetail.getInWareHouse());
				voucherDetailService.save(voucherDetail);
			}
		}
		else if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			//核算货品
			List<Object[]> datas = billDetailService.getDetailsByGroup(bill.getFid(), 0, amountSource);
			for(Object[] data : datas){
				WarehouseBillDetail billDetail = warehouseBillDetailService.get((String) data[0]);
				BigDecimal accountAmount = (BigDecimal) data[1];
				accountAmount = NumberUtil.multiply(accountAmount, new BigDecimal(hedge));
				VoucherDetail voucherDetail = getVoucherDetail(voucher, bill, fiscalAccount, subject, rmbCurrency, voucherResume, accountAmount, direction);
				voucherDetail.setUnit(billDetail.getAccountUint());
				voucherDetail.setGoods(billDetail.getGoods());
				voucherDetailService.save(voucherDetail);
			}
		}
		else{
			//不核算货品、仓库、数量
			BigDecimal amount = billDetailService.getTotalAmount(bill.getFid(), amountSource);
			amount = NumberUtil.multiply(amount, new BigDecimal(hedge));
			VoucherDetail voucherDetail = getVoucherDetail(voucher, bill, fiscalAccount, subject, rmbCurrency, voucherResume, amount, direction);
			voucherDetailService.save(voucherDetail);
		}
	}
	
	/**
	 * 获取凭证明细
	 * @param voucher 凭证
	 * @param bill 单据
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param currency 币别
	 * @param voucherResume 摘要
	 * @param amount 金额
	 * @param direction 方向
	 * @return
	 */
	private VoucherDetail getVoucherDetail(Voucher voucher, WarehouseBill bill, FiscalAccount fiscalAccount, 
			FiscalAccountingSubject subject, AuxiliaryAttr currency, String voucherResume, BigDecimal amount, int direction){
		VoucherDetail voucherDetail = new VoucherDetail();
		voucherDetail.setVoucher(voucher);
		voucherDetail.setResume(voucherResume);
		voucherDetail.setAccountingSubject(subject);
		voucherDetail.setFiscalAccount(fiscalAccount);
		voucherDetail.setOrg(SecurityUtil.getCurrentOrg());
		voucherDetail.setCreator(SecurityUtil.getCurrentUser());
		voucherDetail.setCreateTime(Calendar.getInstance().getTime());
		voucherDetail.setUpdateTime(Calendar.getInstance().getTime());
		//借方金额、贷方金额
		if(direction == FiscalAccountingSubject.DIRECTION_BORROW){
			voucherDetail.setDebitAmount(amount);
		}
		else{
			voucherDetail.setCreditAmount(amount);
		}
		//核算销售商
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setCustomer(bill.getCustomer());
		}
		//核算供应商
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setSupplier(bill.getSupplier());
		}
		//核算部门
		if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			switch(bill.getBillType()){
				case WarehouseBuilderCodeHelper.pdd:
				case WarehouseBuilderCodeHelper.dcd:
				case WarehouseBuilderCodeHelper.bsd:{
					Member member = bill.getInMember();
					if(member != null){
						voucherDetail.setDepartment(member.getDept());
					}
					break;
				}
				default:{
					voucherDetail.setDepartment(bill.getDept());
					break;
				}
			}
		}
		//核算业务员
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setMember(bill.getInMember());
		}
		//核算外币
		if(subject.getCurrencySign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setCurrency(currency);
			voucherDetail.setExchangeRate(BigDecimal.ONE);
			voucherDetail.setCurrencyAmount(amount);
		}
		return voucherDetail;
	}
	
	/**
	 * 根据约定规则，获取科目
	 * @param bill
	 * @param subject
	 * @return
	 */
	private FiscalAccountingSubject getSubject(WarehouseBill bill, final FiscalAccountingSubject subject){
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
	 * 普通单据校验规则
	 * @param billType 单据类型
	 * @param subject 科目
	 * @return
	 */
	@Override
	public RequestResult check(Integer billType, final FiscalAccountingSubject subject, Object ... args){
		if(subject.getFlag() == FiscalAccountingSubject.FLAG_PARENT){
			FiscalAccountingSubject resultSubject = getSubject((WarehouseBill) args[0], subject);
			if(resultSubject == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目设置错误!");
			}
			else{
				return new RequestResult();
			}
		}
		
		String subjectName = subject.getName();
		switch (billType) {
			case WarehouseBuilderCodeHelper.cgfp:{
				//采购发票
				if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算销售商!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算仓库!");
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.xsfp:{
				//销售发票
				if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算供应商!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算仓库!");
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.cgth:
			{
				//采购入库、采购退货
				if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算销售商!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.xsch:
			case WarehouseBuilderCodeHelper.xsth:
			{
				//销售出货、销售退货
				if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算供应商!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.pdd:
			case WarehouseBuilderCodeHelper.dcd:
			case WarehouseBuilderCodeHelper.bsd:
			case WarehouseBuilderCodeHelper.scll:
			case WarehouseBuilderCodeHelper.sctl:
			case WarehouseBuilderCodeHelper.cprk:
			case WarehouseBuilderCodeHelper.cptk:	
			{
				//盘点单、调仓单、报损单、生产领料单、生产退料、成品入库单
				if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算供应商!");
				}
				if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算销售商!");
				}
				if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
					return new RequestResult(RequestResult.RETURN_FAILURE, subjectName + "科目设置了核算项目!");
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
