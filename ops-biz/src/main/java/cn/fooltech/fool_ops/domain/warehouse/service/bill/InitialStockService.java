package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fooltech.fool_ops.validator.ValidatorUtils;
import cn.fooltech.fool_ops.validator.bill.BillClassTransferUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>期初库存网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年11月26日
 */
@Service
public class InitialStockService extends BaseWarehouseWebServiceBuilder{
	
	
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).qckc}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}

	/**
	 * 重写save
	 * @param vo
	 * @return 
	 */
	@Override
	public RequestResult save(WarehouseBillVo vo) {
		String fid = vo.getFid();
		String code = vo.getCode();
		String voucherCode = vo.getVoucherCode();//凭证号
		Integer billType = vo.getBillType();//单据类型
		Date endDate = vo.getEndDate();//计划完成时间
		//Date billDate = vo.getBillDate();//单据日期
		String freeAmount = vo.getFreeAmount();
		String describe = vo.getDescribe();
		String inWareHouseId = vo.getInWareHouseId();//进仓仓库ID
		String outWareHouseId = vo.getOutWareHouseId();//出仓仓库ID
		String customerId = vo.getCustomerId();//客户ID
		String supplierId = vo.getSupplierId();//供应商ID
		String deptId = vo.getDeptId();//部门ID
		String inMemberId = vo.getInMemberId();//收货人ID
		String outMemberId = vo.getOutMemberId();//发货人ID
		String eventId = vo.getEventId();//事件ID
		Date now = new Date();
			
		//校验数据
		RequestResult result = checkByInitStockRule(vo);
		if(result.getReturnCode() == RequestResult.RETURN_FAILURE){
			return result;
		}
		
		WarehouseBill entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new WarehouseBill();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}
		else{
			entity = billRepo.findOne(fid);
			entity.setUpdateTime(now);
		}
		
		//会计期间
		StockPeriod stockPeriod = stockPeriodService.findFirstPeriod();
		entity.setStockPeriod(stockPeriod);
		
		Date billDate = stockPeriod.getStartDate();//单据日期
		
		entity.setCode(code.trim());
		entity.setEndDate(endDate);
		entity.setBillDate(billDate);
		entity.setDescribe(describe);
		entity.setBillType(billType);
		entity.setVoucherCode(voucherCode);
		entity.setFreeAmount(NumberUtil.toBigDeciaml(freeAmount));
		
		//进仓仓库
		if(StringUtils.isNotBlank(inWareHouseId)){
			entity.setInWareHouse(attrService.get(inWareHouseId));
		}
		//出仓仓库
		if(StringUtils.isNotBlank(outWareHouseId)){
			entity.setOutWareHouse(attrService.get(outWareHouseId));
		}
		//客户
		if(StringUtils.isNotBlank(customerId)){
			entity.setCustomer(customerService.get(customerId));
		}
		//供应商
		if(StringUtils.isNotBlank(supplierId)){
			entity.setSupplier(supplierService.get(supplierId));
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			entity.setDept(orgService.get(deptId));
		}
		//收货人
		if(StringUtils.isNotBlank(inMemberId)){
			entity.setInMember(memberService.get(inMemberId));
		}
		//发货人
		if(StringUtils.isNotBlank(outMemberId)){
			entity.setOutMember(memberService.get(outMemberId));
		}
		
		billRepo.save(entity);
		
		//新增、编辑仓库单据明细
		List<WarehouseBillDetail> details = saveBillDetail(entity, vo.getDetailList(),vo.getRelationId());
		
		//单据的合计金额
		BigDecimal totalAmount = calculateTotalAmount(entity.getBillType(), details);
		entity.setTotalAmount(totalAmount);
		billRepo.save(entity);
		Map<String, Object> map = Maps.newHashMap();
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(entity.getFid());
		result.setDataExt(map);
		return result;
	}
	
	/**
	 * 根据规则校验数据
	 * @param vo
	 * @return
	 */
	public RequestResult checkByInitStockRule(WarehouseBillVo vo){

		//验证主表字段
		Class clazz = BillClassTransferUtils.getSupportClass(vo.getBillType());
		String inValid = null;
		if(clazz!=null){
			inValid = ValidatorUtils.inValidMsg(vo, clazz);
		}else{
			inValid = ValidatorUtils.inValidMsg(vo);
		}
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}

		String accId = SecurityUtil.getFiscalAccountId();
		
		StockPeriod stockPeriod = stockPeriodService.findFirstPeriod();
		if(stockPeriod == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_NOT_EXIST, "第一个会计期间不存在");
		}
		else if(stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_UN_USED, "第一个会计期间未启用");
		}
		else if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_CHECKED, "第一个会计期间已结账");
		}
		
		RequestResult result = new RequestResult();
		if(!checkDataRealTime(vo)){
			result.setMessage("页面数据失效，请重新刷新页面!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		if(isCodeExist(accId, vo.getCode(), vo.getFid())){
			result.setMessage("单号已存在!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		if(StringUtils.isNotBlank(vo.getVoucherCode()) && 
				isVoucherCodeExist(accId, vo.getVoucherCode(), vo.getFid(),
						vo.getBillDate(), vo.getBillType())){
			result.setMessage("原始单号已存在!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		if(StringUtils.isNotBlank(vo.getFid()) && !canOperateByStatus(vo.getFid())){
			result.setMessage("无效操作，单据处于已审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		return result;
	}
	
	@Override
	public RequestResult delete(String id) {
		WarehouseBill entity = billRepo.findOne(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(entity.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		
		RequestResult result = new RequestResult();
		if(entity.getRecordStatus() == WarehouseBill.STATUS_AUDITED){
			result.setMessage("无效操作，单据处于已审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else if(relationService.isAssociated(id)){
			result.setMessage("无效操作，该单据已被关联!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			
			billDetailService.deleteByBill(id);
			relationService.deleteRelation(id);
			billRepo.delete(id);
		}
		return result;
	}
	
}
