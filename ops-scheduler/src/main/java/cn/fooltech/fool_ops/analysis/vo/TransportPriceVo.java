package cn.fooltech.fool_ops.analysis.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
@NoArgsConstructor
public class TransportPriceVo {

    private String id;


    //单号,从单据单号生成规则生成单号
    private String code;


    //单据日期,取当前时间
    private Date billDate;


    //供应商,关联供应商
    private String supplierId;
    private String supplierName;


    //发货地ID,关联场地表
    private String deliveryPlace;
    private String deliveryPlaceName;


    //收货地ID,关联场地表
    private String receiptPlace;
    private String receiptPlaceName;

    //运输方式ID,关联辅助属性运输方式
    private String transportTypeId;
    private String transportTypeName;

    //装运方式ID,关联辅助属性装运方式
    private String shipmentTypeId;
    private String shipmentTypeName;

    //有效日期

    private Date effectiveDate;


    //金额
    private Float amount;


    //可执行标识,1-可执行 2-难执行 3-无法执行
    private Integer executeSign;


    //预计天数
    private Integer expectedDays;


    //描述
    private String describe;


    //状态,0-失效 1-有效

    private Integer enable;


    private Date createTime;


    private String creatorId;


    private Date updateTime;


    private String orgId;


    private String accId;
//    private String fiscalAccountId;

    private Date startDay;


    private Date endDay;

}