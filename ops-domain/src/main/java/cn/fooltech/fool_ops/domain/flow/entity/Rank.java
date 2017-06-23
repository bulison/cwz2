package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>评分</>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "TFLOW_RANK")
public class Rank extends OpsOrgEntity {


    private static final long serialVersionUID = 6296931005214496768L;

    public static short TYPE_PLAN = 0;//计划
    public static short TYPE_TASK = 1;//事件

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 0：计划；1：事件
     */
    private Short type;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 评分
     */
    private BigDecimal rank;

    /**
     * 评分人
     */
    private User creator;

    /**
     * 评分时间
     */
    private Date createTime;

    /**
     * 评论
     */
    private String comment;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @Column(name = "FTYPE")
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    @Column(name = "FBUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Column(name = "FRANK")
    public BigDecimal getRank() {
        return rank;
    }

    public void setRank(BigDecimal rank) {
        this.rank = rank;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "COMMENT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
