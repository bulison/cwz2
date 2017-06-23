package cn.fooltech.fool_ops.domain.wage.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 工资
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_wage")
public class Wage extends OpsOrgEntity {

    private static final long serialVersionUID = -5080924413238411238L;

    /**
     * 月份
     */
    private Date wageDate;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 审核时间
     */
    private Date auditorTime;

    /**
     * 审核人
     */
    private User auditor;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 工资明细
     */
    private List<WageDetail> details = Lists.newArrayList();
    ;

    @Temporal(TemporalType.DATE)
    @Column(name = "FDATE")
    public Date getWageDate() {
        return wageDate;
    }

    public void setWageDate(Date wageDate) {
        this.wageDate = wageDate;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEP_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

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

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    public Date getAuditorTime() {
        return auditorTime;
    }

    public void setAuditorTime(Date auditorTime) {
        this.auditorTime = auditorTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "wage")
    public List<WageDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WageDetail> details) {
        this.details = details;
    }
}
