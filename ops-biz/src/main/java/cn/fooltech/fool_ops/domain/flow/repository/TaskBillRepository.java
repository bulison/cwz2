package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.flow.entity.TaskBill;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface TaskBillRepository
		extends FoolJpaRepository<TaskBill, String>, FoolJpaSpecificationExecutor<TaskBill> {

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<TaskBill> findPageBy(TaskBillVo vo, Pageable pageable) {
		return findAll(new Specification<TaskBill>() {
			@Override
			public Predicate toPredicate(Root<TaskBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getPlanId())) {
					predicates.add(builder.equal(root.get("plan").get("fid"), vo.getPlanId()));
				}

				if (!Strings.isNullOrEmpty(vo.getTaskId())) {
					predicates.add(builder.equal(root.get("task").get("fid"), vo.getTaskId()));
				}

				if (vo.getBillSign() != null) {
					predicates.add(builder.equal(root.get("billSign"), vo.getBillSign()));
				}

				if (!Strings.isNullOrEmpty(vo.getBillId())) {
					predicates.add(builder.equal(root.get("bill"), vo.getBillId()));
				}

				// 单据日期搜索

				if (vo.getStartDay() != null) {
					Date startDate = vo.getStartDay();
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
				}
				if (vo.getEndDay() != null) {
					Date endDate = vo.getEndDay();
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
	@Query("select a from TaskBill a where task.fid=?1")
	public List<TaskBill> queryByTaskId(String taskId);
	
	@Query("select a from TaskBill a where plan.fid=?1")
	public List<TaskBill> queryByPlanId(String planId);
	
	@Modifying
	@Query("delete from TaskBill where task.fid=?1")
	public void delByTaskId(String taskId);
	
	/**
	 * 根据单据id查找计划事件关联单据
	 * @param bill 单据id
	 * @return
	 */
	@Query("select a from TaskBill a where bill=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TaskBill queryByRelation(String bill);
}
