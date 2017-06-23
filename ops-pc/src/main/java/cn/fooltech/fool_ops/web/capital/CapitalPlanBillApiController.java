package cn.fooltech.fool_ops.web.capital;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanBillService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @Description:资金计划关联单据 网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@RestController
@RequestMapping(value = "/api/capitalPlanBill")
public class CapitalPlanBillApiController extends BaseController{
	
	/**
	 * 单据物料网页服务类
	 */
	@Autowired
	private CapitalPlanBillService billService;
	
	
	/**
	 * 资金计划关联列表
	 * @return
	 */
	@ApiOperation("获取资金计划关联列表")
	@GetMapping("/list")
	@ResponseBody
	public PageJson list(CapitalPlanBillVo vo,PageParamater pageParamater){
		Page<CapitalPlanBillVo> page = billService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 资金计划关联编辑页面
	 */
	@GetMapping("/edit")
	@ResponseBody
	public CapitalPlanBillVo edit(String id) {
		if(StringUtils.isNotBlank(id)){
			CapitalPlanBillVo vo=billService.getById(id);
			return vo;
		}
		return null;
	}
	
	@ApiOperation("保存资金计划关联")
	@PutMapping("/save")
	public RequestResult save(CapitalPlanBillVo vo) {
		return billService.save(vo);
	}
	
	/**
	 * 删除资金计划关联
	 * @param id
	 * @param bindType 类型 1-单据金额，2-收付款金额
	 * @return
	 */
	@DeleteMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id, Integer bindType) {
		return billService.delete(id,bindType);
	}
	
	
	
}