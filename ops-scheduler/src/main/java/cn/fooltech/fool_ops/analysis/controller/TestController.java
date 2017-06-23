package cn.fooltech.fool_ops.analysis.controller;


import cn.fooltech.fool_ops.analysis.dao.CostAnalysisDao;
import cn.fooltech.fool_ops.analysis.service.GenerateCostAnalysisService;
import cn.fooltech.fool_ops.analysis.service.GenerateTransportRouteService;

import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    GenerateTransportRouteService generateTransportRouteService;
    @Autowired
    GenerateCostAnalysisService generateCostAnalysisService;

    @GetMapping("/get")
    public  int insertOne(){
          generateTransportRouteService.generateTransportRoute();
        return 0;
    }

    @GetMapping("/get1")
    public  int selectAllCostAnalysisBillByToday(){
        ;
         generateCostAnalysisService.generateCostAnalysis();
        return 0;
    }



}
