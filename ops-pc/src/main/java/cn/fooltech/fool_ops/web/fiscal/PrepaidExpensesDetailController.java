package cn.fooltech.fool_ops.web.fiscal;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.fiscal.service.PrepaidExpensesDetailService;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * <p>待摊费用明细网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/prepaidExpensesDetail")
public class PrepaidExpensesDetailController {
	
	@Autowired
	private PrepaidExpensesDetailService prepaidExpensesDetailService;
	
	/**
	 * 查询明细
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PrepaidExpensesDetailVo vo, PageParamater paramater){
		return prepaidExpensesDetailService.query(vo, paramater);
	}
	
}
