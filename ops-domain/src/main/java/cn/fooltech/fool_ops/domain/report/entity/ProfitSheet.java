package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * 期间资产负债实体类
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_fiscal_profit")
public class ProfitSheet extends ProfitFormula {

    private static final long serialVersionUID = -1652303843913541347L;

    /**
     * 会计期间
     */
    private FiscalPeriod period;

    /**
     * 本期金额
     */
    private BigDecimal currentPeriodAmount;

    /**
     * 上期金额
     */
    private BigDecimal lastPeriodAmount;


    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_PERIOD_ID", nullable = false)
    public FiscalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(FiscalPeriod period) {
        this.period = period;
    }

    @Column(name = "FCURRENT_PERIOD_AMOUNT")
    public BigDecimal getCurrentPeriodAmount() {
        return currentPeriodAmount;
    }

    public void setCurrentPeriodAmount(BigDecimal currentPeriodAmount) {
        this.currentPeriodAmount = currentPeriodAmount;
    }

    @Column(name = "FLAST_PERIOD_AMOUNT")
    public BigDecimal getLastPeriodAmount() {
        return lastPeriodAmount;
    }

    public void setLastPeriodAmount(BigDecimal lastPeriodAmount) {
        this.lastPeriodAmount = lastPeriodAmount;
    }
}
