package cn.fooltech.fool_ops.domain.basedata.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>表单传输对象-单位</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月6日
 */
public class UnitVo implements Serializable {

    private static final long serialVersionUID = 8777660328394257414L;

    /**
     * ID
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
    @Length(max = 20, message = "编号长度超过20个字符")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 20, message = "名称长度超过20个字符")
    private String name;

    /**
     * 换算关系
     */
    @NotBlank(message = "换算关系不能为空")
    @Length(max = 30, message = "换算关系长度超过30个字符")
    private String scale;

    /**
     * 单位标志
     */
    private Integer flag;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过{max}个字符")
    private String describe;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 组
     */
    private String parentId;

    /**
     * 组名称
     */
    private String parentName;

    /**
     * 状态
     */
    private Integer enable;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否为根目录
     */
    private boolean root;

    /**
     * 是否为该组第一个
     */
    private boolean first;

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

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

}

