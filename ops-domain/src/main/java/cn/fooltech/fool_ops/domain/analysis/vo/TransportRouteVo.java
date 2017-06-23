package cn.fooltech.fool_ops.domain.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel("运输路径表Vo")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportRouteVo {

    // 主键
    @ApiModelProperty(value = "主键")
    private String id;

    // 运输报价ID 默认取值的运输报价
    @ApiModelProperty(value = "运输报价ID 默认取值的运输报价")
    private String transportBillId;
    @ApiModelProperty(value = "运输报价名称 默认取值的运输报价")
    private String transportBillName;

    // 线路路径 记录JSON
    @ApiModelProperty(value = "线路路径 记录JSON")
    private String route;

    // 发货地ID 关联场地表
    @ApiModelProperty(value = "发货地ID  关联场地表")
    private String deliveryPlaceId;
    @ApiModelProperty(value = "发货地名称  关联场地表")
    private String deliveryPlaceName;

    // 收货地ID 关联场地表
    @ApiModelProperty(value = "收货地ID  关联场地表")
    private String receiptPlaceId;
    @ApiModelProperty(value = "收货地名称  关联场地表")
    private String receiptPlaceName;

    // 运输方式ID(关联辅助属性运输方式)
    @ApiModelProperty(value = "运输方式ID(关联辅助属性运输方式)")
    private String transportTypeId;
    @ApiModelProperty(value = "运输方式名称(关联辅助属性运输方式)")
    private String transportTypeName;

    // 装运方式ID(关联辅助属性装运方式)
    @ApiModelProperty(value = "装运方式ID(关联辅助属性装运方式)")
    private String shipmentTypeId;
    @ApiModelProperty(value = "装运方式名称(关联辅助属性装运方式)")
    private String shipmentTypeName;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    // 创建人
    @ApiModelProperty(value = "创建人id")
    private String creatorId;
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    // 修改时间戳(初始值为当前时间)
    @ApiModelProperty(value = "修改时间戳(初始值为当前时间)")
    private String updateTime;

    // 组织ID,机构ID
    @ApiModelProperty(value = "组织ID,机构ID")
    private String orgId;
    @ApiModelProperty(value = "组织名称,机构ID")
    private String orgName;

    // 账套ID
    @ApiModelProperty(value = "账套ID")
    private String fiscalAccountId;
    @ApiModelProperty(value = "账套名称")
    private String fiscalAccountName;

}