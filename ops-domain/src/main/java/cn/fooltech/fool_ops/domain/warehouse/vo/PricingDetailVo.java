package cn.fooltech.fool_ops.domain.warehouse.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象-核价详细信息</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年4月7日
 */
public class PricingDetailVo implements Serializable {

    private static final long serialVersionUID = 541925046665051321L;

    /**
     * ID
     */
    private String fid;

    /**
     * 仓库单据
     */
    private String billId;

    /**
     * 仓库单据明细
     */
    private String billDetailId;

    /**
     * 货品
     */
    private String goodsId;

    /**
     * 货品属性
     */
    private String goodsSpecId;

    /**
     * 单位
     */
    private String unitId;

    /**
     * 修改前单据单价
     */
    private String beforePrice;

    /**
     * 修改后单据单价
     */
    private String afterPrice;

    /**
     * 修改前金额
     */
    private String beforeAmount;

    /**
     * 修改后金额
     */
    private String afterAmount;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(String billDetailId) {
        this.billDetailId = billDetailId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getBeforePrice() {
        return beforePrice;
    }

    public void setBeforePrice(String beforePrice) {
        this.beforePrice = beforePrice;
    }

    public String getAfterPrice() {
        return afterPrice;
    }

    public void setAfterPrice(String afterPrice) {
        this.afterPrice = afterPrice;
    }

    public String getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(String beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public String getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(String afterAmount) {
        this.afterAmount = afterAmount;
    }

}
