package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel("场地费报价模板")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroundTemplateVo{

    @ApiModelProperty(value = "id")
    private String id;


    //描述

    @ApiModelProperty(value = "描述")
    private String describe;


    //创建时间

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    //创建人

    @ApiModelProperty(value = "创建人")
    private String creatorId;


    //修改时间戳
    @ApiModelProperty(value = "修改时间戳")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //组织ID
    @ApiModelProperty(value = "组织ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    private String accId;

    //场地名称
    @ApiModelProperty(value = "场地名称")
    private String groundName;

    //场地ID
    @ApiModelProperty(value = "场地ID")
    private String groundId;
}