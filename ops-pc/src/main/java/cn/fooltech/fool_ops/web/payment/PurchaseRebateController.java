package cn.fooltech.fool_ops.web.payment;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.service.SalesRebateService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;

/**
 * 
 * <p>采购返利单控制类</p>
 * @author lgk
 * @date 2016年4月6日下午04:07:09
 * @version V1.0
 */
@Controller
@RequestMapping("/purchaseRebBill")
public class PurchaseRebateController  extends BaseController{
	@Autowired
	private OrgService orgService;
	
	/**
	 * 会计期间Web服务类
	 */
	@Autowired
	protected StockPeriodService periodService;
	
	/**
	 * 打印模板配置服务表
	 */
	@Autowired
	private PrintTempService printTempWebService;
	
	@Autowired
	private PaymentBillService paymentBillWebService;
	
	@Autowired
	private SalesRebateService salesRebateWebService;
	
	/**
	 * 判断在此时间段内是否还存在同一个供应商相同对公标识的采购返利单
	 * @param fid 单据ID
	 * @param supplierId 供应商
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkExist")
	public RequestResult checkExist(@RequestParam String fid, @RequestParam String supplierId, 
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer toPublic ){
		return salesRebateWebService.checkPurchaseRebateExist(fid, supplierId, startDate, endDate, toPublic);
	}
	
	/**
	 *采购返利单管理
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/warehourse/cgfld/cgfldManage";
	}
	/**
	 *新增采购返利单
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		String code=paymentBillWebService.getNewCode(PaymentBill.TYPE_PURCHASE_REBATE);
		request.setAttribute("code", code);
		
		StockPeriod period = periodService.getLastNotCheck();
		if(period!=null){
			request.setAttribute("startDate", DateUtilTools.date2String(period.getStartDate()));
			request.setAttribute("endDate", DateUtilTools.date2String(period.getEndDate()));
		}
		
		return "/warehourse/cgfld/cgfldEdit";
	}
	/**
	 *保存返利单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(PaymentBillVo vo){
		vo.setBillType(PaymentBill.TYPE_PURCHASE_REBATE);
		return salesRebateWebService.saveSales(vo);
	}
	/**
	 *修改付款单
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String id,@RequestParam(value = "mark", defaultValue = "0") Integer mark,HttpServletRequest request){
		PaymentBillVo obj=salesRebateWebService.getById(id);
		if(mark == 1) {
			request.setAttribute("code", paymentBillWebService.getNewCode(PaymentBill.TYPE_PURCHASE_REBATE));
			obj.setTotalCheckAmount(null);
			obj.setRecordStatus(null);
			obj.setAuditorName(null);
			obj.setAuditTime(null);
			obj.setCancelorName(null);
			obj.setCancelTime(null);
			obj.setCreatorName(null);
			obj.setCreateTime(null);
		}
		request.setAttribute("obj", obj);
		return "/warehourse/cgfld/cgfldEdit"; 
	}
	/**
	 *审核
	 * @return
	 */
	@RequestMapping(value="/passAudit")
	@ResponseBody
	public RequestResult passAudit(String id){
		return salesRebateWebService.passPurchAudit(id);
	}
	/**
	 *付款单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(PaymentBillVo vo,PageParamater pageParamater){
		vo.setBillType(PaymentBill.TYPE_PURCHASE_REBATE);
		Page<PaymentBillVo> page = salesRebateWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	/**
	 *作废
	 * @return
	 */
	@RequestMapping(value="/cancel")
	@ResponseBody
	public RequestResult cancle(String id){
		return salesRebateWebService.cancle(id);
	}
	/**
	 * 打印
	 * @param id 单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/cgfldPrint")
	public String sdkPrint(String id,String code, Model model){
		PaymentBillVo obj=salesRebateWebService.getById(id);
		model.addAttribute("orgName",orgService.get(obj.getOrgId()).getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "xsfld");
		PrintTemp  temp =  printTempWebService.getByOrgId(orgService.get(obj.getOrgId()).getOrgId(), code);
		if(temp != null){
			if(temp.getPageRow()!=null){
				model.addAttribute("pageRow", temp.getPageRow());
			}
			String printUrl = temp.getPrintTempUrl();
			if(StringUtils.isNotBlank(printUrl)){
				return printUrl;
			}
			
		}
		return "/warehourse/fkd/print";
	}
	/**
	 * 获取本期采购
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/getSales")
	@ResponseBody
	public String getSales(String startTime,String endTime,String supplierId){
		return salesRebateWebService.getPurchSummary(startTime, endTime, supplierId);
	}
	/**
	 *删除付款单
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return paymentBillWebService.delete(id);
	}
	
	/**
	 * 获取收/负款单号 和 收/负款金额	
	 * @param request
	 * @param model
	 * @param type 单据类型（收款单：51；付款的52；）
	 * @param fid  单据id
	 * @return
	 */
	@RequestMapping(value = "/getCodeAndAmount")
	@ResponseBody
	public RequestResult getCodeAndAmount(HttpServletRequest request,ModelMap model,Integer type,String fid){
		String code="";
		if (type!=null&&type==51) {
			code=paymentBillWebService.getNewCode(type);
		}
		else if(type!=null&&type==52){
			code=paymentBillWebService.getNewCode(type);
		}
		model.put("code", code);
		if(Strings.isNullOrEmpty(fid)) return new RequestResult(RequestResult.RETURN_FAILURE,"编号不能为空！");
		PaymentBillVo obj = salesRebateWebService.getById(fid);
		//费用金额
		BigDecimal amount = Strings.isNullOrEmpty(obj.getAmount())?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(obj.getAmount()));
		//累计收付款金额
		BigDecimal totalCheckAmount =Strings.isNullOrEmpty(obj.getTotalCheckAmount())?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(obj.getTotalCheckAmount()));
		model.put("amount", totalCheckAmount.subtract(amount));
		model.put("totalPayAmount", totalCheckAmount);
		return new RequestResult(RequestResult.RETURN_SUCCESS, "操作成功",model);
	}
	/**
	 * 保存收付款单和勾对表记录
	 * @param vo		       表单传输对象 - 收付款单
	 * @param ckBillType  勾对单据类型：11.采购入库;12.采购退货;15.采购发票;41.销售出货;42.销售退货;44.销售发票.
	 * @param billId  	单据id
	 * @return
	 */
	@RequestMapping(value="/savePaymentReceived")
	@ResponseBody
	public RequestResult savePaymentReceived (PaymentBillVo vo,Integer ckBillType,String billId){
		return paymentBillWebService.savePaymentReceived(vo,ckBillType,billId);
	}
}
