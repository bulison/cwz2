package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface CustomerSupplierViewRepository extends JpaRepository<CustomerSupplierView, String>, JpaSpecificationExecutor<CustomerSupplierView> {

    /**
     * 根据参数查找分页
     *
     * @param orgId
     * @param categoryName
     * @param areaId
     * @param type
     * @param name
     * @param code
     * @param searchKey
     * @param pageRequest
     * @return
     */
    public default Page<CustomerSupplierView> findPageBy(String orgId, String categoryName, String areaId, Integer type, String name,
                                                         String code, String searchKey, Pageable page) {

        return findAll(new Specification<CustomerSupplierView>() {

            @Override
            public Predicate toPredicate(Root<CustomerSupplierView> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.get("recordStatus"), OpsEntity.STATUS_SAC));
                if (StringUtils.isNotBlank(searchKey)) {
                	String anyLike = PredicateUtils.getAnyLike(searchKey);
                    predicates.add(builder.or(
                            builder.like(root.get("code"),anyLike),
                            builder.like(root.get("name"),anyLike)
                    ));
                }
                if (StringUtils.isNotBlank(code)) {
                    predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(code)));
                }
                if (StringUtils.isNotBlank(name)) {
                    predicates.add(builder.like(root.get("name"), PredicateUtils.getAnyLike(name)));
                }

                if (type != null) {
                    predicates.add(builder.equal(root.get("type"), type));
                }
                if (StringUtils.isNotBlank(areaId)) {
                    predicates.add(builder.equal(root.get("area").get("fid"), areaId));
                }
                if (StringUtils.isNotBlank(categoryName)) {
                    predicates.add(builder.like(root.get("category").get("name"), PredicateUtils.getAnyLike(categoryName)));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);

    }


}
