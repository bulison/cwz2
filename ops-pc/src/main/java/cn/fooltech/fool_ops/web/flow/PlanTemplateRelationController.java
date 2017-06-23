package cn.fooltech.fool_ops.web.flow;

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
import cn.fooltech.fool_ops.domain.flow.service.PlanTemplateRelationService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateRelationVo;
import cn.fooltech.fool_ops.web.base.BaseController;



/**
 * <p> 计划模板关联控制器类</p>
 * @author cwz
 * @version 1.0
 * @date 2017-2-8 09:17:09
 */
@Controller
@RequestMapping(value = "/flow/planTemplateRelation")
public class PlanTemplateRelationController extends BaseController{
	
	/**
	 * 计划模板关联服务类
	 */
	@Autowired
	private PlanTemplateRelationService webService;
	
	/**
	 * 去计划模板关联信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		return "/flow/planTemplateRelation/manage";
	}
	
	/**
	 * 查找计划模板关联列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PlanTemplateRelationVo vo,PageParamater pageParamater){
		Page<PlanTemplateRelationVo> page = webService.query(vo, pageParamater);
		PageJson pageJson =new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(page.getContent());
		return pageJson;
	}
	
	
	/**
	 * 编辑计划模板关联页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		PlanTemplateRelationVo relationVo = webService.getById(id);
		model.put("relationVo",relationVo);
		return "/flow/planTemplateRelation/edit";
	}

	/**
	 * 新增/编辑计划模板关联
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "/save",method=RequestMethod.POST)
//	public RequestResult save(PlanTemplateRelationVo vo){
//		return webService.save(vo);
//	}
	
	/**
	 * 删除计划模板关联
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}

	/**
	 * 设置路径模板按钮
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/settleRouteTemplate")
	public RequestResult settleRouteTemplate(@RequestParam String id){
		return webService.settleRouteTemplate(id);
	}

	/**
	 * 设置采购模板按钮
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/settlePurchaseTemplate")
	public RequestResult settlePurchaseTemplate(@RequestParam String id){
		return webService.settlePurchaseTemplate(id);
	}

	/**
	 * 设置销售模板按钮
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/settleSaleTemplate")
	public RequestResult settleSaleTemplate(@RequestParam String id){
		return webService.settleSaleTemplate(id);
	}

	/**
	 * 判断是否已设置所有模板（采购、运输、销售）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/settleAllTemplate")
	public RequestResult settleAllTemplate(String ids){
		return webService.settleAllTemplate(ids);
	}
}