package cn.fooltech.fool_ops.web.report;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 报表查询条件配置控制器
 * @author lzf
 */
@Controller
@RequestMapping ("/resultType")
public class ResultTypeController{
	//管理界面
	@RequestMapping("/manage")
	public String manage(){
		return "/report/reportType/reportType";
	}
	
	//添加查询条件
	@RequestMapping("/list")
	public String list(String reportId,HttpServletRequest request){
		request.setAttribute("reportId", reportId);
		return "/report/reportType/addReportType";
	}
}
