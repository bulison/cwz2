package cn.fooltech.fool_ops.web.rate;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.rate.entity.RateMember;
import cn.fooltech.fool_ops.domain.rate.entity.RateMemberSum;
import cn.fooltech.fool_ops.domain.rate.service.RateMemberService;
import cn.fooltech.fool_ops.domain.rate.vo.RateMemberSumVo;
import cn.fooltech.fool_ops.web.base.BaseController;

@Controller
@RequestMapping(value = "/rate/rateMember")
public class RateMemberController extends BaseController{
	@Autowired
	private RateMemberService rmwService;
	
	@RequestMapping(value="/manage")
	public String  manage(){
		return "/rate/member/manage";
	}
	
	@RequestMapping(value="/detailPage")
	public String  detailPage(){
		return "/rate/member/detail";
	}
	@Deprecated
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(@DateTimeFormat(pattern = "yyyy-mm-dd")Date startDay, @DateTimeFormat(pattern = "yyyy-mm-dd")Date endDay, String memberIds, PageParamater pageParamater){
		Page<RateMemberSum> page = rmwService.query(startDay, endDay, memberIds, pageParamater);
		PageJson pageJson = this.getSumPageJson(page);
		return pageJson;
	}
	
	@RequestMapping(value="/getUserEvents")
	@ResponseBody
	public PageJson queryUser(@RequestParam Date startDate, @RequestParam Date endDate, String userId, PageParamater pageParamater){
		Page<RateMember> page = rmwService.queryUserEvents(startDate, endDate, userId, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	protected PageJson getSumPageJson(Page<RateMemberSum> page) {
		PageJson pageJson = new PageJson();
		List<RateMemberSumVo> vos = rmwService.getSumVos(page.getContent());
		pageJson.setRows(vos);
		pageJson.setTotal(page.getTotalElements());
		return pageJson;
	}
	/**
	 * 数据预处理
	 */
	@RequestMapping(value="/rateMemberDataPreprocess")	
	public RequestResult  dataPreprocessingEventsNumber(){
		return rmwService.rateMemberDataPreprocess();
	}
}
