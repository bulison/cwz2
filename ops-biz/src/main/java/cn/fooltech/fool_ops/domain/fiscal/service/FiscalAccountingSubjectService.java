package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.fooltech.fool_ops.component.redis.RedisService;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.asset.service.AssetService;
import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import cn.fooltech.fool_ops.domain.base.repository.DropDataRepository;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.BankRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.SupplierRepository;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrTypeService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.cashier.service.BankBillService;
import cn.fooltech.fool_ops.domain.cashier.service.BankInitService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalInitBalance;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplate;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalTemplateType;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.member.repository.MemberRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.service.BillSubjectDetailService;
import cn.fooltech.fool_ops.domain.voucher.service.BillSubjectService;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherDetailService;
import cn.fooltech.fool_ops.domain.voucher.service.WageVoucherService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.utils.input.FivepenUtils;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>
 * 财务-科目网页服务类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015-11-24 08:34:35
 */
@Service

public class FiscalAccountingSubjectService
		extends BaseService<FiscalAccountingSubject,FiscalAccountingSubjectVo,String > {

	private final int PINYIN_LIMIT = 300;// 拼音长度限制

	private final static String CODE_FORMAT = "^[0-9]+(\\.{1}[0-9]+)*$";
	private final static Pattern CODE_P = Pattern.compile(CODE_FORMAT);
	/**
	 * 财务-科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectRepository subjectRepo;

	@Autowired
	private AuxiliaryAttrRepository attrRepo;
	
	@Autowired
	private AuxiliaryAttrService attrService;

	/**
	 * 用户属性服务类
	 */
	@Autowired
	private UserAttrService userAttrService;

	/**
	 * 属性类型服务类
	 */
	@Autowired
	private AuxiliaryAttrTypeService attrTypeService;

	/**
	 * 单位服务类
	 */
	@Autowired
	private UnitService unitService;

	/**
	 * 模板类型服务类
	 */
	@Autowired
	private FiscalTemplateTypeService templateTypeService;

	/**
	 * 模板服务类
	 */
	@Autowired
	private FiscalTemplateService templateService;

	@Autowired
	private VoucherDetailService voucherDetailService;

	@Autowired
	protected CustomerRepository customerRepo;

	@Autowired
	protected SupplierRepository supplierRepo;

	@Autowired
	protected MemberRepository memberRepo;

	@Autowired
	protected GoodsRepository goodsRepo;

	@Autowired
	protected BankRepository bankRepo;

	@Autowired
	protected OrganizationRepository orgRepo;

	/**
	 * 科目初始数据服务类
	 */
	@Autowired
	private FiscalInitBalanceService initBalanceService;

	/**
	 * 固定资产卡片服务类
	 */
	@Autowired
	private AssetService assetService;

	/**
	 * 单据关联会计科目模板明细服务类
	 */
	@Autowired
	private BillSubjectDetailService billSubjectDetailService;

	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;

	/**
	 * 出纳
	 */
	@Autowired
	private BankBillService bankBillService;

	/**
	 * 银行初始设置
	 */
	@Autowired
	private BankInitService bankInitService;

	/**
	 * 计提税费服务类
	 */
	@Autowired
	private AccrueTaxationService accrueTaxationService;

	/**
	 * 转出未交增值税服务类
	 */
	@Autowired
	private TurnOutTaxService turnOutTaxService;

	/**
	 * 结转损益科目服务类
	 */
	@Autowired
	private CarryForwardProfitLossService carryForwardProfitLossService;
	
	/**
	 * 待摊费用服务类
	 */
	//@Autowired
	//private PrepaidExpensesService prepaidExpensesService;
	
	/**
	 * 工资凭证服务类，用于工资生成凭证
	 */
	@Autowired
	private WageVoucherService wageVoucherService;
	
	@Autowired
	private DropDataRepository dropRepo;

	@Autowired
	private RedisService redisService;
	
	/**
	 * 单据、会计科目关联网页服务类
	 */
	@Autowired
	private BillSubjectService billSubjectWebService;
	/**
	 * 比较器
	 */
	private Comparator<FiscalAccountingSubjectVo> comparatorVo = new Comparator<FiscalAccountingSubjectVo>() {

		@Override
		public int compare(FiscalAccountingSubjectVo o1, FiscalAccountingSubjectVo o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	};

	/**
	 * 查询财务-科目列表信息，按照财务-科目主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param vo
	 */
	@Transactional(readOnly = true)
	public Page<FiscalAccountingSubjectVo> query(FiscalAccountingSubjectVo vo, PageParamater pageParamater) {
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		String accId = SecurityUtil.getFiscalAccountId();
		String name = vo.getName();
		String code = vo.getCode();
		Integer type = vo.getType();
		Short bankSubject = vo.getBankSubject();
		Short cashSubject = vo.getCashSubject();
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		
		return getPageVos(subjectRepo.findPageBy(accId, code, name, type, bankSubject, 
				cashSubject, pageRequest), pageRequest);
	}

	/**
	 * 查询财务-科目列表信息，按照财务-科目主键降序排列<br>
	 * 
	 * @param vo
	 */
	@Transactional(readOnly = true)
	public List<FiscalAccountingSubject> query(FiscalAccountingSubjectVo vo) {

		Sort sort = new Sort(Direction.DESC, "createTime");
		String accId = SecurityUtil.getFiscalAccountId();
		String name = vo.getName();
		String code = vo.getCode();
		Integer type = vo.getType();
		Short bankSubject = vo.getBankSubject();
		Short cashSubject = vo.getCashSubject();
		
		return subjectRepo.findBy(accId, code, name, type, bankSubject, cashSubject, sort);
	}

	/**
	 * 获取下级子节点（只取下级，不取下下级）
	 * 
	 * @param @return
	 */
	@Transactional(readOnly = true)
	public List<FiscalAccountingSubject> getChildren(String fid) {
		return subjectRepo.findByParentId(fid);
	}

	/**
	 * 获取一个主键值
	 *
	 * @param @return
	 */
	@Transactional(readOnly = true)
	public FiscalAccountingSubject getSubjectFid(String relationId) {
		return subjectRepo.queryFidByRelationId(relationId);
	}

	/**
	 * 获取下级子节点（只取下级，不取下下级，分页）
	 * 
	 * @param @return
	 */
	@Transactional(readOnly = true)
	public Page<FiscalAccountingSubjectVo> getChildrenPage(String fid, PageParamater pageParamater) {

		Sort sort = new Sort(Direction.ASC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		
		Page<FiscalAccountingSubject> page = subjectRepo.findPageByParentId(fid, Constants.ROOT, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 获取科目树
	 * 
	 * @param vo
	 * @return
	 */
	public List<FiscalAccountingSubjectVo> getTree(FiscalAccountingSubjectVo vo, Integer closeLevel) {

		boolean tree = true;
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		User creator = SecurityUtil.getCurrentUser();
		Organization org = SecurityUtil.getCurrentOrg();

		createRootIfNotExist(org, account, creator);

		String accId = account.getFid();
		int showRoot = vo.getShowRoot();
		int showDisable = vo.getShowDisable();
		Short flag = vo.getFlag();
		Integer searchType = vo.getSearchType();
		String searchKey = vo.getSearchKey();
		Integer subjectType = vo.getSubjectType();
		Integer direction = vo.getDirection();
		Short bankSubject = vo.getBankSubject();
		Short cashSubject = vo.getCashSubject();
		
		//====================
		if (vo.getFlag() != null) {
			tree = false;
		}
		if (StringUtils.isNotBlank(vo.getSearchKey()) && searchType != null) {
			if (searchType == 0) {
				tree = false;
			} else if (searchType == 1) {
				tree = false;
			}
		}
		if (vo.getBankSubject() != null && FiscalAccountingSubject.BANK_YES == vo.getBankSubject()) {
			tree = false;
		}
		if (vo.getCashSubject() != null && FiscalAccountingSubject.CASH_YES == vo.getCashSubject()) {
			tree = false;
		}
		//====================

		final List<FiscalAccountingSubject> subjects = subjectRepo.findBy(accId, showRoot, 
				showDisable, flag, searchType, searchKey, subjectType, direction, bankSubject, cashSubject);
		final List<FiscalAccountingSubjectVo> vos = getVos(subjects);

		if (tree) {
			FastTreeUtils<FiscalAccountingSubjectVo> fastTreeUtils = new FastTreeUtils<FiscalAccountingSubjectVo>();
			return fastTreeUtils.buildTreeData(vos, closeLevel, comparatorVo, new TreeRootCallBack<FiscalAccountingSubjectVo>() {

				@Override
				public boolean isRoot(FiscalAccountingSubjectVo v) {
					return !findRoot(subjects, v);
				}
			});
		} else {
			return vos;
		}
	}

	/**
	 * 在列表中能否找到父节点
	 * 
	 * @return
	 */
	private boolean findRoot(final List<FiscalAccountingSubject> subjects, final FiscalAccountingSubjectVo child) {
		if (Strings.isNullOrEmpty(child.getParentId())) {
			return false;
		}
		for (FiscalAccountingSubject iter : subjects) {
			if (child.getParentId().equals(iter.getFid())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找当前账套下所有科目
	 * 
	 * @return
	 */
	public List<FiscalAccountingSubject> queryAll() {
		return subjectRepo.findByAccId(SecurityUtil.getFiscalAccountId());
	}

	/**
	 * 单个财务-科目实体转换为vo
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public FiscalAccountingSubjectVo getVo(FiscalAccountingSubject entity) {
		if (entity == null)
			return null;
		FiscalAccountingSubjectVo vo = new FiscalAccountingSubjectVo();
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());
		vo.setType(entity.getType());
		vo.setZjm(entity.getZjm());
		vo.setDirection(entity.getDirection());
		vo.setCashSign(entity.getCashSign());
		vo.setCurrencySign(entity.getCurrencySign());
		vo.setCussentAccountSign(entity.getCussentAccountSign());
		vo.setSupplierSign(entity.getSupplierSign());
		vo.setCustomerSign(entity.getCustomerSign());
		vo.setDepartmentSign(entity.getDepartmentSign());
		vo.setMemberSign(entity.getMemberSign());
		vo.setWarehouseSign(entity.getWarehouseSign());
		vo.setProjectSign(entity.getProjectSign());
		vo.setGoodsSign(entity.getGoodsSign());
		vo.setQuantitySign(entity.getQuantitySign());
		vo.setDescribe(entity.getDescribe());
		vo.setRelationType(entity.getRelationType());
		vo.setRelationId(entity.getRelationId());
		vo.setRelationName(getRelationName(entity));
		vo.setLevel(entity.getLevel());
		vo.setFlag(entity.getFlag());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setEnable(entity.getEnable());
		vo.setFid(entity.getFid());
		vo.setCashSubject(entity.getCashSubject());
		vo.setBankSubject(entity.getBankSubject());

		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}

		AuxiliaryAttr currency = entity.getCurrency();
		if (currency != null) {
			vo.setCurrencyId(currency.getFid());
			vo.setCurrencyName(currency.getName());
			vo.setCurrencyCode(currency.getCode());
		}

		AuxiliaryAttr subject = entity.getSubject();
		if (subject != null) {
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
			vo.setSubjectCode(subject.getCode());
		}

		FiscalAccount account = entity.getFiscalAccount();
		if (account != null) {
			vo.setFiscalAccountId(account.getFid());
			vo.setFiscalAccountName(account.getName());
		}

		FiscalAccountingSubject parent = entity.getParent();
		if (parent != null) {
			vo.setParentId(parent.getFid());
			vo.setParentCode(parent.getCode());
			vo.setParentName(parent.getName());
		}

		Unit unit = entity.getUnit();
		if (unit != null) {
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
			vo.setUnitCode(unit.getCode());
		}

		return vo;
	}

	/**
	 * 删除财务-科目<br>
	 */
	public RequestResult delete(String fid) {

		// 判断是否被引用，有则不能删除
		FiscalAccountingSubject entity = subjectRepo.findOne(fid);
		FiscalAccountingSubject parent = entity.getParent();

		RequestResult checkchild = checkSubjectRef(entity,2);
		if (checkchild.getReturnCode() == RequestResult.RETURN_FAILURE) {
			return checkchild;
		}

		// 父节点除了即将要删除的子节点外没有其他节点则将该节点变成子节点
		if (subjectRepo.countByParentId(parent.getFid()) == 1) {
			parent.setFlag(FiscalAccountingSubject.FLAG_CHILD);
			subjectRepo.save(parent);
		}

		initBalanceService.deleteBySubject(entity);

		subjectRepo.delete(fid);
		return buildSuccessRequestResult();
	}

	/**
	 * 新增/编辑财务-科目（判断条件）
	 * 
	 * @param vo
	 * @param saveType
	 *            保存类型（1：导入，2：表单添加或修改）
	 * @return
	 */
	@Transactional
	public RequestResult save(FiscalAccountingSubjectVo vo, int saveType) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		String accountId = SecurityUtil.getFiscalAccountId();
		String fid = vo.getFid();
		String zjm = vo.getZjm();
		String code = vo.getCode();
		Integer type = vo.getType();
		String parentId = vo.getParentId();
		Short cashSubject = vo.getCashSubject();
		Short bankSubject = vo.getBankSubject();
		if (saveType == 1) {
			// 检索科目类别
			RequestResult response = attrService.checkSubAuxiliaryAttr(AuxiliaryAttrType.CODE_SUBJECT_TYPE,
					vo.getSubjectCode(), vo.getSubjectName());
			// 科目类别-1：错误提示
			if (!response.isSuccess()) {
				return buildFailRequestResult(response.getMessage());
			}
			// 科目类别：编号没有填写，名称有填写：根据名称取数据存在则取存在数据
			if (response.getErrorCode() == 1) {
				String code2 = response.getMessage();
				if (!Strings.isNullOrEmpty(code2)) {
					vo.setSubjectCode(code2);
				} else {
					return buildFailRequestResult("科目类别编号或名称有误");
				}

			}
			// 科目类别：编号有填写，名称没有填写：根据编号取数据存在则取存在数据
			if (response.getErrorCode() == 2) {
				String name = response.getMessage();
				if (!Strings.isNullOrEmpty(name)) {
					vo.setSubjectName(name);
				} else {
					return buildFailRequestResult("科目类别编号或名称有误");
				}

			}
			if(cashSubject+bankSubject>1){
				return buildFailRequestResult("现金科目和银行科目不能同时勾选");
			}
			
		}

		if (vo.getDirection() == null) {
			return buildFailRequestResult("余额方向必填");
		}
		if (type == null) {
			return buildFailRequestResult("科目类型必填");
		}
		if (StringUtils.isBlank(vo.getSubjectId()) && StringUtils.isBlank(vo.getSubjectCode())) {
			return buildFailRequestResult("科目类别必填");
		}
		if (StringUtils.isBlank(code)) {
			return buildFailRequestResult("科目编号必填");
		}
		if (!CODE_P.matcher(code).matches()) {
			return buildFailRequestResult("科目编号格式不正确");
		}
		if (StringUtils.isBlank(vo.getName())) {
			return buildFailRequestResult("科目名称必填");
		}
		if (subjectRepo.countByCode(accountId, code, fid) > 0) {
			return buildFailRequestResult("编号已存在");
		}
		if (subjectRepo.countByZjm(accountId, code, fid) > 0) {
			return buildFailRequestResult("编号和其他科目的助记码冲突");
		}
		if (StringUtils.isNotBlank(zjm)) {
			if (subjectRepo.countByZjm(accountId, zjm, fid) > 0) {
				return buildFailRequestResult("助记码已存在");
			}
			if (subjectRepo.countByCode(accountId, zjm, fid) > 0) {
				return buildFailRequestResult("助记码和其他科目的编号冲突");
			}
		}
		if (checkAccountingSign(vo.getCurrencySign())) {
			if (StringUtils.isBlank(vo.getCurrencyId()) && StringUtils.isBlank(vo.getCurrencyCode())) {
				return buildFailRequestResult("勾选了核算外币币别必填");
			}
		}
		if (checkAccountingSign(vo.getQuantitySign())) {
			if (StringUtils.isBlank(vo.getUnitId()) && StringUtils.isBlank(vo.getUnitCode())) {
				return buildFailRequestResult("勾选了核算数量单位必填");
			}
		}

		FiscalAccountingSubject parent = null;
		if (StringUtils.isNotBlank(parentId)) {
			parent = subjectRepo.findOne(parentId);
		} else if (StringUtils.isNotBlank(code)) {
			if (!code.contains(".")) {
				parent = createRootIfNotExist();
			} else {
				String parentCode = code.substring(0, code.lastIndexOf("."));
				parent = subjectRepo.findTopByCode(accountId, parentCode);
				if (parent == null)
					return buildFailRequestResult("编码填写错误，找不到上级科目");
			}
		}

		if (parent != null) {
			if (!isRoot(parent)) {
				if (code.contains(".")) {
					String left = code.substring(0, code.lastIndexOf("."));
					if (!left.equals(parent.getCode())) {
						return buildFailRequestResult("编号前缀与父节点不一致");
					}
				}
				if (parent.getType() != type) {
					return buildFailRequestResult("科目类型与上级不一致");
				}
				
				if (StringUtils.isBlank(fid)) {
//					if (initBalanceService.countBySubjectId(parent.getFid()) > 0) {
//						return buildFailRequestResult("科目期初数据已引用上级科目，不能新增");
//						initBalanceService.findBySubjectId(parent.getFid());
//					}
//					if (voucherDetailService.countBySubjectId(parent.getFid()) > 0) {
//						return buildFailRequestResult("凭证已引用上级科目，不能新增");
//					}
				}
				 
			}
		} else {
			return new RequestResult(RequestResult.RETURN_FAILURE, "找不到上级科目，不能新增");
		}

		if (StringUtils.isNotBlank(fid)) {
			FiscalAccountingSubject entity = subjectRepo.findOne(vo.getFid());
			if (entity == null) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据不存在，可能已被删除，请刷新再试");
			}

			List<FiscalAccountingSubject> childrens = Lists.newArrayList();
			getAllChildren(entity, childrens);

			// 置为无效
			if (vo.getEnable() == FiscalAccountingSubject.ENABLE_NO) {
				for (FiscalAccountingSubject iter : childrens) {
					if (iter.getEnable() == FiscalAccountingSubject.ENABLE_YES) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "下级所有科目都为无效时才可以置为无效");
					}
				}

				FiscalPeriod period = periodService.getPeriod(new Date(), accountId);
				if (period != null) {
					Long count = voucherDetailService.countBySubjectId(entity.getFid(), period.getStartDate(),
							period.getEndDate());
					if (count!=null && count > 0) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "当前会计期间有该科目的凭证，要等下一个会计期间才可以置为无效");
					}
				}
				
				BigDecimal result = initBalanceService.getAmountBySubjectId(entity.getFid(), entity.getDirection(),
						accountId);
				result = NumberUtil.add(result, voucherDetailService.getAmountBySubjectId(entity.getFid(), accountId,
						Lists.newArrayList(Voucher.STATUS_AUDITED, Voucher.STATUS_POSTED)));
				if (BigDecimal.ZERO.compareTo(result) != 0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "置为无效时结余金额不等于0，不可以修改");
				}
			} else {
				// 判断是否被引用，有则不能修改
				RequestResult check = checkSubjectRef(entity,1);
				if (!check.isSuccess()) {
					return check;
				}

				if (entity.getDirection() != vo.getDirection()) {
					for (FiscalAccountingSubject iter : childrens) {

						// 判断是否被引用，有则不能修改
						RequestResult checkchild = checkSubjectRef(iter,1);
						if (!checkchild.isSuccess()) {
							return checkchild;
						}
					}
				}
			}

			String updateTime = DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME);
			if (!updateTime.equals(vo.getUpdateTime())) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改，请刷新再试");
			}
		}
		return saveEntity(vo);
	}

	/**
	 * 检查科目是否被引用
	 * @param subject 科目对象
	 * @param type 	  类型：1、保存修改；2、删除;0:不处理
	 * @return
	 */
	private RequestResult checkSubjectRef(FiscalAccountingSubject subject,int type) {

		String fid = subject.getFid();
		String subjectName = subject.getName();
		// 判断固定资产是否引用到，有则不能修改、删除
		if (assetService.existRefSubject(fid)) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "固定资产已引用科目[" + subjectName + "]");
		}

		// 判断银行单据记录是否引用到，有则不能修改、删除
		if (bankBillService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "出纳已引用科目[" + subjectName + "]");
		}

		// 判断计提税费是否引用到，有则不能修改、删除
		if (accrueTaxationService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "计提税费已引用科目[" + subjectName + "]");
		}
		
		// 判断结转损益是否引用到，有则不能修改、删除
		if (carryForwardProfitLossService.countBySubjectIdAndType(fid,1) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "结转损益已引用科目[" + subjectName + "]");
		}
		
		// 判断结转制造费用是否引用到，有则不能修改、删除
		if (carryForwardProfitLossService.countBySubjectIdAndType(fid,2) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "结转制造费用已引用科目[" + subjectName + "]");
		}
		// 判断待摊费用是否引用到，有则不能修改、删除
		//if (prepaidExpensesService.countBySubject(fid) > 0) {
		//	return new RequestResult(RequestResult.RETURN_FAILURE, "待摊费用已引用科目[" + subjectName + "]");
		//}
		if (turnOutTaxService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "转出未交增值税已引用科目[" + subjectName + "]");
		}
		
		// 判断是否被引用到科目期初数据，有则不能修改、删除
		if (initBalanceService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "科目期初数据已引用科目[" + subjectName + "]");
		}
		
		// 判断是否被引用到单据、科目关联模板
		if (billSubjectDetailService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "单据、科目关联模板已引用科目[" + subjectName + "]");
		}
		// 判断科目是否被引用到凭证，有则不能修改、删除
		if (voucherDetailService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "凭证已引用科目[" + subjectName + "]");
		}
		
		// 判断工资凭证服务是否被引用到科目，有则不能修改、删除
		if (wageVoucherService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资凭证已引用科目[" + subjectName + "]");
		}
		if (bankInitService.countBySubjectId(fid) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "初始银行设置已引用科目[" + subjectName + "]");
		}
		//删除操作
		if(type==2){
			// 判断是否有子节点
			if (subjectRepo.countByParentId(fid) > 0) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "[" + subjectName + "]"+"存在子节点不能删除");
			}	
		}
		//保存操作
		if(type==1){
			if (subjectRepo.countByParentId(fid) > 0) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "[" + subjectName + "]"+"存在子节点不能修改");
			}	
		}

		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 保存/新增
	 * 
	 * @param vo
	 */

	@Transactional
	private RequestResult saveEntity(FiscalAccountingSubjectVo vo) {
		Integer oldDirection = vo.getDirection();
		FiscalAccountingSubject entity = null;
		if (StringUtils.isBlank(vo.getFid())) {
			entity = new FiscalAccountingSubject();
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setDept(SecurityUtil.getCurrentDept());
		} else {
			entity = subjectRepo.findOne(vo.getFid());
			if (entity == null) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			oldDirection = entity.getDirection();
		}
		entity.setCode(vo.getCode());
		entity.setName(vo.getName());
		entity.setType(vo.getType());
		entity.setZjm(vo.getZjm());
		entity.setDirection(vo.getDirection());
		entity.setCashSign(vo.getCashSign());
		entity.setCurrencySign(vo.getCurrencySign());
		entity.setCussentAccountSign(vo.getCussentAccountSign());
		entity.setSupplierSign(vo.getSupplierSign());
		entity.setCustomerSign(vo.getCustomerSign());
		entity.setDepartmentSign(vo.getDepartmentSign());
		entity.setMemberSign(vo.getMemberSign());
		entity.setWarehouseSign(vo.getWarehouseSign());
		entity.setProjectSign(vo.getProjectSign());
		entity.setGoodsSign(vo.getGoodsSign());
		entity.setQuantitySign(vo.getQuantitySign());
		entity.setDescribe(vo.getDescribe());
		entity.setEnable(vo.getEnable());
		entity.setCashSubject(vo.getCashSubject());
		entity.setBankSubject(vo.getBankSubject());

		String parentId = vo.getParentId();
		String parentCode = vo.getParentCode();
		String orgId = SecurityUtil.getCurrentOrgId();
		String accountId = SecurityUtil.getFiscalAccountId();

		FiscalAccountingSubject parent = null;
		// 父级
		if (StringUtils.isNotBlank(parentId)) {
			parent = subjectRepo.findOne(parentId);
			entity.setParent(parent);
			entity.setLevel(parent.getLevel() + 1);
		} else if (StringUtils.isNotBlank(vo.getParentCode())) {
			parent = subjectRepo.findTopByCode(accountId, parentCode);
			if (parent == null)
				return new RequestResult(RequestResult.RETURN_FAILURE, "上级科目编码填写错误，找不到上级科目");
			entity.setParent(parent);
			entity.setLevel(parent.getLevel() + 1);
		} else {

			String code = vo.getCode();
			if (!code.contains(".")) {
				parent = createRootIfNotExist();
				entity.setParent(parent);
				entity.setLevel(1);
			} else {
				parentCode = code.substring(0, code.lastIndexOf("."));
				parent = subjectRepo.findTopByCode(accountId, parentCode);
				if (parent == null)
					return new RequestResult(RequestResult.RETURN_FAILURE, "编码填写错误，找不到上级科目");
				entity.setParent(parent);
				entity.setLevel(parent.getLevel() + 1);
			}
		}

		// 科目类别
		if (StringUtils.isNotBlank(vo.getSubjectId())) {
			entity.setSubject(attrRepo.findOne(vo.getSubjectId()));
		} else if (StringUtils.isNotBlank(vo.getSubjectCode())) {
			AuxiliaryAttr attrSubject = attrRepo.getByCode(orgId, AuxiliaryAttrType.CODE_SUBJECT_TYPE,
					vo.getSubjectCode(), accountId);
			if (attrSubject == null)
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目类别编码填写错误，找不到科目类别");
			entity.setSubject(attrSubject);
		}

		// 币别
		if (checkAccountingSign(vo.getCurrencySign())) {
			if (StringUtils.isNotBlank(vo.getCurrencyId())) {
				entity.setCurrency(attrRepo.findOne(vo.getCurrencyId()));
			} else if (StringUtils.isNotBlank(vo.getCurrencyCode())) {

				AuxiliaryAttr currency = attrRepo.getByCode(orgId, AuxiliaryAttrType.CODE_CURRENCY,
						vo.getCurrencyCode(), accountId);
				if (currency == null)
					return new RequestResult(RequestResult.RETURN_FAILURE, "币别编码填写错误，找不到币别");
				entity.setCurrency(currency);
			}
		}

		// 单位
		if (checkAccountingSign(vo.getQuantitySign())) {

			if (StringUtils.isNotBlank(vo.getUnitId())) {
				entity.setUnit(unitService.get(vo.getUnitId()));
			} else if (StringUtils.isNotBlank(vo.getUnitCode())) {

				Unit unit2 = unitService.findTopByLeafCode(orgId, vo.getUnitCode());
				/*1805 货品单位组的编码和单位的编码相同，导入科目有核算币别时，提示单位编码不能填写单位组的编码 	2017-4-13*/
				/*先查询子节点，判断是否存在，否则查询父节点 	2017-4-13*/
				if(unit2!=null){
					if (unit2.getEnable() == Unit.UNABLE) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "单位无效");
					}
					entity.setUnit(unit2);
				}else{
					Unit unit = unitService.getByCode(orgId, vo.getUnitCode());
					if (unit == null) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "单位编码填写错误，找不到单位");
					}
					if (unit.getEnable() == Unit.UNABLE) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "单位无效");
					}
					if (unit.getFlag() == Unit.FLAG_GROUP) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "单位编码不能填写单位组的编码");
					}
					entity.setUnit(unit);
				}

				
			}
		}
		//设置关联数据code，因为导入时用了relationCode字段，页面保存用来relationId字段。2016-8-4 cwz
		if(!Strings.isNullOrEmpty(vo.getRelationId()))vo.setRelationCode(vo.getRelationId());
		// 设置关联数据
		if (vo.getRelationType() != null && StringUtils.isNotBlank(vo.getRelationCode())) {

			if (isAccountData(entity)) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目勾选了核算项则不能填写关联数据");
			}

			entity.setRelationType(vo.getRelationType());
			if (StringUtils.isNotBlank(vo.getRelationId())) {
				entity.setRelationId(vo.getRelationId());
			} else {
				RequestResult relation = getRelationIdByCode(vo);
				if (!relation.isSuccess()) {
					return relation;
				}
				if (relation.getData() == null) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "关联数据编码填写错误或无效");
				}
				String relationId = relation.getData().toString();
				if (StringUtils.isBlank(relationId)) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "关联数据编码填写错误或无效");
				}
				entity.setRelationId(relationId);
			}
		} else {
			entity.setRelationType(null);
			entity.setRelationId(null);
		}

		// 当新增时，把期初数据搬到新增的子节点上
		if (StringUtils.isBlank(vo.getFid()) && !isRoot(parent)
//				&& initBalanceService.countBySubjectId(parent.getFid()) > 0
				&& parent.getFlag() == FiscalAccountingSubject.FLAG_CHILD) {

			FiscalAccountingSubject pparent = parent.getParent();
			entity.setLevel(entity.getLevel() - 1);
			entity.setParent(pparent);
			entity.setIsCheck(FiscalAccountingSubject.UN_ACCOUNT);
			entity.setFlag(FiscalAccountingSubject.FLAG_PARENT);
			entity.setName(parent.getName());
			entity.setCode(parent.getCode());
			entity.setDescribe(parent.getDescribe());
			entity.setZjm(parent.getZjm());
			entity.setPinyin(PinyinUtils.getPinyinCode(entity.getName(), PINYIN_LIMIT));
			entity.setFivepen(FivepenUtils.getFivePenCode(entity.getName()));
			fillProperties(parent, entity);

			subjectRepo.save(entity);

			parent.setParent(entity);
			parent.setLevel(parent.getLevel() + 1);
			parent.setFlag(FiscalAccountingSubject.FLAG_CHILD);
			parent.setName(vo.getName());
			parent.setCode(vo.getCode());
			parent.setDescribe(vo.getDescribe());
			parent.setZjm(vo.getZjm());
			parent.setPinyin(PinyinUtils.getPinyinCode(parent.getName(), PINYIN_LIMIT));
			parent.setFivepen(FivepenUtils.getFivePenCode(parent.getName()));
			parent.setRelationType(vo.getRelationType());
			if (StringUtils.isNotBlank(vo.getRelationId())) {
				parent.setRelationId(vo.getRelationId());
			}
			subjectRepo.save(parent);

			if(initBalanceService.countBySubjectId(parent.getFid()) > 0){
				BigDecimal amount = initBalanceService.getAmountBySubjectId(parent.getFid(), null, accountId);
				FiscalInitBalance balance = new FiscalInitBalance(entity);
				balance.setAmount(amount);
				balance.setDept(SecurityUtil.getCurrentDept());
				balance.setOrg(SecurityUtil.getCurrentOrg());
				balance.setFiscalAccount(SecurityUtil.getFiscalAccount());
				balance.setCreateTime(new Date());
				balance.setCreator(SecurityUtil.getCurrentUser());
				balance.setIsCheck(FiscalInitBalance.UN_CHECK);
				balance.setIsFill(FiscalInitBalance.NOT_FILL);
				balance.setDirection(entity.getDirection());
				initBalanceService.save(balance);
			}
			
		} else {
			if (Strings.isNullOrEmpty(entity.getFid())) {
				entity.setFlag(FiscalAccountingSubject.FLAG_CHILD);
			} else {
				if (subjectRepo.countByParentId(entity.getFid()) > 0) {
					entity = setToParentFlag(entity);
				} else {
					entity.setFlag(FiscalAccountingSubject.FLAG_CHILD);
				}

				// 如果修改科目方向，则要修改下属科目方向
				if (!Strings.isNullOrEmpty(vo.getFid())) {
					if (oldDirection != vo.getDirection()) {
						List<FiscalAccountingSubject> childrens = Lists.newArrayList();
						getAllChildren(entity, childrens);
						for (FiscalAccountingSubject iter : childrens) {
							iter.setDirection(vo.getDirection());
							subjectRepo.save(iter);
						}
					}
				}
			}

			if (parent != null) {
				parent = setToParentFlag(parent);
				subjectRepo.save(parent);
			}
		}

		entity.setIsCheck(isCheckSubject(entity));
		entity.setPinyin(PinyinUtils.getPinyinCode(vo.getName(), PINYIN_LIMIT));
		entity.setFivepen(FivepenUtils.getFivePenCode(vo.getName()));

		subjectRepo.save(entity);

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}


	/**
	 * 设置属性变成父节点
	 * 
	 * @param entity
	 * @return
	 */
	private FiscalAccountingSubject setToParentFlag(FiscalAccountingSubject entity) {
		entity.setCustomerSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setSupplierSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setWarehouseSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setDepartmentSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setMemberSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setGoodsSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setProjectSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setQuantitySign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setCussentAccountSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setCurrencySign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setCashSign(FiscalAccountingSubject.UN_ACCOUNT);
		entity.setUnit(null);
		entity.setCurrency(null);
		entity.setRelationId(null);
		entity.setRelationType(null);
		entity.setFlag(FiscalAccountingSubject.FLAG_PARENT);
		return entity;
	}

	/**
	 * 获得关联对象ID
	 * @param vo
	 * @return
	 */
	private RequestResult getRelationIdByCode(FiscalAccountingSubjectVo vo) {
		Integer relationType = vo.getRelationType();
		String orgId = SecurityUtil.getCurrentOrgId();
		String relationCode = vo.getRelationCode();
		String accountId = SecurityUtil.getFiscalAccountId();

		Object result = null;
		String msg = "";

		if (relationType == null)
			return null;
		switch (relationType) {
		case FiscalAccountingSubject.RELATION_BANK:
			Bank bank = bankRepo.findTopByCode(orgId, relationCode);
			result = bank == null ? null : bank.getFid();
			break;
		case FiscalAccountingSubject.RELATION_SUPPLIER:
			Supplier supplier = supplierRepo.findTopByCode(orgId, relationCode);
			result = supplier == null ? null : supplier.getFid();
			break;
		case FiscalAccountingSubject.RELATION_CUSTOMER:
			Customer customer = customerRepo.findTopByCode(orgId, relationCode);
			result = customer == null ? null : customer.getFid();
			break;
		case FiscalAccountingSubject.RELATION_DEPARTMENT:
			Organization department = orgRepo.getByCode(orgId, relationCode);
			result = department == null ? null : department.getFid();
			break;
		case FiscalAccountingSubject.RELATION_MEMBER:
			Member member = memberRepo.getByCode(orgId, relationCode);
			result = member == null ? null : member.getFid();
			break;
		case FiscalAccountingSubject.RELATION_WAREHOUSE:
			AuxiliaryAttr warehouse = attrRepo.getByCode(orgId, AuxiliaryAttrType.CODE_WAREHOUSE, relationCode);
			result = warehouse == null ? null : warehouse.getFid();
			break;
		case FiscalAccountingSubject.RELATION_PROJECT:
			AuxiliaryAttr project = attrRepo.getByCode(orgId, AuxiliaryAttrType.CODE_PROJECT, relationCode,
					accountId);
			result = project == null ? null : project.getFid();
			break;
		case FiscalAccountingSubject.RELATION_GOODS:
			Goods goods = goodsRepo.findTopByCode(orgId, relationCode);
			// 如果为空，关联数据编码填写错误或无效
			if (goods == null)
				return new RequestResult(RequestResult.RETURN_FAILURE, "关联数据编码填写错误或无效");
			if (goods.getFlag().equals(Goods.FLAG_GROUP)) {
				msg = "不能使用货品组";
				return new RequestResult(RequestResult.RETURN_FAILURE, msg);
			}
			result = goods == null ? null : goods.getFid();
			break;
		}

		return buildSuccessRequestResult(result);
	}

	/**
	 * 科目编号取上一个同级别科目编号加一
	 * 
	 * @param fid
	 */
	public String getCode(String fid) {

		FiscalAccountingSubject parent = null;
		if (StringUtils.isNotBlank(fid)) {
			parent = subjectRepo.findOne(fid);
		}
		if (parent == null) {
			return "";
		}
		if (parent.getCode().equals(Constants.ROOT)) {
			return "";
		}

		String code = subjectRepo.getBrotherCode(parent.getFid());
		if (code == null) {
			return parent.getCode() + ".01";
		}

		return getNextCode(code);
	}
	
	/**
	 * 获得当前编号的下一编号
	 * 
	 * @param code
	 */
	private String getNextCode(String code) {
		String left = "";
		String right = code;
		if (code.contains(".")) {
			left = code.substring(0, code.lastIndexOf("."));
			right = code.substring(code.lastIndexOf(".") + 1);
		}
		Integer num = Integer.parseInt(right) + 1;
		String numStr = num.toString();
		while (numStr.length() < right.length()) {
			numStr = "0" + numStr;
		}
		if (left.length() > 0)
			return left + "." + numStr;
		else
			return numStr;
	}

	/**
	 * 初始化科目
	 * @param pwd 验证密码
	 * @param type 1、财务科目初始化；2、数据初始化
	 * @return
	 */
	@Transactional
	public RequestResult saveInitialize(String pwd,int type,String accountId) {
		boolean checkUserPwd = checkUserPwd(pwd);
		if(!checkUserPwd){
			return buildFailRequestResult("输入密码有误,格式化科目失败。");
		}
		String accId = SecurityUtil.getFiscalAccountId();

		//删除所有固定资产
		dropRepo.deleteDataByAccId("AssetDetail", accId);
		dropRepo.deleteDataByAccId("Asset", accId);
		
		//删除所有银行单据记录
		dropRepo.deleteDataByAccId("BankBill", accId);
		
		//删除所有计提税费
		dropRepo.deleteDataByAccId("AccrueTaxation", accId);
		
		//删除所有结转损益科目
		dropRepo.deleteDataByAccId("CarryForwardProfitLoss", accId);
		
		//删除所有财务-科目初始数据
		dropRepo.deleteDataByAccId("FiscalInitBalance", accId);
		
		//删除所有待摊费用
		dropRepo.deleteDataByAccId("PrepaidExpenses", accId);
		
		//删除所有待摊费用明细
		dropRepo.deleteDataByAccId("PrepaidExpensesDetail", accId);
		
		//删除所有转出未交增值税
		dropRepo.deleteDataByAccId("TurnOutTax", accId);
		
		//删除所有制作凭证模板,清除账套关联数据
		dropRepo.deleteDataByAccId("BillSubjectDetail", accId);
		dropRepo.deleteDataByAccId("BillSubject", accId);
		
		//删除所有凭证
		dropRepo.deleteDataByAccId("VoucherDetail", accId);
		dropRepo.deleteDataByAccId("Voucher", accId);
		
		//删除所有工资凭证
		dropRepo.deleteDataByAccId("WageVoucher", accId);
		
		//删除初始银行
		dropRepo.deleteDataByAccId("BankInit", accId);
		
		// 3、删除所有科目
		if (type==1) {
			subjectRepo.deleteByAccId(accId);
		}
		dropRepo.deleteFiscalMultiColumnDetailByMasterAccId("FiscalMultiColumnDetail",accId);
		dropRepo.deleteDataByAccId("FiscalMultiColumn",accId);
		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}


	/**
	 * 根据模板导入科目
	 */
	@Transactional
	public RequestResult saveByTemplate(String templateTypeId,String pwd) {
		boolean checkUserPwd = checkUserPwd(pwd);
		if(!checkUserPwd){
			return new RequestResult(RequestResult.RETURN_FAILURE,"输入密码有误,加载模版失败。");
		}

		FiscalTemplateType templateType = templateTypeService.get(templateTypeId);
		if (templateType == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "找不到模板");
		}
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		// 1、初始化科目
		saveInitialize(pwd,1,fiscalAccountId);
		
		saveByTemplateType(templateTypeId);
		// 初始化制作凭证模版,导入excel数据 2016-7-26 cwz
		billSubjectWebService.initBillSubject();

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}

	/**
	 * 根据模板初始化所有科目
	 * 
	 * @param templateTypeId
	 */
	private void saveByTemplateType(String templateTypeId) {
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		String orgId = org.getFid();
		User creator = SecurityUtil.getCurrentUser();

		Map<String, AuxiliaryAttr> auxiMaps = Maps.newHashMap();
		Map<String, FiscalAccountingSubject> subjectMaps = Maps.newHashMap();

		List<FiscalTemplate> templateList = templateService.findByTempateType(templateTypeId);

		FiscalAccountingSubject root = createRootIfNotExist(org, account, creator);

		for (FiscalTemplate template : templateList) {

			String code = template.getAttrCode();
			String key = AuxiliaryAttrType.CODE_SUBJECT_TYPE + "_" + code;
			AuxiliaryAttr addAttr = auxiMaps.get(key);
			if (addAttr == null) {
				addAttr = attrRepo.getByCode(orgId, AuxiliaryAttrType.CODE_SUBJECT_TYPE, code, account.getFid());
				if (addAttr == null) {
					// 没有找到则需要新建该属性
					addAttr = new AuxiliaryAttr();
					AuxiliaryAttrType attrType = attrTypeService.findByCode(AuxiliaryAttrType.CODE_SUBJECT_TYPE, orgId,
							account.getFid());
					addAttr.setCategory(attrType);
					addAttr.setCode(code);
					addAttr.setEnable(AuxiliaryAttr.STATUS_ENABLE);
					addAttr.setOrg(org);
					addAttr.setFiscalAccount(account);
					addAttr.setCreateTime(Calendar.getInstance().getTime());
					addAttr.setCreator(creator);
					addAttr.setName(template.getAttr());
					attrRepo.save(addAttr);
				}
				auxiMaps.put(key, addAttr);
			}

			FiscalAccountingSubject subject = VoFactory.createValue(FiscalAccountingSubject.class, template);
			subject.setFid(null);
			subject.setCreator(creator);
			subject.setOrg(org);
			subject.setFiscalAccount(account);
			subject.setEnable(FiscalAccountingSubject.ENABLE_YES);
			subject.setSubject(addAttr);
			subject.setCreateTime(Calendar.getInstance().getTime());

			String subjectCode = subject.getCode();

			// 分割父编号
			int index = subjectCode.lastIndexOf(".");
			if (index >= 0) {
				String parentCode = subjectCode.substring(0, subjectCode.lastIndexOf("."));
				FiscalAccountingSubject parent = subjectMaps.get(parentCode);
				subject.setParent(parent);
				subject.setLevel(parent.getLevel() + 1);
				subject.setFlag(FiscalAccountingSubject.FLAG_CHILD);

				if (parent.getFlag() != FiscalAccountingSubject.FLAG_PARENT) {
					parent.setFlag(FiscalAccountingSubject.FLAG_PARENT);
					subjectRepo.save(parent);
				}
			} else {
				subject.setLevel(1);
				subject.setFlag(FiscalAccountingSubject.FLAG_CHILD);
				subject.setParent(root);
			}
			subject.setIsCheck(isCheckSubject(subject));
			subject.setPinyin(PinyinUtils.getPinyinCode(subject.getName(), PINYIN_LIMIT));
			subject.setFivepen(FivepenUtils.getFivePenCode(subject.getName()));
			subjectRepo.save(subject);
			subjectMaps.put(subjectCode, subject);
		}
	}

	/**
	 * 获取关联名称
	 * 
	 * @param subject
	 * @return
	 */
	private String getRelationName(FiscalAccountingSubject subject) {
		if (Strings.isNullOrEmpty(subject.getRelationId()))
			return "";
		if (subject.getRelationType() == null)
			return "";

		String relationId = subject.getRelationId();
		switch (subject.getRelationType()) {
		case FiscalAccountingSubject.RELATION_BANK:
			Bank bank = bankRepo.findOne(relationId);
			return bank.getName();
		case FiscalAccountingSubject.RELATION_SUPPLIER:
			Supplier supplier = supplierRepo.findOne(relationId);
			return supplier.getName();
		case FiscalAccountingSubject.RELATION_CUSTOMER:
			Customer customer = customerRepo.findOne(relationId);
			return customer.getName();
		case FiscalAccountingSubject.RELATION_DEPARTMENT:
			Organization department = orgRepo.findOne(relationId);
			return department.getOrgName();
		case FiscalAccountingSubject.RELATION_MEMBER:
			Member member = memberRepo.findOne(relationId);
			return member.getUsername();
		case FiscalAccountingSubject.RELATION_WAREHOUSE:
			AuxiliaryAttr warehouse = attrRepo.findOne(relationId);
			return warehouse.getName();
		case FiscalAccountingSubject.RELATION_PROJECT:
			AuxiliaryAttr project = attrRepo.findOne(relationId);
			return project.getName();
		case FiscalAccountingSubject.RELATION_GOODS:
			Goods good = goodsRepo.findOne(relationId);
			return good.getName();
		}
		return "";
	}

	/**
	 * 引入
	 * 
	 * @param subjectId
	 *            :科目ID
	 * @param relationType
	 *            :1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品、9现金
	 */
	public RequestResult saveImport(String subjectId, int relationType) {

		if (initBalanceService.countBySubjectId(subjectId) > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "科目已有期初数据，不能引入");
		}

		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalAccountingSubject parent = subjectRepo.findOne(subjectId);
		if (parent.getFlag() != FiscalAccountingSubject.FLAG_CHILD) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "非明细科目不能引入");
		}
		if (StringUtils.isNotBlank(parent.getRelationId())) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "科目已关联数据，不能引入");
		}
		if (isAccountSubject(parent)) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "科目已勾选核算项，不能引入");
		}

		List<FiscalAccountingSubject> children = Lists.newArrayList(parent.getChilds());
		List<FiscalAccountingSubject> newdata = Lists.newArrayList();

		switch (relationType) {
		case FiscalAccountingSubject.RELATION_BANK:
			if (parent.getCashSubject()!=null&&parent.getCashSubject()==1) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "现金科目已勾选,不能引入");
			}
			List<Bank> banks = bankRepo.findByOrgIdAndType(orgId, Bank.TYPE_BANK);
			addNewChildren(children, banks, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_SUPPLIER:
			List<Supplier> suppliers = supplierRepo.findByOrgId(orgId);
			addNewChildren(children, suppliers, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_CUSTOMER:
			List<Customer> customers = customerRepo.findByOrgId(orgId);
			addNewChildren(children, customers, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_DEPARTMENT:
			List<Organization> departments = orgRepo.findAllLeaf(orgId);
			addNewChildren(children, departments, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_MEMBER:
			List<Member> members = memberRepo.findByEnableAndOrgId(orgId, Member.ENABLE);
			addNewChildren(children, members, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_WAREHOUSE:
			List<AuxiliaryAttr> warehouses = attrRepo.findAllLeafByOrgId(orgId, AuxiliaryAttrType.CODE_WAREHOUSE);
			addNewChildren(children, warehouses, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_PROJECT:
			List<AuxiliaryAttr> projects = attrRepo.findAllLeafByAccId(accId, AuxiliaryAttrType.CODE_PROJECT);
			addNewChildren(children, projects, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_GOODS:
			List<Goods> goods = goodsRepo.findAllLeafByOrgId(orgId);
			addNewChildren(children, goods, newdata, relationType, parent);
			break;
		case FiscalAccountingSubject.RELATION_CASH:
			if (parent.getBankSubject()!=null&&parent.getBankSubject()==1) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "银行科目已勾选,不能引入");
			}
			List<Bank> cash = bankRepo.findByOrgIdAndType(orgId, Bank.TYPE_CASH);
			addNewChildren(children, cash, newdata, relationType, parent);
			break;
		}
		//批量插入引用
		saveImportList(newdata, parent);

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}

	/**
	 * 引入相应数据关系
	 * 
	 * @param children
	 * @param dataList
	 * @param newdata
	 * @param relationType
	 * @param parent
	 */
	private void addNewChildren(final List<FiscalAccountingSubject> children, final List<? extends BasePO> dataList,
			final List<FiscalAccountingSubject> newdata, int relationType, FiscalAccountingSubject parent) {

		String currentCode = getCode(parent.getFid());
		for (BasePO data : dataList) {
			boolean finded = false;
			for (FiscalAccountingSubject child : children) {
				if (child.getRelationType() == relationType && data.getFid().equals(child.getRelationId())) {
					finded = true;
					break;
				}
			}
			if (finded)
				continue;

			FiscalAccountingSubject newChild = getNewChild(data, relationType, parent, currentCode);
			currentCode = getNextCode(currentCode);
			if (parent.getFlag() == FiscalAccountingSubject.FLAG_CHILD) {
				parent.setFlag(FiscalAccountingSubject.FLAG_PARENT);
				subjectRepo.save(parent);
			}
			newdata.add(newChild);
		}

	}

	
	/**
	 * 根据数据关系生成新的科目
	 * 
	 * @param data
	 * @param relationType
	 * @param parent
	 * @param currentCode
	 * @return
	 */
	private FiscalAccountingSubject getNewChild(BasePO data, int relationType, FiscalAccountingSubject parent,
			String currentCode) {
		FiscalAccountingSubject entity = new FiscalAccountingSubject();
		entity.setFid(null);
		entity.setBankSubject(parent.getBankSubject());
		entity.setCashSubject(parent.getCashSubject());
		entity.setDirection(parent.getDirection());
		entity.setType(parent.getType());
		entity.setCreateTime(Calendar.getInstance().getTime());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setRelationId(data.getFid());
		entity.setRelationType(relationType);
		entity.setEnable(FiscalAccountingSubject.ENABLE_YES);
		entity.setParent(parent);
		entity.setFlag(FiscalAccountingSubject.FLAG_CHILD);
		entity.setLevel(parent.getLevel() + 1);
		entity.setDescribe("");
		entity.setCode(currentCode);
		entity.setOrg(parent.getOrg());
		entity.setFiscalAccount(parent.getFiscalAccount());
		entity.setSubject(parent.getSubject());
		entity.setCurrency(parent.getCurrency());
		entity.setUnit(parent.getUnit());

		switch (relationType) {
		case FiscalAccountingSubject.RELATION_BANK:
			Bank bank = (Bank) data;
			entity.setName(bank.getName());
			if (bank.getType() == Bank.TYPE_BANK) {
				entity.setBankSubject(FiscalAccountingSubject.BANK_YES);
			} else if (bank.getType() == Bank.TYPE_CASH) {
				entity.setCashSubject(FiscalAccountingSubject.CASH_YES);
			}
			break;
		case FiscalAccountingSubject.RELATION_SUPPLIER:
			Supplier supplier = (Supplier) data;
			entity.setName(supplier.getName());
			break;
		case FiscalAccountingSubject.RELATION_CUSTOMER:
			Customer customer = (Customer) data;
			entity.setName(customer.getName());
			break;
		case FiscalAccountingSubject.RELATION_DEPARTMENT:
			Organization org = (Organization) data;
			entity.setName(org.getOrgName());
			break;
		case FiscalAccountingSubject.RELATION_MEMBER:
			Member member = (Member) data;
			entity.setName(member.getUsername());
			break;
		case FiscalAccountingSubject.RELATION_WAREHOUSE:
			AuxiliaryAttr warehouse = (AuxiliaryAttr) data;
			entity.setName(warehouse.getName());
			break;
		case FiscalAccountingSubject.RELATION_PROJECT:
			AuxiliaryAttr project = (AuxiliaryAttr) data;
			entity.setName(project.getName());
			break;
		case FiscalAccountingSubject.RELATION_GOODS:
			Goods goods = (Goods) data;
			entity.setName(goods.getName());
			break;
		case FiscalAccountingSubject.RELATION_CASH:
			Bank cash = (Bank) data;
			entity.setName(cash.getName());
			if (cash.getType() == Bank.TYPE_BANK) {
				entity.setBankSubject(FiscalAccountingSubject.BANK_YES);
			} else if (cash.getType() == Bank.TYPE_CASH) {
				entity.setCashSubject(FiscalAccountingSubject.CASH_YES);
			}
			break;
		}

		return entity;
	}

	/**
	 * 根据编号、助记码模糊查找
	 * @return
	 */
	public List<FiscalAccountingSubjectVo> getSubject(String parentId, String searchKey, 
			Integer direction, Short leafFlag, Short bankSubject, Short cashSubject) {
		String accId = SecurityUtil.getFiscalAccountId();
		String inputType = userAttrService.getInputType();

		List<FiscalAccountingSubject> list = subjectRepo.vagueSearch(inputType, parentId, searchKey, 
				direction, leafFlag, bankSubject, cashSubject, accId);
		return getVos(list);
	}

	/**
	 * 根据父节点查所有子
	 * 
	 * @param parentId:父节点ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FiscalAccountingSubjectVo> getTreeByParentId(String parentId) {
		List<FiscalAccountingSubject> list = Lists.newLinkedList();

		FiscalAccountingSubject parent = subjectRepo.findOne(parentId);
		if (parent == null)
			return Collections.EMPTY_LIST;
		getAllChildren(parent, list);

		parent.setParent(null);
		list.add(0, parent);

		FastTreeUtils<FiscalAccountingSubjectVo> fastTreeUtils = new FastTreeUtils<FiscalAccountingSubjectVo>();
		return fastTreeUtils.buildTreeData(getVos(list), 1, comparatorVo);
	}
	
	/**
	 * 获取所有子科目，包含下下级科目
	 * @param entity
	 * @return
	 */
	public void getAllChildren(
			FiscalAccountingSubject entity, final List<FiscalAccountingSubject> childs) {
		List<FiscalAccountingSubject> children = getChildren(entity.getFid());
		
		childs.addAll(children);
		for(FiscalAccountingSubject iter:children){
			getAllChildren(iter, childs);
		}
	}

	/**
	 * 根据编号查找
	 * 
	 * @param code
	 */
	public FiscalAccountingSubject getByCode(String code) {
		String accId = SecurityUtil.getFiscalAccountId();
		return subjectRepo.findTopByCode(accId,code);
	}

	/**
	 * 根据科目类型获取科目
	 * 
	 * @param type
	 *            科目类型
	 * @param flag
	 *            节点标识
	 * @return
	 */
	public List<FiscalAccountingSubjectVo> getSubjectByType(int type, short flag) {
		String accId = SecurityUtil.getFiscalAccountId();
		List<FiscalAccountingSubject> entities = subjectRepo.findByTypeAndFlag(accId, type, flag);
		return getVos(entities);
	}

	
	/**
	 * 根据子节点查询所有父科目名称，用|隔开
	 * @param id:子节点ID
	 * @return
	 */
	public String getParentNamesById(String id) {
		FiscalAccountingSubject subject = subjectRepo.findOne(id);
		String name = "";
		FiscalAccountingSubject parent = subject.getParent();
		while(!isRoot(parent)){
			name = parent.getName()+"|"+name;
			parent = parent.getParent();
		}
		return name;
	}
	
	
	/**
	 * 验证密码输入
	 * @param pwd 密码
	 * @return
	 */
	public boolean checkUserPwd(String pwd){
		return SecurityUtil.checkUserPwd(pwd);
	}
	
	
	/**
	 * 批量插入引入数据，引入项的第一个子节点引用父节点的id，防止插入了新科目，其他引用项还是会引用父科目，变为引用第一个子科目
	 * @param newdata 引入的数据集
	 * @param parent  父节点对象	
	 */
	@Transactional
	private void saveImportList(List<FiscalAccountingSubject>newdata,FiscalAccountingSubject parent){
		int temp=0;
		for (FiscalAccountingSubject newEntity : newdata) {
			if(temp==0){	
				String name = newEntity.getName();
				String code = newEntity.getCode();
				String describe = newEntity.getDescribe();
				String zjm = newEntity.getZjm();
				Short cashSubject = newEntity.getCashSubject();
				Short bankSubject = newEntity.getBankSubject();
				Integer relationType = newEntity.getRelationType();
				String relationId = newEntity.getRelationId();
				FiscalAccountingSubject pparent = parent.getParent();
				newEntity.setLevel(newEntity.getLevel() - 1);
				newEntity.setParent(pparent);
				newEntity.setIsCheck(FiscalAccountingSubject.UN_ACCOUNT);
				newEntity.setFlag(FiscalAccountingSubject.FLAG_PARENT);
				newEntity.setName(parent.getName());
				newEntity.setCode(parent.getCode());
				newEntity.setDescribe(parent.getDescribe());
				newEntity.setZjm(parent.getZjm());
				newEntity.setPinyin(PinyinUtils.getPinyinCode(newEntity.getName(), PINYIN_LIMIT));
				newEntity.setFivepen(FivepenUtils.getFivePenCode(newEntity.getName()));
				fillProperties(parent, newEntity);
				subjectRepo.save(newEntity);
				parent.setParent(newEntity);
				parent.setLevel(parent.getLevel() + 1);
				parent.setFlag(FiscalAccountingSubject.FLAG_CHILD);
				parent.setName(name);
				parent.setCode(code);
				parent.setDescribe(describe);
				parent.setZjm(zjm);
				parent.setCashSubject(cashSubject);
				parent.setBankSubject(bankSubject);
				parent.setRelationType(relationType);
				parent.setRelationId(relationId);
				parent.setPinyin(PinyinUtils.getPinyinCode(parent.getName(), PINYIN_LIMIT));
				parent.setFivepen(FivepenUtils.getFivePenCode(parent.getName()));
				subjectRepo.save(parent);
			}else{
				FiscalAccountingSubject pparent = parent.getParent();
				newEntity.setParent(pparent);
				subjectRepo.save(newEntity);
			}
			temp++;
			newEntity.setIsCheck(isCheckSubject(newEntity));
			newEntity.setPinyin(PinyinUtils.getPinyinCode(newEntity.getName(), PINYIN_LIMIT));
			newEntity.setFivepen(FivepenUtils.getFivePenCode(newEntity.getName()));
			subjectRepo.save(newEntity);
		}
	}
	
	/**
	 * 判断是否核算科目
	 * @param subject
	 * @return
	 */
	private Short isCheckSubject(FiscalAccountingSubject subject) {
		if (isAccountSubject(subject)) {
			return FiscalAccountingSubject.ACCOUNT;
		} else {
			return FiscalAccountingSubject.UN_ACCOUNT;
		}
	}
	
	/**
	 * 根据编号查找本节点下第一个叶子节点
	 * @param code
	 * @return
	 */
	public FiscalAccountingSubject getFirstLeafByCode(String code){
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalAccountingSubject subject = subjectRepo.findTopByCode(accId, code);
		if(subject ==null) return null;
		return getFirstLeaf(subject);
	}

	/**
	 * 根据id查找本节点下第一个叶子节点
	 * @return
	 */
	public FiscalAccountingSubject getFirstLeaf(FiscalAccountingSubject parent) {
		
		FiscalAccountingSubject subject = subjectRepo.findTopByParentId(parent.getFid());
		if(subject != null){
			return getFirstLeaf(subject);
		}
		return parent;
	}
	
	/**
	 * 根据起始编号和结束编号查找有核算数量的科目
	 * @param start
	 * @param end
	 * @param accId 
	 * @return
	 */
	public List<FiscalAccountingSubject> queryByLimit(String start,String end, Integer level, String accId){
		return subjectRepo.findBy(start, end, level, accId);
	}
	
	/**
	 * 获取下一个科目
	 * @return
	 */
	public FiscalAccountingSubject getNextSubject(String curSubjectId, String startCode, 
			String endCode, Integer level){
		String accId = SecurityUtil.getFiscalAccountId();
		return subjectRepo.findTopByNextSubject(accId, curSubjectId, startCode, endCode, level);
	}
	
	/**
	 * 获取上一个科目
	 * @return
	 */
	public FiscalAccountingSubject getLastSubject(String curSubjectId, String startCode, 
			String endCode, Integer level){
		String accId = SecurityUtil.getFiscalAccountId();
		return subjectRepo.findTopByLastSubject(accId, curSubjectId, startCode, endCode, level);
	}
	
	/**
	 * 查找默认科目
	 * @return
	 */
	public FiscalAccountingSubjectVo findDefaultSubjectVo(){
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "code");
		return getVo(subjectRepo.findDefaultSubject(accId, sort));
	}

	@Override
	public CrudRepository<FiscalAccountingSubject, String> getRepository() {
		return subjectRepo;
	}


	/**
	 * 判断是否根节点
	 * @param entity
	 * @return
	 */
	public boolean isRoot(FiscalAccountingSubject entity){
		if(entity.getParent()==null){
			return true;
		}
		if(entity.getCode()!=null && Constants.ROOT.equals(entity.getCode())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断一个科目是否核算科目
	 * @return
	 */
	public boolean isAccountSubject(FiscalAccountingSubject subject){
		if(subject.getFlag()==FiscalAccountingSubject.FLAG_PARENT){
			return false;
		}
		//数量
		if(checkAccountingSign(subject.getQuantitySign())){
			return true;
		}
		//外币
		if(checkAccountingSign(subject.getCurrencySign())){
			return true;
		}
		//供应商
		if(checkAccountingSign(subject.getSupplierSign())){
			return true;
		}
		//客户
		if(checkAccountingSign(subject.getCustomerSign())){
			return true;
		}
		//部门
		if(checkAccountingSign(subject.getDepartmentSign())){
			return true;
		}
		//职员
		if(checkAccountingSign(subject.getMemberSign())){
			return true;
		}
		//仓库
		if(checkAccountingSign(subject.getWarehouseSign())){
			return true;
		}
		//项目
		if(checkAccountingSign(subject.getProjectSign())){
			return true;
		}
		//货品
		if(checkAccountingSign(subject.getGoodsSign())){
			return true;
		}
		//单位
		if(checkAccountingSign(subject.getQuantitySign())){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否核算项
	 * @param sign
	 * @return
	 */
	public boolean checkAccountingSign(Short sign){
		if(sign==null) return false;
		return FiscalAccountingSubject.ACCOUNT==sign?true:false;
	}
	
	/**
	 * 复制属性
	 * @param src
	 * @param dest
	 */
	private void fillProperties(FiscalAccountingSubject src, FiscalAccountingSubject dest) {
		dest.setBankSubject(src.getBankSubject());
		dest.setCashSign(src.getCashSign());
		dest.setCashSubject(src.getCashSubject());
		dest.setCreateTime(src.getCreateTime());
		dest.setCreator(src.getCreator());
		dest.setCurrency(src.getCurrency());
		dest.setCurrencySign(src.getCurrencySign());
		dest.setCussentAccountSign(src.getCussentAccountSign());
		dest.setCustomerSign(src.getCustomerSign());
		dest.setDepartmentSign(src.getDepartmentSign());
		dest.setDept(src.getDept());
		dest.setOrg(src.getOrg());
		dest.setDirection(src.getDirection());
		dest.setFiscalAccount(src.getFiscalAccount());
		dest.setGoodsSign(src.getGoodsSign());
		dest.setMemberSign(src.getMemberSign());
		dest.setOrg(src.getOrg());
		dest.setProjectSign(src.getProjectSign());
		dest.setQuantitySign(src.getQuantitySign());
		dest.setRelationId(src.getRelationId());
		dest.setRelationType(src.getRelationType());
		dest.setSubject(src.getSubject());
		dest.setSupplierSign(src.getSupplierSign());
		dest.setType(src.getType());
		dest.setUnit(src.getUnit());
		dest.setUpdateTime(src.getUpdateTime());
		dest.setWarehouseSign(src.getWarehouseSign());
	}
	
	/**
	 * 不存在根节点时创建，否则返回
	 * @return
	 */
	public FiscalAccountingSubject createRootIfNotExist() {
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		User creator = SecurityUtil.getCurrentUser();
		return createRootIfNotExist(org, account, creator);
	}
	
	/**
	 * 不存在根节点时创建，否则返回
	 * @param org
	 * @param account
	 * @param creator
	 * @return
	 */
	public FiscalAccountingSubject createRootIfNotExist(Organization org, FiscalAccount account, User creator) {
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalAccountingSubject root = subjectRepo.findTopRoot(accId);
		if(root==null){
			
			FiscalAccountingSubject entity = new FiscalAccountingSubject();
			entity.setCode(Constants.ROOT);
			entity.setName("科目");
			entity.setFiscalAccount(account);
			entity.setDirection(FiscalAccountingSubject.DIRECTION_BORROW);
			entity.setType(FiscalAccountingSubject.TYPE_CB);
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setCreator(creator);
			entity.setOrg(org);
			entity.setEnable(FiscalAccountingSubject.ENABLE_YES);
			entity.setFlag(FiscalAccountingSubject.FLAG_PARENT);
			entity.setLevel(0);
			entity.setIsCheck(FiscalAccountingSubject.UN_ACCOUNT);
			subjectRepo.save(entity);
			return entity;
		}else{
			return root;
		}
	}
	
	/**
	 * 判断一个科目是否核算项（包括核算供应商、核算销售商、核算部门、核算职员、核算仓库、核算项目、核算货品）
	 * @return
	 */
	public boolean isAccountData(FiscalAccountingSubject subject){
		if(subject.getFlag()==FiscalAccountingSubject.FLAG_PARENT){
			return false;
		}
		//供应商
		if(checkAccountingSign(subject.getSupplierSign())){
			return true;
		}
		//客户
		if(checkAccountingSign(subject.getCustomerSign())){
			return true;
		}
		//部门
		if(checkAccountingSign(subject.getDepartmentSign())){
			return true;
		}
		//职员
		if(checkAccountingSign(subject.getMemberSign())){
			return true;
		}
		//仓库
		if(checkAccountingSign(subject.getWarehouseSign())){
			return true;
		}
		//项目
		if(checkAccountingSign(subject.getProjectSign())){
			return true;
		}
		//货品
		if(checkAccountingSign(subject.getGoodsSign())){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取银行和现金科目（只取叶子节点）
	 * @param accountId
	 * @return
	 */
	public List<FiscalAccountingSubject> getBankCashSubjects(String accountId) {
		return subjectRepo.getBankCashSubjects(accountId);
	}

	/**
	 * 获取银行科目（只取叶子节点）
	 * @param accountId
	 * @return
	 */
	public FiscalAccountingSubject getBankSubject(String accountId, String bankId) {
		return subjectRepo.getTopBankSubject(accountId, bankId);
	}
	/**
	 * 判断某个货品被引用的次数
	 * @param goodsId 货品ID
	 * @return
	 */
	public long countByGoods(String goodsId){
		return subjectRepo.countByGoods(goodsId);
	}


	/**
	 * 获取缓存的key
	 * @return
	 */
	public String getCacheKey(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(FiscalAccountingSubjectVo.class.getSimpleName());
		buffer.append(":");
		buffer.append(SecurityUtil.getCurrentOrgId());
		buffer.append(":");
		buffer.append(SecurityUtil.getFiscalAccountId());
		String key = buffer.toString();

		return key;
	}
	/**
	 * 根据供应商ID查询有多少
	 */
	public long countByRelationId(String relationId){
		return subjectRepo.countByRelationId(relationId);
	}
}
