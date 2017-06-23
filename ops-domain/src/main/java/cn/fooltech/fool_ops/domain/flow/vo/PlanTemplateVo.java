package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 计划模板</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:45:13
 */
public class PlanTemplateVo implements Serializable {

    private static final long serialVersionUID = -2734029699410309294L;

    @NotNull(message = "编号必填")
    @Length(max = 20, message = "编号长度超过{max}个字符")
    private String code;//编号

    @NotNull(message = "名称必填")
    @Length(max = 20, message = "名称长度超过{max}个字符")
    private String name;//名称

    @NotNull(message = "预计完成天数必填")
    @Min(value = 1, message = "预计完成天数不能小于{value}")
    @Max(value = 2000000000, message = "预计完成天数不能大于{value}")
    private BigDecimal days;//预计完成天数

    @NotBlank(message = "任务级别必填")
    @Length(max = 32, message = "任务级别长度超过{max}个字符")
    private String taskLevelId;//任务级别ID
    private String taskLevelName;//任务级别名称
    private String describe;//描述
    private String createTime;//创建时间
    private String creatorName;//创建人
    private Short status;//状态:1-启用，0-停用
    private String fid;

    private String details;//明细
    private String searchKey;//搜索关键字(匹配编号、描述)

    private String deptId;//责任部门
    private String deptName;

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
     * 责任人ID
     */
    @NotBlank(message = "责任人必填")
    @Length(max = 32, message = "责任人长度超过{max}个字符")
    private String principalerId;
    
    private String principalerName;
    
    /**
     * 承办人ID
     */
    @NotBlank(message = "承办人必填")
    @Length(max = 32, message = "承办人长度超过{max}个字符")
    private String undertakerId;
    
    private String undertakerName;

    //从成本分析跳转过来时要填的主表ID或明细表ID
    private String dataId;

    //从成本分析跳转过来时要填的模板类型（1-采购、2-运输、3-销售）
    private Integer templateType;

    /**
     * @author lsl
     * @updateTime 2017-03-02
     * 修改时间戳
     */
    private String updateTime;

    public static final int TEMPLATE_TYPE_PURCHASE = 1;
    public static final int TEMPLATE_TYPE_TRANSPORT = 2;
    public static final int TEMPLATE_TYPE_SALE = 3;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDays() {
        return this.days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Short getStatus() {
        return this.status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    /**
     * updateTime 2017-03-02
     * author lsl
     */
    public String getUpdateTime() {
        return this.updateTime;
    }
    /**
     * updateTime 2017-03-02
     * author lsl
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
