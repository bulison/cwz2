package cn.fooltech.fool_ops.web.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 销售退货单据
 * @author lgk
 * @date 2015年9月29日上午9:58:19
 * @version V1.0
 */
@Controller
@RequestMapping(value="/salesshipper/{buildCode}/")
public class SalesShipperController extends BaseWareHouseController{
}
