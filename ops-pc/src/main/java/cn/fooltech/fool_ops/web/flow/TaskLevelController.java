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
import cn.fooltech.fool_ops.domain.flow.service.TaskLevelService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskLevelVo;

/**
 * <p>事件级别网页控制器</p>
 * @author CYX
 * @version 1.0
 * @date 2016年11月10日
 */
@Controller
@RequestMapping("/flow/taskLevel")
public class TaskLevelController {
	
	@Autowired
	private TaskLevelService taskLevelService;
	
	/**
	 * 管理界面
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/flow/taskLevel/manage";
	}
	
	/**
	 * 分页查询事件级别
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(TaskLevelVo vo, PageParamater paramater) {
		Page<TaskLevelVo> vos = taskLevelService.getPageTaskLevel(vo, paramater);
		return new PageJson(vos);
	}
	
	/**
	 * 查询事件级别,过滤已停用的
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<TaskLevelVo> queryAll(TaskLevelVo vo) {
		return taskLevelService.getListTaskLevel(vo);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(TaskLevelVo vo) {
		return taskLevelService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return taskLevelService.delete(fid);
	}

}
