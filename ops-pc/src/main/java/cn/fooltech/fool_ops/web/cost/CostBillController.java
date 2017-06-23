package cn.fooltech.fool_ops.web.cost;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.cost.service.CostBillCheckService;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.cost.vo.CostBillVo;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;

/**
 * <p>費用單控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年10月8日
 */
@Controller
@RequestMapping(value = "/costBill")
public class CostBillController extends BaseController{
	/**
	 * 收付款单网页服务类
	 */
	@Autowired
	private PaymentBillService paymentBillWebService;
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private CostBillService costBillWebService;
	
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	
	@Autowired
	private CostBillCheckService checkService;
	
	/**
	 * 会计期间Web服务类
	 */
	@Autowired
	protected StockPeriodService periodService;
	
	/**
	 *费用单管理
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/warehourse/fyd/manage";
	}
	
	/**
	 *新增费用单
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		String code=costBillWebService.getNewCode();
		request.setAttribute("code", code);
		
		StockPeriod period = periodService.getLastNotCheck();
		if(period!=null){
			request.setAttribute("startDate", DateUtilTools.date2String(period.getStartDate()));
			request.setAttribute("endDate", DateUtilTools.date2String(period.getEndDate()));
		}
		
		return "/warehourse/fyd/edit";
	}
	/**
	 *修改费用单
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String fid,@RequestParam(value = "mark", defaultValue = "0") Integer mark,HttpServletRequest request){
		CostBillVo obj=costBillWebService.getById(fid);
		if(mark == 1) {
			request.setAttribute("code", costBillWebService.getNewCode());
			obj.setTotalCheckAmount(null);
			obj.setRecordStatus(null);
			obj.setAuditorName(null);
			obj.setAuditTime(null);
			obj.setCancelorName(null);
			obj.setCancelTime(null);
			obj.setCreatorName(null);
			obj.setCreateTime(null);
			obj.setTotalUnCheckAmount(null);
		}
		request.setAttribute("obj", obj);
		return "/warehourse/fyd/edit"; 
	}
	
	/**
	 *费用单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(CostBillVo vo,PageParamater pageParamater){
		Page<CostBillVo> page = costBillWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 *保存费用单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(CostBillVo vo){
		return costBillWebService.save(vo);
	}
	
	/**
	 *删除费用单
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(CostBillVo vo){
		return costBillWebService.delete(vo.getFid());
	}
	
	/**
	 *审核
	 * @return
	 */
	@RequestMapping(value="/passAudit")
	@ResponseBody
	public RequestResult passAudit(CostBillVo vo){
		return costBillWebService.passAudit(vo.getFid());
	}
	
	/**
	 *作废
	 * @return
	 */
	@RequestMapping(value="/cancel")
	@ResponseBody
	public RequestResult cancle(CostBillVo vo){
		return costBillWebService.cancle(vo.getFid());
	}
	
	/**
	 *查询费用项目
	 * @return
	 */
	@RequestMapping(value="/fee")
	@ResponseBody
	public List<CommonTreeVo> fee(){
		 return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("008");
	}
	
	/**
	 * 打印
	 * @param id 单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/print")
	public String print(String id, Model model){
		CostBillVo obj=costBillWebService.getById(id);
		model.addAttribute("orgName",orgService.getOrg().getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "fyd");
		return "/warehourse/fkd/print";
	}
	
	
	/**
	 * 打印
	 * @param id 单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/printMulti")
	public String printMulti(String billIds, Model model){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> ids = splitter.splitToList(billIds);
		List<CostBillVo> obj= costBillWebService.getByIds(ids);
		model.addAttribute("orgName",orgService.getOrg().getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "fyd");
		return "/warehourse/fkd/print";
	}
	
	/**
	 *获取收付款单信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getById")
	public CostBillVo getCostBill(String id){
		return costBillWebService.getById(id);
	}
	
	/**
	 * 根据单据ID查找已勾兑的单据
	 * @param billId		单据ID
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByWarehouseId")
	public PageJson getByWarehouseId(@RequestParam String billId, PageParamater paramater){
		return new PageJson(checkService.getByWarehouseId(billId, paramater));
	}
	
	/**
	 * 根据费用单ID查找已勾兑的单据
	 * @param billId		费用单ID
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryByCostBillId")
	public PageJson queryByCostBillId(@RequestParam String billId, PageParamater paramater){
		return new PageJson(checkService.queryByCostBillId(billId, paramater));
	}

	/**
	 * 获取收款单号和收款金额	
	 * @param request 请求对象
	 * @param model 一个Map
	 * @param type 单据类型（收款单：51；付款的52；）
	 * @param fid  费用单id
	 * @return RequestResult
	 */
	@RequestMapping(value="/getCodeAndAmount")
	@ResponseBody
	public RequestResult getCodeAndAmount(HttpServletRequest request,ModelMap model,Integer type,String fid){
		String code = "";
		if (type!=null && type==51) {
			code = paymentBillWebService.getNewCode(type);
		}
		else if (type!=null && type==52) {
			code = paymentBillWebService.getNewCode(type);
		}
		model.put("code", code);

		if (Strings.isNullOrEmpty(fid)) {
			return new RequestResult(RequestResult.RETURN_FAILURE,"编号不能为空！");
		}
		CostBillVo obj = costBillWebService.getById(fid);
		BigDecimal freeAmount = obj.getFreeAmount();//费用金额
		BigDecimal totalPayAmount = obj.getTotalPayAmount();//累计收付款金额
		model.put("amount", freeAmount.subtract(totalPayAmount));

		BigDecimal payAmount = obj.getPayAmount();//填写的付款金额
		if (payAmount != null) {
			model.put("payAmount", payAmount);
		}
		model.put("totalPayAmount", totalPayAmount);
		return new RequestResult(RequestResult.RETURN_SUCCESS, "操作成功", model);
	}

	/**
	 * 保存收付款单和勾对表记录
	 * @param vo		       表单传输对象 - 收付款单
	 * @param expenseType  费用标识 0--不处理 1--增加往来单位应收/应付款项 2--减少往来单位应收/应付款项
	 * @param costBillId  费用单id
	 * @return RequestResult
	 */
	@RequestMapping(value="/savePaymentReceived")
	@ResponseBody
	public RequestResult savePaymentReceived (PaymentBillVo vo,Integer expenseType,String costBillId){
		return costBillWebService.savePaymentReceived(vo,expenseType,costBillId);
	}

	/**
	 * 根据费用单内容筛选单据类别
	 * @param expenseType	费用标识 0--不处理 1--增加往来单位应收/应付款项 2--减少往来单位应收/应付款项
	 * @param csvType		类型 1：客户；2：供应商
	 * @return
	 */
	@RequestMapping(value="/getBillType")
	@ResponseBody
	public RequestResult getBillType (Integer expenseType,Integer csvType){
		return costBillWebService.getBillType(expenseType,csvType);
	}
	
	/**
	 * 根据多个ID获取
	 * @param billIds
	 * @return
	 */
	@RequestMapping(value="/getByIds")
	@ResponseBody
	public List<CostBillVo> getByIds(@RequestParam String billIds){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> ids = splitter.splitToList(billIds);
		return costBillWebService.getByIds(ids);
	}

    /**
     * 核对费用单
     * @param billId 费用单ID
     * @return RequestResult Request对象
     */
    @ResponseBody
    @RequestMapping(value = "check")
    public RequestResult check(@RequestParam String billId) {
        return costBillWebService.check(billId);
    }
}
