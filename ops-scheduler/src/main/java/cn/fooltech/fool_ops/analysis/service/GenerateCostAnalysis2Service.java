//package cn.fooltech.fool_ops.analysis.service;
//
//import cn.fooltech.fool_ops.analysis.dao.CostAnalysis2Dao;
//import cn.fooltech.fool_ops.analysis.dao.CostAnalysisDao;
//import cn.fooltech.fool_ops.analysis.vo.CostAnalysisBilldetailVo;
//import cn.fooltech.fool_ops.analysis.vo.RouteVo;
//import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Administrator on 2017/1/5.
// */
//@Service
//public class GenerateCostAnalysis2Service {
//    @Autowired
//    CostAnalysis2Dao costAnalysis2Dao;
//
//    @Transactional
//    public void generateCostAnalysis() {
//        long start = System.currentTimeMillis(); // 记录起始时间
//        //tsb_purchase_price
//        //将货品报价表中有效日期小于当天的记录的状态置为无效
//        costAnalysis2Dao.invalidPurchasePriceByEffectiveDate();
//        //将场地费报价表中有效日期小于当天的记录的状态置为无效；
//        costAnalysis2Dao.invalidGroundPriceByEffectiveDate();
//        costAnalysis2Dao.selectInsertCostAnalysisBill();
//        List<CostAnalysisBillVo> costAnalysisBillVos=generateCostDao.selectAllCostAnalysisBillByToday();
//        for(CostAnalysisBillVo vo:costAnalysisBillVos){
////            JSON.parseArray()
//            String fid=vo.getId();
//            String routeStr=vo.getRoute();
//            List<RouteVo> routeVos = new ArrayList<RouteVo>();
//            routeVos = JSONObject.parseArray(routeStr, RouteVo.class);
//            for (RouteVo route:routeVos){
//                CostAnalysisBilldetailVo detailVo= new CostAnalysisBilldetailVo();
////                detailVo.setId();
//                detailVo.setBillId(fid);
//                detailVo.setDeliveryPlace(route.getDe());
//                detailVo.setReceiptPlace(route.getRe());
//                detailVo.setTransportTypeId(route.getTr());
//                detailVo.setShipmentTypeId(route.getSh());
//                detailVo.setCreateTime(new Date());
//                detailVo.setUpdateTime(new Date());
//                detailVo.setOrgId(vo.getOrgId());
//                detailVo.setAccId(vo.getAccId());
//                generateCostDao.insertCostAnalysisBilldetail(detailVo);
//            }
////            JSON.parseArray(route);
//        }
//
//
//        generateCostDao.updateCostAnalysisBilldetailFreight();
//        //678不做
//        generateCostDao.updateCostAnalysisBillFreight();
//        generateCostDao.updateCostAnalysisBillByYesterday();
//
//        long end = System.currentTimeMillis();       // 记录结束时间
//    }
//}
