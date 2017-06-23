package cn.fooltech.fool_ops.domain.report.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>现金流量汇总分析<p> 
 * @author cwz
 * @date 2017年3月22日 下午4:35:15
 */
@Repository
public class CashFlowAnalysisRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 现金流量汇总(总数)
	 */
	public long count(String orgId, String accId,String startDate,String endDate) {
		
		Query query = entityManager.createNativeQuery("call p_cash_flow_analysis(:orgId,:accId,:startDate,:endDate,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("startDate",startDate==null?"":startDate );
		query.setParameter("endDate", endDate==null?"":endDate );
		query.setParameter("offset", 0);
		query.setParameter("limit", 0);
		query.setParameter("countflag", 1);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}
	/**
	 * 查找详情列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object[]> queryDetail(String orgId, String accId,String startDate,String endDate,  int first,int pageSize) {
		Query query = entityManager.createNativeQuery("call p_cash_flow_analysis(:orgId,:accId,:startDate,:endDate,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("startDate",startDate==null?"":startDate);
		query.setParameter("endDate", endDate==null?"":endDate);
		query.setParameter("offset", first);
		query.setParameter("limit", pageSize);
		query.setParameter("countflag", 0);
		List list = query.getResultList();
		return list;
	}

}
