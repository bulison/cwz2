package cn.fooltech.fool_ops.domain.capital.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanChangeLog;

public interface CapitalPlanChangeLogRepository extends JpaRepository<CapitalPlanChangeLog,String>{
/**
 * 查找所有资金计划日志并分页
 */
	public default Page<CapitalPlanChangeLog> query(Pageable pageable){
		return findAll(pageable);
	};
	
	/**
	 *  根据明细id查询日志
	 * @param detailId 明细id
	 * @return
	 */
	@Query("select a from CapitalPlanChangeLog a where detail.id=?1")
	public List<CapitalPlanChangeLog> queryByDetailId(String detailId);
}
