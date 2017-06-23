package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateDetail;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.flow.entity.TaskLevel;
import cn.fooltech.fool_ops.domain.flow.repository.PlanTemplateDetailRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateDetailVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * <p>
 * 计划模板明细网页服务类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2016-05-18 09:46:07
 */
@Service
public class PlanTemplateDetailService extends BaseService<PlanTemplateDetail, PlanTemplateDetailVo, String> {

	/**
	 * 计划模板明细持久类
	 */
	@Autowired
	private PlanTemplateDetailRepository detailRepository;

	/**
	 * 事件级别服务类
	 */
	@Autowired
	private TaskLevelService taskLevelService;
	
	/**
	 * 保密级别服务类
	 */
	@Autowired
	private SecurityLevelService securityLevelService;

	/**
	 * 用户持久类
	 */
	@Autowired
	private UserRepository userRepo;

	@Autowired
	OrganizationRepository organizationRepository;
	/**
	 * 比较器
	 */
	private Comparator<PlanTemplateDetailVo> comparatorVo = new Comparator<PlanTemplateDetailVo>() {

		@Override
		public int compare(PlanTemplateDetailVo o1, PlanTemplateDetailVo o2) {
			return o1.getNumber().compareTo(o2.getNumber());
		}
	};

	/**
	 * 查询计划模板明细列表信息，按照计划模板明细number升序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	public Page<PlanTemplateDetail> query(PlanTemplateDetailVo vo, PageParamater pageParamater) {
		Sort sort = new Sort(Direction.ASC, "number");
		PageRequest request = getPageRequest(pageParamater, sort);
		Page<PlanTemplateDetail> query = detailRepository.query(vo, request);
		return query;
	}

	/**
	 * 查询计划模板明细列表信息，按照计划模板明细number升序排列<br>
	 * 
	 * @param vo
	 */
	public List<PlanTemplateDetail> query(PlanTemplateDetailVo vo) {
		return detailRepository.query(SecurityUtil.getCurrentOrgId());
	}

	/**
	 * 单个计划模板明细实体转换为vo
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public PlanTemplateDetailVo getVo(PlanTemplateDetail entity) {
		if (entity == null)
			return null;
		PlanTemplateDetailVo vo = new PlanTemplateDetailVo();
		vo.setNumber(entity.getNumber());
		vo.setBillId(entity.getBillId());
		vo.setBillCode(entity.getBillCode());
		vo.setBillType(entity.getBillType());
		vo.setTaskName(entity.getTaskName());
		vo.setPreDays(entity.getPreDays());
		vo.setDays(entity.getDays());
		vo.setDescribe(entity.getDescribe());
		vo.setFid(entity.getFid());
		vo.setPlanTemplateId(entity.getPlanTemplate().getFid());
		vo.setParentId(entity.getParent() == null ? "" : entity.getParent().getFid());
		vo.setAmountType(entity.getAmountType());
		vo.setAmount(entity.getAmount());
		TaskLevel level = entity.getTaskLevel();
		if (level != null) {
			vo.setTaskLevelId(level.getFid());
			vo.setTaskLevelName(level.getName());
		}

		User undertaker = entity.getUndertaker();
		if (undertaker != null) {
			vo.setUndertakerId(undertaker.getFid());
			vo.setUndertakerName(undertaker.getUserName());
		}

		User principal = entity.getPrincipal();
		if (principal != null) {
			vo.setPrincipalId(principal.getFid());
			vo.setPrincipalName(principal.getUserName());
		}

		Organization dept = entity.getDept();
		if (dept != null) {
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}

		SecurityLevel securlevel = entity.getSecurityLevel();
		if (securlevel != null) {
			vo.setSecurityLevelId(securlevel.getFid());
			vo.setSecurityLevelName(securlevel.getName());
		}

		return vo;
	}

	/**
	 * 删除计划模板明细<br>
	 */
	public RequestResult delete(String fid) {
		detailRepository.delete(fid);
		return buildSuccessRequestResult();
	}

	/**
	 * 获取计划模板明细信息
	 * 
	 * @param fid
	 *            计划模板明细ID
	 * @return
	 */
	public PlanTemplateDetailVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(detailRepository.findOne(fid));
	}

	/**
	 * 新增/编辑计划模板明细
	 * 
	 * @param vo
	 */
	private PlanTemplateDetail save(PlanTemplateDetailVo vo, PlanTemplate template, PlanTemplateDetail parent,
			PlanTemplateDetail root) {

		PlanTemplateDetail entity = new PlanTemplateDetail();
		Long count1=detailRepository.countByShort(template.getFid(), vo.getNumber(),vo.getFid());
		if(count1>0){
			return null;
		}
		Long count2=detailRepository.countByTaskName(template.getFid(), vo.getTaskName(),vo.getFid());
		if(count2>0){
			return null;
		}

		entity.setNumber(vo.getNumber());
		
		entity.setTaskName(vo.getTaskName());
		entity.setDays(vo.getDays());
		entity.setPreDays(vo.getPreDays());
		entity.setDescribe(vo.getDescribe());
		entity.setPlanTemplate(template);
		entity.setAmountType(vo.getAmountType());
		entity.setAmount(vo.getAmount());
		if (parent == null) {
			entity.setParent(root);
		} else {
			entity.setParent(parent);
		}

		TaskLevel taskLevel = taskLevelService.get(vo.getTaskLevelId());
		entity.setTaskLevel(taskLevel);

		SecurityLevel securityLevel = securityLevelService.get(vo.getSecurityLevelId());
		entity.setSecurityLevel(securityLevel);

		if (!Strings.isNullOrEmpty(vo.getUndertakerId())) {
			User undertaker = userRepo.findOne(vo.getUndertakerId());
			entity.setUndertaker(undertaker);
		}

		if (!Strings.isNullOrEmpty(vo.getPrincipalId())) {
			User principal = userRepo.findOne(vo.getPrincipalId());
			entity.setPrincipal(principal);
		}

		if (!Strings.isNullOrEmpty(vo.getDeptId())) {
			Organization dept = organizationRepository.findOne(vo.getDeptId());
			entity.setDept(dept);
		}

		String billType = vo.getBillType() + "";
		if (!"".equals(billType)) {
			// WarehouseBill bill = billService.get(vo.getBillId());
			// if(bill!=null){
			// entity.setBillId(bill.getFid());
			// entity.setBillCode(bill.getCode());
			entity.setBillType(vo.getBillType());
			// }
		}
		detailRepository.save(entity);

		return entity;
	}

	/**
	 * 查找树列表
	 * 
	 * @param planTemplateId
	 * @return
	 */
	public List<PlanTemplateDetailVo> queryTree(String planTemplateId) {

		List<PlanTemplateDetailVo> vos = getVos(detailRepository.queryByTemplateId(planTemplateId));
		FastTreeUtils<PlanTemplateDetailVo> fastTreeUtils = new FastTreeUtils<PlanTemplateDetailVo>();
		return fastTreeUtils.buildTreeData(vos, 1, comparatorVo, new TreeRootCallBack<PlanTemplateDetailVo>() {

			@Override
			public boolean isRoot(PlanTemplateDetailVo v) {
				return Strings.isNullOrEmpty(v.getParentId()) ? true : false;
			}
		});
	}

	/**
	 * 保存列表明细
	 * 
	 * @param detailList
	 */
	public RequestResult saveTemplateDetails(Collection<PlanTemplateDetailVo> detailList, PlanTemplate template,
			PlanTemplateDetail parent, PlanTemplateDetail root) {
		for (PlanTemplateDetailVo iter : detailList) {
			PlanTemplateDetail curParent = save(iter, template, parent, root);
			RequestResult s=new RequestResult();
			if (iter.getChildren() != null) {
				s=saveTemplateDetails(iter.getChildren(), template, curParent, root);
				
			}
			if(curParent==null||s.getReturnCode()==1){
				return buildFailRequestResult(iter.getTaskName());
				
				
			}

		}
		
		return buildSuccessRequestResult();
	}
	/**
	 * 检查明细是否有重复
	 * @param detailList
	 * @param template
	 * @param parent
	 * @param root
	 * @return
	 */
	public String checkTemplateDetails(Collection<PlanTemplateDetailVo> detailList, PlanTemplate template,
			PlanTemplateDetail parent, PlanTemplateDetail root) {
		for (PlanTemplateDetailVo iter : detailList) {
			Long count1=detailRepository.countByShort(template.getFid(), iter.getNumber(),iter.getFid());
			if(count1>0){
				return null;
			}
			Long count2=detailRepository.countByTaskName(template.getFid(), iter.getTaskName(),iter.getFid());
			if(count2>0){
				return null;
			}
		}
		return "success";
	}

	/**
	 * 递归判断是否有非法记录
	 */
	public RequestResult recurseCheck(Collection<PlanTemplateDetailVo> detailList) {
		for (PlanTemplateDetailVo iter : detailList) {
			String inValid = ValidatorUtils.inValidMsg(iter);
			if (inValid != null) {
				return buildFailRequestResult(inValid);
			}

			if (iter.getChildren() != null) {
				RequestResult result = recurseCheck(iter.getChildren());
				if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
					return result;
				}
			}
		}
		return buildSuccessRequestResult();
	}

	/**
	 * josn转换List<PlanTemplateDetailVo>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<PlanTemplateDetailVo> getDetails(String details) {
		JSONArray array = JSONArray.fromObject(details);

		Map<String, Class> classMap = Maps.newHashMap();
		classMap.put("children", PlanTemplateDetailVo.class);

		JsonConfig config = new JsonConfig();
		config.setClassMap(classMap);
		config.setRootClass(PlanTemplateDetailVo.class);

		Collection<PlanTemplateDetailVo> datas = JSONArray.toCollection(array, config);
		return datas;
	}

	@Override
	public CrudRepository<PlanTemplateDetail, String> getRepository() {
		return detailRepository;
	}

	/**
	 * 根据主表删除明细
	 * 
	 * @param templateId
	 *            主表ID
	 */
	public void deleteByTemplateId(String templateId) {
		List<PlanTemplateDetail> details = detailRepository.queryByTemplateId(templateId);
		for (PlanTemplateDetail detail : details) {
			super.delete(detail.getFid());
		}
	}
	
	
	/**
	 * 获取计划模板的里最大序号的
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	public PlanTemplateDetail getCodeMaxLengthByPlanTemplate(String planTemplateId){
		PlanTemplateDetail detail = detailRepository.getCodeMaxLengthByPlanTemplate(planTemplateId);
		return detail;
	}
	/**
	 * 获取计划模板的根明细
	 * @param planTemplateId 计划模板ID
	 * @return
	 */
	public PlanTemplateDetail getRootByPlanTemplate(String planTemplateId){
		PlanTemplateDetail template = detailRepository.getRootByPlanTemplate(planTemplateId);
		return template;
	}
	/**
	 * 根据主表查询
	 * @return
	 */
	public List<PlanTemplateDetail> queryByTemplateId(String templateId){
		List<PlanTemplateDetail> list = detailRepository.queryByTemplateId(templateId);
		return list;
	}
}
