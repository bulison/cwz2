package cn.fooltech.fool_ops.web.payment;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import cn.fooltech.fool_ops.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentCheck;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.warehouse.service.bill.ProductionReceiveWebService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>
 * 勾对控制器类
 * </p>
 * @author lzf
 * @version 1.0
 * @date 2015-10-06
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/paymentCheck")
public class PaymentCheckController extends BaseController{
	
	@Autowired
	private PaymentCheckService paymentCheckWebService;
	
	@Autowired
	private ProductionReceiveWebService productionReceiveWebService;

	
	/**
	 *勾选界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(String paymentBillId,HttpServletRequest request){
		request.setAttribute("paymentBillId", paymentBillId);
		return "/warehourse/billCheck/paymentCheck";
	}
	
	/**
	 * 查看某单据的收付款列表
	 * @return
	 */
	@RequestMapping(value = "/checkDetail")
	public String checkDetail(String billId,HttpServletRequest request){
		request.setAttribute("billId", billId);
		return "/warehourse/billCheck/detail";
	}
	
	/**
	 * 查看某单据的收付款列表--弹窗
	 * @return
	 */
	@RequestMapping(value = "/checkingWin")
	public String checkingWin(String billId,String billCode,HttpServletRequest request){
		request.setAttribute("billId", billId);
		return "/warehourse/billCheck/checkingWin";
	}
	
	/**
	 *已勾对单据列表
	 * @return
	 */
	@RequestMapping(value="/checkedList")
	@ResponseBody
	public PageJson checkedList(PaymentCheckVo vo,PageParamater pageParamater){
		Page<PaymentCheckVo> page=paymentCheckWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 勾对的单据列表
	 * @return
	 */
	@RequestMapping(value="/checkedBillList")
	@ResponseBody
	public PageJson checkedBillList(PaymentCheckVo vo,PageParamater pageParamater){
		Page<PaymentCheckVo> page = paymentCheckWebService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 未勾对单据列表
	 * @param billId 付款单ID（必填）
	 * @param checkStartDay 开始日期
	 * @param checkEndDay 结束日期
	 * @param checkBillCode 对单单号
	 * @param checkBillType（必填）
	 */
	@RequestMapping(value="/uncheckedList")
	@ResponseBody
	public PageJson uncheckedList(@RequestParam String billId, String checkStartDay, String checkEndDay, 
			String checkBillCode, @RequestParam Integer checkBillType, PageParamater pagep){
		return paymentCheckWebService.queryCheckBill(billId, checkStartDay, 
				checkEndDay, checkBillCode, checkBillType, pagep);
	}

	/**
	 * 勾对
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/check")
	@ResponseBody
	public RequestResult check(PaymentCheckVo vo) throws Exception{
		PaymentCheckVo checkVo = paymentCheckWebService.queryBillTotalPayAmountByBillId(vo.getBillId());
		BigDecimal amount = vo.getAmount();
		BigDecimal freeAmount = vo.getFreeAmount();
		BigDecimal billTotalAmount = checkVo.getBillTotalPayAmount();

		BigDecimal add = NumberUtil.add(amount, billTotalAmount);
		add = NumberUtil.add(add, freeAmount);
		vo.setBillTotalPayAmount(add);
		vo.setBillFreeAmount(checkVo.getFreeAmount());;
		return paymentCheckWebService.save(vo);
	}
	
	/**
	 *删除
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(PaymentCheckVo vo) throws Exception{
		String checkId = vo.getFid();
		return paymentCheckWebService.delete(checkId);
	}

}
