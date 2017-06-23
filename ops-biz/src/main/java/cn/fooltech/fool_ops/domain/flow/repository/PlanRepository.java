package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;

public interface PlanRepository extends JpaSpecificationExecutor<Plan>, FoolJpaRepository<Plan,String> {
	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<Plan> query(PlanVo vo,Pageable pageable,String excludeId){
		Page<Plan> findAll = findAll(new Specification<Plan>() {
			
			@Override
			public Predicate toPredicate(Root<Plan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.or(builder.equal(root.get("hide"), 1), builder.isNull(root.get("hide"))));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				
				//fid不为空时排除当前id
				/*if(StringUtils.isNotBlank(vo.getFid())){
					predicates.add(builder.notEqual(root.get("fid"),vo.getFid()));
				}*/
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"),excludeId));
				}

				//编号
				if(StringUtils.isNotBlank(vo.getCode())){
					
					predicates.add(builder.like(root.get("code"),PredicateUtils.getAnyLike(vo.getCode())));
				}
				//名称
				if(StringUtils.isNotBlank(vo.getName())){
					predicates.add(builder.like(root.get("name"),PredicateUtils.getAnyLike(vo.getName())));
				}
				//部门
				if(StringUtils.isNotBlank(vo.getDeptId())){
					predicates.add(builder.equal(root.get("dept").get("fid"), vo.getDeptId()));
				}
				//状态
				if(vo.getStatus() != null){
					predicates.add(builder.equal(root.get("status"), vo.getStatus()));
				}
				//计划开始时间
				if(StringUtils.isNotBlank(vo.getAntipateStartTime())){
					Date antipateStartTime = DateUtilTools.string2Date(vo.getAntipateStartTime());
					predicates.add(builder.greaterThanOrEqualTo(root.get("antipateStartTime"), antipateStartTime));
				}
				//计划结束时间
				if(StringUtils.isNotBlank(vo.getAntipateEndTime())){
					Date antipateEndTime = DateUtilTools.string2Date(vo.getAntipateEndTime());
					predicates.add(builder.lessThanOrEqualTo(root.get("antipateStartTime"), antipateEndTime));
				}
				//发起人
				if(StringUtils.isNotBlank(vo.getInitiaterId())){
					predicates.add(builder.equal(root.get("initiater").get("fid"), vo.getInitiaterId()));
				}
				//责任人
				if(StringUtils.isNotBlank(vo.getPrincipalerId())){
					predicates.add(builder.equal(root.get("principaler").get("fid"), vo.getPrincipalerId()));
				}
				//审核人
				if(StringUtils.isNotBlank(vo.getAuditerId())){
					predicates.add(builder.equal(root.get("auditer").get("fid"), vo.getAuditerId()));
				}
				//计划级别
				if(StringUtils.isNotBlank(vo.getPlanLevelId())){
					predicates.add(builder.equal(root.get("planLevel").get("fid"), vo.getPlanLevelId()));
				}
				//保密级别
				if(StringUtils.isNotBlank(vo.getSecurityLevelId())){
					predicates.add(builder.equal(root.get("securityLevel").get("fid"), vo.getSecurityLevelId()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 判断计划编号是否已存在
	 * @param accountId 财务账套ID
	 * @param code 计划编号
	 * @param excludeId 排除的计划ID
	 * @return
	 */
	public default boolean isCodeExist(String accountId, String code, String excludeId){
		long count = this.count(new Specification<Plan>() {
			
			@Override
			public Predicate toPredicate(Root<Plan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("code"), code));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		if(count>0){
			return true;
		}
		return false;
	}
	/**
	 * 删除计划
	 * @param planId 计划ID
	 */
	public default void deletePlan(String planId){
		String sql = "call delete_plan(:planId)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("planId", planId);
		query.executeUpdate();
	}
	/**
	 * 计划合并成功后，原计划的相关表都要更新计划id
	 * @param beforePlanId	原计划ID
	 * @param afterPlanId	修改后计划ID
	 */
	public default void updatePlanId(String beforePlanId,String afterPlanId){
		String sql = "call p_updatePlanId(:beforePlanId,:afterPlanId)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("beforePlanId", beforePlanId);
		query.setParameter("afterPlanId", afterPlanId);
		query.executeUpdate();
	}
	@Query("select a from Plan a where org.fid=?1 and fiscalAccount.fid=?2")
	public List<Plan> queryPlanList(String orgId, String fiscalAccountId);
//	@Query("select a from Supervise a where warnSetting.fid=?1 and type=?2")
//	public List<Supervise> queryBySettingId(String settingId, Integer type);
//
//	@Query("select a from Supervise a where warnSetting.fid=?1")
//	public List<Supervise> queryBywarnSetting(String settingId);
	
}	

