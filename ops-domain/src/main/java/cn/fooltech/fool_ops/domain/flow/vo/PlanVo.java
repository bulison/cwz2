package cn.fooltech.fool_ops.domain.flow.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

/**
 * <p>表单传输对象- 计划</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
public class PlanVo implements Serializable {

    private static final long serialVersionUID = -8540654354675807911L;

    /**
     * ID
     */
    private String fid;

    /**
     * 计划类型
     */
    //@NotBlank(message = "计划类型必填")
    private String planType;

    /**
     * 编号
     */
    @NotBlank(message = "计划编号必填")
    @Length(max = 20, message = "计划编号长度超过{max}个字符")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "计划名称必填")
    @Length(max = 20, message = "计划名称长度超过{max}个字符")
    private String name;

    /**
     * 部门ID
     */
//	@NotBlank(message="计划部门必填")
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 修改时间
     */
    private String updateTime;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date actualStartTime;

    /**
     * 实际结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date actualEndTime;

    /**
     * 计划级别ID
     */
    @NotBlank(message = "计划级别必填")
    @Length(max = 32, message = "计划级别长度超过{max}个字符")
    private String planLevelId;

    /**
     * 计划级别名称
     */
    private String planLevelName;

    /**
     * 保密级别ID
     */
    @NotBlank(message = "保密级别必填")
    @Length(max = 32, message = "保密级别长度超过{max}个字符")
    private String securityLevelId;

    /**
     * 保密级别名称
     */
    private String securityLevelName;

    /**
     * 预计收益率
     */
    private String estimatedYieldrate;

    /**
     * 实际收益率
     */
    private String effectiveYieldrate;

    /**
     * 计划金额
     */
    @Length(max = 28, message = "计划金额长度超过{max}位")
    private String estimatedAmount;
    /**
     * 实际发生金额
     */
    @Length(max = 28, message = "计划金额长度超过{max}位")
    private String realAmount;
    
    /**
     *  实际发生金额
     * @return
     */
    public String getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(String realAmount) {
		this.realAmount = realAmount;
	}

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发起人ID
     */
    @NotBlank(message = "发起人必填")
    @Length(max = 32, message = "发起人长度超过{max}个字符")
    private String initiaterId;

    /**
     * 发起人名称
     */
    private String initiaterName;

    /**
     * 责任人ID
     */
    @NotBlank(message = "责任人必填")
    @Length(max = 32, message = "责任人长度超过{max}个字符")
    private String principalerId;

    /**
     * 责任人名称
     */
    private String principalerName;

    /**
     * 审核人ID
     */
    @NotBlank(message = "审核人必填")
    @Length(max = 32, message = "审核人长度超过{max}个字符")
    private String auditerId;

    /**
     * 审核人名称
     */
    private String auditerName;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 附件IDs(多个用逗号隔开)
     */
    private String attachIds;

    /**
     * 原计划完成日期
     */
    private Date originalEndTime;

    /**
     * 即时收益率
     *//*
    private String currentYieldRate;
	
	*//**
     * 市场参考收益率
     *//*
	private String marketYieldRate;
	
	*//**
     * 预计月收益率
     *//*
	private String monthYieldRate;*/

    /**
     * 计划模板ID
     */
    private String planTemplateId;

    /**
     * 全部事件是否已完成<br>
     * 0-否   1-是
     */
    private Integer isAllTaskComplete;

    /**
     * 是否需要发送短信
     */
    private Integer sendPhoneMsg;

    /**
     * 是否需要发送邮件
     */
    private Integer sendEmail;

    /**
     * 是否隐藏：0-隐藏，1-显示
     */
    private Integer hide;
    
    /**
     * 延迟次数
     */
    private Integer delayedTime;
    /**
     * 参考收益率【取当天的资金日损率】
     */
    private BigDecimal referenceYieldrate;
    /**
     * 收入金额
     */
    private BigDecimal inAmount;
    /**
     * 支出金额
     */
    private BigDecimal outAmount;
    
    public String getFid() {
        return fid;
    }

    public Integer getHide() {
		return hide;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

	public void setFid(String fid) {
        this.fid = fid;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getPlanLevelId() {
        return planLevelId;
    }

    public void setPlanLevelId(String planLevelId) {
        this.planLevelId = planLevelId;
    }

    public String getPlanLevelName() {
        return planLevelName;
    }

    public void setPlanLevelName(String planLevelName) {
        this.planLevelName = planLevelName;
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
    /**
     * 获取预计收益率
     * @return
     */
    public String getEstimatedYieldrate() {
        return estimatedYieldrate;
    }
    /**
     * 设置预计收益率
     * @param estimatedYieldrate
     */
    public void setEstimatedYieldrate(String estimatedYieldrate) {
        this.estimatedYieldrate = estimatedYieldrate;
    }
    /**
     * 获取实际收益率
     * @return
     */
    public String getEffectiveYieldrate() {
        return effectiveYieldrate;
    }
    /**
     *  设置实际收益率
     * @param effectiveYieldrate
     */
    public void setEffectiveYieldrate(String effectiveYieldrate) {
        this.effectiveYieldrate = effectiveYieldrate;
    }

    public String getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(String estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getAttachIds() {
        return attachIds;
    }

    public void setAttachIds(String attachIds) {
        this.attachIds = attachIds;
    }

    public Date getOriginalEndTime() {
        return originalEndTime;
    }

    public void setOriginalEndTime(Date originalEndTime) {
        this.originalEndTime = originalEndTime;
    }

	/*public String getCurrentYieldRate() {
		return currentYieldRate;
	}

	public void setCurrentYieldRate(String currentYieldRate) {
		this.currentYieldRate = currentYieldRate;
	}

	public String getMarketYieldRate() {
		return marketYieldRate;
	}

	public void setMarketYieldRate(String marketYieldRate) {
		this.marketYieldRate = marketYieldRate;
	}*/

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanTemplateId() {
        return planTemplateId;
    }

    public void setPlanTemplateId(String planTemplateId) {
        this.planTemplateId = planTemplateId;
    }

    public Integer getIsAllTaskComplete() {
        return isAllTaskComplete;
    }

    public void setIsAllTaskComplete(Integer isAllTaskComplete) {
        this.isAllTaskComplete = isAllTaskComplete;
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

	/*public String getMonthYieldRate() {
		return monthYieldRate;
	}

	public void setMonthYieldRate(String monthYieldRate) {
		this.monthYieldRate = monthYieldRate;
	}*/

    /**
     * 获取延迟次数
     * @return
     */
	public Integer getDelayedTime() {
		return delayedTime;
	}

	public void setDelayedTime(Integer delayedTime) {
		this.delayedTime = delayedTime;
	}
	/**
	 * 获取参考收益率
	 * @return
	 */
	public BigDecimal getReferenceYieldrate() {
		return referenceYieldrate;
	}

	public void setReferenceYieldrate(BigDecimal referenceYieldrate) {
		this.referenceYieldrate = referenceYieldrate;
	}

	/**
	 * 获取收入金额
	 * @return
	 */
	public BigDecimal getInAmount() {
		return inAmount;
	}

	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}

	/**
	 * 获取支出金额
	 * @return
	 */
	public BigDecimal getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
}
