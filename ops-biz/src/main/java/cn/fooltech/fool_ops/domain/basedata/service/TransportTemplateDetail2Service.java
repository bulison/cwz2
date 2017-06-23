package cn.fooltech.fool_ops.domain.basedata.service;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplate;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateDetail1Repository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateDetail2Repository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail2Vo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 运输费报价模板(从2表)
 * @author cwz
 * @date   2016-12-9
 */
@Service
public class TransportTemplateDetail2Service extends BaseService<TransportTemplateDetail2, TransportTemplateDetail2Vo, String> {


    @Autowired
    private TransportTemplateDetail2Repository repository;
    @Autowired
    private TransportTemplateRepository templateRepository;
    //辅助属性持久层
    @Autowired
    private AuxiliaryAttrRepository attrRepository;
    @Autowired
    private TransportTemplateDetail1Repository detail1;

    @Override
    public TransportTemplateDetail2Vo getVo(TransportTemplateDetail2 entity) {
//        TransportTemplateDetail2Vo vo = VoFactory.createValue(TransportTemplateDetail2Vo.class, entity);
        TransportTemplateDetail2Vo vo = new TransportTemplateDetail2Vo();
        vo.setFid(entity.getId());
        vo.setDescribe(entity.getDescribe());
        vo.setCode(entity.getCode());
        TransportTemplateDetail1 detail1 = entity.getDetail1();
        if(detail1!=null){
        	vo.setDetail1Fid(detail1.getId());
        }
        TransportTemplate template = entity.getTemplate();
        if(template!=null){
        	vo.setTemplateFid(template.getId());
        	vo.setTemplateName(template.getName());
        }
        AuxiliaryAttr transportCost = entity.getTransportCost();
        if(transportCost!=null){
        	 vo.setTransportCostFid(transportCost.getFid());
        	 vo.setTransportCostName(transportCost.getName());
        }
        AuxiliaryAttr transportUnit = entity.getTransportUnit();
        if(transportUnit!=null){
            vo.setTransportUnitFid(transportUnit.getFid());
            vo.setTransportUnitName(transportUnit.getName());
        }
        vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
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
    public CrudRepository<TransportTemplateDetail2, String> getRepository() {
        return repository;
    }
    /**
     * 运输费报价查找分页
     * @param groundId
     * @param paramater
     * @return
     */
    public Page<TransportTemplateDetail2Vo> query(TransportTemplateDetail2Vo vo, PageParamater paramater) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        PageRequest request = getPageRequest(paramater,sort);
        Page<TransportTemplateDetail2> page = repository.findPageBy(vo, request);
        Page<TransportTemplateDetail2Vo> pageVos = getPageVos(page, request);
        return pageVos;
    }
    
    /**
     * 保存运输费报价模板从1
     * @param vo
     * @return
     */
    @Transactional
	public RequestResult save(TransportTemplateDetail2Vo vo) {
    	TransportTemplateDetail2 info = new TransportTemplateDetail2();
    	//先删除，后添加（从2表有数据先删除从2数据）
//    	if(!Strings.isNullOrEmpty(vo.getFid())){
//    		delete(vo.getFid());
//    		vo.setFid(null);
//    	}
    	Date now = new Date();
    	info.setCreateTime(now);
    	info.setCreator(SecurityUtil.getCurrentUser());
    	info.setDescribe(vo.getDescribe());
    	info.setFiscalAccount(SecurityUtil.getFiscalAccount());
    	info.setOrg(SecurityUtil.getCurrentOrg());
    	if(!Strings.isNullOrEmpty(vo.getTemplateFid())){
    		info.setTemplate(templateRepository.findOne(vo.getTemplateFid()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getDetail1Fid())){
    		info.setDetail1(detail1.findOne(vo.getDetail1Fid()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getTransportCostFid())){
    		info.setTransportCost(attrRepository.findOne(vo.getTransportCostFid()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getTransportUnitFid())){
    		info.setTransportUnit(attrRepository.findOne(vo.getTransportUnitFid()));
    	}
    	info.setUpdateTime(now);
    	info.setCode(vo.getCode());
    	//保存主表数据
        repository.save(info);
        return buildSuccessRequestResult();
    }
    /**
     * 删除运输费报价模板（从2表）
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
	 * josn转换List<TransportTemplateDetail1Vo>
	 * 
	 * @return
	 */
	public Map<String,List<TransportTemplateDetail2Vo>> getDetails(String details) {
		Map<String,List<TransportTemplateDetail2Vo>> map = Maps.newHashMap();
		try {
			JSONArray array = JSONArray.fromObject(details);
			JSONObject object = array.getJSONObject(0);
			Integer total = (Integer) object.get("total");
			if(total>0){
				for (int i = 0; i < total; i++) {
					Set keySet = object.keySet();
					for (Object obj : keySet) {
						String key="";
						if(!obj.equals("total")){
							key = obj.toString();
							Object object2 = object.get(key);
							List<TransportTemplateDetail2Vo> list = Lists.newArrayList();
							list = JsonUtil.toObjectList(object2.toString(), TransportTemplateDetail2Vo.class);
							map.put(key, list);
						}
						
					}
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		return map;
	}
	public TransportTemplateDetail2 queryByFid(String accId,String templateId,String id){
		return repository.queryByFid(accId, templateId, id);
	}
	public List<TransportTemplateDetail2> queryByTemplateId(String accId,String templateId,String detail1Id){
		return repository.queryByTemplateId(accId, templateId,detail1Id);
	}
	public List<TransportTemplateDetail2> queryByTemplateId(String accId,String templateId){
		return repository.queryByTemplateId(accId, templateId);
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
