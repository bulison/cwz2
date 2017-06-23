package cn.fooltech.fool_ops.domain.warehouse.vo;

import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class PeriodStockAmountVo {

    private String warehouseId;//仓库
    private String warehouseName;//仓库
    private String goodsId;//货品
    private String goodsName;//货品
    private String goodsCode;//货品编号
    private String goodsSpecId;//货品属性
    private String goodsSpecName;//货品属性
    private String accountUnitId;//记账单位
    private String accountUnitName;//记账单位
    private String periodId;

    private BigDecimal preQuentity = BigDecimal.ZERO;//上期结存数
    private BigDecimal preAmount = BigDecimal.ZERO;//上期结存金额

    private BigDecimal inQuentity = BigDecimal.ZERO;//本期入库数
    private BigDecimal inAmount = BigDecimal.ZERO;//本期入库金额

    private BigDecimal outQuentity = BigDecimal.ZERO;//本期出库数
    private BigDecimal outAmount = BigDecimal.ZERO;//本期出库金额

    private BigDecimal profitQuentity = BigDecimal.ZERO;//本期盘点盈亏数
    private BigDecimal profitAmount = BigDecimal.ZERO;//本期盘点盈亏金额

    private BigDecimal accountQuentity = BigDecimal.ZERO;//本期结存数
    private BigDecimal accountAmount = BigDecimal.ZERO;//本期结存金额

    private BigDecimal moveInQuentity = BigDecimal.ZERO;//调仓入数
    private BigDecimal moveOutQuentity = BigDecimal.ZERO;//调仓出数

    private BigDecimal movePrice = BigDecimal.ZERO;//调仓单价

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

    //调出金额
    private BigDecimal moveOutAmount = BigDecimal.ZERO;

    //调入金额
    private BigDecimal moveInAmount = BigDecimal.ZERO;

    //上期结存单价
    private BigDecimal prePrice = BigDecimal.ZERO;
}
