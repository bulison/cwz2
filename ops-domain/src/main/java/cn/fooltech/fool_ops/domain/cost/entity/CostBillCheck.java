package cn.fooltech.fool_ops.domain.cost.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>费用单勾对实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月8日
 */
@Entity
@Table(name = "tsb_cost_billcheck")
public class CostBillCheck extends OpsOrgEntity {

    private static final long serialVersionUID = 8493988203296891549L;

    /**
     * 关联费用单
     */
    private CostBill costBill;

    /**
     * 关联单据
     */
    private String bill;

    /**
     * 对单日期
     */
    private Date checkDate;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount = BigDecimal.ZERO;

    /**
     * 支出金额
     */
    private BigDecimal freeAmount = BigDecimal.ZERO;

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
     * 获取关联的费用单
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCOST_BILL_ID")
    public CostBill getCostBill() {
        return costBill;
    }

    /**
     * 设置费用单记录表
     */
    public void setCostBill(CostBill costBill) {
        this.costBill = costBill;
    }

    /**
     * 获取关联的单据
     */
//	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @Column(name = "FBILL_ID")
    public String getBill() {
        return bill;
    }

    /**
     * 设置关联单据表
     */
    public void setBill(String bill) {
        this.bill = bill;
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
    @Column(name = "FINCOME_AMOUNT")
    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    /**
     * 设置收入金额
     */
    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    /**
     * 获取支出金额
     */
    @Column(name = "FFREE_AMOUNT")
    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    /**
     * 设置支出金额
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
}
