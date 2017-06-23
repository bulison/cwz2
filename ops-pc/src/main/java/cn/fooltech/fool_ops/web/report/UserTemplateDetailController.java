package cn.fooltech.fool_ops.web.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.domain.report.service.UserTemplateDetailService;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateDetailVo;


@RequestMapping("/report/UserTemplateDetail")
@Controller
public class UserTemplateDetailController {
	
	@Autowired
	UserTemplateDetailService utdService;
	
	@RequestMapping("/getByUserTemplateId")
	@ResponseBody
	public List<UserTemplateDetailVo> getByUserTemplateId(String templateId)throws Exception{
		List<UserTemplateDetailVo> vos = utdService.getByUserTemplateId(templateId);
		return vos;
	}
}
