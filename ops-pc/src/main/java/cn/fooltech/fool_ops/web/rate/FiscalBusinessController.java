package cn.fooltech.fool_ops.web.rate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.rate.service.FiscalBusinessService;
import cn.fooltech.fool_ops.domain.rate.vo.FiscalBusinessVo;




/**
 * 经营收益率
 * @author 
 *
 */
@Controller
@RequestMapping("/rate/FiscalBusiness")
public class FiscalBusinessController {
	@Autowired
	private FiscalPeriodService periodService;
	@Autowired
	private FiscalBusinessService businessService;
	/**
	 * 账套会计期间
	 * @return
	 */
	@RequestMapping(value = "/queryPeriods")
	@ResponseBody
	public List<FiscalPeriodVo> queryPeriods(FiscalPeriodVo vo,PageParamater pageParamater){
		List<FiscalPeriodVo> vos=periodService.getAllUsedPeriod();
		return vos;
	}
	/**
	 * 经营收益率界面
	 */
	@RequestMapping(value="/manage")
	public String listFiscalBusiness(){
		return "/rate/listFiscalBusiness/manage";
	}
	/**
	 * 查询经营收益率信息
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<FiscalBusinessVo> query(FiscalBusinessVo vo){
		businessService.saveInitFiscalBusiness(vo.getPeriodId(), true);
		return businessService.query(vo);
	}
	/**
	 * 新增/编辑经营收益率
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(FiscalBusinessVo vo){
		return businessService.save(vo);
	}
	/**
	 * 删除经营收益率
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String fid){
		return businessService.delete(fid);
	}
	/**
	 * 计算经营收益率
	 */
	@RequestMapping(value = "/computeFormula")
	@ResponseBody
	public RequestResult computeFormula(String periodId){
		return businessService.saveComputeFormula(periodId);
	}
	/**
	 * 把当前公式写入公式表中
	 */
	@RequestMapping(value = "/saveBusinessFormula")
	@ResponseBody
	public RequestResult saveBusinessFormula(String periodId){
		return businessService.saveBusinessFormula(periodId);
	}
	/**
	 * 从公式表中把公式写入
	 */
	@RequestMapping(value = "/resumeFormula")
	@ResponseBody
	public RequestResult resumeFormula(String periodId){
		return businessService.saveResumeFormula(periodId);
	}
	/**
	 * 修复旧数据,用完注释
	 */
	@RequestMapping(value = "/initFormula")
	@ResponseBody
	public RequestResult initFormula(){
		return businessService.initFormula();
	}
}
