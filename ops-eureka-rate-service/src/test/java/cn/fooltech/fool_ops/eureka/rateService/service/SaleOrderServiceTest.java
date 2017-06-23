package cn.fooltech.fool_ops.eureka.rateService.service;

import cn.fooltech.fool_ops.eureka.rateService.dao.SaleOrderDao;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import cn.fooltech.fool_ops.eureka.rateService.RateServiceApplication;

import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RateServiceApplication.class)
@WebAppConfiguration
public class SaleOrderServiceTest {

    @Autowired
    private SaleOrderDao saleOrderDao;

    @Test
    public void testListSaleOrder() {
        System.out.println("running testListSaleOrder");

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("startDate", "");
        parameterMap.put("endDate", "");
        parameterMap.put("customerId", "");
        parameterMap.put("saleId", "");
        parameterMap.put("saleCode", "");
        parameterMap.put("accId", "");
        parameterMap.put("orgId", "");
        parameterMap.put("offset", 1);
        parameterMap.put("rows", 10);
        parameterMap.put("countFlag", 1);

//        System.out.println(saleOrderDao.listSaleOrder(parameterMap));
//        Assert.assertEquals("make believe", "sony");
    }

    public void testCountSaleOrder() {

    }

    public void testListSaleOrderDetail() {

    }

    public void testCountSaleOrderDetail() {

    }
}
