package cn.fooltech.fool_ops.domain.cost.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>表单传输对象 - 费用申请单</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-03 15:32:22
 */
public class ExpenApplyBillVo implements Serializable {

    private static final long serialVersionUID = 2050117949070244332L;
    @NotBlank(message = "单号必填")
    @Length(max = 50, message = "单号长度超过{max}个字符")
    private String code;//单号
    @Length(max = 50, message = "原始单号长度超过{max}个字符")
    private String voucherCode;//原单号
    private Integer billType;//单据类型
    @NotBlank(message = "申请日期必填")
    @Length(max = 10, message = "申请日期长度超过{max}个字符")
    private String date;//单据日期
    private String planEnd;//计划完成日期
    private String periodId;//会计期间ID
    private String periodName;//会计期间名称
    @NotNull(message = "金额必填")
    @Min(value = 1, message = "金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "金额不能大于{value}")
    private BigDecimal amount;//金额
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;//描述
    private String auditTime;//审核时间
    private String auditorName;//审核人
    private String cancelTime;//作废时间
    private String cancelName;//作废人
    private String createTime;//创建时间
    private String creatorName;//创建人
    private String updateTime;//修改时间戳
    @NotBlank(message = "部门必填")
    @Length(max = 32, message = "部门ID长度超过{max}个字符")
    private String deptId;//部门ID
    private String deptName;//部门名称
    @NotBlank(message = "申请人必填")
    @Length(max = 32, message = "申请人ID长度超过{max}个字符")
    private String memberId;//申请人ID
    private String memberName;//申请人名称
    private Integer recordStatus;//状态
    private String fid;

    /**
     * 页面搜索关键字- 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 页面搜索关键字- 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVoucherCode() {
        return this.voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getBillType() {
        return this.billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlanEnd() {
        return this.planEnd;
    }

    public void setPlanEnd(String planEnd) {
        this.planEnd = planEnd;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCancelTime() {
        return this.cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
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

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getCancelName() {
        return cancelName;
    }

    public void setCancelName(String cancelName) {
        this.cancelName = cancelName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
