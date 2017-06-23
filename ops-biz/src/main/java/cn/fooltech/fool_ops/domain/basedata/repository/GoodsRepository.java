package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * 货品Repository
 *
 * @author xjh
 */
public interface GoodsRepository extends JpaRepository<Goods, String>, FoolJpaSpecificationExecutor<Goods> {

    /**
     * 获取某个机构下，最顶级的货品(根节点)
     *
     * @param orgId 机构ID
     * @return
     */
    @Query("select g from Goods g where g.org.fid=?1 and g.parent.fid is null")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Goods findTopRootGoodsOfOrgId(String orgId);

    /**
     * 获取所有标识为货品的记录
     *
     * @param vo
     * @return
     */
    public default Page<Goods> findAllChildren(String goodsId, String orgId, String parentId, Integer showChild, Integer showDisable,
                                               String code, String name, String searchKey, String spec, List<String> excludeIds, Pageable page) {
        return findAll(new Specification<Goods>() {

            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.get("flag"), Goods.FLAG_GOODS));
                if (showDisable != null && showDisable == Constants.NOTSHOW) {
                    predicates.add(builder.notEqual(root.get("recordStatus"), Goods.STATUS_SNU));
                }
                if (StringUtils.isNotBlank(parentId)) {
                    if (showChild != null && showChild == Constants.SHOW) {
                        String key = PredicateUtils.getAnyLike(parentId);
                        predicates.add(builder.like(root.<String>get("fullParentId"), key));
                    } else {
                        predicates.add(builder.equal(root.<String>get("parent").get("fid"), parentId));
                    }
                }
                if (StringUtils.isNotBlank(name)) {
                    String key = PredicateUtils.getAnyLike(name);
                    predicates.add(builder.like(root.<String>get("name"), key));
                }
                if (StringUtils.isNotBlank(code)) {
                    String key = PredicateUtils.getAnyLike(code);
                    predicates.add(builder.like(root.<String>get("code"), key));
                }
                if (StringUtils.isNotBlank(goodsId)) {
                    predicates.add(builder.equal(root.<String>get("fid"), goodsId));
                }
                if (StringUtils.isNotBlank(searchKey)) {
                    String key = PredicateUtils.getAnyLike(searchKey);
                    predicates.add(builder.or(
                            builder.like(root.<String>get("code"), key),
                            builder.like(root.<String>get("name"), key)
                    ));
                }
                if (StringUtils.isNotBlank(spec)) {
                    String key = PredicateUtils.getAnyLike(spec);
                    predicates.add(builder.like(root.<String>get("spec"), key));
                }
                if (excludeIds != null && excludeIds.size() > 0) {
                    predicates.add(builder.not(root.get("fid").in(excludeIds)));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);
    }

    /**
     * 根据编号计算个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from Goods g where g.org.fid=?1 and g.code=?2 and g.flag=?3")
    public Long countByCode(String orgId, String code, int flag);

    /**
     * 根据编号计算个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from Goods g where g.org.fid=?1 and g.code=?2 and g.flag=?3 and g.fid!=?4")
    public Long countByCode(String orgId, String code, int flag, String excludeId);

    /**
     * 获取某张单据下的所有货品
     *
     * @param billId    仓库单据ID
     * @param paramater
     * @return
     */
    public default Page<Goods> findPageBy(String billId, String code, String name, String spec, Pageable page) {
        return findAll(new Specification<Goods>() {

            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                final Subquery<String> detailQuery = query.subquery(String.class);
                final Root<WarehouseBillDetail> detailRoot = detailQuery.from(WarehouseBillDetail.class);
                final Join<WarehouseBillDetail, Goods> goods = detailRoot.join("goods", JoinType.LEFT);

                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(detailRoot.<String>get("bill").get("fid"), billId));
                if (StringUtils.isNotBlank(name)) {
                    String key = PredicateUtils.getAnyLike(name);
                    predicates.add(builder.like(goods.<String>get("name"), key));
                }
                if (StringUtils.isNotBlank(code)) {
                    String key = PredicateUtils.getAnyLike(code);
                    predicates.add(builder.like(goods.<String>get("code"), key));
                }
                if (StringUtils.isNotBlank(spec)) {
                    String key = PredicateUtils.getAnyLike(spec);
                    predicates.add(builder.like(goods.<String>get("spec"), key));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));

                detailQuery.select(goods.<String>get("fid"));
                detailQuery.where(predicate);
                return builder.in(root.get("fid")).value(detailQuery);
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
    @Query("select g from Goods g where g.org.fid=?1 and g.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Goods findTopByCode(String orgId, String code);

    /**
     * 根据条码查询
     *
     * @param orgId
     * @param barCode
     * @return
     */
    @Query("select count(*) from Goods b where b.org.fid=?1 and b.barCode=?2")
    public Long countByBarCode(String orgId, String barCode);

    /**
     * 根据条码查询
     *
     * @param orgId
     * @param barCode
     * @param excludeId
     * @return
     */
    @Query("select count(*) from Goods b where b.org.fid=?1 and b.barCode=?2 and b.fid!=?3")
    public Long countByBarCode(String orgId, String barCode, String excludeId);

    /**
     * 根据父节点ID统计子节点个数
     *
     * @param goodsId
     * @return
     */
    @Query("select count(*) from Goods b where b.parent.fid=?1")
    public Long countByParentId(String goodsId);

    /**
     * 获取所有标识为货品的记录
     *
     * @param vo
     * @return
     */
    public default List<Goods> findByVagueSearch(String orgId, String inputType,
                                                 String searchKey, Integer limit, List<String> excludeIds) {
        return findAll(new Specification<Goods>() {

            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                predicates.add(builder.equal(root.<String>get("flag"), Goods.FLAG_GOODS));
                predicates.add(builder.equal(root.<String>get("recordStatus"), Goods.STATUS_SAC));

                if (!Strings.isNullOrEmpty(searchKey)) {

                    String likeKey = PredicateUtils.getAnyLike(searchKey.trim());

                    if (UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)) {
                        predicates.add(builder.or(
                                builder.like(root.<String>get("code"), likeKey),
                                builder.like(root.<String>get("name"), likeKey),
                                builder.like(root.<String>get("fivepen"), likeKey),
                                builder.like(root.<String>get("barCode"), likeKey)

                        ));
                    } else {
                        predicates.add(builder.or(
                                builder.like(root.<String>get("code"), likeKey),
                                builder.like(root.<String>get("name"), likeKey),
                                builder.like(root.<String>get("pinyin"), likeKey),
                                builder.like(root.<String>get("barCode"), likeKey)

                        ));
                    }
                }
                if (excludeIds != null && excludeIds.size() > 0) {
                    predicates.add(builder.not(root.get("fid").in(excludeIds)));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, limit);
    }

    /**
     * 根据条码获取货品信息
     *
     * @param orgId
     * @param barCode
     * @return
     */
    @Query("select g from Goods g where g.org.fid=?1 and g.barCode=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Goods findTopByBarCode(String orgId, String barCode);


    /**
     * 根据级别查找有效的货品
     *
     * @param orgId
     * @param level
     * @return
     */
    @Query("select g from Goods g where g.org.fid=?1 and g.level=?2 and g.recordStatus='" + Goods.STATUS_SAC+"'")
    public List<Goods> findByLevel(String orgId, Integer level);

    /**
     * 根据账套ID查找有效的叶子节点
     *
     * @param accId
     * @param code
     * @return
     */
    @Query(value = "select g.* from tbd_goods g where g.fid in (select a.fid from tbd_goods as a left join tbd_goods as b on a.fid=b.FPARENT_ID where b.fid is null and a.FFLAG=1 and a.RECORD_STATUS='SAC' and a.forg_id=?1)", nativeQuery = true)
    public List<Goods> findAllLeafByOrgId(String orgId);

    /**
     * 根据单位ID统计
     *
     * @param unitId
     * @return
     */
    @Query("select count(*) from Goods g where g.unit.fid=?1")
    public Long countByUnitId(String unitId);

    /**
     * 根据属性ID统计
     *
     * @param unitId
     * @return
     */
    @Query("select count(*) from Goods g where g.spec.fid=?1")
    public Long countByGoodsSpecId(String specId);

    /**
     * 根据货品名称统计
     *
     * @param goodsName
     * @param orgId
     * @return
     */
    @Query("select count(*) from Goods g where g.name=?1 and g.org.fid=?2")
    public Long countByGoodsName(String goodsName, String orgId);

    /**
     * 根据货品名称统计，排除excludeId
     *
     * @param goodsName
     * @param orgId
     * @param excludeId
     * @return
     */
    @Query("select count(*) from Goods g where g.name=?1 and g.org.fid=?2 and g.fid!=?3")
    public Long countByGoodsName(String goodsName, String orgId, String excludeId);
    /**
     * 根据货品ID进行查询
     */
    @Query("select g from Goods g where g.fid=?1 and g.org.fid=?2 ")
    public Goods findGoodsById(String goodsName, String orgId);
}
