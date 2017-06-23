package cn.fooltech.fool_ops.web.freight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 收货地址网页控制器
 * </p>
 * 
 * @author cwz
 * @date 2016-12-5
 */
@RestController
@RequestMapping("/api/freightAddress")
public class FreightAddressApiController {
	private Logger logger = LoggerFactory.getLogger(FreightAddressApiController.class);
	@Autowired
	private FreightAddressService service;


	/**
	 * 收货地址树
	 * @return
	 */
	@GetMapping(value = "/getTree")
	@ApiOperation("获取收货地址树")
	public List<FreightAddressVo> getTree(FreightAddressVo vo){

		List<FreightAddressVo> treeData = service.getTree(vo);
		return treeData;

	}
	/**
	 *  获取货运地址表下拉树
	 * @return
	 */
	@GetMapping(value = "/findAddressTree")
	@ApiOperation("获取货运地址表下拉树")
    public List<FreightAddressVo> findAddressTree(FreightAddressVo vo) {
		List<FreightAddressVo> list = service.findAddressTree(vo);
		return list;
    }

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	@ApiOperation("保存货运地址表")
	@PutMapping("/save")
	public RequestResult save(FreightAddressVo vo) {
		return service.save(vo);
	}

	/**
	 * 删除
	 * 
	 * @param fid
	 * @return
	 */
	@ApiOperation("删除货运地址")
	@DeleteMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}
	/**
	 * 调整父地址
	 * @param beforeFid  调整前的fid
	 * @param afterFid	   需要调整的fid
	 * @return
	 */
	@ApiOperation("调整父货运地址")
	@PutMapping("/changeEntity")
	public RequestResult changeEntity(String beforeFid,String afterFid){
		return service.changeEntity(beforeFid, afterFid);
	}

	/**
	 * 根据ID获取货运地址信息
	 * @return
	 */
	@ApiOperation("根据ID获取货运地址信息")
	@GetMapping("/getById")
	public FreightAddressVo getById(String fid){
		return service.getById(fid);
	}
}
