package cn.fooltech.fool_ops.domain.rate.vo;

import cn.fooltech.fool_ops.domain.rate.entity.RateMember;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>执行人效益分析vo</p>
 *
 * @author yrl
 * @version 1.0
 * @date 2016年6月30日
 */
public class RateMemberVo implements Serializable {
    private static final long serialVersionUID = -8550347949418520789L;

    private String fid;
    private String userId;//人员id
    private String userName;//人员
    private String eventName;//事件
    private Date eventPlanStartDate;//事件计划开始日期
    private Date eventPlanEndDate;//事件计划完成日期
    private Date eventBeginDate;//事件开始日期
    private Date eventCompleteDate;//事件完成日期
    private int eventsNumber = 0;//办理事件数
    private BigDecimal totalTime;//总时间
    private int delayNumber = 0;//延误次数
    private BigDecimal delayTime;//延误时间
    private int isComplete = RateMember.NOT_COMPLETE;//事件是否完成
    private String orgId;//机构id
    private String rate;//效益
    private int isDelay;//事件是否延误
    private int calcDelayNumber=0;//计算延误次数
    private BigDecimal efficiency;//效率
    private int isCalc=RateMember.isCalc_N;//计算标识
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventPlanStartDate() {
        return eventPlanStartDate;
    }

    public void setEventPlanStartDate(Date eventPlanStartDate) {
        this.eventPlanStartDate = eventPlanStartDate;
    }

    public Date getEventPlanEndDate() {
        return eventPlanEndDate;
    }

    public void setEventPlanEndDate(Date eventPlanEndDate) {
        this.eventPlanEndDate = eventPlanEndDate;
    }

    public Date getEventBeginDate() {
        return eventBeginDate;
    }

    public void setEventBeginDate(Date eventBeginDate) {
        this.eventBeginDate = eventBeginDate;
    }

    public Date getEventCompleteDate() {
        return eventCompleteDate;
    }

    public void setEventCompleteDate(Date eventCompleteDate) {
        this.eventCompleteDate = eventCompleteDate;
    }

    public int getEventsNumber() {
        return eventsNumber;
    }

    public void setEventsNumber(int eventsNumber) {
        this.eventsNumber = eventsNumber;
    }

    public BigDecimal getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(BigDecimal totalTime) {
        this.totalTime = totalTime;
    }

    public int getDelayNumber() {
        return delayNumber;
    }

    public void setDelayNumber(int delayNumber) {
        this.delayNumber = delayNumber;
    }

    public BigDecimal getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(BigDecimal delayTime) {
        this.delayTime = delayTime;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(int isDelay) {
        this.isDelay = isDelay;
    }

	public int getCalcDelayNumber() {
		return calcDelayNumber;
	}

	public BigDecimal getEfficiency() {
		return efficiency;
	}

	public int getIsCalc() {
		return isCalc;
	}

	public void setCalcDelayNumber(int calcDelayNumber) {
		this.calcDelayNumber = calcDelayNumber;
	}

	public void setEfficiency(BigDecimal efficiency) {
		this.efficiency = efficiency;
	}

	public void setIsCalc(int isCalc) {
		this.isCalc = isCalc;
	}


}
