package cn.fooltech.fool_ops.domain.register.entity;

import java.util.Date;


/**
 * <p>手机短信实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年6月17日
 */
public class Sms {

    public static final int WAIT_SEND = 0;//待发送 (待发送状态是由业务调用者指定)
    public static final int SENDED = 1;//已发送
    public static final int SEND_FAIL = 2;//发送失败
    public static final int RE_SEND = 3;//重发
    public static final int WAIT_CALL = 4;//待对端回调
    private static final long serialVersionUID = 545607437066372848L;
    /**
     * 手机号码
     */
    private String tel;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送标识
     */
    private Integer sendFlag;

    /**
     * 发送结果
     */
    private String sendResult;

    /**
     * 是否重试
     */
    private Boolean sendRepeat;

    /**
     * 重试次数
     */
    private Integer repeatTime;

    /**
     * 当前重试次数
     */
    private Integer currRepeatTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 流水号
     */
    private String tradeNo;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }

    public Boolean getSendRepeat() {
        return sendRepeat;
    }

    public void setSendRepeat(Boolean sendRepeat) {
        this.sendRepeat = sendRepeat;
    }

    public Integer getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(Integer repeatTime) {
        this.repeatTime = repeatTime;
    }

    public Integer getCurrRepeatTime() {
        return currRepeatTime;
    }

    public void setCurrRepeatTime(Integer currRepeatTime) {
        this.currRepeatTime = currRepeatTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
