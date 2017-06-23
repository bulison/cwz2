package cn.fooltech.fool_ops.web.sysman;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.sysman.service.RoleService;
import cn.fooltech.fool_ops.domain.sysman.vo.RoleVo;


/**
 * <p>角色网页控制器类</p>
 * @author lzf
 * @version 2.0
 * 2015年5月13日
 */
@Controller
@RequestMapping(value = "/roleController")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRepository userRepo; 
	
	/**
	 * 角色信息维护页面
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/roleMessageUI")
	public String roleMessageUI(){
		return "/sysmanWeb/roleMessage";
	}
	
	/**
	 * 角色信息维护-角色列表界面
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/onRoleList")
	public String onRoleList(HttpServletRequest request){
		return "/sysmanWeb/onRoleList";
	}
	
	/**
	 * 角色信息维护-角色列表(json)
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/roleList")
	public @ResponseBody PageJson roleList( @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = "10") int rows){
		return new PageJson(roleService.query(page, rows));
	}
	
	/**
	 * 角色信息维护-新增、编辑角色页面
	 * @param fid 角色ID
	 * @return
	 */
	@RequestMapping(value = "/addRoleUI")
	public String addRoleUI(String fid, Model model){
		if(StringUtils.isNotBlank(fid)){
			RoleVo role = roleService.getById(fid, ThrowException.Throw);
			model.addAttribute("obj", role);
		}
		return "/sysmanWeb/addRole";
	}
	
	/**
	 * 角色信息维护-保存角色信息
	 * @param request
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public RequestResult saveOrUpdate(RoleVo vo){
		roleService.saveOrUpdate(vo);
		return new RequestResult();
	}
	
	/**
	 * 角色信息维护-删除角色
	 */
	@RequestMapping(value="/delete")
	public String delete(@RequestParam String fid){
		roleService.delete(fid);
		return "redirect:/roleController/roleMessageUI";
	}
	
	/**
	 * 角色权限维护-角色列表界面
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/onRoleRights")
	public String onRoleRights(HttpServletRequest request){
		return "/sysmanWeb/onRoleRights";
	}
	
	/**
	 * 按角色授权-用户列表页面
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/onAuthorizeByRole")
	public String onAuthorizeByRole(HttpServletRequest request){
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId", orgId);
		return "/sysmanWeb/onAuthorizeByRole";
	}
	
	/**
	 * 按角色授权页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/authorize")
	public String authorize(HttpServletRequest request){
		String userId=request.getParameter("userId");
		request.setAttribute("userId", userId);
		return "/sysmanWeb/authorize";
	}
	
	/**
	 * 按角色授权
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/roleToUser")
	public String roleToUser(String check,String userId){
		User entity = userRepo.findOne(userId);
		String[] checkIds=null;
		
		if(check!=null&&check!=""){
			checkIds=check.split(",");
		}
		
		List<Role> role=entity.getRoles();
		role.removeAll(role);
		if(checkIds!=null){
			for(String id:checkIds){
				Role roleEntity=roleService.findOne(id);
				role.add(roleEntity);
			}
			entity.setRoles(role);
		}else{
			entity.setRoles(role);
			}
		
		userRepo.save(entity);
		return "redirect:/sysmanPageController/authorizeByRole";
	}

}
