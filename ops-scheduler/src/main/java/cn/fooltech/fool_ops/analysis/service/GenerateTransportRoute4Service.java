package cn.fooltech.fool_ops.analysis.service;

import cn.fooltech.fool_ops.analysis.dao.TransportPriceDao;
import cn.fooltech.fool_ops.analysis.dao.TransportRoute3Dao;
import cn.fooltech.fool_ops.analysis.dao.TransportRoute4Dao;
import cn.fooltech.fool_ops.analysis.vo.TransportPriceVo;
import cn.fooltech.fool_ops.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.*;

@Service
public class GenerateTransportRoute4Service {
    @Autowired
    TransportPriceDao transportPriceDao;
    @Autowired
    TransportRoute4Dao transportRoute4Dao;

    //    @Scheduled(cron="0 15 00 * * ? *")
//    @Transient
    public void generateTransportRoute() throws InterruptedException {
        Long start = System.currentTimeMillis(); // 记录起始时间
        transportPriceDao.invalideByEffectiveDate();
        List<TransportPriceVo> transportPriceVoList = transportPriceDao.selectValidTransportPrice();
        transportRoute4Dao.createTemporaryRouteTable();
//        transportRoute4Dao.createTemporaryRouteTable_2();
        int i=0;
        for (TransportPriceVo vo : transportPriceVoList) {
            System.out.println(DateUtils.getCurrentDateTime()+"===================================="+i++);
            transportRoute4Dao.deleteTemporaryRouteTable();
            generateRoute1(vo);
            generateRoute2(vo);
            generateRoute3(vo);
            generateRoute4(vo);
            generateRoute5(vo);
            generateRoute6(vo);
            generateRoute7(vo);
            //数据迁移等2表
            List<TransportRouteVo> allRouteVoList= transportRoute4Dao.selectAllTemporaryRoute();
            if(null!=allRouteVoList&&allRouteVoList.size()>0) {
                transportRoute4Dao.insertTransportRouteBatch(allRouteVoList);
                //数据迁移等2表
                Thread.sleep(1000  );
            }
        }
        transportRoute4Dao.dropTemporaryRouteTable();


    }
    public void generateRoute1(TransportPriceVo vo)  {

        int isExistRouteRule1 = transportRoute4Dao.isExistRouteRule1(vo.getId());
        if (isExistRouteRule1 > 0 && StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getReceiptPlace())) && StringUtils.isNoneEmpty(StringUtils.trimToEmpty(vo.getDeliveryPlace()))) {
        } else {
            String routeStr = "{\"de\":\"该记录的发货地ID\",  \"sh\":\"该记录的装运方式ID\",  \"tr\":\"该记录的运输方式ID\",  \"re\":\"该记录的收货地ID\"}";
            routeStr = StringUtils.replace(routeStr, "该记录的发货地ID", vo.getDeliveryPlace());
            routeStr = StringUtils.replace(routeStr, "该记录的装运方式ID", vo.getShipmentTypeId());
            routeStr = StringUtils.replace(routeStr, "该记录的运输方式ID", vo.getTransportTypeId());
            routeStr = StringUtils.replace(routeStr, "该记录的收货地ID", vo.getReceiptPlace());
            TransportRouteVo routeVo = new TransportRouteVo();
            routeVo.setTransportBillId(vo.getId());
            routeVo.setDeliveryPlace(vo.getDeliveryPlace());
            routeVo.setReceiptPlace(vo.getReceiptPlace());
            routeVo.setShipmentTypeId(vo.getShipmentTypeId());
            routeVo.setTransportTypeId(vo.getTransportTypeId());
            routeVo.setCreatorId(vo.getCreatorId());
            routeVo.setCreateTime(new Date());
            routeVo.setUpdateTime(new Date());
            routeVo.setOrgId(vo.getOrgId());
            routeVo.setAccId(vo.getAccId());
            routeVo.setRoute(routeStr);
            transportRoute4Dao.insertTempTable(routeVo);
        }

    }

    public void generateRoute2(TransportPriceVo vo)  {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule2(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }

    public void generateRoute3(TransportPriceVo vo) throws InterruptedException {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule3(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }

    public void generateRoute4(TransportPriceVo vo) throws InterruptedException {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule4(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }
    public void generateRoute5(TransportPriceVo vo) throws InterruptedException {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule5(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }
    public void generateRoute6(TransportPriceVo vo) throws InterruptedException {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule6(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }
    public void generateRoute7(TransportPriceVo vo) throws InterruptedException {
        Map map= new HashMap();
        map.put("deliveryPlace",vo.getDeliveryPlace());
        map.put("receiptPlace",vo.getReceiptPlace());
        map.put("shipmentTypeId",vo.getShipmentTypeId());
        map.put("transportTypeId",vo.getTransportTypeId());
        map.put("creatorId",vo.getCreatorId());
        map.put("orgId",vo.getOrgId());
        map.put("accId",vo.getAccId());
        List<TransportRouteVo> routeRule2List= transportRoute4Dao.getRouteListByRule7(map);
        if(null!=routeRule2List&&routeRule2List.size()>0) {
            transportRoute4Dao.insertTempTableBatch(routeRule2List);
        }

    }

}
