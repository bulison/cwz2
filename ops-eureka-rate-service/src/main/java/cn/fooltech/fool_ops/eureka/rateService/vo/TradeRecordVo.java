package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 */
public class TradeRecordVo {

    private Date billDate;//日期
    private Integer paymentType;//支付类型（1：收入，-1：支出）
    private BigDecimal amount;//金额
    private Integer amountType;//金额类型（0-预计，1-实际）

    public static final int AMOUNT_TYPE_REAL = 1;
    public static final int AMOUNT_TYPE_EXPECT = 0;
    public static final int PAYMENT_TYPE_INCOME = 1;
    public static final int PAYMENT_TYPE_COST = -1;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }
}
