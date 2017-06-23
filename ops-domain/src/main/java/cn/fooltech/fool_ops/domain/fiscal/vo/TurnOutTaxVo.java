package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 转出未交增值税</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 10:12:03
 */
public class TurnOutTaxVo implements Serializable {

    private static final long serialVersionUID = 3836355946176716142L;

    /**
     * 凭证字
     */
    @NotBlank(message = "凭证字必填")
    private String voucherWordId;
    private String voucherWordName;

    /**
     * 凭证日期
     */
    @NotBlank(message = "凭证日期必填")
    private String voucherDate;

    /**
     * 凭证摘要
     */
    private String resume;

    /**
     * 销项税科目
     */
    @NotBlank(message = "销项税科目必填")
    private String outSubjectId;
    private String outSubjectName;
    private String outSubjectCode;

    /**
     * 进项税科目
     */
    @NotBlank(message = "进项税科目必填")
    private String inSubjectId;
    private String inSubjectName;
    private String inSubjectCode;

    /**
     * 转出未交增值税科目
     */
    @NotBlank(message = "转出未交增值税科目必填")
    private String taxSubjectId;
    private String taxSubjectName;
    private String taxSubjectCode;

    /**
     * 未交增值税科目
     */
    @NotBlank(message = "未交增值税科目必填")
    private String unpaidSubjectId;
    private String unpaidSubjectName;
    private String unpaidSubjectCode;

    /**
     * 支付税费科目
     */
    @NotBlank(message = "支付税费科目必填")
    private String paySubjectId;
    private String paySubjectName;
    private String paySubjectCode;

    /**
     * 修改时间戳
     */
    private String updateTime;
    private String fid;

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

    public String getVoucherWordId() {
        return voucherWordId;
    }

    public void setVoucherWordId(String voucherWordId) {
        this.voucherWordId = voucherWordId;
    }

    public String getVoucherWordName() {
        return voucherWordName;
    }

    public void setVoucherWordName(String voucherWordName) {
        this.voucherWordName = voucherWordName;
    }

    public String getOutSubjectId() {
        return outSubjectId;
    }

    public void setOutSubjectId(String outSubjectId) {
        this.outSubjectId = outSubjectId;
    }

    public String getOutSubjectName() {
        return outSubjectName;
    }

    public void setOutSubjectName(String outSubjectName) {
        this.outSubjectName = outSubjectName;
    }

    public String getInSubjectId() {
        return inSubjectId;
    }

    public void setInSubjectId(String inSubjectId) {
        this.inSubjectId = inSubjectId;
    }

    public String getInSubjectName() {
        return inSubjectName;
    }

    public void setInSubjectName(String inSubjectName) {
        this.inSubjectName = inSubjectName;
    }

    public String getTaxSubjectId() {
        return taxSubjectId;
    }

    public void setTaxSubjectId(String taxSubjectId) {
        this.taxSubjectId = taxSubjectId;
    }

    public String getTaxSubjectName() {
        return taxSubjectName;
    }

    public void setTaxSubjectName(String taxSubjectName) {
        this.taxSubjectName = taxSubjectName;
    }

    public String getOutSubjectCode() {
        return outSubjectCode;
    }

    public void setOutSubjectCode(String outSubjectCode) {
        this.outSubjectCode = outSubjectCode;
    }

    public String getInSubjectCode() {
        return inSubjectCode;
    }

    public void setInSubjectCode(String inSubjectCode) {
        this.inSubjectCode = inSubjectCode;
    }

    public String getTaxSubjectCode() {
        return taxSubjectCode;
    }

    public void setTaxSubjectCode(String taxSubjectCode) {
        this.taxSubjectCode = taxSubjectCode;
    }

    public String getUnpaidSubjectName() {
        return unpaidSubjectName;
    }

    public void setUnpaidSubjectName(String unpaidSubjectName) {
        this.unpaidSubjectName = unpaidSubjectName;
    }

    public String getPaySubjectName() {
        return paySubjectName;
    }

    public void setPaySubjectName(String paySubjectName) {
        this.paySubjectName = paySubjectName;
    }

    public String getUnpaidSubjectId() {
        return unpaidSubjectId;
    }

    public void setUnpaidSubjectId(String unpaidSubjectId) {
        this.unpaidSubjectId = unpaidSubjectId;
    }

    public String getUnpaidSubjectCode() {
        return unpaidSubjectCode;
    }

    public void setUnpaidSubjectCode(String unpaidSubjectCode) {
        this.unpaidSubjectCode = unpaidSubjectCode;
    }

    public String getPaySubjectId() {
        return paySubjectId;
    }

    public void setPaySubjectId(String paySubjectId) {
        this.paySubjectId = paySubjectId;
    }

    public String getPaySubjectCode() {
        return paySubjectCode;
    }

    public void setPaySubjectCode(String paySubjectCode) {
        this.paySubjectCode = paySubjectCode;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
