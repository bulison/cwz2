package cn.fooltech.fool_ops.domain.message.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.message.entity.MessageParamater;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;

public interface MessageTemplateParamaterRepository extends JpaSpecificationExecutor<MessageTemplateParamater>, FoolJpaRepository<MessageTemplateParamater,String> {
	
	/**
	 * 根据业务类型、业务场景查找模板
	 * @param busClass
	 * @param busScene
	 * @param orgId
	 * @return
	 */
	@Query("select a from MessageTemplateParamater a where template.fid=?1")
	public List<MessageTemplateParamater> queryByTemplateId(String templateId);

	
}	

