package cn.fooltech.fool_ops.web.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.report.service.SysReportService;
import cn.fooltech.fool_ops.domain.report.vo.SysReportVo;

/**
 * 
 * @author tjr
 * 
 */
@Controller
@RequestMapping("/report/SysRepor/")
public class SysReportController {
	
	@Autowired
	private SysReportService sysReportService;
	
	/**
	 * 管理界面
	 * @param reportId
	 * @param request
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(String reportId,HttpServletRequest request) {
		String reportName= sysReportService.get(reportId).getReportName();
		request.setAttribute("reportName", reportName);
		return "/report/report";
	}
	
	/**
	 * 按钮页面
	 * @param reportId
	 * @param request
	 * @return
	 */
	@RequestMapping("/button")
	public String button(String reportId,HttpServletRequest request) {
		request.setAttribute("reportId", reportId);
		return "/report/reportButton";
	}
	
	/**
	 * 获取树状结构数据
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getTree")
	public List<CommonTreeVo> getTree() throws Exception {
		return sysReportService.getTree();
	}
	
	/**
	 * 获取报表列表
	 * @param vo
	 * @param paramater
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<SysReportVo> list(SysReportVo vo){
		return sysReportService.list(vo);
	}
	
	/**
	 * 详细界面
	 * @param fid
	 * @param request
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String fid,HttpServletRequest request){
		SysReportVo vo = sysReportService.getById(fid);
		request.setAttribute("vo", vo);
		return "/report/reportType/addReportType";
	}
	
	/**
	 * 新增、编辑报表
	 */
	@ResponseBody
	@RequestMapping("/saveReport")
	public RequestResult saveReport(SysReportVo vo)throws Exception{
		return sysReportService.save(vo);
	}
	
	/**
	 * 删除报表
	 */
	@ResponseBody
	@RequestMapping("/deleteReport")
	public RequestResult deleteReport(SysReportVo vo)throws Exception{
		return sysReportService.delete(vo.getFid());
	}
	
}
