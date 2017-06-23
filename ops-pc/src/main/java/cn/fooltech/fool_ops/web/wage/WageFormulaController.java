package cn.fooltech.fool_ops.web.wage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.service.WageFormulaService;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>工资公式网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 10:05:05
 */
@Controller
@RequestMapping(value = "/wageFormula")
public class WageFormulaController extends BaseController{
	
	/**
	 * 工资公式网页服务类
	 */
	@Autowired
	private WageFormulaService wageFormulaService;
	
	/**
	 * 去工资公式列表信息页面<br>
	 */
	@RequestMapping("/listWageFormula")
	public String listWageFormula(ModelMap model){
		return "/wage/wageFormula";
	}
	
	/**
	 * 查找工资公式列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryWageFormula")
	public PageJson queryWageFormula(WageFormulaVo wageFormulaVo,PageParamater pageParamater){
		Page<WageFormulaVo> page = wageFormulaService.query(wageFormulaVo,pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	/**
	 * 查找工资公式所有列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<WageFormulaVo> queryAll(Short isView){
		return wageFormulaService.getByAccountId(isView);
	}
	
	
	/**
	 * 新增/编辑工资公式页面
	 * @return
	 */
	@RequestMapping(value = "/editWageFormula")
	public String editWageFormula(ModelMap model, @RequestParam String fid){
		WageFormulaVo wageFormulaVo = wageFormulaService.getByFid(fid);
		model.put("wageFormula",wageFormulaVo);
		return "/wage/editWageFormula";
	}
	
	/**
	 * 新增/编辑工资公式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(WageFormulaVo wageFormulaVo){
		return wageFormulaService.save(wageFormulaVo);
	}
	
	/**
	 * 删除工资公式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteWageFormula")
	public RequestResult deleteWageFormula(@RequestParam String fid){
		return wageFormulaService.delete(fid);
	}
	
}