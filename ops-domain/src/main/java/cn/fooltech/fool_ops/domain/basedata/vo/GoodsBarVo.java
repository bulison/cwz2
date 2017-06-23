package cn.fooltech.fool_ops.domain.basedata.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 货品条码</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-09-12 15:21:20
 */
public class GoodsBarVo implements Serializable {

    private static final long serialVersionUID = -7709946748002331745L;
    private String fid;//主键

    @NotBlank(message = "货品不能为空")
    private String goodsId;//货品ID
    private String goodsName;//货品名称
    private String goodsCode;//货品编号

    private String goodsSpecId;//属性ID
    private String goodsSpecName;//属性名称

    private String goodsSpecGroupId;//属性组ID
    private String goodsSpecGroupName;//属性组名称

    @NotBlank(message = "单位不能为空")
    private String unitId;//单位ID
    private String unitName;//单位名称

    private String unitGroupId;//单位组Id
    private String unitGroupName;//单位组名称

    @NotBlank(message = "条码不能为空")
    @Length(max = 50, message = "条码长度不超过{max}个字符")
    private String barCode;//条码

    private String createTime;//创建时间
    private String updateTime;//修改时间戳

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getGoodsSpecGroupId() {
        return goodsSpecGroupId;
    }

    public void setGoodsSpecGroupId(String goodsSpecGroupId) {
        this.goodsSpecGroupId = goodsSpecGroupId;
    }

    public String getUnitGroupId() {
        return unitGroupId;
    }

    public void setUnitGroupId(String unitGroupId) {
        this.unitGroupId = unitGroupId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitGroupName() {
        return unitGroupName;
    }

    public void setUnitGroupName(String unitGroupName) {
        this.unitGroupName = unitGroupName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSpecName() {
        return goodsSpecName;
    }

    public void setGoodsSpecName(String goodsSpecName) {
        this.goodsSpecName = goodsSpecName;
    }

    public String getGoodsSpecGroupName() {
        return goodsSpecGroupName;
    }

    public void setGoodsSpecGroupName(String goodsSpecGroupName) {
        this.goodsSpecGroupName = goodsSpecGroupName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}
