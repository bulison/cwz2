package cn.fooltech.fool_ops.domain.fiscal.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplateType;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalTemplateTypeRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalTemplateTypeVo;

/**
 * <p>财务-科目模板类型网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-11-26 11:39:12
 */
@Service
public class FiscalTemplateTypeService extends BaseService<FiscalTemplateType,FiscalTemplateTypeVo,String> {
	
	/**
	 * 财务-科目模板类型服务类
	 */
	@Autowired
	private FiscalTemplateTypeRepository fiscalTemplateTypeRepository;
	
	
	/**
	 * 查询财务-科目模板类型列表信息，按照财务-科目模板类型主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public List<FiscalTemplateTypeVo> queryAll(){
		return getVos(fiscalTemplateTypeRepository.findAll());
	}
	
	
	/**
	 * 单个财务-科目模板类型实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public FiscalTemplateTypeVo getVo(FiscalTemplateType entity){
		if(entity == null)
			return null;
		FiscalTemplateTypeVo vo = new FiscalTemplateTypeVo();
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setFid(entity.getFid());
		
		return vo;
	}
	
	/**
	 * 获取财务-科目模板类型信息
	 * @param fid 财务-科目模板类型ID
	 * @return
	 */
	public FiscalTemplateTypeVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(fiscalTemplateTypeRepository.findOne(fid));
	}


	@Override
	public CrudRepository<FiscalTemplateType, String> getRepository() {
		return fiscalTemplateTypeRepository;
	}

}
