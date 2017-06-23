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
import cn.fooltech.fool_ops.domain.flow.service.PlanGoodsService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanGoodsVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>
 * 计划货品控制器类
 * </p>
 * 
 * @author cwz
 * @version 1.0
 * @date 2017-2-8 09:17:09
 */
@Controller
@RequestMapping(value = "/flow/planGoods")
public class PlanGoodsController extends BaseController {

	/**
	 * 计划货品服务类
	 */
	@Autowired
	private PlanGoodsService webService;

	/**
	 * 去计划货品信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/flow/planGoods/manage";
	}

	/**
	 * 查找计划模板列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PlanGoodsVo vo, PageParamater pageParamater) {
		Page<PlanGoodsVo> page = webService.query(vo, pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(page.getContent());
		return pageJson;
	}

	/**
	 * 编辑计划模板页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id) {
		PlanGoodsVo planGoodsVo = webService.getById(id);
		model.put("planGoodsVo", planGoodsVo);
		return "/flow/planGoods/edit";
	}

	/**
	 * 新增/编辑计划模板
	 * 
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	public RequestResult save(PlanGoodsVo vo) {
//		return webService.save(vo);
//	}

	/**
	 * 删除计划模板
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id) {
		return webService.delete(id);
	}
}