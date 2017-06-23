package cn.fooltech.fool_ops.analysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/12/31.
 */
@Component
public class GenerateTransportPrice {
    @Autowired
    GenerateTransportRouteService generateTransportRouteService;
//@Autowired
//@Scheduled(cron="0/30 * *  * * ? ")
void generate(){
    //测试记录等于无效
 //插入运输报价表
//    、、System.out.println("进入测试");
    generateTransportRouteService.generateTransportRoute();

}
}
