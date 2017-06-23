package cn.fooltech.fool_ops.domain.bom.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>表单传输对象 - 单据物料</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-04-05 10:09:13
 */
public class BomVo implements Serializable {

    private static final long serialVersionUID = 5302554631882354924L;

    @NotBlank(message = "货品必填")
    private String goodsId;//货品ID
    private String goodsName;//货品名称
    private String goodsCode;//货品编号（搜索时用）
    private String specId;//规格ID
    private String specName;//规格名称
    private String accountUnitId;//记账单位ID
    private String accountUnitName;//记账单位名称
    private BigDecimal accountQuentity;//记账数量
    private Short enable;//是否有效;0--无效; 1--有效;
    private Short fdefault;//是否默认配方;0--否; 1--是;

    @Length(max = 200, message = "描述不能超过200个字符")
    private String describe;//描述
    private String createTime;//创建时间
    private String creatorName;//创建人名称（搜索时用）
    private String creatorId;//创建人ID
    private String fid;
    private String details;//物料明细
    private String unitId;//单位Id
    private String unitName;//单位Name
    private String unitGroupId;//单位组Id
    private String specGroupId;//货品属性组ID
    private String updateTime;//修改时间

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * 获取前台表单传过来的明细集合
     */
    @SuppressWarnings("rawtypes")
    @JsonIgnore
    public List<BomDetailVo> getDetailList() {
        List<BomDetailVo> list = new ArrayList<BomDetailVo>();
        if (StringUtils.isNotBlank(this.details)) {
            JSONArray array = JSONArray.fromObject(this.details);
            List details = (List) JSONArray.toCollection(array, BomDetailVo.class);
            Iterator iterator = details.iterator();
            while (iterator.hasNext()) {
                BomDetailVo detail = (BomDetailVo) iterator.next();
                list.add(detail);
            }
        }
        return list;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUnitGroupId() {
        return unitGroupId;
    }

    public void setUnitGroupId(String unitGroupId) {
        this.unitGroupId = unitGroupId;
    }

    public String getSpecGroupId() {
        return specGroupId;
    }

    public void setSpecGroupId(String specGroupId) {
        this.specGroupId = specGroupId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
