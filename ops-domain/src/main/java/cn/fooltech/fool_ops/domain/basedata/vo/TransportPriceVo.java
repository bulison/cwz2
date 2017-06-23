package cn.fooltech.fool_ops.domain.basedata.vo;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import javax.persistence.Column;
import java.math.BigDecimal;

@ApiModel("运输费报价")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportPriceVo {
    @ApiModelProperty(value = "fid")
    private String fid;
    //单号,从单据单号生成规则生成单号
    @ApiModelProperty(value = "单号,从单据单号生成规则生成单号")
    private String code;


    //单据日期,取当前时间
    @ApiModelProperty(value = "单据日期,取当前时间")
    @JSONField(format = "yyyy-MM-dd")
    private String billDate;

    //有效日期
    @ApiModelProperty(value = "有效日期")
    @JSONField(format = "yyyy-MM-dd")
    private String effectiveDate;


    //金额
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;


    //可执行标识,1-可执行 2-难执行 3-无法执行
    @ApiModelProperty(value = "可执行标识,1-可执行 2-难执行 3-无法执行")
    private Integer executeSign;


    //预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays;

    //描述
    @ApiModelProperty(value = "描述")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    //修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    private String updateTime;

    @ApiModelProperty(value = "承运单位id,关联供应商")
    private String supplierId;
    //供应商,关联供应商
    @ApiModelProperty(value = "承运单位名称,关联供应商")
    private String supplierName;
    @ApiModelProperty(value = "报价单位id,关联供应商")
    private String priceUnitId;
    @ApiModelProperty(value = "报价单位名称,关联供应商")
    private String priceUnitName;
    
    @ApiModelProperty(value = "发货地id,关联场地表")
    private String deliveryPlaceId;
    //发货地名称,关联场地表
    @ApiModelProperty(value = "发货地名称,关联场地表")
    private String deliveryPlaceName;


    @ApiModelProperty(value = "收货地id,关联场地表")
    private String receiptPlaceId;
    //收货地名称,关联场地表
    @ApiModelProperty(value = "收货地名称,关联场地表")
    private String receiptPlaceName;


    //运输方式名称,关联辅助属性运输方式
    @ApiModelProperty(value = "运输方式名称,关联辅助属性运输方式")
    private String transportTypeName;

    @ApiModelProperty(value = "运输方式Id,关联辅助属性运输方式")
    private String transportTypeId;


    //装运方式名称,关联辅助属性装运方式
    @ApiModelProperty(value = "装运方式名称,关联辅助属性装运方式")
    private String shipmentTypeName;

    @ApiModelProperty(value = "装运方式id,关联辅助属性装运方式")
    private String shipmentTypeId;
    
    //运输单位名称,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位名称,关联辅助属性运输计价单位")
    private String transportUnitName;
    
    @ApiModelProperty(value = "运输单位id,关联辅助属性运输计价单位")
    private String transportUnitId;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;
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
    @ApiModelProperty(value = "运输费报价从1表")
    private String details1;

    @ApiModelProperty(value = "状态：0-失效 1-有效，默认值1")
    private Integer enable;
    @ApiModelProperty(value = "开始日期(搜索)")
    private String startDay;
    @ApiModelProperty(value = "结束日期(搜索)")
    private String endDay;
    
    @ApiModelProperty(value = "日状态：0-失效 1-有效，默认值1")
    private Integer dayEnable;
    
    @ApiModelProperty(value = "图片base64的json字符串")
    private String base64Str;
    
    
}