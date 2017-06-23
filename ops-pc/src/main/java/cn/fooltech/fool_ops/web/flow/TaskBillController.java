package cn.fooltech.fool_ops.web.flow;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.entity.TaskBill;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.web.base.BaseController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 计划事件关联单据 控制器类
 * </p>
 * 
 * @author cwz
 * @version 1.0
 * @date 2017-2-8 09:17:09
 */
@Controller
@RequestMapping(value = "/flow/taskBill")
public class TaskBillController extends BaseController {

	/**
	 * 计划事件关联单据 服务类
	 */
	@Autowired
	private TaskBillService webService;

	/**
	 * 去计划事件关联单据 信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/flow/taskBill/manage";
	}

	/**
	 * 查找计划模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(TaskBillVo vo, PageParamater pageParamater) {
		Page<TaskBillVo> page = webService.query(vo, pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(page.getContent());
		return pageJson;
	}

	/**
	 * 编辑计划事件关联单据 页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id) {
		TaskBillVo taskBillVo = webService.getById(id);
		model.put("taskBillVo", taskBillVo);
		return "/flow/taskBill/edit";
	}

	/**
	 * 新增/编辑计划事件关联单据 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public RequestResult save(TaskBillVo vo) {
		return webService.save(vo);
	}

	/**
	 * 删除计划事件关联单据 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id) {
		return webService.delete(id);
	}
	
	/**
	 * 关联单据，挂多张单据
	 * @param vo	关联单据VO
	 * @param ids	选中单据id，多个单据用逗号拼接
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/relateTaskBill")
	public RequestResult relateTaskBill(String vos){
		return webService.relateTaskBill(vos);
		
	}
	/**
	 * 根据时间id查询 关联单据
	 * @param queryByTaskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryByTaskId")
	public List<TaskBillVo> queryByTaskId(String queryByTaskId) {
		List<TaskBillVo> vos = webService.queryByTaskId(queryByTaskId);
		return vos;
	}
	
}