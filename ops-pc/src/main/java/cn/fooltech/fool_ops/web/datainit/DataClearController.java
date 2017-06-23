package cn.fooltech.fool_ops.web.datainit;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.datainit.DataClearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>数据清空Controller</p>
 * @author xjh
 * @version 1.0
 * @date 2015年10月19日
 */
@Controller
@RequestMapping(value="/data")
public class DataClearController {

	@Autowired
	private DataClearService clearWebService;
	
	@RequestMapping("/manage")
	public String manage(){
		return "/initdata/initdata";
	}
	/**
	 * 清空业务数据
	 * @param tag  1、单据格式化；2、财务格式化；3、系统格式化
	 * @param pwd  验证密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/clear")
	public RequestResult clear(@RequestParam(value = "tag[]") int[] tag, String pwd){
		return clearWebService.clear(tag,pwd);
	}
}
