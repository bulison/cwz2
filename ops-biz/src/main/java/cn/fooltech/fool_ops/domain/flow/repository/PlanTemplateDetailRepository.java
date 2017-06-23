package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateDetail;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateDetailVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface PlanTemplateDetailRepository extends JpaRepository<PlanTemplateDetail, String>, FoolJpaSpecificationExecutor<PlanTemplateDetail> {

	/**
	 * 查询计划模板明细列表信息，按照计划模板明细number升序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<PlanTemplateDetail> query(PlanTemplateDetailVo vo,Pageable pageable){
		Page<PlanTemplateDetail> findAll = findAll(new Specification<PlanTemplateDetail>() {
			
			@Override
			public Predicate toPredicate(Root<PlanTemplateDetail> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("dept").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 查询计划模板明细列表信息，按照计划模板明细number升序排列<br>
	 * @param vo
	 */
	@Query("select a from PlanTemplateDetail a where dept.fid=?1 order by number")
	public List<PlanTemplateDetail> query(String orgId);
	/**
	 * 根据主表查询
	 * @return
	 */
	@Query("select a from PlanTemplateDetail a where planTemplate.fid=?1 order by number")
	public List<PlanTemplateDetail> queryByTemplateId(String templateId);
	
	/**
	 * 获取计划模板的里最大序号的
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	@Query("select a from PlanTemplateDetail a where planTemplate.fid=?1 and parent.fid is not null order by number desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PlanTemplateDetail getCodeMaxLengthByPlanTemplate(String planTemplateId);
	/**
	 * 获取计划模板的根明细
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	@Query("select a from PlanTemplateDetail a where planTemplate.fid=?1 and parent.fid is null")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PlanTemplateDetail getRootByPlanTemplate(String planTemplateId);

//	@Query("select a from Supervise a where warnSetting.fid=?1 and type=?2")
//	public List<Supervise> queryBySettingId(String settingId, Integer type);
//
//	@Query("select a from Supervise a where warnSetting.fid=?1")
//	public List<Supervise> queryBywarnSetting(String settingId);
	/**
	 * 查找计划模板明细中是否有同名或者同序号
	 */
	@Query("select count(*) from PlanTemplateDetail a where planTemplate.fid=?1 and number=?2 and fid!=?3")
	public Long countByShort(String planTemplateId,Short number,String fid);
	@Query("select count(*) from PlanTemplateDetail a where planTemplate.fid=?1 and taskName=?2 and fid!=?3")
	public Long countByTaskName(String planTemplateId,String taskName,String fid);
}	

