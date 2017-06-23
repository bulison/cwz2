package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.warehouse.entity.OutStorage;

/**
 * 出仓Repository
 * @author xjh
 *
 */
public interface OutStorageRepository extends JpaRepository<OutStorage, String>, JpaSpecificationExecutor<OutStorage>{

	/**
	 * 根据入库ID查询
	 * @param inStorgeId
	 * @return
	 */
	@Query("select o from OutStorage o where o.inStorage.fid=?1")
	public List<OutStorage> findByInStorageId(String inStorgeId);
	
	/**
	 * 根据入库明细ID查询记录数
	 * @param inStorgeId
	 * @return
	 */
	@Query("select count(*) from OutStorage o where o.billDetailIn.fid=?1")
	public Long countByBillDetailInId(String billDetailId);
	
	/**
	 * 根据出库明细ID查询
	 * @param billDetailId
	 * @return
	 */
	@Query("select o from OutStorage o where o.billDetailOut.fid=?1")
	public List<OutStorage> findByBillDetailOutId(String billDetailId);
	
	/**
	 * 根据入库明细ID查询
	 * @param billDetailId
	 * @return
	 */
	@Query("select o from OutStorage o where o.billDetailIn.fid=?1")
	public List<OutStorage> findByBillDetailInId(String billDetailId);

	/**
	 * 根据入库ID查询入库总金额
	 * @param inStorgeId
	 * @return
	 */
	@Query("select sum(o.amountIn) from OutStorage o where o.inStorage.fid=?1")
	public BigDecimal getTotalAmountInByInStorageId(String inStorgeId);
	
	/**
	 * 根据出库ID查询入库总金额
	 * @param inStorgeId
	 * @return
	 */
	@Query("select sum(o.amountIn) from OutStorage o where o.billDetailOut.fid=?1")
	public BigDecimal getTotalAmountInByBillDetailOutId(String billDetailId);
	
	/**
	 * 根据出库ID查询入库总金额
	 * @param inStorgeId
	 * @return
	 */
	@Query("select sum(o.amountIn) from OutStorage o where o.billDetailOut.fid=?1 and o.billDetailOut.inWareHouse.fid=?2")
	public BigDecimal getTotalAmountInByBillDetailOutId(String billDetailId, String warehouseId);
	
	/**
	 * 根据出库IDs查询入库总金额
	 * @param inStorgeId
	 * @return
	 */
	@Query("select sum(o.amountIn) from OutStorage o where o.billDetailOut.fid in ?1")
	public BigDecimal getTotalAmountInByBillDetailOutIds(List<String> billDetailIds);
	
	/**
	 * 获取某张单据的出库记录
	 * @param billId 仓库单据ID
	 * @return
	 */
	@Query("select o from OutStorage o where o.billDetailOut.bill.fid=?1")
	public List<OutStorage> findByBillId(String billId);
	
	/**
	 * 获取出库记录
	 * @param billDetailId 单据明细ID(入库)
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public default List<OutStorage> findByDetailIn(String billDetailId, String goodsId, String specId, Sort sort){
		return findAll(new Specification<OutStorage>() {
			
			@Override
			public Predicate toPredicate(Root<OutStorage> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("billDetailIn").get("fid"), billDetailId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				if(Strings.isNullOrEmpty(specId)){
					predicates.add(builder.isNull(root.get("goodsSpec")));
				}else{
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), specId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 获取出库记录
	 * @param billDetailId 单据明细ID(出库)
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public default List<OutStorage> findByDetailOut(String billDetailId, String goodsId, String specId, Sort sort){
		return findAll(new Specification<OutStorage>() {
			
			@Override
			public Predicate toPredicate(Root<OutStorage> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("billDetailOut").get("fid"), billDetailId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				if(Strings.isNullOrEmpty(specId)){
					predicates.add(builder.isNull(root.get("goodsSpec")));
				}else{
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), specId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
}
