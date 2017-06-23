package cn.fooltech.fool_ops.domain.basedata.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xjh on 2017/1/4.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SimplePurchasePriceVo {

    private Date billDate;
    private String supplierName;
    private BigDecimal unitScale;
    private BigDecimal deliveryPrice;
}
