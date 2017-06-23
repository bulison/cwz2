package cn.fooltech.fool_ops.web.index;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.sysman.service.MenuService;
import cn.fooltech.fool_ops.domain.sysman.vo.MenuVo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;

@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = "/main/indexPage")
	public String indexPage(ModelMap model){
		return "indexPage/indexpageNew";
	}
	
	/**
	 * 根路径
	 * @return
	 */
	@RequestMapping("/")
	public String root(){



		return "redirect:/main/index";
	}
	
	/**
	 * 用户引导页面
	 * @return
	 */
	@RequestMapping("/main/guidance")
	public String guidance(){
		return "/indexPage/guidance";
	}
	
	@RequestMapping(value={"/main/home","/main/index"})
	public String index(HttpSession session, ModelMap model) {
		
		List<MenuVo> menus = menuService.buildMenuData(true);
		
		if(menus.size()>0){
			model.put("menuDatas", JsonUtil.toJsonString(menus));
			
			SecurityUser user = SecurityUtil.getSecurityUser();
			
			if(user!=null){
				model.put("isAdmin", user.isAdmin());
				model.put("username", user.getUsername());
				model.put("usercode", user.getUserCode());
				model.put("userId", user.getId());
				model.put("sex", user.getSex());
				model.put("orgName", user.getOrgName());
			}
			
		}
		
		initFiscalAccount(session);
		
		return "main/index";
	}
	
	/**
	 * 初始化账套信息到session
	 * @param session
	 */
	private void initFiscalAccount(HttpSession session){
		SecurityUser user = SecurityUtil.getSecurityUser();
		session.setAttribute(Constants.DEFAULT_FISCAL_ACCOUNT_NAME, user.getFiscalAccountName());
	}
	
	/**
	 * 搜索资源
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/main/queryMenu")
	@ResponseBody
	public List<MenuVo> queryMenu(String name){
		List<MenuVo> menus = menuService.buildMenuData(true);
		menus = menuService.filterRes(menus, name);
		return menus;
	}

	/**
	 * 仓库解锁权限验证页面
	 * @return
	 */
	@RequestMapping(value = "/indexController/userCheck")
	public String userCheck() {
		return "/warehourse/cgth/userCheck";
	}
}
