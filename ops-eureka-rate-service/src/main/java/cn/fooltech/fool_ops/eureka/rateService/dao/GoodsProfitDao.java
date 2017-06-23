package cn.fooltech.fool_ops.eureka.rateService.dao;

import cn.fooltech.fool_ops.eureka.rateService.vo.GoodsProfitVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 货品利润分析汇总表mapper
 */
@Mapper
public interface GoodsProfitDao {
    public List<GoodsProfitVo> findProfitAnalysis(@Param("accId") String accId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  @Param("goodsId") String goodsId,
                                                  @Param("specId") String specId,
                                                  @Param("page") Integer page,
                                                  @Param("rows") Integer rows);
}
