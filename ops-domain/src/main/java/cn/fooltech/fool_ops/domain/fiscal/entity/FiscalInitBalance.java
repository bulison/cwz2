package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 财务-科目初始数据
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月17日
 */
@Entity
@Table(name = "tbd_fiscal_init_balance")
public class FiscalInitBalance extends OpsOrgEntity {

    public static final short CHECK = 1;//核算数据
    public static final short UN_CHECK = 0;//非核算数据
    public static final short FILL = 1;//填写数据
    public static final short NOT_FILL = 0;//非填写数据
    private static final long serialVersionUID = -6938228425174001936L;
    /**
     * 科目
     */
    private FiscalAccountingSubject subject;

    /**
     * 余额方向
     */
    private Integer direction;

    /**
     * 金额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 外币币别
     */
    private AuxiliaryAttr currency;

    /**
     * 外币金额
     */
    private BigDecimal currencyAmount = BigDecimal.ZERO;

    /**
     * 供应商
     */
    private Supplier supplier;

    /**
     * 销售商
     */
    private Customer customer;

    /**
     * 部门
     */
    private Organization department;

    /**
     * 职员
     */
    private Member member;

    /**
     * 仓库
     */
    private AuxiliaryAttr warehouse;

    /**
     * 项目
     */
    private AuxiliaryAttr project;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 描述
     */
    private String describe;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 是否核算数据
     */
    private Short isCheck = UN_CHECK;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 是否填写数据 0:计算所得 1:填写所得
     */
    private Short isFill = FILL;

    public FiscalInitBalance() {
        super();
    }

    public FiscalInitBalance(FiscalAccountingSubject subject) {
        super();
        this.subject = subject;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

    /**
     * 获取科目
     */
    @JoinColumn(name = "FFISCAL_SUBJECT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    /**
     * 设置科目
     */
    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
    }

    /**
     * 获取余额方向
     */
    @Column(name = "FDIRECTION")
    public Integer getDirection() {
        return direction;
    }

    /**
     * 设置余额方向
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * 获取金额
     */
    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取单位
     */
    @JoinColumn(name = "FUNIT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置单位
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取数量
     */
    @Column(name = "FQUANTITY")
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取外币币别
     */
    @JoinColumn(name = "FCURRENCY_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public AuxiliaryAttr getCurrency() {
        return currency;
    }

    /**
     * 设置外币币别
     */
    public void setCurrency(AuxiliaryAttr currency) {
        this.currency = currency;
    }

    /**
     * 获取外币金额
     */
    @Column(name = "FCURRENCY_AMOUNT")
    public BigDecimal getCurrencyAmount() {
        return currencyAmount;
    }

    /**
     * 设置外币金额
     */
    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    /**
     * 获取供应商
     */
    @JoinColumn(name = "FSUPPLIER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * 设置供应商
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * 获取销售商
     */
    @JoinColumn(name = "FCUSTOMER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Customer getCustomer() {
        return customer;
    }

    /**
     * 设置销售商
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * 获取部门
     */
    @JoinColumn(name = "FDEPARTMENT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Organization getDepartment() {
        return department;
    }

    /**
     * 设置部门
     */
    public void setDepartment(Organization department) {
        this.department = department;
    }

    /**
     * 获取职员
     */
    @JoinColumn(name = "FMEMBER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Member getMember() {
        return member;
    }

    /**
     * 设置职员
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取仓库
     */
    @JoinColumn(name = "FWAREHOUSE_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public AuxiliaryAttr getWarehouse() {
        return warehouse;
    }

    /**
     * 设置仓库
     */
    public void setWarehouse(AuxiliaryAttr warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * 获取项目
     */
    @JoinColumn(name = "FPROJECT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public AuxiliaryAttr getProject() {
        return project;
    }

    /**
     * 设置项目
     */
    public void setProject(AuxiliaryAttr project) {
        this.project = project;
    }

    /**
     * 获取货品
     */
    @JoinColumn(name = "FGOODS_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置货品
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 获取描述
     */
    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR")
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置账套
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取修改时间戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间戳
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FIS_CHECK")
    public Short getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Short isCheck) {
        this.isCheck = isCheck;
    }

    /**
     * 是否填写数据
     *
     * @return
     */
    @Column(name = "FIS_FILL")
    public Short getIsFill() {
        return isFill;
    }

    public void setIsFill(Short isFill) {
        this.isFill = isFill;
    }
}
