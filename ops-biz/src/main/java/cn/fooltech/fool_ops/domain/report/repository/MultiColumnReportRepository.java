package cn.fooltech.fool_ops.domain.report.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

/**
 * <p>多栏明细账报表</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月15日
 */
@Repository
public class MultiColumnReportRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 查询
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @param startFiscalPeriodId 开始财务会计期间
	 * @param endFiscalPeriodId 结束财务会计期间
	 * @param settingId 多栏明细账设置ID
	 * @param voucherStatus 凭证状态
	 * @param start
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> query(String orgId, String accountId, String startFiscalPeriodId, String endFiscalPeriodId,
			String settingId, String voucherStatus, int start, int maxResult){
		String sql = "call multiColumn_report(:orgId, :accountId, :settingId, :startPeriodId , :endPeroidId, :voucherStatus, :start, :maxResult, 0)";
			
		Query query = entityManager.createNativeQuery(sql); 
		query.setParameter("orgId", orgId);
		query.setParameter("accountId", accountId);
		query.setParameter("settingId", settingId);
		query.setParameter("startPeriodId", startFiscalPeriodId);
		query.setParameter("endPeroidId", endFiscalPeriodId);
		query.setParameter("voucherStatus", voucherStatus);
		query.setParameter("start", start);
		query.setParameter("maxResult", maxResult);
		return query.getResultList();
	}
	
	/**
	 * 统计记录数
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @param startFiscalPeriodId 开始财务会计期间
	 * @param endFiscalPeriodId 结束财务会计期间
	 * @param settingId 多栏明细账设置ID
	 * @param voucherStatus 凭证状态
	 * @param start
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long count(String orgId, String accountId, String startFiscalPeriodId, String endFiscalPeriodId,
			String settingId, String voucherStatus){
		String sql = "call multiColumn_report(:orgId, :accountId, :settingId, :startPeriodId , :endPeroidId, :voucherStatus, 0, 10, 1)";
		Query query = entityManager.createNativeQuery(sql); 
		query.setParameter("orgId", orgId);
		query.setParameter("accountId", accountId);
		query.setParameter("settingId", settingId);
		query.setParameter("startPeriodId", startFiscalPeriodId);
		query.setParameter("endPeroidId", endFiscalPeriodId);
		query.setParameter("voucherStatus", voucherStatus);
		List<Object> datas = query.getResultList();
		if(CollectionUtils.isNotEmpty(datas)){
			BigInteger total = (BigInteger) datas.get(0);
			return total.longValue();
		}
		return 0L;
	}
	
}
