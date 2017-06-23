package cn.fooltech.fool_ops.domain.message.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 待办任务Vo
 * Created by xjh on 2017/2/9.
 */
@Getter
@Setter
@NoArgsConstructor
public class TodoVo {

    private String busId;//事件或计划ID
    private String busType;//事件或计划类型
    private String busTitle;//待办任务描述
    private Date endTime;//截止时间
}
