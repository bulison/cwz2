package cn.fooltech.fool_ops.web.transport;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * <p>
 *货品运输计价换算关系网页控制器
 * </p>
 * @author cwz
 * @date 2016-12-7
 */
@RestController
@RequestMapping("/api/goodsTransport")
public class GoodsTransportApiController {
	private Logger logger = LoggerFactory.getLogger(GoodsTransportApiController.class);
	@Autowired
	private GoodsTransportService service;


	/**
	 * 获取货品运输计价换算关系列表
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation("获取货品运输计价换算关系列表")
	public PageJson query(GoodsTransportVo vo,PageParamater pageParamater){
		Page<GoodsTransportVo> page = service.query(vo, pageParamater);
		return new PageJson(page);
	}

	/**
	 * 新增、编辑货品运输计价换算关系
	 * 
	 * @param vo
	 * @return
	 */
	
	@PutMapping("/save")
	@ApiOperation("保存货品运输计价换算关系")
	public RequestResult save(GoodsTransportVo vo) {
		return service.save(vo);
	}

	/**
	 * 删除货品运输计价换算关系
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete")
	@ApiOperation("删除货品运输计价换算关系")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}

	/**
	 * 根据货品及属性ID查询运输单位
	 * @param goodsId
	 * @param specId
	 * @return
	 */
	@GetMapping("/queryTransportUnit")
	@ApiOperation("根据货品及属性ID查询运输单位")
	public List<AuxiliaryAttrVo> queryTransportUnit(@RequestParam String goodsId, String specId) {
		return service.queryUnitVos(goodsId,specId);
	}

}
