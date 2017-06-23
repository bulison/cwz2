package cn.fooltech.fool_ops.domain.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplate;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 运输费报价模板
 * 
 * @author cwz
 * @date 2016-12-8
 */
@Service
public class TransportTemplateService extends BaseService<TransportTemplate, TransportTemplateVo, String> {

	@Autowired
	private TransportTemplateRepository repository;
	@Autowired
	private TransportTemplateDetail1Service detail1Service;
	@Autowired
	private TransportTemplateDetail2Service detail2Service;
	// 收货地址 业务层
	@Autowired
	private FreightAddressService addressService;
	// 辅助属性持久层
	@Autowired
	private AuxiliaryAttrRepository attrRepository;

	@Override
	public TransportTemplateVo getVo(TransportTemplate entity) {
		// TransportTemplateVo vo =
		// VoFactory.createValue(TransportTemplateVo.class, entity);
		TransportTemplateVo vo = new TransportTemplateVo();
		vo.setId(entity.getId());
		vo.setName(entity.getName());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		vo.setCode(entity.getCode());
		vo.setEnable(entity.getEnable());
		vo.setDescribe(entity.getDescribe());
		vo.setExpectedDays(entity.getExpectedDays());
		FreightAddress deliveryPlace = entity.getDeliveryPlace();
		if (deliveryPlace != null) {
			vo.setDeliveryPlaceFid(deliveryPlace.getFid());
			vo.setDeliveryPlaceName(deliveryPlace.getName());
		}
		FreightAddress receiptPlace = entity.getReceiptPlace();
		if (receiptPlace != null) {
			vo.setReceiptPlaceFid(receiptPlace.getFid());
			vo.setReceiptPlaceName(receiptPlace.getName());
		}
		AuxiliaryAttr attr = entity.getTransportType();
		if (attr != null) {
			vo.setTransportTypeFid(attr.getFid());
			vo.setTransportTypeName(attr.getName());
		}
		AuxiliaryAttr attr2 = entity.getShipmentType();
		if (attr2 != null) {
			vo.setShipmentTypeFid(attr2.getFid());
			vo.setShipmentTypeName(attr2.getName());
		}
		// 组织机构
		Organization org = entity.getOrg();
		if (org != null) {
			vo.setOrgId(org.getFid());
			vo.setOrgName(org.getOrgName());
		}
		FiscalAccount fiscalAccount = entity.getFiscalAccount();
		if (fiscalAccount != null) {
			vo.setFiscalAccountId(fiscalAccount.getFid());
			vo.setFiscalAccountName(fiscalAccount.getName());
		}
		User user = entity.getCreator();
		if (user != null) {
			vo.setCreatorId(user.getFid());
			vo.setCreatorName(user.getUserName());
		}
		return vo;
	}

	@Override
	public CrudRepository<TransportTemplate, String> getRepository() {
		return repository;
	}

	/**
	 * 运输费报价查找分页
	 * 
	 * @param groundId
	 * @param paramater
	 * @return
	 */
	public Page<TransportTemplateVo> query(TransportTemplateVo vo, PageParamater paramater) {
		Sort sort = new Sort(Sort.Direction.ASC, "code");
		PageRequest request = getPageRequest(paramater, sort);
		Page<TransportTemplate> page = repository.findPageBy(vo, request);
		Page<TransportTemplateVo> pageVos = getPageVos(page, request);
		// List<TransportTemplate> content = page.getContent();
		// List<TransportTemplateVo> vos = getVos(content);
		return pageVos;
	}

	/**
	 * 保存运输费报价模板
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(TransportTemplateVo vo) {
		try {
			String accountId = SecurityUtil.getFiscalAccountId();
			TransportTemplate info = null;
			Date now = new Date();
			Long codeCount = repository.queryByCodeCount(vo.getCode(), accountId);
			Long nameCount = repository.queryByNameCount(vo.getName(), accountId);
			String transportTypeFid = vo.getTransportTypeFid();	//运输方式
			String shipmentTypeFid = vo.getShipmentTypeFid();	//装运方式
			String deliveryPlaceFid = vo.getDeliveryPlaceFid(); //发货地
			String receiptPlaceFid = vo.getReceiptPlaceFid();	//收货地
			//保存时检查收发货地是否存在同一个组下。
			FreightAddressVo deliveryPlace = addressService.queryFullParentById(deliveryPlaceFid);
			FreightAddressVo receiptPlace = addressService.queryFullParentById(receiptPlaceFid);
			if(deliveryPlace.getFid().equals(receiptPlace.getFid()))return new RequestResult(RequestResult.RETURN_FAILURE, "发货地址跟收货地址不能在同组内互发");
			Long typeCount = repository.queryByTypeCount(transportTypeFid, shipmentTypeFid, deliveryPlaceFid,receiptPlaceFid, accountId);
			if (StringUtils.isBlank(vo.getId())) {
				info = new TransportTemplate();
				info.setCreateTime(now);
				info.setFiscalAccount(SecurityUtil.getFiscalAccount());
				info.setCreator(SecurityUtil.getCurrentUser());
				info.setOrg(SecurityUtil.getCurrentOrg());
				info.setUpdateTime(now);
				if (codeCount > 0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "编号已存在!");
				}
				if (nameCount > 0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "名称已存在!");
				}
				if (typeCount > 0 && vo.getEnable() == 1) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用!");
				}
			} else {
				info = repository.findOne(vo.getId());
				if (!checkUpdateTime(vo.getUpdateTime(), info.getUpdateTime())) {
					return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
				}
				if (codeCount > 0 && !info.getCode().equals(vo.getCode())) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "编号已存在!");
				}
				if (nameCount > 0 && !info.getName().equals(vo.getName())) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "名称已存在!");
				}
//				String ttFid = info.getTransportType().getFid();
//				String stFid = info.getShipmentType().getFid();
//				String dpFid = info.getDeliveryPlace().getFid();
//				String rpFid = info.getReceiptPlace().getFid();
//				if (typeCount > 0 && !ttFid.equals(transportTypeFid) && !stFid.equals(shipmentTypeFid)
//						&& !dpFid.equals(deliveryPlaceFid) && !rpFid.equals(receiptPlaceFid) && vo.getEnable() == 1) {
//					return new RequestResult(RequestResult.RETURN_FAILURE, "运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用!");
//				}
				if (typeCount > 0) {
					Long typeCount2 = repository.queryByTypeCount(transportTypeFid, shipmentTypeFid, deliveryPlaceFid,
							receiptPlaceFid, accountId,vo.getId());
					if(typeCount2>0&&vo.getEnable()==1){
						return new RequestResult(RequestResult.RETURN_FAILURE, "运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用!");
					}
				}
			}
			info.setCode(vo.getCode());
			if (StringUtils.isNotBlank(vo.getDeliveryPlaceFid())) {
				info.setDeliveryPlace(addressService.findOne(vo.getDeliveryPlaceFid()));
			}
			info.setDescribe(vo.getDescribe());
			info.setEnable(vo.getEnable());
			info.setExpectedDays(vo.getExpectedDays());
			info.setName(vo.getName());
			if (StringUtils.isNotBlank(vo.getReceiptPlaceFid())) {
				info.setReceiptPlace(addressService.findOne(vo.getReceiptPlaceFid()));
			}
			if (StringUtils.isNotBlank(vo.getShipmentTypeFid())) {
				info.setShipmentType(attrRepository.findOne(vo.getShipmentTypeFid()));
			}
			if (StringUtils.isNotBlank(vo.getTransportTypeFid())) {
				info.setTransportType(attrRepository.findOne(vo.getTransportTypeFid()));
			}
			//先判断明细项。
			//从1表不能为空，从1表有数据，从2表必须要有对应明细数据
			List<TransportTemplateDetail1Vo> list=Lists.newArrayList();
			try {
				list = detail1Service.getDetails(vo.getDetails1());
			} catch (Exception e1) {
				e1.printStackTrace();
				return buildFailRequestResult("请填写模版明细!");
			}
			Map<String, List<TransportTemplateDetail2Vo>> map;
			String code = "";
				map = detail2Service.getDetails(vo.getDetails2());
				if (Strings.isNullOrEmpty(vo.getDetails2())){
					return buildFailRequestResult("运输费用不能为空！");
				}
				RequestResult detailsResult = detail1Service.checkAddress(list, info);
				if (detailsResult.getReturnCode() == 1){
					return detailsResult;
				}
				for (TransportTemplateDetail1Vo transportTemplateDetail1Vo : list) {
					List<TransportTemplateDetail2Vo> list2 = map.get(transportTemplateDetail1Vo.getCode());
					if (list2 == null || list2.size() < 1){
						code = transportTemplateDetail1Vo.getCode();
						break;
					}
				}
				if(!Strings.isNullOrEmpty(code)){
					return buildFailRequestResult(code+":运输费不能为空!");
				}
			info.setUpdateTime(now);
			// 保存主表数据,先删除从表的记录
			repository.save(info);
			if (!Strings.isNullOrEmpty(info.getId())) {
				detail1Service.delByTempId(info.getId());
				detail2Service.delByTempId(info.getId());
			}
			// 保存从1表数据
			for (TransportTemplateDetail1Vo transportTemplateDetail1Vo : list) {
				if (Strings.isNullOrEmpty(transportTemplateDetail1Vo.getTransportTypeFid())){
					continue;
				}
				transportTemplateDetail1Vo.setTemplateFid(info.getId());
				transportTemplateDetail1Vo.setShipmentTypeFid(info.getShipmentType().getFid());
				RequestResult save = detail1Service.save(transportTemplateDetail1Vo);
				if (save.getReturnCode() == 1){
					return save;
				}
				// 保存从2表数据
				TransportTemplateDetail1 data = (TransportTemplateDetail1) save.getData();
				List<TransportTemplateDetail2Vo> list2 = map.get(data.getCode());
				for (TransportTemplateDetail2Vo transportTemplateDetail2Vo : list2) {
					transportTemplateDetail2Vo.setTemplateFid(info.getId());
					transportTemplateDetail2Vo.setDetail1Fid(data.getId());
					RequestResult save2 = detail2Service.save(transportTemplateDetail2Vo);
					if (save.getReturnCode() == 1)
						return save2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("保存运输费报价模板失败！");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 启用/停用运输费报价模板
	 * 
	 * @param id
	 *            编号
	 * @param enable
	 *            状态
	 * @return
	 */
	@Transactional
	public RequestResult changeEnable(String id, int enable) {
		try {
			TransportTemplate template = repository.findOne(id);
			String transportTypeFid = template.getTransportType().getFid();
			String shipmentTypeFid = template.getShipmentType().getFid();
			String deliveryPlaceFid = template.getDeliveryPlace().getFid();
			String receiptPlaceFid = template.getReceiptPlace().getFid();
			Long typeCount = repository.queryByTypeCount(transportTypeFid, shipmentTypeFid, deliveryPlaceFid,
					receiptPlaceFid, SecurityUtil.getFiscalAccountId());
			if (typeCount >= 1 && enable == 1) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用!");
			}
			repository.updateEnable(enable, id);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("启用/停用操作失败!");
		}
		return buildSuccessRequestResult("操作成功!");
	}

	/**
	 * 根据用户选择运输方式和装运方式，查找运输费报价模板
	 * 
	 * @param transportTypeId 	运输方式
	 * @param shipmentTypeId 	装运方式
	 * @param deliveryPlaceId	发货地
	 * @param receiptPlaceId	收货地
	 *            
	 * @return
	 */
	public List<TransportTemplateVo> findByTemp(String transportTypeId, String shipmentTypeId, String deliveryPlaceId,
			String receiptPlaceId) {
		List<TransportTemplate> list = repository.findByTemp(transportTypeId, shipmentTypeId, deliveryPlaceId,receiptPlaceId, SecurityUtil.getFiscalAccountId());
		//如果找不到模板，则分别查找发货地、收货地的上级地址，是否存在运输报价模板（先从收货地查找，找到最上级为止），
		//如果找到运输报价模板，则引用该模板数据，如果还找不到模板，提示用户设置模板；
		if(list==null||list.size()==0){
			//收货地查找
			List<TransportTemplateVo> templates = findTempByAddress(transportTypeId, shipmentTypeId, deliveryPlaceId, receiptPlaceId,1);
			if(templates!=null&&templates.size()>0){
				return templates;
			}else{
				//发货地
				List<TransportTemplateVo> templates2 = findTempByAddress(transportTypeId, shipmentTypeId, deliveryPlaceId, receiptPlaceId,2);
				if(templates2!=null&&templates2.size()>0){
					return templates2;
				}else{
					return Lists.newArrayList();
				}
				
			}
		}else{
			List<TransportTemplateVo> vos = getVos(list);
			return vos;
		}

	}
	
	public List<TransportTemplateVo> findTempByAddress(String transportTypeId, String shipmentTypeId,
			String deliveryPlaceId, String receiptPlaceId,int type) {
		FreightAddress address=null;
		if(type==1){
			address = addressService.findOne(receiptPlaceId);
		}else{
			address = addressService.findOne(deliveryPlaceId);
		}
		if (address != null) {
			String fullParentId = address.getFullParentId();
	        List<String> excludeIds = null;
	        if (!Strings.isNullOrEmpty(fullParentId)) {
	            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
	            excludeIds = splitter.splitToList(fullParentId);
	            List<TransportTemplate> findByTemp=null;
	            for (String parentId : excludeIds) {
	            	if(type==1){
	            		findByTemp = repository.findByTemp(transportTypeId, shipmentTypeId, deliveryPlaceId, parentId,SecurityUtil.getFiscalAccountId());
	            	}else{
	            		findByTemp = repository.findByTemp(transportTypeId, shipmentTypeId, parentId, receiptPlaceId,SecurityUtil.getFiscalAccountId());
	            	}
	            	if(findByTemp!=null){
	            		 return getVos(findByTemp);
	            	}
	            }
	           
	        }

		}
		return Lists.newArrayList() ;
	}
	/**
	 * 根据发货地查询记录
	 * 
	 * @param fid
	 * @return
	 */
	public Long queryByDeliveryPlaceCount(String fid) {
		return repository.queryByDeliveryPlaceCount(fid, SecurityUtil.getFiscalAccountId());
	};

	/**
	 * 根据收货地查询记录
	 * 
	 * @param fid
	 * @return
	 */
	public Long queryByReceiptPlaceCount(String fid) {
		return repository.queryByReceiptPlaceCount(fid, SecurityUtil.getFiscalAccountId());
	};
	
	/**
	 * 删除记录，先删从1，从2表记录，然后删除主表
	 */
	@Transactional
	public RequestResult delete(String id){
		try {
			detail1Service.delByTempId(id);
			detail2Service.delByTempId(id);
			repository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除运输费报价模版出错!");
		}
		return buildSuccessRequestResult();
	}

}
