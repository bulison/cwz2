package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>货品库存表单传输类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月13日
 */
public class GoodsStockVo implements Serializable {

    private static final long serialVersionUID = -2927599722877948227L;

    private String periodId;//会计期间ID

    private String warehouseId;
    private String warehouseName;//仓库

    private String goodsId;
    private String goodsCode;//货品编号
    private String goodsName;//货品名称
    private String goodsSpec;//货品型号

    private String specId;
    private String spec;//货品属性
    private String accountUnit;//计量单位
    private String lastAccountQuentity;//上期结余数
    private String lastAccountAmount;//上期结余金额
    private String inQuentity;//本期入库数
    private String inAmount;//本期入库金额
    private String outQuentity;//本期出库数
    private String outAmount;//本期出库金额
    private String prefitQuentity;//本期盘点盈亏数
    private String prefitAmount;//本期盘点盈亏金额
    private String accountQuentity;//本期结存数
    private String accountAmount;//本期结存金额
    private String moveIn;//调入数
    private String moveOut;//调出数

    private Integer calTotal;//计算总仓/分仓 1：计算总仓 0：计算分仓

    private Integer intoutTag;//出入仓标识
    private Integer billType;//单据类型

    private String unitPrice;//货品平均单价

    //本期结存单价
    private BigDecimal accountPrice;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(String accountUnit) {
        this.accountUnit = accountUnit;
    }

    public String getLastAccountQuentity() {
        return lastAccountQuentity;
    }

    public void setLastAccountQuentity(String lastAccountQuentity) {
        this.lastAccountQuentity = lastAccountQuentity;
    }

    public String getLastAccountAmount() {
        return lastAccountAmount;
    }

    public void setLastAccountAmount(String lastAccountAmount) {
        this.lastAccountAmount = lastAccountAmount;
    }

    public String getInQuentity() {
        return inQuentity;
    }

    public void setInQuentity(String inQuentity) {
        this.inQuentity = inQuentity;
    }

    public String getInAmount() {
        return inAmount;
    }

    public void setInAmount(String inAmount) {
        this.inAmount = inAmount;
    }

    public String getOutQuentity() {
        return outQuentity;
    }

    public void setOutQuentity(String outQuentity) {
        this.outQuentity = outQuentity;
    }

    public String getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(String outAmount) {
        this.outAmount = outAmount;
    }

    public String getPrefitQuentity() {
        return prefitQuentity;
    }

    public void setPrefitQuentity(String prefitQuentity) {
        this.prefitQuentity = prefitQuentity;
    }

    public String getPrefitAmount() {
        return prefitAmount;
    }

    public void setPrefitAmount(String prefitAmount) {
        this.prefitAmount = prefitAmount;
    }

    public String getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(String accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getCalTotal() {
        return calTotal;
    }

    public void setCalTotal(Integer calTotal) {
        this.calTotal = calTotal;
    }

    public String getMoveIn() {
        return moveIn;
    }

    public void setMoveIn(String moveIn) {
        this.moveIn = moveIn;
    }

    public String getMoveOut() {
        return moveOut;
    }

    public void setMoveOut(String moveOut) {
        this.moveOut = moveOut;
    }

    public Integer getIntoutTag() {
        return intoutTag;
    }

    public void setIntoutTag(Integer intoutTag) {
        this.intoutTag = intoutTag;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAccountPrice() {
        return accountPrice;
    }

    public void setAccountPrice(BigDecimal accountPrice) {
        this.accountPrice = accountPrice;
    }
}
