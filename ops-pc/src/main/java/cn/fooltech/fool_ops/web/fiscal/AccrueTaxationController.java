package cn.fooltech.fool_ops.web.fiscal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.AccrueTaxation;
import cn.fooltech.fool_ops.domain.fiscal.service.AccrueTaxationService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.vo.AccrueTaxationVo;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>计提税费网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 16:14:16
 */
@Controller
@RequestMapping(value = "/accrueTaxation")
public class AccrueTaxationController extends BaseController{
	
	private final static String CODE_2221_02 = "2221.02";
	private final static String CODE_6403 = "6403";
	private final static String CODE_2221_09 = "2221.09";
	private final static String CODE_1002 = "1002";
	
	/**
	 * 计提税费网页服务类
	 */
	@Autowired
	private AccrueTaxationService accrueTaxationService;
	
	/**
	 * 科目网页服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 凭证网页服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 去计提税费列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listAccrueTaxation(ModelMap model){
		
		FiscalAccountingSubjectVo subject1 = subjectService.getVo(subjectService.getFirstLeafByCode(CODE_2221_02));
		FiscalAccountingSubjectVo subject2 = subjectService.getVo(subjectService.getFirstLeafByCode(CODE_6403));
		FiscalAccountingSubjectVo subject3 = subjectService.getVo(subjectService.getFirstLeafByCode(CODE_2221_09));
		FiscalAccountingSubjectVo subject4 = subjectService.getVo(subjectService.getFirstLeafByCode(CODE_1002));
		
		model.put("CODE_2221_02", subject1);
		model.put("CODE_6403", subject2);
		model.put("CODE_2221_09", subject3);
		model.put("CODE_1002", subject4);
		
		return "/fiscal/accrueTaxation/accrueTaxation";
	}
	
	/**
	 * 查找计提税费列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<AccrueTaxationVo> query(AccrueTaxationVo accrueTaxationVo){
		
		List<AccrueTaxation> list = accrueTaxationService.query();
		return accrueTaxationService.getVos(list);
	}
	
	
	/**
	 * 新增/编辑计提税费
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(AccrueTaxationVo accrueTaxationVo){
		return accrueTaxationService.save(accrueTaxationVo);
	}
	
	/**
	 * 删除计提税费，多个ID用逗号隔开
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String fids){
		return accrueTaxationService.delete(fids);
	}
	
	/**
	 * 删除所有<br>
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAll")
	public RequestResult deleteAll(){
		return accrueTaxationService.deleteAll();
	}
	
	/**
	 * 计提税费<br>
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAccrued")
	public RequestResult saveAccrued(AccrueTaxationVo vo){
		RequestResult result = accrueTaxationService.saveAccruedTax(vo);
		if(result.isSuccess() && result.getData()!=null){
			String voucherId = result.getData().toString();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
			}
		}
		return result;
	}
	
	/**
	 * 支付税费<br>
	 */
	@ResponseBody
	@RequestMapping(value = "/savePayTax")
	public RequestResult savePayTax(AccrueTaxationVo vo){
		RequestResult result = accrueTaxationService.savePayTax(vo);
		if(result.isSuccess() && result.getData()!=null){
			String voucherId = result.getData().toString();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
			}
		}
		return result;
	}
	
}