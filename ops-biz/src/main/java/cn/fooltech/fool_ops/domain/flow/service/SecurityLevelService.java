package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.flow.repository.SecurityLevelRepository;
import cn.fooltech.fool_ops.domain.flow.vo.SecurityLevelVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>保密级别网页服务类</p>
 * @author CYX
 * @version 1.0
 * @date 2016年11月10日
 */
@Service("ops.SecurityLevelService")
public class SecurityLevelService extends BaseService<SecurityLevel, SecurityLevelVo, String>{

	@Autowired
	private SecurityLevelRepository securityLevelRepo;
	
	@Override
	public SecurityLevelVo getVo(SecurityLevel entity) {
		// TODO Auto-generated method stub
		if (entity == null)
			return null;
		SecurityLevelVo vo = VoFactory.createValue(SecurityLevelVo.class, entity);
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		return vo;
	}

	@Override
	public CrudRepository<SecurityLevel, String> getRepository() {
		// TODO Auto-generated method stub
		return this.securityLevelRepo;
	}
	
	/**
	 * 分页获取所有保密级别的记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<SecurityLevelVo> getPageSecurityLevel(SecurityLevelVo vo, PageParamater paramater) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		Order[] orders = {new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name")};
		Sort sort = new Sort(orders);
		
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<SecurityLevel> pageSecurityLevel = securityLevelRepo.findAllTaskType(orgId, searchKey, pageRequest);
		
		Page<SecurityLevelVo> pageVos = getPageVos(pageSecurityLevel, pageRequest);
		return pageVos;
	}

	/**
	 * 获取所有保密级别的记录，返回列表
	 * @param vo
	 * @return
	 */
	public List<SecurityLevelVo> getListSecurityLevel(SecurityLevelVo vo) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		List<SecurityLevel> listSecurityLevel = securityLevelRepo.findBySearchKey(orgId, searchKey, null);
		List<SecurityLevelVo> listVos = getVos(listSecurityLevel);
		return listVos;
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	private boolean checkDataRealTime(SecurityLevelVo vo) {
		if (StringUtils.isNotBlank(vo.getFid())) {
			SecurityLevel entity = securityLevelRepo.findOne(vo.getFid());
			Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}
	
	/**
	 * 判断编号是否有效
	 * @param code 编号
	 * @return
	 */
	private boolean isCodeExist(String orgId, String code, String excludeId) {
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = securityLevelRepo.countByCode(orgId, code);
		}
		else {
			count = securityLevelRepo.countByCode(orgId, code, excludeId);
		}
		if(count != null && count > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 判断名称是否有效
	 * @param orgId
	 * @param code
	 * @param excludeId
	 * @return
	 */
	private boolean isNameExist(String orgId, String name, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count=securityLevelRepo.countByName(orgId, name);
		}else{
			count=securityLevelRepo.countByName(orgId, name, excludeId);
		}
		if(count != null && count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(SecurityLevelVo vo) {
		//检验数据
		String inValid = ValidatorUtils.inValidMsg(vo);
		if (inValid != null) {
			return buildFailRequestResult(inValid);
		}
		
		String fid = vo.getFid();
		String code = vo.getCode();
		String name = vo.getName();
		Integer level = vo.getLevel();
		Integer checkoutStatus = vo.getCheckoutStatus();
		String describe = vo.getDescribe();
		String orgId = SecurityUtil.getCurrentOrgId();
		Date now = new Date();
		
		if (!checkDataRealTime(vo)) {
			return buildFailRequestResult("页面数据失效，请重新刷新页面!");
		}
		if (isCodeExist(orgId, code, fid)) {
			return buildFailRequestResult("编号重复!");
		}
		if(isNameExist(orgId,name,fid)){
			return buildFailRequestResult("名称重复!");
		}

		SecurityLevel entity = null;
		if (StringUtils.isBlank(fid)) {
			entity = new SecurityLevel();
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
		}
		else {
			entity = securityLevelRepo.findOne(fid);
			if (entity == null) {
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
		}
		entity.setCode(code.trim());
		entity.setName(name.trim());
		entity.setLevel(level);
		entity.setCheckoutStatus(checkoutStatus);
		entity.setDescribe(describe);
		entity.setUpdateTime(now);
		
		securityLevelRepo.save(entity);
		
		RequestResult result = buildSuccessRequestResult();
		SecurityLevelVo securityLevelVo = VoFactory.createValue(SecurityLevelVo.class, entity);
		securityLevelVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(securityLevelVo);
		
		return result;
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid) {
		System.out.println(fid);
		SecurityLevel entity = securityLevelRepo.findOne(fid);
		if (entity == null) {
			return buildFailRequestResult("该记录不存在或已被删除!");
		}
		if (securityLevelRepo.isReferenced(fid) > 0) {
			return buildFailRequestResult("该记录已被引用，无法删除!");
		}
		if(securityLevelRepo.isStart(fid)>0){
			return buildFailRequestResult("该记录已被启用，无法删除!");
		}
		securityLevelRepo.delete(entity);
		RequestResult result = buildSuccessRequestResult();
		return result;
	}
	
	
}
