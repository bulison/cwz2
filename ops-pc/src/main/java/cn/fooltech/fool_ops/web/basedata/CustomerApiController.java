package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import io.swagger.annotations.ApiOperation;

/**
 * <p>客户网页控制器类</p>
 * @author lxf
 * @version 1.0
 * @date 2014年12月23日
 * @update rqh 2015-09-14
 */
@RestController
@RequestMapping(value = "/api/customer")
public class CustomerApiController {
	
	/**
	 * 客户网页服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	
	
	/**
	 * 客户列表信息(JSON)
	 * @param vo
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation("获取客户列表信息")
	@GetMapping(value = "/list")
	@ResponseBody
	public PageJson list(CustomerVo vo,PageParamater pageParamater) throws Exception{
		Page<CustomerVo> page = customerService.query(vo, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	
	/**
	 * 编辑页面
	 * @param id 客户ID
	 * @return
	 */
	@GetMapping("/get")
	@ApiOperation("根据fid获取客户信息")
	public CustomerVo edit(String id){
		CustomerVo customer = customerService.getById(id);
//		model.addAttribute("customer", customer);
//		return "/basedata/customer/customerEdit";
		return customer;
	}

	/**
	 * 判断编号是否有效
	 * @param vo
	 * @return
	 */
//	@RequestMapping("/isCodeValid")
//	@ResponseBody
//	public RequestResult isCodeValid(CustomerVo vo){
//		return customerService.isCodeValid(vo);
//	}
	

	/**
	 * 模糊查询(根据客户编号、客户名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@GetMapping("/vagueSearch")
	@ApiOperation("模糊查询(根据客户编号、客户名称)")
	public List<CustomerVo> vagueSearch(CustomerVo vo){
		return customerService.vagueSearch(vo);
	}
	
}
