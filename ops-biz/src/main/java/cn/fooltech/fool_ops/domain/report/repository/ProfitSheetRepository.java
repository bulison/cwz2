package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.BalanceSheet;
import cn.fooltech.fool_ops.domain.report.entity.ProfitSheet;

public interface ProfitSheetRepository extends JpaRepository<ProfitSheet, String>{


	/**
	 * 根据财务会计期间查找
	 * @param periodId
	 * @return
	 */
	@Query("select b from ProfitSheet b where b.period.fid=?1")
	public List<ProfitSheet> findByPeriodId(String periodId);
	
	/**
	 * 根据财务会计期间查找
	 * @param periodId
	 * @return
	 */
	@Query("select b from ProfitSheet b where b.period.fid=?1")
	public List<ProfitSheet> findByPeriodId(String periodId, Sort sort);

	/**
	 * 根据财务会计期间统计
	 * @param periodId
	 * @return
	 */
	@Query("select count(*) from ProfitSheet b where b.period.fid=?1")
	public Long countByPeriodId(String periodId);

	/**
	 * 根据行号和会计期间统计
	 * @param number
	 * @param periodId
	 * @return
	 */
	@Query("select count(*) from ProfitSheet b where b.period.fid=?2 and b.number=?1")
	public Long countByNumberAndPeriodId(Integer number, String periodId);
	
	/**
	 * 根据行号,会计期间,排除ID统计
	 * @param number
	 * @param periodId
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from ProfitSheet b where b.period.fid=?2 and b.number=?1 and b.fid!=?3")
	public Long countByNumberAndPeriodId(Integer number, String periodId, String excludeId);
}
