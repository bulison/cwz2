package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import io.swagger.annotations.ApiOperation;

/**
 * <p>供应商网页控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年4月23日
 */
@RestController
@RequestMapping(value = "/api/supplier")
public class SupplierApiController {
	
	/**
	 *供应商服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
	
	
	/**
	 * 供应商列表信息(JSON)
	 * @param vo
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 */
	@ApiOperation("获取供应商列表信息")
	@GetMapping(value = "/list")
	public @ResponseBody PageJson list(SupplierVo vo,PageParamater pageParamater){
//		Page<SupplierVo> query = supplierService.query(vo, pageParamater);
		return supplierService.queryJson(vo,pageParamater);
	}
	

	
	/**
	 * 编辑页面
	 * @param id 供应商ID
	 * @return
	 */
	@GetMapping("/get")
	@ApiOperation("根据fid获取供应商信息")
	@ResponseBody
	public SupplierVo edit(String id){
		SupplierVo supplier = supplierService.getById(id);
		return supplier;
//		model.addAttribute("vo", supplier);
//		return "/basedata/supplier/updateSupplier";
	}
	
	

	/**
	 * 判断编号是否有效
	 * @param vo
	 * @return
	 */
//	@RequestMapping("/isCodeValid")
//	@ResponseBody
//	public RequestResult isCodeValid(SupplierVo vo){
//		return supplierService.isCodeValid(vo);
//	}
	
	
	/**
	 * 模糊查询(根据供应商编号、供应商名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@GetMapping("/vagueSearch")
	@ApiOperation("模糊查询(根据客户编号、客户名称)")
	public List<SupplierVo> vagueSearch(SupplierVo vo){
		return supplierService.vagueSearch(vo);
	}
	
}
