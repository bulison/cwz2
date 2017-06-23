package cn.fooltech.fool_ops.domain.rate.vo;

import java.io.Serializable;

public class RateMemberSumVo implements Serializable {
    private static final long serialVersionUID = 8435517560057161961L;
    private String userId;
    private String userName;
    private Double allTime;
    private Double eventsNum;
    private Double delayNum;
    private Double delayTime;
    private Double rate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAllTime() {
        return allTime;
    }

    public void setAllTime(Double allTime) {
        this.allTime = allTime;
    }

    public Double getEventsNum() {
        return eventsNum;
    }

    public void setEventsNum(Double eventsNum) {
        this.eventsNum = eventsNum;
    }

    public Double getDelayNum() {
        return delayNum;
    }

    public void setDelayNum(Double delayNum) {
        this.delayNum = delayNum;
    }

    public Double getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Double delayTime) {
        this.delayTime = delayTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }


}
