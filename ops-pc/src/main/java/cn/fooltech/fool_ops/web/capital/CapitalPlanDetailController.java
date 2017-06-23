package cn.fooltech.fool_ops.web.capital;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.vo.BindingBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * 
 * @Description:资金计划明细  网页控制器类
 * @author cwz
 * @date 2017年3月1日 上午9:19:27
 */
@Controller
@RequestMapping(value = "/capitalPlanDetail")
public class CapitalPlanDetailController extends BaseController{
	
	/**
	 * 资金计划明细网页服务类
	 */
	@Autowired
	private CapitalPlanDetailService detailService;
	
	
	/**
	 * 资金计划明细列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(CapitalPlanDetailVo vo,PageParamater pageParamater){
		Page<CapitalPlanDetailVo> page = detailService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 资金计划明细列表弹窗
	 */
	@RequestMapping("/window")
	public String window(ModelMap model) {
		return "/capitalManage/capitalPlan/capitalPlanList";
	}
	
	/**
	 * 资金计划明细管理页面
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/capital/planDetail/manage";
	}
	
	/**
	 * 资金计划明细编辑页面
	 */
	@RequestMapping("/edit")
	public String edit(String id, @RequestParam(value = "mark", defaultValue = "0") Integer mark, 
			Model model) {
		if(StringUtils.isNotBlank(id)){
			CapitalPlanDetailVo vo=detailService.getById(id);
			model.addAttribute("obj", vo);
		}
		return "/capital/planDetail/edit";
	}
	
	/**
	 * 绑定单据页面
	 */
	@RequestMapping("/bingingPage")
	public String bingingPage() {
		return "/capitalManage/capitalPlan/bindingBill";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(CapitalPlanDetailVo vo) {
		return detailService.save(vo);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id) {
		return detailService.delete(id);
	}
	/**
	 *  查询未绑定单据
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/uncheckedList")
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
	@RequestMapping("/checkedList")
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
	@RequestMapping("/changeByCapitalId")
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
	@RequestMapping("/delayDate")
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
	@RequestMapping("/adjustedAmount")
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
	@RequestMapping("/splitDetail")
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
	@RequestMapping("/copyDetail")
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
	@RequestMapping("/mergeDetail")
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
	@RequestMapping("/bindingBill")
	@ResponseBody
	public RequestResult bindingBill(CapitalPlanBillVo vo) {
		RequestResult result = detailService.bindingBill(vo);
		return result;
	}
	/**
	 * 根据主表关联id获取资金计划从表记录
	 * @param relationId 主表关联id
	 * @return
	 */
	@RequestMapping("/queryByPlantId")
	@ResponseBody
	public List<CapitalPlanDetailVo> queryByPlantId(String relationId){
		return detailService.queryByPlantId(relationId );
	}
}
	