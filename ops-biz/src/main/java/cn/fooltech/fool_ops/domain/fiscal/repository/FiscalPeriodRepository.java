package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.utils.DateUtilTools;

public interface FiscalPeriodRepository extends FoolJpaRepository<FiscalPeriod, String>, 
	FoolJpaSpecificationExecutor<FiscalPeriod> {

	/**
	 * 根据账套查找会计期间
	 * @param accId
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 order by startDate")
	public List<FiscalPeriod> findByAccountId(String accId);

	/**
	 * 根据状态查询
	 * @param accId
	 * @param checkoutStatus
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus=?2")
	public List<FiscalPeriod> findByCheckStatus(String accId, int checkoutStatus);

	/**
	 * 获取所有已启用或已结账的会计期间
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus in (0,1)")
	public List<FiscalPeriod> getAllUsedCheckedPeriod(String accId);


	/**
	 * 根据状态查询
	 * @param accId
	 * @param sort
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus!="+FiscalPeriod.CHECKED)
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopPeriodByUnCheck(String accId, Sort sort);

	/**
	 * 根据日期获取上一个会计期间
	 * @param accId
	 * @param date
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.endDate<?2 order by endDate desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findPrePeriodByDate(String accId, Date date);

	/**
	 * 根据日期获取下一个会计期间
	 * @param accId
	 * @param date
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.startDate>?2 order by startDate asc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findNextPeriodByDate(String accId, Date date);
	
	/**
	 * 根据状态查询
	 * @param accId
	 * @param sort
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus!="+FiscalPeriod.CHECKED)
	public List<FiscalPeriod> findByUnCheck(String accId, Sort sort);

	/**
	 * 查找第一个会计期间
	 * @param accId
	 * @param sort
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopPeriodByAccId(String accId, Sort sort);

	/**
	 * 根据开始日期查找第一个会计期间
	 * @param accId
	 * @param startDate
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.startDate=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopPeriodByStartDate(String accId, Date startDate);
	
	/**
	 * 根据结束日期查找第一个会计期间
	 * @param accId
	 * @param endDate
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.endDate=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopByEndDate(String accId, Date endDate);
	
    /**
	 * 获取最后一个已结账的会计期间
	 * @param accountId
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus="+FiscalPeriod.CHECKED)
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
    public FiscalPeriod getLastCheckedPeriod(String accountId,Sort sort);

	/**
	 * 根据名称统计
	 * @param name
	 * @param accId
	 * @return
	 */
	@Query("select count(*) from FiscalPeriod s where s.period=?1 and s.fiscalAccount.fid=?2")
	public Long countByName(String name, String accId);
	
	/**
	 * 根据名称统计
	 * @param name
	 * @param accId
	 * @return
	 */
	@Query("select count(*) from FiscalPeriod s where s.period=?1 and s.fiscalAccount.fid=?2 and s.fid!=?3")
	public Long countByName(String name, String accId, String excludeId);

	/**
	 * 根据账套ID查找分页
	 * @param accId
	 * @param page
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1")
	public Page<FiscalPeriod> findPageByAccountId(String accId, Pageable page);

	/**
	 * 根据日期查询会计期间
	 * @param date
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.startDate<=?2 and s.endDate>=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopByDate(String accId, Date date);
	
	/**
	 * 根据日期查询会计期间
	 * @param date
	 * @return
	 */
	@Query("select s from FiscalPeriod s where s.fiscalAccount.fid=?1 and s.startDate<=?2 and s.endDate>=?2 and s.checkoutStatus=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalPeriod findTopByDate(String accId, Date date, int checkoutStatus);

	/**
	 * 查找重叠时间的会计期间
	 * @param start
	 * @param end
	 * @param accId
	 * @param excludeId
	 * @return
	 */
	public default Long countByPeriod(Date start, Date end, String accId, String excludeId){
		return this.count(new Specification<FiscalPeriod>() {
			
			@Override
			public Predicate toPredicate(Root<FiscalPeriod> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				if (StringUtils.isNotBlank(excludeId)) {
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				predicates.add(
					builder.or(
						builder.and(
							builder.lessThanOrEqualTo(root.<Date>get("startDate"), start),
							builder.greaterThanOrEqualTo(root.<Date>get("endDate"), start)
						),
						builder.and(
							builder.lessThanOrEqualTo(root.<Date>get("startDate"), end),
							builder.greaterThanOrEqualTo(root.<Date>get("endDate"), end)
						)
					)
				);
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	/**
	 * 根据账套ID和状态列表查询
	 * @param accId
	 * @param statuLists
	 * @return
	 */
	@Query("select count(*) from FiscalPeriod s where s.fiscalAccount.fid=?1 and checkoutStatus in ?2")
	public Long countByAccIdAndStatus(String accId, List<Integer> statuLists);
	
	 /**
     * 获得当年最小的会计期间
     * @return
     */
	public default FiscalPeriod getMinPeriod(FiscalPeriod period) {

		Sort sort = new Sort(Direction.ASC, "startDate");
		return this.findTop(new Specification<FiscalPeriod>() {

			@Override
			public Predicate toPredicate(Root<FiscalPeriod> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount"), period.getFiscalAccount()));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(period.getStartDate());
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				Date startYear = null;
				Date endYear = null;
				if (day >= 20) {
					startYear = DateUtilTools.changeToYearStart(period.getEndDate());
					endYear = DateUtilTools.changeDateTime(startYear, 0, 19, 0, 0, 0);
				} else {
					startYear = DateUtilTools.changeToYearStart(period.getStartDate());
					endYear = DateUtilTools.changeDateTime(startYear, 0, 19, 0, 0, 0);
				}

				predicates.add(builder.or(builder.greaterThanOrEqualTo(root.get("startDate"), startYear),
						builder.and(builder.lessThanOrEqualTo(root.get("startDate"), startYear),
								builder.greaterThanOrEqualTo(root.get("endDate"), endYear))));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 获取期间损益类科目的结余金额
	 * @param periodId 会计期间ID
	 */
	public default double getBalanceOfLossSubject(String periodId) {
		String sql = "call balance_of_loss_subject(:periodId)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("periodId", periodId);
		Object data = query.getSingleResult();
		if(data!=null){
			BigDecimal balance = (BigDecimal) data;
			return balance.doubleValue();
		}
		return 0d;
	}
}
