package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.component.core.MatchDegreeOrder;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface FiscalAccountingSubjectRepository extends JpaRepository<FiscalAccountingSubject, String>, 
	FoolJpaSpecificationExecutor<FiscalAccountingSubject> {

	/**
	 * 根据属性查找
	 * @param accId
	 * @param code
	 * @param name
	 * @param type
	 * @param bankSubject
	 * @param cashSubject
	 * @return
	 */
	public default List<FiscalAccountingSubject> findBy(String accId, String code, String name, 
			Integer type, Short bankSubject, Short cashSubject, Sort sort){
		return findAll(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));
				
				if (StringUtils.isNotBlank(code)) {
					predicates.add(builder.equal(root.get("code"), code));
				}
				if (StringUtils.isNotBlank(name)) {
					predicates.add(builder.equal(root.get("name"), name));
				}
				if (type != null) {
					predicates.add(builder.equal(root.get("type"), type));
				}
				if (bankSubject != null) {
					predicates.add(builder.equal(root.get("bankSubject"), bankSubject));
				}
				if (cashSubject != null) {
					predicates.add(builder.equal(root.get("cashSubject"), cashSubject));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 根据属性查找
	 * @param accId
	 * @param code
	 * @param name
	 * @param type
	 * @return
	 */
	public default List<FiscalAccountingSubject> findBy(String accId, String code, String name, 
			Integer type){
		return findAll(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));
				
				if (StringUtils.isNotBlank(code)) {
					predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(code)));
				}
				if (StringUtils.isNotBlank(name)) {
					predicates.add(builder.like(root.get("name"), PredicateUtils.getAnyLike(name)));
				}
				if (type != null) {
					predicates.add(builder.equal(root.get("type"), type));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 根据属性查找分页
	 * @param accId
	 * @param code
	 * @param name
	 * @param type
	 * @param bankSubject
	 * @param cashSubject
	 * @return
	 */
	public default Page<FiscalAccountingSubject> findPageBy(String accId, String code, String name, 
			Integer type, Short bankSubject, Short cashSubject, Pageable page){
		return findAll(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));
				
				if (StringUtils.isNotBlank(code)) {
					predicates.add(builder.equal(root.get("code"), code));
				}
				if (StringUtils.isNotBlank(name)) {
					predicates.add(builder.equal(root.get("name"), name));
				}
				if (type != null) {
					predicates.add(builder.equal(root.get("type"), type));
				}
				if (bankSubject != null && FiscalAccountingSubject.BANK_YES == bankSubject) {
					predicates.add(builder.equal(root.get("bankSubject"), bankSubject));
				}
				if (cashSubject != null && FiscalAccountingSubject.CASH_YES == cashSubject) {
					predicates.add(builder.equal(root.get("cashSubject"), cashSubject));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	
	/**
	 * 根据起始编号和结束编号查找有核算数量的科目
	 * @param start
	 * @param end
	 * @param accId 
	 * @return
	 */
	public default List<FiscalAccountingSubject> findBy(String start,String end, Integer level, String accId){
		return findAll(new Specification<FiscalAccountingSubject>() {
			
			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("quantitySign"), FiscalAccountingSubject.ACCOUNT));
				predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));
				if (StringUtils.isNotBlank(start)) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("code"), start));
				}
				if (StringUtils.isNotBlank(end)) {
					predicates.add(builder.lessThanOrEqualTo(root.get("code"), end));
				}
				if(level!=null){
					predicates.add(builder.lessThanOrEqualTo(root.get("level"), level));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	/**
	 * 下一个科目
	 * @param curSubjectId
	 * @param startCode
	 * @param endCode
	 * @param level
	 * @return
	 */
	public default FiscalAccountingSubject findTopByNextSubject(String accId, String curSubjectId, String startCode, String endCode,
			Integer level){
		Sort sort = new Sort(Direction.ASC, "code");

		FiscalAccountingSubject curSubject = findOne(curSubjectId);
		String curSubjectCode = curSubject.getCode();

		return findTop(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {

				List<Predicate> predicates = getReportPredicates(accId, level, root, builder);

				if(StringUtils.isNotBlank(curSubjectCode)){
					predicates.add(builder.greaterThan(root.get("code"), curSubjectCode));//大于等于
				}
				if(StringUtils.isNotBlank(startCode)){
					predicates.add(builder.greaterThanOrEqualTo(root.get("code"), startCode));
				}
				if(StringUtils.isNotBlank(endCode)){
					predicates.add(builder.lessThanOrEqualTo(root.get("code"), endCode));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 上一个科目
	 * @param curSubjectId
	 * @param startCode
	 * @param endCode
	 * @param level
	 * @return
	 */
	public default FiscalAccountingSubject findTopByLastSubject(String accId, String curSubjectId, String startCode, String endCode,
			Integer level){
		Sort sort = new Sort(Direction.DESC, "code");
		FiscalAccountingSubject curSubject = findOne(curSubjectId);
		String curSubjectCode = curSubject.getCode();

		return findTop(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = getReportPredicates(accId, level, root, builder);

				if(StringUtils.isNotBlank(startCode)){
					predicates.add(builder.greaterThanOrEqualTo(root.get("code"), startCode));//大于等于
				}
				if(StringUtils.isNotBlank(curSubjectCode)){
					predicates.add(builder.lessThan(root.get("code"), curSubjectCode));
				}
				if(StringUtils.isNotBlank(endCode)){
					predicates.add(builder.lessThanOrEqualTo(root.get("code"), endCode));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	
	default List<Predicate> getReportPredicates(String accId,
			Integer level, Root<FiscalAccountingSubject> root, CriteriaBuilder builder){
		

		List<Predicate> predicates = Lists.newArrayList();
		predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
		predicates.add(builder.notEqual(root.get("code"), Constants.ROOT));
		predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));

		if(level != null){
			predicates.add(builder.lessThanOrEqualTo(root.get("level"), level));
		}
		return predicates;
	}
	
	/**
	 * 查找父ID查找子科目 
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.parent.fid=?1 and a.enable=1")
	public List<FiscalAccountingSubject> findByParentId(String parentId);
	
	/**
	 * 查找父ID查找第一个子科目 
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.parent.fid=?1 and a.enable=1 order by a.code asc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject findTopByParentId(String parentId);

	/**
	 * 查找默认科目
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.level=1 and a.enable=1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject findDefaultSubject(String accId, Sort sort);
	
	/**
	 * 根据编号和级别查找
	 * @return
	 */
	@Query(value="select * from tbd_fiscal_accounting_subject a where a.facc_id=?1 and a.flevel=?3 and a.fenable=1 and a.fcode regexp concat('^', ?2)", nativeQuery=true)
	public List<FiscalAccountingSubject> findByCodeAndLevel(String accId, String code, Integer level);

	/**
	 * 根据账套和科目编号查找
	 * @param accId
	 * @param code
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.code=?2  and a.enable=1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject findTopByCode(String accId, String code);

	/**
	 * 根据科目类型和科目标识查找
	 * @param accId
	 * @param type
	 * @param flag
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.type=?2 and a.flag=?3  and a.enable=1 order by a.code asc")
	public List<FiscalAccountingSubject> findByTypeAndFlag(String accId, int type, short flag);
	
	/**
	 * 根据账套ID查找
	 * @param accId
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.enable=1 order by a.code asc")
	public List<FiscalAccountingSubject> findByAccId(String accId);

	/**
	 * 模糊查询
	 * @param inputType
	 * @param parentId
	 * @param searchKey
	 * @param direction
	 * @param leafFlag
	 * @param bankSubject
	 * @param cashSubject
	 * @param accId
	 * @return
	 */
	public default List<FiscalAccountingSubject> vagueSearch(String inputType, String parentId, String searchKey,
			Integer direction, Short leafFlag, Short bankSubject, Short cashSubject, String accId){
		Sort sort = null;
		Order order1 = new Order(Direction.ASC, "code");
		if(NumberUtils.isDigits(searchKey)){
			MatchDegreeOrder order2 = new MatchDegreeOrder(Direction.ASC, "code", searchKey);
			sort = new Sort(order2, order1);
		}else{
			sort = new Sort(order1);
		}
		
		return findAll(new Specification<FiscalAccountingSubject>() {
			
			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("enable"), FiscalAccountingSubject.ENABLE_YES));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.notEqual(root.get("code"), Constants.ROOT));
				
				if(StringUtils.isNotBlank(searchKey)){
					String likeKey = searchKey.trim();
					likeKey = PredicateUtils.getAnyLike(likeKey);
					if(UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)){
						predicates.add(builder.or(
								builder.like(root.get("code"), likeKey),
								builder.like(root.get("zjm"), likeKey),
								builder.like(root.get("name"), likeKey),
								builder.like(root.get("fivepen"), likeKey)
							));
					}else{
						predicates.add(builder.or(
								builder.like(root.get("code"), likeKey),
								builder.like(root.get("zjm"), likeKey),
								builder.like(root.get("name"), likeKey),
								builder.like(root.get("pinyin"), likeKey)
							));
					}
				}
				
				if(direction != null){
					predicates.add(builder.equal(root.get("direction"), direction));
				}
				if(cashSubject != null||bankSubject != null){
					predicates.add(
							builder.or(
									builder.equal(root.get("cashSubject"), cashSubject),
									builder.equal(root.get("bankSubject"), bankSubject)
								)
						);
				}
				if(leafFlag != null && leafFlag == FiscalAccountingSubject.FLAG_CHILD){
					predicates.add(builder.equal(root.get("flag"), leafFlag));
				}
				if(StringUtils.isNotBlank(parentId)){
					predicates.add(builder.equal(root.get("parent").get("fid"), parentId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 查找根节点
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.code='"+Constants.ROOT+"'")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject findTopRoot(String accId);
	
	/**
	 * 根据父ID查询兄弟节点的编号
	 */
	public default String getBrotherCode(String parentId){
		
		FiscalAccountingSubject subject = findTop(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList(); 
				
				String excludeCode = PredicateUtils.getRightLike(".99");
				predicates.add(builder.notLike(root.get("code"), excludeCode));//排除99其他
				
				if(StringUtils.isNotBlank(parentId)){
					predicates.add(builder.equal(root.get("parent").get("fid"), parentId));
				}else{
					predicates.add(builder.isNull(root.get("parent")));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, new Sort(Direction.DESC, "code"));
		
		if(subject!=null)return subject.getCode();
		return null;
	}

	/**
	 * 查找父ID查找子科目 
	 * @return
	 */
	@Query("select count(*) from FiscalAccountingSubject a where a.parent.fid=?1")
	public Long countByParentId(String fid);
	
	/**
	 * 根据编号计算个数
	 * @return
	 */
	public default Long countByCode(String accId, String code, String excludeId){
		return count(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("code"), code));
				if(!Strings.isNullOrEmpty(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 根据助记码计算个数
	 * @return
	 */
	public default Long countByZjm(String accId, String zjm, String excludeId){
		return count(new Specification<FiscalAccountingSubject>() {

			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("zjm"), zjm));
				if(!Strings.isNullOrEmpty(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	/**
	 * 根据父ID查找分页(包含parentId自己，会展示无效节点)
	 * @param parentId
	 * @param excludeCode
	 * @param page
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.parent.fid=?1 or a.fid=?1 and a.code!=?2")
	public Page<FiscalAccountingSubject> findPageByParentId(String parentId, String excludeCode, Pageable page);
	
	/**
	 * 根据属性查找
	 * @param accId
	 * @param showRoot
	 * @param showDisable
	 * @param flag
	 * @param searchType
	 * @param searchKey
	 * @param subjectType
	 * @param direction
	 * @param bankSubject
	 * @param cashSubject
	 * @return
	 */
	public default List<FiscalAccountingSubject> findBy(String accId, int showRoot, int showDisable, 
			Short flag, Integer searchType, String searchKey, Integer subjectType, Integer direction,
			Short bankSubject, Short cashSubject){
		
		Sort sort = new Sort(Direction.ASC, "code");
		
		return findAll(new Specification<FiscalAccountingSubject>() {
			
			@Override
			public Predicate toPredicate(Root<FiscalAccountingSubject> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				
				if (showRoot == Constants.NOTSHOW) {
					predicates.add(builder.notEqual(root.get("code"), Constants.ROOT));
				}
				if (showDisable == Constants.NOTSHOW) {
					predicates.add(builder.notEqual(root.get("enable"), FiscalAccountingSubject.ENABLE_NO));
				}
				
				if (flag != null) {
					predicates.add(builder.equal(root.get("flag"), flag));
				}
				
				if (subjectType != null) {
					predicates.add(builder.equal(root.get("type"), subjectType));
				}
				
				if(direction != null){
					predicates.add(builder.equal(root.get("direction"), direction));
				}
				
				if (bankSubject != null && FiscalAccountingSubject.BANK_YES == bankSubject
						&& cashSubject != null && FiscalAccountingSubject.CASH_YES == cashSubject) {
					predicates.add(builder.or(
							builder.equal(root.get("bankSubject"), bankSubject),
							builder.equal(root.get("cashSubject"), cashSubject)
						));
					
				}else if (bankSubject != null && FiscalAccountingSubject.BANK_YES == bankSubject) {
					predicates.add(builder.equal(root.get("bankSubject"), bankSubject));
				}else if (cashSubject != null && FiscalAccountingSubject.CASH_YES == cashSubject) {
					predicates.add(builder.equal(root.get("cashSubject"), cashSubject));
				}
				
				if(searchType!=null){
					if (StringUtils.isNotBlank(searchKey)) {
						String likeKey = searchKey.trim();
						likeKey = PredicateUtils.getAnyLike(likeKey);
						if (searchType == 0) {
							predicates.add(builder.or(
									builder.like(root.get("code"), likeKey),
									builder.like(root.get("zjm"), likeKey)
								));
							predicates.add(builder.notEqual(root.get("code"), Constants.ROOT));
						} else if (searchType == 1) {
							predicates.add(builder.or(
									builder.like(root.get("code"), likeKey),
									builder.like(root.get("name"), likeKey)
								));
						} else if (searchType == 2) {
							predicates.add(builder.or(
									builder.equal(root.get("type"), Integer.parseInt(searchKey)),
									builder.equal(root.get("code"), Constants.ROOT)
								));
						} else if (searchType == 3) {
							predicates.add(builder.or(
									builder.like(root.get("code"), likeKey),
									builder.like(root.get("zjm"), likeKey),
									builder.like(root.get("name"), likeKey)
								));
						} 
					}
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);

	}

	/**
	 * 根据账套和状态查找
	 * @param accId
	 * @param enable
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.enable=?2")
	public List<FiscalAccountingSubject> findByAccIdAndEnable(String accId, short enable);
	
	/**
	 * 根据类型，名称，账套查找
	 * @param type
	 * @param name
	 * @param accId
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?3 and a.type=?1 and a.name=?2 and a.enable=1")
	public List<FiscalAccountingSubject> findSubjectByTypeAndName(int type, String name, String accId);
	
	/**
	 * 根据账套、类型查找
	 * @param type
	 * @param accId
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and a.type=?2 and a.enable=1")
	public List<FiscalAccountingSubject> findSubjectByAccIdAndType(String accId, int type);
	
	/**
	 * 获取银行和现金科目（只取叶子节点）
	 * @param accountId
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and flag=1 and (bankSubject=1 or cashSubject=1) and a.enable=1")
	public List<FiscalAccountingSubject> getBankCashSubjects(String accountId);
	
	/**
	 * 根据BankID查询有多少
	 */
	@Query("select count(*) from FiscalAccountingSubject f where f.relationId=?1")
	public Long countByRelationId(String relationId);

	/**
	 * 根据单位ID统计
	 * @param unitId
	 * @return
	 */
	@Query("select count(*) from FiscalAccountingSubject s where s.unit.fid=?1")
	public Long countByUnitId(String unitId);
	
	/**
	 * 初始化科目
	 */
	@Modifying
	@Query("delete from FiscalAccountingSubject f where f.fiscalAccount.fid=?1 and f.code!='ROOT'")
	public void deleteByAccId(String accId);

	/**
	 * 获取银行科目（只取叶子节点）
	 * @param accountId
	 * @param bankId
	 * @return
	 */
	@Query("select a from FiscalAccountingSubject a where a.fiscalAccount.fid=?1 and flag=1 and bankSubject=1 and relationId=?2 and a.enable=1 and relationType="+FiscalAccountingSubject.RELATION_BANK)
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject getTopBankSubject(String accountId, String bankId);
	
	/**
	 * 判断某个货品被引用的次数
	 * @param goodsId 货品ID
	 * @return
	 */
	@Query("select count(a) from FiscalAccountingSubject a where  relationType="+FiscalAccountingSubject.RELATION_GOODS+" and relationId=?1")
	public long countByGoods(String goodsId);

	/**
	 * 根据bankId查询科目id
	 */
	@Query("select a from FiscalAccountingSubject a where a.relationId=?1 order by a.code asc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccountingSubject queryFidByRelationId(String relationId);
}
