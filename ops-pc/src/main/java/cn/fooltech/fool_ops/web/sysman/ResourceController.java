package cn.fooltech.fool_ops.web.sysman;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.sysman.entity.RoleAuth;
import cn.fooltech.fool_ops.domain.sysman.repository.RoleAuthRepository;
import cn.fooltech.fool_ops.domain.sysman.service.ResourceService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.domain.sysman.vo.MenuVo;
import cn.fooltech.fool_ops.domain.sysman.vo.ResourceVo;
import cn.fooltech.fool_ops.domain.sysman.vo.UserVo;
import cn.fooltech.fool_ops.utils.JsonUtil;

/**
 * <p>资源网页控制器类</p>
 * @author lzf
 * @version 1.0
 * 2015年5月20日
 */
@Controller
@RequestMapping(value = "/resourceController")
public class ResourceController {
	
	@Autowired
	private ResourceService resService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleAuthRepository roleAuthRepo;
	
	
	@RequestMapping(value="/resourceMessageUI")
	public String resourceMessageUI(){
		return "/sysmanWeb/resourceMessage";
	}
	
	/**
	 * 获取资源信息
	 * @param request
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getRes")
	public String getRes(HttpServletRequest request){
		String fid=request.getParameter("fid");
		ResourceVo result = resService.getById(fid, ThrowException.Throw);
		
		String parentName="";
		if(result.getParent()!=null){
			parentName = resService.getById(result.getParent()).getResName();
		}
		String resDesc=result.getResDesc();
		String resName=result.getResName();
		String resString=result.getResString();
		Short resType=result.getResType();
		Short permType=result.getPermType();
		String code=result.getCode();
		String smallIcoPath=result.getSmallIcoPath();
		Integer show=result.getShow();
		
		request.setAttribute("parentName", parentName);
		request.setAttribute("resDesc", resDesc);
		request.setAttribute("resName", resName);
		request.setAttribute("resString", resString);
		request.setAttribute("resType", resType);
		request.setAttribute("permType", permType);
		request.setAttribute("code", code);
		request.setAttribute("smallIcoPath", smallIcoPath);
		request.setAttribute("show", show);
		return "/sysmanWeb/onResourceList";
	}
	
	/**
	 * 新增资源页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/addResourceUI")
	public String addResourceUI(ResourceVo vo,HttpServletRequest request){
		String parent=vo.getParent();
		request.setAttribute("parent", parent);
		return "/sysmanWeb/addResource";
	}
	
	/**
	 * 修改资源页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/updateResourceUI")
	public String updateResourceUI(ResourceVo vo,HttpServletRequest request){
		ResourceVo entity = resService.getById(vo.getFid(), ThrowException.Throw);
		String fid=entity.getFid();
		String resName=entity.getResName();
		String resDesc=entity.getResDesc();
		String resString=entity.getResString();
		Short resType=entity.getResType();
		Short permType=entity.getPermType();
		String parent=entity.getParent();
		String code=entity.getCode();
		String smallIcoPath=entity.getSmallIcoPath();
		Integer rankOrder=null;
		if(entity.getRankOrder()!=null){
			rankOrder=entity.getRankOrder();
		}
		Integer show=entity.getShow();
		
		
		request.setAttribute("resName", resName);
		request.setAttribute("resDesc", resDesc);
		request.setAttribute("resString", resString);
		request.setAttribute("resType", resType);
		request.setAttribute("permType", permType);
		request.setAttribute("parent", parent);
		request.setAttribute("code", code);
		request.setAttribute("rankOrder", rankOrder);
		request.setAttribute("fid", fid);
		request.setAttribute("smallIcoPath", smallIcoPath);
		request.setAttribute("show",show);
		return "/sysmanWeb/addResource";
	}
	
	/**
	 * 新增资源信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/saveOrUpdate")
	@ResponseBody
	public RequestResult saveOrUpdate(ResourceVo vo){
		return resService.save(vo);
	}
	
	/**
	 * 获取企业的全部资源
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllTree")
	@ResponseBody
	public List<ResourceVo> getAllTree(){
		List<ResourceVo> resList = resService.buildResourceTree(false);
		return resList;
	}
	
	/**
	 * 删除资源信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public String delete(ResourceVo vo){
		resService.delete(vo.getFid());
		return "1";
	}
	
	/**
	 * 按功能授权页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/function")
	public String function(HttpServletRequest request){
		String fid=request.getParameter("fid");
		request.setAttribute("fid", fid);
		return "/sysmanWeb/function";
	}
	
	/**
	 * 按向角色授权页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/permissions")
	public String permissions(HttpServletRequest request){
		String fid=request.getParameter("fid");
		request.setAttribute("fid", fid);
		return "/sysmanWeb/permissions";
	}
	
	/**
	 * 按向用户授予角色页面
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/permissionsToUser")
	public String permissionsToUser(HttpServletRequest request){
		String fid=request.getParameter("fid");
		request.setAttribute("roleId", fid);
		return "/sysmanWeb/permissionsToUser";
	}
	
	/**
	 * 获得用户已授权资源信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getResUser")
	@ResponseBody
	public List<String> getResUser(String fid){
		List<ResourceVo> resources = resService.getResourcesByUserId(fid);
		List<String> ids=new ArrayList<String>();
		for(ResourceVo res:resources){
			String id=res.getFid();
			ids.add(id);
		}
		return ids;
	}
	
	/**
	 * 获得用户已授权角色信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getRoleUser")
	public @ResponseBody List<String> getRoleUser(String roleId){
		List<UserVo> users = userService.getUserByRole(roleId);
		List<String> ids=new ArrayList<String>();
		for(UserVo user:users){
			String id=user.getFid();
			ids.add(id);
		}
		return ids;
	}
	
	/**
	 * 获得角色已授权资源信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getResRole")
	public @ResponseBody List<String> getResRole(String fid){
		List<ResourceVo> resources = resService.getResourcesByRoleId(fid);
		List<String> ids = new ArrayList<String>();
		for(ResourceVo res:resources){
			String id=res.getFid();
			ids.add(id);
		}
		return ids;
	}
	
	/**
	 * 子资源列表页面
	 * @param vo
	 * @return 
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/onPermission")
	public String onPermission(String resId,String roleId,HttpServletRequest request){
		request.setAttribute("resId",resId);
		request.setAttribute("roleId",roleId);
		return "/sysmanWeb/onPermissions";
	}
	
	/**
	 * 用户列表
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/onPermissionsToUser")
	public String onPermissionsToUser(HttpServletRequest request){
		String orgId=request.getParameter("orgId");
		String roleId=request.getParameter("roleId");
		request.setAttribute("orgId", orgId);
		request.setAttribute("roleId", roleId);
		return "/sysmanWeb/onPermissionsToUser";
	}
	
	/**
	 * 获得子资源
	 * @param vo
	 * @return 
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getResChilds")
	@ResponseBody
	public List<ResourceVo> getResChilds(@RequestParam String fid, @RequestParam String roleId){
		List<ResourceVo> childs = resService.getResChilds(fid);
		List<String> rids = Lists.newArrayList();
		for(ResourceVo vo:childs){
			rids.add(vo.getFid());
		}
		List<RoleAuth> roleAuths = roleAuthRepo.findByRoleIdAndResourceIds(roleId, rids);
		
		for(RoleAuth ra:roleAuths){
			for(ResourceVo vo:childs){
				if(vo.getFid().equals(ra.getResource().getFid())){
					vo.setDateFilter(ra.getDataFilter());
					break;
				}
			}
		}
		
		return childs;
	}

}
