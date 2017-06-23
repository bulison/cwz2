package fool_ops.scheduler;

import cn.fooltech.fool_ops.analysis.service.*;
import fool_ops.AbstractTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GenerateTransportRouteServiceTest extends AbstractTestCase {

    @Autowired
    GenerateTransportRoute2Service generateTransportRoute2Service ;
    @Autowired
    GenerateCostAnalysisService generateCostAnalysisService;
    @Autowired
    GenerateTransportRoute3Service generateTransportRoute3Service;
    @Autowired
    GenerateTransportRoute4Service generateTransportRoute4Service;
    @Test
    public  void test4() throws InterruptedException {
//        System.out.print("===============");
        generateTransportRoute4Service.generateTransportRoute();
//        generateTransportRouteService.initTransportPriceData();
    }
    @Test
    public  void test3() throws InterruptedException {
//        System.out.print("===============");
        generateTransportRoute3Service.generateTransportRoute();
//        generateTransportRouteService.initTransportPriceData();
    }
    @Test
    public  void test(){
//        System.out.print("===============");
        generateTransportRoute2Service.generateTransportRoute();
//        generateTransportRouteService.initTransportPriceData();
    }
    @Test
    public  void test2(){
//        System.out.print("===============");
        generateCostAnalysisService.generateCostAnalysis();
//        generateTransportRouteService.initTransportPriceData();
    }
}
