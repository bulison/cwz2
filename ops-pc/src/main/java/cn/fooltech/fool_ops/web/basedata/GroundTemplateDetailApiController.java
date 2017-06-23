package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateDetailService;
import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateDetailVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 场地费报价模板明细控制器
 * @author xjh
 * @since 1.0
 */
@RestController
@RequestMapping("/api/groundTemplateDetail")
public class GroundTemplateDetailApiController  extends BaseController {

	private Logger logger = Logger.getLogger(GroundTemplateDetailApiController.class);
	
	@Autowired
	private GroundTemplateDetailService gtdService;

	@ApiOperation("获取场地费报价模板明细列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name="templateId", value="模板ID", required = true, dataType = "string", paramType = "query")
	})
	@GetMapping("/list")
	public PageJson list(@RequestParam String templateId, PageParamater paramater){
		Page<GroundTemplateDetailVo> page = gtdService.query(templateId, paramater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}

	@ApiOperation("保存场地费报价模板明细表描述")
	@PutMapping("/save")
	public RequestResult save(GroundTemplateDetailVo vo){
		return gtdService.save(vo);
	}

	@ApiOperation("删除场地费报价模板明细")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="id", required = true, dataType = "string", paramType = "query")
	})
	@DeleteMapping("/delete")
	public RequestResult delete(@RequestParam String id){
		return gtdService.delete(id);
	}

}
