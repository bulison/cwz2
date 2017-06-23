package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.sysman.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

	@Query("select r from Role r where r.orgId.fid=?1 and r.validation=1")
	public Page<Role> findPageByOrgId(String orgId, Pageable pageable);
	
	/**
	 * 根据角色名称查找角色
	 * @param roleName
	 * @return
	 */
	public List<Role> findByRoleName(String roleName);
}
