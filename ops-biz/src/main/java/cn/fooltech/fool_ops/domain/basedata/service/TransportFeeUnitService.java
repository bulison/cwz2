package cn.fooltech.fool_ops.domain.basedata.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportFeeUnitVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * 
 * @author hjr
 *	2017/04/20
 */
@Service
public class TransportFeeUnitService extends BaseService<AuxiliaryAttr,TransportFeeUnitVo,String>{
    @Autowired
    private AuxiliaryAttrRepository repository;
    @Autowired
    private AuxiliaryAttrTypeService attrTypeService;
    @Autowired
    private AuxiliaryAttrService attrService;
    @Autowired
    private RedisService redisService;
    /**
     * 实体转化VO
     */
	@Override
	public TransportFeeUnitVo getVo(AuxiliaryAttr entity) {
		TransportFeeUnitVo vo=new TransportFeeUnitVo();
        vo.setFid(entity.getFid());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setFlag(entity.getFlag());
        vo.setEnable(entity.getEnable());
        vo.setDescribe(entity.getDescribe());
        vo.setSystemSign(entity.getSystemSign());
        vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
        vo.setScale(entity.getScale());	
		return vo;
	}
	/**
	 * 获取所有运输单位费用
	 */
	public Page<TransportFeeUnitVo> query(PageParamater pageParamater){
		PageRequest pageRequest = getPageRequest(pageParamater);
        String orgId = SecurityUtil.getCurrentOrgId(); //机构ID
        AuxiliaryAttrType attrType = attrTypeService.findTransportFee(orgId);
        if(attrType!=null){
        	Page<AuxiliaryAttr> attrRootData = attrService.findPageAllRootData(attrType.getFid(), orgId,pageRequest);
        		 return getPageVos(attrRootData,pageRequest);
        	}
        else{
        	return null;
        }
        
	}
	/**
	 * 新增,编辑运输费用单位
	 */
	public RequestResult save(TransportFeeUnitVo vo){
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }

        String fid = vo.getFid();
        String code = vo.getCode();
        String name = vo.getName();
        Short enable = vo.getEnable();
        String describe = vo.getDescribe();

        String orgId = SecurityUtil.getCurrentOrgId(); //机构ID
        String accountId = SecurityUtil.getFiscalAccountId(); //财务账套ID
        Date now = Calendar.getInstance().getTime(); //当前时间
        AuxiliaryAttrType attrType = attrTypeService.findByCode(AuxiliaryAttrType.CODE_TRANSIT_FEE_UNIT, orgId, accountId); //辅助属性类别
        Assert.notNull(attrType, "运输费用单位类别不存在!");
        if (!checkDataRealTime(vo)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "页面数据失效，请重新刷新页面!");
        }
        AuxiliaryAttr entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new AuxiliaryAttr();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
            //节点标识
            entity.setFlag(AuxiliaryAttr.LEAF);
        } else {
            entity = attrService.get(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "运输费用单位不存在或已被删除!");
            }
            entity.setUpdateTime(now);
        }
        if (attrService.isCodeExist(orgId, accountId, AuxiliaryAttrType.CODE_TRANSIT_FEE_UNIT, code, null, fid)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "组内编号重复!");
        }
        entity.setCode(code);
        entity.setName(name);
        entity.setEnable(enable);
        entity.setDescribe(describe);
        entity.setCategory(attrType);
        entity.setScale(vo.getScale());
        attrService.save(entity);
        return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 删除运输费用单位
	 */
	public RequestResult delete(String fid){
        try {
        	if (attrService.auxiliaryUse(fid)) {
        		return new RequestResult(RequestResult.RETURN_FAILURE, "运输费用已被使用，不允许删除!");
        	} else {
                repository.delete(fid);
        		return new RequestResult();
        	}
        }catch (Exception e) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "系统繁忙，请稍后再试!");
        }

        	
	}
	
    /**
     * 获得缓存的Key
     */
    protected String getCacheKey(final String categoryCode){

        String key = "";
        key += BaseConstant.getCacheKey(categoryCode);
        key += ":";
        key += SecurityUtil.getCurrentOrgId();
        key += ":";
        key += SecurityUtil.getFiscalAccountId();

        return key;
    }
	 /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(TransportFeeUnitVo vo){

        if (StringUtils.isNotBlank(vo.getFid())) {
            AuxiliaryAttr entity = attrService.get(vo.getFid());
            Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    
    }
	@Override
	public CrudRepository<AuxiliaryAttr, String> getRepository() {
		return repository;
	}

}
