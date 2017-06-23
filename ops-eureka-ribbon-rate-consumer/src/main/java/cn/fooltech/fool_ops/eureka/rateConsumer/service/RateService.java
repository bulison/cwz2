package cn.fooltech.fool_ops.eureka.rateConsumer.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import com.google.common.collect.Maps;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * 收益率服务类
 * 使用断路器防止线程因调用故障服务被长时间占用不释放，避免了故障在分布式系统中的蔓延
 */
@Service
public class RateService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取销售订单收益率
     * @param accId
     * @param goodsId
     * @param specId
     * @param page
     * @param rows
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallBack")
    public PageJson saleOrderAnalyze(String accId, String goodsId,
                                     String specId, String page, String rows) {

        String url = "http://api-gateway/api/rate/saleOrderAnalyze";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accId", accId)
                .queryParam("goodsId", goodsId)
                .queryParam("specId", specId)
                .queryParam("page", page)
                .queryParam("rows", rows);

        System.out.println(builder.build().encode().toUri());

        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
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
