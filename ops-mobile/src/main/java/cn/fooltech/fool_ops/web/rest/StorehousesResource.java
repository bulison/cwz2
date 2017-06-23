package cn.fooltech.fool_ops.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.domain.basedata.service.StorehousesService;
import cn.fooltech.fool_ops.domain.basedata.vo.FastStorehousesVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/storehouses")
public class StorehousesResource {
	@Autowired
	private StorehousesService storehousesWebService;
	@GetMapping("/getTree")
	@ApiOperation("获取所有仓库信息")
	public List<FastStorehousesVo> getFastTree(){
		return storehousesWebService.getFastTree();
	}
}
