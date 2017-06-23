package cn.fooltech.fool_ops.web.report;

import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * <p>应付分析报表</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月9日
 */
@Controller
@RequestMapping("/receivableReport")
public class ReceivableReportController {

	/**
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;
	
	/**
	 * 打印模板配置服务表
	 */
	@Autowired
	private PrintTempService printTempWebService;
	
	/**
	 * 打印对账单
	 * @return
	 */
	@RequestMapping("/printAccountCheck")
	public String printAccountCheck(String code, String orgName, String details, Model model){
		Organization org = SecurityUtil.getCurrentOrg();
		model.addAttribute("org", orgService.getVo(org));
		model.addAttribute("orgName", orgName);
		model.addAttribute("details", details);
		
		PrintTemp temp = printTempWebService.getByOrgId(org.getFid(), code);
		if (temp != null) {
			if (temp.getPageRow() != null) {
				model.addAttribute("pageRow", temp.getPageRow());
			}
			String printUrl = temp.getPrintTempUrl();
			if (StringUtils.isNotBlank(printUrl)) {
				return printUrl;
			}
		}
		return "/report/receivableAnalysisPrint/printer";
	} 
	
}
