package cn.fooltech.fool_ops.domain.initialBank.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>现金银行期初</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月9日
 */
@Entity
@Table(name = "tsb_initial_bank")
public class InitialBank extends OpsEntity {

    private static final long serialVersionUID = -5061026787399863424L;

    private Bank bank;//银行
    private BigDecimal amount;//结余金额
    private String describe;//描述
    private StockPeriod period;//会计期间

    private Date createTime;//创建时间
    private User creator;//创建人
    private Organization org;//机构
    private Date updateTime;//修改时间戳

    /**
     * 部门
     */
    private Organization dept;

    private Integer checkoutStatus;//会计期间是否已结账

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

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


    @JoinColumn(name = "FBANK_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JsonIgnore
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @JoinColumn(name = "STOCK_PERIOD_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JsonIgnore
    public StockPeriod getPeriod() {
        return period;
    }

    public void setPeriod(StockPeriod period) {
        this.period = period;
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

    @Transient
    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置账套
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
