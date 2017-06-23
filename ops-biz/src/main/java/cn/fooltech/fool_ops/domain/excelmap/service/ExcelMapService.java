package cn.fooltech.fool_ops.domain.excelmap.service;

import java.util.List;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.excelmap.entity.ExcelMap;
import cn.fooltech.fool_ops.domain.excelmap.repository.ExcelMapRepository;
import cn.fooltech.fool_ops.domain.excelmap.vo.ExcelMapVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Excel关系映射服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月23日
 */
@Service
public class ExcelMapService extends BaseService<ExcelMap, ExcelMapVo, String> {
	
	@Autowired
	private ExcelMapRepository execMapRepo;
	
	/**
	 * 查询分页, 默认每页10个元素
	 */
	public Page<ExcelMapVo> query(ExcelMapVo vo, PageParamater paramater){
		
		Sort sort = new Sort(Direction.DESC, "clazz");
		PageRequest request = getPageRequest(paramater, sort);
		
		String clazz = vo.getClazz();
		String cnName = vo.getCnName();
		String field = vo.getField();
		Integer type = vo.getType();
		
		return getPageVos(execMapRepo.findPageBy(clazz, cnName, field, type, request), request);
	}

	/**
	 * 通过Class的全名称查找所有匹配的映射
	 * @param clazz:类的全路径
	 * @param type:类型标识
	 * @param fexport:导出标识
	 * @param fimport:导入标识
	 */
	public List<ExcelMapVo> findByClazz(Class clazz, Integer type, boolean fexport, boolean fimport){
		Sort sort = new Sort(Direction.ASC, "sequence");
		String clazzStr = clazz.getName();
		return getVos(execMapRepo.findBy(clazzStr, type, fexport, fimport, sort));
	}
	
	/**
	 * 修改/新增
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(ExcelMapVo vo){
		ExcelMap entity = null;
		if(Strings.isNullOrEmpty(vo.getFid())){
			entity = new ExcelMap();
		}else{
			entity = execMapRepo.findOne(vo.getFid());
		}
		entity.setClazz(vo.getClazz());
		entity.setCnName(vo.getCnName());
		entity.setField(vo.getField());
		entity.setProcessor(vo.getProcessor());
		entity.setSequence(vo.getSequence());
		entity.setType(vo.getType());
		entity.setNeed(vo.getNeed()!=null&&vo.getNeed()>0?true:false);
		entity.setFexport(vo.getFexport()!=null&&vo.getFexport()>0?true:false);
		entity.setFimport(vo.getFimport()!=null&&vo.getFimport()>0?true:false);
		entity.setValidation(vo.getValidation()!=null&&vo.getValidation()>0?true:false);
		
		execMapRepo.save(entity);
		return buildSuccessRequestResult(getVo(entity));
	}

	@Override
	public ExcelMapVo getVo(ExcelMap entity) {
		ExcelMapVo vo = new ExcelMapVo();
		vo.setFid(entity.getFid());
		vo.setClazz(entity.getClazz());
		vo.setCnName(entity.getCnName());
		vo.setField(entity.getField());
		vo.setProcessor(entity.getProcessor());
		vo.setSequence(entity.getSequence());
		vo.setType(entity.getType());
		vo.setNeed(entity.getNeed()!=null&&entity.getNeed()?1:0);
		vo.setFexport(entity.getFexport()!=null&&entity.getFexport()?1:0);
		vo.setFimport(entity.getFimport()!=null&&entity.getFimport()?1:0);
		vo.setValidation(entity.getValidation()!=null&&entity.getValidation()?1:0);
		return vo;
	}

	@Override
	public CrudRepository<ExcelMap, String> getRepository() {
		return this.execMapRepo;
	}
}
