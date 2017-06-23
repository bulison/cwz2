package cn.fooltech.fool_ops.domain.cashier.vo;

import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>单据传输对象</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月6日
 */
public class BankBillSimpleVo implements Serializable {

    private static final long serialVersionUID = 3991903249844627492L;

    /**
     * 日期
     */
    private String date;

    /**
     * 金额
     */
    private String amount;

    public BankBillSimpleVo() {

    }

    public BankBillSimpleVo(Date date, BigDecimal amount) {
        this.date = date == null ? "" : DateUtils.getStringByFormat(date, "yyyy-MM-dd");
        this.amount = NumberUtil.bigDecimalToStr(amount);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
