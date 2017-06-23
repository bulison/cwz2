package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
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
import cn.fooltech.fool_ops.domain.flow.entity.TaskType;
import cn.fooltech.fool_ops.domain.flow.repository.TaskTypeRepository;
import cn.fooltech.fool_ops.domain.flow.vo.TaskTypeVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>事件分类网页服务类</p>
 * @author CYX
 * @version 1.0
 * @date 2016年10月31日
 */
@Service("ops.TaskTypeService")
public class TaskTypeService extends BaseService<TaskType, TaskTypeVo, String> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private TaskTypeRepository taskTypeRepo;
	
	@Override
	public TaskTypeVo getVo(TaskType entity) {
		if (entity == null)
			return null;
		TaskTypeVo vo = VoFactory.createValue(TaskTypeVo.class, entity);
		
//		TaskTypeVo vo = new TaskTypeVo();
//		vo.setFid(entity.getFid());
//		vo.setCode(entity.getCode());
//		vo.setName(entity.getName());
//		vo.setDescribe(entity.getDescribe());
//		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
//		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		Date updateTime = entity.getUpdateTime();
		vo.setUpdateTime(DateUtilTools.time2String(updateTime));
		return vo;
	}

	@Override
	public CrudRepository<TaskType, String> getRepository() {
		// TODO Auto-generated method stub
		return this.taskTypeRepo;
	}
	
	/**
	 * 分页获取所有事件类型的记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TaskTypeVo> getPageTaskType(TaskTypeVo vo, PageParamater paramater){
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		Order[] orders = { new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name") };
		Sort sort = new Sort(orders);
		
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<TaskType> page = taskTypeRepo.findAllTaskType(orgId, searchKey, pageRequest);
		
		Page<TaskTypeVo> pageVos = getPageVos(page, pageRequest);
		return pageVos;
	}
	
	/**
	 * 获取所有事件类型的记录，返回列表
	 * @param vo
	 * @return
	 */
	public List<TaskTypeVo> getListTaskType(TaskTypeVo vo){
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		List<TaskType> listTaskType = taskTypeRepo.findBySearchKey(orgId, searchKey, null);
		List<TaskTypeVo> listVos = getVos(listTaskType);
		return listVos;
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	private boolean checkDataRealTime(TaskTypeVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			TaskType entity = taskTypeRepo.findOne(vo.getFid());
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
	public boolean isCodeExist(String orgId, String code, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = taskTypeRepo.countByCode(orgId, code);
		}else{
			count = taskTypeRepo.countByCode(orgId, code, excludeId);
		}
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	/**
	 * 判断名称是否重复
	 * 
	 */
	public boolean isNameExist(String orgId,String name,String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = taskTypeRepo.countByName(orgId, name);
		}
		else{
			count=taskTypeRepo.countByName(orgId,name,excludeId);
		}
		if(count!=null && count>0){
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
	public RequestResult save(TaskTypeVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		String fid = vo.getFid();
		String code = vo.getCode();
		String name = vo.getName();
		String describe = vo.getDescribe();
		String orgId = SecurityUtil.getCurrentOrgId();
		Date now = new Date();

		//校验数据
		if(!checkDataRealTime(vo)){
			return buildFailRequestResult("页面数据失效，请重新刷新页面!");
		}
		
		if(isCodeExist(orgId, code, fid)){
			return buildFailRequestResult("编号重复!");
		}
		if(isNameExist(orgId,name,fid)){
			return buildFailRequestResult("名称重复!");
		}
		
		
		TaskType entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new TaskType();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
		}
		else{
			entity = taskTypeRepo.findOne(fid);
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
		}
		entity.setCode(code.trim());
		entity.setName(name.trim());
		entity.setDescribe(describe);
		entity.setUpdateTime(now);
		
		taskTypeRepo.save(entity);
		
		RequestResult result = buildSuccessRequestResult();
		
		TaskTypeVo taskTypeVo = VoFactory.createValue(TaskTypeVo.class, entity);
		taskTypeVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(taskTypeVo);
		
		return result;
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid){
		TaskType entity = taskTypeRepo.findOne(fid);
		if(entity == null){
			return buildFailRequestResult("该记录不存在或已被删除!");
		}
		//if(taskTypeRepo.isReferenced(entityManager, fid)){
		if(taskTypeRepo.isReferenced(fid) > 0){
			return buildFailRequestResult("该记录已被引用，无法删除!");
		}
		taskTypeRepo.delete(entity);
		return buildSuccessRequestResult();
	}

}
