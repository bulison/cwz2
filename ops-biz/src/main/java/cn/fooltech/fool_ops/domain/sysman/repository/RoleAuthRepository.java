package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.RoleAuth;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAuth;

public interface RoleAuthRepository extends JpaRepository<RoleAuth, String>, JpaSpecificationExecutor<RoleAuth>{

	/**
	 * 根据角色和资源s查找角色资源
	 * @param role
	 * @param resList
	 * @return
	 */
	public default List<RoleAuth> findByRoleAndResources(final Role role, final List<Resource> resList) {
		return findAll(new Specification<RoleAuth>() {
			@Override
			public Predicate toPredicate(Root<RoleAuth> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<Role>get("role"), role));
				predicates.add(root.<Resource>get("resource").in(resList));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
	}
	
	/**
	 * 根据角色和资源ids查找角色资源
	 * @param role
	 * @param resIds
	 * @return
	 */
	@Query("select r from RoleAuth r where r.role.fid=?1 and resource.fid in ?2")
	public List<RoleAuth> findByRoleIdAndResourceIds(final String roleId, final List<String> resIds);

}
