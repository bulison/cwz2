package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.warehouse.entity.CashCheck;
import cn.fooltech.fool_ops.domain.warehouse.vo.CashCheckVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 现金盘点单 持久层
 * @author cwz
 *
 */
public interface CashCheckRepository extends JpaRepository<CashCheck, String>,JpaSpecificationExecutor<CashCheck>{

	
	/**
	 * 获取上一次盘点数
	 * @param orgId			机构id
	 * @param accountId     账套id
	 * @param subjectId		科目id	
	 * @return
	 */
	public default CashCheck queryLastNumber(String orgId,String accountId,String subjectId,Pageable pageable) {
		Page<CashCheck> page = findAll(new Specification<CashCheck>() {
			@Override
			public Predicate toPredicate(Root<CashCheck> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				if (!Strings.isNullOrEmpty(subjectId)) {
					predicates.add(builder.equal(root.get("subject").get("fid"), subjectId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		List<CashCheck> list = page.getContent();
		return list.get(0)!=null?list.get(0):null;
	}
	/**
	 * 现金科目盘点，一天只能一次
	 * 
	 * @param subjectId
	 * @param date
	 * @return
	 */
	@Query("select c from CashCheck c where subject.fid=?1 and date=?2")
	public CashCheck countByDate(String subjectId, Date date);
	
	/**
	 * 分页查询现金盘点单
	 * @param checkVo
	 * @param pageParamater
	 * @return
	 */
	public default Page<CashCheck> query(CashCheckVo checkVo,Pageable pageable){
		return findAll(new Specification<CashCheck>() {
			@Override
			public Predicate toPredicate(Root<CashCheck> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				List<Predicate> predicates = Lists.newArrayList();
				Organization org = SecurityUtil.getCurrentOrg();
				if (org != null) {
					predicates.add(builder.equal(root.get("org").get("fid"), org.getFid()));
				}
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
	}
}
