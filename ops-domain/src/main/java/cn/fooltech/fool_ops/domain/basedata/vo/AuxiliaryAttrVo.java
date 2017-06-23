package cn.fooltech.fool_ops.domain.basedata.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>表单传输对象 - 辅助属性</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-09-06 10:14:28
 */
public class AuxiliaryAttrVo implements Serializable {

    private static final long serialVersionUID = 2442195890154310419L;
    private String fid;
    @NotBlank(message = "编号不能为空")
    @Length(max = 50, message = "编号长度超过50个字符")
    private String code;
    @NotBlank(message = "名称不能为空")
    @Length(max = 50, message = "名称长度超过50个字符")
    private String name;

    @Length(max = 200, message = "描述不能超过{max}个字符")
    private String describe;
    private Date createTime;

    private String updateTime;
    private Short enable;

    private String categoryId;
    private String categoryCode;
    private String categoryName;

    private String parentId;
    private String parentName;

    private String first;

    private Short systemSign;

    /**
     * 财务账套ID
     */
    private String fiscalAccountId;

    /**
     * 财务账套名称
     */
    private String fiscalAccountName;

    /**
     * 换算关系
     */
    private BigDecimal scale;

    /**
     * 保存完成后是否返回该节点及子节点的树结构，0：不返回；1：返回
     */
    private Integer treeFlag = TREE_FLAG_SIGLE;

    public final static int TREE_FLAG_SIGLE = 0;
    public final static int TREE_FLAG_MULTI = 1;

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

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

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

    public Short getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(Short systemSign) {
        this.systemSign = systemSign;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public Integer getTreeFlag() {
        return treeFlag;
    }

    public void setTreeFlag(Integer treeFlag) {
        this.treeFlag = treeFlag;
    }
}
