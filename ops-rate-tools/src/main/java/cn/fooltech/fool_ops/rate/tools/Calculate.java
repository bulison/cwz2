package cn.fooltech.fool_ops.rate.tools;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Calculate {
    // 控制台是否打印计算过程
    //private static boolean show = false;

    // 默认除法运算精度
    public static final int DEF_DIV_SCALE = 20;


    /**
     * 计算RateBean，不考虑交易数据预计、实际标识
     *
     * @param rateBean
     * @return
     * @throws Exception
     */
    public static RateBean initRateBean(RateBean rateBean) {
        if (null == rateBean)
            return rateBean;

        if (rateBean.getExpendList() == null
                || rateBean.getExpendList().size() == 0)
            return rateBean;

        List<EveryTradeBean> incomeList = rateBean.getIncomeList();
        List<EveryTradeBean> expendList = rateBean.getExpendList();

        Collections.reverse(incomeList);
        Collections.sort(expendList);

        //没有设置开始时间则取第一笔交易时间
        if (rateBean.getFirstPayDay() == null) {
            // 第一笔支出时间
            Date firstPayDay = expendList.get(0).getTime();
            rateBean.setFirstPayDay(firstPayDay);
        }

        initOccupyDay(rateBean);//计算资金占用天数Tn Tm
        initTotalIncome(rateBean);//计算净收入
        initTotalOutcome(rateBean);//计算净支出
        initAvgeffectiveDay(rateBean);//计算有效天数

        initRate(rateBean);//计算即时收益率
        initCycleRate(rateBean);//计算周期收益率
        initMarketRate(rateBean);//计算市场参考收益率

        return rateBean;
    }

    /**
     * 计算RateBean，考虑交易数据预计、实际标识
     *
     * @param rateBean
     * @return
     * @throws Exception
     */
    public static RateBean initRealRateBean(RateBean rateBean) {
        if (null == rateBean)
            return rateBean;

        if (rateBean.getExpendList() == null
                || rateBean.getExpendList().size() == 0)
            return rateBean;

        List<EveryTradeBean> incomeList = rateBean.getIncomeList();
        List<EveryTradeBean> expendList = rateBean.getExpendList();

        Collections.reverse(incomeList);
        Collections.sort(expendList);

        //没有设置开始时间则取第一笔交易时间
        if (rateBean.getFirstPayDay() == null) {
            // 第一笔支出时间
            Date firstPayDay = expendList.get(0).getTime();
            rateBean.setFirstPayDay(firstPayDay);
        }

        initOccupyDay(rateBean);//计算资金占用天数Tn Tm
        initTotalIncome(rateBean);//计算净收入
        initTotalOutcome(rateBean);//计算净支出
        initAvgeffectiveDay(rateBean);//计算有效天数

        initRealCycleRate(rateBean);//计算周期收益率
        initMarketRate(rateBean);//计算市场参考收益率

        return rateBean;
    }

    /**
     * 初始化资金占用天数(计算Tn Tm)
     *
     * @param rateBean
     */
    private static void initOccupyDay(RateBean rateBean) {
        List<EveryTradeBean> expendList = rateBean.getExpendList();
        for (EveryTradeBean everyTradeBean : expendList) {
            // Tn = 收入日期 - 开始日期
            everyTradeBean.setOccupyDays(fundOccupyDays(everyTradeBean.getTime(), rateBean.getFirstPayDay()));
        }

        List<EveryTradeBean> incomeList = rateBean.getIncomeList();
        for (EveryTradeBean everyTradeBean : incomeList) {
            // Tm = 支出日期 - 开始日期
            everyTradeBean.setOccupyDays(fundOccupyDays(everyTradeBean.getTime(), rateBean.getFirstPayDay()));
        }
    }

    /**
     * 总收入
     *
     * @return
     * @throws Exception
     */
    private static void initTotalIncome(RateBean rateBean) {

        List<EveryTradeBean> incomeList = rateBean.getIncomeList();
        double total = 0;

        // Cn净收入=∑[每笔收入*(1+β)^-Tn]
        for (EveryTradeBean everyTradeBean : incomeList) {
            double result = ArithUtil.add(1, rateBean.getDayFundLostRate());//1+β
            result = Math.pow(result, -everyTradeBean.getOccupyDays());//(1+β)^-Tn
            result = ArithUtil.mul(result, everyTradeBean.getMoney());//每笔收入*(1+β)^-Tn
            total = ArithUtil.add(result, total);
        }

        rateBean.setIncomeTotal(total);
    }

    /**
     * 总支出
     *
     * @throws Exception
     */
    private static void initTotalOutcome(RateBean rateBean) {

        List<EveryTradeBean> outcomeList = rateBean.getExpendList();
        double total = 0;

        for (EveryTradeBean everyTradeBean : outcomeList) {

            double result = ArithUtil.add(1, rateBean.getDayFundLostRate());//1+β
            result = Math.pow(result, -everyTradeBean.getOccupyDays());//(1+β)^-Tn
            result = ArithUtil.mul(result, everyTradeBean.getMoney());//每笔支出*(1+β)^-Tn
            total = ArithUtil.add(result, total);
        }
        rateBean.setExpendTotal(total);
    }

    /**
     * 计算平均收入或支出天数
     *
     * @param rateBean
     * @return
     * @throws Exception
     */
    private static void initAvgeffectiveDay(RateBean rateBean) {

        List<EveryTradeBean> incomelist = rateBean.getIncomeList();
        List<EveryTradeBean> expendlist = rateBean.getExpendList();

        double avgIncomeDay = 0;
        for (EveryTradeBean everyTradeBean : incomelist) {
            // Σ(每笔回款/回款总数*距第一笔支出天数)

            avgIncomeDay = ArithUtil.add(ArithUtil.mul(ArithUtil.div(
                    everyTradeBean.getMoney(), rateBean.getIncomeTotal(), DEF_DIV_SCALE),
                    everyTradeBean.getOccupyDays()), avgIncomeDay);
        }
        rateBean.setAvgIncomeDay(avgIncomeDay);

        double avgOutcomeDay = 0;
        for (EveryTradeBean everyTradeBean : expendlist) {
            // Σ(每笔支出/支出总数*距第一笔支出天数)

            avgOutcomeDay = ArithUtil.add(ArithUtil.mul(ArithUtil.div(
                    everyTradeBean.getMoney(), rateBean.getExpendTotal(), DEF_DIV_SCALE),
                    everyTradeBean.getOccupyDays()), avgOutcomeDay);
        }
        rateBean.setAvgOutcomeDay(avgOutcomeDay);

        double effectiveDay = Math.abs(ArithUtil.sub(avgIncomeDay, avgOutcomeDay));
        effectiveDay = effectiveDay <= 1 ? 1 : effectiveDay;
        rateBean.setEffectiveDay(effectiveDay);
    }

    /**
     * 计算即时收益率
     *
     * @param rateBean
     * @throws Exception
     */
    private static void initRate(RateBean rateBean) {

        // 支出成本
        double expendTotal = rateBean.getExpendTotal();
        // 总收入
        double totalIncome = rateBean.getIncomeTotal();
        // 利润
        double profit = totalIncome - expendTotal;
        // 收益率
        double rate = Calculate.calculateRate(profit, expendTotal);
        rateBean.setRate(rate);
    }

    /**
     * 计算周期收益率
     */
    private static void initCycleRate(RateBean rateBean) {

        double rate = 0;
        rate = ArithUtil.sub(rateBean.getIncomeTotal(), rateBean.getExpendTotal());
        rate = ArithUtil.div(rate, rateBean.getExpendTotal(), DEF_DIV_SCALE);

        if (rateBean.getEffectiveDay() > 30) {
            rate = ArithUtil.mul(30, rate);
            rate = ArithUtil.div(rate, rateBean.getEffectiveDay(), DEF_DIV_SCALE);
        }
        rate = ArithUtil.mul(100, rate);
        rateBean.setCycleRate(rate);
    }


    /**
     * 计算周期收益率
     */
    private static void initRealCycleRate(RateBean rateBean) {

        double rate = 0;

        double realIncomeTotal = 0, realCostTotal = 0;

        // 实际Cn净收入=∑[每笔收入*(1+β)^-Tn]
        for (EveryTradeBean everyTradeBean : rateBean.getIncomeList()) {

            if (everyTradeBean.getAmountType() == EveryTradeBean.AMOUNT_TYPE_EXPECT) continue;
            double result = ArithUtil.add(1, rateBean.getDayFundLostRate());//1+β
            result = Math.pow(result, -everyTradeBean.getOccupyDays());//(1+β)^-Tn
            result = ArithUtil.mul(result, everyTradeBean.getMoney());//每笔收入*(1+β)^-Tn
            realIncomeTotal = ArithUtil.add(result, realIncomeTotal);
        }

        //实际净支出
        for (EveryTradeBean everyTradeBean : rateBean.getExpendList()) {
            if (everyTradeBean.getAmountType() == EveryTradeBean.AMOUNT_TYPE_EXPECT) continue;

            double result = ArithUtil.add(1, rateBean.getDayFundLostRate());//1+β
            result = Math.pow(result, -everyTradeBean.getOccupyDays());//(1+β)^-Tm
            result = ArithUtil.mul(result, everyTradeBean.getMoney());//每笔支出*(1+β)^-Tm

            realCostTotal = ArithUtil.add(result, realCostTotal);
        }

        double realProfit = ArithUtil.sub(realIncomeTotal, realCostTotal);
        rate = ArithUtil.div(realProfit, rateBean.getExpendTotal(), DEF_DIV_SCALE);

        if (rateBean.getEffectiveDay() > 30) {
            rate = ArithUtil.mul(30, rate);
            rate = ArithUtil.div(rate, rateBean.getEffectiveDay(), DEF_DIV_SCALE);
        }
        rate = ArithUtil.mul(100, rate);
        rateBean.setCycleRate(rate);
    }


    /**
     * 计算市场参考收益率
     */
    private static void initMarketRate(RateBean rateBean) {

        double rate = ArithUtil.mul(rateBean.getDayFundLostRate(), 30);
        rate = ArithUtil.mul(rate, 100);
        rateBean.setMarketRate(rate);
    }

    /**
     * 即时收益率
     *
     * @param profit    利润
     * @param totalCost 成本
     * @return 返回%
     * @throws Exception
     */
    private static double calculateRate(double profit, double totalCost) {
        // (利润/成本)*100%
        return ArithUtil.mul(ArithUtil.div(profit, totalCost, DEF_DIV_SCALE),
                100);
    }

    /**
     * 资金占用天数
     *
     * @param currTradeDay  当前交易发生时间
     * @param lastIncomeDay 最后一笔收入事件
     * @return
     * @throws Exception
     */
    private static double fundOccupyDays(Date currTradeDay, Date lastIncomeDay) {
        if (currTradeDay == null || lastIncomeDay == null)
            return 0;
        // 总天数-该笔支出的时间
        return daysBetween(currTradeDay, lastIncomeDay);
    }

    /**
     * 计算日期差
     *
     * @param d1
     * @param d2
     * @return
     */
    private static int daysBetween(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    public static void outRateBean(RateBean rateBean) throws Exception {

        //资金断裂日损率
        double fundLostRate = rateBean.getDayFundLostRate();
        out("资金日损率:" + fundLostRate);
        // 有效天数
        double effectiveDay = rateBean.getEffectiveDay();
        out("有效天数T:" + effectiveDay);
        // 总支出
        double expendTotal = rateBean.getExpendTotal();
        out("净支出:" + expendTotal);
        // 总收入
        double totalIncome = rateBean.getIncomeTotal();
        out("净收入:" + totalIncome);
        // 平均收入天数
        double avgIncomeDay = rateBean.getAvgIncomeDay();
        out("平均收入天数:" + avgIncomeDay);
        // 平均支出天数
        double avgOutcomeDay = rateBean.getAvgOutcomeDay();
        out("平均支出天数:" + avgOutcomeDay);
        // 即时收益率
        double rate = rateBean.getRate();
        out("收益率:" + rate);
        // 周期收益率
        double cycleRate = rateBean.getCycleRate();
        out("周期收益率:" + cycleRate);
    }

    private static void out(String out) {
        System.out.println(out);
    }

    public static void main(String[] args) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        EveryTradeBean bean1 = new EveryTradeBean();
        bean1.setTime(sdf.parse("2017-01-01"));
        bean1.setMoney(100);

        EveryTradeBean bean2 = new EveryTradeBean();
        bean2.setTime(sdf.parse("2017-02-01"));
        bean2.setMoney(200);

        RateBean rate = new RateBean();

        rate.setDayFundLostRate(0.0028);
        rate.getExpendList().add(bean1);
        rate.getIncomeList().add(bean2);

        Calculate.initRateBean(rate);

        outRateBean(rate);

    }
}