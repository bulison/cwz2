package cn.fooltech.fool_ops.domain.cost.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.entity.CostBillCheck;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillCheckRepository;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.cost.vo.CostBillCheckVo;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService.BillType;
import cn.fooltech.fool_ops.domain.payment.vo.CheckBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;

/**
 * <p>收付款对单网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-24 15:32:59
 */
@Service
public class CostBillCheckService extends BaseService<CostBillCheck, CostBillCheckVo, String> {
	
	@Autowired
	private WarehouseBillRepository billRepo;
	
	@Autowired
	private CostBillRepository costBillRepo;
	
	@Autowired
	private CostBillCheckRepository checkRepo;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService periodService;
	
	
	@Autowired
	private PaymentCheckService payCheckService;
	
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillRepository paymentRepo;
	
	
	/**
	 * 查找已勾兑的单据
	 */
	public Page<CostBillCheckVo> query(CostBillCheckVo vo, PageParamater paramater){

		String accId = SecurityUtil.getFiscalAccountId();
		String costBillId = vo.getCostBillId();
		//排序
		Sort sort = new Sort(Direction.DESC, "createTime");
		//分页
		PageRequest pageRequest = getPageRequest(paramater, sort);

		Page<CostBillCheck> page = checkRepo.findPageBy(accId, costBillId, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 查找未勾兑的单据
	 */
	public Page queryByBillType(WarehouseBillVo vo, PageParamater paramater, BillType billType){
		
		Assert.notNull(vo.getCostBillId());
		
		CostBill costbill = costBillRepo.findOne(vo.getCostBillId());
		String accId = SecurityUtil.getFiscalAccountId();
		List<String> excludeIds = checkRepo.findCheckedBillIdsByCostBillId(vo.getCostBillId());
		
		Date startDay = vo.getStartDay();
		Date endDay = vo.getEndDate();
		String code = vo.getCode();
		Integer type = vo.getBillType();
		
		//排序
		Sort sort = new Sort(Direction.DESC, "code");
		//分页
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		if(billType.equals(BillType.Warehouse)){
			
			return billRepo.findPageBy(accId, costbill, startDay, endDay, code, 
					type, excludeIds, pageRequest);
					
		}else if(billType.equals(BillType.Cost)){
			
			return costBillRepo.findPageBy(accId, costbill, startDay, endDay, code, 
					type, excludeIds, pageRequest);
			
		}else{
			return paymentRepo.findPageBy(accId, costbill, startDay, endDay, code, 
					type, excludeIds, pageRequest);
			
		}
	}
	
	/**
	 * 根据单据ID查找已勾兑的单据
	 * @param wbillId
	 * @param paramater
	 * @return
	 */
	public Page<CostBillCheckVo> getByWarehouseId(String wbillId, PageParamater paramater){
		//分页
		PageRequest pageRequest = getPageRequest(paramater);
		Page<CostBillCheck> page = checkRepo.queryByWarehgouseBillId(wbillId, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	/**
	 *  根据费用单ID查找已勾兑的单据
	 * @param costBillId
	 * @param paramater
	 * @return
	 */
	public Page<CostBillCheckVo> queryByCostBillId(String costBillId, PageParamater paramater){
		//分页
		PageRequest pageRequest = getPageRequest(paramater);
		Page<CostBillCheck> page = checkRepo.queryByCostBillId(costBillId, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 单个收付款单勾对实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public CostBillCheckVo getVo(CostBillCheck entity){
		if(entity == null)
			return null;
		CostBillCheckVo vo = new CostBillCheckVo();
		Integer checkBillType = entity.getBillType()==null?0: entity.getBillType();
		BillType type = payCheckService.checkBillType(checkBillType);
		if(type==BillType.Warehouse){//仓储单据的表
			WarehouseBill bill = billRepo.findOne(entity.getBill());
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(bill.getFreeAmount()==null?BigDecimal.ZERO:bill.getFreeAmount());
			vo.setBillTotalAmount(bill.getTotalAmount()==null?BigDecimal.ZERO:bill.getTotalAmount());
			vo.setBillTotalPayAmount(bill.getTotalPayAmount()==null?BigDecimal.ZERO:bill.getTotalPayAmount());
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtils.getStringByFormat(bill.getBillDate(), DATE_TIME));
			}
			//客户
			Customer customer = bill.getCustomer();
			if(customer != null){
				vo.setCsvId(customer.getFid());
				vo.setCsvName(customer.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
			}
			//供应商
			Supplier supplier = bill.getSupplier();
			if(supplier != null){
				vo.setCsvId(supplier.getFid());
				vo.setCsvName(supplier.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
			}
			Integer recordStatus = bill.getRecordStatus();
			vo.setRecordStatus(recordStatus);
			
		}else if(type==BillType.Pay){//收付款单的表
			PaymentBill bill = paymentRepo.findOne(entity.getBill());
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(BigDecimal.ZERO);
			vo.setBillTotalAmount(bill.getAmount()==null?BigDecimal.ZERO:bill.getAmount());
			vo.setBillTotalPayAmount(bill.getTotalCheckAmount()==null?BigDecimal.ZERO:bill.getTotalCheckAmount());
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtils.getStringByFormat(bill.getBillDate(), DATE_TIME));
			}
			//客户
			Customer customer = bill.getCustomer();
			if(customer != null){
				vo.setCsvId(customer.getFid());
				vo.setCsvName(customer.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
			}
			//供应商
			Supplier supplier = bill.getSupplier();
			if(supplier != null){
				vo.setCsvId(supplier.getFid());
				vo.setCsvName(supplier.getName());
				vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
			}
			Integer recordStatus = bill.getRecordStatus();
			vo.setRecordStatus(recordStatus);
			
		}else if(type==BillType.Cost){//费用单的表
			CostBill bill = costBillRepo.findOne(entity.getBill());
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
			vo.setBillFreeAmount(BigDecimal.ZERO);
			vo.setBillTotalAmount(bill.getAmount()==null?BigDecimal.ZERO:bill.getAmount());
			vo.setBillTotalPayAmount(bill.getTotalCheckAmount()==null?BigDecimal.ZERO:bill.getTotalCheckAmount());
			if(bill.getBillDate()!=null){
				vo.setBillDate(DateUtils.getStringByFormat(bill.getBillDate(), DATE_TIME));
			}
			Integer recordStatus = bill.getRecordStatus();
			vo.setRecordStatus(recordStatus);
			//客户,供应商
			CustomerSupplierView csv = bill.getCsv();
			if(csv != null){
				vo.setCsvId(csv.getFid());
				vo.setCsvName(csv.getName());
				vo.setCsvType(csv.getType());
			}
		}else{
			throw new RuntimeException("unsupport billType!");
		}
		vo.setFid(entity.getFid());
		
		
		vo.setDescribe(entity.getDescribe());
		vo.setFreeAmount(entity.getFreeAmount());
		vo.setIncomeAmount(entity.getIncomeAmount());
		vo.setCostBillId(entity.getCostBill().getFid());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DATE_TIME));
		vo.setCheckDate(DateUtils.getStringByFormat(entity.getCheckDate(), DATE_TIME));
		
		
		if(entity.getIncomeAmount().abs().compareTo(entity.getFreeAmount().abs())<0){
			vo.setAmount(entity.getFreeAmount());
		}else{
			vo.setAmount(entity.getIncomeAmount());
		}
		
		return vo;
	}
	
	/**
	 * 删除收付款单勾对<br>
	 */
	public RequestResult delete(String fid){
		
		CostBillCheck check = checkRepo.findOne(fid);
		CostBill bill = check.getCostBill();
		if(bill.getStockPeriod().getCheckoutStatus() == StockPeriod.CHECKED){
			return buildFailRequestResult("会计期间已结账");
		}
		
		BigDecimal amount = bill.getAmount();
		if(amount.compareTo(BigDecimal.ZERO)>0){
			BigDecimal totalAmount = bill.getIncomeAmount().max(bill.getFreeAmount());
			BigDecimal checkAmount = check.getIncomeAmount().max(check.getFreeAmount());
			BigDecimal afterTotalCheck = bill.getTotalCheckAmount().subtract(checkAmount);
			if(afterTotalCheck.compareTo(totalAmount) > 0){
				return buildFailRequestResult("[费用单应收或应付金额]小于[费用单累计勾对金额]，不能删除勾对!");
			}
			bill.setTotalCheckAmount(afterTotalCheck);
		}else{
			BigDecimal totalAmount = bill.getIncomeAmount().min(bill.getFreeAmount());
			BigDecimal checkAmount = check.getIncomeAmount().min(check.getFreeAmount());
			BigDecimal afterTotalCheck = bill.getTotalCheckAmount().subtract(checkAmount);
			if(afterTotalCheck.abs().compareTo(totalAmount.abs()) > 0){
				return buildFailRequestResult("[费用单应收或应付金额]小于[费用单累计勾对金额]，不能删除勾对!");
			}
			bill.setTotalCheckAmount(afterTotalCheck);
		}
		
		/*Integer checkBillType = check.getBillType()==null?0: check.getBillType();
		BillType type = payCheckService.checkBillType(checkBillType);
		if(type==BillType.Warehouse){//仓储单据的表
			WarehouseBill warehouseBill = billService.get(check.getBill());
			billService.save(warehouseBill);
		}else if(type==BillType.Pay){//收付款单的表
			PaymentBill paymentBill = paymentRepo.findOne(check.getBill());
			paymentRepo.save(paymentBill);
			
		}else if(type==BillType.Cost){//费用单的表
			CostBill costBill = costBillRepo.findOne(check.getBill());
			costBillRepo.save(costBill);
		}else{
			throw new RuntimeException("unsupport billType!");
		}*/
		
		costBillRepo.save(bill);
		checkRepo.delete(fid);
		return buildSuccessRequestResult();
	}
	
	public String getBillTypeName(PaymentBill bill){
		if(bill.getBillType()==WarehouseBuilderCodeHelper.skd){
			return "收款单";
		}else if(bill.getBillType()==WarehouseBuilderCodeHelper.fkd){
			return "付款单";
		}
		return "";
	}
	
	
	/**
	 * 判断时间戳是否相等
	 */
	public boolean checkUpdateTime(CostBillCheckVo vo, CostBillCheck entity){
		String updateTime = DateUtils.getStringByFormat(entity.getUpdateTime(), DATE_TIME);
		if(updateTime.equals(vo.getUpdateTime())){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 新增/编辑收付款单
	 * @param vo
	 * @throws Exception 
	 */
	public RequestResult save(CostBillCheckVo vo) throws Exception{
		if(StringUtils.isBlank(vo.getBillId())){
			return buildFailRequestResult("请选择关联的单据");
		}
		if(StringUtils.isBlank(vo.getCostBillId())){
			return buildFailRequestResult("请选择关联的费用单");
		}
		
		List<CostBillCheck> checks = checkRepo.findByCostBillId(vo.getCostBillId());
		BigDecimal total = BigDecimal.ZERO;
		CostBill costBill = costBillRepo.findOne(vo.getCostBillId());
		//支出金额
		if(costBill.getFreeAmount().compareTo(BigDecimal.ZERO)!=0){
			if(costBill.getFreeAmount().compareTo(BigDecimal.ZERO)>0){
				//先统计已勾兑列表，勾对的金额是否超出单据金额
				for(CostBillCheck pcheck:checks){
					total = total.add(pcheck.getFreeAmount());
				}
				total = total.add(vo.getAmount());
//				if(total.compareTo(costBill.getFreeAmount())>0){
//					return buildFailRequestResult("对单总金额超出费用单总支出金额");
//				}
			}else{
				for(CostBillCheck pcheck:checks){
					total = total.add(pcheck.getFreeAmount());
				}
				total = total.add(vo.getAmount().multiply(new BigDecimal(-1)));
				if(total.abs().compareTo(costBill.getFreeAmount().abs())>0){
					return buildFailRequestResult("对单总金额超出费用单总支出金额");
				}
				vo.setAmount(vo.getAmount().multiply(new BigDecimal(-1)));
			}
			
		}
		//收入金额
		else if(costBill.getIncomeAmount().compareTo(BigDecimal.ZERO)!=0){
			if(costBill.getIncomeAmount().compareTo(BigDecimal.ZERO)>0){
				for(CostBillCheck pcheck:checks){
					total = total.add(pcheck.getIncomeAmount());
				}
				total = total.add(vo.getAmount().multiply(new BigDecimal(-1)));
				if(total.abs().compareTo(costBill.getIncomeAmount().abs())>0){	
					return buildFailRequestResult("对单总金额超出费用单总收入金额");
				}
			}else{
				for(CostBillCheck pcheck:checks){
					total = total.add(pcheck.getIncomeAmount());
				}
				total = total.subtract(vo.getAmount().multiply(new BigDecimal(-1)));
				if(total.abs().compareTo(costBill.getIncomeAmount().abs())>0){
					return buildFailRequestResult("对单总金额超出费用单总收入金额");
				}
				vo.setAmount(vo.getAmount().multiply(new BigDecimal(-1)));				
			}
		}
		
		String accId = SecurityUtil.getFiscalAccountId();
		Date date = Calendar.getInstance().getTime();//取当前日期
		StockPeriod period = periodService.getPeriod(date, accId);
		if(period == null){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_NOT_EXIST, "当前日期没有没有对应会计期间");
		}
		else if(StockPeriod.CHECKED == period.getCheckoutStatus()){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_CHECKED, "当前日期对应的会计期间已结账");
		}
		else if(StockPeriod.UN_USED == period.getCheckoutStatus()){
			return buildFailRequestResult(ErrorCode.STOCK_PERIOD_UN_USED, "当前日期对应的会计期间未启用");
		}
		
		CostBillCheck entity = saveEntity(vo, costBill, period);
		
		return buildSuccessRequestResult(entity.getFid());
	}

	/**
	 * 新增/编辑收付款单
	 * @param vo
	 * @throws Exception 
	 */
	@Transactional
	private CostBillCheck saveEntity(CostBillCheckVo vo, CostBill costBill, StockPeriod period) throws Exception {
		CostBillCheck entity = new CostBillCheck();
		Integer checkBillType = vo.getBillType()==null?0: vo.getBillType();
		BillType type = payCheckService.checkBillType(checkBillType);
		if(type==BillType.Warehouse){//仓储单据的表
			WarehouseBill bill = billRepo.findOne(vo.getBillId());
			entity.setBill(bill.getFid());
		}else if(type==BillType.Pay){//收付款单的表
			PaymentBill paymentBill = paymentRepo.findOne(vo.getBillId());
			entity.setBill(paymentBill.getFid());
			
		}else if(type==BillType.Cost){//费用单的表
			CostBill costBill2 = costBillRepo.findOne(vo.getBillId());
			entity.setBill(costBill2.getFid());
		}else{
			throw new RuntimeException("unsupport billType!");
		}
		
		entity.setFid(null);//作为新数据保存
		//设置支出金额
		if(costBill.getFreeAmount().compareTo(BigDecimal.ZERO)!=0){
			entity.setFreeAmount(vo.getAmount());
			
		}else if(costBill.getIncomeAmount().compareTo(BigDecimal.ZERO)!=0){
			entity.setIncomeAmount(vo.getAmount());
		}
		entity.setBillType(checkBillType);
		entity.setCostBill(costBill);
		entity.setCheckDate(new Date());//check date 取当前时间
		entity.setPeriod(period);
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setDept(SecurityUtil.getCurrentDept());
		entity.setDescribe(vo.getDescribe());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		checkRepo.save(entity);
		BigDecimal totalCheckAmount = costBill.getTotalCheckAmount();
		BigDecimal amount = vo.getAmount();

		//收付款单累计勾对金额
		costBill.setTotalCheckAmount(totalCheckAmount.add(amount));
		costBillRepo.save(costBill);
		
		return entity; 
	}


	/**
	 * 查询付款/收款单能对单的单据
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public PageJson queryCheckBill(WarehouseBillVo vo,PageParamater paramater){
		Integer checkBillType = vo.getBillType()==null?0: vo.getBillType();

		BillType type = payCheckService.checkBillType(checkBillType);
		if(type==BillType.Warehouse){//仓储单据的表
			Page<WarehouseBill> query = (Page<WarehouseBill>) queryByBillType(vo, paramater, BillType.Warehouse);
			List<CheckBillVo> list = payCheckService.getWarehouseCheckBillVo(query);
			return getPageJson(list,query.getTotalElements());
			
		}else if(type==BillType.Pay){//收付款单的表
			Page<PaymentBill> query = (Page<PaymentBill>) queryByBillType(vo, paramater, BillType.Pay);
			List<CheckBillVo> list = payCheckService.getPaymentCheckBillVo(query);
			return getPageJson(list,query.getTotalElements());
			
		}else if(type==BillType.Cost){//费用单的表
			Page<CostBill> query = (Page<CostBill>) queryByBillType(vo, paramater, BillType.Cost);
			List<CheckBillVo> list = payCheckService.getCostCheckBillVo(query);
			return getPageJson(list,query.getTotalElements());
			
		}else{
			throw new RuntimeException("unsupport billType!");
		}
	}

	@Override
	public CrudRepository<CostBillCheck, String> getRepository() {
		return checkRepo;
	}

	/**
	 * 根据费用单ID统计
	 * @param costbillId
	 * @return
	 */
	public Long countByCostBillId(String costbillId) {
		return checkRepo.countByCostBillId(costbillId);
	}

}
