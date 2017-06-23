package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * <p>即时分仓库存</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年4月6日
 */
@Entity
@NoArgsConstructor
@ToString
@Table(name = "TSB_STOCK_STORE")
public class StockStore extends OpsOrgEntity {

    private static final long serialVersionUID = 1947907351165087069L;

    /**
     * 仓库
     */
    private AuxiliaryAttr warehouse;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 货品属性
     */
    private GoodsSpec goodsSpec;

    /**
     * 记账单位
     */
    private Unit accountUnit;

    /**
     * 结存数量
     */
    private BigDecimal accountQuentity = BigDecimal.ZERO;

    /**
     * 出库结存数量
     */
    private BigDecimal checkoutQuentity = BigDecimal.ZERO;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    //结存金额
    private BigDecimal accountAmount = BigDecimal.ZERO;

    //结存单价
    private BigDecimal accountPrice = BigDecimal.ZERO;

    /**
     * 仓库
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_ID", nullable = false)
    public AuxiliaryAttr getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(AuxiliaryAttr warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * 货品
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 货品属性
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSPEC_ID")
    public GoodsSpec getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(GoodsSpec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    /**
     * 记账单位
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACCOUNT_UINT_ID", nullable = false)
    public Unit getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(Unit accountUnit) {
        this.accountUnit = accountUnit;
    }

    /**
     * 结存数量
     */
    @Column(name = "FACCOUNT_QUENTITY", nullable = false)
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    /**
     * 出库结存数量
     */
    @Column(name = "FCHECKOUT_QUENTITY", nullable = false)
    public BigDecimal getCheckoutQuentity() {
        return checkoutQuentity;
    }

    public void setCheckoutQuentity(BigDecimal checkoutQuentity) {
        this.checkoutQuentity = checkoutQuentity;
    }

    /**
     * 财务账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    //结存金额
    @Column(name = "FACCOUNT_AMOUNT")
    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    //结存单价
    @Column(name = "FACCOUNT_PRICE")
    public BigDecimal getAccountPrice() {
        return accountPrice;
    }

    public void setAccountPrice(BigDecimal accountPrice) {
        this.accountPrice = accountPrice;
    }
}
