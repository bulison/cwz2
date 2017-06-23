package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.sysman.entity.MupdateRecord;
import cn.fooltech.fool_ops.domain.sysman.vo.MupdateRecordVo;

public interface MupdateRecordRepository
		extends FoolJpaRepository<MupdateRecord, String>, FoolJpaSpecificationExecutor<MupdateRecord> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<MupdateRecord> findPageBy(MupdateRecordVo vo,Pageable pageable) {
		return findAll(new Specification<MupdateRecord>() {
			@Override
			public Predicate toPredicate(Root<MupdateRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();


				if (!Strings.isNullOrEmpty(vo.getDeviceType())) {
					predicates.add(builder.equal(root.get("deviceType"), vo.getDeviceType()));
				}

				if (!Strings.isNullOrEmpty(vo.getVersion())) {
					predicates.add(builder.equal(root.get("version"), vo.getVersion()));
				}

				if (!Strings.isNullOrEmpty(vo.getRemark())) {
					predicates.add(builder.equal(root.get("remark"), vo.getRemark()));
				}

				if (vo.getIsNeed()!=null) {
					predicates.add(builder.equal(root.get("isNeed"), vo.getIsNeed()));
				}

				if (!Strings.isNullOrEmpty(vo.getDownUrl())) {
					predicates.add(builder.equal(root.get("downUrl"), vo.getDownUrl()));
				}

				if (vo.getDownType()!=null) {
					predicates.add(builder.equal(root.get("downType"), vo.getDownType()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
}
