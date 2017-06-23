package cn.fooltech.fool_ops.domain.period.entity;

import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 会计期间
 *
 * @author lgk
 * @version V1.0
 * @date 2015年9月8日上午9:36:14
 */
@Entity
@Table(name = "tbd_stock_period")
public class StockPeriod extends BasePO implements Serializable {

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
     * 所属企业
     */
    private Organization org;

    /**
     * 主键
     */
    private String fid;

    /**
     * 获取主键
     */
    @Override
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "assigned-id")
    @GenericGenerator(name = "assigned-id", strategy = "assigned")
    public String getFid() {
        return fid;
    }

    /**
     * 设置主键
     */
    @Override
    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 获取所属企业
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    @JsonIgnore
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
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
