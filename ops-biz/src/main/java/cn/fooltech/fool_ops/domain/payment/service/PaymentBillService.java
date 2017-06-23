package cn.fooltech.fool_ops.domain.payment.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fooltech.fool_ops.domain.cashier.service.BankBillService;
import cn.fooltech.fool_ops.domain.cashier.vo.BankBillVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;


/**
 * <p>收付款单网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-24 15:32:59
 */
@Service
public class PaymentBillService extends BaseService<PaymentBill, PaymentBillVo, String> {
	
	@Autowired
	private PaymentBillRepository paymentBillRepo;
	
	/**
	 * 收付款单勾对服务类
	 */
	@Autowired
	private PaymentCheckService checkService;
	
	/**
	 * 客户服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 供应商服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService stockPeriodService;
	
	/**
	 * 银行服务类
	 */
	@Autowired
	private BankService bankService;
	
	/**
	 * 单据单号生成规则服务类
	 */
	@Autowired
	private BillRuleService ruleService;

    /**
     * 银行账单服务
     */
    @Autowired
    private BankBillService bankBillService;

    /**
     * 科目服务
     */
    @Autowired
    private FiscalAccountingSubjectService fiscalAccountingSubjectService;
    
	/**
	 * 计划事件关联单据 服务类
	 */
	@Autowired
	private TaskBillService taskBillService;
	
	@Autowired
	private CapitalPlanService capitalPlanService;

	@Autowired
	private CapitalPlanDetailService detailService;

    /**
	 * 查询收付款单列表信息，按照收付款单主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param paymentBillVo
	 */
	public Page<PaymentBillVo> query(PaymentBillVo paymentBillVo, PageParamater pageParamater){
		
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		Page<PaymentBill> page = paymentBillRepo.findPageBy(paymentBillVo, accId, pageRequest);
		
		return getPageVos(page, pageRequest);
	}
	
	
	/**
	 * 单个收付款单实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public PaymentBillVo getVo(PaymentBill entity){
		if(entity == null)
			return null;
		PaymentBillVo vo = new PaymentBillVo();
		vo.setCode(entity.getCode());
		vo.setVouchercode(entity.getVouchercode());
		vo.setBillType(entity.getBillType());
		BigDecimal amount = NumberUtil.scale(entity.getAmount(), 2);
		BigDecimal totalCheckAmount = NumberUtil.scale(entity.getTotalCheckAmount(), 2);
		vo.setAmount(NumberUtil.stripTrailingZeros(amount).toPlainString());
		vo.setTotalCheckAmount(NumberUtil.stripTrailingZeros(totalCheckAmount).toPlainString());
//		vo.setAmount(NumberUtil.stripTrailingZeros(entity.getAmount()).toPlainString());
//		vo.setTotalCheckAmount(NumberUtil.stripTrailingZeros(entity.getTotalCheckAmount()).toPlainString());
		
		BigDecimal uncheckAmount = NumberUtil.subtract(entity.getAmount(), entity.getTotalCheckAmount());
//		String uncheckAmountStr = NumberUtil.stripTrailingZeros(uncheckAmount.toPlainString());
		vo.setTotalUnCheckAmount(NumberUtil.bigDecimalToStr(NumberUtil.scale(uncheckAmount, 2)));
		
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime(),DATE_TIME));
		vo.setCancelTime(DateUtilTools.date2String(entity.getCancelTime(),DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setOrgId(entity.getOrg().getFid());
		vo.setRecordStatus(entity.getRecordStatus());
		
		Bank bank = entity.getBank();
		if(bank!=null){
			vo.setBankId(bank.getFid());
			vo.setBankName(bank.getName());
			vo.setBankAccount(bank.getAccount());
		}
		
		Customer customer = entity.getCustomer();
		if(customer!=null){
			vo.setCustomerId(customer.getFid());
			vo.setCustomerName(customer.getName());
			vo.setCsvId(customer.getFid());
			vo.setCsvName(customer.getName());
			vo.setCustomerPhone(customer.getPhone());
			vo.setCustomerAddress(customer.getAddress());
		}
		
		Supplier supplier = entity.getSupplier();
		if(supplier!=null){
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
			vo.setCsvId(supplier.getFid());
			vo.setCsvName(supplier.getName());
			vo.setSupplierPhone(supplier.getPhone());
			vo.setSupplierAddress(supplier.getAddress());
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}
		
		StockPeriod period = entity.getStockPeriod();
		if(period!=null){
			vo.setStockPeriodId(period.getFid());
			vo.setStockPeriodName(period.getPeriod());
		}
		
		User auditor = entity.getAuditor();
		if(auditor!=null){
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		
		User cancelor = entity.getCancelor();
		if(cancelor!=null){
			vo.setCancelorId(cancelor.getFid());
			vo.setCancelorName(cancelor.getUserName());
		}
		
		User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}

		User saveAsBankBillOperator = entity.getSaveAsBankBillOperator();
		if(saveAsBankBillOperator!=null) {
		    vo.setSaveAsBankBillOperatorId(saveAsBankBillOperator.getFid());
		    vo.setSaveAsBankBillOperatorName(saveAsBankBillOperator.getUserName());
        }
		
		if(entity.getBillDate()!=null){
			vo.setBillDate(DateUtilTools.date2String(entity.getBillDate(), DATE_TIME));
		}
		
		if(entity.getOrg()!=null){
			vo.setOrgId(entity.getOrg().getFid());
		}
		
		return vo;
	}
	
	/**
	 * 删除收付款单<br>
	 */
	public RequestResult delete(String fid){
		PaymentBill bill = paymentBillRepo.findOne(fid);
		if(bill.getRecordStatus()!=PaymentBill.STATUS_UNAUDITED){
			return buildFailRequestResult("非待审核状态不能删除");
		}
		if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult("会计期间已结账");
		}
		paymentBillRepo.delete(bill);
		return buildSuccessRequestResult();
	}

    /**
     * 更新收付款单
     */
    public void cancelBankBillRecord(String fid){

        if (fid != null) {
            PaymentBill bill = paymentBillRepo.findOne(fid);

            if (bill != null) {
                bill.setSaveAsBankBillOperator(null);
                bill.setSaveAsBankBillTime(null);
                paymentBillRepo.save(bill);
            }
        }
    }
	
	/**
	 * 审核收付款单<br>
	 */
	public RequestResult passAudit(String fid){
		PaymentBill bill = paymentBillRepo.findOne(fid);
		if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		bill.setAuditor(SecurityUtil.getCurrentUser());
		bill.setAuditTime(Calendar.getInstance().getTime());
		bill.setRecordStatus(PaymentBill.STATUS_AUDITED);
		paymentBillRepo.save(bill);
		/**资金计划审核**/
		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
		if(capitalPlan!=null){
			RequestResult storagePassAudit = capitalPlanService.passAudit(capitalPlan.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			if(storagePassAudit.getReturnCode()==1){
				return storagePassAudit;
			}
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 作废收付款单<br>
	 */
	public RequestResult cancle(String fid){
		try {
			TaskBillVo taskBillVo = taskBillService.queryByRelation(fid);
			if(taskBillVo!=null){
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				return buildFailRequestResult("该单据已关联"+planName+"计划中的"+taskName+"事件!");
			}
			if(checkService.countByBillId(fid) > 0){
				return buildFailRequestResult("单据已被勾对，不能作废");
			}
			PaymentBill bill = paymentBillRepo.findOne(fid);
			if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
				return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
			}
			
			bill.setCancelor(SecurityUtil.getCurrentUser());
			bill.setCancelTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(PaymentBill.STATUS_CANCELED);
			paymentBillRepo.save(bill);
			/** 资金计划取消**/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
			if(capitalPlan!=null){
				RequestResult storagePassAudit = capitalPlanService.cancel(capitalPlan.getId(), CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(storagePassAudit.getReturnCode()==1){
					return storagePassAudit;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("作废收付款单出错!");
		}
		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 判断时间戳是否相等
	 */
	public boolean checkUpdateTime(PaymentBillVo vo, PaymentBill entity){
		String updateTime = DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME);
		if(updateTime.equals(vo.getUpdateTime())){
			return true;
		}
		return false;
	}
	

	/**
	 * 新增/编辑收付款单
	 * @param vo
	 */
	public RequestResult save(PaymentBillVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		PaymentBill entity = null;
		
		Date billDate = DateUtilTools.string2Date(vo.getBillDate(), DATE);
		String accountId = SecurityUtil.getFiscalAccountId();
		//会计期间
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, accountId);
		if(stockPeriod == null){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_NOT_EXIST, "单据日期没有对应会计期间");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_UN_USED, "会计期间未启用");
		}

		Integer billType = vo.getBillType();
		boolean exist = this.existTodayVoucherCode(vo.getVouchercode(), billType, vo.getFid(), billDate);
		if(exist)return buildFailRequestResult("原始单号同一日内重复");

		if(billType!=null){
			//收款
			if(billType==51){
				if(Strings.isNullOrEmpty(vo.getMemberId())) return buildFailRequestResult("收款人必须填写");
			}
			//付款
			if(billType==52){
				if(Strings.isNullOrEmpty(vo.getMemberId())) return buildFailRequestResult("付款人必须填写");
			}
		}
		if(StringUtils.isBlank(vo.getFid())){
			entity = new PaymentBill();
			entity = getEntity(vo, entity);
			entity.setStockPeriod(stockPeriod);
			entity.setBillDate(billDate);
			entity.setTotalCheckAmount(BigDecimal.ZERO);
			
			//temp.setCode(getNewCode(vo.getBillType()));
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setUpdateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			if(StringUtils.isNotBlank(vo.getCustomerId())){
				entity.setCustomer(customerService.get(vo.getCustomerId()));
			}
			
			if(StringUtils.isNotBlank(vo.getSupplierId())){
				entity.setSupplier(supplierService.get(vo.getSupplierId()));
			}
			paymentBillRepo.save(entity);
		}else {
			entity = paymentBillRepo.findOne(vo.getFid());
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
			if(entity.getRecordStatus()!=PaymentBill.STATUS_UNAUDITED){
				return buildFailRequestResult("非待审核状态不能修改");
			}
			if(!checkUpdateTime(vo,entity)){
				return buildFailRequestResult("数据已被修改，请刷新重试");
			}
			
			entity = getEntity(vo, entity);
			entity.setStockPeriod(stockPeriod);
			entity.setBillDate(billDate);

			//每次修改都更新
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setUpdateTime(Calendar.getInstance().getTime());
			if(StringUtils.isNotBlank(vo.getCustomerId())){
				entity.setCustomer(customerService.get(vo.getCustomerId()));
			}
			
			if(StringUtils.isNotBlank(vo.getSupplierId())){
				entity.setSupplier(supplierService.get(vo.getSupplierId()));
			}
			paymentBillRepo.save(entity);
		}
		
		Map<String, String> map = Maps.newHashMap();
		map.put("fid", entity.getFid());
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
		
		return buildSuccessRequestResult(map);
	}

	/**
	 * 判断是否存在原始单号
	 * @param voucherCode
	 * @param excludeId
	 * @param billDate
	 * @return
	 */
	public boolean existTodayVoucherCode(String voucherCode, Integer billType, String excludeId, Date billDate){
		if(Strings.isNullOrEmpty(voucherCode))return false;
		Long count = 0L;
		String accId = SecurityUtil.getFiscalAccountId();
		if(Strings.isNullOrEmpty(excludeId)) {
			count = paymentBillRepo.countTodayVoucherCode(accId, billType, voucherCode, billDate);
		}else{
			count = paymentBillRepo.countTodayVoucherCode(accId, billType, voucherCode, billDate, excludeId);
		}
		if(count>0)return true;
		else return false;
	}
	
	/**
	 * 获取新单号
	 * @param billType 单据类型
	 * @return
	 */
	@Transactional(readOnly = false)
	public String getNewCode(int billType){
		String orgId = SecurityUtil.getCurrentOrgId();
		return ruleService.createFullCode(orgId, billType);
	}
	

	/**
	 * VO转换Entity
	 * @throws Exception 
	 */
	public PaymentBill getEntity(PaymentBillVo vo, PaymentBill entity) {
		
		entity.setCode(vo.getCode());
		entity.setVouchercode(vo.getVouchercode());
		entity.setBillType(vo.getBillType());
		BigDecimal amount = new BigDecimal(vo.getAmount());
		entity.setAmount(NumberUtil.scale(amount, 2));
//		entity.setAmount(new BigDecimal(vo.getAmount()));
		entity.setTotalCheckAmount(BigDecimal.ZERO);
		entity.setDescribe(vo.getDescribe());
		entity.setToPublic(vo.getToPublic());
		
		if(StringUtils.isNotBlank(vo.getBankId())){
			entity.setBank(bankService.get(vo.getBankId()));
		}
		
		if(StringUtils.isNotBlank(vo.getCustomerId())){
			entity.setCustomer(customerService.get(vo.getCustomerId()));
		}
		
		if(StringUtils.isNotBlank(vo.getSupplierId())){
			entity.setSupplier(supplierService.get(vo.getSupplierId()));
		}
		
		if(StringUtils.isNotBlank(vo.getMemberId())){
			entity.setMember(memberService.get(vo.getMemberId()));
		}
		
		if(StringUtils.isNotBlank(vo.getStockPeriodId())){
			entity.setStockPeriod(stockPeriodService.get(vo.getStockPeriodId()));
		}
		
		return entity;
	}
	/**
	 * 保存收付款单和勾对表记录
	 * @param vo		             表单传输对象 - 收付款单
	 * @param ckBillType    勾对单据类型：11.采购入库;12.采购退货;15.采购发票;41.销售出货;42.销售退货;44.销售发票.
	 * @param billId 		 单据id
	 * @return
	 */
	@Transactional
	public RequestResult savePaymentReceived(PaymentBillVo vo,Integer ckBillType,String billId) {
		try {
			//设置单据日期为当前时间
			vo.setBillDate(DateUtils.getCurrentDate());
			//计算费用单累计收付款金额
			PaymentBill paymentBill = paymentBillRepo.findOne(billId);
			//计算费用单累计收付款金额
			BigDecimal totalPayAmount = paymentBill.getTotalCheckAmount();
			//费用单金额
			BigDecimal befAmount = paymentBill.getAmount();
			//输入费用金额
			BigDecimal amount = Strings.isNullOrEmpty(vo.getAmount())?BigDecimal.ZERO:new BigDecimal(vo.getAmount());
			//收/付款金额不能为0
			if(amount.compareTo(BigDecimal.ZERO)==0){
				return buildFailRequestResult("金额不能为0");
			}
			if(amount.compareTo(BigDecimal.ZERO) >= 0) return buildFailRequestResult("金额必须是负数");
			totalPayAmount = totalPayAmount.add(amount.multiply(new BigDecimal(-1)));
			//费用单累计收付款金额不能大于费用单金额。
			if(totalPayAmount.compareTo(befAmount)>0){
				return buildFailRequestResult("收/付款金额不能大于费用单金额");
			}
			Integer recordStatus = paymentBill.getRecordStatus();
			if(recordStatus==0) return buildFailRequestResult("请审核该费用单！");
			if(recordStatus==2) return buildFailRequestResult("该费用单已作废！");
			if(vo!=null){
				//单据不为空，设置供应商或者客户
				if(paymentBill.getSupplier()!=null){
					vo.setSupplierId(paymentBill.getSupplier().getFid());
				}
				if(paymentBill.getCustomer()!=null){
					//销售返利单对私时，客户为空
					if(vo.getToPublic()!=null&&vo.getToPublic()==1&&ckBillType==56){
						vo.setCustomerId(null);
					}else{
						vo.setCustomerId(paymentBill.getCustomer().getFid());
					}
					
				}
				//收付款单状态设置为已审核
				//新增/编辑收付款单
				RequestResult result = save(vo);
				
				if(!result.isSuccess()) return result;
				
				//获取收付款单新增编号
				Map<String, Object> map = result.getDataExt();
				String fid = map.get("fid")==null?"":(String) map.get("fid");
				//审核收付款单
				RequestResult passAudit = this.passAudit(fid);
				
				if(!passAudit.isSuccess()) return passAudit;
				
				if(Strings.isNullOrEmpty(fid)){
					if(vo.getBillType()==51){
						return buildFailRequestResult("收款单ID不能为空");
					}
					else if(vo.getBillType()==52){
						return buildFailRequestResult("付款单ID不能为空");
					}
				}
				//勾对表中自动添加一条记录
				PaymentCheckVo pmcVo=new PaymentCheckVo();
				pmcVo.setPaymentBillId(fid);//收付款单ID
				pmcVo.setBillType(ckBillType);//单据类型
				pmcVo.setBillId(billId);		//费用单FID
				pmcVo.setAmount(amount.abs());//勾对金额不能为负数
				pmcVo.setBillTotalPayAmount(totalPayAmount);
				pmcVo.setFreeAmount(null);
				RequestResult result2 = checkService.save(pmcVo);
				if(!result2.isSuccess()) return result2;
				//更新累计收付款金额
				paymentBill.setTotalCheckAmount(totalPayAmount);
				paymentBillRepo.save(paymentBill);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("保存收付款单和勾对表记录有误！");
		}
		return buildSuccessRequestResult();
	}


	@Override
	public CrudRepository<PaymentBill, String> getRepository() {
		return paymentBillRepo;
	}


	/**
	 * 查找未勾对的表
	 * @param paybill
	 * @param accId
	 * @param checkStartDay
	 * @param checkEndDay
	 * @param checkBillCode
	 * @param checkBillType
	 * @param excludeIds
	 * @param pageRequest
	 * @return
	 */
	public Page<PaymentBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay,
			String checkEndDay, String checkBillCode, Integer checkBillType, List<String> excludeIds,
			PageRequest pageRequest) {
		return paymentBillRepo.queryUnCheckBills(paybill, accId, checkStartDay,
				checkEndDay, checkBillCode, checkBillType, excludeIds, pageRequest);
	}
	
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param supplierId 供应商ID
	 * @return
	 */
	public long countBySupplier(String supplierId){
		return paymentBillRepo.countBySupplier(supplierId);
	}
	/**
	 * 统计与某个客户相关的单据数
	 * @param customerId 客户ID
	 * @return
	 */
	public long countByCustomer(String customerId) {
		return paymentBillRepo.countByCustomer(customerId);
	}
	/**
	 * 根据IDs查找收付款单
	 * @param ids
	 * @return
	 */
	public List<PaymentBillVo> getByIds(List<String> ids) {
		return getVos(paymentBillRepo.findByIds(ids));
	}

	/**
	 * 保存首付单到银行日记单
	 * @param ids
	 * @return
	 */
	public RequestResult savePaymentBill2BankBill(List<String> ids){
        List<PaymentBill> paymentBills = paymentBillRepo.findByIds(ids);

        // 結果集
        Map<String, String> rstMap = Maps.newHashMap();

        // 先筛选不符合条件的
        paymentBills.forEach(paymentBill -> add2MapWithReason(paymentBill, rstMap));

        // 符合条件的生成日记单
        paymentBills.stream().
                filter(paymentBill -> paymentBill.getAuditor()!=null).
                filter(paymentBill -> paymentBill.getRecordStatus()!=PaymentBill.STATUS_CANCELED).
                filter(paymentBill -> paymentBill.getSaveAsBankBillOperator()==null).
                forEach(paymentBill -> genBankBillByPaymentBill(paymentBill, rstMap));

        // return buildSuccessRequestResult(rstMap);
        return new RequestResult(RequestResult.RETURN_SUCCESS, "操作成功", rstMap);
	}

    /**
     * 挑选不符合条件的对象放进结果里
     */
    private void add2MapWithReason(PaymentBill paymentBill, Map<String, String> rstMap){
        if (paymentBill.getAuditor() == null) {
            rstMap.put(paymentBill.getCode(), "收付款单状态未审核");
        } else if (paymentBill.getSaveAsBankBillOperator() != null) {
            rstMap.put(paymentBill.getCode(), "收付款单无法重复生成日记单");
        } else if (paymentBill.getRecordStatus() == PaymentBill.STATUS_CANCELED) {
            rstMap.put(paymentBill.getCode(), "已作废无法生成日记单");
		}
    }

    /**
     * 生成银行日记账单
     */
    private void genBankBillByPaymentBill(PaymentBill paymentBill, Map<String, String> rstMap){
        BankBillVo bankBillVo = new BankBillVo();

//        bankBillVo.setType(3);
        bankBillVo.setSource(paymentBill.getFid());
        bankBillVo.setSettlementDate(DateUtilTools.time2String(paymentBill.getBillDate())); //业务日期取收付款单单据日期
        bankBillVo.setVoucherDate(DateUtilTools.time2String(new Date())); //单据日期取系统日期
        bankBillVo.setSettlementNo("");

        //账户取单据账号
        String bankId = paymentBill.getBank().getFid();
        FiscalAccountingSubject fiscalAccountingSubject = fiscalAccountingSubjectService.getSubjectFid(bankId);

        if (fiscalAccountingSubject == null) {
            rstMap.put(paymentBill.getCode(), "科目沒有关联");
        } else {
        	Short bankSubject = fiscalAccountingSubject.getBankSubject();
        	Short cashSubject = fiscalAccountingSubject.getCashSubject();
        	if(bankSubject!=null&&bankSubject==1){
        		 bankBillVo.setType(3);
        	}else if(cashSubject!=null&&cashSubject==1){
        		 bankBillVo.setType(5);
        	}else{
        		rstMap.put(paymentBill.getCode(), "科目不是现金或者银行科目");
        	}
            bankBillVo.setSubjectId(fiscalAccountingSubject.getFid()); //科目ID
            bankBillVo.setMemberId(paymentBill.getMember().getFid()); //经手人

            //金额取单据金额,收付费对应借 贷
            //摘要的格式为“收到/支付+供应商/客户+货款”，如：支付蠢材公司货款

            if (paymentBill.getBillType().compareTo(paymentBill.TYPE_INCOME)==0) {
                bankBillVo.setDebit(paymentBill.getAmount());
                bankBillVo.setCredit(BigDecimal.ZERO);
                if (paymentBill.getCustomer() != null) {
                    bankBillVo.setResume("收到"  + paymentBill.getCustomer().getName() + "货款");
                }
            } else if(paymentBill.getBillType().compareTo(paymentBill.TYPE_EXPEND)==0) {
                bankBillVo.setCredit(paymentBill.getAmount());
                bankBillVo.setDebit(BigDecimal.ZERO);
                if (paymentBill.getSupplier() != null) {
                    bankBillVo.setResume("支付" + paymentBill.getSupplier().getName() + "货款");
                }
            }

            String saveResult = bankBillService.saveBankBill(bankBillVo);
            if (saveResult.contains("ERROR")) {
                saveResult = saveResult.replace("ERROR:", "");
				rstMap.put(paymentBill.getCode(), saveResult);
            } else {
                paymentBill.setSaveAsBankBillOperator(SecurityUtil.getCurrentUser());
                paymentBill.setSaveAsBankBillTime(Calendar.getInstance().getTime());
                paymentBillRepo.save(paymentBill);
            }
        }
    }
    /**
     * 勾对付款
     * @param id
     * @param check
     * @return
     */
    public RequestResult paymentBillCheck(String id,short check){
    	PaymentBill entity=paymentBillRepo.findOne(id);
    	if(entity!=null){
        	
        	entity.setIsCheck(check);
        	paymentBillRepo.save(entity);
        	return buildSuccessRequestResult();
    	}else{
			return buildFailRequestResult("单据不存在!");
		}

    }
}
