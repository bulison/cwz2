package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.List;

import javax.persistence.criteria.*;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.asset.entity.AssetDetail;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpensesDetail;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesDetailVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface PrepaidExpensesDetailRepository extends JpaRepository<PrepaidExpensesDetail, String>, JpaSpecificationExecutor<PrepaidExpensesDetail> {

	/**
	 * 根据账套ID查找
	 * @param accId
	 * @return
	 */
	@Query("select a from PrepaidExpensesDetail a where a.fiscalAccount.fid=?1 order by a.updateTime desc")
	public List<PrepaidExpensesDetail> findByAccId(String accId);
	
	@Query("select a from PrepaidExpensesDetail a where a.expenses.fid=?1 ")
	public List<PrepaidExpensesDetail> findByExpensesId(String expensesId);
	
	/**
	 * 根据expensesId获取明细
	 * @return
	 */
	@Query("select a from PrepaidExpensesDetail a where a.expenses.fid=?1 ")
	public List<PrepaidExpensesDetail> queryByExpensesId(String expensesId,Sort sort);
	
	/**
	 * 获得查询接口
	 * @param vo
	 * @return
	 */
	public default Page<PrepaidExpensesDetail> query(PrepaidExpensesDetailVo vo,Pageable pageable){
		Page<PrepaidExpensesDetail> findAll = findAll(new Specification<PrepaidExpensesDetail>() {
			
			@Override
			public Predicate toPredicate(Root<PrepaidExpensesDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				//费用编号
				if(StringUtils.isNotBlank(vo.getExpensesCode())){
					predicates.add(builder.like(root.<String>get("expenses").get("expensesCode"), PredicateUtils.getAnyLike(vo.getExpensesCode())));
				}
				//费用编号
				if(StringUtils.isNotBlank(vo.getExpensesName())){
					predicates.add(builder.like(root.<String>get("expenses").get("expensesName"), PredicateUtils.getAnyLike(vo.getExpensesName())));
				}
				//单据
				if(vo.getStartDate() != null){
					predicates.add(builder.greaterThan(root.get("date"), vo.getStartDate()));
				}
				if(vo.getEndDate() != null){
					predicates.add(builder.lessThan(root.get("date"), vo.getEndDate()));
				}

				//过滤已生成过凭证的记录
				if(vo.getShowGened()!=null){
					Subquery<PrepaidExpensesDetail> subquery = query.subquery(PrepaidExpensesDetail.class);
					Root<VoucherBill> fromProject = subquery.from(VoucherBill.class);
					subquery.select(fromProject.get("fid"));
					subquery.where(
							builder.equal(fromProject.get("billId"), root.get("fid"))
					);

					if(vo.getShowGened()== Constants.SHOW){
						predicates.add(builder.exists(subquery));
					} if(vo.getShowGened()== Constants.NOTSHOW){
						predicates.add(builder.not(builder.exists(subquery)));
					}
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 获得查询接口总条数
	 * @param vo
	 * @return
	 */
	public default Long queryByCount(PrepaidExpensesDetailVo vo){
		
		long count = this.count(new Specification<PrepaidExpensesDetail>() {
			
			@Override
			public Predicate toPredicate(Root<PrepaidExpensesDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				//费用编号
				if(StringUtils.isNotBlank(vo.getExpensesCode())){
					predicates.add(builder.like(root.<String>get("expenses").get("expensesCode"), "'%"+vo.getExpensesCode()+"%'"));
				}
				//费用编号
				if(StringUtils.isNotBlank(vo.getExpensesName())){
					predicates.add(builder.like(root.<String>get("expenses").get("expensesName"), "'%"+vo.getExpensesName()+"%'"));
				}
				//单据
				if(vo.getStartDate() != null){
					predicates.add(builder.greaterThan(root.get("date"), vo.getStartDate()));
				}
				if(vo.getEndDate() != null){
					predicates.add(builder.lessThan(root.get("date"), vo.getEndDate()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return count;
	}

}
