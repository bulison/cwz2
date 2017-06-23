package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售出库单关联VO
 */
public class SaleOutRelationVo {

    private String billId;//关联单ID
    private Date billDate;//关联单据日期/勾对日期
    private Integer billType;//单据类型
    private String billCode;//单据编号
    private String goodsNameSpec;//货品+属性
    private BigDecimal quentity;//数量
    private BigDecimal price;//价格
    private BigDecimal amount;//金额
    private BigDecimal costAmount;//成本金额

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getGoodsNameSpec() {
        return goodsNameSpec;
    }

    public void setGoodsNameSpec(String goodsNameSpec) {
        this.goodsNameSpec = goodsNameSpec;
    }

    public BigDecimal getQuentity() {
        return quentity;
    }

    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }
}
