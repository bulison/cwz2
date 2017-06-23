package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
//import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
@Mapper
public interface CostAnalysisDao {
    void invalidPurchasePriceByEffectiveDate();

    void invalidGroundPriceByEffectiveDate();

    void selectInsertCostAnalysisBill();

    List<CostAnalysisBillVo> selectAllCostAnalysisBillByToday();

    void insertCostAnalysisBilldetail(CostAnalysisBilldetailVo billdetailVo);

    void updateCostAnalysisBilldetailFreight();

    void updateCostAnalysisBillFreight();
    void updateCostAnalysisBillByYesterday();

    List<TransportRouteVo> selectTransportRouteByCondition3();

    void cleanCostAnalysisBillToday();
}
