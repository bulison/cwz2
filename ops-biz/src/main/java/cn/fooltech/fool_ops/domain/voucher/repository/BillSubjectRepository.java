package cn.fooltech.fool_ops.domain.voucher.repository;

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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;

public interface BillSubjectRepository extends JpaRepository<BillSubject, String>, JpaSpecificationExecutor<BillSubject> {

	/**
	 * 根据账套ID和单据类型查询
	 * @param accId
	 * @param billType
	 * @return
	 */
	public default Page<BillSubject> findPageBy(String accId, Integer billType, Pageable page){
		return findAll(new Specification<BillSubject>() {
			
			@Override
			public Predicate toPredicate(Root<BillSubject> root, CriteriaQuery<?> query, 
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				//单据类型
				if(billType != null){
					predicates.add(builder.equal(root.get("billType"), billType));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

}
