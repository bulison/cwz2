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
 * <p>出库记录</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月23日
 */
@Entity
@Table(name = "TSB_STORAGE_OUT")
public class OutStorage extends OpsOrgEntity {

    private static final long serialVersionUID = 3948395483697829773L;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 入库记录
     */
    private InStorage inStorage;

    /**
     * 入库记录的仓库单据明细
     */
    private WarehouseBillDetail billDetailIn;

    /**
     * 出库记录的仓库单据明细
     */
    private WarehouseBillDetail billDetailOut;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 货品属性
     */
    private GoodsSpec goodsSpec;

    /**
     * 记账单位(最小单位)
     */
    private Unit accountUint;

    /**
     * 记账单价
     */
    private BigDecimal accountUintPrice;

    /**
     * 记账数量
     */
    private BigDecimal accountQuentity;

    /**
     * 记账金额
     */
    private BigDecimal accountAmount;

    /**
     * 入库记账金额
     */
    private BigDecimal amountIn;

    /**
     * 入库单价
     */
    private BigDecimal inPrice;

    /**
     * 入库日期
     */
    private Date inDate;

    /**
     * 出库日期
     */
    private Date outDate;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE", nullable = false)
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置单据类型
     *
     * @param billType
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取入库记录
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSTORAGE_IN_ID", nullable = false)
    public InStorage getInStorage() {
        return inStorage;
    }

    /**
     * 设置入库记录
     *
     * @param inStorage
     */
    public void setInStorage(InStorage inStorage) {
        this.inStorage = inStorage;
    }

    /**
     * 获取入库记录的仓库单据明细
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBILL_DETAIL_IN_ID", nullable = false)
    public WarehouseBillDetail getBillDetailIn() {
        return billDetailIn;
    }

    /**
     * 设置入库记录的仓库单据明细
     *
     * @param billDetailIn
     */
    public void setBillDetailIn(WarehouseBillDetail billDetailIn) {
        this.billDetailIn = billDetailIn;
    }

    /**
     * 获取出库记录的仓库单据明细
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBILL_DETAIL_ID", nullable = false)
    public WarehouseBillDetail getBillDetailOut() {
        return billDetailOut;
    }

    /**
     * 设置出库记录的仓库单据明细
     *
     * @param billDetailOut
     */
    public void setBillDetailOut(WarehouseBillDetail billDetailOut) {
        this.billDetailOut = billDetailOut;
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
    @JoinColumn(name = "FSPEC_ID")
    public GoodsSpec getGoodsSpec() {
        return goodsSpec;
    }

    /**
     * 设置货品属性
     *
     * @param goodsSpec
     */
    public void setGoodsSpec(GoodsSpec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    /**
     * 获取记账单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACCOUNT_UINT_ID", nullable = false)
    public Unit getAccountUint() {
        return accountUint;
    }

    /**
     * 设置记账单位
     *
     * @param accountUint
     */
    public void setAccountUint(Unit accountUint) {
        this.accountUint = accountUint;
    }

    /**
     * 获取记账单价
     *
     * @return
     */
    @Column(name = "FACCOUNT_UINT_PRICE", nullable = false)
    public BigDecimal getAccountUintPrice() {
        return accountUintPrice;
    }

    /**
     * 设置记账单价
     *
     * @param accountUintPrice
     */
    public void setAccountUintPrice(BigDecimal accountUintPrice) {
        this.accountUintPrice = accountUintPrice;
    }

    /**
     * 获取记账数量
     *
     * @return
     */
    @Column(name = "FACCOUNT_QUENTITY", nullable = false)
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    /**
     * 设置记账数量
     *
     * @param accountQuentity
     */
    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    /**
     * 获取记账金额
     *
     * @return
     */
    @Column(name = "FACCOUNT_AMOUNT", nullable = false)
    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    /**
     * 设置记账金额
     *
     * @param accountAmount
     */
    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    /**
     * 获取入库记账金额
     *
     * @return
     */
    @Column(name = "FAMOUNT_IN", nullable = false)
    public BigDecimal getAmountIn() {
        return amountIn;
    }

    /**
     * 设置入库记账金额
     *
     * @param amountIn
     */
    public void setAmountIn(BigDecimal amountIn) {
        this.amountIn = amountIn;
    }

    /**
     * 获取入库单价
     *
     * @return
     */
    @Column(name = "FPRICE_IN", nullable = false)
    public BigDecimal getInPrice() {
        return inPrice;
    }

    /**
     * 设置入库单价
     *
     * @param inPrice
     */
    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    /**
     * 获取入库日期
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FDATE_IN", nullable = false)
    public Date getInDate() {
        return inDate;
    }

    /**
     * 设置入库日期
     *
     * @param inDate
     */
    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    /**
     * 获取出库日期
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FDATE_OUT", nullable = false)
    public Date getOutDate() {
        return outDate;
    }

    /**
     * 设置出库日期
     *
     * @param outDate
     */
    public void setOutDate(Date outDate) {
        this.outDate = outDate;
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
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
