package cn.fooltech.fool_ops.eureka.rateService.dao;

import cn.fooltech.fool_ops.eureka.rateService.vo.GoodsProfitDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 货品利润分析汇总表mapper
 */
@Mapper
public interface GoodsProfitDetailDao {
    public List<GoodsProfitDetailVo> findProfitAnalysisDetail(@Param("accId") String accId,
                                                              @Param("startDate") Date startDate,
                                                              @Param("endDate") Date endDate,
                                                              @Param("goodsId") String goodsId,
                                                              @Param("specId") String specId,
                                                              @Param("page") Integer page,
                                                              @Param("rows") Integer rows);
}
