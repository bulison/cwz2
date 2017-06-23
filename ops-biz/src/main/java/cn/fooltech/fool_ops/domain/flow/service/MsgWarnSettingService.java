package cn.fooltech.fool_ops.domain.flow.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.MsgWarnSetting;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Supervise;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.MsgWarnSettingRepository;
import cn.fooltech.fool_ops.domain.flow.vo.MsgWarnSettingVo;
import cn.fooltech.fool_ops.domain.flow.vo.SuperviseVo;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.entity.MessageParamater;
import cn.fooltech.fool_ops.domain.message.repository.MessageParamaterRepository;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplate;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateParamaterService;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.ReflectionUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * <p>消息预警配置网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-17 14:08:34
 */
@Service
public class MsgWarnSettingService extends BaseService<MsgWarnSetting,MsgWarnSettingVo,String> {
	private static final Logger logger = LoggerFactory.getLogger(MsgWarnSettingService.class);
	/**
	 * 消息预警配置服务类
	 */
	@Autowired
	private MsgWarnSettingRepository repository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageParamaterRepository paramaterRepository;
	
	/**
	 * 发送器生成服务
	 */
//	@Autowired
//	private SenderGeneratorService senderGenService;
	
	/**
	 * 事件级别服务类
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	
	/**
	 * 监督人服务类
	 */
	@Autowired
	private SuperviseService superviseService;
	
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
	 * 查询消息预警配置列表信息，按照消息预警配置taskType排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<MsgWarnSetting> query(MsgWarnSettingVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.ASC,"taskType");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<MsgWarnSetting> query = repository.query(vo, request);
		return query;
	}
	
	
	/**
	 * 单个消息预警配置实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public MsgWarnSettingVo getVo(MsgWarnSetting entity){
		if(entity == null)
			return null;
		MsgWarnSettingVo vo = new MsgWarnSettingVo();
		vo.setDays(entity.getDays());
		vo.setRetryDays(entity.getRetryDays());
		vo.setRange(entity.getRange());
		vo.setSendType(entity.getSendType());
		vo.setTriggerType(entity.getTriggerType());
		vo.setTaskType(entity.getTaskType());
		vo.setToSuperior(entity.getToSuperior());
		vo.setToSubordinate(entity.getToSubordinate());
		vo.setTaskLevel(new BigDecimal(entity.getTaskLevel()));
		vo.setBusScene(entity.getBusScene());
		vo.setType(entity.getType());
		vo.setIsSystem(entity.getIsSystem());
		vo.setFid(entity.getFid());
		List<SuperviseVo> supervises = superviseService.queryBySettingId(vo.getFid(), vo.getSendType());
		String superviseIds="";
		for(SuperviseVo supervise:supervises){
			superviseIds=superviseIds+supervise.getSuperviseId()+",";
		}
		if(!superviseIds.equals("")){
			vo.setSuperviseIds(superviseIds.substring(0,superviseIds.length()-1));
		}
		return vo;
	}
	
	/**
	 * 删除消息预警配置<br>
	 */
	@Transactional
	public RequestResult delete(String fid){
		MsgWarnSetting entity = repository.findOne(fid);
		if(entity.getIsSystem()==MsgWarnSetting.TYPE_SYSTEM_YES){
			return buildFailRequestResult("系统默认配置不能删除");
		}
		superviseService.deleteBySettingId(entity.getFid());
		repository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取消息预警配置信息
	 * @param fid 消息预警配置ID
	 * @return
	 */
	public MsgWarnSettingVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(repository.findOne(fid));
	}
	
	/**
	 * 新增/编辑消息预警配置
	 * @param vo
	 */
	@Transactional
	public RequestResult save(MsgWarnSettingVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		MsgWarnSetting entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new MsgWarnSetting();
			entity.setOrg(SecurityUtil.getCurrentOrg());
		}else {
			entity = repository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}
		
		//系统属性只能修改提前天数、重发天数、值域
		entity.setDays(vo.getDays());
		entity.setRetryDays(vo.getRetryDays());
		entity.setRange(vo.getRange());
		
		if(entity.getIsSystem()==MsgWarnSetting.TYPE_SYSTEM_NO || StringUtils.isBlank(vo.getFid())){
			entity.setSendType(vo.getSendType());
			entity.setTriggerType(vo.getTriggerType());
			entity.setTaskType(vo.getTaskType());
			entity.setToSuperior(vo.getToSuperior());
			entity.setToSubordinate(vo.getToSubordinate());
			entity.setTaskLevel((vo.getTaskLevel()).intValue());
			entity.setBusScene(vo.getBusScene());
			entity.setIsSystem(MsgWarnSetting.TYPE_SYSTEM_NO);
			
			Integer busScene = Integer.parseInt(vo.getBusScene());
			
			entity.setType(busScene >= Plan.STATUS_DRAFT ? MsgWarnSetting.TYPE_PLAN : MsgWarnSetting.TYPE_TASK);
			
//			if(entity.getSendType()==MsgWarnSetting.SEND_TYPE_BMJDR||entity.getSendType()==MsgWarnSetting.SEND_TYPE_GSJDR){
//				superviseService.deleteBySettingId(entity.getFid());
//				superviseService.saveSupervises(entity, vo.getSuperviseIds());
//			}
		}
		
		repository.save(entity);
		if(entity.getSendType()==MsgWarnSetting.SEND_TYPE_BMJDR||entity.getSendType()==MsgWarnSetting.SEND_TYPE_GSJDR){
			superviseService.deleteBySettingId(entity.getFid());
			superviseService.saveSupervises(entity, vo.getSuperviseIds());
		}
		
		return buildSuccessRequestResult(getVo(entity));
	}


	@Override
	public CrudRepository<MsgWarnSetting, String> getRepository() {
		return repository;
	}
	/**
	 * 通过配置查找类型为“计划的”消息接收人
	 * @param plan
	 * @param triggerType
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<User> queryPlanReceiver(Plan plan, Integer triggerType){

		List<MsgWarnSetting> settings = repository.query(plan.getPlanLevel().getLevel(), triggerType, MsgWarnSetting.TYPE_PLAN, plan.getOrg().getFid());
		
		Map<String, User> receiverMap = Maps.newHashMap();
		
		for(MsgWarnSetting set:settings){
			switch (set.getSendType()) {
			case MsgWarnSetting.SEND_TYPE_FQR://发起人
				addReceiver(plan.getInitiater(), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_ZRR://责任人
				addReceiver(plan.getPrincipaler(), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_CBR://承办人
				logger.warn("承办人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_GZR://关注人
				logger.warn("关注人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_PJR://评价人
				logger.warn("评价人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_LYR://留言人
				logger.warn("留言人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_PFR://评分人
				logger.warn("评分人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_BMJDR://部门监督人
				addReceiver(superviseService.queryBySettingId(set.getFid(), Supervise.TYPE_DEPT), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_GSJDR://公司监督人
				addReceiver(superviseService.queryBySettingId(set.getFid(), Supervise.TYPE_ORG), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_SHR://审核人
				addReceiver(plan.getAuditer(), receiverMap);
				break;
			default:
				break;
			}
		}
		
		return Lists.newArrayList(receiverMap.values());
	}
	/**
	 * 添加接收人到Map中，重复的不再添加
	 * @param receiver
	 * @param cache
	 */
	private void addReceiver(User receiver, final Map<String, User> cache){
		if(receiver==null)return;
		if(cache.containsKey(receiver.getFid()))return;
		cache.put(receiver.getFid(), receiver);
	}
	/**
	 * 添加接收人到Map中，重复的不再添加
	 * @param receivers
	 * @param cache
	 */
	private void addReceiver(List<SuperviseVo> receivers, final Map<String, User> cache){
		for(SuperviseVo receiver:receivers){
			User supervise = superviseService.findOne(receiver.getFid()).getSupervise();
			if(cache.containsKey(supervise.getFid()))return;
			cache.put(supervise.getFid(), supervise);
		}
	}
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
			User sender, Integer triggerType, FiscalAccount account, int tag) throws Exception{
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
	public void sendMessage(String busClass, String busScene, Map<String,Object> paramMap,User receiver, User sender, 
			Integer triggerType, FiscalAccount account, int tag) throws Exception{
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
						/*Class clazz = Class.forName(type);
						Method method = ReflectionUtils.getDeclaredMethod(clazz, getGetterMethod(objField));
						
						if(method==null){
							logger.error("模板参数错误:"+paramater.getFid()+":"+objField+":找不到对应的getter");
						}
						Object fieldObj = method.invoke(data);*/
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

			String content = cn.fooltech.fool_ops.utils.StringUtils.composeMessage(template.getFormatPattern(), cache);
			String title = cn.fooltech.fool_ops.utils.StringUtils.composeMessage(template.getTitle(), cache);


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
			
			messageService.save(entity);
			
			for(MessageParamater p:msgParamaters){
				p.setMessage(entity);
				paramaterRepository.save(p);
			}
			
//			if(entity.getSendType().equals(MessageTemplate.SEND_TYPE_PHONE) && SendUtils.isPhoneMsgSupport(tag)){
//				Sender sendFacty = senderGenService.getSuitSender(entity.getSendType());
//				sendFacty.send(entity, 3, true);
//			}else if(entity.getSendType().equals(MessageTemplate.SEND_TYPE_EMAIL) && SendUtils.isEmailSupport(tag)){
//				Sender sendFacty = senderGenService.getSuitSender(entity.getSendType());
//				sendFacty.send(entity, 3, true);
//			}
		}
	}
	
	/**
	 * 通过配置查找类型为"事件"的消息接收人
	 * @param task
	 * @param triggerType
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<User> queryTaskReceiver(Task task, Integer triggerType){
		List<MsgWarnSetting> settings=repository.query(task.getTaskLevel().getLevel(), triggerType, MsgWarnSetting.TYPE_TASK, task.getOrg().getFid());
		Map<String, User> receiverMap = Maps.newHashMap();
		
		for(MsgWarnSetting set:settings){
			switch (set.getSendType()) {
			case MsgWarnSetting.SEND_TYPE_FQR://发起人
				addReceiver(task.getInitiater(), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_ZRR://责任人
				addReceiver(task.getPrincipaler(), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_CBR://承办人
				addReceiver(task.getUndertaker(), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_GZR://关注人
				logger.warn("关注人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_PJR://评价人
				logger.warn("评价人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_LYR://留言人
				logger.warn("留言人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_PFR://评分人
				logger.warn("评分人功能待完善");
				break;
			case MsgWarnSetting.SEND_TYPE_BMJDR://部门监督人
				addReceiver(superviseService.queryBySettingId(set.getFid(), Supervise.TYPE_DEPT), receiverMap);
				break;
			case MsgWarnSetting.SEND_TYPE_GSJDR://公司监督人
				addReceiver(superviseService.queryBySettingId(set.getFid(), Supervise.TYPE_ORG), receiverMap);
				break;
			default:
				break;
			}
		}
		return Lists.newArrayList(receiverMap.values());
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
	 * 获取重发天数
	 * @param task
	 * @param triggerType
	 * @return
	 */
	public Integer getRetryDays(Task task, Integer triggerType){

		int taskLevel = task.getTaskLevel().getLevel();
		int type = MsgWarnSetting.TYPE_TASK;
		String orgId = task.getOrg().getFid();

		return repository.getRetryDays(taskLevel, triggerType, type, orgId);
	}

	/**
	 * 获取提前提醒的天数
	 * @param task
	 * @param triggerType
	 * @return
	 */
	public Integer getForwardDays(Task task, Integer triggerType, String busScene){

		int taskLevel = task.getTaskLevel().getLevel();
		int type = MsgWarnSetting.TYPE_TASK;
		String orgId = task.getOrg().getFid();

		return repository.getForwardDays(taskLevel, triggerType, type, orgId, busScene);
	}
}
