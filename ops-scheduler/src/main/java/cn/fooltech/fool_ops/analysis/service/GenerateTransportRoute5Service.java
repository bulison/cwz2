//package cn.fooltech.fool_ops.analysis.service;
//
//import cn.fooltech.fool_ops.analysis.dao.TransportPriceDao;
//import cn.fooltech.fool_ops.analysis.dao.TransportRoute5Dao;
//import cn.fooltech.fool_ops.analysis.dao.TransportRouteNewDao;
//import cn.fooltech.fool_ops.analysis.vo.TransportPriceVo;
//import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.beans.Transient;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Administrator on 2017/1/4.
// */
//@Slf4j
//@Service
//public class GenerateTransportRoute5Service {
//    @Autowired
//    TransportPriceDao transportPriceDao;
//    @Autowired
//    TransportRoute5Dao transportRoute5Dao;
//    //    @Scheduled(cron="0 15 00 * * ? *")
//    @Transient
//    public void generateTransportRoute() {
//        Long start = System.currentTimeMillis(); // 记录起始时间
//        transportPriceDao.invalideByEffectiveDate();
//        transportRoute5Dao.deleteAll();
//        List<TransportPriceVo> transportPriceVoList = transportPriceDao.selectValidTransportPrice();
//        int i = 0;
//        for (TransportPriceVo vo : transportPriceVoList) {
//
//            generateRoute1(vo);
//        }
//        Long end = System.currentTimeMillis();
//        System.out.println("================结束时间=============================");
////        System.out.println(DateUtil.getDateFomat());
//        System.out.println("================结束时间=============================");
//
//    }
//    public void generateRoute1(TransportPriceVo vo) {
//        log.debug("================generateRoute=============================");
//        int isExistRouteRule1 = transportRoute5Dao.isExistRouteRule1(vo.getId());
//        if (isExistRouteRule1 > 0&& StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getReceiptPlace()))&&StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getDeliveryPlace()))) {
//        } else {
//            String routeStr = "{\"de\":\"该记录的发货地ID\",  \"sh\":\"该记录的装运方式ID\",  \"tr\":\"该记录的运输方式ID\",  \"re\":\"该记录的收货地ID\"}";
//            routeStr = StringUtils.replace(routeStr, "该记录的发货地ID", vo.getDeliveryPlace());
//            routeStr = StringUtils.replace(routeStr, "该记录的装运方式ID", vo.getShipmentTypeId());
//            routeStr = StringUtils.replace(routeStr, "该记录的运输方式ID", vo.getTransportTypeId());
//            routeStr = StringUtils.replace(routeStr, "该记录的收货地ID", vo.getReceiptPlace());
//
//            TransportRouteVo firstTransportRouteVo = new TransportRouteVo();
//            firstTransportRouteVo.setTransportBillId(vo.getId());
//            firstTransportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//            firstTransportRouteVo.setReceiptPlace(vo.getReceiptPlace());
//            firstTransportRouteVo.setShipmentTypeId(vo.getShipmentTypeId());
//            firstTransportRouteVo.setTransportTypeId(vo.getTransportTypeId());
//            firstTransportRouteVo.setCreatorId(vo.getCreatorId());
////            firstTransportRouteVo.setCreateTime(new Date());
////            firstTransportRouteVo.setUpdateTime(new Date());
//            firstTransportRouteVo.setOrgId(vo.getOrgId());
//            firstTransportRouteVo.setAccId(vo.getAccId());
//            firstTransportRouteVo.setRoute(routeStr);
//            System.out.println("=================== A-B======================");
//            transportRoute5Dao.insertRoute1(firstTransportRouteVo);
//
//            List<TransportRouteVo> transportRouteVoList2 = transportRoute5Dao.getByReceiptPlace2( vo.getDeliveryPlace());
//          for(TransportRouteVo routeVo:transportRouteVoList2) {
//              int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getReceiptPlace());
//              if (isExistRoute == -1) {
//                  TransportRouteVo transportRouteVo = new TransportRouteVo();
//                  transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//                  transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
//                  transportRouteVo.setShipmentTypeId(null);
//                  transportRouteVo.setTransportTypeId(null);
//                  transportRouteVo.setCreatorId(vo.getCreatorId());
//                  transportRouteVo.setCreateTime(new Date());
//                  transportRouteVo.setUpdateTime(new Date());
//                  transportRouteVo.setOrgId(vo.getOrgId());
//                  transportRouteVo.setAccId(vo.getAccId());
//                  transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
////                  int ie = transportRouteNewDao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                  if (ie == 0) {
//                      System.out.println("=================== A-B-C======================"+transportRouteVo.getRoute());
//                  transportRoute5Dao.insertRoute2(transportRouteVo);
//                  }
//
//          }
//          //33
//            List<TransportRouteVo> transportRouteVoList3 = transportRoute5Dao.getByDeliveryPlace3( vo.getReceiptPlace());
//            for(TransportRouteVo routeVo:transportRouteVoList3) {
//                int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getDeliveryPlace());
//                if (isExistRoute == -1) {
//                    TransportRouteVo transportRouteVo = new TransportRouteVo();
//                    transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//                    transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
//                    transportRouteVo.setShipmentTypeId(null);
//                    transportRouteVo.setTransportTypeId(null);
//                    transportRouteVo.setCreatorId(vo.getCreatorId());
//                    transportRouteVo.setCreateTime(new Date());
//                    transportRouteVo.setUpdateTime(new Date());
//                    transportRouteVo.setOrgId(vo.getOrgId());
//                    transportRouteVo.setAccId(vo.getAccId());
//                    transportRouteVo.setRoute(routeStr+","+ routeVo.getRoute() );
////                    int ie = transportRoute5Dao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                    System.out.println("=================== A-B-C-D======================"+transportRouteVo.getRoute());
////                    if (ie == 0) {
//                        transportRoute5Dao.insert3(transportRouteVo);
////                    }
//                }
//            }
//            //44
//            List<TransportRouteVo> transportRouteVoList4 = transportRoute5Dao.getByReceiptPlaceGruop4(vo.getDeliveryPlace());
//            for(TransportRouteVo routeVo:transportRouteVoList4) {
//                int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getReceiptPlace());
//                if (isExistRoute == -1) {
//                    TransportRouteVo transportRouteVo = new TransportRouteVo();
//                    transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//                    transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
//                    transportRouteVo.setShipmentTypeId(null);
//                    transportRouteVo.setTransportTypeId(null);
//                    transportRouteVo.setCreateTime(new Date());
//                    transportRouteVo.setUpdateTime(new Date());
//                    transportRouteVo.setOrgId(vo.getOrgId());
//                    transportRouteVo.setAccId(vo.getAccId());
//                    transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
//                System.out.println("=================== A-B-C-D-E======================"+transportRouteVo.getRoute());
////                transportRouteDao.insert(transportRouteVo);
////                    int ie =  transportRouteNewDao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                    if (ie == 0) {
//                    transportRoute5Dao.insert4(transportRouteVo);
////                    }
//                }
//            }
//            //55
//            List<TransportRouteVo> transportRouteVoList5 = transportRoute5Dao.getByDeliveryPlaceGruop5(vo.getReceiptPlace());
//            for(TransportRouteVo routeVo:transportRouteVoList5) {
//                int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getDeliveryPlace());
//                if (isExistRoute == -1) {
//                    TransportRouteVo transportRouteVo = new TransportRouteVo();
////                transportRouteVo.setId(UUIDTool.getUUID());
//                    transportRouteVo.setDeliveryPlace(routeVo.getDeliveryPlace());
//                    transportRouteVo.setReceiptPlace(vo.getReceiptPlace());
//                    transportRouteVo.setShipmentTypeId(null);
//                    transportRouteVo.setTransportTypeId(null);
//                    transportRouteVo.setCreateTime(new Date());
//                    transportRouteVo.setUpdateTime(new Date());
//                    transportRouteVo.setOrgId(vo.getOrgId());
//                    transportRouteVo.setAccId(vo.getAccId());
//                    transportRouteVo.setRoute(routeStr + "," + routeVo.getRoute());
//                System.out.println("=================== A-B-C-D-E-F======================"+transportRouteVo.getRoute());
////                transportRouteDao.insert(transportRouteVo);
////                    int ie =  transportRouteNewDao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                    if (ie == 0) {
//                    transportRoute5Dao.insert5(transportRouteVo);
////                    }
//                }
//            }
//            //66
//            List<TransportRouteVo> transportRouteVoList6 = transportRoute5Dao.getByDeliveryPlaceGruop6(vo.getReceiptPlace());
//            for(TransportRouteVo routeVo:transportRouteVoList6) {
//                int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getReceiptPlace());
//                if (isExistRoute == -1) {
//                    TransportRouteVo transportRouteVo = new TransportRouteVo();
////                transportRouteVo.setId(UUIDTool.getUUID());
//                    transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//                    transportRouteVo.setReceiptPlace(vo.getReceiptPlace());
//                    transportRouteVo.setShipmentTypeId(null);
//                    transportRouteVo.setTransportTypeId(null);
//                    transportRouteVo.setCreateTime(new Date());
//                    transportRouteVo.setUpdateTime(new Date());
//                    transportRouteVo.setOrgId(vo.getOrgId());
//                    transportRouteVo.setAccId(vo.getAccId());
//
//
//                    transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
//                System.out.println("=================== A-B-C-D-E-F-G======================"+transportRouteVo.getRoute());
////                transportRouteDao.insert(transportRouteVo);
////                    int ie = transportRouteNewDao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                    if (ie == 0) {
//                    transportRoute5Dao.insert6(transportRouteVo);
////                    }
//                }
//            }
//            //777
//            List<TransportRouteVo> transportRouteVoList7 = transportRoute5Dao.getByReceiptPlaceGruop7(vo.getDeliveryPlace());
//            for(TransportRouteVo routeVo:transportRouteVoList7) {
//                int isExistRoute =   StringUtils.indexOf(routeVo.getRoute(), vo.getDeliveryPlace());
//                if (isExistRoute == -1) {
//                    TransportRouteVo transportRouteVo = new TransportRouteVo();
//                    transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
//                    transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
//                    transportRouteVo.setShipmentTypeId(null);
//                    transportRouteVo.setTransportTypeId(null);
//                    transportRouteVo.setCreateTime(new Date());
//                    transportRouteVo.setUpdateTime(new Date());
//                    transportRouteVo.setOrgId(vo.getOrgId());
//                    transportRouteVo.setAccId(vo.getAccId());
//                    transportRouteVo.setRoute(routeStr + "," + routeVo.getRoute());
//                    System.out.println("=================== A-B-C-D-E-F-G-H======================"+transportRouteVo.getRoute());
////                    int ie = transportRouteNewDao.isFullExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
////                    if (ie == 0) {
//                    transportRoute5Dao.insert7(transportRouteVo);
////                    }
//                }
//            }
//
//
//
//
//        }
//
//        }
//    }
//
