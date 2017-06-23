package cn.fooltech.fool_ops.domain.basedata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.sf.json.JSONArray;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

@ApiModel("运输费报价模板从1表")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportPriceDetail1Vo {
    @ApiModelProperty(value = "编号")
    private String id;
    //预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays;

    //金额
    @ApiModelProperty(value = "金额")
    private Float amount;

    //描述
    @ApiModelProperty(value = "描述")
    private String describe;

    //创建时间
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //修改时间戳	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳	初始值为当前时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


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

    //单据ID,关联运输费报价
    @ApiModelProperty(value = "单据ID,关联运输费报价")
    private String billId;
    @ApiModelProperty(value = "单据名称,关联运输费报价")
    private String billName;


    //发货地ID,关联场地表
    @ApiModelProperty(value = "发货地ID,关联场地表")
    private String deliveryPlaceId;
    @ApiModelProperty(value = "发货地名称,关联场地表")
    private String deliveryPlaceName;


    //收货地ID,关联场地表
    @ApiModelProperty(value = "收货地ID,关联场地表")
    private String receiptPlaceId;
    @ApiModelProperty(value = "收货地名称,关联场地表")
    private String receiptPlaceName;
    //运输方式ID,关联辅助属性运输方式
    @ApiModelProperty(value = "运输方式ID,关联辅助属性运输方式")
    private String transportTypeId;
    @ApiModelProperty(value = "运输方式名称,关联辅助属性运输方式")
    private String transportTypeName;
    //装运方式ID,关联辅助属性运输方式
    @ApiModelProperty(value = "装运方式ID,关联辅助属性运输方式")
    private String shipmentTypeId;
    @ApiModelProperty(value = "装运方式名称,关联辅助属性运输方式")
    private String shipmentTypeName;

    @ApiModelProperty(value = "运输费报价从2表")
    private JSONArray details2;
    
    //运输单位ID,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位ID,关联辅助属性运输计价单位")
    private String transportUnitId;
    @ApiModelProperty(value = "运输单位名称,关联辅助属性运输计价单位")
    private String transportUnitName;

}