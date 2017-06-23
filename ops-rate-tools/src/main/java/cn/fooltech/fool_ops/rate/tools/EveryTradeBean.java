package cn.fooltech.fool_ops.rate.tools;

import java.util.Date;

/**
 * 每一笔交易数据
 */
public class EveryTradeBean implements Comparable<EveryTradeBean> {
    public static final int AMOUNT_TYPE_REAL = 1;
    public static final int AMOUNT_TYPE_EXPECT = 0;
    private double money;//金额
    private Date time;//交易时间
    private Integer amountType = AMOUNT_TYPE_EXPECT;//金额类型（0-预计，1-实际）
    private double occupyDays;//资金占用天数

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getOccupyDays() {
        return this.occupyDays;
    }

    public void setOccupyDays(double occupyDays) {
        this.occupyDays = occupyDays;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public int compareTo(EveryTradeBean o) {
        try {
            return getTime().compareTo(o.getTime());
        } catch (Exception ex) {
        }
        return 0;
    }
}