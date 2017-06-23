package cn.fooltech.fool_ops.domain.message.template.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.MsgWarnSetting;
import cn.fooltech.fool_ops.domain.flow.entity.Supervise;
import cn.fooltech.fool_ops.domain.flow.vo.MsgWarnSettingVo;
import cn.fooltech.fool_ops.domain.flow.vo.SuperviseVo;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplate;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, String>, FoolJpaSpecificationExecutor<MessageTemplate> {

	/**
	 * 根据业务类型、业务场景查找模板
	 * @param busClass
	 * @param busScene
	 * @param triggerType
	 * @return
	 */
	@Query("select a from MessageTemplate a where a.busClass=?1 and a.busScene=?2 and a.triggerType=?3")
	public List<MessageTemplate> queryTemplates(String busClass, String busScene, String triggerType);
	
}	

