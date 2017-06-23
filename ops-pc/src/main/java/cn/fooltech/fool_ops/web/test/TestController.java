package cn.fooltech.fool_ops.web.test;

import cn.fooltech.fool_ops.rate.tools.Calculate;
import cn.fooltech.fool_ops.rate.tools.EveryTradeBean;
import cn.fooltech.fool_ops.rate.tools.RateBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;


/**
 * Created by Administrator on 2016/11/22.
 */

@RestController
@RequestMapping("/api")
public class TestController {

    /**
     * 计算实际收益率
     * @return
     * @throws Exception
     */
    @GetMapping("rateTest")
    public String rateTest()  throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RateBean real = new RateBean();

        EveryTradeBean etb1 = new EveryTradeBean();
        etb1.setMoney(820687);
        etb1.setTime(sdf.parse("2017-05-05"));
        etb1.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb2 = new EveryTradeBean();
        etb2.setMoney(410343.52);
        etb2.setTime(sdf.parse("2017-05-02"));
        etb2.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb3 = new EveryTradeBean();
        etb3.setMoney(410000);
        etb3.setTime(sdf.parse("2017-04-25"));
        etb3.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb4 = new EveryTradeBean();
        etb4.setMoney(410000);
        etb4.setTime(sdf.parse("2017-04-21"));
        etb4.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb5 = new EveryTradeBean();
        etb5.setMoney(420000);
        etb5.setTime(sdf.parse("2017-04-20"));
        etb5.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb6 = new EveryTradeBean();
        etb6.setMoney(410000);
        etb6.setTime(sdf.parse("2017-04-28"));
        etb6.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        real.getIncomeList().add(etb1);
        real.getIncomeList().add(etb2);
        real.getIncomeList().add(etb3);
        real.getIncomeList().add(etb4);
        real.getIncomeList().add(etb5);
        real.getIncomeList().add(etb6);
        ////////////////////////////////////////////////

        EveryTradeBean etbc1 = new EveryTradeBean();
        etbc1.setMoney(180672.44);
        etbc1.setTime(sdf.parse("2017-02-28"));
        etbc1.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etbc2 = new EveryTradeBean();
        etbc2.setMoney(2473782);
        etbc2.setTime(sdf.parse("2017-02-14"));
        etbc2.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etbc3 = new EveryTradeBean();
        etbc3.setMoney(-359.0745025275);
        etbc3.setTime(sdf.parse("2017-02-15"));
        etbc3.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        real.getExpendList().add(etbc1);
        real.getExpendList().add(etbc2);
        real.getExpendList().add(etbc3);


        real.setDayFundLostRate(0.00022);
        real.setFirstPayDay(sdf.parse("2017-02-15"));

        Calculate.initRealRateBean(real);
        Calculate.outRateBean(real);

        return real.getCycleRate()+"";
    }

    /**
     * 计算预计收益率
     * @return
     * @throws Exception
     */
    @GetMapping("rateTest2")
    public String rateTest2()  throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RateBean real = new RateBean();

        EveryTradeBean etb1 = new EveryTradeBean();
        etb1.setMoney(820687);
        etb1.setTime(sdf.parse("2017-05-05"));
        etb1.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb2 = new EveryTradeBean();
        etb2.setMoney(410343.52);
        etb2.setTime(sdf.parse("2017-05-02"));
        etb2.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb3 = new EveryTradeBean();
        etb3.setMoney(410000);
        etb3.setTime(sdf.parse("2017-04-25"));
        etb3.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb4 = new EveryTradeBean();
        etb4.setMoney(410000);
        etb4.setTime(sdf.parse("2017-04-21"));
        etb4.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb5 = new EveryTradeBean();
        etb5.setMoney(420000);
        etb5.setTime(sdf.parse("2017-04-20"));
        etb5.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etb6 = new EveryTradeBean();
        etb6.setMoney(410000);
        etb6.setTime(sdf.parse("2017-04-28"));
        etb6.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        real.getIncomeList().add(etb1);
        real.getIncomeList().add(etb2);
        real.getIncomeList().add(etb3);
        real.getIncomeList().add(etb4);
        real.getIncomeList().add(etb5);
        real.getIncomeList().add(etb6);
        ////////////////////////////////////////////////

        EveryTradeBean etbc1 = new EveryTradeBean();
        etbc1.setMoney(180672.44);
        etbc1.setTime(sdf.parse("2017-02-28"));
        etbc1.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etbc2 = new EveryTradeBean();
        etbc2.setMoney(2473782);
        etbc2.setTime(sdf.parse("2017-02-14"));
        etbc2.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        EveryTradeBean etbc3 = new EveryTradeBean();
        etbc3.setMoney(-359.0745025275);
        etbc3.setTime(sdf.parse("2017-02-15"));
        etbc3.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);

        real.getExpendList().add(etbc1);
        real.getExpendList().add(etbc2);
        real.getExpendList().add(etbc3);

        real.setDayFundLostRate(0.00022);
        real.setFirstPayDay(sdf.parse("2017-02-15"));

        Calculate.initRateBean(real);
        Calculate.outRateBean(real);

        return real.getCycleRate()+"";
    }
}
