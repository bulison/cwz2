package cn.fooltech.fool_ops.web.capital;

import java.math.BigDecimal;
import java.util.List;

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
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.vo.BindingBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @Description:资金计划 网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@RestController
@RequestMapping(value = "/api/capitalPlanDetail")
public class CapitalPlanDetailApiController extends BaseController {

	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanDetailService detailService;


	/**
	 * 资金计划列表
	 * 
	 * @return
	 */
	@ApiOperation("获取资金计划列表")
	@GetMapping("/list")
	@ResponseBody
	public List<CapitalPlanDetailVo> list(CapitalPlanDetailVo vo, PageParamater pageParamater) {
		pageParamater.setRows(Integer.MAX_VALUE);
		Page<CapitalPlanDetailVo> page = detailService.query(vo, pageParamater);
		List<CapitalPlanDetailVo> list = page.getContent();
		return list;
	}

	/**
	 * 资金计划编辑页面
	 */
	@ApiOperation("根据主键查询资金计划")
	@GetMapping("/edit")
	@ResponseBody
	public CapitalPlanDetailVo edit(String id) {
		if (StringUtils.isNotBlank(id)) {
			CapitalPlanDetailVo vo = detailService.getById(id);
			return vo;
		}
		return null;
	}

	@ApiOperation("保存资金计划")
	@PutMapping("/save")
	@ResponseBody
	public RequestResult save(CapitalPlanDetailVo vo) {
		return detailService.save(vo);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除资金计划")
	@DeleteMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id) {
		return detailService.delete(id);
	}
	/**
	 * 查询未绑定单据
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ApiOperation("查询未绑定单据")
	@GetMapping("/uncheckedList")
	@ResponseBody
	public PageJson uncheckedList(BindingBillVo vo, PageParamater paramater) {
		PageParamater pageParamater = new PageParamater(paramater.getPage(), paramater.getRows(), 0);
		PageJson list = detailService.uncheckedList(vo, pageParamater);
		return list;
	}
	/**
	 * 查询绑定单据（根据明细id查询【detailId】）
	 * @param vo		
	 * @param paramater
	 * @return
	 */
	@ApiOperation("查询绑定单据")
	@GetMapping("/checkedList")
	@ResponseBody
	public Page<CapitalPlanBillVo> checkedList(CapitalPlanBillVo vo, PageParamater paramater) {
		Page<CapitalPlanBillVo> list = detailService.checkedList(vo, paramater);
		return list;
	}
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param id			主键
	 * @param recordStatus	修改后的状态
	 * @param updateTime	修改时间
	 * @param editType 		可否限制：0-不限制，1-限制
	 * @return
	 */
	@ApiOperation("根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】")
	@GetMapping("/changeByCapitalId")
	@ResponseBody
	public RequestResult changeByCapitalId(String id, int recordStatus,String updateTime,Integer editType) {
		RequestResult result = detailService.changeByCapitalId(id, recordStatus,updateTime,editType);
		return result;
	}
	/**
	 * 延期
	 * @param id		明细id
	 * @param date		延期日期
	 * @param remark	备注
	 * @return
	 */
	@ApiOperation("延期")
	@GetMapping("/delayDate")
	@ResponseBody
	public RequestResult delayDate(String id, String date, String remark) {
		RequestResult result = detailService.delayDate(id, DateUtils.getDateFromString(date), remark);
		return result;
	}
	
	/**
	 * 调整金额
	 * @param id		明细id
	 * @param amount	调整后金额
	 * @param remark	备注
	 * @return
	 */
	@ApiOperation("调整金额")
	@GetMapping("/adjustedAmount")
	@ResponseBody
	public RequestResult adjustedAmount(String id, BigDecimal amount, String remark) {
		RequestResult result = detailService.adjustedAmount(id, amount, remark);
		return result;
	}
	/**
	 * 拆分明细
	 * @param id 		明细id
	 * @param amount	拆分金额
	 * @param remark	备注
	 * @return
	 */
	@ApiOperation("拆分明细")
	@GetMapping("/splitDetail")
	@ResponseBody
	public RequestResult splitDetail(String id, BigDecimal amount, String remark) {
		RequestResult result = detailService.splitDetail(id, amount, remark);
		return result;
	}
	/**
	 * 复制资金计划明细表
	 * @param detailId	明细对象ID
	 * @param amount	 拆分金额
	 * @param type		类型：1.单据类型，2.计划或用户手动添加类型
	 * @param zhengfu	正负标识：0-正数，1-负数
	 * @return
	 */
	@ApiOperation("复制资金计划明细表")
	@GetMapping("/copyDetail")
	@ResponseBody
	public RequestResult copyDetail(String detailId, BigDecimal amount, int type,int zhengfu,String remark) {
		RequestResult result = detailService.copyDetail(detailId, amount, type,zhengfu,remark);
		return result;
	}
	
	/**
	 * 合并资金计划明细
	 * @param ids			需要合并的id
	 * @param planId		主表id
	 * @param relationId	关联id
	 * @return
	 */
	@ApiOperation("合并资金计划明细")
	@GetMapping("/mergeDetail")
	@ResponseBody
	public RequestResult mergeDetail(String ids, String planId, String relationId) {
		RequestResult result = detailService.mergeDetail(ids, planId, relationId);
		return result;
	}
	/**
	 * 绑定单据
	 * @param vo
	 * @return
	 */
	@ApiOperation("绑定单据")
	@GetMapping("/bindingBill")
	@ResponseBody
	public RequestResult bindingBill(CapitalPlanBillVo vo) {
		RequestResult result = detailService.bindingBill(vo);
		return result;
	}
}