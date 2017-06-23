package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>
 * 财务-科目
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月17日
 */
@Entity
@Table(name = "tbd_fiscal_accounting_subject")
public class FiscalAccountingSubject extends OpsOrgEntity {

    public static final short ENABLE_NO = 0;//状态- 无效
    public static final short ENABLE_YES = 1; //状态- 有效
    public static final short BANK_NO = 0;//不是银行科目
    public static final short BANK_YES = 1; //是银行科目
    public static final short CASH_NO = 0;//不是现金科目
    public static final short CASH_YES = 1; //是现金科目
    public static final short FLAG_PARENT = 0;//标识- 父节点
    public static final short FLAG_CHILD = 1;//标识- 子节点
    public static final int DIRECTION_BORROW = 1;//借方
    public static final int DIRECTION_LOAN = -1;//贷方
    public static final short ACCOUNT = 1;//核算
    public static final short UN_ACCOUNT = 0;//不核算
    //资产、负债、共同、所有者权益、成本、损益
    public static final int TYPE_ZC = 1;
    public static final int TYPE_FZ = 2;
    public static final int TYPE_GT = 3;
    public static final int TYPE_SYZ = 4;
    public static final int TYPE_CB = 5;
    public static final int TYPE_SY = 6;
    //1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品、9现金
    public static final int RELATION_BANK = 1;
    public static final int RELATION_SUPPLIER = 2;
    public static final int RELATION_CUSTOMER = 3;
    public static final int RELATION_DEPARTMENT = 4;
    public static final int RELATION_MEMBER = 5;
    public static final int RELATION_WAREHOUSE = 6;
    public static final int RELATION_PROJECT = 7;
    public static final int RELATION_GOODS = 8;
    public static final int RELATION_CASH = 9;
    private static final long serialVersionUID = 8914038335040908895L;
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
     * 科目类别
     */
    private AuxiliaryAttr subject;

    /**
     * 助记码
     */
    private String zjm;

    /**
     * 余额方向
     */
    private Integer direction;

    /**
     * 币别
     */
    private AuxiliaryAttr currency;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 核算现金流量
     */
    private Short cashSign = UN_ACCOUNT;

    /**
     * 核算外币
     */
    private Short currencySign = UN_ACCOUNT;

    /**
     * 核算往来帐
     */
    private Short cussentAccountSign = UN_ACCOUNT;

    /**
     * 核算供应商
     */
    private Short supplierSign = UN_ACCOUNT;

    /**
     * 核算销售商
     */
    private Short customerSign = UN_ACCOUNT;

    /**
     * 核算部门
     */
    private Short departmentSign = UN_ACCOUNT;

    /**
     * 核算职员
     */
    private Short memberSign = UN_ACCOUNT;

    /**
     * 核算仓库
     */
    private Short warehouseSign = UN_ACCOUNT;

    /**
     * 核算项目
     */
    private Short projectSign = UN_ACCOUNT;

    /**
     * 核算货品
     */
    private Short goodsSign = UN_ACCOUNT;

    /**
     * 核算数量
     */
    private Short quantitySign = UN_ACCOUNT;

    /**
     * 描述
     */
    private String describe;

    /**
     * 父级
     */
    private FiscalAccountingSubject parent;

    /**
     * 关联类别 1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品
     */
    private Integer relationType;

    /**
     * 关联ID
     */
    private String relationId;

    /**
     * 级数
     */
    private Integer level;

    /**
     * 节点标识 1为子节点，0为父节点
     */
    private Short flag = FLAG_PARENT;

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
     * 状态:0--无效,1--有效
     */
    private Short enable = ENABLE_YES;

    /**
     * 子科目
     */
    private Set<FiscalAccountingSubject> childs = new HashSet<FiscalAccountingSubject>(0);

    /**
     * 现金科目
     */
    private Short cashSubject;

    /**
     * 银行科目
     */
    private Short bankSubject;

    /**
     * 是否核算科目
     */
    private Short isCheck = UN_ACCOUNT;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 拼音首字母
     */
    private String pinyin;

    /**
     * 五笔首字母
     */
    private String fivepen;

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
     * 获取编码
     */
    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     */
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

    /**
     * 设置名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取科目类型
     */
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    /**
     * 设置科目类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取科目类别
     */
    @JoinColumn(name = "FSUBJECT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getSubject() {
        return subject;
    }

    /**
     * 设置科目类别
     */
    public void setSubject(AuxiliaryAttr subject) {
        this.subject = subject;
    }

    /**
     * 获取助记码
     */
    @Column(name = "FZJM")
    public String getZjm() {
        return zjm;
    }

    /**
     * 设置助记码
     */
    public void setZjm(String zjm) {
        this.zjm = zjm;
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
     * 获取币别
     */
    @JoinColumn(name = "FCURRENCY_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getCurrency() {
        return currency;
    }

    /**
     * 设置币别
     */
    public void setCurrency(AuxiliaryAttr currency) {
        this.currency = currency;
    }

    /**
     * 获取单位
     */
    @JoinColumn(name = "FUNIT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
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
     * 获取核算现金流量
     */
    @Column(name = "FCASH_SIGN")
    public Short getCashSign() {
        return cashSign;
    }

    /**
     * 设置核算现金流量
     */
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

    /**
     * 设置核算外币
     */
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

    /**
     * 设置核算往来帐
     */
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

    /**
     * 设置核算供应商
     */
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

    /**
     * 设置核算销售商
     */
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

    /**
     * 设置核算部门
     */
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

    /**
     * 设置核算职员
     */
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

    /**
     * 设置核算仓库
     */
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

    /**
     * 设置核算项目
     */
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

    /**
     * 设置核算货品
     */
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

    /**
     * 设置核算数量
     */
    public void setQuantitySign(Short quantitySign) {
        this.quantitySign = quantitySign;
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
     * 获取父级
     */
    @JoinColumn(name = "FPARENT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getParent() {
        return parent;
    }

    /**
     * 设置父级
     */
    public void setParent(FiscalAccountingSubject parent) {
        this.parent = parent;
    }

    /**
     * 获取关联类别
     */
    @Column(name = "FRELATION_TYPE")
    public Integer getRelationType() {
        return relationType;
    }

    /**
     * 设置关联类型
     */
    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    /**
     * 获取关联ID
     */
    @Column(name = "FRELATION_ID")
    public String getRelationId() {
        return relationId;
    }

    /**
     * 设置关联ID
     */
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /**
     * 获取级数
     */
    @Column(name = "FLEVEL")
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置级别
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取节点标识
     */
    @Column(name = "FFLAG")
    public Short getFlag() {
        return flag;
    }

    /**
     * 设置节点标识
     */
    public void setFlag(Short flag) {
        this.flag = flag;
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
     * 设置更新时间戳
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取状态
     */
    @Column(name = "FENABLE")
    public Short getEnable() {
        return enable;
    }

    /**
     * 设置状态
     */
    public void setEnable(Short enable) {
        this.enable = enable;
    }

    /**
     * 获取下级科目
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<FiscalAccountingSubject> getChilds() {
        return childs;
    }

    public void setChilds(Set<FiscalAccountingSubject> childs) {
        this.childs = childs;
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

    @Column(name = "FIS_CHECK")
    public Short getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Short isCheck) {
        this.isCheck = isCheck;
    }

    @Column(name = "FPINYIN")
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Column(name = "FFIVEPEN")
    public String getFivepen() {
        return fivepen;
    }

    public void setFivepen(String fivepen) {
        this.fivepen = fivepen;
    }
}
