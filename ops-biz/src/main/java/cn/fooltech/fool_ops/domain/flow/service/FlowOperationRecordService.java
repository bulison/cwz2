package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.FlowOperationRecordRepository;
import cn.fooltech.fool_ops.domain.flow.repository.TaskRepository;
import cn.fooltech.fool_ops.domain.flow.vo.FlowOperationRecordVo;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.utils.VoFactory;


/**
 * <p>流程操作记录服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
@Service
public class FlowOperationRecordService extends BaseService<FlowOperationRecord,FlowOperationRecordVo,String> {

	
	@Autowired
	private TaskService taskService;
	/**
	 * 事件服务类
	 */
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private FlowOperationRecordRepository repository;
	

	
	/**
	 * 删除
	 * @param recorsds
	 * @param entity
	 */
	public void delete(List<FlowOperationRecord> recorsds, Object ... args){
		if(CollectionUtils.isNotEmpty(recorsds)){
			for(FlowOperationRecord record : recorsds){
				delete(record);
			}
		}
	}
	
	/**
	 * 获取事件的操作记录
	 * @param taskId 事件ID
	 * @param triggerType 触发动作类型
	 * @return
	 */
	public List<FlowOperationRecord> getByTask(String taskId, int triggerType){
		return repository.getByTask(taskId, triggerType);
	}
	
	/**
	 * 获取事件的最新的操作记录
	 * @param taskId 事件ID
	 * @return
	 */
	public FlowOperationRecord getLastByTask(String taskId, Integer triggerType){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(new PageParamater(1, 1, 0),sort);
		return repository.getLastByTask(taskId, triggerType, request);
	}
	
	/**
	 * 获取事件的所有操作记录
	 * @param taskId
	 * @return
	 */
	public List<FlowOperationRecord> getByTask(String taskId) {
		return repository.getByTask(taskId);
	}
	
	/**
	 * 获取事件的所有操作记录
	 * @param taskId
	 * @return
	 */
	public List<FlowOperationRecord> getByPlan(String planId) {
		return repository.getByPlan(planId);
	}
	
	/**
	 * 获取计划的操作记录
	 * @param taskId 事件ID
	 * @param triggerType 触发动作类型
	 * @return
	 */
	public List<FlowOperationRecord> getByPlan(String planId, int triggerType){
		return repository.getByPlan(planId,triggerType);
	}
	
	/**
	 * 获取已关联的前置事件
	 * @param taskId 事件ID
	 * @return
	 */
	public List<Task> getFrontRelevanceTask(String taskId){
		List<FlowOperationRecord> records = getByTask(taskId, Message.TRIGGER_TYPE_RELEVANCE);
		if(CollectionUtils.isNotEmpty(records)){
			FlowOperationRecord record = records.get(0);
			String frontIds = record.getFrontRelevanceId();
			if(StringUtils.isNotBlank(frontIds)){
				String[] ids = frontIds.split(",");
				return taskService.getByIds(ids);
			}
		}
		return Collections.emptyList();
	}
	
	/**
	 * 获取已关联的后置事件
	 * @param taskId 事件ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> getBehindRelevanceTask(String taskId){
		List<Task> list = taskRepository.getBehindRelevanceTask(taskId);
		return list;
	}
	
	/**
	 * 删除某业务对象相关的操作记录
	 * @param busId 业务对象ID
	 */
	@Transactional
	public void deleteByBusId(String busId){
		repository.deleteByBusId(busId);
	}

	@Override
	public FlowOperationRecordVo getVo(FlowOperationRecord entity) {
		FlowOperationRecordVo vo = VoFactory.createValue(FlowOperationRecordVo.class, entity);
		return vo;
	}

	@Override
	public CrudRepository<FlowOperationRecord, String> getRepository() {
		return repository;
	}
	
}
