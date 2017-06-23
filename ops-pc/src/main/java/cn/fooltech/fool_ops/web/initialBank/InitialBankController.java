package cn.fooltech.fool_ops.web.initialBank;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.initialBank.entity.InitialBank;
import cn.fooltech.fool_ops.domain.initialBank.service.InitialBankService;
import cn.fooltech.fool_ops.domain.initialBank.vo.InitialBankVo;
import cn.fooltech.fool_ops.domain.initialPay.service.InitialPayService;
import cn.fooltech.fool_ops.domain.initialReceivable.service.InitialReceivableService;
import cn.fooltech.fool_ops.web.base.BaseController;



/**
 * <p>现金银行初期管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月8日
 */
@Controller
@RequestMapping(value = "/initialBankController")
public class InitialBankController extends BaseController{
	@Autowired
	InitialBankService initialBankService;
	@Autowired
	InitialPayService initialPayService;
	@Autowired
	InitialReceivableService initialReceivableWebService;
	/**
	 * 现金银行期初在会计期间已启动可以进行增删改，否则不允许操作。
	 * @param model
	 * @return 标识enableEditOrDelete，true：可操作；false：不可操作
	 */
	@RequestMapping(value="/initialBankManager")
	public String initialBankManager(ModelMap model){
//		//是否允许修改删除：true：允许；false：不允许
		model.put("enableEditOrDelete", initialPayService.enableEditOrDelete());
		Integer state = initialReceivableWebService.getTheFristPeriod();
		model.put("state", state);
		return "/initial/initialBank";
	}
	
	@RequestMapping(value="/addInitialBank")
	public String addInitialBank(){
		return "/initial/addInitialBank";
	}
	
	@RequestMapping(value="/editInitialBank")
	public String editInitialBank(InitialBankVo vo,HttpServletRequest request){
		InitialBankVo entity=initialBankService.getByFid(vo.getFid());
		request.setAttribute("entity", entity);
		return "/initial/addInitialBank"; 
	}
	
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(InitialBankVo vo,PageParamater pageParamater){
		Page<InitialBankVo> page=initialBankService.query(vo, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(InitialBankVo vo){
		return initialBankService.save(vo);
		
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(String fid){
//		int result=initialBankWebService.delete(fid).getResult();
		return initialBankService.delete(fid);
	}

}
