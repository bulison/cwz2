package cn.fooltech.fool_ops.domain.rate.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRate;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRateLog;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface LoanRateLogRepository extends FoolJpaRepository<LoanRateLog, String>,
	FoolJpaSpecificationExecutor<LoanRateLog> {

	public default Page<LoanRateLog> findPageBy(String orgId, Pageable page){
		return this.findAll(new Specification<LoanRateLog>() {
			
			@Override
			public Predicate toPredicate(Root<LoanRateLog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
}	

