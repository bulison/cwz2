package cn.fooltech.fool_ops.domain.rate.entity;

import java.math.BigDecimal;

public class RateMemberSum {
    private String userId;
    private String userName;
    private BigDecimal allTime;
    private BigDecimal eventsNum;
    private BigDecimal delayNum;
    private BigDecimal delayTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getAllTime() {
        return allTime;
    }

    public void setAllTime(BigDecimal allTime) {
        this.allTime = allTime;
    }

    public BigDecimal getEventsNum() {
        return eventsNum;
    }

    public void setEventsNum(BigDecimal eventsNum) {
        this.eventsNum = eventsNum;
    }

    public BigDecimal getDelayNum() {
        return delayNum;
    }

    public void setDelayNum(BigDecimal delayNum) {
        this.delayNum = delayNum;
    }

    public BigDecimal getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(BigDecimal delayTime) {
        this.delayTime = delayTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
