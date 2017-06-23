package cn.fooltech.fool_ops.domain.flow.entity;

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

/**
 * @Description:流程计划每天收益率
 * @author cwz
 * @date 2017年4月12日 上午10:08:35
 */
@Entity
@Table(name = "tflow_yield_rate")
public class YieldRate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 计划ID
	private Plan plan;

	// 日期
	private Date date;

	// 实际收益率
	private BigDecimal effectiveYieldrate;

	// 当前预计收益率
	private BigDecimal currentYieldRate;

	// 参考收益率【取当天的资金日损率】
	private BigDecimal referenceYieldrate;

	// 修改时间戳【初始值为当前时间】
	private Date updateTime;

	// 组织ID【机构ID】
	private Organization organization;

	// 账套ID
	private FiscalAccount fiscalAccount;

	@Id
	@Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_ID", nullable = false)
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	@Column(name = "FDATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name = "FEFFECTIVE_YIELDRATE")
	public BigDecimal getEffectiveYieldrate() {
		return effectiveYieldrate;
	}

	public void setEffectiveYieldrate(BigDecimal effectiveYieldrate) {
		this.effectiveYieldrate = effectiveYieldrate;
	}
	@Column(name = "FCURRENT_YIELD_RATE")
	public BigDecimal getCurrentYieldRate() {
		return currentYieldRate;
	}

	public void setCurrentYieldRate(BigDecimal currentYieldRate) {
		this.currentYieldRate = currentYieldRate;
	}
	@Column(name = "FREFERENCE_YIELDRATE")
	public BigDecimal getReferenceYieldrate() {
		return referenceYieldrate;
	}

	public void setReferenceYieldrate(BigDecimal referenceYieldrate) {
		this.referenceYieldrate = referenceYieldrate;
	}
	@Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}

}