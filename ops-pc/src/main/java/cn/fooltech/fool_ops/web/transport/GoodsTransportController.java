package cn.fooltech.fool_ops.web.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;

/**
 * <p>
 *货品运输计价换算关系网页控制器
 * </p>
 * @author cwz
 * @date 2016-12-7
 */
@Controller
@RequestMapping("/goodsTransport")
public class GoodsTransportController {
	private Logger logger = LoggerFactory.getLogger(GoodsTransportController.class);
	@Autowired
	private GoodsTransportService service;
	
	/**
	 * 管理界面
	 */
	@RequestMapping("/manage")
	public String manage() {
		return "/basedata/goodsTranBox";
	}
	
	/**
	 * 获取货品运输计价换算关系 列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
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
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(GoodsTransportVo vo) {
		return service.save(vo);
	}

	/**
	 * 删除货品运输计价换算关系
	 * 
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}
	

	/**
	 * 根据货品ID+货品属性ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTopBySpec")
	public GoodsTransportVo findTopBySpec(String goodsId,String goodSpecId,String transportUnitId, String shipmentTypeId){
		return service.findTopBySpec(goodsId, goodSpecId, transportUnitId, shipmentTypeId);
	}

}
