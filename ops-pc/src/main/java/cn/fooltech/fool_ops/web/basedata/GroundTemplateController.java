package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 控制器
 * @author xjh
 * @since 1.0
 */
@Controller
@RequestMapping("/groundTemplate")
public class GroundTemplateController  extends BaseController {

	private Logger logger = Logger.getLogger(GroundTemplateController.class);
	
	@Autowired
	private GroundTemplateService groundTemplateService;

	/**
	 * 场地费报价列表页
	 * @return
	 */
	@RequestMapping("manage")
	public String manage() {
		return "/basedata/groundTemplate/manage";
	}

	/**
	 * 场地费报价列表页
	 * @return
	 */
	@RequestMapping("detailManage")
	public String detailManage() {
		return "/basedata/groundTemplate/detailManage";
	}

	/**
	 * 新增/编辑地费报价
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {

		if(!Strings.isNullOrEmpty(id)){
			GroundTemplateVo vo = groundTemplateService.getById(id);
			model.put("vo", vo);
		}

		return "/basedata/groundTemplate/edit";
	}
}
