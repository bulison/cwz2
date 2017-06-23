package cn.fooltech.fool_ops.domain.warehouse.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 即时分仓库存</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年4月12日
 */
public class StockStoreVo implements Serializable {

    private static final long serialVersionUID = -3580310339209171954L;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 货品ID
     */
    private String goodsId;

    /**
     * 货品编号
     */
    private String goodsCode;

    /**
     * 货品名称
     */
    private String goodsName;

    /**
     * 货品属性ID
     */
    private String goodsSpecId;

    /**
     * 货品属性名称
     */
    private String goodsSpecName;

    /**
     * 结存数量
     */
    private String accountQuentity;

    /**
     * 出库结存数量
     */
    private String checkoutQuentity;

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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
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

    public String getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(String accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public String getCheckoutQuentity() {
        return checkoutQuentity;
    }

    public void setCheckoutQuentity(String checkoutQuentity) {
        this.checkoutQuentity = checkoutQuentity;
    }

}
