package cn.fooltech.fool_ops.web.timeAxis.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.utils.DateJsonValueProcessor;
import cn.fooltech.fool_ops.utils.StrUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


/**
 * <p>单据、会计科目关联网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月2日
 */
@Controller
@RequestMapping("/timeAxis")
public class TimeAxisController {
	
	@Autowired
	private PlanService planWebService;
	
	@Autowired
	private TaskService taskWebService;
	
	/**
	 * 计划管理页面
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/axis")
	public String axis(String id){
		return "/timeaxis/newAxis";
	}
	
	/**
	 * 计划明细页面
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/planDetail")
	public String planDetail(String id,HttpServletRequest request){
		if(StrUtil.notEmpty(id)){
			PlanVo plan=planWebService.getById(id);
			request.setAttribute("plan", plan);
		}
		return "/timeaxis/planDetail";
	}
	
	/**
	 * 新增事件页面
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/addTask")
	public String addTask(String id,HttpServletRequest request){
		if(StrUtil.notEmpty(id)){
			TaskVo task=taskWebService.getById(id);
			request.setAttribute("task", task);
		}
		return "/timeaxis/addTask";
	}
	
	/**
	 * 事件列表页面
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/taskList")
	public String taskList(String planId,HttpServletRequest request){
		if(StrUtil.notEmpty(planId)){
			List<TaskVo> taskList=taskWebService.queryAllByPlan(planId);
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
			JSONArray jsonArray = JSONArray.fromObject(taskList, jsonConfig);
			request.setAttribute("taskList", jsonArray.toString());
		}
		return "/timeaxis/gantt";
	}
	
	/**
	 * 通过计划模板添加事件
	 * @param planId 计划ID
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	@RequestMapping("/addTaskByPlanTemplate")
	public String addTaskByPlanTemplate(String planId, String planTemplateId) throws Exception{
		taskWebService.addTaskByPlanTemplate(planId, planTemplateId);
		return "redirect:/flow/plan/axis?id=" + planId;
	}
	
}

