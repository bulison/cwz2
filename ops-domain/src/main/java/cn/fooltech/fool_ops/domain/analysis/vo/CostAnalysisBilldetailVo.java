package cn.fooltech.fool_ops.domain.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

@ApiModel("成本分析明细Vo")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CostAnalysisBilldetailVo {

    // 主键
    @ApiModelProperty(value = "主键")
    private String id;
    // 单据ID
    @ApiModelProperty(value = "单据ID")
    private String billId;

    // 序号
    @ApiModelProperty(value = "序号")
    private Integer no;

    // 运输报价ID
    @ApiModelProperty(value = "运输报价ID")
    private String transportBillId;
    private String transportBillName;

    //报价日期
    @ApiModelProperty(value = "报价日期")
    private String billDate;

    // 运输公司  关联供应商
    @ApiModelProperty(value = "运输公司  关联供应商")
    private String supplierId;
    private String supplierName;

    // 发货地ID 关联场地表
    @ApiModelProperty(value = "发货地ID  关联场地表")
    private String deliveryPlaceId;
    private String deliveryPlaceName;

    // 收货地ID 关联场地表
    @ApiModelProperty(value = "收货地ID  关联场地表")
    private String receiptPlaceId;
    private String receiptPlaceName;

    // 运输方式ID(关联辅助属性运输方式)
    @ApiModelProperty(value = "运输方式ID(关联辅助属性运输方式)")
    private String transportTypeId;
    private String transportTypeName;

    // 装运方式ID(关联辅助属性装运方式)
    @ApiModelProperty(value = "装运方式ID(关联辅助属性装运方式)")
    private String shipmentTypeId;
    private String shipmentTypeName;

    //运输计价单位ID  关联辅助属性运输费计价单位
    @ApiModelProperty(value = "运输计价单位ID  关联辅助属性运输费计价单位")
    private String transportUnitId;
    private String transportUnitName;

    // 运输费用
    @ApiModelProperty(value = "运输费用")
    private BigDecimal freightPrice;

    // 调整运输费用
    @ApiModelProperty(value = "调整运输费用")
    private BigDecimal publishFreightPrice;

    // 换算关系  运输单位与货品基本单位的换算关系
    @ApiModelProperty(value = "换算关系  运输单位与货品基本单位的换算关系")
    private BigDecimal conversionRate;

    // 折算运输单价
    @ApiModelProperty(value = "折算运输单价")
    private BigDecimal basePrice;

    // 折算运输单价
    @ApiModelProperty(value = "调整折算运输单价")
    private BigDecimal publishBasePrice;

    // 可执行标识(1-可执行 2-难执行 3-无法执行)
    @ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
    private Integer executeSign;

    // 预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays;

    // 场地费用单价
    @ApiModelProperty(value = "场地费用单价")
    private BigDecimal groundCostPrice;

    // 备注
    @ApiModelProperty(value = "备注")
    @Length(max = 100, message = "备注长度超过{max}个字符")
    private String remark;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    // 创建人
    @ApiModelProperty(value = "创建人id")
    private String creatorId;
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    // 修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
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