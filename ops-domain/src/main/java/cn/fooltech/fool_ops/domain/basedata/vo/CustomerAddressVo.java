package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.CustomerAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerAddressVo extends CustomerAddress {

    //客户或供应商名称
    @ApiModelProperty(value = "客户或供应商名称")
    private String customerName;

    //客户或供应商类型
    @ApiModelProperty(value = "客户或供应商类型:1：客户；2：供应商")
    private Integer customerType;

    //地址
    @ApiModelProperty(value = "地址")
    private String addressName;
}