package cn.fooltech.fool_ops.domain.flow.vo;

import java.math.BigDecimal;

/**
 * 费用明细VO
 */
public class TransportFeeDetailVo {
    private String feeId;
    private String feeName;
    private BigDecimal amount;

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
