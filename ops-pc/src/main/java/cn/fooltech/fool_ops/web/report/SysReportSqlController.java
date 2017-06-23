package cn.fooltech.fool_ops.web.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.report.service.SysReportSqlService;
import cn.fooltech.fool_ops.domain.report.vo.SysReportSqlVo;


/**
 * <p>系统报表查询SQL网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年10月12日
 */
@RequestMapping("/report/sql")
@Controller
public class SysReportSqlController {
	
	/**
	 * 系统报表查询SQL网页服务类
	 */
	@Autowired
	private SysReportSqlService sqlWebService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/report/sql/sql";
	}
	
	/**
	 * 查询语句列表
	 * @param reportId
	 * @param request
	 * @return
	 */
	@RequestMapping("/listPage")
	public String list(String reportId,HttpServletRequest request){
		request.setAttribute("reportId", reportId);
		return "/report/sql/sqlList";
	}
	
	/**
	 * 获取列表
	 * @param vo
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<SysReportSqlVo> list(SysReportSqlVo vo){
		SysReportSqlVo data = sqlWebService.findBySysReportId(vo.getSysReportId());
		return Lists.newArrayList(data);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(SysReportSqlVo vo){
		return sqlWebService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return sqlWebService.delete(id);
	}
	
	/**
	 * 明细
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		SysReportSqlVo vo = sqlWebService.getById(id, ThrowException.Throw);
		model.addAttribute("detail", vo);
		return "/reportsql/detail";
	}
	
}
