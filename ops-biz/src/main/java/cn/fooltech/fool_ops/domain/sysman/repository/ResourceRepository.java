package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.sysman.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

	/**
	 * 查找菜单
	 * @param isAdmin
	 * @return
	 */
	public default List<Resource> findMenuWithOrgLevel(boolean isAdmin){
		
		final ImmutableList<Short> resTypes = ImmutableList.of((short) 0, (short) 1);
		
		return findAll(new Specification<Resource>() {
			
			@Override
			public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<Integer>get("resApp"), 1));
				predicates.add(builder.equal(root.<Short>get("validation"), (short)1));
				predicates.add(root.<Short>get("resType").in(resTypes));
				if(isAdmin){
					predicates.add(builder.equal(root.<Short>get("permType"), (short)1));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		//}, new Sort(Lists.newArrayList(new Order(Direction.ASC, "rankOrder"))));
		}, new Sort(Direction.ASC, "rankOrder"));
	}
	
	/**
	 * 获取子资源数据
	 * @param fId 资源Id
	 * @return
	 */
	@Query("select r from Resource r where r.parent.fid=?1 order by r.rankOrder asc")
	public List<Resource> getChildResources(String fid);
	
	/**
	 * 根据合法字段查找资源
	 * @param validation
	 * @return
	 */
	public List<Resource> findByValidation(short validation);

	/**
	 * 检查资源编号
	 * @param code
	 * @param excludeId
	 * @param neResType
	 * @return
	 */
	public default Long countByResourceCode(String code, String excludeId, Short neResType){
		
		return count(new Specification<Resource>() {
			
			@Override
			public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<String>get("code"), code));
				predicates.add(builder.equal(root.<Short>get("validation"), (short)1));
				if(neResType != null){
					predicates.add(builder.notEqual(root.<Short>get("neResType"), neResType));
				}
				if(!Strings.isNullOrEmpty(excludeId)){
					predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
	}

	/**
	 * 查找所有有效资源
	 * @param sort
	 * @return
	 */
	public default List<Resource> findAllValid(Sort sort){
		return findAll(new Specification<Resource>() {
			@Override
			public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<Short>get("validation"), (short)1));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		}, sort);
	}
}
