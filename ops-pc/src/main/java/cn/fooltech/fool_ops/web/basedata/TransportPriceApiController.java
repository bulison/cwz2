
package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceDetail1Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceDetail2Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 运输费报价网页控制器类
 * </p>
 * 
 * @author cwz
 * @date 2016-12-9
 */
@RestController
@RequestMapping("/api/transportPrice")
public class TransportPriceApiController extends BaseController {
	private Logger logger = Logger.getLogger(TransportPriceApiController.class);
	/**
	 * 运输费报价服务类
	 */
	@Autowired
	private TransportPriceService priceService;
	/**
	 * 运输费报价(从1表)服务类
	 */
	@Autowired
	private TransportPriceDetail1Service detail1Service;
	/**
	 * 运输费报价(从2表)服务类
	 */
	@Autowired
	private TransportPriceDetail2Service detail2Service;

	/**
	 * 运输费报价表信息(JSON)
	 * 
	 * @param vo
	 * @param page
	 *            当前页数
	 * @param rows
	 *            页面大小
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价表信息")
	@GetMapping("/list")
	public @ResponseBody PageJson list(TransportPriceVo vo, PageParamater pageParamater, Model model) {
		Page<TransportPriceVo> query = priceService.query(vo, pageParamater);
		return priceService.getPageJson(query.getContent(), query.getTotalElements());
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 */

	@ApiOperation("保存运输费报价模板")
	@PutMapping("/save")
	public RequestResult save(@RequestBody TransportPriceVo vo) {
		return priceService.save(vo);
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
		return priceService.delete(id);
	}
	/**
	 * 运输费报价模板1详细信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价1详细信息")
	@GetMapping("/detail")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "billId", value = "主表ID", required = true, dataType = "string", paramType = "query")})
	public PageJson detail(@RequestParam String billId,PageParamater paramater) {
		List<TransportPriceDetail1Vo> query = detail1Service.query(billId, paramater);
		PageJson pageJson = new PageJson();
		pageJson.setRows(query);
		pageJson.setTotal((long) query.size());
		return pageJson;
	}
	/**
	 * 运输费报价模板2详细信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@ApiOperation("获取运输费报价2详细信息")
	@GetMapping("/detail2")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "billId", value = "主表ID", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "detail1Id", value = "从表ID", required = true, dataType = "string", paramType = "query")
		
	})
	public List<TransportPriceDetail2Vo> detail2(@RequestParam String billId,String detail1Id) {
		List<TransportPriceDetail2Vo> query = detail2Service.query(billId, detail1Id);
		return query;
	}

}
