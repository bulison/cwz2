package cn.fooltech.fool_ops.domain.bom.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.bom.entity.Bom;
import cn.fooltech.fool_ops.domain.bom.entity.BomDetail;
import cn.fooltech.fool_ops.domain.bom.repository.BomDetailRepository;
import cn.fooltech.fool_ops.domain.bom.repository.BomRepository;
import cn.fooltech.fool_ops.domain.bom.vo.BomDetailVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 物料service
 * @author xjh
 *
 */
@Service
public class BomDetailService extends BaseService<BomDetail, BomDetailVo, String>{
	
	@Autowired
	private BomDetailRepository detailRepo;
	
	@Autowired
	private BomRepository bomRepo;
	
	/**
	 * 单位服务类
	 */
	@Autowired
	private UnitService unitService;
	
	/**
	 * 货品服务类
	 */
	@Autowired
	protected GoodsService goodsService;
	
	/**
	 * 货品属性服务类
	 */
	@Autowired
	private GoodsSpecService goodsSpecService;


	@Override
	public CrudRepository<BomDetail, String> getRepository() {
		return null;
	}

	@Override
	public BomDetailVo getVo(BomDetail entity) {
		if(entity == null)
			return null;
		BomDetailVo vo = new BomDetailVo();
		vo.setQuentity(entity.getQuentity());
		vo.setAccountQuentity(entity.getAccountQuentity());
		vo.setDescribe(entity.getDescribe());
		vo.setFid(entity.getFid());
		
		Goods goods = entity.getGoods();
		GoodsSpec spec = entity.getSpec();
		Unit unit = entity.getUnit();
		Unit accountUnit = entity.getAccountUnit();
		Unit unitGroup = accountUnit.getParent();
		
		vo.setGoodsId(goods.getFid());
		vo.setGoodsName(goods.getName());
		vo.setGoodsCode(goods.getCode());
		
		vo.setUnitId(unit.getFid());
		vo.setUnitName(unit.getName());
		
		vo.setUnitGroupId(unitGroup.getFid());
		vo.setUnitGroupName(unitGroup.getName());
		
		vo.setAccountUnitId(accountUnit.getFid());
		vo.setAccountUnitName(accountUnit.getName());
		
		if(spec!=null){
			vo.setSpecId(spec.getFid());
			vo.setSpecName(spec.getName());
			
			GoodsSpec group = spec.getParent();
			if(group!=null){
				vo.setGoodsSpecGroupId(group.getFid());
			}
		}
		
		return vo;
	}
	
	/**
	 * 根据BomId获取
	 */
	public List<BomDetailVo> getByBomId(String bomId){
		return getVos(detailRepo.findByBomId(bomId));
	}

	/**
	 * 根据BomId删除
	 */
	public void deleteByBomId(String bomId){
		List<BomDetail> details = detailRepo.findByBomId(bomId);
		detailRepo.delete(details);
	}
	
	/**
	 * 根据物料查询物料组成明细<br>
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	public List<BomDetailVo> queryDefaultBomDetails(String goodsId, String specId){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Bom defaultBom = bomRepo.findTopByDefaultMaterial(accId, goodsId, specId);
		if(defaultBom == null) {
			defaultBom = bomRepo.findTopByDefaultMaterial(accId, goodsId, null);
		}
		if(defaultBom==null)return Collections.EMPTY_LIST;
		List<BomDetail> details = detailRepo.findByBomId(defaultBom.getFid());
		
		return getVos(details);
	}
	
	/**
	 * 新增/编辑单据物料明细
	 * @param vo
	 */
	public void save(List<BomDetailVo> vos, Bom bom) {
		
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		for(BomDetailVo vo:vos){
			
			BomDetail entity = new BomDetail();
			
			
			entity.setFiscalAccount(fiscalAccount);
			entity.setOrg(org);
			entity.setCreateTime(Calendar.getInstance().getTime());
			
			Goods goods = goodsService.get(vo.getGoodsId());
			Unit unit = unitService.get(vo.getUnitId());
			
			entity.setQuentity(vo.getQuentity());
			entity.setDescribe(vo.getDescribe());
			entity.setGoods(goods);
			entity.setUnit(unit);
			entity.setBom(bom);
			
			Unit accountUnit = unitService.findAccountUnit(unit.getParent().getFid());
			
			entity.setAccountUnit(accountUnit);
			entity.setAccountQuentity(vo.getQuentity().multiply(unit.getScale()));
			
			if(!Strings.isNullOrEmpty(vo.getSpecId())){
				GoodsSpec spec = goodsSpecService.get(vo.getSpecId());
				if(spec!=null){
					entity.setSpec(spec);
				}
			}else{
				entity.setSpec(null);
			}
			detailRepo.save(entity);
		}
	}
}
