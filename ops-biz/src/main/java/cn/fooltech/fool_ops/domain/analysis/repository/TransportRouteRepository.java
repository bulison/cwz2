package cn.fooltech.fool_ops.domain.analysis.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.analysis.entity.TransportRoute;
import cn.fooltech.fool_ops.domain.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface TransportRouteRepository
		extends FoolJpaRepository<TransportRoute, String>, FoolJpaSpecificationExecutor<TransportRoute> {

	/**
	 * 根据账套ID查询
	 * @param accId
	 * @return
	 */
	@Query("select a from TransportRoute a where a.fiscalAccount.fid=?1")
	public List<TransportRoute> findByAccId(String accId);

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<TransportRoute> findPageBy(String accId, TransportRouteVo vo,
			Pageable pageable) {
		return findAll(new Specification<TransportRoute>() {
			@Override
			public Predicate toPredicate(Root<TransportRoute> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
}
