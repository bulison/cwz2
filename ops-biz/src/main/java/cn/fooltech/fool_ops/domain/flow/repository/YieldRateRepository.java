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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.flow.entity.YieldRate;
import cn.fooltech.fool_ops.domain.flow.vo.YieldRateVo;
/**
 * 
 * <p>流程计划每天收益率 持久层</p>  
 * @author cwz
 * @date 2017年4月12日
 */
public interface YieldRateRepository
		extends FoolJpaRepository<YieldRate, String>, FoolJpaSpecificationExecutor<YieldRate> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<YieldRate> findPageBy(String accId,YieldRateVo vo, Pageable pageable) {
		return findAll(new Specification<YieldRate>() {
			@Override
			public Predicate toPredicate(Root<YieldRate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("accId"), accId));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getPlanId())) {
					predicates.add(builder.equal(root.get("plan").get("fid"), vo.getPlanId()));
				}
				
				if (vo.getDate()!=null) {
					predicates.add(builder.equal(root.get("date"), vo.getDate()));
				}

				if (!Strings.isNullOrEmpty(vo.getOrgId())) {
					predicates.add(builder.equal(root.get("orgId"), vo.getOrgId()));
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
	
	/**
	 * 根据流程ID和时间查询日收益率
	 * @param planId
	 * @param currDate
	 * @return
	 */
	@Query("select a from YieldRate a where plan.fid=?1 and date=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public YieldRate queryByPlanAndDate(String planId,Date currentDate);
	
	/**
	 * 根据id获取每天收益率
	 * @param planId
	 * @return
	 */
	@Query("select a from YieldRate a where plan.fid=?1 order by date")
	public List<YieldRate> queryByPlan(String planId);
	
	
}
