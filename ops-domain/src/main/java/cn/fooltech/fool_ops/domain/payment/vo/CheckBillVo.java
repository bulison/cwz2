package cn.fooltech.fool_ops.domain.payment.vo;

import cn.fooltech.fool_ops.domain.cost.entity.CostBill;

/**
 * 勾对单据VO
 *
 * @author xjh
 */
public class CheckBillVo {

    /**
     * 单据ID
     */
    private String billId;

    /**
     * 单据编号
     */
    private String billCode;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 单据日期
     */
    private String billDate;

    /**
     * 客户或供应商ID
     */
    private String csvId;

    /**
     * 客户或供应商名称
     */
    private String csvName;

    /**
     * 客户或供应商类型
     */
    private Integer csvType;

    /**
     * 应付金额
     */
    private String billAmount;

    /**
     * 累计已付金额
     */
    private String payedAmount;

    /**
     * 未付金额
     */
    private String unpayAmount;

    /**
     * 累计免单金额
     */
    private String totalFreeAmount = "0";

    /**
     * 勾兑时候用的标识
     */
    private String detal = "1";

    /**
     * 费用金额
     */
    private String expenseAmount = "0";

    /**
     * 费用标识,仅当类型为费用单时有用
     * 1--增加往来单位应收/应付款项
     * 2--减少往来单位应收/应付款项
     */
    private Integer expenseType = CostBill.EXPENSE_TYPE_COMMON;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
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

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(String payedAmount) {
        this.payedAmount = payedAmount;
    }

    public String getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(String unpayAmount) {
        this.unpayAmount = unpayAmount;
    }

    public String getTotalFreeAmount() {
        return totalFreeAmount;
    }

    public void setTotalFreeAmount(String totalFreeAmount) {
        this.totalFreeAmount = totalFreeAmount;
    }

    public String getDetal() {
        return detal;
    }

    public void setDetal(String detal) {
        this.detal = detal;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public Integer getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(Integer expenseType) {
        this.expenseType = expenseType;
    }

    public Integer getCsvType() {
        return csvType;
    }

    public void setCsvType(Integer csvType) {
        this.csvType = csvType;
    }
}
