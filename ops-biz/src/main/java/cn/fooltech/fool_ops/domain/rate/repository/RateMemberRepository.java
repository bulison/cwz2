package cn.fooltech.fool_ops.domain.rate.repository;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.rate.entity.RateMember;
import cn.fooltech.fool_ops.domain.rate.entity.RateMemberSum;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface RateMemberRepository extends FoolJpaRepository<RateMember, String>, 
	FoolJpaSpecificationExecutor<RateMember> {

	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<RateMember> query(RateMember vo,Pageable pageable,Date startDate, Date endDate,List<String> memberIds){
		Page<RateMember> findAll = findAll(new Specification<RateMember>() {
			
			@Override
			public Predicate toPredicate(Root<RateMember> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.between(root.get("eventPlanStartDate"), startDate,endDate));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	
	public default Page<RateMember> queryUserEvents(String accId, String userId, Date startDate, Date endDate, Pageable pageable){
		Page<RateMember> findAll = findAll(new Specification<RateMember>() {
			
			@Override
			public Predicate toPredicate(Root<RateMember> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				
				predicates.add(builder.equal(root.get("accId"), accId));
				
				if(StringUtils.isNotBlank(userId)){
					predicates.add(builder.equal(root.get("user").get("fid"), userId));
				}
				if(startDate != null){
					predicates.add(builder.equal(root.get("eventPlanEndDate"), startDate));
				}
				if(endDate != null) {
					predicates.add(builder.equal(root.get("eventPlanEndDate"), endDate));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}

	@Query("select a from RateMember a where event.fid=?1")
	public List<RateMember> queryByEventId(String eventId);
	
	/**
	 * 查找统计分页
	 * @param startDate
	 * @param endDate
	 * @param memberIds
	 * @param accId
	 * @param page
	 * @return
	 */
	public default Page<RateMemberSum> query(Date startDate, Date endDate, List<String> memberIds, String accId, Pageable page) {
		StringBuilder pre = new StringBuilder("SELECT r.fmember_id AS userId, u.user_name AS userName, ");
		pre.append("SUM(ABS(DATEDIFF(FEVENT_PLAN_END_DATE, FEVENT_PLAN_START_DATE)) + 1) AS allTime, ");
		pre.append("SUM(FEVENTS_NUMBER) AS eventsNum, ");
		pre.append("SUM(IF(FDELAY_NUMBER = 1, 1, 0)) AS delayNum, ");
		pre.append("IF(FIS_COMPLETE = 0, SUM(DATEDIFF(CURDATE() + 1, FEVENT_PLAN_END_DATE)), SUM(DATEDIFF(FEVENT_COMPLETE_DATE, FEVENT_PLAN_END_DATE))) AS delayTime ");
		
		StringBuilder mid = new StringBuilder();
		
		mid.append("FROM rate_member r INNER JOIN smg_tuser u ON r.fmember_id = u.fid ");
		mid.append("where 1 = 1 ");
		if(startDate != null) {
			mid.append(" and r.fevent_plan_end_date >= :startDate ");
		}
		if(endDate != null) {
			mid.append(" and r.fevent_plan_end_date <= :endDate ");
		}
		mid.append("and r.FACC_ID=:accId");
		String countSql = "SELECT COUNT(*) "+mid;
		javax.persistence.Query query = getEntityManager().createNativeQuery(countSql);
		if(StringUtils.isNotBlank(accId)) query.setParameter("accId", accId);
		if(startDate != null) query.setParameter("startDate", startDate);
		if(endDate != null) query.setParameter("endDate", endDate);
		Object result = query.getSingleResult();
		Long count = Long.valueOf(String.valueOf(result));
		if(count == 0) {
			return new PageImpl(Collections.emptyList(), page, 0);
		}
		
		mid.append(" group by r.fmember_id limit :start, :pageSize ");
		
		javax.persistence.Query query2 = getEntityManager().createNativeQuery(mid.toString());
		query2.unwrap(RateMemberSum.class);
		if(startDate != null) query2.setParameter("startDate", startDate);
		if(endDate != null) query2.setParameter("endDate", endDate);
		query2.setParameter("start", page.getOffset());
		query2.setParameter("pageSize", page.getPageSize());
		query2.setParameter("accId", accId);
		
		List<RateMemberSum> list = query2.getResultList();
		
		Page<RateMemberSum> pageResult = new PageImpl(list, page, 0);
		return pageResult;
	}
	/**
	 * 根据人员ID,事件ID查询
	 */
	@Query("select r from RateMember r where r.user.fid=?1 and r.event.fid=?2 and r.isComplete=?3 and r.orgId=?4")
	public RateMember queryByUserAndTask(String userId,String taskId,int isComplete,String orgId);
	
	/**
	 * 数据预处理
	 */
	//把FEVENTS_NUMBER值设置为0
	@Modifying
	@Query("update RateMember r set r.isCalc=0 where r.orgId=?1")
	public int dataPreprocessIsCalc(String orgId);
	//通过事件计划完成日期查找出IsCalc需要计算的部分(IsCalc=1)
	@Modifying  
	@Query("update RateMember r set r.eventsNumber=1 , r.eventCompleteDate=?1 , r.isCalc=1 where r.eventPlanEndDate<=?1 and r.isComplete=0 and orgId=?2")
	public int updateIsCalcByPlanEndDate(Date date,String orgId);
	//找出IsCalc=1的事件
	@Query("select r from RateMember r where r.isCalc=1 and r.orgId=?1")
	public List<RateMember> findByisCalcY(String orgId);
	//查询FIS_COMPLETE=1或FIS_COMPLETE=2的事件
	@Query("select r from RateMember r where r.orgId=?1  and (r.isComplete=1 or r.isComplete=2)")
	public  List<RateMember> findByIsComplete(String orgId);
}

