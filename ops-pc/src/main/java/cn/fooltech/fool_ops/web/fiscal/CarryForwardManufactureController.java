package cn.fooltech.fool_ops.web.fiscal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.CarryForwardProfitLoss;
import cn.fooltech.fool_ops.domain.fiscal.service.CarryForwardProfitLossService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.vo.CarryForwardProfitLossVo;

/**
 * <p>结转制造网页控制器</p>
 * @author xjh
 * @version 1.0
 * @date 2016年4月13日
 */
@Controller
@RequestMapping("/manufacture")
public class CarryForwardManufactureController {
	
	/**
	 * 结转损益科目网页服务类
	 */
	@Autowired
	private CarryForwardProfitLossService profitLossService;
	
	/**
	 * 科目网页服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 结转损益管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(Model model){
		return "/report/carryForwardManufacture/manage";
	}
	
	/**
	 * 分页查询
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PageParamater paramater){
		Page<CarryForwardProfitLossVo> page = profitLossService.query(paramater, CarryForwardProfitLoss.TYPE_MANUFACTURE);
		return new PageJson(page);
	}
	
	/**
	 * 新增
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(CarryForwardProfitLossVo vo){
		vo.setType(CarryForwardProfitLoss.TYPE_MANUFACTURE);
		return profitLossService.save(vo);
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return profitLossService.delete(fid);
	}
	
	/**
	 * 删除所有记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAll")
	public RequestResult deleteAll(){
		return profitLossService.deleteAll(CarryForwardProfitLoss.TYPE_MANUFACTURE);
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @param flag 是否必须结转  1 是  0 否
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/makeVoucher")
	public RequestResult makeVoucher(CarryForwardProfitLossVo vo, @RequestParam(defaultValue = "0", required = false) int flag){
		vo.setType(CarryForwardProfitLoss.TYPE_MANUFACTURE);
		return profitLossService.makeVoucher(vo, flag);
	}
	
}
