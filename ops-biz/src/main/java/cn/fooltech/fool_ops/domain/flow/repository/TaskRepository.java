package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface TaskRepository extends JpaRepository<Task, String>, FoolJpaSpecificationExecutor<Task> {

	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<Task> query(TaskVo vo,Pageable pageable){
		Page<Task> findAll = findAll(new Specification<Task>() {
			
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 获取计划的根事件
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select a from Task a where plan.fid=?1 and parent.fid is null")
	public List<Task> getRootTask(String planId);

	/**
	 * 获取计划的一级事件记录数目
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select count(*) from Task a where a.plan.fid=?1 and a.level=1")
	public Long countLevelOneByPlanId(String planId);
	
	/**
	 * 判断计划是否完成（不包括根事件）
	 * @param planId 计划ID
	 * @param excludeTaskIds 排除的事件ID
	 * @return
	 */
	public default boolean isPlanComplete(String planId, String ... excludeTaskIds){
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_FINISHED, Task.STATUS_STOPED);
		long count = count(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("plan").get("fid"), planId));
				predicates.add(builder.isNotNull(root.get("parent").get("fid")));
				predicates.add(builder.not(builder.in(root.get("status")).value(statusList)));
				if (excludeTaskIds != null && excludeTaskIds.length > 0) {
					ArrayList<String> list = Lists.newArrayList(excludeTaskIds);
					predicates.add(builder.not(builder.in(root.get("fid")).value(list)));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		if(count>0){
			return false;
		}
		return true;
	}
	/**
	 * 根据计划ID寻找所有叶子节点
	 * @param planId
	 * @return
	 */
	@Query("select a from Task a where a.treeFlag=0 and a.plan.fid=?1")
	public List<Task> queryAllChildren(String planId);
	
	/**
	 * 统计某个计划的事件数量，排除根事件
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select count(*) from Task a where parent.fid is not null and plan.fid=?1")
	public long countByPlanId(String planId);
	
	/**
	 * 获取某个计划中，处于某个层级的事件
	 * @param planId 计划ID
	 * @param level 事件层级
	 * @return
	 */
	@Query("select a from Task a where plan.fid=?1 and level=?2")
	public List<Task> getByLevel(String planId, int level);

	/**
	 * 统计某个计划的未完成事件数量，排除根事件
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select count(a) from Task a where plan.fid=?1 and parent.fid is not null and status not in(?2)")
	public long countNotCompleteByPlanId(String planId,ArrayList<Integer> list);


	/**
	 * 查找未完成事件，排除根事件
	 * @param list
	 * @return
	 */
	@Query("select a from Task a where a.parent.fid is not null and a.status not in(?1)")
	public List<Task> findNotComplete(List<Integer> list);

	/**
	 * 获取事件
	 * @param ids 事件ID
	 * @return
	 */
	@Query("select a from Task a where fid in(?1)")
	public List<Task> getByIds(ArrayList<String> ids);
	
	/**
	 * 获取已关联的后置事件
	 * @param taskId 事件ID
	 * @return
	 */
	@Query("select a from Task a where triggerType=54 and frontRelevanceId like '%?2%' and businessType=1")
	public List<Task> getBehindRelevanceTask(String taskId);

	/**
	 * 查询计划下的事件，排除根事件
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select a from Task a where plan.fid=?1 and parent is not null order by antipateStartTime asc")
	public List<Task> getTaskByPlan(String planId);
	/**
	 * 查询计划下的事件，排除根事件(计算收益率显示使用)
	 * @param planId 计划ID
	 * @return
	 */
	@Query("select a from Task a where plan.fid=?1 and parent is not null and amount !=0 order by antipateStartTime asc")
	public List<Task> getTaskByPlan2(String planId);
	
	@Query("select a from Task a where treeFlag is null")
	public List<Task> getTaskTreeFlag();
	
	/**
	 * 判断计划内，事件的编号是否已存在
	 * @param planId 计划ID
	 * @param code 事件编号
	 * @param excludeId 排除的事件ID
	 * @return
	 */
	public default boolean isCodeExist(String planId, String code, String excludeId){
		long count = count(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("plan").get("fid"), planId));
				predicates.add(builder.equal(root.get("code"), code));
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		if(count<=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断计划内，事件的名称是否已存在
	 * @param planId 计划ID
	 * @param name 事件名称
	 * @param excludeId 排除的事件ID
	 * @return
	 */
	public default boolean isNameExist(String planId, String name, String excludeId){
		long count = count(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("plan").get("fid"), planId));
				predicates.add(builder.equal(root.get("name"), name));
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		if(count<=0){
			return false;
		}
		return true;
	}
//	@Query("select a from PlanTemplateDetail a where planTemplate.fid=?1 and parent.fid is null")
//	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	/**
	 * 获取某个事件的直属下级子事件
	 * @param taskId 事件ID
	 * @return
	 */
	@Query("select a from Task a where parent.fid=?1 order by code")
	public List<Task> getChilds(String taskId);
	
	/**
	 * 获取某事件未完成的直属下级子事件数量
	 * @param taskId
	 * @return
	 */
	@Query("select count(a) from Task a where parent.fid=?1 and status not in(3,6)")
	public long countNotFinishChilds(String taskId);
	/**
	 * 判断某事件的所有子事件，是否都已经完成了
	 * @param taskId 事件ID
	 * @param excludeIds 排除的事件ID
	 * @return
	 */
	public default boolean isAllChildsComplete(String taskId, String ... excludeIds){
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_FINISHED, Task.STATUS_STOPED);
		List<String> excludeIdsList = Lists.newArrayList(excludeIds);
		long count = count(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String anyLike = PredicateUtils.getLeftLike(taskId);
				predicates.add(builder.like(root.get("parentIds"),anyLike));
				predicates.add(builder.not(root.get("status").in(statusList)));
				if(excludeIdsList!=null&&excludeIdsList.size()>0){
					predicates.add(builder.not(root.get("fid").in(excludeIdsList)));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		if(count>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取某个事件的所有子事件·
	 * @param taskId 事件ID
	 * @return
	 */
	public default List<Task> getAllChilds(String taskId){
		return findAll(new Specification<Task>() {
					@Override
					public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
						List<Predicate> predicates = Lists.newArrayList();
						predicates.add(builder.like(root.get("parentIds"), PredicateUtils.getLeftLike(taskId)));
						Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
						return predicate;
					}
		});
	}
	/**
	 * 获取某事件的直属下级子事件数量
	 * @param taskId 事件ID
	 * @return
	 */
	@Query("select count(a) from Task a where parent.fid=?1")
	public long countChilds(String taskId);
	
	/**
	 * 根据某个事件，获取符合条件的前置关联事件
	 * @param task
	 * @return
	 */
	public default List<Task> getFrontRelevanceTask(Task task){
		if(task == null){
			return Collections.emptyList();
		}
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART,
				Task.STATUS_EXECUTING, Task.STATUS_DELAYED_UNFINISH);
		 List<Task> findAll = findAll(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.in(root.get("status")).value(statusList));
				predicates.add(builder.equal(root.get("plan").get("fid"), task.getPlan().getFid()));
				predicates.add(builder.lessThan(root.get("antipateEndTime"),task.getAntipateStartTime()));
				predicates.add(builder.isNotNull(root.get("parent").get("fid")));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		 return findAll;
	}
	/**
	 * 根据某个事件，获取符合条件的后置关联事件
	 * @param task
	 * @return
	 */
	public default List<Task> getBehindRelevanceTask(Task task){
		if(task == null){
			return Collections.emptyList();
		}
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART);
		 List<Task> findAll = findAll(new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.in(root.get("status")).value(statusList));
				predicates.add(builder.equal(root.get("plan").get("fid"), task.getPlan().getFid()));
				predicates.add(builder.greaterThan(root.get("antipateStartTime"),task.getAntipateEndTime()));
				predicates.add(builder.isNotNull(root.get("parent").get("fid")));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		 return findAll;
	}
	/**
	 * 修改计划id
	 * @param oldPlanId	
	 * @param newPlanId
	 */
	@Modifying
	@Query("update Task t set plan.fid=?2 where plan.fid=?1 and parent.fid is not null")
	public void changeByPlanId(String oldPlanId,String newPlanId);
	/**
	 * 修改父节点id，把事件挂到其他事件中，只操作1级事件
	 * @param fid	
	 * @param parentId
	 */
	@Modifying
	@Query("update Task t set parent.fid=?2 where fid=?1")
	public void changeByParentId(String fid,String parentId);
	/**
	 * 替换父节点中需要替换的节点id
	 * @param old		待替换id
	 * @param replace	替换id
	 * @param planId	计划id
	 */
	@Modifying
	@Query(nativeQuery=true,value="update tflow_task t set t.FPARENT_IDS=REPLACE(FPARENT_IDS,?1,?2) where t.FPLAN_ID=?3 and t.FLEVEL !=0")
	public void changeByParentIds(String old,String replace,String planId);

	/**
	 * 根据父节点路径查找最小预计开始时间
	 * @param parentIds
	 * @return
	 */
	@Query("select min(t.antipateStartTime) from Task t where t.parentIds like ?1")
	public Date findMinStart(String parentIds);

	/**
	 * 根据父节点路径查找最大预计开始时间
	 * @param parentIds
	 * @return
	 */
	@Query("select max(t.antipateStartTime) from Task t where t.parentIds like ?1")
	public Date findMaxStart(String parentIds);

	/**
	 * 根据父节点路径查找最小预计结束时间
	 * @param parentIds
	 * @return
	 */
	@Query("select min(t.antipateEndTime) from Task t where t.parentIds like ?1")
	public Date findMinEnd(String parentIds);

	/**
	 * 根据父节点路径查找最大预计结束时间
	 * @param parentIds
	 * @return
	 */
	@Query("select max(t.antipateEndTime) from Task t where t.parentIds like ?1")
	public Date findMaxEnd(String parentIds);
}

