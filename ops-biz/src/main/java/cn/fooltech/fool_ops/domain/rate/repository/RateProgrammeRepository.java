package cn.fooltech.fool_ops.domain.rate.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgramme;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface RateProgrammeRepository extends FoolJpaRepository<RateProgramme, String>, 
	FoolJpaSpecificationExecutor<RateProgramme> {

	/**
	 * 根据参数查找分页
	 * @param searchKey
	 * @param pageRequest
	 * @return
	 */
	public default Page<RateProgramme> findPageBy(String orgId,String accId, String searchKey, Pageable page){
		return findAll(new Specification<RateProgramme>(){

			@Override
			public Predicate toPredicate(Root<RateProgramme> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				if(!Strings.isNullOrEmpty(searchKey)){
					predicates.add(builder.like(root.get("name"), PredicateUtils.getAnyLike(searchKey)));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
			
		}, page);
		
	}

	
}	

