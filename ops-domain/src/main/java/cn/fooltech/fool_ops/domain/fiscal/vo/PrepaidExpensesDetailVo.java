package cn.fooltech.fool_ops.domain.fiscal.vo;

import cn.fooltech.fool_ops.config.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>表单传输对象 - 待摊费用明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-14 09:02:47
 */
public class PrepaidExpensesDetailVo implements Serializable {

    private static final long serialVersionUID = -3485787202192135025L;

    private String expensesId;//待摊费用主表ID
    private String expensesCode;//待摊费用编号
    private String expensesName;//待摊费用名称

    private String date;//日期
    private BigDecimal amount;//金额
    private String remark;//备注
    private String createTime;//创建时间

    private String creatorId;//创建人ID
    private String creatorName;//创建人名称

    private String updateTime;//修改时间戳
    private String fid;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate; //开始日期

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate; //结束日期

    private String voucherId; //凭证ID
    private String voucherWordNumber; //凭证字号

    /**
     * 是否展示已生成凭证的数据
     */
    private Integer showGened = Constants.SHOW;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(String expensesId) {
        this.expensesId = expensesId;
    }

    public String getExpensesName() {
        return expensesName;
    }

    public void setExpensesName(String expensesName) {
        this.expensesName = expensesName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getExpensesCode() {
        return expensesCode;
    }

    public void setExpensesCode(String expensesCode) {
        this.expensesCode = expensesCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherWordNumber() {
        return voucherWordNumber;
    }

    public void setVoucherWordNumber(String voucherWordNumber) {
        this.voucherWordNumber = voucherWordNumber;
    }

    public Integer getShowGened() {
        return showGened;
    }

    public void setShowGened(Integer showGened) {
        this.showGened = showGened;
    }
}
