package cn.fooltech.fool_ops.domain.transport.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 货品运输计价换算关系
 *
 * @author cwz
 * @date 2016-12-6
 */
@Entity
@Table(name = "tbd_goods_transport")
public class GoodsTransport extends OpsEntity {
    private static final long serialVersionUID = 1L;

    private FiscalAccount accId;//账套ID
    private BigDecimal conversionRate;//换算关系
    private Date createTime;//创建时间
    private User creator;//创建人
    private String describe;//描述
    private GoodsSpec goodSpec;//货品属性ID（关联属性表）
    private Goods goods;//货品ID（关联货品表）
    private Organization org;//机构
    private AuxiliaryAttr shipmentType;//装运方式ID（关联辅助属性装运方式）
    private AuxiliaryAttr transportUnit;//运输单位（关联辅助属性运输费计价单位）
    private Unit unit;//货品单位（关联单位表）
    private Date updateTime;//修改时间
    private Short sysSign;//系统标识 0：用户生成，1：系统生成

    public static final short SYS_SIGN_USER = 0;
    public static final short SYS_SIGN_GEN = 1;

    public GoodsTransport() {
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", updatable = false)
    public FiscalAccount getAccId() {
        return accId;
    }

    public void setAccId(FiscalAccount accId) {
        this.accId = accId;
    }

    @Column(name = "FCONVERSION_RATE")
    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOOD_SPEC_ID")
    public GoodsSpec getGoodSpec() {
        return goodSpec;
    }

    public void setGoodSpec(GoodsSpec goodSpec) {
        this.goodSpec = goodSpec;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID", updatable = false)
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    public AuxiliaryAttr getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(AuxiliaryAttr shipmentType) {
        this.shipmentType = shipmentType;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_UNIT_ID")
    public AuxiliaryAttr getTransportUnit() {
        return transportUnit;
    }

    public void setTransportUnit(AuxiliaryAttr transportUnit) {
        this.transportUnit = transportUnit;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNIT_ID")
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FSYS_SIGN")
    public Short getSysSign() {
        return sysSign;
    }

    public void setSysSign(Short sysSign) {
        this.sysSign = sysSign;
    }
}