package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerAddress;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.StringUtils;
import com.google.common.base.Strings;
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
import javax.persistence.criteria.*;
import java.util.List;

/**
 * 客户持久层
 *
 * @author cwz
 * @date 2016-10-28
 */
public interface CustomerRepository extends JpaRepository<Customer, String>, FoolJpaSpecificationExecutor<Customer> {


    /**
     * 分页查询
     * @param vo
     * @param orgId
     * @param pageable
     * @return
     */
    public default Page<Customer> query(CustomerVo vo, String orgId, Pageable pageable) {
        return findAll(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                // 客户编号
                if (StringUtils.isNotBlank(vo.getCode())) {
                    predicates.add(builder.like(root.<String>get("code"), "%" + vo.getCode() + "%"));
                    // detachedCriteria.add(Restrictions.like("code",vo.getCode(), MatchMode.ANYWHERE));
                }
                // 客户名称
                if (StringUtils.isNotBlank(vo.getName())) {
                    predicates.add(builder.like(root.<String>get("name"), "%" + vo.getName() + "%"));
                    // detachedCriteria.add(Restrictions.like("name",vo.getName(), MatchMode.ANYWHERE));
                }
                // 客户类别
                if (StringUtils.isNotBlank(vo.getCategoryId())) {
                    predicates.add(builder.equal(root.<String>get("category").get("fid"), vo.getCategoryId()));
                    // detachedCriteria.add(Restrictions.eq("category.fid",vo.getCategoryId()));
                }
                // 过滤有效数据
                if (vo.getShowDisable() != null && CustomerVo.NOT_SHOW == vo.getShowDisable()) {
                    predicates.add(builder.notEqual(root.<String>get("recordStatus"), Customer.STATUS_SNU));
                    // detachedCriteria.add(Restrictions.ne("recordStatus",Customer.STATUS_SNU));
                }
                // 搜索关键字
                String searchKey = vo.getSearchKey();
                if (StringUtils.isNotBlank(searchKey)) {
                    predicates.add(
                            builder.or(builder.like(root.<String>get("code"), "%" + searchKey + "%"),
                                    builder.like(root.<String>get("name"), "%" + searchKey + "%")));
                }
                if(vo.getOnlyDefaultAddress()!=null && vo.getOnlyDefaultAddress()==Constants.SHOW){
                    Subquery<Customer> subquery = query.subquery(Customer.class);
                    Root<CustomerAddress> fromProject = subquery.from(CustomerAddress.class);

                    subquery.select(fromProject.get("fid"));
                    subquery.where(builder.equal(fromProject.get("customerId"), root.get("fid")));
                    predicates.add(builder.exists(subquery));
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
        List<Customer> findAll = findAll(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("code"), code.trim()));
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
     * 检测客户名称是否存在
     *
     * @param orgId
     * @param name
     * @param excludeId
     * @return
     */
    public default Customer isNameExist(String orgId, String name, String excludeId) {
        Customer findOne = findOne(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
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
        return findOne;
    }

    /**
     * 检测客户简称是否存在
     *
     * @param orgId
     * @param shortName
     * @param excludeId
     * @return
     */
    public default Customer isShortNameExist(String orgId, String shortName, String excludeId) {
        Customer findOne = findOne(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("shortName"), shortName));
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
        return findOne;
    }

    /**
     * 模糊查询(根据客户编号、客户名称)
     *
     * @param orgId
     * @param searchKey
     * @param maxSize
     * @return
     */
    public default List<Customer> vagueSearch(String orgId, String userId, String searchKey,
                                              int maxSize, String recordStatus, String inputType) {
        Sort sort = new Sort(Direction.ASC, "code", "name");
        return findAll(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));

                if (Strings.isNullOrEmpty(recordStatus)) {
                    predicates.add(builder.equal(root.get("recordStatus"), Customer.STATUS_SAC));
                } else {
                    predicates.add(builder.equal(root.get("recordStatus"), recordStatus));
                }

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
    @Query("select b from Customer b where b.org.fid=?1")
    public List<Customer> findByOrgId(String orgId);


    /**
     * 根据编码获取数据
     *
     * @author xjh
     */
    @Query("select b from Customer b where b.org.fid=?1 and code=?2 and recordStatus!='" + Customer.STATUS_SNU + "'")
    public Customer getByCode(String orgId, String code);


    /**
     * 根据机构ID和编号查询
     *
     * @return
     */
    @Query("select b from Customer b where b.org.fid=?1 and b.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Customer findTopByCode(String orgId, String code);

}
