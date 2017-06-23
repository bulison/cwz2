package cn.fooltech.fool_ops.web.cashier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cashier.entity.BankInit;
import cn.fooltech.fool_ops.domain.cashier.service.BankInitService;
import cn.fooltech.fool_ops.domain.cashier.vo.BankInitVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * 银行初始化Controller
 * @author lgk
 * @date 2015年12月14日下午2:56:59
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/cashierBankInitController")
public class BankInitController extends BaseController{
	@Autowired
	private BankInitService bankInitWebService;
	
	
	/**
	 * 银行初始化页面
	 * @return
	 */
	@RequestMapping(value="/goListBankInit")
	public String goListBankInit(ModelMap model){
		/**
		 * 出纳系统已启用或财务已结账lock=false；不能再修改或删除
		 */
		model.put("lock", bankInitWebService.enableEditOrDelete());
		
		return "/cashier/initBank/initBank";
	}
	/**
	 * 银行初始化列表
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/listBankInit")
	@ResponseBody
	public PageJson queryBankInit(BankInitVo bankInitVo,PageParamater paramater){
		Page<BankInit> page = bankInitWebService.query(bankInitVo, paramater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(bankInitWebService.getVos(page.getContent()));
		return pageJson;
 	}
	
	/**
	 * 引入
	 * @return
	 */
	@RequestMapping(value="/importBank")
	@ResponseBody
	public RequestResult importBank(){
	  return bankInitWebService.saveByImport();
	}
	/**
	 * 启动
	 * @param fid
	 * @return
	 */
	@RequestMapping(value="/updateUse")
	@ResponseBody
	public RequestResult startStatus(){
		return bankInitWebService.updateUse();
	}
	
	
	/**
	 * 反启动
	 * @param fid
	 * @return
	 */
	@RequestMapping(value="/updateUnUse")
	@ResponseBody
	public RequestResult unStartStatus(){
		return bankInitWebService.updateUnUse();
	}
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(String fid){
		return bankInitWebService.delete(fid);
	}
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult update(BankInitVo vo){
		return bankInitWebService.save(vo);
	}
	

	@RequestMapping(value = "/test")
	@ResponseBody
	public RequestResult test(){
		return bankInitWebService.saveByImport();
	}
}
