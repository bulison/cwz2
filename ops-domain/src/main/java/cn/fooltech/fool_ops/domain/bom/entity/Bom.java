package cn.fooltech.fool_ops.domain.bom.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 物料表
 *
 * @author xjh
 */
@Entity
@Table(name = "tsb_bom")
public class Bom extends OpsOrgEntity {

    public static final short ENABLE = 1;//有效
    public static final short DISABLE = 0;//无效
    public static final short DEFAULT = 1;//默认配方
    public static final short NOT_DEFAULT = 0;//非默认配方
    private static final long serialVersionUID = -2785300792920305694L;
    /**
     * 货品
     */
    private Goods goods;

    /**
     * 规格
     */
    private GoodsSpec spec;

    /**
     * 记账单位
     */
    private Unit accountUnit;

    /**
     * 记账数量
     */
    private BigDecimal accountQuentity;

    /**
     * 是否有效0--无效 1--有效
     */
    private Short enable = DISABLE;

    /**
     * 默认配方0--否 1--是
     */
    private Short fdefault = NOT_DEFAULT;

    /**
     * 描述
     */
    private String describe;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 权限过滤的部门
     */
    private Organization dept;

    /**
     * 明细
     */
    private Set<BomDetail> details = Sets.newHashSet();

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
    @JoinColumn(name = "FACCOUNT_UINT_ID")
    public Unit getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(Unit accountUnit) {
        this.accountUnit = accountUnit;
    }

    @Column(name = "FQUENTITY")
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    @Column(name = "FENABLE")
    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @Column(name = "FDEFAULT")
    public Short getFdefault() {
        return fdefault;
    }

    public void setFdefault(Short fdefault) {
        this.fdefault = fdefault;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "bom")
    public Set<BomDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<BomDetail> details) {
        this.details = details;
    }
}
