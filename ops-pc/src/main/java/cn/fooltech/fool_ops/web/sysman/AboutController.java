package cn.fooltech.fool_ops.web.sysman;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>关于我们 网页控制器类</p>
 * @author lzf
 * @version 1.0
 * 2015年7月1日
 */
@Controller
@RequestMapping(value="/aboutController")
public class AboutController {
	
	/**
	 * 关于页面
	 * @return
	 */
	@RequestMapping(value="/about")
	public String about(String site,HttpServletRequest request){
		request.setAttribute("site", site);
		return "about/about";
	}
	
	/**
	 * 关于我们页面
	 * @return
	 */
	@RequestMapping(value="/aboutUs")
	public String aboutUs(){
		return "about/aboutUs";
	}
	
	/**
	 * 活动报道页面
	 * @return
	 */
	@RequestMapping(value="/activityReport")
	public String activityReport(){
		return "about/activityReport";
	}
	
	/**
	 * 联系我们页面
	 * @return
	 */
	@RequestMapping(value="/contractUs")
	public String contractUs(){
		return "about/contractUs";
	}
	
	/**
	 * 友情链接页面
	 * @return
	 */
	@RequestMapping(value="/friendSite")
	public String friendSite(){
		return "about/friendSite";
	}
	
	/**
	 * 人才招聘页面
	 * @return
	 */
	@RequestMapping(value="/recruitment")
	public String recruitment(){
		return "about/recruitment";
	}

}
