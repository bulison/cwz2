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
import cn.fooltech.fool_ops.domain.flow.entity.TaskTemplate;
import cn.fooltech.fool_ops.domain.flow.service.TaskTemplateService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskTemplateVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>事件模板网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-16 17:08:33
 */
@Controller
@RequestMapping(value = "/flow/taskTemplate")
public class TaskTemplateController extends BaseController{
	
	/**
	 * 事件模板网页服务类
	 */
	@Autowired
	private TaskTemplateService webService;
	
	/**
	 * 去事件模板列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		return "/flow/taskTemplate/manage";
	}
	
	/**
	 * 查找事件模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(TaskTemplateVo vo,PageParamater pageParamater){
		Page<TaskTemplateVo> page = webService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	
	/**
	 * 查找事件模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<TaskTemplateVo> queryAll(TaskTemplateVo vo,String taskTypeId){
		List<TaskTemplate> list = webService.queryAll(vo,taskTypeId);
		return webService.getVos(list);
	}
	
	/**
	 * 编辑事件模板页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		
		//==1则为修改
		if(mark==1){
			TaskTemplateVo taskTemplateVo = webService.getByFid(id);
			model.put("taskTemplate",taskTemplateVo);
		}
		
		return "/flow/taskTemplate/edit";
	}
	
	/**
	 * 新增/编辑事件模板
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(TaskTemplateVo vo){
		return webService.save(vo);
	}
	
	/**
	 * 删除事件模板
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}
}