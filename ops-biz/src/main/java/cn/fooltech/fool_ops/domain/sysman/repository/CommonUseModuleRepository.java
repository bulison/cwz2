package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.sysman.entity.CommonUseModule;

public interface CommonUseModuleRepository extends JpaRepository<CommonUseModule, String>, JpaSpecificationExecutor<CommonUseModule> {

	/**
	 * 根据创建人查询
	 * @param userId
	 * @return
	 */
	@Query("select c from CommonUseModule c where c.creator.fid=?1")
	public List<CommonUseModule> findByCreatorId(String userId);

}
