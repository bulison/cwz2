package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>计划</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "TFLOW_PLAN")
public class Plan extends OpsOrgEntity {

    /**
     * 状态- 草稿
     */
    public static final int STATUS_DRAFT = 100;
    /**
     * 状态- 已提交待审核
     */
    public static final int STATUS_TO_CHECK = 101;
    /**
     * 状态- 已审核办理中
     */
    public static final int STATUS_EXECUTING = 102;
    /**
     * 状态- 已延迟
     */
    public static final int STATUS_DELAYED = 103;
    /**
     * 状态- 已终止
     */
    public static final int STATUS_STOPED = 104;
    /**
     * 状态- 已完成
     */
    public static final int STATUS_FINISHED = 105;
    public static final String TYPE_CGJH = "CGJH";//采购计划
    public static final String TYPE_XSJH = "XSJH";//销售计划
    public static final String TYPE_SCJH = "SCJH";//生产计划
    public static final String TYPE_PDJH = "PDJH";//盘点计划
    public static final String TYPE_BSJH = "BSJH";//报损计划
    public static final String TYPE_CGTH = "CGTH";//采购退货计划
    public static final String TYPE_XSTH = "XSTH";//销售退货计划
    public static final String TYPE_DCJH = "DCJH";//调仓计划
    public static final String TYPE_SCLL = "SCLL";//生产领料计划
    public static final String TYPE_XSFL = "XSFL";//销售返利计划
    public static final String TYPE_CGFL = "CGFL";//采购返利计划
    public static final String TYPE_SCTL = "SCTL";//生产退料计划
    public static final String TYPE_CPTK = "CPTK";//成品退库计划
    public static final String TYPE_BTSW = "BTSW";//普通计划
    private static final long serialVersionUID = -8664521975804606788L;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 部门
     */
    private Organization dept;
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
     * 修改时间
     */
    private Date updateTime;
    /**
     * 计划开始时间
     */
    private Date antipateStartTime;
    /**
     * 计划结束时间
     */
    private Date antipateEndTime;
    /**
     * 实际开始时间
     */
    private Date actualStartTime;
    /**
     * 实际结束时间
     */
    private Date actualEndTime;
    /**
     * 计划级别
     */
    private TaskLevel planLevel;
    /**
     * 保密级别
     */
    private SecurityLevel securityLevel;
    /**
     * 预计收益率
     */
    private BigDecimal estimatedYieldrate;
    /**
     * 实际收益率
     */
    private BigDecimal effectiveYieldrate;
    /**
     * 计划金额
     */
    private BigDecimal estimatedAmount;
    /**
     * 状态
     */
    private Integer status = STATUS_DRAFT;
    /**
     * 发起人
     */
    private User initiater;
    /**
     * 责任人
     */
    private User principaler;
    /**
     * 审核人
     */
    private User auditer;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 附件
     */
    private Attach attach;
    /**
     * 原计划完成日期
     */
    private Date originalEndTime;
    /**
     * 即时收益率
     */
    private BigDecimal currentYieldRate;
    /**
     * 市场参考收益率
     */
    private BigDecimal marketYieldRate;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 计划类型
     */
    private String planType;
    /**
     * 是否需要发送短信
     */
    private Integer sendPhoneMsg;
    /**
     * 是否需要发送邮件
     */
    private Integer sendEmail;
    /**
     * 实际发生金额
     */
    private BigDecimal realAmount = BigDecimal.ZERO;
    
    /**
     * 是否隐藏：0-隐藏，1-显示
     */
    private Integer hide = Constants.SHOW;
    
    /**
     * 延迟次数
     */
    private Integer delayedTime;
    /**
     * 参考收益率【取当天的资金日损率】
     */
    private BigDecimal referenceYieldrate;
    /**
     * 收入金额
     */
    private BigDecimal inAmount;
    /**
     * 支出金额
     */
    private BigDecimal outAmount;
    
    @Column(name = "FHIDE")
    public Integer getHide() {
		return hide;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

	/**
     *  实际发生金额
     * @return
     */
    @Column(name = "FREAL_AMOUNT", length = 50, nullable = false)
    public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

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
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", length = 50, nullable = false)
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
     * 获取计划开始时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FANTIPATE_START_TIME")
    public Date getAntipateStartTime() {
        return antipateStartTime;
    }

    /**
     * 设置计划开始时间
     *
     * @param antipateStartTime
     */
    public void setAntipateStartTime(Date antipateStartTime) {
        this.antipateStartTime = antipateStartTime;
    }

    /**
     * 获取计划结束时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FANTIPATE_END_TIME")
    public Date getAntipateEndTime() {
        return antipateEndTime;
    }

    /**
     * 设置计划结束时间
     *
     * @param antipateEndTime
     */
    public void setAntipateEndTime(Date antipateEndTime) {
        this.antipateEndTime = antipateEndTime;
    }

    /**
     * 获取实际开始时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FACTUAL_START_TIME")
    public Date getActualStartTime() {
        return actualStartTime;
    }

    /**
     * 设置实际开始时间
     *
     * @param actualStartTime
     */
    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    /**
     * 获取实际结束时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FACTUAL_END_TIME")
    public Date getActualEndTime() {
        return actualEndTime;
    }

    /**
     * 设置实际结束时间
     *
     * @param actualEndTime
     */
    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    /**
     * 获取计划级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_LEVEL_ID")
    public TaskLevel getPlanLevel() {
        return planLevel;
    }

    /**
     * 设置计划级别
     *
     * @param planLevel
     */
    public void setPlanLevel(TaskLevel planLevel) {
        this.planLevel = planLevel;
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

    /**
     * 设置保密级别
     *
     * @param securityLevel
     */
    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * 获取预计收益率
     *
     * @return
     */
    @Column(name = "FESTIMATED_YIELDRATE")
    public BigDecimal getEstimatedYieldrate() {
        return estimatedYieldrate;
    }

    /**
     * 设置预计收益率
     *
     * @param estimatedYieldrate
     */
    public void setEstimatedYieldrate(BigDecimal estimatedYieldrate) {
        this.estimatedYieldrate = estimatedYieldrate;
    }

    /**
     * 获取实际收益率
     *
     * @return
     */
    @Column(name = "FEFFECTIVE_YIELDRATE")
    public BigDecimal getEffectiveYieldrate() {
        return effectiveYieldrate;
    }

    /**
     * 设置实际收益率
     *
     * @param effectiveYieldrate
     */
    public void setEffectiveYieldrate(BigDecimal effectiveYieldrate) {
        this.effectiveYieldrate = effectiveYieldrate;
    }

    /**
     * 获取计划金额
     *
     * @return
     */
    @Column(name = "FESTIMATED_AMOUNT")
    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    /**
     * 设置计划金额
     *
     * @param estimatedAmount
     */
    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "FSTATUS", nullable = false)
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取发起人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FINITIATER_ID")
    public User getInitiater() {
        return initiater;
    }

    /**
     * 设置发起人
     *
     * @param initiater
     */
    public void setInitiater(User initiater) {
        this.initiater = initiater;
    }

    /**
     * 获取责任人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPRINCIPAL_ID")
    public User getPrincipaler() {
        return principaler;
    }

    /**
     * 设置责任人
     *
     * @param principaler
     */
    public void setPrincipaler(User principaler) {
        this.principaler = principaler;
    }

    /**
     * 获取审核人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITER_ID")
    public User getAuditer() {
        return auditer;
    }

    /**
     * 设置审核人
     *
     * @param auditer
     */
    public void setAuditer(User auditer) {
        this.auditer = auditer;
    }

    /**
     * 获取审核时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 设置审核时间
     *
     * @param auditTime
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 获取附件
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FATTACH_ID")
    public Attach getAttach() {
        return attach;
    }

    /**
     * 设置附件
     *
     * @param attach
     */
    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    /**
     * 获取原计划完成时间
     *
     * @return
     */
    @Column(name = "FORIGINAL_END_TIME")
    public Date getOriginalEndTime() {
        return originalEndTime;
    }

    /**
     * 设置原计划完成时间
     *
     * @param originalEndTime
     */
    public void setOriginalEndTime(Date originalEndTime) {
        this.originalEndTime = originalEndTime;
    }

    /**
     * 获取即时收益率
     *
     * @return
     */
    @Column(name = "FCURRENT_YIELD_RATE")
    public BigDecimal getCurrentYieldRate() {
        return currentYieldRate;
    }

    /**
     * 设置即时收益率
     *
     * @param currentYieldRate
     */
    public void setCurrentYieldRate(BigDecimal currentYieldRate) {
        this.currentYieldRate = currentYieldRate;
    }

    /**
     * 获取市场收益率
     *
     * @return
     */
    @Column(name = "FMARKET_YIELD_RATE")
    public BigDecimal getMarketYieldRate() {
        return marketYieldRate;
    }

    /**
     * 设置市场收益率
     *
     * @param marketYieldRate
     */
    public void setMarketYieldRate(BigDecimal marketYieldRate) {
        this.marketYieldRate = marketYieldRate;
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
     * 获取计划类型
     *
     * @return
     */
    @Column(name = "FPLAN_TYPE")
    public String getPlanType() {
        return planType;
    }

    /**
     * 设置计划类型
     *
     * @param planType
     */
    public void setPlanType(String planType) {
        this.planType = planType;
    }

    /**
     * 发送短信
     *
     * @return
     */
    @Column(name = "FSEND_PHONE_MSG")
    public Integer getSendPhoneMsg() {
        return sendPhoneMsg;
    }

    public void setSendPhoneMsg(Integer sendPhoneMsg) {
        this.sendPhoneMsg = sendPhoneMsg;
    }

    /**
     * 发送邮件
     *
     * @return
     */
    @Column(name = "FSEND_EMAIL")
    public Integer getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Integer sendEmail) {
        this.sendEmail = sendEmail;
    }
    /**
     * 获取延迟次数
     * @return
     */
    @Column(name = "FDELAYED_TIME")
	public Integer getDelayedTime() {
		return delayedTime;
	}

	public void setDelayedTime(Integer delayedTime) {
		this.delayedTime = delayedTime;
	}
	/**
	 * 获取参考收益率
	 * @return
	 */
	@Column(name = "FREFERENCE_YIELDRATE")
	public BigDecimal getReferenceYieldrate() {
		return referenceYieldrate;
	}

	public void setReferenceYieldrate(BigDecimal referenceYieldrate) {
		this.referenceYieldrate = referenceYieldrate;
	}

	/**
	 * 获取收入金额
	 * @return
	 */
	@Column(name = "FIN_AMOUNT")
	public BigDecimal getInAmount() {
		return inAmount;
	}

	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}

	/**
	 * 获取支出金额
	 * @return
	 */
	@Column(name = "FOUT_AMOUNT")
	public BigDecimal getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
    
}
