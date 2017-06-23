package cn.fooltech.fool_ops.domain.cost.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.cost.entity.ExpenApplyBill;
import org.springframework.data.jpa.repository.Query;

public interface ExpenApplyBillRepository extends JpaRepository<ExpenApplyBill, String>, JpaSpecificationExecutor<ExpenApplyBill> {

	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param voucherCode
	 * @param billDate
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from ExpenApplyBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.date=?3 and b.fid!=?4")
	public Long countTodayVoucherCode(String accId, String voucherCode, Date billDate, String excludeId);


	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param voucherCode
	 * @param billDate
	 * @return
	 */
	@Query("select count(*) from ExpenApplyBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.date=?3")
	public Long countTodayVoucherCode(String accId, String voucherCode, Date billDate);


	/**
	 * 根据参数查找分页
	 * @param accId
	 * @param startDate
	 * @param endDate
	 * @param deptId
	 * @param memberId
	 * @param page
	 * @return
	 */
	public default Page<ExpenApplyBill> findPageBy(String accId, Date startDate, Date endDate, String deptId, String memberId,
			Pageable page){
		return findAll(new Specification<ExpenApplyBill>() {
			
			@Override
			public Predicate toPredicate(Root<ExpenApplyBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据日期搜索
				if(startDate != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("date"), startDate));
				}
				if(endDate != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("date"), endDate));
				}
				//业务部门搜索
				if(StringUtils.isNotBlank(deptId)){
					predicates.add(builder.equal(root.get("dept").get("fid"), deptId));
				}
				//申请人
				if(StringUtils.isNotBlank(memberId)){
					predicates.add(builder.equal(root.get("member").get("fid"), memberId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

}
