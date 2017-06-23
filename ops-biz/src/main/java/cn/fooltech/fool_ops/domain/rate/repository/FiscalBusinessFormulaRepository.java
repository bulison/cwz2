package cn.fooltech.fool_ops.domain.rate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.rate.entity.FiscalBusinessFormula;

public interface FiscalBusinessFormulaRepository extends FoolJpaRepository<FiscalBusinessFormula,String>,FoolJpaSpecificationExecutor<FiscalBusinessFormula>{
	/**
	 * 根据账套ID查找
	 */
	@Query("select f from FiscalBusinessFormula f where f.fiscalAccount.fid=?1")
	public List<FiscalBusinessFormula> findByAccountId(String accid);
	/**
	 * 根据账套ID和ORGID查找是否已经有公式
	 */
	@Query("select count(*) from FiscalBusinessFormula f where f.fiscalAccount.fid=?1 and f.org.fid=?2")
	public Long existsFormula(String accId,String orgId);
}
