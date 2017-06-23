package cn.fooltech.fool_ops.web.basedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.BillRule;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.basedata.vo.BillRuleVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>单据单号生成规则</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月14日
 */
@Controller
@RequestMapping("/billrule")
public class BillRuleController extends BaseController{
	
	/**
	 * 单据单号生成规则网页服务类
	 */
	@Autowired
	private BillRuleService ruleService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/billRule/billRule";
	}
	
	/**
	 * 规则列表(JSON)
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(PageParamater paramater){
		Page<BillRuleVo> page = ruleService.query(paramater);
		return new PageJson(page);
	}
	
	/**
	 * 新增规则页面
	 * @param id 规则ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add (){
		return "/billRule/addBillRule";
	}
	
	/**
	 * 规则明细页面
	 * @param id 规则ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		BillRuleVo billRule = ruleService.getById(id);
		model.addAttribute("billRule", billRule);
		return "/basedata/billrule/billRuleDetail";
	}
	
	/**
	 * 编辑页面
	 * @param id 规则ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		BillRuleVo billRule = ruleService.getById(id);
		model.addAttribute("billRule", billRule);
		return "/billRule/addBillRule";
	}
	
	/**
	 * 编辑
	 * @param vo
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(BillRuleVo vo){
		return ruleService.save(vo);
	}
	
}
