package cn.fooltech.fool_ops.domain.basedata.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Derek on 2017/1/18.
 */
@NoArgsConstructor
@Setter
@Getter
public class RouteVo {
    private String de;//该记录的发货地ID
    private String sh;//该记录的装运方式ID
    private String tr;//该记录的运输方式ID”
    private String re;//该记录的收货地ID
}
