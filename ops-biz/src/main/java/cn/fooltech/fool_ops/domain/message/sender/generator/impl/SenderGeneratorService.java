package cn.fooltech.fool_ops.domain.message.sender.generator.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.domain.message.sender.factory.Sender;
import cn.fooltech.fool_ops.domain.message.sender.generator.ISenderGenerator;

/**
 * <p>根据code构造发送器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年5月19日
 */
@Service
public class SenderGeneratorService implements ISenderGenerator{
	
	/**
	 * 所有实现了SenderFactory的对象，自动注入
	 */
	@Autowired
	private Set<Sender> senders;

	
	@Override
	public Set<Sender> construct(String ...codes) {
		
		Set<Sender> supportSenders = Sets.newHashSet();
		for(String code:codes){
			for(Sender sender:senders){
				if(sender.isSupport(code)){
					supportSenders.add(sender);
					break;
				}
			}
		}
		return supportSenders;
	}
	
	/**
	 * 获得发送实现类
	* @param code:发送方式编码 
	* @throws
	 */
	public Sender getSuitSender(String code){
		for(Sender sender:senders){
			if(sender.isSupport(code)){
				return sender;
			}
		}
		return null;
	}

}
