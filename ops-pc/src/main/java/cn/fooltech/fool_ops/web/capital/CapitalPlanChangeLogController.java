package cn.fooltech.fool_ops.web.capital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanChangeLogService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanChangeLogVo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/capitalPlanChangeLog")
public class CapitalPlanChangeLogController {
	@Autowired
	private CapitalPlanChangeLogService capitalPlanChangeLogService;
	@ApiOperation("显示所有资金计划关联")
	@GetMapping("/queryAll")
	public Page<CapitalPlanChangeLogVo> queryAll(PageParamater pageParamater){
		return capitalPlanChangeLogService.queryAll(pageParamater);
	}
/*	@ApiOperation("根据")
	public Page<CapitalPlanChangeLogVo> query(PageParamater pageParamater,){
		
	}*/
	@ApiOperation("添加或修改资金计划关联")
	@PutMapping("/save")
	public RequestResult save(CapitalPlanChangeLogVo vo){
		return capitalPlanChangeLogService.save(vo);
	}
	@ApiOperation("删除资金计划关联")
	@DeleteMapping("/delete")
	public RequestResult delete(String id){
		return capitalPlanChangeLogService.delete(id);
	}
	

}
