package cn.fooltech.fool_ops.web.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateDetail;
import cn.fooltech.fool_ops.domain.flow.service.PlanTemplateDetailService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateDetailVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>计划模板明细网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:46:07
 */
@Controller
@RequestMapping(value = "/flow/planTemplateDetail")
public class PlanTemplateDetailController extends BaseController{
	
	/**
	 * 计划模板明细网页服务类
	 */
	@Autowired
	private PlanTemplateDetailService webService;
	
	/**
	 * 去计划模板明细列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		return "/flow/planTemplateDetail/manage";
	}
	
	/**
	 * 查找计划模板明细列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PlanTemplateDetailVo vo,PageParamater pageParamater){
		Page<PlanTemplateDetail> page = webService.query(vo, pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(webService.getVos(page.getContent()));
		return pageJson;
	}
	
	/**
	 * 查找计划模板明细树列表<br>
	 */
	@ResponseBody
	@RequestMapping("/queryTree")
	public List<PlanTemplateDetailVo> queryTree(String planTemplateId,PageParamater pageParamater){
		return webService.queryTree(planTemplateId);
	}
	
	/**
	 * 编辑计划模板明细页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		
		//==1则为修改
		if(mark==1){
			PlanTemplateDetailVo planTemplateDetailVo = webService.getByFid(id);
			model.put("planTemplateDetail",planTemplateDetailVo);
		}
		
		return "/flow/planTemplateDetail/edit";
	}
	
	/**
	 * 新增/编辑计划模板明细
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(PlanTemplateDetailVo vo){
		return webService.save(vo);
	}*/
	
	/**
	 * 删除计划模板明细
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}
}