package cn.fooltech.fool_ops.domain.payment.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherFilter;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface PaymentBillRepository extends FoolJpaRepository<PaymentBill, String>, 
	JpaSpecificationExecutor<PaymentBill>, CheckFilter, VoucherFilter {

	/**
	 * 统计某个单据类型下的单据数量
	 *
	 * @param orgId    机构ID
	 * @param billType 单据类型
	 * @return
	 */
	@Query("select count(*) from PaymentBill b where b.org.fid=?1 and b.billType=?2")
	public Long countByBillType(String orgId, int billType);

	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param billType
	 * @param vouchercode
	 * @param billDate
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from PaymentBill b where b.fiscalAccount.fid=?1 and b.billType=?2 and b.vouchercode=?3 and b.billDate=?4 and b.fid!=?5")
	public Long countTodayVoucherCode(String accId, Integer billType, String vouchercode, Date billDate, String excludeId);


	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param billType
	 * @param vouchercode
	 * @param billDate
	 * @return
	 */
	@Query("select count(*) from PaymentBill b where b.fiscalAccount.fid=?1 and b.billType=?2 and b.vouchercode=?3 and b.billDate=?4")
	public Long countTodayVoucherCode(String accId, Integer billType, String vouchercode, Date billDate);


	/**
	 * 根据参数查找分页
	 * @param paymentBillVo
	 * @param page
	 * @return
	 */
	public default Page<PaymentBill> findPageBy(PaymentBillVo paymentBillVo, String accId, Pageable page){
		return findAll(new Specification<PaymentBill>() {
			
			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, 
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据类型
				if(paymentBillVo.getBillType() != null){
					predicates.add(builder.equal(root.get("billType"), paymentBillVo.getBillType()));
				}
				
				//单据状态搜索
				if(paymentBillVo.getRecordStatus() != null){
					predicates.add(builder.equal(root.get("recordStatus"), paymentBillVo.getRecordStatus()));
				}
				//单据日期搜索
				if(paymentBillVo.getStartDay() != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), paymentBillVo.getStartDay()));
				}
				if(paymentBillVo.getEndDay() != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), paymentBillVo.getEndDay()));
				}
				if(paymentBillVo.getTaskFilter() != null){
					if(paymentBillVo.getTaskFilter()==1){
						predicates.add(builder.isNull(root.get("task").get("fid")));
					}
					
				}
				//单号
				if(StringUtils.isNotBlank(paymentBillVo.getCode())){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(paymentBillVo.getCode())));
				}
				
				//凭证号
				if(StringUtils.isNotBlank(paymentBillVo.getVouchercode())){
					predicates.add(builder.like(root.get("vouchercode"), PredicateUtils.getAnyLike(paymentBillVo.getVouchercode())));
				}
				

//				predicates.add(builder.greaterThanOrEqualTo((root.<Long>get("totalAmount")+root.<Long>get("totalAmount")),
//						root.<Long>get("totalAmount")));
				

				String searchKey = paymentBillVo.getSearchKey();
				//关键字搜索
				if(StringUtils.isNotBlank(searchKey)){
					predicates.add(builder.or(
							builder.like(root.get("vouchercode"), PredicateUtils.getAnyLike(searchKey)),
							builder.like(root.get("code"), PredicateUtils.getAnyLike(searchKey))
							));
				}
				// 单号
				String codeOrVoucherCode = paymentBillVo.getCodeOrVoucherCode();
				if (StringUtils.isNotBlank(codeOrVoucherCode)) {
					predicates.add(builder.or(
							builder.like(root.get("vouchercode"), PredicateUtils.getAnyLike(codeOrVoucherCode)),
							builder.like(root.get("code"), PredicateUtils.getAnyLike(codeOrVoucherCode))
							));
				}
				if (StringUtils.isNotBlank(paymentBillVo.getBankId())) {
					predicates.add(builder.equal(root.get("bank").get("fid"), paymentBillVo.getBankId()));
				}
				//销售商
				if(StringUtils.isNotBlank(paymentBillVo.getCustomerId())){
					predicates.add(builder.equal(root.get("customer").get("fid"), paymentBillVo.getCustomerId()));
				}
				//供应商
				if(StringUtils.isNotBlank(paymentBillVo.getSupplierId())){
					predicates.add(builder.equal(root.get("supplier").get("fid"), paymentBillVo.getSupplierId()));
				}
				//负责人
				if(StringUtils.isNotBlank(paymentBillVo.getMemberId())){
					predicates.add(builder.equal(root.get("member").get("fid"), paymentBillVo.getMemberId()));
				}
				//受款人
				if(StringUtils.isNotBlank(paymentBillVo.getPayeeName())){
					predicates.add(builder.like(root.get("payeeName"), PredicateUtils.getAnyLike(paymentBillVo.getPayeeName())));
				}
				//受款人ID
				if (StringUtils.isNotBlank(paymentBillVo.getPayeeId())) {
					predicates.add(builder.equal(root.get("payee").get("fid"), paymentBillVo.getPayeeId()));
				}

				if (StringUtils.isNotBlank(paymentBillVo.getAmount())) {
					BigDecimal amount = new BigDecimal(paymentBillVo.getAmount());
					predicates.add(builder.equal(root.get("amount"), amount));
				}

				// 是否已生成银行日记单
                if ("1".equals(paymentBillVo.getSaveAsBankBillOperatorId())) {
                    predicates.add(builder.isNotNull(root.get("saveAsBankBillOperator")));
                } else if("0".equals(paymentBillVo.getSaveAsBankBillOperatorId())) {
				    predicates.add(builder.isNull(root.get("saveAsBankBillOperator")));
                }

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	
	public default List<Predicate> getDatePredicates(String startDate, String endDate, Root<PaymentBill> root, 
			CriteriaBuilder builder){
		
		Date start = DateUtilTools.string2Date(startDate);
		Date end = DateUtilTools.string2Date(endDate);
		List<Predicate> predicates = Lists.newArrayList();
		predicates.add(builder.and(
				builder.equal(root.get("startDate"), start),
				builder.greaterThan(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.equal(root.get("startDate"), start),
				builder.lessThan(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.greaterThan(root.get("startDate"), start),
				builder.greaterThan(root.get("endDate"), end),
				builder.greaterThan(root.get("endDate"), start),
				builder.lessThanOrEqualTo(root.get("startDate"), end)
			));
		predicates.add(builder.and(
				builder.greaterThan(root.get("startDate"), start),
				builder.lessThan(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.lessThan(root.get("startDate"), start),
				builder.greaterThan(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.lessThan(root.get("startDate"), start),
				builder.lessThan(root.get("endDate"), end),
				builder.greaterThanOrEqualTo(root.get("endDate"), start)
			));
		predicates.add(builder.and(
				builder.lessThan(root.get("startDate"), start),
				builder.equal(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.equal(root.get("startDate"), start),
				builder.equal(root.get("endDate"), end)
			));
		predicates.add(builder.and(
				builder.greaterThan(root.get("startDate"), start),
				builder.equal(root.get("endDate"), end)
			));
		return predicates;
	}
	
	/**
	 * 判断在此时间段内是否还存在同一个供应商相同对公标识的采购返利单
	 * @param supplierId 供应商
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	public default boolean checkPurchaseRebateExist(String accId, String excludeId, String supplierId, String startDate,
			String endDate, Integer toPublic) {
		
		Long count = count(new Specification<PaymentBill>() {

			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				List<Predicate> datePredicates = getDatePredicates(startDate, endDate, root, builder);
				predicates.add(builder.or(datePredicates.toArray(new Predicate[] {})));
				predicates.add(builder.equal(root.get("toPublic"), toPublic));
				predicates.add(builder.notEqual(root.get("fid"), excludeId));
				predicates.add(builder.notEqual(root.get("recordStatus"), PaymentBill.STATUS_CANCELED));
				predicates.add(builder.equal(root.get("supplier").get("fid"), supplierId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("billType"), PaymentBill.TYPE_PURCHASE_REBATE));
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		
		return count>0;
	}
	
	/**
	 * 判断在此时间段内是否还存在同一个客户相同对公标识的销售返利单
	 * @param customerId 客户
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param toPublic 对公标识
	 * @return
	 */
	public default boolean checkSalesRebateExist(String accId, String excludeId, String customerId, String startDate,
			String endDate, Integer toPublic) {
		Long count = count(new Specification<PaymentBill>() {

			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				List<Predicate> datePredicates = getDatePredicates(startDate, endDate, root, builder);
				predicates.add(builder.or(datePredicates.toArray(new Predicate[] {})));
				predicates.add(builder.equal(root.get("toPublic"), toPublic));
				predicates.add(builder.notEqual(root.get("fid"), excludeId));
				predicates.add(builder.notEqual(root.get("recordStatus"), PaymentBill.STATUS_CANCELED));
				predicates.add(builder.equal(root.get("supplier").get("fid"), customerId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("billType"), PaymentBill.TYPE_SALES_REBATE));
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		
		return count>0;
	}


	/**
	 * 查找未勾对的单据
	 * @param paybill
	 * @param accId
	 * @param checkStartDay
	 * @param checkEndDay
	 * @param checkBillCode
	 * @param checkBillType
	 * @param excludeIds
	 * @param pageRequest
	 * @return
	 */
	public default Page<PaymentBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay, String checkEndDay, 
			String checkBillCode, int checkBillType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<PaymentBill>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.notEqual(root.get("fid"), paybill.getFid()));
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), PaymentBill.STATUS_AUDITED));
				
				//单据类型
				predicates.add(builder.equal(root.get("billType"), checkBillType));
				
				//单号搜索
				if(StringUtils.isNotBlank(checkBillCode)){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(checkBillCode)));
				}
				//单据日期搜索
				if(StringUtils.isNotBlank(checkStartDay)){
					Date startDate = DateUtilTools.string2Date(checkStartDay);
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
				}
				if(StringUtils.isNotBlank(checkEndDay)){
					Date endDate = DateUtilTools.string2Date(checkEndDay);
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
				}
				
				//金额为勾对完
				predicates.add(
					builder.greaterThan(
							builder.abs(round(builder, builder.coalesce(root.get("amount"), 0))),
							builder.abs(round(builder, builder.coalesce(root.get("totalCheckAmount"), 0)))
					)
				);
				
				addCheckFilter(paybill, checkBillType, excludeIds, predicates, builder, root);
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}


	/**
	 * 根据参数查找分页
	 * @param vo
	 * @param pageRequest
	 * @return
	 */
	public default Page<PaymentBill> findPageBy(String accId, CostBill costbill, Date startDay, Date endDay, String code,
			Integer billType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<PaymentBill>() {
			
			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, 
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("recordStatus"), CostBill.STATUS_AUDITED));
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据日期搜索
				if(startDay != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDay));
				}
				if(endDay != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDay));
				}
				//单号
				if(StringUtils.isNotBlank(code)){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(code)));
				}
				//单据类型
				if(billType != null){
					predicates.add(builder.equal(root.get("billType"), billType));
				}
				
				//排除IDs
				if(CollectionUtils.isNotEmpty(excludeIds)){
					predicates.add(builder.not(root.get("fid").in(excludeIds)));
				}
				
				if(costbill.getCsv()!=null){
					if(costbill.getCsv().getType()==CustomerSupplierView.TYPE_SUPPLIER){
						predicates.add(builder.equal(root.get("supplier").get("fid"), costbill.getCsv().getFid()));
					}else{
						predicates.add(builder.equal(root.get("customer").get("fid"), costbill.getCsv().getFid()));
					}
				}
				
				//状态
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	/**
	 * 分页查询，用于制作凭证
	 * @param vo
	 * @param page
	 * @return
	 */
	public default Page<PaymentBill> queryCommonBills(String accId, VoucherMakeVo vo, Pageable page){
		return findAll(new Specification<PaymentBill>() {
			
			@Override
			public Predicate toPredicate(Root<PaymentBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), CostBill.STATUS_AUDITED));
				
				//单据类型
				if(vo.getBillType() != null && vo.getBillType() != WarehouseBuilderCodeHelper.base){
					predicates.add(builder.equal(root.get("billType"), vo.getBillType()));
				}
				//单号搜索
				if(StringUtils.isNotBlank(vo.getCode())){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(vo.getCode())));
				}
				//单据日期搜索
				if(vo.getStartDay() != null){
					Date startDate = vo.getStartDay();
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
				}
				if(vo.getEndDay() != null){
					Date endDate = vo.getEndDay();
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
				}
				//销售商搜索
				if(StringUtils.isNotBlank(vo.getCustomerId())){
					predicates.add(builder.equal(root.get("customer").get("fid"), vo.getCustomerId()));
				}
				//供应商搜索
				if(StringUtils.isNotBlank(vo.getSupplierId())){
					predicates.add(builder.equal(root.get("supplier").get("fid"), vo.getSupplierId()));
				}
				//银行账号
				if(StringUtils.isNotBlank(vo.getBankId())){
					predicates.add(builder.equal(root.get("bank").get("fid"), vo.getBankId()));
				}
				
				predicates.add(addVoucherFilter(root, CostBill.class, query, builder, vo));
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param supplierId 供应商ID
	 * @return
	 */
	@Query("select count(a) from PaymentBill a where supplier.fid=?1")
	public long countBySupplier(String supplierId);
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param supplierId 供应商ID
	 * @return
	 */
	@Query("select count(a) from PaymentBill a where customer.fid=?1")
	public long countByCustomer(String customerId);
	/**
	 * 根据单据IDs查找
	 * @param ids
	 * @return
	 */
	@Query("select c from PaymentBill c where c.fid in ?1")
	public List<PaymentBill> findByIds(List<String> ids);
}
