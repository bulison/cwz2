package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.BalanceSheetFormula;

public interface BalanceSheetFormulaRepository extends JpaRepository<BalanceSheetFormula, String>{

	/**
	 * 根据账套ID查找
	 * @param accId
	 * @return
	 */
	@Query("select f from BalanceSheetFormula f where f.fiscalAccount.fid=?1")
	public List<BalanceSheetFormula> findByAccId(String accId);

	
}
