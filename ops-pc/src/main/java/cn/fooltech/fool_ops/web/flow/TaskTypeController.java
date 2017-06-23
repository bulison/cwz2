package cn.fooltech.fool_ops.web.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.service.TaskTypeService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskTypeVo;

/**
 * <p>事件分类网页控制器</p>
 * @author CYX
 * @version 1.0
 * @date 2016年10月31日
 */
@Controller
@RequestMapping("/flow/taskType")
public class TaskTypeController {
	
	@Autowired
	private TaskTypeService taskTypeService;
	
	/**
	 * 管理页面
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/flow/taskType/manage";
	}
	
	/**
	 * 查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(TaskTypeVo vo, PageParamater paramater){
		Page<TaskTypeVo> vos = taskTypeService.getPageTaskType(vo, paramater);
		return new PageJson(vos);
	}
	
	/**
	 * 查询全部
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<TaskTypeVo> queryAll(TaskTypeVo vo){
		return taskTypeService.getListTaskType(vo);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(TaskTypeVo vo){
		return taskTypeService.save(vo);
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return taskTypeService.delete(fid);
	}
}
