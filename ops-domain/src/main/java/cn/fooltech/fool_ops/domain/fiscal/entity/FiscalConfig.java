package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>财务参数设置</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月17日
 */
@Entity
@Table(name = "TBD_FISCAL_CFG")
public class FiscalConfig extends OpsOrgEntity {

    //摘要默认上一条
    public static final String F01 = "F01";
    //上期未结账，本期可以审核
    public static final String F02 = "F02";
    //凭证号是否区分凭证字
    public static final String F03 = "F03";
    //不允许手工修改凭证号
    public static final String F05 = "F05";
    //凭证的创建人和审核人是同一个人的情况下，是否允许审核
    public static final String F06 = "F06";
    //否
    public static final String FALSE = "0";
    //是
    public static final String TRUE = "1";
    private static final long serialVersionUID = 3731335099910070182L;
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
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

    /**
     * 获取编码
     */
    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     */
    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     */
    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置名称
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取值
     */
    @Column(name = "FVALUE")
    public String getValue() {
        return value;
    }

    /**
     * 设置值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取值类型
     */
    @Column(name = "FVALUETYPE")
    public Integer getValueType() {
        return valueType;
    }

    /**
     * 设置值类型
     */
    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    /**
     * 获取可选值
     */
    @Column(name = "FSELECTVALUE")
    public String getSelectValue() {
        return selectValue;
    }

    /**
     * 设置可选值
     */
    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR")
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置账套
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取修改时间戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间戳
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
