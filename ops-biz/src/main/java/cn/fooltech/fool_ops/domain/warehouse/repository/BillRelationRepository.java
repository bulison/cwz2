package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;

/**
 * 单据关联Repository
 * @author xjh
 *
 */
public interface BillRelationRepository extends JpaRepository<BillRelation, String>{

	/**
	 * 根据billId查询仓储单据关联
	 * @param billId
	 * @return
	 */
	public List<BillRelation> findByBillId(String billId);
	
	/**
	 * 根据billId和状态查询仓储单据关联
	 * @param billId
	 * @return
	 */
	public List<BillRelation> findByBillIdAndRecordStatus(String billId, int status);
	
	/**
	 * 根据refBillId查询仓储单据关联
	 * @param refBillId
	 * @return
	 */
	public List<BillRelation> findByRefBillId(String refBillId);

	/**
	 * 根据refBillIds查询仓储单据关联
	 * @param refBillIds
	 * @return
	 */
	@Query("select b from BillRelation b where b.refBillId in ?1 and b.recordStatus!="+BillRelation.CALCLE)
	public List<BillRelation> findByRefBillIdIn(List<String> refBillIds);

	/**
	 * 根据refBillId和关联状态查询
	 * @param refBillId
	 * @return
	 */
	@Query("select count(*) from BillRelation b where b.refBillId=?1 and b.recordStatus!="+BillRelation.CALCLE)
	public Long countByBillId(String refBillId);
}
