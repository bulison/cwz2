package cn.fooltech.fool_ops.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by Derek on 2017/1/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LineChartVo {

    private String charTitle;
    private String lineTitle[];
    private String xAxis[];
    private List<LineDataVo> lineDataVoList;
}
