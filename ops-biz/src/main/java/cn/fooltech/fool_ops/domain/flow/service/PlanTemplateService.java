package cn.fooltech.fool_ops.domain.flow.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateDetail;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.domain.flow.repository.PlanTemplateDetailRepository;
import cn.fooltech.fool_ops.domain.flow.repository.PlanTemplateRepository;
import cn.fooltech.fool_ops.domain.flow.repository.TaskPlantemplateDetailRepository;
import cn.fooltech.fool_ops.domain.flow.repository.TaskTemplateRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateDetailVo;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>计划模板网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:45:13
 */
@Service
public class PlanTemplateService extends BaseService<PlanTemplate,PlanTemplateVo,String> {
	
	/**
	 * 计划模板服务类
	 */
	@Autowired
	private PlanTemplateRepository repository;
	/**
	 * 计划事件关联模板服务类
	 */
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	/**
	 * 计划模板明细服务类
	 */
	@Autowired
	private PlanTemplateDetailRepository detailRepository;
	
	/**
	 * 计划模板明细Web服务类
	 */
	@Autowired
	private PlanTemplateDetailService detailService;
	
	/**
	 * 事件级别服务类
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	/**
	 * 明细服务类
	 */
	@Autowired
	private TaskPlantemplateDetailRepository taskPlantemplateDetailRepository;
	
	/**
	 * 保密级别服务类
	 */
	@Autowired
	private SecurityLevelService securityLevelService;
	
	/**
	 * 用户服务类
	 */
	@Autowired
	private UserService userService;

	/**
	 * 计划模板关联服务类
	 */
	@Autowired
	private PlanTemplateRelationService relationService;
	
	/**
	 * 查询计划模板列表信息，按照计划模板主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<PlanTemplate> query(PlanTemplateVo vo,PageParamater pageParamater){
		
		Sort sort = new Sort(Direction.DESC,"createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<PlanTemplate> page = repository.query(vo, request);
		return page;
	}
	
	
	/**
	 * 单个计划模板实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public PlanTemplateVo getVo(PlanTemplate entity){
		if(entity == null)
			return null;
		PlanTemplateVo vo = new PlanTemplateVo();
		vo.setCode(entity.getCode());
		vo.setDays(entity.getDays());
		vo.setDescribe(entity.getDescribe());
		vo.setCreatorName(entity.getCreator().getUserName());
		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		vo.setStatus(entity.getStatus());
		vo.setName(entity.getName());
		vo.setFid(entity.getFid());
		SecurityLevel securityLevel = entity.getSecurityLevel();
		if(securityLevel != null) {
			vo.setSecurityLevelId(securityLevel.getFid());
			vo.setSecurityLevelName(securityLevel.getName());
		}
		TaskLevel level = entity.getTaskLevel();
		if(level!=null){
			vo.setTaskLevelId(level.getFid());
			vo.setTaskLevelName(level.getName());
		}
		User principaler = entity.getPrincipaler();
		if(principaler!=null){
			vo.setPrincipalerId(principaler.getFid());
			vo.setPrincipalerName(principaler.getUserName());

			Organization dept = principaler.getOrgId();
			if(dept!=null){
				vo.setDeptId(dept.getFid());
				vo.setDeptName(dept.getOrgName());
			}
		}
		User undertaker = entity.getUndertaker();
		if(undertaker!=null){
			vo.setUndertakerId(undertaker.getFid());
			vo.setUndertakerName(undertaker.getUserName());
		}

		/**
		 * @author lsl
		 * @updateTime 2017-03-02
		 */
		vo.setUpdateTime(DateUtil.format(entity.getUpdateTime(), DATE_TIME));

		return vo;
	}

	/**
	 * 删除计划模板<br>
	 */
	@Transactional
	public RequestResult delete(String fid){
/*		Long count=taskTemplateRepository.countPlantemplateId(fid)+taskPlantemplateDetailRepository.countPlantemplateId(fid);
		if(count>0){
			return buildFailRequestResult("该计划模板已被引用");
		}*/
		detailService.deleteByTemplateId(fid);//删除旧的明细记录
		repository.delete(fid);
		relationService.deleteByTemplateId(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取计划模板信息
	 * @param fid 计划模板ID
	 * @return
	 */
	public PlanTemplateVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(repository.findOne(fid));
	}
	

	/**
	 * 新增/编辑计划模板
	 * @param vo
	 */
	@Transactional
	public RequestResult save(PlanTemplateVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		PlanTemplate entity = new PlanTemplate();
		String orgId = SecurityUtil.getCurrentOrgId();
		if(repository.countSameCode(vo.getFid(), orgId, vo.getCode())>0){
			return buildFailRequestResult("编号重复");
		}
		if(repository.countSameName(vo.getFid(), orgId, vo.getName())>0){
			return buildFailRequestResult("名称重复");
		}

		//判断模板是否有重复
		if(!Strings.isNullOrEmpty(vo.getDataId()) && vo.getTemplateType()!=null){

			Integer templateType = vo.getTemplateType();
			if(templateType==PlanTemplateVo.TEMPLATE_TYPE_SALE){
				if(relationService.settleSaleTemplate(vo.getDataId()).isSuccess()){
					return buildSuccessRequestResult("已有关联模板，不需重复新建");
				}
			}
		}
		
		Collection<PlanTemplateDetailVo> vos = detailService.getDetails(vo.getDetails());
		RequestResult result = detailService.recurseCheck(vos);
		if(result.getReturnCode()==RequestResult.RETURN_FAILURE){
			return result;
		}
		
	//	PlanTemplate entity = new PlanTemplate();
		if(StringUtils.isBlank(vo.getFid())){
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}else {
			entity = repository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}

			String updateTime = DateUtilTools.time2String(entity.getUpdateTime());
			if(!vo.getUpdateTime().equals(updateTime)){
				return  new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改");
			}

			/**
			 * @author lsl
			 * updateTime 2017-03-02
			 * 判断数据是否已被其他用户修改
			 */
			entity = transferVo(vo, entity);

			entity.setUpdateTime(Calendar.getInstance().getTime());
			detailService.deleteByTemplateId(vo.getFid());//删除旧的明细记录
		}
		
		entity.setCode(vo.getCode());
		entity.setName(vo.getName());
		entity.setDays(vo.getDays());
		entity.setDescribe(vo.getDescribe());
		entity.setStatus(PlanTemplate.STATUS_STOP);
		
		TaskLevel taskLevel = taskLevelService.get(vo.getTaskLevelId());
		entity.setTaskLevel(taskLevel);
		SecurityLevel securityLevel = securityLevelService.get(vo.getSecurityLevelId());
		entity.setSecurityLevel(securityLevel);
		if(!Strings.isNullOrEmpty(vo.getPrincipalerId())){
			User principaler = userService.get(vo.getPrincipalerId());
			entity.setPrincipaler(principaler);
		}
		if(!Strings.isNullOrEmpty(vo.getUndertakerId())){
			User undertaker = userService.get(vo.getUndertakerId());
			entity.setUndertaker(undertaker);
		}
		

		repository.save(entity);
		PlanTemplateDetail rootDetail = addRootTemplateDetail(entity);
		RequestResult s=detailService.saveTemplateDetails(vos, entity, null, rootDetail);
		if(s.getReturnCode()==1){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return buildFailRequestResult(s.getMessage()+"与其他事件(子事件)名称或序号重复"); 
		}


		RequestResult saveResult = buildSuccessRequestResult();
		if(!Strings.isNullOrEmpty(vo.getDataId()) && vo.getTemplateType()!=null){

			//TODO 保存计划模板时，添加判断条件
			//如果templateType=3，则根据dataId作为主键，查找CostAnalysisBill

			Integer templateType = vo.getTemplateType();
			if(templateType==PlanTemplateVo.TEMPLATE_TYPE_TRANSPORT){
				saveResult = relationService.saveWithCostAnalyzeBillDetailId(vo.getDataId(), templateType, entity);
			}else{
				saveResult = relationService.saveWithCostAnalyzeBillId(vo.getDataId(), templateType, entity);
			}
			entity.setStatus(PlanTemplate.STATUS_USE);
			repository.save(entity);
		}
		
		return saveResult;
	}
	
	/**
	 * 添加计划模板的根节点明细
	 * @param planTemplate
	 */
	@Transactional
	public PlanTemplateDetail addRootTemplateDetail(PlanTemplate planTemplate){
		PlanTemplateDetail rootDetail = new PlanTemplateDetail();
		rootDetail.setNumber((short)0);
		rootDetail.setPlanTemplate(planTemplate);
		rootDetail.setTaskName("root");
		rootDetail.setAmount(BigDecimal.ZERO);
		rootDetail.setAmountType(0);
		detailRepository.save(rootDetail);
		return rootDetail;
	}

	/**
	 * 启用
	 * @return
	 */
	public RequestResult updateUse(String id){
		if(Strings.isNullOrEmpty(id)){
			return buildFailRequestResult("id参数错误");
		}
		PlanTemplate entity = repository.findOne(id);
		if(entity==null){
			return buildFailRequestResult("数据已被其他用户删除，请刷新再试");
		}
		
		
		
		entity.setStatus(PlanTemplate.STATUS_USE);
		repository.save(entity);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 停用
	 * @return
	 */
	public RequestResult updateUnUse(String id){
		if(Strings.isNullOrEmpty(id)){
			return buildFailRequestResult("id参数错误");
		}
		PlanTemplate entity = repository.findOne(id);
		if(entity==null){
			return buildFailRequestResult("数据已被其他用户删除，请刷新再试");
		}
		entity.setStatus(PlanTemplate.STATUS_STOP);
		repository.save(entity);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 查找已启用的计划模板列表信息<br>
	 * @return
	 */
	public List<PlanTemplate> queryAll(PlanTemplateVo vo) {
		Sort sort = new Sort(Direction.DESC, "createTime");
		return repository.queryAll(SecurityUtil.getCurrentOrgId(),sort);
	}


	@Override
	public CrudRepository<PlanTemplate, String> getRepository() {
		return repository;
	}

	/**
	 * @author lsl
	 * @updateTime 2017-03-02
	 * vo转entity
	 */
	public PlanTemplate transferVo(PlanTemplateVo vo, PlanTemplate entity){
		entity.setCode(vo.getCode());
		entity.setName(vo.getName());
		entity.setDays(vo.getDays());
		entity.setDescribe(vo.getDescribe());
		entity.setStatus(PlanTemplate.STATUS_STOP);

		TaskLevel taskLevel = taskLevelService.get(vo.getTaskLevelId());
		entity.setTaskLevel(taskLevel);
		SecurityLevel securityLevel = securityLevelService.get(vo.getSecurityLevelId());
		entity.setSecurityLevel(securityLevel);
		if(!Strings.isNullOrEmpty(vo.getPrincipalerId())){
			User principaler = userService.get(vo.getPrincipalerId());
			entity.setPrincipaler(principaler);
		}
		if(!Strings.isNullOrEmpty(vo.getUndertakerId())){
			User undertaker = userService.get(vo.getUndertakerId());
			entity.setUndertaker(undertaker);
		}
		return  entity;
	}
}

