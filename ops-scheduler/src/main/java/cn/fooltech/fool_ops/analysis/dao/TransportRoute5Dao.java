package cn.fooltech.fool_ops.analysis.dao;

import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
@Mapper
public interface TransportRoute5Dao {
    void deleteAll();

    int isExistRouteRule1(String id);

    void insert(TransportRouteVo firstTransportRouteVo);
    void  addTransportRouteBatch(List<TransportRouteVo> transportRouteVos);

    List<TransportRouteVo> getByReceiptPlace(@Param("receiptPlace") String receiptPlace);

    List<TransportRouteVo> getByDeliveryPlace(@Param("deliveryPlace") String deliveryPlace);

    int isExistReceiptPlaceRoute(@Param("receiptPlace") String receiptPlace);
    int isExistDeliveryPlaceRoute(@Param("deliveryPlace") String deliveryPlace);
    int isFullExistRoute(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace, @Param("route") String route);

    List<TransportRouteVo> getByDeliveryPlaceGruop(@Param("deliveryPlace") String deliveryPlace);
    List<TransportRouteVo> getByReceiptPlaceGruop(@Param("receiptPlace") String receiptPlace);
}
