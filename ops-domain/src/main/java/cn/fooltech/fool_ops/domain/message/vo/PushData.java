package cn.fooltech.fool_ops.domain.message.vo;

import lombok.Data;

/**
 * 消息推送数据对象
 * Created by xjh on 2017/4/10.
 */
@Data
public class PushData {

    private SimpleMessageVo message;
    private Long unread = 0L;
}
