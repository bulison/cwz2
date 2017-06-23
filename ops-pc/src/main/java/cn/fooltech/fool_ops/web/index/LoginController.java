package cn.fooltech.fool_ops.web.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	
	@Value("${cn.fooltech.fool_ops.debug.loginName}")
	private String loginName = "";
	
	@Value("${cn.fooltech.fool_ops.debug.password}")
	private String password = "";
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value ="/login", method=RequestMethod.GET)
	public String toLogin(ModelMap model ) {
		// 触屏
		String url = "main/login";
		
		/*UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
		
		//手机浏览器或者平板电脑
		if(DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType)){
			url = "main/mobileLogin";
		}*/
		model.put("defaultLoginName", loginName);
		model.put("defaultPassword", password);
		return url;
	}
}