package cn.fooltech.fool_ops.domain.fiscal.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalConfig;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalConfigRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalConfigVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>财务参数设置服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-11-23 16:28:37
 */
@Service
public class FiscalConfigService extends BaseService<FiscalConfig, FiscalConfigVo, String>{
	
	private static final Logger logger = LoggerFactory.getLogger(FiscalConfigService.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private FiscalConfigRepository configRepo;
	
	/**
	 * 新增账套时导入数据
	 * @param paramsMap
	 * @param initFile
	 * @throws Exception
	 */
	@Transactional
	public void initFiscalConfig( Map<String,String> paramsMap, String initFile) {
		List<String> lines = null;
		try {
			File sqlFile = ResourceUtils.getFile(initFile);
			lines = Files.readLines(sqlFile, Charsets.UTF_8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
        for(String line:lines){
        	if(StringUtils.isBlank(line)){
        		continue;
        	}
        	String FID = UUID.randomUUID().toString().replaceAll("-", "");
        	paramsMap.put("FID", FID);
        	
        	String exeSql = line;
        	
        	for(String key:paramsMap.keySet()){
        		String processKey = ":"+key;
        		exeSql = exeSql.replaceAll(processKey, paramsMap.get(key));
        	}
        	Query query = entityManager.createNativeQuery(exeSql);
    		query.executeUpdate();
        }
	}

	@Override
	public FiscalConfigVo getVo(FiscalConfig entity) {
		if(entity == null)
			return null;
		FiscalConfigVo vo = new FiscalConfigVo();
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setDescribe(entity.getDescribe());
		vo.setValue(entity.getValue());
		vo.setValueType(entity.getValueType());
		vo.setSelectValue(entity.getSelectValue());
		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		vo.setFid(entity.getFid());
		
		return vo;
	}

	@Override
	public CrudRepository<FiscalConfig, String> getRepository() {
		return configRepo;
	}
	
	/**
	 * 编辑财务参数设置
	 * @param vo
	 */
	public RequestResult update(FiscalConfigVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		if(StringUtils.isBlank(vo.getCode())){
			return buildFailRequestResult("编号必填");
		}
		if(StringUtils.isBlank(vo.getName())){
			return buildFailRequestResult("名称必填");
		}
		if(vo.getValueType()==null){
			return buildFailRequestResult("值转换类型必填");
		}
		
		FiscalConfig entity = configRepo.findOne(vo.getFid());
		 //检查是否同时修改的页面，提示退出或刷新再保存。
		if(!checkDataRealTime(vo)){
			return buildFailRequestResult("页面数据失效，请重新刷新页面!");
		}
		
		entity.setCode(vo.getCode());
		entity.setName(vo.getName());
		entity.setDescribe(vo.getDescribe());
		entity.setValue(vo.getValue());
		entity.setValueType(vo.getValueType());
		entity.setSelectValue(vo.getSelectValue());
		entity.setUpdateTime(new Date());
		configRepo.save(entity);
		
		return buildSuccessRequestResult();
	}
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	public boolean checkDataRealTime(FiscalConfigVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			FiscalConfig entity = configRepo.findOne(vo.getFid());
			if(entity==null) return false;
			Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}
	
	/**
	 * 查询财务参数设置列表信息，按照财务参数设置主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<FiscalConfigVo> query(FiscalConfigVo fiscalConfigVo,PageParamater pageParamater){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		
		return getPageVos(configRepo.findPageByAccId(accId, pageRequest), pageRequest);
	}
	
	/**
	 * 获取财务参数配置
	 * @param fiscalAccountId 财务账套ID
	 * @param code 编号
	 * @return
	 * @author rqh
	 */
	public FiscalConfig getConfig(String fiscalAccountId, String code){
		return configRepo.findTopByAccIdAndCode(fiscalAccountId, code);
	}
	
	/**
	 * 获取财务参数配置的值
	 * @param fiscalAccountId 财务账套ID
	 * @param code 编号
	 * @param defaultVaue 默认值
	 * @return
	 * @author rqh
	 */
	public String getConfigValue(String fiscalAccountId, String code, String defaultVaue){
		FiscalConfig config = getConfig(fiscalAccountId, code);
		if(config == null){
			return defaultVaue;
		}
		return config.getValue();
	}
}
