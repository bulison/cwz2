package cn.fooltech.fool_ops.domain.wage.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;

/**
 * 工资公式 持久层
 * 
 * @author cwz
 * @date 2016-10-27
 */
public interface WageFormulaRepository
		extends JpaRepository<WageFormula, String>, JpaSpecificationExecutor<WageFormula> {

	/**
	 * 查询工资公式列表信息，按照工资公式主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	public default Page<WageFormula> query(WageFormulaVo vo, String accountId, Pageable pageable) {

		return findAll(new Specification<WageFormula>() {
			@Override
			public Predicate toPredicate(Root<WageFormula> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();

				if (StringUtils.isNotBlank(accountId)) {
					predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);

	}

	/**
	 * 根据账套查找
	 * 
	 * @param fiscalAccountId
	 * @return
	 */
	public default List<WageFormula> getByAccountId(String fiscalAccountId, Short isView) {
		Sort sort = new Sort(Direction.ASC, "orderNo");
		List<WageFormula> findAll = findAll(new Specification<WageFormula>() {
			@Override
			public Predicate toPredicate(Root<WageFormula> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), fiscalAccountId));
				if (isView != null) {
					predicates.add(builder.equal(root.get("isView"), isView));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
		return findAll;
	}

	public default List<WageFormula> queryByCriteria(WageFormulaVo vo,String accountId) {
		List<WageFormula> findAll = findAll(new Specification<WageFormula>() {
			@Override
			public Predicate toPredicate(Root<WageFormula> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (StringUtils.isNotBlank(vo.getFid())) {
					predicates.add(builder.notEqual(root.<String>get("fid"), vo.getFid()));
				}
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return findAll;
	}

	/**
	 * 根据属性列名统计
	 * 
	 * @param selfFid
	 * @param criterion
	 * @return
	 */
	public default long countByColumnName(WageFormulaVo vo,String accountId) {
		List<WageFormula> findAll = findAll(new Specification<WageFormula>() {
			@Override
			public Predicate toPredicate(Root<WageFormula> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (StringUtils.isNotBlank(vo.getFid())) {
					predicates.add(builder.notEqual(root.<String>get("fid"), vo.getFid()));
				}
				if (StringUtils.isNotBlank(vo.getColumnName())) {
					predicates.add(builder.equal(root.<String>get("columnName"), vo.getColumnName()));
				}
				if (StringUtils.isNotBlank(accountId)) {
					predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return findAll != null ? findAll.size() : 0;
	}
	/**
	 * 根据属性列序号统计
	 * 
	 * @param selfFid
	 * @param criterion
	 * @return
	 */
	public default long countByColumnNo(WageFormulaVo vo,String accountId) {
		List<WageFormula> findAll = findAll(new Specification<WageFormula>() {
			@Override
			public Predicate toPredicate(Root<WageFormula> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (StringUtils.isNotBlank(vo.getFid())) {
					predicates.add(builder.notEqual(root.<String>get("fid"), vo.getFid()));
				}
				if (vo.getOrderNo()!=null) {
					predicates.add(builder.equal(root.<Integer>get("orderNo"), vo.getOrderNo()));
				}
				if (StringUtils.isNotBlank(accountId)) {
					predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return findAll != null ? findAll.size() : 0;
	}
}
