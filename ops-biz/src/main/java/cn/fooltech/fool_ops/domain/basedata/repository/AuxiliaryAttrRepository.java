package cn.fooltech.fool_ops.domain.basedata.repository;


import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
 * 辅助属性持久层
 *
 * @author cwz
 * @date 2016-10-24
 */
public interface AuxiliaryAttrRepository extends JpaRepository<AuxiliaryAttr, String>, JpaSpecificationExecutor<AuxiliaryAttr> {
    /**
     * 根据节点标识查询对象，flag==1按编码查询，否则按名称查询
     *
     * @param entity 辅助属性对象
     * @param flag   节点标识：1为子节点，0为父节点
     * @return
     */
    public default AuxiliaryAttr findRepeatByCategory(final AuxiliaryAttr entity, final int flag) {
        return findOne(new Specification<AuxiliaryAttr>() {

            @Override
            public Predicate toPredicate(Root<AuxiliaryAttr> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("category").get("fid"), entity.getCategory().getFid()));
                if (!Strings.isNullOrEmpty(entity.getFid())) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), entity.getFid()));
                }
                if (flag == 1) {
                    predicates.add(builder.equal(root.<String>get("code"), entity.getCode()));
                } else {
                    predicates.add(builder.equal(root.<String>get("name"), entity.getName()));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
    }

    @Query("select a from AuxiliaryAttr a where a.category.fid=?1")
    public List<AuxiliaryAttr> findRepeatByCategory(String categoryId);

    /**
     * 根据类别编号查询列表
     * @param categoryCode categoryCode
     * @return
     */
    @Query("select a from AuxiliaryAttr a where a.category.code=?1 and a.org.fid=?2 and (a.fiscalAccount is null or a.fiscalAccount.fid=?3)")
    public List<AuxiliaryAttr> findByCategoryCode(String categoryCode,  String orgId, String accId);

    /**
     * 根据类别名字查询列表
     * @param name name
     */
    @Query("select a from AuxiliaryAttr a where a.category.fid=?1 and a.org.fid=?2 and (a.fiscalAccount is null or a.fiscalAccount.fid=?3)")
    public List<AuxiliaryAttr> findByCategoryId(String fid, String orgId, String accId);

    /**
     * 根据肤质属性ID查找是否已经给使用
     * @param auxiliaryId
     * @return
     * @throws Exception
     */
//	@Procedure(name="f_auxiliary_is_use")
//	@Query("select f_auxiliary_is_use(?1)")
//	public String auxiliaryUse(String auxiliaryId);

    /**
     * 判断组内编号是否已存在
     *
     * @param orgId     机构ID
     * @param accountId 财务账套ID
     * @param typeCode  类别的编号
     * @param code      属性的编号
     * @param 父节点ID
     * @param excludeId 排除的ID
     * @return
     * @author cwz
     */
    public default boolean isCodeExist(final String orgId, final String accountId, final String typeCode, final String code, final String parentId, final String excludeId) {
        List<AuxiliaryAttr> findAll = findAll(new Specification<AuxiliaryAttr>() {
            @Override
            public Predicate toPredicate(Root<AuxiliaryAttr> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.<String>get("category").get("code"), typeCode));
                predicates.add(builder.equal(root.<String>get("code"), code));
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                if (StringUtils.isBlank(parentId)) {
                    predicates.add(builder.isNull(root.<String>get("parent").get("fid")));
                } else {
                    predicates.add(builder.equal(root.<String>get("parent").get("fid"), parentId));
                }
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
        if (CollectionUtils.isEmpty(findAll)) {
            return false;
        }
        return true;
    }

    /**
     * 根据父ID查找有效的子节点
     *
     * @param parentId
     * @return
     * @throws Excepiton
     */
    @Query("select o from AuxiliaryAttr o where org.fid=?1 and parent.fid=?2 and enable=1")
    public List<AuxiliaryAttr> findByParentAndEnable(String ordId, String parentId);


    /**
     * 根据分类ID查找辅助属性的根节点(忽略无效节点)
     */
    public default List<AuxiliaryAttr> findRootData(final String orgId) {
        return findAll(new Specification<AuxiliaryAttr>() {

            @Override
            public Predicate toPredicate(Root<AuxiliaryAttr> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                if (StringUtils.isNotBlank(orgId)) {
                    predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                }
                predicates.add(builder.isNull(root.<String>get("parent").get("fid")));
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
    }

    /**
     * 根据分类ID查找辅助属性的根节点(忽略无效节点)
     */
    @Query("select o from AuxiliaryAttr o where category.fid=?1 and org.fid=?2 and enable=1 and parent.fid is null order by code")
    public List<AuxiliaryAttr> findRootData(String catagoryId, String ordId);
    /**
     * 根据分类ID查找辅助属性的根节点(忽略无效节点,加入模糊匹配)
     */
    @Query("select o from AuxiliaryAttr o where category.fid=?1 and org.fid=?2 and enable=1 and o.name like %?3% and parent.fid is null order by code")
    public List<AuxiliaryAttr> fuzzyKeyFindRootData(String catagoryId, String ordId,String searchKey);
    /**
     * 根据父节点查询子节点
     *
     * @param parent
     * @param orgId
     * @param accountId
     * @return
     */
    public default List<AuxiliaryAttr> findByParent(final AuxiliaryAttr parent, final String orgId,
                                                    String accountId) {
        return findAll(new Specification<AuxiliaryAttr>() {

            @Override
            public Predicate toPredicate(Root<AuxiliaryAttr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("parent").get("fid"), parent.getFid()));
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.or(
                        builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId),
                        builder.isNull(root.<FiscalAccount>get("fiscalAccount"))
                ));
                predicates.add(builder.equal(root.<String>get("enable"), AuxiliaryAttr.STATUS_ENABLE));

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }

        }, new Sort(Direction.ASC, "code"));
    }

    /**
     * 递归获得父亲路径
     *
     * @param attr
     * @return
     */
    public default String getParentIds(AuxiliaryAttr attr, String parentIds) {
        AuxiliaryAttr parent = attr.getParent();//注意有数据的parentId为空白字符串
        if (parent != null) {
            if (Strings.isNullOrEmpty(parentIds)) {
                parentIds = parent.getFid();
            } else {
                Joiner joiner = Joiner.on(",").skipNulls();
                parentIds = joiner.join(parent.getFid(), parentIds);
            }

            return getParentIds(parent, parentIds);
        }
        return parentIds;
    }

    /**
     * 检索编号或者名称
     *
     * @param orgId     机构ID
     * @param typeCode  类别的编号
     * @param code      属性的编号
     * @param accountId 财务账套ID
     * @param name      名称
     * @return
     */
    public default List<AuxiliaryAttr> checkByCodeAndName(final String orgId, final String typeCode, final String code, final String accountId, final String name) {
        List<AuxiliaryAttr> findAll = findAll(new Specification<AuxiliaryAttr>() {
            @Override
            public Predicate toPredicate(Root<AuxiliaryAttr> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("category").get("code"), typeCode));
                predicates.add(builder.equal(root.<String>get("enable"), AuxiliaryAttr.STATUS_ENABLE));
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
                if (!Strings.isNullOrEmpty(code)) {
                    predicates.add(builder.equal(root.<String>get("code"), code));
                }
                if (!Strings.isNullOrEmpty(name)) {
                    predicates.add(builder.equal(root.<String>get("name"), name));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }

        });
        return findAll;
    }

    /**
     * 根据目录编号及属性编号查询属性（限制1个结果集）
     *
     * @param categoryCode
     * @param code
     * @param orgId
     * @return
     */
    @Query("select a from AuxiliaryAttr a where a.category.code=?1 and a.code=?2 and a.org.fid=?3")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public AuxiliaryAttr findTopByCode(String categoryCode, String code, String orgId);

    /**
     * 根据分类ID查找辅助属性的个数
     */
    @Query("select count(a) from AuxiliaryAttr a where a.category.fid=?1 and a.enable=1")
    public Long countByCategory(String catagoryId);

    /**
     * 根据编号查找
     *
     * @param ordId    机构id
     * @param typeCode 关联科目的code
     * @param code     科目code
     * @return
     */
    @Query("select a from AuxiliaryAttr a where org.fid=?1 and a.category.code=?2 and a.enable=1 and code=?3")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public AuxiliaryAttr getByCode(String ordId, String typeCode, String code);

    /**
     * 根据编号查找
     *
     * @param ordId    机构id
     * @param typeCode 关联科目的code
     * @param code     科目code
     * @return
     */
    @Query("select a from AuxiliaryAttr a where org.fid=?1 and a.category.code=?2 and a.enable=1 and a.code=?3 and a.fiscalAccount.fid=?4")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public AuxiliaryAttr getByCode(String ordId, String typeCode, String code, String accId);

    /**
     * 根据分类ID查找辅助属性的根节点(包含无效节点)
     */
    @Query("select a from AuxiliaryAttr a where category.fid=?1 and org.fid=?2 and parent.fid is null order by code")
    public List<AuxiliaryAttr> findAllRootData(String catagoryId, String ordId);
    /**
     * 根据分类ID查找辅助属性的根节点(包含无效节点)
     */
    @Query("select a from AuxiliaryAttr a where category.fid=?1 and org.fid=?2 order by code")
    public List<AuxiliaryAttr> findAllData(String catagoryId, String ordId);

    /**
     * 根据分类ID查找辅助属性的根节点(包含无效节点)
     * 返回page
     */
    @Query("select a from AuxiliaryAttr a where category.fid=?1 and org.fid=?2 and parent.fid is null order by code")
    public Page<AuxiliaryAttr> findPageAllRootData(String catagoryId, String ordId,Pageable page);
    /**
     * 根据机构ID、类别编码、级别查询
     *
     * @param orgId
     * @param categoryCode
     * @param level
     * @return
     */
    @Query("select a from AuxiliaryAttr a where a.category.code=?2 and a.org.fid=?1 and a.level=?3")
    public List<AuxiliaryAttr> findBy(String orgId, String categoryCode, Integer level);

    /**
     * 根据机构ID查找有效的叶子节点
     *
     * @param ordId
     * @param code
     * @return
     */
    @Query(value = "select o.* from tbd_auxiliary_attr o where o.fid in(select a.fid from tbd_auxiliary_attr as a "
            + " left join tbd_auxiliary_attr as b on a.fid=b.FPARENT_ID "
            + " left join tbd_auxiliary_attr_type as c on a.FCATEGORY_ID=c.fid  "
            + " where a.forg_id=?1 "
            + " and b.fid is null and c.fcode=?2 and a.enable=1)", nativeQuery = true)
    public List<AuxiliaryAttr> findAllLeafByOrgId(String ordId, String code);

    /**
     * 根据账套ID查找有效的叶子节点
     *
     * @param accId
     * @param code
     * @return
     */
    @Query(value = "select o.* from tbd_auxiliary_attr o where o.fid in(select a.fid from tbd_auxiliary_attr as a "
            + " left join tbd_auxiliary_attr as b on a.fid=b.FPARENT_ID "
            + " left join tbd_auxiliary_attr_type as c on a.FCATEGORY_ID=c.fid  "
            + " where a.facc_id=?1 "
            + " and b.fid is null and c.fcode=?2 and a.enable=1)", nativeQuery = true)
    public List<AuxiliaryAttr> findAllLeafByAccId(String accId, String code);
}
