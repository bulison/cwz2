package cn.fooltech.fool_ops.domain.fiscal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplate;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalTemplateRepository;

/**
 * <p>财务-科目模板服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-11-26 11:38:38
 */
@Service
public class FiscalTemplateService extends AbstractBaseService<FiscalTemplate, String> {

	@Autowired
	private FiscalTemplateRepository templateRepository;


	@Override
	public CrudRepository<FiscalTemplate, String> getRepository() {
		return templateRepository;
	}
	
	
	/**
	 * 根据模板类型查找对应的模板
	 * @param @return
	 */
	public List<FiscalTemplate> findByTempateType(String typeId){
		return templateRepository.findByTempateType(typeId);
	}
}
