package cn.fooltech.fool_ops.domain.basedata.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 货品定价</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-09-12
 */
public class GoodsPriceVo implements Serializable {

    private static final long serialVersionUID = 7047212574030614571L;

    private String fid;
    //	@NotBlank(message="货品必填")
    private String goodsId;//货品ID
    private String goodsName;//货品名称
    private String goodsCode;//货品编码
    //	@NotBlank(message="货品属性必填")
    private String goodsSpecId;//货品属性Id
    private String goodsSpecName;//货品属性名称
    private String goodsSpecCode;//货品属性编码

    private String spec;//规格
    private String barCode;//条形码

    private String unitId;//单位
    private String unitName;//单位
    private String unitCode;//单位编码
    private String searchKey;//搜索关键字（名称，编码）

    @NotNull(message = "销售价必填")
    private BigDecimal salePrice;//售价

    @NotNull(message = "最低价必填")
    private BigDecimal lowestPrice;//最低价

    @NotNull(message = "会员价必填")
    private BigDecimal vipPrice;//会员价
    private BigDecimal lowestStock;//最低库存
    private BigDecimal heightestStock;//最高库存

    private String createTime;//创建时间
    private String updateTime;//修改时间戳

    @NotNull(message = "销售一级价下限必填")
    private BigDecimal salesLowerLimit1;//销售一级价下限

    @NotNull(message = "销售二级价下限必填")
    private BigDecimal salesLowerLimit2;//销售二级价下限

    @NotNull(message = "销售一级价上限必填")
    private BigDecimal salesUpperLimit1;//销售一级价上限

    @NotNull(message = "销售二级价上限必填")
    private BigDecimal salesUpperLimit2;//销售二级价上限

    @NotNull(message = "采购一级价下限必填")
    private BigDecimal purchaseLowerLimit1;//采购一级价下限

    @NotNull(message = "采购二级价下限必填")
    private BigDecimal purchaseLowerLimit2;//采购二级价下限

    @NotNull(message = "采购一级价上限必填")
    private BigDecimal purchaseUpperLimit1;//采购一级价上限

    @NotNull(message = "采购二级价上限必填")
    private BigDecimal purchaseUpperLimit2;//采购二级价上限
    private BigDecimal purchasingCycle;//增加采购周期（天）
    private BigDecimal capacity;//生产产能（计量数量/天）


    public BigDecimal getSalesLowerLimit1() {
        return salesLowerLimit1;
    }

    public void setSalesLowerLimit1(BigDecimal salesLowerLimit1) {
        this.salesLowerLimit1 = salesLowerLimit1;
    }

    public BigDecimal getSalesLowerLimit2() {
        return salesLowerLimit2;
    }

    public void setSalesLowerLimit2(BigDecimal salesLowerLimit2) {
        this.salesLowerLimit2 = salesLowerLimit2;
    }

    public BigDecimal getSalesUpperLimit1() {
        return salesUpperLimit1;
    }

    public void setSalesUpperLimit1(BigDecimal salesUpperLimit1) {
        this.salesUpperLimit1 = salesUpperLimit1;
    }

    public BigDecimal getSalesUpperLimit2() {
        return salesUpperLimit2;
    }

    public void setSalesUpperLimit2(BigDecimal salesUpperLimit2) {
        this.salesUpperLimit2 = salesUpperLimit2;
    }

    public BigDecimal getPurchaseLowerLimit1() {
        return purchaseLowerLimit1;
    }

    public void setPurchaseLowerLimit1(BigDecimal purchaseLowerLimit1) {
        this.purchaseLowerLimit1 = purchaseLowerLimit1;
    }

    public BigDecimal getPurchaseLowerLimit2() {
        return purchaseLowerLimit2;
    }

    public void setPurchaseLowerLimit2(BigDecimal purchaseLowerLimit2) {
        this.purchaseLowerLimit2 = purchaseLowerLimit2;
    }

    public BigDecimal getPurchaseUpperLimit1() {
        return purchaseUpperLimit1;
    }

    public void setPurchaseUpperLimit1(BigDecimal purchaseUpperLimit1) {
        this.purchaseUpperLimit1 = purchaseUpperLimit1;
    }

    public BigDecimal getPurchaseUpperLimit2() {
        return purchaseUpperLimit2;
    }

    public void setPurchaseUpperLimit2(BigDecimal purchaseUpperLimit2) {
        this.purchaseUpperLimit2 = purchaseUpperLimit2;
    }

    public BigDecimal getPurchasingCycle() {
        return purchasingCycle;
    }

    public void setPurchasingCycle(BigDecimal purchasingCycle) {
        this.purchasingCycle = purchasingCycle;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
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

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getGoodsSpecName() {
        return goodsSpecName;
    }

    public void setGoodsSpecName(String goodsSpecName) {
        this.goodsSpecName = goodsSpecName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(BigDecimal vipPrice) {
        this.vipPrice = vipPrice;
    }

    public BigDecimal getLowestStock() {
        return lowestStock;
    }

    public void setLowestStock(BigDecimal lowestStock) {
        this.lowestStock = lowestStock;
    }

    public BigDecimal getHeightestStock() {
        return heightestStock;
    }

    public void setHeightestStock(BigDecimal heightestStock) {
        this.heightestStock = heightestStock;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsSpecCode() {
        return goodsSpecCode;
    }

    public void setGoodsSpecCode(String goodsSpecCode) {
        this.goodsSpecCode = goodsSpecCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
