package cn.fooltech.fool_ops.domain.cashier.vo;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 出纳-初始银行设置</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 11:12:47
 */
public class BankInitVo implements Serializable {

    private static final long serialVersionUID = 5584872651311655302L;

    @Max(value = 9999999999L, message = "日记账期初余额不能超过{value}")
    private BigDecimal accountInit;//银行日记账期初余额

    @Max(value = 9999999999L, message = "对账单期初余额不能超过{value}")
    private BigDecimal statementInit;//银行对账单期初余额

    @Max(value = 9999999999L, message = "日记账借方金额不能超过{value}")
    private BigDecimal accountDebit;//日记账借方金额

    @Max(value = 9999999999L, message = "日记账贷方金额不能超过{value}")
    private BigDecimal accountCredit;//日记账贷方金额

    @Max(value = 9999999999L, message = "日记账调整后余额不能超过{value}")
    private BigDecimal accountBalance;//日记账调整后余额

    @Max(value = 9999999999L, message = "对账单借方金额不能超过{value}")
    private BigDecimal statementDebit;//对账单借方金额

    @Max(value = 9999999999L, message = "对账单贷方金额不能超过{value}")
    private BigDecimal statementCredit;//对账单贷方金额

    @Max(value = 9999999999L, message = "对账单调整后余额不能超过{value}")
    private BigDecimal statementBalance;//对账单调整后余额
    private Short start;//启用： 0--未启用 1--启用
    private String createTime;
    private String updateTime;
    private String fid;

    /**
     * 科目
     */
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private Short bankSubject;//是否银行科目 0：不是  1：是
    private Short cashSubject;//是否现金科目 0：不是  1：是

    /**
     * 创建人
     */
    private String creatorId;
    private String creatorName;

    /**
     * 账套
     */
    private String fiscalAccountId;
    private String fiscalAccountName;


    public BigDecimal getAccountInit() {
        return this.accountInit;
    }

    public void setAccountInit(BigDecimal accountInit) {
        this.accountInit = accountInit;
    }

    public BigDecimal getStatementInit() {
        return this.statementInit;
    }

    public void setStatementInit(BigDecimal statementInit) {
        this.statementInit = statementInit;
    }

    public BigDecimal getAccountDebit() {
        return this.accountDebit;
    }

    public void setAccountDebit(BigDecimal accountDebit) {
        this.accountDebit = accountDebit;
    }

    public BigDecimal getAccountCredit() {
        return this.accountCredit;
    }

    public void setAccountCredit(BigDecimal accountCredit) {
        this.accountCredit = accountCredit;
    }

    public BigDecimal getAccountBalance() {
        return this.accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getStatementDebit() {
        return this.statementDebit;
    }

    public void setStatementDebit(BigDecimal statementDebit) {
        this.statementDebit = statementDebit;
    }

    public BigDecimal getStatementCredit() {
        return this.statementCredit;
    }

    public void setStatementCredit(BigDecimal statementCredit) {
        this.statementCredit = statementCredit;
    }

    public BigDecimal getStatementBalance() {
        return this.statementBalance;
    }

    public void setStatementBalance(BigDecimal statementBalance) {
        this.statementBalance = statementBalance;
    }

    public Short getStart() {
        return this.start;
    }

    public void setStart(Short start) {
        this.start = start;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Short getBankSubject() {
        return bankSubject;
    }

    public void setBankSubject(Short bankSubject) {
        this.bankSubject = bankSubject;
    }

    public Short getCashSubject() {
        return cashSubject;
    }

    public void setCashSubject(Short cashSubject) {
        this.cashSubject = cashSubject;
    }
}
