package cn.fooltech.fool_ops.analysis.dao;


import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
//import cn.fooltech.fool_ops.domain.analysis.vo.TransportRouteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TransportRouteDao {
    /**
     * 清空所有路径表
     */
    public void deleteAll();



    /**
     *新增路径记录
     * @param vo
     */
    public  void insert(TransportRouteVo vo);

    /**
     * 获取总数
     * 根据报价单号查询该单单运输路径
     *
     * @param transportBillId 价单号
     * @return 运输路径总数
     */
    public int isExistRouteRule1(String transportBillId);
    /***
     * 查找运输路径表的收货地=该记录的发货地，
     * 并且该记录的收货地不存在运输路径表的线路路径中的记录，
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     *
     */
    public List<TransportRouteVo> getListByRouteRule2(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);

    /**
     * 查找运输路径表的发货地=该记录的收货地，
     * 并且该记录的发货地不存在运输路径表的线路路径中的记录
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     * @return
     */
    public List<TransportRouteVo> getListByRouteRule3(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);

    /**
     * 找出运输路径表的收货地为组地址的记录，
     * 把组下的地址与该记录的发货地匹配，
     * 并且该记录的收货地不存在运输路径表的线路路径中的记录
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     * @return
     */
    public List<TransportRouteVo> getListByRouteRule4(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);
    /**
     * 找出运输路径表的发货地为组地址的记录，
     * 把组下的地址与该记录的收货地匹配，
     * 并且该记录的发货地不存在运输路径表的线路路径中的记录
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     * @return
     */
    public List<TransportRouteVo> getListByRouteRule5(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);
    /**
     * 如果该记录的发货地为组地址，
     * 把组下的地址与运输路径表的收货地匹配，
     * 并且该记录的收货地不存在运输路径表的线路路径中的记录
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     * @return
     */
    public List<TransportRouteVo> getListByRouteRule6(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);

    /**
     * 如果该记录的收货地为组地址，
     * 把组下的地址与运输路径表的发货地匹配，
     * 并且该记录的发货地不存在运输路径表的线路路径中的记录
     * @param receiptPlace 收货地址
     * @param deliveryPlace 发货地址
     * @return
     */

    public List<TransportRouteVo> getListByRouteRule7(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);


    public TransportRouteVo getByReceiptPlace(String Place);
//
    public TransportRouteVo getByDeliveryPlace(String Place);

    public int isExistRoute(@Param("receiptPlace") String receiptPlace, @Param("deliveryPlace") String deliveryPlace,@Param("route")String route);
}
