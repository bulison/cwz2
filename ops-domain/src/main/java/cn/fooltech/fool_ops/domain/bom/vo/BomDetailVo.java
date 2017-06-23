package cn.fooltech.fool_ops.domain.bom.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 单据物料明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-05 10:09:13
 */
public class BomDetailVo implements Serializable {

    private static final long serialVersionUID = 1894852677175702222L;

    @NotEmpty(message = "物料必填")
    private String goodsId;//货品ID
    private String goodsName;//货品名称
    private String goodsCode;//货品编号
    private String specId;//规格ID
    private String specName;//规格名称
    private String goodsSpecGroupId;//规格组ID

    @NotEmpty(message = "物料单位必填")
    private String unitId;//单位ID
    private String unitName;//单位名称
    private String unitGroupId;//单位组ID
    private String unitGroupName;//单位组名称

    @NotNull(message = "物料数量必填")
    private BigDecimal quentity;//数量
    private String accountUnitId;//记账单位ID
    private String accountUnitName;//记账单位名称
    private BigDecimal accountQuentity;//记账数量
    private Short enable;//是否有效;0--无效; 1--有效;
    private Short fdefault;//是否默认配方;0--否; 1--是;

    @Length(max = 200, message = "物料描述不能超过200个字符")
    private String describe;//描述
    private String createTime;//创建时间
    private String fid;

    public BigDecimal getAccountQuentity() {
        return this.accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public Short getFdefault() {
        return this.fdefault;
    }

    public void setFdefault(Short fdefault) {
        this.fdefault = fdefault;
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

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getAccountUnitId() {
        return accountUnitId;
    }

    public void setAccountUnitId(String accountUnitId) {
        this.accountUnitId = accountUnitId;
    }

    public String getAccountUnitName() {
        return accountUnitName;
    }

    public void setAccountUnitName(String accountUnitName) {
        this.accountUnitName = accountUnitName;
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

    public BigDecimal getQuentity() {
        return quentity;
    }

    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getUnitGroupId() {
        return unitGroupId;
    }

    public void setUnitGroupId(String unitGroupId) {
        this.unitGroupId = unitGroupId;
    }

    public String getUnitGroupName() {
        return unitGroupName;
    }

    public void setUnitGroupName(String unitGroupName) {
        this.unitGroupName = unitGroupName;
    }

    public String getGoodsSpecGroupId() {
        return goodsSpecGroupId;
    }

    public void setGoodsSpecGroupId(String goodsSpecGroupId) {
        this.goodsSpecGroupId = goodsSpecGroupId;
    }
}
