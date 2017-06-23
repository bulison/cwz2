package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplate;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplateDetail;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel("场地费报价模板明细")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroundTemplateDetailVo {

    //主键

    @ApiModelProperty(value = "主键")
    private String id;

    //运输费用ID
    @ApiModelProperty(value = "运输费用ID")
    @NotEmpty(message = "运输费用必填")
    private String transportCostId;


    //运输单位ID
    @ApiModelProperty(value = "运输单位ID")
    @NotEmpty(message = "运输单位必填")
    private String transportUnitId;


    //成本费用标识

    @ApiModelProperty(value = "成本费用标识:0-否 1-是")
    @NotNull(message = "成本标识必填")
    private Integer costSign;


    //描述

    @ApiModelProperty(value = "描述")
    private String describe;


    //创建时间

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    //创建人

    @ApiModelProperty(value = "创建人")
    private String creatorId;


    //修改时间戳

    @ApiModelProperty(value = "修改时间戳")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;


    //组织ID

    @ApiModelProperty(value = "组织ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    private String accId;


    @ApiModelProperty(value = "运输费用名称")
    private String transportCostName;


    //运输单位名称
    @ApiModelProperty(value = "运输单位名称")
    private String transportUnitName;

    //运输模板ID
    @ApiModelProperty(value = "模板ID")
    private String templateId;

}