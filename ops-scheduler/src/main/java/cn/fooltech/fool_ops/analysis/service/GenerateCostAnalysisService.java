package cn.fooltech.fool_ops.analysis.service;

import cn.fooltech.fool_ops.analysis.dao.CostAnalysisDao;
import cn.fooltech.fool_ops.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.analysis.vo.RouteVo;
import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class GenerateCostAnalysisService {

    @Autowired
    CostAnalysisDao generateCostDao;
//    @Scheduled(cron="0/5 * *  * * ? ")
    @Transactional
    public void generateCostAnalysis() {
        long start = System.currentTimeMillis(); // 记录起始时间
        //tsb_purchase_price
        //将货品报价表中有效日期小于当天的记录的状态置为无效
        generateCostDao.invalidPurchasePriceByEffectiveDate();
        //将场地费报价表中有效日期小于当天的记录的状态置为无效；
        generateCostDao.invalidGroundPriceByEffectiveDate();
        generateCostDao.cleanCostAnalysisBillToday();
        generateCostDao.selectInsertCostAnalysisBill();
        List<CostAnalysisBillVo> costAnalysisBillVos=generateCostDao.selectAllCostAnalysisBillByToday();
        for(CostAnalysisBillVo vo:costAnalysisBillVos){

            String fid=vo.getId();
            String routeStr=vo.getRoute();
            List<RouteVo> routeVos = new ArrayList<RouteVo>();
            routeVos = JSONObject.parseArray(routeStr, RouteVo.class);
            log.debug("99999999999999999999999999"+"routeStr"+routeStr);

            for (RouteVo route:routeVos){
                log.debug("888888888888888888888"+"route",route.toString());
                CostAnalysisBilldetailVo detailVo= new CostAnalysisBilldetailVo();
                detailVo.setBillId(fid);
                detailVo.setDeliveryPlace(route.getDe());
                detailVo.setReceiptPlace(route.getRe());
                detailVo.setTransportTypeId(route.getTr());
                detailVo.setShipmentTypeId(route.getSh());
                detailVo.setCreateTime(new Date());
                detailVo.setUpdateTime(new Date());
                detailVo.setOrgId(vo.getOrgId());
                detailVo.setAccId(vo.getAccId());
                generateCostDao.insertCostAnalysisBilldetail(detailVo);
            }
//            JSON.parseArray(route);
        }


        generateCostDao.updateCostAnalysisBilldetailFreight();
        //678不做
        generateCostDao.updateCostAnalysisBillFreight();
        generateCostDao.updateCostAnalysisBillByYesterday();

        long end = System.currentTimeMillis();       // 记录结束时间
        System.out.println(end-start);              // 相减得出运行时间
    }
}
