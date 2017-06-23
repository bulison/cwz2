package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 货品属性Repository
 *
 * @author xjh
 */
public interface GoodsSpecRepository extends JpaRepository<GoodsSpec, String>, JpaSpecificationExecutor<GoodsSpec> {

    /**
     * 根据编号查询个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.code=?2 and g.parent.fid is null")
    public Long countByCode(String orgId, String code);
    
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.code=?2 and fid !=?3")
    public Long countByCode(String orgId, String code,String fid);

    /**
     * 根据编号查询个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.code=?2 and g.parent.fid=?3 and fid !=?4")
    public Long countByCode(String orgId, String code, String excludeId,String fid);
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.code=?2 and g.parent.fid=?3")
    public Long countByCodeAndParent(String orgId, String code, String excludeId);

    /**
     * 根据名称查询个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.parent.fid=?2 and g.name=?3")
    public Long countByName(String orgId, String parentId, String name);

    /**
     * 根据名称查询个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from GoodsSpec g where g.org.fid=?1 and g.parent.fid=?2 and g.name=?3 and g.fid!=?4")
    public Long countByName(String orgId, String parentId, String name, String excludeId);


    /**
     * 根据父级和名称查询
     *
     * @param orgId
     * @param code
     * @return
     */
    public default List<GoodsSpec> findByParentIdAndName(String orgId, String parentId, String name) {
        return findAll(new Specification<GoodsSpec>() {

            @Override
            public Predicate toPredicate(Root<GoodsSpec> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                if (StringUtils.isNotBlank(parentId)) {
                    predicates.add(builder.equal(root.<String>get("parent").get("fid"), parentId));
                }
                // 搜索关键字
                String searchKey = name;
                if (StringUtils.isNotBlank(searchKey)) {
                    predicates.add(
                            builder.or(
                            		builder.like(root.<String>get("code"), "%" + searchKey + "%"),
                                    builder.like(root.<String>get("name"), "%" + searchKey + "%")));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
    }

    /**
     * 获取机构下全部有效的货品组
     *
     * @param orgId  机构ID
     * @param rootId 根节点ID
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.parent.fid=?2 and g.flag=" + GoodsSpec.FLAG_GROUP + " and g.recordStatus!='" + GoodsSpec.STATUS_SNU + "'")
    public List<GoodsSpec> findSpecGroups(String orgId, String rootId);


    /**
     * 获取某个机构下，最顶级的货品属性(根节点)
     *
     * @param orgId 机构ID
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.parent.fid is null")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsSpec findTopByRoot(String orgId);

    /**
     * 获取某个机构全部有效的叶子货品属性
     *
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.flag=" + GoodsSpec.FLAG_SPEC + " and g.recordStatus!='" +GoodsSpec.STATUS_SNU+"'")
    public List<GoodsSpec> getLeafSpec(String orgId);

    /**
     * 模糊搜索货品属性(根据编号、名称)
     *
     * @param vo
     * @return
     */
    public default Page<GoodsSpec> findPageByVagueSearch(String orgId, String groupId, String searchKey, Pageable page) {
        return findAll(new Specification<GoodsSpec>() {

            @Override
            public Predicate toPredicate(Root<GoodsSpec> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("parent").get("fid"), groupId));
                if (StringUtils.isNotBlank(searchKey)) {
                    String key = PredicateUtils.getAnyLike(searchKey);
                    predicates.add(builder.or(
                            builder.like(root.<String>get("code"), key),
                            builder.like(root.<String>get("name"), key)
                    ));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);
    }

    /**
     * 模糊搜索货品属性(根据名称,只查有效的货品属性)
     *
     * @param vo
     * @return
     */
    public default Page<GoodsSpec> findPageByName(String orgId, String groupId,
                                                  String name, Pageable page) {
        return findAll(new Specification<GoodsSpec>() {

            @Override
            public Predicate toPredicate(Root<GoodsSpec> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("parent").get("fid"), groupId));
                predicates.add(builder.equal(root.<String>get("flag"), GoodsSpec.FLAG_SPEC));
                predicates.add(builder.equal(root.<String>get("recordStatus"), GoodsSpec.STATUS_SNU));
                if (StringUtils.isNotBlank(name)) {
                    String key = PredicateUtils.getAnyLike(name);
                    predicates.add(builder.like(root.<String>get("name"), key));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);
    }

    /**
     * 根据编号查找
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsSpec findTopByCode(String orgId, String code);
    /**
     * 根据编号查找
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.code=?2 and g.flag !=" + GoodsSpec.FLAG_GROUP )
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsSpec findTopByParentIsNotNullCode(String orgId, String code);
    /**
     * 根据编号和父属性ID查找
     * @param orgId
     * @param code
     * @param parentId
     * @return
     */
    @Query("select g from GoodsSpec g where g.org.fid=?1 and g.code=?2 and g.parent.fid=?3 ")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsSpec findTopByCodeAndSpecGroupId(String orgId, String code,String parentId);
}
