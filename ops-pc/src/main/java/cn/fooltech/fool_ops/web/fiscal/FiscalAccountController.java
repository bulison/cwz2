package cn.fooltech.fool_ops.web.fiscal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>财务账套控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年11月17日
 */
@Controller
@RequestMapping(value = "/fiscalAccount")
public class FiscalAccountController extends BaseController{
	@Autowired
	private FiscalAccountService accService;
	
	/**
	 * 财务账套管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/fiscal/account/manage";
	}
	
	/**
	 * 新增账套页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "/fiscal/account/edit";
	}
	/**
	 * 新增账套页面
	 * @return
	 */
	@RequestMapping(value = "/windows")
	public String windows(){
		return "/fiscal/account/changeAccount";
	}
	/**
	 * 修改账套页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(FiscalAccountVo vo,HttpServletRequest request){
		FiscalAccountVo obj = accService.getById(vo.getFid());
		request.setAttribute("obj", obj);
		return "/fiscal/account/edit";
	}
	
	/**
	 * 保存账套
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(FiscalAccountVo vo){
		return accService.save(vo);
	}
	
	/**
	 * 删除账套
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(FiscalAccountVo vo){
		return accService.delete(vo.getFid());
	}
	
	/**
	 * 账套列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(FiscalAccountVo vo,PageParamater pageParamater){
		Page<FiscalAccountVo> page = accService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 获取当前机构账套列表
	 * @return
	 */
	@RequestMapping(value = "/queryByOrg")
	@ResponseBody
	public List<FiscalAccountVo> queryByOrg(){
		return accService.queryByOrg();
	}
	
	/**
	 * 设为默认账套
	 * @return
	 */
	@RequestMapping(value = "/setDefaultLogin")
	@ResponseBody
	public RequestResult setDefaultLogin(FiscalAccountVo vo){
		return accService.saveDefaultLogin(vo.getFid());
	}

	/**
	 * 切换账套
	 * @return
	 */
	@RequestMapping(value = "/changeAccount")
	@ResponseBody
	public RequestResult changeAccount(String accountId, HttpSession session){
		return accService.changeFiscalAccount(accountId, session);
	}
	
}
