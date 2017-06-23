package cn.fooltech.fool_ops.eureka.rateService.service;

import cn.fooltech.fool_ops.eureka.rateService.dao.SaleOrderDao;
import cn.fooltech.fool_ops.eureka.rateService.utils.DateUtilTools;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderVo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>销售订单</p>
 *
 * @author chk
 * @date 2017-03-31
 */

@Service
public class SaleOrderService {

    private final static int COUNT_NOT = 0;  // 不统计数量
    private final static int COUNT_YES = 1;  // 统计数量
    private final static int DEFAULT_OFFSET = 0;  // 页数默认从0开始
    private final static int DEFAULT_PAGE_SIZE = 0;  // 页数大小默认从10

    @Autowired
    private SaleOrderDao saleOrderDao;

    /**
     * 查询销售单列表
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param accId
     * @param page
     * @param rows
     * @return
     */
    public List<SaleOrderVo> listSaleOrder(String startDate, String endDate,
                                           String customerId, String saleId,
                                           String saleCode, String accId,
                                           Integer page,
                                           Integer rows){
        // 分页
        int first = (page - 1) * rows;

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("startDate", startDate);
        parameterMap.put("endDate", endDate);
        parameterMap.put("customerId", customerId);
        parameterMap.put("saleId", saleId);
        parameterMap.put("saleCode", saleCode);
        parameterMap.put("accId", accId);
        parameterMap.put("offset", first);
        parameterMap.put("rows", rows);
        parameterMap.put("countFlag", COUNT_NOT);
        return saleOrderDao.listSaleOrder(parameterMap);
    }

    /**
     * 查询销售单总数
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param accId
     * @return
     */
    public Long countSaleOrder(String startDate, String endDate,
                               String customerId, String saleId,
                               String saleCode,String accId) {

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("startDate", startDate);
        parameterMap.put("endDate", endDate);
        parameterMap.put("customerId", customerId);
        parameterMap.put("saleId", saleId);
        parameterMap.put("saleCode", saleCode);
        parameterMap.put("accId", accId);
        parameterMap.put("offset", DEFAULT_OFFSET);
        parameterMap.put("rows",DEFAULT_PAGE_SIZE);
        parameterMap.put("countFlag", COUNT_YES);
        return saleOrderDao.countSaleOrder(parameterMap);
    }

    /**
     * 查询销售单详情列表
     * @param startDate
     * @param endDate
     * @param supplierCode
     * @param saleId
     * @param saleCode
     * @param accId
     * @param goodsId
     * @param specId
     * @param page
     * @param rows
     * @return
     */
    public List<SaleOrderDetailVo> listSaleOrderDetail(String startDate, String endDate,
                                                       String supplierCode, String saleId,
                                                       String saleCode, String accId,
                                                       String goodsId, String specId, Integer page,
                                                       Integer rows) {
        // 分页
        int first = (page - 1) * rows;

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("startDate", startDate);
        parameterMap.put("endDate", endDate);
        parameterMap.put("customerId", supplierCode);
        parameterMap.put("saleId", saleId);
        parameterMap.put("saleCode", saleCode);
        parameterMap.put("goodsId", goodsId);
        parameterMap.put("goodsSpecId", specId);
        parameterMap.put("accId", accId);
        parameterMap.put("offset", first);
        parameterMap.put("rows", rows);
        parameterMap.put("countFlag", COUNT_NOT);
        return saleOrderDao.listSaleOrderDetail(parameterMap);
    }

    /**
     * 查询销售单详情总数
     * @param startDate
     * @param endDate
     * @param supplierCode
     * @param saleId
     * @param saleCode
     * @param accId
     * @param goodsId
     * @param specId
     * @return
     */
    public Long countSaleOrderDetail(String startDate, String endDate,
                                     String supplierCode, String saleId,
                                     String saleCode, String accId,
                                     String goodsId, String specId) {

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("startDate", startDate);
        parameterMap.put("endDate", endDate);
        parameterMap.put("customerId", supplierCode);
        parameterMap.put("saleId", saleId);
        parameterMap.put("saleCode", saleCode);
        parameterMap.put("goodsId", goodsId);
        parameterMap.put("goodsSpecId", specId);
        parameterMap.put("accId", accId);
        parameterMap.put("offset", DEFAULT_OFFSET);
        parameterMap.put("rows",DEFAULT_PAGE_SIZE);
        parameterMap.put("countFlag", COUNT_YES);

        return saleOrderDao.countSaleOrderDetail(parameterMap);
    }
}
