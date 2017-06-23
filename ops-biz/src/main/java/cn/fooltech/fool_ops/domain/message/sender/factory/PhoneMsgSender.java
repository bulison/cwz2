package cn.fooltech.fool_ops.domain.message.sender.factory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.PhoneMsgService;
import cn.fooltech.fool_ops.component.core.PhoneMsgService.SendResponse;
import cn.fooltech.fool_ops.domain.message.entity.AbstractSender;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;

/**
 * <p>手机短信工厂类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年5月19日
 */

@Component("ops.PhoneMsgSender")
public class PhoneMsgSender extends AbstractSender {

	private static final Logger logger = LoggerFactory.getLogger(PhoneMsgSender.class);
	
	/**
	 * 默认不启用短信
	 */
	@Value("${cn.fooltech.fool_ops.phone.enable}")
	private int NOT_ENABLE_PHONE_MSG = 0;
	
	/**
	 * 消息服务类
	 */
	@Autowired
	protected MessageService messageService;
	
	/**
	 * 用户服务类
	 */
	@Autowired
	protected UserService userService;
	
	/**
	 * 用户服务类
	 */
	@Autowired
	protected PhoneMsgService phoneMsgService;
	
	@Async
	@Override
	@Transactional
	public void send(Message message, int repeatTime, boolean sendImmediately) {

		User receiver = userService.get(message.getReceiverId());
		String content = message.getContent();
		String phone = receiver.getPhoneOne();
		int repeat = repeatTime;
		SendResponse result = null;
		
		if(StringUtils.isNotBlank(phone)){
			do{
				try {
					result = phoneMsgService.send(phone, content);
					logger.info("发送短信：内容{1}，结果{2},", content, result.getResponseDesc());
				} catch (Exception e) {
					e.printStackTrace();
				}
				repeat--;
			}while(repeat>=0 && !result.getResponseCode().equals("0"));
			
		}
		
	}
	

	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.message.sender.factory.SenderCode).PHONE}")
	public void setCode(String code) {
		super.code = code;
	}
}
