package cn.fooltech.fool_ops.eureka.rateConsumer.web;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.eureka.rateConsumer.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {


    @Autowired
    private RateService rateService;


    @RequestMapping(value = "/saleOrderAnalyze", method = RequestMethod.GET)
    public PageJson saleOrderAnalyze(String accId, String goodsId,
                                     String specId, String page, String rows) {
        return rateService.saleOrderAnalyze(accId, goodsId, specId, page, rows);
    }


}