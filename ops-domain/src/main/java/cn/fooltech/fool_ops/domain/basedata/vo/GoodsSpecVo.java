package cn.fooltech.fool_ops.domain.basedata.vo;

import cn.fooltech.fool_ops.config.Constants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象- 货品属性</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月9日
 */
public class GoodsSpecVo implements Serializable {

    private static final long serialVersionUID = 7632908244377737705L;

    /**
     * ID
     */
    private String fid;

    /**
     * 机构ID
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
     * 属性组标
     */
    private Integer flag;

    /**
     * 记录状态
     */
    private String recordStatus;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过{max}个字符")
    private String describe;

    /**
     * 父属性ID
     */
    private String parentId;

    /**
     * 父属性名称
     */
    private String parentName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 模糊搜索关键字
     */
    private String searchKey;

    /**
     * 模糊搜索结果集大小
     */
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;

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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public Integer getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(Integer searchSize) {
        this.searchSize = searchSize;
    }

}
