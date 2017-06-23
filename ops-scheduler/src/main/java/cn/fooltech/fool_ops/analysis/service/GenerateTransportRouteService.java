package cn.fooltech.fool_ops.analysis.service;


import cn.fooltech.fool_ops.analysis.dao.TransportPriceDao;
import cn.fooltech.fool_ops.analysis.dao.TransportRouteDao;
import cn.fooltech.fool_ops.analysis.vo.TransportPriceVo;
import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

//import cn.fooltech.fool_ops.domain.analysis.vo.TransportRouteVo;
//import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;

@Slf4j
@Service
public class GenerateTransportRouteService {


    @Autowired
    TransportPriceDao transportPriceDao;
    @Autowired
    TransportRouteDao transportRouteDao;


    //    @Scheduled(cron="0 15 00 * * ? *")
    @Transient
    public void generateTransportRoute() {
        Long start = System.currentTimeMillis(); // 记录起始时间
        transportPriceDao.invalideByEffectiveDate();
        transportRouteDao.deleteAll();
        List<TransportPriceVo> transportPriceVoList = transportPriceDao.selectValidTransportPrice();
        int i = 0;
        for (TransportPriceVo vo : transportPriceVoList) {
            generateRoute(vo);
        }
        Long end = System.currentTimeMillis();

        log.debug("=============================================",end-start);

    }


    public void generateRoute(TransportPriceVo vo) {
        int isExistRouteRule1 = transportRouteDao.isExistRouteRule1(vo.getId());
        if (isExistRouteRule1 > 0&&StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getReceiptPlace()))&&StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getDeliveryPlace()))) {
        } else {
            String routeStr = "{\"de\":\"该记录的发货地ID\",  \"sh\":\"该记录的装运方式ID\",  \"tr\":\"该记录的运输方式ID\",  \"re\":\"该记录的收货地ID\"}";
            routeStr = StringUtils.replace(routeStr, "该记录的发货地ID", vo.getDeliveryPlace());
            routeStr = StringUtils.replace(routeStr, "该记录的装运方式ID", vo.getShipmentTypeId());
            routeStr = StringUtils.replace(routeStr, "该记录的运输方式ID", vo.getTransportTypeId());
            routeStr = StringUtils.replace(routeStr, "该记录的收货地ID", vo.getReceiptPlace());

            TransportRouteVo firstTransportRouteVo = new TransportRouteVo();
            firstTransportRouteVo.setTransportBillId(vo.getId());
            firstTransportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
            firstTransportRouteVo.setReceiptPlace(vo.getReceiptPlace());
            firstTransportRouteVo.setShipmentTypeId(vo.getShipmentTypeId());
            firstTransportRouteVo.setTransportTypeId(vo.getTransportTypeId());
            firstTransportRouteVo.setCreatorId(vo.getCreatorId());
            firstTransportRouteVo.setCreateTime(new Date());
            firstTransportRouteVo.setUpdateTime(new Date());
            firstTransportRouteVo.setOrgId(vo.getOrgId());
            firstTransportRouteVo.setAccId(vo.getAccId());
            firstTransportRouteVo.setRoute(routeStr);
            transportRouteDao.insert(firstTransportRouteVo);

            List<TransportRouteVo> transportRouteVoList2 = transportRouteDao.getListByRouteRule2(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);
            for (TransportRouteVo routeVo : transportRouteVoList2) {
                if(StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getReceiptPlace()))&&StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getDeliveryPlace()))) {
                    TransportRouteVo transportRouteVo = new TransportRouteVo();
                    transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
                    transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
                    transportRouteVo.setShipmentTypeId(null);
                    transportRouteVo.setTransportTypeId(null);
                    transportRouteVo.setCreatorId(vo.getCreatorId());
                    transportRouteVo.setCreateTime(new Date());
                    transportRouteVo.setUpdateTime(new Date());
                    transportRouteVo.setOrgId(vo.getOrgId());
                    transportRouteVo.setAccId(vo.getAccId());
                    transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
                    int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                    if (ie == 0) {
                        transportRouteDao.insert(transportRouteVo);
                    }
                }
            }


            List<TransportRouteVo> transportRouteVoList3 = transportRouteDao.getListByRouteRule3(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);
            for (TransportRouteVo routeVo : transportRouteVoList3) {
                TransportRouteVo transportRouteVo = new TransportRouteVo();
                transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
                transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
                transportRouteVo.setShipmentTypeId(null);
                transportRouteVo.setTransportTypeId(null);
                transportRouteVo.setCreatorId(vo.getCreatorId());
                transportRouteVo.setCreateTime(new Date());
                transportRouteVo.setUpdateTime(new Date());
                transportRouteVo.setOrgId(vo.getOrgId());
                transportRouteVo.setAccId(vo.getAccId());
                transportRouteVo.setRoute(routeStr+","+ routeVo.getRoute() );
                int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                if (ie == 0) {
                    transportRouteDao.insert(transportRouteVo);
                }
            }

            List<TransportRouteVo> transportRouteVoList4 = transportRouteDao.getListByRouteRule4(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);
            for (TransportRouteVo routeVo : transportRouteVoList4) {
                TransportRouteVo transportRouteVo = new TransportRouteVo();
                transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
                transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
                transportRouteVo.setShipmentTypeId(null);
                transportRouteVo.setTransportTypeId(null);
                transportRouteVo.setCreateTime(new Date());
                transportRouteVo.setUpdateTime(new Date());
                transportRouteVo.setOrgId(vo.getOrgId());
                transportRouteVo.setAccId(vo.getAccId());
                transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
//                System.out.println("=================== A-B-C-D-E======================"+transportRouteVo.getRoute());
//                transportRouteDao.insert(transportRouteVo);
                int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                if (ie == 0) {
                    transportRouteDao.insert(transportRouteVo);
                }
            }

            List<TransportRouteVo> transportRouteVoList5 = transportRouteDao.getListByRouteRule5(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);


            for (TransportRouteVo routeVo : transportRouteVoList5) {
//                System.out.println("=========================================" +transportRouteVoList5.size());
                TransportRouteVo transportRouteVo = new TransportRouteVo();
//                transportRouteVo.setId(UUIDTool.getUUID());
                transportRouteVo.setDeliveryPlace(routeVo.getDeliveryPlace());
                transportRouteVo.setReceiptPlace(vo.getReceiptPlace());
                transportRouteVo.setShipmentTypeId(null);
                transportRouteVo.setTransportTypeId(null);
                transportRouteVo.setCreateTime(new Date());
                transportRouteVo.setUpdateTime(new Date());
                transportRouteVo.setOrgId(vo.getOrgId());
                transportRouteVo.setAccId(vo.getAccId());
                transportRouteVo.setRoute(routeStr + "," + routeVo.getRoute());
//                System.out.println("=================== A-B-C-D-E-F======================"+transportRouteVo.getRoute());
//                transportRouteDao.insert(transportRouteVo);
                int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                if (ie == 0) {
                    transportRouteDao.insert(transportRouteVo);
                }
            }

            List<TransportRouteVo> transportRouteVoList6 = transportRouteDao.getListByRouteRule6(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);


            for (TransportRouteVo routeVo : transportRouteVoList6) {
//                System.out.println("=========================================" +transportRouteVoList6.size());
                TransportRouteVo transportRouteVo = new TransportRouteVo();
//                transportRouteVo.setId(UUIDTool.getUUID());
                transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
                transportRouteVo.setReceiptPlace(vo.getReceiptPlace());
                transportRouteVo.setShipmentTypeId(null);
                transportRouteVo.setTransportTypeId(null);
                transportRouteVo.setCreateTime(new Date());
                transportRouteVo.setUpdateTime(new Date());
                transportRouteVo.setOrgId(vo.getOrgId());
                transportRouteVo.setAccId(vo.getAccId());


                transportRouteVo.setRoute(routeVo.getRoute() + "," + routeStr);
//                System.out.println("=================== A-B-C-D-E-F-G======================"+transportRouteVo.getRoute());
//                transportRouteDao.insert(transportRouteVo);
                int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                if (ie == 0) {
                    transportRouteDao.insert(transportRouteVo);
                }
            }
            List<TransportRouteVo> transportRouteVoList7 = transportRouteDao.getListByRouteRule7(vo.getReceiptPlace(), vo.getDeliveryPlace(),routeStr);

//            System.out.println("====================getListByRouteRule7=====================" );
            for (TransportRouteVo routeVo : transportRouteVoList7) {
//                System.out.println("=========================================" +transportRouteVoList7.size());
                TransportRouteVo transportRouteVo = new TransportRouteVo();
                transportRouteVo.setDeliveryPlace(vo.getDeliveryPlace());
                transportRouteVo.setReceiptPlace(routeVo.getReceiptPlace());
                transportRouteVo.setShipmentTypeId(null);
                transportRouteVo.setTransportTypeId(null);
                transportRouteVo.setCreateTime(new Date());
                transportRouteVo.setUpdateTime(new Date());
                transportRouteVo.setOrgId(vo.getOrgId());
                transportRouteVo.setAccId(vo.getAccId());
                transportRouteVo.setRoute(routeStr + "," + routeVo.getRoute());
                int ie = transportRouteDao.isExistRoute(vo.getReceiptPlace(), vo.getDeliveryPlace(), transportRouteVo.getRoute());
                if (ie == 0) {
                    transportRouteDao.insert(transportRouteVo);
                }

            }
        }


    }


}

