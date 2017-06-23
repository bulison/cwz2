package cn.fooltech.fool_ops.domain.report.dao;

import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务报价Dao
 * Created by xjh on 2017/1/11.
 */
@Mapper
public interface BusinessPriceReportDao {

    /**
     * 查询业务报价报表
     * @return
     */
    public List<CostAnalysisBillVo> queryBusinessPriceReport(@Param("accId") String accId,
                                                             @Param("customerId") String customerId,
                                                             @Param("receiptPlaceId") String receiptPlaceId,
                                                             @Param("goodsId") String goodsId);


    /**
     * 根据主表ID查询业务报价明细记录
     * @param billId
     * @return
     */
    public List<CostAnalysisBilldetailVo> queryBusinessPriceDetail(@Param("billId") String billId);
}
