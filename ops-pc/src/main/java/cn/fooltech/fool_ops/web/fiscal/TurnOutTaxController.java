package cn.fooltech.fool_ops.web.fiscal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.TurnOutTaxService;
import cn.fooltech.fool_ops.domain.fiscal.vo.TurnOutTaxVo;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>转出未交增值税网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 10:12:03
 */
@Controller
@RequestMapping(value = "/turnOutTax")
public class TurnOutTaxController extends BaseController{
	
	/**
	 * 转出未交增值税网页服务类
	 */
	@Autowired
	private TurnOutTaxService turnOutTaxService;
	
	/**
	 * 凭证网页服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 去转出未交增值税列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model){
		model.put("entity", turnOutTaxService.queryByAccount());
		return "/fiscal/turnOutTax/turnOutTax";
	}
	

	/**
	 * 结转转出未交增值税
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOutUnpaidTax",method=RequestMethod.POST)
	public RequestResult saveOutUnpaidTax(TurnOutTaxVo turnOutTaxVo){
		RequestResult result =  turnOutTaxService.saveOutUnpaidTax(turnOutTaxVo);
		
		if(result.isSuccess() && result.getData()!=null){
			String voucherId = result.getData().toString();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
			}
		}
		return result;
	}
	
	/**
	 * 结转未交增值税
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveChangeUnpaidTax",method=RequestMethod.POST)
	public RequestResult saveChangeUnpaidTax(TurnOutTaxVo turnOutTaxVo){
		RequestResult result = turnOutTaxService.saveChangeUnpaidTax(turnOutTaxVo);
		
		if(result.isSuccess() && result.getData()!=null){
			String voucherId = result.getData().toString();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
			}
		}
		return result;
	}
	
	
	/**
	 * 支付未交增值税
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savePayUnpaidTax",method=RequestMethod.POST)
	public RequestResult savePayUnpaidTax(TurnOutTaxVo turnOutTaxVo){
		RequestResult result = turnOutTaxService.savePayUnpaidTax(turnOutTaxVo);
		
		if(result.isSuccess() && result.getData()!=null){
			String voucherId = result.getData().toString();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
			}
		}
		return result;
	}
	
}