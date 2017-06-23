package cn.fooltech.fool_ops.web.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.report.service.ProfitSheetService;
import cn.fooltech.fool_ops.domain.report.vo.ProfitSheetVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>利润网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-02-18 16:27:29
 */
@Controller
@RequestMapping(value = "/report/profitSheet")
public class ProfitSheetController extends BaseController{
	
	/**
	 * 利润网页服务类
	 */
	@Autowired
	private ProfitSheetService webService;
	
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 去利润列表信息页面<br>
	 */
	@RequestMapping("/listProfitSheet")
	public String listProfitSheet(ModelMap model){
		return "/report/listProfitSheet";
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
	 * 查找利润列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<ProfitSheetVo> query(ProfitSheetVo vo){
		webService.saveInitProfitSheet(vo.getPeriodId(), true);
		return webService.getVos(webService.query(vo));
	}
	
	/**
	 * 新增/编辑利润
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(ProfitSheetVo profitSheetVo){
		return webService.save(profitSheetVo);
	}
	
	/**
	 * 删除利润
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String fid){
		return webService.delete(fid);
	}
	
	/**
	 * 计算利润表
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/computeFormula")
	@ResponseBody
	public RequestResult computeFormula(String periodId){
		return webService.saveComputeFormula(periodId);
	}
	
	/**
	 * 把当前公式写入资产利润公式表中
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/saveSheetFormula")
	@ResponseBody
	public RequestResult saveSheetFormula(String periodId){
		return webService.saveSheetFormula(periodId);
	}
	
	/**
	 * 从利润公式表写入当前公式数据列中
	 * @param periodId
	 * @return
	 */
	@RequestMapping(value = "/resumeFormula")
	@ResponseBody
	public RequestResult resumeFormula(String periodId){
		return webService.saveResumeFormula(periodId);
	}
}