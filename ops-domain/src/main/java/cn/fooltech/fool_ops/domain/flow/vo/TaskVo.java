package cn.fooltech.fool_ops.domain.flow.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

/**
 * <p>表单传输对象- 事件</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
public class TaskVo implements Serializable {

    public final static int SECURE_SHOW = 1;
    public final static int SECURE_NOTSHOW = 0;
    private static final long serialVersionUID = 6262597315305334018L;
    /**
     * ID
     */
    private String fid;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 父事件ID
     */
    private String parentId;
    /**
     * 父事件名称
     */
    private String parentName;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 序号
     */
    private Integer number;
    /**
     * 描述
     */
    private String describe;
    /**
     * 计划开始时间
     */
    private String antipateStartTime;
    /**
     * 计划结束时间
     */
    private String antipateEndTime;
    /**
     * 实际开始时间
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStartTime;
    /**
     * 实际结束时间
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualEndTime;
    /**
     * 事件级别ID
     */
    private String taskLevelId;
    /**
     * 事件级别Val
     */
    private Integer taskLevelVal;
    /**
     * 事件级别名称
     */
    private String taskLevelName;
    /**
     * 保密级别ID
     */
    private String securityLevelId;
    /**
     * 保密级别名称
     */
    private String securityLevelName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 预计金额
     */
    private String amount;
    /**
     * 实际金额
     */
    private String realAmount;
    /**
     * 发起人ID
     */
    private String initiaterId;
    /**
     * 发起人名称
     */
    private String initiaterName;
    /**
     * 承办人ID
     */
    private String undertakerId;
    /**
     * 承办人名称
     */
    private String undertakerName;
    /**
     * 责任人Id
     */
    private String principalerId;
    /**
     * 责任人名称
     */
    private String principalerName;
    /**
     * 审核人Id
     */
    private String auditerId;
    /**
     * 审核人名称
     */
    private String auditerName;
    /**
     * 审核时间
     */

    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auditTime;
    /**
     * 单据类型
     */
    private Integer billType;
    /**
     * 单据ID
     */
    private String billId;
    /**
     * 单据编号
     */
    private String billCode;
    /**
     * 事件类别ID
     */
    private String taskTypeId;
    /**
     * 事件类别名称
     */
    private String taskTypeName;
    /**
     * 单据引用方式
     */
    private Integer referenceType;
    /**
     * 原计划完成时间
     */

    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date originalEndTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 审核结果<br>
     * 0-审核通过 , 1-拒绝<br>
     */
    private Integer checkResult = 1;
    /***
     * 延迟后的日期
     */

    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date delayedEndTime;
    /**
     * 延迟理由
     */
    private String deplayReason;
    /**
     * 前置关联事件ID
     */
    private String frontRelevanceIds;
    /**
     * 后置关联事件ID
     */
    private String behindRelevanceIds;
    /**
     * 层级数
     */
    private Integer level;
    /**
     * 附件IDs(多个用逗号隔开)
     */
    private String attachIds;
    /**
     * 办理结束的描述
     */
    private String endExcuteDescribe;
    /**
     * 是否选中
     */
    private boolean checked = false;
    /**
     * 事件关联数量
     */
    private Integer relevanceQuantity=0;
    /**
     * 分派标识
     */
    private Integer assignFlag;
    /**
     * 是否有未完成的子节点<br>
     * 0- 无    1- 有<br>
     */
    private Integer hasChilds;
    /**
     * 是否需要发送短信
     */
    private Integer sendPhoneMsg;
    /**
     * 是否需要发送邮件
     */
    private Integer sendEmail;
    /**
     * 用户是否能看到事件具体信息
     * 1：能看到 0：不能看到
     */
    private Integer secureShow = SECURE_SHOW;
    /**
     * 用户是否能添加同级事件
     * 1：能添加 0：不能添加
     */
    private Integer secureAdd = SECURE_SHOW;
    /**
     * 用户是否能添加下级事件
     * 1：能添加 0：不能添加
     */
    private Integer secureAddChild = SECURE_SHOW;

    /**
     * 子事件
     */
    private List<TaskVo> children = Lists.newArrayList();

    private String deptId;//部门ID
    private String deptName;//部门名称

    private String startMin;//最小开始时间
    private String startMax;//最大开始时间
    private String endMin;//最小结束时间
    private String endMax;//最大结束时间
    /**
     * 延迟次数
     */
    private Integer delayedTime;
    
    /**
     * 延期天数
     */
    private String fextensionDays;
    /**
     * 承办效率
     */
    private String contractorsEfficiency;

    
    public String getFextensionDays() {
		return fextensionDays;
	}

	public void setFextensionDays(String fextensionDays) {
		this.fextensionDays = fextensionDays;
	}

	public String getContractorsEfficiency() {
		return contractorsEfficiency;
	}

	public void setContractorsEfficiency(String contractorsEfficiency) {
		this.contractorsEfficiency = contractorsEfficiency;
	}

	public Integer getDelayedTime() {
		return delayedTime;
	}

	public void setDelayedTime(Integer delayedTime) {
		this.delayedTime = delayedTime;
	}

	public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getStartMax() {
        return startMax;
    }

    public void setStartMax(String startMax) {
        this.startMax = startMax;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public String getEndMax() {
        return endMax;
    }

    public void setEndMax(String endMax) {
        this.endMax = endMax;
    }

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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAntipateStartTime() {
        return antipateStartTime;
    }

    public void setAntipateStartTime(String antipateStartTime) {
        this.antipateStartTime = antipateStartTime;
    }

    public String getAntipateEndTime() {
        return antipateEndTime;
    }

    public void setAntipateEndTime(String antipateEndTime) {
        this.antipateEndTime = antipateEndTime;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getTaskLevelId() {
        return taskLevelId;
    }

    public void setTaskLevelId(String taskLevelId) {
        this.taskLevelId = taskLevelId;
    }

    public String getTaskLevelName() {
        return taskLevelName;
    }

    public void setTaskLevelName(String taskLevelName) {
        this.taskLevelName = taskLevelName;
    }

    public String getSecurityLevelId() {
        return securityLevelId;
    }

    public void setSecurityLevelId(String securityLevelId) {
        this.securityLevelId = securityLevelId;
    }

    public String getSecurityLevelName() {
        return securityLevelName;
    }

    public void setSecurityLevelName(String securityLevelName) {
        this.securityLevelName = securityLevelName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInitiaterId() {
        return initiaterId;
    }

    public void setInitiaterId(String initiaterId) {
        this.initiaterId = initiaterId;
    }

    public String getInitiaterName() {
        return initiaterName;
    }

    public void setInitiaterName(String initiaterName) {
        this.initiaterName = initiaterName;
    }

    public String getUndertakerId() {
        return undertakerId;
    }

    public void setUndertakerId(String undertakerId) {
        this.undertakerId = undertakerId;
    }

    public String getUndertakerName() {
        return undertakerName;
    }

    public void setUndertakerName(String undertakerName) {
        this.undertakerName = undertakerName;
    }

    public String getPrincipalerId() {
        return principalerId;
    }

    public void setPrincipalerId(String principalerId) {
        this.principalerId = principalerId;
    }

    public String getPrincipalerName() {
        return principalerName;
    }

    public void setPrincipalerName(String principalerName) {
        this.principalerName = principalerName;
    }

    public String getAuditerId() {
        return auditerId;
    }

    public void setAuditerId(String auditerId) {
        this.auditerId = auditerId;
    }

    public String getAuditerName() {
        return auditerName;
    }

    public void setAuditerName(String auditerName) {
        this.auditerName = auditerName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public Integer getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(Integer referenceType) {
        this.referenceType = referenceType;
    }

    public Date getOriginalEndTime() {
        return originalEndTime;
    }

    public void setOriginalEndTime(Date originalEndTime) {
        this.originalEndTime = originalEndTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public Date getDelayedEndTime() {
        return delayedEndTime;
    }

    public void setDelayedEndTime(Date delayedEndTime) {
        this.delayedEndTime = delayedEndTime;
    }

    public String getDeplayReason() {
        return deplayReason;
    }

    public void setDeplayReason(String deplayReason) {
        this.deplayReason = deplayReason;
    }

    public String getFrontRelevanceIds() {
        return frontRelevanceIds;
    }

    public void setFrontRelevanceIds(String frontRelevanceIds) {
        this.frontRelevanceIds = frontRelevanceIds;
    }

    public String getBehindRelevanceIds() {
        return behindRelevanceIds;
    }

    public void setBehindRelevanceIds(String behindRelevanceIds) {
        this.behindRelevanceIds = behindRelevanceIds;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAttachIds() {
        return attachIds;
    }

    public void setAttachIds(String attachIds) {
        this.attachIds = attachIds;
    }

    public String getEndExcuteDescribe() {
        return endExcuteDescribe;
    }

    public void setEndExcuteDescribe(String endExcuteDescribe) {
        this.endExcuteDescribe = endExcuteDescribe;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getRelevanceQuantity() {
        return relevanceQuantity;
    }

    public void setRelevanceQuantity(Integer relevanceQuantity) {
        this.relevanceQuantity = relevanceQuantity;
    }

    public void setRelevanceQuantity(int relevanceQuantity) {
        this.relevanceQuantity = relevanceQuantity;
    }

    public Integer getAssignFlag() {
        return assignFlag;
    }

    public void setAssignFlag(Integer assignFlag) {
        this.assignFlag = assignFlag;
    }

    public Integer getHasChilds() {
        return hasChilds;
    }

    public void setHasChilds(Integer hasChilds) {
        this.hasChilds = hasChilds;
    }

    public Integer getSendPhoneMsg() {
        return sendPhoneMsg;
    }

    public void setSendPhoneMsg(Integer sendPhoneMsg) {
        this.sendPhoneMsg = sendPhoneMsg;
    }

    public Integer getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Integer sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Integer getTaskLevelVal() {
        return taskLevelVal;
    }

    public void setTaskLevelVal(Integer taskLevelVal) {
        this.taskLevelVal = taskLevelVal;
    }

    public Integer getSecureAdd() {
        return secureAdd;
    }

    public void setSecureAdd(Integer secureAdd) {
        this.secureAdd = secureAdd;
    }

    public Integer getSecureShow() {
        return secureShow;
    }

    public void setSecureShow(Integer secureShow) {
        this.secureShow = secureShow;
    }

    public Integer getSecureAddChild() {
        return secureAddChild;
    }

    public void setSecureAddChild(Integer secureAddChild) {
        this.secureAddChild = secureAddChild;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<TaskVo> getChildren(){
        return children;
    }

    public void setChildren(List<TaskVo> children){
        this.children = children;
    }
}
