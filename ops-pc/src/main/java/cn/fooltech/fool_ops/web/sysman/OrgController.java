package cn.fooltech.fool_ops.web.sysman;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.vo.OrganizationVo;


/**
 * <p>组织网页控制器类</p>
 */
@Controller
@RequestMapping(value = "/orgController")
public class OrgController {
	
	@Autowired
	private OrgService orgService;
	
	/**
	 * 获取企业的全部部门
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllTree")
	@ResponseBody
	public List<OrganizationVo> getAllTree(){
		return orgService.getTreeData();
	}
	
	/**
	 * 获取所有机构
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/getAll")
	@ResponseBody
	public List<OrganizationVo> getAllOrg(OrganizationVo vo){
	  return orgService.getAllOrg(vo);
	}
	
	 /**
	  * 部门信息管理页面
	  * @param request
 	  * @return
	  */
	 @RequestMapping(value = "/deptMessage")
	 public String deptMessage(HttpServletRequest request){
		 request.setAttribute("orgId", orgService.getOrg());
			return "/sysmanWeb/deptMessage";
	 }
	
	/**
	 * 获取组织信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/getOrg")
	public String getOrg(OrganizationVo vo,HttpServletRequest request){
		OrganizationVo result = orgService.getOrg();
		
		String email=result.getEmail();
		String address=result.getFaddress();
		String fax=result.getFax();
		String homePage=result.getHomePage();
		String orgDesc=result.getOrgDesc();
		String orgName=result.getOrgName();
		String phoneOne=result.getPhoneOne();
		String postCode=result.getPostCode();
		request.setAttribute("email", email);
		request.setAttribute("address", address);
		request.setAttribute("fax", fax);
		request.setAttribute("homePage", homePage);
		request.setAttribute("orgDesc", orgDesc);
		request.setAttribute("orgName", orgName);
		request.setAttribute("phoneOne", phoneOne);
		request.setAttribute("postCode", postCode);
		return "/sysmanWeb/orgMessage";
	}
	
	/**
	 * 修改企业信息
	 * @param vo
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/save")
	public String save(OrganizationVo vo){
		orgService.save(vo);
		return "redirect:/orgController/getOrg";
	}
	
	/**
	 * 获取部门信息
	 * @param request
	 * @return
	 */
	 @RequestMapping(value = "/List")
	 public String deptList(String id, HttpServletRequest request){
		 OrganizationVo entity = orgService.getById(id, ThrowException.Throw);
		 String orgName=entity.getOrgName();
		 String phoneOne=entity.getPhoneOne();
		 String orgCode=entity.getOrgCode();
		 request.setAttribute("orgName", orgName);
		 request.setAttribute("phoneOne", phoneOne);
		 request.setAttribute("orgCode", orgCode);
		 return "/sysmanWeb/onDeptList";
	 }
	 
	 /**
	 * 新增部门信息页面
	 * @param request
	 * @return
	 */
	 @RequestMapping(value = "/addDeptUI")
	 public String addDeptUI(HttpServletRequest request){
		 String parent=(String)request.getParameter("id");
		 request.setAttribute("parent", parent);
		 return "/sysmanWeb/addDept";
	 }
	 
	 /**
	  * 新增部门信息
	  * @param request
  	  * @return
	  */
	 @RequestMapping(value = "/addDept")
	 @ResponseBody
	 public RequestResult addDept(OrganizationVo vo){
		 return orgService.saveDept(vo);
	 }
	 
	 
	 /**
	  * 刪除部门信息
	  * @param request
	 * @return 
  	  * @return
	  */
	 @RequestMapping(value = "/deleteDept")
	 @ResponseBody
	 public RequestResult deleteDept(@RequestParam String fid){
		 return orgService.deleteDept(fid);
	}
	 
	 /**
	  * 修改部门信息页面
	  * @param request
  	  * @return
	  */
	 @RequestMapping(value = "/updateDeptUI")
	 public String updateDeptUI(String id, ModelMap model){
		 OrganizationVo entity = orgService.getById(id, ThrowException.Throw);
		 String orgName=entity.getOrgName();
		 String phone=entity.getPhoneOne();
		 String orgCode=entity.getOrgCode();
		 model.put("fid", id);
		 model.put("telPhone", phone);
		 model.put("orgName", orgName);
		 model.put("orgCode", orgCode);

		 String parent = entity.getParent() == null ? null : entity.getParent();
		 model.put("parent", parent);
		 
		 return "/sysmanWeb/addDept";
	 }
	 
	 /**
	  * 获取部门信息
	  * @param request
  	  * @return
	  */
	 @RequestMapping(value = "/getEntity")
	 @ResponseBody
	 public OrganizationVo getEntity(String fid){
		 return orgService.getById(fid, ThrowException.Throw);
	 }
}


