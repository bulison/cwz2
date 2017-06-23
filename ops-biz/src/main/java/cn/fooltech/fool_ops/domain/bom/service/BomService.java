package cn.fooltech.fool_ops.domain.bom.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.bom.entity.Bom;
import cn.fooltech.fool_ops.domain.bom.entity.BomDetail;
import cn.fooltech.fool_ops.domain.bom.repository.BomRepository;
import cn.fooltech.fool_ops.domain.bom.vo.BomDetailVo;
import cn.fooltech.fool_ops.domain.bom.vo.BomVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;

/**
 * 物料service
 * @author xjh
 *
 */
@Service
public class BomService extends BaseService<Bom, BomVo, String>{

	@Autowired
	private BomRepository bomRepo;
	
	/**
	 * 单据物料明细服务类
	 */
	@Autowired
	private BomDetailService detailService;
	
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

	/**
	 * 查询单据物料列表信息，按照单据物料主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<BomVo> query(BomVo bomVo,PageParamater pageParamater){
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		String goodsId = bomVo.getGoodsId();
		String goodsCode = bomVo.getGoodsCode();
		String goodsName = bomVo.getGoodsName();
		String creatorId = bomVo.getCreatorId();
		String creatorName = bomVo.getCreatorName();
		Short enable = bomVo.getEnable();
		
		return getPageVos(bomRepo.findPageBy(accId, goodsId, goodsCode, goodsName, 
				creatorId, creatorName, enable, pageRequest), pageRequest);
	}
	
	/**
	 * 新增/编辑单据物料
	 * @param vo
	 */
	public RequestResult save(BomVo vo) {
		
		RequestResult check = checkBeforeSave(vo);
		if(check.getReturnCode()==RequestResult.RETURN_FAILURE){
			return check;
		}
		 //检查是否同时修改的页面，提示退出或刷新再保存。
		if(!checkDataRealTime(vo)){
			RequestResult result = new RequestResult();
			result.setMessage("页面数据失效，请重新刷新页面!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		Bom entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new Bom();
			entity.setAccountQuentity(vo.getAccountQuentity());
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setUpdateTime(new Date());
		}else {
			entity = bomRepo.findOne(vo.getFid());
			entity.setUpdateTime(new Date());
		}
		
		Goods goods = goodsService.get(vo.getGoodsId());
		Unit accountUnit = unitService.get(vo.getAccountUnitId());
		
		entity.setAccountQuentity(vo.getAccountQuentity());
		entity.setDescribe(vo.getDescribe());
		entity.setGoods(goods);
		entity.setAccountUnit(accountUnit);
		
		if(!Strings.isNullOrEmpty(vo.getSpecId())){
			GoodsSpec spec = goodsSpecService.get(vo.getSpecId());
			if(spec!=null){
				entity.setSpec(spec);
			}
		}else{
			entity.setSpec(null);
		}
		bomRepo.save(entity);
		detailService.deleteByBomId(vo.getFid());
		detailService.save(vo.getDetailList(), entity);
		
		//校验数据
		RequestResult result = new RequestResult();
		Map<String, Object> map = Maps.newHashMap();
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(entity.getFid());
		result.setDataExt(map);
		return result;
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	public boolean checkDataRealTime(BomVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			Bom entity = bomRepo.findOne(vo.getFid());
			Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}
	
	/**
	 * 检查是否能新增或修改
	 * @return
	 */
	private RequestResult checkBeforeSave(BomVo vo){
		
		//验证字段
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		if(!Strings.isNullOrEmpty(vo.getFid())){
			Bom bom = bomRepo.findOne(vo.getFid());
			if(bom == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(bom.getEnable()==Bom.ENABLE){
				return new RequestResult(RequestResult.RETURN_FAILURE, "启用了不能修改");
			}
		}
		
		List<BomDetailVo> detailVos = vo.getDetailList();
		String goodsId = vo.getGoodsId();
		String specId = vo.getSpecId()==null?null:vo.getSpecId();
		String accId = SecurityUtil.getFiscalAccountId();
		
		for(BomDetailVo detailVo:detailVos){
			List<String> temp = Lists.newArrayList();
			if(findRecurse(accId, goodsId, specId, detailVo.getGoodsId(), detailVo.getSpecId(), temp)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "货品递归引用不能保存");
			}
			
			//验证字段
			inValid = ValidatorUtils.inValidMsg(detailVo);
			if(inValid!=null){
				return buildFailRequestResult(inValid);
			}
		}
		
		return new RequestResult();
	}
	
	/**
	 * 递归查找，存在递归引用返回true
	 * @return
	 */
	public boolean findRecurse(String accId, String goodsId, String specId, String detailGoodsId, String detailSpecId, List<String> detailIds){
		
		if(Strings.isNullOrEmpty(specId)){
			if(goodsId.equals(detailGoodsId)){
				return true;
			}
		}else{
			if(goodsId.equals(detailGoodsId) && specId.equals(detailSpecId)){
				return true;
			}
		}
		
		List<Bom> boms = bomRepo.findByGoodsId(accId, detailGoodsId);
		boolean flag = false;
		for(Bom bom:boms){
			Set<BomDetail> details = bom.getDetails();
			
			for(BomDetail detail:details){
				
				String tgoodsId = detail.getGoods().getFid();
				String tspecId = detail.getSpec()==null?null:detail.getSpec().getFid();
				
				for(String check:detailIds){
					Splitter splitter = Splitter.on(",").trimResults();
					List<String> checkList = splitter.splitToList(check);
					String checkGoodsId = checkList.get(0);
					String checkSpecId = checkList.get(1);
					
					if(Strings.isNullOrEmpty(checkSpecId) && Strings.isNullOrEmpty(detailSpecId)){
						if(checkGoodsId.equals(detailGoodsId))return true;
					}else if(!Strings.isNullOrEmpty(checkSpecId) && !Strings.isNullOrEmpty(detailSpecId)){
						if(checkGoodsId.equals(detailGoodsId) && checkSpecId.equals(detailSpecId))return true;
					}
				}
				
				List<String> newDetailIds = Lists.newArrayList(detailIds);
				newDetailIds.add(detailGoodsId+","+(detailSpecId==null?"":detailSpecId));
				
				if(Strings.isNullOrEmpty(specId)){
					if(tspecId==null && goodsId.equals(tgoodsId)){
						flag = true;
						break;
					}else{
						if(findRecurse(accId, goodsId, specId, tgoodsId, tspecId, newDetailIds)){
							flag = true;
							break;
						}
					}
				}else{
					if(goodsId.equals(tgoodsId) && specId.equals(tspecId)){
						flag = true;
						break;
					}else{
						if(findRecurse(accId, goodsId, specId, tgoodsId, tspecId, newDetailIds)){
							flag = true;
							break;
						}
					}
				}
			}
			if(flag)break;
		}
		return flag;
	}
	
	/**
	 * 删除单据物料<br>
	 */
	public RequestResult delete(String fid){
		
		Bom bom = bomRepo.findOne(fid);
		
		if(bom.getEnable()==Bom.ENABLE){
			return new RequestResult(RequestResult.RETURN_FAILURE, "启用了不能删除");
		}
		
		//TODO 判断是否被引用，被引用则不能删除
		// .....
		
		detailService.deleteByBomId(fid);
		bomRepo.delete(fid);
		return new RequestResult();
	}
	
	/**
	 * 启用
	 * @return
	 */
	public RequestResult updateEnable(String fid){
		
		Bom bom = bomRepo.findOne(fid);
		if(bom == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
		}
		
		if(bom.getEnable()==Bom.ENABLE){
			return new RequestResult(RequestResult.RETURN_FAILURE, "启用了不能删除");
		}
		String accId = SecurityUtil.getFiscalAccountId();
		String goodsId = bom.getGoods().getFid();
		String specId = bom.getSpec()==null?null:bom.getSpec().getFid();
		
		Long count = bomRepo.countSame(goodsId, specId, accId, fid);
		if(count==null || count==0){
			bom.setFdefault(Bom.DEFAULT);
		}
		bom.setEnable(Bom.ENABLE);
		bomRepo.save(bom);
		
		return new RequestResult();
	}
	
	/**
	 * 停用
	 * @return
	 */
	public RequestResult updateDisable(String fid){
		
		Bom bom = bomRepo.findOne(fid);
		if(bom == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
		}
		bom.setEnable(Bom.DISABLE);
		bom.setFdefault(Bom.NOT_DEFAULT);
		bomRepo.save(bom);
		
		return new RequestResult();
	}
	
	/**
	 * 设为默认
	 * @return
	 */
	public RequestResult updateDefault(String fid){
		
		Bom bom = bomRepo.findOne(fid);
		if(bom == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
		}
		if(bom.getEnable()==Bom.DISABLE){
			return new RequestResult(RequestResult.RETURN_FAILURE, "停用了不能设置默认");
		}
		
		String goodsId = bom.getGoods().getFid();
		String specId = bom.getSpec()==null?null:bom.getSpec().getFid();
		String accId = SecurityUtil.getFiscalAccountId();
		
		List<Bom> sameBoms = bomRepo.findByMaterial(accId, goodsId, specId, fid);
		for(Bom same:sameBoms){
			if(same.getFdefault()==Bom.DEFAULT){
				same.setFdefault(Bom.NOT_DEFAULT);
				bomRepo.save(same);
			}
		}
		
		bom.setFdefault(Bom.DEFAULT);
		bomRepo.save(bom);
		
		return new RequestResult();
	}
	
	@Override
	public BomVo getVo(Bom entity) {
		if(entity == null)
			return null;
		BomVo vo = new BomVo();
		vo.setAccountQuentity(NumberUtil.stripTrailingZeros(entity.getAccountQuentity()));
		vo.setEnable(entity.getEnable());
		vo.setFdefault(entity.getFdefault());
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		
		Goods goods = entity.getGoods();
		GoodsSpec spec = entity.getSpec();
		Unit accountUnit = entity.getAccountUnit();
		
		vo.setGoodsId(goods.getFid());
		vo.setGoodsName(goods.getName());
		vo.setGoodsCode(goods.getCode());
		
		vo.setAccountUnitId(accountUnit.getFid());
		vo.setAccountUnitName(accountUnit.getName());
		
		//单位组Id
		Unit unitGroup = goods.getUnitGroup();
		if(unitGroup != null){
			vo.setUnitGroupId(unitGroup.getFid());
		}
		
		//单位
		Unit unit = goods.getUnit();
		if(unit!=null){
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
		}
		
		//货品属性
		 GoodsSpec goodsSpec = goods.getGoodsSpec();
		 if(goodsSpec != null){
			 vo.setSpecGroupId(goodsSpec.getFid());
		 }
		
		if(spec!=null){
			vo.setSpecId(spec.getFid());
			vo.setSpecName(spec.getName());
		}
		
		List<BomDetailVo> details = detailService.getByBomId(entity.getFid());
		Collections.sort(details, new Comparator<BomDetailVo>(){
			@Override
			public int compare(BomDetailVo o1, BomDetailVo o2) {
				return o1.getGoodsCode().compareTo(o2.getGoodsCode());
			}
			
		});
		vo.setDetails(JSONArray.fromObject(details).toString());
		
		return vo;
	}

	@Override
	public CrudRepository<Bom, String> getRepository() {
		return bomRepo;
	}
	/**
	 * 根据goodsId、specId查找数据
	 * @return
	 */
	public List<Bom> findByMaterial(String selfId, String accId, String goodsId, String specId, Short fdefault){
		return bomRepo.findByMaterial(selfId, accId, goodsId, specId, fdefault);
	}

	/**
	 * 根据goodsId查找数据
	 * @return
	 */
	public  List<Bom> findByMaterial(String accId, String goodsId, Short fdefault){
		return bomRepo.findByMaterial(accId, goodsId,fdefault);
	}
}
