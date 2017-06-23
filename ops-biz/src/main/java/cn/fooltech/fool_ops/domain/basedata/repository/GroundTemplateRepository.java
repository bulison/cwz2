package cn.fooltech.fool_ops.domain.basedata.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplate;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public interface GroundTemplateRepository extends FoolJpaRepository<GroundTemplate, String>, FoolJpaSpecificationExecutor<GroundTemplate> {

    /**
     * 根据参数查找分页
     *
     * @return
     */
    public default List<GroundTemplate> findBy(String searchKey, String groundId, String accId) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");

        return findAll(new Specification<GroundTemplate>() {
            @Override
            public Predicate toPredicate(Root<GroundTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();

                predicateList.add(criteriaBuilder.equal(root.get("accId"), accId));
                if (!Strings.isNullOrEmpty(groundId)) {
                    predicateList.add(criteriaBuilder.equal(root.get("ground").get("fid"), groundId));
                }
                if (!Strings.isNullOrEmpty(searchKey)) {
                    predicateList.add(criteriaBuilder.like(root.get("ground").get("name"), PredicateUtils.getAnyLike(searchKey)));
                }
                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));

                return predicate;
            }
        }, sort);
    }
}
