package cn.fooltech.fool_ops.domain.message.vo;

import lombok.Data;

/**
 * Created by Derek on 2017/3/9.
 */
@Data
public class SimpleMessageVo {

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 触发动作类型
     */
    private String triggerType;
}
