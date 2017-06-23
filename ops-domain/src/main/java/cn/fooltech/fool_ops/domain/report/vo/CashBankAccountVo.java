package cn.fooltech.fool_ops.domain.report.vo;

import cn.fooltech.fool_ops.utils.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class CashBankAccountVo implements Serializable {
    //银行ID
    private String bankId;
    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


    // 日期
    @JSONField(format = "yyyy-MM-dd")
    private Date time;
    // 单号
    private String code;
    // 凭证号
    private String voucherCode;
    // 单据类型
    private Integer billType;
    //单据类型名称
    private String billTypeName;
    // 收入金额
    private String income;
    // 支出金额
    private String expend;
    // 结余金额
    private String balanceAmount;
    // 备注
    private String remark;
    // 摘要
    private String abst;
    // 摘要翻译
    private String abstName;
    //往来单位
    private String contactUnit;

    private String sort;
    private String order;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpend() {
        return expend;
    }

    public void setExpend(String expend) {
        this.expend = expend;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAbstName() {
        return abstName;
    }

    public void setAbstName(String abstName) {
        this.abstName = abstName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getOrderInt() {
        if (StrUtil.isEmpty(this.order)) return 1;
        return StrUtil.isSameA(this.order, "desc".intern()) ? 0 : 1;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    public String getContactUnit() {
        return contactUnit;
    }

    public void setContactUnit(String contactUnit) {
        this.contactUnit = contactUnit;
    }

}
