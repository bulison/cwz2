package cn.fooltech.fool_ops.domain.flow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.fooltech.fool_ops.domain.common.vo.SimpleAttachVo;
import com.google.common.collect.Lists;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.flow.entity.FlowAttach;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.FlowAttachRepository;

/**
 * 流程附件类
 * @author yrl
 * @version 1.0
 * @date 2016年7月15日
 */
@Service
public class FlowAttachService extends BaseService<FlowAttach,FlowAttach, String> {
	@Autowired
	AttachService attachService;
	@PersistenceContext 
	EntityManager entityManager;
	
	@Autowired
	FlowAttachRepository attachRepository;
	/**
	 * 根据FlowOperationRecord的主键，返回相应流程下的附件集，以map格式返回，键是文件名，值是文件id是attach实体的主键
	 * @param recordId
	 * @return
	 */
	public List<SimpleAttachVo> getFileMapByOperRecord(String recordId) {
		List<FlowAttach> attachList = attachRepository.queryByflowRecordId(recordId);
		return getAttachMap(attachList);
	}
	
	/**
	 * 根据计划ID获取附件
	 * @param planId
	 * @return
	 */
	public List<SimpleAttachVo> getFileMapByPlanId(String planId) {
		List<FlowAttach> list = attachRepository.getFileMapByPlanId(planId);
		return getAttachMap(list);
	}
	
	/**
	 * 根据事件ID获取附件
	 * @param taskId
	 * @return
	 */
	public List<SimpleAttachVo> getFileMapByTaskId(String taskId) {
		List<FlowAttach> attachList = attachRepository.queryByTaskId(taskId);
		return getAttachMap(attachList);
	}
	
	/**
	 * 获得附件匹配表
	 * @param attachList
	 * @return
	 */
	private List<SimpleAttachVo> getAttachMap(List<FlowAttach> attachList){
		List<SimpleAttachVo> attachVos = Lists.newArrayList();
		if(attachList != null && attachList.size() > 0) {
			for(FlowAttach flowAttach : attachList) {
				Attach attach = attachService.get(flowAttach.getAttachId());
				if(attach != null) {
					String fileName = attach.getFileName();
					String id = attach.getFid();
					attachVos.add(new SimpleAttachVo(id, fileName));
				}
			}
		}
		return attachVos;
	}
	
	/**
	 * //事件保存附件,插入流程附件表,和插入附件表
	 * @param task
	 * @param operationRecord
	 * @param attachIds
	 */
	@Transactional
	public void saveTaskFlowAttach(Task task, FlowOperationRecord operationRecord, String attachIds) {
		
		if(Strings.isNullOrEmpty(attachIds))return;
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> attachIdList = splitter.splitToList(attachIds);
		
		for(String attachId : attachIdList) {
			Attach attach = attachService.get(attachId);
			if(attach==null)continue;
			attach.setBusId(operationRecord.getFid());
			attachService.save(attach);
			FlowAttach flowAttach = new FlowAttach();
			flowAttach.setFlowRecordId(operationRecord.getFid());
			flowAttach.setPlanId(task.getPlan().getFid());
			flowAttach.setTaskId(task.getFid());
			flowAttach.setAttachId(attach.getFid());
			save(flowAttach);
		}
	}

	/**
	 * 根据事件删除流程事件附件（不删除文件）
	 * @param taskId
	 */
	@Transactional
	public void deleteTaskAttach(String taskId) {
		List<FlowAttach> list = attachRepository.queryByTaskId(taskId);

		for(FlowAttach attach:list){
			attachRepository.delete(attach);
		}
	}

	/**
	 * 根据计划删除流程计划附件（不删除文件）
	 * @param planId
	 */
	@Transactional
	public void deletePlanAttach(String planId) {
		List<FlowAttach> list = attachRepository.getFileMapByPlanId(planId);

		for(FlowAttach attach:list){
			attachRepository.delete(attach);
		}
	}
	
	/**
	 * //事件保存附件
	 * @param task
	 * @param attachIds
	 */
	@Transactional
	public void saveTaskAttach(Task task, String attachIds) {
		
		if(Strings.isNullOrEmpty(attachIds))return;
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> attachIdList = splitter.splitToList(attachIds);
		
		for(String attachId : attachIdList) {
			Attach attach = attachService.get(attachId);
			if(attach==null)continue;
			attach.setBusId(task.getFid());
			attachService.save(attach);
			FlowAttach flowAttach = new FlowAttach();
			flowAttach.setPlanId(task.getPlan().getFid());
			flowAttach.setTaskId(task.getFid());
			flowAttach.setAttachId(attach.getFid());
			save(flowAttach);
		}
	}
	
	/**
	 * //计划保存附件,插入流程附件表,和插入附件表
	 * @param plan
	 * @param operationRecord
	 * @param attachIds
	 */
	@Transactional
	public void savePlanFlowAttach(Plan plan, FlowOperationRecord operationRecord, String attachIds) {
		
		if(Strings.isNullOrEmpty(attachIds))return;
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> attachIdList = splitter.splitToList(attachIds);
		for(String attachId : attachIdList) {
			Attach attach = attachService.get(attachId);
			if(attach==null)continue;
			attach.setBusId(operationRecord.getFid());
			attachService.save(attach);
			FlowAttach flowAttach = new FlowAttach();
			flowAttach.setFlowRecordId(operationRecord.getFid());
			flowAttach.setPlanId(plan.getFid());
			flowAttach.setAttachId(attach.getFid());
			save(flowAttach);
		}
	}
	
	/**
	 * //计划保存附件,和更新附件表
	 * @param plan
	 * @param attachIds
	 */
	@Transactional
	public void savePlanAttach(Plan plan, String attachIds) {
		
		if(Strings.isNullOrEmpty(attachIds))return;
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> attachIdList = splitter.splitToList(attachIds);
		for(String attachId : attachIdList) {
			Attach attach = attachService.get(attachId);
			if(attach==null)continue;
			attach.setBusId(plan.getFid());
			attachService.save(attach);
			FlowAttach flowAttach = new FlowAttach();
			flowAttach.setPlanId(plan.getFid());
			flowAttach.setAttachId(attach.getFid());
			save(flowAttach);
		}
	}


	@Override
	public CrudRepository getRepository() {
		return attachRepository;
	}

	@Override
	public FlowAttach getVo(FlowAttach entity) {
		return null;
	}
	
	public void deleteByColumn(String attachId){
		deleteDataByAttachId("FlowAttach", attachId);
	}
	/**
	 * 根据账套删除
	 * @param clazzName
	 * @param attachId
	 */
	@Transactional
	public void deleteDataByAttachId(String clazzName, String attachId){
		String hql = "delete from "+clazzName+" where attachId=:attachId";
		Query query = entityManager.createQuery(hql);
		query.setParameter("attachId", attachId);
		query.executeUpdate();
	}
}
