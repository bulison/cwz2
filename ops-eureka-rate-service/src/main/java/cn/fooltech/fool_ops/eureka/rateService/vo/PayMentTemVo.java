package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收付款单临时VO
 * @author hjr
 *2017-4-10	
 */
public class PayMentTemVo {
	private BigDecimal amount;		//勾对金额
	private BigDecimal freeAmount;	//免单金额
	private Date paymentBillDate;	//收款单单据日期
	private Date wareHouseBillDate;	//销售出库单单据日期
	public BigDecimal getAmount() {
		return amount;
	}
	public BigDecimal getFreeAmount() {
		return freeAmount;
	}
	public Date getPaymentBillDate() {
		return paymentBillDate;
	}
	public Date getWareHouseBillDate() {
		return wareHouseBillDate;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setFreeAmount(BigDecimal freeAmount) {
		this.freeAmount = freeAmount;
	}
	public void setPaymentBillDate(Date paymentBillDate) {
		this.paymentBillDate = paymentBillDate;
	}
	public void setWareHouseBillDate(Date wareHouseBillDate) {
		this.wareHouseBillDate = wareHouseBillDate;
	}
}
