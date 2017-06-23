package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.fiscal.entity.AccrueTaxation;

public interface AccrueTaxationRepository extends JpaRepository<AccrueTaxation, String>, JpaSpecificationExecutor<AccrueTaxation> {

	/**
	 * 根据账套ID查找
	 * @param accId
	 * @return
	 */
	@Query("select a from AccrueTaxation a where a.fiscalAccount.fid=?1 order by a.updateTime desc")
	public List<AccrueTaxation> findByAccId(String accId);

	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from AccrueTaxation a where a.baseSubject.fid=?1 or a.collectSubject.fid=?1 or a.paySubject.fid=?1 or a.addSubject.fid=?1")
	public Long countBySubjectId(String subjectId);
}
