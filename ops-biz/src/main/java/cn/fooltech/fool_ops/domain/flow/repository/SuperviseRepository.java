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
import cn.fooltech.fool_ops.domain.flow.entity.Supervise;
import cn.fooltech.fool_ops.domain.flow.vo.MsgWarnSettingVo;
import cn.fooltech.fool_ops.domain.flow.vo.SuperviseVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface SuperviseRepository extends JpaRepository<Supervise, String>, FoolJpaSpecificationExecutor<Supervise> {

	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<Supervise> query(SuperviseVo vo,Pageable pageable){
		Page<Supervise> findAll = findAll(new Specification<Supervise>() {
			
			@Override
			public Predicate toPredicate(Root<Supervise> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 根据消息预警配置ID查询监督人
	 * @return
	 */
	@Query("select a from Supervise a where warnSetting.fid=?1 and type=?2")
	public List<Supervise> queryBySettingId(String settingId, Integer type);

	@Query("select a from Supervise a where warnSetting.fid=?1")
	public List<Supervise> queryBywarnSetting(String settingId);
	
}	

