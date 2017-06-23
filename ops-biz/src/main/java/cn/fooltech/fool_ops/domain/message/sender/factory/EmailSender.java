package cn.fooltech.fool_ops.domain.message.sender.factory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.EmailService;
import cn.fooltech.fool_ops.domain.message.entity.AbstractSender;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;


/**
 * <p>发送电子邮件工厂类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年5月19日
 */

@Component("ops.EmailSender")
public class EmailSender extends AbstractSender{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

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
	 * 邮件服务类
	 */
	@Autowired
	protected EmailService emailService;
	
	
	/**
	 * 发送电子邮件(使用异步方式)
	* @param message
	* @param template 
	 */
	@Override
	@Async
	@Transactional
	public void send(Message message, int repeatTime, boolean sendImmediately) {

		User receiver = userService.get(message.getReceiverId());
		
		String email = receiver.getEmail();
		String subject = message.getTitle();//使用title作为邮件标题
		String content = message.getContent();
		int repeat = repeatTime;
		boolean result = false;
		
		if(StringUtils.isNotBlank(email)){
			do{
				try {
					result = emailService.sendCommonMail(email, subject, content);
					logger.info("发送邮件：标题{0}，内容{1}，结果{2},", subject, content, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				repeat--;
			}while(repeat>=0 && result==false);
			
		}
		
	}

	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.message.sender.factory.SenderCode).EMAIL}")
	public void setCode(String code) {
		super.code = code;
	}

}
