package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerAddressService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerAddressVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 客户/供应商默认收/发货地址控制器
 * @author xjh
 * @since 1.0
 */
@Controller
@RequestMapping("/customerAddress")
public class CustomerAddressController  extends BaseController {

	private Logger logger = Logger.getLogger(CustomerAddressController.class);
	
	@Autowired
	private CustomerAddressService customerAddressService;

	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(CustomerAddressVo vo){
		return customerAddressService.save(vo);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return customerAddressService.delete(id);
	}


	@RequestMapping("/list")
	@ResponseBody
	public List<CustomerAddressVo> list(String csvId){
		return customerAddressService.query(csvId);
	}

	@RequestMapping("/updateDefault")
	@ResponseBody
	public RequestResult updateDefault(String id){
		return customerAddressService.updateDefault(id);
	}

	@RequestMapping("/getByCsvId")
	@ResponseBody
	public FreightAddressVo getFreightAddressByCsvId(String csvId){
		return customerAddressService.getFreightAddressByCsvId(csvId);
	}
}
