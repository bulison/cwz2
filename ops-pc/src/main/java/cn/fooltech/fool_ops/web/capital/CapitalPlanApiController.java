package cn.fooltech.fool_ops.web.capital;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @Description:资金计划 网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@RestController
@RequestMapping(value = "/api/capitalPlan")
public class CapitalPlanApiController extends BaseController {

	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanService planService;

	/**
	 * 资金计划列表
	 * 
	 * @return
	 */
	@ApiOperation("获取资金计划列表")
	@GetMapping("/list")
	@ResponseBody
	public PageJson list(CapitalPlanVo vo, PageParamater pageParamater) {
		Page<CapitalPlanVo> page = planService.query(vo, pageParamater);
		return new PageJson(page);
	}

	/**
	 * 资金计划编辑页面
	 */
	@ApiOperation("根据主键查询资金计划")
	@GetMapping("/edit")
	@ResponseBody
	public CapitalPlanVo edit(String id) {
		if (StringUtils.isNotBlank(id)) {
			CapitalPlanVo vo = planService.getById(id);
			return vo;
		}
		return null;
	}

	@ApiOperation("保存资金计划")
	@PutMapping("/save")
	@ResponseBody
	public RequestResult save(@RequestBody CapitalPlanVo vo) {
		return planService.save(vo,1);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除资金计划")
	@DeleteMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id) {
		return planService.delete(id);
	}
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param id			主键
	 * @param recordStatus	修改后的状态
	 * @param updateTime	修改时间
	 * @param editType		可否限制：0-不限制，1-限制
	 * @return
	 */
	@ApiOperation("根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】")
	@GetMapping("/changeByCapitalId")
	@ResponseBody
	public RequestResult changeByCapitalId(String id, int recordStatus,String updateTime,Integer editType) {
		RequestResult result = planService.changeByCapitalId(id, recordStatus,updateTime,editType);
		return result;
	}
	@ApiOperation("根据关联id返回资金计划和明细")
	@GetMapping("/queryByWarehouse")
	@ResponseBody
	public CapitalPlanVo queryByWarehouse(String relationId){
		CapitalPlanVo vo = planService.queryByWarehouse(relationId);
		return vo;
	}
}