package cn.fooltech.fool_ops.domain.message.template.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;
import cn.fooltech.fool_ops.domain.message.template.repository.MessageTemplateParamaterRepository;

/**
 * <p>消息模板参数服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Service
public class MessageTemplateParamaterService extends BaseService<MessageTemplateParamater, MessageTemplateParamater, String>{
	
	@Autowired
	MessageTemplateParamaterRepository repository;
	/**
	 * 根据业务类型、业务场景查找模板
	 * @param busClass
	 * @param busScene
	 * @param orgId
	 * @return
	 */
	public List<MessageTemplateParamater> queryByTemplateId(String templateId){
		return repository.queryByTemplateId(templateId);
	}
	@Override
	public MessageTemplateParamater getVo(MessageTemplateParamater entity) {
		return null;
	}
	@Override
	public CrudRepository<MessageTemplateParamater, String> getRepository() {
		return repository;
	}

}
