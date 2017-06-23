package cn.fooltech.fool_ops.domain.message.sender.factory;

import cn.fooltech.fool_ops.domain.message.entity.AbstractSender;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>网页推送消息工厂类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年5月19日
 */

@Component("ops.InnerMsgSender")
public class InnerMsgSender extends AbstractSender {

	private static final Logger logger = LoggerFactory.getLogger(InnerMsgSender.class);
	
	/**
	 * 消息服务类
	 */
	@Autowired
	protected MessageService messageService;
	
	/**
	 * 推送服务类
	 */
	@Autowired
	protected PushService pushService;
	
	@Async
	@Override
	public void send(Message message, int repeatTime, boolean sendImmediately) {

		SimpleMessageVo vo = new SimpleMessageVo();
		vo.setContent(message.getContent());
		vo.setTitle(message.getTitle());
		vo.setTriggerType(message.getTriggerType());
		String userId = message.getReceiverId();

		pushService.push(userId, vo, true);
	}
	

	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.message.sender.factory.SenderCode).INNER}")
	public void setCode(String code) {
		super.code = code;
	}
}
