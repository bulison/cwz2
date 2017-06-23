package cn.fooltech.fool_ops.analysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/12/31.
 */
@Component
public class ScheduleTaskService {
//    @Autowired
//    GenerateCostAnalysisService generateCostAnalysisService;
//    @Autowired
//    GenerateFactoryDeliveryAnalysisService generateFactoryDeliveryAnalysisService;
    @Autowired
    GenerateTransportRouteService generateTransportRouteService;

//    @Scheduled(cron="50 01 00 * * ?")
    public  void GenerateCostAnalysis(){
        long start = System.currentTimeMillis();
//        generateTransportRouteService.generateTransportRoute();
//        generateCostAnalysisService.generateCostAnalysis();
//        generateFactoryDeliveryAnalysisService.generateTransportRoute();
        long end = System.currentTimeMillis();       // 记录结束时间
        System.out.println(end-start);              // 相减得出运行时间
    }

}
