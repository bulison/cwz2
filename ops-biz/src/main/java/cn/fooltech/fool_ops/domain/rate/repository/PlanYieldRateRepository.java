package cn.fooltech.fool_ops.domain.rate.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

/**
 * 
 * <p>
 * 流程收益率分析【报表】
 * </p>
 * 
 * @author cwz
 * @date 2017年4月17日
 */
@Repository
public class PlanYieldRateRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 流程收益率分析(总数)
	 */
	public long count(String orgId, String accId, String startDate, String endDate, String planCode, String planName,
			String initiate, String principal, String status, Integer sidx, Integer sord) {
		Query query = entityManager.createNativeQuery(
				"call p_yield_analysis(:orgId,:accId,:startDate,:endDate,:planCode,:planName,"
						+ ":initiate,:principal,:status,:sidx,:sord,:start,:limit,:countflag)");
		query.setParameter("orgId", orgId == null ? "" : orgId);
		query.setParameter("accId", accId == null ? "" : accId);
		query.setParameter("startDate", startDate == null ? "" : startDate);
		query.setParameter("endDate", endDate == null ? "" : endDate);
		query.setParameter("planCode", planCode == null ? "" : planCode);
		query.setParameter("planName", planName == null ? "" : planName);
		query.setParameter("initiate", initiate == null ? "" : initiate);
		query.setParameter("principal", principal == null ? "" : principal);
		query.setParameter("status", status == null ? "" : status);
		query.setParameter("sidx", sidx == null ? 1 : sidx);
		query.setParameter("sord", sord == null ? 0 : sord);
		query.setParameter("start", 0);
		query.setParameter("limit", 0);
		query.setParameter("countflag", 1);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}

	/**
	 * 查找详情列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object[]> queryDetail(String orgId, String accId, String startDate, String endDate, String planCode,
			String planName, String initiate, String principal, String status, Integer sidx, Integer sord, int first,
			int pageSize) {
		Query query = entityManager.createNativeQuery(
				"call p_yield_analysis(:orgId,:accId,:startDate,:endDate,:planCode,:planName,"
						+ ":initiate,:principal,:status,:sidx,:sord,:start,:limit,:countflag)");
		query.setParameter("orgId", orgId == null ? "" : orgId);
		query.setParameter("accId", accId == null ? "" : accId);
		query.setParameter("startDate", startDate == null ? "" : startDate);
		query.setParameter("endDate", endDate == null ? "" : endDate);
		query.setParameter("planCode", planCode == null ? "" : planCode);
		query.setParameter("planName", planName == null ? "" : planName);
		query.setParameter("initiate", initiate == null ? "" : initiate);
		query.setParameter("principal", principal == null ? "" : principal);
		query.setParameter("status", status == null ? "" : status);
		query.setParameter("sidx", sidx == null ? 1 : sidx);
		query.setParameter("sord", sord == null ? 0 : sord);
		query.setParameter("start", first);
		query.setParameter("limit", pageSize);
		query.setParameter("countflag", 0);
		List list = query.getResultList();
		return list;
	}

}
