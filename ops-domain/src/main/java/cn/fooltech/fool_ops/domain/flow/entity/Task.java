package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>事件</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "TFLOW_TASK")
public class Task extends OpsOrgEntity {

    /**
     * 分派标识- 未分派
     */
    public static final int ASSIGN_FLAG_NO = 0;
    /**
     * 分派标识- 已分派
     */
    public static final int ASSIGN_FLAG_YES = 1;
    /**
     * 子节点
     */
    public static final int TREE_FLAG_CHILD = 0;
    /**
     * 父节点
     */
    public static final int TREE_FLAG_PARENT = 1;
    /**
     * 状态- 草稿
     */
    public static final int STATUS_DRAFT = 0;
    /**
     * 状态- 办理中
     */
    public static final int STATUS_EXECUTING = 1;
    /**
     * 状态- 已办理待审核
     */
    public static final int STATUS_EXCUTED_CHECK = 2;
    /**
     * 状态- 已完成
     */
    public static final int STATUS_FINISHED = 3;
    /**
     * 状态- 已延迟且未开始办理
     */
    public static final int STATUS_DELAYED_UNSTART = 4;
    /**
     * 状态- 已延迟且未结束办理
     */
    public static final int STATUS_DELAYED_UNFINISH = 5;
    /**
     * 状态- 已终止
     */
    public static final int STATUS_STOPED = 6;
    private static final long serialVersionUID = 4697680567462674657L;
    /**
     * 计划
     */
    private Plan plan;
    /**
     * 父事件
     */
    private Task parent;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 序号
     */
    private Integer number;
    /**
     * 描述
     */
    private String describe;
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
     * 事件级别
     */
    private TaskLevel taskLevel;
    /**
     * 保密级别
     */
    private SecurityLevel securityLevel;
    /**
     * 状态
     */
    private Integer status = STATUS_DRAFT;
    /**
     * 预计金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 实际金额
     */
    private BigDecimal realAmount = BigDecimal.ZERO;
    /**
     * 发起人
     */
    private User initiater;
    /**
     * 承办人
     */
    private User undertaker;
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
     * 单据类型
     */
    private Integer billType;
    /**
     * 单据ID
     */
    private String billId;
    /**
     * 事件类别
     */
    private TaskType taskType;
    /**
     * 部门
     */
    private Organization dept;
    /**
     * 附件
     */
    private Attach attach;
    /**
     * 单据引用方式
     */
    private Integer referenceType;
    /**
     * 延迟后的结束时间
     */
    private Date delayedEndTime;
    /**
     * 原计划完成时间
     */
    private Date originalEndTime;
    /**
     * 层级数
     */
    private Integer level;
    /**
     * 事件关联的数量
     */
    private Integer relevanceQuantity = 0;
    /**
     * 所有父事件ID路径
     */
    private String parentIds;
    /**
     * 分派标识
     */
    private Integer assignFlag = ASSIGN_FLAG_NO;
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
     * 最后一次触发延迟报警的日期
     */
    private Date lastDelayAlarmDate;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 是否需要发送短信
     */
    private Integer sendPhoneMsg;
    /**
     * 是否需要发送邮件
     */
    private Integer sendEmail;
    /**
     * 树节点标识
     */
    private Integer treeFlag = TREE_FLAG_CHILD;
    /**
     * 子事件
     */
    private Set<Task> childs = new HashSet<Task>(0);

    /**
     * 延迟次数
     */
    private Integer delayedTime;
    
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
     * 获取计划
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_ID", nullable = false)
    public Plan getPlan() {
        return plan;
    }

    /**
     * 设置计划
     *
     * @param plan
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * 获取父事件
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID")
    public Task getParent() {
        return parent;
    }

    /**
     * 设置父事件
     *
     * @param parent
     */
    public void setParent(Task parent) {
        this.parent = parent;
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
     * 获取序号
     *
     * @return
     */
    @Column(name = "FNUMBER", nullable = false)
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置序号
     *
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
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
     * 获取计划开始时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FANTIPATE_START_TIME", nullable = false)
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
    @Column(name = "FANTIPATE_END_TIME", nullable = false)
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
     * 获取事件级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTASK_LEVEL_ID")
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
     * 获取事件关联数量
     *
     * @return
     */
    @Column(name = "FRELEVANCE_QUANTITY")
    public Integer getRelevanceQuantity() {
        return relevanceQuantity;
    }

    /**
     * 设置事件关联数量
     *
     * @param relevanceQuantity
     */
    public void setRelevanceQuantity(Integer relevanceQuantity) {
        this.relevanceQuantity = relevanceQuantity;
    }

    /**
     * 获取所有父事件ID路径
     *
     * @return
     */
    @Column(name = "FPARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }

    /**
     * 设置所有父事件ID路径
     *
     * @param parentIds
     */
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    /**
     * 获取分派标识
     *
     * @return
     */
    @Column(name = "FASSIGN_FLAG")
    public Integer getAssignFlag() {
        return assignFlag;
    }

    /**
     * 设置分派标识
     *
     * @param assignFlag
     */
    public void setAssignFlag(Integer assignFlag) {
        this.assignFlag = assignFlag;
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
     * 获取金额
     *
     * @return
     */
    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * 获取单据ID
     *
     * @return
     */
    @Column(name = "FBILL_ID", length = 32)
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
     * 获取事件级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTASK_TYPE_ID")
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * 设置事件级别
     *
     * @param taskType
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
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
     * 获取引用类型
     *
     * @return
     */
    @Column(name = "FREFERENCE_TYPE")
    public Integer getReferenceType() {
        return referenceType;
    }

    /**
     * 设置引用类型
     *
     * @param referenceType
     */
    public void setReferenceType(Integer referenceType) {
        this.referenceType = referenceType;
    }

    /**
     * 获取延迟后的结束时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FDELAYED_END_TIME")
    public Date getDelayedEndTime() {
        return delayedEndTime;
    }

    /**
     * 设置延迟后的结束时间
     *
     * @param delayedEndTime
     */
    public void setDelayedEndTime(Date delayedEndTime) {
        this.delayedEndTime = delayedEndTime;
    }

    /**
     * 获取原计划完成时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
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
     * 获取层级数
     *
     * @return
     */
    @Column(name = "FLEVEL", nullable = false)
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置层级数
     *
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * 获取最后一次触发延迟报警的日期
     *
     * @return
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FLAST_DELAY_ALARM_DATE")
    public Date getLastDelayAlarmDate() {
        return lastDelayAlarmDate;
    }

    /**
     * 设置最后一次触发延迟报警的日期
     *
     * @param lastDelayAlarmDate
     */
    public void setLastDelayAlarmDate(Date lastDelayAlarmDate) {
        this.lastDelayAlarmDate = lastDelayAlarmDate;
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
     * 获取子事件
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<Task> getChilds() {
        return childs;
    }

    /**
     * 设置子事件
     *
     * @param childs
     */
    public void setChilds(Set<Task> childs) {
        this.childs = childs;
    }

    @Override
    public boolean equals(Object obj) {
        Task other = (Task) obj;
        return this.fid.equals(other.getFid());
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
     * 获取节点标识
     *
     * @return
     */
    @Column(name = "FTREE_FLAG")
    public Integer getTreeFlag() {
        return treeFlag;
    }

    public void setTreeFlag(Integer treeFlag) {
        this.treeFlag = treeFlag;
    }

    /**
     * 计划实际发生金额
     *
     * @return
     */
    @Column(name = "FREAL_AMOUNT")
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

}
