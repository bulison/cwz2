package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 财务会计期间</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-23 16:46:11
 */
public class FiscalPeriodVo implements Serializable {

    /**
     * 最后一个会计期间标识
     */
    public final static Integer LAST = 1;
    public static final short DEFAULT_SELECT_NO = 0;
    public static final short DEFAULT_SELECT_FIRST = 1;
    public static final short DEFAULT_SELECT_NOT_CHECK = 2;
    private static final long serialVersionUID = 1199716793136788697L;
    /**
     * 会计期间
     */
    @NotBlank(message = "会计期间必填")
    @Length(max = 10, message = "会计期间长度超过{max}个字符")
    private String period;
    /**
     * 开始日期
     */
    @NotBlank(message = "开始日期必填")
    @Length(max = 10, message = "开始日期长度超过{max}个字符")
    private String startDate;
    /**
     * 结束日期
     */
    @NotBlank(message = "结束日期必填")
    @Length(max = 10, message = "结束日期长度超过{max}个字符")
    private String endDate;
    /**
     * 已结账标识
     */
    private Integer checkoutStatus;
    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String description;
    /**
     * 输入天数
     */
    private Integer number;
    /**
     * 账套
     */
    private String fiscalAccountId;
    private String fiscalAccountName;
    private String updateTime;
    private String fid;
    /**
     * 是否为最后
     */
    private Integer isLast = 0;
    /**
     * 是否勾选：用于界面显示
     */
    private Short isChecked = 0;
    /**
     * 默认选择项
     * 0:默认不选择
     * 1：默认选择第一个
     * 2：默认选择第一个未结账的
     */
    private Short defaultSelect = DEFAULT_SELECT_NO;

    /**
     * 已结账标识(仓储会计期间)
     */
    private Integer stockCheckoutStatus;

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCheckoutStatus() {
        return this.checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

    public Short getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Short isChecked) {
        this.isChecked = isChecked;
    }

    public Short getDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(Short defaultSelect) {
        this.defaultSelect = defaultSelect;
    }

    public Integer getStockCheckoutStatus() {
        return stockCheckoutStatus;
    }

    public void setStockCheckoutStatus(Integer stockCheckoutStatus) {
        this.stockCheckoutStatus = stockCheckoutStatus;
    }

}
