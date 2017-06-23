package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工效率明细表
 * @author hjr	
 * 2017-03-28
 */
public class RateMemberDetailVo {
	private String taskName; //事件名称
	private Date planStartDate;//计划开始日期
	private Date planEndDate; //计划结束日期
	private Date completeDate;//完成日期
	private BigDecimal delayTime;//延误时间
	private int delayNumber;//延误次数
	public String getTaskName() {
		return taskName;
	}
	public Date getPlanStartDate() {
		return planStartDate;
	}
	public Date getPlanEndDate() {
		return planEndDate;
	}
	public Date getCompleteDate() {
		return completeDate;
	}

	public BigDecimal getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(BigDecimal delayTime) {
		this.delayTime = delayTime;
	}
	public int getDelayNumber() {
		return delayNumber;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public void setDelayNumber(int delayNumber) {
		this.delayNumber = delayNumber;
	}
	
}
