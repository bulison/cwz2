package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 资金计划明细表临时VO
 * @author hjr
 *2017-4-11
 */
public class CapitalPlanDetailTemVo {
	private String fid;//主键
	private Date paymentDate;	//预计收付款日期
	private Date orgPaymentDate;//原预计首付款日期
	private String relationId;	//关联ID 可关联仓库单据等
	private BigDecimal planAmount;//计划收付款金额
	private BigDecimal billAmount;//单据金额
	private BigDecimal paymentAmount;//收付款金额
	public Date getPaymentDate() {
		return paymentDate;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public BigDecimal getPlanAmount() {
		return planAmount;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public Date getOrgPaymentDate() {
		return orgPaymentDate;
	}
	public void setOrgPaymentDate(Date orgPaymentDate) {
		this.orgPaymentDate = orgPaymentDate;
	}
	
	
}
