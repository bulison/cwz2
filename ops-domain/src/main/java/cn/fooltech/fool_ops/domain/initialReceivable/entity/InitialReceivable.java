package cn.fooltech.fool_ops.domain.initialReceivable.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>期初应收</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月10日
 */
/*@Entity
@Table(name = "tsb_initial_receivable")*/
@Deprecated
public class InitialReceivable extends OpsEntity {

    private static final long serialVersionUID = -8760101109676371188L;

    private Customer customer;//客户
    private BigDecimal amount;//应收金额
    private String describe;//描述
    private StockPeriod period;//会计期间
    private Member member;//负责人

    private Date createTime;//创建时间
    private User creator;//创建人
    private Organization org;//机构
    private Date updateTime;//修改时间戳

    @JoinColumn(name = "FCUSTOMER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
    public StockPeriod getPeriod() {
        return period;
    }

    public void setPeriod(StockPeriod period) {
        this.period = period;
    }

    @JoinColumn(name = "FMEMBER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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

}
