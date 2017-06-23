package cn.fooltech.fool_ops.domain.flow.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>预计成本</p>
 *
 * @author c
 * @date 2017-03-01
 */

@Data
@ToString
@NoArgsConstructor
public class PlanTransportFeeVo implements Serializable {

    private static final long serialVersionUID = -5064375632842519245L;

    public static final String[] HEADER_TITLE = {"日期", "采购地", "收货地", "运输方式", "装运方式",
            "承运单位", "货品", "货品属性", "计量单位", "柜重", "出厂价", "税后价",  "运输费用",
            "成本总价", "预计时间", "备注"};
    public static final String[] HEADER_KEY = {"billDate", "deliveryPlace", "receiptPlace",
            "transportType", "shippingType", "supplier", "goodsName", "goodsSpec",
            "transportUnit", "conversionRate", "factoryPrice", "taxPrice", "freightPrice",
            "totalFee", "expectDay", "remark"};

    /**
     * 日期
     */
    private String billDate;

    /**
     * 采购地
     */
    private String deliveryPlace;

    /**
     * 收货地
     */
    private String receiptPlace;

    /**
     * 运输方式
     */
    private String transportType;

    /**
     * 装运方式
     */
    private String shippingType;

    /**
     * 承运单位
     */
    private String supplier;

    /**
     * 货品
     */
    private String goodsName;

    /**
     * 货品属性
     */
    private String goodsSpec;

    /**
     * 计量单位
     */
    private String transportUnit;

    /**
     * 柜重（取换算关系）
     */
    private BigDecimal conversionRate;

    /**
     * 出厂价
     */
    private BigDecimal factoryPrice;

    /**
     * 税后价
     */
    private BigDecimal taxPrice;

    /**
     * 成本总价
     */
    private BigDecimal totalFee;

    /**
     * 运输费用
     */
    private BigDecimal freightPrice;

    /**
     * 费用id,用于LAZY LOAD
     */
    private String feeId;


    /**
     * 预计时间
     */
    private int expectDay;

    /**
     * 备注
     */
    private String remark;

    /**
     * 处理后的费用名称费用金额容器
     */
    private Map<String, TransportFeeDetailVo> feeMap;

}
