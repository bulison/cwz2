package cn.fooltech.fool_ops.domain.payment.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentCheck;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface PaymentCheckRepository extends JpaRepository<PaymentCheck, String>, JpaSpecificationExecutor<PaymentCheck>{

	/**
	 * 根据单据ID查询勾对记录
	 * @param billId  单据ID
	 * @return
	 */
	@Query("select count(*) from PaymentCheck p where p.billId=?1 or p.paymentBill.fid=?2 ")
	public Long countByBillId(String billId,String paymentBill);

	/**
	 * 获取仓储单据DetachedCriteria
	 */
	public default void addFilter(List<Predicate> predicates, String accId, String billId, String checkStartDay, 
			String checkEndDay, String checkBillCode, Integer checkBillType, Root<PaymentBill> root, 
			CriteriaBuilder builder){
		
		List<String> excludeIds = findBillIdsByPaymentId(billId);
		
		predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
		predicates.add(builder.equal(root.get("recordStatus"), WarehouseBill.STATUS_AUDITED));
		predicates.add(builder.not(root.get("fid").in(excludeIds)));
		
		//单号搜索
		if(StringUtils.isNotBlank(checkBillCode)){
			predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(checkBillCode)));
		}
		//单据日期搜索
		if(!Strings.isNullOrEmpty(checkStartDay)){
			predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), DateUtilTools.string2Date(checkStartDay)));
		}
		if(!Strings.isNullOrEmpty(checkEndDay)){
			predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), DateUtilTools.string2Date(checkEndDay)));
		}
	}
	
	/**
	 * 根据收付款单ID查询单据ID
	 * @param paymentId
	 * @return
	 */
	@Query("select c.billId from PaymentCheck c where c.paymentBill.fid=?1")
	public List<String> findBillIdsByPaymentId(String paymentId);

	/**
	 * 根据仓储单据ID查找所有收付款记录
	 */
	@Query("select c.paymentBill from PaymentCheck c where c.fiscalAccount.fid=?1 and c.billId=?2 and c.paymentBill.billType=?3")
	public Page<PaymentBill> queryByWarehgouseBillId(String accId, String wbillId, Integer billType,
			Pageable page);

	/**
	 * 根据账套ID和收付款ID查找分页
	 * @param accId
	 * @param paybillId
	 * @param pageRequest
	 * @return
	 */
	@Query("select c from PaymentCheck c where c.fiscalAccount.fid=?1 and (c.paymentBill.fid=?2 or c.billId=?2)")
	public Page<PaymentCheck> findPageByPaybillId(String accId, String paybillId, Pageable page);
	
	/**
	 * 根据收付款单ID查询勾对单据IDs
	 * @param paymentId
	 * @return
	 */
	@Query("select p.billId from PaymentCheck p where p.paymentBill.fid=?1")
	public List<String> findCheckedBillIdsByPaymentId(String paymentId);
	
	/**
	 * 根据收付款id获取勾对集合
	 * @param paymentId
	 * @return
	 */
	@Query("select c from PaymentCheck c where c.paymentBill.fid=?1")
	public List<PaymentCheck> findByPaymentId(String paymentId);
	
	/**
	 * 根据单据id获取勾对集合
	 * @param billId
	 * @return
	 */
	@Query("select c from PaymentCheck c where c.billId=?1")
	public List<PaymentCheck> findByBillId(String billId);

}
