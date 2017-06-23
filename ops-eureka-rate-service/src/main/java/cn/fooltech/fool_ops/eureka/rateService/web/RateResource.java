package cn.fooltech.fool_ops.eureka.rateService.web;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.eureka.rateService.dao.GoodsProfitDao;
import cn.fooltech.fool_ops.eureka.rateService.vo.SaleOrderAnalyzeVo;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * restful  收益率Controller
 */
@RestController
@RequestMapping(value = "/api/rate")
public class RateResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;
    @Autowired
    private GoodsProfitDao goodsProfitDao;

    /**
     * 销售订单分析（演示）
     * @param accId
     * @param goodsId
     * @param specId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOrderAnalyze")
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson saleOrderAnalyze(String accId, String goodsId,
                                     String specId,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "rows", defaultValue = "10")Integer rows) {
        ServiceInstance instance = client.getLocalServiceInstance();

        logger.info("/saleOrderAnalyze, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        logger.info("/saleOrderAnalyze, accId:" + accId + ",goodsId:" + goodsId + ", specId:" + specId);

        List<SaleOrderAnalyzeVo> datas = Lists.newArrayList();
        SaleOrderAnalyzeVo vo1 = new SaleOrderAnalyzeVo("aaa", "方块煤", 64.6521);
        SaleOrderAnalyzeVo vo2 = new SaleOrderAnalyzeVo("bbb", "煤粉", 78.4102);

        datas.add(vo1);
        datas.add(vo2);

        PageRequest pageRequest = new PageRequest(page-1, rows);
        Page<SaleOrderAnalyzeVo> pageData = new PageImpl<SaleOrderAnalyzeVo>(datas, pageRequest, 2);

        return new PageJson(pageData);
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