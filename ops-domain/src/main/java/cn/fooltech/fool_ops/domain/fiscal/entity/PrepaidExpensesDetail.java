package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 待摊费用明细
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年4月13日
 */
@Entity
@Table(name = "tbd_prepaid_expenses_detail")
public class PrepaidExpensesDetail extends OpsOrgEntity {

    private static final long serialVersionUID = 985532363646508014L;

    /**
     * 关联的待摊费用
     */
    private PrepaidExpenses expenses;

    /**
     * 日期
     */
    private Date date;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

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

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FEXPENSES_ID")
    public PrepaidExpenses getExpenses() {
        return expenses;
    }

    public void setExpenses(PrepaidExpenses expenses) {
        this.expenses = expenses;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FDATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }
}
