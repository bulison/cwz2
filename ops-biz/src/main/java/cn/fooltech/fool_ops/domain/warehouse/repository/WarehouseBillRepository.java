package cn.fooltech.fool_ops.domain.warehouse.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.CheckFilter;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherFilter;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
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

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 仓储单据Repository
 * @author xjh
 *
 */
public interface WarehouseBillRepository extends FoolJpaRepository<WarehouseBill, String>, 
	JpaSpecificationExecutor<WarehouseBill>, VoucherFilter, CheckFilter{

	/**
	 * 统计某个单据类型下的单据数量
	 *
	 * @param orgId    机构ID
	 * @param billType 单据类型
	 * @return
	 */
	@Query("select count(*) from WarehouseBill b where b.org.fid=?1 and b.billType=?2")
	public Long countByBillType(String orgId, int billType);

	/**
	 * 根据银行编号计算个数
	 * @param accId
	 * @param code
	 * @return
	 */
	@Query("select count(b.fid) from WarehouseBill b where b.fiscalAccount.fid=?1 and b.code=?2")
	public Long countByCode(String accId, String code);
	
	/**
	 * 根据银行编号计算个数
	 * @param accId
	 * @param code
	 * @param excludeId
	 * @return
	 */
	@Query("select count(b.fid) from WarehouseBill b where b.fiscalAccount.fid=?1 and b.code=?2 and b.fid!=?3")
	public Long countByCode(String accId, String code, String excludeId);
	
	/**
	 * 根据银行编号计算个数
	 * @param accId
	 * @param voucherCode
	 * @return
	 */
	@Query("select count(b.fid) from WarehouseBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.billDate>=?3 and b.billDate<=?4")
	public Long countByVoucherCode(String accId, String voucherCode,Date start,Date end);
	
	/**
	 * 根据银行编号计算个数
	 * @param accId
	 * @param voucherCode
	 * @param excludeId
	 * @return
	 */
	@Query("select count(b.fid) from WarehouseBill b where b.fiscalAccount.fid=?1 and b.voucherCode=?2 and b.fid!=?3 and billDate>=?4 and billDate<=?5")
	public Long countByVoucherCode(String accId, String voucherCode, String excludeId,Date start,Date end);
	
	/**
	 * 分页查询
	 * @param vo
	 * @param page
	 * @return
	 */
	public default Page<WarehouseBill> findPageBy(String accId, WarehouseBillVo vo, Pageable page){
		return findAll(new Specification<WarehouseBill>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据类型
				if(vo.getBillType() != null && vo.getBillType() != WarehouseBuilderCodeHelper.base){
					predicates.add(builder.equal(root.get("billType"), vo.getBillType()));
				}
				//单号搜索
				if(StringUtils.isNotBlank(vo.getCode())){
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(vo.getCode())));
				}
				//凭证号搜索
				if(StringUtils.isNotBlank(vo.getVoucherCode())){
					predicates.add(builder.like(root.get("voucherCode"), PredicateUtils.getAnyLike(vo.getVoucherCode())));
				}
				
				//单号或凭证号
				if(StringUtils.isNotBlank(vo.getCodeOrVoucherCode())){
					String likekey = PredicateUtils.getAnyLike(vo.getCodeOrVoucherCode());
					predicates.add(builder.or(
							builder.like(root.get("code"), likekey),
							builder.like(root.get("voucherCode"), likekey)
						));
				}

				//仓库搜索
				if(StringUtils.isNotBlank(vo.getInWareHouseId())){

					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);
					Root<WarehouseBillDetail> fromProject = subquery.from(WarehouseBillDetail.class);
					subquery.select(fromProject.get("fid"));
					subquery.where(
							builder.equal(fromProject.get("bill").get("fid"), root.get("fid")),
							builder.equal(fromProject.get("inWareHouse").get("fid"), vo.getInWareHouseId())
					);

					predicates.add(builder.or(
							builder.equal(root.get("inWareHouse").get("fid"), vo.getInWareHouseId()),
							builder.exists(subquery)
							));
				}
				if(StringUtils.isNotBlank(vo.getOutWareHouseId())){
					predicates.add(builder.equal(root.get("outWareHouse").get("fid"), vo.getOutWareHouseId()));
				}
				//单据状态搜索
				if(vo.getRecordStatus() != null){
					predicates.add(builder.equal(root.get("recordStatus"), vo.getRecordStatus()));
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
				if(vo.getTaskFilter() != null){
					if(vo.getTaskFilter()==1){
						predicates.add(builder.isNull(root.get("task").get("fid")));
					}
					
				}
				//销售商搜索
				if(StringUtils.isNotBlank(vo.getCustomerId())){
					predicates.add(builder.equal(root.get("customer").get("fid"), vo.getCustomerId()));
				}
				//供应商搜索
				if(StringUtils.isNotBlank(vo.getSupplierId())){
					predicates.add(builder.equal(root.get("supplier").get("fid"), vo.getSupplierId()));
				}
				//制单人搜索
				if(StringUtils.isNotBlank(vo.getCreateId())){
					predicates.add(builder.equal(root.get("creator").get("fid"), vo.getCreateId()));
				}
				//经手人搜索
				if(StringUtils.isNotBlank(vo.getInMemberId())){
					predicates.add(builder.equal(root.get("inMember").get("fid"), vo.getInMemberId()));
				}
				//业务部门搜索
				if(StringUtils.isNotBlank(vo.getDeptId())){
					predicates.add(builder.equal(root.get("dept").get("fid"), vo.getDeptId()));
				}
				//货品搜索
				if(StringUtils.isNotBlank(vo.getGoodsId())){
					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);  
			        Root<WarehouseBillDetail> fromProject = subquery.from(WarehouseBillDetail.class);  
			        subquery.select(fromProject.get("fid"));
			        subquery.where(
			        		builder.equal(fromProject.get("bill").get("fid"), root.get("fid")),
			        		builder.equal(fromProject.get("goods").get("fid"), vo.getGoodsId())
			        	);
			        predicates.add(builder.exists(subquery));
				}
				//单据关联
				if(StringUtils.isNotBlank(vo.getRelationId())){
					
					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);
			        Root<BillRelation> fromProject = subquery.from(BillRelation.class);
			        
			        subquery.select(fromProject.get("fid"));
			        subquery.where(
			        		builder.equal(fromProject.get("billId"), root.get("fid")),
			        		builder.equal(fromProject.get("refBillId"), vo.getRelationId())
			        	);
			        predicates.add(builder.exists(subquery));
				}
				//单据关联
				if(StringUtils.isNotBlank(vo.getRelationCode())){

					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);
					Root<BillRelation> fromProject0 = subquery.from(BillRelation.class);
					Root<WarehouseBill> fromProject1 = subquery.from(WarehouseBill.class);
					subquery.select(fromProject1.get("fid"));
					subquery.where(
							builder.equal(fromProject0.get("billId"), root.get("fid")),
							builder.equal(fromProject0.get("refBillId"), fromProject1.get("fid")),
							builder.equal(fromProject1.get("code"), vo.getRelationCode())
					);
					predicates.add(builder.exists(subquery));
				}
				if(vo.getProductionStatus()!=null){
					predicates.add(builder.equal(root.get("productionStatus"), vo.getProductionStatus()));
				}
				if(StringUtils.isNoneBlank(vo.getTransportNo())){
					predicates.add(builder.like(root.get("transportNo"), PredicateUtils.getAnyLike(vo.getTransportNo())));
				}
				if(StringUtils.isNoneBlank(vo.getDeliveryPlaceId())){
					predicates.add(builder.equal(root.get("deliveryPlace").get("fid"), vo.getDeliveryPlaceId()));
				}
				if(StringUtils.isNoneBlank(vo.getReceiptPlaceId())){
					predicates.add(builder.equal(root.get("receiptPlace").get("fid"), vo.getReceiptPlaceId()));
				}
				if(StringUtils.isNoneBlank(vo.getCarNo())){
					predicates.add(builder.like(root.get("carNo"), PredicateUtils.getAnyLike(vo.getCarNo())));
				}
				//箱号搜索
				if(StringUtils.isNotBlank(vo.getContainerNumber())){
					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);
					Root<TransportBilldetail> fromProject = subquery.from(TransportBilldetail.class);
					subquery.select(fromProject.get("id"));
					subquery.where(
							builder.equal(fromProject.get("transportBill").get("fid"), root.get("fid")),
							builder.like(fromProject.get("containerNumber"),
									PredicateUtils.getAnyLike(vo.getContainerNumber()))
					);
					predicates.add(builder.exists(subquery));
				}
				//封号搜索
				if(StringUtils.isNotBlank(vo.getSealingNumber())){
					Subquery<WarehouseBill> subquery = query.subquery(WarehouseBill.class);
					Root<TransportBilldetail> fromProject = subquery.from(TransportBilldetail.class);
					subquery.select(fromProject.get("id"));
					subquery.where(
							builder.equal(fromProject.get("transportBill").get("fid"), root.get("fid")),
							builder.or(builder.like(fromProject.get("sealingNumber1"),
											PredicateUtils.getAnyLike(vo.getSealingNumber())),
										builder.like(fromProject.get("sealingNumber2"),
											PredicateUtils.getAnyLike(vo.getSealingNumber()))
							)
					);
					predicates.add(builder.exists(subquery));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}

	/**
	 * 统计某个单据类型下同一供应商的单据数量
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID 
	 * @param billType
	 * @param supplierId
	 */
	public default Long countByBillTypeSupplier(String orgId, String accountId, int billType, String supplierId,
			String selfId) {
		List<WarehouseBill> list = findAll(new Specification<WarehouseBill>() {
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				predicates.add(builder.equal(root.get("billType"), billType));
				predicates.add(builder.equal(root.get("supplier").get("fid"), supplierId));
				if (StringUtils.isNotBlank(selfId)) {
					predicates.add(builder.notEqual(root.get("fid"), selfId));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return list!=null&&list.size()>0?list.size():0L;
	}
	/**
	 * 统计某个单据类型下同一客户的单据数量
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @param billType
	 * @param customerId
	 */
	public default Long countByBillTypeCustomer(String orgId, String accountId, int billType, String customerId, String selfId) {

		List<WarehouseBill> list = findAll(new Specification<WarehouseBill>() {
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				predicates.add(builder.equal(root.get("billType"), billType));
				predicates.add(builder.equal(root.get("customer").get("fid"), customerId));
				if (StringUtils.isNotBlank(selfId)) {
					predicates.add(builder.notEqual(root.get("fid"), selfId));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return list!=null&&list.size()>0?list.size():0L;
	}

	/**
	 * 统计与某个供应商相关的单据单据金额
	 * @param supplierId
	 * @param accountId 财务账套ID
	 * @param billType
	 * @return
	 */
	@Query("select sum(b.totalAmount) from WarehouseBill b where b.supplier.fid=?1 and b.fiscalAccount.fid=?2 and b.billType=?3 and b.recordStatus="+WarehouseBill.STATUS_AUDITED)
	public BigDecimal sumBySupplier(String supplierId, String accountId, int billType);
	
	/**
	 * 统计与某个供应商相关的单据单据金额
	 * @param customerId
	 * @param accountId 财务账套ID
	 * @param billType
	 * @return
	 */
	@Query("select sum(b.totalAmount) from WarehouseBill b where b.customer.fid=?1 and b.fiscalAccount.fid=?2 and b.billType=?3 and b.recordStatus="+WarehouseBill.STATUS_AUDITED)
	public BigDecimal sumByCustomer(String customerId, String accountId, int billType);
	
	
	/**
	 * 统计相关的单据单据金额
	 * @param accountId 财务账套ID
	 * @param billType
	 * @return
	 */
	@Query("select sum(b.totalAmount) from WarehouseBill b where b.fiscalAccount.fid=?1 and b.billType=?2 and b.recordStatus="+WarehouseBill.STATUS_AUDITED)
	public BigDecimal sumAmount(String accountId, int billType);
	
	/**
	 * 根据时间获取单据
	 * @param startDate
	 * @param endDate
	 * @param accountId
	 * @return
	 */
	@Query("select b from WarehouseBill b where b.fiscalAccount.fid=?3 and b.billType in ?4 and b.createTime>=?1 and b.createTime<=?2 and b.recordStatus="+WarehouseBill.STATUS_AUDITED)
	public List<WarehouseBill> getWarehouseBills(Date startDate,Date endDate,String accountId,List<Integer> billTypes);
	
	
	/**
	 * 分页查询
	 * @param vo
	 * @param page
	 * @return
	 */
	public default Page<WarehouseBill> queryCommonBills(String accId, VoucherMakeVo vo, Pageable page){
		return findAll(new Specification<WarehouseBill>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), WarehouseBill.STATUS_AUDITED));
				
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
				//业务部门搜索
				if(StringUtils.isNotBlank(vo.getDeptId())){
					predicates.add(builder.equal(root.get("dept").get("fid"), vo.getDeptId()));
				}
				
				predicates.add(addVoucherFilter(root, WarehouseBill.class, query, builder, vo));
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	

	/**
	 * 根据时间段获取销售出货-销售退货总金额
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param accountId 账套ID
	 * @param customerId 客户ID
	 * @return
	 */
	public default BigDecimal getCustomerSummary(Date startDate,Date endDate, String accountId,
			String customerId){
		
		String sql = "select sum(case when a.FBILL_TYPE=41 then a.FTOTAL_AMOUNT else -1*a.FTOTAL_AMOUNT end) from TSB_WAREHOUSE_BILL a where a.RECORD_STATUS=1 and a.facc_id=:accId and a.FBILL_DATE<=:endDate and a.FBILL_DATE>=:startDate and a.FCUSTOMER_ID=:customerId and a.FBILL_TYPE in (41,42)";
		
		javax.persistence.Query sqlQuery = getEntityManager().createNativeQuery(sql);
		sqlQuery.setParameter("accId", accountId);
		sqlQuery.setParameter("endDate", endDate);
		sqlQuery.setParameter("startDate", startDate);
		sqlQuery.setParameter("customerId", customerId);
		
		Object data = sqlQuery.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	
	/**
	 * 根据时间段获取采购入库-采购退货总金额
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param accountId 账套ID
	 * @return
	 */
	public default BigDecimal getSupplierSummary(Date startDate,Date endDate, String accountId,
			String supplierId){
		
		String sql = "select sum(case when a.FBILL_TYPE=11 then a.FTOTAL_AMOUNT else -1*a.FTOTAL_AMOUNT end) from TSB_WAREHOUSE_BILL a where a.RECORD_STATUS=1 and a.facc_id=:accId and a.FBILL_DATE<=:endDate and a.FBILL_DATE>=:startDate and a.FSUPPLIER_ID=:supplierId and a.FBILL_TYPE in (11,12)";
		
		javax.persistence.Query sqlQuery = getEntityManager().createNativeQuery(sql);
		sqlQuery.setParameter("accId", accountId);
		sqlQuery.setParameter("endDate", endDate);
		sqlQuery.setParameter("startDate", startDate);
		sqlQuery.setParameter("supplierId", supplierId);
		
		Object data = sqlQuery.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	
	/**
	 * 分页查询
	 * @return
	 */
	public default Page<WarehouseBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay, String checkEndDay, 
			String checkBillCode, int checkBillType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<WarehouseBill>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//单据状态搜索
				predicates.add(builder.equal(root.get("recordStatus"), WarehouseBill.STATUS_AUDITED));
				
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
						round(builder,
							builder.sum(
									builder.diff(
										builder.diff(
												builder.coalesce(root.get("totalAmount"), 0),
												builder.coalesce(root.get("freeAmount"), 0)
											),
											builder.coalesce(root.get("deductionAmount"), 0)
									),
								builder.coalesce(root.get("expenseAmount"), 0)
							)
						),
						round(builder, builder.coalesce(root.get("totalPayAmount"), 0))
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
	 * @return
	 */
	public default Page<WarehouseBill> findPageBy(String accId, CostBill costbill, Date startDay, Date endDay, String code,
			Integer billType, List<String> excludeIds, Pageable page){
		return findAll(new Specification<WarehouseBill>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, 
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
	 * 根据发货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from WarehouseBill a where a.deliveryPlace.fid=?1")
	public Long queryByDeliveryPlaceCount(String fid);
	
	/**
	 * 根据收货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from WarehouseBill a where a.receiptPlace.fid=?1")
	public Long queryByReceiptPlaceCount(String fid);
	
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param supplierId 供应商ID
	 * @return
	 */
	@Query("select count(a) from WarehouseBill a where a.supplier.fid=?1")
	public long countBySupplier(String supplierId);
	/**
	 * 统计所有单据引用了某个供应商的数量
	 * @param customerId 供应商ID
	 * @return
	 */
	@Query("select count(a) from WarehouseBill a where a.customer.fid=?1")
	public long countByCustomer(String customerId);


}


