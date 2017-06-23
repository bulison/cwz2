package cn.fooltech.fool_ops.web.warehouse;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.warehouse.service.PricingService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.vo.PricingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>单据核价网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年4月5日
 */
@Controller
@RequestMapping("/pricing")
public class PricingController {

	/**
	 * 核价网页服务类
	 */
	@Autowired
	private PricingService pricingWebService;


	@RequestMapping("/pricingUI")
	public String pricingUI(){
		return "/warehourse/pricing";
	}
	
	/**
	 * 核价
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkPrice")
	public RequestResult checkPrice(PricingVo vo){
		RequestResult result = pricingWebService.checkPrice(vo);
		return result;
	}
	
}
