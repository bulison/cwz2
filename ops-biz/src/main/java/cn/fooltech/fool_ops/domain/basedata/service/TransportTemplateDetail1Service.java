package cn.fooltech.fool_ops.domain.basedata.service;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplate;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateDetail1Repository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateDetail2Repository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail1Vo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
/**
 * 运输费报价模板(从1表)
 * @author cwz
 * @date   2016-12-8
 */
@Service
public class TransportTemplateDetail1Service extends BaseService<TransportTemplateDetail1, TransportTemplateDetail1Vo, String> {


    @Autowired
    private TransportTemplateDetail1Repository repository;
    @Autowired
    private TransportTemplateDetail2Repository detail2;
    
    @Autowired
    private TransportTemplateRepository templateRepository;
	//收货地址 持久层
    @Autowired
    private FreightAddressRepository addressRepository;
    //辅助属性持久层
    @Autowired
    private AuxiliaryAttrRepository attrRepository;
    
    @Override
    public TransportTemplateDetail1Vo getVo(TransportTemplateDetail1 entity) {
//        TransportTemplateDetail1Vo vo = VoFactory.createValue(TransportTemplateDetail1Vo.class, entity);
        TransportTemplateDetail1Vo vo = new TransportTemplateDetail1Vo();
        vo.setFid(entity.getId());
        vo.setDescribe(entity.getDescribe());
        vo.setExpectedDays(entity.getExpectedDays());
        vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        vo.setCode(entity.getCode());
        FreightAddress deliveryPlace = entity.getDeliveryPlace();
        if(deliveryPlace!=null){
        	vo.setDeliveryPlaceFid(deliveryPlace.getFid());
        	vo.setDeliveryPlaceName(deliveryPlace.getName());
        }
        FreightAddress receiptPlace = entity.getReceiptPlace();
        if(receiptPlace!=null){
        	vo.setReceiptPlaceFid(receiptPlace.getFid());
        	vo.setReceiptPlaceName(receiptPlace.getName());
        }
        TransportTemplate template = entity.getTemplate();
        if(template!=null){
        	vo.setTemplateFid(template.getId());
        	vo.setTemplateName(template.getName());
        }
        AuxiliaryAttr transportType = entity.getTransportType();
        if(transportType!=null){
        	vo.setTransportTypeFid(transportType.getFid());
        	vo.setTransportTypeName(transportType.getName());
        }
        AuxiliaryAttr attr2 = entity.getShipmentType();
        if(attr2!=null){
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
        if(fiscalAccount!=null){
        	vo.setFiscalAccountId(fiscalAccount.getFid());
        	vo.setFiscalAccountName(fiscalAccount.getName());
        }
        User user = entity.getCreator();
        if(user!=null){
        	vo.setCreatorId(user.getFid());
        	vo.setCreatorName(user.getUserName());
        }
        return vo;
    }

    @Override
    public CrudRepository<TransportTemplateDetail1, String> getRepository() {
        return repository;
    }    
    /**
     * 运输费报价查找分页
     * @param groundId
     * @param paramater
     * @return
     */
    public Page<TransportTemplateDetail1Vo> query(TransportTemplateDetail1Vo vo, PageParamater paramater) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        PageRequest request = getPageRequest(paramater,sort);
        Page<TransportTemplateDetail1> page = repository.findPageBy(vo, request);
        Page<TransportTemplateDetail1Vo> pageVos = getPageVos(page, request);
        return pageVos;
    }
    
    /**
     * 保存运输费报价模板从1
     * @param vo
     * @return
     */
    @Transactional
	public RequestResult save(TransportTemplateDetail1Vo vo) {
    	TransportTemplateDetail1 info = new TransportTemplateDetail1();
    	//先删除，后添加（从2表有数据先删除从2数据）
//    	if(!Strings.isNullOrEmpty(vo.getFid())){
//    		detail2.delByDetail1(vo.getFid());
//    		repository.delete(vo.getFid());
//    		vo.setFid(null);
//    	}
    	Date now = new Date();
    	info.setCreateTime(now);
    	info.setCreator(SecurityUtil.getCurrentUser());
    	if(!Strings.isNullOrEmpty(vo.getDeliveryPlaceFid())){
    		info.setDeliveryPlace(addressRepository.findOne(vo.getDeliveryPlaceFid()));
    	}
    	info.setDescribe(vo.getDescribe());
    	info.setExpectedDays(vo.getExpectedDays());
    	info.setFiscalAccount(SecurityUtil.getFiscalAccount());
    	info.setOrg(SecurityUtil.getCurrentOrg());
    	if(!Strings.isNullOrEmpty(vo.getReceiptPlaceFid())){
    		info.setReceiptPlace(addressRepository.findOne(vo.getReceiptPlaceFid()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getTransportTypeFid())){
    		info.setTransportType(attrRepository.findOne(vo.getTransportTypeFid()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getTemplateFid())){
    		info.setTemplate(templateRepository.findOne(vo.getTemplateFid()));
    	}
    	if(StringUtils.isNotBlank(vo.getShipmentTypeFid())){
    		info.setShipmentType(attrRepository.findOne(vo.getShipmentTypeFid()));
    	}
    	info.setUpdateTime(now);
    	info.setCode(vo.getCode());
    	//保存主表数据
        repository.save(info);
        return buildSuccessRequestResult(info);
    }
    /**
     * 删除运输费报价模板（从1表）
     * @param id		编号
     * @param enable	状态
     * @return
     */
    @Transactional
    public RequestResult delete(String id) {
    	try {
			repository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除运输费报价模板操作失败!");
		}
        return buildSuccessRequestResult();
    }
    
    /**
     * 判断从表发/收货地址格式是否有误
     * @param details1
     * @param template
     * @return
     */
    public RequestResult checkAddress(List<TransportTemplateDetail1Vo> details1,TransportTemplate template){
//    	从表1的发货地址、收货地址默认出主表的发货地址、收货地址，
//    	提交保存时判断第一条记录的发货地址等于主表的发货地址，
//    	最后一条记录的收货地址=主表的收货地址，除第一条记录外，每条记录的发货地址等于上一条记录的收货地址；
    	//发货地址
    	String teDeliveryPlaceFid = template.getDeliveryPlace().getFid();
    	//收货地址
    	String teReceiptPlacefid = template.getReceiptPlace().getFid();
    	try {
			if(details1.size()==1){
				String deliveryPlaceFid = details1.get(0).getDeliveryPlaceFid();
				String receiptPlaceFid = details1.get(0).getReceiptPlaceFid();
				//判断第一条记录的发货地址等于主表的发货地址，最后一条记录的收货地址=主表的收货地址
				if(!teDeliveryPlaceFid.equals(deliveryPlaceFid)&&!teReceiptPlacefid.equals(receiptPlaceFid)){
					return buildFailRequestResult("第一条记录的发货地址等于主表的发货地址，最后一条记录的收货地址=主表的收货地址!");
				}
			}else{
				for (int i = 0; i < details1.size(); i++) {
					TransportTemplateDetail1Vo vo = details1.get(0);
					TransportTemplateDetail1Vo vo2 = details1.get(details1.size()-1);
					if(i==0&&!vo.getDeliveryPlaceFid().equals(teDeliveryPlaceFid)){
						return buildFailRequestResult("从表第一条数据发货地址必须等于主表发货地址!");
					}
					//除第一条记录外，每条记录的发货地址等于上一条记录的收货地址；
					if(i>0){
						if(!details1.get(i).getDeliveryPlaceFid().equals(details1.get(i-1).getReceiptPlaceFid())){
							return buildFailRequestResult("除第一条记录外，每条记录的发货地址等于上一条记录的收货地址!");
						}
					}
					if(i==details1.size()&&!vo2.getReceiptPlaceFid().equals(teReceiptPlacefid)){
						return buildFailRequestResult("从表最后一条数据收货地址必须等于主表收货地址!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    	return buildSuccessRequestResult();
    }
	/**
	 * josn转换List<TransportTemplateDetail1Vo>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TransportTemplateDetail1Vo> getDetails(String details) {
		List<TransportTemplateDetail1Vo> list = JsonUtil.toObjectList(details, TransportTemplateDetail1Vo.class);
		return list;
	}
	
	public TransportTemplateDetail1 queryByFid(String accId,String templateId,String id){
		return repository.queryByFid(accId, templateId, id);
	}
	public List<TransportTemplateDetail1> queryByTemplateId(String accId,String templateId){
		return repository.queryByFid(accId, templateId);
	}
	/**
	 * 根据模版id删除从表记录
	 * @param tempId
	 */
	@Transactional
	public void delByTempId(String tempId){
		repository.delByTempId(tempId);
	}
}
