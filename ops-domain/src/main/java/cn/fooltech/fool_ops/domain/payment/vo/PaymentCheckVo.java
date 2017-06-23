package cn.fooltech.fool_ops.domain.payment.vo;

import cn.fooltech.fool_ops.domain.cost.entity.CostBill;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>勾对表单VO</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月28日
 */
public class PaymentCheckVo implements Serializable {

    private static final long serialVersionUID = 6425890517450940098L;

    private String fid;
    private String paymentBillId;//收付款ID
    private String paymentBillCode;//收付款Code
    private String billId;//单据ID
    private String checkDate;//对单日期
    private BigDecimal amount = BigDecimal.ZERO;//本次勾对金额
    private BigDecimal freeAmount = BigDecimal.ZERO;//免单金额
    private BigDecimal billTotalAmount = BigDecimal.ZERO;//单据开单金额
    private BigDecimal billFreeAmount = BigDecimal.ZERO;//单据免单金额
    private BigDecimal billTotalPayAmount = BigDecimal.ZERO;//单据累计勾对金额
    private String expenseAmount; //费用金额
    private String describe;//描述
    private String updateTime;//修改时间戳

    private String billCode;//单据编号
    private String billDate;//单据日期
    private String csvId;//销售商/供应商
    private String csvName;
    private Integer csvType;

    private Integer billType;//收付款单单据类型（也用于搜索过滤） 收款单：51；付款单：52

    /**
     * 费用标识
     * 0--不处理
     * 1--增加往来单位应收/应付款项
     * 2--减少往来单位应收/应付款项
     */
    private Integer expenseType = CostBill.EXPENSE_TYPE_COMMON;

    /**
     * 勾兑时候用的标识
     */
    private String detal = "1";

    public String getPaymentBillId() {
        return paymentBillId;
    }

    public void setPaymentBillId(String paymentBillId) {
        this.paymentBillId = paymentBillId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getBillTotalAmount() {
        return billTotalAmount;
    }

    public void setBillTotalAmount(BigDecimal billTotalAmount) {
        this.billTotalAmount = billTotalAmount;
    }

    public BigDecimal getBillFreeAmount() {
        return billFreeAmount;
    }

    public void setBillFreeAmount(BigDecimal billFreeAmount) {
        this.billFreeAmount = billFreeAmount;
    }

    public BigDecimal getBillTotalPayAmount() {
        return billTotalPayAmount;
    }

    public void setBillTotalPayAmount(BigDecimal billTotalPayAmount) {
        this.billTotalPayAmount = billTotalPayAmount;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getCsvId() {
        return csvId;
    }

    public void setCsvId(String csvId) {
        this.csvId = csvId;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public Integer getCsvType() {
        return csvType;
    }

    public void setCsvType(Integer csvType) {
        this.csvType = csvType;
    }

    public String getPaymentBillCode() {
        return paymentBillCode;
    }

    public void setPaymentBillCode(String paymentBillCode) {
        this.paymentBillCode = paymentBillCode;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(Integer expenseType) {
        this.expenseType = expenseType;
    }

    public String getDetal() {
        return detal;
    }

    public void setDetal(String detal) {
        this.detal = detal;
    }
}
