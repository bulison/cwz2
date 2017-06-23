/**
 *
 */
package cn.fooltech.fool_ops.domain.message.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * <p>消息</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "tmc_message")
public class Message extends OpsEntity {

    /**
     * 状态 - 未读
     */
    public static final int STATUS_UNREAD = 0;
    /**
     * 状态 - 已读
     */
    public static final int STATUS_READ = 1;
    /**
     * 状态 - 停用
     */
    public static final int STATUS_SUSPEND = 2;
    /**
     * 提醒消息
     */
    public final static int TYPE_NOTIFY = 0;
    /**
     * 待办
     */
    public final static int TYPE_WAIT_PROCESS = 1;
    /**
     * 预警
     */
    public final static int TYPE_WARNING = 2;
    public static final int TRIGGER_TYPE_SUBMIT = 10; //提交
    public static final int TRIGGER_TYPE_UPDATE = 11; //修改
    public static final int TRIGGER_TYPE_DELETE = 12; //删除
    public static final int TRIGGER_TYPE_STOP = 13; //终止
    public static final int TRIGGER_TYPE_CHANGE_UNDERTAKER = 14;//变更承办人
    public static final int TRIGGER_TYPE_CHANGE_PRINCIPALER = 15;//变更责任人
    public static final int TRIGGER_TYPE_DELAY = 20; //申请延迟
    public static final int TRIGGER_TYPE_CHECK = 30; //审核
    public static final int TRIGGER_TYPE_CHECK_EXECUTE_YES = 31; //审核通过办理
    public static final int TRIGGER_TYPE_CHECK_EXECUTE_NO = 32; //审核不通过办理
    public static final int TRIGGER_TYPE_CHECK_DELAY_YES = 33; //审核通过申请延迟
    public static final int TRIGGER_TYPE_CHECK_DELAY_NO = 34; //审核不通过申请延迟
    public static final int TRIGGER_TYPE_NEW = 40; //新建、分派
    public static final int TRIGGER_TYPE_COMPLETE = 41; //完成
    public static final int TRIGGER_TYPE_EXECUTE_END = 42; //办理结束
    public static final int TRIGGER_TYPE_EXECUTE_START = 43; //办理开始
    //public static final int TRIGGER_TYPE_TASK_ALL_COMPLETE = 44; //所有事件均已完成
    public static final int TRIGGER_TYPE_EVALUATE = 50; //评价
    public static final int TRIGGER_TYPE_SCORE = 51; //评分
    public static final int TRIGGER_TYPE_FOLLOW = 52; //关注
    public static final int TRIGGER_TYPE_CHAT = 53; //留言
    public static final int TRIGGER_TYPE_RELEVANCE = 54; //关联
    public static final int TRIGGER_TYPE_EARLY_REMIND = 60; //提醒
    public static final int TRIGGER_TYPE_DELAY_ALARM = 61; //延迟报警
    public static final int TRIGGER_TYPE_YIELD_ALARM = 62; //收益率报警
    public static final int TRIGGER_TYPE_STOCK_ALARM = 63; //库存报警
    public static final int TRIGGER_TYPE_ADVANCE_WARN = 64; //提前一天预告有事办理
    public static final int TRIGGER_TYPE_PURCHASE_PRODUCE = 70; //库存不足触发采购计划或生产计划

    public static final int TRIGGER_TYPE_DELAY_NOT_SUBMIT = 81;//计划超过X天没提交
    public static final int TRIGGER_TYPE_CAPTIAL_WARNING = 90;//资金池预警
    public static final int TRIGGER_TYPE_NORMAL_PRICE_NOTIFY = 100;//报价提醒

    private static final long serialVersionUID = -7807893965626511213L;
    /**
     * 类型
     */
    private Integer type = TYPE_NOTIFY;
    /**
     * 标题
     */
    private String title;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 接收人ID
     */
    private String receiverId;
    /**
     * 接收人名称
     */
    private String receiverName;
    /**
     * 发送人ID
     */
    private String senderId;
    /**
     * 发送人名称
     */
    private String senderName;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 状态，未读/已读/停用
     */
    private Integer status = STATUS_UNREAD;
    /**
     * 读取时间
     */
    private Date readTime;
    /**
     * 用户操作时间（点击按钮的时间）
     */
    private Date operTime;
    /**
     * 是否已通知
     */
    private Boolean isnotify = false;
    /**
     * 发送方式
     */
    private String sendType;
    /**
     * 触发动作类型
     */
    private String triggerType;
    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 消息参数
     */
    private List<MessageParamater> params = Lists.newArrayList();

    /**
     * 获取类型
     *
     * @return
     */
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取标题
     *
     * @return
     */
    @Column(name = "FTITLE")
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取短信内容
     *
     * @return
     */
    @Column(name = "FCONTENT")
    public String getContent() {
        return content;
    }

    /**
     * 设置短信内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取接收人ID
     *
     * @return
     */
    @Column(name = "FRECEIVER_ID")
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * 设置接收人ID
     *
     * @param receiverId
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取接收人名称
     *
     * @return
     */
    @Column(name = "FRECEIVER_NAME")
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置接收人名称
     *
     * @param receiverName
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取发送人ID
     *
     * @return
     */
    @Column(name = "FSENDER_ID")
    public String getSenderId() {
        return senderId;
    }

    /**
     * 设置发送人ID
     *
     * @param senderId
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取发送人姓名
     *
     * @return
     */
    @Column(name = "FSENDER_NAME")
    public String getSenderName() {
        return senderName;
    }

    /**
     * 设置发送人姓名
     *
     * @param senderName
     */
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

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "FSTATUS")
    public int getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取读取时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FREAD_TIME")
    public Date getReadTime() {
        return readTime;
    }

    /**
     * 设置读取时间
     *
     * @param readTime
     */
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    /**
     * 获取操作时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FOPER_TIME")
    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    /**
     * 是否已通知
     *
     * @return Boolean
     */
    @Column(name = "FISNOTIFY")
    public Boolean getIsnotify() {
        return isnotify;
    }

    public void setIsnotify(Boolean isnotify) {
        this.isnotify = isnotify;
    }

    /**
     * 获取发送方式
     */
    @Column(name = "FSEND_TYPE")
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @Column(name = "FTRIGGER_TYPE")
    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "message")
    public List<MessageParamater> getParams() {
        return params;
    }

    public void setParams(List<MessageParamater> params) {
        this.params = params;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
