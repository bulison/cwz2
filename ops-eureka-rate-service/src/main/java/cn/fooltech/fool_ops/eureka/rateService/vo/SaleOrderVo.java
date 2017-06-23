package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.io.Serializable;
import java.util.Date;

public class SaleOrderVo implements Serializable {

    private static final long serialVersionUID = -1349879327349103311L;

    /**
     * 订单ID
     */
    private String billId;

    /**
     * 销售订单单号
     */
    private String saleCode;

    /**
     * 业务员
     */
    private String saleId;

    /**
     * 业务员
     */
    private String sale;

    /**
     * 销售商ID
     */
    private String supplierCode;

    /**
     * 销售商名称
     */
    private String supplierName;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 已发货金额
     */
    private String deliveryAmount;

    /**
     * 订单日期
     */
    private String saleDate;

    /**
     * 计划完成日期
     */
    private String finishDate;

    /**
     * 货品成本
     */
    private String goodsFee;

    /**
     * 末次发货日期
     */
    private String lastDate;

    /**
     * 末次退货日期
     */
    private String lastBackDate;

    /**
     * 退货金额
     */
    private String backAmount;

    /**
     * 销售费用
     */
    private String saleExp;

    /**
     * 末次收款日期
     */
    private String lastIncomeDate;

    /**
     * 已收金额
     */
    private String hasIncome;

    /**
     * 欠收金额
     */
    private String notIncome;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 提成金额
     */
    private String percentageAmount;

    public String getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(String percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(String goodsFee) {
        this.goodsFee = goodsFee;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastBackDate() {
        return lastBackDate;
    }

    public void setLastBackDate(String lastBackDate) {
        this.lastBackDate = lastBackDate;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public String getSaleExp() {
        return saleExp;
    }

    public void setSaleExp(String saleExp) {
        this.saleExp = saleExp;
    }

    public String getLastIncomeDate() {
        return lastIncomeDate;
    }

    public void setLastIncomeDate(String lastIncomeDate) {
        this.lastIncomeDate = lastIncomeDate;
    }

    public String getHasIncome() {
        return hasIncome;
    }

    public void setHasIncome(String hasIncome) {
        this.hasIncome = hasIncome;
    }

    public String getNotIncome() {
        return notIncome;
    }

    public void setNotIncome(String notIncome) {
        this.notIncome = notIncome;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
