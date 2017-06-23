package cn.fooltech.fool_ops.domain.rate.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.rate.entity.FiscalBusiness;

public interface FiscalBusinessRepository extends FoolJpaRepository<FiscalBusiness,String>,FoolJpaSpecificationExecutor<FiscalBusiness>{
	
	/**
	 * 根据财务会计期间统计
	 * @param periodId
	 * @return
	 */
	@Query("select count(*) from FiscalBusiness f where f.period.fid=?1")
	public Long countByPeriodId(String periodId);
	/**
	 * 根据财务会记期间与行号统计
	 */
	@Query("select count(*) from FiscalBusiness f where f.period.fid=?1 and f.number=?2")
	public Long countByNumberAndPeriodId(String periodId,Integer number);
	/**
	 * 根据行号,会记期间,排除ID统计
	 */
	@Query("select count(*) from FiscalBusiness f where f.period.fid=?1 and f.number=?2 and f.fid!=?3")
	public Long countByNumberAndPeriodId(String periodId,Integer number,String excludeId);
	/**
	 * 根据财务会记期间查询
	 * 
	 */
	@Query("select f from FiscalBusiness f where f.period.fid=?1")
	public List<FiscalBusiness> findByPeriod(String periodId);

	/**
	 * 根据财务会记期间查询
	 * 
	 */
	@Query("select f from FiscalBusiness f where f.period.fid=?1")
	public List<FiscalBusiness> findByPeriod(String periodId,Sort sort);
}
