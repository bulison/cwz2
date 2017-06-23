package cn.fooltech.fool_ops.domain.voucher.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>表单传输对象- 凭证制作</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月30日
 */
public class VoucherMakeVo implements Serializable {

    private static final long serialVersionUID = 5997431601314105326L;

    /**
     * 单据ID
     */
    private String fid;

    /**
     * 单据编号
     */
    private String code;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 单据日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    /**
     * 银行账号ID
     */
    private String bankId;

    /**
     * 开始日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    /**
     * 结束日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 往来单位ID(客户、供应商ID)
     */
    private String contactUnitId;

    /**
     * 往来单位名称(客户、供应商名称)
     */
    private String contactUnitName;

    /**
     * 创建日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 金额
     */
    private String amount;

    /**
     * 优惠金额
     */
    private String specialAmount;

    /**
     * 经手人名称
     */
    private String memberName;

    /**
     * 凭证ID
     */
    private String voucherId;

    /**
     * 是否已生成凭证的标识
     * 0 未生成  1 已生成
     */
    private Integer voucherTag;

    /**
     * 凭证号
     */
    private Integer voucherNumber;

    /**
     * 凭证日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    /**
     * 凭证字ID
     */
    private String voucherWordId;

    /**
     * 凭证字名称
     */
    private String voucherWordName;

    /**
     * 凭证字+凭证号
     */
    private String voucherWordNumber;

    /**
     * 附件数
     */
    private Integer accessoryNumber;

    /**
     * 凭证摘要
     */
    private String voucherResume;

    /**
     * 单据ID(多个用逗号隔开)
     */
    private String billIds;

    /**
     * 工资公式ID
     */
    private String wageFormulaId;

    /**
     * 核算工资的月份
     */
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date month;

    /**
     * 借方科目ID
     */
    private String debitSubjectId;

    /**
     * 贷方科目ID
     */
    private String creditSubjectId;

    /**
     * 仓库名称
     */
    private String warehouseStockName;

    /**
     * 模板ID
     */
    private String templateId;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getContactUnitId() {
        return contactUnitId;
    }

    public void setContactUnitId(String contactUnitId) {
        this.contactUnitId = contactUnitId;
    }

    public String getContactUnitName() {
        return contactUnitName;
    }

    public void setContactUnitName(String contactUnitName) {
        this.contactUnitName = contactUnitName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSpecialAmount() {
        return specialAmount;
    }

    public void setSpecialAmount(String specialAmount) {
        this.specialAmount = specialAmount;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(Integer voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Integer getVoucherTag() {
        return voucherTag;
    }

    public void setVoucherTag(Integer voucherTag) {
        this.voucherTag = voucherTag;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherWordId() {
        return voucherWordId;
    }

    public void setVoucherWordId(String voucherWordId) {
        this.voucherWordId = voucherWordId;
    }

    public String getVoucherWordName() {
        return voucherWordName;
    }

    public void setVoucherWordName(String voucherWordName) {
        this.voucherWordName = voucherWordName;
    }

    public String getVoucherWordNumber() {
        return voucherWordNumber;
    }

    public void setVoucherWordNumber(String voucherWordNumber) {
        this.voucherWordNumber = voucherWordNumber;
    }

    public Integer getAccessoryNumber() {
        return accessoryNumber;
    }

    public void setAccessoryNumber(Integer accessoryNumber) {
        this.accessoryNumber = accessoryNumber;
    }

    public String getVoucherResume() {
        return voucherResume;
    }

    public void setVoucherResume(String voucherResume) {
        this.voucherResume = voucherResume;
    }

    public String getBillIds() {
        return billIds;
    }

    public void setBillIds(String billIds) {
        this.billIds = billIds;
    }

    public String getWageFormulaId() {
        return wageFormulaId;
    }

    public void setWageFormulaId(String wageFormulaId) {
        this.wageFormulaId = wageFormulaId;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getDebitSubjectId() {
        return debitSubjectId;
    }

    public void setDebitSubjectId(String debitSubjectId) {
        this.debitSubjectId = debitSubjectId;
    }

    public String getCreditSubjectId() {
        return creditSubjectId;
    }

    public void setCreditSubjectId(String creditSubjectId) {
        this.creditSubjectId = creditSubjectId;
    }

    public String getWarehouseStockName() {
        return warehouseStockName;
    }

    public void setWarehouseStockName(String warehouseStockName) {
        this.warehouseStockName = warehouseStockName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
