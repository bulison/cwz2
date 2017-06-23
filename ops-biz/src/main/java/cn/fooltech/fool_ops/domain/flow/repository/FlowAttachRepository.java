package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.FlowAttach;
import cn.fooltech.fool_ops.utils.SecurityUtil;
/**
 * 流程附件类 持久层
 * @author cwz
 *
 */
public interface FlowAttachRepository extends JpaRepository<FlowAttach, String>, FoolJpaSpecificationExecutor<FlowAttach> {


	public default Page<FlowAttach> query(FlowAttach vo,Pageable pageable){
		Page<FlowAttach> findAll = findAll(new Specification<FlowAttach>() {
			
			@Override
			public Predicate toPredicate(Root<FlowAttach> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	@Query("select a from FlowAttach a where taskId=?1 and flowRecordId is null")
	public List<FlowAttach> queryByTaskId(String taskId);
	
	@Query("select a from FlowAttach a where flowRecordId=?1 ")
	public List<FlowAttach> queryByflowRecordId(String flowRecordId);
	
	@Query("select a from FlowAttach a where planId=?1 and flowRecordId is null and  taskId is null")
	public List<FlowAttach> getFileMapByPlanId(String planId);
}	

