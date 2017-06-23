package cn.fooltech.fool_ops.domain.basedata.vo;

import cn.fooltech.fool_ops.domain.basedata.entity.GroundPriceDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel("场地费报价从表")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroundPriceDetailVo extends GroundPriceDetail {

    //运输费用,关联辅助属性运输费用
    @ApiModelProperty(value = "运输费用,关联辅助属性运输费用")
    private String transportCostName;

    //运输单位,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位,关联辅助属性运输计价单位")
    private String transportUnitName;

}