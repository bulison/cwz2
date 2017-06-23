package cn.fooltech.fool_ops.web.period;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.period.vo.StockPeriodVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>仓储会计期间控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月27日
 */
@Controller
@RequestMapping(value = "/periodController")
public class PeriodController extends BaseController{
	
	@Autowired
	private StockPeriodService periodService;
	
	/**
	 * 仓储会计期间结账、反结账
	 * @param request
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changeStatus")
	public RequestResult changeStatus(HttpServletRequest request, StockPeriodVo vo){
		return periodService.changeStatus(vo);
	}
	
	/**
	 * 获取所有未结账的会计期间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/comboboxData")
	public List<StockPeriodVo> comboboxData(){
		return periodService.getNotCheck();
	}
	
	/**
	 * 获取所有未结账、已结账的会计期间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public List<StockPeriodVo> queryAll(){
		return periodService.queryAll();
	}
	
	/**
	 * 判断第一个会计期间是否结账
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/theFirstCheck")
	public RequestResult getIsCheck(){
		boolean check = periodService.theFirstCheck();
		if(check){
			return new RequestResult();
		}
		return new RequestResult(RequestResult.RETURN_FAILURE,"");
	}
	
}
