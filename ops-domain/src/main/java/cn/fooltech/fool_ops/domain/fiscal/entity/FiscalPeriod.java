package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>财务会计期间</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月17日
 */
@Entity
@Table(name = "TBD_FISCAL_PERIOD")
public class FiscalPeriod extends OpsOrgEntity {

    public static final int UN_USED = -1;//未启用
    public static final int USED = 0;//已启用
    public static final int CHECKED = 1;//已结账
    private static final long serialVersionUID = 4739334065661866284L;
    /**
     * 会计期间
     */
    private String period;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 已结账标识
     */
    private Integer checkoutStatus = UN_USED;
    /**
     * 描述
     */
    private String description;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 创建人
     */
    private User creator;

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
     * 获取会计期间
     */
    @Column(name = "FPERIOD")
    public String getPeriod() {
        return period;
    }

    /**
     * 设计会计期间
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * 获取开始日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FSTART_DATE")
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FEND_DATE")
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取状态
     */
    @Column(name = "FCHECKOUT_STATUS")
    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    /**
     * 设置状态
     */
    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    /**
     * 获取描述
     */
    @Column(name = "FDESCRIPTION")
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     */
    public void setDescription(String description) {
        this.description = description;
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
}
