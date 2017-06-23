package cn.fooltech.fool_ops.web.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.rate.service.LoanRateService;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateVo;

/**
 * <p>央行贷款利率网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年6月14日
 */
@Controller
@RequestMapping("/rate/loanrate")
public class LoanRateController {

	@Autowired
	private LoanRateService loanRateWebService;
	
	/**
	 * 基础信息采集管理页面
	 * @throws Exception
	 */
	@RequestMapping("/manage")
	public String manage() throws Exception{
		return "rate/baseInfoCollection/manage";
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 * @throws Exception
	 *//*
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(LoanRateVo vo, PageParamater paramater) throws Exception{
		return new PageJson(loanRateWebService.query(vo, paramater));
	}
	
	*//**
	 * 新增、编辑
	 * @param vo
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(LoanRateVo vo){
		return loanRateWebService.save(vo);
	}

	*//**
	 * 删除
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return loanRateWebService.delete(fid);
	}*/
	
}
