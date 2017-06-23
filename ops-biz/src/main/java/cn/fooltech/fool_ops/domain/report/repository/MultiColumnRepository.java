package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumn;

public interface MultiColumnRepository extends JpaRepository<FiscalMultiColumn, String>, 
	JpaSpecificationExecutor<FiscalMultiColumn> {

	/**
	 * 根据账套ID查找
	 */
	@Query("select m from FiscalMultiColumn m where m.fiscalAccount.fid=?1")
	public List<FiscalMultiColumn> findByAccId(String accId);
}
