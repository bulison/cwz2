package cn.fooltech.fool_ops.domain.cashier.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 出纳-银行单据记录</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 15:07:24
 */
public class BankBillVo implements Serializable {

    public static final short SHOW_DEFAULT = 1;
    public static final short NOT_SHOW_DEFAULT = 0;
    private static final long serialVersionUID = 41669495932952173L;
    /**
     * 类型
     * 1--期初企业未到账
     * 2--期初银行未到账
     * 3--银行日记账
     * 4--银行对账单
     * 5--现金日记账
     */
    private Integer type;
    /**
     * 结算日期（业务日期）
     */
    private String settlementDate;
    /**
     * 当天顺序号
     */
    @Min(value = 0, message = "当天顺序号不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "当天顺序号不能大于{value}")
    private BigDecimal orderno;
    /**
     * 结算方式
     */

    private String settlementTypeId;
    private String settlementTypeName;
    private String settlementTypeCode;
    /**
     * 结算号
     */
    @Length(max = 10, message = "结算号超过{max}位数")
    private String settlementNo;
    /**
     * 借方金额
     */

    @Min(value = Integer.MIN_VALUE, message = "借方金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "借方金额不能大于{value}")
    private BigDecimal debit;
    /**
     * 贷方金额
     */
    @Min(value = Integer.MIN_VALUE, message = "贷方金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "贷方金额不能大于{value}")
    private BigDecimal credit;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 经手人
     */
    private String memberId;
    private String memberName;
    private String memberCode;
    /**
     * 凭证日期（单据日期）
     */
    private String voucherDate;
    /**
     * 摘要
     */
    @Length(max = 200, message = "摘要长度超过{max}个字符")
    private String resume;
    /**
     * 勾对标识：0--未勾对 1--已勾对(原来checked与easyui冲突，现改成fchecked)
     */
    private Short fchecked;
    /**
     * 勾对记录(关联本表)
     */
    private String checkId;
    /**
     * 勾对日期
     */
    private String checkDate;
    private String createTime;
    private String updateTime;
    private String fid;
    /**
     * 科目
     */
    @Length(max = 32, message = "科目长度超过{max}个字符")
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    /**
     * 选择多个科目
     */
    private String subjectIds;
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
    /**
     * 初始银行设置ID
     */
    private String bankInitId;
    /**
     * 金额方向
     */
    private Integer direction;
    /**
     * 用于搜索过滤
     */
    private String startDate;
    private String endDate;
    /**
     * 多个类型
     */
    private String types;
    /**
     * 当没有填写subjectId和subjectIds属性时是否展示默认 0：不展示 1：展示
     */
    private Short showDefault = SHOW_DEFAULT;
    /**
     * 手续费
     */
    @Min(value = 1, message = "手续费不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "手续费不能大于{value}")
    private BigDecimal poundage;

    /**
     * 来源标识
     */
    private String source;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSettlementDate() {
        return this.settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getOrderno() {
        return this.orderno;
    }

    public void setOrderno(BigDecimal orderno) {
        this.orderno = orderno;
    }

    public String getSettlementNo() {
        return this.settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return this.credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public String getVoucherDate() {
        return this.voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getResume() {
        return this.resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
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

    public String getSettlementTypeId() {
        return settlementTypeId;
    }

    public void setSettlementTypeId(String settlementTypeId) {
        this.settlementTypeId = settlementTypeId;
    }

    public String getSettlementTypeName() {
        return settlementTypeName;
    }

    public void setSettlementTypeName(String settlementTypeName) {
        this.settlementTypeName = settlementTypeName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
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

    public String getBankInitId() {
        return bankInitId;
    }

    public void setBankInitId(String bankInitId) {
        this.bankInitId = bankInitId;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Short getFchecked() {
        return fchecked;
    }

    public void setFchecked(Short fchecked) {
        this.fchecked = fchecked;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public Short getShowDefault() {
        return showDefault;
    }

    public void setShowDefault(Short showDefault) {
        this.showDefault = showDefault;
    }

    public String getSettlementTypeCode() {
        return settlementTypeCode;
    }

    public void setSettlementTypeCode(String settlementTypeCode) {
        this.settlementTypeCode = settlementTypeCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
