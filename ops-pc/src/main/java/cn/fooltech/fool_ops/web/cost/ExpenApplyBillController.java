package cn.fooltech.fool_ops.web.cost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.cost.service.ExpenApplyBillService;
import cn.fooltech.fool_ops.domain.cost.vo.ExpenApplyBillVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.StrUtil;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>费用申请单网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-03 15:32:22
 */
@Controller
@RequestMapping(value = "/expenApplyBill")
public class ExpenApplyBillController extends BaseController{
	
	/**
	 * 费用申请单网页服务类
	 */
	@Autowired
	private ExpenApplyBillService expenApplyBillWebService;
	
	/**
	 * 单据单号生成规则网页服务类
	 */
	@Autowired
	private BillRuleService billRuleWebService;
	
	/**
	 * 去费用申请单列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		return "warehourse/expenApply/manage";
	}
	
	/**
	 * 查找费用申请单列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(ExpenApplyBillVo expenApplyBillVo,PageParamater pageParamater){
		Page<ExpenApplyBillVo> page = expenApplyBillWebService.query(expenApplyBillVo,pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 编辑费用申请单页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, String id, @RequestParam(value = "mark", defaultValue = "0") Integer mark){
		ExpenApplyBillVo expenApplyBillVo=new ExpenApplyBillVo();
		if(StrUtil.notEmpty(id)){
			expenApplyBillVo = expenApplyBillWebService.getById(id);
		}
		if(mark == 1) {
			model.addAttribute("code", getNewCode());
			model.addAttribute("mark", mark);
			expenApplyBillVo.setFid(null);
			expenApplyBillVo.setCreateTime(null);
			expenApplyBillVo.setCreatorName(null);
			expenApplyBillVo.setCancelTime(null);
			expenApplyBillVo.setCancelName(null);
			expenApplyBillVo.setAuditTime(null);
			expenApplyBillVo.setAuditorName(null);
			expenApplyBillVo.setUpdateTime(null);
			expenApplyBillVo.setRecordStatus(null);
		}
		model.put("obj",expenApplyBillVo);
		return "warehourse/expenApply/edit";
	}
	
	/**
	 * 获取新单号
	 * @param buildCode
	 * @return
	 */
	protected String getNewCode(){
		int billType = WarehouseBuilderCodeHelper.getBillType(WarehouseBuilderCode.fysqd);
		return billRuleWebService.getNewCode(billType);
	}
	
	/**
	 * 新增/编辑费用申请单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(ExpenApplyBillVo expenApplyBillVo){
		return expenApplyBillWebService.save(expenApplyBillVo);
	}
	
	/**
	 * 删除费用申请单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String fid){
		return expenApplyBillWebService.delete(fid);
	}
	
	/**
	 * 作废费用申请单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancle")
	public RequestResult cancle(@RequestParam String fid){
		return expenApplyBillWebService.cancle(fid);
	}
	
	/**
	 * 审核费用申请单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/passAudit")
	public RequestResult passAudit(@RequestParam String fid){
		return expenApplyBillWebService.passAudit(fid);
	}
	
}