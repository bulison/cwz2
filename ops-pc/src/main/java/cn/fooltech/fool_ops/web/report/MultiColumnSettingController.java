package cn.fooltech.fool_ops.web.report;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.fooltech.fool_ops.domain.report.service.FiscalMultiColumnService;

/**
 * <p>多栏明细账报表控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月21日
 */
@Controller
@RequestMapping("/multiCcolumn")
public class MultiColumnSettingController {
	
	/**
	 * 多栏明细账设置网页服务类
	 */
	@Autowired
	private FiscalMultiColumnService multiColumnService;
	 
	@RequestMapping("/report")
	public String report(){
		return "/report/fiscal/multiCcolumnSetting/report";
	}
	
	@RequestMapping("/edit")
	public String edit(String fid,HttpServletRequest request){
		
		if(StringUtils.isNotBlank(fid)){
			request.setAttribute("vo", multiColumnService.getById(fid));
		}
		
		return "/report/fiscal/multiCcolumnSetting/add"; 
	}
	
}
