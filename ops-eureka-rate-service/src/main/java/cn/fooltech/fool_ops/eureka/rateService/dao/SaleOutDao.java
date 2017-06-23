package cn.fooltech.fool_ops.eureka.rateService.dao;

import cn.fooltech.fool_ops.eureka.rateService.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 销售出库单分析mapper
 */
@Mapper
public interface SaleOutDao {

    /**
     * 销售出库单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @param starRow
     * @param maxRow
     * @return
     */
    public List<SaleOutVo> saleOutAnalyze(
          @Param("startDate") String startDate,
          @Param("endDate") String endDate,
          @Param("customerId") String customerId,
          @Param("saleMemberId") String saleId,
          @Param("saleCode") String saleCode,
          @Param("orgId") String orgId,
          @Param("accountId") String accountId,
          @Param("starRow") Integer starRow,
          @Param("maxRow") Integer maxRow);

    /**
     * 统计销售出库单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @return
     */
    public Long countSaleOutAnalyze(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("customerId") String customerId,
            @Param("saleMemberId") String saleId,
            @Param("saleCode") String saleCode,
            @Param("orgId") String orgId,
            @Param("accountId") String accountId);


    /**
     * 销售出库单关联分析
     * @param saleoutId
     * @param starRow
     * @param maxRow
     * @return
     */
    public List<SaleOutRelationVo> saleOutRelationAnalyze(
            @Param("saleoutId") String saleoutId,
            @Param("starRow") Integer starRow,
            @Param("maxRow") Integer maxRow);

    /**
     * 统计销售出库单关联分析
     * @param saleoutId
     * @return
     */
    public Long countSaleOutRelationAnalyze(@Param("saleoutId") String saleoutId);


    /**
     * 销售出库单明细分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @param starRow
     * @param maxRow
     * @return
     */
    public List<SaleOutDetailVo> saleOutDetailAnalyze(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("customerId") String customerId,
            @Param("saleMemberId") String saleId,
            @Param("saleCode") String saleCode,
            @Param("goodId") String goodId,
            @Param("goodSpecId") String goodSpecId,
            @Param("orgId") String orgId,
            @Param("accountId") String accountId,
            @Param("starRow") Integer starRow,
            @Param("maxRow") Integer maxRow);

    /**
     * 统计销售出库单明细分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @return
     */
    public Long countSaleOutDetailAnalyze(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("customerId") String customerId,
            @Param("saleMemberId") String saleId,
            @Param("saleCode") String saleCode,
            @Param("goodId") String goodId,
            @Param("goodSpecId") String goodSpecId,
            @Param("orgId") String orgId,
            @Param("accountId") String accountId);

    /**
     * 根据销售出库单ID查询交易记录
     * @param saleOutId
     * @return
     */
    public List<TradeRecordVo> queryTradeRecord(@Param("saleOutId") String saleOutId);
}
