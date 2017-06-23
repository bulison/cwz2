package cn.fooltech.fool_ops.domain.member.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.member.repository.MemberRepository;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.service.RoleService;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.MD5Util;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.input.FivepenUtils;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

import javax.annotation.Resource;

/**
 * <p>人员网页服务类</p>
 * @author cwz
 * @version 1.0
 * @date 2016-10-28
 */
@Service
public class MemberService extends BaseService<Member, MemberVo, String>  implements BaseDataCache {
	private final int PINYIN_LIMIT = 300;//拼音长度限制
	//普通用户角色
	private final static String commonRoleId = "402881114df53094014df5336f9c0024";
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAttrService userAttrService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberRepository memberRepository;
	/**
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;
//	/**
//	 * 辅助属性网页服务类
//	 */
//	@Autowired
//	private AuxiliaryAttrService auxiAttrService;
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrRepository attrRepository;
	
	/**
	 * 单据服务类
	 */
	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService billService;

	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentService;

	/**
	 * 费用单服务类
	 */
	@Autowired
	private CostBillService costbillService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private RedisService redisService;

	/**
	 * 财务科目服务类
	 */
	@Autowired
	private FiscalAccountService subjectService;
	
	/**
	 * 获取普通用户角色
	 * @return
	 */
	public List<Role> getCommonRoleSets(){
		//角色
		List<Role> roles = Lists.newArrayList();
	   Role role = roleService.findOne(commonRoleId);
	   roles.add(role);
	   return roles;
	}


	/**
	 * 查询人员列表信息，按照人员主键降序排列
	 * @param memberVo
	 * @param pageParamater
	 * @return
	 */
	public Page<MemberVo> query(MemberVo memberVo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.ASC, "userCode");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<Member> page = memberRepository.query(memberVo, SecurityUtil.getCurrentOrgId(), request);
		Page<MemberVo> pageVos = getPageVos(page, request);
		return pageVos;
	}
	
	/**
	 * 查找所有子节点ID
	 * @param deptId
	 * @return
	 */
/*	private void queryChildrenId(String deptId, final List<String> childIds){
		
		childIds.add(deptId);
		Organization dept = orgService.get(deptId);
		
		Set<Organization> childs = dept.getChilds();
		
		if(childs!=null){
			for(Organization org: childs){
				queryChildrenId(org.getFid(), childIds);
			}
		}
	}*/
	
	
	/**
	 * 单个人员实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public MemberVo getVo(Member entity){
		if(entity == null)
			return null;
		MemberVo vo = new MemberVo();
		vo.setFid(entity.getFid());
		vo.setUsername(entity.getUsername());
		vo.setSex(entity.getSex());
		vo.setEmail(entity.getEmail());
		vo.setPostcode(entity.getPostcode());
		vo.setAddress(entity.getAddress());
		vo.setFax(entity.getFax());
		vo.setIdCard(entity.getIdCard());
		vo.setIsWebLogin(entity.getIsWebLogin());
		vo.setIsMobileLogin(entity.getIsMobileLogin());
		vo.setPhoneOne(entity.getPhoneOne());
		vo.setPhoneTwo(entity.getPhoneTwo());
		vo.setUserDesc(entity.getUserDesc());
		vo.setIsInterface(entity.getIsInterface());
		vo.setUserCode(entity.getUserCode());
		vo.setJobNumber(entity.getJobNumber());
		vo.setPosition(entity.getPosition());
		if(entity.getEntryDay()!=null){
			vo.setEntryDay(DateUtil.format(entity.getEntryDay(), DATE));
		}
		if(entity.getDepartureDate()!=null){
			vo.setDepartureDate(DateUtil.format(entity.getDepartureDate(), DATE));
		}
		vo.setDepartureReason(entity.getDepartureReason());
		vo.setCreateTime(entity.getCreateTime());
		vo.setUpdateTime(DateUtil.format(entity.getUpdateTime(), DATE_TIME));
		vo.setEnable(entity.getEnable());
		
		if(entity.getJobStatus()!=null){
			AuxiliaryAttr jobStatus = entity.getJobStatus();
			vo.setJobStatusId(jobStatus.getFid());
			vo.setJobStatusName(jobStatus.getName());
			vo.setJobStatusCode(jobStatus.getCode());
		}
		
		if(entity.getEducation()!=null){
			AuxiliaryAttr education = entity.getEducation();
			vo.setEducationId(education.getFid());
			vo.setEducationName(education.getName());
			vo.setEducationCode(education.getCode());
		}
		
		if(entity.getDept()!=null){
			Organization dept = entity.getDept();
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
			vo.setDeptCode(dept.getOrgCode());
		}
		
		if(entity.getOrg()!=null){
			Organization org = entity.getDept();
			vo.setOrgId(org.getFid());
			vo.setOrgName(org.getOrgName());
		}
		
		try {
			if(entity.getUser()!=null){
				User user = entity.getUser();
				if(user!=null)
				vo.setLoginName(user.getUserCode());
			}
		} catch (Exception e) {
		}
		
		return vo;
	}
	
	
	/**
	 * 单个人员实体转换为Fastvo
	 * @param entity
	 * @return
	 */
	public MemberVo getFastVo(Member entity){
		MemberVo vo = new MemberVo();
		vo.setFid(entity.getFid());
		vo.setUsername(entity.getUsername());
		vo.setUserCode(entity.getUserCode());
		vo.setPhoneOne(entity.getPhoneOne());
		vo.setJobNumber(entity.getJobNumber());
		vo.setPosition(entity.getPosition());
		vo.setSearchSize(null);
		vo.setSearchKey(null);
		if(entity.getDept()!=null){
			Organization dept = entity.getDept();
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		return vo;
	}
	
	/**
	 * 删除人员<br>
	 */
	public RequestResult delete(String fid){
		
		Member member = memberRepository.findOne(fid);
		
		member.setEnable(StorageBaseEntity.STATUS_DISABLE);
		memberRepository.save(member);
		
		User user = member.getUser();
		if(user!=null){
			user.setValidation((short) 0);			
			userService.save(user);
		}
		
		redisService.remove(getCacheKey());
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取人员信息
	 * @param fid 人员ID
	 * @return
	 */
	public MemberVo getByFid(String fid) {
		Assert.notNull(fid);
		Member entity = memberRepository.findOne(fid);
		if(entity==null || entity.getEnable()==StorageBaseEntity.STATUS_DISABLE){
			throw new DataNotExistException("数据已被删除");
		}
		return getVo(entity);
	}
	

	/**
	 * 新增/编辑人员
	 * @param vo
	 */
	public RequestResult save(MemberVo vo) {
		
		Member entity = null;
		
		//演示hibernate-validator的使用；也可以加在方法的参数中methd(@Valid xxx)
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		
		if(StringUtils.isBlank(vo.getDeptId()) && StringUtils.isBlank(vo.getDeptCode())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "部门必填");
		}
		if(StringUtils.isNotBlank(vo.getDeptCode())){
			Organization dept = orgService.getByCode(SecurityUtil.getCurrentOrgId(), vo.getDeptCode().trim(), null);
			if(dept==null)return new RequestResult(RequestResult.RETURN_FAILURE, "找不到部门");
		}
		
		if(StringUtils.isBlank(vo.getUserCode())){
			 return new RequestResult(RequestResult.RETURN_FAILURE, "编号必填");
		}
		
		if(!memberRepository.checkUserCode(SecurityUtil.getCurrentOrgId(), vo.getUserCode(), vo.getFid())){
			 return new RequestResult(RequestResult.RETURN_FAILURE, "用户编号重复");
		}
		if(Strings.isNullOrEmpty(vo.getPhoneOne())){
			 return new RequestResult(RequestResult.RETURN_FAILURE, "手机号码必填");
		}
		if(vo.getIsMobileLogin()==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "是否移动端登陆必填");
		}
		if(vo.getIsWebLogin()==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "是否移Web登陆必填");
		}
		if(!memberRepository.checkPhoneOne(vo.getPhoneOne(), vo.getFid(), SecurityUtil.getCurrentOrgId())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "手机号重复");
		}
		
		Short mobileLogin = vo.getIsMobileLogin();
		Short webLogin = vo.getIsWebLogin();
		if(mobileLogin==null)mobileLogin = 0;
		if(webLogin==null)webLogin = 0;
		boolean isLogin = (mobileLogin+webLogin)>0?true:false;
		
		try {
			entity = new Member();
			if(StringUtils.isBlank(vo.getFid())){
				
				entity = transferVo(vo, entity);
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setOrg(SecurityUtil.getCurrentOrg());
				entity.setDep(SecurityUtil.getCurrentDept());
				
				entity.setCreateTime(Calendar.getInstance().getTime());
				entity.setEnable(StorageBaseEntity.STATUS_ENABLE);
				
				//判断是否允许登录，允许则创建登录用户
				if(isLogin){
					if(Strings.isNullOrEmpty(vo.getLoginName())){
						return new RequestResult(RequestResult.RETURN_FAILURE, "登录名必填");
					}
					if(userRepository.checkCodeIsExist(null, vo.getLoginName())){
						return new RequestResult(RequestResult.RETURN_FAILURE, "登录名已存在");
					}
					if(!Strings.isNullOrEmpty(vo.getPassword()) && vo.getPassword().length()<6){
						return new RequestResult(RequestResult.RETURN_FAILURE, "自定义密码至少6位");
					}
					
					User user = createUser(entity, vo.getLoginName(), vo.getPassword());
					entity.setUser(user);
				}
				entity.setPinyin(PinyinUtils.getPinyinCode(vo.getUsername(), PINYIN_LIMIT));
				entity.setFivepen(FivepenUtils.getFivePenCode(vo.getUsername()));
				memberRepository.save(entity);
			}else {
				entity = memberRepository.findOne(vo.getFid());
				if(entity == null){
					return  new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
				}
				entity = transferVo(vo, entity);
				
				String updateTime = DateUtil.format(entity.getUpdateTime(), DATE_TIME);
				if(!vo.getUpdateTime().equals(updateTime)){
					return  new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改");
				}
				entity.setUpdateTime(Calendar.getInstance().getTime());
				
				/** 判断是否允许登录，允许则检查是否有登录用户，没有则创建登录用户
				  不允许登录则检查是否有登录用户，有则置为无效*/
				if(entity.getEnable() == StorageBaseEntity.STATUS_ENABLE && isLogin){
					
					if(Strings.isNullOrEmpty(vo.getLoginName())){
						 return new RequestResult(RequestResult.RETURN_FAILURE, "登录名必填");
					}
					
					User check = entity.getUser();
					try {
					
						if(check!=null){
							if(userRepository.checkCodeIsExist(check.getFid(), vo.getLoginName())){
								return new RequestResult(RequestResult.RETURN_FAILURE, "登录名已存在");
							}
						}else{
							if(userRepository.checkCodeIsExist(null, vo.getLoginName())){
								return new RequestResult(RequestResult.RETURN_FAILURE, "登录名已存在");
							}
						}
					
						if(check==null && isLogin){
							User user = createUser(entity, vo.getLoginName(), vo.getPassword());
							entity.setUser(user);
						}else if(check!=null && isLogin){
							User user = entity.getUser();
							user.setValidation(StorageBaseEntity.STATUS_ENABLE);
							user.setUserCode(vo.getLoginName());
							user.setIsMobileLogin(vo.getIsMobileLogin());
							userRepository.save(user);
						}else if(check!=null && !isLogin){
							User user = entity.getUser();
							user.setValidation((short) 0);
							user.setUserCode(vo.getLoginName());
							user.setIsMobileLogin(vo.getIsMobileLogin());
							userRepository.save(user);
						}
						
					} catch (Exception e) {
						//用户可能已被删除
						User user = createUser(entity, vo.getLoginName(), vo.getPassword());
						entity.setUser(user);
					}
					
				}else{
					if(entity.getUser()!=null){
						User user = entity.getUser();
						user.setValidation((short)0);
						userRepository.save(user);;
					}
				}
				entity.setPinyin(PinyinUtils.getPinyinCode(vo.getUsername(), PINYIN_LIMIT));
				entity.setFivepen(FivepenUtils.getFivePenCode(vo.getUsername()));
				memberRepository.save(entity);
			}
		} catch (Exception e) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "系统异常");
		}
		
		Map<String, String> map = Maps.newHashMap();
		map.put("fid", entity.getFid());
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
//		ResultResponse result = new ResultResponse(ResultResponse.SUCCESS, "操作成功", map);

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult(map);
	}

	/**
	 * 根据Member创建User
	 */
	private User createUser(Member member, String loginName, String password){
		User user = new User();
		user.setAccountName(member.getUsername());
		user.setPhoneOne(member.getPhoneOne());
		if(Strings.isNullOrEmpty(password)){
			user.setPassWord(MD5Util.encrypt("123456"));
		}else{
			user.setPassWord(MD5Util.encrypt(password));
		}
		user.setCreateDate(new Date());
		user.setUserCode(loginName);
		user.setUserDesc(member.getUserDesc());
		user.setUserName(member.getUsername());
		user.setFisinterface(member.getIsInterface());
		user.setValidation((short)1);//0:失效,1:有效
		user.setEmail(member.getEmail());
		user.setFax(member.getFax());
		user.setOrgId(member.getDept());
		user.setIsAdmin((short)0);
		user.setIsMobileLogin(member.getIsMobileLogin());
		user.setTopOrg(member.getOrg());
		user.setRoles(getCommonRoleSets());
		userService.save(user);

		redisService.remove(getCacheKey());

		return user;
	}
	
	/**
	 * VO转换entity
	 */
	private Member transferVo(MemberVo vo, Member entity) throws Exception{
		
		String orgId = SecurityUtil.getCurrentOrgId();
		entity.setUsername(vo.getUsername());
		entity.setSex(vo.getSex());
		entity.setEmail(vo.getEmail());
		entity.setPostcode(vo.getPostcode());
		entity.setAddress(vo.getAddress());
		entity.setFax(vo.getFax());
		entity.setIdCard(vo.getIdCard());
		entity.setIsWebLogin(vo.getIsWebLogin());
		entity.setIsMobileLogin(vo.getIsMobileLogin());
		entity.setPhoneOne(vo.getPhoneOne());
		entity.setPhoneTwo(vo.getPhoneTwo());
		entity.setUserDesc(vo.getUserDesc());
		entity.setIsInterface(vo.getIsInterface());
		entity.setUserCode(vo.getUserCode());
		entity.setJobNumber(vo.getJobNumber());
		entity.setPosition(vo.getPosition());
		
		if(StringUtils.isNotBlank(vo.getEntryDay())){
			entity.setEntryDay(DateUtil.str2Date(vo.getEntryDay(), DATE));
		}
		if(StringUtils.isNotBlank(vo.getDepartureDate())){
			entity.setDepartureDate(DateUtil.str2Date(vo.getDepartureDate(), DATE));
		}
		entity.setDepartureReason(vo.getDepartureReason());
		
		if(StringUtils.isNotBlank(vo.getDeptId())){
			entity.setDept(orgService.findOne(vo.getDeptId()));
		}else if(StringUtils.isNotBlank(vo.getDeptCode())){
			Organization dept = orgService.getByCode(SecurityUtil.getCurrentOrgId(), vo.getDeptCode().trim(), null);
			entity.setDept(dept);
		}
		if(StringUtils.isNotBlank(vo.getJobStatusId())){
			entity.setJobStatus(attrRepository.findOne(vo.getJobStatusId()));
		}else if(StringUtils.isNotBlank(vo.getJobStatusCode())){
			entity.setJobStatus(attrRepository.getByCode(orgId, AuxiliaryAttrType.CODE_JOB_STATUS,
					vo.getJobStatusCode()));
		}
		if(StringUtils.isNotBlank(vo.getEducationId())){
			entity.setEducation(attrRepository.findOne(vo.getEducationId()));
		}else if(StringUtils.isNotBlank(vo.getEducationCode())){
			entity.setEducation(attrRepository.getByCode(orgId, AuxiliaryAttrType.CODE_EDUCATION,
					vo.getEducationCode()));
		}
		
		return entity;
	}
	
	/**
	 * 检查是否统一机构下是否有相同的用户编号
	 */
	public boolean checkUserCode(String orgId, String userCode, String memberId){
		return memberRepository.checkUserCode(orgId, userCode, memberId);
	}
	
	/**
	 * 根据部门查找人员列表列表
	 * @param deptId
	 */
	public List<CommonTreeVo> queryTreeByDept(String deptId){
		List<Member> members = memberRepository.findByDeptIdOrderByCreateTimeAsc(deptId);
		if(members==null || members.size()==0)return Collections.EMPTY_LIST;
		List<CommonTreeVo> treeList = Lists.newArrayList();
		for (Member member : members) {
			CommonTreeVo treeVo = new CommonTreeVo();
			treeVo.setId(member.getFid());
			treeVo.setText(member.getUsername());
			treeVo.setState("open");
			treeList.add(treeVo);
		}
		return treeList;
	}
	
	/**
	 * 自动填充的查询
	 */
	@SuppressWarnings("unchecked")
	public List<MemberVo> queryForAutoComplete(String key, String val){
		return null;
//		DetachedCriteria dc = null;
//		try {
//			dc = getAutoCompleteCriteria(key, val);
//			if(dc==null)return Collections.EMPTY_LIST;
//			dc.add(Restrictions.eq("org.fid", getCurrentOrgFid()));
//		} catch (Exception e) {
//			e.printStackTrace();//for test
//			return Collections.EMPTY_LIST;
//		}
//		return getVos(memberRepository.queryByCriteria(dc));
	}
	
	/**
	 * 模糊查询(根据人员编号、人员名称)
	 * @param vo
	 * @return
	 */
	public List<MemberVo> vagueSearch(MemberVo vo){
		String userId = SecurityUtil.getCurrentUserId();
		String inputType = userAttrService.getInputType();
		List<Member> entities = memberRepository.vagueSearch(SecurityUtil.getCurrentOrgId(), vo.getSearchKey(), vo.getSearchSize(),userId,inputType);
		return getVos(entities);
	}

	@Override
	public CrudRepository<Member, String> getRepository() {
		return this.memberRepository;
	}
	/**
	 * 根据编号获取
	 * @param orgId    机构id
	 * @param userCode 用户编号
	 * @return
	 */
	public Member getByCode(String orgId, String userCode){
		return memberRepository.getByCode(orgId, userCode);
	}
	
	/**
	 * 根据部门查找在职人员+本月离职人员
	 */
	public List<Member> findNotDepartureByDeptId(String deptId, Date departureDate){
		return memberRepository.findNotDepartureByDeptId(deptId, departureDate);

	}

	@Override
	public String getCacheName() {
		return BaseConstant.MEMBER;
	}
}
