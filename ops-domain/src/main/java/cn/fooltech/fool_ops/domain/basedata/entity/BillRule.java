package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>单据单号生成规则</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月14日
 */
@Entity
@Table(name = "TSB_BILL_RULE")
public class BillRule extends OpsOrgEntity {

    /**
     * 规则类型- 系统自动生成
     */
    public static final int RULE_TYPE_SYS = 0;
    /**
     * 规则类型- 用户手动输入
     */
    public static final int RULE_TYPE_USER = 1;
    /**
     * 年月日-默认格式
     */
    public static final String DATE_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_YYMMDD = "yyyyMMdd";
    public static final String DATE_YYYYMM = "yyyyMMdd";
    public static final String DATE_YYMM = "yyyyMMdd";
    private static final long serialVersionUID = 4305513185525614729L;
    /**
     * 单据名称
     */
    private String billName;
    /**
     * 单据类型
     */
    private Integer billType;
    /**
     * 规则类型
     */
    private Integer ruleType = RULE_TYPE_SYS;
    /**
     * 前缀
     */
    private String prefix = "";
    /**
     * 年月日
     */
    private String date;

    /**
     * 序号长度
     */
    private Integer serial = 4;

    /**
     * 现时单号(序号)
     */
    private String lazyCode = "0001";

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取单据名称
     *
     * @return
     */
    @Column(name = "FBILL_NAME", nullable = false, length = 50)
    public String getBillName() {
        return billName;
    }

    /**
     * 设置单据名称
     *
     * @param billName
     */
    public void setBillName(String billName) {
        this.billName = billName;
    }

    /**
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE", nullable = false)
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置单据类型
     *
     * @param billType
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取规则类型
     *
     * @return
     */
    @Column(name = "FRULE_TYPE", nullable = false)
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * 设置规则类型
     *
     * @param ruleType
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * 获取前缀
     *
     * @return
     */
    @Column(name = "FPREFIX", nullable = false, length = 6)
    public String getPrefix() {
        return prefix;
    }

    /**
     * 设置前缀
     *
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取年月日
     *
     * @return
     */
    @Column(name = "FDATE", length = 10)
    public String getDate() {
        return date;
    }

    /**
     * 设置年月日
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 获取序号长度
     *
     * @return
     */
    @Column(name = "FSERIAL")
    public Integer getSerial() {
        return serial;
    }

    /**
     * 设置序号长度
     *
     * @param serial
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * 获取现时单号
     *
     * @return
     */
    @Column(name = "FLAZY_CODE", nullable = false, length = 26)
    public String getLazyCode() {
        return lazyCode;
    }

    /**
     * 设置现时单号
     *
     * @param lazyCode
     */
    public void setLazyCode(String lazyCode) {
        this.lazyCode = lazyCode;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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

}
