package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherDetailRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherDetailVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>凭证明细网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Service
public class VoucherDetailService extends BaseService<VoucherDetail, VoucherDetailVo, String>{
	
	@Autowired
	private VoucherDetailRepository detailRepo;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	/**
	 * 财务账套
	 */
	@Autowired
	private FiscalAccountService fiscalAccountService;
	
	/**
	 * 科目
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 货品服务类
	 */
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 单位服务类
	 */
	@Autowired
	private UnitService unitService;
	
	/**
	 * 客户服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 供应商服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;
	
	/**
	 * 获取凭证明细
	 * @param voucherId 凭证ID
	 * @return
	 */
	public List<VoucherDetailVo> getByVoucher(String voucherId){
		List<VoucherDetail> details = detailRepo.findDetailsByVoucherId(voucherId);
		return getVos(details);
	}
	
	/**
	 * 根据凭证ID查询
	 * @param voucherId
	 * @return
	 */
	public List<VoucherDetail> findDetailsByVoucherId(String voucherId){
		return detailRepo.findDetailsByVoucherId(voucherId);
	}
	
	/**
	 * 新增、保存
	 * @param vo
	 * @return
	 */
	public RequestResult save(VoucherDetailVo vo){
		String fid = vo.getFid();
		BigDecimal creditAmount = vo.getCreditAmount();//贷方金额
		BigDecimal debitAmount = vo.getDebitAmount();//借方金额
		String currencyAmount = vo.getCurrencyAmount();//外币金额
		String exchangeRate = vo.getExchangeRate();//汇率
		String voucherId = vo.getVoucherId();//凭证
		String fiscalAccountId = vo.getFiscalAccountId();//财务账套
		String currencyId = vo.getCurrencyId();//外币币别
		String resume = vo.getResume();//摘要
		String subjectId = vo.getSubjectId();//科目
		String projectId = vo.getProjectId();//项目
		String departmentId = vo.getDepartmentId();//部门
		String unitId = vo.getUnitId();//单位
		String quantity = vo.getQuantity();//数量
		String goodsId = vo.getGoodsId();//货品
		String memberId = vo.getMemberId();//职员
		String customerId = vo.getCustomerId();//销售商
		String supplierId = vo.getSupplierId();//供应商
		String warehouseId = vo.getWarehouseId();//仓库
		Date now = Calendar.getInstance().getTime();
		
		Assert.isTrue(StringUtils.isNotBlank(voucherId), "凭证不能为空!");
		Assert.isTrue(StringUtils.isNotBlank(fiscalAccountId), "财务账套不能为空!");
		
		VoucherDetail entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new VoucherDetail();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
		}
		else{
			entity = detailRepo.findOne(fid);
		}
		entity.setUpdateTime(now);
		entity.setResume(resume);
		entity.setCreditAmount(creditAmount);
		entity.setDebitAmount(debitAmount);
		entity.setCurrencyAmount(this.toBigDeciaml(currencyAmount));
		entity.setExchangeRate(this.toBigDeciaml(exchangeRate));
		entity.setQuantity(this.toBigDeciaml(quantity));
		//凭证
		if(StringUtils.isNotBlank(voucherId)){
			entity.setVoucher(voucherService.get(voucherId));
		}
		//财务账套
		if(StringUtils.isNotBlank(fiscalAccountId)){
			entity.setFiscalAccount(fiscalAccountService.get(fiscalAccountId));
		}
		//外币币别
		if(StringUtils.isNotBlank(currencyId)){
			entity.setCurrency(attrService.get(currencyId));
		}
		//科目
		if(StringUtils.isNotBlank(subjectId)){
			entity.setAccountingSubject(subjectService.get(subjectId));
		}
		//项目
		if(StringUtils.isNotBlank(projectId)){
			entity.setProject(attrService.get(projectId));
		}
		//部门
		if(StringUtils.isNotBlank(departmentId)){
			entity.setDepartment(orgService.get(departmentId));
		}
		//单位
		if(StringUtils.isNotBlank(unitId)){
			entity.setUnit(unitService.get(unitId));
		}
		//货品
		if(StringUtils.isNotBlank(goodsId)){
			entity.setGoods(goodsService.get(goodsId));
		}
		//职员
		if(StringUtils.isNotBlank(memberId)){
			entity.setMember(memberService.get(memberId));
		}
		//销售商
		if(StringUtils.isNotBlank(customerId)){
			entity.setCustomer(customerService.get(customerId));
		}
		//供应商
		if(StringUtils.isNotBlank(supplierId)){
			entity.setSupplier(supplierService.get(supplierId));
		}
		//仓库
		if(StringUtils.isNotBlank(warehouseId)){
			entity.setWarehouse(attrService.get(warehouseId));
		}
		
		detailRepo.save(entity);
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @return
	 */
	public RequestResult delete(String id){
		VoucherDetail entity = detailRepo.findOne(id);
		Voucher voucher = entity.getVoucher();
		if(voucher.getRecordStatus() != Voucher.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "不允许删除该明细!");
		}
		detailRepo.delete(entity);
		return new RequestResult();
	}
	
	/**
	 * 根据凭证删除凭证明细
	 * @param voucherId 凭证ID
	 */
	public void deleteByVoucher(String voucherId){
		List<VoucherDetail> details = detailRepo.findDetailsByVoucherId(voucherId);
		for(VoucherDetail detail : details){
			detailRepo.delete(detail);
		}
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public VoucherDetailVo getVo(VoucherDetail entity){
		BigDecimal quantity = entity.getQuantity();
		BigDecimal debitAmount = entity.getDebitAmount();
		BigDecimal creditAmount = entity.getCreditAmount();
		BigDecimal exchangeRate = entity.getExchangeRate();
		BigDecimal currencyAmount = entity.getCurrencyAmount();
		VoucherDetailVo vo = new VoucherDetailVo();
		vo.setFid(entity.getFid());
		vo.setResume(entity.getResume());
		vo.setQuantity(quantity==null?"":NumberUtil.bigDecimalToStr(quantity));
		vo.setDebitAmount(debitAmount==null?BigDecimal.ZERO:debitAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
		vo.setCreditAmount(creditAmount==null?BigDecimal.ZERO:creditAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
		vo.setExchangeRate(exchangeRate==null?"":NumberUtil.bigDecimalToStr(exchangeRate));
		vo.setCurrencyAmount(currencyAmount==null?"":NumberUtil.bigDecimalToStr(currencyAmount));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		
		//机构
		Organization org = entity.getOrg();
		if(org != null){
			vo.setOrgId(org.getFid());
		}
		//凭证
		Voucher voucher = entity.getVoucher();
		if(voucher != null){
			vo.setVoucherId(voucher.getFid());
		}
		//财务账套
		FiscalAccount fiscalAccount = entity.getFiscalAccount();
		if(fiscalAccount != null){
			vo.setFiscalAccountId(fiscalAccount.getFid());
			vo.setFiscalAccountName(fiscalAccount.getName());
		}
		//科目
		FiscalAccountingSubject subject = entity.getAccountingSubject();
		if(subject != null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectCode(subject.getCode());
			vo.setSubjectName(subject.getName());
			vo.setCashSign(subject.getCashSign());
			vo.setSupplierSign(subject.getSupplierSign());
			vo.setCustomerSign(subject.getCustomerSign());
			vo.setDepartmentSign(subject.getDepartmentSign());
			vo.setMemberSign(subject.getMemberSign());
			vo.setWarehouseSign(subject.getWarehouseSign());
			vo.setProjectSign(subject.getProjectSign());
			vo.setGoodsSign(subject.getGoodsSign());
			vo.setQuantitySign(subject.getQuantitySign());
			vo.setCurrencySign(subject.getCurrencySign());
			vo.setCussentAccountSign(subject.getCussentAccountSign());
			//父科目
			FiscalAccountingSubject parentSubject = subject.getParent();
			if(parentSubject != null){
				vo.setParentSubjectId(parentSubject.getFid());
				vo.setParentSubjectCode(parentSubject.getCode());
				vo.setParentSubjectName(parentSubject.getName());
			}
		}
		//单位
		Unit unit = entity.getUnit();
		if(unit != null){
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
		}
		//币别
		AuxiliaryAttr currency = entity.getCurrency();
		if(currency != null){
			vo.setCurrencyId(currency.getFid());
			vo.setCurrencyName(currency.getName());
		}
		//供应商
		Supplier supplier = entity.getSupplier();
		if(supplier != null){
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
		}
		//销售商
		Customer customer = entity.getCustomer();
		if(customer != null){
			vo.setCustomerId(customer.getFid());
			vo.setCustomerName(customer.getName());
		}
		//部门
		Organization department = entity.getDepartment();
		if(department != null){
			vo.setDepartmentId(department.getFid());
			vo.setDepartmentName(department.getOrgName());
		}
		//职员
		Member member = entity.getMember();
		if(member != null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}
		//仓库
		AuxiliaryAttr warehouse = entity.getWarehouse();
		if(warehouse != null){
			vo.setWarehouseId(warehouse.getFid());
			vo.setWarehouseName(warehouse.getName());
		}
		//项目
		AuxiliaryAttr project = entity.getProject();
		if(project != null){
			vo.setProjectId(project.getFid());
			vo.setProjectName(project.getName());
		}
		//货品
		Goods goods = entity.getGoods();
		if(goods != null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsName(goods.getName());
		}
		return vo;
	}
	
	/**
	 * 字符串转BigDecimal
	 * @param num
	 * @return 
	 */
	private BigDecimal toBigDeciaml(String num){
		if(StringUtils.isNotBlank(num)){
			return new BigDecimal(num);
		}
		return null;
	}

	@Override
	public CrudRepository<VoucherDetail, String> getRepository() {
		return detailRepo;
	}
	
	/**
	 * 获取凭证里的第一条凭证明细
	 * @param voucherId 凭证ID
	 * @return
	 */
	public VoucherDetail getFirstDetailOfVoucher(String voucherId){
		return detailRepo.getFirstDetailOfVoucher(voucherId);
	}
	
	/**
	 * 校验凭证下的贷方金额和借方金额是否相等
	 * @param voucherId 凭证ID
	 * @return true 相等   false 不相等
	 */
	public  boolean checkAmount(String voucherId){
		List<VoucherDetail> details = detailRepo.findDetailsByVoucherId(voucherId);
																																																				
		BigDecimal totalDebitAmount = BigDecimal.ZERO; //借方总金额
		BigDecimal totalCreditAmount = BigDecimal.ZERO; //贷方总金额
		for(VoucherDetail detail : details){
			if(detail.getDebitAmount() != null){
				totalDebitAmount = totalDebitAmount.add(detail.getDebitAmount());
			}
			if(detail.getCreditAmount() != null){
				totalCreditAmount = totalCreditAmount.add(detail.getCreditAmount());
			}
		}
		return totalDebitAmount.compareTo(totalCreditAmount) == 0;
	}
	
	/**
	 * 根据科目ID统计凭证明细个数
	 */
	public Long countBySubjectId(String subjectId, Date start, Date end){
		return detailRepo.countBySubjectId(subjectId, start, end);
	}
	
	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	public Long countBySubjectId(String subjectId){
		return detailRepo.countBySubjectId(subjectId);
	}
	
	/**
	 * 根据科目ID查询凭证结余金额
	 * @param code
	 * @param accId
	 */
	public BigDecimal getAmountBySubjectId(String subjectId, String accId, 
			List<Integer> statusList){
		return detailRepo.getAmountBySubjectId(subjectId, accId, statusList);
	}

	/**
	 * 根据凭证ID合计金额
	 * @param voucherId
	 * @return
	 */
	public BigDecimal sumAmountByVoucherId(String voucherId){
		BigDecimal sum = detailRepo.sumAmountByVoucherId(voucherId);
		if(sum==null){
			return BigDecimal.ZERO;
		}else{
			return sum;
		}
	}
}
