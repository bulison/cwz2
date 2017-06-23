package cn.fooltech.fool_ops.domain.cost.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>费用单-实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月6日
 */
@Entity
@Table(name = "tsb_cost_bill")
public class CostBill extends OpsOrgEntity {
    /**
     * 状态- 已勾对
     */
    public static final short CHECKED=1;
    /**
     * 状态- 未勾对
     */
    public static final short NOCHECKED=0;
    /**
     * 状态- 未核对
     */
    public static final int STATUS_UNCHECK= 0;
    /**
     * 状态- 已核对
     */
    public static final int STATUS_CHECKED = 1;
    /**
     * 不处理
     */
    public static final int NOT_USED = 2;
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
    public static final int EXPENSE_TYPE_COMMON = 0;//不处理
    public static final int EXPENSE_TYPE_ADD = 1;//增加往来单位应收/应付款项
    public static final int EXPENSE_TYPE_SUBTRACT = 2;//减少往来单位应收/应付款项
    private static final long serialVersionUID = 8856360393932043372L;
    /**
     * 单号
     */
    private String code;
    /**
     * 凭证号
     */
    private String voucherCode;
    /**
     * 单据日期
     */
    private Date billDate;
    /**
     * 费用项目
     */
    private AuxiliaryAttr fee;
    /**
     * 收入金额
     */
    private BigDecimal incomeAmount = BigDecimal.ZERO;
    /**
     * 支出金额
     */
    private BigDecimal freeAmount = BigDecimal.ZERO;
    /**
     * 银行
     */
    private Bank bank;
    /**
     * 部门
     */
    private Organization dept;
    /**
     * 经手人
     */
    private Member member;
    /**
     * 收付款关联的客户或供应商
     */
    private CustomerSupplierView csv;
    /**
     * 累计勾对金额
     */
    private BigDecimal totalCheckAmount = BigDecimal.ZERO;
    /**
     * 累计勾对收付款的金额
     */
    private BigDecimal totalPayAmount = BigDecimal.ZERO;
    /**
     * 付款金额
     */
    private BigDecimal payAmount = BigDecimal.ZERO;
    /**
     * 描述
     */
    private String describe;
    /**
     * 会计期间
     */
    private StockPeriod stockPeriod;
    /**
     * 付款人
     */
    private Member payer;
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
     * 核对标识
     */
    private int checked;
    /**
     * 核对人
     */
    private User checker;
    /**
     * 核对时间
     */
    private Date checkTime;
    /**
     * 作废人
     */
    private User cancelor;
    /**
     * 作废时间
     */
    private Date cancelTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private Integer recordStatus = STATUS_UNAUDITED;
    /**
     * 权限过滤的部门
     */
    private Organization dep;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 费用标识
     * 0--不处理
     * 1--增加往来单位应收/应付款项
     * 2--减少往来单位应收/应付款项
     */
    private Integer expenseType = EXPENSE_TYPE_COMMON;

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
	
    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "FVOUCHERCODE")
    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    @Column(name = "FDATE")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @JoinColumn(name = "FFEE_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getFee() {
        return fee;
    }

    public void setFee(AuxiliaryAttr fee) {
        this.fee = fee;
    }

    @Column(name = "FINCOME_AMOUNT")
    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    @Column(name = "FFREE_AMOUNT")
    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    @JoinColumn(name = "FBANK_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @JoinColumn(name = "FDEP_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @JoinColumn(name = "FMEMBER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @JoinColumn(name = "FCUSTOMER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public CustomerSupplierView getCsv() {
        return csv;
    }

    public void setCsv(CustomerSupplierView csv) {
        this.csv = csv;
    }

    @Column(name = "FTOTAL_CHECK_AMOUNT")
    public BigDecimal getTotalCheckAmount() {
        return totalCheckAmount;
    }

    public void setTotalCheckAmount(BigDecimal totalCheckAmount) {
        this.totalCheckAmount = totalCheckAmount;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @JoinColumn(name = "FSTOCK_PERIOD_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public StockPeriod getStockPeriod() {
        return stockPeriod;
    }

    public void setStockPeriod(StockPeriod stockPeriod) {
        this.stockPeriod = stockPeriod;
    }

    @JoinColumn(name = "FPAYER")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Member getPayer() {
        return payer;
    }

    public void setPayer(Member payer) {
        this.payer = payer;
    }

    @JoinColumn(name = "FCREATOR_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JoinColumn(name = "FAUDITOR")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    @Column(name = "FAUDIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Column(name = "FCHECKED")
    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    @JoinColumn(name = "FCHECK_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getChecker() {
        return checker;
    }

    public void setChecker(User checker) {
        this.checker = checker;
    }

    @Column(name = "FCHECK_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @JoinColumn(name = "FCANCELOR")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getCancelor() {
        return cancelor;
    }

    public void setCancelor(User cancelor) {
        this.cancelor = cancelor;
    }

    @Column(name = "FCANCEL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
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
    public Organization getDep() {
        return dep;
    }

    public void setDep(Organization dep) {
        this.dep = dep;
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
     * 获取费用标识
     *
     * @return
     */
    @Column(name = "FEXPENSE_TYPE")
    public Integer getExpenseType() {
        return expenseType;
    }

    /**
     * 设置费用标识
     *
     * @param expenseType
     */
    public void setExpenseType(Integer expenseType) {
        this.expenseType = expenseType;
    }

    /**
     * 获取金额
     *
     * @return
     */
    @Transient
    public BigDecimal getAmount() {
        //if(this.incomeAmount!=null && incomeAmount.compareTo(BigDecimal.ZERO)!=0)return incomeAmount;
        return freeAmount;
    }

    @Column(name = "FTOTAL_PAY_AMOUNT")
    public BigDecimal getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(BigDecimal totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    @Column(name = "FPAY_AMOUNT")
    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
    @Column(name="fis_check")
	public short getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(short isCheck) {
		this.isCheck = isCheck;
	}
    
}
