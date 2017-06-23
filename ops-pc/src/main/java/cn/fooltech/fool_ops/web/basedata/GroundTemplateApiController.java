package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 场地费报价模板控制器
 * @author xjh
 * @since 1.0
 */
@RestController
@RequestMapping("/api/groundTemplate")
public class GroundTemplateApiController extends BaseController{

	private Logger logger = Logger.getLogger(GroundTemplateApiController.class);
	
	@Autowired
	private GroundTemplateService groundTemplateService;

	@ApiOperation("获取场地费报价模板列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name="groundId", value="场地ID", required = false, dataType = "string", paramType = "query")
	})
	@GetMapping("/list")
	public List<GroundTemplateVo> list(String searchKey, String groundId, String addressId){
		List<GroundTemplateVo> list = groundTemplateService.query(searchKey, groundId, addressId);
		return list;
	}

	@ApiOperation("保存场地费报价模板主表描述")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="id", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name="describe", value="描述", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name="updateTime", value="修改时间戳", required = false, dataType = "string", paramType = "query")
	})
	@PutMapping("/save")
	public RequestResult save(@RequestParam String id, String describe, @RequestParam String updateTime){
		return groundTemplateService.save(id, describe, updateTime);
	}

	@ApiOperation("删除场地费报价模板")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="id", required = true, dataType = "string", paramType = "query")
	})
	@DeleteMapping("/delete")
	public RequestResult delete(@RequestParam String id){
		return groundTemplateService.delete(id);
	}
}
