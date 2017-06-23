package cn.fooltech.fool_ops.eureka.rateService.dao;

import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>订单销售分析</p>
 *
 * @author c
 * @date 2017-03-31
 */

@Mapper
public interface SaleOrderDao {

    /**
     * @param argsMap 查询销售单总数，参数为查询条件的Map,需要包括下面字段
     * startDate 开始日期
     * endDate 结束日期
     * customerId 客户
     * saleId 业务员
     * saleCode 单号
     * goodsId 货品
     * goodsSpecId 货品属性
     * accId 账套ID
     * orgId 机构ID
     */
    List<SaleOrderVo> listSaleOrder(Map<String, Object> argsMap);

    /**
     * @param argsMap 查询销售单总数，参数为查询条件的Map,需要包括下面字段
     * startDate 开始日期
     * endDate 结束日期
     * customerId 客户
     * saleId 业务员
     * saleCode 单号
     * goodsId 货品
     * goodsSpecId 货品属性
     * accId 账套ID
     * orgId 机构ID
     */
    Long countSaleOrder(Map<String, Object> argsMap);

    /**
     * @param argsMap 查询销售单详情列表，参数为查询条件
     * startDate 开始日期
     * endDate 结束日期
     * customerId 客户
     * saleId 业务员
     * saleCode 单号
     * goodsId 货品
     * goodsSpecId 货品属性
     * accId 账套ID
     * orgId 机构IDa
     */
    List<SaleOrderDetailVo> listSaleOrderDetail(Map<String, Object> argsMap);

    /**
     * @param argsMap 查询销售单详情总数，参数为查询条件的Map,需要包括下面字段
     * startDate 开始日期
     * endDate 结束日期
     * customerId 客户
     * saleId 业务员
     * saleCode 单号
     * goodsId 货品
     * goodsSpecId 货品属性
     * accId 账套ID
     * orgId 机构IDa
     */
    Long countSaleOrderDetail(Map<String, Object> argsMap);
}
