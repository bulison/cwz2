package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerAddressService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerAddressVo;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 客户/供应商默认收/发货地址控制器
 * @author xjh
 * @since 1.0
 */
@RestController
@RequestMapping("/api/customerAddress")
public class CustomerAddressApiController  extends BaseController {

	private Logger logger = Logger.getLogger(CustomerAddressApiController.class);
	
	@Autowired
	private CustomerAddressService customerAddressService;

	@ApiOperation("保存")
	@PutMapping("/save")
	public RequestResult save(CustomerAddressVo vo){
		return customerAddressService.save(vo);
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete")
	public RequestResult delete(String id){
		return customerAddressService.delete(id);
	}


	@ApiOperation("获取列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name="csvId", value="往来单位ID", required = true, dataType = "string", paramType = "query")
	})
	@GetMapping("/list")
	public List<CustomerAddressVo> list(String csvId){
		return customerAddressService.query(csvId);
	}

	@ApiOperation("设置默认")
	@PutMapping("/updateDefault")
	public RequestResult updateDefault(String id){
		return customerAddressService.updateDefault(id);
	}

	@ApiOperation("根据收支单位（客户ID/供应商ID）获取默认地址，没有则返回空")
	@GetMapping("/getByCsvId")
	public FreightAddressVo getFreightAddressByCsvId(@RequestParam String csvId){
		return customerAddressService.getFreightAddressByCsvId(csvId);
	}
}
