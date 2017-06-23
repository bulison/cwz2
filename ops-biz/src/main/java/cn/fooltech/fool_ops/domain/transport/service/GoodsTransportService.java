package cn.fooltech.fool_ops.domain.transport.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.basedata.vo.UnitVo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.repository.GoodsTransportRepository;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

import static cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport.SYS_SIGN_GEN;

/**
 * 货品运输计价换算关系 
 * @author cwz
 *
 */
@Service
public class GoodsTransportService extends BaseService<GoodsTransport, GoodsTransportVo, String>{

	@Autowired
	private GoodsTransportRepository repository;
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private UnitService unitService;
	
	@Override
	public GoodsTransportVo getVo(GoodsTransport entity) {
		if(entity==null) return null;
		GoodsTransportVo vo = new GoodsTransportVo();
		vo.setCreateTime(DateUtils.getDateString(entity.getCreateTime()));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		if(entity.getCreator()!=null){
			vo.setCreatorId(entity.getCreator().getFid());
			vo.setCreatorName(entity.getCreator().getUserName());
		}
		if(entity.getAccId()!=null){
			vo.setAccId(entity.getAccId().getFid());
			vo.setAccName(entity.getAccId().getName());
		}
		vo.setConversionRate(entity.getConversionRate());
		vo.setFid(entity.getFid());
		vo.setDescribe(entity.getDescribe());
		if(entity.getGoods()!=null){
			vo.setGoodsId(entity.getGoods().getFid());
			vo.setGoodsName(entity.getGoods().getName());
		}
		if(entity.getGoodSpec()!=null){
			vo.setGoodSpecId(entity.getGoodSpec().getFid());
			vo.setGoodSpecName(entity.getGoodSpec().getName());
		}
		if(entity.getShipmentType()!=null){
			vo.setShipmentTypeId(entity.getShipmentType().getFid());
			vo.setShipmentTypeName(entity.getShipmentType().getName());
		}
		if(entity.getTransportUnit()!=null){
			vo.setTransportUnitId(entity.getTransportUnit().getFid());
			vo.setTransportUnitName(entity.getTransportUnit().getName());
		}
		if(entity.getUnit()!=null){
			vo.setUnitId(entity.getUnit().getFid());
			vo.setUnitName(entity.getUnit().getName());
		}
		if(entity.getOrg()!=null){
			vo.setOrgId(entity.getOrg().getFid());
		}
		vo.setSysSign(entity.getSysSign());
		return vo;
	}

	@Override
	public CrudRepository<GoodsTransport, String> getRepository() {
		return repository;
	}
	
	/**
	 * 查询货品运输计价换算关系 <br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	public Page<GoodsTransportVo> query(GoodsTransportVo vo, PageParamater pageParamater) {
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater, sort);
		Page<GoodsTransport> query = repository.query(vo, request);
		Page<GoodsTransportVo> page = getPageVos(query, request);
		return page;
	} 
	/**
	 * 获取货品运输计价换算关系 信息
	 * @param id 货地址信ID
	 * @return
	 */
	public GoodsTransportVo getById(String id) {
		GoodsTransport entity = repository.findOne(id);
		return getVo(entity);
	}

	/**
	 * 新增/编辑货品运输计价换算关系 信息
	 * @param vo
	 */
	@Transactional
	public RequestResult save(GoodsTransportVo vo) {
//		新增、修改：判断货品ID+货品属性ID+装运方式ID+运输计价单位ID不能重复； 
		String inValid = ValidatorUtils.inValidMsg(vo);
		if (inValid != null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		String fid = vo.getFid();
		Date now = new Date();
		String voGoodsId = vo.getGoodsId();
		String voGoodSpecId = vo.getGoodSpecId()==null?"":vo.getGoodSpecId(); 
		String voShipmentTypeId = vo.getShipmentTypeId();
		String voTransportUnitId = vo.getTransportUnitId();
//		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		String orgId = SecurityUtil.getCurrentOrgId();
		Long count=0L;
		if(!Strings.isNullOrEmpty(voGoodSpecId)){
			 count = repository.findRepeat(voGoodsId, voGoodSpecId, voShipmentTypeId, voTransportUnitId,orgId);
		}else{
			count = repository.findRepeat(voGoodsId, voShipmentTypeId, voTransportUnitId,orgId);
		}
		
		GoodsTransport entity=null;
		if (StringUtils.isBlank(fid)) {
			entity=new GoodsTransport();
			entity.setAccId(SecurityUtil.getFiscalAccount());
			entity.setCreateTime(now);
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());

			if(count>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该货品已经存在相同属性+装运方式+运输单位的记录!");
			}
		} else {
			entity=repository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
//			修改：判断时间戳，如果不一致提示已被其他用户修改不能保存，要刷新数据
			if(entity.getUpdateTime().compareTo(DateUtils.getDateFromString(vo.getUpdateTime()))>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该页面已经失效,请刷新页面数据!");
			}
			if(count>0){
				Long count2=0L;
				if(!Strings.isNullOrEmpty(voGoodSpecId)){
					 count2 = repository.findRepeatById(voGoodsId, voGoodSpecId, voShipmentTypeId, voTransportUnitId,orgId,entity.getFid());
				}else{
					count2 = repository.findRepeatById(voGoodsId, voShipmentTypeId, voTransportUnitId,orgId,entity.getFid());
				}
				if(count2>0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "该货品已经存在相同属性+装运方式+运输单位的记录!");
				}
				
			}
			entity.setUpdateTime(now);
		}
		entity.setConversionRate(vo.getConversionRate());
		entity.setDescribe(vo.getDescribe());
		entity.setGoods(goodsService.get(voGoodsId));
		GoodsSpec goodsSpec = null;
		if(voGoodSpecId!=null){
			goodsSpec=goodsSpecService.get(voGoodSpecId);
		}
		entity.setGoodSpec(goodsSpec);
		entity.setShipmentType(attrService.get(voShipmentTypeId));
		entity.setTransportUnit(attrService.get(voTransportUnitId));
		entity.setUnit(unitService.get(vo.getUnitId()));
		entity.setUpdateTime(now);

		if(entity.getSysSign()==null||entity.getSysSign()==SYS_SIGN_GEN){
			entity.setSysSign(GoodsTransport.SYS_SIGN_USER);
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
		}

		try {
			repository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("保存失败："+e);
		}
		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 根据fid删除
	 */
	@Transactional
	public RequestResult delete(String fid){
		try {
			repository.delete(fid);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除失败："+e.getMessage());
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 根据货品ID+货品属性ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @return
	 */
	public GoodsTransport findGoodsTransport(String goodsId,String goodSpecId,String transportUnitId){
		if(Strings.isNullOrEmpty(goodSpecId)){
			return repository.findTopBy(goodsId, transportUnitId);
		}else{
			return repository.findTopBy(goodsId, goodSpecId, transportUnitId);
		}
	}

	/**
	 * 根据货品ID+货品属性ID查询换算关系不为1的记录
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @return
	 */
	public List<GoodsTransportVo> findByGoods(String goodsId,String goodSpecId){
		if(Strings.isNullOrEmpty(goodSpecId)){
			return getVos(repository.findByGoods(goodsId));
		}else{
			return getVos(repository.findByGoods(goodsId, goodSpecId));
		}
	}


	/**
	 * 根据货品ID，属性ID查询运输单位
	 * @param goodsId
	 * @param goodSpecId
	 * @return
	 */
	public List<AuxiliaryAttrVo> queryUnitVos(String goodsId, String goodSpecId){
		List<GoodsTransport> gtList = repository.queryList(goodsId, goodSpecId);
		Map<String, AuxiliaryAttrVo> map = Maps.newLinkedHashMap();

		for(GoodsTransport gt:gtList){
			AuxiliaryAttr attr = gt.getTransportUnit();
			AuxiliaryAttrVo vo = attrService.getVo(attr);
			vo.setScale(gt.getConversionRate());
			map.put(attr.getFid(), vo);
		}
		return Lists.newArrayList(map.values());
	}
	
	/**
	 * 根据货品ID+货品属性ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
	 */
	public GoodsTransportVo findTopBySpec(String goodsId,String goodSpecId,String transportUnitId, String shipmentTypeId){
		GoodsTransport goodsTransport = null;
		if(Strings.isNullOrEmpty(goodSpecId)){
			goodsTransport = repository.findTopBySpecIsNull(goodsId, transportUnitId, shipmentTypeId);
		}else{
			goodsTransport = repository.findTopBySpecNotNull(goodsId, goodSpecId, transportUnitId, shipmentTypeId);
		}
		if(goodsTransport!=null){
			GoodsTransportVo vo = getVo(goodsTransport);
			return vo;
		}
		return null;
	}
}
