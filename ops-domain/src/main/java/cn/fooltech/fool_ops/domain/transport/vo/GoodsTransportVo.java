package cn.fooltech.fool_ops.domain.transport.vo;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 货品运输计价换算关系
 *
 * @author cwz
 * @date 2016-12-6
 */
public class GoodsTransportVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fid;//
    private String accId;//账套ID
    private String accName;//账套名称
    private BigDecimal conversionRate;//换算关系
    private String createTime;//创建时间
    private String creatorId;//创建人id
    private String creatorName;//创建人名称
    private String describe;//描述
    private String goodSpecId;//货品属性ID（关联属性表）
    private String goodSpecName;//货品属性名称（关联属性表）
    private String goodsId;//货品ID（关联货品表）
    private String goodsName;//货品名称（关联货品表）
    private String orgId;//机构id
    private String orgName;//机构名称
    private String shipmentTypeId;//装运方式ID（关联辅助属性装运方式）
    private String shipmentTypeName;//装运方式名称（关联辅助属性装运方式）
    private String transportUnitId;//运输单位ID（关联辅助属性运输费计价单位）
    private String transportUnitName;//运输单位名称（关联辅助属性运输费计价单位）
    private String unitId;//货品单位（关联单位表）
    private String unitName;//货品单位（关联单位表）
    private String updateTime;//修改时间
    private Short sysSign;//系统标识 0：用户生成，1：系统生成

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(String goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    public String getGoodSpecName() {
        return goodSpecName;
    }

    public void setGoodSpecName(String goodSpecName) {
        this.goodSpecName = goodSpecName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getShipmentTypeId() {
        return shipmentTypeId;
    }

    public void setShipmentTypeId(String shipmentTypeId) {
        this.shipmentTypeId = shipmentTypeId;
    }

    public String getShipmentTypeName() {
        return shipmentTypeName;
    }

    public void setShipmentTypeName(String shipmentTypeName) {
        this.shipmentTypeName = shipmentTypeName;
    }

    public String getTransportUnitId() {
        return transportUnitId;
    }

    public void setTransportUnitId(String transportUnitId) {
        this.transportUnitId = transportUnitId;
    }

    public String getTransportUnitName() {
        return transportUnitName;
    }

    public void setTransportUnitName(String transportUnitName) {
        this.transportUnitName = transportUnitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Short getSysSign() {
        return sysSign;
    }

    public void setSysSign(Short sysSign) {
        this.sysSign = sysSign;
    }
}