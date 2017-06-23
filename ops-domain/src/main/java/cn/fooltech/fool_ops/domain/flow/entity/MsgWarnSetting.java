package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>消息预警配置</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月17日
 */
@Entity
@Table(name = "tflow_msg_warn_setting")
public class MsgWarnSetting extends OpsOrgEntity {
    /**
     * 发起人
     */
    public static final int SEND_TYPE_FQR = 0;

    //==============消息接收对象的类型===============
    /**
     * 责任人
     */
    public static final int SEND_TYPE_ZRR = 1;
    /**
     * 承办人
     */
    public static final int SEND_TYPE_CBR = 2;
    /**
     * 关注人
     */
    public static final int SEND_TYPE_GZR = 3;
    /**
     * 评价人
     */
    public static final int SEND_TYPE_PJR = 4;
    /**
     * 留言人
     */
    public static final int SEND_TYPE_LYR = 5;
    /**
     * 评分人
     */
    public static final int SEND_TYPE_PFR = 6;
    /**
     * 部门监督人
     */
    public static final int SEND_TYPE_BMJDR = 7;
    /**
     * 公司监督人
     */
    public static final int SEND_TYPE_GSJDR = 8;
    /**
     * 审核人
     */
    public static final int SEND_TYPE_SHR = 9;
    /**
     * 触发事件
     */
    public static final int TASK_TYPE_CFSJ = 0;

    //==============事件类型（触发的动作类型）===============
    /**
     * 前关联事件
     */
    public static final int TASK_TYPE_QGLSJ = 1;
    /**
     * 后关联事件
     */
    public static final int TASK_TYPE_HGLSJ = 2;
    /**
     * 计划
     */
    public static final int TYPE_PLAN = 0;

    //==============配置类型===============
    /**
     * 事件
     */
    public static final int TYPE_TASK = 1;
    /**
     * 系统默认生成配置
     */
    public static final int TYPE_SYSTEM_YES = 1;

    //==============是否系统配置===============
    /**
     * 用户自定义配置
     */
    public static final int TYPE_SYSTEM_NO = 0;
    private static final long serialVersionUID = -1681838469892360347L;

    //=======================================================
    /**
     * 事件级别
     */
    private Integer taskLevel;

    /**
     * 提前提醒天数
     */
    private Integer days;

    /**
     * 配置类型0：计划配置  1：事件配置
     */
    private Integer type;

    /**
     * 重发天数
     */
    private Integer retryDays;

    /**
     * 值域
     */
    private BigDecimal range;

    /**
     * 消息接收对象的类型
     */
    private Integer sendType;

    /**
     * 触发动作后的业务状态（场景）
     */
    private String busScene;

    /**
     * 触发动作的类型
     */
    private Integer triggerType;

    /**
     * 事件类型（触发的动作类型）
     */
    private Integer taskType;

    /**
     * 上级发送
     */
    private Integer toSuperior;

    /**
     * 下级发送
     */
    private Integer toSubordinate;

    /**
     * 是否系统配置
     */
    private Integer isSystem = TYPE_SYSTEM_NO;

    private Set<Supervise> superviseSet = Sets.newHashSet();

    /**
     * 获取任务级别
     *
     * @return
     */
    @Column(name = "FTASK_LEVEL")
    public Integer getTaskLevel() {
        return taskLevel;
    }

    /**
     * 设置任务级别
     *
     * @param taskLevel
     */
    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    /**
     * 获取提前提醒天数
     *
     * @return
     */
    @Column(name = "FDAYS")
    public Integer getDays() {
        return days;
    }

    /**
     * 设置提前提醒天数
     *
     * @param days
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * 获取重发天数
     *
     * @return
     */
    @Column(name = "FRETRY_DAYS")
    public Integer getRetryDays() {
        return retryDays;
    }

    /**
     * 设置重发天数
     *
     * @param retryDays
     */
    public void setRetryDays(Integer retryDays) {
        this.retryDays = retryDays;
    }

    /**
     * 获取值域
     *
     * @return
     */
    @Column(name = "FRANGE")
    public BigDecimal getRange() {
        return range;
    }

    /**
     * 设置值域
     *
     * @param range
     */
    public void setRange(BigDecimal range) {
        this.range = range;
    }

    /**
     * 获取发送类型
     *
     * @return
     */
    @Column(name = "FSEND_TYPE")
    public Integer getSendType() {
        return sendType;
    }

    /**
     * 设置发送类型
     *
     * @param sendType
     */
    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    /**
     * 获取触发动作类型
     *
     * @return
     */
    @Column(name = "FTRIGGER_TYPE")
    public Integer getTriggerType() {
        return triggerType;
    }

    /**
     * 设置触发动作类型
     *
     * @param triggerType
     */
    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    /**
     * 获取事件类型
     *
     * @return
     */
    @Column(name = "FTASK_TYPE")
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * 设置事件类型
     *
     * @param taskType
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取上级发送
     *
     * @return
     */
    @Column(name = "FTO_SUPERIOR")
    public Integer getToSuperior() {
        return toSuperior;
    }

    /**
     * 设置上级发送
     *
     * @param toSuperior
     */
    public void setToSuperior(Integer toSuperior) {
        this.toSuperior = toSuperior;
    }

    /**
     * 获取下级发送
     *
     * @return
     */
    @Column(name = "FTO_SUBORDINATE")
    public Integer getToSubordinate() {
        return toSubordinate;
    }

    /**
     * 设置下级发送
     *
     * @param toSubordinate
     */
    public void setToSubordinate(Integer toSubordinate) {
        this.toSubordinate = toSubordinate;
    }

    /**
     * 获取配置类型
     *
     * @return
     */
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    /**
     * 设置配置类型
     *
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取触发动作后的业务状态（场景）
     *
     * @return
     */
    @Column(name = "FBUS_SCENE")
    public String getBusScene() {
        return busScene;
    }

    /**
     * 设置触发动作后的业务状态（场景）
     *
     * @param busScene
     */
    public void setBusScene(String busScene) {
        this.busScene = busScene;
    }

    /**
     * 获取是否系统配置
     *
     * @return
     */
    @Column(name = "FIS_SYSTEM")
    public Integer getIsSystem() {
        return isSystem;
    }

    /**
     * 设置是否系统配置
     *
     * @param isSystem
     */
    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    /**
     * 获取监督人
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "warnSetting")
    public Set<Supervise> getSuperviseSet() {
        return superviseSet;
    }

    /**
     * 设置监督人
     *
     * @param superviseSet
     */
    public void setSuperviseSet(Set<Supervise> superviseSet) {
        this.superviseSet = superviseSet;
    }
}
