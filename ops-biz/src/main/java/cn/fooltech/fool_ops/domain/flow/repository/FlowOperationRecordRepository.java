package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface FlowOperationRecordRepository extends JpaSpecificationExecutor<FlowOperationRecord>, FoolJpaRepository<FlowOperationRecord,String> {

	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<FlowOperationRecord> query(FlowOperationRecord info,Pageable pageable){
		Page<FlowOperationRecord> findAll = findAll(new Specification<FlowOperationRecord>() {
			
			@Override
			public Predicate toPredicate(Root<FlowOperationRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 获取事件的操作记录
	 * @param taskId 事件ID
	 * @param triggerType 触发动作类型
	 * @return
	 */
	
	@Query("select a from FlowOperationRecord a where task.fid=?1 and triggerType=?2 and businessType="+FlowOperationRecord.BUSINESS_TYPE_TASK+" order by createTime desc")
	public List<FlowOperationRecord> getByTask(String taskId, int triggerType);
	
	/**
	 * 获取事件的最新的操作记录
	 * @param taskId 事件ID
	 * @return
	 */
	public default FlowOperationRecord getLastByTask(String taskId, Integer triggerType,Pageable pageable){
		Page<FlowOperationRecord> findAll = findAll(new Specification<FlowOperationRecord>() {
			
			@Override
			public Predicate toPredicate(Root<FlowOperationRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("task").get("fid"), taskId));
				if (triggerType != null) {
					predicates.add(builder.equal(root.get("triggerType"), triggerType));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		List<FlowOperationRecord> list = findAll.getContent();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取事件的所有操作记录
	 * @param taskId
	 * @return
	 */
	@Query("select a from FlowOperationRecord a where task.fid=?1 ")
	public List<FlowOperationRecord> getByTask(String taskId);
	
	/**
	 * 获取事件的所有操作记录
	 * @param taskId
	 * @return
	 */
	@Query("select a from FlowOperationRecord a where plan.fid=?1 ")
	public List<FlowOperationRecord> getByPlan(String planId);
	
	/**
	 * 获取计划的操作记录
	 * @param taskId 事件ID
	 * @param triggerType 触发动作类型
	 * @return
	 */
	@Query("select a from FlowOperationRecord a where plan.fid=?1 and triggerType=?2 and businessType="+FlowOperationRecord.BUSINESS_TYPE_PLAN+" order by createTime desc")
	public List<FlowOperationRecord> getByPlan(String planId, int triggerType);

	/**
	 * 删除某业务对象相关的操作记录
	 * @param busId 业务对象ID
	 */
	@Modifying
	@Query("delete from FlowOperationRecord where plan.fid =?1 or task.fid =?1")
	void deleteByBusId(String busId);
}	

