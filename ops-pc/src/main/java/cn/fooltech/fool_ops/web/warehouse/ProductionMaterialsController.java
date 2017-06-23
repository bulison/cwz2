package cn.fooltech.fool_ops.web.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;


/**
 * <p>生产领料网页控制器</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月28日
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/productionMaterials/{buildCode}/")
public class ProductionMaterialsController extends BaseWareHouseController{
	
}