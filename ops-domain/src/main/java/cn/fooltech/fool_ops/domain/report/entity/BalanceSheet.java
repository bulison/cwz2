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
@Table(name = "tbd_fiscal_balancesheet")
public class BalanceSheet extends SheetFormula {

    private static final long serialVersionUID = -1652303843913541347L;

    /**
     * 会计期间
     */
    private FiscalPeriod period;

    /**
     * 资产期未余额
     */
    private BigDecimal assetPeriodEnd;

    /**
     * 资产年初余额
     */
    private BigDecimal assetYearBegin;

    /**
     * 负债期未余额
     */
    private BigDecimal debitPeriodEnd;

    /**
     * 负债年初余额
     */
    private BigDecimal debitYearBegin;


    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_PERIOD_ID", nullable = false)
    public FiscalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(FiscalPeriod period) {
        this.period = period;
    }

    @Column(name = "FASSET_PERIOD_END")
    public BigDecimal getAssetPeriodEnd() {
        return assetPeriodEnd;
    }

    public void setAssetPeriodEnd(BigDecimal assetPeriodEnd) {
        this.assetPeriodEnd = assetPeriodEnd;
    }

    @Column(name = "FASSET_YEAR_BEGIN")
    public BigDecimal getAssetYearBegin() {
        return assetYearBegin;
    }

    public void setAssetYearBegin(BigDecimal assetYearBegin) {
        this.assetYearBegin = assetYearBegin;
    }

    @Column(name = "FDEBT_YEAR_BEGIN")
    public BigDecimal getDebitYearBegin() {
        return debitYearBegin;
    }

    public void setDebitYearBegin(BigDecimal debitYearBegin) {
        this.debitYearBegin = debitYearBegin;
    }

    @Column(name = "FDEBT_PERIOD_END")
    public BigDecimal getDebitPeriodEnd() {
        return debitPeriodEnd;
    }

    public void setDebitPeriodEnd(BigDecimal debitPeriodEnd) {
        this.debitPeriodEnd = debitPeriodEnd;
    }
}
