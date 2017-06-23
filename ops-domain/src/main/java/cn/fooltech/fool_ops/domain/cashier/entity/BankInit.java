package cn.fooltech.fool_ops.domain.cashier.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 初始银行设置-实体类
 *
 * @author xjh
 */

@Entity
@Table(name = "tbd_bank_init")
public class BankInit extends OpsOrgEntity {

    private static final long serialVersionUID = 1386628540640910107L;

    public static short START = 1;
    public static short UN_START = 0;

    /**
     * 科目
     */
    private FiscalAccountingSubject subject;

    /**
     * 日记账期初余额
     */
    private BigDecimal accountInit = BigDecimal.ZERO;

    /**
     * 对账单期初余额
     */
    private BigDecimal statementInit = BigDecimal.ZERO;

    /**
     * 日记账借方金额
     */
    private BigDecimal accountDebit = BigDecimal.ZERO;

    /**
     * 日记账贷方金额
     */
    private BigDecimal accountCredit = BigDecimal.ZERO;

    /**
     * 日记账调整后余额
     */
    private BigDecimal accountBalance = BigDecimal.ZERO;

    /**
     * 对账单借方金额
     */
    private BigDecimal statementDebit = BigDecimal.ZERO;

    /**
     * 对账单贷方金额
     */
    private BigDecimal statementCredit = BigDecimal.ZERO;

    /**
     * 对账单调整后余额
     */
    private BigDecimal statementBalance = BigDecimal.ZERO;

    /**
     * 启用： 0--未启用 1--启用
     */
    private Short start = UN_START;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 部门
     */
    private Organization dept;

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

    /**
     * 获取科目
     */
    @JoinColumn(name = "FFISCAL_SUBJECT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
    }

    @Column(name = "FACCOUNT_INIT")
    public BigDecimal getAccountInit() {
        return accountInit;
    }

    public void setAccountInit(BigDecimal accountInit) {
        this.accountInit = accountInit;
    }

    @Column(name = "FSTATEMENT_INIT")
    public BigDecimal getStatementInit() {
        return statementInit;
    }

    public void setStatementInit(BigDecimal statementInit) {
        this.statementInit = statementInit;
    }

    @Column(name = "FACCOUNT_DEBIT")
    public BigDecimal getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(BigDecimal accountDebit) {
        this.accountDebit = accountDebit;
    }

    @Column(name = "FACCOUNT_CREDIT")
    public BigDecimal getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(BigDecimal accountCredit) {
        this.accountCredit = accountCredit;
    }

    @Column(name = "FACCOUNT_BALANCE")
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Column(name = "FSTATEMENT_DEBIT")
    public BigDecimal getStatementDebit() {
        return statementDebit;
    }

    public void setStatementDebit(BigDecimal statementDebit) {
        this.statementDebit = statementDebit;
    }

    @Column(name = "FSTATEMENT_CREDIT")
    public BigDecimal getStatementCredit() {
        return statementCredit;
    }

    public void setStatementCredit(BigDecimal statementCredit) {
        this.statementCredit = statementCredit;
    }

    @Column(name = "FSTATEMENT_BALANCE")
    public BigDecimal getStatementBalance() {
        return statementBalance;
    }

    public void setStatementBalance(BigDecimal statementBalance) {
        this.statementBalance = statementBalance;
    }

    @Column(name = "FSTART")
    public Short getStart() {
        return start;
    }

    public void setStart(Short start) {
        this.start = start;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取修改时间戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
