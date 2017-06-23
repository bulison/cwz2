package cn.fooltech.fool_ops.domain.cost.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalPeriodRepository;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerSupplierService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.BillRuleVo;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.cost.vo.CostBillVo;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>费用单网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-10-7
 */
@Service
public class CostBillService extends BaseService<CostBill, CostBillVo, String> {

	
	@Autowired
	private BillRuleService billRuleService;
	/**
	 * 收付款对单网页服务类
	 */
	@Autowired
	private PaymentCheckService paymentCheckWebService;
	/**
	 * 收付款单网页服务类
	 */
	@Autowired
	private PaymentBillService paymentBillWebService;
	/**
	 * 费用单服务类
	 */
	@Autowired
	private CostBillRepository costBillRepo;
	
	/**
	 * 费用单勾对服务类
	 */
	@Autowired
	private CostBillCheckService checkService;
	
	/**
	 * 客户供应商服务类
	 */
	@Autowired
	private CustomerSupplierService csService;
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
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
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;

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
	 * 收付款单勾对服务类
	 */
	@Autowired
	private PaymentCheckService paymentCheckService;

	/**
	 * 财务期间
	 */
	@Autowired
	private FiscalPeriodService fiscalPeriodService;
	/**
	 * 计划事件关联单据 服务类
	 */
	@Autowired
	private TaskBillService taskBillService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CapitalPlanService capitalPlanService;
	
	@Autowired
	private CapitalPlanDetailService detailService;
	
	/**
	 * 查询费用单列表信息，按照收付款单单号降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<CostBillVo> query(CostBillVo vo, PageParamater pageParamater){
		
		String accId = SecurityUtil.getFiscalAccountId();
		
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		
		Page<CostBill> page = costBillRepo.findPageBy(accId, vo, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	
	/**
	 * 单个收付款单实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public CostBillVo getVo(CostBill entity){
		if(entity == null)
			return null;
		CostBillVo vo = new CostBillVo();
		vo.setCode(entity.getCode());
		vo.setVoucherCode(entity.getVoucherCode());
		vo.setDescribe(entity.getDescribe());
		vo.setFreeAmount(NumberUtil.stripTrailingZeros(entity.getFreeAmount()));
		vo.setIncomeAmount(NumberUtil.stripTrailingZeros(entity.getIncomeAmount()));
		vo.setTotalCheckAmount(NumberUtil.stripTrailingZeros(entity.getTotalCheckAmount()));
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime(),DATE_TIME));
		vo.setCancelTime(DateUtilTools.date2String(entity.getCancelTime(),DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setOrgId(entity.getOrg().getFid());
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setExpenseType(entity.getExpenseType());
		vo.setTotalPayAmount(entity.getTotalPayAmount());
		vo.setChecked(entity.getChecked()==CostBill.STATUS_CHECKED);
		vo.setPayAmount(NumberUtil.stripTrailingZeros(entity.getPayAmount()));

		BigDecimal freeAmount = entity.getFreeAmount();//支出金额
		BigDecimal incomeAmount = entity.getIncomeAmount();//收入金额
		BigDecimal totalCheckAmount = entity.getTotalCheckAmount();//累计勾对金额
//		int compareTo = freeAmount.compareTo(incomeAmount);
		//未勾对金额设值

		if(freeAmount.compareTo(BigDecimal.ZERO)!=0){
			BigDecimal totalUncheckAmount = freeAmount.subtract(totalCheckAmount);;
			vo.setTotalUnCheckAmount(NumberUtil.stripTrailingZeros(totalUncheckAmount));
			
		}else if(incomeAmount.compareTo(BigDecimal.ZERO)!=0){
			BigDecimal totalUncheckAmount = incomeAmount.subtract(totalCheckAmount);;
			vo.setTotalUnCheckAmount(NumberUtil.stripTrailingZeros(totalUncheckAmount));
			
		}else{
			vo.setTotalUnCheckAmount(new BigDecimal(0));
		}

		Bank bank = entity.getBank();
		if(bank!=null){
			vo.setBankId(bank.getFid());
			vo.setBankName(bank.getName());
			vo.setBankAccount(bank.getAccount());
		}
		
		CustomerSupplierView csv = entity.getCsv();
		if(csv!=null){
			vo.setCsvId(csv.getFid());
			vo.setCsvName(csv.getName());
			vo.setCsvType(csv.getType());
			
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}

		Member payer = entity.getPayer();
		if (payer != null) {
			vo.setPayerId(payer.getFid());
			vo.setPayerName(payer.getUsername());
		}
		
		StockPeriod period = entity.getStockPeriod();
		if(period!=null){
			vo.setPeriodId(period.getFid());
			vo.setPeriodName(period.getPeriod());
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
		
		if(entity.getBillDate()!=null){
			vo.setBillDate(DateUtilTools.date2String(entity.getBillDate(), DATE_TIME));
		}
		
		if(entity.getOrg()!=null){
			vo.setOrgId(entity.getOrg().getFid());
		}
		
		Organization dept = entity.getDept();
		if(dept!=null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		
		AuxiliaryAttr fee = entity.getFee();
		if(fee!=null){
			vo.setFeeId(fee.getFid());
			vo.setFeeName(fee.getName());
		}
		
		return vo;
	}
	
	/**
	 * 删除费用单<br>
	 */
	public RequestResult delete(String fid){
		CostBill bill = costBillRepo.findOne(fid);
		if(bill.getRecordStatus()!=CostBill.STATUS_UNAUDITED){
			return buildFailRequestResult("非待审核状态不能删除");
		}
		if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult("会计期间已结账");
		}
		costBillRepo.delete(bill);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 审核费用单<br>
	 */
	public RequestResult passAudit(String fid){
		CostBill bill = costBillRepo.findOne(fid);
		if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		bill.setAuditor(SecurityUtil.getCurrentUser());
		bill.setAuditTime(Calendar.getInstance().getTime());
		bill.setRecordStatus(CostBill.STATUS_AUDITED);
		costBillRepo.save(bill);
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
	 * 作废费用单<br>
	 */
	public RequestResult cancle(String fid){
		try {
			TaskBillVo taskBillVo = taskBillService.queryByRelation(fid);
			if(taskBillVo!=null){
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				return buildFailRequestResult("该单据已关联"+planName+"计划中的"+taskName+"事件!");
			}
			if(checkService.countByCostBillId(fid) > 0){
				return buildFailRequestResult("(费用)单据已被勾对，不能作废");
			}
			if(paymentCheckService.countByBillId(fid) > 0){
				return buildFailRequestResult("(收/付款)单据已被勾对，不能作废");
			}
			
			CostBill bill = costBillRepo.findOne(fid);
			
			if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
				return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
			}
			bill.setCancelor(SecurityUtil.getCurrentUser());
			bill.setCancelTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(CostBill.STATUS_CANCELED);
			costBillRepo.save(bill);
			/**
			 * 资金计划取消
			 */
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
			if(capitalPlan!=null){
				RequestResult storagePassAudit = capitalPlanService.cancel(capitalPlan.getId(), CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(storagePassAudit.getReturnCode()==1){
					return storagePassAudit;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("作废费用单出错!");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断时间戳是否相等
	 */
	public boolean checkUpdateTime(CostBillVo vo, CostBill entity){
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
	public RequestResult save(CostBillVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		/**
		* 费用标识
		* 0--不处理
		* 1--增加往来单位应收/应付款项
		* 2--减少往来单位应收/应付款项
		*/
		Integer expenseType = vo.getExpenseType();

		BigDecimal freeAmount = vo.getFreeAmount();
		if(freeAmount==null||freeAmount.compareTo(BigDecimal.ZERO)==0){
			return buildFailRequestResult("请输入费用金额");
		}
		if(expenseType==1||expenseType==2){
			if(Strings.isNullOrEmpty(vo.getCsvId())) return buildFailRequestResult("请选择收支单位");
			if(freeAmount.compareTo(BigDecimal.ZERO)<0){
				return buildFailRequestResult("费用金额不可以输入负数");
			}
		}

		Date billDate = DateUtilTools.string2Date(vo.getBillDate(), DATE);
		boolean exist = this.existTodayVoucherCode(vo.getVoucherCode(), vo.getFid(), billDate);
		if(exist)return buildFailRequestResult("原始单号同一日内重复");

		CostBill entity = null;

		//会计期间
		String accId = SecurityUtil.getFiscalAccountId();
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, accId);
		if(stockPeriod == null){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_NOT_EXIST, "单据日期没有对应会计期间");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_UN_USED, "会计期间未启用");
		}


		
		if(StringUtils.isBlank(vo.getFid())){
			entity = new CostBill();
			entity = getEntity(vo, entity);
			entity.setStockPeriod(stockPeriod);
			entity.setBillDate(billDate);
			entity.setTotalCheckAmount(BigDecimal.ZERO);

			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setUpdateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDep(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());

			costBillRepo.save(entity);
		}else {
			entity = costBillRepo.findOne(vo.getFid());
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
			if(entity.getRecordStatus()!=CostBill.STATUS_UNAUDITED){
				return buildFailRequestResult("非待审核状态不能修改");
			}
			if(!checkUpdateTime(vo,entity)){
				return buildFailRequestResult("数据已被修改，请刷新重试");
			}
			
			entity = getEntity(vo, entity);
			entity.setStockPeriod(stockPeriod);
			entity.setBillDate(billDate);
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setUpdateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			
			costBillRepo.save(entity);
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
	public boolean existTodayVoucherCode(String voucherCode, String excludeId, Date billDate){
		if(Strings.isNullOrEmpty(voucherCode))return false;
		Long count = 0L;
		String accId = SecurityUtil.getFiscalAccountId();
		if(Strings.isNullOrEmpty(excludeId)) {
			count = costBillRepo.countTodayVoucherCode(accId, voucherCode, billDate);
		}else{
			count = costBillRepo.countTodayVoucherCode(accId, voucherCode, billDate, excludeId);
		}
		if(count>0)return true;
		else return false;
	}

	/**
	 * 核对费用单
     * @param billId 费用单ID
     * @return RequestResult Request对象
	 */
	public RequestResult check(String billId) {

	    CostBill entity = costBillRepo.findOne(billId);

        if (entity==null) {
            return buildFailRequestResult("费用单不存在或已被删除!");
        }

        // 已经结账的不能核对
		String stockPeriodId = entity.getStockPeriod() == null ? "": entity.getStockPeriod().getFid();
		FiscalPeriod period = fiscalPeriodService.findOne(stockPeriodId);

        if (period != null && period.getCheckoutStatus().equals(FiscalPeriod.CHECKED)) {
            return buildFailRequestResult("财务会计期间已结账，无法核对!");
        }

        entity.setChecked(CostBill.STATUS_CHECKED);
        entity.setChecker(SecurityUtil.getCurrentUser());
        entity.setCheckTime(Calendar.getInstance().getTime());
        costBillRepo.save(entity);

        return buildSuccessRequestResult("费用单核对成功");
	}

    /**
     * 核对费用单
     * @param billId 费用单ID
     * @return RequestResult Request对象
     */
    public RequestResult unCheck(String billId) {

        CostBill entity = costBillRepo.findOne(billId);

        if (entity==null) {
            return buildFailRequestResult("收费单不存在或已被删除!");
        }

        entity.setChecked(0);
        entity.setChecker(null);
        entity.setCheckTime(null);
        costBillRepo.save(entity);

        return buildSuccessRequestResult("费用单取消核对成功");
    }
	
	/**
	 * 获取新单号
	 * @return
	 */
	@Transactional(readOnly = false)
	public String getNewCode(){
		String orgId = SecurityUtil.getCurrentOrgId();
		return ruleService.createFullCode(orgId, WarehouseBuilderCodeHelper.fyd);
	}
	
	
	/**
	 * VO转换Entity
	 * @throws Exception 
	 */
	public CostBill getEntity(CostBillVo vo, CostBill entity) {
		
		entity.setCode(vo.getCode());
		entity.setVoucherCode(vo.getVoucherCode());
		entity.setExpenseType(vo.getExpenseType());
		if(vo.getPayAmount()==null){
			entity.setPayAmount(BigDecimal.ZERO);
		}else{
			entity.setPayAmount(vo.getPayAmount());
		}
		if(vo.getFreeAmount()==null){
			entity.setFreeAmount(BigDecimal.ZERO);
		}else{
			entity.setFreeAmount(vo.getFreeAmount());
		}
		if(vo.getIncomeAmount()==null){
			entity.setIncomeAmount(BigDecimal.ZERO);
		}else{
			entity.setIncomeAmount(vo.getIncomeAmount());
		}
		entity.setDescribe(vo.getDescribe());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		
		if(StringUtils.isNotBlank(vo.getBankId())){
			entity.setBank(bankService.get(vo.getBankId()));
		}
		if(StringUtils.isNotBlank(vo.getCsvId())){
			entity.setCsv(csService.get(vo.getCsvId()));
		}
		if(StringUtils.isNotBlank(vo.getMemberId())){
			entity.setMember(memberService.get(vo.getMemberId()));
		}
		if(StringUtils.isNotBlank(vo.getPeriodId())){
			entity.setStockPeriod(stockPeriodService.get(vo.getPeriodId()));
		}
		if(StringUtils.isNotBlank(vo.getFeeId())){
			entity.setFee(attrService.get(vo.getFeeId()));
		}
		if(StringUtils.isNotBlank(vo.getDeptId())){
			entity.setDept(orgService.get(vo.getDeptId()));
		}
		if(StringUtils.isNotBlank(vo.getPayerId())){
			entity.setPayer(memberService.get(vo.getPayerId()));
		}

		return entity;
	}
	/**
	 * 保存收付款单和勾对表记录
	 * @param vo		       表单传输对象 - 收付款单
	 * @param expenseType  费用标识 0--不处理 1--增加往来单位应收/应付款项 2--减少往来单位应收/应付款项
	 * @param costBillId  费用单id
	 * @return
	 */
	@Transactional
	public RequestResult savePaymentReceived (PaymentBillVo vo,Integer expenseType,String costBillId) {
		RequestResult save = null;
		try {
			//设置单据日期为当前时间
			vo.setBillDate(DateUtils.getCurrentDate());
			//计算费用单累计收付款金额
			CostBill costBill = costBillRepo.findOne(costBillId);
//			//计算费用单累计收付款金额
			BigDecimal totalPayAmount = costBill.getTotalPayAmount();
			//费用单金额
			BigDecimal befAmount = costBill.getAmount();
			//输入费用金额
			BigDecimal amount = Strings.isNullOrEmpty(vo.getAmount())?BigDecimal.ZERO:new BigDecimal(vo.getAmount());
			//收/付款金额不能为0
			if(amount.compareTo(BigDecimal.ZERO)==0){
				return buildFailRequestResult("金额不能为0");
			}
			
			if (expenseType == 0 ) {
				//1、费用标识为0，费用金额小于0（费用不处理，金额小于0，即收入金额） [FTOTAL_PAY_AMOUNT字段累加收款金额 *-1]
				//4、费用标识为0，费用金额大于0（费用不处理，金额大于0，即支出金额）
				if(amount.compareTo(BigDecimal.ZERO) < 0) return buildFailRequestResult("金额必须是正数");
				if(amount!=null&&befAmount.compareTo(BigDecimal.ZERO) < 0){
					totalPayAmount = totalPayAmount.add(amount.multiply(new BigDecimal(-1)));
	
				}
				if(amount!=null&&befAmount.compareTo(BigDecimal.ZERO) > 0){
					totalPayAmount = totalPayAmount.add(amount);
				}
			}
			// 2、费用标识为1，费用金额大于0（费用增加，往来单位选客户，金额大于0，即多收客户金额）
			// 5、费用标识为1，费用金额大于0（费用增加，往来单位选供应商，金额大于0，即多付供应商金额）
			if (expenseType == 1 ) {
				if( amount.compareTo(BigDecimal.ZERO) <= 0) return buildFailRequestResult("金额必须是正数");
				totalPayAmount = totalPayAmount.add(amount);
			}
			// 3、费用标识为2，费用金额大于0（费用减少，往来单位选客户，金额大于0，即少收客户金额）[因为输入是负数，累加的金额的时候要将输入的收款金额*-1；]
			// 6、费用标识为2，费用金额大于0（费用减少，往来单位选供应商，金额大于0，即少付供应商金额）
			if (expenseType == 2) {
				if(amount.compareTo(BigDecimal.ZERO) >= 0) return buildFailRequestResult("金额必须是负数");
				totalPayAmount = totalPayAmount.add(amount.multiply(new BigDecimal(-1)));
			}
			if(expenseType==2||expenseType == 0 &&befAmount.compareTo(BigDecimal.ZERO)<0){
				//费用单累计收付款金额不能大于费用单金额。
				if(totalPayAmount.compareTo(befAmount)<0){
					return buildFailRequestResult("收/付款金额不能超过费用单金额");
				}
			}else{
				//费用单累计收付款金额不能大于费用单金额。
				if(totalPayAmount.compareTo(befAmount)>0){
					return buildFailRequestResult("收/付款金额不能超过费用单金额");
				}
			}

			Integer recordStatus = costBill.getRecordStatus();
			if(recordStatus==0) return buildFailRequestResult("请审核该费用单！");
			if(recordStatus==2) return buildFailRequestResult("该费用单已作废！");
			if(vo!=null){
				CustomerSupplierView csv = costBill.getCsv();
				if(csv!=null){
					//1.客户；2供应商
					Integer type = costBill.getCsv().getType();
					if(type!=null){
						if(type==1){
							vo.setCustomerId(costBill.getCsv().getFid());
						}
						if(type==2){
							vo.setSupplierId(costBill.getCsv().getFid());
						}
					}
				}

				//收付款单状态设置为已审核
				//新增/编辑收付款单
				save = paymentBillWebService.save(vo);
				
				if(!save.isSuccess()) return save;
				
				//获取收付款单新增编号
				Map<String, Object> map = save.getDataExt();
				String fid = map.get("fid")==null?"":(String) map.get("fid");
				RequestResult passAudit = paymentBillWebService.passAudit(fid);
				
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
				pmcVo.setBillType(53);//单据类型
				pmcVo.setBillId(costBillId);		//费用单FID
				pmcVo.setAmount(amount.abs());//勾对金额不能为负数
				pmcVo.setBillTotalPayAmount(totalPayAmount);
				pmcVo.setFreeAmount(null);
				save=paymentCheckWebService.save(pmcVo);
				if(!save.isSuccess()) return save;
				//更新累计收付款金额
				costBill.setTotalPayAmount(totalPayAmount);
				costBillRepo.save(costBill);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("保存收付款单和勾对表记录有误！");
		}
		return save;
	}
	/**
	 * 根据费用单内容筛选单据类别
	 * @param expenseType	费用标识
	 *                         0--不处理
	 *                         1--增加往来单位应收/应付款项
	 *                         2--减少往来单位应收/应付款项
	 * @param csvType		类型 1：客户；2：供应商
	 * @return
	 */
	public RequestResult getBillType(Integer expenseType, Integer csvType) {
		List<Integer> types = null;
		try {
			
			if (expenseType != null && expenseType != 0 && csvType != null) {
				if(csvType == 2){
					types = Lists.newArrayList(11,12,15,55);
				}else if(csvType == 1){
					types = Lists.newArrayList(41,42,44,56);
				}
			}
			
			List<BillRuleVo> vos = billRuleService.findByBillTypes(types);
			return buildSuccessRequestResult(vos);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("单据类别查询出错！");
		}
	}


	/**
	 * 根据多个ID获取
	 * @param ids
	 * @return
	 */
	public List<CostBillVo> getByIds(List<String> ids) {
		return getVos(costBillRepo.findByIds(ids));
	}


	@Override
	public CrudRepository<CostBill, String> getRepository() {
		return costBillRepo;
	}
	/**
	 * 统计所有单据引用某个客户或者供应商的数量
	 * @param clientId 客户ID、供应商ID
	 * @return
	 */
	public long countByCustomerAndSupplier(String clientId){
		return costBillRepo.countByCustomerAndSupplier(clientId);
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
	public Page<CostBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay,
											   String checkEndDay, String checkBillCode, Integer checkBillType, List<String> excludeIds,
											   PageRequest pageRequest) {
		return costBillRepo.queryUnCheckBills(paybill, accId, checkStartDay,
				checkEndDay, checkBillCode, checkBillType, excludeIds, pageRequest);
	}
	public RequestResult costBillCheck(String id,short check){
		CostBill entity=costBillRepo.findOne(id);
		if(entity!=null){
			entity.setIsCheck(check);
			costBillRepo.save(entity);
        	return buildSuccessRequestResult();
    	}else{
			return buildFailRequestResult("单据不存在!");
		}
	}
}
