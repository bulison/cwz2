package cn.fooltech.fool_ops.domain.payment.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentCheck;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentCheckRepository;
import cn.fooltech.fool_ops.domain.payment.vo.CheckBillVo;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.service.AuditService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>收付款对单网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-24 15:32:59
 */
@Service("ops.PaymentCheckService")
public class PaymentCheckService extends BaseService<PaymentCheck, PaymentCheckVo, String> {
	
	/**
	 * 单据服务类
	 */
	@Resource(name="ops.WarehouseBillService")
	private WarehouseBillService billService;
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentBillService;
	
	/**
	 * 费用单据服务类
	 */
	@Autowired
	private CostBillService costBillService;
	
	/**
	 * 收付款单勾对服务类
	 */
	@Autowired
	private PaymentCheckRepository checkRepo;

	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService periodService;
	
	/**
	 * 仓库单据审核工具类
	 */
	@Autowired
	protected AuditService auditService;
	
	@Autowired
	protected CapitalPlanService capitalPlanService;
	
	@Autowired
	protected CapitalPlanDetailService capitalPlanDetailService;
	
	
	/**
	 * 查找已勾兑的单据
	 */
	public Page<PaymentCheckVo> query(PaymentCheckVo vo, PageParamater paramater){
		
		String accId = SecurityUtil.getFiscalAccountId();
		String paybillId = vo.getPaymentBillId();
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		Page<PaymentCheck> checkPage = checkRepo.findPageByPaybillId(accId, paybillId, pageRequest);
		for(PaymentCheck check:checkPage){
			if(check.getBillId().equals(vo.getPaymentBillId())){
				check.setSelf(true);
			}
		}
		return getPageVos(checkPage, pageRequest);
	}
	
	/**
	 * 查找已勾兑的单据
	 */
	public Page<PaymentBillVo> getByWarehouseId(String wbillId, Integer billType, PageParamater paramater){
		
		String accId = SecurityUtil.getFiscalAccountId();
		
		//分页
		PageRequest pageRequest = getPageRequest(paramater);
		Page<PaymentBill> page = checkRepo.queryByWarehgouseBillId(accId, wbillId, billType, pageRequest);
		return paymentBillService.getPageVos(page, pageRequest);
	}
	
	
	/**
	 * 单个收付款单勾对实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public PaymentCheckVo getVo(PaymentCheck entity){
		if(entity == null)
			return null;
		PaymentCheckVo vo = new PaymentCheckVo();
		
		vo.setAmount(NumberUtil.scale(entity.getAmount(), 2));
		vo.setPaymentBillCode(entity.getPaymentBill().getCode());
		vo.setPaymentBillId(entity.getPaymentBill().getFid());
		vo.setFid(entity.getFid());
		vo.setDescribe(entity.getDescribe());
		vo.setFreeAmount(NumberUtil.scale(entity.getFreeAmount(), 2));
//		vo.setUpdateTime(DateUtils.format(entity.getUpdateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DATE_TIME));
//		vo.setCheckDate(DateUtils.format(entity.getCheckDate(), DATE_TIME));
		vo.setCheckDate(DateUtils.getStringByFormat(entity.getCheckDate(), DATE_TIME));
		
		//客户
		Customer customer = null;
		Supplier supplier = null;

		BillType type = checkBillType(entity.getBillType());
		String billId = entity.getBillId();
		if(type==BillType.Warehouse){
			WarehouseBill bill = billService.get(billId);
			
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(bill.getFreeAmount()==null?BigDecimal.ZERO:bill.getFreeAmount());
			
			BigDecimal totalAmount = bill.getTotalAmount()==null?BigDecimal.ZERO:bill.getTotalAmount();
			BigDecimal expenseAmount = bill.getExpenseAmount()==null?BigDecimal.ZERO:bill.getExpenseAmount();

			BigDecimal billTotal = NumberUtil.subtract(totalAmount, bill.getDeductionAmount());
			billTotal = NumberUtil.add(billTotal, expenseAmount);
			vo.setBillTotalAmount(billTotal);
			
			BigDecimal billTotalPayAmount = NumberUtil.add(bill.getTotalPayAmount(), bill.getFreeAmount());
			vo.setBillTotalPayAmount(billTotalPayAmount);
			
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtilTools.date2String(bill.getBillDate(), DATE_TIME));
			}
			vo.setBillType(bill.getBillType());
			vo.setDetal(auditService.getBillTag(bill).toString());
			customer = bill.getCustomer();
			supplier = bill.getSupplier();
		}else if(type==BillType.Pay){
			PaymentBill bill = null;
			if(entity.isSelf()){
				bill = entity.getPaymentBill();
				PaymentBill another = paymentBillService.get(billId);
				vo.setPaymentBillCode(another.getCode());
				vo.setPaymentBillId(another.getFid());
			}else{
				bill = paymentBillService.get(billId);
			}
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(null);
			vo.setBillTotalAmount(bill.getAmount());
			vo.setBillTotalPayAmount(bill.getTotalCheckAmount());
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtilTools.date2String(bill.getBillDate(), DATE_TIME));
			}
			vo.setBillType(bill.getBillType());
			if(bill.getBillType()==WarehouseBuilderCodeHelper.xsfld||bill.getBillType()==WarehouseBuilderCodeHelper.cgfld){
				vo.setDetal("-1");
			} else if(bill.getAmount().compareTo(BigDecimal.ZERO)<0){
				vo.setDetal("-1");
			}
			customer = bill.getCustomer();
			supplier = bill.getSupplier();
		}else if(type==BillType.Cost){
			CostBill bill = costBillService.get(billId);
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(null);
			vo.setBillTotalAmount(bill.getAmount());
			vo.setBillTotalPayAmount(bill.getTotalPayAmount());
			vo.setExpenseType(bill.getExpenseType());
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtilTools.date2String(bill.getBillDate(), DATE_TIME));
			}
			vo.setBillType(WarehouseBuilderCodeHelper.fyd);
			if(bill.getExpenseType()==CostBill.EXPENSE_TYPE_SUBTRACT){
				vo.setDetal("-1");
			}
			
			CustomerSupplierView csv = bill.getCsv();
			if(csv!=null){
				vo.setCsvId(csv.getFid());
				vo.setCsvName(csv.getName());
				vo.setCsvType(csv.getType());
			}
		}
		
		//客户
		if(customer != null){
			vo.setCsvId(customer.getFid());
			vo.setCsvName(customer.getName());
			vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
		}
		//供应商
		if(supplier != null){
			vo.setCsvId(supplier.getFid());
			vo.setCsvName(supplier.getName());
			vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
		}
		
		return vo;
	}
	
	
	/**
	 * 查询付款/收款单能对单的单据
	 * @param billId 付款单ID（必填）
	 * @param checkStartDay
	 * @param checkEndDay
	 * @param checkBillCode
	 * @param checkBillType（必填）
	 * @return
	 */
	public PageJson queryCheckBill(String billId, String checkStartDay, String checkEndDay, 
			String checkBillCode, Integer checkBillType, PageParamater pagep){
		
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(pagep, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		
		BillType type = checkBillType(checkBillType);
		PaymentBill paybill = paymentBillService.get(billId);
		List<String> excludeIds = checkRepo.findCheckedBillIdsByPaymentId(billId);

		if(type==BillType.Warehouse){//仓储单据的表
			
			
			Page<WarehouseBill> bills = billService.queryUnCheckBills(paybill, accId, checkStartDay, checkEndDay, 
					checkBillCode, checkBillType, excludeIds, pageRequest);
			return getPageJson(getWarehouseCheckBillVo(bills), bills.getTotalElements());
			
		}else if(type==BillType.Pay){//收付款单的表

			Page<PaymentBill> bills = paymentBillService.queryUnCheckBills(paybill, accId, checkStartDay, checkEndDay, 
					checkBillCode, checkBillType, excludeIds, pageRequest);
			return getPageJson(getPaymentCheckBillVo(bills), bills.getTotalElements());
			
		}else if(type==BillType.Cost){//费用单的表

			Page<CostBill> bills = costBillService.queryUnCheckBills(paybill, accId, checkStartDay, checkEndDay,
					checkBillCode, checkBillType, excludeIds, pageRequest);
			return getPageJson(getCostCheckBillVo(bills), bills.getTotalElements());

		}else{
			throw new RuntimeException("unsupport billType!");
		}
	}
	
	/**
	 * 获取未勾兑单据VO
	 * @param bills
	 * @return
	 */
	public List<CheckBillVo> getCostCheckBillVo(Page<CostBill> bills) {
		List<CheckBillVo> list = Lists.newArrayList();
		for(CostBill cost:bills.getContent()){
			CheckBillVo vo = new CheckBillVo();
			vo.setBillId(cost.getFid());
			vo.setBillCode(cost.getCode());
			vo.setBillDate(DateUtilTools.time2String(cost.getBillDate()));
			vo.setBillType(WarehouseBuilderCodeHelper.fyd);
			vo.setBillAmount(NumberUtil.stripTrailingZeros(cost.getAmount()).toPlainString());
			vo.setExpenseType(cost.getExpenseType());
			
			CustomerSupplierView csv = cost.getCsv();
			if(csv!=null){
				vo.setCsvId(csv.getFid());
				vo.setCsvName(csv.getName());
				vo.setCsvType(csv.getType());
			}
			//vo.setTotalFreeAmount(null);
			vo.setPayedAmount(NumberUtil.stripTrailingZeros(cost.getTotalPayAmount()).toPlainString());
			vo.setUnpayAmount(NumberUtil.stripTrailingZeros(NumberUtil.subtract(cost.getAmount(), 
					cost.getTotalPayAmount())).toPlainString());
			if(cost.getExpenseType()==CostBill.EXPENSE_TYPE_SUBTRACT){
				vo.setDetal("-1");
			}
			list.add(vo);
		}
		return list;
	}

	/**
	 * 获取未勾兑单据VO
	 * @param bills
	 * @return
	 */
	public List<CheckBillVo> getPaymentCheckBillVo(Page<PaymentBill> bills) {
		List<CheckBillVo> list = Lists.newArrayList();
		for(PaymentBill payment:bills.getContent()){
			CheckBillVo vo = new CheckBillVo();
			vo.setBillId(payment.getFid());
			vo.setBillCode(payment.getCode());
			
			vo.setBillDate(DateUtilTools.time2String(payment.getBillDate()));
			vo.setBillType(payment.getBillType());
			vo.setBillAmount(NumberUtil.stripTrailingZeros(payment.getAmount()).toPlainString());
			
			Supplier supplier = payment.getSupplier();
			if(supplier!=null){
				vo.setCsvId(supplier.getFid());
				vo.setCsvName(supplier.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
			}else{
				Customer customer = payment.getCustomer();
				if(customer!=null){
					vo.setCsvId(customer.getFid());
					vo.setCsvName(customer.getName());
					vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
				}
			}
			
			//vo.setTotalFreeAmount(null);
			vo.setPayedAmount(NumberUtil.stripTrailingZeros(payment.getTotalCheckAmount()).toPlainString());
			vo.setUnpayAmount(NumberUtil.stripTrailingZeros(NumberUtil.subtract(payment.getAmount(), 
					payment.getTotalCheckAmount())).toPlainString());
			if(payment.getBillType()==WarehouseBuilderCodeHelper.xsfld||payment.getBillType()==WarehouseBuilderCodeHelper.cgfld){
				vo.setDetal("-1");
			} else if(payment.getAmount().compareTo(BigDecimal.ZERO)<0){
				vo.setDetal("-1");
			}
			list.add(vo);
		}
		return list;
	}

	/**
	 * 获取未勾兑单据VO
	 * @param bills
	 * @return
	 */
	public List<CheckBillVo> getWarehouseCheckBillVo(Page<WarehouseBill> bills) {
		List<CheckBillVo> list = Lists.newArrayList();
		for(WarehouseBill bill:bills.getContent()){
			CheckBillVo vo = new CheckBillVo();
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			
			vo.setBillDate(DateUtilTools.time2String(bill.getBillDate()));
			vo.setBillType(bill.getBillType());

			
			Supplier supplier = bill.getSupplier();
			if(supplier!=null){
				vo.setCsvId(supplier.getFid());
				vo.setCsvName(supplier.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
			}else{
				Customer customer = bill.getCustomer();
				if(customer!=null){
					vo.setCsvId(customer.getFid());
					vo.setCsvName(customer.getName());
					vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
				}
			}
			
			vo.setTotalFreeAmount(NumberUtil.stripTrailingZeros(bill.getFreeAmount()).toPlainString());
			vo.setDetal(auditService.getBillTag(bill).toString());
			vo.setExpenseAmount(NumberUtil.bigDecimalToStr(bill.getExpenseAmount()));

			if(bill.getBillType()==WarehouseBuilderCodeHelper.shd){
				vo.setBillAmount(
						NumberUtil.stripTrailingZeros(
								NumberUtil.subtract(bill.getTotalAmount(),bill.getDeductionAmount())
						).toPlainString());
			}else{
				vo.setBillAmount(NumberUtil.stripTrailingZeros(bill.getTotalAmount()).toPlainString());
			}

			vo.setPayedAmount(NumberUtil.stripTrailingZeros(bill.getTotalPayAmount()).toPlainString());
			vo.setUnpayAmount(NumberUtil.stripTrailingZeros(
					NumberUtil.subtract(NumberUtil.subtract(bill.getTotalAmount(), bill.getDeductionAmount()),
							bill.getTotalPayAmount())).toPlainString());

			list.add(vo);
		}
		return list;
	}

	/**
	 * 判断是那种类型单据表
	 * @param checkBillType
	 * @return 0:仓储单据表 1：收付款单表 2：费用单表
	 */
	public BillType checkBillType(int checkBillType){
		if(checkBillType==WarehouseBuilderCodeHelper.skd || checkBillType==WarehouseBuilderCodeHelper.fkd ||
				checkBillType==WarehouseBuilderCodeHelper.cgfld || checkBillType==WarehouseBuilderCodeHelper.xsfld){
			return BillType.Pay;
		}
		if(checkBillType==WarehouseBuilderCodeHelper.fyd){
			return BillType.Cost;
		}
		return BillType.Warehouse;
	}
	
	public enum BillType{
		Warehouse,
		Pay,
		Cost
	}
	
	/**
	 * 删除收付款单勾对<br>
	 */
	@Transactional
	public RequestResult delete(String fid){
		
		PaymentCheck entity = checkRepo.findOne(fid);
		Date checkDate = entity.getCheckDate();
		String orgId = SecurityUtil.getCurrentOrgId();
		String accountId = SecurityUtil.getFiscalAccountId();
		StockPeriod stockPeriod = periodService.getPeriod(checkDate, accountId);
		
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult("会计期间已结账");
		}
		
		RequestResult result = null;
		BillType type = checkBillType(entity.getBillType());
		if(type==BillType.Warehouse){
			result = deleteWarehouseCheck(entity);
		}else if(type==BillType.Pay){
			result = deletePayCheck(entity);
		}else if(type==BillType.Cost){
			result = deleteCostCheck(entity);
		}else{
			result = buildFailRequestResult("不支持的单据类型");
		}
		if(result.isSuccess()){
			checkRepo.delete(fid);
		}
		
		return result;
	}
	
	/**
	 * 删除与付款单/收款单的勾对
	 * @param entity
	 * @return
	 */
	@Transactional
	private RequestResult deleteCostCheck(PaymentCheck entity) {
		String billId = entity.getBillId(); 
		BigDecimal amount = entity.getAmount();
		BigDecimal freeAmount = entity.getFreeAmount();
		CostBill checkbill = costBillService.get(entity.getBillId());
		PaymentBill paybill = entity.getPaymentBill();
		BigDecimal checkAmount = entity.getAmount();
		
		String payBillName = WarehouseBuilderCodeHelper.getBillName(paybill.getBillType());
		
		if(checkbill.getExpenseType()==CostBill.EXPENSE_TYPE_ADD){
			paybill = processPaybill(checkAmount, paybill, AddType.Negative);
			checkbill = processCostBill(checkAmount, checkbill, AddType.Negative);
		}else if(checkbill.getExpenseType()==CostBill.EXPENSE_TYPE_SUBTRACT){
			if(checkDeleteWillOverflow(paybill, checkAmount)){
				return buildFailRequestResult("勾对金额较大，删除后会导致"+payBillName+"累计勾对金额大于"+payBillName+"金额，不能删除");
			}
			paybill = processPaybill(checkAmount, paybill, AddType.Positive);
			checkbill = processCostBill(checkAmount, checkbill, AddType.Negative);
		}else{
			if(checkbill.getAmount().compareTo(BigDecimal.ZERO)>0){
				paybill = processPaybill(checkAmount, paybill, AddType.Negative);
				checkbill = processCostBill(checkAmount, checkbill, AddType.Negative);
			}else{
				paybill = processPaybill(checkAmount, paybill, AddType.Negative);
				checkbill = processCostBill(checkAmount, checkbill, AddType.Positive);
			}
			
		}
		
		costBillService.save(checkbill);
		paymentBillService.save(paybill);
		/*资金计划：取消对单时处理*/
		PaymentCheckVo checkVo = queryBillTotalPayAmountByBillId(billId);
		BigDecimal billTotalAmount = checkVo.getBillTotalPayAmount();
		BigDecimal add = billTotalAmount.subtract(amount);
		PaymentCheckVo pmcVo = new PaymentCheckVo();
		pmcVo.setBillTotalPayAmount(add);
		pmcVo.setFreeAmount(checkVo.getFreeAmount().subtract(freeAmount));
		pmcVo.setBillId(billId);
		RequestResult changePaymentAmount = capitalPlanService.changePaymentAmount(pmcVo,2);
		if(changePaymentAmount.getReturnCode()==1){
			return changePaymentAmount;
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除是否会导致金额溢出
	 * @param paybill
	 * @param checkAmount
	 * @return
	 */
	private boolean checkDeleteWillOverflow(PaymentBill paybill, BigDecimal checkAmount){
		if(paybill.getAmount().compareTo(BigDecimal.ZERO)>0){
			BigDecimal total = NumberUtil.add(paybill.getTotalCheckAmount(), checkAmount);
			if(total.compareTo(paybill.getAmount())>0){
				return true;
			}
		}else{
			BigDecimal total = NumberUtil.add(paybill.getTotalCheckAmount(), checkAmount);
			total = NumberUtil.multiply(total, new BigDecimal(-1));
			BigDecimal payAmount = NumberUtil.multiply(paybill.getAmount(), new BigDecimal(-1));
			if(total.compareTo(payAmount)>0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除与费用单的勾对
	 * @param entity
	 * @return
	 */
	@Transactional
	private RequestResult deletePayCheck(PaymentCheck entity) {
		String billId = entity.getBillId();
		BigDecimal amount2 = entity.getAmount();
		BigDecimal freeAmount = entity.getFreeAmount();
		PaymentBill checkbill = paymentBillService.get(entity.getBillId());
		PaymentBill paybill = entity.getPaymentBill();
		
		String payBillName = WarehouseBuilderCodeHelper.getBillName(paybill.getBillType());
		
		BigDecimal checkAmount = entity.getAmount();
		if(checkbill.getBillType()==WarehouseBuilderCodeHelper.xsfld||
				checkbill.getBillType()==WarehouseBuilderCodeHelper.cgfld){
			
			if(checkDeleteWillOverflow(paybill, checkAmount)){
				return buildFailRequestResult("勾对金额较大，删除后会导致"+payBillName+"累计勾对金额大于"+payBillName+"金额，不能删除");
			}
		}
		
		if(paybill.getAmount().compareTo(BigDecimal.ZERO)>0){
			if(checkbill.getBillType()==WarehouseBuilderCodeHelper.xsfld || checkbill.getBillType()==WarehouseBuilderCodeHelper.cgfld){
				paybill = processPaybill(checkAmount, paybill, AddType.Positive);
				checkbill = processPaybill(checkAmount, checkbill, AddType.Negative);
			}else{
				paybill = processPaybill(checkAmount, paybill, AddType.Negative);
				checkbill = processPaybill(checkAmount, checkbill, AddType.Positive);
			}
		}else{
				paybill = processPaybill(checkAmount, paybill, AddType.Positive);
				checkbill = processPaybill(checkAmount, checkbill, AddType.Negative);
		}
		
		paymentBillService.save(checkbill);
		paymentBillService.save(paybill);
		/*资金计划：取消对单时处理*/
		PaymentCheckVo checkVo = queryBillTotalPayAmountByBillId(billId);
		BigDecimal billTotalAmount = checkVo.getBillTotalPayAmount();
		BigDecimal add = billTotalAmount.subtract(amount2);
		PaymentCheckVo pmcVo = new PaymentCheckVo();
		pmcVo.setBillTotalPayAmount(add);
		pmcVo.setFreeAmount(checkVo.getFreeAmount().subtract(freeAmount));
		pmcVo.setBillId(billId);
		RequestResult changePaymentAmount = capitalPlanService.changePaymentAmount(pmcVo,2);
		if(changePaymentAmount.getReturnCode()==1){
			return changePaymentAmount;
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 删除仓储勾对
	 * @return
	 */
	@Transactional
	private RequestResult deleteWarehouseCheck(PaymentCheck entity){
		String billId = entity.getBillId();
		WarehouseBill bill = billService.get(entity.getBillId());
		PaymentBill paymentBill = entity.getPaymentBill();
		
		BigDecimal checkAmount = entity.getAmount();
		BigDecimal freeAmount = entity.getFreeAmount();
		
		String payBillName = WarehouseBuilderCodeHelper.getBillName(paymentBill.getBillType());
		
		if(bill.getBillType()==WarehouseBuilderCodeHelper.xsth||
				bill.getBillType()==WarehouseBuilderCodeHelper.cgth){
			
			if(checkDeleteWillOverflow(paymentBill, checkAmount)){
				return buildFailRequestResult("勾对金额较大，删除后会导致"+payBillName+"累计勾对金额大于"+payBillName+"金额，不能删除");
			}
			
		}
		
		if(bill.getBillType()==WarehouseBuilderCodeHelper.xsth){
			if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
			}else{
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
			}
		}else if(bill.getBillType()==WarehouseBuilderCodeHelper.cgth){
			if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
			}else{
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
			}
		}else{
			if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
			}else{
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
			}
		}
		
		bill = processWarehousebill(checkAmount, freeAmount, bill, AddType.Negative);
		billService.save(bill);
		/*资金计划：取消对单时处理*/
		PaymentCheckVo pmcVo = new PaymentCheckVo();
		pmcVo.setBillId(billId);
		/*1.获取总共勾对金额==资金计划已付金额*/
		/*2.获取总免单金额*/
		/*3. 设置总付款金额与总免单金额*/
		//step3
		//设置单据累计勾对金额
		pmcVo.setBillTotalPayAmount(bill.getTotalPayAmount());
		//设置单据免单金额
		pmcVo.setBillFreeAmount(bill.getFreeAmount());
		RequestResult changePaymentAmount = capitalPlanService.changePaymentAmount(pmcVo,2);
		if(changePaymentAmount.getReturnCode()==1){
			return changePaymentAmount;
		}
		paymentBillService.save(paymentBill);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断时间戳是否相等
	 */
	public boolean checkUpdateTime(PaymentCheckVo vo, PaymentCheck entity){
		String updateTime = DateUtils.getStringByFormat(entity.getUpdateTime(), DATE_TIME);
		if(updateTime.equals(vo.getUpdateTime())){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 新增/编辑收付款单【勾对】
	 * @param paymentBillId 收付款单ID，必填
	 * @param billId 要对单的单据ID，必填
	 * @param billType 要对单的单据类型，必填
	 * @param amount 对单金额，必填
	 * @param freeAmount 免单金额
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@Transactional
	public RequestResult save(PaymentCheckVo vo) throws Exception{
		RequestResult check = buildSuccessRequestResult();
		PaymentCheck saveEntity = null;
		try {
			BigDecimal billTotalPayAmount = vo.getBillTotalPayAmount();
			if(StringUtils.isBlank(vo.getBillId())){
				return buildFailRequestResult("请选择关联的单据");
			}
			if(StringUtils.isBlank(vo.getPaymentBillId())){
				return buildFailRequestResult("请选择关联的收/付款单");
			}
			BigDecimal amount = vo.getAmount();
			BigDecimal freeAmount = vo.getFreeAmount();
			String billId = vo.getBillId();
			String paybillId = vo.getPaymentBillId();
			String billCode = vo.getBillCode();
			
			PaymentBill paybill = paymentBillService.get(paybillId);
			
			BillType type = checkBillType(vo.getBillType());
			
			String payBillName = WarehouseBuilderCodeHelper.getBillName(paybill.getBillType());
			String checkBillName = WarehouseBuilderCodeHelper.getBillName(vo.getBillType());
			
			if(type==BillType.Warehouse){//仓储单据的表
				 check = checkWarehouseBill(amount, freeAmount, billId, paybill, payBillName, checkBillName);
				if(!check.isSuccess())return check;
			}else if(type==BillType.Pay){//收付款单的表
				 check = checkPayBill(amount, billId, paybill, payBillName, checkBillName);
				if(!check.isSuccess())return check;
			}else if(type==BillType.Cost){//费用单的表
				 check = checkCostBill(amount, billId, paybill, payBillName, checkBillName);
				if(!check.isSuccess())return check;
			}else{
				return buildFailRequestResult("单据类型不支持");
			}
			
			String accId = SecurityUtil.getFiscalAccountId();
			Date date = Calendar.getInstance().getTime();//取当前日期
			StockPeriod period = periodService.getPeriod(date, accId);
			if(period==null){
				return buildFailRequestResult("当前日期没有没有对应会计期间");
			}
			else if(StockPeriod.CHECKED == period.getCheckoutStatus()){
				return buildFailRequestResult("当前日期对应的会计期间已结账");
			}
			else if(StockPeriod.UN_USED == period.getCheckoutStatus()){
				return buildFailRequestResult("当前日期对应的会计期间未启用");
			}
			
			saveEntity = saveEntity(vo, paybill, period);
			/**资金计划审核**/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(billId);
			if(capitalPlan!=null){
				/*资金计划自动对单*/
				/*1.获取总共勾对金额==资金计划已付金额*/
				/*2.获取总免单金额*/
				/*3.设置总付款金额与总免单金额*/
				vo.setBillId(billId);
				vo.setBillCode(billCode);
				vo.setBillTotalPayAmount(billTotalPayAmount);
				vo.setFreeAmount(vo.getBillFreeAmount());
				RequestResult changePaymentAmount = capitalPlanService.changePaymentAmount(vo, 1);
				if(changePaymentAmount.getReturnCode()==1){
					return changePaymentAmount;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("收付款单【勾对】出错!");
		}
		if(saveEntity !=null){
			return buildSuccessRequestResult(saveEntity.getFid());
		}
		return buildSuccessRequestResult();
		
	}
	
	/**
	 * 检查仓储单据是否满足勾对条件
	 * @return
	 */
	private RequestResult checkWarehouseBill(BigDecimal amount, BigDecimal freeAmount, String billId,
			PaymentBill paybill, String payBillName, String checkBillName){
		WarehouseBill bill = billService.get(billId);
		BigDecimal leftAmount = NumberUtil.add(bill.getTotalAmount(), bill.getExpenseAmount());
		leftAmount = NumberUtil.subtract(leftAmount, bill.getTotalPayAmount());
		leftAmount = NumberUtil.subtract(leftAmount, bill.getFreeAmount());
		leftAmount = NumberUtil.subtract(leftAmount, bill.getDeductionAmount());

		BigDecimal currentAmount = NumberUtil.add(amount, freeAmount);
		if(currentAmount.compareTo(leftAmount)>0){
			return buildFailRequestResult("对单金额超出"+checkBillName+"未收/未付金额");
		}
		
		//负数处理
		if(bill.getBillType()==WarehouseBuilderCodeHelper.xsth||
				bill.getBillType()==WarehouseBuilderCodeHelper.cgth){
			BigDecimal curCheck = NumberUtil.add(amount, freeAmount);
			BigDecimal billAmount = NumberUtil.add(bill.getTotalAmount(), bill.getExpenseAmount());
			if(curCheck.compareTo(billAmount)!=0){
				return buildFailRequestResult(checkBillName+"类型的单据需要一次勾兑完毕");
			}
		}else{
			BigDecimal delta = NumberUtil.subtract(paybill.getAmount(), paybill.getTotalCheckAmount()).abs();
			if(delta.compareTo(amount)<0){
				return buildFailRequestResult("对单总金额超出"+payBillName+"总金额");
			}
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 检查收付款单据是否满足勾对条件
	 * @return
	 */
	private RequestResult checkPayBill(BigDecimal amount, String billId,
			PaymentBill paybill, String payBillName, String checkBillName){
		PaymentBill bill = paymentBillService.get(billId);
		
		int billType = bill.getBillType();
		
		BigDecimal delta = NumberUtil.subtract(bill.getAmount(), bill.getTotalCheckAmount()).abs();
		if(delta.compareTo(amount)<0){
			return buildFailRequestResult("对单总金额超出"+checkBillName+"总金额");
		}
		
		if(paybill.getAmount().compareTo(BigDecimal.ZERO)>0 && (
				billType==WarehouseBuilderCodeHelper.cgfld ||
				billType==WarehouseBuilderCodeHelper.xsfld)){
			//返利单会减小已对单金额
		}else{
			BigDecimal delta2 = NumberUtil.subtract(paybill.getAmount(), paybill.getTotalCheckAmount()).abs();
			if(delta2.compareTo(amount)<0){
				return buildFailRequestResult("对单总金额超出"+payBillName+"总金额");
			}
		}
		
		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 检查费用单据是否满足勾对条件
	 * @return
	 */
	private RequestResult checkCostBill(BigDecimal amount, String billId,
			PaymentBill paybill, String payBillName, String checkBillName){
		CostBill bill = costBillService.get(billId);
		
		BigDecimal delta = NumberUtil.subtract(bill.getAmount(), bill.getTotalPayAmount());
		if(delta.abs().compareTo(amount)<0){
			return buildFailRequestResult("对单总金额超出"+checkBillName+"总金额");
		}
		
		if(paybill.getAmount().compareTo(BigDecimal.ZERO)>0){
			if(bill.getExpenseType()==CostBill.EXPENSE_TYPE_SUBTRACT){
				//减少
			}else{
				BigDecimal delta2 = NumberUtil.subtract(paybill.getAmount(), paybill.getTotalCheckAmount()).abs();
				if(delta2.compareTo(amount)<0){
					return buildFailRequestResult("对单总金额超出"+payBillName+"总金额");
				}
			}
		}else{
			BigDecimal delta2 = NumberUtil.subtract(paybill.getAmount(), paybill.getTotalCheckAmount()).abs();
			if(delta2.compareTo(amount)<0){
				return buildFailRequestResult("对单总金额超出"+payBillName+"总金额");
			}
		}
		
		return buildSuccessRequestResult();
	}

	/**
	 * 新增/编辑收付款单
	 * @param vo
	 * @throws Exception 
	 */
	private PaymentCheck saveEntity(PaymentCheckVo vo, PaymentBill paymentBill, StockPeriod period) throws Exception {
		
		String billId = vo.getBillId();
		Integer billType = vo.getBillType();
		
		PaymentCheck entity = new PaymentCheck();
		
		BigDecimal checkAmount = vo.getAmount();
		BigDecimal freeAmount = vo.getFreeAmount();
		
		BillType type = checkBillType(billType);
		if(type==BillType.Warehouse){//仓储单据的表
			WarehouseBill bill = billService.get(billId);
			if(bill.getBillType()==WarehouseBuilderCodeHelper.xsth){
				if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				}else{
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				}
			}else if(bill.getBillType()==WarehouseBuilderCodeHelper.cgth){
				if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				}else{
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				}
			}else{
				if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
				}else{
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				}
			}
			
			bill = processWarehousebill(checkAmount, freeAmount, bill, AddType.Positive);
			billService.save(bill);
			
			entity.setBillType(bill.getBillType());
			
		}else if(type==BillType.Pay){//收付款单的表
			PaymentBill bill = paymentBillService.get(billId);
			
			if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
				if(bill.getBillType()==WarehouseBuilderCodeHelper.xsfld || bill.getBillType()==WarehouseBuilderCodeHelper.cgfld){
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
					bill = processPaybill(checkAmount, bill, AddType.Positive);
				}else{
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
					bill = processPaybill(checkAmount, bill, AddType.Negative);
				}
			}else{
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				bill = processPaybill(checkAmount, bill, AddType.Positive);
			}
			paymentBillService.save(bill);
			
			entity.setBillType(bill.getBillType());
			
		}else if(type==BillType.Cost){//费用单的表
			
			CostBill bill = costBillService.get(billId);
			
			
			if(bill.getExpenseType()==CostBill.EXPENSE_TYPE_ADD){
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
				bill = processCostBill(checkAmount, bill, AddType.Positive);
			}else if(bill.getExpenseType()==CostBill.EXPENSE_TYPE_SUBTRACT){
				paymentBill = processPaybill(checkAmount, paymentBill, AddType.Negative);
				bill = processCostBill(checkAmount, bill, AddType.Positive);
			}else{
				if(bill.getAmount().compareTo(BigDecimal.ZERO)>0){
					//付款
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
					bill = processCostBill(checkAmount, bill, AddType.Positive);
				}else{
					//收款
					paymentBill = processPaybill(checkAmount, paymentBill, AddType.Positive);
					bill = processCostBill(checkAmount, bill, AddType.Negative);
				}
			}
			
			costBillService.save(bill);
			
			entity.setBillType(WarehouseBuilderCodeHelper.fyd);
		}else{
			throw new RuntimeException("unsupport billType!");
		}
		
		entity.setFid(null);//作为新数据保存
		entity.setBillId(billId);
		entity.setAmount(vo.getAmount());
		entity.setPaymentBill(paymentBill);
		entity.setCheckDate(new Date());//check date 取当前时间
		entity.setPeriod(period);
		
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setDept(SecurityUtil.getCurrentDept());
		entity.setDescribe(vo.getDescribe());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setFreeAmount(freeAmount==null?BigDecimal.ZERO:freeAmount);
		
		checkRepo.save(entity);
		paymentBillService.save(paymentBill);
		
		return entity;
	}
	
	/**
	 * 收款单/付款单处理
	 * @return
	 */
	private PaymentBill processPaybill(BigDecimal amount, PaymentBill bill, AddType addType){
		if(addType==AddType.Positive){
			bill.setTotalCheckAmount(NumberUtil.add(bill.getTotalCheckAmount(), amount));
		}else{
			bill.setTotalCheckAmount(NumberUtil.subtract(bill.getTotalCheckAmount(), amount));
		}
		
		return bill;
	}
	
	/**
	 *仓储单据处理
	 * @return
	 */
	private WarehouseBill processWarehousebill(BigDecimal amount, BigDecimal freeAmount, 
			WarehouseBill bill, AddType addType){
		if(addType==AddType.Positive){
			bill.setTotalPayAmount(NumberUtil.add(bill.getTotalPayAmount(), amount));
			bill.setFreeAmount(NumberUtil.add(bill.getFreeAmount(), freeAmount));
		}else{
			bill.setTotalPayAmount(NumberUtil.subtract(bill.getTotalPayAmount(), amount));
			bill.setFreeAmount(NumberUtil.subtract(bill.getFreeAmount(), freeAmount));
		}
		return bill;
	}
	
	/**
	 *仓储单据处理
	 * @return
	 */
	private CostBill processCostBill(BigDecimal amount, CostBill bill, AddType addType){
		if(addType==AddType.Positive){
			bill.setTotalPayAmount(NumberUtil.add(bill.getTotalPayAmount(), amount));
		}else{
			bill.setTotalPayAmount(NumberUtil.subtract(bill.getTotalPayAmount(), amount));
		}
		return bill;
	}
	
	public enum AddType{
		Positive,
		Negative
	}
	/**
	 * 根据单据ID查询勾对记录
	 * @param billId  单据ID
	 * @return
	 */
	public Long countByBillId(String billId){
		return checkRepo.countByBillId(billId,billId);
	}
	


	@Override
	public CrudRepository<PaymentCheck, String> getRepository() {
		return checkRepo;
	}

	/**
	 * 根据单据获取已勾对总金额
	 * @param billId
	 * @return
	 */
	public PaymentCheckVo queryBillTotalPayAmountByBillId(String billId) {
		PaymentCheckVo vo = new PaymentCheckVo();
		List<PaymentCheck> list = checkRepo.findByBillId(billId);
		BigDecimal amountSum = BigDecimal.ZERO;
		BigDecimal freeAmountSum = BigDecimal.ZERO;
		for (PaymentCheck check : list) {
			BigDecimal amount = check.getAmount();
			amountSum = amountSum.add(amount);
			BigDecimal freeAmount = check.getFreeAmount();
			freeAmountSum = freeAmountSum.add(freeAmount);
		}
		vo.setBillTotalPayAmount(amountSum);
		vo.setFreeAmount(freeAmountSum);
		return vo;
	}
}
