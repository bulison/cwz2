package cn.fooltech.fool_ops.domain.period.vo;

import java.io.Serializable;

/**
 * <p>网页表单传输对象-仓储会计期</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年3月25日
 */
public class StockPeriodVo implements Serializable {

    private static final long serialVersionUID = 8966300255663572765L;

    private String fid;

    /**
     * 会计期间
     */
    private String period;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 已结账标识
     */
    private Integer checkoutStatus;

    /**
     * 描述
     */
    private String description;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
