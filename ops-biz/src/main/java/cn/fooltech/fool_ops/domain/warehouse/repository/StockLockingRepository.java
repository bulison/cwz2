package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import cn.fooltech.fool_ops.domain.warehouse.entity.Pricing;

/**
 * 库存锁定Repository
 * @author xjh
 *
 */
public interface StockLockingRepository extends JpaRepository<Pricing, String>{

	/**
	 * 获取单据内所有货品的最大假锁天数
	 * @param billId 单据ID
	 * @return
	 */	
	@Procedure(procedureName="getFakeLockingDaysNew")
	public BigDecimal getMaxFakeLockingDays(@Param("billId") String billId);
	
	/**
	 * 根据草稿单，判断销售出货、采购退货单出货时，是否需要库存解锁权
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @param billId 单据ID
	 */
	@Procedure(procedureName="storckLockingDecideNew")
	public Integer isNeedUnclock(@Param("orgId") String orgId, @Param("accountId") String accountId, 
			@Param("billId") String billId);
}
