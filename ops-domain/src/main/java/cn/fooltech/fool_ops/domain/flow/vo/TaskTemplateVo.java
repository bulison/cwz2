package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 事件模板</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-16 17:08:33
 */
public class TaskTemplateVo implements Serializable {

    private static final long serialVersionUID = 4566752630880070294L;

    @NotBlank(message = "编号必填")
    @Length(max = 50, message = "编号长度超过{max}个字符")
    private String code;//编号

    @NotBlank(message = "名称必填")
    @Length(max = 50, message = "名称长度超过{max}个字符")
    private String name;//名称

    @NotNull(message = "预计完成天数必填")
    @Min(value = 1, message = "预计完成天数不能小于{value}")
    @Max(value = 2000000000, message = "预计完成天数不能大于{value}")
    private BigDecimal endDays;//预计完成天数
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;//描述
    private String createTime;//创建时间
    private String updateTime;//修改时间戳
    private String fid;

    @NotBlank(message = "任务类型必填")
    @Length(max = 32, message = "任务类型长度超过{max}个字符")
    private String taskTypeId;//任务类型
    private String taskTypeName;

    @NotBlank(message = "任务级别必填")
    @Length(max = 32, message = "任务级别长度超过{max}个字符")
    private String taskLevelId;//任务级别
    private String taskLevelName;

    private String creatorName;//创建人名称
    private String searchKey;//搜索关键字(匹配编号、名称)

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getEndDays() {
        return this.endDays;
    }

    public void setEndDays(BigDecimal endDays) {
        this.endDays = endDays;
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

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
