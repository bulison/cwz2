package cn.fooltech.fool_ops.domain.message.template.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplate;
import cn.fooltech.fool_ops.domain.message.template.repository.MessageTemplateRepository;

/**
 * <p>消息模板服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Service("ops.MessageTemplateService")
public class MessageTemplateService extends BaseService<MessageTemplate, MessageTemplate, String> {
	@Autowired
	MessageTemplateRepository repository;

	/**
	 * 根据业务类型、业务场景查找模板
	 * @param busClass
	 * @param busScene
	 * @param triggerType
	 * @return
	 */
	public List<MessageTemplate> queryTemplates(String busClass, String busScene, Integer triggerType){
		return repository.queryTemplates(busClass, busScene, triggerType.toString());
	}


	@Override
	public MessageTemplate getVo(MessageTemplate entity) {
		return null;
	}


	@Override
	public CrudRepository<MessageTemplate, String> getRepository() {
		return repository;
	}
	
}
