package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 货品定价
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月11日
 */
@Entity
@Table(name = "tsb_goods_price")
public class GoodsPrice extends OpsEntity {

    private static final long serialVersionUID = -8276236269327684821L;

    private Goods goods;//货品
    private GoodsSpec goodsSpec;//货品属性
    private Unit unit;//单位
    private BigDecimal salePrice;//售价
    private BigDecimal lowestPrice;//最低价
    private BigDecimal vipPrice;//会员价
    private BigDecimal lowestStock;//最低库存
    private BigDecimal heightestStock;//最高库存

    private Date createTime;//创建时间
    private User creator;//创建人
    private Organization org;//机构
    private Date updateTime;//修改时间戳

    /**
     * 部门
     */
    private Organization dept;

    //---add @date 2016-02-23---
    private BigDecimal salesLowerLimit1;//销售一级价下限
    private BigDecimal salesLowerLimit2;//销售二级价下限
    private BigDecimal salesUpperLimit1;//销售一级价上限
    private BigDecimal salesUpperLimit2;//销售二级价上限
    private BigDecimal purchaseLowerLimit1;//采购一级价下限
    private BigDecimal purchaseLowerLimit2;//采购二级价下限
    private BigDecimal purchaseUpperLimit1;//采购一级价上限
    private BigDecimal purchaseUpperLimit2;//采购二级价上限
    private BigDecimal purchasingCycle;//增加采购周期（天）
    private BigDecimal capacity;//生产产能（计量数量/天）

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
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
    @JoinColumn(name = "FGOOD_SPEC_ID")
    public GoodsSpec getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(GoodsSpec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNIT_ID")
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Column(name = "FSALE_PRICE")
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    @Column(name = "FLOWEST_PRICE")
    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    @Column(name = "FVIP_PRICE")
    public BigDecimal getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(BigDecimal vipPrice) {
        this.vipPrice = vipPrice;
    }

    @Column(name = "FLOWEST_STOCK")
    public BigDecimal getLowestStock() {
        return lowestStock;
    }

    public void setLowestStock(BigDecimal lowestStock) {
        this.lowestStock = lowestStock;
    }

    @Column(name = "FHEIGHTEST_STOCK")
    public BigDecimal getHeightestStock() {
        return heightestStock;
    }

    public void setHeightestStock(BigDecimal heightestStock) {
        this.heightestStock = heightestStock;
    }


    /**
     * 获取创建时间
     */
    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    @JsonIgnore
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    @JsonIgnore
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    @Column(name = "FSALES_LOWER_LIMIT1")
    public BigDecimal getSalesLowerLimit1() {
        return salesLowerLimit1;
    }

    public void setSalesLowerLimit1(BigDecimal salesLowerLimit1) {
        this.salesLowerLimit1 = salesLowerLimit1;
    }

    @Column(name = "FSALES_LOWER_LIMIT2")
    public BigDecimal getSalesLowerLimit2() {
        return salesLowerLimit2;
    }

    public void setSalesLowerLimit2(BigDecimal salesLowerLimit2) {
        this.salesLowerLimit2 = salesLowerLimit2;
    }

    @Column(name = "FSALES_UPPER_LIMIT1")
    public BigDecimal getSalesUpperLimit1() {
        return salesUpperLimit1;
    }

    public void setSalesUpperLimit1(BigDecimal salesUpperLimit1) {
        this.salesUpperLimit1 = salesUpperLimit1;
    }

    @Column(name = "FSALES_UPPER_LIMIT2")
    public BigDecimal getSalesUpperLimit2() {
        return salesUpperLimit2;
    }

    public void setSalesUpperLimit2(BigDecimal salesUpperLimit2) {
        this.salesUpperLimit2 = salesUpperLimit2;
    }

    @Column(name = "FPURCHASE_LOWER_LIMIT1")
    public BigDecimal getPurchaseLowerLimit1() {
        return purchaseLowerLimit1;
    }

    public void setPurchaseLowerLimit1(BigDecimal purchaseLowerLimit1) {
        this.purchaseLowerLimit1 = purchaseLowerLimit1;
    }

    @Column(name = "FPURCHASE_LOWER_LIMIT2")
    public BigDecimal getPurchaseLowerLimit2() {
        return purchaseLowerLimit2;
    }

    public void setPurchaseLowerLimit2(BigDecimal purchaseLowerLimit2) {
        this.purchaseLowerLimit2 = purchaseLowerLimit2;
    }

    @Column(name = "FPURCHASE_UPPER_LIMIT1")
    public BigDecimal getPurchaseUpperLimit1() {
        return purchaseUpperLimit1;
    }

    public void setPurchaseUpperLimit1(BigDecimal purchaseUpperLimit1) {
        this.purchaseUpperLimit1 = purchaseUpperLimit1;
    }

    @Column(name = "FPURCHASE_UPPER_LIMIT2")
    public BigDecimal getPurchaseUpperLimit2() {
        return purchaseUpperLimit2;
    }

    public void setPurchaseUpperLimit2(BigDecimal purchaseUpperLimit2) {
        this.purchaseUpperLimit2 = purchaseUpperLimit2;
    }

    @Column(name = "FPURCHASING_CYCLE")
    public BigDecimal getPurchasingCycle() {
        return purchasingCycle;
    }

    public void setPurchasingCycle(BigDecimal purchasingCycle) {
        this.purchasingCycle = purchasingCycle;
    }

    @Column(name = "FCAPACITY")
    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }


}
