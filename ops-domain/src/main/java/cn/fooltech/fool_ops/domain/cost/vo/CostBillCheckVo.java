package cn.fooltech.fool_ops.domain.cost.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>费用勾对表单VO</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月8日
 */
public class CostBillCheckVo implements Serializable {

    private static final long serialVersionUID = 6425890517450940098L;

    private String fid;
    private String costBillId;//费用单ID
    private String billId;//单据ID
    private String checkDate;//对单日期
    private BigDecimal amount = BigDecimal.ZERO;//对单金额
    private String describe;//描述
    private String updateTime;//修改时间戳

    private BigDecimal incomeAmount = BigDecimal.ZERO;//对单收入金额
    private BigDecimal freeAmount = BigDecimal.ZERO;//对单支出入金额

    private BigDecimal billTotalAmount = BigDecimal.ZERO;//单据开单金额
    private BigDecimal billFreeAmount = BigDecimal.ZERO;//单据免单金额
    private BigDecimal billTotalPayAmount = BigDecimal.ZERO;//单据累计勾对金额

    private String billCode;//单据编号
    private String billDate;//单据日期

    private String csvId;//销售商/供应商
    private String csvName;
    private Integer csvType;

    private Integer billType;//单据类型（也用于搜索过滤） 收款单：51；付款单：52
    /**
     * 状态
     */
    private Integer recordStatus ;
    
    
    public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getCostBillId() {
        return costBillId;
    }

    public void setCostBillId(String costBillId) {
        this.costBillId = costBillId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    public BigDecimal getBillTotalAmount() {
        return billTotalAmount;
    }

    public void setBillTotalAmount(BigDecimal billTotalAmount) {
        this.billTotalAmount = billTotalAmount;
    }

    public BigDecimal getBillFreeAmount() {
        return billFreeAmount;
    }

    public void setBillFreeAmount(BigDecimal billFreeAmount) {
        this.billFreeAmount = billFreeAmount;
    }

    public BigDecimal getBillTotalPayAmount() {
        return billTotalPayAmount;
    }

    public void setBillTotalPayAmount(BigDecimal billTotalPayAmount) {
        this.billTotalPayAmount = billTotalPayAmount;
    }

    public String getCsvId() {
        return csvId;
    }

    public void setCsvId(String csvId) {
        this.csvId = csvId;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public Integer getCsvType() {
        return csvType;
    }

    public void setCsvType(Integer csvType) {
        this.csvType = csvType;
    }
}
