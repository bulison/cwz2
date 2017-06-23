package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.UnitVo;
import cn.fooltech.fool_ops.domain.bom.entity.Bom;
import cn.fooltech.fool_ops.domain.bom.entity.BomDetail;
import cn.fooltech.fool_ops.domain.bom.service.BomDetailService;
import cn.fooltech.fool_ops.domain.bom.service.BomService;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;

/**
 * <p>生产计划单网页服务类</p>
 * @author lgk
 * @date 2016年3月30日下午03:01:32
 * @version V1.0
 */
@Service("ops.ProductionPlanWebService")
public class ProductionPlanWebService extends BaseWarehouseWebServiceBuilder{
	
	@Autowired
	private BomService bomService;
	
	@Autowired
	private BomDetailService bomDetailService;
	
	@Autowired
	private WarehouseBillDetailService detailWebService;
	
	@Autowired
	private GoodsPriceService priceService;
	
	@Autowired
	private UnitService unitWebService;
	
	
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).scjhd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}
	
	@Override
	public RequestResult delete(String id) {
//		 TODO 自动生成的方法存根
		return super.delete(id);
	}
	
	@Override
	public RequestResult save(WarehouseBillVo vo) {
		RequestResult parentResult = super.save(vo);
		if(parentResult.getReturnCode()==RequestResult.RETURN_SUCCESS && 
				StringUtils.isNotBlank(vo.getRelationId())){
			
			String billId = parentResult.getData().toString();
			int refType = WarehouseBuilderCodeHelper.getBillType(WarehouseBuilderCode.xsdd);
			super.insertBillRelation(billId, vo.getRelationId(), vo.getBillType(), refType);
		}
		return parentResult;
	}
	
	@Override
	public RequestResult passAudit(String id, StockLockingVo vo) {
		WarehouseBill bill = super.get(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(bill.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		RequestResult result = new RequestResult();
		if(bill.getRecordStatus() != WarehouseBill.STATUS_UNAUDITED){
			result.setMessage("无效操作，非待审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			bill.setAuditor(SecurityUtil.getCurrentUser());
			bill.setAuditTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(WarehouseBill.STATUS_AUDITED);
			super.save(bill);
		}
		return periodCheck;
	}
	
	public RequestResult proStatus(String id,Integer proStatus) {
		WarehouseBill bill = super.get(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(bill.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		RequestResult result = new RequestResult();
		if(bill.getRecordStatus() != WarehouseBill.STATUS_AUDITED){
			result.setMessage("无效操作，非审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			bill.setProductionStatus(proStatus);
			super.save(bill);
		}
		return periodCheck;
	}
	
	/**
	 * 从Bom表查找物料组成，如果有相同货品，自动合并
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<WarehouseBillDetailVo> calculation(String id){
		
		String accId = SecurityUtil.getFiscalAccountId();
		WarehouseBill bill = super.get(id);
		List<WarehouseBillDetail> details = bill.getDetails();
		
		Map<String, WarehouseBillDetail> cache = Maps.newLinkedHashMap();
		
		for(WarehouseBillDetail detail:details){
			
			if(detail.getDetailType()!=null && detail.getDetailType()==WarehouseBillDetail.DETAIL_TYPE_METERIAL)continue;
				
			String goodsId = detail.getGoods().getFid();
			String specId = detail.getGoodsSpec()==null?"":detail.getGoodsSpec().getFid();
			
			List<Bom> boms = bomService.findByMaterial(null, accId, goodsId, specId, Bom.DEFAULT);
			if(boms.size()==0){
				boms = bomService.findByMaterial(accId, goodsId, Bom.DEFAULT);
			}
			if(boms.size()>0){
				Bom bom = boms.get(0);
				Set<BomDetail> bomDetails = bom.getDetails();
				
				for(BomDetail bomDetail:bomDetails){
					
					String bomGoodsId = bomDetail.getGoods().getFid();
					String bomSpecId = bomDetail.getSpec()==null?"":bomDetail.getSpec().getFid();
					
					String key = bomGoodsId+"#"+bomSpecId;
					if(cache.containsKey(key)){
						WarehouseBillDetail detailCache = cache.get(key);
						
						BigDecimal accountQuentity = NumberUtil.multiply(detail.getAccountQuentity(), bomDetail.getAccountQuentity());
						accountQuentity = NumberUtil.add(detailCache.getAccountQuentity(), accountQuentity);
						detailCache.setAccountQuentity(accountQuentity);
						detailCache.setQuentity(accountQuentity);
					}else{
						WarehouseBillDetail detailCache = new WarehouseBillDetail();
						detailCache.setFid(null);
						detailCache.setAccountUint(detail.getAccountUint());
						detailCache.setBill(bill);
						detailCache.setGoods(bomDetail.getGoods());
						detailCache.setGoodsSpec(bomDetail.getSpec());
						detailCache.setScale(detailCache.getAccountUint().getScale());
						detailCache.setUnit(bomDetail.getAccountUnit());
						detailCache.setCreateTime(Calendar.getInstance().getTime());
						detailCache.setUpdateTime(Calendar.getInstance().getTime());
						detailCache.setCreator(SecurityUtil.getCurrentUser());
						
						BigDecimal accountQuentity = NumberUtil.multiply(detail.getAccountQuentity(), bomDetail.getAccountQuentity());
						
						detailCache.setAccountQuentity(accountQuentity);
						detailCache.setQuentity(accountQuentity);
						cache.put(key, detailCache);
					}
				}
			}
		}
		
		List<WarehouseBillDetailVo> cacheDetails = detailWebService.getVos(Lists.newArrayList(cache.values()));
		Collections.sort(cacheDetails, new Comparator<WarehouseBillDetailVo>(){

			@Override
			public int compare(WarehouseBillDetailVo o1, WarehouseBillDetailVo o2) {
				return o1.getGoodsCode().compareTo(o2.getGoodsCode());
			}
			
		});
		
		for(WarehouseBillDetailVo vo:cacheDetails){
			BigDecimal price = priceService.getLowestPrice(vo.getGoodsId(), vo.getAccountUintID(), vo.getGoodsSpecId());
			vo.setAccountUintPrice(NumberUtil.bigDecimalToStr(price, 4));
			vo.setUnitPrice(NumberUtil.bigDecimalToStr(price, 4));
		}
		
		return cacheDetails;
	}
	
	/**
	 * 从Bom表查找物料组成，如果有相同货品，自动合并
	 * @param wbId 仓库单据id
	 * @param goodsId 货品关联id
	 * @param specId  属性关联id	
	 * @param uintId  单位关联id
	 * @param counts  货品数量
	 * @return
	 */
	public List<WarehouseBillDetailVo> calculation(String wbId, String goodsId, String specId,String uintId,String counts){
		WarehouseBill bill =null;
		if(!Strings.isNullOrEmpty(wbId)){
			bill= super.get(wbId);
		}
		UnitVo unitVo = unitWebService.getById(uintId);
		//单位换算关系
		double scale=1;
		if(unitVo!=null){
			scale = Strings.isNullOrEmpty(unitVo.getScale())?1:Double.valueOf(unitVo.getScale());
		}
		
		String accId = SecurityUtil.getFiscalAccountId();
		List<WarehouseBillDetail> entities=new ArrayList<WarehouseBillDetail>();
		//检索bom表是否存在该货品
		List<Bom> boms = bomService.findByMaterial(null, accId, goodsId, specId, Bom.DEFAULT);
		//如果查不到数根据货品id，和默认配方查询
		if(boms.size()==0){
			boms = bomService.findByMaterial(accId, goodsId, Bom.DEFAULT);
		}if(boms.size()>0){
			Bom bom = boms.get(0);
			Set<BomDetail> details = bom.getDetails();
			for (BomDetail bomDetail : details) {
				//给物品材料赋值，计算材料个数返回
				WarehouseBillDetail detailCache = new WarehouseBillDetail();
				detailCache.setFid(null);
				detailCache.setAccountUint(bomDetail.getUnit());
				if(bill!=null){
					detailCache.setBill(bill);
				}
//				detailCache.setBill(bill);
				detailCache.setGoods(bomDetail.getGoods());
				detailCache.setGoodsSpec(bomDetail.getSpec());
				detailCache.setScale(detailCache.getAccountUint().getScale());
				detailCache.setUnit(bomDetail.getUnit());
				detailCache.setCreateTime(Calendar.getInstance().getTime());
				detailCache.setUpdateTime(Calendar.getInstance().getTime());
				detailCache.setCreator(SecurityUtil.getCurrentUser());
				//输入货品数量
				BigDecimal inAccountQuentity = Strings.isNullOrEmpty(counts)?BigDecimal.ZERO: new BigDecimal(counts);
				BigDecimal accountQuentity = NumberUtil.multiply(inAccountQuentity, bomDetail.getAccountQuentity());
				BigDecimal quentity = NumberUtil.multiply(inAccountQuentity, bomDetail.getQuentity());
				//输入数量和记账数量 * 换算单位得出最终的数量
				detailCache.setAccountQuentity(NumberUtil.multiply(accountQuentity, new BigDecimal(scale)));
				detailCache.setQuentity(NumberUtil.multiply(quentity, new BigDecimal(scale)));
				entities.add(detailCache);
			}
		}
		List<WarehouseBillDetailVo> cacheDetails = detailWebService.getVos(entities);
		if (cacheDetails!=null&&cacheDetails.size()>0) {
			return cacheDetails;
		}
		return null;
	}
	/**
	 * 循环物品对象数组，合计出材料总数
	 * @param vo 仓库单据记录明细
	 * @return
	 */
	public List<WarehouseBillDetailVo> calculationResult(WarehouseBillVo vo){
		//获取货品对象数组
		List<WarehouseBillDetailVo> detailList = vo.getDetailList();
		if(detailList==null||detailList.size()==0) return null;
		Map<String, WarehouseBillDetailVo> cache = Maps.newLinkedHashMap();
		//获取单个货品材料，有相同材料累加数量。
		for (WarehouseBillDetailVo wbdVo : detailList) {
			List<WarehouseBillDetailVo> calculation = calculation(wbdVo.getBillId(), wbdVo.getGoodsId(), wbdVo.getGoodsSpecId(), wbdVo.getUnitId(), wbdVo.getQuentity());
			if (calculation!=null&&calculation.size()>0) {
				for (WarehouseBillDetailVo detailVo : calculation) {
					String goodsId = detailVo.getGoodsId();
					String goodsSpecId = detailVo.getGoodsSpecId();
					String unitId = detailVo.getUnitId();
					String key = goodsId + "#" + goodsSpecId + "#" + unitId;
					//如果材料相同，累加记账数量，和货品数量;否则新增一个材料
					if(cache.containsKey(key)){
						WarehouseBillDetailVo detailCache = cache.get(key);
						double tempAccountQuentity = Strings.isNullOrEmpty(detailVo.getAccountQuentity())?0:Double.parseDouble(detailVo.getAccountQuentity());
						double tempQuentity =Strings.isNullOrEmpty(detailVo.getQuentity())?0:Double.parseDouble(detailVo.getQuentity());
						double accountQuentity =Strings.isNullOrEmpty(detailCache.getAccountQuentity())?0:Double.parseDouble(detailCache.getAccountQuentity());
						double quentity =Strings.isNullOrEmpty(detailCache.getQuentity())?0:Double.parseDouble(detailCache.getQuentity());
						detailCache.setAccountQuentity(NumberUtil.bigDecimalToStr(new BigDecimal((tempAccountQuentity+accountQuentity))));
						detailCache.setQuentity(NumberUtil.bigDecimalToStr(new BigDecimal((tempQuentity+quentity))));
					}else{
						WarehouseBillDetailVo detailCache = new WarehouseBillDetailVo();
						detailCache.setAccountQuentity(NumberUtil.bigDecimalToStr(new BigDecimal(detailVo.getAccountQuentity())));
						detailCache.setAccountUintID(detailVo.getAccountUintID());
						detailCache.setAccountUintName(detailVo.getAccountUintName());
						detailCache.setBarCode(detailVo.getBarCode());
						detailCache.setBillCode(detailVo.getBillCode());
						detailCache.setBillId(detailVo.getBillId());
						detailCache.setCostPrice(detailVo.getCostPrice());
						detailCache.setCreateTime(detailVo.getCreateTime());
						detailCache.setCreatorName(detailVo.getCreatorName());
						detailCache.setDetailType(detailVo.getDetailType());
						detailCache.setGoodsCode(detailVo.getGoodsCode());
						detailCache.setGoodsId(goodsId);
						detailCache.setGoodsName(detailVo.getGoodsName());
						detailCache.setLowestPrice(detailVo.getLowestPrice());
						detailCache.setQuentity(NumberUtil.bigDecimalToStr(new BigDecimal(detailVo.getQuentity())));
						detailCache.setScale(detailVo.getScale());
						detailCache.setUnitGroupId(detailVo.getUnitGroupId());
						detailCache.setUnitGroupName(detailVo.getUnitGroupName());
						detailCache.setUnitId(unitId);
						detailCache.setUnitName(detailVo.getUnitName());
						detailCache.setUpdateTime(detailVo.getUpdateTime());
						detailCache.setUnitPrice(detailVo.getUnitPrice());
						detailCache.setGoodsSpecId(goodsSpecId);
						detailCache.setGoodsSpecName(detailVo.getGoodsSpecName());;
						detailCache.setGoodsSpecGroupId(detailVo.getGoodsSpecGroupId());
						cache.put(key, detailCache);
					}
				}
			}
		}
		List<WarehouseBillDetailVo> cacheDetails =Lists.newArrayList(cache.values());
		if (cacheDetails != null && cacheDetails.size() > 0) {
			Collections.sort(cacheDetails, new Comparator<WarehouseBillDetailVo>(){

				@Override
				public int compare(WarehouseBillDetailVo o1, WarehouseBillDetailVo o2) {
					return o1.getGoodsCode().compareTo(o2.getGoodsCode());
				}
				
			});
			return cacheDetails;
		}
		
		return cacheDetails;
	}
}
