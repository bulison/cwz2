package cn.fooltech.fool_ops.domain.basedata.dao;

import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePeerQuoteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by xjh on 2017/1/3.
 */
@Mapper
public interface PeerQuoteReportDao {

    /**
     * 获取报表列表
     * @param vo
     * @return
     */
    public List<PeerQuoteVo> queryReport(PeerQuoteVo vo);
    
    /**
     * 根据收货地ID获取列表
     * @param vo
     * @return
     */
    public List<PeerQuoteVo> queryByReceiptPlaceCount(PeerQuoteVo vo);

    /**
     * 获取曲线图数据
     * @param vo
     * @return
     */
    public List<SimplePeerQuoteVo> queryPriceTrend(PeerQuoteVo vo);

    /**
     * 获取成本数据
     * @param vo
     * @return
     */
    public List<SimplePeerQuoteVo> queryCostPriceTrend(PeerQuoteVo vo);
}
