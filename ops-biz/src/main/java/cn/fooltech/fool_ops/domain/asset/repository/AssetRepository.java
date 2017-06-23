package cn.fooltech.fool_ops.domain.asset.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.vo.AssetVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 固定资产卡片  持久层
 * @author cwz
 *
 */
public interface AssetRepository extends JpaRepository<Asset, String>,JpaSpecificationExecutor<Asset> {
	
	public default Page<Asset> query(AssetVo assetVo,Pageable pageable){
		Page<Asset> findAll = findAll(new Specification<Asset>() {
			
			@Override
			public Predicate toPredicate(Root<Asset> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		return findAll;
	}
	/**
	 * 根据编号统计
	 * @param fiscalAccountId
	 * @param assetCode
	 * @param fid
	 * @return
	 */
	public default Long countByCode(String fiscalAccountId, String assetCode,String fid) {
		List<Asset> findAll = findAll(new Specification<Asset>() {

			@Override
			public Predicate toPredicate(Root<Asset> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), fiscalAccountId));
				predicates.add(builder.equal(root.get("assetCode"), assetCode));
				 if(StringUtils.isNotBlank(fid)){
					 predicates.add(builder.notEqual(root.get("fid"), fid));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		
		return findAll!=null&&findAll.size()>0?findAll.size():0L;
	}
	/**
	 * 根据状态查找
	 * @param statusList
	 * @return
	 */
	public default List<Asset> queryByStatus(String fiscalAccountId, List<Short> statusList) {
		List<Asset> findAll = findAll(new Specification<Asset>() {
			@Override
			public Predicate toPredicate(Root<Asset> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), fiscalAccountId));
				predicates.add(root.get("recordStatus").in(statusList));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return findAll;
	}
	
	/**
	 * 根据科目ID统计引用数
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from Asset where assetSubject.fid=?1 or depreciationSubject.fid=?1 or paymentSubject.fid=?1 or clearSubject.fid=?1 or expenseSubject.fid=?1")
	public Long countBySubjectId(String subjectId);
}
