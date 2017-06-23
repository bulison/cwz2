package cn.fooltech.fool_ops.analysis.service;

import cn.fooltech.fool_ops.analysis.dao.TransportPriceDao;
import cn.fooltech.fool_ops.analysis.dao.TransportRoute3Dao;
import cn.fooltech.fool_ops.analysis.dao.TransportRouteNewDao;
import cn.fooltech.fool_ops.analysis.vo.TransportPriceVo;
import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Slf4j
@Service
public class GenerateTransportRoute3Service {
    @Autowired
    TransportPriceDao transportPriceDao;
    @Autowired
    TransportRoute3Dao transportRoute3Dao;
    //    @Scheduled(cron="0 15 00 * * ? *")
    @Transient
    public void generateTransportRoute() throws InterruptedException {
        Long start = System.currentTimeMillis(); // 记录起始时间
        transportPriceDao.invalideByEffectiveDate();
        transportRoute3Dao.deleteAll();
        List<TransportPriceVo> transportPriceVoList = transportPriceDao.selectValidTransportPrice();
        int i = 0;
        for (TransportPriceVo vo : transportPriceVoList) {
            i=i+1;
            System.out.println("================generateRoute============================="+i);
            generateRoute(vo,i);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute2(map);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute3(map);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute4(map);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute5(map);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute6(map);
        }
        for (TransportPriceVo vo : transportPriceVoList) {
            Map map= new  HashMap();
            map.put("deliveryPlace",vo.getDeliveryPlace());
            map.put("receiptPlace",vo.getReceiptPlace());
            map.put("shipmentTypeId",vo.getShipmentTypeId());
            map.put("transportTypeId",vo.getTransportTypeId());
            map.put("creatorId",vo.getCreatorId());
            map.put("orgId",vo.getOrgId());
            map.put("accId",vo.getAccId());
            generateRoute7(map);
        }
        Long end = System.currentTimeMillis();
        System.out.println("================结束时间=============================");
//        System.out.println(DateUtil.getDateFomat());
        System.out.println("================结束时间=============================");

    }
    public void generateRoute(TransportPriceVo vo,int size) throws InterruptedException {
        log.debug("================generateRoute=============================");
        int isExistRouteRule1 = transportRoute3Dao.isExistRouteRule1(vo.getId());
        if (isExistRouteRule1 > 0&& StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getReceiptPlace()))&&StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getDeliveryPlace()))) {
        } else {
//            transportRoute3Dao.insertRule1();
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
            System.out.println("=================== A-B======================");
            Thread.sleep(1000*1);
            transportRoute3Dao.insert(firstTransportRouteVo);











        }

    }

    public void generateRoute2(Map map) throws InterruptedException {
        log.debug("=================== Rule2===start==================="+ DateUtil.format(new Date(),DateUtil.dateTime));
        if(transportRoute3Dao.isExistRouteRule2(map)>0) {
            log.debug("=================== isExistRouteRule2====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));
            transportRoute3Dao.insertRouteRule2(map);
            log.debug("=================== insertRouteRule2====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }
    }
    public void generateRoute3(Map map) throws InterruptedException {
        log.debug("=================== Rule3===start==================="+ DateUtil.format(new Date(),DateUtil.dateTime));
        if(transportRoute3Dao.isExistRouteRule3(map)>0) {
            log.debug("=================== isExistRouteRule3====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

            transportRoute3Dao.insertRouteRule3(map);
            log.debug("=================== insertRouteRule3====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }

    }
    public void generateRoute4(Map map) throws InterruptedException {
        log.debug("=================== Rule4===start==================="+ DateUtil.format(new Date(),DateUtil.dateTime));
        if(transportRoute3Dao.isExistRouteRule4(map)>0) {
            log.debug("=================== isExistRouteRule4====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

            transportRoute3Dao.insertRouteRule4(map);
            log.debug("=================== insertRouteRule4====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }
    }
    public void generateRoute5(Map map) throws InterruptedException {
        log.debug("=================== Rule5===start==================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        if(transportRoute3Dao.isExistRouteRule5(map)>0) {
            log.debug("=================== isExistRouteRule5====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

            transportRoute3Dao.insertRouteRule5(map);
            log.debug("=================== insertRouteRule5====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }
    }
    public void generateRoute6(Map map) throws InterruptedException {
        log.debug("=================== Rule6====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        if(transportRoute3Dao.isExistRouteRule6(map)>0) {
            log.debug("=================== isExistRouteRule6====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

            transportRoute3Dao.insertRouteRule6(map);
            log.debug("=================== insertRouteRule6====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }
    }
    public void generateRoute7(Map map) throws InterruptedException {
        log.debug("=================== RouteRule7====start=================="+ DateUtil.format(new Date(),DateUtil.dateTime));


        if(transportRoute3Dao.isExistRouteRule7(map)>0) {
            log.debug("=================== isExistRouteRule7====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

            transportRoute3Dao.insertRouteRule7(map);
            log.debug("=================== insertRouteRule7====end=================="+ DateUtil.format(new Date(),DateUtil.dateTime));

        }

    }
}

