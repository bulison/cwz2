package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * 用户查询条件模版表
 *
 * @author tjr
 */
@Entity
@Table(name = "rep_user_template")
@SuppressWarnings("serial")
public class UserTemplate extends OpsEntity {
    /**
     * 模板名称
     */
    private String templatename;
    /**
     * 报表ID
     */
    private SysReport report;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 所属企业
     */
    private Organization org;

    public UserTemplate() {
    }

    public UserTemplate(String fid) {
        this.fid = fid;
    }

    @Column(name = "FTEMPLATENAME", length = 50, nullable = false)
    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FREPORTID", updatable = false)
    public SysReport getReport() {
        return report;
    }

    public void setReport(SysReport report) {
        this.report = report;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }
}
