package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplate;

public interface FiscalTemplateRepository extends JpaRepository<FiscalTemplate, String>, JpaSpecificationExecutor<FiscalTemplate> {

	
	@Query("select f from FiscalTemplate f where templateType.fid=?1 order by code")
	public List<FiscalTemplate> findByTempateType(String typeId);

}
