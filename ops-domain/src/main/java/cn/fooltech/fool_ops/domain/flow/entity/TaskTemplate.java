package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>事件模板</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月16日
 */
@Entity
@Table(name = "tflow_task_template")
public class TaskTemplate extends OpsOrgEntity {

    private static final long serialVersionUID = 4333691971466445087L;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 任务类型
     */
    private TaskType taskType;

    /**
     * 预计完成天数
     */
    private BigDecimal endDays;

    /**
     * 任务级别
     */
    private TaskLevel taskLevel;

    /**
     * 描述
     */
    private String describe;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 获取编号
     *
     * @return
     */
    @Column(name = "FCODE", length = 50, nullable = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE", length = 200)
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     *
     * @param describe
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取任务类型
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTYPE_ID", nullable = true)
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * 设置任务类型
     *
     * @param taskType
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取预计完成天数
     *
     * @return
     */
    @Column(name = "FEND_DAYS")
    public BigDecimal getEndDays() {
        return endDays;
    }

    /**
     * 设置预计完成天数
     *
     * @param endDays
     */
    public void setEndDays(BigDecimal endDays) {
        this.endDays = endDays;
    }

    /**
     * 获取任务级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FLEVEL_ID", nullable = true)
    public TaskLevel getTaskLevel() {
        return taskLevel;
    }

    /**
     * 设置任务级别
     *
     * @param taskLevel
     */
    public void setTaskLevel(TaskLevel taskLevel) {
        this.taskLevel = taskLevel;
    }
}
