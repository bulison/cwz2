package cn.fooltech.fool_ops.analysis.dao;

/**
 * Created by Administrator on 2016/12/31.
 */

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GenDataDao {
    void genTsbPurchasePrice();
    void genTsbTransportPrice();
}
