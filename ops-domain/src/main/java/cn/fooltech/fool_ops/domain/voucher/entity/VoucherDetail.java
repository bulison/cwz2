package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 凭证明细
 * </p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Entity
@Table(name = "TBD_VOUCHER_DETAIL")
public class VoucherDetail extends OpsOrgEntity {

    private static final long serialVersionUID = -7725382204015167440L;

    /**
     * 凭证
     */
    private Voucher voucher;

    /**
     * 摘要
     */
    private String resume;

    /**
     * 科目
     */
    private FiscalAccountingSubject accountingSubject;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount = BigDecimal.ZERO;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount = BigDecimal.ZERO;
    ;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 数量
     */
    private BigDecimal quantity = BigDecimal.ZERO;

    /**
     * 外币币别
     */
    private AuxiliaryAttr currency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate = BigDecimal.ZERO;
    ;

    /**
     * 外币金额
     */
    private BigDecimal currencyAmount = BigDecimal.ZERO;

    /**
     * 供应商
     */
    private Supplier supplier;

    /**
     * 销售商
     */
    private Customer customer;

    /**
     * 部门
     */
    private Organization department;

    /**
     * 职员
     */
    private Member member;

    /**
     * 仓库
     */
    private AuxiliaryAttr warehouse;

    /**
     * 项目
     */
    private AuxiliaryAttr project;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

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
     * 获取凭证
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHERID", nullable = false)
    public Voucher getVoucher() {
        return voucher;
    }

    /**
     * 设置凭证
     *
     * @param voucher
     */
    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    /**
     * 获取摘要
     *
     * @return
     */
    @Column(name = "FRESUME", length = 200)
    public String getResume() {
        return resume;
    }

    /**
     * 设置摘要
     *
     * @param resume
     */
    public void setResume(String resume) {
        this.resume = resume;
    }

    /**
     * 获取科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUBJECTID")
    public FiscalAccountingSubject getAccountingSubject() {
        return accountingSubject;
    }

    /**
     * 设置科目
     *
     * @param accountingSubject
     */
    public void setAccountingSubject(FiscalAccountingSubject accountingSubject) {
        this.accountingSubject = accountingSubject;
    }

    /**
     * 获取借方金额
     *
     * @return
     */
    @Column(name = "FDEBIT_AMOUNT")
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    /**
     * 设置借方金额
     *
     * @param debitAmount
     */
    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    /**
     * 获取贷方金额
     *
     * @return
     */
    @Column(name = "FCREDIT_AMOUNT")
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    /**
     * 设置贷方金额
     *
     * @param creditAmount
     */
    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    /**
     * 获取单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNIT_ID")
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取数量
     *
     * @return
     */
    @Column(name = "FQUANTITY")
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取外币币别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCURRENCY_ID")
    public AuxiliaryAttr getCurrency() {
        return currency;
    }

    /**
     * 设置外币币别
     *
     * @param currency
     */
    public void setCurrency(AuxiliaryAttr currency) {
        this.currency = currency;
    }

    /**
     * 获取汇率
     *
     * @return
     */
    @Column(name = "FEXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    /**
     * 设置汇率
     *
     * @param exchangeRate
     */
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * 获取外币金额
     *
     * @return
     */
    @Column(name = "FCURRENCY_AMOUNT")
    public BigDecimal getCurrencyAmount() {
        return currencyAmount;
    }

    /**
     * 设置外币金额
     *
     * @param currencyAmount
     */
    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    /**
     * 获取供应商
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * 设置供应商
     *
     * @param supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * 获取销售商
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCUSTOMER_ID")
    public Customer getCustomer() {
        return customer;
    }

    /**
     * 设置销售商
     *
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * 获取部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPARTMENT_ID")
    public Organization getDepartment() {
        return department;
    }

    /**
     * 设置部门
     *
     * @param department
     */
    public void setDepartment(Organization department) {
        this.department = department;
    }

    /**
     * 获取职员
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMEMBER_ID")
    public Member getMember() {
        return member;
    }

    /**
     * 设置职员
     *
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取仓库
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_ID")
    public AuxiliaryAttr getWarehouse() {
        return warehouse;
    }

    /**
     * 设置仓库
     *
     * @param warehouse
     */
    public void setWarehouse(AuxiliaryAttr warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * 获取项目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPROJECT_ID")
    public AuxiliaryAttr getProject() {
        return project;
    }

    /**
     * 设置项目
     *
     * @param project
     */
    public void setProject(AuxiliaryAttr project) {
        this.project = project;
    }

    /**
     * 获取货品
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置货品
     *
     * @param goods
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
