package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Mapper
public interface TransportRoute3Dao {
    void deleteAll();

    int isExistRouteRule1(String id);

    void insert(TransportRouteVo firstTransportRouteVo);

    void insertRouteRule2(Map map);

    void insertRouteRule3(Map map);

    void insertRouteRule4(Map map);

    void insertRouteRule5(Map map);

    void insertRouteRule6(Map map);

    void insertRouteRule7(Map map);


    int isExistRouteRule2(Map map);

    int isExistRouteRule3(Map map);

    int isExistRouteRule4(Map map);

    int isExistRouteRule5(Map map);

    int isExistRouteRule6(Map map);


    int  isExistRouteRule7(Map map);;


}
