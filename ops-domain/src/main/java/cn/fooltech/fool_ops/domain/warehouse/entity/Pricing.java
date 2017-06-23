package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>单据核价</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年4月5日
 */
@Entity
@Table(name = "TSB_BILL_PRICING")
public class Pricing extends OpsOrgEntity {

    private static final long serialVersionUID = 2720509840780025186L;

    /**
     * 仓库单据
     */
    private WarehouseBill bill;

    /**
     * 仓库单据明细
     */
    private WarehouseBillDetail billDetail;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 货品属性
     */
    private GoodsSpec goodSpec;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 修改前单据单价
     */
    private BigDecimal beforePrice;

    /**
     * 修改后单据单价
     */
    private BigDecimal afterPrice;

    /**
     * 修改前记账单价
     */
    private BigDecimal beforeAccountPrice;

    /**
     * 修改后记账单价
     */
    private BigDecimal afterAccountPrice;

    /**
     * 修改前金额
     */
    private BigDecimal beforeAmount;

    /**
     * 修改后金额
     */
    private BigDecimal afterAmount;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取仓库单据
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_BILL_ID", nullable = false)
    public WarehouseBill getBill() {
        return bill;
    }

    /**
     * 设置仓库单据
     *
     * @param bill
     */
    public void setBill(WarehouseBill bill) {
        this.bill = bill;
    }

    /**
     * 获取仓库单据明细
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_DETAIL_ID")
    public WarehouseBillDetail getBillDetail() {
        return billDetail;
    }

    /**
     * 设置仓库单据明细明细
     *
     * @param billDetail
     */
    public void setBillDetail(WarehouseBillDetail billDetail) {
        this.billDetail = billDetail;
    }

    /**
     * 获取货品
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置货品
     *
     * @param goods
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 获取货品属性
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_SPEC_ID")
    public GoodsSpec getGoodSpec() {
        return goodSpec;
    }

    /**
     * 设置货品属性
     *
     * @param goodSpec
     */
    public void setGoodSpec(GoodsSpec goodSpec) {
        this.goodSpec = goodSpec;
    }

    /**
     * 获取单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUINT_ID", nullable = false)
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取修改前单据单价
     *
     * @return
     */
    @Column(name = "FBEFORE_PRICE", nullable = false)
    public BigDecimal getBeforePrice() {
        return beforePrice;
    }

    /**
     * 设置修改前单据单价
     *
     * @param beforePrice
     */
    public void setBeforePrice(BigDecimal beforePrice) {
        this.beforePrice = beforePrice;
    }

    /**
     * 获取修改后单据单价
     *
     * @return
     */
    @Column(name = "FAFTER_PRICE", nullable = false)
    public BigDecimal getAfterPrice() {
        return afterPrice;
    }

    /**
     * 设置修改后单据单价
     *
     * @param afterPrice
     */
    public void setAfterPrice(BigDecimal afterPrice) {
        this.afterPrice = afterPrice;
    }

    /**
     * 获取修改前记账单价
     *
     * @return
     */
    @Column(name = "FBEFORE_ACCOUNT_PRICE", nullable = false)
    public BigDecimal getBeforeAccountPrice() {
        return beforeAccountPrice;
    }

    /**
     * 设置修改前记账单价
     *
     * @param beforeAccountPrice
     */
    public void setBeforeAccountPrice(BigDecimal beforeAccountPrice) {
        this.beforeAccountPrice = beforeAccountPrice;
    }

    /**
     * 获取修改后记账单价
     *
     * @return
     */
    @Column(name = "FAFTER_ACCOUNT_PRICE", nullable = false)
    public BigDecimal getAfterAccountPrice() {
        return afterAccountPrice;
    }

    /**
     * 设置修改后记账单价
     *
     * @param afterAccountPrice
     */
    public void setAfterAccountPrice(BigDecimal afterAccountPrice) {
        this.afterAccountPrice = afterAccountPrice;
    }

    /**
     * 获取修改前金额
     *
     * @return
     */
    @Column(name = "FBEFORE_AMOUNT", nullable = false)
    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    /**
     * 设置修改前金额
     *
     * @param beforeAmount
     */
    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    /**
     * 获取修改后金额
     *
     * @return
     */
    @Column(name = "FAFTER_AMOUNT", nullable = false)
    public BigDecimal getAfterAmount() {
        return afterAmount;
    }

    /**
     * 设置修改后金额
     *
     * @param afterAmount
     */
    public void setAfterAmount(BigDecimal afterAmount) {
        this.afterAmount = afterAmount;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
