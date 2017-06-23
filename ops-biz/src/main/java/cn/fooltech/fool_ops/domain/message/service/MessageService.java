package cn.fooltech.fool_ops.domain.message.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.TaskRepository;
import cn.fooltech.fool_ops.domain.message.dao.MessageDao;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.entity.MessageParamater;
import cn.fooltech.fool_ops.domain.message.repository.MessageParamaterRepository;
import cn.fooltech.fool_ops.domain.message.repository.MessageRepository;
import cn.fooltech.fool_ops.domain.message.sender.factory.Sender;
import cn.fooltech.fool_ops.domain.message.sender.generator.impl.SenderGeneratorService;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplate;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateParamaterService;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateService;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.message.vo.MessageVo;
import cn.fooltech.fool_ops.domain.message.vo.TodoVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ReflectionUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.fooltech.fool_ops.utils.SecurityUtil.getFiscalAccountId;


/**
 * 信息网页服务类
 * @author xjh
 * @version 1.0
 * @dete 2015年5月21日
 */
@Service
public class MessageService extends BaseService<Message,MessageVo,String>{
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	public static final String DELIVRD = "DELIVRD";//短消息转发成功
	public static final String EXPIRED = "EXPIRED";//短消息超过有效期
	public static final String ERR_104 = "ERR:104";//系统忙
	
	/**
	 * 发送器生成服务
	 */
	@Autowired
	private SenderGeneratorService senderGenService;
	/**
	 * 消息服务类
	 */
	@Autowired
	private MessageRepository messageRepo;

	/**
	 * 消息Dao
	 */
	@Autowired
	private MessageDao messageDao;
	/**
	 * 消息模板服务类
	 */
	@Autowired
	private MessageTemplateService templateService;
	/**
	 * 消息模板参数服务类
	 */
	@Autowired
	private MessageTemplateParamaterService templateParamaterService;
	
	/**
	 * 消息参数服务类
	 */
	@Autowired
	private MessageParamaterRepository paramaterRepository;
	
	@Autowired
	private TaskRepository taskRepo;

	/**
	 * 不使用模板，发送普通消息
	 */
	@Transactional
	public void sendNormalMsg(User sender, User receiver, String title, String content,
							  FiscalAccount account, int triggerType){

		Message entity = new Message();
		entity.setContent(content);
		entity.setTitle(title);

		if(sender!=null){
			entity.setSenderId(sender.getFid());
			entity.setSenderName(sender.getUserName());
		}
		entity.setReceiverId(receiver.getFid());
		entity.setReceiverName(receiver.getUserName());
		entity.setSendTime(Calendar.getInstance().getTime());
		entity.setSendType(MessageTemplate.SEND_TYPE_INNER);
		entity.setStatus(Message.STATUS_UNREAD);
		entity.setTriggerType(triggerType+"");
		entity.setType(Message.TYPE_NOTIFY);
		entity.setFiscalAccount(account);

		save(entity);

		Sender sendFacty = senderGenService.getSuitSender(MessageTemplate.SEND_TYPE_INNER);
		sendFacty.send(entity, 3, true);

	}

	/**
	 * 不使用模板，发送普通消息
	 */
	@Transactional
	public void sendCaptialWarningMsg(User sender, User receiver, String title, String content, FiscalAccount account){

		Message entity = new Message();
		entity.setContent(content);
		entity.setTitle(title);

		if(sender!=null){
			entity.setSenderId(sender.getFid());
			entity.setSenderName(sender.getUserName());
		}
		entity.setReceiverId(receiver.getFid());
		entity.setReceiverName(receiver.getUserName());
		entity.setSendTime(Calendar.getInstance().getTime());
		entity.setSendType(MessageTemplate.SEND_TYPE_INNER);
		entity.setStatus(Message.STATUS_UNREAD);
		entity.setTriggerType(Message.TRIGGER_TYPE_CAPTIAL_WARNING+"");
		entity.setType(Message.TYPE_WARNING);
		entity.setFiscalAccount(account);

		save(entity);

		Sender sendFacty = senderGenService.getSuitSender(MessageTemplate.SEND_TYPE_INNER);
		sendFacty.send(entity, 3, true);

	}

	/**
	 * 查找待办任务分页
	 * @param page
	 * @return
	 */
	public Page<TodoVo> findTodoPage(PageParamater page){
		String receiverId = SecurityUtil.getCurrentUserId();
		String accId = SecurityUtil.getFiscalAccountId();

		com.github.pagehelper.Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows());

		List<TodoVo> datas = messageDao.findTodoPage(accId, receiverId);

		PageRequest pageRequest = getPageRequest(page);
		Page<TodoVo> pageImpl = new PageImpl<TodoVo>(datas, pageRequest, pageHelper.getTotal());
		return pageImpl;
	}

	/**
	 * 分页查找未读消息
	 */
	public Page<MessageVo> query(MessageVo vo,PageParamater pageParamater){

		Order[] orders = {new Order(Direction.ASC, "status"), new Order(Direction.DESC, "sendTime")};
		Sort sort = new Sort(orders);
		PageRequest pageRequest = getPageRequest(pageParamater, sort);

		Page<Message> page = findPage(vo, pageRequest);

		return getPageVos(page, pageRequest);
	}

	/**
	 * 分页查找未读消息
	 */
	public Page<Message> findPage(MessageVo vo, PageRequest pageRequest){
		String receiverId = SecurityUtil.getCurrentUserId();//获取当前登录的用户的信息
		Integer type =  vo.getType();//  类型
		Integer status = vo.getStatus();  //状态  已读
		String lastEndTime = vo.getLastEndTime();//记录临界时间
		String accId = vo.getAccId(); //获取账套Id
		String title = vo.getTitle();

		Page<Message> page = messageRepo.findPageBy(receiverId, type, status, lastEndTime, accId, title, pageRequest);
		return page;
	}

	/**
	 * 分页查找未读消息
	 */
	public Page<MessageVo> querySimple(MessageVo vo,PageParamater pageParamater){

		Order[] orders = {new Order(Direction.ASC, "status"), new Order(Direction.DESC, "sendTime")};
		Sort sort = new Sort(orders);
		PageRequest pageRequest = getPageRequest(pageParamater, sort);

		Page<Message> page = findPage(vo, pageRequest);

		List<MessageVo> list = page.getContent()
				.stream()
				.map(p->getSimpleVo(p))
				.collect(Collectors.toList());

		Page<MessageVo> pageVos = new PageImpl(list, pageRequest, page.getTotalElements());
		return pageVos;
	}
	
	/**
	 * 分页查找未读消息
	 */
	public List<MessageVo> queryAllUnReadMessages(MessageVo vo){
		String accId = getFiscalAccountId();
		List<Message> messageList = messageRepo.findRelatedUnreadMsg(vo.getReceiverId(), accId);
		return getVos(messageList);
	}
	
	
	/**
	 * 单个实体转换成vo
	 * @param entity
	 * @return
	 */
	@Override
	public MessageVo getVo(Message entity ) {
		MessageVo vo = new MessageVo();
		vo.setFid(entity.getFid());
		vo.setType(entity.getType());
		vo.setTitle(entity.getTitle());
		vo.setContent(entity.getContent());
		vo.setReceiverId(entity.getReceiverId());
		vo.setReceiverName(entity.getReceiverName());
		vo.setSenderName(entity.getSenderName());
		vo.setSendTime(entity.getSendTime());
		vo.setStatus(entity.getStatus());
		vo.setFid(entity.getFid());
		vo.setOperTime(entity.getOperTime());
		vo.setIsnotify(entity.getIsnotify());
		vo.setTriggerType(entity.getTriggerType());
		
		FiscalAccount account = entity.getFiscalAccount();
		if(account!=null){
			vo.setAccId(account.getFid());
			vo.setAccName(account.getName());
		}
		
		List<MessageParamater> params = entity.getParams();

		if(params!=null && params.size()>0){
			JSONArray array = new JSONArray();
			for(MessageParamater p:params){
				JSONObject json = new JSONObject();
				json.put("busData", p.getBusData());
				json.put("busClass", p.getBusClass());
				json.put("busScene", p.getBusScene());

				if(p.getBusClass().equals(Task.class.getName())){
					long total = taskRepo.countChilds(p.getBusData());
					json.put("childSize", total);
				}

				array.add(json);
			}

			vo.setBusParamObj(array.toString());
		}else{
			vo.setBusParamObj("[]");
		}

		return vo;
	}

	/**
	 * 单个实体转换成vo
	 * @param entity
	 * @return
	 */
	public MessageVo getSimpleVo(Message entity ) {
		MessageVo vo = new MessageVo();
		vo.setFid(entity.getFid());
		vo.setType(entity.getType());
		vo.setTitle(entity.getTitle());
		vo.setContent(entity.getContent());
		vo.setReceiverId(entity.getReceiverId());
		vo.setReceiverName(entity.getReceiverName());
		vo.setSenderName(entity.getSenderName());
		vo.setSendTime(entity.getSendTime());
		vo.setStatus(entity.getStatus());
		vo.setFid(entity.getFid());
		vo.setOperTime(entity.getOperTime());
		vo.setIsnotify(entity.getIsnotify());
		vo.setTriggerType(entity.getTriggerType());

		return vo;
	}
	
	/**
	 * 查看信息
	 * @param messageId
	 */
	public RequestResult read(String messageId) {
		Message entity = messageRepo.findOne(messageId);
		if(entity.getStatus() == Message.STATUS_UNREAD){
			entity.setStatus(Message.STATUS_READ);
			entity.setReadTime(new Date());
			messageRepo.save(entity);
		}
		return buildSuccessRequestResult(getVo(entity));
	}
	
	/**
	 * 操作信息
	 */
	public RequestResult oper(String messageId) {
		operMessage(messageId);
		return buildSuccessRequestResult();
	}
	/**
	 * 操作指定信息，将其状态设置为已操作
	 * @param messageId
	 */
	@Transactional
	public Message operMessage(String messageId) {
		Message entity = get(messageId);
		return oper(entity);
	}
	
	/**
	 * 操作指定信息，将其状态设置为已操作
	 * @param entity
	 */
	@Transactional
	public Message oper(Message entity) {
		if(entity != null && entity.getOperTime()==null && entity.getType()== Message.TYPE_WAIT_PROCESS){
			
			//把消息设置为已操作
			entity.setOperTime(Calendar.getInstance().getTime());
			save(entity);
		}
		return entity;
	}
	/**
	 * 操作信息，将其状态设置为已操作
	 * @param busClass 业务类型
	 * @param busScene 业务场景（操作前的状态）
	 * @param busData 业务数据（FID）
	 */
	@Transactional
	public void oper(String busClass, String busScene, String busData){
		List<MessageParamater> list = paramaterRepository.oper(busClass, busScene, busData);
		for(MessageParamater msgParam:list){
			oper(msgParam.getMessage());
		}
	}

	/**
	 * 拉取用户第一条未通知的消息,没有则返回null
	 * 其余的设置为已通知
	 */
	public MessageVo existUnNotifyMessage() {

		String accId = getFiscalAccountId();
		String receiverId = SecurityUtil.getCurrentUserId();

		List<Message> messages = messageRepo.findUnNotifyMessage(receiverId, accId);
		if(messages.size()>0){
			for(Message message:messages){
				message.setIsnotify(Boolean.TRUE);
				messageRepo.save(message);
			}
			return getVo(messages.get(0));
		}else return null;
	}
	
	/**
	 * 拉取未读消息条数
	 */
	public long countUnReadMessage() {
		String accId = SecurityUtil.getFiscalAccountId();
		String userId = SecurityUtil.getCurrentUserId();
		return messageRepo.countUnReadMessage(userId,accId);
	}

	@Override
	public CrudRepository<Message, String> getRepository() {
		return messageRepo;
	}


	/**
	 * 短信回调，更新短信发送记录
	 * @param tradeNo:流水号
	 * @param state:返回状态
	 */
	/*public void updateCallBack(String tradeNo, String state) {
		Sms sms = smsService.findUniqueBy("tradeNo", tradeNo);
		if(sms!=null){
			sms.setLastUpdateTime(new Date());
			sms.setSendResult(sms.getSendResult()+";"+state);

			if(DELIVRD.equals(state)){
				sms.setSendFlag(Sms.SENDED);
			}else if(EXPIRED.equals(state)||ERR_104.equals(state)){
				//判断发送次数是否等于重发次数，小于则继续重发
				if(sms.getSendRepeat() && sms.getCurrRepeatTime()<sms.getRepeatTime()){
					
					PhoneMsgUtils.SendResponse response = null;
					int curRepeatTime = sms.getCurrRepeatTime();
					do{
						response = PhoneMsgUtils.send(sms.getTel(), sms.getContent());
						curRepeatTime++;
						sms.setSendResult(sms.getSendResult()+";"+response.getResponseDesc());
						
						// 发送记录暂时不处理
					}while(!SendResponse.SUCCESS_CODE.equals(response.getResponseCode()) && curRepeatTime < sms.getRepeatTime());
					
					sms.setCurrRepeatTime(curRepeatTime);
					//发送成功
					if(SendResponse.SUCCESS_CODE.equals(response.getResponseCode())){
						
						//保存短信记录表
						sms.setSendFlag(Sms.WAIT_CALL);
						sms.setTradeNo(response.getTradeNo());
					}else{
						sms.setSendFlag(Sms.SEND_FAIL);
					}
				}else{
					sms.setSendFlag(Sms.SEND_FAIL);
				}
			}else{
				sms.setSendFlag(Sms.SEND_FAIL);
			}
			smsService.update(sms);
		}
	}*/
	
	/**
	 * 发送信息
	 * @param busClass
	 * @param busScene
	 * @param paramMap
	 * @param receiver
	 * @throws Exception 
	 */
	@Transactional
	public void sendMessage(String busClass, String busScene, Map<String,Object> paramMap,
			User receiver, Integer triggerType, FiscalAccount account, int tag) throws Exception{
		sendMessage(busClass, busScene, paramMap, receiver, null, triggerType, account, tag);
	}
	
	/**
	 * 发送信息
	 * @param busClass
	 * @param busScene
	 * @param paramMap
	 * @param receivers
	 * @throws Exception 
	 */
	@Transactional
	public void sendMessage(String busClass, String busScene, Map<String,Object> paramMap,List<User> receivers, 
			Integer triggerType, FiscalAccount account, int tag) throws Exception{
		for(User recev:receivers){
			sendMessage(busClass, busScene, paramMap, recev, null, triggerType, account, tag);
		}
	}
	
	/**
	 * 发送信息
	 * @param busClass
	 * @param busScene
	 * @param paramMap
	 * @param receivers
	 * @param sender
	 * @throws Exception 
	 */
	public void sendMessage(String busClass, String busScene, Map<String,Object> paramMap, List<User> receivers, 
			User sender, Integer triggerType, FiscalAccount account, int tag){
		for(User recev:receivers){
			sendMessage(busClass, busScene, paramMap, recev, sender, triggerType, account, tag);
		}
	}
	
	/**
	 * 发送信息
	 * @param busClass
	 * @param busScene
	 * @param paramMap
	 * @param receiver
	 * @param sender
	 * @throws Exception 
	 */
	@Transactional
	public void sendMessage(String busClass, String busScene, Map<String,Object> paramMap,User receiver, User sender, 
			Integer triggerType, FiscalAccount account, int tag){
		List<MessageTemplate> templates = templateService.queryTemplates(busClass, busScene, triggerType);
		Map<String, String> cache = Maps.newHashMap();
		
		for(MessageTemplate template:templates){
			
			List<MessageTemplateParamater> paramaters = templateParamaterService.queryByTemplateId(template.getFid());
			List<MessageParamater> msgParamaters = Lists.newArrayList(); 
			
			for(MessageTemplateParamater paramater:paramaters){
				
				String paramaterAlias = paramater.getParamaterAlias();
				if(!cache.containsKey(paramaterAlias)){
					String key = paramater.getParamaterKey();
					String type = paramater.getParamaterType();
					String objField = paramater.getObjField();
					
					Object data = paramMap.get(key);
					Assert.notNull(data, "发送参数对象不能为空;type:"+type+";key:"+key+";fid:"+paramater.getFid());
					
					try {
						Object fieldObj = ReflectionUtils.invokeMethod(data, getGetterMethod(objField), null, null);
						
						if(paramater.getType().equals(MessageTemplateParamater.TYPE_REPLACE)){
							cache.put(paramaterAlias, fieldObj==null?"":fieldObj.toString());
						}else{
							MessageParamater mp = new MessageParamater();
							mp.setBusClass(busClass);
							mp.setBusData(fieldObj==null?"":fieldObj.toString());
							mp.setBusScene(busScene);
							msgParamaters.add(mp);
						}
						
					}catch (SecurityException e) {
						e.printStackTrace();
						logger.error("模板参数错误:"+paramater.getFid()+":"+e.getMessage());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						logger.error("模板参数错误:"+paramater.getFid()+":"+e.getMessage());
					}
				}
			}
			
			String content = StringUtils.composeMessage(template.getFormatPattern(), cache);
			String title = StringUtils.composeMessage(template.getTitle(), cache);

			Message entity = new Message();
			entity.setContent(content);
			entity.setTitle(title);
			
			if(sender!=null){
				entity.setSenderId(sender.getFid());
				entity.setSenderName(sender.getUserName());
			}
			entity.setReceiverId(receiver.getFid());
			entity.setReceiverName(receiver.getUserName());
			entity.setSendTime(Calendar.getInstance().getTime());
			entity.setSendType(template.getSendType());
			entity.setStatus(Message.STATUS_UNREAD);
			entity.setTriggerType(template.getTriggerType());
			entity.setType(template.getOpertype());
			entity.setFiscalAccount(account);
			
			save(entity);
			
			for(MessageParamater p:msgParamaters){
				p.setMessage(entity);
				paramaterRepository.save(p);
			}
			
			if(entity.getSendType().equals(MessageTemplate.SEND_TYPE_PHONE) && SendUtils.isPhoneMsgSupport(tag)){
				Sender sendFacty = senderGenService.getSuitSender(entity.getSendType());
				sendFacty.send(entity, 3, true);
			}else if(entity.getSendType().equals(MessageTemplate.SEND_TYPE_EMAIL) && SendUtils.isEmailSupport(tag)){
				Sender sendFacty = senderGenService.getSuitSender(entity.getSendType());
				sendFacty.send(entity, 3, true);
			}else if(entity.getSendType().equals(MessageTemplate.SEND_TYPE_INNER)){
				Sender sendFacty = senderGenService.getSuitSender(entity.getSendType());
				sendFacty.send(entity, 3, true);
			}
		}
	}
	/**
	 * 获得getter
	 * @param field
	 * @return
	 */
	private String getGetterMethod(String field){
		return "get"+uppercaseFirstChar(field);
	}

	public static String uppercaseFirstChar(String in) {
		if ((in == null) || (in.length() == 0)) {
			return in;
		}
		int length = in.length();
		StringBuilder sb = new StringBuilder(length);

		sb.append(Character.toUpperCase(in.charAt(0)));
		if (length > 1) {
			String remaining = in.substring(1);
			sb.append(remaining);
		}
		return sb.toString();
	}
	/**
	 * 使用模板处理参数，返回处理后的字符串
	 */
	public String processTemplate(String templateStr,Map<String,String> map) throws Exception{
		return StringUtils.composeMessage(templateStr, map);
	}

	/**
	 * 操作信息，将其状态设置为已操作
	 * @param busData 业务数据（FID）
	 */
	@Transactional
	public void operAllMessage(String busData){
		List<MessageParamater> msgParams = paramaterRepository.getByBusData(busData);
		for(MessageParamater msgParam:msgParams){
			oper(msgParam.getMessage());
		}
	}

}
