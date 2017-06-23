package cn.fooltech.fool_ops.web.register;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cn.fooltech.fool_ops.component.core.PhoneMsgService;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.register.entity.Sms;
import cn.fooltech.fool_ops.domain.register.service.RegisterService;
import cn.fooltech.fool_ops.domain.register.vo.UserAndOrgInfoVo;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.DateUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * <p>注册控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年8月28日
 */
@Controller
@RequestMapping(value = "/registerController")
public class RegisterController {
	
	@Autowired
    private RegisterService registerService;
	
	@Autowired
	private OrgService orgService;

	
	@Autowired
	private PhoneMsgService phoneMsgService;
	
	@RequestMapping(value = "/registerPage")
	public String settingUI(){
		return "register/register";
	} 
	
	/**
	 * 注册信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/registerInfo")
	@ResponseBody
	public RequestResult registerInfo(UserAndOrgInfoVo vo){
		if(orgService.isCodeExist(vo.getOrgCode())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "机构代码已存在!");
		}
		if(registerService.isUserExists(vo)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "用户账号已存在!");
		}
		registerService.saveUserInfo(vo);
		return new RequestResult();
	}
	
	/**
	 * 验证码短信发送
	 * @param request
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/sendSecurityCode")
	@ResponseBody
	@Deprecated
	public String sendSecurityCode(HttpServletRequest request,UserAndOrgInfoVo vo){
		//		phoneMsgSender.send(message, template, repeatTime, sendImmediately);
//		PhoneMsgUtils.send(receiver, content)
		//已经存在用户
//		if(registerService.isPhoneExists(vo)){
//			return "3";
//		}
		Sms sms = (Sms)request.getSession().getAttribute(vo.getPhoneOne());
		if(sms==null){

		}else{
			long stime = sms.getCreateTime().getTime();
			Date now = new Date();
			if(now.getTime() > (stime+ DateUtils.MILLIS_PER_MINUTE)){
				sms.setCreateTime(now);
			}else{
				//时间没到一分钟
				return "2";
			}
		}
		request.getSession().setAttribute(vo.getPhoneOne(), sms);
		String code = RandomUtils.nextInt(10)+""+RandomUtils.nextInt(10)+""+RandomUtils.nextInt(10)+""+RandomUtils.nextInt(10)+""+RandomUtils.nextInt(10)+""+RandomUtils.nextInt(10);
		request.getSession().setAttribute("securityCode", code);
		request.getSession().setAttribute("phone", vo.getPhoneOne());
		/*sms.setContent("你的验证码为"+code);
		PhoneMsgUtils.SendResponse sendResponse = PhoneMsgUtils.send(sms.getTel(), sms.getContent());
		//发送成功
		if(SendResponse.SEND_SUCCESS.equals(sendResponse.getResponseCode())){
				//保存短信记录表
			sms.setSendFlag(Sms.WAIT_CALL);
			sms.setTradeNo(sendResponse.getTradeNo());
		}else{
			sms.setSendFlag(Sms.SEND_FAIL);
			return "0";
		}*/
		return "1";
	}
	
	@RequestMapping(value="/getcode")
	@ResponseBody
	@Deprecated
	public String getCode(HttpServletRequest request){
		String code = (String)request.getSession().getAttribute("securityCode");
		return code;
	}
	
}
