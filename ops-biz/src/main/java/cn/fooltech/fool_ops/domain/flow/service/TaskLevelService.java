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
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.domain.flow.repository.TaskLevelRepository;
import cn.fooltech.fool_ops.domain.flow.vo.TaskLevelVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>事件级别网页服务类</p>
 * @author CYX
 * @version 1.0
 * @date 2016年11月10日
 */
@Service("ops.TaskLevelService")
public class TaskLevelService extends BaseService<TaskLevel, TaskLevelVo, String>  {

	@Autowired
	private TaskLevelRepository taskLevelRepo;
	
	@Override
	public TaskLevelVo getVo(TaskLevel entity) {
		// TODO Auto-generated method stub
		if (entity == null)
			return null;
		TaskLevelVo vo = VoFactory.createValue(TaskLevelVo.class, entity);
		Date updateTime = entity.getUpdateTime();
		vo.setUpdateTime(DateUtilTools.time2String(updateTime));
		return vo;
	}

	@Override
	public CrudRepository<TaskLevel, String> getRepository() {
		// TODO Auto-generated method stub
		return this.taskLevelRepo;
	}

	/**
	 * 分页获取所有事件级别的记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TaskLevelVo> getPageTaskLevel(TaskLevelVo vo, PageParamater paramater) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		Order[] orders = {new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name")};
		Sort sort = new Sort(orders);
		
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<TaskLevel> page = taskLevelRepo.findAllTaskLevel(orgId, searchKey, pageRequest);
		
		Page<TaskLevelVo> pageVos = getPageVos(page, pageRequest);
		return pageVos;
	}
	
	/**
	 * 获取所有事件级别的记录，返回列表
	 * @param vo
	 * @return
	 */
	public List<TaskLevelVo> getListTaskLevel(TaskLevelVo vo) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String searchKey = vo.getSearchKey();
		
		List<TaskLevel> listTaskLevel = taskLevelRepo.findBySearchKey(orgId, searchKey, null);
		List<TaskLevelVo> listVos = getVos(listTaskLevel);
		return listVos;
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	private boolean checkDataRealTime(TaskLevelVo vo) {
		if (StringUtils.isNotBlank(vo.getFid())) {
			TaskLevel entity = taskLevelRepo.findOne(vo.getFid());
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
			count = taskLevelRepo.countByCode(orgId, code);
		}
		else {
			count = taskLevelRepo.countByCode(orgId, code, excludeId);
		}
		if(count != null && count > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 判断名称是否重复
	 * @param orgId
	 * @param name
	 * @param excludeId
	 * @return
	 */
	public boolean isNameExist(String orgId,String name,String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count =taskLevelRepo.countByName(orgId, name);
		}else{
			count =taskLevelRepo.countByName(orgId, name, excludeId);
		}
		if(count!= null &&count>0){
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
	public RequestResult save(TaskLevelVo vo) {
		//检验数据
		String inValid = ValidatorUtils.inValidMsg(vo);
		if (inValid != null){
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
		
		TaskLevel entity = null;
		if (StringUtils.isBlank(fid)) {
			entity = new TaskLevel();
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
		}
		else {
			entity = taskLevelRepo.findOne(fid);
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
		
		taskLevelRepo.save(entity);
		
		RequestResult result = buildSuccessRequestResult();
		TaskLevelVo taskLevelVo = VoFactory.createValue(TaskLevelVo.class, entity);
		taskLevelVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(taskLevelVo);
		return result;
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid) {
		TaskLevel entity = taskLevelRepo.findOne(fid);
		if (entity == null) {
			return buildFailRequestResult("该记录不存在或已被删除!");
		}
		if (taskLevelRepo.isReferenced(fid) > 0) {
			return buildFailRequestResult("该记录已被引用，无法删除!");
		}
		if(taskLevelRepo.isStart(fid)>0){
			return buildFailRequestResult("该记录已被启用，无法删除!");
		}
		taskLevelRepo.delete(entity);
		RequestResult result = buildSuccessRequestResult();
		return result;
	}
}
