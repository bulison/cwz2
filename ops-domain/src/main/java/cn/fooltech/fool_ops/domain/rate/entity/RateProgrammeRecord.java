package cn.fooltech.fool_ops.domain.rate.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 收益率计算的每一条交易记录
 *
 * @author xjh
 */
@Entity
@Table(name = "rate_programme_record")
public class RateProgrammeRecord extends OpsEntity {

    public static final short TYPE_INCOME = 0;//收入
    public static final short TYPE_OUTCOME = 1;//支出
    public static final short STATUS_COMPLETE = 2;//完成
    public static final short STATUS_PROGRESS = 1;//进行中
    public static final short STATUS_WAIT = 0;//未开始
    private static final long serialVersionUID = -3634400382086793447L;
    /**
     * 预计交易时间
     */
    private Date tradeDate;

    /**
     * 实际交易时间
     */
    private Date realDate;

    /**
     * 交易状态
     */
    private Short tradeStatus = STATUS_PROGRESS;

    /**
     * 交易类型
     */
    private Short type;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 收益率计算方案
     */
    private RateProgramme rateProgramme;

    @Column(name = "FTYPE")
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FTRADE_DATE")
    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    @Column(name = "FTRADE_STATUS")
    public Short getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRATE_PROGRAMME_ID", nullable = false)
    public RateProgramme getRateProgramme() {
        return rateProgramme;
    }

    public void setRateProgramme(RateProgramme rateProgramme) {
        this.rateProgramme = rateProgramme;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FREAL_DATE")
    public Date getRealDate() {
        return realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }
}
