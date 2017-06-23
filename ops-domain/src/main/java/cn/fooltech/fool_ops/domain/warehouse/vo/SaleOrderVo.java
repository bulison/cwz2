package cn.fooltech.fool_ops.domain.warehouse.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class SaleOrderVo implements Serializable {

    private static final long serialVersionUID = -1349879327349103311L;

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
     * 订单金额
     */
    private String amount;

    /**
     * 已发货金额
     */
    private String deliveryAmount;

    /**
     * 订单日期
     */
    private String saleDate;

    /**
     * 计划完成日期
     */
    private String finishDate;

    /**
     * 货品成本
     */
    private String goodsFee;

    /**
     * 末次发货日期
     */
    private String lastDate;

    /**
     * 末次退货日期
     */
    private String lastBackDate;

    /**
     * 退货金额
     */
    private String backAmount;

    /**
     * 销售费用
     */
    private String saleExp;

    /**
     * 末次收款日期
     */
    private String lastIncomeDate;

    /**
     * 已收金额
     */
    private String hasIncome;

    /**
     * 欠收金额
     */
    private String notIncome;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

}
