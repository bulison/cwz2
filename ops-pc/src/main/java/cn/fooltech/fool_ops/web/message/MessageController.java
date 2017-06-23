package cn.fooltech.fool_ops.web.message;

import java.util.List;

import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import cn.fooltech.fool_ops.domain.message.vo.TodoVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.vo.MessageVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONObject;

/**
 * <p>消息计划网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Controller
@RequestMapping(value ="/message")
public class MessageController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(MessageController.class);


	@Autowired
	private MessageService messageService;

	@Autowired
	private PushService pushService;
	
	/**
	 * 消息列表页
	 */
	@RequestMapping(value = "/listMessage")
	public String listMessage(ModelMap model){
		model.put("accId", SecurityUtil.getFiscalAccountId());//当前登录账套ID
		return "/message/messageNew";
	}

	/**
	 * 消息列表页
	 */
	@RequestMapping(value = "/listTodo")
	public String listTodo(ModelMap model){
		model.put("accId", SecurityUtil.getFiscalAccountId());//当前登录账套ID
		return "/message/todo";
	}
	
	/**
	 * 消息列表页
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public PageJson query(PageParamater pageParamater, MessageVo vo){
		Page<MessageVo> page = messageService.query(vo,pageParamater);
		
		String lastEndTime = "";
		if(page.getContent().size()>0){
			MessageVo message = page.getContent().get(page.getContent().size()-1);
			lastEndTime = DateUtilTools.time2String(message.getSendTime());
		}
		PageJson pageJson = new PageJson(page);
		pageJson.setOther(lastEndTime);
		return pageJson;
	}

	/**
	 * 查看待办任务分页
	 */
	@RequestMapping(value="/queryTodo")
	@ResponseBody
	public PageJson queryTodo(PageParamater pageParamater){
		Page<TodoVo> page = messageService.findTodoPage(pageParamater);
		return new PageJson(page);
	}
	
	
	/**
	 * 查看信息
	 */
	@RequestMapping(value="/read")
	@ResponseBody
	public RequestResult read(@RequestParam String messageId){
		return messageService.read(messageId);
	}
	
	/**
	 * 操作信息
	 */
	@RequestMapping(value="/oper")
	@ResponseBody
	public RequestResult oper(@RequestParam String messageId){
		return messageService.oper(messageId);
	}
	
	/**
	 * 拉取未读消息
	 * @return
	 */
	@RequestMapping(value = "/pullUnReadMessage")
	public @ResponseBody List<MessageVo> pullUnReadMessage(MessageVo vo){
		List<MessageVo> list = messageService.queryAllUnReadMessages(vo);
		return list;
	}
	
	/**
	 * 拉取当前登录用户第一条未通知的消息和未读消息的条数
	 */
	@RequestMapping(value = "/checkMessage")
	@ResponseBody
	public UnReadMessage checkMessage(){
		//拉取当前登录用户第一条未通知的消息
		MessageVo messageVo = messageService.existUnNotifyMessage();
		
		//获取未读消息条数
		long count = messageService.countUnReadMessage();
		JSONObject json = new JSONObject();
		json.put("message", messageVo);
		json.put("unread", count);

		UnReadMessage unReadMessage = new UnReadMessage();
		unReadMessage.setMessage(messageVo);
		unReadMessage.setUnread(count);
		return unReadMessage;
	}

//	/**
//	 * 其他服务刷新推送
//	 * @param userId
//	 */
//	@GetMapping(value = "/refreshPush")
//	public void refreshPush(@RequestParam String userId){
//
//		logger.info("pc push:"+userId);
//
//		//拉取当前登录用户第一条未通知的消息
//		MessageVo message = messageService.existUnNotifyMessage();
//
//		if(message!=null){
//			SimpleMessageVo svo = new SimpleMessageVo();
//			svo.setContent(message.getContent());
//			svo.setTitle(message.getTitle());
//			svo.setTriggerType(message.getTriggerType());
//			pushService.push(userId, svo, false);
//		}else{
//			pushService.push(userId, null, false);
//		}
//
//	}

	@Getter
	@Setter
	@NoArgsConstructor
	class UnReadMessage{
		MessageVo message;
		long unread;
	}
	
}
