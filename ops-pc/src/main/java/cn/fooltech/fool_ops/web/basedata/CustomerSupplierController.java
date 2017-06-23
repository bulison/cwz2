package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerSupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.CsvVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * <p>
 * 客户供应商网页控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-10-08
 */
@Controller
@RequestMapping("/customerSupplier")
public class CustomerSupplierController extends BaseController {
	
	@Autowired
	private CustomerSupplierService csvService;
	

	/**
	 * 客户供应商弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/warehourse/fyd/csvWindow";
	}
	
	/**
	 * 收发货地址设定界面
	 */
	@RequestMapping(value = "/cawindow")
	public String cawindow() {
		return "/basedata/customerAddress/window";
	}
	
	/**
	 * 客户供应商列表信息(JSON)
	 * @param vo

	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(CsvVo vo, PageParamater pageParamater){
		Page<CsvVo> page=csvService.query(vo, pageParamater);
		return new PageJson(page);
	}

}
