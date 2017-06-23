package cn.fooltech.fool_ops.web.report;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.service.SysFilterService;
import cn.fooltech.fool_ops.domain.report.service.SysReportService;
import cn.fooltech.fool_ops.domain.report.vo.SysFilterVo;
import cn.fooltech.fool_ops.domain.report.vo.SysReportVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * 报表查询条件配置控制器
 * @author lzf
 */
@Controller
@RequestMapping ("/reportFilter")
public class FilterController extends BaseController{
	
	@Autowired
	SysFilterService sysFilterWebService;
	@Autowired
	SysReportService sysReportWebService;
	@Autowired
	SysReportService sysReportService;
	
	
	//管理界面
	@RequestMapping("/manage")
	public String manage(){
		return "/report/filter/filter";
	}
	
	//添加查询条件列表
	@RequestMapping("/list")
	public String list(String reportId,HttpServletRequest request){
		request.setAttribute("reportId", reportId);
		return "/report/filter/filterList";
	}
	
	//添加报表类型界面
	@RequestMapping("/addReportType")
	public String addReportType(){
		return "/report/resultType/addReportType";
	}
	
	//修改报表类型界面
	@RequestMapping("/editReportType")
	public String editReportType(SysReportVo vo,HttpServletRequest request){
		SysReport entity=sysReportService.get(vo.getFid());
		request.setAttribute("entity", entity);
		return "/report/filter/addReportType";
	}
	
	/**
	 * 根据report id 获取where条件
	 * @param reportId
	 * @throws Exception
	 * @author tjr
	 */
	@ResponseBody
	@RequestMapping("/getWhereByReportId")
	public PageJson getWhereByReportId(String reportId)throws Exception{
		List<SysFilterVo> list = sysFilterWebService.getByReportId(reportId);
		SysReport report=sysReportService.get(reportId);
		Map<String, Object> map = Maps.newHashMap();
		map.put("showPage", report.getShowPage());
		
		PageJson pageJson = new PageJson();
		pageJson.setTotal(0L);
		pageJson.setRows(list);
		pageJson.setOther(map);
		return  pageJson;
	}
	
	/**
	 * 查询所有查询条件
	 */
	@ResponseBody
	@RequestMapping("/queryFilter")
	public PageJson queryFilter(SysFilterVo vo,PageParamater paramater){
		Page<SysFilterVo> pager = sysFilterWebService.query(vo, paramater);
		return new PageJson(pager);
	}
	
	/**
	 * 删除查询条件
	 */
	@ResponseBody
	@RequestMapping("/deleteFilter")
	public String deleteFilter(SysFilterVo vo,PageParamater paramater){
		sysFilterWebService.delete(vo.getFid());
		return "1";
	}
	
	/**
	 * 保存查询条件
	 */
	@ResponseBody
	@RequestMapping("/saveFilter")
	public RequestResult saveFilter(SysFilterVo vo,PageParamater paramater){
		return sysFilterWebService.save(vo);
	}
	
}
