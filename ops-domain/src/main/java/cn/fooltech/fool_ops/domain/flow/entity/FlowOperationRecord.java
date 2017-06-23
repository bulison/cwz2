package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>流程操作记录</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "TFLOW_TASK_OPERATION")
public class FlowOperationRecord extends OpsOrgEntity {

    /**
     * 业务类型- 计划
     */
    public static final int BUSINESS_TYPE_PLAN = 0;
    /**
     * 业务类型- 事件
     */
    public static final int BUSINESS_TYPE_TASK = 1;
    private static final long serialVersionUID = 8596538763884935220L;
    /**
     * 计划
     */
    private Plan plan;
    /**
     * 事件
     */
    private Task task;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 位置信息
     */
    private String location;
    /**
     * 前置关联事件
     */
    private String frontRelevanceId = "";
    /**
     * 附件
     */
    private Attach attach;
    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 业务场景
     */
    private String businessScene;

    /**
     * 触发动作类型
     */
    private Integer triggerType;

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
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取计划
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_ID")
    public Plan getPlan() {
        return plan;
    }

    /**
     * 设置计划
     *
     * @param plan
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * 获取事件
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTASK_ID")
    public Task getTask() {
        return task;
    }

    /**
     * 设置事件
     *
     * @param task
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * 获取经度
     *
     * @return
     */
    @Column(name = "FLONGITUDE", length = 20)
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return
     */
    @Column(name = "FLATITUDE", length = 20)
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取位置
     *
     * @return
     */
    @Column(name = "FLOCATION", length = 50)
    public String getLocation() {
        return location;
    }

    /**
     * 设置位置
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取前置关联ID
     *
     * @return
     */
    @Column(name = "FRONT_RELEVANCE_ID", length = 500)
    public String getFrontRelevanceId() {
        return frontRelevanceId;
    }

    /**
     * 设置前置关联ID
     *
     * @param frontRelevanceId
     */
    public void setFrontRelevanceId(String frontRelevanceId) {
        this.frontRelevanceId = frontRelevanceId;
    }

    /**
     * 获取附件
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FATTACH_ID")
    public Attach getAttach() {
        return attach;
    }

    /**
     * 设置附件
     *
     * @param attach
     */
    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    /**
     * 获取业务类型
     *
     * @return
     */
    @Column(name = "FBUS_TYPE", nullable = false)
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型
     *
     * @param businessType
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取业务场景
     *
     * @return
     */
    @Column(name = "FBUS_SCENE", length = 50)
    public String getBusinessScene() {
        return businessScene;
    }

    /**
     * 设置业务场景
     *
     * @param businessScene
     */
    public void setBusinessScene(String businessScene) {
        this.businessScene = businessScene;
    }

    /**
     * 获取触发动作类型
     *
     * @return
     */
    @Column(name = "FTRIGGER_TYPE", nullable = false)
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
     * 获取账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
