package cn.fooltech.fool_ops.domain.capital.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

/**
 * 
 * @Description: 资金计划明细 实体
 * @author cwz
 * @date 2017年2月28日 下午3:43:39
 */
@Entity
@Table(name = "tcapital_plan_detail")
public class CapitalPlanDetail {

	// 主键
	private String id;

	// 主表ID
	private CapitalPlan capital;

	// 说明，简单说明资金用途
	private String explain;

	// 关联类型，记录单据sign，计划为72
	private Integer relationSign;

	// 关联ID 可关联仓库单据、收付款单、费用单、计划事件
	private String relationId;

	//原预计收付款日期
	private Date orgPaymentDate;
	
	// 预计收付款日期
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

	// 准备坏账标识 0-正常，1-坏账
	private Integer badDebtSign;

	// 准备坏账日期
	private Date badDebtDate;

	// 准备坏账操作人
	private User badDebt;

	// 完成标识 0-正常，1-完成；可以取消
	private Integer completeSign;

	// 完成日期
	private Date completeDate;

	// 完成操作人
	private User complete;

	// 创建时间
	private Date createTime;

	// 创建人
	private User creator;

	// 审核时间
	private Date auditTime;

	// 审核人
	private User auditor;

	// 取消时间
	private Date cancelTime;

	// 取消人
	private User cancelor;

	// 修改时间戳，初始值为当前时间
	private Date updateTime;

	// 组织ID，机构ID
	private Organization org;

	// 账套ID
	private FiscalAccount fiscalAccount;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "FID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCAPITAL_ID")
	public CapitalPlan getCapital() {
		return capital;
	}

	public void setCapital(CapitalPlan capital) {
		this.capital = capital;
	}

	@Column(name = "FEXPLAIN")
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	@Column(name = "FRELATION_SIGN")
	public Integer getRelationSign() {
		return relationSign;
	}

	public void setRelationSign(Integer relationSign) {
		this.relationSign = relationSign;
	}
	@Column(name = "FRELATION_ID")
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	@Column(name = "FORG_PAYMENT_DATE")
	public Date getOrgPaymentDate() {
		return orgPaymentDate;
	}

	public void setOrgPaymentDate(Date orgPaymentDate) {
		this.orgPaymentDate = orgPaymentDate;
	}

	@Column(name = "FPAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	@Column(name = "FPLAN_AMOUNT")
	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	@Column(name = "FBILL_AMOUNT")
	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	@Column(name = "FPAYMENT_AMOUNT")
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	@Column(name = "FREMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "RECORD_STATUS")
	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	@Column(name = "FBAD_DEBT_SIGN")
	public Integer getBadDebtSign() {
		return badDebtSign;
	}

	public void setBadDebtSign(Integer badDebtSign) {
		this.badDebtSign = badDebtSign;
	}
	@Column(name = "FBAD_DEBT_DATE")
	public Date getBadDebtDate() {
		return badDebtDate;
	}

	public void setBadDebtDate(Date badDebtDate) {
		this.badDebtDate = badDebtDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FBAD_DEBT_ID")
	public User getBadDebt() {
		return badDebt;
	}

	public void setBadDebt(User badDebt) {
		this.badDebt = badDebt;
	}
	@Column(name = "FCOMPLETE_SIGN")
	public Integer getCompleteSign() {
		return completeSign;
	}

	public void setCompleteSign(Integer completeSign) {
		this.completeSign = completeSign;
	}
	@Column(name = "FCOMPLETE_DATE")
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCOMPLETE_ID")
	public User getComplete() {
		return complete;
	}

	public void setComplete(User complete) {
		this.complete = complete;
	}
	@Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCREATOR_ID")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	@Column(name = "FAUDIT_TIME")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FAUDITOR")
	public User getAuditor() {
		return auditor;
	}

	public void setAuditor(User auditor) {
		this.auditor = auditor;
	}
	@Column(name = "FCANCEL_TIME")
	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCANCELOR")
	public User getCancelor() {
		return cancelor;
	}

	public void setCancelor(User cancelor) {
		this.cancelor = cancelor;
	}

	@Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORG_ID")
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
	
	
}