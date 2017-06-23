package cn.fooltech.fool_ops.web.cashier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.domain.cashier.service.BalanceAdjustService;
import net.sf.json.JSONObject;

/**
 * <p>余额调节网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月6日
 */
@Controller
@RequestMapping("/balanceAdjust")
public class BalanceAdjustController {
	
	/**
	 * 余额调节网页服务类
	 */
	@Autowired
	private BalanceAdjustService balanceAdjustWebService;
	
	/**
	 * 查询
	 * @param subjectId 科目ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public String query(String subjectId){
		JSONObject jsonObject = balanceAdjustWebService.query(subjectId);
		return jsonObject.toString();
	}
	
}
