package cn.fooltech.fool_ops.web.payment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>
 * 收款单控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-10-06
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/receiveBill")
public class ReceiveBillController extends BaseController{
	@Autowired
	private PaymentBillService paymentBillWebService;
	
	/**
	 * 会计期间Web服务类
	 */
	@Autowired
	protected StockPeriodService periodService;
	
	
	/**
	 *收款单管理
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/warehourse/skd/manage";
	}
	
	/**
	 *新增收款单
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		String code=paymentBillWebService.getNewCode(51);
		request.setAttribute("code", code);
		
		StockPeriod period = periodService.getLastNotCheck();
		if(period!=null){
			request.setAttribute("startDate", DateUtilTools.date2String(period.getStartDate()));
			request.setAttribute("endDate", DateUtilTools.date2String(period.getEndDate()));
		}
		
		return "/warehourse/skd/edit";
	}
	
	/**
	 *修改收款单
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String fid,@RequestParam(value = "mark", defaultValue = "0") Integer mark,HttpServletRequest request){
		PaymentBillVo obj=paymentBillWebService.getById(fid);
		if(mark == 1) {
			request.setAttribute("code", paymentBillWebService.getNewCode(51));
			obj.setTotalCheckAmount(null);
			obj.setRecordStatus(null);
			obj.setAuditorName(null);
			obj.setAuditTime(null);
			obj.setCancelorName(null);
			obj.setCancelTime(null);
			obj.setCreatorName(null);
			obj.setCreateTime(null);
			request.setAttribute("mark", mark);
//			System.out.println(mark);
		}
		request.setAttribute("obj", obj);
		return "/warehourse/skd/edit"; 
	}
	
	/**
	 *收款单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(PaymentBillVo vo, PageParamater pageParamater, String saveStatus){
		vo.setBillType(51);
		if ("1".equals(saveStatus)) {
			vo.setSaveAsBankBillOperatorId("1");
		} else if ("0".equals(saveStatus)) {
			vo.setSaveAsBankBillOperatorId("0");
		}
		Page<PaymentBillVo> page = paymentBillWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 *保存收款单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(PaymentBillVo vo){
		return paymentBillWebService.save(vo);
	}
	
	/**
	 *删除收款单
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
}
