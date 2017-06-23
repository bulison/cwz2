package cn.fooltech.fool_ops.domain.rate.vo;

import java.math.BigDecimal;

/**
 * 收益率计算结果
 *
 * @author xjh
 */
public class RateResult {

    private Double realRate;//即时收益率
    private Double expectedRate;//预期收益率
    private Double marketRate;//市场参考收益率
    private Double actualRate;//实际收益率
    private Double currentRate;//当前收益率
    private Double cycleRate;//周期收益率
    private BigDecimal profitRate;//利润率
    private BigDecimal profit;//利润
    public Double getRealRate() {
        return realRate;
    }

    public void setRealRate(Double realRate) {
        this.realRate = realRate;
    }

    public Double getExpectedRate() {
        return expectedRate;
    }

    public void setExpectedRate(Double expectedRate) {
        this.expectedRate = expectedRate;
    }

    public Double getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(Double marketRate) {
        this.marketRate = marketRate;
    }

    public Double getActualRate() {
        return actualRate;
    }

    public void setActualRate(Double actualRate) {
        this.actualRate = actualRate;
    }

    public Double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(Double currentRate) {
        this.currentRate = currentRate;
    }

    public Double getCycleRate() {
        return cycleRate;
    }

    public void setCycleRate(Double cycleRate) {
        this.cycleRate = cycleRate;
    }

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
    
}
