package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售出货单分析VO
 */
public class SaleOutVo {

    private String saleId;//销售出库单ID
    private String saleCode;//销售出库单号
    private String salesManID;//业务员ID
    private String salesMan;//业务员
    private String customerID;//销售商ID
    private String customerCode;//销售商编码
    private String customerName;//销售商名称
    private Date saleDate;//单据日期
    private Date planFinishDate;//计划完成日期
	private BigDecimal saleAmount;//销售金额
    private BigDecimal hasIncome;//已收金额
    private BigDecimal notIncome;//欠收金额
    private BigDecimal backAmount;//退货金额
    private BigDecimal saleExp;//销售费用
    private BigDecimal goodCost;//货品成本
    private BigDecimal backCost;//退货成本
    private BigDecimal profit;//利润
    private BigDecimal estimatedYieldRate;//预计收益率
    private BigDecimal referenceYieldRate;//参考收益率
    private BigDecimal effectiveYieldRate;//实际收益率
    private BigDecimal currentYieldRate;//当前预计收益率
    private Date lastIncomeDate;//末次收款日期
    private Date lastBackDate;//末次退货日期

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

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(String salesManID) {
        this.salesManID = salesManID;
    }

    public String getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(String salesMan) {
        this.salesMan = salesMan;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Date getPlanFinishDate() {
        return planFinishDate;
    }

    public void setPlanFinishDate(Date planFinishDate) {
        this.planFinishDate = planFinishDate;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getHasIncome() {
        return hasIncome;
    }

    public void setHasIncome(BigDecimal hasIncome) {
        this.hasIncome = hasIncome;
    }

    public BigDecimal getNotIncome() {
        return notIncome;
    }

    public void setNotIncome(BigDecimal notIncome) {
        this.notIncome = notIncome;
    }

    public BigDecimal getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(BigDecimal backAmount) {
        this.backAmount = backAmount;
    }

    public BigDecimal getSaleExp() {
        return saleExp;
    }

    public void setSaleExp(BigDecimal saleExp) {
        this.saleExp = saleExp;
    }

    public BigDecimal getGoodCost() {
        return goodCost;
    }

    public void setGoodCost(BigDecimal goodCost) {
        this.goodCost = goodCost;
    }

    public BigDecimal getBackCost() {
        return backCost;
    }

    public void setBackCost(BigDecimal backCost) {
        this.backCost = backCost;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getEstimatedYieldRate() {
        return estimatedYieldRate;
    }

    public void setEstimatedYieldRate(BigDecimal estimatedYieldRate) {
        this.estimatedYieldRate = estimatedYieldRate;
    }

    public BigDecimal getReferenceYieldRate() {
        return referenceYieldRate;
    }

    public void setReferenceYieldRate(BigDecimal referenceYieldRate) {
        this.referenceYieldRate = referenceYieldRate;
    }

    public BigDecimal getEffectiveYieldRate() {
        return effectiveYieldRate;
    }

    public void setEffectiveYieldRate(BigDecimal effectiveYieldRate) {
        this.effectiveYieldRate = effectiveYieldRate;
    }

    public BigDecimal getCurrentYieldRate() {
        return currentYieldRate;
    }

    public void setCurrentYieldRate(BigDecimal currentYieldRate) {
        this.currentYieldRate = currentYieldRate;
    }

    public Date getLastIncomeDate() {
        return lastIncomeDate;
    }

    public void setLastIncomeDate(Date lastIncomeDate) {
        this.lastIncomeDate = lastIncomeDate;
    }

    public Date getLastBackDate() {
        return lastBackDate;
    }

    public void setLastBackDate(Date lastBackDate) {
        this.lastBackDate = lastBackDate;
    }
}
