package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>期间总库存金额实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月22日
 */
@Entity
@NoArgsConstructor
@ToString
@Table(name = "tsb_period_amount")
@AccessType(value = AccessType.Type.FIELD)
public class PeriodAmount extends OpsOrgEntity {

    private static final long serialVersionUID = 2931659959595529159L;

    //货品
    private Goods goods;

    //货品属性
    private GoodsSpec goodsSpec;

    //记账单位
    private Unit accountUnit;

    //上期结存数
    private BigDecimal preQuentity = BigDecimal.ZERO;

    //上期结存金额
    private BigDecimal preAmount = BigDecimal.ZERO;

    //本期入库数
    private BigDecimal inQuentity = BigDecimal.ZERO;

    //本期入库金额
    private BigDecimal inAmount = BigDecimal.ZERO;

    //本期出库数
    private BigDecimal outQuentity = BigDecimal.ZERO;

    //本期出库金额
    private BigDecimal outAmount = BigDecimal.ZERO;

    //本期盘点盈亏数
    private BigDecimal profitQuentity = BigDecimal.ZERO;

    //本期盘点盈亏金额
    private BigDecimal profitAmount = BigDecimal.ZERO;

    //本期结存数
    private BigDecimal accountQuentity = BigDecimal.ZERO;

    //本期结存金额
    private BigDecimal accountAmount = BigDecimal.ZERO;

    //会计期间
    private StockPeriod stockPeriod;

    //修改时间戳
    private Date updateTime;

    //本期结存单价
    private BigDecimal accountPrice = BigDecimal.ZERO;

    //采购入库数量
    private BigDecimal purchaseQuantity = BigDecimal.ZERO;

    //采购入库金额
    private BigDecimal purchaseAmount = BigDecimal.ZERO;

    //采购退货数量
    private BigDecimal purchaseReturnQuantity = BigDecimal.ZERO;

    //采购退货金额
    private BigDecimal purchaseReturnAmount = BigDecimal.ZERO;

    //生产领料数量
    private BigDecimal materialsQuantity = BigDecimal.ZERO;

    //生产领料金额
    private BigDecimal materialsAmount = BigDecimal.ZERO;

    //生产退料数量
    private BigDecimal materialsReturnQuantity = BigDecimal.ZERO;

    //生产退料金额
    private BigDecimal materialsReturnAmount = BigDecimal.ZERO;

    //成品入库数量
    private BigDecimal productQuantity = BigDecimal.ZERO;

    //成品入库金额
    private BigDecimal productAmount = BigDecimal.ZERO;

    //成品退库数量
    private BigDecimal productReturnQuantity = BigDecimal.ZERO;

    //成品退库金额
    private BigDecimal productReturnAmount = BigDecimal.ZERO;

    //销售出货数量
    private BigDecimal saleQuantity = BigDecimal.ZERO;

    //销售出货金额
    private BigDecimal saleAmount = BigDecimal.ZERO;

    //销售退货数量
    private BigDecimal saleReturnQuantity = BigDecimal.ZERO;

    //销售退货金额
    private BigDecimal saleReturnAmount = BigDecimal.ZERO;

    //报损数量
    private BigDecimal lossQuantity = BigDecimal.ZERO;

    //报损金额
    private BigDecimal lossAmount = BigDecimal.ZERO;

    //发货单数量
    private BigDecimal transportOutQuantity = BigDecimal.ZERO;

    //发货单金额
    private BigDecimal transportOutAmount = BigDecimal.ZERO;

    //收货单数量
    private BigDecimal transportInQuantity = BigDecimal.ZERO;

    //收货单金额
    private BigDecimal transportInAmount = BigDecimal.ZERO;

    //调出数量
    private BigDecimal moveOutQuantity = BigDecimal.ZERO;

    //调出金额
    private BigDecimal moveOutAmount = BigDecimal.ZERO;

    //调入数量
    private BigDecimal moveInQuantity = BigDecimal.ZERO;

    //调入金额
    private BigDecimal moveInAmount = BigDecimal.ZERO;

    //上期结存单价
    private BigDecimal prePrice = BigDecimal.ZERO;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    //货品属性
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSPEC_ID")
    public GoodsSpec getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(GoodsSpec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    //记账单位
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACCOUNT_UINT_ID")
    public Unit getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(Unit accountUnit) {
        this.accountUnit = accountUnit;
    }

    //上期结存数
    @Column(name = "FPRE_QUENTITY")
    public BigDecimal getPreQuentity() {
        return preQuentity;
    }

    public void setPreQuentity(BigDecimal preQuentity) {
        this.preQuentity = preQuentity;
    }

    //上期结存金额
    @Column(name = "FPRE_AMOUNT")
    public BigDecimal getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(BigDecimal preAmount) {
        this.preAmount = preAmount;
    }

    //本期入库数
    @Column(name = "FIN_QUENTITY")
    public BigDecimal getInQuentity() {
        return inQuentity;
    }

    public void setInQuentity(BigDecimal inQuentity) {
        this.inQuentity = inQuentity;
    }

    //本期入库金额
    @Column(name = "FIN_AMOUNT")
    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    //本期出库数
    @Column(name = "FOUT_QUENTITY")
    public BigDecimal getOutQuentity() {
        return outQuentity;
    }

    public void setOutQuentity(BigDecimal outQuentity) {
        this.outQuentity = outQuentity;
    }

    //本期出库金额
    @Column(name = "FOUT_AMOUNT")
    public BigDecimal getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    //本期盘点盈亏数
    @Column(name = "FPROFIT_QUENTITY")
    public BigDecimal getProfitQuentity() {
        return profitQuentity;
    }

    public void setProfitQuentity(BigDecimal profitQuentity) {
        this.profitQuentity = profitQuentity;
    }

    //本期盘点盈亏金额
    @Column(name = "FPROFIT_AMOUNT")
    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    //本期结存数
    @Column(name = "FACCOUNT_QUENTITY")
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    //本期结存金额
    @Column(name = "FACCOUNT_AMOUNT")
    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    //会计期间
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSTOCK_PERIOD_ID")
    public StockPeriod getStockPeriod() {
        return stockPeriod;
    }

    public void setStockPeriod(StockPeriod stockPeriod) {
        this.stockPeriod = stockPeriod;
    }

    //修改时间戳
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME", updatable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    //本期结存单价
    @Column(name = "FACCOUNT_PRICE")
    public BigDecimal getAccountPrice() {
        return accountPrice;
    }

    public void setAccountPrice(BigDecimal accountPrice) {
        this.accountPrice = accountPrice;
    }

    //采购入库数量
    @Column(name = "FPURCHASE_QUANTITY")
    public BigDecimal getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(BigDecimal purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    //采购入库金额
    @Column(name = "FPURCHASE_AMOUNT")
    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    //采购退货数量
    @Column(name = "FPURCHASE_RETURN_QUANTITY")
    public BigDecimal getPurchaseReturnQuantity() {
        return purchaseReturnQuantity;
    }

    public void setPurchaseReturnQuantity(BigDecimal purchaseReturnQuantity) {
        this.purchaseReturnQuantity = purchaseReturnQuantity;
    }

    //采购退货金额
    @Column(name = "FPURCHASE_RETURN_AMOUNT")
    public BigDecimal getPurchaseReturnAmount() {
        return purchaseReturnAmount;
    }

    public void setPurchaseReturnAmount(BigDecimal purchaseReturnAmount) {
        this.purchaseReturnAmount = purchaseReturnAmount;
    }

    //生产领料数量
    @Column(name = "FMATERIALS_QUANTITY")
    public BigDecimal getMaterialsQuantity() {
        return materialsQuantity;
    }

    public void setMaterialsQuantity(BigDecimal materialsQuantity) {
        this.materialsQuantity = materialsQuantity;
    }

    //生产领料金额
    @Column(name = "FMATERIALS_AMOUNT")
    public BigDecimal getMaterialsAmount() {
        return materialsAmount;
    }

    public void setMaterialsAmount(BigDecimal materialsAmount) {
        this.materialsAmount = materialsAmount;
    }

    //生产退料数量
    @Column(name = "FMATERIALS_RETURN_QUANTITY")
    public BigDecimal getMaterialsReturnQuantity() {
        return materialsReturnQuantity;
    }

    public void setMaterialsReturnQuantity(BigDecimal materialsReturnQuantity) {
        this.materialsReturnQuantity = materialsReturnQuantity;
    }

    //生产退料金额
    @Column(name = "FMATERIALS_RETURN_AMOUNT")
    public BigDecimal getMaterialsReturnAmount() {
        return materialsReturnAmount;
    }

    public void setMaterialsReturnAmount(BigDecimal materialsReturnAmount) {
        this.materialsReturnAmount = materialsReturnAmount;
    }

    //成品入库数量
    @Column(name = "FPRODUCT_QUANTITY")
    public BigDecimal getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(BigDecimal productQuantity) {
        this.productQuantity = productQuantity;
    }

    //成品入库金额
    @Column(name = "FPRODUCT_AMOUNT")
    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    //成品退库数量
    @Column(name = "FPRODUCT_RETURN_QUANTITY")
    public BigDecimal getProductReturnQuantity() {
        return productReturnQuantity;
    }

    public void setProductReturnQuantity(BigDecimal productReturnQuantity) {
        this.productReturnQuantity = productReturnQuantity;
    }

    //成品退库金额
    @Column(name = "FPRODUCT_RETURN_AMOUNT")
    public BigDecimal getProductReturnAmount() {
        return productReturnAmount;
    }

    public void setProductReturnAmount(BigDecimal productReturnAmount) {
        this.productReturnAmount = productReturnAmount;
    }

    //销售出货数量
    @Column(name = "FSALE_QUANTITY")
    public BigDecimal getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(BigDecimal saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    //销售出货金额
    @Column(name = "FSALE_AMOUNT")
    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    //销售退货数量
    @Column(name = "FSALE_RETURN_QUANTITY")
    public BigDecimal getSaleReturnQuantity() {
        return saleReturnQuantity;
    }

    public void setSaleReturnQuantity(BigDecimal saleReturnQuantity) {
        this.saleReturnQuantity = saleReturnQuantity;
    }

    //销售退货金额
    @Column(name = "FSALE_RETURN_AMOUNT")
    public BigDecimal getSaleReturnAmount() {
        return saleReturnAmount;
    }

    public void setSaleReturnAmount(BigDecimal saleReturnAmount) {
        this.saleReturnAmount = saleReturnAmount;
    }

    //报损数量
    @Column(name = "FLOSS_QUANTITY")
    public BigDecimal getLossQuantity() {
        return lossQuantity;
    }

    public void setLossQuantity(BigDecimal lossQuantity) {
        this.lossQuantity = lossQuantity;
    }

    //报损金额
    @Column(name = "FLOSS_AMOUNT")
    public BigDecimal getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(BigDecimal lossAmount) {
        this.lossAmount = lossAmount;
    }

    //发货单数量
    @Column(name = "FTRANSPORT_OUT_QUANTITY")
    public BigDecimal getTransportOutQuantity() {
        return transportOutQuantity;
    }

    public void setTransportOutQuantity(BigDecimal transportOutQuantity) {
        this.transportOutQuantity = transportOutQuantity;
    }

    //发货单金额
    @Column(name = "FTRANSPORT_OUT_AMOUNT")
    public BigDecimal getTransportOutAmount() {
        return transportOutAmount;
    }

    public void setTransportOutAmount(BigDecimal transportOutAmount) {
        this.transportOutAmount = transportOutAmount;
    }

    //收货单数量
    @Column(name = "FTRANSPORT_IN_QUANTITY")
    public BigDecimal getTransportInQuantity() {
        return transportInQuantity;
    }

    public void setTransportInQuantity(BigDecimal transportInQuantity) {
        this.transportInQuantity = transportInQuantity;
    }

    //收货单金额
    @Column(name = "FTRANSPORT_IN_AMOUNT")
    public BigDecimal getTransportInAmount() {
        return transportInAmount;
    }

    public void setTransportInAmount(BigDecimal transportInAmount) {
        this.transportInAmount = transportInAmount;
    }

    //调出数量
    @Column(name = "FMOVE_OUT_QUANTITY")
    public BigDecimal getMoveOutQuantity() {
        return moveOutQuantity;
    }

    public void setMoveOutQuantity(BigDecimal moveOutQuantity) {
        this.moveOutQuantity = moveOutQuantity;
    }

    //调出金额
    @Column(name = "FMOVE_OUT_AMOUNT")
    public BigDecimal getMoveOutAmount() {
        return moveOutAmount;
    }

    public void setMoveOutAmount(BigDecimal moveOutAmount) {
        this.moveOutAmount = moveOutAmount;
    }

    //调入数量
    @Column(name = "FMOVE_IN_QUANTITY")
    public BigDecimal getMoveInQuantity() {
        return moveInQuantity;
    }

    public void setMoveInQuantity(BigDecimal moveInQuantity) {
        this.moveInQuantity = moveInQuantity;
    }

    //调入金额
    @Column(name = "FMOVE_IN_AMOUNT")
    public BigDecimal getMoveInAmount() {
        return moveInAmount;
    }

    public void setMoveInAmount(BigDecimal moveInAmount) {
        this.moveInAmount = moveInAmount;
    }

    @Column(name = "FPRE_PRICE")
    public BigDecimal getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(BigDecimal prePrice) {
        this.prePrice = prePrice;
    }
}
