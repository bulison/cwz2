package cn.fooltech.fool_ops.domain.rate.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>执行人效益分析</p>
 *
 * @author yrl
 * @version 1.0
 * @date 2016年6月29日
 */
@Entity
@Table(name = "rate_member")
public class RateMember extends OpsEntity {
    public static final Short IS_COMPLETE = 1;//已完成
    public static final Short NOT_COMPLETE = 0;//未完成
    public static final Short STOP_TASK = 2;//中止
    public static final Short ADVANCE = 2;//提前
    public static final Short DELAY = 1;//延期
    public static final Short ONTIME = 0;//刚好完成
    public static final Short isCalc_Y=1;//计算
    public static final Short isCalc_N=0;//不计算
    private static final long serialVersionUID = -7052160100839485214L;
    private User user;//人员
    private Task event;//事件
    private Date eventPlanStartDate;//事件计划开始日期
    private Date eventPlanEndDate;//事件计划完成日期
    private Date eventBeginDate;//事件开始日期
    private Date eventCompleteDate;//事件完成日期
    private int eventsNumber = 0;//办理事件数
    private BigDecimal planTotalTime;//总时间
    private int delayNumber = 0;//延误次数
    private int calcDelayNumber=0;//计算延误次数
    private BigDecimal delayTime;//延误时间
    private int isComplete = NOT_COMPLETE;//事件是否完成
    private BigDecimal efficiency;//效率
    private int isCalc=isCalc_N;//计算标识
    private String accId;//财务账套
    private String orgId;//财务账套

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMEMBER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FEVENT_ID")
    public Task getEvent() {
        return event;
    }

    public void setEvent(Task event) {
        this.event = event;
    }

    @Column(name = "FEVENT_PLAN_START_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEventPlanStartDate() {
        return eventPlanStartDate;
    }

    public void setEventPlanStartDate(Date eventPlanStartDate) {
        this.eventPlanStartDate = eventPlanStartDate;
    }

    @Column(name = "FEVENT_PLAN_END_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEventPlanEndDate() {
        return eventPlanEndDate;
    }

    public void setEventPlanEndDate(Date eventPlanEndDate) {
        this.eventPlanEndDate = eventPlanEndDate;
    }

    @Column(name = "FEVENT_BEGIN_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEventBeginDate() {
        return eventBeginDate;
    }

    public void setEventBeginDate(Date eventBeginDate) {
        this.eventBeginDate = eventBeginDate;
    }

    @Column(name = "FEVENT_COMPLETE_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEventCompleteDate() {
        return eventCompleteDate;
    }

    public void setEventCompleteDate(Date eventCompleteDate) {
        this.eventCompleteDate = eventCompleteDate;
    }

    @Column(name = "FEVENTS_NUMBER")
    public int getEventsNumber() {
        return eventsNumber;
    }

    public void setEventsNumber(int eventsNumber) {
        this.eventsNumber = eventsNumber;
    }



	@Column(name = "FPLAN_TOTAL_TIME", precision = 28, scale = 8)
    public BigDecimal getPlanTotalTime() {
		return planTotalTime;
	}

	public void setPlanTotalTime(BigDecimal planTotalTime) {
		this.planTotalTime = planTotalTime;
	}



    @Column(name = "FDELAY_NUMBER")
    public int getDelayNumber() {
        return delayNumber;
    }

    public void setDelayNumber(int delayNumber) {
        this.delayNumber = delayNumber;
    }

    @Column(name = "FDELAY_TIME", precision = 28, scale = 8)
    public BigDecimal getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(BigDecimal delayTime) {
        this.delayTime = delayTime;
    }

    @Column(name = "FIS_COMPLETE")
    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    @Column(name = "FACC_ID", nullable = false)
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Column(name = "FORG_ID", nullable = false)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    @Column(name = "FCALC_DELAY_NUMBER", nullable = false)
	public int getCalcDelayNumber() {
		return calcDelayNumber;
	}
    @Column(name = "FEFFICIENCY", nullable = false)
	public BigDecimal getEfficiency() {
		return efficiency;
	}
	@Column(name = "FIS_CALC", nullable = false)
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
