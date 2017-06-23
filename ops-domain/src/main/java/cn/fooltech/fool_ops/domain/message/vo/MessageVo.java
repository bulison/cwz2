package cn.fooltech.fool_ops.domain.message.vo;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Liaoxf
 * @version 1.0
 * @date 2015年4月21日
 */
public class MessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String fid;

    /**
     * 类型
     * 提醒消息:0
     * 待办:1
     * 预警:2
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收人ID
     */
    private String receiverId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 状态
     * 未读:0
     * 已读:1
     * 停用:2
     */
    private Integer status;

    /**
     * 业务参数对象
     */
    private String busParamObj;


    /**
     * 消息的开始通知日期
     */
    private Date noticeDate;

    /**
     * 操作时间
     */
    private Date operTime;

    /**
     * 是否已通知
     */
    private Boolean isnotify;

    /**
     * 触发动作类型
     */
    private String triggerType;

    /**
     * 最后信息的时间
     */
    private String lastEndTime;

    /**
     * 账套名称
     */
    private String accId;
    private String accName;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getSenderName() {
        return senderName;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * 读取发送时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FSEND_TIME", updatable = false)
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getBusParamObj() {
        return busParamObj;
    }

    public void setBusParamObj(String busParamObj) {
        this.busParamObj = busParamObj;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public Boolean getIsnotify() {
        return isnotify;
    }

    public void setIsnotify(Boolean isnotify) {
        this.isnotify = isnotify;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getLastEndTime() {
        return lastEndTime;
    }

    public void setLastEndTime(String lastEndTime) {
        this.lastEndTime = lastEndTime;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

}
