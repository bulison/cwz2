package cn.fooltech.fool_ops.eureka.rateService.web;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.eureka.rateService.service.SaleOrderService;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiOperation;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>销售订单</p>
 *
 * @author chk
 * @date 2017-03-31
 */

@RestController
@RequestMapping(value = "/api/saleOrder")
public class SaleOrderResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SaleOrderService saleOrderService;

    @ApiOperation("查询销售订单")
    @GetMapping("/orderAnalyze")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson listSaleOrder(String startDate, String endDate,
                                  String customerId, String saleId,
                                  String saleCode,
                                  @RequestParam String accId,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "rows", defaultValue = "10")Integer rows){


        logger.info("/orderAnalyze, startDate:" + startDate + ",endDate:" + endDate +
                ", customerId:" + customerId+", saleId:" + saleId+", saleCode:" + saleCode);

        PageJson pageJson = new PageJson();

        Long total = saleOrderService.countSaleOrder(startDate, endDate,
                customerId, saleId, saleCode, accId);
        if(total!=null && total>0){
            List<SaleOrderVo> list = saleOrderService.listSaleOrder(startDate, endDate,
                    customerId, saleId, saleCode, accId, page, rows);
            pageJson.setRows(list);
            pageJson.setTotal(total);
        }

        return pageJson;
    }

    @ApiOperation("查询销售订单详情")
    @GetMapping("/detailAnalyze")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson listSaleOrderDetail(String startDate, String endDate,
                                        String supplierCode, String saleId,
                                        String saleCode, String goodsId, String specId,
                                        @RequestParam String accId,
                                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(name = "rows", defaultValue = "10")Integer rows) {


        logger.info("/detailAnalyze, startDate:" + startDate + ",endDate:" + endDate +
                ", supplierCode:" + supplierCode+", saleId:" + saleId+", saleCode:" + saleCode
                +", goodsId:" + goodsId+", specId:" + specId);


        PageJson pageJson = new PageJson();

        Long total = saleOrderService.countSaleOrderDetail(startDate, endDate,
                supplierCode, saleId, saleCode, accId, goodsId, specId);

        if (total!=null && total>0) {
            List<SaleOrderDetailVo> list = saleOrderService.listSaleOrderDetail(startDate, endDate,
                    supplierCode, saleId, saleCode, accId, goodsId, specId, page, rows);
            pageJson.setRows(list);
            pageJson.setTotal(total);
        }
        return pageJson;
    }

    /**
     * 失败后的短路回调函数
     * @return
     */
    public PageJson fallBack(){
        PageJson pageJson = new PageJson(PageJson.ERROR_CODE_FAIL);
        return pageJson;
    }
}
