package cn.fooltech.fool_ops.web.payment;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.NumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.Splitter;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;


/**
 * <p>
 * 付款单控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-10-06
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/payBill")
public class PayBillController extends BaseController{
	@Autowired
	private PaymentBillService paymentBillWebService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PaymentCheckService checkService;
	
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
	
	/**
	 *付款单管理
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/warehourse/fkd/manage";
	}
	
	/**
	 *新增付款单
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		String code=paymentBillWebService.getNewCode(52);
		request.setAttribute("code", code);
		
		StockPeriod period = periodService.getLastNotCheck();
		if(period!=null){
			request.setAttribute("startDate", DateUtilTools.date2String(period.getStartDate()));
			request.setAttribute("endDate", DateUtilTools.date2String(period.getEndDate()));
		}
		
		return "/warehourse/fkd/edit";
	}
	
	/**
	 *获取收付款单信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getById")
	public PaymentBillVo getPaymentBill(String id){
		return paymentBillWebService.getById(id);
	}
	
	/**
	 *获取收付款单信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByWarehouseId")
	public PageJson getByWarehouseId(@RequestParam String billId, @RequestParam Integer billType, 
			PageParamater paramater){
		Page<PaymentBillVo> byWarehouseId = checkService.getByWarehouseId(billId, billType, paramater);
		return new PageJson(byWarehouseId);
	}
	
	/**
	 *修改付款单
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String fid,@RequestParam(value = "mark", defaultValue = "0") Integer mark,HttpServletRequest request){
		PaymentBillVo obj=paymentBillWebService.getById(fid);
		if(mark == 1) {
			request.setAttribute("code", paymentBillWebService.getNewCode(52));
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
		return "/warehourse/fkd/edit"; 
	}
	
	/**
	 *付款单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(PaymentBillVo vo,PageParamater pageParamater, String saveStatus){
		vo.setBillType(52);
		if ("1".equals(saveStatus)) {
			vo.setSaveAsBankBillOperatorId("1");
		} else if ("0".equals(saveStatus)) {
			vo.setSaveAsBankBillOperatorId("0");
		}
		Page<PaymentBillVo> page = paymentBillWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 *保存付款单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(PaymentBillVo vo){
		return paymentBillWebService.save(vo);
	}
	
	/**
	 *删除付款单
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(PaymentBillVo vo){
		return paymentBillWebService.delete(vo.getFid());
	}
	
	/**
	 *审核
	 * @return
	 */
	@RequestMapping(value="/passAudit")
	@ResponseBody
	public RequestResult passAudit(PaymentBillVo vo){
		return paymentBillWebService.passAudit(vo.getFid());
	}
	
	/**
	 *作废
	 * @return
	 */
	@RequestMapping(value="/cancel")
	@ResponseBody
	public RequestResult cancle(PaymentBillVo vo){
		return paymentBillWebService.cancle(vo.getFid());
	}

	/**
	 * 打印
	 * @param id
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("/skdPrint")
	public String sdkPrint(String id,String code, Model model){
		PaymentBillVo obj=paymentBillWebService.getById(id);
		model.addAttribute("orgName",orgService.get(obj.getOrgId()).getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "skd");
		
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
	 * 打印
	 * @param id
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("/fkdPrint")
	public String fkdPrint(String id,String code, Model model){
		PaymentBillVo obj=paymentBillWebService.getById(id);
		model.addAttribute("orgName",orgService.get(obj.getOrgId()).getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", "fkd");
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
	 * 打印
	 * @param code
	 * @param billIds 单据ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/printMulti/{code}")
	public String printMulti(@PathVariable(value="code") String code, @RequestParam String billIds, Model model){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> ids = splitter.splitToList(billIds);
		List<PaymentBillVo> obj = paymentBillWebService.getByIds(ids);
		obj = mergeBills(obj);
		model.addAttribute("orgName",SecurityUtil.getCurrentOrg().getOrgName());
		model.addAttribute("data", JSONArray.fromObject(obj));
		model.addAttribute("code", code);
		String orgFid = SecurityUtil.getCurrentOrgId();
		PrintTemp  temp =  printTempWebService.getByOrgCode(orgFid, code);
		if (temp != null) {
			String printUrl = temp.getPrintTempUrl();
			if (temp.getPageRow() != null) {
				model.addAttribute("pageRow", temp.getPageRow());
			}
			if (StringUtils.isNotBlank(printUrl)) {
				return printUrl;
			}
			
		}
		return "/warehourse/fkd/print";
	}

	/**
	 * 合并打印单据
	 * @param bills
	 * @return
	 */
	private List<PaymentBillVo> mergeBills(List<PaymentBillVo> bills){
		Map<String, PaymentBillVo> map = Maps.newLinkedHashMap();
		for(PaymentBillVo vo:bills){
			String key = vo.getBankId()+"#";
			if(vo.getBillType()== WarehouseBuilderCodeHelper.fkd){
				key = key+vo.getSupplierId();
			}else{
				key = key+vo.getCustomerId();
			}

			if(map.containsKey(key)){
				PaymentBillVo exist = map.get(key);
				BigDecimal val1 = new BigDecimal(exist.getAmount());
				BigDecimal val2 = new BigDecimal(vo.getAmount());
				exist.setAmount(NumberUtil.bigDecimalToStr(NumberUtil.add(val1, val2)));
				map.put(key, exist);
			}else{
				map.put(key, vo);
			}
		}
		return Lists.newArrayList(map.values());
	}
	
	@RequestMapping("/findTemp")
	@ResponseBody
	public RequestResult findTemp(String code, Model model){
		String orgFid = SecurityUtil.getCurrentOrgId();
		PrintTemp  temp =  printTempWebService.getByOrgCode(orgFid, code);
		if (temp != null) {
			if (temp.getPageRow() != null) {
				return new RequestResult(RequestResult.RETURN_SUCCESS,String.valueOf(temp.getPageRow()));
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE,String.valueOf(5));
			}
			
		}
		return new RequestResult(RequestResult.RETURN_FAILURE,String.valueOf(5));
	}

	/**
	 * 保存收付费单到银行日记单
	 * @param billIds
	 * @return
	 */
	@RequestMapping("save/as/bankBill")
    @ResponseBody
	public RequestResult savePaymentBill2BankBill(@RequestParam String billIds){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		return paymentBillWebService.savePaymentBill2BankBill(splitter.splitToList(billIds));
	}
}
