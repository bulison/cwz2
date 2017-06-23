package cn.fooltech.fool_ops.web.rate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.rate.service.PlanYieldRateService;
import cn.fooltech.fool_ops.domain.rate.vo.PlanYieldRateVo;
import cn.fooltech.fool_ops.domain.report.vo.CashFlowAnalysisVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 
 * <p>流程收益率分析【报表】 </p>  
 * @author cwz
 * @date 2017年4月17日
 */
@Controller
@RequestMapping("/rate/planYieldRate/")
public class PlanYieldRateController {

	/**
	 * 现金流量汇总分析服务类
	 */
	@Autowired
	private PlanYieldRateService webService;

	@RequestMapping(value = "/manage")
	public String manage() {
		return "/rate/planYieldRate/manage";
	}


	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(PlanYieldRateVo vo, PageParamater paramater)
			throws Exception {
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<PlanYieldRateVo> list = webService.getList(vo,orgId,accId,paramater);
		Long total = webService.countList(vo);

		PageJson pageJson = new PageJson();
		if (null == list) {
			pageJson.setRows(new ArrayList());
			pageJson.setTotal(0L);
		} else {
			pageJson.setRows(list);
			pageJson.setTotal(total);
		}
		return pageJson;
	}


	/**
	 * 导出，需要在cfg_excel_map表配置对应字段
	 * 
	 * @param vo
	 * @param paramater
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export")
	public void export(PlanYieldRateVo vo, PageParamater paramater,
			HttpServletResponse response) throws Exception {
		paramater.setRows(Integer.MAX_VALUE);
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<PlanYieldRateVo> list = webService.getList(vo,orgId,accId,paramater);
		ExcelUtils.exportExcel(CashFlowAnalysisVo.class, list, "流程收益率分析.xls", response);
	}

	
}
