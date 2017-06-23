package cn.fooltech.fool_ops.eureka.rateService.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售出货单明细
 */
public class SaleOutDetailVo {

    private String saleId;//销售出库单ID
    private String detailId;//销售出库单明细ID
    private String goodId;//货品ID
    private String specId;//货品属性ID
    private String unitId;//货品单位ID
    private String salesManID;//业务员ID
    private String customerID;//销售商ID
    private String saleCode;//销售出库单号
    private String salesMan;//业务员
    private String customerCode;//销售商编码
    private String customerName;//销售商名称

    @JSONField(format = "yyyy-MM-dd")
    private Date saleDate;//单据日期

    @JSONField(format = "yyyy-MM-dd")
    private Date planFinishDate;//计划完成日期
    private String goodCode;//货品编码
    private String goodName;//货品名称
    private String specName;//货品属性名称
    private String goodUnit;//货品单位（基本单位）
    private BigDecimal saleQuantity;//销售数量
    private BigDecimal salePrice;//销售单价
    private BigDecimal saleAmount;//销售金额
    private BigDecimal hasIncome;//已收金额
    private BigDecimal notIncome;//欠收金额
    private BigDecimal backQuantity;//退货数量
    private BigDecimal backAmount;//退货金额
    private BigDecimal goodCost;//货品成本
    private BigDecimal backCost;//退货成本
    private BigDecimal totalSaleExp;//该销售单总销售费用
    private BigDecimal saleExp;//销售费用
    private BigDecimal profit;//利润

    @JSONField(format = "yyyy-MM-dd")
    private Date lastIncomeDate;//末次收款日期

    @JSONField(format = "yyyy-MM-dd")
    private Date lastBackDate;//末次退货日期
    private Integer priceFlag;//单价标志

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

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(String salesManID) {
        this.salesManID = salesManID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(String salesMan) {
        this.salesMan = salesMan;
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

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getGoodUnit() {
        return goodUnit;
    }

    public void setGoodUnit(String goodUnit) {
        this.goodUnit = goodUnit;
    }

    public BigDecimal getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(BigDecimal saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public BigDecimal getBackQuantity() {
        return backQuantity;
    }

    public void setBackQuantity(BigDecimal backQuantity) {
        this.backQuantity = backQuantity;
    }

    public BigDecimal getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(BigDecimal backAmount) {
        this.backAmount = backAmount;
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

    public BigDecimal getTotalSaleExp() {
        return totalSaleExp;
    }

    public void setTotalSaleExp(BigDecimal totalSaleExp) {
        this.totalSaleExp = totalSaleExp;
    }

    public BigDecimal getSaleExp() {
        return saleExp;
    }

    public void setSaleExp(BigDecimal saleExp) {
        this.saleExp = saleExp;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
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

    public Integer getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(Integer priceFlag) {
        this.priceFlag = priceFlag;
    }
}
