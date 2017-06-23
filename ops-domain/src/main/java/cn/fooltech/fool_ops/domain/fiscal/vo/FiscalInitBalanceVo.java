package cn.fooltech.fool_ops.domain.fiscal.vo;

import com.google.common.collect.Lists;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>表单传输对象 - 财务-科目初始数据</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-24 10:08:19
 */
public class FiscalInitBalanceVo implements Serializable {

    private static final long serialVersionUID = -6597735583510461623L;

    /**
     * 科目
     */
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer subjectType;
    private String subjectCategory;

    /**
     * 节点标识 1为子节点，0为父节点
     */
    private Short subjectFlag;

    /**
     * 余额方向
     */
    private Integer direction;

    /**
     * 金额
     */
    @Max(value = Integer.MAX_VALUE, message = "金额不能大于{value}")
    private BigDecimal amount;

    /**
     * 单位
     */
    private String unitId;
    private String unitName;
    private String unitCode;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 外币金额
     */
    private BigDecimal currencyAmount;

    /**
     * 币别
     */
    private String currencyId;
    private String currencyName;
    private String currencyCode;

    /**
     * 供应商
     */
    private String supplierId;
    private String supplierName;
    private String supplierCode;

    /**
     * 销售商
     */
    private String customerId;
    private String customerName;
    private String customerCode;

    /**
     * 部门
     */
    private String departmentId;
    private String departmentName;
    private String departmentCode;

    /**
     * 职员
     */
    private String memberId;
    private String memberName;
    private String memberCode;

    /**
     * 仓库
     */
    private String warehouseId;
    private String warehouseName;
    private String warehouseCode;

    /**
     * 项目
     */
    private String projectId;
    private String projectName;
    private String projectCode;

    /**
     * 货品
     */
    private String goodsId;
    private String goodsName;
    private String goodsCode;

    /**
     * 创建人
     */
    private String creatorId;
    private String creatorName;

    /**
     * 账套
     */
    private String fiscalAccountId;
    private String fiscalAccountName;
    private String fiscalAccountCode;

    /**
     * 描述
     */
    private String describe;
    private String createTime;
    private String updateTime;
    private String fid;

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

    /**
     * 是否核算数据
     */
    private Short isCheck;

    /**
     * 是否核算科目
     */
    private Short isCheckSubject;

    private List<FiscalInitBalanceVo> children = Lists.newArrayList();

    public Integer getDirection() {
        return this.direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCurrencyAmount() {
        return this.currencyAmount;
    }

    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
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

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
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

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
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

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
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

    public String getFiscalAccountCode() {
        return fiscalAccountCode;
    }

    public void setFiscalAccountCode(String fiscalAccountCode) {
        this.fiscalAccountCode = fiscalAccountCode;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public List<FiscalInitBalanceVo> getChildren() {
        return children;
    }

    public void setChildren(List<FiscalInitBalanceVo> children) {
        this.children = children;
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

    public String getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(String subjectCategory) {
        this.subjectCategory = subjectCategory;
    }

    public Short getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Short isCheck) {
        this.isCheck = isCheck;
    }

    public Short getIsCheckSubject() {
        return isCheckSubject;
    }

    public void setIsCheckSubject(Short isCheckSubject) {
        this.isCheckSubject = isCheckSubject;
    }

    public Short getSubjectFlag() {
        return subjectFlag;
    }

    public void setSubjectFlag(Short subjectFlag) {
        this.subjectFlag = subjectFlag;
    }
}
