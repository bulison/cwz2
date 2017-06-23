package cn.fooltech.fool_ops.eureka.rateService.web;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.eureka.rateService.service.SaleOrderService;
import cn.fooltech.fool_ops.eureka.rateService.service.SaleOutService;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOutDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOutRelationVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOutVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 销售出库单Resource
 */
@RestController
@RequestMapping(value = "/api/saleOut")
public class SaleOutResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SaleOutService saleOutService;

    @ApiOperation("查询销售出库单分析")
    @GetMapping("/saleOutAnalyze")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson listSaleOrder(String startDate, String endDate,
                                  String customerId, String saleId,
                                  String saleCode,
                                  @RequestParam(defaultValue = "0")Double dayFundLoseRate,
                                  @RequestParam String orgId,
                                  @RequestParam String accId,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "rows", defaultValue = "10")Integer rows){


        logger.info("/saleOutAnalyze, startDate:" + startDate + ",endDate:" + endDate +
                ", customerId:" + customerId+", saleId:" + saleId+", saleCode:" + saleCode
                +", dayFundLoseRate:" + dayFundLoseRate+", orgId:" + orgId+", accId:" + accId);

        Page<SaleOutVo> pagedata = saleOutService.pageSaleOut(startDate, endDate, customerId,
                saleId, saleCode, orgId, accId, dayFundLoseRate, page, rows);

        return new PageJson(pagedata);
    }

    @ApiOperation("查询销售出库单关联单据")
    @GetMapping("/saleOutRelation")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson saleOutRelation(@RequestParam String saleOutId,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "rows", defaultValue = "10")Integer rows){

        logger.info("/saleOutRelation, saleOutId:" + saleOutId);

        Page<SaleOutRelationVo> pagedata = saleOutService.pageSaleOutRelation(saleOutId, page, rows);

        return new PageJson(pagedata);
    }

    @ApiOperation("查询销售出库单分析")
    @GetMapping("/saleOutDetailAnalyze")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson listSaleOrderDetail(String startDate, String endDate,
                                  String customerId, String saleId,
                                  String saleCode,String goodId,String goodSpecId,
                                  @RequestParam String orgId,
                                  @RequestParam String accId,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "rows", defaultValue = "10")Integer rows){


        logger.info("/saleOutDetailAnalyze, startDate:" + startDate + ",endDate:" + endDate +
                ", customerId:" + customerId+", saleId:" + saleId+", saleCode:" + saleCode
                +", goodId:" + goodId+", goodSpecId:" + goodSpecId+", orgId:" + orgId+", accId:" + accId);

        Page<SaleOutDetailVo> pagedata = saleOutService.pageSaleOutDetail(startDate, endDate, customerId,
                saleId, saleCode, goodId, goodSpecId, orgId, accId, page, rows);

        return new PageJson(pagedata);
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
