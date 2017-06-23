package cn.fooltech.fool_ops.domain.message.sender.factory;

import cn.fooltech.fool_ops.domain.message.entity.Message;

/**
 * <p>抽象的发送器接口</p>
 * @author xjh
 * @version 1.0
 * @date 2015年5月19日
 */
public interface Sender {

	public void send(Message message, int repeatTime, boolean sendImmediately);
	
	public boolean isSupport(String code);
}
