package cn.fooltech.fool_ops.domain.flow.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import cn.fooltech.fool_ops.domain.flow.entity.TaskType;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface TaskTypeRepository extends JpaRepository<TaskType, String>, FoolJpaSpecificationExecutor<TaskType> {

	/**
	 * 根据编号或名称查询企业事件类型
	 * @param orgId
	 * @param searchKey
	 * @param limit
	 * @return List<TaskType>
	 */
	public default List<TaskType> findBySearchKey(final String orgId, final String searchKey, final Integer limit) {

		Order[] orders = { new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name") };
		Sort sort = new Sort(orders);

		return findAll(new Specification<TaskType>() {

			@Override
			public Predicate toPredicate(Root<TaskType> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));

				if (StringUtils.isNotBlank(searchKey)) {
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
	 * 根据编号或名称分页查询企业事件类型
	 * @param orgId
	 * @param searchKey
	 * @param page
	 * @return Page<TaskType>
	 */
	public default Page<TaskType> findAllTaskType(final String orgId, final String searchKey, final Pageable page){
		return findAll(new Specification<TaskType>() {
			
			@Override
			public Predicate toPredicate(Root<TaskType> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
	 * 
	 * @param orgId
	 * @param code
	 * @return count
	 */
	@Query("select count(*) from TaskType t where t.org.fid=?1 and t.code=?2")
	public Long countByCode(String orgId, String code);

	/**
	 * 根据编号统计个数，用于判断编号是否重复
	 * 
	 * @param orgId
	 * @param code
	 * @param excludeId
	 * @return count
	 */
	@Query("select count(*) from TaskType t where t.org.fid=?1 and t.code=?2 and t.fid!=?3")
	public Long countByCode(String orgId, String code, String excludeId);
	/**
	 * 根据名称统计个数,用于判断名称是否重复
	 * @param orgId
	 * @param name
	 * @return
	 */
	@Query("select count(*) from TaskType t where t.org.fid=?1 and t.name=?2 and t.fid!=?3")
	public Long countByName(String orgId, String name,String excludeId);
	/**
	 * 	 * 根据名称统计个数,用于判断名称是否重复
	 * @param orgId
	 * @param name
	 * @return
	 */
	@Query("select count(*) from TaskType t where t.org.fid=?1 and t.name=?2")
	public Long countByName(String orgId, String name);
	/**
	 * 判断是否被引用
	 * @param id
	 * @return
	 */
	@Procedure(procedureName="p_check_tasktype_reference")
	public Long isReferenced(@Param("fid") String fid);
	
	/**
	 * 判断是否被引用
	 * @param fid
	 * @return true 是  false 否
	 */
//	public default boolean isReferenced(EntityManager entityManager, String fid){
//		String sql = "select check_tasktype_reference(\""+fid+"\")";
//		javax.persistence.Query query = entityManager.createNativeQuery(sql);
//		
//		/*String hql = "select check_tasktype_reference(？)";
//		javax.persistence.Query query = entityManager.createQuery(hql);
//		query.setParameter(1, fid);*/
//		
//		
//		List data = query.getResultList();
//		if (CollectionUtils.isNotEmpty(data)){
//			BigInteger total = (BigInteger)data.get(0);
//			return total.intValue() > 0;
//		}
//		return false;
//	}
	
}	

