package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>销售退货网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月24日
 */
@Service("ops.SalesReturnWebService")
public class SalesReturnWebService extends BaseWarehouseWebServiceBuilder{
	
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).xsth}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}

	/**
	 * 重写save，插入单据关联
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
		Date billDate = vo.getBillDate();//单据日期
		String freeAmount = vo.getFreeAmount();
		String describe = vo.getDescribe();
		String inWareHouseId = vo.getInWareHouseId();//进仓仓库ID
		String outWareHouseId = vo.getOutWareHouseId();//出仓仓库ID
		String customerId = vo.getCustomerId();//客户ID
		String supplierId = vo.getSupplierId();//供应商ID
		String deptId = vo.getDeptId();//部门ID
		String inMemberId = vo.getInMemberId();//收货人ID
		String outMemberId = vo.getOutMemberId();//发货人ID
		String otherCharge = vo.getOtherCharges();
		Integer productionStatus = vo.getProductionStatus();//
		Date planStart = vo.getPlanStart();
		Date now = new Date();
			
		//校验数据
		RequestResult result = checkByRule(vo);
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
			entity.setCreatorDept(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}
		else{
			entity = billRepo.findOne(fid);
			entity.setUpdateTime(now);
		}
		entity.setCode(code.trim());
		entity.setEndDate(endDate);
		entity.setBillDate(billDate);
		entity.setDescribe(describe);
		entity.setBillType(billType);
		entity.setVoucherCode(voucherCode);
		entity.setFreeAmount(NumberUtil.toBigDeciaml(freeAmount));
		entity.setOtherCharges(NumberUtil.toBigDeciaml(otherCharge));
		if(productionStatus != null){
		  entity.setProductionStatus(productionStatus);
		}else{
		  entity.setProductionStatus(WarehouseBill.NOT_STARTED);
		}
		entity.setPlanStart(planStart);
		//会计期间
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, SecurityUtil.getFiscalAccountId());
		entity.setStockPeriod(stockPeriod);
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
		
		List<WarehouseBill> refBills = null;
		if(StringUtils.isNotBlank(vo.getRelationId())){
			int refType = WarehouseBuilderCodeHelper.getBillType(builderCode);
			refBills = super.insertBillRelation(entity.getFid(), vo.getRelationId(), vo.getBillType(), refType);
		}
		
		//匹配关联的明细ID
		List<WarehouseBillDetailVo> detailVos = vo.getDetailList();
		if(CollectionUtils.isNotEmpty(refBills)){
			for(WarehouseBillDetailVo detailVo:detailVos){
				if(Strings.isNullOrEmpty(detailVo.getRefDetailId())){
					WarehouseBillDetail refDetail = catchRefDetail(refBills, detailVo);
					if(refDetail!=null){
						detailVo.setRefDetailId(refDetail.getFid());
					}
				}
			}
		}
		
		//新增、编辑仓库单据明细
		List<WarehouseBillDetail> details = saveBillDetail(entity, detailVos,vo.getRelationId());
		
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
}
