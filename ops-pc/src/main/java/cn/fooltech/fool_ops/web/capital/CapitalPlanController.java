package cn.fooltech.fool_ops.web.capital;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @Description:资金计划 网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@Controller
@RequestMapping(value = "/capitalPlan")
public class CapitalPlanController extends BaseController{
	
	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanService planService;
	
	/**
	 * 单据规则服务类
	 */
	@Autowired
	private BillRuleService billRuleService;
	
	/**
	 * 资金计划列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(CapitalPlanVo vo,PageParamater pageParamater){
		Page<CapitalPlanVo> page = planService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 资金计划管理页面
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/capitalManage/capitalPlan/manage";
	}
	/**
	 * 资金计划编辑页面
	 */
	@RequestMapping("/edit")
	public String edit(String id, @RequestParam(value = "mark", defaultValue = "0") Integer mark, 
			Model model) {
		if(StringUtils.isNotBlank(id)){
			CapitalPlanVo vo=planService.getById(id);
			model.addAttribute("obj", vo);
		}else {
			//新增获取单号
			String code = billRuleService.getNewCode(100);
			model.addAttribute("code", code);
		}
		return "/capitalManage/capitalPlan/edit";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(CapitalPlanVo vo) {
		return planService.save(vo,1);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id) {
		return planService.del(id,0);
	}
	
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param id			主键
	 * @param recordStatus	修改后的状态
	 * @param updateTime	修改时间
	 * @param editType 		可否限制：0-不限制，1-限制
	 * @return
	 */
	@RequestMapping("/changeByCapitalId")
	@ResponseBody
	public RequestResult changeByCapitalId(String id, int recordStatus,String updateTime,Integer editType) {
		RequestResult result = planService.changeByCapitalId(id, recordStatus,updateTime,editType);
		return result;
	}

	/**
	 * 根据关联id返回资金计划和明细
	 * @param relationId 关联id
	 * @return
	 */
	@RequestMapping("/queryByWarehouse")
	@ResponseBody
	public CapitalPlanVo queryByWarehouse(String relationId){
		CapitalPlanVo vo = planService.queryByWarehouse(relationId);
		return vo;
	}
	/**
	 * 判断资金计划的金额是否等于单据金额
	 * @param relationId	单据id
	 * @param billAmount	仓储单据金额
	 * @return
	 */
	@RequestMapping("/checkPlanAmount")
	@ResponseBody
	public RequestResult checkPlanAmount(String relationId,BigDecimal billAmount){
		RequestResult result = planService.checkPlanAmount(relationId,billAmount);
		return result;
	}
	/**
	 * 资金计划审核（自动添加记录）
	 * @param relationSign	关联类型
	 * @param relationId	关联id
	 * @return
	 */
	@RequestMapping("/capitalPassAudit")
	@ResponseBody
	public RequestResult capitalPassAudit(int relationSign, String relationId) {
		RequestResult result = planService.capitalPassAudit(relationSign, relationId);
		return result;
	}
	/**
	 * 资金计划 审核
	 * @param id
	 * @param recordStatus
	 * @param updateTime
	 * @return
	 */
	@RequestMapping("/passAudit")
	@ResponseBody
	public RequestResult passAudit(String id, int recordStatus,String updateTime) {
		RequestResult result = planService.passAudit(id,recordStatus, updateTime);
		return result;
	}
}