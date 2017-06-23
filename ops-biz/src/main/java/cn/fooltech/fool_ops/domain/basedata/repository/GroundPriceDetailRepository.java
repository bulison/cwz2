package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPriceDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroundPriceDetailRepository extends FoolJpaRepository<GroundPriceDetail, String>{


    /**
     * 根据主表ID查询明细
     * @param billId
     * @return
     */
    @Query("select g from GroundPriceDetail g where g.billId=?1")
    public List<GroundPriceDetail> findByBillId(String billId);
}
