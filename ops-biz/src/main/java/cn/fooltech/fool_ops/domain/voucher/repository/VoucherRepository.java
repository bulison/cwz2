package cn.fooltech.fool_ops.domain.voucher.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface VoucherRepository extends JpaRepository<Voucher, String>, FoolJpaSpecificationExecutor<Voucher> {

	/**
	 * 根据VO查找分页
	 * @param accId
	 * @param vo
	 * @param page
	 * @return
	 */
	public default Page<Voucher> findPageByVo(String accId, VoucherVo vo, Pageable page){
		return findAll(new Specification<Voucher>() {
			
			@Override
			public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务账套
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				//ID
				if(StringUtils.isNotBlank(vo.getFid())){
					predicates.add(builder.equal(root.get("fid"), vo.getFid()));
				}
				//财务会计期间
				if(StringUtils.isNotBlank(vo.getFiscalPeriodId())){
					predicates.add(builder.equal(root.get("fiscalPeriod").get("fid"), vo.getFiscalPeriodId()));
				}
				//凭证摘要
				if(StringUtils.isNotBlank(vo.getVoucherResume())){
					
					Subquery<Voucher> subquery = query.subquery(Voucher.class);  
			        Root<VoucherDetail> fromProject = subquery.from(VoucherDetail.class);  
			        subquery.select(fromProject.get("fid"));
			        String likekey = PredicateUtils.getAnyLike(vo.getVoucherResume());
			        subquery.where(
			        		builder.equal(fromProject.get("voucher").get("fid"), root.get("fid")),
			        		builder.like(fromProject.get("resume"), likekey)
			        	);
			        predicates.add(builder.exists(subquery));
				}
				//凭证字
				if(StringUtils.isNotBlank(vo.getVoucherWordId())){
					predicates.add(builder.equal(root.get("voucherWord").get("fid"), vo.getVoucherWordId()));
				}
				//开始凭证号
				if(vo.getStartVoucherNumber() != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("voucherNumber"), vo.getStartVoucherNumber()));
				}
				//结束凭证号
				if(vo.getEndVoucherNumber() != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("voucherNumber"), vo.getEndVoucherNumber()));
				}
				//凭证开始日期
				if(vo.getStartDay() != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("voucherDate"), vo.getStartDay()));
				}
				//凭证结束日期
				if(vo.getEndDay() != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("voucherDate"), vo.getEndDay()));
				}
				//状态
				if(vo.getRecordStatus() != null){
					predicates.add(builder.equal(root.get("recordStatus"), vo.getRecordStatus()));
				}
				//创建人
				if(StringUtils.isNotBlank(vo.getCreatorId())){
					predicates.add(builder.equal(root.get("creator").get("fid"), vo.getCreatorId()));
				}
				//审核人
				if(StringUtils.isNotBlank(vo.getAuditorId())){
					predicates.add(builder.equal(root.get("auditor").get("fid"), vo.getAuditorId()));
				}
				//记账人
				if(StringUtils.isNotBlank(vo.getPostPeopleId())){
					predicates.add(builder.equal(root.get("postPeople").get("fid"), vo.getPostPeopleId()));
				}
				//附件数
				if(vo.getAccessoryNumber() != null){
					predicates.add(builder.equal(root.get("accessoryNumber"), vo.getAccessoryNumber()));
				}
				//科目
				if(StringUtils.isNotBlank(vo.getSubjectId())){
					Subquery<Voucher> subquery = query.subquery(Voucher.class);  
			        Root<VoucherDetail> fromProject = subquery.from(VoucherDetail.class);  
			        subquery.select(fromProject.get("fid"));
			        Short subjectFlag = vo.getSubjectFlag();
			        
			        /*cwz 2017-5-10 2154 填制凭证查询列表页面，查询条件科目，选择父科目后，请将父科目下所有的包含子科目的凭证都搜索出来 start  */ 
			        //判断是否叶子节点，否就该节点下记录全部查询，否则只查询当前科目记录
			        if(subjectFlag==0){
			        	String subjectCode = PredicateUtils.getRightLike(vo.getSubjectCode()+".");
				        subquery.where(
				        		builder.equal(fromProject.get("voucher").get("fid"), root.get("fid")),
				        		builder.like(fromProject.get("accountingSubject").get("code"), subjectCode)
//				        		,
//				        		builder.greaterThanOrEqualTo(fromProject.get("accountingSubject").get("level"), vo.getSubjectLevel())
				        	);
			        }else{
				        subquery.where(
				        		builder.equal(fromProject.get("voucher").get("fid"), root.get("fid")),
				        		builder.equal(fromProject.get("accountingSubject").get("fid"), vo.getSubjectId())
				        	);
			        }
			        /*cwz 2017-5-10 2154 填制凭证查询列表页面，查询条件科目，选择父科目后，请将父科目下所有的包含子科目的凭证都搜索出来 end  */ 
			        predicates.add(builder.exists(subquery));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	
	/**
	 * 根据参数查找
	 * @param periodId
	 * @param voucherWordId
	 * @param startNumber
	 * @param endNumber
	 * @return
	 */
	public default List<Voucher> findBy(String periodId, String voucherWordId, Integer startNumber, Integer endNumber){
		return findAll(new Specification<Voucher>() {
			
			@Override
			public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				//财务会计期间
				predicates.add(builder.equal(root.get("fiscalPeriod").get("fid"), periodId));
				
				//凭证字
				if(StringUtils.isNotBlank(voucherWordId)){
					predicates.add(builder.equal(root.get("voucherWord").get("fid"), voucherWordId));
				}
				//开始凭证号
				if(startNumber != null){
					predicates.add(builder.greaterThanOrEqualTo(root.get("voucherNumber"), startNumber));
				}
				//结束凭证号
				if(endNumber != null){
					predicates.add(builder.lessThanOrEqualTo(root.get("voucherNumber"), endNumber));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	/**
	 * 获取某个财务会计期间下最新的凭证
	 * @param periodId 财务会计期间ID
	 * @return
	 */
	@Query("select v from Voucher v where v.fiscalPeriod.fid=?1 order by v.createTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public Voucher getNewestVoucher(String periodId);
	
	/**
	 * 获取某个财务账套下，某个凭证日期区间内已审核的凭证<br>
	 * 根据凭证字、凭证号排序<br>
	 * @param fiscalAccountId 财务账套ID
	 * @param startVoucherDate 凭证开始日期
	 * @param endVoucherDate 凭证结束日期
	 * @return
	 */
	@Query("select v from Voucher v where v.fiscalAccount.fid=?1 and v.voucherDate>=?2 and v.voucherDate<=?3 and v.recordStatus="
			+Voucher.STATUS_AUDITED+" order by v.voucherNumber asc,voucherWord.fid asc")
	public List<Voucher> getAuditedVouchersByOrders(String fiscalAccountId, 
			Date startVoucherDate, Date endVoucherDate);
	
	/**
	 * 获取某个财务会计期间下最大的凭证号
	 * @param fiscalPeriodId 财务会计期间ID
	 * @return
	 */
	@Query("select max(v.voucherNumber) from Voucher v where v.fiscalPeriod.fid=?1")
	public Integer getMaxVoucherNumber(String fiscalPeriodId);
	
	/**
	 * 获取某个财务会计期间下最大的凭证号，区分凭证字
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @return
	 */
	@Query("select max(v.voucherNumber) from Voucher v where v.fiscalPeriod.fid=?1 and v.voucherWord.fid=?2")
	public Integer getMaxVoucherNumber(String fiscalPeriodId, String voucherWordId);
	
	/**
	 * 获取某个财务会计期间下最大的凭证号，自动判断区分凭证字
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @return
	 */
	public default Integer getMaxVoucherNumberAuto(String fiscalPeriodId, String voucherWordId){
		Integer result = null;
		if(Strings.isNullOrEmpty(voucherWordId)){
			result = getMaxVoucherNumber(fiscalPeriodId);
		}else{
			result = getMaxVoucherNumber(fiscalPeriodId, voucherWordId);
		}
		if(result==null){
			return 0;
		}
		return result;
	}
	
	
	/**
	 * 获取某个财务会计期间下，最大的凭证顺序号
	 * @param fiscalPeriodId 财务会计期间ID
	 * @return
	 */
	@Query("select max(v.voucherNumber) from Voucher v where v.fiscalPeriod.fid=?1")
	public Integer getMaxNumber(String fiscalPeriodId);
	
	/**
	 * 计算某个财务会计期间下，凭证字和凭证号相同数据的个数
	 * @param fiscalPeriodId
	 * @param voucherWordId
	 * @param voucherNumber
	 * @param excludeId
	 * @return
	 */
	public default Long countByWordAndNumber(String fiscalPeriodId, String voucherWordId, 
			Integer voucherNumber, String excludeId){
		return count(new Specification<Voucher>() {
			
			@Override
			public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.get("voucherNumber"), voucherNumber));
				predicates.add(builder.equal(root.get("fiscalPeriod").get("fid"), fiscalPeriodId));
				predicates.add(builder.equal(root.get("voucherWord").get("fid"), voucherWordId));
				
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	/**
	 * 判断某个财务会计期间下,不以凭证字作为区分的情况下，凭证号是否已存在
	 * @param fiscalPeriodId
	 * @param voucherWordId
	 * @param voucherNumber
	 * @param excludeId
	 * @return
	 */
	public default Long countByVoucherNumber(String fiscalPeriodId, 
			Integer voucherNumber, String excludeId){
		return count(new Specification<Voucher>() {
			
			@Override
			public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.get("voucherNumber"), voucherNumber));
				predicates.add(builder.equal(root.get("fiscalPeriod").get("fid"), fiscalPeriodId));
				
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 
	 * @param fiscalPeriodId
	 * @param voucherWordId
	 * @param voucherNumber
	 * @param excludeId
	 * @return
	 */
	public default Long countByNumber(String fiscalPeriodId, Integer number, String excludeId){
		return count(new Specification<Voucher>() {
			
			@Override
			public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.get("fiscalPeriod").get("fid"), fiscalPeriodId));
				predicates.add(builder.equal(root.get("number"), number));
				
				if(StringUtils.isNotBlank(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 判断某个财务会计期间下，凭证字和凭证号是否已存在
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @param voucherNumber 凭证号
	 * @param excludeId 排除实体的ID
	 * @return true 存在   false 不存在
	 */
	public default boolean isWordAndNumberExist(String fiscalPeriodId, String voucherWordId, 
			Integer voucherNumber, String excludeId){
		Long count = countByWordAndNumber(fiscalPeriodId, voucherWordId, voucherNumber, excludeId);
		if(count==null)return false;
		return count>0?true:false;
	}
	/**
	 * 判断某个财务会计期间下,不以凭证字作为区分的情况下，凭证号是否已存在
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @param voucherNumber 凭证号
	 * @param excludeId 排除实体的ID
	 * @return true 存在   false 不存在
	 */
	public default boolean isVoucherNumberExist(String fiscalPeriodId, 
			Integer voucherNumber, String excludeId){
		Long count = countByVoucherNumber(fiscalPeriodId, voucherNumber, excludeId);
		if(count==null)return false;
		return count>0?true:false;
	}
	/**
	 * 判断某个财务会计期间下，某个顺序号是否已存在
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param number 顺序号
	 * @param excludeId 排除实体的ID
	 * @return true 存在   false 不存在
	 */
	public default boolean isNumberExist(String fiscalPeriodId, Integer number, String excludeId){
		Long count = countByNumber(fiscalPeriodId, number, excludeId);
		if(count==null)return false;
		return count>0?true:false;
	}
	
	/**
	 * 获取下一个凭证
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @param voucherNumber 凭证号
	 * @return
	 */
	@Query("select v from Voucher v where v.fiscalPeriod.fid=?1 and v.voucherWord.fid=?2 and v.voucherNumber=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public Voucher getNextVoucher(String fiscalPeriodId, String voucherWordId, int voucherNumber);
	
	/**
	 * 根据科目（包括七个辅助核算、单位、外币币别），合并凭证明细
	 * @param voucherId 凭证ID
	 */
	@Procedure(procedureName="merge_voucher_detail")
	public void mergeDetailBySubject(@Param("voucherId")String voucherId);

	/**
	 * 根据会计期间ID和状态列表查询
	 * @param fiscalPeriodId
	 * @param statusLists
	 * @return
	 */
	@Query("select count(*) from Voucher v where v.fiscalPeriod.fid=?1 and v.recordStatus in ?2")
	public Long countByPeriodIdAndStatus(String fiscalPeriodId, List<Integer> statusLists);

	/**
	 * 根据会计期间统计
	 * @param id
	 * @return
	 */
	@Query("select count(*) from Voucher v where v.fiscalPeriod.fid=?1")
	public Long countByPeriodId(String id);
}
