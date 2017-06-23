package cn.fooltech.fool_ops.web.report;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.report.service.QuentityAmountService;
import cn.fooltech.fool_ops.domain.report.vo.QuentityAmountDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.SubjectVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;

/**
 * <p>
 * 数量金额明细账
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015年10月13日
 */
@Controller
@RequestMapping("/report/quentityAmount/")
public class QuentityAmountController {

	@Autowired
	private QuentityAmountService webService;
	
	@Autowired
	private FiscalPeriodService periodService;

	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/quentityAmount/manage";
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(QuentityAmountDetailVo vo, PageParamater paramater)
			throws Exception {
		List<QuentityAmountDetailVo> list = webService.queryData(vo, paramater);
		Long total = webService.countData(vo);

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
	 * 根据起始编号和结束编号查找所有包含核算数量的科目
	 * @param startCode
	 * @param endCode
	 * @return
	 */
	@RequestMapping("/querySubjects")
	@ResponseBody
	public List<SubjectVo> querySubjects(String startCode, String endCode, Integer level) {
		return webService.querySubject(startCode, endCode, level);
	}
	
	/**
	 * 账套会计期间
	 * @return
	 */
	@RequestMapping(value = "/queryPeriods")
	@ResponseBody
	public List<FiscalPeriodVo> queryPeriods(FiscalPeriodVo vo,PageParamater pageParamater){
		List<FiscalPeriodVo> vos=periodService.getAllPeriod();
		for(FiscalPeriodVo iter:vos){
			if(iter.getCheckoutStatus()==FiscalPeriod.USED||iter.getCheckoutStatus()==FiscalPeriod.UN_USED){
				iter.setIsChecked((short)1);
				break;
			}
		}
		return vos;
	}

	/**
	 * 导出，需要在cfg_excel_map表配置对应字段
	 * 
	 * @param vo
	 * @param paramater
	 * @param buildCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export")
	public void export(QuentityAmountDetailVo vo, PageParamater paramater,
			HttpServletResponse response) throws Exception {
		paramater.setRows(Integer.MAX_VALUE);
		List<QuentityAmountDetailVo> list = webService.queryData(vo, paramater);
		ExcelUtils.exportExcel(QuentityAmountDetailVo.class, list, "数量金额明细.xls", response);
	}

}
