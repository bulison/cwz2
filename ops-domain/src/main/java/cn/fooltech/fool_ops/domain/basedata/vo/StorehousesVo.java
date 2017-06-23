package cn.fooltech.fool_ops.domain.basedata.vo;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>表单传输对象- 仓库(辅助属性)</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年3月18日
 */
public class StorehousesVo implements Serializable {

    private static final long serialVersionUID = 1179017510301364345L;

    /**
     * 主键
     */
    protected String fid;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过{max}个字符")
    private String describe;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 数据是否有效
     */
    private Short enable;

    /**
     * 父仓库ID
     */
    private String parentId;

    /**
     * 父仓库名称
     */
    private String parentName;

    /**
     * 1为子节点，0为父节点
     */
    private Short flag;

    /**
     * 系统标识
     */
    private Short systemSign;

    /**
     * 节点层级
     */
    private Integer level;

    public String getFid() {
        return fid;
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

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
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

    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    public Short getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(Short systemSign) {
        this.systemSign = systemSign;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
