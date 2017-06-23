package cn.fooltech.fool_ops.domain.report.service;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.repository.GoodsStockRepository;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>货品库存</p>
 * @author xjh
 * @version 1.0
 * @date 2015年10月13日
 */
@Service
public class GoodsStockService {

	@Autowired
	private GoodsStockRepository goodsStockRepo;
	
	/**
	 * 获取列表
	 */
	public List<GoodsStockVo> getList(GoodsStockVo vo, PageParamater paramater) throws Exception {

		String periodId = vo.getPeriodId();
		if(StringUtils.isBlank(periodId)){
			return Collections.EMPTY_LIST;
		}
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		String warehouseId = vo.getWarehouseId();
		String goodsId = vo.getGoodsId();
		String specId = vo.getSpecId();
		Integer calTotal = vo.getCalTotal();
		
		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		
		return getVos(goodsStockRepo.queryGoodsStock(periodId, orgId, accId, warehouseId, 
				goodsId, specId, calTotal, first, pageSize));
	}
	
	public long countList(GoodsStockVo vo){
		String periodId = vo.getPeriodId();
		if(StringUtils.isBlank(periodId)){
			return 0;
		}
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		String warehouseId = vo.getWarehouseId();
		String goodsId = vo.getGoodsId();
		String specId = vo.getSpecId();
		Integer calTotal = vo.getCalTotal();
		
		return goodsStockRepo.countGoodsStock(periodId, orgId, accId, warehouseId, goodsId, 
				specId, calTotal);
	}

	/**
	 * 获得Vo的list
	 */
	public List<GoodsStockVo> getVos(List<Object[]> list) {
		List<GoodsStockVo> vos = Lists.newArrayList();
		if (list != null){
			for (Object[] objects : list) {
				GoodsStockVo vo = getVo(objects);
				if(vo!=null){
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 获得Vo
	 */
	public GoodsStockVo getVo(Object[] obj) {
		if (obj == null)
			return null;
		GoodsStockVo vo = new GoodsStockVo();
		try {
			vo.setWarehouseId(obj[1]!=null?obj[1].toString():"");
			vo.setWarehouseName(obj[2]!=null?obj[2].toString():"");
			vo.setGoodsId(obj[3]!=null?obj[3].toString():"");
			vo.setGoodsCode(obj[4]!=null?obj[4].toString():"");
			vo.setGoodsName(obj[5]!=null?obj[5].toString():"");
			vo.setGoodsSpec(obj[6]!=null?obj[6].toString():"");
			vo.setSpecId(obj[7]!=null?obj[7].toString():"");
			vo.setSpec(obj[8]!=null?obj[8].toString():"");
			vo.setAccountUnit(obj[9]!=null?obj[9].toString():"");
			vo.setLastAccountQuentity(NumberUtil.stripTrailingZeros(obj[10], 2));
			vo.setLastAccountAmount(NumberUtil.stripTrailingZeros(obj[11], 2));
			vo.setInQuentity(NumberUtil.stripTrailingZeros(obj[12], 2));
			vo.setInAmount(NumberUtil.stripTrailingZeros(obj[13], 2));
			vo.setOutQuentity(NumberUtil.stripTrailingZeros(obj[14], 2));
			vo.setOutAmount(NumberUtil.stripTrailingZeros(obj[15], 2));
			vo.setPrefitQuentity(NumberUtil.stripTrailingZeros(obj[16], 2));
			vo.setPrefitAmount(NumberUtil.stripTrailingZeros(obj[17], 2));
			vo.setAccountQuentity(NumberUtil.stripTrailingZeros(obj[18], 2));
			vo.setAccountAmount(NumberUtil.stripTrailingZeros(obj[19], 2));
			vo.setPeriodId(obj[20]!=null?obj[20].toString():"");
			vo.setMoveIn(NumberUtil.stripTrailingZeros(obj[21], 2));
			vo.setMoveOut(NumberUtil.stripTrailingZeros(obj[22], 2));
			vo.setUnitPrice(NumberUtil.stripTrailingZeros(obj[24], 2));
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取详情列表
	 */
	public List<GoodsStockDetailVo> getDetailList(GoodsStockVo vo,
			PageParamater paramater) {
		String periodId = vo.getPeriodId();
		if(StringUtils.isBlank(periodId)){
			return Collections.EMPTY_LIST;
		}
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		String warehouseId = vo.getWarehouseId();
		String goodsId = vo.getGoodsId();
		String specId = vo.getSpecId();
		Integer calTotal = vo.getCalTotal();
		
		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		
		return getDetailVos(goodsStockRepo.queryDetail(periodId, orgId, accId, warehouseId, goodsId, 
				specId, calTotal, first, pageSize));
	}
	
	/**
	 * 转换详情VO列表
	 */
	private List<GoodsStockDetailVo> getDetailVos(List<Object[]> detailList) {
		List<GoodsStockDetailVo> detailVos = Lists.newArrayList();
		if (detailList != null){
			for (Object[] objects : detailList) {
				GoodsStockDetailVo vo = getDetailVo(objects);
				if(vo!=null){
					detailVos.add(vo);
				}
			}
		}
		return detailVos;
	}

	/**
	 * 转换详情VO
	 */
	private GoodsStockDetailVo getDetailVo(Object[] data) {
		if (data == null)
			return null;
		GoodsStockDetailVo detail = new GoodsStockDetailVo();
		try {
			detail.setDate(data[1].toString());
			detail.setCode(data[2].toString());
			detail.setBillType(data[3].toString());
			detail.setQuentity(NumberUtil.stripTrailingZeros(data[4].toString(),2));
			detail.setUnitName(data[5].toString());
			detail.setAmount(NumberUtil.stripTrailingZeros(data[6].toString(),2));
			detail.setCostAmount(NumberUtil.stripTrailingZeros(data[7].toString(),2));
			detail.setType((Integer) data[8]);
			detail.setMoveOut((Short) data[14]);
			
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long countDetailList(GoodsStockVo vo) {
		String periodId = vo.getPeriodId();
		if(StringUtils.isBlank(periodId)){
			return 0L;
		}
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		String warehouseId = vo.getWarehouseId();
		String goodsId = vo.getGoodsId();
		String specId = vo.getSpecId();
		Integer calTotal = vo.getCalTotal();
		
		return goodsStockRepo.countDetail(periodId, orgId, accId, warehouseId, goodsId, 
				specId, calTotal);
	}
}
