
package cn.fooltech.fool_ops.web.basedata;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateDetail1Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateDetail2Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 运输费报价模板网页控制器类
 * </p>
 * 
 * @author cwz
 * @date 2016-12-9
 */
@RestController
@RequestMapping("/api/transportTemplate")
public class TransportTemplateApiController extends BaseController {
	private Logger logger = Logger.getLogger(TransportTemplateApiController.class);
	/**
	 * 运输费报价模板服务类
	 */
	@Autowired
	private TransportTemplateService templateService;
	/**
	 * 运输费报价模板(从1表)服务类
	 */
	@Autowired
	private TransportTemplateDetail1Service detail1Service;
	/**
	 * 运输费报价模板(从2表)服务类
	 */
	@Autowired
	private TransportTemplateDetail2Service detail2Service;

	/**
	 * 运输费报价模板表信息(JSON)
	 * 
	 * @param vo
	 * @param page
	 *            当前页数
	 * @param rows
	 *            页面大小
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价模板表信息")
	@GetMapping("/list")
	public @ResponseBody PageJson list(TransportTemplateVo vo, PageParamater pageParamater, Model model) {
		Page<TransportTemplateVo> query = templateService.query(vo, pageParamater);
		return templateService.getPageJson(query.getContent(), query.getTotalElements());
	}

	/**
	 * 运输费报价模板1详细信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价模板1详细信息")
	@GetMapping("/detail")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "templateId", value = "模板ID", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "id", value = "从1表ID", required = true, dataType = "string", paramType = "query") })
	public PageJson detail(@RequestParam String templateId, @RequestParam String id, Model model) {
		TransportTemplateDetail1 queryByFid = detail1Service.queryByFid(SecurityUtil.getFiscalAccountId(), templateId,
				id);
		TransportTemplateDetail1Vo vo = detail1Service.getVo(queryByFid);
		PageJson pageJson = new PageJson();
		pageJson.setRows(vo);
		pageJson.setTotal(1L);
		return pageJson;
	}

	/**
	 * 运输费报价模板2详细信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价模板2详细信息")
	@GetMapping("/detail2")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "templateId", value = "模板ID", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "id", value = "从1表ID", required = true, dataType = "string", paramType = "query") })
	public PageJson detail2(@RequestParam String templateId, @RequestParam String id, Model model) {
		TransportTemplateDetail2 queryByFid = detail2Service.queryByFid(SecurityUtil.getFiscalAccountId(), templateId,
				id);
		model.addAttribute("vo", detail2Service.getVo(queryByFid));
		TransportTemplateDetail2Vo vo = detail2Service.getVo(queryByFid);
		PageJson pageJson = new PageJson();
		pageJson.setRows(vo);
		pageJson.setTotal(1L);
		return pageJson;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 */

	@ApiOperation("保存运输费报价模板")
	@PutMapping("/save")
	public RequestResult save(TransportTemplateVo vo) {
		try {
			return templateService.save(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RequestResult(e.getMessage());
		}
	}

	/**
	 * 删除运输费报价模板信息
	 * 
	 * @param vo
	 * @return
	 */
	@ApiOperation("删除运输费报价模板信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "query") })
	@DeleteMapping("/delete")
	public RequestResult delete(@RequestParam String id) {
		return templateService.delete(id);
	}

}
