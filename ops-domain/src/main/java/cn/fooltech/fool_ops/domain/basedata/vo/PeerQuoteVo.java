package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.PeerQuote;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("同行报价")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PeerQuoteVo {

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "货品名称")
    private String goodsName;

    @ApiModelProperty(value = "货品编号")
    private String goodsCode;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "属性名称")
    private String specName;

    @ApiModelProperty(value = "收货地名称")
    private String receiptPlaceName;

    @ApiModelProperty(value = "制单人")
    private String creatorName;

    @ApiModelProperty(value = "开始日期，查询用")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    @ApiModelProperty(value = "结束日期，查询用")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    //主键
    @ApiModelProperty(value = "主键")
    private String id;


    //单号,从单据单号生成规则生成单号
    @ApiModelProperty(value = "单号,从单据单号生成规则生成单号")
    private String code;


    //单据日期,取当前时间
    @ApiModelProperty(value = "单据日期,取当前时间")
    private Date billDate;


    //供应商,直接输入名字
    @ApiModelProperty(value = "供应商,直接输入名字")
    private String supplier;


    //客户,关联客户表
    @ApiModelProperty(value = "客户,关联客户表")
    private String customerId;


    //货品ID,关联货品表
    @ApiModelProperty(value = "货品ID,关联货品表")
    private String goodsId;


    //货品属性ID,关联属性表
    @ApiModelProperty(value = "货品属性ID,关联属性表")
    private String goodSpecId;


    //货品单位,关联单位表
    @ApiModelProperty(value = "货品单位,关联单位表")
    private String unitId;


    //交货总价
    @ApiModelProperty(value = "交货总价")
    private BigDecimal deliveryPrice;


    //收货地ID,关联场地表
    @ApiModelProperty(value = "收货地ID,关联场地表")
    private String receiptPlace;


    //描述
    @ApiModelProperty(value = "描述")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")
    private String creatorId;


    //修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //组织ID
    @ApiModelProperty(value = "组织ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    private String accId;
    
    @ApiModelProperty(value = "图片base64的json字符串")
    private String base64Str;
}