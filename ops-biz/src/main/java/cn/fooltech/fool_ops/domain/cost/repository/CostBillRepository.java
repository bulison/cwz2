package cn.fooltech.fool_ops.domain.cost.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.vo.CostBillVo;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.CheckFilter;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherFilter;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CostBillRepository extends FoolJpaRepository<CostBill, String>,
	JpaSpecificationExecutor<CostBill>, VoucherFilter, CheckFilter {


	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param voucherCode
	 * @param billDate
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from CostBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.billDate=?3 and b.fid!=?4")
	public Long countTodayVoucherCode(String accId, String voucherCode, Date billDate, String excludeId);


	/**
	 * 统计今天的原始单号
	 * @param accId
	 * @param voucherCode
	 * @param billDate
	 * @return
	 */
	@Query("select count(*) from CostBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.billDate=?3")
	public Long countTodayVoucherCode(String accId, String voucherCode, Date billDate);


	/**
	 * 统计某个单据类型下的单据数量
	 *
	 * @param orgId    机构ID
	 * @return
	 */
	@Query("select count(*) from CostBill b where b.org.fid=?1")
	public Long countByBillType(String orgId);

	/**
	 * 根据单据IDs查找
	 * @param ids
	 * @return
	 */
	@Query("select c from CostBill c where c.fid in ?1")
	public List<CostBill> findByIds(List<String> ids);

	/**
	 * 根据参数查找分页
	 * @param vo
	 * @param page
	 * @return
	 */
	public default Page<CostBill> findPageBy(String accId, CostBillVo vo, Pageable page){
		return findAll(new Specification<CostBill>() {
			
			@Override
			public Predicate toPredicate(Root<CostBill> root, CriteriaQuery<?> query, 
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				if(vo.getRecordStatus() != null){
					predicates.add(builder.equal(root.get("recordStatus"), vo.getRecordStatus()));
				}
				
				//单据日期搜索
				if(vo.getStartDay() != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), vo.getStartDay()));
				}
				if(vo.getEndDay() != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), vo.getEndDay()));
				}
				if(vo.getTaskFilter() != null){
					if(vo.getTaskFilter()==1){
						predicates.add(builder.isNull(root.get("task").get("fid")));
					}
				}
				//单号
				if(StringUtils.isNotBlank(vo.getCode())){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(vo.getCode())));
				}
				//凭证号
				if(StringUtils.isNotBlank(vo.getVoucherCode())){
					predicates.add(builder.like(root.get("voucherCode"), PredicateUtils.getAnyLike(vo.getVoucherCode())));
				}
				
				String searchKey = vo.getSearchKey();
				//关键字搜索
				if(StringUtils.isNotBlank(searchKey)){
					predicates.add(
						builder.or(
								builder.like(root.get("code"), PredicateUtils.getAnyLike(searchKey)),
								builder.like(root.get("voucherCode"), PredicateUtils.getAnyLike(searchKey))
						)
					);
				}
				//销售商、供应商
				if(StringUtils.isNotBlank(vo.getCsvId())){
					predicates.add(builder.equal(root.get("csv").get("fid"), vo.getCsvId()));
				}
				//经手人
				if(StringUtils.isNotBlank(vo.getMemberId())){
					predicates.add(builder.equal(root.get("member").get("fid"), vo.getMemberId()));
				}
				//费用项目
				if(StringUtils.isNotBlank(vo.getFeeId())){
					predicates.add(builder.equal(root.get("fee").get("fid"), vo.getFeeId()));
				}
				//银行账号
				if(StringUtils.isNotBlank(vo.getBankAccount())){
					predicates.add(builder.like(root.get("bank").get("account"), 
							PredicateUtils.getAnyLike(vo.getBankAccount())));
				}
				if (vo.getFreeAmount()!=null) {
					predicates.add(builder.equal(root.get("freeAmount"), vo.getFreeAmount()));
				}
				//已核对
				if (String.valueOf(CostBill.STATUS_CHECKED).equals(vo.getCheckerId())) {
					predicates.add(builder.equal(root.get("checked"), CostBill.STATUS_AUDITED));
				}
				//未核对
				if (String.valueOf(CostBill.NOT_USED).equals(vo.getCheckerId())) {
					predicates.add(builder.equal(root.get("checked"), CostBill.STATUS_UNCHECK));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

	/**
	 * 根据参数查找分页
	 * @return
	 */
	public default Page<CostBill> findPageBy(String accId, CostBill costbill, Date startDay, Date endDay, String code,
			Integer billType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<CostBill>() {
			
			@Override
			public Predicate toPredicate(Root<CostBill> root, CriteriaQuery<?> query, 
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
//				if(billType != null){
//					predicates.add(builder.equal(root.get("billType"), billType));
//				}
				
				//排除IDs
				if(CollectionUtils.isNotEmpty(excludeIds)){
					predicates.add(builder.not(root.get("fid").in(excludeIds)));
				}
				
				if(costbill.getCsv()!=null){
//					if(costbill.getCsv().getType()==CustomerSupplierView.TYPE_SUPPLIER){
//						predicates.add(builder.equal(root.get("csv").get("fid"), costbill.getCsv().getFid()));
//					}else{
//						predicates.add(builder.equal(root.get("csv").get("fid"), costbill.getCsv().getFid()));
//					}
					predicates.add(builder.equal(root.get("csv").get("fid"), costbill.getCsv().getFid()));
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
	public default Page<CostBill> queryCommonBills(String accId, VoucherMakeVo vo, Pageable page){
		return findAll(new Specification<CostBill>() {
			
			@Override
			public Predicate toPredicate(Root<CostBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), CostBill.STATUS_AUDITED));
				
				//单据类型
//				if(vo.getBillType() != null && vo.getBillType() != WarehouseBuilderCodeHelper.base){
//					predicates.add(builder.equal(root.get("billType"), vo.getBillType()));
//				}
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
				//业务部门搜索
				if(StringUtils.isNotBlank(vo.getDeptId())){
					predicates.add(builder.equal(root.get("dept").get("fid"), vo.getDeptId()));
				}
				//银行账号
				if(StringUtils.isNotBlank(vo.getBankId())){
					predicates.add(builder.equal(root.get("bank").get("fid"), vo.getBankId()));
				}
				//往来单位(客户、供应商)
				if(StringUtils.isNotBlank(vo.getSupplierId())){
					predicates.add(builder.equal(root.get("csv").get("fid"), vo.getSupplierId()));
				}
				if(StringUtils.isNotBlank(vo.getCustomerId())){
					predicates.add(builder.equal(root.get("csv").get("fid"), vo.getCustomerId()));
				}
				
				predicates.add(addVoucherFilter(root, CostBill.class, query, builder, vo));
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param clientId 供应商ID
	 * @return
	 */
	@Query("select count(a) from CostBill a where a.csv.fid=?1")
	public long countByCustomerAndSupplier(String clientId);


	/**
	 * 查找未勾对的单据
	 * @param paybill
	 * @param accId
	 * @param checkStartDay
	 * @param checkEndDay
	 * @param checkBillCode
	 * @param checkBillType
	 * @param excludeIds
	 * @param page
	 * @return
	 */
	public default Page<CostBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay, String checkEndDay,
													String checkBillCode, int checkBillType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<CostBill>() {

			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(Root<CostBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();

				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), CostBill.STATUS_AUDITED));

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
								builder.abs(round(builder, builder.coalesce(root.get("freeAmount"), 0))),
								builder.abs(round(builder, builder.coalesce(root.get("totalPayAmount"), 0)))
						)
				);

				addCheckFilter(paybill, checkBillType, excludeIds, predicates, builder, root);

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
}
