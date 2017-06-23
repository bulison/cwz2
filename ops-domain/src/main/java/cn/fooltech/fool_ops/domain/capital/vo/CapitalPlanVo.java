package cn.fooltech.fool_ops.domain.capital.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;


/***
 * 
 * @Description: 资金计划VO
 * @author cwz
 * @date 2017年2月28日 下午4:34:10
 */
public class CapitalPlanVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 单号
	private String code;

	// 说明，简单说明资金用途
	private String explain;

	// 关联类型，记录单据sign，计划为71
	private Integer relationSign;

	// 关联ID 可关联仓库单据、收付款单、费用单、计划
	private String relationId;

	// 预计收付款日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date paymentDate;

	// 计划收付金额
	private BigDecimal planAmount;

	// 单据金额
	private BigDecimal billAmount;

	// 收付款金额
	private BigDecimal paymentAmount;

	// 备注
	private String remark;

	// 状态 0-草稿，1-审核，2-坏账，3-完成，4-取消
	private Integer recordStatus;

	// 是否计算 0-不计算，1-计算；当单据被事件关联，则不计算
	private Integer calculation;

	// 准备坏账标识 0-正常，1-坏账；可以取消
	private Integer badDebtSign;

	// 准备坏账日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date badDebtDate;

	// 准备坏账操作人
	private String badDebtId;
	private String badDebtName;

	// 完成标识 0-正常，1-完成；可以取消
	private Integer completeSign;

	// 完成日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date completeDate;

	// 完成操作人
	private String completeId;
	private String completeName;

	
	// 创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	// 创建人
	private String creatorId;
	private String creatorName;

	// 审核时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date auditTime;

	// 审核人
	private String auditorId;
	private String auditorName;

	// 取消时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date cancelTime;

	// 取消人
	private String cancelorId;
	private String cancelorName;

	// 修改时间戳，初始值为当前时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	// 组织ID，机构ID
	private String orgId;
	private String orgname;

	// 账套ID
	private String fiscalAccountId;
	private String fiscalAccountName;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDay;

    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDay;
	/**
	 * 明细记录json数据
	 */
	private String details;

	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Integer getRelationSign() {
		return relationSign;
	}

	public void setRelationSign(Integer relationSign) {
		this.relationSign = relationSign;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getCalculation() {
		return calculation;
	}

	public void setCalculation(Integer calculation) {
		this.calculation = calculation;
	}

	public Integer getBadDebtSign() {
		return badDebtSign;
	}

	public void setBadDebtSign(Integer badDebtSign) {
		this.badDebtSign = badDebtSign;
	}

	public Date getBadDebtDate() {
		return badDebtDate;
	}

	public void setBadDebtDate(Date badDebtDate) {
		this.badDebtDate = badDebtDate;
	}

	public String getBadDebtId() {
		return badDebtId;
	}

	public void setBadDebtId(String badDebtId) {
		this.badDebtId = badDebtId;
	}

	public String getBadDebtName() {
		return badDebtName;
	}

	public void setBadDebtName(String badDebtName) {
		this.badDebtName = badDebtName;
	}

	public Integer getCompleteSign() {
		return completeSign;
	}

	public void setCompleteSign(Integer completeSign) {
		this.completeSign = completeSign;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getCompleteId() {
		return completeId;
	}

	public void setCompleteId(String completeId) {
		this.completeId = completeId;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
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

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
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

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
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

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
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