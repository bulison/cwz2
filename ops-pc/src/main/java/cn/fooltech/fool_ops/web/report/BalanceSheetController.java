package cn.fooltech.fool_ops.web.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.report.service.BalanceSheetService;
import cn.fooltech.fool_ops.domain.report.vo.BalanceSheetVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>资产负债网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-02-16 09:34:26
 */
@Controller
@RequestMapping(value = "/report/balanceSheet")
public class BalanceSheetController extends BaseController{
	
	/**
	 * 资产负债网页服务类
	 */
	@Autowired
	private BalanceSheetService webService;
	
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 去资产负债列表信息页面<br>
	 */
	@RequestMapping("/list")
	public String listBalanceSheet(ModelMap model){
		return "/report/listBalanceSheet";
	}
	
	/**
	 * 查找资产负债列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<BalanceSheetVo> query(BalanceSheetVo vo){
		webService.saveInitBalanceSheet(vo.getPeriodId(), true);
		List<BalanceSheetVo> list = webService.getVos(webService.query(vo));
		return list;
	}
	
	
	/**
	 * 账套会计期间
	 * @return
	 */
	@RequestMapping(value = "/queryPeriods")
	@ResponseBody
	public List<FiscalPeriodVo> queryPeriods(FiscalPeriodVo vo,PageParamater pageParamater){
		List<FiscalPeriodVo> vos=periodService.getAllUsedCheckedPeriod();
		return vos;
	}
	
	/**
	 * 保存/新增
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(BalanceSheetVo vo){
		return webService.save(vo);
	}
	
	/**
	 * 删除
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(String fid){
		return webService.delete(fid);
	}
	
	/**
	 * 计算资产负债表
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/computeFormula")
	@ResponseBody
	public RequestResult computeFormula(String periodId){
		return webService.saveComputeFormula(periodId);
	}
	
	/**
	 * 把当前公式写入资产负债公式表中
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/saveSheetFormula")
	@ResponseBody
	public RequestResult saveSheetFormula(String periodId){
		return webService.saveSheetFormula(periodId);
	}
	
	/**
	 * 从资产负债公式表写入当前公式数据列中
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/resumeFormula")
	@ResponseBody
	public RequestResult resumeFormula(String periodId){
		return webService.saveResumeFormula(periodId);
	}
	
}