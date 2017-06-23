package cn.fooltech.fool_ops.domain.rate.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.rate.entity.SaleOutRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

/**
 * 销售出库单收益率Repository
 */
public interface SaleOutRateRepository extends FoolJpaRepository<SaleOutRate, String> {

    @Query("select u from SaleOutRate u where u.saleOutId=?1")
    @QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
    public SaleOutRate findByBillId(String billId);
}
