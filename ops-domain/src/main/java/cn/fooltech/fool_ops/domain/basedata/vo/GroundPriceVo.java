package cn.fooltech.fool_ops.domain.basedata.vo;

import cn.fooltech.fool_ops.domain.basedata.entity.GroundPrice;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel("场地费报价")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroundPriceVo extends GroundPrice {

    @ApiModelProperty(value = "地址名称")
    private String addressName;

    @ApiModelProperty(value = "开始日期，查询用")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    @ApiModelProperty(value = "结束日期，查询用")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    @ApiModelProperty(value = "场地类型，查询用")
    private String groundId;

    @ApiModelProperty(value = "场地报价明细json字符串，保存用")
    private String details;

    @ApiModelProperty(value = "场地报价明细")
    private List<GroundPriceDetailVo> detailVos;
    
    @ApiModelProperty(value = "图片base64的json字符串")
    private String base64Str;
}