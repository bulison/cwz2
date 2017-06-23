package cn.fooltech.fool_ops.web.report;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.service.MultiColumnReportService;
import cn.fooltech.fool_ops.domain.report.vo.MultiColumnReportVo;


/**
 * <p>多栏明细账报表控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月21日
 */
@Controller
@RequestMapping("/multiColumn/report")
public class MultiColumnReportController {
	
	/**
	 * 多栏明细账报表服务类
	 */
	@Autowired
	private MultiColumnReportService multiColumnService;
	
	/**
	 * 报表结果查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(MultiColumnReportVo vo, PageParamater paramater){
		return multiColumnService.query(vo, paramater);
	}
	
	/**
	 * 获取报表的表头
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTitles")
	public Map<String, Map<String, String>> getTitles(String settingId){
		Map<String, String> fixTitles = multiColumnService.getFixTitiles();
		Map<String, String> creditTitles = multiColumnService.getDynamicCreditTitles(settingId, fixTitles.size());
		Map<String, String> debitTitles = multiColumnService.getDynamicDebitTitles(settingId, fixTitles.size() + creditTitles.size());
		
		Map<String, String> sizes = new LinkedHashMap<String, String>();
		sizes.put("fixSize", String.valueOf(fixTitles.size()));
		sizes.put("creditSize", String.valueOf(creditTitles.size()));
		sizes.put("debitSize", String.valueOf(debitTitles.size()));
		
		Map<String, Map<String, String>> titles = new LinkedHashMap<String, Map<String,String>>();
		titles.put("fixTitle", fixTitles); //固定
		titles.put("creditTitle", creditTitles); //贷方
		titles.put("debitTitle", debitTitles); //借方
		titles.put("sizes", sizes);
		return titles;
	}
	
	@RequestMapping("/toList")
	public String toList(String settingId,HttpServletRequest request){
		Map<String, Map<String, String>> titles = getTitles(settingId);
		//MultiColumnReportResultVo multiColumnReportResultVo = query(vo,paramater);
		request.setAttribute("titles", titles);
		return "/report/fiscal/multiCcolumnSetting/reportList"; 
	}
	
}
