package cn.fooltech.fool_ops.web.rate;

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
import cn.fooltech.fool_ops.domain.rate.entity.RateProgramme;
import cn.fooltech.fool_ops.domain.rate.service.RateProgrammeService;
import cn.fooltech.fool_ops.domain.rate.vo.RateProgrammeRecordVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateProgrammeVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateResult;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>收益率方案网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-06-14 08:57:07
 */
@Controller
@RequestMapping(value = "/rate/program")
public class RateProgrammeController extends BaseController{
	
	/**
	 * 收益率方案网页服务类
	 */
	@Autowired
	private RateProgrammeService webService;
	
	/**
	 * 去收益率方案列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listRateProgramme(ModelMap model){
		return "/rate/program/manage";
	}
	
	/**
	 * 查找收益率方案列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(RateProgrammeVo vo,PageParamater pageParamater){
		Page<RateProgrammeVo> page = webService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 编辑收益率方案页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		
		//==1则为修改
		if(mark==1){
			RateProgramme rateProgramme = webService.get(id);
			model.put("rateProgramme",webService.getVo(rateProgramme, true));
		}
		
		return "/rate/program/edit";
	}
	
	/**
	 * 新增/编辑收益率方案
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(RateProgrammeVo vo){
		return webService.save(vo);
	}
	/**
	 * 新增/编辑收益率方案明细
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDetail",method=RequestMethod.POST)
	public RequestResult saveDetail(RateProgrammeRecordVo vo){
		return webService.saveDetail(vo);
	}
	
	/**
	 * 删除收益率方案
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}
	/**
	 * 删除收益率方案明细
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteDetail")
	public RequestResult deleteDetail(@RequestParam String id){
		return webService.deleteDetail(id);
	}
	/**
	 * 计算收益率方案
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/calRate")
	public RateResult calRate(@RequestParam String id) {
		return webService.calRate(id);
	}
}