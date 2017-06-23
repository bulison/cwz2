package cn.fooltech.fool_ops.domain.flow.vo;

import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>表单传输对象 - 计划模板明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:46:07
 */
public class PlanTemplateDetailVo extends FastTreeVo<PlanTemplateDetailVo> implements Serializable {

    private static final long serialVersionUID = 2103431565658774212L;

    private String planTemplateId;//计划模板ID

    private String parentId;//父节点ID

    @NotNull(message = "序号必填")
    private Short number;//序号

    private String billId;//单据ID
    private String billCode;//单据编号
    private Integer billType;//单据类型

    @NotBlank(message = "事件名称必填")
    private String taskName;//事件名称

    @NotBlank(message = "事件级别必填")
    private String taskLevelId;//事件级别ID
    private String taskLevelName;//事件级别名称

    @NotNull(message = "预计完成天数必填")
    private Integer days;//预计完成天数

    private Integer preDays = 0;//前置天数

    private String describe;//描述
    private String deptId;//部门ID
    private String deptName;//部门名称
    private String principalId;//负责人ID
    private String principalName;//负责人名称
    private String undertakerId;//承办人ID
    private String undertakerName;//承办人名称

    /**
     * 保密级别ID
     */
    private String securityLevelId;

    /**
     * 保密级别名称
     */
    private String securityLevelName;
    
    
    /**
     * 金额类型(0-固定值，1-比例值，2-余下金额)
     */
    private Integer amountType;
    
    /**
     * 金额（当金额类型为1时，限制-100至100之间，为2时，变灰，不用填）
     */
    private BigDecimal amount;
    

    public Integer getAmountType() {
		return amountType;
	}

	public void setAmountType(Integer amountType) {
		this.amountType = amountType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	private String fid;

    private List<PlanTemplateDetailVo> children = Lists.newArrayList();

    public Short getNumber() {
        return this.number;
    }

    public void setNumber(Short number) {
        this.number = number;
    }

    public Integer getBillType() {
        return this.billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getDays() {
        return this.days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPlanTemplateId() {
        return planTemplateId;
    }

    public void setPlanTemplateId(String planTemplateId) {
        this.planTemplateId = planTemplateId;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
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

    @Override
    public List<PlanTemplateDetailVo> getChildren() {
        return children;
    }

    public void setChildren(List<PlanTemplateDetailVo> children) {
        this.children = children;
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

    public Integer getPreDays() {
        return preDays;
    }

    public void setPreDays(Integer preDays) {
        this.preDays = preDays;
    }

    public String getId() {
        return fid;
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

    @Override
    public String getText() {
        if (taskName != null || billCode != null) {
            return billCode + " " + taskName;
        } else {
            return taskName;
        }
    }


}
