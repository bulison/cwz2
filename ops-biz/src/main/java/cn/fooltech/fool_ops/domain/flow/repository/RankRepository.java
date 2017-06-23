package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Criterion;
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
import cn.fooltech.fool_ops.domain.flow.entity.Rank;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface RankRepository extends JpaRepository<Rank, String>, FoolJpaSpecificationExecutor<Rank> {

	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<Rank> query(Rank vo,Pageable pageable){
		Page<Rank> findAll = findAll(new Specification<Rank>() {
			
			@Override
			public Predicate toPredicate(Root<Rank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 根据类型和业务ID查询评分
	 * @return
	 */
	@Query("select a from Rank a where type=?1 and businessId=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public Rank findOneByTypeAndBusinessId(Short type, String businessId);
	
	/**
	 * 根据类型和业务ID和评分人查询评分
	 * @return
	 */
	@Query("select a from Rank a where type=?1 and businessId=?2 and creator.fid=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public Rank findOneByTypeAndBusinessId(Short type, String businessId, String creatorId);
	
	
	@Query("select a from Rank a where businessId=?1")
	public List<Rank> findByBusinessId(String businessId);
	
	/**
	 * 判断用户是否对该计划或事件评过分
	 * @param type
	 * @param businessId 计划或事件的ID
	 * @param userId 用户ID
	 * @return
	 */
	@Query("select count(a) from Rank a where type=?1 and businessId=?2 and creator.fid=?3")
	public Long isRanked(Short type, String businessId, String userId);
}	

