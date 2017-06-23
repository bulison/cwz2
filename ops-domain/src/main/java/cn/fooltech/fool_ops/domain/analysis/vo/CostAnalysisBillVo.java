package cn.fooltech.fool_ops.domain.analysis.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.sf.json.JSONArray;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

@ApiModel("成本分析Vo")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CostAnalysisBillVo {

    // 主键
    @ApiModelProperty(value = "主键")
    private String id;

    // 单据日期
    @ApiModelProperty(value = "单据日期")
    private String billDate;

    // 线路路径
    @ApiModelProperty(value = "线路路径")
    private String route;

    // 采购公司 关联供应商
    @ApiModelProperty(value = "采购公司Id  关联供应商")
    private String supplierId;
    @ApiModelProperty(value = "采购公司名称  关联供应商")
    private String supplierName;

    // 货品ID
    @ApiModelProperty(value = "货品ID")
    private String goodsId;
    @ApiModelProperty(value = "货品名称")
    private String goodsName;

    // 货品属性ID
    @ApiModelProperty(value = "货品属性ID")
    private String goodsSpecId;
    @ApiModelProperty(value = "货品属性名称")
    private String goodsSpecName;

    // 货品单位ID 货品记账单位
    @ApiModelProperty(value = "货品单位ID  货品记账单位")
    private String unitId;
    @ApiModelProperty(value = "货品单位名称  货品记账单位")
    private String unitName;

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

    // 出厂价
    @ApiModelProperty(value = "出厂价")
    private BigDecimal factoryPrice;

    // 调整出厂价
    @ApiModelProperty(value = "调整出厂价")
    private BigDecimal publishFactoryPrice;

    // 运输费用
    @ApiModelProperty(value = "运输费用")
    private BigDecimal freightPrice;

    // 调整运输费用
    @ApiModelProperty(value = "调整运输费用")
    private BigDecimal publishFreightPrice;

    // 成本总价
    @ApiModelProperty(value = "成本总价")
    private BigDecimal totalPrice;

    // 调整成本总价
    @ApiModelProperty(value = "调整成本总价")
    private BigDecimal publishTotalPrice;

    // 可执行标识(1-可执行 2-难执行 3-无法执行)
    @ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
    private Integer executeSign;

    // 预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays;

    // 备注
    @ApiModelProperty(value = "备注")
    @Length(max = 100, message = "备注长度超过{max}个字符")
    private String remark;

    // 发布 0-不发布 1-发布
    @ApiModelProperty(value = "发布 0-不发布 1-发布")
    private Integer publish;

    // 是否采购 0-仓库【运输报价】 1-采购【成本分析】
    @ApiModelProperty(value = "是否采购 0-仓库【运输报价】 1-采购【成本分析】")
    private Integer purchase;

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

    private String accId;
    // 账套ID
    @ApiModelProperty(value = "账套ID")
    private String fiscalAccountId;
    @ApiModelProperty(value = "账套名称")
    private String fiscalAccountName;

    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;
    //成本分析明细:导出数据使用的属性
    private JSONArray detail;
    //成本分析明细：页面使用的属性
    private String details;
    @ApiModelProperty(value = "开始日期(搜索)")
    private String startDay;
    @ApiModelProperty(value = "结束日期(搜索)")
    private String endDay;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户默认收货地ID")
    private String defaultAddressId;

    @ApiModelProperty(value = "货品报价单ID")
    private String purchaseId;

    @ApiModelProperty(value = "损耗(元)")
    private BigDecimal loss;

    @ApiModelProperty(value = "采购日期")
    private String purchaseDate;

    @ApiModelProperty(value = "中转站列表值")
    private List<BillValue> separateValues = Lists.newArrayList();

    @ApiModelProperty(value = "运输费报价Ids")
    private String transportIds;
}