package cn.fooltech.fool_ops.web.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.report.service.UserTemplateService;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateVo;

@Controller
@RequestMapping("/report/UserTemplate/")
public class UserTemplateController {

	@Autowired
	private UserTemplateService utService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public RequestResult save(UserTemplateVo vo)throws Exception{
		return utService.save(vo);
	}
	
	/**
	 * 查找用户查询条件模板列表
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getUserTemplate")
	public List<UserTemplateVo> getUserTemplate(UserTemplateVo vo)throws Exception{
		return utService.list(vo);
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(UserTemplateVo vo, PageParamater paramater)throws Exception{
		Page<UserTemplateVo> page = utService.query(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 选择模板
	 * @return
	 * @author lzf
	 */
	@RequestMapping("/selectTemp")
	public String selectTemp() {
		return "/report/template/selectTemplate";
	}
	
	/**
	 * 保存模板
	 * @author lzf 
	 */
	@RequestMapping("/saveTemp")
	public String saveTemp() {
		return "/report/template/addTemplate";
	}
	
}
