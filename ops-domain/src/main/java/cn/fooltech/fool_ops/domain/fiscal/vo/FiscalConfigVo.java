package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 财务参数设置</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-23 16:28:37
 */
public class FiscalConfigVo implements Serializable {

    private static final long serialVersionUID = -3845680834684337432L;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;

    /**
     * 值
     */
    private String value;

    /**
     * 值转换类型
     * 0--字符；1--布尔；2--数字；3--日期；4--可选值；
     */
    private Integer valueType;

    /**
     * 可选值,用逗号相隔
     */
    private String selectValue;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String updateTime;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 关联的账套ID
     */
    private String fiscalAccountId;

    /**
     * 关联的账套名称
     */
    private String fiscalAccountName;

    private String fid;

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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getValueType() {
        return this.valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getSelectValue() {
        return this.selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
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
}
