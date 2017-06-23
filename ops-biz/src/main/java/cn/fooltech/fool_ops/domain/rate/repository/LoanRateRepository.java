package cn.fooltech.fool_ops.domain.rate.repository;


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

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRate;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

public interface LoanRateRepository extends FoolJpaRepository<LoanRate, String>, 
	FoolJpaSpecificationExecutor<LoanRate> {

	@Query("select l from LoanRate l where l.org.fid=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public LoanRate findTopBy(String orgId);//查找银行贷款利率

	public Long countByOrgFid(String orgId);//统计记录总数

	public  List<LoanRate>findByOrgFid(String orgId);//获取记录集

	public  List<LoanRate>deleteByOrgFid(String orgId);//删除所有记录

	public default Page<LoanRate> findPageBy(String orgId,Pageable page){
		return this.findAll(new Specification<LoanRate>() {

			@Override
			public Predicate toPredicate(Root<LoanRate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

	/**
	 * 根据时间查找银行贷款利率
	 * @param orgId
	 * @param date
	 * @return
	 *//*
	@Query("select l from LoanRate l where l.org.fid=?1 and l.date<=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public LoanRate findTopBy(String orgId, Date date);

	*//**
	 * 根据日期计算总数
	 * @param orgId
	 * @param date
	 * @param excludeId
	 * @return
	 *//*
	@Query("select count(*) from LoanRate l where l.org.fid=?1 and l.date=?2 and l.fid!=?3")
	public Long countByDate(String orgId, Date date, String excludeId);

	*//**
	 * 根据日期计算总数
	 * @param orgId
	 * @param date
	 * @param excludeId
	 * @return
	 *//*
	@Query("select count(*) from LoanRate l where l.org.fid=?1 and l.date=?2")
	public Long countByDate(String orgId, Date date);

	public default Page<LoanRate> findPageBy(String orgId, Date date, Pageable page){
		return this.findAll(new Specification<LoanRate>() {
			
			@Override
			public Predicate toPredicate(Root<LoanRate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				if(date != null){
					predicates.add(builder.equal(root.get("date"), date));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}*/
}	

