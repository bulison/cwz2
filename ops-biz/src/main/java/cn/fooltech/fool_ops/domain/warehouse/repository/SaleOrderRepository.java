package cn.fooltech.fool_ops.domain.warehouse.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * <p>销售分析报表<p>
 * @date 2017-3-28
 */

@Repository
public class SaleOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 销售订单分析
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Object[]> querySaleOrderList(String orgId, String accId, Date startDate, Date endDate,
                                             String saleCode, String customerId, String saleId,
                                             int first, int pageSize) {
        String querySql = "call p_sale_order_analysis" +
                "(:startDate,:endDate,:customerId,:saleId,:saleCode,:accId,:orgId,:offset,:rows,:countFlag)";
        Query query = entityManager.createNativeQuery(querySql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("customerId", customerId);
        query.setParameter("saleId", saleId);
        query.setParameter("saleCode", saleCode);
        query.setParameter("accId", accId);
        query.setParameter("orgId", orgId);
        query.setParameter("offset", first);
        query.setParameter("rows", pageSize);
        query.setParameter("countFlag", 0);
        return query.getResultList();
    }

    /**
     * 销售订单分析(总数)
     * @param orgId 条件包含：开始日期、结束日期、客户、业务员、单号；
     */
    public long countSaleOrderList(String orgId, String accId, Date startDate, Date endDate,
                      String saleCode, String customerId, String saleId) {

        String querySql = "call p_sale_order_analysis" +
                "(:startDate,:endDate,:customerId,:saleId,:saleCode,:accId,:orgId,:offset,:rows,:countFlag)";
        Query query = entityManager.createNativeQuery(querySql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("customerId", customerId);
        query.setParameter("saleId", saleId);
        query.setParameter("saleCode", saleCode);
        query.setParameter("accId", accId);
        query.setParameter("orgId", orgId);
        query.setParameter("offset", 0);
        query.setParameter("rows", 0);
        query.setParameter("countFlag", 1);
        List list = query.getResultList();
        return ((BigInteger) list.get(0)).longValue();
    }

}
