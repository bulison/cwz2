package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.TransportPriceVo;
//import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransportPriceDao  {
      //运输报价表状态置为无效

    /**
     * 置无效标记有效日期小于当天的记录
     */
    public void invalideByEffectiveDate();

    /**
     * 取出所有有效运输报价
     * @return 效运输报价
     */
    public List<TransportPriceVo> selectValidTransportPrice();




}
