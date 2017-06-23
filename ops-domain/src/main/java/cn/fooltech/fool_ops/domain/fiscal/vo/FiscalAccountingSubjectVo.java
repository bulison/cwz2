package cn.fooltech.fool_ops.domain.fiscal.vo;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.List;

/**
 * <p>表单传输对象 - 财务-科目</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-24 08:34:35
 */
public class FiscalAccountingSubjectVo extends FastTreeVo<FiscalAccountingSubjectVo> implements Serializable {

    private static final long serialVersionUID = 3853284616174838710L;

    /**
     * 编号
     */
    @Length(max = 50, message = "编号长度超过{max}个字符")
    private String code;

    /**
     * 名称
     */
    @Length(max = 50, message = "名称长度超过{max}个字符")
    private String name;

    /**
     * 科目类型
     */
    @Max(value = 100, message = "科目类型不能大于{value}")
    private Integer type;

    /**
     * 科目类别
     */
    @Length(max = 32, message = "科目类别长度超过{max}个字符")
    private String subjectId;
    private String subjectName;
    private String subjectCode;

    /**
     * 助记码
     */
    @Length(max = 50, message = "助记码长度超过{max}个字符")
    private String zjm;

    /**
     * 余额方向
     */
    @Max(value = 100, message = "余额方向长度超过{value}")
    private Integer direction;

    /**
     * 币别
     */
    @Length(max = 32, message = "币别长度超过{max}个字符")
    private String currencyId;
    private String currencyName;
    private String currencyCode;

    /**
     * 单位
     */
    @Length(max = 32, message = "单位长度超过{max}个字符")
    private String unitId;
    private String unitName;
    private String unitCode;

    /**
     * 核算现金流量
     */
    private Short cashSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算外币
     */
    private Short currencySign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算往来帐
     */
    private Short cussentAccountSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算供应商
     */
    private Short supplierSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算销售商
     */
    private Short customerSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算部门
     */
    private Short departmentSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算职员
     */
    private Short memberSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算仓库
     */
    private Short warehouseSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算项目
     */
    private Short projectSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算货品
     */
    private Short goodsSign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 核算数量
     */
    private Short quantitySign = FiscalAccountingSubject.UN_ACCOUNT;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;

    /**
     * 父级
     */
    @Length(max = 32, message = "父级长度超过{max}个字符")
    private String parentId;
    private String parentName;
    private String parentCode;

    /**
     * 关联类别 1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品
     */
    private Integer relationType;

    /**
     * 关联ID
     */
    @Length(max = 32, message = "关联ID长度超过{max}个字符")
    private String relationId;
    private String relationName;

    /**
     * 关联编码
     */
    private String relationCode;

    /**
     * 级数
     */
    private Integer level;

    /**
     * 节点标识 1为子节点，0为父节点
     */
    private Short flag;

    /**
     * 创建时间
     */
    private String createTime;

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
     * 修改时间戳
     */
    private String updateTime;

    /**
     * 状态:0--无效,1--有效
     */
    private Short enable = FiscalAccountingSubject.ENABLE_YES;
    private String fid;

    /**
     * 现金科目
     */
    private Short cashSubject;

    /**
     * 银行科目
     */
    private Short bankSubject;

    /**
     * searchType 0:编码或助记码 1：名称 2：科目类型 3:编码或助记码或名称
     * 搜索Keyword
     */
    private Integer searchType;
    private String searchKey;

    /**
     * 科目类型
     */
    private Integer subjectType;

    /**
     * 是否显示跟节点
     */
    private Integer showRoot = Constants.SHOW;

    /**
     * 是否显示无效数据
     */
    private Integer showDisable = Constants.NOTSHOW;

    private List<FiscalAccountingSubjectVo> children = Lists.newArrayList();

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getZjm() {
        return this.zjm;
    }

    public void setZjm(String zjm) {
        this.zjm = zjm;
    }

    public Integer getDirection() {
        return this.direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Short getCashSign() {
        return this.cashSign;
    }

    public void setCashSign(Short cashSign) {
        this.cashSign = cashSign;
    }

    public Short getCurrencySign() {
        return this.currencySign;
    }

    public void setCurrencySign(Short currencySign) {
        this.currencySign = currencySign;
    }

    public Short getCussentAccountSign() {
        return this.cussentAccountSign;
    }

    public void setCussentAccountSign(Short cussentAccountSign) {
        this.cussentAccountSign = cussentAccountSign;
    }

    public Short getSupplierSign() {
        return this.supplierSign;
    }

    public void setSupplierSign(Short supplierSign) {
        this.supplierSign = supplierSign;
    }

    public Short getCustomerSign() {
        return this.customerSign;
    }

    public void setCustomerSign(Short customerSign) {
        this.customerSign = customerSign;
    }

    public Short getDepartmentSign() {
        return this.departmentSign;
    }

    public void setDepartmentSign(Short departmentSign) {
        this.departmentSign = departmentSign;
    }

    public Short getMemberSign() {
        return this.memberSign;
    }

    public void setMemberSign(Short memberSign) {
        this.memberSign = memberSign;
    }

    public Short getWarehouseSign() {
        return this.warehouseSign;
    }

    public void setWarehouseSign(Short warehouseSign) {
        this.warehouseSign = warehouseSign;
    }

    public Short getProjectSign() {
        return this.projectSign;
    }

    public void setProjectSign(Short projectSign) {
        this.projectSign = projectSign;
    }

    public Short getGoodsSign() {
        return this.goodsSign;
    }

    public void setGoodsSign(Short goodsSign) {
        this.goodsSign = goodsSign;
    }

    public Short getQuantitySign() {
        return this.quantitySign;
    }

    public void setQuantitySign(Short quantitySign) {
        this.quantitySign = quantitySign;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getRelationType() {
        return this.relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public String getRelationId() {
        return this.relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Short getFlag() {
        return this.flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
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

    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getFiscalAccountCode() {
        return fiscalAccountCode;
    }

    public void setFiscalAccountCode(String fiscalAccountCode) {
        this.fiscalAccountCode = fiscalAccountCode;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public Short getCashSubject() {
        return cashSubject;
    }

    public void setCashSubject(Short cashSubject) {
        this.cashSubject = cashSubject;
    }

    public Short getBankSubject() {
        return bankSubject;
    }

    public void setBankSubject(Short bankSubject) {
        this.bankSubject = bankSubject;
    }

    @Override
    public List<FiscalAccountingSubjectVo> getChildren() {
        return children;
    }

    public void setChildren(List<FiscalAccountingSubjectVo> children) {
        this.children = children;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public Integer getShowRoot() {
        return showRoot;
    }

    public void setShowRoot(Integer showRoot) {
        this.showRoot = showRoot;
    }

    public Integer getShowDisable() {
        return showDisable;
    }

    public void setShowDisable(Integer showDisable) {
        this.showDisable = showDisable;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String getText() {
        if (name != null || code != null) {
            return code + " " + name;
        } else {
            return name;
        }

    }

    @Override
    public String getId() {
        return fid;
    }
}
