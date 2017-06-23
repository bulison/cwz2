package cn.fooltech.fool_ops.domain.fiscal.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

@Service
public class FiscalAccountService extends BaseService<FiscalAccount, FiscalAccountVo, String>{
	
	private final static String INIT_FILE = "classpath:datainit/init_fiscal_cfg.sql";
	
	@Autowired
	private FiscalAccountRepository accountRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	/**
	 * 财务参数配置服务类
	 */
	@Autowired
	private FiscalConfigService fiscalCfgService;
	
	/**
	 * 查询财务账套列表信息，按照财务账套主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<FiscalAccountVo> query(FiscalAccountVo fiscalAccountVo, PageParamater pageParamater){
		
		String orgId = SecurityUtil.getCurrentOrgId();
		Sort sort = new Sort(Direction.DESC, "name");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		
		return getPageVos(accountRepo.findPageByOrgId(orgId, pageRequest), pageRequest);
	}
	
	/**
	 * 查询财务账套列表信息<br>
	 */
	public List<FiscalAccountVo> queryByOrg(){
		String orgId = SecurityUtil.getCurrentOrgId();
		Sort sort = new Sort(Direction.DESC, "name");
		List<FiscalAccountVo> vos = getVos(accountRepo.findByOrgId(orgId, sort));
		return vos;
	}
	
	/**
	 * 删除财务账套<br>
	 */
	public RequestResult delete(String fid){
		return buildFailRequestResult("账套不能删除");
	}
	
	/**
	 * 创建默认财务账套,如果存在则返回默认账套
	 */
	@Transactional
	public FiscalAccount createDefaultAccount(String orgId, String creatorId) {
		
		FiscalAccount exist = accountRepo.findTopByDefaultFlag(orgId, FiscalAccount.FLAG_YES);
		if(exist!=null ){
			return exist;
		}
		
		Organization org = new Organization();
		org.setFid(orgId);
		
		FiscalAccount entity = new FiscalAccount();
		entity.setName("企业默认登录账套");
		entity.setEnable(FiscalAccount.ENABLE_YES);
		entity.setDescription("企业默认登录账套");
		entity.setDefaultFlag(FiscalAccount.FLAG_YES);
		entity.setOrg(org);
		
		accountRepo.save(entity);
		initFiscalConfig(entity.getFid(), orgId, creatorId);
		return entity;
	}
	
	/**
	 * 初始化数据
	*/
	@Transactional
	public void initFiscalConfig(String accountId, String orgId, String creatorId) {
		
		User user = userRepo.findOne(creatorId);
		String deptId = user.getOrgId().getFid();
		
		//--------初始化数据 start------
		Date now = Calendar.getInstance().getTime();
		LinkedHashMap<String,String> map = Maps.newLinkedHashMap();
		map.put("FCREATE_TIME", DateUtilTools.date2String(now, DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		map.put("FCREATOR_ID", creatorId);
		map.put("FORG_ID", orgId);
		map.put("FACC_ID", accountId);
		map.put("FDEPT_ID", deptId);
		
		fiscalCfgService.initFiscalConfig(map, INIT_FILE);
		//--------初始化数据 end------	
	}
	
	/**
	 * 新增/编辑财务账套
	 * @param vo
	 */
	@Transactional
	public RequestResult saveDefaultLogin(String fid) {
		
		String orgId = SecurityUtil.getCurrentOrgId();
		FiscalAccount account = accountRepo.findOne(fid);
		if(account.getDefaultFlag()==FiscalAccount.FLAG_YES){
			return buildFailRequestResult("该账套已是默认登录");
		}
		FiscalAccount old = accountRepo.findTopByDefaultFlag(orgId, FiscalAccount.FLAG_YES);
		old.setDefaultFlag(FiscalAccount.FLAG_NO);
		account.setDefaultFlag(FiscalAccount.FLAG_YES);
		accountRepo.save(old);
		accountRepo.save(account);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 切换账套
	 * @param fid
	 * @return
	 */
	public RequestResult changeFiscalAccount(String fid, HttpSession session){
		
		//System.out.println("before:"+SecurityUtil.getFiscalAccountId());
		
		FiscalAccount change = accountRepo.findOne(fid);
		if(change==null)return buildFailRequestResult("找不到账套");
		
		String accId = change.getFid();
		String accName = change.getName();
		SecurityUtil.changeFiscalAccount(accId, accName);
		
		session.setAttribute(Constants.DEFAULT_FISCAL_ACCOUNT_NAME, accName);
		//System.out.println("after:"+SecurityUtil.getFiscalAccountId());
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断名称是否存在
	 * @param orgId
	 * @param bankName
	 * @param selfId
	 * @return
	 */
	public boolean isNameExist(String orgId, String name, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = accountRepo.countByName(orgId, name);
		}else{
			count = accountRepo.countByName(orgId, name, excludeId);
		}
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 新增/编辑财务账套
	 * @param vo
	 */
	@Transactional
	public RequestResult save(FiscalAccountVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		User user = SecurityUtil.getCurrentUser();
		
		if(isNameExist(org.getFid(), vo.getName(), vo.getFid())){
			return buildFailRequestResult("名称重复");
		}
		
		FiscalAccount entity = new FiscalAccount();
		if(StringUtils.isBlank(vo.getFid())){
			
			if(vo.getDefaultFlag()==FiscalAccount.FLAG_YES &&
					accountRepo.findTopByDefaultFlag(org.getFid(), FiscalAccount.FLAG_YES)!=null ){
				return buildFailRequestResult("已有默认登录账套");
			}
			
			entity.setName(vo.getName());
			entity.setEnable(vo.getEnable());
			entity.setDescription(vo.getDescription());
			entity.setDefaultFlag(vo.getDefaultFlag());
			entity.setCreator(user);
			entity.setOrg(org);
			entity.setDept(dept);
			
			accountRepo.save(entity);
			initFiscalConfig(entity.getFid(), org.getFid(), user.getFid());
		}else {
			entity = accountRepo.findOne(vo.getFid());
			
			if(entity==null){
				return buildFailRequestResult("数据不存在，可能已被删除，请刷新再试");
			}
			
			entity.setName(vo.getName());
			entity.setEnable(vo.getEnable());
			entity.setDescription(vo.getDescription());
			
			accountRepo.save(entity);
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 保存初始账套
	 * @param
	 */
	@Transactional
	public void saveInitAccount(FiscalAccountVo vo, Organization org, User creator){
		
		FiscalAccount entity = new FiscalAccount();
		entity.setName(vo.getName());
		entity.setEnable(vo.getEnable());
		entity.setDescription(vo.getDescription());
		entity.setDefaultFlag(vo.getDefaultFlag());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(org);
		entity.setDept(SecurityUtil.getCurrentDept());
		
		accountRepo.save(entity);
		initFiscalConfig(entity.getFid(), org.getFid(), creator.getFid());
	}
/*	*//**
	 * 查询所有账套,数据初始化用,只需要用一次,过后注释
	 */
	public List<FiscalAccount> findAll(){
		return accountRepo.findAll();
	}
	@Override
	public FiscalAccountVo getVo(FiscalAccount entity) {
		if(entity == null)
			return null;
		FiscalAccountVo vo = new FiscalAccountVo();
		vo.setName(entity.getName());
		vo.setEnable(entity.getEnable());
		vo.setDefaultFlag(entity.getDefaultFlag());
		vo.setDescription(entity.getDescription());
		vo.setFid(entity.getFid());
		
		return vo;
	}

	@Override
	public CrudRepository<FiscalAccount, String> getRepository() {
		return accountRepo;
	}
}
