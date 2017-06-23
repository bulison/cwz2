package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 消息预警配置</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-17 14:08:34
 */
public class MsgWarnSettingVo implements Serializable {

    private static final long serialVersionUID = -6345177667655006383L;

    @NotNull(message = "任务级别必填")
    @Min(value = 1, message = "任务级别不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "任务级别不能大于{value}")
    private BigDecimal taskLevel;//任务级别
    private Integer days;//提前提醒天数
    private Integer retryDays;//重发天数
    private BigDecimal range;//值域

    //消息接收对象的类型
    /*0 - 发起人
	1 - 责任人
	2 - 承办人
	3 - 关注人
	4 - 评价人
	5 - 留言人
	6 - 评分人
	7 - 部门监督人
	8 - 公司监督*/
    @NotNull(message = "消息接收对象类型必填")
    private Integer sendType;

    //触发动作的类型
	/*10:提交
	11:修改
	12:删除
	13:终止
	20:申请延迟 
	30:审核
	31:审核通过办理
	32:审核不通过办理
	33:审核通过申请延迟 
	34:审核不通过申请延迟
	40:新建
	41:完成
	42:办理结束
	43:办理开始
	50:评价
	51:评分
	52:关注
	53:留言
	54:关联
	60:提醒
	61:延迟报警
	62:收益率报警
	63:库存报警 */
    @NotNull(message = "触发动作类型必填")
    private Integer triggerType;

    //事件类型（触发的动作类型）
	/*0 - 触发事件
	1 - 前关联事件
	2 - 后关联事件*/
    private Integer taskType;
    private Integer toSuperior;//上级发送
    private Integer toSubordinate;//下级发送
    private Integer isSystem;//是否系统配置;0用户自定义配置; 1:系统默认生成配置
    private String fid;

    private String superviseIds;//监督人IDs，多个用逗号隔开

    @NotBlank(message = "场景必填")
    private String busScene;//发送场景

    private Integer type;//类型：0：计划；1：事件

    public Integer getDays() {
        return this.days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getRetryDays() {
        return this.retryDays;
    }

    public void setRetryDays(Integer retryDays) {
        this.retryDays = retryDays;
    }

    public BigDecimal getRange() {
        return this.range;
    }

    public void setRange(BigDecimal range) {
        this.range = range;
    }

    public Integer getSendType() {
        return this.sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    public Integer getTaskType() {
        return this.taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getToSuperior() {
        return this.toSuperior;
    }

    public void setToSuperior(Integer toSuperior) {
        this.toSuperior = toSuperior;
    }

    public Integer getToSubordinate() {
        return this.toSubordinate;
    }

    public void setToSubordinate(Integer toSubordinate) {
        this.toSubordinate = toSubordinate;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public BigDecimal getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(BigDecimal taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public String getSuperviseIds() {
        return superviseIds;
    }

    public void setSuperviseIds(String superviseIds) {
        this.superviseIds = superviseIds;
    }

    public String getBusScene() {
        return busScene;
    }

    public void setBusScene(String busScene) {
        this.busScene = busScene;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
