package cn.fooltech.fool_ops.domain.cost.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.entity.CostBillCheck;
import cn.fooltech.fool_ops.utils.StringUtils;

public interface CostBillCheckRepository extends JpaRepository<CostBillCheck, String>, JpaSpecificationExecutor<CostBillCheck> {

	/**
	 * 根据费用单查找勾对记录
	 * @param costBillId
	 * @return
	 */
	@Query("select c from CostBillCheck c where c.costBill.fid=?1")
	public List<CostBillCheck> findByCostBillId(String costBillId);

	/**
	 * 
	 * @param wbillId
	 * @param page
	 * @return
	 */
	@Query("select c from CostBillCheck c where c.bill=?1")
	public Page<CostBillCheck> queryByWarehgouseBillId(String wbillId, Pageable page);
	/**
	 * 
	 * @param costBillId
	 * @param page
	 * @return
	 */
	@Query("select c from CostBillCheck c where c.costBill.fid=?1")
	public Page<CostBillCheck> queryByCostBillId(String costBillId, Pageable page);

	/**
	 * 根据参数查找分页
	 * @param accId
	 * @param page
	 * @return
	 */
	public default Page<CostBillCheck> findPageBy(String accId, String costBillId, Pageable page){
		return findAll(new Specification<CostBillCheck>() {
			
			@Override
			public Predicate toPredicate(Root<CostBillCheck> root, CriteriaQuery<?> query, 
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				if(StringUtils.isNotBlank(costBillId)){
					predicates.add(builder.equal(root.get("costBill").get("fid"), costBillId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

	/**
	 * 根据收付款单ID查询勾对单据IDs
	 * @param paymentId
	 * @return
	 */
	@Query("select p.bill from CostBillCheck p where p.costBill.fid=?1")
	public List<String> findCheckedBillIdsByCostBillId(String costBillId);

	/**
	 * 根据费用单ID统计
	 * @param costbillId
	 * @return
	 */
	@Query("select count(*) from CostBillCheck p where p.costBill.fid=?1")
	public Long countByCostBillId(String costbillId);
}
