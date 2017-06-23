package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SaleOrderDetailVo implements Serializable {

    private static final long serialVersionUID = -4617531831238260688L;

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
     * 订单日期
     */
    private String saleDate;

    /**
     * 计划完成日期
     */
    private String finishDate;

    /**
     * 货品名称ID
     */
    private String goodsId;

    /**
     * 货品编码
     */
    private String goodsCode;

    /**
     * 货品名称
     */
    private String goodsName;

    /**
     * 货品属性ID
     */
    private String goodsSpecId;

    /**
     * 货品属性
     */
    private String goodsSpecName;

    /**
     * 单位（货品基本单位）
     */
    private String goodsUnit;

    /**
     * 订货数量
     */
    private BigDecimal bookTotal;

    /**
     * 订货价格
     */
    private BigDecimal bookPrice;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 已发货数量
     */
    private BigDecimal deliveredTotal;

    /**
     * 已发货金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 欠发货数量
     */
    private BigDecimal undeliveredTotal;

    /**
     * 欠发货金额
     */
    private BigDecimal unDeliveredAmount;

    /**
     * 已收金额
     */
    private BigDecimal hasIncome;

    /**
     * 欠收金额
     */
    private BigDecimal notIncome;

    /**
     * 退货数量
     */
    private BigDecimal backTotal;

    /**
     * 退货金额
     */
    private BigDecimal backAmount;

    /**
     * 货品成本
     */
    private BigDecimal goodsFee;

    /**
     * 销售费用
     */
    private BigDecimal saleExp;

    /**
     * 该销售单的总销售费用
     */
    private BigDecimal totalSaleExp;

    /**
     * 末次发货日期
     */
    private String lastDate;

    /**
     * 末次收款日期
     */
    private String lastIncomeDate;

    /**
     * 末次退货日期
     */
    private String lastBackDate;

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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getGoodsSpecName() {
        return goodsSpecName;
    }

    public void setGoodsSpecName(String goodsSpecName) {
        this.goodsSpecName = goodsSpecName;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public BigDecimal getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(BigDecimal bookTotal) {
        this.bookTotal = bookTotal;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDeliveredTotal() {
        return deliveredTotal;
    }

    public void setDeliveredTotal(BigDecimal deliveredTotal) {
        this.deliveredTotal = deliveredTotal;
    }
   
    public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public BigDecimal getUndeliveredTotal() {
        return undeliveredTotal;
    }

    public void setUndeliveredTotal(BigDecimal undeliveredTotal) {
        this.undeliveredTotal = undeliveredTotal;
    }

    public BigDecimal getUnDeliveredAmount() {
        return unDeliveredAmount;
    }

    public void setUnDeliveredAmount(BigDecimal unDeliveredAmount) {
        this.unDeliveredAmount = unDeliveredAmount;
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

    public BigDecimal getBackTotal() {
        return backTotal;
    }

    public void setBackTotal(BigDecimal backTotal) {
        this.backTotal = backTotal;
    }

    public BigDecimal getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(BigDecimal backAmount) {
        this.backAmount = backAmount;
    }

    public BigDecimal getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(BigDecimal goodsFee) {
        this.goodsFee = goodsFee;
    }

    public BigDecimal getSaleExp() {
        return saleExp;
    }

    public void setSaleExp(BigDecimal saleExp) {
        this.saleExp = saleExp;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastIncomeDate() {
        return lastIncomeDate;
    }

    public void setLastIncomeDate(String lastIncomeDate) {
        this.lastIncomeDate = lastIncomeDate;
    }

    public String getLastBackDate() {
        return lastBackDate;
    }

    public void setLastBackDate(String lastBackDate) {
        this.lastBackDate = lastBackDate;
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

    public BigDecimal getTotalSaleExp() {
        return totalSaleExp;
    }

    public void setTotalSaleExp(BigDecimal totalSaleExp) {
        this.totalSaleExp = totalSaleExp;
    }
}
