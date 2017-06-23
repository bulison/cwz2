package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.report.entity.SysFilter;
import cn.fooltech.fool_ops.domain.report.repository.SysFilterRepository;
import cn.fooltech.fool_ops.domain.report.repository.SysReportRepository;
import cn.fooltech.fool_ops.domain.report.vo.SysFilterVo;
import cn.fooltech.fool_ops.utils.VoFactory;


/**
 * <p>数据分析查询条件网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-10-12 13:39:56
 */
@Service
public class SysFilterService extends BaseService<SysFilter, SysFilterVo, String> {
	
	/**
	 * 数据分析查询条件服务类
	 */
	@Autowired
	private SysFilterRepository filterRepo;
	
	/**
	 * 报表服务类
	 */
	@Autowired
	private SysReportRepository reportRepo;
	
	
	/**
	 * 查询数据分析查询条件列表信息，按照数据分析查询条件序号升序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 */
	public Page<SysFilterVo> query(SysFilterVo vo,PageParamater paramater){
		
		String reportId = vo.getSysReportId();
		Assert.notNull(reportId);
		
		Sort sort = new Sort(Direction.ASC, "order");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		return getPageVos(filterRepo.findPageByReportId(reportId, pageRequest), pageRequest);
	}
	
	/**
	 * 根据报表ID查询数据分析查询条件列表信息
	 */
	public List<SysFilterVo> getByReportId(String reportId){
		Sort sort = new Sort(Direction.ASC, "order");
		return getVos(filterRepo.findByReportId(reportId, sort));
	}
	
	
	/**
	 * 单个数据分析查询条件实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public SysFilterVo getVo(SysFilter entity){
		if(entity == null)
			return null;
		SysFilterVo vo = VoFactory.createValue(SysFilterVo.class, entity);
		if(entity.getNeed()!=null&&entity.getNeed()==SysFilter.NEED){
			vo.setNeed(Boolean.TRUE);
		}else{
			vo.setNeed(Boolean.FALSE);
		}
		if(entity.getShow()==null){
			vo.setShow(SysFilter.SHOW);
		}else{
			vo.setShow(entity.getShow());
		}
		vo.setSysReportId(entity.getSysReport().getFid());
		vo.setSysReportName(entity.getSysReport().getReportName());
		
		return vo;
	}
	
	/**
	 * 新增/编辑数据分析查询条件
	 * @param vo
	 */
	public RequestResult save(SysFilterVo vo) {
		
		if(StringUtils.isBlank(vo.getSysReportId())){
			return buildFailRequestResult("请选择关联报表");
		}
		
		if(StringUtils.isBlank(vo.getFid())){
			SysFilter entity = VoFactory.createValue(SysFilter.class, vo);
			
			entity.setSysReport(reportRepo.findOne(vo.getSysReportId()));
			
			filterRepo.save(entity);
		}else {
			SysFilter entity = filterRepo.findOne(vo.getFid());
			entity.setTableName(vo.getTableName());
			entity.setFieldName(vo.getFieldName());
			entity.setAliasName(vo.getAliasName());
			entity.setDisplayName(vo.getDisplayName());
			entity.setInputType(vo.getInputType());
			entity.setMark(vo.getMark());
			entity.setCompare(vo.getCompare());
			entity.setValue(vo.getValue());
			entity.setOrder(vo.getOrder());
			
			if(vo.getNeed()!=null&&vo.getNeed()){
				entity.setNeed(SysFilter.NEED);
			}else{
				entity.setNeed(SysFilter.UN_NEED);
			}
			
			if(vo.getShow()==null){
				entity.setShow(SysFilter.SHOW);
			}else{
				entity.setShow(vo.getShow());
			}
			
			entity.setSysReport(reportRepo.findOne(vo.getSysReportId()));
			
			filterRepo.save(entity);
		}
		
		return buildSuccessRequestResult();
	}

	@Override
	public CrudRepository<SysFilter, String> getRepository() {
		return filterRepo;
	}

}
