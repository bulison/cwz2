package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>计划模板</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月16日
 */
@Entity
@Table(name = "tflow_plan_template")
public class PlanTemplate extends OpsOrgEntity {

    /**
     * 停用
     */
    public static final short STATUS_STOP = 0;
    /**
     * 启用
     */
    public static final short STATUS_USE = 1;
    private static final long serialVersionUID = 4333691971466445087L;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 预计完成天数
     */
    private BigDecimal days;
    /**
     * 任务级别
     */
    private TaskLevel taskLevel;
    /**
     * 描述
     */
    private String describe;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态:1-启用，0-停用
     */
    private Short status = STATUS_USE;
    /**
     * 明细
     */
    private List<PlanTemplateDetail> details = Lists.newArrayList();

    /**
     * 保密级别
     */
    private SecurityLevel securityLevel;
    
    /**
     * 责任人
     */
    private User principaler;
    
    /**
     * 承办人
     */
    private User undertaker;
    
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * @author lsl
     * @updateTime 2017-03-02
     * 更新时间戳
     */
    private Date updateTime;
    /**
     * 获取编号
     *
     * @return
     */
    @Column(name = "FCODE", length = 50, nullable = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE", length = 200)
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

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
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
     * 获取预计完成天数
     *
     * @return
     */
    @Column(name = "FDAYS")
    public BigDecimal getDays() {
        return days;
    }

    /**
     * 设置预计完成天数
     *
     * @param endDays
     */
    public void setDays(BigDecimal days) {
        this.days = days;
    }

    /**
     * 获取任务级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_LEVEL_ID", nullable = true)
    public TaskLevel getTaskLevel() {
        return taskLevel;
    }

    /**
     * 设置任务级别
     *
     * @param taskLevel
     */
    public void setTaskLevel(TaskLevel taskLevel) {
        this.taskLevel = taskLevel;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "FSTATUS")
    public Short getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 获取明细
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "planTemplate")
    public List<PlanTemplateDetail> getDetails() {
        return details;
    }

    /**
     * 设置明细
     *
     * @param details
     */
    public void setDetails(List<PlanTemplateDetail> details) {
        this.details = details;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * 获取责任人ID
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPRINCIPAL_ID")
	public User getPrincipaler() {
		return principaler;
	}

	public void setPrincipaler(User principaler) {
		this.principaler = principaler;
	}

    /**
     * 获取承办人ID
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNDERTAKER_ID")
	public User getUndertaker() {
		return undertaker;
	}

	public void setUndertaker(User undertaker) {
		this.undertaker = undertaker;
	}

    /**
     * 获取账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * @author lsl
     * @updateTime 2017-03-02
     * 获取更新时间
     * @return
     */
    @Column(name = "FUPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @author lsl
     * @updateTime 2017-03-02
     * 设置更新时间
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
