package cn.fooltech.fool_ops.domain.rate.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售出库单收益率
 */
@Entity
@Table(name = "tsb_sale_out_rate")
public class SaleOutRate extends OpsOrgEntity{

    /**
     * 销售出库单ID
     */
    private String saleOutId;

    /**
     * 预计收益率
     */
    private BigDecimal estimateRate;

    /**
     * 参考收益率
     */
    private BigDecimal marketRate;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    @Column(name = "FSALE_OUT_ID")
    public String getSaleOutId() {
        return saleOutId;
    }

    public void setSaleOutId(String saleOutId) {
        this.saleOutId = saleOutId;
    }

    @Column(name = "FESTIMATE_RATE")
    public BigDecimal getEstimateRate() {
        return estimateRate;
    }

    public void setEstimateRate(BigDecimal estimateRate) {
        this.estimateRate = estimateRate;
    }

    @Column(name = "FMARKET_RATE")
    public BigDecimal getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(BigDecimal marketRate) {
        this.marketRate = marketRate;
    }

    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }
}
