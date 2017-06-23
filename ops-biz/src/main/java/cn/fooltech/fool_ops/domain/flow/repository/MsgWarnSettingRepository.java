package cn.fooltech.fool_ops.domain.flow.repository;

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
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.vo.MsgWarnSettingVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface MsgWarnSettingRepository
		extends JpaRepository<MsgWarnSetting, String>, FoolJpaSpecificationExecutor<MsgWarnSetting> {

	/**
	 * 查询消息预警配置列表信息，按照消息预警配置taskType排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	public default Page<MsgWarnSetting> query(MsgWarnSettingVo vo, Pageable pageable) {
		Page<MsgWarnSetting> findAll = findAll(new Specification<MsgWarnSetting>() {

			@Override
			public Predicate toPredicate(Root<MsgWarnSetting> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				if (vo.getTaskType() != null) {
					predicates.add(builder.equal(root.get("taskType"), vo.getTaskType()));
				}
				if (vo.getType() != null) {
					predicates.add(builder.equal(root.get("type"), vo.getType()));
				}
				if (vo.getIsSystem() != null) {
					predicates.add(builder.equal(root.get("isSystem"), vo.getIsSystem()));
				}
				if (vo.getSendType() != null) {
					predicates.add(builder.equal(root.get("sendType"), vo.getSendType()));
				}
				if (vo.getTriggerType() != null) {
					predicates.add(builder.equal(root.get("triggerType"), vo.getTriggerType()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		return findAll;
	}

	/**
	 * 查询消息预警配置列表信息
	 * @param taskLevel
	 * @param triggerType
	 * @param type
	 * @param orgId
	 * @return
	 */
	@Query("select a from MsgWarnSetting a where a.taskLevel>=?1 and a.triggerType=?2 and a.type=?3 and a.org.fid=?4")
	public List<MsgWarnSetting> query(Integer taskLevel, Integer triggerType, Integer type, String orgId);


	/**
	 * 获取重发天数
	 * @param taskLevel
	 * @param triggerType
	 * @param type
	 * @param orgId
	 * @return
	 */
	@Query("select min(a.retryDays) from MsgWarnSetting a where a.retryDays is not null and a.taskLevel>=?1 and a.triggerType=?2 and a.type=?3 and a.org.fid=?4")
	public Integer getRetryDays(Integer taskLevel, Integer triggerType, Integer type, String orgId);

	/**
	 * 获取提前提醒的天数
	 * @param taskLevel
	 * @param triggerType
	 * @param type
	 * @param orgId
	 * @return
	 */
	@Query("select min(a.days) from MsgWarnSetting a where a.days is not null and a.taskLevel>=?1 and a.triggerType=?2 and a.type=?3 and a.org.fid=?4 and a.busScene=?5")
	public Integer getForwardDays(Integer taskLevel, Integer triggerType, Integer type, String orgId, String busScene);
}
