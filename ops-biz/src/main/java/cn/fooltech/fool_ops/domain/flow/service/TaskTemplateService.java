package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.domain.flow.entity.TaskTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.TaskType;
import cn.fooltech.fool_ops.domain.flow.repository.TaskTemplateRepository;
import cn.fooltech.fool_ops.domain.flow.vo.TaskTemplateVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>事件模板网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-16 17:08:33
 */
@Service
public class TaskTemplateService extends BaseService<TaskTemplate,TaskTemplateVo,String> {
	
	/**
	 * 事件模板服务类
	 */
	@Autowired
	private TaskTemplateRepository repository;
	
	/**
	 * 事件分类服务类
	 */
	@Autowired
	private TaskTypeService taskTypeService;
	
	/**
	 * 事件级别服务类
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	
	
	/**
	 * 查询事件模板列表信息，按照事件模板主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<TaskTemplateVo> query(TaskTemplateVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<TaskTemplate> page = repository.query(vo, request);
		return getPageVos(page, request);
	}
	
	
	/**
	 * 单个事件模板实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public TaskTemplateVo getVo(TaskTemplate entity){
		if(entity == null)
			return null;
		TaskTemplateVo vo = new TaskTemplateVo();
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setEndDays(entity.getEndDays());
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		vo.setFid(entity.getFid());
		
		TaskLevel level = entity.getTaskLevel();
		vo.setTaskLevelId(level.getFid());
		vo.setTaskLevelName(level.getName());
		
		TaskType type = entity.getTaskType();
		vo.setTaskTypeId(type.getFid());
		vo.setTaskTypeName(type.getName());
		
		User creator = entity.getCreator();
		vo.setCreatorName(creator.getUserName());
		
		return vo;
	}
	
	/**
	 * 删除事件模板<br>
	 */
	public RequestResult delete(String fid){
		
		//TODO 有引用不能删除
//		repository.deleteById(fid);
		repository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取事件模板信息
	 * @param fid 事件模板ID
	 * @return
	 */
	public TaskTemplateVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(repository.findOne(fid));
	}
	

	/**
	 * 新增/编辑事件模板
	 * @param vo
	 */
	public RequestResult save(TaskTemplateVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		String orgId = SecurityUtil.getCurrentOrgId();
		if(repository.countSameCode(vo.getFid(), orgId, vo.getCode())>0){
			return buildFailRequestResult("编号重复");
		}
		
		TaskTemplate entity = new TaskTemplate();
		if(StringUtils.isBlank(vo.getFid())){
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
		}else {
			entity = repository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
		}
		
		entity.setCode(vo.getCode());
		entity.setName(vo.getName());
		entity.setEndDays(vo.getEndDays());
		entity.setDescribe(vo.getDescribe());
		entity.setUpdateTime(Calendar.getInstance().getTime());
		
		TaskType taskType = taskTypeService.get(vo.getTaskTypeId());
		TaskLevel taskLevel = taskLevelService.get(vo.getTaskLevelId());
		
		entity.setTaskType(taskType);
		entity.setTaskLevel(taskLevel);
		
		repository.save(entity);
		
		return buildSuccessRequestResult(getVo(entity));
	}
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<TaskTemplate> queryAll(TaskTemplateVo vo,String taskTypeId) {
		return repository.queryAll(vo,taskTypeId);
	}
	


	@Override
	public CrudRepository<TaskTemplate, String> getRepository() {
		return repository;
	}
}
