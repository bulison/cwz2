package cn.fooltech.fool_ops.domain.warehouse.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString()
@NoArgsConstructor
public class SaleOrderDetailVo implements Serializable {

    private static final long serialVersionUID = -4617531831238260688L;

    /**
     * 订单ID
     */
    private String billId;

    /**
     * 销售订单单号
     */
    private String saleCode;

    /**
     * 业务员
     */
    private String saleId;

    /**
     * 业务员
     */
    private String sale;

    /**
     * 销售商ID
     */
    private String supplierCode;

    /**
     * 销售商名称
     */
    private String supplierName;

    /**
     * 订单日期
     */
    private String saleDate;

    /**
     * 计划完成日期
     */
    private String finishDate;

    /**
     * 货品名称ID
     */
    private String goodsId;

    /**
     * 货品编码
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
     * 货品属性
     */
    private String goodsSpecName;

    /**
     * 单位（货品基本单位）
     */
    private String goodsUnit;

    /**
     * 订货数量
     */
    private Long bookTotal;

    /**
     * 订货价格
     */
    private BigDecimal bookPrice;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 已发货数量
     */
    private Long deliveredTotal;

    /**
     * 已发货金额
     */
    private BigDecimal deliveredAmount;

    /**
     * 欠发货数量
     */
    private Long undeliveredTotal;

    /**
     * 欠发货金额
     */
    private BigDecimal unDeliveredAmount;

    /**
     * 已收金额
     */
    private BigDecimal hasIncome;

    /**
     * 欠收金额
     */
    private BigDecimal notIncome;

    /**
     * 退货数量
     */
    private Long backTotal;

    /**
     * 退货金额
     */
    private BigDecimal backAmount;

    /**
     * 货品成本
     */
    private BigDecimal goodsFee;

    /**
     * 销售费用
     */
    private BigDecimal saleExp;

    /**
     * 末次发货日期
     */
    private String lastDate;

    /**
     * 末次收款日期
     */
    private String lastIncomeDate;

    /**
     * 末次退货日期
     */
    private String lastBackDate;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

}
