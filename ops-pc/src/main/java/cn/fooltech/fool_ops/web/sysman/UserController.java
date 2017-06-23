package cn.fooltech.fool_ops.web.sysman;

import cn.fooltech.fool_ops.component.core.ImageScale;
import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.AttachVo;
import cn.fooltech.fool_ops.domain.sysman.service.RoleService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.domain.sysman.vo.UserVo;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>用戶网页控制器类</p>
 * @author lzf
 * @version 2.0
 * 2015年5月13日
 */
@Controller
@RequestMapping(value = "/userController")
public class UserController {
	
	public final static List<String> SupportImageType = new ArrayList<String>(Arrays.asList("jpeg","jpg","png"));
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AttachService attachService;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 文件服务器
	 */
	@Autowired
	private FileSystem fileSystem;
	
	/**
	 * 人员弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/sysmanWeb/userWindow";
	}
	
	
	/**
	 * 用户个人信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/userInfoUI")
	public String userInfoUI(HttpServletRequest request){
		UserVo entity = userService.getUser();
	    request.setAttribute("wrong", 0);
	    request.setAttribute("rs", entity);
		return "/basedata/userMessage";
	}
	
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody 
	public RequestResult save(UserVo vo,String rePsw,HttpServletRequest request){
		return userService.save(vo);
	}
	
	/**
	 * 用户信息管理页面
	 * @return
	 */
	@RequestMapping(value = "/userMessageUI")
	public String userMessageUI(){
		return "/sysmanWeb/userMessage";
	}
	
	/**
	 * 帮助文档页面
	 * @return
	 */
	@RequestMapping(value = "/helpDoc")
	public String helpDoc(){
		return "/sysmanWeb/helpDoc";
	}
	
	/**
	 * 根据机构ID返回用户列表(json)
	 * @param orgId
	 * @return
	 */
	/*@RequestMapping(value = "usersTreeByDep")
	public @ResponseBody List<CommonTreeVo> usersByDep(String orgId){
		return userService.queryTreeByDept(orgId);
	}*/
	
	/**
	 * 用户信息管理-用户列表<br>
	 * 当orgId为空时，查询整个机构的用户，不为空时，查询某个部门的用户<br>
	 * @param orgId 部门ID
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value = "/userList")
	@ResponseBody
	public PageJson userList(String orgId, @RequestParam(defaultValue="1", required=false) Integer subDept, 
			PageParamater paramater){
		Page<UserVo> users = userService.queryByDept(orgId, paramater, subDept);
		return new PageJson(users);
	}
	
	/**
	 * 用户列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/onUserList")
	public String onUserList(HttpServletRequest request){
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId", orgId);
		return "/sysmanWeb/onUserList";
	}
	
	/**
	 * 用户信息管理-添加、编辑用户页面
	 * @param orgId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/addUserUI")
	public String addUserMessage(String orgId, String userId, Model model){
		if(StringUtils.isNotBlank(userId)){
			UserVo user = userService.getById(userId);
			model.addAttribute("obj", user);
		}
		model.addAttribute("orgId", orgId);
		return "sysmanWeb/addUser";
	}
	
	/**
	 * 用户信息管理-添加保存用户
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/saveOrUpdate")
	@ResponseBody
	public RequestResult saveOrUpdate(UserVo vo){
		if(Strings.isNullOrEmpty(vo.getFid())){
			return userService.create(vo);
		}else{
			return userService.save(vo);
		}
	}
	
	/**
	 * 用户信息管理-刪除用户
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public RequestResult delete(UserVo vo){
		return userService.delete(vo);
	}
	
	/**
	 * 按功能授权-用户列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/onAuthorizeByFunction")
	public String onAuthorizeByFunction(HttpServletRequest request){
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId", orgId);
		return "/sysmanWeb/onAuthorizeByFunction";
	}
	
	
	
	/**
	 * 按功能授权
	 */
	@RequestMapping(value="/functionUser")
	@ResponseBody
	public RequestResult functionUser(String check,String parent,String userID){		
		return userService.functionUser(check, parent, userID);
	}
	
	/**
	 * 把资源授权给角色
	 */
	@RequestMapping(value="/functionRole")
	@ResponseBody
	public RequestResult functionRole(String check,String resId,String roleId,@RequestParam(defaultValue="0",required=false) Integer subRes){
		return userService.saveFunctionRole(check, resId, roleId, subRes);
	}
	
	/**
	 * 根据ID获取用户信息
	 * @param fid
	 * @return
	 */
	@RequestMapping(value="/getById")
	@ResponseBody
	public UserVo getById(String fid){
		return userService.getById(fid, ThrowException.Throw);
	}
	
	/**
	 * 获得用户已授权角色信息
	 */
	@RequestMapping(value="/getUserRole")
	@ResponseBody
	public List<String> getUserRole(@RequestParam String userId){
		return userService.getUserRole(userId);
	}
	
	/**
	 * 角色授权给用户
	 * @param roleId
	 * @param check
	 * @param unCheck
	 */
	@RequestMapping(value = "/roleToUser")
	@ResponseBody
	public RequestResult roleToUser(String roleId,String check,String unCheck){
		String addUserFids=check.replaceAll(",", ";");
		String delUserFids=unCheck.replaceAll(",", ";");
		return roleService.savaRoleUser(addUserFids, delUserFids, roleId);
	}
	
	/**
	 * 模糊查询(根据编号、名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<UserVo> vagueSearch(UserVo vo){
		return userService.vagueSearch(vo);
	}
	
	
	/**
	 * 上传头像
	 * @param userId 用户ID
	 * @author lzf
	 * @return 
	 */
	@RequestMapping(value = "/Upload")
	@ResponseBody
	public String Upload(String userId, @RequestParam("file") MultipartFile file) throws Exception{
		String fileName=file.getOriginalFilename();
		String fileSuffix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		
		if(!SupportImageType.contains(fileSuffix)){
			System.err.println("上传文件类型错误");
			return getErrorTag();
		}
		Attach entity = userService.uploadAttach(userId, file);
		
		String path=entity.getFilePath();
		String headPortrait = path.replaceAll("\\\\", "/");
		userService.updateHeadPortrait(userId, headPortrait);
		
		String left = path.substring(0, path.indexOf('.'));
		String right = path.substring(path.lastIndexOf('.'));
		String filePath = fileSystem.getRoot()+left+"_64"+right;
		ImageScale scale = new ImageScale();
		scale.scale(fileSystem.getRoot()+path, filePath, 64, false, 64);
		//attachService.processImage(path,path,"70X70",ImageScale.SIZE_70X70,0);
		return getSuccessTag();
	}
	
	/**
	 * 获取头像路径
	 * @param userId 用户ID
	 * @author lzf
	 * @return 
	 */
	@RequestMapping(value = "/getUpload")
	@ResponseBody
	@Deprecated
	public String getUpload(String userId) throws Exception{
		AttachVo vo = userService.getUploadAttach(userId);
		String path="";
		if(vo!=null){
			path=vo.getFilePath();
		}
		return path;
	}
	
	/**
	 * 获取头像，直接返回头像文件流
	 * @param userId
	 */
	@RequestMapping(value = "/getHeadPortrait/{userId}.jpg")
	public void getHeadPortrait(@PathVariable String userId, HttpServletResponse reponse){
		AttachVo vo = userService.getUploadAttach(userId);
		String path="";
		if(vo!=null){
			path=vo.getFilePath();
		}
		String filePath = fileSystem.getRoot()+path;
		
		reponse.setHeader("Content-Type","image/jpeg");
		
		InputStream fis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream(filePath);
			os = reponse.getOutputStream();
			byte buff[] = new byte[1024];
			while(fis.read(buff)!=-1){
				os.write(buff);
			}
			os.flush();
			
		} catch (FileNotFoundException e) {
			try {
				String defaultHeadpath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"head.jpg";
				fis = new FileInputStream(defaultHeadpath);
				os = reponse.getOutputStream();
				byte buff[] = new byte[1024];
				while(fis.read(buff)!=-1){
					os.write(buff);
				}
				os.flush();
			} catch (FileNotFoundException e1) {
				//e1.printStackTrace();
			}catch (IOException e1) {
				//e1.printStackTrace();
			} 
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			try {
				if(fis!=null)fis.close();
				if(os!=null)os.close();
			} catch (IOException e) {
			}
		}
		
	}
	
	/**
	 * 获取请求处理成功的返回标识
	 * @return
	 */
	private String getSuccessTag(){
		return "1";
	}
	
	/**
	 * 获取请求处理失败的返回标识
	 * @return
	 */
	private String getErrorTag(){
		return "0";
	}
	
}
