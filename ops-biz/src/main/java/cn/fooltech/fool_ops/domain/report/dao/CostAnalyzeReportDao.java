package cn.fooltech.fool_ops.domain.report.dao;

import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业务报价Dao
 * Created by xjh on 2017/1/11.
 */
@Mapper
public interface CostAnalyzeReportDao {

    /**
     * 查询业务报价报表
     * @return
     */
    public List<CostAnalysisBillVo> queryCostAnalyzeReport(@Param("accId") String accId,
                                                           @Param("receiptPlaceId") String receiptPlaceId,
                                                             @Param("customerId") String customerId,
                                                             @Param("goodsId") String goodsId);


    /**
     * 根据主表ID查询业务报价明细记录
     * @param billId
     * @return
     */
    public List<CostAnalysisBilldetailVo> queryCostAnalyzeDetail(@Param("billId") String billId);
}
