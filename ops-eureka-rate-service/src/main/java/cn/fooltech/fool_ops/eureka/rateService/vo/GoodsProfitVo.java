package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2017/3/29.
 */
public class GoodsProfitVo {
    //货品ID
    private String goodsId;
    //货品属性ID
    private String SpecId;
    //货品编号
    private String goodsCode;
    //货品名称
    private String goodsName;
    //货品属性
    private String SpecName;
    //记账单位ID
    private String accountUnitId;
    //记账单位
    private String accountUnitName;
    //销售数量
    private BigDecimal accountQuentity;
    //销售金额
    private BigDecimal accountAmount;
    //货品成本
    private BigDecimal costAmount;
    //利润
    private BigDecimal profit;
    //利润率
    private BigDecimal profitRate;

//    private List<GoodsProfitDetailVo> details;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSpecId() {
        return SpecId;
    }

    public void setSpecId(String specId) {
        SpecId = specId;
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

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String specName) {
        SpecName = specName;
    }

    public String getAccountUnitId() {
        return accountUnitId;
    }

    public void setAccountUnitId(String accountUnitId) {
        this.accountUnitId = accountUnitId;
    }

    public String getAccountUnitName() {
        return accountUnitName;
    }

    public void setAccountUnitName(String accountUnitName) {
        this.accountUnitName = accountUnitName;
    }

    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitRate() {return profitRate;}

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

/*    public List<GoodsProfitDetailVo> getDetails() {return details;}

    public void setDetails(List<GoodsProfitDetailVo> details) {this.details = details;}*/
}
