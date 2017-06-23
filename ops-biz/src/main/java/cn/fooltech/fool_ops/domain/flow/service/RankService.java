package cn.fooltech.fool_ops.domain.flow.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Rank;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.RankRepository;
import cn.fooltech.fool_ops.domain.flow.vo.RankVo;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>评分网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-06-08 10:02:29
 */
@Service
public class RankService extends BaseService<Rank,RankVo,String> {
	
	private final static boolean SINGLE_RANK = true;//不允许多次评分
	
	/**
	 * 评分服务类
	 */
	@Autowired
	private RankRepository repository;
	
	/**
	 * 消息服务
	 */
	@Autowired
	private MessageService msgService;
	
	/**
	 * 消息配置
	 */
	@Autowired
	private MsgWarnSettingService msgSettingService;
	
	/**
	 * 事件服务类
	 */
	@Autowired
	private TaskService taskService;
	
	/**
	 * 计划服务类
	 */
	@Autowired
	private PlanService planService;
	
	/**
	 * 根据业务ID和类型获取评分
	 * @param vo
	 * @return
	 */
	public Rank findOneByTypeAndBusinessId(RankVo vo){
		Assert.notNull(vo.getType());
		Assert.notNull(vo.getBusinessId());
		return repository.findOneByTypeAndBusinessId(vo.getType(), vo.getBusinessId());
	}
	
	/**
	 * 根据业务id获取评分vo
	 * @param busId
	 * @return
	 */
	public RankVo findByBusinessId(String busId) {
		List<Rank> rankList = repository.findByBusinessId(busId);
		RankVo vo = null;
		if(rankList != null) {
			Rank rank = rankList.get(0);
			vo = getVo(rank);
		}
		return vo;
	}
	
	/**
	 * 单个评分实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public RankVo getVo(Rank entity){
		if(entity == null)
			return null;
		RankVo vo = new RankVo();
		vo.setType(entity.getType());
		vo.setBusinessId(entity.getBusinessId());
		vo.setRank(entity.getRank());
		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		vo.setFid(entity.getFid());
		vo.setComment(entity.getComment());
		vo.setCreatorName(entity.getCreator().getUserName());
		return vo;
	}
	
	/**
	 * 删除评分<br>
	 */
	public RequestResult delete(String fid){
		repository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取评分信息
	 * @param fid 评分ID
	 * @return
	 */
	public RankVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(repository.findOne(fid));
	}
	

	/**
	 * 新增评分
	 * @param vo
	 * @throws Exception 
	 */
	public RequestResult save(RankVo vo) throws Exception {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		User currentUser = SecurityUtil.getCurrentUser();
		
		if(SINGLE_RANK){
			Rank rank = repository.findOneByTypeAndBusinessId(vo.getType(), vo.getBusinessId(), currentUser.getFid());
			if(rank!=null)return buildFailRequestResult("不能重复评分");
		}
		
		short type = vo.getType();
		String businessId = vo.getBusinessId();
		
		Rank entity = new Rank();
		entity.setCreateTime(DateUtilTools.now());
		entity.setCreator(currentUser);
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		
		entity.setType(type);
		entity.setBusinessId(businessId);
		entity.setRank(vo.getRank());
		entity.setComment(vo.getComment());
		repository.save(entity);
		
		if(type == Rank.TYPE_TASK) {
			Task task = taskService.get(entity.getBusinessId());
			sendTaskMessage(task, entity, currentUser, Message.TRIGGER_TYPE_SCORE);
		}else if(type == Rank.TYPE_PLAN) {
			Plan plan = planService.get(entity.getBusinessId());
			sendPlanMessage(plan, entity, currentUser, Message.TRIGGER_TYPE_SCORE);
		}

		return buildSuccessRequestResult();
	}
	
	/**
	 * 发送事件评分消息
	 * @throws Exception 
	 */
	public void sendTaskMessage(Task task, Rank rank, User sender, Integer triggerType) throws Exception {
		String busClass = Task.class.getName();
		String busScene = task.getStatus()+"";
		List<User> receivers = msgSettingService.queryTaskReceiver(task, triggerType);
		Map<String, Object> paramMap = buildMap(task, rank, sender);
		int tag = SendUtils.getTag(task.getSendPhoneMsg(), task.getSendEmail());
		msgService.sendMessage(busClass, busScene, paramMap, receivers, sender, triggerType, task.getFiscalAccount(), tag);
	}
	
	/**
	 * 发送计划评分消息
	 * @param plan
	 * @param rank
	 * @param sender
	 * @param triggerType
	 * @throws Exception 
	 */
	public void sendPlanMessage(Plan plan, Rank rank, User sender, Integer triggerType) throws Exception {
		String busClass = Plan.class.getName();
		String busScene = plan.getStatus()+"";
		List<User> receivers = msgSettingService.queryPlanReceiver(plan, triggerType);
		Map<String, Object> paramMap = buildMap(plan, rank,sender);
		int tag = SendUtils.getTag(plan.getSendPhoneMsg(), plan.getSendEmail());
		msgService.sendMessage(busClass, busScene, paramMap, receivers, sender, triggerType, plan.getFiscalAccount(), tag);
	}
	
	/**
	 * 创建发送事件消息的Map
	 * @param task 事件
	 * @param sender 发送人
	 * @return
	 */
	public Map<String, Object> buildMap(Task task, Rank rank, User sender){
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("user", sender);
		paramMap.put("task", task);
		paramMap.put("rank", rank);
		paramMap.put("plan", task.getPlan());
		return paramMap;
	}
	
	/**
	 * 创建发送计划消息的Map
	 * @param plan
	 * @param sender
	 * @return
	 */
	public Map<String, Object> buildMap(Plan plan, Rank rank, User sender){
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("user", sender);
		paramMap.put("rank", rank);
		paramMap.put("plan", plan);
		return paramMap;
	}

	@Override
	public CrudRepository<Rank, String> getRepository() {
		return repository;
	}
	/**
	 * 判断用户是否对该计划或事件评过分
	 * @param type
	 * @param businessId 计划或事件的ID
	 * @param userId 用户ID
	 * @return
	 */
	public boolean isRanked(Short type, String businessId, String userId) {
		Long count = repository.isRanked(type, businessId, userId);
		return count>0;
	}
}
