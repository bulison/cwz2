package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.StringUtils;

public interface TaskLevelRepository extends JpaRepository<TaskLevel, String>, FoolJpaSpecificationExecutor<TaskLevel>{

	/**
	 * 根据编号或名称查询企业事件级别
	 * @Param orgId
	 * @Param searchKey
	 * @Param limit
	 * @return List<SecurityLevel>
	 */
	public default List<TaskLevel> findBySearchKey(final String orgId, final String searchKey, final Integer limit) {
		
		Order[] orders = {new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name") };
		Sort sort = new Sort(orders);
		
		return findAll(new Specification<TaskLevel>() {
			
			@Override
			public Predicate toPredicate(Root<TaskLevel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("checkoutStatus"), TaskLevel.STATUS_USED));
				if (StringUtils.isNotBlank(searchKey)){
					String key = PredicateUtils.getAnyLike(searchKey);
					predicates.add(builder.or(
							builder.like(root.get("code"), key),
							builder.like(root.get("name"), key)
						));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		}, sort, limit);
	}
	
	/**
	 * 根据编号或名称分页查询企事业事件级别
	 * @Param orgId
	 * @Param searchKey
	 * @Param page
	 * @Return Page<TaskLevel>
	 */
	public default Page<TaskLevel> findAllTaskLevel(final String orgId, final String searchKey, final Pageable page) {
		
		return findAll(new Specification<TaskLevel>() {
			
			@Override
			public Predicate toPredicate(Root<TaskLevel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				if (StringUtils.isNotBlank(searchKey)){
					String key = PredicateUtils.getAnyLike(searchKey);
					predicates.add(builder.or(
							builder.like(root.get("code"), key),
							builder.like(root.get("name"), key)
						));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
				return predicate;
			}
		}, page);
	}
	
	/**
	 * 根据编号统计个数，用于判断编号是否重复
	 * @param orgId
	 * @param code
	 * @return count
	 */
	@Query("select count(*) from TaskLevel t where t.org.fid=?1 and t.code=?2")
	public Long countByCode(String orgId, String code);

	/**
	 * 根据编号统计个数，用于判断编号是否重复
	 * @param orgId
	 * @param code
	 * @param excludeId
	 * @return count
	 */
	@Query("select count(*) from TaskLevel t where t.org.fid=?1 and t.code=?2 and t.fid!=?3")
	public Long countByCode(String orgId, String code, String excludeId);
	/**
	 * 根据名称统计个数,用于判断名称是否重复
	 * @param orgId
	 * @param name
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from TaskLevel t where t.org.fid=?1 and t.name=?2 and t.fid!=?3")
	public Long countByName(String orgId, String name, String excludeId);
	
	/**
	 * 根据名称统计个数,用于判断名称是否重复
	 * @param orgId
	 * @param name
	 * @return
	 */
	@Query("select count(*) from TaskLevel t where t.org.fid=?1 and t.name=?2")
	public Long countByName(String orgId, String name);
	
	/**
	 * 判断是否被引用
	 * @param id
	 * @return
	 */
	@Procedure(procedureName="p_check_tasklevel_reference")
	public Long isReferenced(@Param("fid") String fid);
	/**
	 * 判断是否被启用
	 * @param fid
	 * @return
	 */
	@Query("select count(*) from TaskLevel t where t.fid=?1 and t.checkoutStatus=1")
	public Long isStart(String fid);
}

