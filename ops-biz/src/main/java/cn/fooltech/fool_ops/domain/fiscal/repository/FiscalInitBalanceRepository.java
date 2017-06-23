package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.fooltech.fool_ops.utils.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalInitBalance;

public interface FiscalInitBalanceRepository extends FoolJpaRepository<FiscalInitBalance, String>, 
	FoolJpaSpecificationExecutor<FiscalInitBalance> {

	/**
	 * 根据装套查找
	 * @param accountId
	 * @return
	 */
	@Query("select a from FiscalInitBalance a where a.fiscalAccount.fid=?1")
	public List<FiscalInitBalance> findByAccId(String accountId);

	/**
	 * 根据属性查找
	 * @param accId
	 * @param code
	 * @param name
	 * @param type
	 * @return
	 */
	public default List<FiscalInitBalance> findBy(String accId, String code, String name, 
			Integer type){
		
		Sort sort = new Sort(Direction.ASC, "subject.code");
		return findAll(new Specification<FiscalInitBalance>() {

			@Override
			public Predicate toPredicate(Root<FiscalInitBalance> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("isCheck"), FiscalInitBalance.UN_CHECK));
				
				if (StringUtils.isNotBlank(code)) {
					predicates.add(builder.like(root.get("subject").get("code"), PredicateUtils.getAnyLike(code)));
				}
				if (StringUtils.isNotBlank(name)) {
					predicates.add(builder.like(root.get("subject").get("name"), PredicateUtils.getAnyLike(name)));
				}
				if (type != null) {
					predicates.add(builder.equal(root.get("subject").get("type"), type));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 根据账套ID查找所有根
	 */
	public default List<FiscalInitBalance> findParentsByAccId(String accId, Integer type) {
		
		Sort sort = new Sort(Direction.ASC, "subject.code");
		return findAll(new Specification<FiscalInitBalance>() {

			@Override
			public Predicate toPredicate(Root<FiscalInitBalance> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("subject").get("level"), 1));
				predicates.add(builder.equal(root.get("isCheck"), FiscalInitBalance.UN_CHECK));

				if (type != null) {
					predicates.add(builder.equal(root.get("subject").get("type"), type));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 根据科目ID查询不包含核算科目的期初数据
	 * @param subjectId
	 * @return
	 */
	@Query("select f from FiscalInitBalance f where f.subject.fid=?1 and f.isCheck=?2 order by f.subject.code asc,f.createTime asc")
	public List<FiscalInitBalance> findBySubjectIdAndCheckStatus(String subjectId, short checkStatus);
	
	/**
	 * 根据科目ID查询不包含核算科目的期初数据
	 * @param subjectId
	 * @return
	 */
	@Query("select f from FiscalInitBalance f where f.subject.fid=?1 and f.isCheck="+FiscalInitBalance.UN_CHECK+" order by f.subject.code asc,f.createTime asc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalInitBalance findTopUnCheckBySubjectId(String subjectId);
	
	/**
	 * 根据科目ID查询期初数据
	 * @param subjectId
	 * @return
	 */
	@Query("select f from FiscalInitBalance f where f.subject.fid=?1")
	public List<FiscalInitBalance> findBySubjectId(String subjectId);
	
	/**
	 * 根据关联查询数据
	 * @param entity
	 * @return
	 */
	public default List<FiscalInitBalance> findByRelationData(FiscalInitBalance entity) {
		Sort sort = new Sort(Direction.ASC, "subject.code");
		return findAll(new Specification<FiscalInitBalance>() {

			@Override
			public Predicate toPredicate(Root<FiscalInitBalance> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				return getPredicate(entity, root, builder);
			}
		}, sort);
	}

	
	/**
	 * 根据关联查询数据
	 * @param entity
	 * @return
	 */
	public default FiscalInitBalance findTopByRelationData(FiscalInitBalance entity) {
		return findTop(new Specification<FiscalInitBalance>() {

			@Override
			public Predicate toPredicate(Root<FiscalInitBalance> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				return getPredicate(entity, root, builder);
			}
		});
	}
	
	default Predicate getPredicate(FiscalInitBalance entity, Root<FiscalInitBalance> root, CriteriaBuilder builder){
		List<Predicate> predicates = Lists.newArrayList();
		if(entity.getDepartment()!=null){
			predicates.add(builder.equal(root.get("department"), entity.getDepartment()));
		}
		if(entity.getSupplier()!=null){
			predicates.add(builder.equal(root.get("supplier"), entity.getSupplier()));
		}
		if(entity.getCustomer()!=null){
			predicates.add(builder.equal(root.get("customer"), entity.getCustomer()));
		}
		if(entity.getWarehouse()!=null){
			predicates.add(builder.equal(root.get("warehouse"), entity.getWarehouse()));
		}
		if(entity.getGoods()!=null){
			predicates.add(builder.equal(root.get("goods"), entity.getGoods()));
		}
		if(entity.getMember()!=null){
			predicates.add(builder.equal(root.get("member"), entity.getMember()));
		}
		if(entity.getProject()!=null){
			predicates.add(builder.equal(root.get("project"), entity.getProject()));
		}
		
		predicates.add(builder.equal(root.get("subject"), entity.getSubject()));
		predicates.add(builder.equal(root.get("isCheck"), entity.getIsCheck()));
		
		Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
		return predicate;
	}
	
	/**
	 * 根据余额方向计算期初总金额
	 * @param accountId
	 * @param direction
	 * @return
	 */
	@Query("select sum(b.amount) from FiscalInitBalance b where b.fiscalAccount.fid=?1 and b.direction=?2 and b.isFill="+FiscalInitBalance.FILL)
	public BigDecimal sumAmountByDirection(String accountId, int direction);

	
	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from FiscalInitBalance b where b.subject.fid=?1 and b.amount!=0")
	public Long countBySubjectId(String subjectId);
	
	/**
	 * 查找该科目核算项目的数量
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from FiscalInitBalance b where b.subject.fid=?1 and b.isCheck="+FiscalInitBalance.CHECK)
	public long countBrotherAccounting(String subjectId);

	/**
	 * 根据科目ID、余额方向获取金额
	 * @param subjectId
	 * @param direction
	 * @param accId
	 * @return
	 */
	public default BigDecimal getAmountBySubjectId(String subjectId, Integer direction, String accId){
		
		BigDecimal result = null;
		if(direction==null){
			result = getAmountWithoutDirection(subjectId, accId);
		}else{
			result = getAmountWithDirection(subjectId, direction, accId);
		}
		if(result==null)result = BigDecimal.ZERO;
		return result;
	}

	/**
	 * 根据科目ID、余额方向获取金额
	 * @param subjectId
	 * @param direction
	 * @param accId
	 * @return
	 */
	@Query("select sum(b.amount) from FiscalInitBalance b where b.fiscalAccount.fid=?3 and b.direction=?2 and b.subject.fid=?1 and b.isCheck="+FiscalInitBalance.UN_CHECK)
	public BigDecimal getAmountWithDirection(String subjectId, Integer direction, String accId);

	/**
	 * 根据科目ID获取金额
	 * @param subjectId
	 * @param accId
	 * @return
	 */
	@Query("select sum(b.amount) from FiscalInitBalance b where b.fiscalAccount.fid=?2 and b.subject.fid=?1 and b.isCheck="+FiscalInitBalance.UN_CHECK)
	public BigDecimal getAmountWithoutDirection(String subjectId, String accId);

	/**
	 * 根据科目编号，科目方向获取金额
	 * @param code
	 * @return
	 */
	public default BigDecimal getAmountBySubjectCode(String code, Integer direction, String accId){
		if(Strings.isNullOrEmpty(code))return BigDecimal.ZERO;
		
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> codes = splitter.splitToList(code);
		List<String> conditions = Lists.newArrayList();
		for(String temp:codes){
			conditions.add("b.fcode='"+temp+"'");
		}
		Joiner joiner = Joiner.on(" or ");
		String codeCondition = joiner.join(conditions);
		
		String sql = "select sum(coalesce(FAMOUNT,0))";
		sql += " from tbd_fiscal_init_balance a left join tbd_fiscal_accounting_subject b";
        sql += " on a.FFISCAL_SUBJECT_ID=b.FID where a.FACC_ID=:accId and a.FIS_CHECK=0 and";
		sql += " ("+codeCondition+")";
		if(direction!=null){
			sql += " and a.fdirection="+direction;
		}
		
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("accId", accId);
		Object result = query.getSingleResult();
		if(result!=null)return (BigDecimal)result;
		return BigDecimal.ZERO;
	}
}
