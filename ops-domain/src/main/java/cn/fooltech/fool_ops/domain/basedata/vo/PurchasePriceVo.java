package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

//@ApiModel("")
@Data
@ToString
@NoArgsConstructor
public class PurchasePriceVo {

    //主键
    @ApiModelProperty(value = "主键")
    private String id;

    //单号
    @ApiModelProperty(value = "单号")
    private String code;

    //单据日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private Date billDate;

    //供应商
    @ApiModelProperty(value = "供应商")
    private String supplierId;

    //货品属性ID
    @ApiModelProperty(value = "货品属性ID")
    private String goodSpecId;

    //货品单位
    @ApiModelProperty(value = "货品单位")
    private String unitId;

    //厂价
    @ApiModelProperty(value = "厂价")
    private BigDecimal factoryPrice;

    //税点
    @ApiModelProperty(value = "税点")
    private BigDecimal taxPoint;

    //税后价
    @ApiModelProperty(value = "税后价")
    private BigDecimal afterTaxPrice;

    //提货费
    @ApiModelProperty(value = "提货费")
    private BigDecimal pickUpCharge;


    //交货总价
    @ApiModelProperty(value = "交货总价")
    private BigDecimal deliveryPrice;


    //有效日期
    @ApiModelProperty(value = "有效日期")
    private Date effectiveDate;


    //发货地ID
    @ApiModelProperty(value = "发货地ID")
    private String deliveryPlace;

    //描述
    @ApiModelProperty(value = "描述")
    private String fdescribe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    //创建人
    @ApiModelProperty(value = "创建人")
    private String creatorId;

    //修改时间戳
    @ApiModelProperty(value = "修改时间戳")
    private Date updateTime;

    //组织ID
    @ApiModelProperty(value = "组织ID")
    private String orgId;

    //账套ID
    @ApiModelProperty(value = "账套ID")
    private String accId;

    private String supplierName;
    private String   unitGroupId;

    private String goodsId;
    private String goodsName;

    private String goodsCode;

    private String unitName;


    private String specName;

    private  String deliveryPlaceName;



//    @ApiModelProperty(value = "开始日期，查询用")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

//    @ApiModelProperty(value = "结束日期，查询用")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 记录状态
     */
//    @ApiModelProperty(value = "记录状态")
    private String recordStatus = VehicleInformation.STATUS_SAC;
    /**
     * 搜索关键字
     */
//    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;
    /**
     * 模糊搜索结果集大小
     */
//    @ApiModelProperty(value = "模糊搜索结果集大小")
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;


    private String creatorName;
    
    @ApiModelProperty(value = "日状态：0-失效 1-有效，默认值1")
    private Integer dayEnable;
    
    @ApiModelProperty(value = "状态,0-失效 1-有效")
    private Integer enable;
    
    @ApiModelProperty(value = "图片base64的json字符串")
    private String base64Str;
}