package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * 数量金额明细账Vo
 *
 * @author xjh
 */
public class QuentityAmountDetailVo implements Serializable {

    private static final long serialVersionUID = -5932109451356601453L;
    private String voucherDate;//凭证日期
    private String voucherWordNum;//凭证字号
    private String voucherNum;//顺序号
    private String voucherResume;//摘要
    private String subjectCode;//科目编号
    private String subjectName;//科目名称
    private String debitUnit;//借方单位
    private String debitQuentity;//借方数量
    private String debitUnitPrice;//借方单价
    private String debitAmount;//借方金额
    private String creditUnit;//贷方单位
    private String creditQuentity;//贷方数量
    private String creditUnitPrice;//贷方单价
    private String creditAmount;//贷方金额
    private String endUnit;//余额单位
    private String endQuentity;//余额数量
    private String endUnitPrice;//余额单价
    private String endAmount;//余额金额
    private String endDirection;//方向
    private String endAbsAmount;//余额（显示正数）

    //-----过滤条件----
    private String startPeriodId;//财务会计期间ID
    private String endPeriodId;//财务会计期间ID
    private String curSubjectCode;//当前查询的科目编号
    private String voucherStatus;//凭证状态
    private Integer level;//科目级别

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherWordNum() {
        return voucherWordNum;
    }

    public void setVoucherWordNum(String voucherWordNum) {
        this.voucherWordNum = voucherWordNum;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getVoucherResume() {
        return voucherResume;
    }

    public void setVoucherResume(String voucherResume) {
        this.voucherResume = voucherResume;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDebitUnit() {
        return debitUnit;
    }

    public void setDebitUnit(String debitUnit) {
        this.debitUnit = debitUnit;
    }

    public String getDebitQuentity() {
        return debitQuentity;
    }

    public void setDebitQuentity(String debitQuentity) {
        this.debitQuentity = debitQuentity;
    }

    public String getDebitUnitPrice() {
        return debitUnitPrice;
    }

    public void setDebitUnitPrice(String debitUnitPrice) {
        this.debitUnitPrice = debitUnitPrice;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(String creditUnit) {
        this.creditUnit = creditUnit;
    }

    public String getCreditQuentity() {
        return creditQuentity;
    }

    public void setCreditQuentity(String creditQuentity) {
        this.creditQuentity = creditQuentity;
    }

    public String getCreditUnitPrice() {
        return creditUnitPrice;
    }

    public void setCreditUnitPrice(String creditUnitPrice) {
        this.creditUnitPrice = creditUnitPrice;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getEndUnit() {
        return endUnit;
    }

    public void setEndUnit(String endUnit) {
        this.endUnit = endUnit;
    }

    public String getEndQuentity() {
        return endQuentity;
    }

    public void setEndQuentity(String endQuentity) {
        this.endQuentity = endQuentity;
    }

    public String getEndUnitPrice() {
        return endUnitPrice;
    }

    public void setEndUnitPrice(String endUnitPrice) {
        this.endUnitPrice = endUnitPrice;
    }

    public String getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(String endAmount) {
        this.endAmount = endAmount;
    }

    public String getEndDirection() {
        return endDirection;
    }

    public void setEndDirection(String endDirection) {
        this.endDirection = endDirection;
    }

    public String getEndAbsAmount() {
        return endAbsAmount;
    }

    public void setEndAbsAmount(String endAbsAmount) {
        this.endAbsAmount = endAbsAmount;
    }

    public String getCurSubjectCode() {
        return curSubjectCode;
    }

    public void setCurSubjectCode(String curSubjectCode) {
        this.curSubjectCode = curSubjectCode;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getEndPeriodId() {
        return endPeriodId;
    }

    public void setEndPeriodId(String endPeriodId) {
        this.endPeriodId = endPeriodId;
    }

    public String getStartPeriodId() {
        return startPeriodId;
    }

    public void setStartPeriodId(String startPeriodId) {
        this.startPeriodId = startPeriodId;
    }
}
