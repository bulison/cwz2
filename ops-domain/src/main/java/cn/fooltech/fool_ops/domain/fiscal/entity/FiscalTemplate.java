package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;


/**
 * <p>财务科目模板</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月26日
 */
@Entity
@Table(name = "TBD_FISCAL_TEMPLATE")
public class FiscalTemplate extends OpsEntity {

    private static final long serialVersionUID = 8453333324416930114L;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 科目类型
     */
    private Integer type;

    /**
     * 辅助属性名称
     */
    private String attr;

    /**
     * 辅助属性编号
     */
    private String attrCode;

    /**
     * 余额方向
     */
    private Integer direction;

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
     * 类型
     */
    private FiscalTemplateType templateType;

    /**
     * 现金科目
     */
    private Short cashSubject;

    /**
     * 银行科目
     */
    private Short bankSubject;

    /**
     * 获取编码
     */
    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     */
    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FATTR")
    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    @Column(name = "FATTR_CODE")
    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    /**
     * 获取余额方向
     */
    @Column(name = "FDIRECTION")
    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * 获取核算现金流量
     */
    @Column(name = "FCASH_SIGN")
    public Short getCashSign() {
        return cashSign;
    }

    public void setCashSign(Short cashSign) {
        this.cashSign = cashSign;
    }

    /**
     * 获取核算外币
     */
    @Column(name = "FCURRENCY_SIGN")
    public Short getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(Short currencySign) {
        this.currencySign = currencySign;
    }

    /**
     * 获取核算往来帐
     */
    @Column(name = "FCUSSENT_ACCOUNT_SIGN")
    public Short getCussentAccountSign() {
        return cussentAccountSign;
    }

    public void setCussentAccountSign(Short cussentAccountSign) {
        this.cussentAccountSign = cussentAccountSign;
    }

    /**
     * 获取核算供应商
     */
    @Column(name = "FSUPPLIER_SIGN")
    public Short getSupplierSign() {
        return supplierSign;
    }

    public void setSupplierSign(Short supplierSign) {
        this.supplierSign = supplierSign;
    }

    /**
     * 获取核算销售商
     */
    @Column(name = "FCUSTOMER_SIGN")
    public Short getCustomerSign() {
        return customerSign;
    }

    public void setCustomerSign(Short customerSign) {
        this.customerSign = customerSign;
    }

    /**
     * 获取核算部门
     */
    @Column(name = "FDEPARTMENT_SIGN")
    public Short getDepartmentSign() {
        return departmentSign;
    }

    public void setDepartmentSign(Short departmentSign) {
        this.departmentSign = departmentSign;
    }

    /**
     * 获取核算职员
     */
    @Column(name = "FMEMBER_SIGN")
    public Short getMemberSign() {
        return memberSign;
    }

    public void setMemberSign(Short memberSign) {
        this.memberSign = memberSign;
    }

    /**
     * 获取核算仓库
     */
    @Column(name = "FWAREHOUSE_SIGN")
    public Short getWarehouseSign() {
        return warehouseSign;
    }

    public void setWarehouseSign(Short warehouseSign) {
        this.warehouseSign = warehouseSign;
    }

    /**
     * 获取核算项目
     */
    @Column(name = "FPROJECT_SIGN")
    public Short getProjectSign() {
        return projectSign;
    }

    public void setProjectSign(Short projectSign) {
        this.projectSign = projectSign;
    }

    /**
     * 获取核算货品
     */
    @Column(name = "FGOODS_SIGN")
    public Short getGoodsSign() {
        return goodsSign;
    }

    public void setGoodsSign(Short goodsSign) {
        this.goodsSign = goodsSign;
    }

    /**
     * 获取核算数量
     */
    @Column(name = "FQUANTITY_SIGN")
    public Short getQuantitySign() {
        return quantitySign;
    }

    public void setQuantitySign(Short quantitySign) {
        this.quantitySign = quantitySign;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTEMPLATE_TYPE_ID")
    public FiscalTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(FiscalTemplateType templateType) {
        this.templateType = templateType;
    }

    /**
     * 获取科目类型
     */
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "FCASH_SUBJECT")
    public Short getCashSubject() {
        return cashSubject;
    }

    public void setCashSubject(Short cashSubject) {
        this.cashSubject = cashSubject;
    }

    @Column(name = "FBANK_SUBJECT")
    public Short getBankSubject() {
        return bankSubject;
    }

    public void setBankSubject(Short bankSubject) {
        this.bankSubject = bankSubject;
    }
}
