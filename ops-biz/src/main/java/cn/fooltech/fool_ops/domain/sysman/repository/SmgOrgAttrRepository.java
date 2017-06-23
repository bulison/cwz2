package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.sysman.entity.SmgOrgAttr;
import cn.fooltech.fool_ops.domain.sysman.vo.SmgOrgAttrVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface SmgOrgAttrRepository
		extends FoolJpaRepository<SmgOrgAttr, String>, FoolJpaSpecificationExecutor<SmgOrgAttr> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<SmgOrgAttr> findPageBy(SmgOrgAttrVo vo, Pageable pageable) {
		return findAll(new Specification<SmgOrgAttr>() {
			@Override
			public Predicate toPredicate(Root<SmgOrgAttr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));

				if (!Strings.isNullOrEmpty(vo.getFid())) {
					predicates.add(builder.equal(root.get("fid"), vo.getFid()));
				}

				if (!Strings.isNullOrEmpty(vo.getOrgId())) {
					predicates.add(builder.equal(root.get("org").get("fid"), vo.getOrgId()));
				}

				if (!Strings.isNullOrEmpty(vo.getKey())) {
					predicates.add(builder.equal(root.get("key"), vo.getKey()));
				}

				if (!Strings.isNullOrEmpty(vo.getValue())) {
					predicates.add(builder.equal(root.get("value"), vo.getValue()));
				}

				if (!Strings.isNullOrEmpty(vo.getDescribe())) {
					predicates.add(builder.like(root.get("describe"), "%"+vo.getDescribe()+"%"));
				}

				if (!Strings.isNullOrEmpty(vo.getName())) {
					predicates.add(builder.equal(root.get("name"), vo.getName()));
				}

				if (!Strings.isNullOrEmpty(vo.getRecordState())) {
					predicates.add(builder.equal(root.get("recordState"), vo.getRecordState()));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
	@Query("select a from SmgOrgAttr a where org.fid=?1 and key=?2 and recordState='SAC'")
	@QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
	public SmgOrgAttr queryByOrg(String orgId,String key);
	
}
