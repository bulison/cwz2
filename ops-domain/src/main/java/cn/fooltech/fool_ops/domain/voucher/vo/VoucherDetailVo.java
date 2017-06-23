package cn.fooltech.fool_ops.domain.voucher.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象- 凭证明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
public class VoucherDetailVo implements Serializable {

    private static final long serialVersionUID = 169041825948491747L;

    /**
     * ID
     */
    private String fid;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 凭证ID
     */
    @NotBlank(message = "凭证ID必填")
    @Length(max = 32, message = "凭证字ID超过{max}个字符")
    private String voucherId;

    /**
     * 摘要
     */
    @Length(max = 200, message = "摘要超过{max}个字符")
    private String resume;

    /**
     * 科目ID
     */
    @NotBlank(message = "科目ID必填")
    @Length(max = 32, message = "科目ID超过{max}个字符")
    private String subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 父科目ID
     */
    private String parentSubjectId;

    /**
     * 父科目编码
     */
    private String parentSubjectCode;

    /**
     * 父科目名称
     */
    private String parentSubjectName;

    /**
     * 借方金额
     */
//	@Min(value=0,message="借方金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "借方金额不能大于{value}")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
//	@Min(value=0,message="贷方金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "贷方金额不能大于{value}")
    private BigDecimal creditAmount;

    /**
     * 单位ID
     */
    private String unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 外币币别ID
     */
    private String currencyId;

    /**
     * 外币币别名称
     */
    private String currencyName;

    /**
     * 汇率
     */
    private String exchangeRate;

    /**
     * 外币金额
     */
    private String currencyAmount;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 销售商ID
     */
    private String customerId;

    /**
     * 销售商名称
     */
    private String customerName;

    /**
     * 部门Id
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 职员Id
     */
    private String memberId;

    /**
     * 职员名称
     */
    private String memberName;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 货品ID
     */
    private String goodsId;

    /**
     * 货品名称
     */
    private String goodsName;

    /**
     * 财务账套ID
     */
    private String fiscalAccountId;

    /**
     * 财务账套名称
     */
    private String fiscalAccountName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 核算现金流量
     */
    private Short cashSign;

    /**
     * 核算外币
     */
    private Short currencySign;

    /**
     * 核算往来帐
     */
    private Short cussentAccountSign;

    /**
     * 核算供应商
     */
    private Short supplierSign;

    /**
     * 核算销售商
     */
    private Short customerSign;

    /**
     * 核算部门
     */
    private Short departmentSign;

    /**
     * 核算职员
     */
    private Short memberSign;

    /**
     * 核算仓库
     */
    private Short warehouseSign;

    /**
     * 核算项目
     */
    private Short projectSign;

    /**
     * 核算货品
     */
    private Short goodsSign;

    /**
     * 核算数量
     */
    private Short quantitySign;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getParentSubjectId() {
        return parentSubjectId;
    }

    public void setParentSubjectId(String parentSubjectId) {
        this.parentSubjectId = parentSubjectId;
    }

    public String getParentSubjectCode() {
        return parentSubjectCode;
    }

    public void setParentSubjectCode(String parentSubjectCode) {
        this.parentSubjectCode = parentSubjectCode;
    }

    public String getParentSubjectName() {
        return parentSubjectName;
    }

    public void setParentSubjectName(String parentSubjectName) {
        this.parentSubjectName = parentSubjectName;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(String currencyAmount) {
        this.currencyAmount = currencyAmount;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Short getCashSign() {
        return cashSign;
    }

    public void setCashSign(Short cashSign) {
        this.cashSign = cashSign;
    }

    public Short getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(Short currencySign) {
        this.currencySign = currencySign;
    }

    public Short getCussentAccountSign() {
        return cussentAccountSign;
    }

    public void setCussentAccountSign(Short cussentAccountSign) {
        this.cussentAccountSign = cussentAccountSign;
    }

    public Short getSupplierSign() {
        return supplierSign;
    }

    public void setSupplierSign(Short supplierSign) {
        this.supplierSign = supplierSign;
    }

    public Short getCustomerSign() {
        return customerSign;
    }

    public void setCustomerSign(Short customerSign) {
        this.customerSign = customerSign;
    }

    public Short getDepartmentSign() {
        return departmentSign;
    }

    public void setDepartmentSign(Short departmentSign) {
        this.departmentSign = departmentSign;
    }

    public Short getMemberSign() {
        return memberSign;
    }

    public void setMemberSign(Short memberSign) {
        this.memberSign = memberSign;
    }

    public Short getWarehouseSign() {
        return warehouseSign;
    }

    public void setWarehouseSign(Short warehouseSign) {
        this.warehouseSign = warehouseSign;
    }

    public Short getProjectSign() {
        return projectSign;
    }

    public void setProjectSign(Short projectSign) {
        this.projectSign = projectSign;
    }

    public Short getGoodsSign() {
        return goodsSign;
    }

    public void setGoodsSign(Short goodsSign) {
        this.goodsSign = goodsSign;
    }

    public Short getQuantitySign() {
        return quantitySign;
    }

    public void setQuantitySign(Short quantitySign) {
        this.quantitySign = quantitySign;
    }

}
