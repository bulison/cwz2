package cn.fooltech.fool_ops.domain.basedata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel("运输费报价模板从1表")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportTemplateDetail1Vo {

    @ApiModelProperty(value = "fid")
    private String fid;

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

    @ApiModelProperty(value = "模板id,关联运输费报价模板")
    private String templateFid;
    //模板名称,关联运输费报价模板
    @ApiModelProperty(value = "模板名称,关联运输费报价模板")
    private String templateName;

    @ApiModelProperty(value = "发货地id")
    private String deliveryPlaceFid;
    //发货地名称
    @ApiModelProperty(value = "发货地名称")
    private String deliveryPlaceName;

    @ApiModelProperty(value = "收货地Id")
    private String receiptPlaceFid;
    //收货地名称
    @ApiModelProperty(value = "收货地名称")
    private String receiptPlaceName;

    @ApiModelProperty(value = "运输方式id")
    private String transportTypeFid;
    //运输方式名称
    @ApiModelProperty(value = "运输方式名称")
    private String transportTypeName;
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
    // 装运方式名称
    @ApiModelProperty(value = "装运方式名称")
    private String shipmentTypeName;

    @ApiModelProperty(value = "装运方式Id")
    private String shipmentTypeFid;
    //从1表编号（用于添加修改从2表）
    @ApiModelProperty(value = "从1表编号（用于添加修改从2表）")
    private String code;
}