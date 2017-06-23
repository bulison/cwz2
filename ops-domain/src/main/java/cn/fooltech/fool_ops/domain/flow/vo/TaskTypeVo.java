package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象- 事件类别</p>
 *
 * @author CYX
 * @version 1.0
 * @date 2016年10月28日
 */
public class TaskTypeVo implements Serializable {

    private static final long serialVersionUID = -5444193842757650219L;

    /**
     * id
     */
    private String fid;

    /**
     * 组织机构ID
     */
    private String orgId;

    /**
     * 编号
     */
    @NotBlank(message = "编号不能为空")
    @Length(max = 50, message = "编号长度超过{max}个字符")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 100, message = "名称长度超过{max}个字符")
    private String name;

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
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 搜索关键字
     */
    private String searchKey;


    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
