package cn.fooltech.fool_ops.domain.cost.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 表单传输对象--费用单
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月7日
 */
public class CostBillVo implements Serializable {

    private static final long serialVersionUID = 6541789074502772261L;

    private String fid;
    private String orgId;
    /**
     * 类型 1：客户；2：供应商
     */
    private Integer csvType;
    /**
     * 累计收付款金额
     */
    private BigDecimal totalPayAmount;
    /**
     * 单号
     */
    @NotBlank(message = "单号必填")
    @Length(max = 50, message = "单号长度超过{max}个字符")
    private String code;

    /**
     * 凭证号
     */
    @Length(max = 50, message = "原始单号长度超过{max}个字符")
    private String voucherCode;

    /**
     * 单据日期
     */
    @NotBlank(message = "单据日期必填")
    @Length(max = 10, message = "单据日期长度超过{max}个字符")
    private String billDate;

    /**
     * 费用项目ID
     */
    @Length(max = 32, message = "费用项目ID长度超过{max}个字符")
    private String feeId;

    /**
     * 费用项目名称
     */
    private String feeName;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

    /**
     * 支出金额
     */
    private BigDecimal freeAmount;

    /**
     * 银行ID
     */
    // @NotBlank(message="银行账号必填")
    @Length(max = 32, message = "账号长度超过{max}个字符")
    private String bankId;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 部门ID
     */
    @NotBlank(message = "部门必填")
    @Length(max = 32, message = "部门ID长度超过{max}个字符")
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 经手人Id
     */
    @NotBlank(message = "经手人必填")
    @Length(max = 32, message = "经手人长度超过{max}个字符")
    private String memberId;

    /**
     * 经手人名称
     */
    private String memberName;

    /**
     * 收付款关联的客户或供应商
     */
    @Length(max = 32, message = "收支单位长度超过{max}个字符")
    private String csvId;

    /**
     * 收付款关联的客户或供应商名称
     */
    private String csvName;

    /**
     * 累计勾对金额
     */
    private BigDecimal totalCheckAmount;
    /**
     * 累计未勾对金额
     */
    private BigDecimal totalUnCheckAmount;

    /**
     * 累计未勾对金额
     */
    private BigDecimal payAmount;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;

    /**
     * 会计期间Id
     */
    private String periodId;

    /**
     * 会计期间名称
     */
    private String periodName;

    /**
     * 付款人
     */
    private String payerId;

    /**
     * 付款人名称
     */
    private String payerName;

    /**
     * 创建人Id
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 审核人名称
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 核对标识
     */
    private boolean checked;

    /**
     * 核对人ID
     */
    private String checkerId;

    /**
     * 核对人名字
     */
    private String checkerName;

    /**
     * 核对时间
     */
    private String checkTime;

    /**
     * 作废人Id
     */
    private String cancelorId;

    /**
     * 作废人名称
     */
    private String cancelorName;

    /**
     * 作废时间
     */
    private String cancelTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 状态
     */
    private Integer recordStatus;

    /**
     * 页面搜索关键字- 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    /**
     * 页面搜索关键字- 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 查询字段：code、vouchercode
     */
    private String searchKey;

    /**
     * 费用标识 0--不处理 1--增加往来单位应收/应付款项 2--减少往来单位应收/应付款项
     */
    private Integer expenseType = 0;
    
    //单据关联事件id（计划关联单据使用）
    private String taskId;
    
   //单据关联事件名称（计划关联单据使用）
    private String taskName;
    
    //关联事件id过滤查询。等于1才处理
    private Integer taskFilter;
    private short isCheck;
    public Integer getTaskFilter() {
		return taskFilter;
	}

	public void setTaskFilter(Integer taskFilter) {
		this.taskFilter = taskFilter;
	}

	/**
     * 单据关联事件id（计划关联单据使用）
     * @return
     */
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 单据关联事件名称（计划关联单据使用）
	 * @return
	 */
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

    public BigDecimal getTotalUnCheckAmount() {
        return totalUnCheckAmount;
    }

    public void setTotalUnCheckAmount(BigDecimal totalUnCheckAmount) {
        this.totalUnCheckAmount = totalUnCheckAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getCsvId() {
        return csvId;
    }

    public void setCsvId(String csvId) {
        this.csvId = csvId;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public BigDecimal getTotalCheckAmount() {
        return totalCheckAmount;
    }

    public void setTotalCheckAmount(BigDecimal totalCheckAmount) {
        this.totalCheckAmount = totalCheckAmount;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getAuditTime() {
        return auditTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCancelorId() {
        return cancelorId;
    }

    public void setCancelorId(String cancelorId) {
        this.cancelorId = cancelorId;
    }

    public String getCancelorName() {
        return cancelorName;
    }

    public void setCancelorName(String cancelorName) {
        this.cancelorName = cancelorName;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(Integer expenseType) {
        this.expenseType = expenseType;
    }

    public Integer getCsvType() {
        return csvType;
    }

    public void setCsvType(Integer csvType) {
        this.csvType = csvType;
    }

    public BigDecimal getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(BigDecimal totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

	public short getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(short isCheck) {
		this.isCheck = isCheck;
	}

}
