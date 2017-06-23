package cn.fooltech.fool_ops.web.sysman;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>权限设置网页控制器</p>
 * @author lzf
 * @version 1.0
 * @date 2015年5月12日
 */
@Controller
@RequestMapping(value="/sysmanPageController")
public class SysmanPageController {
	
	@RequestMapping(value="/test")
	public String test(){
		return "test/test";
	}
	
	//单位信息维护
	@RequestMapping(value="/orgMessage")
	public String orgMessage(){
		return "sysmanWeb/orgMessage";
	}
	
	//部门信息维护
	@RequestMapping(value="/deptMessage")
	public String deptMessage(){
		return "sysmanWeb/deptMessage";
	}
	
	//用户信息维护
	@RequestMapping(value="/userMessage")
	public String userMessage(){
		return "sysmanWeb/userMessage";
	}
	
	//角色信息维护
	@RequestMapping(value="/roleMessage")
	public String roleMessage(){
		return "sysmanWeb/roleMessage";
	}
	
	//资源信息维护
	@RequestMapping(value="/resourceMessage")
	public String resourceMessage(){
		return "sysmanWeb/resourceMessage";
	}
	
	//角色权限维护
	@RequestMapping(value="/roleRights")
	public String roleRights(){
		return "sysmanWeb/roleRights";
	}
	
	@RequestMapping(value="/permissions")
	public String permissions(){
		return "sysmanWeb/permissions";
	}
	
	//按角色授权
	@RequestMapping(value="/authorizeByRole")
	public String authorizeByRole(){		
		return "sysmanWeb/authorizeByRole";
	}
	
	@RequestMapping(value="/authorize")
	public String authorize(){
		return "sysmanWeb/authorize";
	}
	
	//按功能授权
	@RequestMapping(value="/authorizeByFunction")
	public String authorizeByFunction(){
		return "sysmanWeb/authorizeByFunction";
	}
	
	@RequestMapping(value="/function")
	public String function(){
		return "sysmanWeb/function";
	}
	
	@RequestMapping(value="/authorizeByFunctionList")
	public String authorizeByFunctionList(){
		return "sysmanWeb/onAuthorizeByFunction";
	}
	
	@RequestMapping(value="/authorizeList")
	public String authorizeList(){
		return "sysmanWeb/onAuthorize";
	}
	
	@RequestMapping(value="/authorizeByRoleList")
	public String authorizeByRoleList(){
		return "sysmanWeb/onAuthorizeByRole";
	}
	
	@RequestMapping(value="/permissionsList")
	public String permissionsList(){
		return "sysmanWeb/onPermissions";
	}
	
	@RequestMapping(value="/roleRightsList")
	public String roleRightsList(){
		return "sysmanWeb/onRoleRights";
	}
	
	@RequestMapping(value="/resourceMessageList")
	public String resourceMessageList(){
		return "sysmanWeb/onResourceList";
	}
	
	@RequestMapping(value="/addResourceMessage")
	public String addResourceMessage(){
		return "sysmanWeb/addResource";
	}
	
	@RequestMapping(value="/roleMessageList")
	public String roleMessageList(){
		return "sysmanWeb/onRoleList";
	}
	
	@RequestMapping(value="/addRoleMessage")
	public String addRoleMessage(){
		return "sysmanWeb/addRole";
	}
	
	@RequestMapping(value="/userMessageList")
	public String userMessageList(){
		return "sysmanWeb/onUserList";
	}
	
	@RequestMapping(value="/addUserMessage")
	public String addUserMessage(){
		return "sysmanWeb/addUser";
	}
	
	@RequestMapping(value="/deptMessageList")
	public String orgMessageList(){
		return "sysmanWeb/onDeptList";
	}
	
	@RequestMapping(value="/addDeptMessage")
	public String addDeptMessage(){
		return "sysmanWeb/addDept";
	}

}
