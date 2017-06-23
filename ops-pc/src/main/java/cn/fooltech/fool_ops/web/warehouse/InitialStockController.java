package cn.fooltech.fool_ops.web.warehouse;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;

/**
 * <p>期初库存网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/initialstock/{buildCode}/")
public class InitialStockController extends BaseWareHouseController{
	
	/**
	 * 上传文件并导入
	 * @author xjh
	 * @date 2016年1月11日
	 */
	@RequestMapping(value="/import", method=RequestMethod.POST)
	@ResponseBody
	public void importExcel(@PathVariable("buildCode") WarehouseBuilderCode buildCode,
			HttpServletRequest request, HttpServletResponse response){
		
		List<ImportVoBean> voBeans = Lists.newArrayList();
		//把excel转换成VO对象
		RequestResult result = ExcelUtils.importExcel(WarehouseBillDetailVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		if(!result.isSuccess()){
			WebUtils.writeJsonToHtml(response, result);
		}
		
		List<ImportVoBean> details = defaultService.initialDetailVos(voBeans);
		result.setData(details);
		result.setReturnCode(1);
		WebUtils.writeJsonToHtml(response, result);
		/*return result;*/
	}
	
	/*@RequestMapping(value="/saveFixErrorData", method=RequestMethod.GET)
	@ResponseBody
	public RequestResult saveFixErrorData(@PathVariable("buildCode") WarehouseBuilderCode buildCode,
			HttpServletRequest request, HttpServletResponse response){
		return defaultWebService.saveFixErrorData();
	}*/
}
