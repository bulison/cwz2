package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;


/**
 * 用户查询条件模版内容表
 *
 * @author tjr
 */
@Entity
@Table(name = "rep_user_template_detail")
@SuppressWarnings("serial")
public class UserTemplateDetail extends OpsEntity {
    /**
     * 关联用户查询条件模板
     */
    private UserTemplate template;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 参数别名
     */
    private String aliasName;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * 输入方式
     */
    private Integer inputType;
    /**
     * 标识
     */
    private String mark;
    /**
     * 表表ID
     */
    private SysReport report;
    /**
     * 比较关系
     */
    private Integer compare;
    /**
     * 查询值
     */
    private String value;
    /**
     * 顺序号
     */
    private Integer order;


    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTEMPLATEID", updatable = false)
    public UserTemplate getTemplate() {
        return template;
    }

    public void setTemplate(UserTemplate template) {
        this.template = template;
    }

    @Column(name = "FTABLENAME", length = 50, nullable = false)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Column(name = "FFIELDNAME", length = 50, nullable = false)
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FALIASNAME", length = 50)
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Column(name = "FDISPLAYNAME", length = 50, nullable = false)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column(name = "FINPUTTYPE", nullable = false)
    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    @Column(name = "FMARK")
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FREPORTID", updatable = false)
    public SysReport getReport() {
        return report;
    }

    public void setReport(SysReport report) {
        this.report = report;
    }

    @Column(name = "FCOMPARE")
    public Integer getCompare() {
        return compare;
    }

    public void setCompare(Integer compare) {
        this.compare = compare;
    }

    @Column(name = "FVALUE", length = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "FORDER")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
