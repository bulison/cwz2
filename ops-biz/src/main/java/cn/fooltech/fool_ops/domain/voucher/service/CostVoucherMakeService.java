package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;



/**
 * <p>费用单凭证制作网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月8日
 */
@Service("ops.CostVoucherMakeService")
public class CostVoucherMakeService extends VoucherMakeAbstractService{
	
	/**
	 * 费用单服务类
	 */
	@Autowired
	private CostBillService costBillService;
	
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
	 * 销售商服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 供应商服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
	/**
	 * 财务科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
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
	 * 制作费用单的凭证
	 */
	@Override
	@Transactional
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
		
		//校验科目
		Integer billType = vo.getBillType();
		String[] billIds = vo.getBillIds().split(",");
		for(int i=0; i<billIds.length; i++){
			CostBill costBill = costBillService.get(billIds[i]);
			
			//销售商、供应商
			CustomerSupplierView csv = costBill.getCsv();
			Customer customer = null;
			Supplier supplier = null;
			if(csv != null){
				customer = customerService.get(csv.getFid());
				supplier = supplierService.get(csv.getFid());
			}
			
			for(BillSubjectDetail templateDetail : templateDetails){
				boolean isBankSubject = false; //是否为银行科目
				FiscalAccountingSubject subject = null;
				if(templateDetail.getSubjectSource() == BillSubjectDetail.SUBJECT_SOURCE_BANK){
					isBankSubject = true;
					Bank bank = costBill.getBank();
					if(bank == null){
						return new RequestResult(RequestResult.RETURN_FAILURE, 
								String.format("模板设置错误，单据(%s)的银行账号为空，找不到对应的银行科目!", costBill.getCode()));
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
				RequestResult subjectCheck = check(billType, subject, customer, supplier, isBankSubject);
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
			CostBill costBill = costBillService.get(billIds[i]);
			
			//如果单据之前已经生成过凭证，则不能再次生成
			VoucherBill record = voucherBillService.getRecord(billIds[i], billType, fiscalAccount.getFid());
			if(record != null){
				continue;
			}
			
			//销售商、供应商
			CustomerSupplierView csv = costBill.getCsv();
			Customer customer = null;
			Supplier supplier = null;
			if(csv != null){
				customer = customerService.get(csv.getFid());
				supplier = supplierService.get(csv.getFid());
			}
			
			//添加凭证明细
			for(BillSubjectDetail templateDetail : templateDetails){
				FiscalAccountingSubject subject = null;
				if(templateDetail.getSubjectSource() == BillSubjectDetail.SUBJECT_SOURCE_BANK){
					subject = subjectService.getBankSubject(SecurityUtil.getFiscalAccountId(), costBill.getBank().getFid());
				}
				else{
					subject = templateDetail.getSubject();
				}
				saveVoucherDetail(voucher, costBill, fiscalAccount, subject, customer, supplier, templateDetail.getResume(), 
						templateDetail.getDirection(), templateDetail.getAmountSource());
			}
			
			//添加单据、凭证关联
			VoucherBill voucherBill = new VoucherBill();
			voucherBill.setVoucher(voucher);
			voucherBill.setBillType(billType);
			voucherBill.setBillId(costBill.getFid());
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
	 * 添加凭证明细
	 * @param voucher 凭证
	 * @param costBill 费用单
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param customer 客户
	 * @param supplier 供应商
	 * @param voucherResume 摘要
	 * @param direction 方向
	 * @param amountSource 金额来源
	 */
	public void saveVoucherDetail(Voucher voucher, CostBill costBill, FiscalAccount fiscalAccount, FiscalAccountingSubject subject, 
			Customer customer, Supplier supplier, String voucherResume, int direction, int amountSource){
		VoucherDetail voucherDetail = new VoucherDetail();
		voucherDetail.setVoucher(voucher);
		voucherDetail.setResume(voucherResume);
		voucherDetail.setAccountingSubject(subject);
		voucherDetail.setFiscalAccount(fiscalAccount);
		voucherDetail.setOrg(SecurityUtil.getCurrentOrg());
		voucherDetail.setCreator(SecurityUtil.getCurrentUser());
		voucherDetail.setCreateTime(Calendar.getInstance().getTime());
		voucherDetail.setUpdateTime(Calendar.getInstance().getTime());
		
		BigDecimal amount = costBill.getIncomeAmount().add(costBill.getFreeAmount());
		if(amountSource != BillSubjectDetail.AMOUNT_SOURCE_BILL){
			amount = BigDecimal.ZERO;
		}
		
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
			voucherDetail.setCustomer(customer);
		}
		//核算供应商
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setSupplier(supplier);
		}
		//核算部门
		if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setDepartment(costBill.getDept());
		}
		//核算业务员
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setMember(costBill.getMember());
		}
		//核算外币
		if(subject.getCurrencySign() == FiscalAccountingSubject.ACCOUNT){
			BigDecimal money = costBill.getFreeAmount().add(costBill.getIncomeAmount());
			voucherDetail.setCurrency(getRmbCurrency(fiscalAccount));
			voucherDetail.setExchangeRate(BigDecimal.ONE);
			voucherDetail.setCurrencyAmount(money);
		}
		voucherDetailService.save(voucherDetail);
	}
	
	@Override
	public RequestResult check(Integer billType, FiscalAccountingSubject subject, Object ... args) {
		Customer customer = (Customer) args[0]; //客户
		Supplier supplier = (Supplier) args[1]; //供应商
		boolean isBankSubject = (Boolean) args[2];//是否为银行科目
		
		if(isBankSubject){
			if(isSigned(subject)){
				return new RequestResult(RequestResult.RETURN_FAILURE, String.format("银行科目(%s)勾选了核算项目!", subject.getName()));
			}
			else{
				return new RequestResult();
			}
		}
		
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT && customer == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, subject.getName() + "科目设置了核算销售商!");
		}
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT && supplier == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, subject.getName() + "科目设置了核算供应商!");
		}
		if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			return new RequestResult(RequestResult.RETURN_FAILURE, subject.getName() + "科目设置了核算仓库!");
		}
		if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
			return new RequestResult(RequestResult.RETURN_FAILURE, subject.getName() + "科目设置了核算项目!");
		}
		if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			return new RequestResult(RequestResult.RETURN_FAILURE, subject.getName() + "科目设置了核算货品!");
		}
		return new RequestResult();
	}
	
}
