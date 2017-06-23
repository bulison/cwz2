package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 计提税费</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 16:14:16
 */
public class AccrueTaxationVo implements Serializable {

    private static final long serialVersionUID = -3111099903740134624L;

    /**
     * 凭证字
     */
    //@NotBlank(message="凭证字必填")
    private String voucherWordId;
    private String voucherWordName;

    /**
     * 凭证日期
     */
    private String voucherDate;

    /**
     * 凭证摘要
     */
    private String resume;

    /**
     * 税费基数科目
     */
    @NotBlank(message = "税费基数科目必填")
    private String baseSubjectId;
    private String baseSubjectCode;
    private String baseSubjectName;

    /**
     * 应交税费科目
     */
    @NotBlank(message = "应交税费科目必填")
    private String paySubjectId;
    private String paySubjectCode;
    private String paySubjectName;

    /**
     * 附加税费科目
     */
    @NotBlank(message = "附加税费科目必填")
    private String addSubjectId;
    private String addSubjectCode;
    private String addSubjectName;

    /**
     * 征收税费科目
     */
    @NotBlank(message = "征收税费科目必填")
    private String collectSubjectId;
    private String collectSubjectCode;
    private String collectSubjectName;

    /**
     * 税费基数科目取数范围
     * 1--取科目余额
     * 2--取期间发生额
     */
    private Integer baseType;

    /**
     * 税点
     */
    @NotNull(message = "税点必填")
    private BigDecimal point;

    /**
     * 修改时间戳
     */
    private String updateTime;
    private String fid;


    public String getBaseSubjectId() {
        return baseSubjectId;
    }

    public void setBaseSubjectId(String baseSubjectId) {
        this.baseSubjectId = baseSubjectId;
    }

    public String getBaseSubjectName() {
        return baseSubjectName;
    }

    public void setBaseSubjectName(String baseSubjectName) {
        this.baseSubjectName = baseSubjectName;
    }

    public String getPaySubjectId() {
        return paySubjectId;
    }

    public void setPaySubjectId(String paySubjectId) {
        this.paySubjectId = paySubjectId;
    }

    public String getPaySubjectName() {
        return paySubjectName;
    }

    public void setPaySubjectName(String paySubjectName) {
        this.paySubjectName = paySubjectName;
    }

    public String getAddSubjectId() {
        return addSubjectId;
    }

    public void setAddSubjectId(String addSubjectId) {
        this.addSubjectId = addSubjectId;
    }

    public String getAddSubjectName() {
        return addSubjectName;
    }

    public void setAddSubjectName(String addSubjectName) {
        this.addSubjectName = addSubjectName;
    }

    public BigDecimal getPoint() {
        return this.point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
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

    public String getBaseSubjectCode() {
        return baseSubjectCode;
    }

    public void setBaseSubjectCode(String baseSubjectCode) {
        this.baseSubjectCode = baseSubjectCode;
    }

    public String getPaySubjectCode() {
        return paySubjectCode;
    }

    public void setPaySubjectCode(String paySubjectCode) {
        this.paySubjectCode = paySubjectCode;
    }

    public String getAddSubjectCode() {
        return addSubjectCode;
    }

    public void setAddSubjectCode(String addSubjectCode) {
        this.addSubjectCode = addSubjectCode;
    }

    public String getVoucherWordId() {
        return voucherWordId;
    }

    public void setVoucherWordId(String voucherWordId) {
        this.voucherWordId = voucherWordId;
    }

    public String getCollectSubjectId() {
        return collectSubjectId;
    }

    public void setCollectSubjectId(String collectSubjectId) {
        this.collectSubjectId = collectSubjectId;
    }

    public String getCollectSubjectCode() {
        return collectSubjectCode;
    }

    public void setCollectSubjectCode(String collectSubjectCode) {
        this.collectSubjectCode = collectSubjectCode;
    }

    public String getCollectSubjectName() {
        return collectSubjectName;
    }

    public void setCollectSubjectName(String collectSubjectName) {
        this.collectSubjectName = collectSubjectName;
    }

    public Integer getBaseType() {
        return baseType;
    }

    public void setBaseType(Integer baseType) {
        this.baseType = baseType;
    }

    public String getVoucherWordName() {
        return voucherWordName;
    }

    public void setVoucherWordName(String voucherWordName) {
        this.voucherWordName = voucherWordName;
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
