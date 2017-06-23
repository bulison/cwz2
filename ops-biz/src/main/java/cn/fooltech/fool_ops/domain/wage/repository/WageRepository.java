package cn.fooltech.fool_ops.domain.wage.repository;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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

import cn.fooltech.fool_ops.domain.wage.entity.Wage;
import cn.fooltech.fool_ops.domain.wage.vo.WageVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 工资持久层
 * 
 * @author cwz
 * @date 2016-11-7
 */
public interface WageRepository extends JpaRepository<Wage, String>, JpaSpecificationExecutor<Wage> {
	

	/**
	 * 查询工资列表信息，按照工资主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	public default Page<Wage> query(WageVo wageVo, Pageable pageable) {
		Page<Wage> findAll = findAll(new Specification<Wage>() {
			@Override
			public Predicate toPredicate(Root<Wage> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				}
				if(StringUtils.isNotBlank(wageVo.getWageDate())){
					Date wageDate = DateUtilTools.string2Date(wageVo.getWageDate(), "yyyy-MM");
					predicates.add(builder.equal(root.<String>get("wageDate"), wageDate));
				}
				if(StringUtils.isNotBlank(wageVo.getDeptId())){
					predicates.add(builder.equal(root.<String>get("dept").get("fid"), wageVo.getDeptId()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
				return predicate;
			}
		}, pageable);
		return findAll;
	}
	@Query("select count(w) from Wage w where dept.fid=?1 and fiscalAccount.fid=?2 and wageDate BETWEEN ?3 and ?4")
	public Long queryByDeptAndAccount(String deptId,String accountId,Date start,Date end);
	
	@Query("select count(w) from Wage w where dept.fid=?1 and fiscalAccount.fid=?2 and wageDate BETWEEN ?3 and ?4 and fid!=?5")
	public Long queryByDeptAndAccount(String deptId,String accountId,Date start,Date end,String fid);

	/**
	 * 根据部门ID和时间段查询是否有工资单记录
	 * @param deptId
	 * @param start
	 * @param end
	 * @return
	 */
	public default Wage getByExist(String deptId, Date start, Date end, String accId,Pageable pageable) {
		Page<Wage> findAll = findAll(new Specification<Wage>() {
			@Override
			public Predicate toPredicate(Root<Wage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("dept").get("fid"), deptId));
				if (start != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("wageDate"), start));
				}
				if (end != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("wageDate"), end));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll!=null&&findAll.getContent().size()>0?findAll.getContent().get(0):null;
	}
	
	
	public default List<Wage> queryByCriteria(WageVo vo) {
		final String YEAR = "yyyy";
		final String MONTH = "yyyy-MM";
		Sort sort = new Sort(Direction.DESC, "wageDate");
		
		List<Wage> findAll = findAll(new Specification<Wage>() {
			@Override
			public Predicate toPredicate(Root<Wage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (StringUtils.isNotBlank(vo.getWageDate())) {
					Date wageDate = DateUtilTools.string2Date(vo.getWageDate(), MONTH);
//					criteria.add(Restrictions.eq("wageDate", wageDate));
					predicates.add(builder.equal(root.<Date>get("wageDate"), wageDate));
				} else if (StringUtils.isNotBlank(vo.getYear())) {
					Date temp = DateUtilTools.string2Date(vo.getYear(), YEAR);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(temp);
					calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
					Date yearStart = calendar.getTime();

					calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date yearEnd = calendar.getTime();
//					criteria.add(Restrictions.between("wageDate", yearStart, yearEnd));
					predicates.add(builder.between(root.<Date>get("wageDate"), yearStart, yearEnd));
				} 
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				}
				if (StringUtils.isNotBlank(vo.getDeptId())) {
					predicates.add(builder.equal(root.<String>get("dept").get("fid"), vo.getDeptId()));
				}
				predicates.add(builder.isNotNull(root.get("auditorTime")));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
		return findAll;
	}
	/**
	 * 查找区间内所有工资的月份
	 * @param start
	 * @param end
	 * @return
	 */
	public default  List<Wage> queryAllMonth(Date start, Date end, String accountId, String deptId) 
	{
		Sort sort = new Sort(Direction.ASC, "wageDate");
		
		List<Wage> list = findAll(new Specification<Wage>() {
			@Override
			public Predicate toPredicate(Root<Wage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				predicates.add(builder.between(root.<Date>get("wageDate"), start, end));
				predicates.add(builder.isNotNull(root.get("auditorTime")));
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				if (StringUtils.isNotBlank(deptId)) {
					predicates.add(builder.equal(root.<String>get("dept").get("fid"), deptId));
				}
				query.select(root.get("wageDate")).distinct(true);
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				query.where(predicate);
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, sort);
		return list;
	}
}
