package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Mapper
public interface TransportRoute4Dao {
    void deleteAll();
    void createTemporaryRouteTable();
    void deleteTemporaryRouteTable();
    void dropTemporaryRouteTable();

    int isExistRouteRule1(String id);

    void insertTempTable(TransportRouteVo vo);
    void insertTempTableBatch(List<TransportRouteVo> voList);

    List<TransportRouteVo> selectAllTemporaryRoute();

    void insertTransportRouteBatch(List<TransportRouteVo> allRouteVoList);


    List<TransportRouteVo> getRouteListByRule2(Map map);

    List<TransportRouteVo> getRouteListByRule3(Map map);
    List<TransportRouteVo> getRouteListByRule4(Map map);
    List<TransportRouteVo> getRouteListByRule5(Map map);
    List<TransportRouteVo> getRouteListByRule6(Map map);
    List<TransportRouteVo> getRouteListByRule7(Map map);
//    void insertRouteRule2(Map map);
//
//    void insertRouteRule3(Map map);
//
//    void insertRouteRule4(Map map);
//
//    void insertRouteRule5(Map map);
//
//    void insertRouteRule6(Map map);
//
//    void insertRouteRule7(Map map);
//
//
//    int isExistRouteRule2(Map map);
//
//
//    int isExistRouteRule3(Map map);
//
//    int isExistRouteRule4(Map map);
//
//    int isExistRouteRule5(Map map);
//
//    int isExistRouteRule6(Map map);
//
//
//    int  isExistRouteRule7(Map map);;

//    List<TransportRouteVo> getRouteListByRule2(Map map);


}
