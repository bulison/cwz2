package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface TodayCostAnalysisRepository extends FoolJpaRepository<CostAnalysisBill,String> {

    /**
     * 生成路径
     */
    @Procedure(procedureName="proc_gen_transport_route")
    @Modifying
    public void genRoute();

    /**
     * 生成路径
     */
    @Procedure(procedureName="proc_cost_analyze_delete_old_all")
    @Modifying
    public void dropOldData(@Param("purchase") Integer purchase);

    /**
     * 生成路径
     */
    @Procedure(procedureName="proc_cost_analyze_delete_old")
    @Modifying
    public void dropOldData(@Param("purchase") Integer purchase, @Param("accId") String accId);


    /**
     * 将货品报价表中有效日期小于当天的记录的状态置为无效
     */
    @Modifying
    @Query(value = "update PurchasePrice a set a.enable = 0 where a.enable=1 and a.effectiveDate < ?1")
    public void updatePurchasePriceDisable(Date today);


    /**
     * 将场地费报价表中有效日期小于当天的记录的状态置为无效
     */
    @Modifying
    @Query(value = "update GroundPrice a set a.enable = 0 where a.enable=1 and a.effectiveDate < ?1")
    public void updateGroundPriceDisable(Date today);

}
