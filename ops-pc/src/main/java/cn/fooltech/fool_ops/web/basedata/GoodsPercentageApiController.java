package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPercentageService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPercentageVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * <p>
 * 货品提成网页控制器类
 * </p>
 * 
 * @author cwz
 * @date 2017年6月19日
 */
@RestController
@RequestMapping(value = "/api/goodsPercentage")
public class GoodsPercentageApiController {

	/**
	 * 货品提成服务类
	 */
	@Autowired
	private GoodsPercentageService goodsPercentageService;

	/**
	 * 货品提成列表信息
	 * 
	 * @param vo
	 * @param pageParamater
	 * @return
	 */
	@ApiOperation("获取货品提成列表信息")
	@GetMapping(value = "/list")
	public @ResponseBody PageJson list(GoodsPercentageVo vo, PageParamater pageParamater) {
		Page<GoodsPercentageVo> query = goodsPercentageService.query(vo, pageParamater);
		PageJson pageJson = new PageJson(query);
		return pageJson;
	}
	/**
	 * 根据fid获取货品提成
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	@ApiOperation("根据fid获取货品提成")
	@ResponseBody
	public GoodsPercentageVo edit(String id) {
		GoodsPercentageVo percentageVo = goodsPercentageService.getById(id);
		return percentageVo;
	}
	@PutMapping("/save")
	@ApiOperation("保存货品提成")
	public RequestResult save(GoodsPercentageVo vo){
		return goodsPercentageService.save(vo);
	}
	

}
