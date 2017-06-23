package cn.fooltech.fool_ops.domain.period.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.QueryHint;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;

public interface StockPeriodRepository extends JpaRepository<StockPeriod, String> {


	/**
	 * 根据账套查找会计期间
	 * @param accId
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1")
	public List<StockPeriod> findByAccountId(String accId);

	/**
	 * 根据状态查询
	 * @param accId
	 * @param used
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus=?2")
	public List<StockPeriod> findByCheckStatus(String accId, int used);
	
	/**
	 * 根据状态查询
	 * @param accId
	 * @param used
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1 and s.checkoutStatus!="+StockPeriod.CHECKED)
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockPeriod findTopPeriodByUnCheck(String accId, Sort sort);

	/**
	 * 查找第一个会计期间
	 * @param orgId
	 * @param accId
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockPeriod findTopPeriodByAccId(String accId, Sort sort);

	/**
	 * 根据开始日期查找第一个会计期间
	 * @param accId
	 * @param startDate
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1 and s.startDate=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockPeriod findTopPeriodByStartDate(String accId, Date startDate);
	
	/**
	 * 根据结束日期查找第一个会计期间
	 * @param accId
	 * @param endDate
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1 and s.endDate=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockPeriod findTopByEndDate(String accId, Date endDate);
	
	/**
	 * 根据日期查找会计期间
	 * @param accId
	 * @param endDate
	 * @return
	 */
	@Query("select s from StockPeriod s where s.fiscalAccount.fid=?1 and s.endDate>=?2 and s.startDate<=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockPeriod findTopByDate(String accId, Date date);

	/**
	 * 统计会计期间是否已被关系或使用
	 * @param id
	 * @return
	 */
	@Procedure(procedureName="p_stock_period_is_useNew")
	public Long isRelation(@Param("periodId") String id);

	/**
	 * 结账
	 * @param orgId
	 * @param currentPeriodId
	 */
	@Procedure(procedureName="stockPeriodSettle")
	public void settleAccount(@Param("orgId")String orgId, @Param("currentPeriodId")String currentPeriodId);

	/**
	 * 反结账
	 * @param orgId
	 * @param currentPeriodId
	 */
	@Procedure(procedureName="stockPeriodUnSettle")
	public void unSettleAccount(@Param("orgId")String orgId, @Param("currentPeriodId")String currentPeriodId);
	
	  /**
	   * 获取第一个会计期间
	   * @param orgId 机构ID
	   * @param accountId 财务账套ID
	   * @return
	   */
	@Query("select s from StockPeriod s where org.fid=?1 and fiscalAccount.fid=?2 and endDate=?3")
	public StockPeriod getTheFristPeriod(String orgId, String accountId,Date minDate);	

    /**
     * 统计已结账的会计期间个数
     * @param orgId 机构ID
     * @param accountId 财务账套ID
     * @return
     */
	@Query("select count(s) from StockPeriod s where org.fid=?1 and fiscalAccount.fid=?2 and checkoutStatus=1")
    public Long countCheckedPeriod(String orgId, String accountId);
}
