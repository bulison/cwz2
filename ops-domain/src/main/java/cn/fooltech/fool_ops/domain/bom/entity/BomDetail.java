package cn.fooltech.fool_ops.domain.bom.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 物料明细表
 *
 * @author xjh
 */
@Entity
@Table(name = "tsb_bom_detail")
public class BomDetail extends OpsOrgEntity {

    private static final long serialVersionUID = 7202888556660652555L;

    /**
     * 关联的物料
     */
    private Bom bom;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 规格
     */
    private GoodsSpec spec;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 数量
     */
    private BigDecimal quentity;

    /**
     * 记账单位
     */
    private Unit accountUnit;

    /**
     * 记账数量
     */
    private BigDecimal accountQuentity;

    /**
     * 描述
     */
    private String describe;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 创建时间
     */
    private Date createTime;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBOM_ID", nullable = false)
    public Bom getBom() {
        return bom;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSPEC_ID")
    public GoodsSpec getSpec() {
        return spec;
    }

    public void setSpec(GoodsSpec spec) {
        this.spec = spec;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUINT_ID")
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Column(name = "FQUENTITY")
    public BigDecimal getQuentity() {
        return quentity;
    }

    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACCOUNT_UINT_ID")
    public Unit getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(Unit accountUnit) {
        this.accountUnit = accountUnit;
    }

    @Column(name = "FACCOUNT_QUENTITY")
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
