package cn.fooltech.fool_ops.analysis.vo;


import lombok.*;

import java.util.Date;


@Getter
@Setter
@Data
@ToString
@NoArgsConstructor
public class TransportRouteVo {

    private String id;

    // 运输报价ID 默认取值的运输报价
    private String transportBillId;

    // 线路路径 记录JSON
    private String route;

    // 发货地ID 关联场地表
    private String deliveryPlace;
    private String deliveryPlaceName;
    // 收货地ID 关联场地表

    private String receiptPlace;
    private String receiptPlaceName;
    // 运输方式ID(关联辅助属性运输方式)

    private String transportTypeId;

    // 装运方式ID(关联辅助属性装运方式)
    private String shipmentTypeId;


    private Date createTime;


    private String creatorId;


    private Date updateTime;


    private String orgId;


    private String accId;


    private Date startDay;

    private Date endDay;


}