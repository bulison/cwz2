package cn.fooltech.fool_ops.web.flow;

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
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.service.PlanTemplateDetailService;
import cn.fooltech.fool_ops.domain.flow.service.PlanTemplateService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;



/**
 * <p>计划模板网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:45:13
 */
@Controller
@RequestMapping(value = "/flow/planTemplate")
public class PlanTemplateController extends BaseController{

	/**
	 * 计划模板网页服务类
	 */
	@Autowired
	private PlanTemplateService webService;

	/**
	 * 计划模板明细网页服务类
	 */
	@Autowired
	private PlanTemplateDetailService webDetailService;

	/**
	 * 去计划模板列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		return "/flow/planTemplate/manage";
	}

	/**
	 * 查找计划模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PlanTemplateVo vo,PageParamater pageParamater){
		Page<PlanTemplate> page = webService.query(vo, pageParamater);
		PageJson pageJson =new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(webService.getVos(page.getContent()));
		return pageJson;
	}
	
	/**
	 * 查找所有模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryAllTemp")
	public List<PlanTemplateVo> queryAllTemp(PlanTemplateVo vo,PageParamater pageParamater){
		Page<PlanTemplate> page = webService.query(vo, pageParamater);
		return webService.getVos(page.getContent());
	}

	/**
	 * 查找已启用的计划模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<PlanTemplateVo> queryAll(PlanTemplateVo vo){
		List<PlanTemplate> list = webService.queryAll(vo);
		return webService.getVos(list);
	}

	/**
	 * 编辑计划模板页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){

		//==1则为修改
		if(mark==1){
			PlanTemplateVo planTemplateVo = webService.getByFid(id);
			JSONArray array = JSONArray.fromObject(webDetailService.queryTree(id));
			planTemplateVo.setDetails(array.toString());
//			System.out.println(planTemplateVo.getDetails());
//			System.out.println("edit:"+planTemplateVo.getUpdateTime());
			model.put("planTemplate",planTemplateVo);
		}

		return "/flow/planTemplate/edit";
	}

	/**
	 * 新增/编辑计划模板
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(PlanTemplateVo vo){
		return webService.save(vo);
	}

	/**
	 * 删除计划模板
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}

	/**
	 * 启用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUse")
	public RequestResult updateUse(@RequestParam String id){
		return webService.updateUse(id);
	}

	/**
	 * 停用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUnUse")
	public RequestResult updateUnUse(@RequestParam String id){
		return webService.updateUnUse(id);
	}
}