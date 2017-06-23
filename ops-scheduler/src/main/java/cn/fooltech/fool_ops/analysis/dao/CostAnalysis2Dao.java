package cn.fooltech.fool_ops.analysis.dao;

/**
 * Created by Administrator on 2017/1/5.
 */

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CostAnalysis2Dao {


    void invalidPurchasePriceByEffectiveDate();

    void invalidGroundPriceByEffectiveDate();

    void selectInsertCostAnalysisBill();
}
