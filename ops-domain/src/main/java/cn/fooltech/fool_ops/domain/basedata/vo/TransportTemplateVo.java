package cn.fooltech.fool_ops.domain.basedata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel("运输费报价模板")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportTemplateVo {

    @ApiModelProperty(value = "id")
    private String id;

    //编号
    @ApiModelProperty(value = "编号")
    private String code;


    //名称
    @ApiModelProperty(value = "名称")
    private String name;

    //预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays;


    //状态0--停用 1--启用
    @ApiModelProperty(value = "状态0--停用 1--启用")
    private Integer enable;


    //描述
    @ApiModelProperty(value = "描述")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    //修改时间戳	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳	初始值为当前时间")
    private String updateTime;

    @ApiModelProperty(value = "发货地id")
    private String deliveryPlaceFid;
    // 发货地名称
    @ApiModelProperty(value = "发货地名称")
    private String deliveryPlaceName;

    // 收货地名称
    @ApiModelProperty(value = "收货地名称")
    private String receiptPlaceName;

    @ApiModelProperty(value = "收货地Id")
    private String receiptPlaceFid;

    // 运输方式名称
    @ApiModelProperty(value = "运输方式名称")
    private String transportTypeName;

    @ApiModelProperty(value = "运输方式id")
    private String transportTypeFid;

    // 装运方式名称
    @ApiModelProperty(value = "装运方式名称")
    private String shipmentTypeName;

    @ApiModelProperty(value = "装运方式Id")
    private String shipmentTypeFid;
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
    @ApiModelProperty(value = "运输费报价模板从1表")
    private String details1;

    @ApiModelProperty(value = "运输费报价模板从2表")
    private String details2;
}