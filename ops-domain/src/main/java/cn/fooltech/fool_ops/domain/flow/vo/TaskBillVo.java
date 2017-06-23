package cn.fooltech.fool_ops.domain.flow.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * @Description: 计划事件关联单据VO
 * @author cwz
 * @date 2017年3月13日 上午11:54:19
 */
public class TaskBillVo implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 计划ID
	private String planId;
	private String planName;

	// 事件ID
	private String taskId;
	private String taskName;
	// 单据类型
	private Integer billSign;

	// 单据ID
	private String billId;
	private String billName;

	// 创建时间
	private Date createTime;

	// 创建人
	private String creatorId;
	private String creatorName;

	// 修改时间戳(初始值为当前时间)
	private Date updateTime;

	// 组织ID(机构ID)
	private String orgId;
	private String orgName;

	// 账套ID
	private String fiscalAccountId;
	private String fiscalAccountName;
	
	private BigDecimal totalAmount;
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@ApiModelProperty(value = "开始日期，查询用")
	private Date startDay;

	@ApiModelProperty(value = "结束日期，查询用")
	private Date endDay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getBillSign() {
		return billSign;
	}

	public void setBillSign(Integer billSign) {
		this.billSign = billSign;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	
	

}