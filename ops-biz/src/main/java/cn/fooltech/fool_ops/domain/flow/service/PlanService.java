package cn.fooltech.fool_ops.domain.flow.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanBill;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanBillService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountRepository;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoods;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateDetail;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.domain.flow.entity.YieldRate;
import cn.fooltech.fool_ops.domain.flow.repository.PlanGoodsRepository;
import cn.fooltech.fool_ops.domain.flow.repository.PlanRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.rate.service.LoanRateService;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateResult;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.domain.warehouse.entity.StockStore;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.StockStoreService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.rate.tools.ArithUtil;
import cn.fooltech.fool_ops.rate.tools.Calculate;
import cn.fooltech.fool_ops.rate.tools.EveryTradeBean;
import cn.fooltech.fool_ops.rate.tools.RateBean;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;

/**
 * <p>计划网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
@Service
public class PlanService extends BaseService<Plan,PlanVo,String>{

	/**
	 * 事件服务类
	 */
	@Autowired
	private TaskService taskService;
	
	/**
	 * 计划服务类
	 */
	@Autowired
	private PlanRepository planRepository;
	
	/**
	 * 用户服务类
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 事件级别
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	
	/**
	 * 保密级别
	 */
	@Autowired
	private SecurityLevelService securityLevelService;
	
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
	 * 消息服务
	 */
	@Autowired
	private FlowOperationRecordService operationService;
	
	/**
	 * 计划模板服务
	 */
	@Autowired
	private PlanTemplateService planTemplateService;
	
	@Autowired
	private PlanTemplateDetailService planTemplateDetailService;
	
	
	/**
	 * 用户安全级别服务类
	 */
//	@Autowired
//	private UserSecurityLevelService uslService;
	
	/**
	 * 央行贷款服务类
	 */
	@Autowired
	private LoanRateService loanRateService;
	
	/**
	 * 流程附件
	 */
	@Autowired
	private FlowAttachService flowAttachService;
	
	@Autowired
	private CapitalPlanService capitalPlanService ;
	
	@Autowired
	private CapitalPlanDetailService capitalPlanDetailService;
	
	@Autowired
	private CapitalPlanBillService capitalPlanBillService;
	
	@Autowired
	private TaskBillService taskBillService;
	
	@Autowired
	private PlanGoodsRepository planGoodsRepository;
	
	@Autowired
	private FreightAddressService freightAddressService;
	
	@Autowired
	private StockStoreService stockStoreService;
	
	@Autowired
	private PaymentBillService paymentBillService;
	
    /**
     * 仓储单据服务类
     */
    @Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService billService;
    
    @Autowired
    private WarehouseBillDetailService billDetailService;
    
    @Autowired
    private YieldRateService yieldRateService;
    
    @Autowired
    private FiscalAccountRepository accountRepository ;
    
    @Autowired
    private OrganizationRepository organizationRepository;


	
	/**
	 * 获取计划信息
	 * @param fid 计划ID
	 * @return
	 */
	public PlanVo getById(String fid){
		Plan entity = planRepository.findOne(fid);
		return getVo(entity, true);
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public PageJson query(PlanVo vo, PageParamater paramater,String excludeId){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(paramater,sort);
		Page<Plan> page = planRepository.query(vo, request,excludeId);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(getVos(page.getContent()));
		return pageJson;
		
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(PlanVo vo, boolean buildRootTask) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		String fid = vo.getFid();
		String code = vo.getCode();
		String name = vo.getName();
		String describe = vo.getDescribe();
		Date antipateEndTime = DateUtils.getDateFromString(vo.getAntipateEndTime()); //预计结束时间
		Date antipateStartTime = DateUtils.getDateFromString(vo.getAntipateStartTime()); //预计开始时间
		String planLevelId = vo.getPlanLevelId();
		String securityLevelId = vo.getSecurityLevelId();
		String estimatedYieldrate = vo.getEstimatedYieldrate(); //预计收益率
		String estimatedAmount = vo.getEstimatedAmount(); //计划金额
		String initiaterId = vo.getInitiaterId(); //发起人
		String principalerId = vo.getPrincipalerId(); //责任人
		String auditerId = vo.getAuditerId();
		//String marketYieldRate = vo.getMarketYieldRate(); //市场参考收益率
		Integer sendPhoneMsg = vo.getSendPhoneMsg();
		Integer sendEmail = vo.getSendEmail();
		Integer hide = vo.getHide();
		Date now = Calendar.getInstance().getTime();

		if(planRepository.isCodeExist(SecurityUtil.getFiscalAccountId(), code, fid)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计划编号重复!");
		}
		if(antipateEndTime!=null&&antipateStartTime!=null){
			if(antipateStartTime.compareTo(antipateEndTime)>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "开始时间要在结束时间之后");
			}
		}

		Plan entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new Plan();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setOriginalEndTime(antipateEndTime);
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}
		else{
			entity = planRepository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该计划不存在或已被删除!");
			}
			if(entity.getStatus() != Plan.STATUS_DRAFT){
				return new RequestResult(RequestResult.RETURN_FAILURE, "计划处于非草稿状态不允许修改!");
			}
		}
		entity.setCode(code);
		entity.setName(name);
		entity.setUpdateTime(now);
		entity.setDescribe(describe);
		entity.setAntipateEndTime(antipateEndTime);
		entity.setAntipateStartTime(antipateStartTime);
//		entity.setEstimatedAmount(NumberUtil.toBigDeciaml(estimatedAmount));
//		entity.setRealAmount(NumberUtil.toBigDeciaml(vo.getRealAmount()));
		//entity.setMarketYieldRate(NumberUtil.toBigDeciaml(marketYieldRate));
		//entity.setEstimatedYieldrate(NumberUtil.toBigDeciaml(estimatedYieldrate));
		entity.setPlanType(vo.getPlanType());

		entity.setSendPhoneMsg(sendPhoneMsg);
		entity.setSendEmail(sendEmail);
		entity.setHide(hide);

		//发起人
		if(StringUtils.isNotBlank(initiaterId)){
			User initiater = userService.get(initiaterId);
			entity.setInitiater(initiater);

			//判断是否有修改发起人，有修改则修改一级事件发起人
			if(StringUtils.isNotBlank(vo.getFid())){
				Task rootTask = taskService.getRootTask(entity.getFid());
				Set<Task> oneLevels = rootTask.getChilds();

				for(Task oneLevel:oneLevels){
					if(oneLevel.getInitiater()==null){
						oneLevel.setInitiater(initiater);
						taskService.save(oneLevel);
					} else if(!oneLevel.getInitiater().getFid().equals(initiaterId)){
						oneLevel.setInitiater(initiater);
						taskService.save(oneLevel);
					}
				}
			}
		}
		//责任人
		if(StringUtils.isNotBlank(principalerId)){
			entity.setPrincipaler(userService.get(principalerId));
		}
		//审核人
		if(StringUtils.isNotBlank(auditerId)){
			entity.setAuditer(userService.get(auditerId));
		}
		//计划级别
		if(StringUtils.isNotBlank(planLevelId)){
			entity.setPlanLevel(taskLevelService.get(planLevelId));
		}
		//保密级别
		if(StringUtils.isNotBlank(securityLevelId)){
			entity.setSecurityLevel(securityLevelService.get(securityLevelId));
		}

		if(StringUtils.isBlank(fid)){
			planRepository.save(entity);
			if(StringUtils.isBlank(vo.getPlanTemplateId()) && buildRootTask){
				//手动创建计划，则添加根事件
				taskService.saveRootTask(entity);
			}
		}
		else{
			planRepository.save(entity);
			//更新根事件
			Task rootTask = taskService.getRootTask(entity.getFid());

			if(rootTask==null)rootTask = taskService.saveRootTask(entity);

			rootTask.setAntipateEndTime(entity.getAntipateEndTime());
			rootTask.setAntipateStartTime(entity.getAntipateStartTime());
			rootTask.setInitiater(entity.getInitiater());
			taskService.save(rootTask);
		}
		
		RequestResult result = new RequestResult();
		result.setData(entity.getFid());

		//修改则删除旧附件数据再保存新增
		if(!Strings.isNullOrEmpty(vo.getFid())){
			flowAttachService.deletePlanAttach(entity.getFid());
		}

		//插入流程附件表
		String attachIds = vo.getAttachIds();
		flowAttachService.savePlanAttach(entity, attachIds);
		return result;
	}

	
	/**
	 * 删除
	 * @return
	 */
	@Transactional
	public RequestResult  delete(String fid){
		Plan entity = planRepository.findOne(fid);
		if(entity == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该计划不存在或已被删除!");
		}

		User initiater = entity.getInitiater();
		User creator = entity.getCreator();
		
		//权限说明：只有计划的发起人或填制人可以删除该计划
		if(!checkValidUser(initiater, creator)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "只有计划的发起人或填制人可以删除该计划!");
		}

		if(entity.getStatus() != Plan.STATUS_DRAFT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非草稿状态的计划，不允许删除!");
		}
		List<TaskVo> taskList = taskService.queryAllByPlan(fid);
		for (TaskVo taskVo : taskList) {
			List<TaskBillVo> taskBillVos = taskBillService.queryByTaskId(taskVo.getFid());
			if (taskBillVos.size()>0) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "计划事件已关联单据，不允许删除计划!");
			}
		}
		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
		if(capitalPlan!=null){
			RequestResult delete = capitalPlanService.delete(capitalPlan);
			if(delete.getReturnCode()==1){
				return delete;
			}
		}

		//operationService.deleteByBusId(entity.getFid());
		planRepository.deletePlan(entity.getFid());
		return new RequestResult();
	}
	
	@Override
	public PlanVo getVo(Plan entity) {
		return getVo(entity, false);
	}
	
	/**
	 * 单个实体转vo
	 */
	public PlanVo getVo(Plan entity, boolean loadMore) {
		if(entity == null){
			return null;
		}
		
		PlanVo vo = new PlanVo();
		vo.setFid(entity.getFid());
		vo.setPlanType(entity.getPlanType());
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setStatus(entity.getStatus());
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(entity.getAuditTime());
		vo.setActualEndTime(entity.getActualEndTime());
		vo.setActualStartTime(entity.getActualStartTime());
		vo.setAntipateEndTime(DateUtils.getDateString(entity.getAntipateEndTime()));
		vo.setAntipateStartTime(DateUtils.getDateString(entity.getAntipateStartTime()));
		vo.setOriginalEndTime(entity.getOriginalEndTime());
		vo.setEstimatedAmount(NumberUtil.bigDecimalToStr(entity.getEstimatedAmount()));
		vo.setRealAmount(NumberUtil.bigDecimalToStr(entity.getRealAmount()));
		//vo.setMarketYieldRate(NumberUtil.bigDecimalToStr(entity.getMarketYieldRate()));
		//vo.setCurrentYieldRate(NumberUtil.bigDecimalToStr(entity.getCurrentYieldRate()));
		//vo.setEffectiveYieldrate(NumberUtil.bigDecimalToStr(entity.getEffectiveYieldrate()));
		//vo.setEstimatedYieldrate(NumberUtil.bigDecimalToStr(entity.getEstimatedYieldrate()));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		
		vo.setSendPhoneMsg(entity.getSendPhoneMsg());
		vo.setSendEmail(entity.getSendEmail());
		vo.setHide(entity.getHide());
		/*start 流程收益率分析增加字段延迟次数   2017-4-11*/
		vo.setDelayedTime(entity.getDelayedTime());
		vo.setReferenceYieldrate(entity.getReferenceYieldrate());
		vo.setInAmount(entity.getInAmount());
		vo.setOutAmount(entity.getOutAmount());
		/*end 流程收益率分析增加字段延迟次数   2017-4-11*/
		//审核人
		User auditer = entity.getAuditer();
		if(auditer != null){
			vo.setAuditerId(auditer.getFid());
			vo.setAuditerName(auditer.getUserName());
		}
		//创建人
		User creator = entity.getCreator();
		if(creator != null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		//发起人
		User initiater = entity.getInitiater();
		if(initiater != null){
			vo.setInitiaterId(initiater.getFid());
			vo.setInitiaterName(initiater.getUserName());
		}
		//责任人
		User principaler = entity.getPrincipaler();
		if(principaler != null){
			vo.setPrincipalerId(principaler.getFid());
			vo.setPrincipalerName(principaler.getUserName());
		}
		//部门
		Organization dept = entity.getDept();
		if(dept != null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		//计划级别
		TaskLevel planLevel = entity.getPlanLevel();
		if(planLevel != null){
			vo.setPlanLevelId(planLevel.getFid());
			vo.setPlanLevelName(planLevel.getName());	
		}
		//保密级别
		SecurityLevel securityLevel = entity.getSecurityLevel();
		if(securityLevel != null){
			vo.setSecurityLevelId(securityLevel.getFid());
			vo.setSecurityLevelName(securityLevel.getName());	
		}
		if(loadMore){
			//全部事件是否已完成
			boolean isCompleted = taskService.isPlanComplete(entity.getFid());
			vo.setIsAllTaskComplete(isCompleted ? 1 : 0);
			//计算收益率
//			vo = computeRate(vo);
			//实际收益率
			vo.setEffectiveYieldrate(NumberUtil.bigDecimalToStr(entity.getEffectiveYieldrate()));
			//预计收益率
			vo.setEstimatedYieldrate(NumberUtil.bigDecimalToStr(entity.getEstimatedYieldrate()));
		}
		
		return vo;
	}
	
	/**
	 * 计算收益率
	 * @return
	 */
	private PlanVo computeRate(PlanVo vo){
		
		List<EveryTradeBean> expectIncomeList = Lists.newArrayList();
		List<EveryTradeBean> expectExpenseList = Lists.newArrayList();
		
		List<EveryTradeBean> realIncomeList = Lists.newArrayList();
		List<EveryTradeBean> realExpenseList = Lists.newArrayList();
		
		List<Task> childTasks = taskService.getTaskByPlanId(vo.getFid());
		for(Task task:childTasks){
//			if(task.getAmount()==null)continue;
//			if(task.getStatus()==Task.STATUS_FINISHED && task.getRealAmount()==null)continue;
			
			EveryTradeBean realbean = new EveryTradeBean();
			EveryTradeBean expectbean = new EveryTradeBean();
			
			BigDecimal amount = task.getAmount();
			BigDecimal realamount = task.getRealAmount();
			if(task.getActualEndTime()==null){
				realbean.setTime(task.getAntipateEndTime());
				realbean.setMoney(amount.abs().doubleValue());
			}else{
				realbean.setTime(task.getActualEndTime());
				realbean.setMoney(realamount.abs().doubleValue());
			}
			expectbean.setTime(task.getAntipateEndTime());
			expectbean.setMoney(amount.abs().doubleValue());
			//收入
			if(amount.compareTo(BigDecimal.ZERO)>0){
				expectIncomeList.add(expectbean);
			}else{
			//支出
				expectExpenseList.add(expectbean);
			}
			//收入
			if(realamount!=null && realamount.compareTo(BigDecimal.ZERO)>0){
				realIncomeList.add(realbean);
			}else if(amount.compareTo(BigDecimal.ZERO)>0){
				realIncomeList.add(realbean);
			}else{
			//支出
				realExpenseList.add(realbean);
			}
		}
		
		/*Date date = vo.getActualStartTime();
		if(date==null)date = DateUtils.getDateFromString(vo.getAntipateStartTime());*/
		double bankRate = loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());
		
		RateBean realRateBean = new RateBean();
		realRateBean.setIncomeList(realIncomeList);
		realRateBean.setExpendList(realExpenseList);
		//找央行贷款利率时取计划的实际开始时间
		realRateBean.setDayFundLostRate(bankRate);
		realRateBean.setCycle(30);//计算周期=30日
		
		RateBean expectRateBean = new RateBean();
		expectRateBean.setIncomeList(expectIncomeList);
		expectRateBean.setExpendList(expectExpenseList);
		//找央行贷款利率时取计划的实际开始时间
		expectRateBean.setDayFundLostRate(bankRate);
		expectRateBean.setCycle(30);//计算周期=30日
		
		Calculate.initRateBean(realRateBean);
		Calculate.initRateBean(expectRateBean);
		//实际收益率
		vo.setEffectiveYieldrate(ArithUtil.round(realRateBean.getCycleRate(), 2)+"");
		//预计收益率
		vo.setEstimatedYieldrate(ArithUtil.round(expectRateBean.getCycleRate(), 2)+"");
		
		/*vo.setEffectiveYieldrate(realRateBean.getCycleRate()+"");
		vo.setEstimatedYieldrate(expectRateBean.getCycleRate()+"");*/
		
		return vo;
	}
	
	/**
	 * 提交	
	 * @param id			计划id
	 * @param describe		描述
	 * @param type			是否处理：0-处理，1-不处理
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public RequestResult submit(String id, String describe,Integer type) throws Exception{
		
		Plan plan = planRepository.findOne(id);
		
		String inValid = ValidatorUtils.inValidMsg(getVo(plan));
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		if(plan.getStatus() != Plan.STATUS_DRAFT){
			return buildFailRequestResult("非草稿状态不能提交审核!");
		}
		
		//权限说明：仅发起人或填制人才能提交
		if(!checkValidUser(plan.getInitiater(), plan.getCreator())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "仅发起人或填制人才能提交!");
		}
		
		//i、没有一级事件的计划，不能提交
		long count = taskService.countByPlanId(id);
		if(count<=0){
			return buildFailRequestResult("没有一级事件的计划，不能提交");
		}
		/* 计划提交时判断资金计划明细表记录的计划收付金额是否与事件的计划金额相同，如果不相同，提示用户会复盖资金计划明细表的计划收付金额；*/
		BigDecimal sumPlanAmount = BigDecimal.ZERO;//资金计划明细表计划收付款金额总和
		BigDecimal sumAmount = BigDecimal.ZERO;//事件预计金额总和
		List<TaskVo> tasks = taskService.queryAllByPlan(id);
		List<String> paymentDate = Lists.newArrayList();
		List<String> taskDate = Lists.newArrayList();
//		int taskCount = 0;
		for (TaskVo task : tasks) {
			String antipateEndTime = task.getAntipateEndTime();
			String antipateStartTime = task.getAntipateStartTime();
			String amount = task.getAmount();
			BigDecimal taskAmount = new BigDecimal(amount)==null?BigDecimal.ZERO:new BigDecimal(amount);
			if(Strings.isNullOrEmpty(antipateEndTime)||taskAmount.compareTo(BigDecimal.ZERO)==0){
				continue ;
			}
			/*
			 * 2017-6-7 cwz
			 * 禅道： 2356 【优化需求】计划事件提取资金计划时，如果是支出（负数）的预计收付款日期取事件的预计开始时间；
			 * 如果是收入（正数）的预计收付款日期默认取事件的预计结束时间
			 */
			if(taskAmount.compareTo(BigDecimal.ZERO)<0){
				taskDate.add(antipateStartTime);
			}else{
				taskDate.add(antipateEndTime);
			}
			
			sumAmount=sumAmount.add(taskAmount);
			String fid = task.getFid();
			List<CapitalPlanDetail> details = capitalPlanDetailService.queryByRelation(fid);
			for (CapitalPlanDetail detail : details) {
				String detailDate = DateUtils.getStringByFormat(detail.getPaymentDate(), "yyyy-MM-dd");
				BigDecimal planAmount = detail.getPlanAmount()==null?BigDecimal.ZERO:detail.getPlanAmount();
				if(planAmount==null||planAmount.compareTo(BigDecimal.ZERO)==0) continue;
				
//				if(taskAmount.compareTo(planAmount)!=0){
//					taskCount++;
//				}
				paymentDate.add(detailDate);
				sumPlanAmount =sumPlanAmount.add(planAmount);
			}
		}
		Arrays.sort(taskDate.toArray());
		Arrays.sort(paymentDate.toArray());
		if(taskDate.size()!=paymentDate.size()){
			return buildFailRequestResult(1,"资金计划明细的预计收付款日期与事件计划结束日期条数不相同!");
		}
		String msg="";
		for (int i = 0; i < taskDate.size(); i++) {
			String date2 = taskDate.get(i);
			String date3 = paymentDate.get(i);
			if(!date2.equals(date3)){
				msg +=""+date2+",";
			}
			
		}
		if(type==null){
			type=0;
		}
		if (!Strings.isNullOrEmpty(msg) && type != 1) {
			return buildFailRequestResult(2,"事件计划开始/结束日期:"+msg+"和资金计划明细的预计收付款日期不同!");
		}
//		if(taskCount>0){
//			return buildFailRequestResult(3,"计划收付金额与事件的计划金额不相同,该操作会覆盖资金计划明细表的计划收付金额!");
//		}
		/*提交后把资金计划表与资金计划明细表的该计划记录的状态置为审核状态；*/
		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(id);
		if(capitalPlan!=null){
			BigDecimal estimatedAmount = plan.getEstimatedAmount();
			if(sumAmount.compareTo(sumPlanAmount)!=0||estimatedAmount.compareTo(capitalPlan.getPlanAmount())!=0){
				return buildFailRequestResult(3,"资金计划明细的计划收付金额与事件的计划金额不相同, 该操作会覆盖资金计划明细表的计划收付金额!");
			}
			RequestResult result = capitalPlanService.passAudit(capitalPlan.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			if(result.getReturnCode()==RequestResult.RETURN_FAILURE){
				return buildFailRequestResult(result.getMessage());
			}
			/** 获取预计收益率* */
			RateResult rateResult = getAnticipatedIncome(capitalPlan,plan);
			//设置计划预计收益率
			if(rateResult.getCycleRate()!=null){
				Double cycleRate = rateResult.getCycleRate();//预计收益率
//				System.out.println(cycleRate);
				plan.setEstimatedYieldrate(new BigDecimal(cycleRate));
			}
		}
	
		plan.setStatus(Plan.STATUS_TO_CHECK);
		/*计划流程审核时写入参考收益率*/
		LoanRateVo loanRateVo = loanRateService.findTopBy(SecurityUtil.getCurrentOrgId());
		if(loanRateVo!=null){
			String rate = loanRateVo.getRate();
			/*参考收益率=资金日损率*30*100%*/
			BigDecimal yieldRate = new BigDecimal(rate);
			yieldRate = yieldRate.multiply(new BigDecimal(30)).multiply(new BigDecimal(100));
			plan.setReferenceYieldrate(yieldRate);
		}
		planRepository.save(plan);
		
		sendMessage(plan, Message.TRIGGER_TYPE_SUBMIT);
		
		FlowOperationRecord record = createOperationRecord(plan, Message.TRIGGER_TYPE_SUBMIT, describe);
		operationService.save(record);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 计划审核
	 * @param auditResult:0.审核通过 1.拒绝，返回发起人  3.终止
	 * @throws Exception 
	 */
	@Transactional
	public RequestResult audit(String id, Integer auditResult, String rejectUserId, String describe) throws Exception{
		Plan plan = planRepository.findOne(id);
		
		if(plan.getStatus() != Plan.STATUS_TO_CHECK){
			if(plan.getAuditTime() != null){
				return buildFailRequestResult("计划已被审核!");
			}
			return buildFailRequestResult("非已提交待审核的状态，不允许审核!");
		}
		
		//依据当前数据计算预期收益率
		msgService.oper(Plan.class.getName(), plan.getStatus()+"", plan.getFid());
		
		//审核通过
		if(auditResult==0){
			
			//权限说明：仅审核人才能审核
			if(!checkValidUser(plan.getAuditer())){
				return buildFailRequestResult("仅审核人才能审核");
			}
			
			plan.setStatus(Plan.STATUS_EXECUTING);
			plan.setAuditTime(Calendar.getInstance().getTime());
			planRepository.save(plan);
			
			sendMessage(plan, Message.TRIGGER_TYPE_CHECK);
			
			FlowOperationRecord record = createOperationRecord(plan, Message.TRIGGER_TYPE_CHECK, describe);
			operationService.save(record);
			
			//分派一级事件的任务
			List<Task> taskList = taskService.getByLevel(plan.getFid(), 1);
			for(Task task : taskList){
				taskService.sendMessage(task, plan.getCreator(), Message.TRIGGER_TYPE_NEW);
				
				FlowOperationRecord operationRecord = taskService.createOperationRecord(task, Message.TRIGGER_TYPE_NEW);
				operationService.save(operationRecord);
			}
			
			Task rootTask = taskService.getRootTask(plan.getFid());
			rootTask.setAssignFlag(Task.ASSIGN_FLAG_YES);
			taskService.save(rootTask);
			
			return buildSuccessRequestResult();
		}else if(auditResult==1){
			
			if(!checkValidUser(plan.getAuditer())){
				return buildFailRequestResult("仅审核人才能拒绝");
			}
			
			//.拒绝，返回发起人
			plan.setStatus(Plan.STATUS_DRAFT);
			planRepository.save(plan);
			
			sendMessage(plan, Message.TRIGGER_TYPE_CHECK);
			
			FlowOperationRecord record = createOperationRecord(plan, Message.TRIGGER_TYPE_CHECK, describe);
			operationService.save(record);
			/*计划点击拒绝后把资金计划表、资金计划明细表对应的该计划的草稿状态的记录，把状态改为草稿；*/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(plan.getFid());
			if(capitalPlan!=null){
				RequestResult result = capitalPlanService.returnPassAudit(capitalPlan.getId(),  CapitalPlan.STATUS_DRAFT,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(result.getReturnCode()==RequestResult.RETURN_FAILURE){
					return buildFailRequestResult(result.getMessage());
				}
				/*删除资金计划关联单据表的数据，把资金计划表、资金计划明细表的单据金额、收付款金额置为0；*/
				List<CapitalPlanDetail> details = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
				for (CapitalPlanDetail detail : details) {
					List<CapitalPlanBill> list = capitalPlanBillService.queryByDetail(detail.getId());
					for (CapitalPlanBill bill : list) {
						capitalPlanBillService.delete(bill);
					}
					detail.setBillAmount(BigDecimal.ZERO);
					detail.setPaymentAmount(BigDecimal.ZERO);
					capitalPlanDetailService.save(detail);
				}
				capitalPlan.setBillAmount(BigDecimal.ZERO);
				capitalPlan.setPaymentAmount(BigDecimal.ZERO);
				capitalPlanService.save(capitalPlan);
			}
			return buildSuccessRequestResult();
		}else if(auditResult==3){
			
			if(!checkValidUser(plan.getAuditer(), plan.getCreator(), plan.getInitiater(), plan.getPrincipaler())){
				return buildFailRequestResult("仅发起人、审核人、填制人才能终止");
			}
			//.终止
			plan.setStatus(Plan.STATUS_STOPED);
			planRepository.save(plan);
			
			sendMessage(plan, Message.TRIGGER_TYPE_CHECK);
			
			FlowOperationRecord record = createOperationRecord(plan, Message.TRIGGER_TYPE_CHECK, describe);
			operationService.save(record);
			/*计划点击终止后把资金计划表、资金计划明细表对应的该计划的审核状态的记录，把状态改为取消；*/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(plan.getFid());
			if(capitalPlan!=null){
				List<CapitalPlanDetail> details = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
				for (CapitalPlanDetail detail : details) {
//					Integer relationSign = detail.getRelationSign();
//					if(relationSign!=CapitalPlan.STATUS_EXECUTING) continue;
					Integer recordStatus = detail.getRecordStatus();
					if(recordStatus!=CapitalPlan.STATUS_EXECUTING) continue;
					detail.setRecordStatus(CapitalPlan.STATUS_CANCEL);
					detail.setCancelor(SecurityUtil.getCurrentUser());
					detail.setCancelTime(new Date());
					detail.setUpdateTime(new Date());
					capitalPlanDetailService.save(detail);
				}
//				RequestResult result = capitalPlanService.cancel(capitalPlan.getId(),  CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
//				if(result.getReturnCode()==RequestResult.RETURN_FAILURE){
//					return buildFailRequestResult(result.getMessage());
//				}
			}
			return buildSuccessRequestResult();
		}
		return buildFailRequestResult("auditResult参数错误");
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
	 * 终止计划
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public RequestResult terminate(String id, String describe) throws Exception{
		
		Plan plan = planRepository.findOne(id);
		
		if(!checkValidUser(plan.getAuditer(), plan.getPrincipaler())){
			return buildFailRequestResult("仅审核人、责任人才能终止计划");
		}
		
		if(plan.getStatus()==Plan.STATUS_DRAFT||
				plan.getStatus()==Plan.STATUS_FINISHED){
			return buildFailRequestResult("不能对新建、已经完成的计划进行终止");
		}
		
		/*计划点击终止后把资金计划表、资金计划明细表对应的该计划的审核状态的记录，把状态改为取消；*/
		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(plan.getFid());
		if(capitalPlan!=null){
			List<CapitalPlanDetail> details = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
			for (CapitalPlanDetail detail : details) {
//				Integer relationSign = detail.getRelationSign();
//				if(relationSign!=CapitalPlan.STATUS_EXECUTING) continue;
				Integer recordStatus = detail.getRecordStatus();
				if(recordStatus!=CapitalPlan.STATUS_EXECUTING) continue;
				detail.setRecordStatus(CapitalPlan.STATUS_CANCEL);
				detail.setCancelor(SecurityUtil.getCurrentUser());
				detail.setCancelTime(new Date());
				detail.setUpdateTime(new Date());
				capitalPlanDetailService.save(detail);
			}
		}
		msgService.oper(Plan.class.getName(), plan.getStatus()+"", plan.getFid());
		
		//.终止
		plan.setStatus(Plan.STATUS_STOPED);
		planRepository.save(plan);
		sendMessage(plan, Message.TRIGGER_TYPE_STOP);
		
		FlowOperationRecord record = createOperationRecord(plan, Message.TRIGGER_TYPE_STOP, describe);
		operationService.save(record);
		/**
		 * 计算流程计划收益率
		 */
		RequestResult stockInOut = stockInOut(id, SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId(), 1);
		if(stockInOut.getReturnCode()==1){
			return stockInOut;
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 完成计划
	 * @param planId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public RequestResult completePlan(String planId,String describe) throws Exception{
		
		Plan entity = planRepository.findOne(planId);
		if(entity==null){
			return buildFailRequestResult("该计划已不存在，请刷新界面!");
		}
		/*计划、事件点击完成时判断计划收付金额、单据金额、收付款金额是否相等，如果相等，直接完成，如果不相等，要填写备注才能完成*/
//		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(entity.getFid());
//		if(capitalPlan!=null){
//			BigDecimal billAmount = capitalPlan.getBillAmount();// 单据金额
//			BigDecimal planAmount = capitalPlan.getPlanAmount();//计划收付金额
//			BigDecimal paymentAmount = capitalPlan.getPaymentAmount();//收付款金额
//			String describe = entity.getDescribe();
//			if (billAmount.compareTo(planAmount) != 0 || billAmount.compareTo(paymentAmount) != 0 ) {
//				if(Strings.isNullOrEmpty(describe)){
//					return buildFailRequestResult("计划收付金额、单据金额、收付款金额不一致，请先填写计划备注!");
//				}
//			}
//		}
		if (Strings.isNullOrEmpty(describe)) {
			return buildFailRequestResult("请先填写计划备注!");
		}
		if(!checkValidUser(entity.getInitiater(), entity.getCreator(), entity.getPrincipaler())){
			return buildFailRequestResult("仅发起人、填制人、责任人才能完成计划");
		}
		long count = taskService.countNotCompleteByPlanId(planId);
		if(count>0){//有未完成事件，计划不能完成
			return buildFailRequestResult("有未完成事件，计划不能完成");
		}
		
		if(entity.getStatus()!=Plan.STATUS_EXECUTING && entity.getStatus()!=Plan.STATUS_DELAYED){
			return buildFailRequestResult("计划不是办理中、延迟中状态不能完成");
		}
		
		entity.setActualEndTime(Calendar.getInstance().getTime());
		entity.setStatus(Plan.STATUS_FINISHED);
		planRepository.save(entity);
		
		sendMessage(entity, Message.TRIGGER_TYPE_COMPLETE);
		
		//.完成
		FlowOperationRecord record = createOperationRecord(entity, Message.TRIGGER_TYPE_COMPLETE, describe);
		operationService.save(record);
		/**
		 * 计算流程计划收益率
		 */
		RequestResult stockInOut = stockInOut(planId, SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId(), 1);
		if(stockInOut.getReturnCode()==1){
			return stockInOut;
		}
		return buildSuccessRequestResult(stockInOut.getData());
	}
	
	
	/**
	 * 创建操作记录
	 * @param plan
	 * @param triggerType
	 * @param describe
	 * @return
	 */
	public FlowOperationRecord createOperationRecord(Plan plan, int triggerType, String describe){
		FlowOperationRecord operationRecord = new FlowOperationRecord();
		operationRecord.setOrg(SecurityUtil.getCurrentOrg());
		operationRecord.setCreator(SecurityUtil.getCurrentUser());
		operationRecord.setFiscalAccount(SecurityUtil.getFiscalAccount());
		operationRecord.setCreateTime(Calendar.getInstance().getTime());
		operationRecord.setPlan(plan);
		operationRecord.setTriggerType(triggerType);
		operationRecord.setBusinessScene(plan.getStatus() + "");
		operationRecord.setBusinessType(FlowOperationRecord.BUSINESS_TYPE_PLAN);
		operationRecord.setDescribe(describe);
		return operationRecord;
	}

	/**
	 * 通过triggerType获得事件流程的名称
	 * @param triggerType 和流程名称的对应关系看message实体的常量
	 * @return
	 */
	public String getOperate(int triggerType) {
		String operateName = "未知流程";
		switch(triggerType) {
		case Message.TRIGGER_TYPE_SUBMIT: 
			operateName = "提交";
			break;
		case Message.TRIGGER_TYPE_UPDATE: 
			operateName = "修改";
			break;
		case Message.TRIGGER_TYPE_DELETE: 
			operateName = "删除";
			break;
		case Message.TRIGGER_TYPE_STOP: 
			operateName = "终止";
			break;
		case Message.TRIGGER_TYPE_CHANGE_UNDERTAKER: 
			operateName = "变更承办人";
			break;
		case Message.TRIGGER_TYPE_CHANGE_PRINCIPALER: 
			operateName = "变更责任人";
			break;
		case Message.TRIGGER_TYPE_DELAY: 
			operateName = "申请延迟 ";
			break;
		case Message.TRIGGER_TYPE_CHECK: 
			operateName = "审核";
			break;
		case Message.TRIGGER_TYPE_CHECK_EXECUTE_YES: 
			operateName = "审核通过办理";
			break;
		case Message.TRIGGER_TYPE_CHECK_EXECUTE_NO: 
			operateName = "审核不通过办理";
			break;
		case Message.TRIGGER_TYPE_CHECK_DELAY_YES: 
			operateName = "审核通过申请延迟 ";
			break;
		case Message.TRIGGER_TYPE_CHECK_DELAY_NO: 
			operateName = "审核不通过申请延迟";
			break;
		case Message.TRIGGER_TYPE_NEW: 
			operateName = "新建、分派 ";
			break;
		case Message.TRIGGER_TYPE_COMPLETE: 
			operateName = "完成";
			break;
		case Message.TRIGGER_TYPE_EXECUTE_END: 
			operateName = "办理结束";
			break;
		case Message.TRIGGER_TYPE_EXECUTE_START: 
			operateName = "办理开始";
			break;
		case Message.TRIGGER_TYPE_EVALUATE: 
			operateName = "评价 ";
			break;
		case Message.TRIGGER_TYPE_SCORE: 
			operateName = "评分";
			break;
		case Message.TRIGGER_TYPE_FOLLOW: 
			operateName = "关注";
			break;
		case Message.TRIGGER_TYPE_CHAT: 
			operateName = "留言 ";
			break;
		case Message.TRIGGER_TYPE_RELEVANCE: 
			operateName = "关联";
			break;
		case Message.TRIGGER_TYPE_EARLY_REMIND: 
			operateName = "提醒";
			break;
		case Message.TRIGGER_TYPE_DELAY_ALARM: 
			operateName = "延迟报警";
			break;
		case Message.TRIGGER_TYPE_YIELD_ALARM: 
			operateName = "收益率报警 ";
			break;
		case Message.TRIGGER_TYPE_STOCK_ALARM: 
			operateName = "库存报警 ";
			break;
		case Message.TRIGGER_TYPE_PURCHASE_PRODUCE: 
			operateName = "库存不足触发采购计划或生产计划 ";
			break;
		}
		return operateName;
	}

	@Override
	public CrudRepository<Plan, String> getRepository() {
		return planRepository;
	}
	/**
	 * 计划发送消息
	 * @param triggerType
	 * @param plan
	 * @throws Exception
	 */
	@Transactional
	public void sendMessage(Plan plan, Integer triggerType) throws Exception{
		String busClass = Plan.class.getName();
		String busScene = plan.getStatus()+"";
		User sender = plan.getCreator();
		List<User> receivers = msgSettingService.queryPlanReceiver(plan, triggerType);
		Map<String, Object> paramMap = buildMap(plan, sender);
		int tag = SendUtils.getTag(plan.getSendPhoneMsg(), plan.getSendEmail());
		msgService.sendMessage(busClass, busScene, paramMap, receivers, sender, triggerType, plan.getFiscalAccount(), tag);
	}
	/**
	 * 创建发送消息的Map
	 * @param plan
	 * @param sender
	 * @return
	 */
	public Map<String, Object> buildMap(Plan plan, User sender){

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("sender", sender);
		paramMap.put("initiater", plan.getInitiater());
		paramMap.put("plan", plan);
		
		return paramMap;
	}

	private static final String SHR = "SHR";//审核人
	private static final String FQR = "FQR";//发起人
	private static final String TZR = "TZR";//填制人
	private static final String ZRR = "ZRR";//责任人

	/**
	 * 根据权限编码获取用户ID Sets
	 * @param plan
	 * @param auths
	 * @return
	 */
	public Set<String> getUserIds(Plan plan, List<String> auths){
		Set<String> userIds = Sets.newHashSet();
		for(String auth:auths){
			if(SHR.equals(auth)){
				if(plan.getAuditer()!=null){
					String auditorId = plan.getAuditer().getFid();
					userIds.add(auditorId);
				}
			}else if(FQR.equals(auth)){
				if(plan.getInitiater()!=null){
					String initiaterId = plan.getInitiater().getFid();
					userIds.add(initiaterId);
				}
			}else if(TZR.equals(auth)){
				if(plan.getCreator()!=null){
					String creatorId = plan.getCreator().getFid();
					userIds.add(creatorId);
				}
			}else if(ZRR.equals(auth)){
				if(plan.getPrincipaler()!=null){
					String principalerId = plan.getPrincipaler().getFid();
					userIds.add(principalerId);
				}
			}
		}
		return userIds;
	}

	/**
	 * 把计划保存到模板
     * @param planId
     * @param planTplId
     * @param taskIds
	 * @return RequestResult
	 */
	@Transactional
	public RequestResult savePlanToTemplate(String planId, String planTplId, String taskIds) {
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        Set<String> selectIds = Sets.newHashSet(splitter.splitToList(taskIds));

		Plan plan = planRepository.findOne(planId);
        Task rootTask = taskService.getRootTask(planId);
        List<Task> tasks = taskService.getByIds(taskIds.split(","));
		PlanTemplate planTemplate = planTemplateService.findOne(planTplId);  //若为空表示没有传模板过来
		boolean isRootSelect = selectIds.contains(rootTask.getFid()); //用于计算准备天数

		if (planTemplate == null) {
            planTemplate = genPlanTemplate(plan);
            planTemplateService.save(planTemplate);
		} else {
			// 清空原有模板事件
			planTemplateDetailService.deleteByTemplateId(planTplId);
		}

		PlanTemplateDetail rootDetail = genPlanTemplateDetail(planTemplate);
		planTemplateDetailService.save(rootDetail);

		// 子事件选中，则其父事件也默认选中
        Set<String> parentIds = Sets.newHashSet();
        tasks.forEach(task -> findParent(task, parentIds));
        selectIds.addAll(parentIds);

        List<Integer> dayList = Lists.newArrayList(0, 1);
        saveTaskToPlanTemplateDetail1(planTemplate, rootDetail, selectIds, rootTask, dayList, isRootSelect);

        // 更新模板总完成天数 FDAYS
		planTemplate.setDays(new BigDecimal(dayList.get(0)));
		planTemplateService.save(planTemplate);

		return buildSuccessRequestResult(planTemplate.getFid());
	}

    /**
     * 递归出事件的所有的父节点FID
     */
    private void findParent(Task task, Set<String> parentIds){
        if (task.getParent() == null) return;

		parentIds.add(task.getParent().getFid());
		findParent(task.getParent(), parentIds);
    }

    private void saveTaskToPlanTemplateDetail1(PlanTemplate planTemplate, PlanTemplateDetail parent, Set<String> selectIds,
											   Task rootTask, List<Integer> dayList, boolean isRootSelect){
		List<Task> childrenList= Lists.newArrayList(rootTask.getChilds());
		Collections.sort(childrenList, new Comparator<Task>() {
			@Override
			public int compare(Task child1, Task child2) {
				int result = (int)(child1.getAntipateStartTime().getTime() - child2.getAntipateStartTime().getTime());
				if (result == 0) result = child1.getFid().compareTo(child2.getFid());
				return result;
			}
		});

		for(Task task:childrenList){
            if (selectIds.contains(task.getFid())) {
                // 符合
                PlanTemplateDetail detail = new PlanTemplateDetail();
                detail.setParent(parent);
                detail.setTaskLevel(task.getTaskLevel());
                detail.setSecurityLevel(task.getSecurityLevel());
//                detail.setAmount(task.getAmount());
                detail.setAmount(new BigDecimal(100));
                detail.setAmountType(PlanTemplateDetail.AMOUNT_TYPE_SCALE);

                int days = DateUtilTools.daysBetween(task.getAntipateStartTime(), task.getAntipateEndTime());
                detail.setDays(days + 1);
                detail.setDept(task.getDept());
                detail.setDescribe(task.getDescribe());
                detail.setPlanTemplate(planTemplate);

				int index1 = dayList.get(1);
                detail.setNumber((short)index1);
				dayList.set(1, dayList.get(1)+1);

                int predays = 0;
                if (task.getParent() != null && task.getParent().getAntipateStartTime() != null) {
                	if (task.getParent().getParent() == null) {
                		if (isRootSelect) {
							predays = DateUtilTools.daysBetween(task.getParent().getAntipateStartTime(),
									task.getAntipateStartTime());
                		}
					} else {
						predays = DateUtilTools.daysBetween(task.getParent().getAntipateStartTime(),
								task.getAntipateStartTime());
					}
                }

				dayList.set(0, dayList.get(0) > days + predays + 1 ? dayList.get(0) : days + predays + 1);

                detail.setPreDays(predays);
                detail.setPrincipal(task.getPrincipaler());
                detail.setUndertaker(task.getUndertaker());
                detail.setTaskName(task.getName());

                planTemplateDetailService.save(detail);
                saveTaskToPlanTemplateDetail1(planTemplate, detail, selectIds, task, dayList, isRootSelect);
            }
        }
    }

	/**
	 * 保存事件到计划模板明细
	 * @param task
	 */
	private void saveTaskToPlanTemplateDetail(Task task, PlanTemplate planTemplate,
											 PlanTemplateDetail parent, Short number, Set<String> needs){

		PlanTemplateDetail detail = new PlanTemplateDetail();
		detail.setParent(parent);
		detail.setTaskLevel(task.getTaskLevel());
		detail.setSecurityLevel(task.getSecurityLevel());
		detail.setAmount(task.getAmount());
		detail.setAmountType(0);

		int days = DateUtilTools.daysBetween(task.getAntipateStartTime(), task.getAntipateEndTime());
		detail.setDays(days);
		detail.setDept(task.getDept());
		detail.setDescribe(task.getDescribe());
		detail.setPlanTemplate(planTemplate);

		detail.setNumber(number);

		int predays = 0;
		if (task.getParent() != null && task.getParent().getAntipateStartTime() != null) {
			predays = DateUtilTools.daysBetween(task.getParent().getAntipateStartTime(),
					task.getAntipateStartTime());
		}

		detail.setPreDays(predays);
		detail.setPrincipal(task.getPrincipaler());
		detail.setUndertaker(task.getUndertaker());
		detail.setTaskName(task.getName());

		planTemplateDetailService.save(detail);

		Set<Task> children = task.getChilds();

		short index = 1;
		for(Task child:children){
//			saveTaskToPlanTemplateDetail(child, planTemplate, detail, index);
			index++;
		}
	}

	/**
	 * 生成一个计划模板对象
	 * @param plan
	 */
	private static PlanTemplate genPlanTemplate(Plan plan){
		PlanTemplate planTemplate = new PlanTemplate();
		planTemplate.setFiscalAccount(plan.getFiscalAccount());
		planTemplate.setOrg(plan.getOrg());
		planTemplate.setCode(plan.getCode());
		planTemplate.setCreateTime(new Date());
		planTemplate.setCreator(SecurityUtil.getCurrentUser());

		BigDecimal days = new BigDecimal(DateUtilTools.daysBetween(plan.getAntipateStartTime(), plan.getAntipateEndTime()));
		planTemplate.setDays(days);
		planTemplate.setDescribe(plan.getDescribe());
		planTemplate.setName(plan.getName());
		planTemplate.setPrincipaler(plan.getPrincipaler());
		planTemplate.setUndertaker(plan.getCreator());
		planTemplate.setSecurityLevel(plan.getSecurityLevel());
		planTemplate.setStatus(PlanTemplate.STATUS_STOP);
		planTemplate.setTaskLevel(plan.getPlanLevel());

		return planTemplate;
	}

	/**
	 * 生成一个计划模板事件对象
	 * @param planTemplate
	 */
	private static PlanTemplateDetail genPlanTemplateDetail(PlanTemplate planTemplate){
		PlanTemplateDetail rootDetail = new PlanTemplateDetail();
		rootDetail.setTaskName("root");
		rootDetail.setPlanTemplate(planTemplate);
		rootDetail.setNumber((short)0);
		rootDetail.setDays(0);
		rootDetail.setPreDays(0);
		rootDetail.setAmount(BigDecimal.ZERO);
		rootDetail.setAmountType(0);

		return rootDetail;
	}
	/**
	 * 计划合并成功后，原计划的相关表都要更新计划id
	 * @param beforePlanId	原计划ID
	 * @param afterPlanId	修改后计划ID
	 */
	@Transactional
	public RequestResult updatePlanId(String beforePlanId,String afterPlanId){
		try {
			planRepository.updatePlanId(beforePlanId, afterPlanId);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("计划合并更改计划ID存储过程出错!");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 根据计划id生成资金计划和明细记录
	 * @param planId	计划id
	 * @return
	 */
	@Transactional
	public RequestResult addCapitalPlan(String planId) {
		try {
			Plan plan = planRepository.findOne(planId);
			if(plan==null){
				return buildFailRequestResult("该计划已不存在，请刷新界面!");
			}
			Integer status = plan.getStatus();
			/*如果计划已经审核状态，则不能点击提取事件计划金额按钮，以及不可编辑资金计划，只能操作绑定事件关联的单据；*/
			if(status==Plan.STATUS_TO_CHECK){
				return buildFailRequestResult("计划已经审核,则不能提取事件计划!");
			}
			List<Task> tasks = taskService.queryAllChildren(planId);
			/*提取事件计划金额，是根据当前计划事件的计划金额，自动生成资金计划记录，预计收付款日期为事件的计划结束日期，金额为事件的计划金额，只有计划金额不为0才写入资金计划记录；
			如果计划已经审核状态，则不能点击提取事件计划金额按钮，以及不可编辑资金计划，只能操作绑定事件关联的单据；*/
			List<CapitalPlanDetailVo> detailVos = Lists.newArrayList();
			for (Task task : tasks) {
				BigDecimal amount = task.getAmount();//预计金额
				Date endTime = task.getAntipateEndTime();
//				Date originalEndTime = task.getOriginalEndTime();//获取原计划完成时间
				if(amount.compareTo(BigDecimal.ZERO)>0){
					CapitalPlanDetailVo detailVo = new CapitalPlanDetailVo();
					detailVo.setPlanAmount(amount);
					detailVo.setRelationSign(72);
					detailVo.setRelationId(task.getFid());
					detailVo.setPaymentDate(endTime);
					detailVos.add(detailVo);
				}
			}
			JSONArray fromObject = JSONArray.fromObject(detailVos);
			String details = fromObject.toString();
			CapitalPlanVo planVo = new CapitalPlanVo();
			planVo.setPlanAmount(plan.getEstimatedAmount());
			planVo.setCalculation(1);
			planVo.setCode(plan.getCode());
			planVo.setPaymentDate(plan.getAntipateEndTime());
			planVo.setRelationId(planId);
			planVo.setRelationSign(71);
			planVo.setDetails(details);
			CapitalPlan relation = capitalPlanService.queryByRelation(planId);
			if(relation!=null){
				capitalPlanService.del(relation.getId(),0);//先删除旧记录，再添加!
			}
			capitalPlanService.save(planVo,0);

		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("提取事件计划出错!");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 流程计划预计收益率
	 * @return
	 */
	public RateResult getAnticipatedIncome(CapitalPlan capitalPlan,Plan plan){
		/* 预计收益率：在计划提交时计算，取资金计划的记录，用收益率计算公式算出收益率，更新计划主表的预计收益率；*/
		if(capitalPlan!=null){
			List<CapitalPlanDetail> records = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
			double rate=loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());

			RateBean real = new RateBean();
			real.setDayFundLostRate(rate);
			real.setFirstPayDay(plan.getAntipateStartTime());
			for(CapitalPlanDetail record:records){
				EveryTradeBean trade = new EveryTradeBean();
				
				trade.setMoney(record.getPlanAmount().doubleValue());
				trade.setTime(record.getOrgPaymentDate());
				
				if(record.getPlanAmount().compareTo(BigDecimal.ZERO)>0){
					real.getIncomeList().add(trade);
					
				}else{
					trade.setMoney(record.getPlanAmount().abs().doubleValue());
					real.getExpendList().add(trade);
				}
			}
			/*	
			 * 注意, 要判断计划对应的计划货品表tflow_plan_goods中有无从仓库发货的记录（FPURCHASE=0），
			 * 如果有存在记录，则在计算净支出的时候增加多一条记录，
			 * 支出金额为计划货品表tflow_plan_goods的货品数量FGOODS_QUENTITY乘以发货地FDELIVERY_PLACE关联仓库的货品成本单价
			 * ，支出日期为运输日期；
			 */
			List<PlanGoods> list = planGoodsRepository.queryByPlanId(capitalPlan.getRelationId());
			if(list.size()>0){
				for (PlanGoods planGoods : list) {
					Integer purchase = planGoods.getPurchase();
					if(purchase==0){
						BigDecimal goodsQuentity = planGoods.getGoodsQuentity();//货品数量
						String deliveryPlace = planGoods.getDeliveryPlace().getFid();
						String goodsId = planGoods.getGoods().getFid();
						String goodsSpecId = planGoods.getGoodsSpec()==null?"":planGoods.getGoodsSpec().getFid();
						FreightAddress freightAddress = freightAddressService.findOne(deliveryPlace);
						String attrWarehouse = freightAddress.getWarehouse().getFid();//仓库id
						String accId = SecurityUtil.getFiscalAccountId();
						StockStore stockStore = stockStoreService.queryLastStock(accId,attrWarehouse, goodsId, goodsSpecId);
						if(stockStore!=null){
							BigDecimal accountPrice = stockStore.getAccountPrice();//货品成本单价
							/*支出金额*/
							BigDecimal zc=goodsQuentity.multiply(accountPrice);
							EveryTradeBean trade = new EveryTradeBean();
							trade.setMoney(zc.doubleValue());
							trade.setTime(planGoods.getTransportDate());
							real.getExpendList().add(trade);
						}

					}
				}
			}
			Calculate.initRateBean(real);
			try {
				Calculate.outRateBean(real);
				RateResult result = new RateResult();
				result.setRealRate(ArithUtil.round(real.getRate(),2));
				result.setCycleRate(ArithUtil.round(real.getCycleRate(),2));//周期收益率
				result.setProfitRate(new BigDecimal(real.getRate()/100));
				return result;
			} catch (Exception e) {
				return null;
			}
		}else{
			return null;
		}

	}
	/**
	 * 实际收益率
	 * @param planId 流程计划ID
	 * @return
	 */
	public RateResult getEffectiveYieldRate(String planId){
		/*
		 * 当前预计收益率、实际收益率：这两个收益率取数大致一样，当流程计划状态为完成或终止时，只取实际发生数来计算，当计划还在进行中，则净收入NP、
		 * 净支出NC、有效天数T的取数都是取实际发生的收付记录+预计发生的收付记录，只有净收益NRV不一样，当前预计收益率的净收益NRV=
		 * 实际发生的收付记录+预计发生的收付记录，而实际收益率的净收益NRV=实际发生的收付记录；

		 * 实际发生的收付记录取流程计划中的事件关联的收付款单，金额取收付款单的金额，日期取收付单的单据日期，付款单的金额要乘-1处理；
		 */
		double rate=loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());
		RateBean real = new RateBean();
		real.setDayFundLostRate(rate);
		List<TaskBillVo> taskBillList = taskBillService.queryByPlanId(planId);
		for (TaskBillVo taskBillVo : taskBillList) {
			Integer billSign = taskBillVo.getBillSign();
			String billId = taskBillVo.getBillId();
			if(billSign==WarehouseBuilderCodeHelper.skd||billSign==WarehouseBuilderCodeHelper.fkd){
				EveryTradeBean trade = new EveryTradeBean();
				PaymentBill paymentBill = paymentBillService.findOne(billId);
				if(paymentBill!=null){
					trade.setMoney(paymentBill.getAmount().doubleValue());
					trade.setTime(paymentBill.getBillDate());
					trade.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);
					if(paymentBill.getAmount().compareTo(BigDecimal.ZERO)>0){
						real.getIncomeList().add(trade);
					}else{
						trade.setMoney(paymentBill.getAmount().abs().doubleValue());
						real.getExpendList().add(trade);
					}
				}

			}
		}
		Calculate.initRealRateBean(real);
//		Calculate.initRateBean(real);
		try {
			Calculate.outRateBean(real);
			RateResult result = new RateResult();
			result.setRealRate(ArithUtil.round(real.getRate(),2));
			result.setCycleRate(ArithUtil.round(real.getCycleRate(),2));//周期,实际，当前预计收益率
			result.setProfitRate(new BigDecimal(real.getRate()/100));
			result.setActualRate(ArithUtil.round(real.getRate(),2));
			return result;
		} catch (Exception e) {
			return null;
		}
		
		
	}
	/**
	 * 当前预计收益率
	 * @param capitalPlan
	 * @return
	 */
	public RateResult getCurrentYieldRate(CapitalPlan capitalPlan){
		/*
		 * 预计发生的收付记录取流程计划对应资金计划明细表中的记录状态不为未完成和取消，且计划金额的绝对值大于收付款金额的绝对值的记录，
		 * 金额取计划金额减收付款金额，日期判断预计收付款日期大于当前日期，则日期=预计收付款日期，否则日期=当前日期；
		 */
		if(capitalPlan!=null){
			List<CapitalPlanDetail> records = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
			double rate=loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());

			RateBean real = new RateBean();
			real.setDayFundLostRate(rate);
			for(CapitalPlanDetail record:records){
				Integer relationSign = record.getRelationSign();
				if(relationSign!=CapitalPlan.STATUS_CANCEL&&relationSign!=CapitalPlan.STATUS_COMPLETE){
					BigDecimal planAmount = record.getPlanAmount().abs();
					BigDecimal paymentAmount = record.getPaymentAmount().abs();
					Date paymentDate = record.getPaymentDate();
					Date nowDate = DateUtils.getDateFromString(DateUtils.getCurrentDate());
					if(planAmount.compareTo(paymentAmount)>0){
						EveryTradeBean trade = new EveryTradeBean();
						BigDecimal subtract = planAmount.subtract(paymentAmount);
						trade.setMoney(subtract.doubleValue());
						if(paymentDate.compareTo(nowDate)>0){
							trade.setTime(paymentDate);
						}else{
							trade.setTime(nowDate);
						}
						if(record.getPlanAmount().compareTo(BigDecimal.ZERO)>0){
							real.getIncomeList().add(trade);
							
						}else{
							trade.setMoney(record.getPlanAmount().abs().doubleValue());
							real.getExpendList().add(trade);
						}
					}
				}

			}
			Calculate.initRateBean(real);
			try {
				Calculate.outRateBean(real);
				RateResult result = new RateResult();
				result.setRealRate(ArithUtil.round(real.getRate(),2));//
				result.setCycleRate(ArithUtil.round(real.getCycleRate(),2));//周期,实际，当前预计收益率
				result.setProfitRate(new BigDecimal(real.getRate()/100));
				return result;
			} catch (Exception e) {
				return null;
			}
		}else{
			return null;
		}

	}

	
	/**
	 * 计算流程计划每天收益率
	 * @param planId
	 * @param orgId
	 * @param fiscalAccount
	 * @param type			1、流程计划终止，完成时调用；2、每天轮询执行
	 * @return
	 */
	@Transactional
	public RequestResult stockInOut(String planId,String orgId,String fiscalAccount,Integer type){
		RateBean shijiReal = new RateBean();
		try {
			Plan plan = findOne(planId);
			Integer status = plan.getStatus();
//			List<WarehouseBillDetail> inList = Lists.newArrayList();
//			List<WarehouseBillDetail> outList = Lists.newArrayList();
			double rate = loanRateService.getBankRate(orgId);
			RateBean real = new RateBean();
			real.setDayFundLostRate(rate);
			shijiReal.setDayFundLostRate(rate);
			real.setFirstPayDay(plan.getAntipateStartTime());
			shijiReal.setFirstPayDay(plan.getAntipateStartTime());
			/* 当流程计划状态为完成或终止时，要判断所有仓库的货品进出数【流程计划中所有事件绑定的单据】 */
			if (type==1) {
				if(status == Plan.STATUS_STOPED || status == Plan.STATUS_FINISHED){
				/** 根据计划id获取当前预计收益率和实际收益率（公共部分）**/
				getProfit(planId, real, shijiReal,1);
				//根据计划查询仓库货品(全部仓库)
				List<WarehouseBillDetail> findGoodsByPlan = billDetailService.findGoodsByPlan(planId);
				Map<String,List<WarehouseBillDetail>> maps = Maps.newHashMap();
				//根据仓库+货品+属性组成数据集。
				for (WarehouseBillDetail detail : findGoodsByPlan) {
					String goodsId = detail.getGoods().getFid();
					String goodsSpecId = detail.getGoodsSpec()==null?"":detail.getGoodsSpec().getFid();
					String inWareHouse = detail.getInWareHouse()==null?"":detail.getInWareHouse().getFid();// 辅助属性仓库id
					String key = goodsId+"-"+goodsSpecId+"-"+inWareHouse;
					List<WarehouseBillDetail> list = maps.get(key);
					if(list==null){
						List<WarehouseBillDetail> planGood = Lists.newArrayList(detail);
						maps.put(key, planGood);
					}
					else{
						//仓库+货品+属性为一个key，过滤相同货品下的重复数据集。
						list.add(detail);
						maps.put(key, list);
					}
				}
				for (String key : maps.keySet()) {
					List<WarehouseBillDetail> list = maps.get(key);
					WarehouseBillDetail billDetail = list.get(0);
					String goodsId = billDetail.getGoods().getFid();
					String goodsSpecId = billDetail.getGoodsSpec()==null?"":billDetail.getGoodsSpec().getFid();
					String inWareHouse = billDetail.getInWareHouse()==null?"":billDetail.getInWareHouse().getFid();// 辅助属性仓库id
					List<WarehouseBillDetail> inWarehouseList = billDetailService.findGoodsAndWarehouse(planId,inWareHouse, goodsId, goodsSpecId);
					BigDecimal inQuentitySum = BigDecimal.ZERO;
					BigDecimal outQuentitySum = BigDecimal.ZERO;
					List<WarehouseBillDetail> inList = Lists.newArrayList();
					List<WarehouseBillDetail> outList = Lists.newArrayList();
					for (WarehouseBillDetail detail : inWarehouseList) {
						WarehouseBill bill = detail.getBill();
						Integer billType = bill.getBillType();
						BigDecimal quentity = detail.getAccountQuentity();
						if (billType == WarehouseBuilderCodeHelper.cgrk
								|| billType == WarehouseBuilderCodeHelper.shd
								|| billType == WarehouseBuilderCodeHelper.xsth) {
							inList.add(detail);
							inQuentitySum = inQuentitySum.add(quentity);
						} else if (billType == WarehouseBuilderCodeHelper.pdd) {
							if (quentity.compareTo(BigDecimal.ZERO) > 0) {
								inList.add(detail);
								inQuentitySum = inQuentitySum.add(quentity.abs());
							} else {
								outList.add(detail);
								outQuentitySum = outQuentitySum.add(quentity.abs());
							}
						}
						else if(billType == WarehouseBuilderCodeHelper.cgth||billType == WarehouseBuilderCodeHelper.fhd
								||billType == WarehouseBuilderCodeHelper.bsd||billType == WarehouseBuilderCodeHelper.xsch){
							outList.add(detail);
							outQuentitySum = outQuentitySum.add(quentity);
						}
					}
					//出仓不等于进仓才计算进出仓笔数
					if(outQuentitySum.compareTo(inQuentitySum)!=0){
						warehouseCalculation(inList, outList, inQuentitySum, outQuentitySum, real, shijiReal,type);
					}
				}

				
			  }
			} else {
				if (status == Plan.STATUS_TO_CHECK || status == Plan.STATUS_EXECUTING
						|| status == Plan.STATUS_DELAYED) {
					/*
					 * 当计划还在进行中，则净收入NP、净支出NC、有效天数T的取数都是取实际发生的收付记录+预计发生的收付记录，
					 * 只有净收益NRV不一样， 当前预计收益率的净收益NRV=实际发生的收付记录+预计发生的收付记录，
					 * 而实际收益率的净收益NRV=实际发生的收付记录；
					 */
					// 根据计划id获取当前预计收益率和实际收益率（公共部分）
					getProfit(planId, real, shijiReal, 2);
					/*
					 * 当前预计收益率在流程计划状态不是完成或终止时，
					 * 要判断计划对应的计划货品表tflow_plan_goods中有无从仓库发货的记录（FPURCHASE=0），
					 * 如果有存在记录，则要判断记录货品的发货地关联仓库的进出数；进出数不相等时，要把差值的成本金额当支出项处理；
					 */
					List<PlanGoods> planGoods = planGoodsRepository.queryByPlanId(planId);
					Map<String,List<WarehouseBillDetail>> maps = Maps.newHashMap();
					for (PlanGoods goods : planGoods) {
						List<WarehouseBillDetail> inList2 = Lists.newArrayList();
						List<WarehouseBillDetail> outList2 = Lists.newArrayList();
						Integer purchase = goods.getPurchase();
						String goodsId = goods.getGoods().getFid();
						String goodsSpecId = goods.getGoodsSpec()==null?"":goods.getGoodsSpec().getFid();
						if(purchase!=0) continue;
						FreightAddress deliveryPlace = goods.getDeliveryPlace();
						if(deliveryPlace==null) continue;
						String attrWarehouse = deliveryPlace.getWarehouse().getFid();// 辅助属性仓库id
						String key = goodsId+"-"+goodsSpecId+"-"+attrWarehouse;
						System.err.println(key);
						List<WarehouseBillDetail> list = maps.get(key);
						if(list!=null){
							continue;
						}
						List<WarehouseBillDetail> inWarehouseList = billDetailService.findGoodsAndWarehouse(planId,attrWarehouse, goodsId, goodsSpecId);
						maps.put(key, inWarehouseList);
						BigDecimal inQuentitySum = BigDecimal.ZERO;
						BigDecimal outQuentitySum = BigDecimal.ZERO;
						for (WarehouseBillDetail detail : inWarehouseList) {
							WarehouseBill bill = detail.getBill();
							Integer billType = bill.getBillType();
							Integer recordStatus = bill.getRecordStatus();
							BigDecimal quentity = detail.getAccountQuentity();
							if(recordStatus!=1)continue;
							if (billType == WarehouseBuilderCodeHelper.cgrk
									|| billType == WarehouseBuilderCodeHelper.shd
									|| billType == WarehouseBuilderCodeHelper.xsth) {
								inList2.add(detail);
								inQuentitySum = inQuentitySum.add(quentity);
							} else if (billType == WarehouseBuilderCodeHelper.pdd) {
								if (quentity.compareTo(BigDecimal.ZERO) > 0) {
									inList2.add(detail);
									inQuentitySum = inQuentitySum.add(quentity.abs());
								} else {
									outList2.add(detail);
									outQuentitySum = outQuentitySum.add(quentity.abs());
								}
							}
							else if(billType == WarehouseBuilderCodeHelper.cgth||billType == WarehouseBuilderCodeHelper.fhd
									||billType == WarehouseBuilderCodeHelper.bsd||billType == WarehouseBuilderCodeHelper.xsch){
								outList2.add(detail);
								outQuentitySum = outQuentitySum.add(quentity);
							}
						}
						if(inWarehouseList.size()<1){
							continue;
						}
						/* 要判断计划对应的计划货品表tflow_plan_goods中有无从仓库发货的记录（FPURCHASE=0），
						 * 如果有存在记录，再判断流程计划的事件关联的发货单是否有发货地=flow_plan_gotods中发货地FDELIVERY_PLACE的记录，
						 * 如果tflow_plan_goods的FPURCHASE=0存在记录且没有相应的发货单记录，
						 * 则在计算预计净支出的时候增加多一条记录，
						 * 支出金额为计划货品表tflow_plan_goods的货品数量FGOODS_QUENTITY乘以发货地FDELIVERY_PLACE关联仓库的货品成本单价
						 * ，支出日期为运输日期（参照预计收益率做法）；注意：该项只针对 当前预计收益率计算，实际收益率不用计算该操作；
						 */
						//出仓大于进仓才计算进出仓笔数
						if(outQuentitySum.compareTo(inQuentitySum)>0){
							warehouseCalculation(inList2, outList2, inQuentitySum, outQuentitySum, real, shijiReal,type);
						}
						//出仓小于进仓，则在计算预计净支出的时候增加多一条记录，
						else if(outQuentitySum.compareTo(inQuentitySum)<0){
							StockStore stockStore = stockStoreService.queryLastStock(fiscalAccount,attrWarehouse, goodsId,goodsSpecId);
							if(stockStore!=null){
								BigDecimal accountPrice = stockStore.getAccountPrice();// 货品成本单价
								BigDecimal goodsQuentity = goods.getGoodsQuentity();// 货品数量
								/* 支出金额 */
								BigDecimal zc = goodsQuentity.multiply(accountPrice);
								EveryTradeBean trade = new EveryTradeBean();
								trade.setMoney(zc.doubleValue());
								trade.setTime(goods.getTransportDate());
								real.getExpendList().add(trade);
							}

						}
					}

				}else{
					return buildSuccessRequestResult();
				}
			}
			// 当前预计收益率
			Calculate.initRateBean(real);
			double yuji = real.getCycleRate();
			// 实际收益率
			Calculate.initRealRateBean(shijiReal);
			double shiji = shijiReal.getCycleRate();
			// 设置计划当前预计收益率
			plan.setCurrentYieldRate(new BigDecimal(yuji));
			// 设置计划实际收益率
			plan.setEffectiveYieldrate(new BigDecimal(shiji));
			// 设置计划参考收益率【取当天的资金日损率】
			/*参考收益率=资金日损率*30*100%*/
			BigDecimal cankaoRate = new BigDecimal(rate);
			cankaoRate = cankaoRate.multiply(new BigDecimal(30)).multiply(new BigDecimal(100));
			plan.setReferenceYieldrate(cankaoRate);
			// 维护流程计划收益率
			planRepository.save(plan);

			Date currentDate = DateUtils.getDateFromString(DateUtils.getCurrentDate());
			// 流程计划每天收益率
			YieldRate yieldRate = yieldRateService.queryByPlanAndDate(planId, currentDate);
			YieldRate yieldRate2 = null;
			if (yieldRate == null) {
				yieldRate2 = new YieldRate();
				yieldRate2.setCurrentYieldRate(new BigDecimal(yuji));
				yieldRate2.setDate(currentDate);
				yieldRate2.setEffectiveYieldrate(new BigDecimal(shiji));
				yieldRate2.setReferenceYieldrate(cankaoRate);
				yieldRate2.setFiscalAccount(accountRepository.findOne(fiscalAccount));
				yieldRate2.setOrganization(organizationRepository.findOne(orgId));
				yieldRate2.setPlan(plan);
				yieldRate2.setUpdateTime(new Date());
			} else {
				yieldRate2 = yieldRate;
				yieldRate2.setCurrentYieldRate(new BigDecimal(yuji));
				yieldRate2.setEffectiveYieldrate(new BigDecimal(shiji));
				yieldRate2.setReferenceYieldrate(cankaoRate);
				yieldRate2.setUpdateTime(new Date());
			}
			yieldRateService.save(yieldRate2);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("流程计划日收益率出错!");
		}

		return buildSuccessRequestResult(shijiReal);
	}
	/**
	 * 根据计划id获取预计收益率和实际收益率（公共部分）
	 * @param planId
	 * @param yuji
	 * @param shiji
	 * @param type	类型：1-计划结束完成；2-每天轮询（当前收益率，实际收益率）
	 */
	public void getProfit(String planId,RateBean yuji,RateBean shiji,Integer type){
		List<TaskBillVo> taskBillList = taskBillService.queryByPlanId(planId);
		for (TaskBillVo taskBillVo : taskBillList) {
			String billId = taskBillVo.getBillId();
			if(Strings.isNullOrEmpty(billId)) continue;
			Integer billSign = taskBillVo.getBillSign();
			/*
			 * 当前预计收益率、实际收益率：这两个收益率取数大致一样，当流程计划状态为完成或终止时，只取实际发生数来计算，当计划还在进行中，则净收入NP、
			 * 净支出NC、有效天数T的取数都是取实际发生的收付记录+预计发生的收付记录，只有净收益NRV不一样，当前预计收益率的净收益NRV=
			 * 实际发生的收付记录+预计发生的收付记录，而实际收益率的净收益NRV=实际发生的收付记录；

			 * 实际发生的收付记录取流程计划中的事件关联的收付款单，金额取收付款单的金额，日期取收付单的单据日期，付款单的金额要乘-1处理；
			 */
			if(billSign==WarehouseBuilderCodeHelper.skd||billSign==WarehouseBuilderCodeHelper.fkd){
				EveryTradeBean yujiTrade = new EveryTradeBean();
				EveryTradeBean shijiTrade = new EveryTradeBean();
				PaymentBill paymentBill = paymentBillService.findOne(billId);
				if(paymentBill!=null){
					Integer billType = paymentBill.getBillType();
					yujiTrade.setMoney(paymentBill.getAmount().doubleValue());
					yujiTrade.setTime(paymentBill.getBillDate());
					shijiTrade.setMoney(paymentBill.getAmount().doubleValue());
					shijiTrade.setTime(paymentBill.getBillDate());
					shijiTrade.setAmountType(EveryTradeBean.AMOUNT_TYPE_REAL);
					if(billType==PaymentBill.TYPE_INCOME){
						yuji.getIncomeList().add(yujiTrade);
						shiji.getIncomeList().add(shijiTrade);
					}else{
						yujiTrade.setMoney(paymentBill.getAmount().doubleValue());
						shijiTrade.setMoney(paymentBill.getAmount().doubleValue());
						yuji.getExpendList().add(yujiTrade);
						shiji.getExpendList().add(shijiTrade);
					}
				}
			}
		}
		/*
		 * 预计发生的收付记录取流程计划对应资金计划明细表中的记录状态不为完成和取消，
		 * 且计划金额的绝对值大于收付款金额的绝对值的记录，金额取计划金额减收付款金额，
		 * 日期判断预计收付款日期大于当前日期，则日期=预计收付款日期，否则日期=当前日期；
		 */
		if(type==2){//每天轮询才需要算预计
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(planId);
			if(capitalPlan!=null){
				List<CapitalPlanDetail> capitalPlanDetail = capitalPlanDetailService.queryByCapitalId(capitalPlan.getId());
				for (CapitalPlanDetail detail : capitalPlanDetail) {
					Integer recordStatus = detail.getRecordStatus();
					BigDecimal planAmount = detail.getPlanAmount();
					BigDecimal paymentAmount = detail.getPaymentAmount();
					Date paymentDate = detail.getPaymentDate();//预计收付款日期
					Date today = DateUtils.getDateFromString(DateUtils.getCurrentDate());
					Date date = paymentDate.compareTo(today)>0?paymentDate:today;
						if (CapitalPlan.STATUS_EXECUTING == recordStatus|| 
							CapitalPlan.STATUS_BAD_DEBT == recordStatus) {
							int compareTo = planAmount.abs().compareTo(paymentAmount.abs());
							if(compareTo>0){
								BigDecimal subtract = planAmount.subtract(paymentAmount);
								EveryTradeBean yujiTrade = new EveryTradeBean();
								yujiTrade.setMoney(subtract.doubleValue());
								yujiTrade.setTime(date);
								EveryTradeBean shijiTrade = new EveryTradeBean();
								shijiTrade.setMoney(subtract.doubleValue());
								shijiTrade.setTime(date);
								if(subtract.compareTo(BigDecimal.ZERO)>0){
									yuji.getIncomeList().add(yujiTrade);
									shiji.getIncomeList().add(shijiTrade);
								}else{
									yujiTrade.setMoney(subtract.abs().doubleValue());
									shijiTrade.setMoney(subtract.abs().doubleValue());
									yuji.getExpendList().add(yujiTrade);
									shiji.getExpendList().add(shijiTrade);
								}
							}
					}
				}
			}
		}

	}
	/**
	 * 仓库计算
	 * @param inList			进仓列表
	 * @param outList			出仓列表
	 * @param inQuentitySum		进仓货品总数
	 * @param outQuentitySum	出仓货品总数
	 * @param real				预计收益率
	 * @param shijiReal			实际收益率
	 * @param type				类型：1-计划结束完成；2-每天轮询（当前收益率，实际收益率）
	 */
	public void warehouseCalculation(
			List<WarehouseBillDetail> inList, 
			List<WarehouseBillDetail> outList, 
			BigDecimal inQuentitySum,
			BigDecimal outQuentitySum, 
			RateBean real, 
			RateBean shijiReal,Integer type) {
		int compareTo = inQuentitySum.compareTo(outQuentitySum);
		/* 如果入仓数大于出仓数，记录X=入仓数-出仓数，再从日期排序，取该仓的入仓单据，用入仓单据数量减X， */
		BigDecimal stockX = inQuentitySum.subtract(outQuentitySum);// 仓库库存多少
		/*
		 * 如果出仓数大于入仓数，记录Y=出仓数-入仓数，再从日期排序，取该仓的出仓单据，用出仓单据数量减Y，
		 * 如果出仓单据数量大于Y，Cm=-1*Y
		 **/
		BigDecimal stockY = outQuentitySum.subtract(inQuentitySum);// 仓库库存多少
		MyCompartor mc = new MyCompartor();
		Collections.sort(inList, mc); // 按照单据日期升序
		Collections.reverse(inList); // 按照单据日期将序
		Collections.sort(outList, mc);
		Collections.reverse(outList);
		if (compareTo != 0) {
			/*
			 * 如果入仓数大于出仓数，记录X=入仓数-出仓数，再从日期排序，取该仓的入仓单据，用入仓单据数量减X，
			 * X=X-入仓单据数量， 再取下一个入仓单据，再执行以上操作，直到X=0；
			 */
			if (compareTo > 0) {
				// 循环入仓记录
				for (WarehouseBillDetail billDetail2 : inList) {
					if(stockX.compareTo(BigDecimal.ZERO)<0) continue;
					BigDecimal accountQuentity = billDetail2.getAccountQuentity().abs();// 获取记账数量
					BigDecimal costAmount = billDetail2.getCostAmount().abs();// 成本金额
					BigDecimal costPrice = billDetail2.getCostPrice().abs();
					BigDecimal accountAmount = billDetail2.getType();// 记账金额
					BigDecimal accountUintPrice = billDetail2.getAccountUintPrice();
					EveryTradeBean trade = new EveryTradeBean();
					/* 如果入仓单据数量不大于X，Cm=入仓单据的成本金额，日期=入仓单据日期， */
					if (stockX.compareTo(accountQuentity) >= 0) {
						if (costAmount != null && costAmount.compareTo(BigDecimal.ZERO) != 0) {
							trade.setMoney(costAmount.doubleValue());
						} else {
							if (accountAmount != null && accountAmount.compareTo(BigDecimal.ZERO) != 0) {
								trade.setMoney(accountAmount.doubleValue());
							} else {
								trade.setMoney(accountUintPrice.multiply(accountQuentity).doubleValue());
							}
						}
						trade.setTime(billDetail2.getBill().getBillDate());
						stockX = stockX.subtract(accountQuentity);
						
						if(type==1){
							trade.setMoney(trade.getMoney()*-1);
							real.getExpendList().add(trade);
							trade.setAmountType(1);
							shijiReal.getExpendList().add(trade);
						}else{
							real.getExpendList().add(trade);
						}
						
					} else {
						/* 如果入仓单据数量大于X，Cm=X*入仓单据的成本单价，日期=入仓单据日期， */
						trade.setMoney(stockX.multiply(costPrice).doubleValue());
						trade.setTime(billDetail2.getBill().getBillDate());
						stockX = stockX.subtract(accountQuentity);
						if(type==1){
							trade.setMoney(trade.getMoney()*-1);
							real.getExpendList().add(trade);
							trade.setAmountType(1);
							shijiReal.getExpendList().add(trade);
						}
					}
				}
			} else if (compareTo < 0) {// 如果出仓数大于入仓数
				/*
				 * 如果出仓数大于入仓数，记录Y=出仓数-入仓数，再从日期排序，取该仓的出仓单据，用出仓单据数量减Y，
				 * 如果出仓单据数量大于Y，Cm=-1*Y*
				 * 出仓单据的成本单价，日期=出仓单据日期，如果出仓单据数量不大于Y，Cm=出仓单据的成本金额*-1，
				 * 日期=出仓单据日期，Y=Y-出仓单据数量 ，再取下一个出仓单据，再执行以上操作，直到Y=0；
				 */
				// 循环出仓记录
				for (WarehouseBillDetail billDetail2 : outList) {
					if(stockY.compareTo(BigDecimal.ZERO)<0) continue;
					BigDecimal accountQuentity = billDetail2.getAccountQuentity().abs();// 获取记账数量
					BigDecimal costAmount = billDetail2.getCostAmount().abs();// 成本金额
					BigDecimal costPrice = billDetail2.getCostPrice().abs();
					BigDecimal accountAmount = billDetail2.getType();// 记账金额
					BigDecimal accountUintPrice = billDetail2.getAccountUintPrice();
					EveryTradeBean trade = new EveryTradeBean();
					/* 如果出仓单据数量不大于Y，Cm=出仓单据的成本金额*-1，日期=出仓单据日期 */
					if (stockY.compareTo(accountQuentity) >= 0) {
						if (costAmount != null && costAmount.compareTo(BigDecimal.ZERO) != 0) {
							trade.setMoney(costAmount.doubleValue());
						} else {
							if (accountAmount != null && accountAmount.compareTo(BigDecimal.ZERO) != 0) {
								trade.setMoney(accountAmount.doubleValue());
							} else {
								BigDecimal multiply = accountUintPrice.multiply(accountQuentity);
								trade.setMoney(multiply.doubleValue());
							}
						}
						trade.setTime(billDetail2.getBill().getBillDate());
						stockY = stockY.subtract(accountQuentity);
						real.getExpendList().add(trade);
						if(type==1){
							trade.setAmountType(1);
						}
						shijiReal.getExpendList().add(trade);

					} else {
						/*
						 * 如果出仓单据数量大于Y，Cm=-1*Y*出仓单据的成本单价，日期=出仓单据日期
						 */
						BigDecimal multiply = stockY.multiply(costPrice);
						trade.setMoney(multiply.doubleValue());
						trade.setTime(billDetail2.getBill().getBillDate());
						stockY = stockY.subtract(accountQuentity);
						real.getExpendList().add(trade);
						if(type==1){
							trade.setAmountType(1);
						}
						shijiReal.getExpendList().add(trade);
					}
				}
			}
		}
	}
	class MyCompartor implements Comparator<WarehouseBillDetail>
	{
	     @Override
	     public int compare(WarehouseBillDetail o1, WarehouseBillDetail o2)
	    {
				WarehouseBillDetail billDetail= (WarehouseBillDetail )o1;
				WarehouseBillDetail billDetail2= (WarehouseBillDetail )o2;
				Date billDate = billDetail.getBill().getBillDate();
				Date billDate2 = billDetail2.getBill().getBillDate();
				
	           return billDate.compareTo(billDate2);

	    }
	}
	
	/**
	 * 收益率轮询
	 */
	@Transactional
	public void yieldPolling()throws Exception{
		//轮询机构
		List<Organization> organizations = organizationRepository.queryRootOrganization();
		for (Organization organization : organizations) {
			String orgId = organization.getFid();
			Sort sort = new Sort(Direction.DESC, "name");
			List<FiscalAccount> fiscalAccounts = accountRepository.findByOrgId(orgId, sort);
			for (FiscalAccount fiscalAccount : fiscalAccounts) {
				String fiscalAccountId = fiscalAccount.getFid();
				List<Plan> planList = planRepository.queryPlanList(orgId, fiscalAccountId);
				for (Plan plan : planList) {
					String planId = plan.getFid();
					RequestResult stockInOut = stockInOut(planId, orgId, fiscalAccountId,2);
					if(stockInOut.getReturnCode()==1){
						throw new Exception("收益率轮询出错!");
					}
				}
			}
		}
	}
}
