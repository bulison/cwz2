package cn.fooltech.fool_ops.domain.capital.entity;
/**
 * 变更资金计划日志实体类
 */
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="tcapital_plan_change_log")
public class CapitalPlanChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ID 主键
	 */
	private String id;
	/**
	 * 明细表ID
	 */
	private CapitalPlanDetail detail;
	/**
	 * 变更类型 1-修改日期，2-修改金额
	 */
	private Integer changeType;
	/**
	 * 上次预计收付日期
	 */
	private Date prePaymentDate;
	/**
	 * 本次预计收付款日期
	 */
	private Date paymentDate;
	/**
	 * 上次预计收付金额
	 */
	private BigDecimal prePaymentAmount;
	/**
	 * 本次预计收付金额
	 */
	private BigDecimal paymentAmount;
	/**
	 * 	备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private User create;
	/**
	 * 修改时间戳 初始值为当前时间
	 */
	private Timestamp updateTime;
	/**
	 * 组织ID 机构ID
	 */
	private Organization org;
	/**
	 * 账套ID
	 */
	private FiscalAccount fiscalAccount;
	@Id
	@ApiModelProperty(value = "主键")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "FID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@ApiModelProperty(value = "明细表ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FDETAIL_ID", nullable = false)
	public CapitalPlanDetail getDetail() {
		return detail;
	}
	public void setDetail(CapitalPlanDetail detail) {
		this.detail = detail;
	}
	@ApiModelProperty(value = "变更类型 1-修改日期，2-修改金额")
	@Column(name = "FCHANGE_TYPE")
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
//	@ApiModelProperty(value = "上次预计收付日期")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FPRE_PAYMENT_DATE")
	public Date getPrePaymentDate() {
		return prePaymentDate;
	}
	public void setPrePaymentDate(Date prePaymentDate) {
		this.prePaymentDate = prePaymentDate;
	}
//	@ApiModelProperty(value = "本次预计收付款日期")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FPAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	//@ApiModelProperty(value = "上次预计收付金额")
	@Column(name = "FPRE_PAYMENT_AMOUNT")
	public BigDecimal getPrePaymentAmount() {
		return prePaymentAmount;
	}
	public void setPrePaymentAmount(BigDecimal prePaymentAmount) {
		this.prePaymentAmount = prePaymentAmount;
	}
	//@ApiModelProperty(value = "本次预计收付金额")
	@Column(name = "FPAYMENT_AMOUNT")
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	//@ApiModelProperty(value = "备注")
	@Column(name = "FREMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
//	@ApiModelProperty(value = "创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//@ApiModelProperty(value = "创建人")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FCREATOR_ID", nullable = false)
	public User getCreate() {
		return create;
	}
	public void setCreate(User create) {
		this.create = create;
	}
	@ApiModelProperty(value = "更新时间(可不输入)")
//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FUPDATE_TIME")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	@ApiModelProperty(value = "组织ID/机构ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FORG_ID", nullable = false)
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}

	@ApiModelProperty(value = "账套ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FACC_ID", nullable = false)
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}
	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
	
}
