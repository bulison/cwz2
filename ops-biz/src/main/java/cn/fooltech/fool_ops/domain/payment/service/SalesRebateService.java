package cn.fooltech.fool_ops.domain.payment.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.entity.WarehouseReturn;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 销售返利单
 * 
 * @author lgk
 *
 */
@Service
public class SalesRebateService extends BaseService<PaymentBill, PaymentBillVo, String> {
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillRepository paymentBillRepo;
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentBillService;
	
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
	 * 仓库服务类
	 */
	@Resource(name="ops.WarehouseBillService")
	private WarehouseBillService warehouseBillService;

	@Autowired
	private WarehouseReturnService warehouseReturnService;
	
	@Autowired
	private CapitalPlanService capitalPlanService;

	@Autowired
	private CapitalPlanDetailService detailService;
	/**
	 * 计划事件关联单据 服务类
	 */
	@Autowired
	private TaskBillService taskBillService;

	@Override
	public PaymentBillVo getVo(PaymentBill entity) {
		if (entity == null)
			return null;
		PaymentBillVo vo = new PaymentBillVo();
		vo.setCode(entity.getCode());
		vo.setVouchercode(entity.getVouchercode());
		vo.setBillType(entity.getBillType());
		BigDecimal amount = NumberUtil.scale(entity.getAmount(), 2);
		BigDecimal totalCheckAmount = NumberUtil.scale(entity.getTotalCheckAmount(), 2);
		vo.setAmount(NumberUtil.stripTrailingZeros(amount).toPlainString());
		vo.setTotalCheckAmount(NumberUtil.stripTrailingZeros(totalCheckAmount).toPlainString());
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime(),DATE_TIME));
		vo.setCancelTime(DateUtilTools.date2String(entity.getCancelTime(),DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(),
				DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setOrgId(entity.getOrg().getFid());
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setRates(entity.getRates());
		vo.setStartDate(DateUtilTools.date2String(entity.getStartDate(),
				DateUtilTools.DATE_PATTERN_YYYY_MM_DD));
		vo.setEndDate(DateUtilTools.date2String(entity.getEndDate(),
				DateUtilTools.DATE_PATTERN_YYYY_MM_DD));
		vo.setToPublic(entity.getToPublic());
		vo.setSales(NumberUtil.stripTrailingZeros(entity.getSales()));
		vo.setDescribe(entity.getDescribe());
		vo.setPayeeName(entity.getPayeeName());
		/*Member member = entity.getPayee();
		if (member != null) {
			vo.setPayeeId(entity.getPayee().getFid());
			vo.setPayeeName(entity.getPayee().getUsername());
		}*/
		Bank bank = entity.getBank();
		if (bank != null) {
			vo.setBankId(bank.getFid());
			vo.setBankName(bank.getName());
			vo.setBankAccount(bank.getAccount());
		}

		Customer customer = entity.getCustomer();
		if (customer != null) {
			vo.setCustomerId(customer.getFid());
			vo.setCustomerName(customer.getName());
			vo.setCsvId(customer.getFid());
			vo.setCsvName(customer.getName());
			vo.setCustomerPhone(customer.getPhone());
			vo.setCustomerAddress(customer.getAddress());
		}

		Supplier supplier = entity.getSupplier();
		if (supplier != null) {
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
			vo.setCsvId(supplier.getFid());
			vo.setCsvName(supplier.getName());
			vo.setSupplierPhone(supplier.getPhone());
			vo.setSupplierAddress(supplier.getAddress());
		}

		Member member = entity.getMember();
		if (member != null) {
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}

		StockPeriod period = entity.getStockPeriod();
		if (period != null) {
			vo.setStockPeriodId(period.getFid());
			vo.setStockPeriodName(period.getPeriod());
		}

		User auditor = entity.getAuditor();
		if (auditor != null) {
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}

		User cancelor = entity.getCancelor();
		if (cancelor != null) {
			vo.setCancelorId(cancelor.getFid());
			vo.setCancelorName(cancelor.getUserName());
		}

		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}

		if (entity.getBillDate() != null) {
			vo.setBillDate(DateUtilTools.date2String(entity.getBillDate(),
					DATE_TIME));
		}
		if (entity.getOrg() != null) {
			vo.setOrgId(entity.getOrg().getFid());
		}

		return vo;
	}

	/**
	 * 作废销售返利单<br>
	 */
	@Transactional
	public RequestResult cancle(String fid) {
		try {
			TaskBillVo taskBillVo = taskBillService.queryByRelation(fid);
			if(taskBillVo!=null){
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				return buildFailRequestResult("该单据已关联"+planName+"计划中的"+taskName+"事件!");
			}
			PaymentBill bill = paymentBillService.get(fid);
			if (bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED) {
				return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
			}
			Long count = checkService.countByBillId(fid);
			if(count!=null&&count>0)return buildFailRequestResult("单据已被勾对，不能作废");
			bill.setCancelor(SecurityUtil.getCurrentUser());
			bill.setCancelTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(PaymentBill.STATUS_CANCELED);
			paymentBillService.save(bill);
			warehouseReturnService.deleteByBillId(fid);
			/**资金计划取消**/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
			if(capitalPlan!=null){
				RequestResult storagePassAudit = capitalPlanService.cancel(capitalPlan.getId(), CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(storagePassAudit.getReturnCode()==1){
					return storagePassAudit;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("作废单据出错!");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 新增/编辑销售返利单
	 * 
	 * @param vo
	 */
	public RequestResult saveSales(PaymentBillVo vo) {
		if(Strings.isNullOrEmpty(vo.getCode()))return buildFailRequestResult("单号必填");
		if(vo.getToPublic()==null)return buildFailRequestResult("对公标识必填");
		
		if(Strings.isNullOrEmpty(vo.getAmount()))return buildFailRequestResult("返利金额必填");
		if(Strings.isNullOrEmpty(vo.getStartDate()))return buildFailRequestResult("开始日期必填");
		if(Strings.isNullOrEmpty(vo.getEndDate()))return buildFailRequestResult("结束日期必填");
		Integer billType = vo.getBillType();
		//类型-采购返利单55
		if(billType==55){
			if(Strings.isNullOrEmpty(vo.getBillDate()))return buildFailRequestResult("收款日期必填");
			if(Strings.isNullOrEmpty(vo.getSupplierName()))return buildFailRequestResult("供应商必填");
		}
		//类型-销售返利单56
		if(billType==56){
			if(Strings.isNullOrEmpty(vo.getCustomerName()))return buildFailRequestResult("销售商必填");
			if(Strings.isNullOrEmpty(vo.getMemberName()))return buildFailRequestResult("付款人必填");
			if(Strings.isNullOrEmpty(vo.getBillDate()))return buildFailRequestResult("付款日期必填");
		}
		BigDecimal amount = new BigDecimal(vo.getAmount()); 
		if(!Strings.isNullOrEmpty(vo.getVouchercode())&&vo.getVouchercode().length()>50){
			return buildFailRequestResult("原始单号超过50个字符");
		}
		if(!Strings.isNullOrEmpty(vo.getDescribe())&&vo.getDescribe().length()>200){
			return buildFailRequestResult("备注超过200个字符");
		}
		if(!Strings.isNullOrEmpty(vo.getPayeeName())&&vo.getPayeeName().length()>50){
			return buildFailRequestResult("受款人超过50个字符");
		}
		if(amount.compareTo(new BigDecimal(Integer.MAX_VALUE))>0){
			return buildFailRequestResult("返利金额不能超过"+Integer.MAX_VALUE);
		}
		
		PaymentBill entity = null;

		Date billDate = DateUtilTools.string2Date(vo.getBillDate(), DATE);
		Date startDate = DateUtilTools.string2Date(vo.getStartDate(), DATE);
		Date endDate = DateUtilTools.string2Date(vo.getEndDate(), DATE);
		if(startDate.compareTo(endDate)>0) return buildFailRequestResult("开始日期大于结束日期");
		
		User user = SecurityUtil.getCurrentUser();
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		String accountId = account.getFid();
		// 会计期间
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, accountId);
		if (stockPeriod == null) {
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_NOT_EXIST, "单据日期没有对应会计期间");
		}
		if (stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED) {
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		if (stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED) {
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_UN_USED, "会计期间未启用");
		}
		
		Date now = DateUtilTools.now();
		if (StringUtils.isBlank(vo.getFid())) {
			entity = new PaymentBill();
			entity = getEntity(vo, entity);
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(user);
			entity.setOrg(org);
			entity.setDept(dept);
			entity.setFiscalAccount(account);
		} else {
			entity = paymentBillService.get(vo.getFid());
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
			if (entity.getRecordStatus() != PaymentBill.STATUS_UNAUDITED) {
				return buildFailRequestResult("非待审核状态不能修改");
			}
			if (!checkUpdateTime(vo, entity)) {
				return buildFailRequestResult("数据已被修改，请刷新重试");
			}
			entity = getEntity(vo, entity);
		}
		
		entity.setSales(vo.getSales());
		entity.setRates(vo.getRates());
		entity.setStockPeriod(stockPeriod);
		entity.setBillDate(billDate);
		entity.setTotalCheckAmount(BigDecimal.ZERO);
		entity.setBillType(vo.getBillType());
		entity.setUpdateTime(now);
		entity.setPayeeName(vo.getPayeeName());
		entity.setToPublic(vo.getToPublic());
		paymentBillService.save(entity);
		
		Map<String, String> map = Maps.newHashMap();
		map.put("fid", entity.getFid());
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
		
		return buildSuccessRequestResult(map);
	}

	/**
	 * 获取本期账单
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getPurchSummary(String startTime, String endTime,
			String supplierId) {
		String accId = SecurityUtil.getFiscalAccountId();
		BigDecimal summary = warehouseBillService.getSupplierSummary(DateUtilTools
				.string2Date(startTime, DateUtilTools.DATE_PATTERN_YYYY_MM_DD),
				DateUtilTools.string2Date(endTime,
						DateUtilTools.DATE_PATTERN_YYYY_MM_DD), accId, supplierId);
		return NumberUtil.stripTrailingZeros(summary).toString();
	}

	/**
	 * 获取本期账单
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getSaleSummary(String startTime, String endTime,
			String customerId) {
		String accId = SecurityUtil.getFiscalAccountId();
		BigDecimal summary = warehouseBillService.getCustomerSummary(DateUtilTools
				.string2Date(startTime, DateUtilTools.DATE_PATTERN_YYYY_MM_DD),
				DateUtilTools.string2Date(endTime,
						DateUtilTools.DATE_PATTERN_YYYY_MM_DD), accId,customerId);

		return NumberUtil.stripTrailingZeros(summary).toString();
	}

	/**
	 * 判断时间戳是否相等
	 */
	public boolean checkUpdateTime(PaymentBillVo vo, PaymentBill entity) {
		String updateTime = DateUtilTools.date2String(entity.getUpdateTime(),
				DATE_TIME);
		if (updateTime.equals(vo.getUpdateTime())) {
			return true;
		}
		return false;
	}

	/**
	 * VO转换Entity
	 * 
	 * @throws Exception
	 */
	public PaymentBill getEntity(PaymentBillVo vo, PaymentBill entity) {


		entity.setCode(vo.getCode());
		entity.setVouchercode(vo.getVouchercode());
		entity.setBillType(vo.getBillType());
		BigDecimal amount = new BigDecimal(vo.getAmount());

		String totalCheckAmountStr = vo.getTotalCheckAmount();
		BigDecimal totalCheckAmount = BigDecimal.ZERO;
		if(totalCheckAmountStr!=null){
			totalCheckAmount = new BigDecimal(totalCheckAmountStr);
		}

		entity.setAmount(NumberUtil.scale(amount, 2));
		entity.setTotalCheckAmount(NumberUtil.scale(totalCheckAmount, 2));
		entity.setDescribe(vo.getDescribe());
		entity.setStartDate(DateUtilTools.string2Date(vo.getStartDate(),
				DateUtilTools.DATE_PATTERN_YYYY_MM_DD));
		entity.setEndDate(DateUtilTools.string2Date(vo.getEndDate(),
				DateUtilTools.DATE_PATTERN_YYYY_MM_DD));
		entity.setToPublic(vo.getToPublic());
		if (StringUtils.isNotBlank(vo.getBankId())) {
			entity.setBank(bankService.get(vo.getBankId()));
		}

		if (StringUtils.isNotBlank(vo.getCustomerId())) {
			entity.setCustomer(customerService.get(vo.getCustomerId()));
		}else{
			entity.setCustomer(null);
		}

		if (StringUtils.isNotBlank(vo.getSupplierId())) {
			entity.setSupplier(supplierService.get(vo.getSupplierId()));
		}else{
			entity.setSupplier(null);
		}

		if (StringUtils.isNotBlank(vo.getMemberId())) {
			entity.setMember(memberService.get(vo.getMemberId()));
		}else{
			entity.setMember(null);
		}

		if (StringUtils.isNotBlank(vo.getStockPeriodId())) {
			entity.setStockPeriod(stockPeriodService.get(vo.getStockPeriodId()));
		}else{
			entity.setStockPeriod(null);
		}

		return entity;
	}

	/**
	 * 查询收付款单列表信息，按照收付款单主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 */
	public Page<PaymentBillVo> query(PaymentBillVo paymentBillVo,
			PageParamater pageParamater) {
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		Page<PaymentBill> page = paymentBillRepo.findPageBy(paymentBillVo, accId, pageRequest);
		
		return getPageVos(page, pageRequest);
	}

	/**
	 * 审核销售返利单<br>
	 */
	@Transactional
	public RequestResult passSateAudit(String fid) {
		PaymentBill bill = paymentBillService.get(fid);
		if (bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED) {
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		
		BigDecimal totalAmount = bill.getAmount();
		BigDecimal summary = warehouseBillService.getCustomerSummary(
				bill.getStartDate(), bill.getEndDate(), bill.getFiscalAccount().getFid(), bill.getCustomer().getFid());

		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<WarehouseBill> warehouseBills = warehouseBillService
				.getWarehouseBills(bill.getStartDate(), bill.getEndDate(),
						orgId, accId,
						WarehouseBuilderCodeHelper.xsth,
						WarehouseBuilderCodeHelper.xsch);
		
		BigDecimal scale = NumberUtil.divide(totalAmount, summary, 20);
		
		for (WarehouseBill warehouseBill : warehouseBills) {
			List<WarehouseBillDetail> billDetails = warehouseBill.getDetails();
			for (WarehouseBillDetail warehouseBillDetail : billDetails) {
				WarehouseReturn warehouseReturn = new WarehouseReturn();
				
				BigDecimal amount = NumberUtil.multiply(scale, warehouseBillDetail.getType());

				warehouseReturn.setAmount(amount);
				warehouseReturn.setFiscalAccount(bill.getFiscalAccount());
				warehouseReturn.setOrg(bill.getOrg());
				warehouseReturn.setWarehouseBill(warehouseBill);
				warehouseReturn.setWarehouseBillDetail(warehouseBillDetail);
				warehouseReturn.setPaymentBill(bill);
				warehouseReturn.setRates(bill.getRates());
				warehouseReturnService.save(warehouseReturn);
			}
		}
		
		bill.setAuditor(SecurityUtil.getCurrentUser());
		bill.setAuditTime(Calendar.getInstance().getTime());
		bill.setRecordStatus(PaymentBill.STATUS_AUDITED);
		bill.setSales(summary);
		bill.setRates(NumberUtil.multiply(scale, new BigDecimal(100)));
		
		paymentBillService.save(bill);
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
	 * 采购返利通过审核
	 * 
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult passPurchAudit(String fid) {
		PaymentBill bill = paymentBillService.get(fid);
		if (bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED) {
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
		}
		
		BigDecimal totalAmount = bill.getAmount();
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		BigDecimal summary = warehouseBillService.getSupplierSummary(
				bill.getStartDate(), bill.getEndDate(), bill.getFiscalAccount().getFid(),bill.getSupplier().getFid());
		List<WarehouseBill> warehouseBills = warehouseBillService
				.getWarehouseBills(bill.getStartDate(), bill.getEndDate(),
						orgId, accId,
						WarehouseBuilderCodeHelper.cgth,
						WarehouseBuilderCodeHelper.cgrk);
		
		BigDecimal scale = NumberUtil.divide(totalAmount, summary, 20);
		
		for (WarehouseBill warehouseBill : warehouseBills) {
			List<WarehouseBillDetail> billDetails = warehouseBill.getDetails();
			for (WarehouseBillDetail warehouseBillDetail : billDetails) {
				WarehouseReturn warehouseReturn = new WarehouseReturn();
				
				
				BigDecimal amount = NumberUtil.multiply(scale, warehouseBillDetail.getType());
				
				warehouseReturn.setAmount(amount);
				warehouseReturn.setFiscalAccount(bill.getFiscalAccount());
				warehouseReturn.setOrg(bill.getOrg());
				warehouseReturn.setWarehouseBill(warehouseBill);
				warehouseReturn.setWarehouseBillDetail(warehouseBillDetail);
				warehouseReturn.setPaymentBill(bill);
				warehouseReturn.setRates(bill.getRates());
				warehouseReturnService.save(warehouseReturn);
			}
		}
		
		bill.setAuditor(SecurityUtil.getCurrentUser());
		bill.setAuditTime(Calendar.getInstance().getTime());
		bill.setRecordStatus(PaymentBill.STATUS_AUDITED);
		bill.setSales(summary);
		bill.setRates(NumberUtil.multiply(scale, new BigDecimal(100)));
		
		paymentBillService.save(bill);
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
	 * 删除收付款单<br>
	 */
	public RequestResult delete(String fid) {
		PaymentBill bill = paymentBillService.get(fid);
		if (bill.getRecordStatus() != PaymentBill.STATUS_UNAUDITED) {
			return buildFailRequestResult("非待审核状态不能删除");
		}
		if (bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED) {
			return buildFailRequestResult("会计期间已结账");
		}
		paymentBillService.delete(bill);
		return buildSuccessRequestResult();
	}

	@Override
	public CrudRepository<PaymentBill, String> getRepository() {
		return paymentBillRepo;
	}
	
	/**
	 * 判断在此时间段内是否还存在同一个供应商相同对公标识的采购返利单
	 * @param supplierId 供应商
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	public RequestResult checkPurchaseRebateExist(String fid, String supplierId, String startDate,
			String endDate, Integer toPublic) {
		
		String accId = SecurityUtil.getFiscalAccountId();
		if(paymentBillRepo.checkPurchaseRebateExist(accId, fid, supplierId, startDate, endDate, toPublic)){
			return buildFailRequestResult("此时间段内存在同一个供应商相同对公标识的采购返利单");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断在此时间段内是否还存在同一个客户相同对公标识的销售返利单
	 * @param customerId 客户
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	public RequestResult checkSalesRebateExist(String fid, String customerId, String startDate,
			String endDate, Integer toPublic) {
		
		String accId = SecurityUtil.getFiscalAccountId();
		if(paymentBillRepo.checkSalesRebateExist(accId, fid, customerId, startDate, endDate, toPublic)){
			return buildFailRequestResult("此时间段内存在同一个客户相同对公标识的销售返利单");
		}
		return buildSuccessRequestResult();
	}
}
