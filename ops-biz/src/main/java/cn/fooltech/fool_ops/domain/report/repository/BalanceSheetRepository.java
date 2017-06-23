package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.report.entity.BalanceSheet;

public interface BalanceSheetRepository extends JpaRepository<BalanceSheet, String>, 
	JpaSpecificationExecutor<BalanceSheet> {

	/**
	 * 根据财务会计期间查找
	 * @param periodId
	 * @return
	 */
	@Query("select b from BalanceSheet b where b.period.fid=?1")
	public List<BalanceSheet> findByPeriodId(String periodId);
	
	/**
	 * 根据财务会计期间查找
	 * @param periodId
	 * @return
	 */
	@Query("select b from BalanceSheet b where b.period.fid=?1")
	public List<BalanceSheet> findByPeriodId(String periodId, Sort sort);

	/**
	 * 根据财务会计期间统计
	 * @param periodId
	 * @return
	 */
	@Query("select count(*) from BalanceSheet b where b.period.fid=?1")
	public Long countByPeriodId(String periodId);
	
	/**
	 * 根据条件统计
	 * @return
	 */
	public default Long countByTag(int tag, String periodId, String excludeId, Integer assetNumber, Integer debitNumber){
		return count(new Specification<BalanceSheet>() {
			
			@Override
			public Predicate toPredicate(Root<BalanceSheet> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList(); 
				
				if(tag==4){
					predicates.add(periodId(periodId, root, builder));
					predicates.add(excludeId(excludeId, root, builder));
					predicates.add(eqDebitOrAssetNumber(debitNumber, root, builder));
				}else if(tag==3){
					predicates.add(periodId(periodId, root, builder));
					predicates.add(excludeId(excludeId, root, builder));
					predicates.add(eqDebitOrAssetNumber(assetNumber, root, builder));
				}else if(tag==2){
					predicates.add(periodId(periodId, root, builder));
					predicates.add(eqDebitOrAssetNumber(debitNumber, root, builder));
				}else if(tag==1){
					predicates.add(periodId(periodId, root, builder));
					predicates.add(eqDebitOrAssetNumber(assetNumber, root, builder));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 获取会计期间ID的查询条件
	 * @param id
	 * @return
	 */
	public default Predicate periodId(String periodId, Root<BalanceSheet> root, CriteriaBuilder builder){
		return builder.equal(root.get("period").get("fid"), periodId);
	}
	
	/**
	 * 获取排除ID的查询条件
	 * @param id
	 * @return
	 */
	public default Predicate excludeId(String id, Root<BalanceSheet> root, CriteriaBuilder builder){
		return builder.notEqual(root.get("fid"), id);
	}
	
	/**
	 * 获取资产号的查询条件
	 * @param id
	 * @return
	 */
	public default Predicate eqAssetNumber(Integer assetNumber, Root<BalanceSheet> root, CriteriaBuilder builder){
		return builder.equal(root.get("assetNumber"), assetNumber);
	}
	
	/**
	 * 获取负债号的查询条件
	 * @param id
	 * @return
	 */
	public default Predicate eqDebitNumber(Integer debitNumber, Root<BalanceSheet> root, CriteriaBuilder builder){
		return builder.equal(root.get("debitNumber"), debitNumber);
	}
	
	/**
	 * 获取负债号or资产号的查询条件
	 * @param id
	 * @return
	 */
	public default Predicate eqDebitOrAssetNumber(Integer number, Root<BalanceSheet> root, CriteriaBuilder builder){
		return builder.or(eqAssetNumber(number, root, builder), eqDebitNumber(number, root, builder));
	}
}
