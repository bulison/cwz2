package cn.fooltech.fool_ops.domain.report.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class QuentityAmountRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Object[]> getData(String startPeriodId,String endPeriodId, String curSubjectCode,
			String voucherStatus, Integer level, int first, int pageSize) {
		Query query = entityManager.createNativeQuery("call quentity_amount_detail_report(:startPeriodId, :endPeriodId,:curSubjectCode,:voucherStatus,:level,:offset,:limit,:countflag)");
		query.setParameter("startPeriodId", startPeriodId);
		query.setParameter("endPeriodId", endPeriodId);
		query.setParameter("curSubjectCode", curSubjectCode==null?"":curSubjectCode);
		query.setParameter("voucherStatus", voucherStatus==null?"":voucherStatus);
		query.setParameter("level", level==null?0:level);
		query.setParameter("offset", first);
		query.setParameter("limit", pageSize);
		query.setParameter("countflag", 0);
		List list = query.getResultList();
		return list;
	}
	
	public Long countData(String startPeriodId,String endPeriodId, String curSubjectCode,
			String voucherStatus, Integer level){
		Query query = entityManager.createNativeQuery("call quentity_amount_detail_report(:startPeriodId, :endPeriodId,:curSubjectCode,:voucherStatus,:level,:offset,:limit,:countflag)");
		query.setParameter("startPeriodId", startPeriodId);
		query.setParameter("endPeriodId", endPeriodId);
		query.setParameter("curSubjectCode", curSubjectCode==null?"":curSubjectCode);
		query.setParameter("voucherStatus", voucherStatus==null?"":voucherStatus);
		query.setParameter("level", level==null?0:level);
		query.setParameter("offset", 0);
		query.setParameter("limit", 1);
		query.setParameter("countflag", 1);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}
}
