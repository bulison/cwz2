package cn.fooltech.fool_ops.web.report;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.service.CashFlowAnalysisService;
import cn.fooltech.fool_ops.domain.report.vo.CashFlowAnalysisVo;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>
 * 现金流量汇总分析
 * </p>
 * 
 * @author cwz
 * @version 1.0
 * @date 2017-3-22
 */
@Controller
@RequestMapping("/report/cashFlowAnalysis/")
public class CashFlowAnalysisController {

	/**
	 * 现金流量汇总分析服务类
	 */
	@Autowired
	private CashFlowAnalysisService webService;

	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/cashFlowAnalysis/manage";
	}

	/**
	 * 显示详情
	 */
	@RequestMapping(value = "/window")
	public String window(GoodsStockVo vo, ModelMap model) {
		model.put("data", vo);
		return "/report/cashFlowAnalysis/detail";
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(CashFlowAnalysisVo vo, PageParamater paramater)
			throws Exception {
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<CashFlowAnalysisVo> list = webService.getList(vo,orgId,accId,paramater);
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
	public void export(CashFlowAnalysisVo vo, PageParamater paramater,
			HttpServletResponse response) throws Exception {
		paramater.setRows(Integer.MAX_VALUE);
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<CashFlowAnalysisVo> list = webService.getList(vo,orgId,accId,paramater);
		ExcelUtils.exportExcel(CashFlowAnalysisVo.class, list, "现金流量汇总分析.xls", response);
	}

	
}