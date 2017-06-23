package cn.fooltech.fool_ops.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by Derek on 2017/1/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LineDataVo {

    private String name;
    private BigDecimal data[];
}
