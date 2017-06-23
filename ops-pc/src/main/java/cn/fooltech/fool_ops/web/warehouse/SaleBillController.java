package cn.fooltech.fool_ops.web.warehouse;

import cn.fooltech.fool_ops.domain.warehouse.service.SaleOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.warehouse.vo.SaleOrderVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 销售订单控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-09-16
 * @see WarehouseBuilderCode
 */

@Controller
@RequestMapping("/salebill/{buildCode}/")
public class SaleBillController extends BaseWareHouseController {

	@Autowired
	private SaleOrderService saleOrderService;

	/**
	 * 销售订单弹出窗口
	 * @return WEB PAGE
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/warehourse/xsdd/xsddWindows";
	}

	/**
	 * 销售订单弹出窗口
	 * @return WEB PAGE
	 */
	@RequestMapping(value = "/cgrkwindow")
	public String cgrkWindow(){
		return "/warehourse/cgrk/cgrkWindows";
	}

	/**
	 * 销售订单弹出窗口
	 * @return WEB PAGE
	 */
	@RequestMapping(value = "/xschwindow")
	public String xschWindow(){
		return "/warehourse/xsch/xschWindows";
	}

	/**
	 * 销售分析报表-销售订单分析
	 * URI: /salebill/analyse/order
	 */
	@RequestMapping("/order")
	@ResponseBody
	public PageJson saleOrderAnalyse(SaleOrderVo saleOrderVo, PageParamater paramater) {
		List<SaleOrderVo> list = saleOrderService.getSaleOrderList(saleOrderVo, paramater);
		Long total = saleOrderService.countSaleOrderList(saleOrderVo);

		PageJson pageJson = new PageJson();
		if (CollectionUtils.isEmpty(list)) {
			pageJson.setRows(new ArrayList());
			pageJson.setTotal(0L);
		} else {
			pageJson.setRows(list);
			pageJson.setTotal(total);
		}
		return pageJson;
	}

	/**
	 * 销售分析报表-销售订单分析
	 * URI: /salebill/analyse/detail
	 */
	@RequestMapping("/details")
	@ResponseBody
	public List<Object> saleOrderDetailAnalyse(SaleOrderVo saleOrderVo) {
		return null;
	}

}
