package cn.fooltech.fool_ops.domain.basedata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel("运输费报价模板从2表")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportPriceDetail2Vo {
    @ApiModelProperty(value = "从2ID")
    private String id;
    //金额
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    //描述
    @ApiModelProperty(value = "描述")
    private String describe;

    //创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    //修改时间戳	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳	初始值为当前时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人id")
    private String creatorId;
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;
    @ApiModelProperty(value = "机构id")
    private String orgId;
    @ApiModelProperty(value = "机构名称")
    private String orgName;
    @ApiModelProperty(value = "账套id")
    private String fiscalAccountId;
    @ApiModelProperty(value = "账套名称")
    private String fiscalAccountName;

    //从1ID,关联运输费报价从1表ID
    @ApiModelProperty(value = "从1ID,关联运输费报价从1表ID")
    private String detail1Id;
    @ApiModelProperty(value = "从1名称,关联运输费报价从1表名称")
    private String detail1Name;
    @ApiModelProperty(value = "从1发货地,关联运输费报价从1表发货地")
    private String deliveryPlace;
    @ApiModelProperty(value = "从1收货地,关联运输费报价从1表收货地")
    private String receiptPlace;


    //单据ID,关联运输费报价
    @ApiModelProperty(value = "单据ID,关联运输费报价")
    private String billId;
    @ApiModelProperty(value = "单据名称,关联运输费报价")
    private String billName;


    //运输费用ID,关联辅助属性运输费用
    @ApiModelProperty(value = "运输费用ID,关联辅助属性运输费用")
    private String transportCostId;
    @ApiModelProperty(value = "运输费用编号,关联辅助属性运输费用")
    private String transportCostCode;
    @ApiModelProperty(value = "运输费用名称,关联辅助属性运输费用")
    private String transportCostName;


    //运输单位ID,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位ID,关联辅助属性运输计价单位")
    private String transportUnitId;
    @ApiModelProperty(value = "运输单位名称,关联辅助属性运输计价单位")
    private String transportUnitName;
}