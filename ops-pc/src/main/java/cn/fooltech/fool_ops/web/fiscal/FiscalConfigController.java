package cn.fooltech.fool_ops.web.fiscal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalConfigService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalConfigVo;


/**
 * 财务参数设置
 * @author lgk
 * @date 2015年11月18日下午3:40:39
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/fiscalConfig")
public class FiscalConfigController {
	
	@Autowired
	private FiscalConfigService configService;
	
	/**
	 * 财务参数设置管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/fiscal/fiscalConfig/manage";
	}
	
	/**
	 * 修改 财务参数设置管理界面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(Model model,String fid){
		if(StringUtils.isNotBlank(fid)){
			FiscalConfigVo obj = configService.getById(fid);
			model.addAttribute("obj", obj);
		}
		return "/fiscal/fiscalConfig/edit";
	}
	
	
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(FiscalConfigVo vo, PageParamater paramater){
		return new PageJson(configService.query(vo, paramater));
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public RequestResult save(FiscalConfigVo vo){
		return configService.update(vo);
	}
}
