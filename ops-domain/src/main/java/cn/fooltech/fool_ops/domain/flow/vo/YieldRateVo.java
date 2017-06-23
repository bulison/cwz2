package cn.fooltech.fool_ops.domain.flow.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @Description: 流程计划每天收益率VO
 * @author cwz
 * @date 2017年4月12日 上午10:13:44
 */
public class YieldRateVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 主键
	private String id;

	// 计划ID
	private String planId;
	private String planName;

	// 日期

	private Date date;

	// 实际收益率
	private BigDecimal effectiveYieldrate;

	// 当前预计收益率
	private BigDecimal currentYieldRate;
	
	//计划预计收益率
	private BigDecimal estimatedYieldrate;

	// 参考收益率【取当天的资金日损率】
	private BigDecimal referenceYieldrate;

	// 修改时间戳【初始值为当前时间】
	private Date updateTime;

	// 组织ID【机构ID】
	private String orgId;
	private String orgName;

	// 账套ID
	private String accId;
	private String accName;
	// 开始日期，查询用
	private Date startDay;

	// 结束日期，查询用
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getEffectiveYieldrate() {
		return effectiveYieldrate;
	}

	public void setEffectiveYieldrate(BigDecimal effectiveYieldrate) {
		this.effectiveYieldrate = effectiveYieldrate;
	}

	public BigDecimal getCurrentYieldRate() {
		return currentYieldRate;
	}

	public void setCurrentYieldRate(BigDecimal currentYieldRate) {
		this.currentYieldRate = currentYieldRate;
	}

	public BigDecimal getReferenceYieldrate() {
		return referenceYieldrate;
	}

	public void setReferenceYieldrate(BigDecimal referenceYieldrate) {
		this.referenceYieldrate = referenceYieldrate;
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

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
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

	public BigDecimal getEstimatedYieldrate() {
		return estimatedYieldrate;
	}

	public void setEstimatedYieldrate(BigDecimal estimatedYieldrate) {
		this.estimatedYieldrate = estimatedYieldrate;
	}
	
	
}