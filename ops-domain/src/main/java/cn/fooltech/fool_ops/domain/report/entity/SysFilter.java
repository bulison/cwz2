package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * <p>数据分析查询条件实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月12日
 */
@Entity
@Table(name = "rep_sys_filter")
public class SysFilter extends OpsEntity {
    public static final int INUT_TYPE_USER = 0;//用户自定义输入
    public static final int INUT_TYPE_CHOOSE = 1;//从代码管理中选择输入
    public static final int INUT_TYPE_SUPPLIER = 2;//从供应商选择输入
    public static final int INUT_TYPE_CUSTOMER = 3;//从销售商选择输入
    public static final int INUT_TYPE_SUPPLIER_CUSTOMER = 4;//从供应商、销售商选择输入
    public static final int INUT_TYPE_MEMBER = 5;//从人员资料选择输入
    public static final int INUT_TYPE_GOODS = 6;//从货品资料选择输入
    public static final int INUT_TYPE_GOODSSPEC = 7;//从货品属性选择输入
    public static final int INUT_TYPE_BANK = 8;//从现金银行选择输入
    public static final int INUT_TYPE_DATE = 9;//从日期控制选择
    public static final int COMPARE_IN = 0;//包含
    public static final int COMPARE_NOT_IN = 1;//排除
    public static final int COMPARE_EQ = 2;//＝
    public static final int COMPARE_NE = 3;//≠
    public static final int COMPARE_GT = 4;//＞
    public static final int COMPARE_GE = 5;//≥
    public static final int COMPARE_LT = 6;//＜
    public static final int COMPARE_LE = 7;//≤
    public static final short NEED = 1;//需要
    public static final short UN_NEED = 0;//不需要
    public static final short SHOW = 1;//需要
    public static final short NOT_SHOW = 0;//不需要
    private static final long serialVersionUID = -2621556228667467678L;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名称
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
     * 标识:当输入方式=1的时候，指示取辅助属性的那一项
     */
    private String mark;

    /**
     * 报表
     */
    private SysReport sysReport;

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

    /**
     * 是否必填 0:非必填  1：必填
     */
    private Short need;

    /**
     * 是否默认显示 0:不显示; 1：显示; null：显示
     */
    private Short show;

    /**
     * 获取表名
     */
    @Column(name = "FTABLENAME")
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取字段名
     */
    @Column(name = "FFIELDNAME")
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置字段名
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获取参数别名
     *
     * @return
     */
    @Column(name = "FALIASNAME", length = 50)
    public String getAliasName() {
        return aliasName;
    }

    /**
     * 设置参数别名
     *
     * @param aliasName
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * 获取显示名称
     */
    @Column(name = "FDISPLAYNAME")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 设置显示名称
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取输入类型
     */
    @Column(name = "FINPUTTYPE")
    public Integer getInputType() {
        return inputType;
    }

    /**
     * 设置输入类型
     */
    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    /**
     * 获取输入类型
     */
    @Column(name = "FMARK")
    public String getMark() {
        return mark;
    }

    /**
     * 设置标识
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * 获取系统报表
     */
    @JoinColumn(name = "FREPORTID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JsonIgnore
    public SysReport getSysReport() {
        return sysReport;
    }

    /**
     * 设置系统报表
     */
    public void setSysReport(SysReport sysReport) {
        this.sysReport = sysReport;
    }

    /**
     * 获取比较关系
     */
    @Column(name = "FCOMPARE")
    public Integer getCompare() {
        return compare;
    }

    /**
     * 设置比较关系
     */
    public void setCompare(Integer compare) {
        this.compare = compare;
    }

    /**
     * 获取查询值
     */
    @Column(name = "FVALUE")
    public String getValue() {
        return value;
    }

    /**
     * 设置查询值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取顺序号
     */
    @Column(name = "FORDER")
    public Integer getOrder() {
        return order;
    }

    /**
     * 设置顺序号
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Column(name = "FNEED")
    public Short getNeed() {
        return need;
    }

    public void setNeed(Short need) {
        this.need = need;
    }

    @Column(name = "FSHOW")
    public Short getShow() {
        return show;
    }

    public void setShow(Short show) {
        this.show = show;
    }
}
