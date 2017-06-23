package cn.fooltech.fool_ops.domain.cashier.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.cashier.entity.BankBill;
import cn.fooltech.fool_ops.domain.cashier.vo.BankBillVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 银行单据 持久层
 * @author cwz
 *
 */
public interface BankBillRepository extends JpaRepository<BankBill, String>,JpaSpecificationExecutor<BankBill> {
	
	/**
	 * 查找自动对账表格1的BankBill记录 
	 * @param accountId 账套id
	 * @param subjectId 科目id
	 * @return
	 */
	@Query("select b from BankBill b where fiscalAccount.fid=?1 and subject.fid=?2 and checked=0 and type in(2,3)")
	public List<BankBill> queryAutoCheckUp(String accountId,String subjectId); 

	/**
	 * 查找自动对账表格2的BankBill记录 
	 * @param accountId
	 * @param checkDate 对账日期
	 * @param limitDate 截止日期
	 * @param checkDay 相隔日期是否勾选 0：不勾选 1：勾选
	 * @param days 相隔多少日期（当checkDay=1时有效）
	 * @param settlementType 结算方式相同是否勾选 0：不勾选 1：勾选
	 * @param settlementNo 结算号相同是否勾选 0：不勾选 1：勾选
	 * @param voucherDate 结算日期相同是否勾选 0：不勾选 1：勾选
	 * @return
	 */
	public default List<BankBill> queryAutoCheckDown(String accountId, Date limitDate,
			int checkDay, int days, int settlementType, 
			int settlementNo, Integer settlementDate, BankBill upRecord,String subjectId) {
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_COMPANY, BankBill.TYPE_STATEMENT);
		List<BankBill> findAll = findAll(new Specification<BankBill>() {
			
			@Override
			public Predicate toPredicate(Root<BankBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				predicates.add(builder.equal(root.get("subject").get("fid"), subjectId));
				predicates.add(builder.equal(root.get("checked"), BankBill.UN_CHECKED));
				predicates.add(root.get("type").in(types));
				predicates.add(builder.lessThanOrEqualTo(root.get("voucherDate"), limitDate));
				BigDecimal credit = upRecord.getCredit()==null?BigDecimal.ZERO:upRecord.getCredit();
				BigDecimal debit = upRecord.getDebit()==null?BigDecimal.ZERO:upRecord.getDebit();
				if(credit.compareTo(BigDecimal.ZERO)>0){
					predicates.add(builder.equal(root.get("debit"), credit));
				}else if(debit.compareTo(BigDecimal.ZERO)>0){
					predicates.add(builder.equal(root.get("credit"), debit));
				}
				
				if(checkDay>0){
					Date start = DateUtilTools.changeDateTime(upRecord.getVoucherDate(), 
							0, -days, 0, 0, 0);
					Date end = DateUtilTools.changeDateTime(upRecord.getVoucherDate(), 
							0, days, 0, 0, 0);
					predicates.add(builder.between(root.get("voucherDate"), start, end));
				}
				if(settlementType>0){
					if(upRecord.getSettlementType()!=null){
						predicates.add(builder.equal(root.get("settlementType"), upRecord.getSettlementType()));
					}else{
						predicates.add(builder.isNull(root.get("settlementType")));
					}
				}
				if(settlementNo>0){
					predicates.add(builder.equal(root.get("settlementNo"), upRecord.getSettlementNo()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return findAll;
	}
	
	public default Page<BankBill> query(BankBillVo bankBillVo,Pageable pageable){
		Page<BankBill> findAll = findAll(new Specification<BankBill>() {
			
			@Override
			public Predicate toPredicate(Root<BankBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				Integer type = bankBillVo.getType();
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				}
				//类型
				if(type!=null){
					predicates.add(builder.equal(root.get("type"), type));
				}else{
					Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
					List<String> typeStrs = splitter.splitToList(bankBillVo.getTypes());
					List<Integer> typeInts = Lists.newArrayList();
					for(String typeStr:typeStrs){
						typeInts.add(Integer.parseInt(typeStr));
					}
//					predicates.add(builder.in(root.get("type").in(typeInts)));
//					query.where(root.get("type").in(typeInts));
					Expression<String> exp = root.get("type");
					predicates.add(exp.in(typeStrs));
					
				}
				
				//开始日期
				if(StringUtils.isNotBlank(bankBillVo.getStartDate())){
					Date startDate = DateUtilTools.string2Date(bankBillVo.getStartDate(), "yyyy-MM-dd");
					predicates.add(builder.greaterThanOrEqualTo(root.get("voucherDate"), startDate));
				}
				//结束日期
				if(StringUtils.isNotBlank(bankBillVo.getEndDate())){
					Date endDate = DateUtilTools.string2Date(bankBillVo.getEndDate(), "yyyy-MM-dd");
					predicates.add(builder.lessThanOrEqualTo(root.get("voucherDate"), endDate));
				}
				//科目ID
				if(StringUtils.isNotBlank(bankBillVo.getSubjectId())){
					predicates.add(builder.equal(root.get("subject").get("fid"), bankBillVo.getSubjectId()));
				}
				//科目IDs
				if(StringUtils.isNotBlank(bankBillVo.getSubjectIds())){
					Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
					List<String> subjectIdList = splitter.splitToList(bankBillVo.getSubjectIds());
					predicates.add(root.get("subject").get("fid").in(subjectIdList));
				}
				
				//结算方式
				if(StringUtils.isNotBlank(bankBillVo.getSettlementTypeId())){
					predicates.add(builder.equal(root.get("settlementType").get("fid"), bankBillVo.getSettlementTypeId()));
				}
				//结算号
				if(StringUtils.isNotBlank(bankBillVo.getSettlementNo())){
					predicates.add(builder.like(root.get("settlementNo"), "%"+bankBillVo.getSettlementNo()+"%"));
				}
				//金额方向
				if(bankBillVo.getDirection()!=null){
					if(bankBillVo.getDirection()>0){
						predicates.add(builder.notEqual(root.get("debit"), BigDecimal.ZERO));
					}else{
						predicates.add(builder.notEqual(root.get("credit"), BigDecimal.ZERO));
					}
				}
				//勾对
				if(bankBillVo.getFchecked()!=null && bankBillVo.getFchecked()!=2){
					predicates.add(builder.equal(root.get("checked"), bankBillVo.getFchecked()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		return findAll;
	}
	/**
	 * 统计贷方金额
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	@Query("select sum(b.credit) from BankBill b where fiscalAccount.fid=?1 and subject.fid=?2 and checked=?3 and type in(?4)")
	public BigDecimal countCreditAmount(String fiscalAccountId, String subjectId, short checked, List<Integer>type);
	/**
	 * 查询记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	@Query("select b from BankBill b where fiscalAccount.fid=?1 and subject.fid=?2 and checked=?3 and type in(?4) order by voucherDate,settlementDate")
	public List<BankBill> getBillds(String fiscalAccountId, String subjectId, short checked, List<Integer> type);

	/**
	 * 查询记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param type 单据类型
	 * @param voucherDate 单据日期
	 */
	@Query("select b from BankBill b where fiscalAccount.fid=?1 and subject.fid=?2 and type=?3 and voucherDate <=?4 order by voucherDate,orderno")
	public List<BankBill> getBillFilterBy(String fiscalAccountId, String subjectId, Integer type, Date voucherDate);

	/**
	 * 统计借方金额
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	@Query("select sum(b.debit) from BankBill b where fiscalAccount.fid=?1 and subject.fid=?2 and checked=?3 and type in(?4)")
	public BigDecimal countDebitAmount(String fiscalAccountId, String subjectId, short checked, List<Integer> type);
	/**
	 * 根据科目id查询银行单据记录
	 * @param subjectId 科目id
	 * @return
	 */
	@Query("select b from BankBill b where subject.fid=?1")
	public List<BankBill> queryBySubject(String subjectId);

	/**
	 * 根据科目id统计
	 * @param subjectId 科目id
	 * @return
	 */
	@Query("select count(*) from BankBill b where b.subject.fid=?1")
	public Long countBySubjectId(String subjectId);

	/**
	 * 根据银行单ID查找银行单记录
	 * @param ids 银行单ID
	 */
	@Query("select b from BankBill b where b.fid in ?1 order by voucherDate asc, orderno asc")
	public List<BankBill> getByIDs(List<String> ids);
	/**
	 * 根据日期和当天单号查询是否有重复
	 */
	@Query("select count(*) from BankBill b where b.voucherDate=?1 and b.orderno=?2 and b.subject.fid=?3 and b.fid!=?4 and b.fiscalAccount.fid=?5 and b.type=?6")
	public Long countByDateAndNo(Date date,int no,String subjectId,String fid,String accId,Integer type);
	@Query("select count(*) from BankBill b where b.voucherDate=?1 and b.orderno=?2 and b.subject.fid=?3  and b.fiscalAccount.fid=?4 and b.type=?5")
	public Long countByDateAndNo(Date date,int no,String subjectId,String accId,Integer type);
}
