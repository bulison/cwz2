package cn.fooltech.fool_ops.domain.report.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class GoodsStockRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 计算库存(总数)
	 */
	public long countGoodsStock(String periodId, String orgId, String accId,
			String warehouseId, String goodsId, String specId, Integer calTotal) {
		
		Query query = entityManager.createNativeQuery("call goodsStock(:orgId,:accId,:periodId,:goodsId,:specId,:warehouseId,:calTotal,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("periodId", periodId);
		query.setParameter("goodsId", goodsId==null?"":goodsId);
		query.setParameter("specId", specId==null?"":specId);
		query.setParameter("warehouseId", warehouseId==null?"":warehouseId);
		query.setParameter("calTotal", calTotal==null?0:calTotal);
		query.setParameter("offset", 0);
		query.setParameter("limit", 0);
		query.setParameter("countflag", 1);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}
	
	/**
	 * 计算库存
	 * */
	public List<Object[]> queryGoodsStock(String periodId, String orgId, String accId,
			String warehouseId, String goodsId, String specId, Integer calTotal,
			int offset, int limit) {
		
		Query query = entityManager.createNativeQuery("call goodsStock(:orgId,:accId,:periodId,:goodsId,:specId,:warehouseId,:calTotal,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("periodId", periodId);
		query.setParameter("goodsId", goodsId==null?"":goodsId);
		query.setParameter("specId", specId==null?"":specId);
		query.setParameter("warehouseId", warehouseId==null?"":warehouseId);
		query.setParameter("calTotal", calTotal==null?0:calTotal);
		query.setParameter("offset", offset);
		query.setParameter("limit", limit);
		query.setParameter("countflag", 0);
		List list = query.getResultList();
		return list;
	}


	/**
	 * 查找详情列表
	 */
	public List<Object[]> queryDetail(String periodId, String orgId, String accId,
			String warehouseId, String goodsId, String specId, Integer calTotal, int first,
			int pageSize) {
		Query query = entityManager.createNativeQuery("call goodsStockDetail(:orgId,:accId,:periodId,:goodsId,:specId,:warehouseId,:calTotal,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("periodId", periodId);
		query.setParameter("goodsId", goodsId==null?"":goodsId);
		query.setParameter("specId", specId==null?"":specId);
		query.setParameter("warehouseId", warehouseId==null?"":warehouseId);
		query.setParameter("calTotal", calTotal==null?0:calTotal);
		query.setParameter("offset", first);
		query.setParameter("limit", pageSize);
		query.setParameter("countflag", 0);
		List list = query.getResultList();
		return list;
	}

	/**
	 * 查找详情总数
	 */
	public Long countDetail(String periodId, String orgId, String accId, String warehouseId,
			String goodsId, String specId, Integer calTotal) {
		Query query = entityManager.createNativeQuery("call goodsStockDetail(:orgId,:accId,:periodId,:goodsId,:specId,:warehouseId,:calTotal,:offset,:limit,:countflag)");
		query.setParameter("orgId", orgId==null?"":orgId);
		query.setParameter("accId", accId==null?"":accId);
		query.setParameter("periodId", periodId);
		query.setParameter("goodsId", goodsId==null?"":goodsId);
		query.setParameter("specId", specId==null?"":specId);
		query.setParameter("warehouseId", warehouseId==null?"":warehouseId);
		query.setParameter("calTotal", calTotal==null?0:calTotal);
		query.setParameter("offset", 0);
		query.setParameter("limit", 0);
		query.setParameter("countflag", 1);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}
}
