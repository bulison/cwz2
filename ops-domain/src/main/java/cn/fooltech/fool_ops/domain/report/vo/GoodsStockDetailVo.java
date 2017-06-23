package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>货品库存详情表单传输类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月18日
 */
public class GoodsStockDetailVo implements Serializable {
    private static final long serialVersionUID = -9034819083106936680L;
    private String date;//日期
    private String code;//单号
    private String billType;//单据类型
    private String quentity;//数量
    private String unitName;//单位
    private String amount;//金额
    private String costAmount;//成本金额
    private String accountQuentity;//结余数量
    private String accountCostAmount;//结余数量
    private Integer type;//单据类型
    private Short moveOut;//是否调出仓

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public String getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(String accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public String getAccountCostAmount() {
        return accountCostAmount;
    }

    public void setAccountCostAmount(String accountCostAmount) {
        this.accountCostAmount = accountCostAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Short getMoveOut() {
        return moveOut;
    }

    public void setMoveOut(Short moveOut) {
        this.moveOut = moveOut;
    }


}
