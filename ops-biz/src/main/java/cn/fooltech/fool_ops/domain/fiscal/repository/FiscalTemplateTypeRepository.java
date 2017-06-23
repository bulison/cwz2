package cn.fooltech.fool_ops.domain.fiscal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplateType;

public interface FiscalTemplateTypeRepository extends JpaRepository<FiscalTemplateType, String>, JpaSpecificationExecutor<FiscalTemplateType> {

}
