package cn.fooltech.fool_ops.domain.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.repository.SysFilterRepository;
import cn.fooltech.fool_ops.domain.report.repository.SysReportRepository;
import cn.fooltech.fool_ops.domain.report.repository.SysReportSqlRepository;
import cn.fooltech.fool_ops.domain.report.repository.UserTemplateDetailRepository;
import cn.fooltech.fool_ops.domain.report.vo.SysReportVo;
import cn.fooltech.fool_ops.utils.TreeDataUtil;
import cn.fooltech.fool_ops.utils.VoFactory;


/**
 * <p></p>
 * @author xjh
 * @version 1.0
 * @date 2015年10月12日
 */
@Service
public class SysReportService extends BaseService<SysReport, SysReportVo, String>{

	@Autowired
	private SysReportRepository reportRepo;
	
	@Autowired
	private SysReportSqlRepository reportSqlRepo;
	
	@Autowired
	private SysFilterRepository filterRepo;
	
	@Autowired
	private UserTemplateDetailRepository templateDetailRepo;
	
	/**
	 * 查询系统报表的分页, 默认每页10个元素
	 */
	public Page<SysReportVo> query(SysReportVo vo, PageParamater paramater){
		Integer code = vo.getCode();
		String parentId = vo.getParentId();
		String reportName = vo.getReportName();
		
		Sort sort = new Sort(Direction.DESC, "reportName");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		return getPageVos(reportRepo.findPageBy(code, parentId, reportName, pageRequest), pageRequest);
	}
	
	/**
	 * 获取报表列表
	 * @param vo
	 * @return
	 */
	public List<SysReportVo> list(SysReportVo vo){
		Integer code = vo.getCode();
		String parentId = vo.getParentId();
		String reportName = vo.getReportName();
		Sort sort = new Sort(Direction.DESC, "reportName");
		List<SysReport> entities = reportRepo.findBy(code, parentId, reportName, sort);
		return getVos(entities);
	}
	
	/**
	 * 获取树状结构数据
	 */
	public List<CommonTreeVo> getTree(){
		Map<String, String> alias = new HashMap<String, String>();
		alias.put("id", "fid");
		alias.put("text", "reportName");
		alias.put("children", "childs");
		SysReport root = reportRepo.findRoot();
		TreeDataUtil<SysReport> util = new TreeDataUtil<SysReport>(alias, "reportName", true);
		return util.getTreeData(root);
	}
	
	
	/**
	 * 获取Vo
	 * @param entity
	 * @return 
	 */
	@Override
	public SysReportVo getVo(SysReport entity) {
		Assert.notNull(entity);
		SysReportVo vo = VoFactory.createValue(SysReportVo.class, entity);
		//上级报表
		SysReport parent = entity.getParent();
		if(parent != null){
			vo.setParentId(parent.getFid());
			vo.setParentName(parent.getReportName());
		}
		return vo;
	}
	
	/**
	 * 删除
	 * @param fid 报表ID
	 * @return
	 */
	public RequestResult delete(String fid){
		SysReport report = reportRepo.findOne(fid);
		if(CollectionUtils.isNotEmpty(report.getChilds())){
			return buildFailRequestResult("请先删除子报表!");
		}
		else if(filterRepo.countByReportId(fid) > 0){ 
			return buildFailRequestResult("请先删除已关联的查询条件!");
		}
		else if(templateDetailRepo.countByReportId(fid) > 0){
			return buildFailRequestResult("请先删除已关联的查询模板!");
		}
		else if(reportSqlRepo.countByReportId(fid) > 0){
			return buildFailRequestResult("请先删除已关联的查询语句!");
		}
		reportRepo.delete(report);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 保存/更新
	 */
	public RequestResult save(SysReportVo vo){
		String fid = vo.getFid();
		Integer code = vo.getCode();
		String headers = vo.getHeaders();
		String reportName = vo.getReportName();
		String countInfo = vo.getCountInfo();
		String parentId = vo.getParentId();
		Short showPage = vo.getShowPage();
		String javaScript = vo.getJavaScript();
		
		if(isCodeExist(vo.getParentId(), vo.getCode(), vo.getFid())){
			return buildFailRequestResult("汇总方式已存在!");
		}
		
		SysReport entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new SysReport();
		}
		else{
			entity = reportRepo.findOne(fid);
		}
		entity.setCode(code);
		entity.setHeaders(headers);
		entity.setReportName(reportName);
		entity.setShowPage(showPage);
		entity.setJavaScript(javaScript);
		entity.setCountInfo(countInfo);
		//上级报表
		if(StringUtils.isNotBlank(parentId)){
			SysReport parent = reportRepo.findOne(parentId);
			entity.setParent(parent);
		}
		
		reportRepo.save(entity);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断某个节点下，编号是否已存在
	 * @return
	 */
	public boolean isCodeExist(String parentId, Integer code, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = reportRepo.countByCode(parentId, code);
		}else{
			count = reportRepo.countByCode(parentId, code, excludeId);
		}
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取报表的表头
	 * @param sysReportId 报表ID
	 * @return
	 */
	public String[] getReportTitle(String sysReportId){
		
		SysReport entity = reportRepo.findOne(sysReportId);
		if(StringUtils.isNotBlank(entity.getHeaders())){
			return entity.getHeaders().split(",");
		}
		return new String[]{};
	}

	@Override
	public CrudRepository<SysReport, String> getRepository() {
		return reportRepo;
	}
	
}
