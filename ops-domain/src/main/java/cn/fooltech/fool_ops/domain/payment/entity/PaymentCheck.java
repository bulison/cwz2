package cn.fooltech.fool_ops.domain.payment.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>收付款单勾对实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月28日
 */
@Entity
@Table(name = "tsb_payment_check")
public class PaymentCheck extends OpsOrgEntity {

    private static final long serialVersionUID = -502045684303804132L;

    /**
     * 关联收付款单
     */
    private PaymentBill paymentBill;

    /**
     * 关联单据
     */
    private String billId;

    /**
     * 对单日期
     */
    private Date checkDate;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 免单金额
     */
    private BigDecimal freeAmount;

    /**
     * 描述
     */
    private String describe;

    /**
     * 会计期间
     */
    private StockPeriod period;

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
     * 部门
     */
    private Organization dept;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 勾对的单据类型
     */
    private Integer billType;

    /**
     * billId是否勾对单自己
     */
    private boolean self = false;

    /**
     * 获取关联的收付款单
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPAYMENT_ID")
    public PaymentBill getPaymentBill() {
        return paymentBill;
    }

    /**
     * 设置收付款记录表
     */
    public void setPaymentBill(PaymentBill paymentBill) {
        this.paymentBill = paymentBill;
    }

    /**
     * 获取关联的单据
     */
    @Column(name = "FBILL_ID")
    public String getBillId() {
        return billId;
    }

    /**
     * 设置关联单据表
     */
    public void setBillId(String billId) {
        this.billId = billId;
    }

    /**
     * 获取对单日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCHECK_DATE")
    public Date getCheckDate() {
        return checkDate;
    }

    /**
     * 设置对单日期
     */
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    /**
     * 获取对单金额
     */
    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置对单金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取免单金额
     */
    @Column(name = "FFREE_AMOUNT")
    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    /**
     * 设置对单金额
     */
    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    /**
     * 获取描述
     */
    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取会计期间
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSTOCK_PERIOD_ID")
    public StockPeriod getPeriod() {
        return period;
    }

    /**
     * 设置会计期间
     */
    public void setPeriod(StockPeriod period) {
        this.period = period;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     */
    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间戳
     */
    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间戳
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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

    @Column(name = "FBILL_TYPE")
    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    @Transient
    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

}
