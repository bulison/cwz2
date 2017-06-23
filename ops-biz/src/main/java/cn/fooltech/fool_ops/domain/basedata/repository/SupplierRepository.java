package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.StringUtils;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 供应商持久层
 *
 * @author cwz
 * @date 2016-10-28
 */
public interface SupplierRepository extends JpaRepository<Supplier, String>, FoolJpaSpecificationExecutor<Supplier> {
    /**
     * 分页查询
     * @param vo
     * @param orgId
     * @param pageable
     * @return
     */
    public default Page<Supplier> query(SupplierVo vo, String orgId, Pageable pageable) {
        return findAll(new Specification<Supplier>() {

            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                // 过滤有效数据
                if (vo.getShowDisable() != null && SupplierVo.NOT_SHOW == vo.getShowDisable()) {
                    predicates.add(builder.notEqual(root.<String>get("recordStatus"), Customer.STATUS_SNU));
                }
                // 搜索关键字
                String searchKey = vo.getSearchKey();
                if (StringUtils.isNotBlank(searchKey)) {
                    searchKey = searchKey.trim();
                    predicates.add(builder.or(builder.like(root.<String>get("code"), "%" + searchKey + "%"),
                            builder.like(root.<String>get("name"), "%" + searchKey + "%")));
                }
                if (StringUtils.isNotBlank(vo.getFid())) {
                    predicates.add(builder.equal(root.<String>get("fid"), vo.getFid()));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);

    }

    /**
     * 判断在某个机构内，编号是否重复
     *
     * @param orgId     机构ID
     * @param code      编号
     * @param excludeId 排除实体的ID
     * @return
     */
    public default boolean isCodeExist(String orgId, String code, String excludeId) {
        List<Supplier> findAll = findAll(new Specification<Supplier>() {
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("code"), code));
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
        return findAll.size() > 0;
    }

    /**
     * 判断供应商名称是否已经存在
     *
     * @param orgId     机构ID
     * @param name      名称
     * @param excludeId 排除实体的ID
     * @return
     */
    public default boolean isNameExist(String orgId, String name, String excludeId) {
        List<Supplier> findAll = findAll(new Specification<Supplier>() {
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("name"), name));
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
        return findAll.size() > 0;
    }

    /**
     * 判断供应商公司简称是否已经存在
     *
     * @param orgId     机构ID
     * @param name      名称
     * @param excludeId 排除实体的ID
     * @return
     */
    public default boolean isShortNameExist(String orgId, String name, String excludeId) {
        List<Supplier> findAll = findAll(new Specification<Supplier>() {
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("shortName"), name));
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
        return findAll.size() > 0;
    }

    /**
     * 模糊查询(根据供应商编号、供应商名称)
     *
     * @param orgId
     * @param searchKey
     * @param maxSize
     * @return
     */
    public default List<Supplier> vagueSearch(String orgId, String userId, String searchKey,
                                              int maxSize, String inputType) {
        Sort sort = new Sort(Direction.ASC, "code", "name");
        return findAll(new Specification<Supplier>() {

            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.notEqual(root.<String>get("recordStatus"), Supplier.STATUS_SNU));

                if (StringUtils.isNotBlank(searchKey)) {
                    String likeKey = searchKey.trim();
                    likeKey = PredicateUtils.getAnyLike(likeKey);
                    if (UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)) {
                        predicates.add(builder.or(
                                builder.like(root.get("code"), likeKey),
                                builder.like(root.get("name"), likeKey),
                                builder.like(root.get("fivepen"), likeKey)
                        ));
                    } else {
                        predicates.add(builder.or(
                                builder.like(root.get("code"), likeKey),
                                builder.like(root.get("name"), likeKey),
                                builder.like(root.get("pinyin"), likeKey)
                        ));
                    }
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, sort, maxSize);
    }

    /**
     * 根据机构ID查询
     *
     * @return
     */
    @Query("select b from Supplier b where b.org.fid=?1")
    public List<Supplier> findByOrgId(String orgId);

    /**
     * 根据编码获取数据
     *
     * @author xjh
     */
    @Query("select b from Supplier b where b.org.fid=?1 and code=?2 and recordStatus !='SNU'")
    public Supplier getByCode(String orgId, String code);

    /**
     * 根据机构ID和编号查询
     *
     * @return
     */
    @Query("select b from Supplier b where b.org.fid=?1 and b.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Supplier findTopByCode(String orgId, String code);
}
