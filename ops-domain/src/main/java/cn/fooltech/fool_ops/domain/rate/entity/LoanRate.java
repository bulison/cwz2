package cn.fooltech.fool_ops.domain.rate.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>央行贷款利率</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年6月14日
 */
@Entity
@Table(name = "RATE_CENTER_BANK")
public class LoanRate extends OpsOrgEntity {

    private static final long serialVersionUID = 4349426527801926910L;

    /**
     * 日期
     */
    private Date date;

    /**
     * 贷款利率
     */
    private BigDecimal rate;

    /**
     * 增幅
     */
    private BigDecimal increase;

    /**
     * 创建时间
     */
    private Date createTime;

    private User creator;


    private Date updateTime;

    /**
     * 获取日期
     *
     * @return
     */
    @Column(name = "FDATE", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    /**
     * 设置日期
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取利率
     *
     * @return
     */
    @Column(name = "FRATE", nullable = false)
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * 设置利率
     *
     * @param rate
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * 获取增幅
     *
     * @return
     */
    @Column(name = "FINCREASE", nullable = false)
    public BigDecimal getIncrease() {
        return increase;
    }

    /**
     * 设置增幅
     *
     * @param increase
     */

    public void setIncrease(BigDecimal increase) {
        this.increase = increase;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(cascade = {},fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
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
}
