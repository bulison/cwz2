package cn.fooltech.fool_ops.domain.warehouse.vo;

import cn.fooltech.fool_ops.validator.bill.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.fooltech.fool_ops.validator.bill.Bsd;
import cn.fooltech.fool_ops.validator.bill.Cgdd;
import cn.fooltech.fool_ops.validator.bill.Cgfp;
import cn.fooltech.fool_ops.validator.bill.Cgrk;
import cn.fooltech.fool_ops.validator.bill.Cprk;
import cn.fooltech.fool_ops.validator.bill.Cptk;
import cn.fooltech.fool_ops.validator.bill.Dcd;
import cn.fooltech.fool_ops.validator.bill.Pdd;
import cn.fooltech.fool_ops.validator.bill.Qckc;
import cn.fooltech.fool_ops.validator.bill.Scjhd;
import cn.fooltech.fool_ops.validator.bill.Scll;
import cn.fooltech.fool_ops.validator.bill.Sctl;
import cn.fooltech.fool_ops.validator.bill.Xsch;
import cn.fooltech.fool_ops.validator.bill.Xsdd;
import cn.fooltech.fool_ops.validator.bill.Xsfp;
import cn.fooltech.fool_ops.validator.bill.Xsth;
/**
 * <p>表单传输对象- 仓库单据记录明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public class WarehouseBillDetailVo implements Serializable {

    private static final long serialVersionUID = 3032473960354236837L;

    /**
     * ID
     */
    private String fid;

    /**
     * 单据ID
     */
    private String billId;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 单据单号
     */
    private String billCode;

    /**
     * 货品ID
     */
    @NotEmpty(message = "货品必填")
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
     * 货品属性
     */
    private String goodsSpecId;

    /**
     * 货品属性编号
     */
    private String goodsSpecCode;

    /**
     * 货品属性名称
     */
    private String goodsSpecName;

    /**
     * 货品属性组ID
     */
    private String goodsSpecGroupId;

    /**
     * 货品属性组名称
     */
    private String goodsSpecGroupName;

    /**
     * 货品规格
     */
    private String goodsSpec;

    /**
     * 货品单位ID
     */
    @NotEmpty(message = "货品单位必填")
    private String unitId;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 货品单位名称
     */
    private String unitName;

    /**
     * 货品单位组ID
     */
    private String unitGroupId;

    /**
     * 货品单位组名称
     */
    private String unitGroupName;
    //提成点数
    private BigDecimal percentage;
    //提成金额
    private BigDecimal percentageAmount;
    /**
     * 货品数量
     */
    @NotEmpty(message = "货品数量必填", groups = {Bsd.class, Cgdd.class, Cgfp.class, Cgrk.class,
            Cprk.class, Cptk.class, Dcd.class, Pdd.class, Qckc.class, Scjhd.class, Scll.class,
            Sctl.class, Xsch.class, Xsdd.class, Xsfp.class, Xsth.class})
    private String quentity;

    /**
     * 成本价
     */
    @NotEmpty(message = "成本价必填", groups = {Xsth.class})
    private String costPrice;

    /**
     * 单价
     */
    @NotEmpty(message = "单价必填")
    @Min(value = 0, message = "单价不能小于0")
    private String unitPrice;

    /**
     * 记账金额
     */
    @NotEmpty(message = "金额必填", groups = {Bsd.class, Cgdd.class, Cgfp.class, Cgrk.class,
            Cprk.class, Cptk.class, Dcd.class, Pdd.class, Qckc.class, Scll.class,
            Sctl.class, Xsch.class, Xsdd.class, Xsfp.class, Xsth.class})
    private String type;

    /**
     * 单位换算
     */
    private String scale;

    /**
     * 记账数量
     */
    private String accountQuentity;

    /**
     * 记账单位ID
     */
    private String accountUintID;

    /**
     * 记账单位名称
     */
    private String accountUintName;

    /**
     * 记账单价
     */
    private String accountUintPrice;

    /**
     * 进仓仓库ID
     */
    @NotEmpty(message = "仓库必填", groups = {Bsd.class, Cgrk.class, Cprk.class, Cptk.class,
            Qckc.class, Scll.class, Sctl.class, Xsch.class, Xsth.class})
    private String inWareHouseId;

    /**
     * 进仓仓库名称
     */
    private String inWareHouseName;

    /**
     * 进仓仓库编码
     */
    private String inWareHouseCode;

    /**
     * 出仓仓库ID
     */
    private String outWareHouseId;

    /**
     * 出仓仓库名称
     */
    private String outWareHouseName;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过200个字符")
    private String describe;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 货品条码
     */
    private String barCode;

    /**
     * 货品的最低销售价
     */
    private String lowestPrice;

    /**
     * 重量
     */
    private String weight;
    /**
     * 货品类型
     */
    private Integer detailType;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税金
     */
    private BigDecimal taxAmount;

    /**
     * 税后金额
     */
    private BigDecimal totalAmount;

    /**
     * 引用的单据明细ID
     */
    private String refDetailId;

    //实收数量
    private BigDecimal receivedQuantity;


    //亏损数量
    private BigDecimal loseQuantity;


    //亏损金额
    private BigDecimal loseAmount;


    //发货成本单价
    private BigDecimal deliveryCostPrice;


    //发货成本金额
    private BigDecimal deliveryCostAmount;


    //成本金额
    private BigDecimal costAmount;


    //运输单位ID
    private String transportUnitId;
    private String transportUnitName;


    //运输数量
    private BigDecimal transportQuentity;


    //运输单价
    private BigDecimal transportPrice;


    //运输金额
    private BigDecimal transportAmount;


    //扣费金额
    private BigDecimal deductionAmount;


    //运输单位换算
    private BigDecimal transprotScale;


    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
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

    public String getGoodsSpecGroupId() {
        return goodsSpecGroupId;
    }

    public void setGoodsSpecGroupId(String goodsSpecGroupId) {
        this.goodsSpecGroupId = goodsSpecGroupId;
    }

    public String getGoodsSpecGroupName() {
        return goodsSpecGroupName;
    }

    public void setGoodsSpecGroupName(String goodsSpecGroupName) {
        this.goodsSpecGroupName = goodsSpecGroupName;
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

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(String accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    public String getAccountUintID() {
        return accountUintID;
    }

    public void setAccountUintID(String accountUintID) {
        this.accountUintID = accountUintID;
    }

    public String getAccountUintName() {
        return accountUintName;
    }

    public void setAccountUintName(String accountUintName) {
        this.accountUintName = accountUintName;
    }

    public String getAccountUintPrice() {
        return accountUintPrice;
    }

    public void setAccountUintPrice(String accountUintPrice) {
        this.accountUintPrice = accountUintPrice;
    }

    public String getInWareHouseId() {
        return inWareHouseId;
    }

    public void setInWareHouseId(String inWareHouseId) {
        this.inWareHouseId = inWareHouseId;
    }

    public String getInWareHouseName() {
        return inWareHouseName;
    }

    public void setInWareHouseName(String inWareHouseName) {
        this.inWareHouseName = inWareHouseName;
    }

    public String getOutWareHouseId() {
        return outWareHouseId;
    }

    public void setOutWareHouseId(String outWareHouseId) {
        this.outWareHouseId = outWareHouseId;
    }

    public String getOutWareHouseName() {
        return outWareHouseName;
    }

    public void setOutWareHouseName(String outWareHouseName) {
        this.outWareHouseName = outWareHouseName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getInWareHouseCode() {
        return inWareHouseCode;
    }

    public void setInWareHouseCode(String inWareHouseCode) {
        this.inWareHouseCode = inWareHouseCode;
    }

    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRefDetailId() {
        return refDetailId;
    }

    public void setRefDetailId(String refDetailId) {
        this.refDetailId = refDetailId;
    }

    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public BigDecimal getLoseQuantity() {
        return loseQuantity;
    }

    public void setLoseQuantity(BigDecimal loseQuantity) {
        this.loseQuantity = loseQuantity;
    }

    public BigDecimal getLoseAmount() {
        return loseAmount;
    }

    public void setLoseAmount(BigDecimal loseAmount) {
        this.loseAmount = loseAmount;
    }

    public BigDecimal getDeliveryCostPrice() {
        return deliveryCostPrice;
    }

    public void setDeliveryCostPrice(BigDecimal deliveryCostPrice) {
        this.deliveryCostPrice = deliveryCostPrice;
    }

    public BigDecimal getDeliveryCostAmount() {
        return deliveryCostAmount;
    }

    public void setDeliveryCostAmount(BigDecimal deliveryCostAmount) {
        this.deliveryCostAmount = deliveryCostAmount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public String getTransportUnitId() {
        return transportUnitId;
    }

    public void setTransportUnitId(String transportUnitId) {
        this.transportUnitId = transportUnitId;
    }

    public String getTransportUnitName() {
        return transportUnitName;
    }

    public void setTransportUnitName(String transportUnitName) {
        this.transportUnitName = transportUnitName;
    }

    public BigDecimal getTransportQuentity() {
        return transportQuentity;
    }

    public void setTransportQuentity(BigDecimal transportQuentity) {
        this.transportQuentity = transportQuentity;
    }

    public BigDecimal getTransportPrice() {
        return transportPrice;
    }

    public void setTransportPrice(BigDecimal transportPrice) {
        this.transportPrice = transportPrice;
    }

    public BigDecimal getTransportAmount() {
        return transportAmount;
    }

    public void setTransportAmount(BigDecimal transportAmount) {
        this.transportAmount = transportAmount;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getTransprotScale() {
        return transprotScale;
    }

    public void setTransprotScale(BigDecimal transprotScale) {
        this.transprotScale = transprotScale;
    }

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getPercentageAmount() {
		return percentageAmount;
	}

	public void setPercentageAmount(BigDecimal percentageAmount) {
		this.percentageAmount = percentageAmount;
	}
    
}
