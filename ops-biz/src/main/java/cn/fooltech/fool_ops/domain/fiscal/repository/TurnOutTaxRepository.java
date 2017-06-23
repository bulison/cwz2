package cn.fooltech.fool_ops.domain.fiscal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.fiscal.entity.TurnOutTax;

public interface TurnOutTaxRepository extends JpaRepository<TurnOutTax, String>{

	/**
	 * 根据账套查找数据
	 * @param accId
	 * @return
	 */
	@Query("select t from TurnOutTax t where t.fiscalAccount.fid=?1")
	public TurnOutTax findByAccountId(String accId);

	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from TurnOutTax a where a.outSubject.fid=?1 or a.inSubject.fid=?1 or a.taxSubject.fid=?1 or a.unpaidSubject.fid=?1 or a.paySubject.fid=?1")
	public Long countBySubjectId(String subjectId);

}
