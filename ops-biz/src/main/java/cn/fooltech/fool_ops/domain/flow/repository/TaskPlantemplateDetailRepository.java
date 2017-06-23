package cn.fooltech.fool_ops.domain.flow.repository;

import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.TaskPlantemplateDetail;

public interface TaskPlantemplateDetailRepository
		extends FoolJpaRepository<TaskPlantemplateDetail, String>, FoolJpaSpecificationExecutor<TaskPlantemplateDetail> {
/**
 * 查找是引用了计划模板
 * @param id
 * @return
 */
/*	@Query("select count(*) from TaskPlantemplateDetail t where t.planTemplate.fid=?1")
	public Long countPlantemplateId(String id);*/

}
