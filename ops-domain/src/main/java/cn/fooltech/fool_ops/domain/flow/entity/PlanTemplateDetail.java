package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>计划模板明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月16日
 */
@Entity
@Table(name = "tflow_plan_template_detail")
public class PlanTemplateDetail extends OpsEntity {

    private static final long serialVersionUID = 4333691971466445087L;

    /**
     * 计划模板
     */
    private PlanTemplate planTemplate;

    /**
     * 序号
     */
    private Short number;

    /**
     * 父亲节点
     */
    private PlanTemplateDetail parent;

    /**
     * 单据ID
     */
    private String billId;

    /**
     * 单据编号
     */
    private String billCode;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 事件名称
     */
    private String taskName;

    /**
     * 前置天数
     */
    private Integer preDays = 0;

    /**
     * 预计完成天数
     */
    private Integer days = 0;

    /**
     * 事件级别
     */
    private TaskLevel taskLevel;

    /**
     * 责任部门
     */
    private Organization dept;

    /**
     * 承办人
     */
    private User undertaker;

    /**
     * 责任人
     */
    private User principal;

    /**
     * 描述
     */
    private String describe;

    /**
     * 子节点
     */
    private List<PlanTemplateDetail> children;

    /**
     * 保密级别
     */
    private SecurityLevel securityLevel;
    
    /**
     * 金额类型(0-固定值，1-比例值，2-余下金额)
     */
    private Integer amountType;

    public static final int AMOUNT_TYPE_FIXED = 0;
    public static final int AMOUNT_TYPE_SCALE = 1;
    public static final int AMOUNT_TYPE_REMAIN = 2;

    
    /**
     * 金额（当金额类型为1时，限制-100至100之间，为2时，变灰，不用填）
     */
    private BigDecimal amount;

    @Column(name = "FAMOUNT_TYPE")
    public Integer getAmountType() {
		return amountType;
	}

	public void setAmountType(Integer amountType) {
		this.amountType = amountType;
	}

	@Column(name = "FAMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
     * 获取计划模板
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_TEMPLATE_ID", nullable = false)
    public PlanTemplate getPlanTemplate() {
        return planTemplate;
    }

    /**
     * 设置计划模板
     *
     * @param planTemplate
     */
    public void setPlanTemplate(PlanTemplate planTemplate) {
        this.planTemplate = planTemplate;
    }

    /**
     * 获取序号
     *
     * @return
     */
    @Column(name = "FNUMBER")
    public Short getNumber() {
        return number;
    }

    /**
     * 设置序号
     *
     * @param number
     */
    public void setNumber(Short number) {
        this.number = number;
    }

    /**
     * 获取父亲节点
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID", nullable = false)
    public PlanTemplateDetail getParent() {
        return parent;
    }

    /**
     * 设置父亲节点
     *
     * @param parent
     */
    public void setParent(PlanTemplateDetail parent) {
        this.parent = parent;
    }

    /**
     * 获取单据ID
     *
     * @return
     */
    @Column(name = "FBILL_ID")
    public String getBillId() {
        return billId;
    }

    /**
     * 设置单据ID
     *
     * @param billId
     */
    public void setBillId(String billId) {
        this.billId = billId;
    }

    /**
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE")
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
     * 获取事件名称
     *
     * @return
     */
    @Column(name = "FTASK_NAME")
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置事件名称
     *
     * @param taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取预计完成天数
     *
     * @return
     */
    @Column(name = "FDAYS")
    public Integer getDays() {
        return days;
    }

    /**
     * 设置预计完成天数
     *
     * @param days
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * 获取事件级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTASK_LEVEL_ID", nullable = false)
    public TaskLevel getTaskLevel() {
        return taskLevel;
    }

    /**
     * 设置事件级别
     *
     * @param taskLevel
     */
    public void setTaskLevel(TaskLevel taskLevel) {
        this.taskLevel = taskLevel;
    }

    /**
     * 获取部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID", nullable = false)
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
     * 获取承办人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNDERTAKER_ID")
    public User getUndertaker() {
        return undertaker;
    }

    /**
     * 设置承办人
     *
     * @param undertaker
     */
    public void setUndertaker(User undertaker) {
        this.undertaker = undertaker;
    }

    /**
     * 获取责任人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPRINCIPAL_ID", nullable = false)
    public User getPrincipal() {
        return principal;
    }

    /**
     * 设置责任人
     *
     * @param principal
     */
    public void setPrincipal(User principal) {
        this.principal = principal;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     *
     * @param describe
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public List<PlanTemplateDetail> getChildren() {
        return children;
    }

    public void setChildren(List<PlanTemplateDetail> children) {
        this.children = children;
    }

    /**
     * 获取单据编号
     *
     * @return
     */
    @Column(name = "FBILL_CODE")
    public String getBillCode() {
        return billCode;
    }

    /**
     * 设置单据编号
     *
     * @param billCode
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    /**
     * 获取前置天数
     *
     * @return
     */
    @Column(name = "FPRE_DAYS")
    public Integer getPreDays() {
        return preDays;
    }

    /**
     * 设置前置天数
     *
     * @param preDays
     */
    public void setPreDays(Integer preDays) {
        this.preDays = preDays;
    }

    @Override
    public boolean equals(Object obj) {
        PlanTemplateDetail other = (PlanTemplateDetail) obj;
        return this.fid.equals(other.getFid());
    }

    /**
     * 获取保密级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSECURITY_LEVEL_ID")
    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }
}
