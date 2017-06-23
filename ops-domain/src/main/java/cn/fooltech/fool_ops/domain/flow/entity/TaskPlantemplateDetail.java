package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *计划事件关联模板表从表
 */
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tflow_task_plantemplate_detail")
public class TaskPlantemplateDetail {

    private static final long serialVersionUID = 1L;


    //主键
    private String id;


    //主表ID
    private TaskPlantemplate bill;


    //计划ID
    private Plan plan;


    //事件ID
    private Task task;


    //计划模板
    private PlanTemplate planTemplate;


    //计划模板明细ID
    private PlanTemplateDetail planTemplateDetail;


    //状态 （事件状态）
    private Integer status;


    //创建时间
    private Date createTime;


    //创建人
    private User creator;


    //修改时间戳 初始值为当前时间
    private Date updateTime;


    //组织ID 机构ID
    private Organization org;


    //账套ID
    private FiscalAccount fiscalAccount;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FID", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JoinColumn(name = "FBILL_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public TaskPlantemplate getBill() {
        return bill;
    }

    public void setBill(TaskPlantemplate bill) {
        this.bill = bill;
    }

    @JoinColumn(name = "FPLAN_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @JoinColumn(name = "FTASK_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @JoinColumn(name = "FPLAN_TEMPLATE_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public PlanTemplate getPlanTemplate() {
        return planTemplate;
    }

    public void setPlanTemplate(PlanTemplate planTemplate) {
        this.planTemplate = planTemplate;
    }

    @JoinColumn(name = "FPLAN_TEMPLATE_DETAIL_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public PlanTemplateDetail getPlanTemplateDetail() {
        return planTemplateDetail;
    }

    public void setPlanTemplateDetail(PlanTemplateDetail planTemplateDetail) {
        this.planTemplateDetail = planTemplateDetail;
    }

    @Column(name = "FSTATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JoinColumn(name = "FCREATOR_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JoinColumn(name = "FORG_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    @JoinColumn(name = "FACC_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }
}