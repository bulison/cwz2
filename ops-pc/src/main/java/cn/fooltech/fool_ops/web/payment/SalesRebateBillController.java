package cn.fooltech.fool_ops.web.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;


/**
 * 
 * <p>销售返利单控制类</p>
 * @author lgk
 * @date 2016年4月6日下午04:16:10
 * @version V1.0
 */
@Controller
@RequestMapping("/salesRebateRebBill")
public class SalesRebateBillController extends BaseController {
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
	 *付款单管理
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/warehourse/xsfld/xsfldManage";
	}
	
	/**
	 *新增付款单
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		String code=paymentBillWebService.getNewCode(PaymentBill.TYPE_SALES_REBATE);
		request.setAttribute("code", code);
		
		StockPeriod period = periodService.getLastNotCheck();
		if(period!=null){
			request.setAttribute("startDate", DateUtilTools.date2String(period.getStartDate()));
			request.setAttribute("endDate", DateUtilTools.date2String(period.getEndDate()));
		}
		
		return "/warehourse/xsfld/xsfldEdit";
	}
	/**
	 *修改付款单
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String id,@RequestParam(value = "mark", defaultValue = "0") Integer mark,HttpServletRequest request){
		PaymentBillVo obj=salesRebateWebService.getById(id);
		if(mark == 1) {
			request.setAttribute("code", paymentBillWebService.getNewCode(PaymentBill.TYPE_SALES_REBATE));
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
		return "/warehourse/xsfld/xsfldEdit"; 
	}
	/**
	 *付款单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(PaymentBillVo vo,PageParamater pageParamater){
		vo.setBillType(WarehouseBuilderCodeHelper.xsfld);
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
	 *保存返利单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(PaymentBillVo vo){
		vo.setBillType(PaymentBill.TYPE_SALES_REBATE);
		return salesRebateWebService.saveSales(vo);
	}
	/**
	 *审核
	 * @return
	 */
	@RequestMapping(value="/passAudit")
	@ResponseBody
	public RequestResult passAudit(String id){
		return salesRebateWebService.passSateAudit(id);
	}
	/**
	 * 打印
	 * @param id 单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/xsfldPrint")
	public String sdkPrint(String id,String code, Model model){
		PaymentBillVo obj=salesRebateWebService.getById(id);
		model.addAttribute("orgName",orgService.get(obj.getOrgId()).getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "xsfld");
		PrintTemp temp = printTempWebService.getByOrgId(
				orgService.get(obj.getOrgId()).getOrgId(), code);
		if (temp != null) {
			if (temp.getPageRow() != null) {
				model.addAttribute("pageRow", temp.getPageRow());
			}
			String printUrl = temp.getPrintTempUrl();
			if (StringUtils.isNotBlank(printUrl)) {
				return printUrl;
			}
		}
		return "/warehourse/fkd/print";
	}
	/**
	 * 获取本期销售
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param customerId 销售商Id
	 * @return
	 */
	@RequestMapping("/getSales")
	@ResponseBody
	public String getSales(String startTime,String endTime,String customerId){
		return salesRebateWebService.getSaleSummary(startTime, endTime,customerId);
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
	 * 判断在此时间段内是否还存在同一个客户相同对公标识的采购返利单
	 * @param fid 单据ID
	 * @param customerId 客户ID
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkExist")
	public RequestResult checkExist(@RequestParam String fid, @RequestParam String customerId, 
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer toPublic ){
		return salesRebateWebService.checkSalesRebateExist(fid, customerId, startDate, endDate, toPublic);
	}
}
