package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.util.Date;
/**
 * 资金计划日志表临时VO
 * @author hjr
 *2017-04-12
 */
public class CapitalPlanChangeLogTemVo {
	private int changeType;
	private Date prePaymentDate;
	private Date paymentDate;
	public int getChangeType() {
		return changeType;
	}
	public Date getPrePaymentDate() {
		return prePaymentDate;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}
	public void setPrePaymentDate(Date prePaymentDate) {
		this.prePaymentDate = prePaymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
}
