package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpenses;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface PrepaidExpensesRepository extends JpaRepository<PrepaidExpenses, String>, JpaSpecificationExecutor<PrepaidExpenses> {

	/**
	 * 根据账套ID查找
	 * @param accId
	 * @return
	 */
	@Query("select a from PrepaidExpenses a where a.fiscalAccount.fid=?1 order by a.updateTime desc")
	public List<PrepaidExpenses> findByAccId(String accId);
	
	/**
	 * 同一账套下的是否有其他相同的expensesCode
	 * @param selfId
	 * @param expensesCode
	 * @param accId
	 * @return
	 */
	@Query("select count(a) from PrepaidExpenses a where a.fid!=?1 and expensesCode=?2 and fiscalAccount.fid=?3")
	public long countByExpensesCode(String selfId, String expensesCode, String accId);
	
	/**
	 * 查询待摊费用列表信息，按照待摊费用主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<PrepaidExpenses> query(PrepaidExpensesVo prepaidExpensesVo,Pageable pageable){
		Page<PrepaidExpenses> findAll = findAll(new Specification<PrepaidExpenses>() {
			
			@Override
			public Predicate toPredicate(Root<PrepaidExpenses> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
}
