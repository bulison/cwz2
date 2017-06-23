package cn.fooltech.fool_ops.domain.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.payment.entity.WarehouseReturn;

public interface WarehouseReturnRepository extends JpaRepository<WarehouseReturn, String>{

	
	/**
	 * 根据收付款单ID查询单据ID
	 * @param paymentId
	 * @return
	 */
	@Query("select c from WarehouseReturn c where c.paymentBill.fid=?1")
	public List<WarehouseReturn> findBillIdsByPaymentId(String paymentId);
}
