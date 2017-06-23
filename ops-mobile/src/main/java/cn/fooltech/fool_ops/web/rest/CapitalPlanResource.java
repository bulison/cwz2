package cn.fooltech.fool_ops.web.rest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @Description:资金计划resource
 * @author hjr
 * @date 2017年6月21日
 */
@RestController
@RequestMapping(value = "/api/capitalPlan")
public class CapitalPlanResource extends AbstractBaseResource{
	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanService planService;
	/**
	 * 资金计划审核（自动添加记录）
	 * @param relationSign	关联类型
	 * @param relationId	关联id
	 * @return
	 */
	@ApiOperation("资金计划审核（自动添加记录）")
	@PostMapping("/capitalPassAudit")
	public RequestResult capitalPassAudit(int relationSign, String relationId) {
		RequestResult result = planService.capitalPassAudit(relationSign, relationId);
		return result;
	}
	/**
	 * 根据关联id返回资金计划和明细
	 * @param relationId 关联id
	 * @return
	 */
	@PostMapping("/queryByWarehouse")
	@ApiOperation("根据关联id返回资金计划和明细")
	public CapitalPlanVo queryByWarehouse(String relationId){
		CapitalPlanVo vo = planService.queryByWarehouse(relationId);
		return vo;
	}
	/**
	 * 判断资金计划的金额是否等于单据金额
	 * @param relationId	单据id
	 * @param billAmount	仓储单据金额
	 * @return
	 */
	@PostMapping("/checkPlanAmount")
	@ApiOperation("判断资金计划的金额是否等于单据金额")
	public RequestResult checkPlanAmount(String relationId,BigDecimal billAmount){
		RequestResult result = planService.checkPlanAmount(relationId,billAmount);
		return result;
	}
	/**
	 * 根据仓库单据ID删除资金计划
	 * @param relationId	单据id
	 * @return
	 */
	@PostMapping("/delByWarehouseBill")
	@ApiOperation("根据仓库单据ID删除资金计划")
	public RequestResult delByWarehouseBill(String billId){
		RequestResult result = planService.delByWarehouseBill(billId);
		return result;
	}
}
