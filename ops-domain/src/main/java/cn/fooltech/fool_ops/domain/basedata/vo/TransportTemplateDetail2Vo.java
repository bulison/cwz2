package cn.fooltech.fool_ops.domain.basedata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel("运输费报价模板从2表")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportTemplateDetail2Vo {
    @ApiModelProperty(value = "fid")
    private String fid;

    //描述
    @ApiModelProperty(value = "描述")
    private String describe;

    //创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    //修改时间戳,	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,	初始值为当前时间")
    private String updateTime;

    @ApiModelProperty(value = "从1Fid,关联运输费报价模板从1表")
    private String detail1Fid;
    //从1名称,关联运输费报价模板从1表ID
    @ApiModelProperty(value = "从1名称,关联运输费报价模板从1表")
    private String detail1Name;

    @ApiModelProperty(value = "模板id,关联运输费报价模板")
    private String templateFid;
    //模板名称,关联运输费报价模板
    @ApiModelProperty(value = "模板名称,关联运输费报价模板")
    private String templateName;

    @ApiModelProperty(value = "运输费用fid,关联辅助属性运输费用")
    private String transportCostFid;
    //运输费用名称,关联辅助属性运输费用
    @ApiModelProperty(value = "运输费用名称,关联辅助属性运输费用")
    private String transportCostName;

    @ApiModelProperty(value = "运输单位Fid,关联辅助属性运输计价单位")
    private String transportUnitFid;
    //运输单位名称,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位名称,关联辅助属性运输计价单位")
    private String transportUnitName;
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
    //从2表编号（用于记录从1表编号）
    @ApiModelProperty(value = "从2表编号（用于记录从1表编号）")
    private String code;

}