package cn.fooltech.fool_ops.domain.payment.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>表单传输对象 - 收付款单</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-09-24 15:32:59
 */
public class PaymentBillVo implements Serializable {

    private static final long serialVersionUID = 1396123845513314532L;

    /**
     * 单号
     */
    @NotBlank(message = "单号必填")
    @Length(max = 50, message = "单号长度超过{max}个字符")
    private String code;

    /**
     * 凭证（原始单号）
     */
    @Length(max = 50, message = "原始单号长度超过{max}个字符")
    private String vouchercode;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 单据日期
     */
    @NotBlank(message = "单据日期必填")
    @Length(max = 10, message = "单据日期长度超过{max}个字符")
    private String billDate;

    /**
     * 金额
     */
    @NotBlank(message = "金额必填")
    @Length(max = 19, message = "金额长度超过{max}位数")
    private String amount;

    /**
     * 累计勾对金额
     */
    private String totalCheckAmount;

    /**
     * 累计未勾对金额
     */
    private String totalUnCheckAmount;

    /**
     * 描述
     */
    @Length(max = 200, message = "备注长度超过{max}个字符")
    private String describe;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 作废时间
     */
    private String cancelTime;

    /**
     * 生成日记单时间
     */
    private String saveAsBankBillTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间戳
     */
    private String updateTime;

    /**
     * 主键
     */
    private String fid;

    /**
     * 页面搜索关键字- 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    /**
     * 页面搜索关键字- 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 银行ID
     */
    @NotBlank(message = "账号必填")
    @Length(max = 32, message = "账号长度超过{max}个字符")
    private String bankId;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户地址
     */
    private String customerAddress;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商电话
     */
    private String supplierPhone;

    /**
     * 供应商地址
     */
    private String supplierAddress;

    /**
     * 客户供应商合并
     */
    private String csvId;
    private String csvName;
    private Integer csvType;

    /**
     * 负责人ID
     */
    private String memberId;

    /**
     * 负责人名称
     */
    private String memberName;

    /**
     * 会计期间ID
     */
    private String stockPeriodId;

    /**
     * 会计期间名称
     */
    private String stockPeriodName;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 名称
     */
    private String auditorName;

    /**
     * 作废人ID
     */
    private String cancelorId;

    /**
     * 作废人名称
     */
    private String cancelorName;

    /**
     * 生成日记单人ID
     */
    private String saveAsBankBillOperatorId;

    /**
     * 生成日记单人名称
     */
    private String saveAsBankBillOperatorName;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 组织机构ID
     */
    private String orgId;

    /**
     * 状态
     */
    private Integer recordStatus;

    /**
     * 查询字段：code、vouchercode
     */
    private String searchKey;

    /**
     * 受款人id
     */
    private String payeeId;
    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 对公标识
     */
    private Integer toPublic;
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 编号或原始订单
     */
    private String codeOrVoucherCode;
    /**
     * 本期销售
     */
    private BigDecimal sales = new BigDecimal(0);
    /**
     * 返利率%
     */
    private BigDecimal rates = new BigDecimal(0);

    //单据关联事件id（计划关联单据使用）
    private String taskId;
    
   //单据关联事件名称（计划关联单据使用）
    private String taskName;
    //关联事件id过滤查询。等于1才处理
    private Integer taskFilter;
    private short isCheck;
    public Integer getTaskFilter() {
		return taskFilter;
	}

	public void setTaskFilter(Integer taskFilter) {
		this.taskFilter = taskFilter;
	}

	/**
     * 单据关联事件id（计划关联单据使用）
     * @return
     */
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 单据关联事件名称（计划关联单据使用）
	 * @return
	 */
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

    public Integer getToPublic() {
        return toPublic;
    }

    public void setToPublic(Integer toPublic) {
        this.toPublic = toPublic;
    }


    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVouchercode() {
        return this.vouchercode;
    }

    public void setVouchercode(String vouchercode) {
        this.vouchercode = vouchercode;
    }

    public Integer getBillType() {
        return this.billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillDate() {
        return this.billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalCheckAmount() {
        return this.totalCheckAmount;
    }

    public void setTotalCheckAmount(String totalCheckAmount) {
        this.totalCheckAmount = totalCheckAmount;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCancelTime() {
        return this.cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
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

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getStockPeriodId() {
        return stockPeriodId;
    }

    public void setStockPeriodId(String stockPeriodId) {
        this.stockPeriodId = stockPeriodId;
    }

    public String getStockPeriodName() {
        return stockPeriodName;
    }

    public void setStockPeriodName(String stockPeriodName) {
        this.stockPeriodName = stockPeriodName;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getCancelorId() {
        return cancelorId;
    }

    public void setCancelorId(String cancelorId) {
        this.cancelorId = cancelorId;
    }

    public String getCancelorName() {
        return cancelorName;
    }

    public void setCancelorName(String cancelorName) {
        this.cancelorName = cancelorName;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCodeOrVoucherCode() {
        return codeOrVoucherCode;
    }

    public void setCodeOrVoucherCode(String codeOrVoucherCode) {
        this.codeOrVoucherCode = codeOrVoucherCode;
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

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getTotalUnCheckAmount() {
        return totalUnCheckAmount;
    }

    public void setTotalUnCheckAmount(String totalUnCheckAmount) {
        this.totalUnCheckAmount = totalUnCheckAmount;
    }

    public String getSaveAsBankBillOperatorId() {
        return saveAsBankBillOperatorId;
    }

    public void setSaveAsBankBillOperatorId(String saveAsBankBillOperatorId) {
        this.saveAsBankBillOperatorId = saveAsBankBillOperatorId;
    }

    public String getSaveAsBankBillOperatorName() {
        return saveAsBankBillOperatorName;
    }

    public void setSaveAsBankBillOperatorName(String saveAsBankBillOperatorName) {
        this.saveAsBankBillOperatorName = saveAsBankBillOperatorName;
    }

    public String getSaveAsBankBillTime() {
        return saveAsBankBillTime;
    }

    public void setSaveAsBankBillTime(String saveAsBankBillTime) {
        this.saveAsBankBillTime = saveAsBankBillTime;
    }

	public short getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(short isCheck) {
		this.isCheck = isCheck;
	}
    
}
