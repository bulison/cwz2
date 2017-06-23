package cn.fooltech.fool_ops.web.capital;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanBillService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * 
 * @Description:资金计划关联单据 网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@Controller
@RequestMapping(value = "/capitalPlanBill")
public class CapitalPlanBillController extends BaseController {

	/**
	 * 资金计划关联单据网页服务类
	 */
	@Autowired
	private CapitalPlanBillService billService;


	/**
	 * 资金计划关联列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(CapitalPlanBillVo vo, PageParamater pageParamater) {
		Page<CapitalPlanBillVo> page = billService.query(vo, pageParamater);
		return new PageJson(page);
	}

	/**
	 * 资金计划关联管理页面
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/capital/planBill/manage";
	}

	/**
	 * 资金计划关联编辑页面
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (StringUtils.isNotBlank(id)) {
			CapitalPlanBillVo vo = billService.getById(id);
			model.addAttribute("obj", vo);
		} 
		return "/capital/planBill/edit";
	}

	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(CapitalPlanBillVo vo) {
		return billService.save(vo);
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param bindType 类型 1-单据金额，2-收付款金额
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id, Integer bindType) {
		return billService.delete(id,bindType);
	}
}