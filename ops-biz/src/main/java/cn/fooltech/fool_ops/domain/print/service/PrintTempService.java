package cn.fooltech.fool_ops.domain.print.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.repository.PrintTempRepository;
import cn.fooltech.fool_ops.domain.print.vo.PrintTempVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;


/**
 * <p>打印web服务类</p>
 * @author lgk
 * @date 2016年3月23日下午02:47:12
 * @version V1.0
 */
@Service
public class PrintTempService extends BaseService<PrintTemp, PrintTempVo, String> {
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private AuxiliaryAttrRepository attrRepo;
	
	@Autowired
	private PrintTempRepository printRepo;

	/**
	 * 查询
	 * @param vo
	 * @param pageParamater
	 * @return
	 */
	public Page<PrintTempVo> query(PrintTempVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		return getPageVos(printRepo.findAll(pageRequest), pageRequest);
	}
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(PrintTempVo vo){
    	
    	Organization org = orgService.findOne(vo.getOrgId());
    	
    	if(StringUtils.isNotBlank(vo.getFid())){
    		PrintTemp printTemp = printRepo.findOne(vo.getFid());
    		if(printTemp!=null){
    			if(!printTemp.getAuxiliaryAttr().getFid().equals(vo.getType())){
    	    		Long count = printRepo.countByOrgIdAndAttrId(vo.getOrgId(), vo.getType());
    	    		if(count!=null && count>0){
    	    			return buildFailRequestResult("一个机构只能一个模板类型");
    	    		}
    			}
    		}
    		AuxiliaryAttr auxiliaryAttr = attrRepo.findOne(vo.getType());
			printTemp.setPrintTempUrl(vo.getPrintTempUrl());
    		printTemp.setAuxiliaryAttr(auxiliaryAttr);
    		printTemp.setPageRow(vo.getPageRow());
    		printTemp.setUpdateTime(new Date());
    		printRepo.save(printTemp);
    	}else{
    		PrintTemp printTemp = new PrintTemp();
    		if(org==null){
 	    	   printTemp.setOrg(SecurityUtil.getCurrentDept());
 	    	}else{
 	    	   printTemp.setOrg(org);
 	    	}
    		printTemp.setPrintTempUrl(vo.getPrintTempUrl());
    		
    		Long count = printRepo.countByOrgIdAndAttrId(vo.getOrgId(), vo.getType());
    		
    		if(count!=null && count>0){
    			return buildFailRequestResult("一个机构只能一个模板类型");
    		}
    		
    		AuxiliaryAttr auxiliaryAttr = attrRepo.findOne(vo.getType());
    		printTemp.setAuxiliaryAttr(auxiliaryAttr);
    		printTemp.setPageRow(vo.getPageRow());
    		printTemp.setCreateTime(new Date());
    		printRepo.save(printTemp);
    	}
    	return buildSuccessRequestResult();
    }
    
    /**
     * 获取打印模板
     * @param orgId
     * @param code
     * @return
     */
	public PrintTemp getByOrgId(String orgId,String code){
    	AuxiliaryAttr auxiliaryAttr = attrRepo.findTopByCode(AuxiliaryAttrType.CODE_PRINT, code, orgId);
    	if(auxiliaryAttr!=null){
    		return printRepo.findTopByOrgIdAndAttrId(orgId, auxiliaryAttr.getFid());
    	}
    	return null;
    }
    
    /**
     * 转换vo
     */
	@Override
	public PrintTempVo getVo(PrintTemp entity) {
		PrintTempVo printTempVo = VoFactory.createValue(PrintTempVo.class, entity);
		printTempVo.setOrgId(entity.getOrg().getFid());
		printTempVo.setOrgName(entity.getOrg().getOrgName());
		
		AuxiliaryAttr type = entity.getAuxiliaryAttr();
		if(type!=null){
			printTempVo.setType(type.getFid());
			printTempVo.setTypeName(type.getName());
		}
		
		printTempVo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		return printTempVo;
	}
	
	@Override
	public CrudRepository<PrintTemp, String> getRepository() {
		return this.printRepo;
	}
    /**
     * 获取打印模板
     * @param orgId
     * @param code
     * @return
     */
    public PrintTemp getByOrgCode(String orgId,String code){
    	return printRepo.getByOrgCode(orgId, code);
    }
}
