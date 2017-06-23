package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 待摊费用</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-14 09:16:27
 */
public class PrepaidExpensesVo implements Serializable {

    private static final long serialVersionUID = 8799080652922474564L;

    @NotBlank(message = "费用编号必填")
    @Length(max = 50, message = "费用编号超过{max}个字符")
    private String expensesCode;//费用编号

    @NotBlank(message = "费用名称必填")
    @Length(max = 50, message = "费用名称超过{max}个字符")
    private String expensesName;//费用名称

    @NotBlank(message = "部门必填")
    private String deptId;//部门ID

    private String deptName;//部门名称

    @NotNull(message = "待摊金额必填")
    @Min(value = 0, message = "待摊金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "待摊金额不能大于{value}")
    private BigDecimal expensesAmount;//待摊金额

    @NotNull(message = "待摊月份必填")
    @Min(value = 1, message = "待摊月份不能小于1")
    @Max(value = Integer.MAX_VALUE, message = "待摊月份不能大于{value}")
    private BigDecimal discountPeriod;//计提期数

    @NotBlank(message = "计提日期必填")
    private String shareDate;//计提日期

    //	@NotBlank(message="借方科目必填")
    @Length(max = 32, message = "借方科目ID超过{max}个字符")
    private String debitSubjectId;//借方科目ID
    private String debitSubjectName;//借方科目名称

    //	@NotBlank(message="贷方科目必填")
    @Length(max = 32, message = "贷方科目ID超过{max}个字符")
    private String creditSubjectId;//贷方科目ID
    private String creditSubjectName;//贷方科目名称
    @Length(max = 200, message = "备注超过{max}个字符")
    private String remark;//备注
    private Short recordStatus;//记录状态;0--未审核;1--审核
    private String createTime;//创建时间
    private String creatorId;//创建人
    private String creatorName;//创建人名称
    private String auditTime;//审核时间
    private String auditorId;//审核人
    private String auditorName;//审核人名称
    private String updateTime;//修改时间戳
    private String fid;

    public String getExpensesCode() {
        return this.expensesCode;
    }

    public void setExpensesCode(String expensesCode) {
        this.expensesCode = expensesCode;
    }

    public String getExpensesName() {
        return this.expensesName;
    }

    public void setExpensesName(String expensesName) {
        this.expensesName = expensesName;
    }

    public BigDecimal getExpensesAmount() {
        return this.expensesAmount;
    }

    public void setExpensesAmount(BigDecimal expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public BigDecimal getDiscountPeriod() {
        return this.discountPeriod;
    }

    public void setDiscountPeriod(BigDecimal discountPeriod) {
        this.discountPeriod = discountPeriod;
    }

    public String getShareDate() {
        return this.shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getRecordStatus() {
        return this.recordStatus;
    }

    public void setRecordStatus(Short recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDebitSubjectId() {
        return debitSubjectId;
    }

    public void setDebitSubjectId(String debitSubjectId) {
        this.debitSubjectId = debitSubjectId;
    }

    public String getDebitSubjectName() {
        return debitSubjectName;
    }

    public void setDebitSubjectName(String debitSubjectName) {
        this.debitSubjectName = debitSubjectName;
    }

    public String getCreditSubjectId() {
        return creditSubjectId;
    }

    public void setCreditSubjectId(String creditSubjectId) {
        this.creditSubjectId = creditSubjectId;
    }

    public String getCreditSubjectName() {
        return creditSubjectName;
    }

    public void setCreditSubjectName(String creditSubjectName) {
        this.creditSubjectName = creditSubjectName;
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

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
}
