package cn.fooltech.fool_ops.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class MainsiteErrorController{

	@RequestMapping("/403")
	public String page403() {
		return "/error/403";
	}
	
	@RequestMapping("/404")
	public String page404() {
		return "/error/404";
	}
	
	@RequestMapping("/500")
	public String page500() {
		return "/error/500";
	}
	
	/**
	 * 500
	 * @return
	 */
	@RequestMapping("/inernal")
	public String internal() {
		return "/error/500";
	}
	
	@RequestMapping("/timeout")
	public String timeout() {
		return "/error/timeout";
	}
	
	/**
	 * 数据不存在
	 * @return
	 */
	@RequestMapping("/nodata")
	public String nodata() {
		return "/error/nodata";
	}

}
