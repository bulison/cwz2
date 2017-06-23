package cn.fooltech.fool_ops.web.cost;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cost.service.CostBillCheckService;
import cn.fooltech.fool_ops.domain.cost.vo.CostBillCheckVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>
 * 勾对控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-10-06
 */
@Controller
@RequestMapping("/costCheck")
public class CostCheckController extends BaseController{
	
	@Autowired
	private CostBillCheckService costBillCheckWebService;
	
	
	/**
	 *勾选界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(String costBillId,HttpServletRequest request){
		request.setAttribute("costBillId", costBillId);
		return "/warehourse/billCheck/costCheck";
	}
	
	/**
	 *已勾对单据列表
	 * @return
	 */
	@RequestMapping(value="/checkedList")
	@ResponseBody
	public PageJson checkedList(CostBillCheckVo vo,PageParamater pageParamater){
		Page<CostBillCheckVo> page=costBillCheckWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 *未勾对单据列表
	 * @return
	 */
	@RequestMapping(value="/uncheckedList")
	@ResponseBody
	public PageJson uncheckedList(WarehouseBillVo vo,PageParamater pageParamater){
		PageJson json = costBillCheckWebService.queryCheckBill(vo, pageParamater);
		return json;
	}
	
	/**
	 *勾对
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/check")
	@ResponseBody
	public RequestResult check(CostBillCheckVo vo) throws Exception{
		return costBillCheckWebService.save(vo);
	}
	
	/**
	 *删除
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(CostBillCheckVo vo) throws Exception{
		String checkId = vo.getFid();
		return costBillCheckWebService.delete(checkId);
	}
}
