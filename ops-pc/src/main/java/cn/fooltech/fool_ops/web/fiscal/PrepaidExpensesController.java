package cn.fooltech.fool_ops.web.fiscal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpenses;
import cn.fooltech.fool_ops.domain.fiscal.service.PrepaidExpensesDetailService;
import cn.fooltech.fool_ops.domain.fiscal.service.PrepaidExpensesService;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesDetailVo;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesVo;
import cn.fooltech.fool_ops.utils.StrUtil;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>待摊费用网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-14 09:16:27
 */
@Controller
@RequestMapping(value = "/prepaidExpenses")
public class PrepaidExpensesController extends BaseController{
	
	/**
	 * 待摊费用网页服务类
	 */
	@Autowired
	private PrepaidExpensesService prepaidExpensesWebService;
	
	/**
	 * 待摊费用明细网页服务类
	 */
	@Autowired
	private PrepaidExpensesDetailService detailWebService;
	
	/**
	 * 去待摊费用列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listPrepaidExpenses(ModelMap model){
		return "/fiscal/prepaidExpenses/manage";
	}
	
	/**
	 * 去待摊费用列表信息页面<br>
	 */
	@RequestMapping("/edit")
	public String edit(String fid,ModelMap model){
		if(StrUtil.notEmpty(fid)){
			PrepaidExpensesVo vo= prepaidExpensesWebService.getByFid(fid);
			model.put("vo", vo);
			model.put("subjectEdit", prepaidExpensesWebService.isEnableEdit(fid));
		}
		return "/fiscal/prepaidExpenses/edit";
	}
	
	/**
	 * 查找待摊费用列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PrepaidExpensesVo prepaidExpensesVo,PageParamater pageParamater){
		Page<PrepaidExpenses> page = prepaidExpensesWebService.query(prepaidExpensesVo,pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(prepaidExpensesWebService.getVos(page.getContent()));
		return pageJson;
	}
	
	/**
	 * 新增/编辑待摊费用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(PrepaidExpensesVo vo){
		return prepaidExpensesWebService.save(vo);
	}
	
	/**
	 * 删除待摊费用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String fid){
		return prepaidExpensesWebService.delete(fid);
	}
	
	/**
	 * 通过审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/passAudit")
	public RequestResult passAudit(@RequestParam String fid){
		return prepaidExpensesWebService.savePassAudit(fid);
	}
	
	/**
	 * 通过审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancleAudit")
	public RequestResult cancleAudit(@RequestParam String fid){
		return prepaidExpensesWebService.saveCancleAudit(fid);
	}
	
	/**
	 * 根据fid查找明细记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDetails")
	public List<PrepaidExpensesDetailVo> queryDetails(String fid){
		return detailWebService.queryByExpensesId(fid);
	}
}