package cn.fooltech.fool_ops.domain.basedata.dao;

import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePurchasePriceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by xjh on 2017/1/3.
 */
@Mapper
public interface PurchasePriceReportDao {

    /**
     * 获取报表列表
     * @param vo
     * @return
     */
    public List<PurchasePriceVo> queryReport(PurchasePriceVo vo);


    /**
     * 获取曲线图数据
     * @param vo
     * @return
     */
    public List<SimplePurchasePriceVo> queryPriceTrend(PurchasePriceVo vo);
}
