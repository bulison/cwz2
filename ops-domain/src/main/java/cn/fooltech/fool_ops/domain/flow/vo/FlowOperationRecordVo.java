package cn.fooltech.fool_ops.domain.flow.vo;

import java.util.Date;


/**
 * <p>流程操作记录VO</p>
 *
 * @author cwz
 * @version 1.0
 * @date 2017-4-11
 */
public class FlowOperationRecordVo {

	private String fid;
    /**
     * 计划
     */
    private String planId;
    /**
     * 事件
     */
    private String taskId;
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
    private String attach;
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
    private String creator;

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
    private String fiscalAccount;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFrontRelevanceId() {
		return frontRelevanceId;
	}

	public void setFrontRelevanceId(String frontRelevanceId) {
		this.frontRelevanceId = frontRelevanceId;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getBusinessScene() {
		return businessScene;
	}

	public void setBusinessScene(String businessScene) {
		this.businessScene = businessScene;
	}

	public Integer getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(String fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
    

}
