package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
/**
 * 执行人总计效益VO
 * @author hjr
 * 2017-3-27
 */
public class RateMemberSumVo {
	private String memberId;//员工ID
	private String memberName;//员工姓名
	private int eventsNumber;//办理事件数
	private BigDecimal planTotalTime;//预计总时间
	private BigDecimal totalTime;//实际完成总时间
	private int calcDelayNumber; //延误总次数
	private BigDecimal delayTime;//延误总天数
	private BigDecimal efficiency;//效率
	public String getMemberId() {
		return memberId;
	}
	public int getEventsNumber() {
		return eventsNumber;
	}
	public BigDecimal getPlanTotalTime() {
		return planTotalTime;
	}
	public BigDecimal getTotalTime() {
		return totalTime;
	}
	public int getCalcDelayNumber() {
		return calcDelayNumber;
	}
	public BigDecimal getDelayTime() {
		return delayTime;
	}
	public BigDecimal getEfficiency() {
		return efficiency;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setEventsNumber(int eventsNumber) {
		this.eventsNumber = eventsNumber;
	}
	public void setPlanTotalTime(BigDecimal planTotalTime) {
		this.planTotalTime = planTotalTime;
	}
	public void setTotalTime(BigDecimal totalTime) {
		this.totalTime = totalTime;
	}
	public void setCalcDelayNumber(int calcDelayNumber) {
		this.calcDelayNumber = calcDelayNumber;
	}
	public void setDelayTime(BigDecimal delayTime) {
		this.delayTime = delayTime;
	}
	public void setEfficiency(BigDecimal efficiency) {
		this.efficiency = efficiency;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
}
