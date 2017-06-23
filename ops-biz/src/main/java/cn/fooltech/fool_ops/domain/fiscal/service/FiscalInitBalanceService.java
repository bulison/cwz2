package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.repository.BankRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.SupplierRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.UnitRepository;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalInitBalance;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalInitBalanceRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalInitBalanceVo;
import cn.fooltech.fool_ops.domain.initialBank.repository.InitialBankRepository;
import cn.fooltech.fool_ops.domain.member.repository.MemberRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>财务-科目初始数据网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-11-24 10:08:19
 */
@Service
public class FiscalInitBalanceService extends BaseService<FiscalInitBalance,FiscalInitBalanceVo,String> {
	
	private static final String YSZK = "应收账款";
	private static final String YFZK = "应付账款";
	private static final String YHCK = "银行存款";
	
	@Autowired
	private FiscalInitBalanceRepository initBalanceRepo;
	
	@Autowired
	private FiscalAccountingSubjectRepository subjectRepo;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	@Autowired
	private UnitRepository unitRepo;
	
	/**
	 * 账套服务类
	 */
	@Autowired
	private FiscalAccountService fiscalAccountService;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private SupplierRepository supplierRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private GoodsRepository goodsRepo;
	
	@Autowired
	private WarehouseBillRepository billRepo;
	
	@Autowired
	private BankRepository bankRepo;
	
	@Autowired
	private OrganizationRepository orgRepo;
	
	@Autowired
	private InitialBankRepository initBankRepo;
	
	@Autowired
	private FiscalPeriodService periodService;
	
	
	private Comparator<FiscalInitBalance> subjectComparator = new Comparator<FiscalInitBalance>() {

		@Override
		public int compare(FiscalInitBalance o1, FiscalInitBalance o2) {
			return o1.getSubject().getCode().compareTo(o2.getSubject().getCode());
		}
	};
	
	private Comparator<FiscalInitBalanceVo> subjectComparatorVo = new Comparator<FiscalInitBalanceVo>() {

		@Override
		public int compare(FiscalInitBalanceVo o1, FiscalInitBalanceVo o2) {
			return o1.getSubjectCode().compareTo(o2.getSubjectCode());
		}
	};
	
	/**
	 * 查询所有初始数据表，没有填写的默认金额=0
	 */
	public List<FiscalInitBalance> queryAll(){
		String accountId = SecurityUtil.getFiscalAccountId();
		List<FiscalInitBalance> datas = initBalanceRepo.findByAccId(accountId);
		//enable
		List<FiscalAccountingSubject> subjects = subjectRepo.findByAccIdAndEnable(accountId, 
				FiscalAccountingSubject.ENABLE_YES);
		for(FiscalAccountingSubject subject:subjects){
			boolean find = false;
			for(FiscalInitBalance data:datas){
				if(data.getSubject().getFid().equals(subject.getFid())){
					find = true;
					break;
				}
			}
			if(!find){
				datas.add(new FiscalInitBalance(subject));
			}
		}
		Collections.sort(datas, subjectComparator);
		return datas;
	}

	/**
	 * 查询所有初始数据表，没有填写的默认金额=0
	 */
	public List<FiscalInitBalanceVo> queryTree(FiscalInitBalanceVo vo){
		
		
		if(!Strings.isNullOrEmpty(vo.getSubjectCode())||!Strings.isNullOrEmpty(vo.getSubjectName())){
			return queryListVo(vo);
		}else{
			return queryTreeVo(vo);
		}
	}
	
	/**
	 * 查找树状Vos
	 * @param vo
	 * @return
	 */
	private List<FiscalInitBalanceVo> queryListVo(FiscalInitBalanceVo vo){
		
		String accountId = SecurityUtil.getFiscalAccountId();
		List<FiscalInitBalanceVo> dataVos = getVos(initBalanceRepo.findBy(accountId, vo.getSubjectCode(),
				vo.getSubjectName(), vo.getSubjectType()));
		
		List<FiscalAccountingSubject> subjects = subjectRepo.findBy(accountId, vo.getSubjectCode(), 
				vo.getSubjectName(), vo.getSubjectType());
		
		for(FiscalAccountingSubject subject:subjects){
			if(subjectService.isRoot(subject)){
				continue;
			}
			boolean exist = false;
			for(FiscalInitBalanceVo check:dataVos){
				if(check.getSubjectCode().equals(subject.getCode())){
					exist = true;
				}	
			}
			if(!exist){
				FiscalInitBalance newdata = new FiscalInitBalance(subject);
				dataVos.add(getVo(newdata));
			}
		}
		Collections.sort(dataVos, subjectComparatorVo);
		return dataVos;
	}
	
	/**
	 * 查找树状Vos
	 * @param vo
	 * @return
	 */
	private List<FiscalInitBalanceVo> queryTreeVo(FiscalInitBalanceVo vo){
		
		String accountId = SecurityUtil.getFiscalAccountId();
		List<FiscalInitBalance> datas = initBalanceRepo.findParentsByAccId(accountId, vo.getSubjectType());
		List<FiscalInitBalanceVo> dataVos = getVos(datas, true);
		
		Integer subjectType = vo.getSubjectType();
		List<FiscalAccountingSubject> subjects = null;
		if(subjectType==null){
			subjects = subjectRepo.findByAccId(accountId);
		}else{
			subjects = subjectRepo.findSubjectByAccIdAndType(accountId, subjectType);
		}
		for(FiscalAccountingSubject subject:subjects){
			if(subjectService.isRoot(subject)){
				continue;
			}
			boolean findTop = false;
			FiscalAccountingSubject parent = subject.getParent();
			
			if(subjectService.isRoot(parent)){
				for(FiscalInitBalanceVo dataVo:dataVos){
					if(dataVo.getSubjectId().equals(subject.getFid())){
						findTop = true;
						break;
					}
				}
				if(!findTop){
					FiscalInitBalance newdata = new FiscalInitBalance(subject);
					dataVos.add(getVo(newdata));
				}
			}else{
				for(FiscalInitBalanceVo dataVo:dataVos){
					findParent(dataVo.getChildren(), subject, dataVo);
				}
			}
		}
		recurseSort(dataVos);
		return dataVos;
	}
	
	/**
	 * 递归排序
	 * @param vos
	 */
	private void recurseSort(List<FiscalInitBalanceVo> vos){
		if(vos==null || vos.size()==0)return;
		Collections.sort(vos, subjectComparatorVo);
		for(FiscalInitBalanceVo vo:vos){
			recurseSort(vo.getChildren());
		}
	}
	
	/**
	 * 查找爸爸
	 * @param dataVos
	 * @param subject
	 * @param parentVo
	 */
	private void findParent(List<FiscalInitBalanceVo> dataVos, FiscalAccountingSubject subject, FiscalInitBalanceVo parentVo){
		if(dataVos==null)return ;
		boolean find = false;
		for(FiscalInitBalanceVo dataVo:dataVos){//先遍历完该层
			if(dataVo.getSubjectId().equals(subject.getFid())){
				find = true;
				return ;
			}
		}
		if(!find){
			if(parentVo.getSubjectId().equals(subject.getParent().getFid())){
				FiscalInitBalance newdata = new FiscalInitBalance(subject);
				parentVo.getChildren().add(getVo(newdata));
			}else{
				for(FiscalInitBalanceVo dataVo:dataVos){
					findParent(dataVo.getChildren(), subject, dataVo);
				}
			}
		}
	}
	
	/**
	 * 查找核算的期初数据
	 * @return
	 */
	public List<FiscalInitBalanceVo> queryAccountingSubject(String subjectId){
		return getVos(initBalanceRepo.findBySubjectIdAndCheckStatus(subjectId, FiscalInitBalance.CHECK));
	}
	
	
	
	public List<FiscalInitBalanceVo> getVos(List<FiscalInitBalance> entities, boolean recurse){
		List<FiscalInitBalanceVo> vos = Lists.newArrayList();
		if (entities != null && !entities.isEmpty()) {
			for (FiscalInitBalance e : entities) {
				vos.add(getVo(e, recurse));
			}
		}
		return vos;
	}
	
	/**
	 * 单个财务-科目初始数据实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public FiscalInitBalanceVo getVo(FiscalInitBalance entity){
		return getVo(entity, false);
	}
	
	/**
	 * 单个财务-科目初始数据实体转换为vo
	 * @param entity
	 * @return
	 */
	public FiscalInitBalanceVo getVo(FiscalInitBalance entity, boolean recurse){
		if(entity == null)
			return null;
		FiscalAccountingSubject subject = entity.getSubject();
		FiscalInitBalanceVo vo = new FiscalInitBalanceVo();
		vo.setDirection(subject.getDirection());
		vo.setAmount(entity.getAmount());
		vo.setQuantity(entity.getQuantity());
		vo.setCurrencyAmount(entity.getCurrencyAmount());
		vo.setDescribe(entity.getDescribe());
		//vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setIsCheck(entity.getIsCheck());
		vo.setIsCheckSubject(subject.getIsCheck());
		
		/*User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}*/
		
		AuxiliaryAttr currency = entity.getCurrency();
		if(currency!=null){
			vo.setCurrencyId(currency.getFid());
			vo.setCurrencyName(currency.getName());
			vo.setCurrencyCode(currency.getCode());
		}
		
		if(subject!=null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
			vo.setSubjectCode(subject.getCode());
			vo.setSubjectType(subject.getType());
			vo.setCashSign(subject.getCashSign());
			vo.setCurrencySign(subject.getCurrencySign());
			vo.setCussentAccountSign(subject.getCussentAccountSign());
			vo.setSupplierSign(subject.getSupplierSign());
			vo.setCustomerSign(subject.getCustomerSign());
			vo.setDepartmentSign(subject.getDepartmentSign());
			vo.setMemberSign(subject.getMemberSign());
			vo.setWarehouseSign(subject.getWarehouseSign());
			vo.setProjectSign(subject.getProjectSign());
			vo.setGoodsSign(subject.getGoodsSign());
			vo.setQuantitySign(subject.getQuantitySign());
			vo.setSubjectFlag(subject.getFlag());
			
			AuxiliaryAttr subjectAttr = subject.getSubject();
			if(subjectAttr!=null){
				vo.setSubjectCategory(subjectAttr.getName());
			}
		}
		
		/*FiscalAccount account = entity.getFiscalAccount();
		if(account!=null){
			vo.setFiscalAccountId(account.getFid());
			vo.setFiscalAccountName(account.getName());
		}*/
		
		Supplier supplier = entity.getSupplier();
		if(supplier!=null){
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
			vo.setSupplierCode(supplier.getCode());
		}
		
		Customer customer = entity.getCustomer();
		if(customer!=null){
			vo.setCustomerId(customer.getFid());
			vo.setCustomerName(customer.getName());
			vo.setCustomerCode(customer.getCode());
		}
		
		Organization department = entity.getDepartment();
		if(department!=null){
			vo.setDepartmentId(department.getFid());
			vo.setDepartmentName(department.getOrgName());
			vo.setDepartmentCode(department.getOrgCode());
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
			vo.setMemberCode(member.getUserCode());
		}
		
		AuxiliaryAttr warehouse = entity.getWarehouse();
		if(warehouse!=null){
			vo.setWarehouseId(warehouse.getFid());
			vo.setWarehouseName(warehouse.getName());
			vo.setWarehouseCode(warehouse.getCode());
		}
		
		AuxiliaryAttr project = entity.getProject();
		if(project!=null){
			vo.setProjectId(project.getFid());
			vo.setProjectName(project.getName());
			vo.setProjectCode(project.getCode());
		}
		
		Goods goods = entity.getGoods();
		if(goods!=null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsName(goods.getName());
			vo.setGoodsCode(goods.getCode());
		}
		
		Unit unit = entity.getUnit();
		if (unit != null) {
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
			vo.setUnitCode(unit.getCode());
		}
		
		if(recurse){
			Set<FiscalAccountingSubject> childSubjects = subject.getChilds();
			if(childSubjects!=null && childSubjects.size()>0){
				
				for(FiscalAccountingSubject child:childSubjects){
					List<FiscalInitBalance> children = 
							initBalanceRepo.findBySubjectIdAndCheckStatus(child.getFid(), FiscalInitBalance.UN_CHECK);
					List<FiscalInitBalanceVo> exists = vo.getChildren();
					exists.addAll(getVos(children, true));
					vo.setChildren(exists);
				}
				
			}
		}
		
		return vo;
	}
	
	/**
	 * 删除财务-科目初始数据<br>
	 */
	public RequestResult delete(String fid){
		
		FiscalInitBalance init = initBalanceRepo.findOne(fid);
		String subjectId = init.getSubject().getFid();
		
		if(initBalanceRepo.countBySubjectId(subjectId)>0){
			return buildFailRequestResult("存在子节点不能删除");
		}
		
		if(initBalanceRepo.countBrotherAccounting(subjectId)>0){
			return buildFailRequestResult("存在核算数据不能删除");
		}
		
		initBalanceRepo.delete(fid);
		return buildSuccessRequestResult();
	}
	

	/**
	 * 新增/编辑财务-科目初始数据（判断条件）
	 * @param vos[0] 当前科目下的期初数据
	 * @param vos[1-n] 勾选的核算数据
	 */
	@Transactional
	public RequestResult save(List<FiscalInitBalanceVo> vos) {
		
		FiscalInitBalanceVo vo = vos.remove(0);
		
		RequestResult checkResult = checkListVaild(vo, vos);
		if(!checkResult.isSuccess()){
			return checkResult;
		}
		
		FiscalAccountingSubject subject = getSubjectByBalance(vo);
		if(vos.size()>0){
			vo.setAmount(this.sumVoAmount(vos));
		}else{
			if(subject.getIsCheck()==FiscalAccountingSubject.ACCOUNT){
				vo.setAmount(BigDecimal.ZERO);
			}
		}
		if(Strings.isNullOrEmpty(vo.getFid())&&
				initBalanceRepo.countBySubjectId(subject.getFid())>0){
			return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
		}
		RequestResult save = saveEntity(vo, true, FiscalInitBalance.UN_CHECK);
		
		//删除科目核算数据
		deleteAccountingBySubjectId(subject.getFid());
		for(FiscalInitBalanceVo check:vos){
			check.setFid(null);
			saveEntity(check, false, FiscalInitBalance.CHECK);
		}
		return buildSuccessRequestResult(save.getData());
	}
	
	/**
	 * 根据VO获取科目
	 * @param vo
	 * @return
	 */
	private FiscalAccountingSubject getSubjectByBalance(FiscalInitBalanceVo vo){
		String accId = SecurityUtil.getFiscalAccountId();
		
		//科目
		FiscalAccountingSubject subject = null;
		if(StringUtils.isNotBlank(vo.getSubjectId())){
			subject = subjectRepo.findOne(vo.getSubjectId());
		}else if(StringUtils.isNotBlank(vo.getSubjectCode())){
			subject = subjectRepo.findTopByCode(accId, vo.getSubjectCode());
		}
		return subject;
	}
	
	/**
	 * 检查VOS的合法性
	 * @param checkVo
	 * @return
	 */
	private RequestResult checkListVaild(FiscalInitBalanceVo vo, List<FiscalInitBalanceVo> checkVos){
		
		for(FiscalInitBalanceVo checkVo:checkVos){
			checkVo.setSubjectId(vo.getSubjectId());
			checkVo.setSubjectCode(vo.getSubjectCode());
			RequestResult cur = checkValid(checkVo, null);
			if(!cur.isSuccess()){
				return cur;
			}
		}
		
		Set<String> checkCache = Sets.newHashSet();
		
		for(FiscalInitBalanceVo checkVo:checkVos){
			FiscalAccountingSubject subject = getSubjectByBalance(checkVo);
			String checkIds = "";
			
			if(subject.getCustomerSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getCustomerId();
			}
			if(subject.getSupplierSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getSupplierId();
			}
			if(subject.getMemberSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getMemberId();
			}
			if(subject.getDepartmentSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getDepartmentId();
			}
			if(subject.getWarehouseSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getWarehouseId();
			}
			if(subject.getProjectSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getProjectId();
			}
			if(subject.getGoodsSign()==FiscalAccountingSubject.ACCOUNT){
				checkIds += checkVo.getGoodsId();
			}
			
			if(!Strings.isNullOrEmpty(checkIds)){
				if(checkCache.contains(checkIds)){
					return buildFailRequestResult("核算项数据不能重复");
				}else{
					checkCache.add(checkIds);
				}
			}
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 检查VO的合法性
	 * @param checkVo
	 * @return
	 */
	private RequestResult checkValid(FiscalInitBalanceVo checkVo, FiscalInitBalance newdata){
		
		if(StringUtils.isNotBlank(checkVo.getFid())){
			FiscalInitBalance entity = initBalanceRepo.findOne(checkVo.getFid());
			if(entity==null)return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			String updateTime = DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME);
			if(!updateTime.equals(checkVo.getUpdateTime())){
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
		}
		
		//科目
		FiscalAccountingSubject subject = getSubjectByBalance(checkVo);
		if(subject==null){
			return buildFailRequestResult("科目必填");
		}
		
		//外币
		if(checkAccountingSign(subject.getCurrencySign())){
			if(checkVo.getCurrencyAmount()==null){
				return buildFailRequestResult("该科目外币核算金额必填");
			}
		}
		//供应商
		if(checkAccountingSign(subject.getSupplierSign())){
			if(StringUtils.isBlank(checkVo.getSupplierId()) && 
					StringUtils.isBlank(checkVo.getSupplierCode())){
				return buildFailRequestResult("该科目核算供应商必填");
			}
			if(newdata!=null&&newdata.getSupplier()==null){
				return buildFailRequestResult("该科目核算供应商填写错误");
			}
			if(newdata!=null&&newdata.getSupplier().getRecordStatus().equals(Supplier.STATUS_SNU)){
				return buildFailRequestResult("该科目核算供应商无效");
			}
		}
		//客户
		if(checkAccountingSign(subject.getCustomerSign())){
			if(StringUtils.isBlank(checkVo.getCustomerId()) && 
					StringUtils.isBlank(checkVo.getCustomerCode())){
				return buildFailRequestResult("该科目核算客户必填");
			}
			if(newdata!=null&&newdata.getCustomer()==null){
				return buildFailRequestResult("该科目核算客户填写错误");
			}
			if(newdata!=null&&newdata.getCustomer().getRecordStatus().equals(Customer.STATUS_SNU)){
				return buildFailRequestResult("该科目核算客户无效");
			}
		}
		//部门
		if(checkAccountingSign(subject.getDepartmentSign())){
			if(StringUtils.isBlank(checkVo.getDepartmentId()) && 
					StringUtils.isBlank(checkVo.getDepartmentCode())){
				return buildFailRequestResult("该科目核算部门必填");
			}
			if(newdata!=null&&newdata.getDepartment()==null){
				return buildFailRequestResult("该科目核算部门填写错误");
			}
		}
		//职员
		if(checkAccountingSign(subject.getMemberSign())){
			if(StringUtils.isBlank(checkVo.getMemberId()) && 
					StringUtils.isBlank(checkVo.getMemberCode())){
				return buildFailRequestResult("该科目核算职员必填");
			}
			if(newdata!=null&&newdata.getMember()==null){
				return buildFailRequestResult("该科目核算职员填写错误");
			}
			if(newdata!=null&&newdata.getMember().getEnable()==Member.DISABLE){
				return buildFailRequestResult("该科目核算职员无效");
			}
		}
		//仓库
		if(checkAccountingSign(subject.getWarehouseSign())){
			if(StringUtils.isBlank(checkVo.getWarehouseId()) && 
					StringUtils.isBlank(checkVo.getWarehouseCode())){
				return buildFailRequestResult("该科目核算仓库必填");
			}
			if(newdata!=null&&newdata.getWarehouse()==null){
				return buildFailRequestResult("该科目核算仓库填写错误");
			}
			if(newdata!=null&&newdata.getWarehouse().getFlag()==AuxiliaryAttr.PARENT){
				return buildFailRequestResult("该科目核算仓库填写错误，只能填写属性子节点");
			}
			if(newdata!=null&&newdata.getWarehouse().getEnable()==AuxiliaryAttr.STATUS_DISABLE){
				return buildFailRequestResult("该科目核算仓库无效");
			}
		}
		//项目
		if(checkAccountingSign(subject.getProjectSign())){
			if(StringUtils.isBlank(checkVo.getProjectId())&& 
					StringUtils.isBlank(checkVo.getProjectCode())){
				return buildFailRequestResult("该科目核算项目必填");
			}
			if(newdata!=null&&newdata.getProject()==null){
				return buildFailRequestResult("该科目核算项目填写错误");
			}
			if(newdata!=null&&newdata.getProject().getFlag()==AuxiliaryAttr.PARENT){
				return buildFailRequestResult("该科目核算项目填写错误，只能填写属性子节点");
			}
			if(newdata!=null&&newdata.getProject().getEnable()==AuxiliaryAttr.STATUS_DISABLE){
				return buildFailRequestResult("该科目核算项目无效");
			}
		}
		//货品
		if(checkAccountingSign(subject.getGoodsSign())){
			if(StringUtils.isBlank(checkVo.getGoodsId()) && 
					StringUtils.isBlank(checkVo.getGoodsCode())){
				return buildFailRequestResult("该科目核算货品必填");
			}
			if(newdata!=null&&newdata.getGoods()==null){
				return buildFailRequestResult("该科目核算货品填写错误");
			}
			if(newdata!=null&&newdata.getGoods()!=null){
				Goods goods = newdata.getGoods();
				if(goods.getFlag()==Goods.FLAG_GROUP){
					return buildFailRequestResult("该科目核算货品不能填写货品组的编号");
				}
				if(!Goods.STATUS_SAC.equals(goods.getRecordStatus())){
					return buildFailRequestResult("该科目核算货品编号无效");
				}
			}
		}
		//数量、单位
		if(checkAccountingSign(subject.getQuantitySign())){
			if(checkVo.getQuantity()==null){
				return buildFailRequestResult("该科目核算数量必填");
			}
		}
		return buildSuccessRequestResult();
	}
	
	private boolean checkAccountingSign(Short sign){
		if(sign==null) return false;
		return FiscalAccountingSubject.ACCOUNT==sign?true:false;
	}
	
	/**
	 * 保存/新增
	 * @param vo
	 * @param updateParent 是否需要更新父节点金额
	 * @param isCheck 是否核算数据
	 */
	@Transactional
	private RequestResult saveEntity(FiscalInitBalanceVo vo, boolean updateParent, short isCheck){
		FiscalInitBalance entity = null;
		
		if(StringUtils.isBlank(vo.getFid())){
			entity = new FiscalInitBalance();
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setDept(SecurityUtil.getCurrentDept());
		}else {
			entity = initBalanceRepo.findOne(vo.getFid());
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
		}
		
		entity.setQuantity(vo.getQuantity());
		entity.setCurrencyAmount(vo.getCurrencyAmount());
		entity.setDescribe(vo.getDescribe());
		
		fillRelationData(entity, vo);
		FiscalAccountingSubject subject = entity.getSubject();
		
//		BigDecimal direction = new BigDecimal(entity.getDirection());
		//计算金额时添加方向
		BigDecimal voDirectAmount = vo.getAmount();
		BigDecimal entityDirectAmount = entity.getAmount();
		if(updateParent){
			if(StringUtils.isBlank(vo.getFid())){
				updateParentAmount(subject.getParent(), voDirectAmount, entity);
			}else{
				if(entity.getAmount()==null)entity.setAmount(BigDecimal.ZERO);
				BigDecimal delta = voDirectAmount.subtract(entityDirectAmount);
				updateParentAmount(subject.getParent(), delta, entity);
			}
		}
		
		//计算完差值金额再把新的金额覆盖
		entity.setAmount(vo.getAmount());
		entity.setIsCheck(isCheck);
		entity.setIsFill(isFill(subject, isCheck));
		
		initBalanceRepo.save(entity);
		
		return buildSuccessRequestResult(entity.getFid());
	}
	
	/**
	 * 判断是否填写数据
	 * @param subject
	 * @param isCheck
	 * @return
	 */
	private Short isFill(FiscalAccountingSubject subject, short isCheck){
		if(isCheck==FiscalInitBalance.CHECK){
			return FiscalInitBalance.FILL;
		}else if(subject.getFlag()==FiscalAccountingSubject.FLAG_CHILD 
				&& isCheck==FiscalInitBalance.UN_CHECK
				&& subject.getIsCheck()==FiscalAccountingSubject.UN_ACCOUNT){
			return FiscalInitBalance.FILL;
		}else{
			return FiscalInitBalance.NOT_FILL;
		}
	}
	
	/**
	 * 合计子节点金额
	 * @param checkVos
	 * @return
	 */
	private BigDecimal sumVoAmount(List<FiscalInitBalanceVo> checkVos){
		BigDecimal total = BigDecimal.ZERO;
		if(checkVos==null)return total;
		for(FiscalInitBalanceVo check:checkVos){
			total = NumberUtil.add(total, check.getAmount());
		}
		return total;
	}
	
	/**
	 * 合计子节点金额
	 * @param inits
	 * @return
	 */
	private BigDecimal sumEntityAmount(List<FiscalInitBalance> inits){
		BigDecimal total = BigDecimal.ZERO;
		if(inits==null)return total;
		for(FiscalInitBalance check:inits){
			if(check.getAmount()!=null){
				total = check.getAmount().add(total);
			}
		}
		return total;
	}
	
	/**
	 * 期初额试算，平衡返回成功
	 */
	public RequestResult trailInital(){
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		BigDecimal borrowAmount = initBalanceRepo.sumAmountByDirection(fiscalAccountId, 
				FiscalAccountingSubject.DIRECTION_BORROW);
		BigDecimal loanAmount = initBalanceRepo.sumAmountByDirection(fiscalAccountId, 
				FiscalAccountingSubject.DIRECTION_LOAN);
		if(borrowAmount==null) borrowAmount = BigDecimal.ZERO;
		if(loanAmount==null) loanAmount = BigDecimal.ZERO;

		String tip1 = "期初数据借方与贷方金额平衡，借方：%s；贷方：%s；";
		String tip2 = "期初数据借方与贷方金额不平衡，借方：%s；贷方：%s；";
		if(borrowAmount.compareTo(loanAmount)!=0){
			tip2 = String.format(tip2, NumberUtil.stripTrailingZeros(borrowAmount),
					NumberUtil.stripTrailingZeros(loanAmount));
			return buildFailRequestResult(tip2);
		}else{
			tip1 = String.format(tip1, NumberUtil.stripTrailingZeros(borrowAmount),
					NumberUtil.stripTrailingZeros(loanAmount));
			return new RequestResult(RequestResult.RETURN_SUCCESS, tip1);
		}
	}
	

	/**
	 * 查找非核算科目期初数据
	 * @param subjectId
	 * @return
	 */
	public FiscalInitBalanceVo getBySubjectId(String subjectId) {
		List<FiscalInitBalance> list = initBalanceRepo
				.findBySubjectIdAndCheckStatus(subjectId, FiscalInitBalance.UN_CHECK);
		
		FiscalAccountingSubject subject = subjectService.get(subjectId);
		if(list.size()==0){
			return getVo(new FiscalInitBalance(subject));
		}else{
			return getVo(list.get(0));
		}
	}
	
	/**
	 * 从进销存的期初应付、期初应收、期初现金的数据导入相应的科目
	 * @return
	 */
	@Transactional
	public RequestResult saveByInitalPayReceive(){
		
		FiscalAccount acc = SecurityUtil.getFiscalAccount();
		
		if(periodService.countUsedPeriod(acc.getFid())>0){
			return buildFailRequestResult("会计期间已启用或已结账，不能添加或修改");
		}
		
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		User currentUser = SecurityUtil.getCurrentUser();
		
		processYszk(acc, currentUser, org, dept);
		processYfzk(acc, currentUser, org, dept);
		processYhck(acc, currentUser, org, dept);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 应收账款
	 */
	private void processYszk(FiscalAccount acc, User currentUser, Organization org, Organization dept){
		List<FiscalAccountingSubject> yszkSubjects = subjectRepo.findSubjectByTypeAndName(
				FiscalAccountingSubject.TYPE_ZC, YSZK, acc.getFid());
		
		for(FiscalAccountingSubject subject:yszkSubjects){
			
			//只取下级
			List<FiscalAccountingSubject> children = subjectService.getChildren(subject.getFid());
			
			//判断该科目是否有下级科目，有下级科目则根据关联ID写入各销售商的期初金额，无下级科目则判断是否有勾选辅助核算销售商，有勾选则写入各销售商的期初金额，无勾选则合计所有销售商期初应收金额写入该科目
			if(children.size()>0){
				
				for(FiscalAccountingSubject child:children){
					
					//有下级科目则只处理关联数据，不处理核算数据
					if(child.getRelationType()==null||child.getRelationType()!=FiscalAccountingSubject.RELATION_CUSTOMER){
						continue;
					}
					Customer customer = customerRepo.findOne(child.getRelationId());
					BigDecimal initRece = billRepo.sumByCustomer(customer.getFid(), acc.getFid(),
							WarehouseBuilderCodeHelper.qcys);
					
					if(initRece==null)initRece = BigDecimal.ZERO;
					
					deleteBySubject(child);
					if(initRece.compareTo(BigDecimal.ZERO)!=0){
						saveByCustomerSubject(child, customer,initRece, FiscalInitBalance.UN_CHECK, acc, currentUser, 
								org, dept, subject, true);
					}
				}
			}else{
				if(isCustomerOtherCheck(subject))continue;
				if(subject.getCustomerSign()==FiscalAccountingSubject.ACCOUNT){
					
					//根据销售商插入期初数据
					List<Customer> customers = customerRepo.findByOrgId(org.getFid());
					
					BigDecimal total = BigDecimal.ZERO;
					FiscalInitBalance temp = null;
					
					if(customers.size()>0){
						deleteBySubject(subject);
						for(Customer customer:customers){
							BigDecimal initRece = billRepo.sumByCustomer(customer.getFid(), acc.getFid(),
									WarehouseBuilderCodeHelper.qcys);
							if(initRece==null)initRece = BigDecimal.ZERO;
							if(initRece.compareTo(BigDecimal.ZERO)!=0){
								total = NumberUtil.add(total, initRece);
								temp = saveByCustomerSubject(subject, customer,
										initRece, FiscalInitBalance.CHECK, acc, currentUser, 
										org, dept, null, false);
							}
							
						}
						//最后更新核算科目的总金额
						if(temp!=null){
							updateParentAmount(subject, total, temp);
						}
					}
				}else{
					//非核算科目计算所有销售商总金额，写入期初数据
					BigDecimal total = billRepo.sumAmount(acc.getFid(), WarehouseBuilderCodeHelper.qcys);
					if(total==null)total = BigDecimal.ZERO;
					deleteBySubject(subject);
					if(total.compareTo(BigDecimal.ZERO)!=0){
						saveByCustomerSubject(subject, null, total, FiscalInitBalance.UN_CHECK, acc, currentUser, 
							org, dept, subject.getParent(), true);
					}
				}
			}
		}
	}
	
	/**
	 * 应付账款
	 */
	private void processYfzk(FiscalAccount acc, User currentUser, Organization org, Organization dept){
		List<FiscalAccountingSubject> yfzkSubjects = subjectRepo.findSubjectByTypeAndName(
				FiscalAccountingSubject.TYPE_FZ, YFZK, acc.getFid());
		
		for(FiscalAccountingSubject subject:yfzkSubjects){
			
			//只取下级
			List<FiscalAccountingSubject> children = subjectService.getChildren(subject.getFid());
			
			//判断该科目是否有下级科目，有下级科目则根据关联ID写入各供应商的期初金额，无下级科目则判断是否有勾选辅助核算供应商，有勾选则写入各供应商的期初金额，无勾选则合计所有供应商期初应收金额写入该科目
			if(children.size()>0){
				
				for(FiscalAccountingSubject child:children){
					//有下级科目则只处理关联数据，不处理核算数据
					if(child.getRelationType()==null||child.getRelationType()!=FiscalAccountingSubject.RELATION_SUPPLIER){
						continue;
					}
					Supplier supplier = supplierRepo.findOne(child.getRelationId());
					BigDecimal initRece = billRepo.sumBySupplier(supplier.getFid(), acc.getFid(),
							WarehouseBuilderCodeHelper.qcyf);
					
					if(initRece==null)initRece = BigDecimal.ZERO;
					
					deleteBySubject(child);
					if(initRece.compareTo(BigDecimal.ZERO)!=0){
						saveBySupplierSubject(child, supplier, initRece, FiscalInitBalance.UN_CHECK, acc, currentUser, 
								org, dept, subject, true);
					}
					
				}
			}else{
				if(isSupplierOtherCheck(subject))continue;
				if(subject.getSupplierSign()==FiscalAccountingSubject.ACCOUNT){
					
					//根据供应商插入期初数据
					List<Supplier> suppliers = supplierRepo.findByOrgId(org.getFid());
					
					BigDecimal total = BigDecimal.ZERO;
					FiscalInitBalance temp = null;
					
					if(suppliers.size()>0){
						deleteBySubject(subject);
						for(Supplier supplier:suppliers){
							BigDecimal initRece = billRepo.sumBySupplier(supplier.getFid(), acc.getFid(),
									WarehouseBuilderCodeHelper.qcyf);
							if(initRece==null)initRece = BigDecimal.ZERO;
							if(initRece.compareTo(BigDecimal.ZERO)!=0){
								total = NumberUtil.add(total, initRece);
								temp = saveBySupplierSubject(subject, supplier,
										initRece, FiscalInitBalance.CHECK, acc, currentUser, 
										org, dept, null, false);
							}
						}
						//最后更新核算科目的总金额
						if(temp!=null){
							updateParentAmount(subject, total, temp);
						}
					}
				}else{
					//非核算科目计算所有供应商总金额，写入期初数据
					BigDecimal total = billRepo.sumAmount(acc.getFid(), WarehouseBuilderCodeHelper.qcyf);
					if(total==null)total = BigDecimal.ZERO;
					deleteBySubject(subject);
					if(total.compareTo(BigDecimal.ZERO)!=0){
						saveBySupplierSubject(subject, null, total, FiscalInitBalance.UN_CHECK, acc, currentUser, 
								org, dept, subject.getParent(), true);
					}
				}
			}
		}
	}
	
	/**
	 * 期初银行
	 */
	private void processYhck(FiscalAccount acc, User currentUser, Organization org, Organization dept){
		
		List<FiscalAccountingSubject> yhckSubjects = subjectRepo.findSubjectByTypeAndName(
				FiscalAccountingSubject.TYPE_ZC, YHCK, acc.getFid());
		
		for(FiscalAccountingSubject subject:yhckSubjects){
			
			//只取下级
			List<FiscalAccountingSubject> children = subjectService.getChildren(subject.getFid());
			
			//有下级科目则根据关联ID写入各银行的期初金额，无下级科目则合计所有银行期初金额写入该科目
			if(children.size()>0){
				for(FiscalAccountingSubject child:children){
					if(child.getRelationType()==null||child.getRelationType()!=FiscalAccountingSubject.RELATION_BANK){
						continue;
					}
					Bank bank = bankRepo.findOne(child.getRelationId());
					BigDecimal amount = initBankRepo.sumByBank(bank.getFid(), acc.getFid());
					if(amount==null)amount = BigDecimal.ZERO;
					deleteBySubject(child);
					if(amount.compareTo(BigDecimal.ZERO)!=0){
						saveByBankSubject(child, bank, amount, acc, currentUser, org, dept, subject, true);
					}
				}
			}
		}
	}
	
	/**
	 * 判断是否勾选了除了客户的其他核算
	 * @param entity
	 * @return
	 */
	private boolean isCustomerOtherCheck(FiscalAccountingSubject entity){
		Short sign = entity.getCustomerSign();
		entity.setCustomerSign(FiscalAccountingSubject.UN_ACCOUNT);
		boolean flag = false;
		if(subjectService.isAccountSubject(entity)){
			flag = true;
		}
		entity.setCustomerSign(sign);
		return flag;
	}
	
	/**
	 * 判断是否勾选了除了供应商的其他核算
	 * @param entity
	 * @return
	 */
	private boolean isSupplierOtherCheck(FiscalAccountingSubject entity){
		Short sign = entity.getSupplierSign();
		entity.setSupplierSign(FiscalAccountingSubject.UN_ACCOUNT);
		boolean flag = false;
		if(subjectService.isAccountSubject(entity)){
			flag = true;
		}
		entity.setSupplierSign(sign);
		return flag;
	}
	
	
	/**
	 * 根据科目和供应商保存期初数据
	 */
	@Transactional
	private FiscalInitBalance saveBySupplierSubject(FiscalAccountingSubject subject, Supplier supplier,
			BigDecimal amount, short check, FiscalAccount account, User user, 
			Organization org, Organization dept, FiscalAccountingSubject parent, boolean updateParent) {
		FiscalInitBalance entity = new FiscalInitBalance();
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(user);
		entity.setOrg(org);
		entity.setDept(dept);
		entity.setFiscalAccount(account);
		entity.setSubject(subject);
		entity.setSupplier(supplier);
		entity.setDirection(subject.getDirection());
		entity.setAmount(amount);
		entity.setIsCheck(check);
		entity.setIsFill(FiscalInitBalance.FILL);
		initBalanceRepo.save(entity);
		if(updateParent){
			updateParentAmount(parent, amount, entity);
		}
		return entity;
	}
	
	/**
	 * 根据科目和客户保存期初数据
	 */
	@Transactional
	private FiscalInitBalance saveByCustomerSubject(FiscalAccountingSubject subject, Customer customer,
			BigDecimal initPay, short check, FiscalAccount account, User user, 
			Organization org, Organization dept, FiscalAccountingSubject parent, boolean updateParent) {
		FiscalInitBalance entity = new FiscalInitBalance();
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(user);
		entity.setOrg(org);
		entity.setDept(dept);
		entity.setFiscalAccount(account);
		entity.setSubject(subject);
		entity.setCustomer(customer);
		entity.setDirection(subject.getDirection());
		entity.setAmount(initPay);
		entity.setIsCheck(check);
		entity.setIsFill(FiscalInitBalance.FILL);
		initBalanceRepo.save(entity);
		if(updateParent){
			updateParentAmount(parent, initPay, entity);
		}
		return entity;
	}
	
	/**
	 * 根据科目和银行保存期初数据
	 */
	@Transactional
	private FiscalInitBalance saveByBankSubject(FiscalAccountingSubject subject, Bank bank,
			BigDecimal initPay, FiscalAccount account, User user, 
			Organization org, Organization dept, FiscalAccountingSubject parent, boolean updateParent) {
		FiscalInitBalance entity = new FiscalInitBalance();
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(user);
		entity.setOrg(org);
		entity.setDept(dept);
		entity.setFiscalAccount(account);
		entity.setSubject(subject);
		entity.setDirection(subject.getDirection());
		entity.setAmount(initPay);
		entity.setIsCheck(FiscalInitBalance.UN_CHECK);
		entity.setIsFill(FiscalInitBalance.FILL);
		initBalanceRepo.save(entity);
		if(updateParent){
			updateParentAmount(parent, initPay, entity);
		}
		return entity;
	}
	
	
	/**
	 * 导入
	 * @param vo
	 */
	@Transactional
	public RequestResult saveByExcelImport(FiscalInitBalanceVo vo){
		
		String accId = SecurityUtil.getFiscalAccountId();
		if(periodService.countUsedPeriod(accId)>0){
			return buildFailRequestResult("会计期间已启用或已结账，不能添加或修改");
		}
		
		FiscalInitBalance entity = new FiscalInitBalance();
		
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setDept(SecurityUtil.getCurrentDept());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setQuantity(vo.getQuantity());
		entity.setCurrencyAmount(vo.getCurrencyAmount());
		entity.setDescribe(vo.getDescribe());
		entity.setIsCheck(vo.getIsCheck());
		
		RequestResult fillImport = fillRelationData(entity, vo);
		if(!fillImport.isSuccess()){
			return fillImport;
		}
		RequestResult checkImport = checkImportValid(vo, entity);
		if(!checkImport.isSuccess()){
			return checkImport;
		}
		
		
		FiscalInitBalance check = initBalanceRepo.findTopByRelationData(entity);
		if(check!=null){
			if(check.getAmount().compareTo(BigDecimal.ZERO)!=0){
				return buildFailRequestResult("数据已存在，不能导入");
			}else{
				initBalanceRepo.delete(check);
			}
		}
		
		BigDecimal totalDelta = deleteByRelationData(entity);
		BigDecimal delta = vo.getAmount().subtract(totalDelta);
		
		FiscalAccountingSubject subject = entity.getSubject();
		if(vo.getIsCheck()==FiscalInitBalance.CHECK){
			updateParentAmount(subject, delta, entity);
		}else{
			updateParentAmount(subject.getParent(), delta, entity);
		}
		
		//计算完差值金额再把新的金额覆盖
		entity.setAmount(vo.getAmount());
		entity.setIsFill(isFill(subject, vo.getIsCheck()));
		
		initBalanceRepo.save(entity);
		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 检查引入数据是否合法
	 * @param vo
	 * @return
	 */
	private RequestResult checkImportValid(FiscalInitBalanceVo vo, FiscalInitBalance entity) {
		
		FiscalAccountingSubject subject = getSubjectByBalance(vo);
		
		if(subject==null){
			return buildFailRequestResult("找不到科目");
		}
		
		String subjectName = subject.getName();
		if(subject.getFlag()!=FiscalAccountingSubject.FLAG_CHILD){
			return buildFailRequestResult("["+subjectName+"]非叶子节点科目不能导入数据");
		}
		if(subject.getIsCheck()==FiscalAccountingSubject.ACCOUNT){
			if(vo.getIsCheck()!=FiscalInitBalance.CHECK){
				return buildFailRequestResult("["+subjectName+"]该科目只能导入核算数据");
			}
		}
		if(subject.getIsCheck()==FiscalAccountingSubject.UN_ACCOUNT && 
				vo.getIsCheck()!=FiscalInitBalance.UN_CHECK){
			return buildFailRequestResult("["+subjectName+"]该科目不是核算科目");
		}
		
		RequestResult check = checkValid(vo, entity);
		if(!check.isSuccess()){
			return check;
		}
		
		return buildSuccessRequestResult();
	}

	/**
	 * 填充实体类数据
	 * @param entity
	 * @param vo
	 * @return
	 */
	private RequestResult fillRelationData(FiscalInitBalance entity, FiscalInitBalanceVo vo){
		String orgId = SecurityUtil.getCurrentOrgId();
		
		//科目
		FiscalAccountingSubject subject = getSubjectByBalance(vo);
		if(subject!=null){
			entity.setSubject(subject);
			entity.setDirection(subject.getDirection());
			entity.setUnit(subject.getUnit());
			entity.setCurrency(subject.getCurrency());
		}else{
			return buildFailRequestResult("找不到科目");
		}
		
		//供应商
		if(StringUtils.isNotBlank(vo.getSupplierId())){
			entity.setSupplier(supplierRepo.findOne(vo.getSupplierId()));
		}else if(StringUtils.isNotBlank(vo.getSupplierCode())){
			entity.setSupplier(supplierRepo.findTopByCode(orgId, vo.getSupplierCode()));
		}
		
		//客户
		if(StringUtils.isNotBlank(vo.getCustomerId())){
			entity.setCustomer(customerRepo.findOne(vo.getCustomerId()));;
		}else if(StringUtils.isNotBlank(vo.getCustomerCode())){
			entity.setCustomer(customerRepo.findTopByCode(orgId, vo.getCustomerCode()));
		}
		
		//部门
		if(StringUtils.isNotBlank(vo.getDepartmentId())){
			entity.setDepartment(orgRepo.findOne(vo.getDepartmentId()));;
		}else if(StringUtils.isNotBlank(vo.getDepartmentCode())){
			entity.setDepartment(orgRepo.getByCode(orgId, vo.getDepartmentCode(), null));
		}
		
		//职员
		if(StringUtils.isNotBlank(vo.getMemberId())){
			entity.setMember(memberRepo.findOne(vo.getMemberId()));
		}else if(StringUtils.isNotBlank(vo.getMemberCode())){
			entity.setMember(memberRepo.getByCode(orgId, vo.getMemberCode()));
		}
		
		//仓库
		if(StringUtils.isNotBlank(vo.getWarehouseId())){
			entity.setWarehouse(attrService.get(vo.getWarehouseId()));
		}else if(StringUtils.isNotBlank(vo.getWarehouseCode())){
			entity.setWarehouse(attrService.getByCode(orgId, AuxiliaryAttrType.CODE_WAREHOUSE, vo.getWarehouseCode()));
		}
		
		//项目
		if(StringUtils.isNotBlank(vo.getProjectId())){
			entity.setProject(attrService.get(vo.getProjectId()));
		}else if(StringUtils.isNotBlank(vo.getProjectCode())){
			entity.setProject(attrService.getByCode(orgId, AuxiliaryAttrType.CODE_PROJECT, vo.getProjectCode()));
		}
		
		//货品
		if(StringUtils.isNotBlank(vo.getGoodsId())){
			entity.setGoods(goodsRepo.findOne(vo.getGoodsId()));
		}else if(StringUtils.isNotBlank(vo.getGoodsCode())){
			entity.setGoods(goodsRepo.findTopByCode(orgId, vo.getGoodsCode()));
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 根据科目删除期初数据（包含核算数据）
	 * @param subject
	 */
	public void deleteBySubject(FiscalAccountingSubject subject) {
		
		List<FiscalInitBalance> data = initBalanceRepo.findBySubjectId(subject.getFid());
		
		if(data.size()>0){
			BigDecimal total = sumEntityAmount(data);
			total = total.multiply(new BigDecimal(-1));
			updateParentAmount(subject.getParent(), total, data.get(0));
			for(FiscalInitBalance initb:data){
				delete(initb.getFid());
			}
		}
	}
	
	/**
	 * 根据科目维护上级金额
	 * @param
	 */
	@Transactional
	private void updateParentAmount(FiscalAccountingSubject subject, BigDecimal amount, FiscalInitBalance child){
		if(subject==null)return;
		if(amount==null)return;
		if(subjectService.isRoot(subject))return;
		FiscalInitBalance init = initBalanceRepo.findTopUnCheckBySubjectId(subject.getFid());
		if(init==null){
			FiscalInitBalance entity = new FiscalInitBalance(subject);
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(child.getCreator());
			entity.setOrg(child.getOrg());
			entity.setFiscalAccount(child.getFiscalAccount());
//			entity.setDirection(child.getDirection());
			/*
			 * start
			 * cwz 2017-5-8 2067 科目初始数据页面，科目余额方向为贷方时，三级科目下输入科目初始数据，合计到父科目的数据有误
			 * 判断方向是否一致：
			 * 1、当前层级方向和父级方向不同，各自乘以余额方向后再相加
			 * 2、方向相同直接相加
			 * 
			 * */
			Integer childDirection = child.getDirection();
			Integer initDirection = subject.getDirection();
			entity.setDirection(initDirection);
			if(childDirection!=initDirection){
				entity.setAmount(amount.multiply(new BigDecimal(-1)));
			}else{
				entity.setAmount(amount);
			}
			/* end cwz 2017-5-8*/
			entity.setIsCheck(FiscalInitBalance.UN_CHECK);
			entity.setIsFill(FiscalInitBalance.NOT_FILL);
			save(entity);
			updateParentAmount(subject.getParent(), amount, child);
		}else{
			if(init.getAmount()==null)init.setAmount(BigDecimal.ZERO);
			/*
			 * start
			 * cwz 2017-5-8 2067 科目初始数据页面，科目余额方向为贷方时，三级科目下输入科目初始数据，合计到父科目的数据有误
			 * 判断方向是否一致：
			 * 1、当前层级方向和父级方向不同，各自乘以余额方向后再相加
			 * 2、方向相同直接相加
			 * 
			 * */
			Integer childDirection = child.getDirection();
			Integer initDirection = init.getDirection();
			if(childDirection!=initDirection){
				BigDecimal add = init.getAmount().add(amount.multiply(new BigDecimal(child.getDirection()).multiply(new BigDecimal(init.getDirection()))));
				init.setAmount(add);
			}else{
				init.setAmount(init.getAmount().add(amount));
			}
			/* end cwz 2017-5-8*/
			init.setIsFill(FiscalInitBalance.NOT_FILL);
			updateParentAmount(subject.getParent(), amount, child);
		}
	}
	
	
	/**
	 * 根据关联删除相同的数据
	 * @param entity
	 * @return 删除数据的总金额
	 */
	@Transactional
	public BigDecimal deleteByRelationData(FiscalInitBalance entity) {
		List<FiscalInitBalance> exists = initBalanceRepo.findByRelationData(entity);
		BigDecimal total = sumEntityAmount(exists);
		
		for(FiscalInitBalance init:exists){
			delete(init);
		}
		return total;
	}
	
	/**
	 * 根据科目ID删除核算数据
	 * @param fid
	 */
	public void deleteAccountingBySubjectId(String subjectId) {
		List<FiscalInitBalance> accountingList = initBalanceRepo
				.findBySubjectIdAndCheckStatus(subjectId, FiscalInitBalance.CHECK);
		for(FiscalInitBalance initb:accountingList){
			initBalanceRepo.delete(initb);
		}
	}
	
	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	public Long countBySubjectId(String subjectId){
		return initBalanceRepo.countBySubjectId(subjectId);
	}
	
	/**
	 * 根据科目ID、余额方向获取金额
	 * @param code
	 * @return
	 */
	public BigDecimal getAmountBySubjectId(String subjectId, Integer direction, String accId){
		return initBalanceRepo.getAmountBySubjectId(subjectId, direction, accId);
	}

	@Override
	public CrudRepository<FiscalInitBalance, String> getRepository() {
		return initBalanceRepo;
	}
}
