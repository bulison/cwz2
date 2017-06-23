package cn.fooltech.fool_ops.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import io.swagger.annotations.ApiOperation;

/**
 * <p>财务会计期间控制器类</p>  
 * @author cwz
 * @date 2017年6月9日
 */
@RestController
@RequestMapping(value = "/api/fiscalPeriod")
public class FiscalPeriodResource extends AbstractBaseResource{
	@Autowired
	private FiscalPeriodService periodService;
	/**
	 * 获得第一个未结账的会计期间
	 * @return
	 */
    @ApiOperation("获得第一个未结账的会计期间")
    @GetMapping("/getFristUnCheckPeriod")
	public ResponseEntity<FiscalPeriodVo> getFristUnCheckPeriod(){
    	 FiscalPeriodVo vo = periodService.getVo(periodService.getFirstNotCheck());
    	 return new ResponseEntity<>(vo, HttpStatus.OK);
	}
}
