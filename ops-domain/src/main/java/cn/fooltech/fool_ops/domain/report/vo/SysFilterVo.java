package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 数据分析查询条件</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-10-12
 */
public class SysFilterVo implements Serializable {

    private static final long serialVersionUID = -4370055279249656272L;
    private String fid;
    private String tableName;//表名
    private String fieldName;//字段名称
    private String aliasName;//参数别名
    private String displayName;//显示名称
    private Integer inputType;//输入类型
    private String mark;//标识
    private Integer compare;//比较关系
    private String value;//查询值
    private Integer order;//顺序号
    private String sysReportId;//报表ID
    private String sysReportName;//报表名称
    private Boolean need;//是否必填 false:非必填  true：必填
    private Short show;//是否默认显示 0:不显示; 1：显示;

    public String getSysReportId() {
        return sysReportId;
    }

    public void setSysReportId(String sysReportId) {
        this.sysReportId = sysReportId;
    }

    public String getSysReportName() {
        return sysReportName;
    }

    public void setSysReportName(String sysReportName) {
        this.sysReportName = sysReportName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getInputType() {
        return this.inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getCompare() {
        return this.compare;
    }

    public void setCompare(Integer compare) {
        this.compare = compare;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Boolean getNeed() {
        return need;
    }

    public void setNeed(Boolean need) {
        this.need = need;
    }

    public Short getShow() {
        return show;
    }

    public void setShow(Short show) {
        this.show = show;
    }

}
