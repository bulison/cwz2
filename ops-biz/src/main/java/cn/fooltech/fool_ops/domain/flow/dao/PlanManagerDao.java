package cn.fooltech.fool_ops.domain.flow.dao;

import cn.fooltech.fool_ops.domain.flow.vo.PlanTransportFeeVo;
import cn.fooltech.fool_ops.domain.flow.vo.TransportFeeDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>预计成本</p>
 *
 * @author c
 * @date 2017-03-01
 */

@Mapper
public interface PlanManagerDao {

    List<PlanTransportFeeVo> queryPlanTransportFee(
            @Param("planId") String planId,
            @Param("deliveryPlace") String deliveryPlace,
            @Param("receiptPlace") String receiptPlace,
            @Param("goodsId") String goodsId,
            @Param("goodsSpecId") String goodsSpecId);

    /**
     * 根据运输奋勇主表ID查询费用明细金额并汇总
     * @param billId
     * @return
     */
    List<TransportFeeDetailVo> queryFeeByTransportBillId(@Param("billId") String billId);
}
