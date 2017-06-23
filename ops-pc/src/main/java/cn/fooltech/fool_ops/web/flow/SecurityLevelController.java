package cn.fooltech.fool_ops.web.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.service.SecurityLevelService;
import cn.fooltech.fool_ops.domain.flow.vo.SecurityLevelVo;

/**
 * <p>保密级别网页控制器类</p>
 * @author CYX 
 * @version 1.0
 * @date 2016年11月11
 */
@Controller
@RequestMapping(value = "/flow/security")
public class SecurityLevelController {
	
	@Autowired
	private SecurityLevelService securityLevelService;
	
	/**
	 * 管理页面
	 */
	@RequestMapping("/manage")
	public String manage() {
		return "/flow/security/manage";
	}
	
	/**
	 * 查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(SecurityLevelVo vo, PageParamater paramater) {
		Page<SecurityLevelVo> vos = securityLevelService.getPageSecurityLevel(vo, paramater);
		return new PageJson(vos);
	}
	
	/**
	 * 查询保密级别,过滤已停用的
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<SecurityLevelVo> queryAll(SecurityLevelVo vo) {
		return securityLevelService.getListSecurityLevel(vo);
	}
	
	/**
	 * 根据ID查询<br>
	 */
	@ResponseBody
	@RequestMapping("/queryById")
	public SecurityLevelVo queryById(String fid) {
		SecurityLevelVo vo = securityLevelService.getById(fid);
		return vo;
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(SecurityLevelVo vo) {
		return securityLevelService.save(vo);
	}

	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return securityLevelService.delete(fid);
	}
}
