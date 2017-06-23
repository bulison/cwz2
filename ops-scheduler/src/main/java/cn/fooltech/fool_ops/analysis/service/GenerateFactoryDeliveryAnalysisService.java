package cn.fooltech.fool_ops.analysis.service;

import cn.fooltech.fool_ops.analysis.dao.FactoryDeliveryAnalysisDao;
import cn.fooltech.fool_ops.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.analysis.vo.RouteVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GenerateFactoryDeliveryAnalysisService {
    FactoryDeliveryAnalysisDao factoryDeliveryAnalysisDao;
//    @Scheduled(cron="0/5 * *  * * ? ")
    @Transactional
    public void generateTransportRoute() {
        long start = System.currentTimeMillis(); // 记录起始时间
        factoryDeliveryAnalysisDao.selectInsertCostAnalysisBill();
        List<CostAnalysisBillVo> costAnalysisBillVos=factoryDeliveryAnalysisDao.selectAllCostAnalysisBillByToday();
        for(CostAnalysisBillVo vo:costAnalysisBillVos){
            String fid=vo.getId();
            String routeStr=vo.getRoute();
            List<RouteVo> routeVos = new ArrayList<RouteVo>();
            routeVos = JSONObject.parseArray(routeStr, RouteVo.class);
            for (RouteVo route:routeVos){
                CostAnalysisBilldetailVo detailVo= new CostAnalysisBilldetailVo();
//                detailVo.setId();
                detailVo.setBillId(fid);
                detailVo.setDeliveryPlace(route.getDe());
                detailVo.setReceiptPlace(route.getRe());
                detailVo.setTransportTypeId(route.getTr());
                detailVo.setShipmentTypeId(route.getSh());
                detailVo.setCreateTime(new Date());
                detailVo.setUpdateTime(new Date());
                detailVo.setOrgId(vo.getOrgId());
                detailVo.setAccId(vo.getAccId());
                factoryDeliveryAnalysisDao.insertCostAnalysisBilldetail(detailVo);
            }
        }
        factoryDeliveryAnalysisDao.updateCostAnalysisBilldetailFreight();
        long end = System.currentTimeMillis();       // 记录结束时间
        System.out.println(end-start);              // 相减得出运行时间
    }
}
