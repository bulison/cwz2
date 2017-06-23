package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.repository.SupplierRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.utils.input.FivepenUtils;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * 供应商网页服务类
 *
 * @author lzf
 * @version 1.0
 * @date 2015年4月15日
 */
@Service
public class SupplierService extends BaseService<Supplier, SupplierVo, String>  implements BaseDataCache {

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 供应商服务类
     */
    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * 辅助属性服务类
     */
    @Autowired
    private AuxiliaryAttrService attrService;

    /**
     * 人员服务类
     */
    @Autowired
    private MemberService memberService;

    @Autowired
    private UserAttrService userAttrService;
    /**
     * 收付款单网页服务类
     */
    @Autowired
    private PaymentBillService paymentBillService;
    /**
     * 仓储单据服务类
     */
    @Resource(name = "ops.WarehouseBillService")
    private WarehouseBillService billService;
    /**
     * 费用单网页服务类
     */
    @Autowired
    private CostBillService costBillService;
    /**
     * 财务-科目网页服务类 
     */
    @Autowired
    private FiscalAccountingSubjectService subjectService;

    /**
     * 数据过滤处理器
     */
//	@Autowired
//	private DataFilterQBCHandler filterQBCHandler;


	@Autowired
	private RedisService redisService;

    /**
     * 分页查询
     *
     * @param vo
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageJson queryJson(SupplierVo vo, PageParamater pageParamater) {
        Page<SupplierVo> page = query(vo, pageParamater);
        PageJson pageJson = new PageJson(page);
        return pageJson;
    }

    /**
     * 分页查询
     *
     * @param vo
     * @param pageParamater
     * @return
     * @throws Exception
     */
    public Page<SupplierVo> query(SupplierVo vo, PageParamater pageParamater) {
//        Sort sort = new Sort(Direction.ASC, "createTime");
        Sort sort = new Sort(Direction.ASC, "code");
        PageRequest request = getPageRequest(pageParamater, sort);
        Page<Supplier> page = supplierRepository.query(vo, SecurityUtil.getCurrentOrgId(), request);
        Page<SupplierVo> pageVos = getPageVos(page, request);
        return pageVos;
    }


    /**
     * 获取供应商信息
     *
     * @param id 供应商ID
     * @return
     */
    public SupplierVo getById(String id) {
        Supplier entity = supplierRepository.findOne(id);
        return getVo(entity);
    }

    /**
     * 单个实体转换为vo
     *
     * @param entity
     * @return
     */
    public SupplierVo getVo(Supplier entity) {
        if (entity == null)
            return null;
        SupplierVo vo = VoFactory.createValue(SupplierVo.class, entity);
        vo.setShortName(entity.getShortName());
        vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(),
                TIME_FORMAT));
        if(entity.getRegisterDate()!=null){
        	 vo.setRegisterDate(DateUtils.getStringByFormat(entity.getRegisterDate(),"yyyy-MM-dd"));
        }
        // 系统计算信用额度
        if (entity.getCreditLineSys() != null) {
            vo.setCreditLineSys(entity.getCreditLineSys().toString());
        }
        // 用户配置信用额度
        if (entity.getCreditLineUser() != null) {
            vo.setCreditLineUser(entity.getCreditLineUser().toString());
        }
        // 组织机构
        Organization org = entity.getOrg();
        if (org != null) {
            vo.setOrgId(org.getFid());
        }
        // 地区
        AuxiliaryAttr area = entity.getArea();
        if (area != null) {
            vo.setAreaId(area.getFid());
            vo.setAreaName(area.getName());
            vo.setAreaCode(area.getCode());
        }
        // 类别
        AuxiliaryAttr category = entity.getCategory();
        if (category != null) {
            vo.setCategoryId(category.getFid());
            vo.setCategoryName(category.getName());
            vo.setCategoryCode(category.getCode());
        }
        // 业务员
        Member member = entity.getMember();
        if (member != null) {
            vo.setMemberId(member.getFid());
            vo.setMemberName(member.getUsername());
            vo.setDeptId(member.getDept().getFid());
            vo.setDeptName(member.getDept().getOrgName());
            vo.setMemberCode(member.getUserCode());
        }
        // 征信级别
        AuxiliaryAttr creditRating = entity.getCreditRating();
        if (creditRating != null) {
            vo.setCreditRatingId(creditRating.getFid());
            vo.setCreditRatingName(creditRating.getName());
            vo.setCreditRatingCode(creditRating.getCode());
        }
        // 创建人
        User creator = entity.getCreator();
        if (creator != null) {
            vo.setCreatorId(creator.getFid());
            vo.setCreatorName(creator.getUserName());
        }
        return vo;
    }

    /**
     * 单个实体转换为Fastvo
     *
     * @param entity
     * @return
     */
    public SupplierVo getFastVo(Supplier entity) {
        if (entity == null)
            return null;
        SupplierVo vo = new SupplierVo();
        vo.setFid(entity.getFid());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setPhone(entity.getPhone());
        vo.setSearchSize(null);
        vo.setShowDisable(null);
        vo.setSearchKey(null);
        return vo;
    }

    /**
     * 新增/编辑供应商信息
     *
     * @param vo
     */
    public RequestResult save(SupplierVo vo, Object... args) {
        //演示hibernate-validator的使用；也可以加在方法的参数中methd(@Valid xxx)
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }
        String fid = vo.getFid();
        String code = vo.getCode();
        String name = vo.getName();
        String describe = vo.getDescribe();
        String shortName = vo.getShortName();
        String creditLineUser = vo.getCreditLineUser();
        String creditLineSys = vo.getCreditLineSys();
        String businessContact = vo.getBusinessContact();
        String fax = vo.getFax();
        String phone = vo.getPhone();
        String email = vo.getEmail();
        String address = vo.getAddress();
        String tel = vo.getTel();
        String principal = vo.getPrincipal();
        String principalPhone = vo.getPrincipalPhone();
        String postCode = vo.getPostCode();
        String registedCapital = vo.getRegistedCapital();
        Integer staffNum = vo.getStaffNum();
        String bussinessRange = vo.getBussinessRange();
        String bank = vo.getBank();
        String account = vo.getAccount();
        String nationTax = vo.getNationTax();
        String landTax = vo.getLandTax();
        Date registerDate = null;
        if(!Strings.isNullOrEmpty(vo.getRegisterDate())){
        	try {
				registerDate = DateUtils.getDateFromString(vo.getRegisterDate());
			} catch (Exception e) {
				e.printStackTrace();
				 return new RequestResult(RequestResult.RETURN_FAILURE, "成立日期式有误");
			}
        }
        String recordStatus = vo.getRecordStatus();
        String areaId = vo.getAreaId(); // 区域
        String categoryId = vo.getCategoryId(); // 类别
        String memberId = vo.getMemberId(); // 业务员
        String creditRatingId = vo.getCreditRatingId(); // 征信级别
        Date now = new Date();
        String orgId = SecurityUtil.getCurrentOrgId();

        // 校验数据
        RequestResult result = checkByRule(vo);
        if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
            return result;
        }

        if (creditLineUser != null && NumberUtil.isNum(creditLineUser) == false) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "用户配置信用额度格式错误");
        }
        if (creditLineSys != null && NumberUtil.isNum(creditLineSys) == false) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "系统计算信用额度格式错误");
        }
        if (phone != null && NumberUtil.isNum(phone) == false) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "业务联系人手机格式错误");
        }

        Supplier entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new Supplier();
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setDept(SecurityUtil.getCurrentDept());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setRecordStatus(Supplier.STATUS_SAC);
        } else {
            entity = supplierRepository.findOne(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
            }
            entity.setUpdateTime(now);
            entity.setRecordStatus(recordStatus);
        }
        entity.setPinyin(PinyinUtils.getPinyinCode(name, 300));
        entity.setFivepen(FivepenUtils.getFivePenCode(name));
        entity.setCode(code.trim());
        entity.setName(name);
        entity.setDescribe(describe);

        entity.setCreditLineUser(NumberUtil.toBigDeciaml(creditLineUser));
        entity.setCreditLineSys(NumberUtil.toBigDeciaml(creditLineSys));
        entity.setPhone(phone);
        entity.setBusinessContact(businessContact);
        entity.setFax(fax);
        entity.setEmail(email);
        entity.setAddress(address);
        entity.setTel(tel);
        entity.setPrincipal(principal);
        entity.setPrincipalPhone(principalPhone);
        entity.setPostCode(postCode);
        entity.setRegistedCapital(registedCapital);
        entity.setStaffNum(staffNum);
        entity.setBussinessRange(bussinessRange);
        entity.setBank(bank);
        entity.setAccount(account);
        entity.setNationTax(nationTax);
        entity.setLandTax(landTax);
        entity.setRegisterDate(registerDate);

        // 简称
        if (StringUtils.isBlank(shortName)) {
            shortName = entity.getName();
        }
        entity.setShortName(shortName);
        // 区域
        if (areaId!=null) {
            entity.setArea(attrService.get(areaId));
        } else if (StringUtils.isNotBlank(vo.getAreaCode())) {
            AuxiliaryAttr byCode = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_AREA, vo.getAreaCode());
            entity.setArea(byCode);
            if (args != null && byCode == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "地区(编号)填写有误！");
            }
        }
        // 类别
        if (categoryId!=null) {
            entity.setCategory(attrService.get(categoryId));
        } else if (StringUtils.isNotBlank(vo.getCategoryCode())) {
            AuxiliaryAttr byCode = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_CUSTOMER_TYPE, vo.getCategoryCode());
            entity.setCategory(byCode);
            if (args != null && byCode == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "分类(编号)填写有误！");
            }
        }
        // 业务员
        if (memberId!=null) {
            entity.setMember(memberService.get(memberId));
        } else if (StringUtils.isNotBlank(vo.getMemberCode())) {
            Member byCode = memberService.getByCode(orgId, vo.getMemberCode());
            entity.setMember(byCode);
            if (args != null && byCode == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "业务人员(编号)填写有误！");
            }
        }
        // 征信级别
        if (creditRatingId!=null) {
            entity.setCreditRating(attrService.get(creditRatingId));
        } else if (StringUtils.isNotBlank(vo.getCreditRatingCode())) {
            AuxiliaryAttr byCode = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_CREDIT, vo.getCreditRatingCode());
            entity.setCreditRating(byCode);
            if (args != null && byCode == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "征信级别(编号)填写有误！");
            }
        }

        supplierRepository.save(entity);

        redisService.remove(getCacheKey());

        return result;
    }

    /**
     * 删除供应商信息
     *
     * @param id 供应商ID
     */
    public RequestResult delete(String id) {
		if(isUsed(id)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该供应商已被使用!");
		}
		supplierRepository.delete(id);

        redisService.remove(getCacheKey());

        return new RequestResult();
    }

    /**
     * 根据规则校验
     *
     * @param vo
     * @return
     */
    public RequestResult checkByRule(SupplierVo vo) {
        RequestResult result = new RequestResult();
        if (!checkDataRealTime(vo)) {
            result.setMessage("页面数据失效，请重新刷新页面!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        if (supplierRepository.isCodeExist(SecurityUtil.getCurrentOrgId(), vo.getCode(), vo.getFid())) {
            result.setMessage("编号重复!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        if (supplierRepository.isNameExist(SecurityUtil.getCurrentOrgId(), vo.getName(), vo.getFid())) {
            result.setMessage("名称重复!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        if (supplierRepository.isShortNameExist(SecurityUtil.getCurrentOrgId(), vo.getShortName(), vo.getFid())) {
            result.setMessage("简称重复!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        return result;
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效 false 无效
     */
    private boolean checkDataRealTime(SupplierVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            Supplier entity = supplierRepository.findOne(vo.getFid());
            Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    /**
     * 判断编号是否有效
     *
     * @param code 编号
     * @return
     */
    public RequestResult isCodeValid(SupplierVo vo) {
        RequestResult result = new RequestResult();
        if (supplierRepository.isCodeExist(SecurityUtil.getCurrentOrgId(), vo.getCode(),
                vo.getFid())) {
            result.setMessage("编号重复!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        return result;
    }

    /**
     * 模糊查询(根据供应商编号、供应商名称)
     *
     * @param vo
     * @return
     */
    public List<SupplierVo> vagueSearch(SupplierVo vo) {
        String userFid = SecurityUtil.getCurrentUserId();
        String inputType = userAttrService.getInputType(userFid);
        List<Supplier> entities = supplierRepository.vagueSearch(SecurityUtil.getCurrentOrgId(), userFid,
                vo.getSearchKey(), vo.getSearchSize(), inputType);
        return getVos(entities);
    }

    @Override
    public CrudRepository<Supplier, String> getRepository() {
        return this.supplierRepository;
    }

    /**
     * 根据编码获取数据
     */
    public Supplier getByCode(String orgId, String code) {
        return supplierRepository.getByCode(orgId, code);
    }

    @Override
    public String getCacheName() {
        return BaseConstant.SUPPLIER;
    }
	/**
	 * 判断某个供应商是否被引用了
	 * @param supplierId 供应商ID
	 * @return
	 */
	public boolean isUsed(String supplierId){
		if(billService.countBySupplier(supplierId) > 0){
			return true;
		}
		if(paymentBillService.countBySupplier(supplierId) > 0){
			return true;
		}
		if(costBillService.countByCustomerAndSupplier(supplierId) > 0){
			return true;
		}
		if(subjectService.countByRelationId(supplierId)>0){
			return true;
		}
		return false;
	}
}
