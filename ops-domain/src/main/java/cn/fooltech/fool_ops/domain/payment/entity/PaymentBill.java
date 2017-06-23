package cn.fooltech.fool_ops.domain.payment.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>收付款单实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月24日
 */
@Entity
@Table(name = "tsb_payment_bill")
public class PaymentBill extends OpsOrgEntity {
    /**
     * 状态- 已勾对
     */
    public static final short CHECKED=1;
    /**
     * 状态- 未勾对
     */
    public static final short NOCHECKED=0;
    /**
     * 状态- 未审核
     */
    public static final int STATUS_UNAUDITED = 0;
    /**
     * 状态- 已审核
     */
    public static final int STATUS_AUDITED = 1;
    /**
     * 状态- 已作废
     */
    public static final int STATUS_CANCELED = 2;
    /**
     * 对公
     */
    public static final int TO_PUBLIC = 0;
    /**
     * 对私
     */
    public static final int TO_PRIVATE = 1;
    /**
     * 类型-收款
     */
    public static final int TYPE_INCOME = 51;
    /**
     * 类型-支出
     */
    public static final int TYPE_EXPEND = 52;
    /**
     * 类型-采购返利单
     */
    public static final int TYPE_PURCHASE_REBATE = 55;
    /**
     * 类型-销售返利单
     */
    public static final int TYPE_SALES_REBATE = 56;
    private static final long serialVersionUID = -2867377822504031157L;
    /**
     * 单号
     */
    private String code;
    /**
     * 凭证
     */
    private String vouchercode;
    /**
     * 单据类型
     */
    private Integer billType;
    /**
     * 单据日期
     */
    private Date billDate;
    /**
     * 银行
     */
    private Bank bank;
    /**
     * 客户
     */
    private Customer customer;
    /**
     * 供应商
     */
    private Supplier supplier;
    /**
     * 金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 累计勾对金额
     */
    private BigDecimal totalCheckAmount = BigDecimal.ZERO;
    /**
     * 负责人
     */
    private Member member;
    /**
     * 描述
     */
    private String describe;
    /**
     * 会计期间
     */
    private StockPeriod stockPeriod;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 审核人
     */
    private User auditor;
    /**
     * 生成银行日记单操作人
     */
    private User saveAsBankBillOperator;
    /**
     * 生产银行日记单操作时间
     */
    private Date saveAsBankBillTime;
    /**
     * 作废时间
     */
    private Date cancelTime;
    /**
     * 作废人
     */
    private User cancelor;
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
     * 状态
     */
    private Integer recordStatus = STATUS_UNAUDITED;
    /**
     * 部门
     */
    private Organization dept;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 受款人
     */
    @Deprecated
    private Member payee;

    /**
     * 受款人
     */
    private String payeeName;

    /**
     * 对公标识
     */
    private Integer toPublic;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 本期销售
     */
    private BigDecimal sales = new BigDecimal(0);
    /**
     * 返利率%
     */
    private BigDecimal rates = new BigDecimal(0);
    
    //单据关联事件（计划关联单据使用）
    private Task task;
    private short isCheck=NOCHECKED;
    /**
     * 单据关联事件（计划关联单据使用）
     * @return
     */
    @JoinColumn(name = "FEVENT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}


    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPAYEE")
    @Deprecated
    public Member getPayee() {
        return payee;
    }

    @Deprecated
    public void setPayee(Member payee) {
        this.payee = payee;
    }

    @Column(name = "FTO_PUBLIC")
    public Integer getToPublic() {
        return toPublic;
    }

    public void setToPublic(Integer toPublic) {
        this.toPublic = toPublic;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FSTART_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEND_DATE")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "FSALES")
    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    @Column(name = "FRATES")
    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "FVOUCHERCODE")
    public String getVouchercode() {
        return vouchercode;
    }

    public void setVouchercode(String vouchercode) {
        this.vouchercode = vouchercode;
    }

    @Column(name = "FBILL_TYPE")
    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FBILL_DATE")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBANK_ID")
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCUSTOMER_ID")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "FTOTAL_CHECK_AMOUNT")
    public BigDecimal getTotalCheckAmount() {
        return totalCheckAmount;
    }

    public void setTotalCheckAmount(BigDecimal totalCheckAmount) {
        this.totalCheckAmount = totalCheckAmount;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMEMBER_ID")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSTOCK_PERIOD_ID")
    public StockPeriod getStockPeriod() {
        return stockPeriod;
    }

    public void setStockPeriod(StockPeriod stockPeriod) {
        this.stockPeriod = stockPeriod;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSAVE_AS_BANKBILL_OPERATOR")
    public User getSaveAsBankBillOperator() {
        return saveAsBankBillOperator;
    }

    public void setSaveAsBankBillOperator(User saveAsBankBillOperator) {
        this.saveAsBankBillOperator = saveAsBankBillOperator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FSAVE_AS_BANKBILL_TIME")
    public Date getSaveAsBankBillTime() {
        return saveAsBankBillTime;
    }

    public void setSaveAsBankBillTime(Date saveAsBankBillTime) {
        this.saveAsBankBillTime = saveAsBankBillTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCANCEL_TIME")
    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCANCELOR")
    public User getCancelor() {
        return cancelor;
    }

    public void setCancelor(User cancelor) {
        this.cancelor = cancelor;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME", updatable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "RECORD_STATUS")
    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
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

    @Column(name = "FPAYEE_NAME")
    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    @Column(name="fis_check")
	public short getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(short isCheck) {
		this.isCheck = isCheck;
	}

}
