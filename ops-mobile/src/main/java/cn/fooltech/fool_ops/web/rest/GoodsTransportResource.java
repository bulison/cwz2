package cn.fooltech.fool_ops.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import io.swagger.annotations.ApiOperation;

/**
 * 货品运输计价换算关系resource
 * @author hjr
 * 2017-6-15
 */
@RestController
@RequestMapping("/api/goodsTransport")
public class GoodsTransportResource extends AbstractBaseResource{
	@Autowired
	private GoodsTransportService service;
	/**
	 * 根据货品及属性ID查询运输单位
	 * @param goodsId
	 * @param specId
	 * @return
	 */
	@GetMapping("/queryTransportUnit")
	@ApiOperation("根据货品及属性ID查询运输单位")
	public ResponseEntity queryTransportUnit(@RequestParam String goodsId, String specId) {
		return listReponse(service.queryUnitVos(goodsId,specId));
	}
}
