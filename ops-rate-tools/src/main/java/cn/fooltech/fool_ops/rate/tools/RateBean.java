package cn.fooltech.fool_ops.rate.tools;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * 收益率
 */
public class RateBean {

    /**
     * 资金日损率
     */
    private double dayFundLostRate = 0;

    /**
     * 收入列表
     */
    private List<EveryTradeBean> incomeList = Lists.newArrayList();
    /**
     * 支出列表
     */
    private List<EveryTradeBean> expendList = Lists.newArrayList();

    /**
     * 第一笔支出时间
     */
    private Date firstPayDay = null;
    /**
     * 最后一笔收入时间
     */
    //private Date lastIncomeDay = null;

    /**
     * 总收入
     */
    private double incomeTotal = 0;
    /**
     * 总支出
     */
    private double expendTotal = 0;

    /**
     * 有效天数
     */
    private double effectiveDay = 1;

    /**
     * 周期
     */
    private int cycle = 30;

    /**
     * 平均收入天数
     */
    private double avgIncomeDay = 0;

    /**
     * 平均支出天数
     */
    private double avgOutcomeDay = 0;


    /**
     * 即时收益率
     */
    private double rate = 0;

    /**
     * 周期收益率
     */
    private double cycleRate = 0;

    /**
     * 市场参考收益率
     */
    private double marketRate = 0;

    public List<EveryTradeBean> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<EveryTradeBean> incomeList) {
        this.incomeList = incomeList;
    }

    public List<EveryTradeBean> getExpendList() {
        return expendList;
    }

//    public Date getLastIncomeDay() {
//        return lastIncomeDay;
//    }

    public void setExpendList(List<EveryTradeBean> expendList) {
        this.expendList = expendList;
    }

    public Date getFirstPayDay() {
        return firstPayDay;
    }

    public void setFirstPayDay(Date firstPayDay) {
        this.firstPayDay = firstPayDay;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

//    public void setLastIncomeDay(Date lastIncomeDay) {
//        this.lastIncomeDay = lastIncomeDay;
//    }

    public double getExpendTotal() {
        return expendTotal;
    }

    public void setExpendTotal(double expendTotal) {
        this.expendTotal = expendTotal;
    }

    public double getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(double effectiveDay) {
        this.effectiveDay = effectiveDay;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public double getAvgIncomeDay() {
        return avgIncomeDay;
    }

    public void setAvgIncomeDay(double avgIncomeDay) {
        this.avgIncomeDay = avgIncomeDay;
    }

    public double getAvgOutcomeDay() {
        return avgOutcomeDay;
    }

    public void setAvgOutcomeDay(double avgOutcomeDay) {
        this.avgOutcomeDay = avgOutcomeDay;
    }


    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getCycleRate() {
        return cycleRate;
    }

    public void setCycleRate(double cycleRate) {
        this.cycleRate = cycleRate;
    }

    public double getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(double marketRate) {
        this.marketRate = marketRate;
    }

    public double getDayFundLostRate() {
        return dayFundLostRate;
    }

    public void setDayFundLostRate(double dayFundLostRate) {
        this.dayFundLostRate = dayFundLostRate;
    }
}
