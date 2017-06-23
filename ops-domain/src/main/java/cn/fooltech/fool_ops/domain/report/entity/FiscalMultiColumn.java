package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * <p>多栏明细账设置</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
@Entity
@Table(name = "TBD_FISCAL_MULTI_COLUMN_SETTING")
public class FiscalMultiColumn extends OpsOrgEntity {

    private static final long serialVersionUID = -4358119699409190316L;

    /**
     * 科目
     */
    private FiscalAccountingSubject subject;

    /**
     * 名称
     */
    private String name;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

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
     * 明细
     */
    private List<FiscalMultiColumnDetail> details = new LinkedList<FiscalMultiColumnDetail>();

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FFISCAL_SUBJECTID", nullable = false)
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    /**
     * 设置财务账套
     *
     * @param subject
     */
    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", length = 100, nullable = false)
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
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
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
     * 获取设置明细
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "fiscalMultiColumn")
    public List<FiscalMultiColumnDetail> getDetails() {
        return details;
    }

    /**
     * 设置设置明细
     *
     * @param details
     */
    public void setDetails(List<FiscalMultiColumnDetail> details) {
        this.details = details;
    }

}
