package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 利润</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-02-18 16:27:29
 */
public class ProfitSheetVo implements Serializable {

    private static final long serialVersionUID = 8923546487099277346L;
    private BigDecimal currentPeriodAmount;//本期金额
    private BigDecimal lastPeriodAmount;//上期金额
    private String item;//项目
    private Integer number;//行号
    private String formula;//公式
    private String updateTime;//修改时间戳
    private String fid;
    private String periodId;//会计期间ID

    public BigDecimal getCurrentPeriodAmount() {
        return this.currentPeriodAmount;
    }

    public void setCurrentPeriodAmount(BigDecimal currentPeriodAmount) {
        this.currentPeriodAmount = currentPeriodAmount;
    }

    public BigDecimal getLastPeriodAmount() {
        return this.lastPeriodAmount;
    }

    public void setLastPeriodAmount(BigDecimal lastPeriodAmount) {
        this.lastPeriodAmount = lastPeriodAmount;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }
}
