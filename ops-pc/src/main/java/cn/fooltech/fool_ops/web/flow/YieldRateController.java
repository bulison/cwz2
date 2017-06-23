package cn.fooltech.fool_ops.web.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.flow.service.YieldRateService;
import cn.fooltech.fool_ops.domain.flow.vo.YieldRateVo;

/**
 * 
 * <p>流程计划每天收益率</p>  
 * @author cwz
 * @date 2017年4月24日
 */
@Controller
@RequestMapping("/flow/yieldRate")
public class YieldRateController {
	
	@Autowired
	private YieldRateService yieldRateService;

	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Page<YieldRateVo> query(YieldRateVo vo, PageParamater paramater) throws Exception{
		return yieldRateService.query(vo, paramater);
	}
	
	/**
	 * 根据计划id查询收益率
	 * @param planId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryByPlan")
	public List<YieldRateVo> queryByPlan(String planId){
		return yieldRateService.queryByPlan(planId);
	}
}
