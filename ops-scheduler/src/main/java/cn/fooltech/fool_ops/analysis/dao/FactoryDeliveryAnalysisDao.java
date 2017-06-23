package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */
@Mapper
public interface FactoryDeliveryAnalysisDao {
    void selectInsertCostAnalysisBill();

    List<CostAnalysisBillVo> selectAllCostAnalysisBillByToday();


    void insertCostAnalysisBilldetail(CostAnalysisBilldetailVo vo);

    void updateCostAnalysisBilldetailFreight();
}
