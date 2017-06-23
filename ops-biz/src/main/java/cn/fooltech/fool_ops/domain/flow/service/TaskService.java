package cn.fooltech.fool_ops.domain.flow.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.flow.entity.*;
import cn.fooltech.fool_ops.domain.flow.repository.TaskBillRepository;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.repository.TaskRepository;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.domain.flow.vo.TemplateData;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.entity.MessageParamater;
import cn.fooltech.fool_ops.domain.message.repository.MessageParamaterRepository;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplate;
import cn.fooltech.fool_ops.domain.message.template.entity.MessageTemplateParamater;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateParamaterService;
import cn.fooltech.fool_ops.domain.message.template.service.MessageTemplateService;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.rate.entity.RateMember;
import cn.fooltech.fool_ops.domain.rate.service.RateMemberService;
import cn.fooltech.fool_ops.domain.rate.vo.PlanYieldRateVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserSecurityLevelService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;

/**
 * <p>事件网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
@Service
public class TaskService extends BaseService<Task, TaskVo, String>{
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	/**
	 * 计划
	 */
	@Autowired
	private PlanService planService;
	
	@Autowired
	private MsgWarnSettingService msgSettingService;
	
	/**
	 * 附件服务类
	 */
	@Autowired
	protected AttachService attachService;
	
	
	/**
	 * 事件服务类
	 */
	@Autowired
	private TaskRepository repository;
	
	/**
	 * 事件分类服务类
	 */
	@Autowired
	private TaskTypeService taskTypeService;
	
	/**
	 * 事件级别服务类
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	
	/**
	 * 保密级别
	 */
	@Autowired
	private SecurityLevelService securityLevelService;
	
	/**
	 * 用户服务类
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 流程操作记录服务类
	 */
	@Autowired
	private FlowOperationRecordService operationRecordService;
	
	/**
	 * 计划模板明细服务类 
	 */
	@Autowired
	private PlanTemplateDetailService planTemplateDetailService;
	
	/**
	 * 消息服务类
	 */
	@Autowired
	private MessageService messageService;

	@Autowired
	private TemplateDataService templateDataService;
	
	/**
	 * 仓储单据服务类
	 */
	@Resource(name="ops.WarehouseBillService")
	private WarehouseBillService warehouseBillService;
	
	/**
	 * 执行人效益类
	 */
	@Autowired
	private RateMemberService rmService;
	
	/**
	 * 流程附件
	 */
	@Autowired
	private FlowAttachService flowAttachService;
	
	
	/**
	 * 用户安全级别服务类
	 */
	@Autowired
	private UserSecurityLevelService uslService;
	
	/**
	 * 收付款服务类
	 */
	@Autowired
	private PaymentBillService paybillService;

	/**
	 * 费用单服务类
	 */
	@Autowired
	private CostBillService costbillService;
	
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
	
	
	@Autowired
	private MessageParamaterRepository paramaterRepository;

	@Autowired
	private TaskPlantemplateService taskPlantemplateService;

	@Autowired
	private TaskPlantemplateDetailService tptdService;
	
	@Autowired
	private CapitalPlanService capitalPlanService;
	
	@Autowired
	private CapitalPlanDetailService capitalPlanDetailService;

	@Autowired
	private TaskBillRepository taskBillRepo;
	
//	/**
//	 * 发送器生成服务
//	 */
//	@Autowired
//	private SenderGeneratorService senderGenService;
	
	public static final String CLASS_NAME = Task.class.getName();
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 流程收益率分析(事件总数)
	 */
	public long count(String planId) {
		Query query = entityManager.createNativeQuery(
				"call p_task_analysis(:planId)");
		query.setParameter("planId", planId == null ? "" : planId);
		List list = query.getResultList();
		return ((BigInteger) list.get(0)).longValue();
	}

	/**
	 * 查找详情列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TaskVo> queryDetail(String planId) {
		Query query = entityManager.createNativeQuery(
				"call p_task_analysis(:planId)");
		query.setParameter("planId", planId ==null ? "" : planId);
		List list = query.getResultList();
		if(list.size()>0){
			return getVos2(list);
		}else{
			return null;
		}
	}
	/**
	 * 获得Vo的list
	 */
	public List<TaskVo> getVos2(List<Object[]> list) {
		List<TaskVo> vos = Lists.newArrayList();
		if (list != null){
			for (Object[] objects : list) {
				TaskVo vo = getVo2(objects);
				if(vo!=null){
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	/**
	 * 获得Vo
	 */
	public TaskVo getVo2(Object[] obj) {
		if (obj == null)
			return null;
		TaskVo vo = new TaskVo();
		try {
			vo.setFid(obj[0]!=null?obj[0].toString():"");//事件ID
			vo.setName(obj[1]!=null?obj[1].toString():"");//事件名称
			vo.setAntipateStartTime(obj[2]!=null?obj[2].toString():"");//计划结束日期
			vo.setOriginalEndTime(obj[3]!=null?DateUtils.getDateFromString(obj[3].toString()):null);//原计划完成日期
			vo.setActualStartTime(obj[4]!=null?DateUtils.getDateFromString(obj[4].toString()):null);//实际开始时间
			vo.setActualEndTime(obj[5]!=null?DateUtils.getDateFromString(obj[5].toString()):null);//实际完成时间
			vo.setStatus(obj[6]!=null?Integer.valueOf(obj[6].toString()):null);//状态
			vo.setFextensionDays(obj[7]!=null?obj[7].toString():"");//延期天数
			vo.setDelayedTime(obj[8]!=null?Integer.valueOf(obj[8].toString()):null);//延期次数
			vo.setContractorsEfficiency(obj[9]!=null?obj[9].toString():"");//承办效率
			vo.setPrincipalerName(obj[10]!=null?obj[10].toString():"");//责任人
			vo.setUndertakerName(obj[11]!=null?obj[11].toString():"");//承办人
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取事件信息
	 * @param fid 事件ID
	 * @return
	 */
	public TaskVo getById(String fid){
		Task entity = repository.findOne(fid);
		return getVo(entity, true);
	}
	
	/*======================================查询计划下的所有事件 start ===============================================*/
	
	/**
	 * 查询计划下的所有事件（树状列表结构）
	 * @param planId
	 * @return
	 */
	public List<TaskVo> queryAllByPlan(String planId){
		List<Task> resultList = new ArrayList<Task>();
		Task rootTask = getRootTask(planId);
		List<Task> taskPool = repository.getTaskByPlan(planId);
		
		recursion(rootTask, taskPool, resultList);
		return getVos(resultList);
	}

	/**
	 * 根据计划ID查找所有的事件
	 * @param planId
	 * @return
	 */
	public List<Task> queryAllRootTasks(String planId){
		Task rootTask = getRootTask(planId);
		return Lists.newArrayList(rootTask.getChilds());
	}

	/**
	 * 根据计划ID查找所有的事件（树状结构）
	 * @param planId
	 */
	public List<TaskVo> queryTreeByPlanId(String planId){
		List<Task> resultList = new ArrayList<Task>();
		Task rootTask = getRootTask(planId);
		List<Task> taskPool = repository.getTaskByPlan(planId);

		if(rootTask != null && CollectionUtils.isNotEmpty(taskPool)) {
			recursion(rootTask, taskPool, resultList);
			List<TaskVo> taskVoList = getVos(resultList);
        	return treeVoList(taskVoList.get(0), taskVoList);
		}
		return new ArrayList<TaskVo>();

	}
	
	/**
	 * 递归
	 * @param task
	 * @param taskPool
	 * @param resultList
	 * @return
	 */
	private void recursion(Task task, List<Task> taskPool, List<Task> resultList){
		resultList.add(task);
		List<Task> childs = getChidls(task, taskPool);
		for(Task child : childs){
			recursion(child, taskPool, resultList);
		}
	}
	
	/**
	 * 根据父事件，获取子事件
	 * @param parentTask 父事件
	 * @param taskPool 事件集合
	 * @return
	 */
	private List<Task> getChidls(Task parentTask, List<Task> taskPool){
		List<Task> childs = new ArrayList<Task>(0);
		for(Task task : taskPool){
			Task parent = task.getParent();
			if(parent != null && parent.equals(parentTask)){
				childs.add(task);
			}
		}
		return sortTask(childs);
	}
	
	/**
	 * 事件排序
	 * @param list
	 * @return
	 */
	private List<Task> sortTask(List<Task> list){
		if(CollectionUtils.isNotEmpty(list)){
			Collections.sort(list, new Comparator<Task>() {
				
				@Override
				public int compare(Task task1, Task task2) {
					//按编号排序
					return task1.getAntipateStartTime().compareTo(task2.getAntipateStartTime());
				}
				
			});
		}
		return list;
	}
	/*======================================查询计划下的所有事件 end ===============================================*/
	
	/*======================================通过计划模板添加事件 start ===============================================*/
	
	/**
	 * 通过计划模板添加事件
	 * @param planId 计划ID
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	@Transactional
	public RequestResult addTaskByPlanTemplate(String planId, String planTemplateId){
		Plan plan = planService.get(planId);

		long count = repository.countByPlanId(planId);//通过模板新增应该只有一个根事件
		if(count>1)return buildFailRequestResult(ErrorCode.FLOW_ALREADY_GENED,"事件已生成，请刷新页面再试");
		
		PlanTemplateDetail max = planTemplateDetailService.getCodeMaxLengthByPlanTemplate(planTemplateId);
		int length = max.getNumber().toString().length();
		
		PlanTemplateDetail rootTemplateDetail = planTemplateDetailService.getRootByPlanTemplate(planTemplateId);
		List<PlanTemplateDetail> detailsPool = planTemplateDetailService.queryByTemplateId(planTemplateId);
		Set<String> codes = Sets.newHashSet();
		recursionAddByTemplate(plan, null, rootTemplateDetail, detailsPool, codes, length);
		return new RequestResult();
	}
	
	
	/**
	 * 根据计划模板，递归添加事件
	 * @param plan 计划
	 * @param parentTask 父事件
	 * @param templateDetail 模板明细
	 * @param detailsPool 模板明细池
	 */
	private void recursionAddByTemplate(Plan plan, Task parentTask, PlanTemplateDetail templateDetail, 
			List<PlanTemplateDetail> detailsPool, Set<String> codes, int length){
		Task task = getTaskByPlanTemplate(plan, parentTask, templateDetail, codes, length);
		repository.save(task);

		//维护父节点标识
		if(parentTask!=null && parentTask.getTreeFlag()==Task.TREE_FLAG_CHILD){
			parentTask.setTreeFlag(Task.TREE_FLAG_PARENT);
			repository.save(parentTask);
		}
		
		List<PlanTemplateDetail> childs = getChildTemplateDetail(templateDetail, detailsPool);
		for(PlanTemplateDetail child : childs){
			recursionAddByTemplate(plan, task, child, detailsPool, codes, length);
		}
	}
	
	/**
	 * 获取子计划模板明细
	 * @param parentDetail 父模板明细
	 * @param detailsPool 模板池
	 * @return
	 */
	private List<PlanTemplateDetail> getChildTemplateDetail(PlanTemplateDetail parentDetail, List<PlanTemplateDetail> detailsPool){
		List<PlanTemplateDetail> childs = new ArrayList<PlanTemplateDetail>();
		for(PlanTemplateDetail detail : detailsPool){
			PlanTemplateDetail parent = detail.getParent();
			if(parent != null && parentDetail.equals(parent)){
				childs.add(detail);
			}
		}
		return childs;
	}
	 
	/**
	 * 根据计划模板明细获取事件
	 * @param plan
	 * @param parentTask
	 * @param templateDetail
	 * @return
	 */
	private Task getTaskByPlanTemplate(Plan plan, Task parentTask, PlanTemplateDetail templateDetail, 
			Set<String> codes, int length){

		//排除重复
		String code = templateDetail.getNumber()*100 + "";
		code = StringUtils.leftPad(code, length+2, "0");
		int i = 1;
		while(codes.contains(code)){
			code = (templateDetail.getNumber()*100+i) + "";
			code = StringUtils.leftPad(code, length+2, "0");
			i++;
		}
		codes.add(code);
		
		Task task = new Task();
		
		task.setCode(code);
		task.setName(templateDetail.getTaskName());
		task.setBillId(templateDetail.getBillId());
		task.setBillType(templateDetail.getBillType());
		task.setNumber(templateDetail.getNumber().intValue());
		
		task.setPlan(plan);
		task.setParent(parentTask);
		task.setDept(templateDetail.getDept());
		task.setDescribe(templateDetail.getDescribe());
		task.setTaskLevel(templateDetail.getTaskLevel());
		task.setPrincipaler(templateDetail.getPrincipal());
		task.setUndertaker(templateDetail.getUndertaker());
		task.setOrg(SecurityUtil.getCurrentOrg());
		task.setCreator(SecurityUtil.getCurrentUser());
		task.setFiscalAccount(SecurityUtil.getFiscalAccount());
		task.setCreateTime(Calendar.getInstance().getTime());
		task.setUpdateTime(Calendar.getInstance().getTime());
		task.setSecurityLevel(templateDetail.getSecurityLevel());
		
		if(parentTask == null){
			//根事件
			task.setLevel(0);
			task.setAntipateEndTime(plan.getAntipateEndTime());
			task.setAntipateStartTime(plan.getAntipateStartTime());
			task.setOriginalEndTime(plan.getAntipateEndTime());
		}
		else{
			int level = parentTask.getLevel() + 1;
			task.setLevel(level);
			
			//预计开始时间、预计结束时间
			Date antipateStartTime = parentTask.getAntipateStartTime();
			Date startTime = DateUtil.addDays(antipateStartTime, templateDetail.getPreDays());
			Date endTime = DateUtil.addDays(startTime, templateDetail.getDays()-1);
			task.setAntipateStartTime(startTime);
			task.setAntipateEndTime(endTime);
			task.setOriginalEndTime(endTime);
			
			//父ID路径、发起人
			if(level == 1 || parentTask.getParent()==null){
				task.setParentIds(parentTask.getFid());
				task.setInitiater(plan.getInitiater());
			}
			else{
				String parentIds = parentTask.getParentIds()==null?"":parentTask.getParentIds();
				parentIds = parentIds+ "," + parentTask.getFid();
				task.setParentIds(parentIds);
				task.setInitiater(parentTask.getUndertaker());
			}
		}
		return task;
	}
	
	/*======================================通过计划模板添加事件 end ===============================================*/
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	public RequestResult save(TaskVo vo) throws Exception{
		String fid = vo.getFid();
		String planId = vo.getPlanId();
		String parentId = vo.getParentId();
		String code = vo.getCode();
		String name = vo.getName();
		String amount = vo.getAmount();
		Integer number = vo.getNumber();
		String describe = vo.getDescribe();
		String billId = vo.getBillId();
		Integer billType = vo.getBillType();
		Integer referenceType = vo.getReferenceType();
		Date antipateStartTime = DateUtils.getDateFromString(vo.getAntipateStartTime());
		Date antipateEndTime = DateUtils.getDateFromString(vo.getAntipateEndTime());
		Date actualStartTime = vo.getActualStartTime();
		Date actualEndTime = vo.getActualEndTime();
		String taskTypeId = vo.getTaskTypeId();
		String taskLevelId = vo.getTaskLevelId();
		String securityLevelId = vo.getSecurityLevelId();
		String undertakerId = vo.getUndertakerId();
		String principalerId = vo.getPrincipalerId();
		Date now = Calendar.getInstance().getTime();
		Integer sendPhoneMsg = vo.getSendPhoneMsg();
		Integer sendEmail = vo.getSendEmail();
		String oldUndertakerId = null; //旧的事件承办人ID
		
		Assert.isTrue(StringUtils.isNotBlank(taskLevelId), "事件级别ID不能为空!");
		
		Plan plan = planService.get(planId);
		
		if(repository.isCodeExist(planId, code, fid)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "事件编号重复!");
		}
		if(repository.isNameExist(planId, name, fid)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "事件名称重复!");
		}
		
		Task entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new Task();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setOriginalEndTime(antipateEndTime);
			
			//添加二级以上事件
			if(StringUtils.isNotBlank(parentId)){
				Task parent = repository.findOne(parentId);
				Integer parentStatus = parent.getStatus();
				if(parentStatus == Task.STATUS_FINISHED || parentStatus == Task.STATUS_STOPED){
					return new RequestResult(RequestResult.RETURN_FAILURE, "已完成或已终止的事件，不允许添加子事件!");
				}
				
				if(parent.getAssignFlag() == Task.ASSIGN_FLAG_YES){
					return new RequestResult(RequestResult.RETURN_FAILURE, "父事件确认分派后，不允许再添加子事件!");					
				}
			}
		}
		else{
			entity = repository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			User undertaker = entity.getUndertaker();
			if(undertaker != null){
				oldUndertakerId = undertaker.getFid();
			}
			
			List<Integer> statusList = Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART);
			RequestResult checkResult = verifyByRule(entity, entity.getInitiater(), statusList, Task.ASSIGN_FLAG_NO);
			if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
				return checkResult;
			}
		}
		entity.setCode(code);
		entity.setName(name);
		entity.setNumber(number);
		entity.setUpdateTime(now);
		entity.setDescribe(describe);
		entity.setBillId(billId);
		entity.setBillType(billType);
		entity.setReferenceType(referenceType);
		entity.setAntipateStartTime(antipateStartTime);
		entity.setAntipateEndTime(antipateEndTime);
		entity.setActualStartTime(actualStartTime);
		entity.setActualEndTime(actualEndTime);
//		entity.setOriginalEndTime(antipateEndTime);
		entity.setAmount(NumberUtil.toBigDeciaml(amount));
		entity.setPlan(plan);
		
		entity.setSendPhoneMsg(sendPhoneMsg);
		entity.setSendEmail(sendEmail);
		
		//父事件
		if(StringUtils.isNotBlank(parentId)){
			//处理二级以上事件
			Task parentTask = repository.findOne(parentId);
			Assert.notNull(parentTask, "父事件不允许为空!");
			entity.setParent(parentTask);
			entity.setLevel(parentTask.getLevel() + 1);
			entity.setInitiater(parentTask.getUndertaker());
			
			//父ID路径
			String parentIds = parentTask.getParentIds()==null?"":parentTask.getParentIds();
			parentIds = parentIds + "," + parentTask.getFid();
			entity.setParentIds(parentIds);
			entity.setParentIds(parentIds);
		}
		else{
			//处理一级事件
			Task rootTask = getRootTask(planId);
			if(rootTask == null){
				rootTask = saveRootTask(plan);
			}
			entity.setLevel(1);
			entity.setParent(rootTask);
			entity.setInitiater(plan.getInitiater());
			entity.setParentIds(rootTask.getFid());
			

		}
		//事件分类
		if(StringUtils.isNotBlank(taskTypeId)){
			entity.setTaskType(taskTypeService.get(taskTypeId));
		}
		//事件级别
		if(StringUtils.isNotBlank(taskLevelId)){
			entity.setTaskLevel(taskLevelService.get(taskLevelId));
		}
		//保密级别
		if(StringUtils.isNotBlank(securityLevelId)){
			entity.setSecurityLevel(securityLevelService.get(securityLevelId));
		}
		//承办人
		if(StringUtils.isNotBlank(undertakerId)){
			entity.setUndertaker(userService.get(undertakerId));
		}
		//责任人
		if(StringUtils.isNotBlank(principalerId)){
			entity.setPrincipaler(userService.get(principalerId));
		}
		//子事件的发起人
		if(StringUtils.isNotBlank(oldUndertakerId)){
			if(!oldUndertakerId.equals(undertakerId)){
				User initiater = userService.get(undertakerId);
				List<Task> childs = repository.getChilds(entity.getFid());
				for(Task child : childs){
					child.setInitiater(initiater);
					repository.save(child);
				}
			}
		}
		
		if(StringUtils.isBlank(fid)){
			repository.save(entity);
			updatePlanAmount(plan);
		}
		else{
			repository.save(entity);
			updatePlanAmount(plan);
		}
		RequestResult result = new RequestResult();
		result.setData(entity.getFid());

		//修改则删除旧附件数据再保存新增
		if(!Strings.isNullOrEmpty(vo.getFid())){
			flowAttachService.deleteTaskAttach(entity.getFid());
		}

		//插入流程附件表
		String attachIds = vo.getAttachIds();
		flowAttachService.saveTaskAttach(entity, attachIds);

		//维护父节点标识
		Task parent = entity.getParent();
		if(parent!=null && parent.getTreeFlag()==Task.TREE_FLAG_CHILD){
			parent.setTreeFlag(Task.TREE_FLAG_PARENT);
			repository.save(parent);
		}

		return result;
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public TaskVo getVo(Task entity){
		return getVo(entity, false);
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @param loadMore 加载更多信息
	 * @return
	 */
	public TaskVo getVo(Task entity, boolean loadMore) {
		if(entity == null){
			return null;
		}
		
		TaskVo vo = new TaskVo();
		vo.setFid(entity.getFid());
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setLevel(entity.getLevel());
		vo.setStatus(entity.getStatus());
		vo.setNumber(entity.getNumber());
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(entity.getAuditTime());
		vo.setAntipateStartTime(DateUtils.getDateString(entity.getAntipateStartTime()));
		vo.setAntipateEndTime(DateUtils.getDateString(entity.getAntipateEndTime()));
		vo.setActualStartTime(entity.getActualStartTime());
		vo.setActualEndTime(entity.getActualEndTime());
		vo.setReferenceType(entity.getReferenceType());
		vo.setOriginalEndTime(entity.getOriginalEndTime());
		vo.setAmount(NumberUtil.bigDecimalToStr(entity.getAmount()));
		vo.setRealAmount(NumberUtil.bigDecimalToStr(entity.getRealAmount()));
		vo.setDelayedEndTime(entity.getDelayedEndTime());
		vo.setRelevanceQuantity(entity.getRelevanceQuantity());
		vo.setAssignFlag(entity.getAssignFlag());
		vo.setBillType(entity.getBillType());
		vo.setSendPhoneMsg(entity.getSendPhoneMsg());
		vo.setSendEmail(entity.getSendEmail());
		
		//计划
		Plan plan = entity.getPlan();
		if(plan != null){
			vo.setPlanId(plan.getFid());
			vo.setPlanName(plan.getName());
		}
		//父事件
		Task parent = entity.getParent();
		if(parent != null){
			vo.setParentId(parent.getFid());
			vo.setParentName(parent.getName());
		}
		//事件分类
		TaskType taskType = entity.getTaskType();
		if(taskType != null){
			vo.setTaskTypeId(taskType.getFid());
			vo.setTaskTypeName(taskType.getName());
		}
		//事件级别
		TaskLevel taskLevel = entity.getTaskLevel();
		if(taskLevel != null){
			vo.setTaskLevelId(taskLevel.getFid());
			vo.setTaskLevelName(taskLevel.getName());
			vo.setTaskLevelVal(taskLevel.getLevel());
		}
		//保密级别
		SecurityLevel securityLevel = entity.getSecurityLevel();
		if(securityLevel != null){
			vo.setSecurityLevelId(securityLevel.getFid());
			vo.setSecurityLevelName(securityLevel.getName());
		}

		//发起人
		User initiater = entity.getInitiater();
		if(initiater != null){
			vo.setInitiaterId(initiater.getFid());
			vo.setInitiaterName(initiater.getUserName());
		}
		//承办人
		User undertaker = entity.getUndertaker();
		if(undertaker != null){
			vo.setUndertakerId(undertaker.getFid());
			vo.setUndertakerName(undertaker.getUserName());
		}
		//责任人
		User principaler = entity.getPrincipaler();
		if(principaler != null){
			vo.setPrincipalerId(principaler.getFid());
			vo.setPrincipalerName(principaler.getUserName());
		}
		//审核人
		User auditer = entity.getAuditer();
		if(auditer != null){
			vo.setAuditerId(auditer.getFid());
			vo.setAuditerName(auditer.getUserName());
		}
		Organization dept = entity.getDept();
		if(dept!=null){
			vo.setDeptName(dept.getOrgName());
			vo.setDeptId(dept.getFid());
		}

		//是否能添加下级事件
		vo.setSecureAddChild(checkAddChild(entity));

		//是否能展示
		vo.setSecureShow(checkSecureShow(entity));

		//是否能添加同级节点
		vo.setSecureAdd(checkAddBrother(entity));
		
		if(loadMore){
			long total = repository.countNotFinishChilds(entity.getFid());
			vo.setHasChilds(total == 0 ? 0 : 1);

			if(parent!=null){
				Date minStart = null;
				Date maxEnd = null;
				Date maxStart = null;
				Date minEnd = null;
				String parentIds = "%"+entity.getParentIds()+","+entity.getFid();

				//一级事件吗
				if(parent.getParent()==null){
					if(entity.getChilds().size()>0){
						//非叶子节点
						maxStart = repository.findMinStart(parentIds);
						minEnd = repository.findMaxEnd(parentIds);
					}
				}else{
					minStart = parent.getAntipateStartTime();
					maxEnd = parent.getAntipateEndTime();

					if(entity.getChilds().size()>0){
						//非叶子节点
						maxStart = repository.findMinStart(parentIds);
						minEnd = repository.findMaxEnd(parentIds);
					}else{
						//叶子节点
						maxStart = parent.getAntipateEndTime();
						minEnd = parent.getAntipateStartTime();
					}
				}

				vo.setStartMin(DateUtilTools.date2String(minStart));
				vo.setStartMax(DateUtilTools.date2String(maxStart));
				vo.setEndMin(DateUtilTools.date2String(minEnd));
				vo.setEndMax(DateUtilTools.date2String(maxEnd));
			}
		}

		return vo;
	}

	/**
	 * 判断事件能否添加子事件
	 * @param task
	 * @return
	 */
	@Transactional(readOnly = true)
	private int checkAddChild(Task task){

		//计划草稿都能添加
		if(task.getPlan().getStatus()!=Plan.STATUS_DRAFT) {
			if(!checkValidUser(task.getUndertaker())){
				return TaskVo.SECURE_NOTSHOW;
			}else if(task.getStatus()!=Task.STATUS_DRAFT && task.getStatus()!=Task.STATUS_DELAYED_UNSTART){////当前事件状态不为0或4
				return TaskVo.SECURE_NOTSHOW;
			}else if(task.getAssignFlag()==Task.ASSIGN_FLAG_YES){//当前事件已分派完成
				return TaskVo.SECURE_NOTSHOW;
			}else if(task.getParent()!=null && task.getParent().getAssignFlag()==Task.ASSIGN_FLAG_NO){//父事件未分派完成
				return TaskVo.SECURE_NOTSHOW;
			}
		}

		return TaskVo.SECURE_SHOW;
	}

	/**
	 * 判断事件能否添加同级事件
	 * @param task
	 * @return
	 */
	@Transactional(readOnly = true)
	private int checkAddBrother(Task task){

		Task parent = task.getParent();
		if(parent==null)return TaskVo.SECURE_NOTSHOW;
		if(!checkValidUser(parent.getUndertaker())){//当前用户不是父事件的承办人
			return TaskVo.SECURE_NOTSHOW;
		}else if(parent.getAssignFlag()==Task.ASSIGN_FLAG_YES){//父事件已分派完成
			return TaskVo.SECURE_NOTSHOW;
		}
		return TaskVo.SECURE_SHOW;

	}

	/**
	 * 判断事件能否展示
	 * @param task
	 * @return
	 */
	@Transactional(readOnly = true)
	private int checkSecureShow(Task task){

		//保密级别
		SecurityLevel securityLevel = task.getSecurityLevel();
		if(securityLevel != null){

			Integer level = uslService.getLevel(SecurityUtil.getCurrentUserId());
			if(level==null){
				return TaskVo.SECURE_SHOW;
			}else if(level!=null && securityLevel.getLevel()<=level){
				return TaskVo.SECURE_SHOW;
			}else{
				return TaskVo.SECURE_NOTSHOW;
			}
		}else{
			return TaskVo.SECURE_SHOW;
		}
	}

	/**
	 * 将事件列表转换成树状结构
	 * @param parent
     * @param taskVoList 事件列表
	 */
	private List<TaskVo> treeVoList(TaskVo parent, List<TaskVo> taskVoList){
        findChildren(parent, taskVoList);

        List<TaskVo> newVoList = Lists.newArrayList();
		newVoList.add(parent);
		return newVoList;
	}

    /**
     * 查找儿子
     * @param parent
     * @param taskVoList 事件列表
     */
    private void findChildren(TaskVo parent, List<TaskVo> taskVoList){
        if(CollectionUtils.isEmpty(taskVoList))return;

        List<TaskVo> childrenList = Lists.newArrayList();
        for (TaskVo vo: taskVoList) {
            if(parent.getFid().equals(vo.getParentId())){
                childrenList.add(vo);
            }
        }
        parent.setChildren(childrenList);

        for (TaskVo vo: childrenList) {
            findChildren(vo, taskVoList);
        }
    }

	/**
	 * 分派任务
	 * @param fid 需要确认分派完成的事件
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult assignTask(String fid) throws Exception{
		Task task = repository.findOne(fid);
		if(task.getAssignFlag() == Task.ASSIGN_FLAG_YES){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已确认分派的事件不允许再次分派!");
		}
		
		RequestResult checkResult = verifyByRule(task, task.getUndertaker(), Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART), Task.ASSIGN_FLAG_YES);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		messageService.oper(CLASS_NAME, String.valueOf(task.getStatus()), task.getFid());
		
		List<Task> childs = repository.getChilds(task.getFid());
		for(Task child : childs){
			sendMessage(child, child.getUndertaker(), Message.TRIGGER_TYPE_NEW);
			
			FlowOperationRecord operationRecord = createOperationRecord(child, Message.TRIGGER_TYPE_NEW);
			operationRecordService.save(operationRecord);
		}
		
		task.setAssignFlag(Task.ASSIGN_FLAG_YES);
		if(task.getStatus()==Task.STATUS_DRAFT){
			task.setStatus(Task.STATUS_EXECUTING);
		}else if(task.getStatus()==Task.STATUS_DELAYED_UNSTART){
			task.setStatus(Task.STATUS_DELAYED_UNFINISH);
		}
		repository.save(task);
		//维护执行效益
		rmService.startExcute(task);

		return new RequestResult();
	}
	
	/**
	 * 办理开始
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult startExcute(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		Date now = Calendar.getInstance().getTime();
		
		RequestResult checkResult = verifyByRule(task, task.getUndertaker(), Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART), Task.ASSIGN_FLAG_YES);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		//检测前置事件关联
		RequestResult relevanceResult = checkStartExcuteForRelevance(task);
		if(relevanceResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return relevanceResult;
		}
		
		messageService.oper(CLASS_NAME, String.valueOf(task.getStatus()), task.getFid());
		
		//维护当前事件
		if(DateUtil.compareDate(now, task.getAntipateEndTime()) <= 0){
			task.setStatus(Task.STATUS_EXECUTING);
		}
		else{
			task.setStatus(Task.STATUS_DELAYED_UNFINISH);
		}
		task.setActualStartTime(now);
		repository.save(task);

		//维护所有父事件
		String parentIds = task.getParentIds();
		if(StringUtils.isNotBlank(parentIds)){
			String[] idArray = parentIds.split(",");
			for(String parentId : idArray){
				Task parentTask = repository.findOne(parentId);
				if(parentTask != null && parentTask.getActualStartTime() == null){
					if(DateUtil.compareDate(now, parentTask.getAntipateEndTime()) <= 0){
						parentTask.setStatus(Task.STATUS_EXECUTING);
					}
					else{
						parentTask.setStatus(Task.STATUS_DELAYED_UNFINISH);
					}
					parentTask.setActualStartTime(now);
					repository.save(parentTask);
				}
			}
		}
		
		//维护计划
		Plan plan = task.getPlan();
		if(plan.getActualStartTime() == null){
			if(DateUtil.compareDate(now, plan.getAntipateEndTime()) <= 0){
				plan.setStatus(Plan.STATUS_EXECUTING);
			}
			else{
				plan.setStatus(Plan.STATUS_DELAYED);
			}
			plan.setActualStartTime(Calendar.getInstance().getTime());
			planService.save(plan);
		}
		
		
		sendMessage(task, task.getUndertaker(), Message.TRIGGER_TYPE_EXECUTE_START);
		
		FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_EXECUTE_START);
		operationRecordService.save(operationRecord);
		//维护执行效益
		rmService.startExcute(task);
		return new RequestResult();
	}
	
	/**
	 * 办理结束
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult endExecute(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		
		RequestResult checkResult = verifyByRule(task, task.getUndertaker(), Lists.newArrayList(Task.STATUS_EXECUTING, Task.STATUS_DELAYED_UNFINISH), Task.ASSIGN_FLAG_YES);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		task.setStatus(Task.STATUS_EXCUTED_CHECK);
		task.setActualEndTime(Calendar.getInstance().getTime());
		
		String realAmount = vo.getRealAmount();
		if(!Strings.isNullOrEmpty(realAmount)){
			task.setRealAmount(new BigDecimal(vo.getRealAmount()));
		}
		repository.save(task);
		
		sendMessage(task, task.getUndertaker(), Message.TRIGGER_TYPE_EXECUTE_END);
		
		FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_EXECUTE_END);
		operationRecord.setDescribe(vo.getEndExcuteDescribe());
		operationRecordService.save(operationRecord);
		//维护执行效益
		rmService.endExcute(task);
		
		//插入流程附件表
		flowAttachService.saveTaskFlowAttach(task, operationRecord, vo.getAttachIds());
		return new RequestResult();
	}
	
	/**
	 * 审核办理<br>
	 * 0-审核通过 , 1-拒绝<br>
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult checkExcute(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		
		RequestResult checkResult = verifyByRule(task, task.getInitiater(), Lists.newArrayList(Task.STATUS_EXCUTED_CHECK), null);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		messageService.oper(CLASS_NAME, String.valueOf(task.getStatus()), task.getFid());
		
		Date antipateEndTime = DateUtil.getSimpleDate(task.getAntipateEndTime());
		Date checkDate = DateUtil.getSimpleDate(Calendar.getInstance().getTime());
		if(vo.getCheckResult() == 0){
			/*计划、事件点击完成时判断计划收付金额、单据金额、收付款金额是否相等，如果相等，直接完成，如果不相等，要填写备注才能完成*/
//			List<CapitalPlanDetail> list = capitalPlanDetailService.queryByRelation(task.getFid());
//			for (CapitalPlanDetail capitalPlanDetail : list) {
//				BigDecimal billAmount = capitalPlanDetail.getBillAmount();// 单据金额
//				BigDecimal planAmount = capitalPlanDetail.getPlanAmount();//计划收付金额
//				BigDecimal paymentAmount = capitalPlanDetail.getPaymentAmount();//收付款金额
//				String describe = vo.getDescribe();
//				if (billAmount.compareTo(planAmount) != 0 || billAmount.compareTo(paymentAmount) != 0 ) {
//					if(Strings.isNullOrEmpty(describe)){
//						return buildFailRequestResult("事件收付金额、单据金额、收付款金额不一致，请先填写计划备注!");
//					}
//				}
//			}

			task.setStatus(Task.STATUS_FINISHED);
			task.setAuditer(task.getInitiater());
			task.setAuditTime(Calendar.getInstance().getTime());
			repository.save(task);
			
			updateParentFinishStatus(task);
			
			handlePlanFinish(task);
			
			sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHECK_EXECUTE_YES);
			
			FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHECK_EXECUTE_YES, vo.getDescribe());
			operationRecordService.save(operationRecord);
			//维护执行人效益
			rmService.endExcute(task);
		}
		else{
			boolean allChildsComplete = repository.isAllChildsComplete(task.getFid());
			if(allChildsComplete){
				//解决父事件的子事件全部已经办理完成，该父事件办理审核时被拒绝，该事件不能继续被办理的问题
				Integer status = checkDate.compareTo(antipateEndTime) > 0 ? Task.STATUS_DELAYED_UNFINISH : Task.STATUS_EXECUTING;
				task.setStatus(status);
				task.setActualEndTime(null);
				task.setAssignFlag(Task.ASSIGN_FLAG_NO);
				repository.save(task);
				
				sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHECK_EXECUTE_NO);
				
				FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHECK_EXECUTE_NO, vo.getDescribe());
				operationRecordService.save(operationRecord);
			}
			else{
				Integer status = checkDate.compareTo(antipateEndTime) > 0 ? Task.STATUS_DELAYED_UNFINISH : Task.STATUS_EXECUTING;
				task.setStatus(status);
				task.setActualEndTime(null);
				repository.save(task);
				
				sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHECK_EXECUTE_NO);
				
				FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHECK_EXECUTE_NO, vo.getDescribe());
				operationRecordService.save(operationRecord);
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 申请延迟
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult applyDelay(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		//Task parentTask = task.getParent();
		
		RequestResult checkResult = verifyByRule(task, task.getUndertaker(), Lists.newArrayList(Task.STATUS_EXECUTING, Task.STATUS_DELAYED_UNFINISH), Task.ASSIGN_FLAG_YES);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		Date delayedEndTime = DateUtil.getSimpleDate(vo.getDelayedEndTime());
		if(DateUtil.compareDate(delayedEndTime, task.getAntipateEndTime()) <= 0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "延迟日期必须大于事件的预计结束日期!");
		}
		/*if(DateUtil.compareDate(delayedEndTime, parentTask.getAntipateEndTime()) > 0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "延迟日期必须小于父事件的预计结束日期!");
		}*/
		
		/*List<Task> relevanceList = operationRecordService.getBehindRelevanceTask(task.getFid());
		for(Task t : relevanceList){
			Date startDate = t.getAntipateStartTime();
			if(DateUtil.compareDate(delayedEndTime, startDate) > 0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "根据事件关联规则，延迟日期必须小于" + DateUtils.getDateString(startDate));
			}
		}*/
		
		task.setDelayedEndTime(delayedEndTime);
		repository.save(task);
		
		sendMessage(task, task.getUndertaker(), Message.TRIGGER_TYPE_DELAY);
		
		FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_DELAY, vo.getDeplayReason());
		operationRecordService.save(operationRecord);
		//插入流程附件表
		flowAttachService.saveTaskFlowAttach(task, operationRecord, vo.getAttachIds());
		
		return new RequestResult();
	}
	
	/**
	 * 审核申请延迟<br>
	 * 0-审核通过 , 1-拒绝<br>
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult checkApplyDelay(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		RequestResult checkResult = verifyByRule(task, task.getInitiater(), Lists.newArrayList(Task.STATUS_EXECUTING, Task.STATUS_DELAYED_UNFINISH), null);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		messageService.oper(CLASS_NAME, String.valueOf(task.getStatus()), task.getFid());
		
		Date delayedEndTime = DateUtil.getSimpleDate(task.getDelayedEndTime());
		//Date checkDate = DateUtil.getSimpleDate(Calendar.getInstance().getTime());
		/*if(checkDate.compareTo(delayedEndTime) > 0){
			task.setDelayedEndTime(null);
			taskService.update(task);
			return new RequestResult(RequestResult.RETURN_SUCCESS, "当前审核时间大于事件延迟后结束时间，审核超时!");
		}*/
		
		if(vo.getCheckResult() == 0){
			task.setStatus(Task.STATUS_EXECUTING);
			task.setAntipateEndTime(delayedEndTime);
			task.setDelayedEndTime(null);
			repository.save(task);
			/*cwz 2017-5-15 2210 计划流程的事件执行延迟操作时，要维护计划表tflow_plan的延迟次数字段 start*/
			Integer delayedTime = task.getDelayedTime()==null?0:task.getDelayedTime();
			task.setDelayedTime(delayedTime+1);
			repository.save(task);
			Plan plan = task.getPlan();
			if(plan!=null){
				Integer delayedTime2 = plan.getDelayedTime()==null?0:plan.getDelayedTime();
				plan.setDelayedTime(delayedTime2+1);
				planService.save(plan);
			}
			/*cwz 2017-5-15 2210 计划流程的事件执行延迟操作时，要维护计划表tflow_plan的延迟次数字段 end*/
			RateMember rm=rmService.queryByUserAndTask(task.getUndertaker().getFid(), task.getFid(), 0);
			rm.setDelayNumber((rm.getDelayNumber()+1));
			sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHECK_DELAY_YES);
			
			FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHECK_DELAY_YES, vo.getDescribe());
			operationRecordService.save(operationRecord);
		}
		else{
			task.setDelayedEndTime(null);
			repository.save(task);
			
			sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHECK_DELAY_NO);
			
			FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHECK_DELAY_NO, vo.getDescribe());
			operationRecordService.save(operationRecord);
		}
		return new RequestResult();
	}
	
	/**
	 * 终止
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult stop(TaskVo vo) throws Exception{
		Task entity = repository.findOne(vo.getFid());
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_EXECUTING, Task.STATUS_EXCUTED_CHECK, Task.STATUS_DELAYED_UNSTART, Task.STATUS_DELAYED_UNFINISH);
		
		RequestResult checkResult = verifyByRule(entity, entity.getInitiater(), statusList, Task.ASSIGN_FLAG_YES);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		//终止当前事件、所有子事件
		List<Task> taskList = repository.getAllChilds(entity.getFid());
		taskList.add(entity);
		for(Task task : taskList){
			task.setStatus(Task.STATUS_STOPED);
			/*事件点击终止时在对应的资金计划明细表对应的事件的状态改为取消，状态为审核才修改；*/
			List<CapitalPlanDetail> list = capitalPlanDetailService.queryByRelation(task.getFid());
			for (CapitalPlanDetail detail : list) {
//				capitalPlanDetailService.changeByCapitalId(detail.getId(), CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),1);
//				Integer relationSign = detail.getRelationSign();
				Integer recordStatus = detail.getRecordStatus();
				if(recordStatus!=CapitalPlan.STATUS_EXECUTING) continue;
				detail.setRecordStatus(CapitalPlan.STATUS_CANCEL);
				detail.setCancelor(SecurityUtil.getCurrentUser());
				detail.setCancelTime(new Date());
				detail.setUpdateTime(new Date());
				capitalPlanDetailService.save(detail);
			}
			repository.save(task);
			
			messageService.operAllMessage(task.getFid());
			
			sendMessage(task, entity.getInitiater(), Message.TRIGGER_TYPE_STOP);
			
			FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_STOP, vo.getDescribe());
			operationRecordService.save(operationRecord);
		}
		
		updateParentFinishStatus(entity);
		handlePlanFinish(entity);
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @param fid 事件ID
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid){
		Task entity = repository.findOne(fid);
		if(entity == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该事件不存在或已被删除!");
		}
		
		Plan plan = entity.getPlan();
		/*if(!compareUser(plan.getCreator(), getCurrentUser()) && !compareUser(plan.getInitiater(), getCurrentUser())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非计划的填制人或发起人，不允许操作!");
		}*/
		
		if(plan.getStatus() > Plan.STATUS_TO_CHECK && entity.getLevel() == 1){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计划审核通过后，不允许删除一级事件!"); 
		}
		
		List<Integer> list = Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART);
		if(!list.contains(entity.getStatus())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非草稿、已延迟且未开始办理状态的事件，不允许删除!");
		}
		
		Task parentTask = entity.getParent();
		if(parentTask.getAssignFlag() == Task.ASSIGN_FLAG_YES){
			return new RequestResult(RequestResult.RETURN_FAILURE, "上级已确认分派的事件，不允许删除!");
		}
		
		if(repository.countChilds(entity.getFid()) > 0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该事件下存在子事件，不允许删除!");			
		}

		List<TaskBill> taskBills = taskBillRepo.queryByTaskId(fid);
		if(taskBills.size()>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计划事件已关联单据，不允许删除!");		 
		}
		operationRecordService.deleteByBusId(entity.getFid());
		repository.delete(entity);

		
		updateParentFinishStatus(entity);

		return buildSuccessRequestResult();
	}
	
	/**
	 * 关联
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult relevance(TaskVo vo) throws Exception{
		Task task = repository.findOne(vo.getFid());
		RequestResult checkResult = verifyByRule(task, task.getInitiater(), null, null);
		if(checkResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return checkResult;
		}
		
		//前置事件关联ID
		String frontRelevanceIds = vo.getFrontRelevanceIds();
		
		List<FlowOperationRecord> records = operationRecordService.getByTask(task.getFid(), Message.TRIGGER_TYPE_RELEVANCE);
		operationRecordService.delete(records);
		
		FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_RELEVANCE);
		operationRecord.setFrontRelevanceId(frontRelevanceIds);
		operationRecordService.save(operationRecord);
		
		//关联数量
		int quantity = 0;
		if(StringUtils.isNotBlank(frontRelevanceIds)){
			quantity = frontRelevanceIds.split(",").length;
		}
		task.setRelevanceQuantity(quantity);
		repository.save(task);
		
		return new RequestResult();
	}
	
	/**
	 * 变更事件责任人、承办人
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public RequestResult change(TaskVo vo) throws Exception {
		String fid = vo.getFid();
		String newUndertakerId = vo.getUndertakerId();
		String principalerId = vo.getPrincipalerId();
		
		Task task = repository.findOne(fid);
		String oldUndertakerId=task.getUndertaker().getFid();
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_DRAFT, Task.STATUS_DELAYED_UNSTART,
				Task.STATUS_EXECUTING, Task.STATUS_DELAYED_UNFINISH);
		
		RequestResult verifyResult = verifyByRule(task, task.getInitiater(), statusList, Task.ASSIGN_FLAG_YES);
		if(verifyResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return verifyResult;
		}
		
		//承办人
		if(StringUtils.isNotBlank(newUndertakerId)){
			User newUndertaker = userService.get(newUndertakerId);
			
			if(newUndertaker != null){
				sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHANGE_UNDERTAKER);//发送給旧的承办人
				//事件办理中更变承办人
				if(task.getStatus()!=0&&task.getStatus()!=4){
					
				
					RateMember rm=rmService.queryByUserAndTask(oldUndertakerId, fid, 0);
					rm.setIsComplete(2);
					task.setUndertaker(newUndertaker);
					rmService.save(rm);
					repository.save(task);
					rmService.startExcute(task);
					sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHANGE_UNDERTAKER);//发送給新的承办人
				
					FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHANGE_UNDERTAKER);
					operationRecordService.save(operationRecord);
				}else{
					task.setUndertaker(newUndertaker);
					repository.save(task);
					sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHANGE_UNDERTAKER);//发送給新的承办人
					
					FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHANGE_UNDERTAKER);
					operationRecordService.save(operationRecord);
				}
				List<Task> childs = repository.getChilds(task.getFid());
				for(Task child : childs){
					child.setInitiater(newUndertaker);
					repository.save(child);
				}
			
				
			}
		}
		
		//责任人
		if(StringUtils.isNotBlank(principalerId)){
			User newPincipaler = userService.get(principalerId);
			if(newPincipaler != null){
				sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHANGE_PRINCIPALER);//发送給旧的责任人
				
				task.setPrincipaler(newPincipaler);
				repository.save(task);
				
				sendMessage(task, task.getInitiater(), Message.TRIGGER_TYPE_CHANGE_PRINCIPALER);//发送給新的责任人
				
				FlowOperationRecord operationRecord = createOperationRecord(task, Message.TRIGGER_TYPE_CHANGE_PRINCIPALER);
				operationRecordService.save(operationRecord);
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 事件审核办理、删除、终止后，更新当前事件的父事件完成状态
	 * @param task
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void updateParentFinishStatus(Task task){
		Task parentTask = task.getParent();
		if(parentTask == null){
			return;
		}
		
		//维护父事件
		if(repository.isAllChildsComplete(parentTask.getFid(), task.getFid()) && parentTask.getAssignFlag() == Task.ASSIGN_FLAG_YES){
			if(parentTask.getLevel() == 0){
				parentTask.setStatus(Task.STATUS_FINISHED);
				parentTask.setActualEndTime(Calendar.getInstance().getTime());
				repository.save(parentTask);
			}
			else{
				parentTask.setStatus(Task.STATUS_EXCUTED_CHECK);
				parentTask.setActualEndTime(Calendar.getInstance().getTime());
				repository.save(parentTask);
				
				sendMessage(parentTask, parentTask.getUndertaker(), Message.TRIGGER_TYPE_EXECUTE_END);
				
				FlowOperationRecord operationRecord = createOperationRecord(parentTask, Message.TRIGGER_TYPE_EXECUTE_END);
				operationRecordService.save(operationRecord);
			}
			//暂时不递归维护上级，否则存在问题
		}
	}
	
	/**
	 * 处理计划完成
	 * @param task
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void handlePlanFinish(Task task) throws Exception{

		//TODO 计划完成状态现在是人工点击完成，以后要怎么处理还需讨论
		//Plan plan = task.getPlan();
		//if(repository.isPlanComplete(plan.getFid(), task.getFid()) && plan.getStatus() != Plan.STATUS_DRAFT){
			//planService.sendMessage(plan, Message.TRIGGER_TYPE_TASK_ALL_COMPLETE);
		//}
	}
	
	/**
	 * 获取事件申请延迟的信息
	 * @param fid 事件ID
	 * @return
	 */
	public TaskVo getApplyDelayMsg(String fid){
		Task task = repository.findOne(fid);
		TaskVo vo = getVo(task);
		
		List<FlowOperationRecord> recordList = operationRecordService.getByTask(fid, Message.TRIGGER_TYPE_DELAY);
		if(CollectionUtils.isNotEmpty(recordList)){
			FlowOperationRecord record = recordList.get(0);
			vo.setDeplayReason(record.getDescribe());
			//TODO 附件ID
		}
		return vo;
	}
	
	/**
	 * 获取事件办理结束的信息
	 * @param fid 事件ID
	 * @return
	 */
	public TaskVo getEndExcuteMsg(String fid){
		Task task = repository.findOne(fid);
		TaskVo vo = getVo(task);
		
		List<FlowOperationRecord> recordList = operationRecordService.getByTask(fid, Message.TRIGGER_TYPE_EXECUTE_END);
		if(CollectionUtils.isNotEmpty(recordList)){
			FlowOperationRecord record = recordList.get(0);
			vo.setEndExcuteDescribe(record.getDescribe());
			//TODO 附件ID
		}
		return vo;
	}
	
	/**
	 * 根据某个事件，获取符合条件的前置关联事件
	 * @param fid
	 * @return
	 */
	public List<TaskVo> getFrontRelevanceTask(String fid){
		Task task = repository.findOne(fid);
		List<Task> entities = repository.getFrontRelevanceTask(task);
		List<TaskVo> vos = getVos(entities);
		
		//设置选中状态
		List<Task> relevancedTaskList = operationRecordService.getFrontRelevanceTask(task.getFid());
		if(CollectionUtils.isNotEmpty(relevancedTaskList)){
			for(TaskVo vo : vos){
				for(Task relevancedTask : relevancedTaskList){
					if(vo.getFid().equals(relevancedTask.getFid())){
						vo.setChecked(true);
					}
				}
			}
		}
		return vos;
	}
	
	/**
	 * 根据某个事件，获取符合条件的后置关联事件
	 * @param fid
	 * @return
	 */
	public List<TaskVo> getBehindRelevanceTask(String fid){
		Task task = repository.findOne(fid);
		List<Task> entities = repository.getBehindRelevanceTask(task);
		List<TaskVo> vos = getVos(entities);
		
		//设置选中状态
		List<Task> relevancedTaskList = operationRecordService.getBehindRelevanceTask(task.getFid());
		if(CollectionUtils.isNotEmpty(relevancedTaskList)){
			for(TaskVo vo : vos){
				for(Task relevancedTask : relevancedTaskList){
					if(vo.getFid().equals(relevancedTask.getFid())){
						vo.setChecked(true);
					}
				}
			}
		}
		return vos;
	}
	
	/**
	 * 获取所有已关联的前置事件
	 * @param fid 事件ID
	 * @return
	 */
	public List<TaskVo> getAllRelevanceTask(String fid){
		List<TaskVo> vos = Lists.newArrayList();
		
		List<FlowOperationRecord> records = operationRecordService.getByTask(fid, Message.TRIGGER_TYPE_RELEVANCE);
		if(CollectionUtils.isNotEmpty(records)){
			FlowOperationRecord record = records.get(0);
			String frontIds = record.getFrontRelevanceId();
			if(StringUtils.isNotBlank(frontIds)){
				String[] idArray = frontIds.split(","); 
				for(String id : idArray){
					vos.add(getVo(repository.findOne(id)));
				}
			}
		}
		return vos;
	}
	
	/**
	 * 校验规则
	 * @param task 事件
	 * @param operater 允许操作的用户
	 * @param statusList 允许操作的事件状态
	 * @param assignFlag 允许操作的父事件分派状态
	 * @return
	 */
	private RequestResult verifyByRule(Task task, User operater, List<Integer> statusList, Integer assignFlag){
		if(task == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "事件不存在或已被删除!");
		}
		
		Plan plan = task.getPlan();
		if(plan.getStatus() == Plan.STATUS_FINISHED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计划已完成，不允许操作!");
		}
		if(plan.getStatus() == Plan.STATUS_STOPED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计划已终止，不允许操作!");
		}
		/*if(operater != null && !compareUser(getCurrentUser(), operater)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非预期的操作者，不允许操作!");
		}*/
		if(CollectionUtils.isNotEmpty(statusList) && !statusList.contains(task.getStatus())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非预期的事件状态，不允许操作!");
		}
		
		Task parentTask = task.getParent();
		if(parentTask != null && assignFlag != null){
			if(parentTask.getAssignFlag() != assignFlag && assignFlag == Task.ASSIGN_FLAG_YES){
				return new RequestResult(RequestResult.RETURN_FAILURE, "上级未分派的事件，不允许操作!");
			}
			else if(parentTask.getAssignFlag() != assignFlag && assignFlag == Task.ASSIGN_FLAG_NO){
				return new RequestResult(RequestResult.RETURN_FAILURE, "上级已分派完成，不允许操作!");
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 根据前置事件关联，判断某事件是否可以开始办理
	 * @param task 事件
	 * @return
	 */
	private RequestResult checkStartExcuteForRelevance(Task task){
		List<String> idList = new ArrayList<String>();
		idList.add(task.getFid());
		if(StringUtils.isNotBlank(task.getParentIds())){
			idList.addAll(Arrays.asList(task.getParentIds().split(",")));
		}
		
		List<Integer> statusList = Lists.newArrayList(Task.STATUS_FINISHED, Task.STATUS_STOPED);
		for(String id : idList){
			List<Task> frontList = operationRecordService.getFrontRelevanceTask(id);
			for(Task t : frontList){
				if(!statusList.contains(t.getStatus())){
					return new RequestResult(RequestResult.RETURN_FAILURE, String.format("[%s]未结束办理，无法开始办理当前事件!", t.getName()));
				}
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 比较两个用户是否是同一个人
	 * @param first
	 * @param second
	 * @return
	 */
	private boolean compareUser(User first, User second){
		if(first == null || second == null){
			return false;
		}
		return first.getFid().equals(second.getFid());
	}
	
	/**
	 * 检查当前用户是否合法的用户
	 * @param users
	 * @return
	 */
	private boolean checkValidUser(User ...users){
		String curUserId = SecurityUtil.getCurrentUserId();
		for(User user:users){
			if(user!=null && user.getFid().equals(curUserId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 创建操作记录
	 * @param task
	 * @param triggerType
	 * @return
	 */
	public FlowOperationRecord createOperationRecord(Task task, int triggerType){
		return createOperationRecord(task, triggerType, null);
	}
	
	/**
	 * 创建操作记录
	 * @param task
	 * @param triggerType
	 * @return
	 */
	public FlowOperationRecord createOperationRecord(Task task, int triggerType, String remark){
		FlowOperationRecord operationRecord = new FlowOperationRecord();
		operationRecord.setOrg(SecurityUtil.getCurrentOrg());
		operationRecord.setCreator(SecurityUtil.getCurrentUser());
		operationRecord.setFiscalAccount(SecurityUtil.getFiscalAccount());
		operationRecord.setCreateTime(Calendar.getInstance().getTime());
		operationRecord.setTask(task);
		operationRecord.setTriggerType(triggerType);
		operationRecord.setBusinessScene(task.getStatus() + "");
		operationRecord.setBusinessType(FlowOperationRecord.BUSINESS_TYPE_TASK);
		operationRecord.setDescribe(remark);
		return operationRecord;
	}
	
	/**
	 * 附件上传
	 * @param multiFile
	 * @return
	 */
	public Attach uploadAttach(MultipartFile multiFile) {
		Attach attach = new Attach();
		
		attach.setFileName(multiFile.getOriginalFilename());
		attach.setType(Attach.TYPE_IMAGE);
		attach.setStatus(Attach.STATUS_INSERT);
		attach.setCreator(SecurityUtil.getCurrentUser());
		attach.setCreateTime(new Date());
		attachService.saveMultiPartFile(attach, multiFile);
		attachService.save(attach);
		return attach;
	}
	
	

	/**
	 * 维护树节点标识
	 * @return
	 */
	public RequestResult updateTaskTreeFlag() {

		List<Task> tasks = repository.getTaskTreeFlag();
		
		for(Task task:tasks){
			int flag = Task.TREE_FLAG_CHILD;
			
			if(task.getChilds().size()>0){
				flag = Task.TREE_FLAG_PARENT;
			}
			task.setTreeFlag(flag);
			repository.save(task);
			
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 添加计划的根事件
	 * @param plan 计划
	 */
	public Task saveRootTask(Plan plan){
		Task rootTask = new Task();
		rootTask.setLevel(0);
		rootTask.setNumber(0);
		rootTask.setPlan(plan);
		rootTask.setCode("root");
		rootTask.setName("rootTask");
		rootTask.setOrg(plan.getOrg());
		rootTask.setCreator(plan.getCreator());
		rootTask.setDept(plan.getDept());
		rootTask.setAntipateEndTime(plan.getAntipateEndTime());
		rootTask.setAntipateStartTime(plan.getAntipateStartTime());
		rootTask.setCreateTime(Calendar.getInstance().getTime());
		rootTask.setUpdateTime(Calendar.getInstance().getTime());
		rootTask.setInitiater(plan.getInitiater());
		rootTask.setUndertaker(plan.getCreator());
		rootTask.setFiscalAccount(plan.getFiscalAccount());
		save(rootTask);
		return rootTask;
	}
	/**
	 * 获取计划的根事件
	 * @param planId 计划ID
	 * @return
	 */
	public Task getRootTask(String planId){
		List<Task> tasks = repository.getRootTask(planId);
		if(CollectionUtils.isNotEmpty(tasks)){
			if(tasks.size() > 1){
				throw new RuntimeException("一个计划不允许存在多个根事件!");
			}
			return tasks.get(0); //唯一
		}
		return null;
	}

	@Override
	public CrudRepository<Task, String> getRepository() {
		return repository;
	}
	/**
	 * 判断计划是否完成（不包括根事件）
	 * @param planId 计划ID
	 * @param excludeTaskIds 排除的事件ID
	 * @return
	 */
	public boolean isPlanComplete(String planId, String ... excludeTaskIds){
		return repository.isPlanComplete(planId, excludeTaskIds);
	}
	/**
	 * 根据计划ID寻找所有叶子节点
	 * @param planId
	 * @return
	 */
	public List<Task> queryAllChildren(String planId) {
		return repository.queryAllChildren(planId);
	}
	
	/**
	 * 统计某个计划的事件数量，排除根事件
	 * @param planId 计划ID
	 * @return
	 */
	public long countByPlanId(String planId){
		return repository.countByPlanId(planId);
	};
	/**
	 * 事件发送消息
	 * @param task 事件
	 * @param sender 发送人
	 * @param triggerType 触发类型
	 * @throws Exception
	 */
	public void sendMessage(Task task, User sender, Integer triggerType){
		String busClass = Task.class.getName();
		String busScene = task.getStatus()+"";
		List<User> receivers = msgSettingService.queryTaskReceiver(task, triggerType);
		Map<String, Object> paramMap = buildMap(task, sender);
		int tag = SendUtils.getTag(task.getSendPhoneMsg(), task.getSendEmail());
		messageService.sendMessage(busClass, busScene, paramMap, receivers, sender, triggerType, task.getFiscalAccount(), tag);
	}
	/**
	 * 创建发送消息的Map
	 * @param task 事件
	 * @param sender 发送人
	 * @return
	 */
	public Map<String, Object> buildMap(Task task, User sender){
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("sender", sender);
		paramMap.put("task", task);
		paramMap.put("plan", task.getPlan());
		return paramMap;
	}
	/**
	 * 获取某个计划中，处于某个层级的事件
	 * @param planId 计划ID
	 * @param level 事件层级
	 * @return
	 */
	public List<Task> getByLevel(String planId, int level){
		return repository.getByLevel(planId, level);
	}
	/**
	 * 统计某个计划的未完成事件数量，排除根事件
	 * @param planId 计划ID
	 * @return
	 */
	public long countNotCompleteByPlanId(String planId){
		ArrayList<Integer> list = Lists.newArrayList(Task.STATUS_FINISHED, Task.STATUS_STOPED);
		return repository.countNotCompleteByPlanId(planId, list);
	}
	/**
	 * 获取事件
	 * @param ids 事件ID
	 * @return
	 */
	public List<Task> getByIds(String ... ids){
		if(ids == null || ids.length == 0){
			return Collections.emptyList();
		}
		ArrayList<String> list = Lists.newArrayList(ids);
		return repository.getByIds(list);
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

	private static final String SHR = "SHR";//审核人
	private static final String FQR = "FQR";//发起人
	private static final String TZR = "TZR";//填制人
	private static final String ZRR = "ZRR";//责任人
	private static final String CBR = "CBR";//承办人
	private static final String PARENT = "parent-";//父事件的
	private static final String PLAN = "plan-";//计划的的
	
	/**
	 * 根据权限编码获取用户ID Sets
	 * @param task
	 * @param auths
	 * @return
	 */
	public Set<String> getUserIds(Task task, List<String> auths){
		Set<String> userIds = Sets.newHashSet();
		Task check = task;
		for(String auth:auths){
			if(auth.startsWith(PLAN)){
				auth = auth.substring(PLAN.length());
				Plan plan = task.getPlan();
				List<String> planAuth = Lists.newArrayList(auth);
				userIds.addAll(planService.getUserIds(plan, planAuth));
				continue;
			}
			if(auth.startsWith(PARENT)){
				auth = auth.substring(PARENT.length());
				check = task.getParent();
			}
			if(check==null)continue;
			if(SHR.equals(auth)){
				if(check.getAuditer()!=null){
					String auditorId = check.getAuditer().getFid();
					userIds.add(auditorId);
				}
			}else if(FQR.equals(auth)){
				if(check.getInitiater()!=null){
					String initiaterId = check.getInitiater().getFid();
					userIds.add(initiaterId);
				}
			}else if(TZR.equals(auth)){
				if(check.getCreator()!=null){
					String creatorId = check.getCreator().getFid();
					userIds.add(creatorId);
				}
			}else if(ZRR.equals(auth)){
				if(check.getPrincipaler()!=null){
					String principalerId = check.getPrincipaler().getFid();
					userIds.add(principalerId);
				}
			}else if(CBR.equals(auth)){
				if(check.getUndertaker()!=null){
					String undertakererId = check.getUndertaker().getFid();
					userIds.add(undertakererId);
				}
			}
		}
		return userIds;
	}
	
	/**
	 * 多计划流程合并
	 * @param vo	操作计划vo
	 * @param ids	需要合并计划(事件)id集合
	 * @param type	1.计划；2.事件
	 * @return
	 */
	@Transactional
	public RequestResult mergePlan(TaskVo vo, String ids, int type) {
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> plantIds = splitter.splitToList(ids);
		try {
			// 计划合并
			if (type == 1) {
				for (String oldPlanId : plantIds) {
					List<Task> byLevel = getByLevel(oldPlanId, 1);
					List<Task> oldParent = getByLevel(oldPlanId, 0);
					String oldParentId = oldParent.get(0).getFid();
					List<Task> tasks = getByLevel(vo.getPlanId(), 0);
					Task parent = tasks.get(0);
					String parentId = parent.getFid();
					for (Task task : byLevel) {
						task.setParent(parent);
						save(task);
					}
					changeByPlanId(oldPlanId, vo.getPlanId());
					changeByParentIds(oldParentId, parentId, vo.getPlanId());
				}
				//把合并了的计划隐藏
				for (String oldPlanId : plantIds) {
					Plan oldPlan = planService.get(oldPlanId);
					oldPlan.setHide(0);
					planService.save(oldPlan);
					RequestResult updatePlanId = planService.updatePlanId(oldPlanId, vo.getPlanId());
					if(updatePlanId.getReturnCode()==1){
						 return buildFailRequestResult(updatePlanId.getMessage());
					}
					
				}
				

			}
			if (type == 2) {
				String fid = vo.getFid();
				if (Strings.isNullOrEmpty(fid))
					return buildFailRequestResult("事件合并时，事件id不能为空!");
				Task task2 = get(fid);
				String parentIds = task2.getParentIds()==null?"":task2.getParentIds();
				for (String oldPlanId : plantIds) {
					// 获取树节点第一层记录
					List<Task> byLevel = getByLevel(oldPlanId, 1);
					List<Task> oldParent = getByLevel(oldPlanId, 0);
					String oldParentId = oldParent.get(0).getFid();
					for (Task task : byLevel) {
						// 先设置需要挂在哪个事件中
						task.setParent(task2);
						task.setParentIds(parentIds + "," + fid);
						save(task);
					}
					// 修改记录的层级（level【当前合并层级+需要合并的层级】）
//					Integer level = task2.getLevel();
					String parentIds2="";
					List<Task> list = repository.getTaskByPlan(oldPlanId);
					for (Task task : list) {
						Integer level2 = task.getLevel();
						if(level2==0) continue;
						task.setLevel(level2+task2.getLevel());
						if(level2==1){
							parentIds2 = task.getParentIds();
						}
						if(level2>=2){
							task.setParentIds(parentIds2 + "," + task.getParent().getFid());
						}
						save(task);
					}
					// 改变事件中的计划id
					changeByPlanId(oldPlanId, vo.getPlanId());
					// 替换事件中旧的父id
					changeByParentIds(oldParentId, fid, vo.getPlanId());
				}
				//把合并了的计划隐藏
				for (String oldPlanId : plantIds) {
					Plan oldPlan = planService.get(oldPlanId);
					oldPlan.setHide(0);
					planService.save(oldPlan);
					RequestResult updatePlanId = planService.updatePlanId(oldPlanId, vo.getPlanId());
					if(updatePlanId.getReturnCode()==1){
						 return buildFailRequestResult(updatePlanId.getMessage());
					}
				}
				task2.setTreeFlag(Task.TREE_FLAG_PARENT);//把当前合并的树节点改为父节点
				save(task2);
			}
			String planId = vo.getPlanId();
			Plan plan2 = planService.findOne(planId);
			//跟新计划金额（累加事件金额）
			updatePlanAmount(plan2);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("多计划流程合并异常!");
		}
		return buildSuccessRequestResult();

	}
	/**
	 * 修改计划id
	 * @param oldPlanId
	 * @param newPlanId
	 */
	@Transactional
	public void changeByPlanId(String oldPlanId,String newPlanId){
		repository.changeByPlanId(oldPlanId, newPlanId);
	}
	/**
	 * 修改父节点id，把事件挂到其他事件中，只操作1级事件
	 * @param fid	
	 * @param parentId
	 */
	@Transactional
	public void changeByParentId(String fid,String parentId){
		repository.changeByParentId(fid, parentId);
	}
	
	/**
	 * 替换父节点中需要替换的节点id
	 * @param old		待替换id
	 * @param replace	替换id
	 * @param planId	计划id
	 */
	@Transactional
	public void changeByParentIds(String old,String replace,String planId){
		repository.changeByParentIds(old, replace,planId);
	}


	/**
	 * 根据TemplateDatas生成事件
	 * @param plan
	 * @param templates
	 * @return
	 */
	@Transactional
	public RequestResult addTaskByTemplateData(Task rootTask, Plan plan,
											   Collection<TemplateData> templates, String typeTag){

		Long step = repository.countLevelOneByPlanId(plan.getFid());

		//生成一级事件
		int levelOneSeq = 1;
		for(TemplateData template:templates){

			//保存TemplateData数据到tflow_task_plantemplate
			TaskPlantemplate taskPlantemplate = taskPlantemplateService.genDataByTemplate(template, plan);

			PlanTemplate planTemplate = template.getTemplate();
			PlanTemplateDetail rootDetail = planTemplateDetailService.
					getRootByPlanTemplate(planTemplate.getFid());

			genTaskByTemplateData(template, rootDetail, rootTask, plan, levelOneSeq,
					typeTag, step.intValue(), taskPlantemplate);

			levelOneSeq++;
		}

		return new RequestResult();
	}

	/**
	 * 按模板数据生成事件、递归
	 * @param template
	 * @param parentTemplateDetail
	 * @param parentTask
	 * @param plan
	 * @param levelOneSeq
	 * @param typeTag
	 * @param step
	 */
	@Transactional
	private void genTaskByTemplateData(TemplateData template,
									   PlanTemplateDetail parentTemplateDetail,
									   Task parentTask,  Plan plan, int levelOneSeq, String typeTag,
										int step, TaskPlantemplate taskPlantemplate){

		List<PlanTemplateDetail> details = parentTemplateDetail.getChildren();

		for(PlanTemplateDetail templateDetail:details){

			int level = parentTask.getLevel()==null?0:parentTask.getLevel() + 1;
			String levelOneSeqStr = ""+levelOneSeq;
			String numStr = ""+(templateDetail.getNumber()+step);

			//事件编号=CG+TemplateData的序列号+T+模板序列号
			//如1-CG0001T0003
			String code = typeTag+StringUtils.leftPad(levelOneSeqStr, 4, "0")
					+"T"+StringUtils.leftPad(numStr, 4, "0");

			Task task = new Task();
			task.setCode(code);
			task.setName(templateDetail.getTaskName());
			task.setBillId(templateDetail.getBillId());
			task.setBillType(templateDetail.getBillType());
			task.setNumber(templateDetail.getNumber().intValue());

			task.setPlan(plan);
			task.setParent(parentTask);
			task.setDept(templateDetail.getDept());

			task.setTaskLevel(templateDetail.getTaskLevel());
			task.setPrincipaler(templateDetail.getPrincipal());
			task.setUndertaker(templateDetail.getUndertaker());
			task.setOrg(SecurityUtil.getCurrentOrg());
			task.setCreator(SecurityUtil.getCurrentUser());
			task.setFiscalAccount(SecurityUtil.getFiscalAccount());
			task.setCreateTime(Calendar.getInstance().getTime());
			task.setUpdateTime(Calendar.getInstance().getTime());
			task.setSecurityLevel(templateDetail.getSecurityLevel());
			task.setLevel(level);

			if(templateDetail.getAmountType()==PlanTemplateDetail.AMOUNT_TYPE_SCALE){
				BigDecimal amount = NumberUtil.multiply(template.getTotalAmount(), templateDetail.getAmount());
				amount = NumberUtil.divide(amount, new BigDecimal(100), 20);
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
				task.setAmount(amount);
			}else{
				task.setAmount(BigDecimal.ZERO);
			}

			//父ID路径、发起人
			if(level == 1){
				task.setParentIds(parentTask.getFid());
				task.setInitiater(plan.getCreator());

				//预计开始时间、预计结束时间
				Date startDate = DateUtilTools.changeDateTime(template.getStartDate(), 0,
						templateDetail.getPreDays(),0,0,0);
				task.setAntipateStartTime(startDate);

				Date endDate = DateUtilTools.changeDateTime(startDate, 0,
						templateDetail.getDays()-1,0,0,0);

				task.setAntipateEndTime(endDate);
				task.setOriginalEndTime(endDate);
				

				String describe = templateDataService.getAllDescribe(template);

				int maxLength = 499;
				describe = cn.fooltech.fool_ops.utils.StringUtils.maxLength(describe, maxLength);

				task.setDescribe(describe);
			}
			else{
				String parentIds = parentTask.getParentIds()==null?"":parentTask.getParentIds();
				parentIds = parentIds+ "," + parentTask.getFid();
				task.setParentIds(parentIds);
				task.setInitiater(parentTask.getUndertaker());

				//预计开始时间、预计结束时间
				Date antipateStartTime = parentTask.getAntipateStartTime();
				Date startTime = DateUtil.addDays(antipateStartTime, templateDetail.getPreDays());
				Date endTime = DateUtil.addDays(startTime, templateDetail.getDays()-1);
				task.setAntipateStartTime(startTime);
				task.setAntipateEndTime(endTime);
				task.setOriginalEndTime(endTime);

				task.setDescribe(templateDetail.getDescribe());
			}
			save(task);
			updatePlanAmount(plan);

			if(taskPlantemplate!=null){
				tptdService.genDataByTask(plan, task, taskPlantemplate, template.getTemplate(),
						templateDetail);
			}

			genTaskByTemplateData(template, templateDetail, task, plan, levelOneSeq,
					typeTag, step, taskPlantemplate);
		}

		//维护父节点标识
		if(parentTask!=null && parentTask.getTreeFlag()==Task.TREE_FLAG_CHILD){
			parentTask.setTreeFlag(Task.TREE_FLAG_PARENT);
			repository.save(parentTask);
		}

	}

	/**
	 * 更新计划预计金额
	 * @param plan
	 * @author lsl
	 */
	private void updatePlanAmount(Plan plan){
		BigDecimal count= BigDecimal.valueOf(0);
		List<Task> list = repository.getTaskByPlan(plan.getFid());
		for(Task child:list)
			count=count.add(child.getAmount());
		plan.setEstimatedAmount(count);
		planService.save(plan);
	}

	/**
	 * 修正任务数据
	 */
	public void fixTaskData(){
		List<Task> tasks = repository.findAll();
		for(Task task:tasks){
			task.setParentIds(findFullParentIds(task.getParent()));
			if(repository.countChilds(task.getFid())>0){
				task.setTreeFlag(Task.TREE_FLAG_PARENT);
			}else{
				task.setTreeFlag(Task.TREE_FLAG_CHILD);
			}
			repository.save(task);
		}
	}

	/**
	 * 查找事件的全父路径
	 * @param parent
	 * @return
	 */
	public String findFullParentIds(Task parent){
		if(parent==null){
			return "";
		}else if(parent.getParent()!=null){
			return findFullParentIds(parent.getParent())+","+parent.getFid();
		}else{
			return parent.getFid();
		}
	}

	/**
	 * 根据计划ID获取事件
	 * @param planId
	 * @return
	 */
	public List<Task> getTaskByPlanId(String planId){
		return repository.getTaskByPlan2(planId);
	}

}
