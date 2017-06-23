package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 待摊费用
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年4月13日
 */
@Entity
@Table(name = "tbd_prepaid_expenses")
public class PrepaidExpenses extends OpsOrgEntity {

    public static final short STATUS_UN_AUDIT = 0;//未审核
    public static final short STATUS_AUDIT = 1;//已审核

    private static final long serialVersionUID = -7190017549219672119L;

    /**
     * 费用编号
     */
    private String expensesCode;

    /**
     * 费用名称
     */
    private String expensesName;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 待摊金额
     */
    private BigDecimal expensesAmount;

    /**
     * 计提期数
     */
    private Integer discountPeriod;

    /**
     * 计提日期
     */
    private Date shareDate;

    /**
     * 借方科目
     */
    private FiscalAccountingSubject debitSubject;

    /**
     * 贷方科目
     */
    private FiscalAccountingSubject creditSubject;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Short recordStatus;
    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 审核人
     */
    private User auditor;

    /**
     * 审核时间
     */
    private Date auditTime;


    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 明细
     */
    private List<PrepaidExpensesDetail> details = Lists.newArrayList();

    @Column(name = "FEXPENSES_CODE")
    public String getExpensesCode() {
        return expensesCode;
    }

    public void setExpensesCode(String expensesCode) {
        this.expensesCode = expensesCode;
    }

    @Column(name = "FEXPENSES_NAME")
    public String getExpensesName() {
        return expensesName;
    }

    public void setExpensesName(String expensesName) {
        this.expensesName = expensesName;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @Column(name = "FEXPENSES_AMOUNT")
    public BigDecimal getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(BigDecimal expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    @Column(name = "FDISCOUNT_PERIOD")
    public Integer getDiscountPeriod() {
        return discountPeriod;
    }

    public void setDiscountPeriod(Integer discountPeriod) {
        this.discountPeriod = discountPeriod;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FSHARE_DATE")
    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEBIT_SUBJECT_ID")
    public FiscalAccountingSubject getDebitSubject() {
        return debitSubject;
    }

    public void setDebitSubject(FiscalAccountingSubject debitSubject) {
        this.debitSubject = debitSubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREDIT_SUBJECT_ID")
    public FiscalAccountingSubject getCreditSubject() {
        return creditSubject;
    }

    public void setCreditSubject(FiscalAccountingSubject creditSubject) {
        this.creditSubject = creditSubject;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "FRECORD_STATUS")
    public Short getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Short recordStatus) {
        this.recordStatus = recordStatus;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
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

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
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
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @OneToMany(mappedBy = "expenses", cascade = {}, fetch = FetchType.LAZY)
    public List<PrepaidExpensesDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PrepaidExpensesDetail> details) {
        this.details = details;
    }
}
