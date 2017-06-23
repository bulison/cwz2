package cn.fooltech.fool_ops.web.report;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.report.service.SysReportQueryService;
import cn.fooltech.fool_ops.domain.report.service.SysReportService;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import cn.fooltech.fool_ops.domain.report.vo.SysReportVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.WebResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * <p>系统报表查询网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年10月12日
 */
@Controller
@RequestMapping("/report")
public class SysReportQueryController {
	
	/**
	 * 系统报表查询网页服务类
	 */
	@Autowired
	private SysReportQueryService queryService;
	
	/**
	 * 报表网页服务类
	 */
	@Autowired
	private SysReportService reportService;
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @param model
	 * @return
	 */
	@RequestMapping("/query")
	@SuppressWarnings("rawtypes")
	public String query(SysReportQueryVo vo, PageParamater paramater, Model model){
		ResultObject result = queryService.query(vo, paramater);
		model.addAttribute("result", JsonUtil.toJsonString(result));
		return "/report/reportResult";
	}
	
	/**
	 * 导出报表
	 * add by xjh
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(SysReportQueryVo vo, HttpServletResponse response) throws Exception{
		
		vo.setCondition(URLDecoder.decode(vo.getCondition(),"utf-8"));
		
		List<Object[]> datas = queryService.queryAll(vo);
		SysReportVo report = reportService.getById(vo.getSysReportId());
		String titles[] = reportService.getReportTitle(vo.getSysReportId());
		ExcelUtils.exportExcel(titles, datas, response, report.getReportName()+".xls");
	}
}
