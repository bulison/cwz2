package cn.fooltech.fool_ops.domain.basedata.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;

/**
 * 货品价格Repository
 *
 * @author xjh
 */
public interface GoodsPriceRepository extends JpaRepository<GoodsPrice, String>, FoolJpaSpecificationExecutor<GoodsPrice> {

    /**
     * 获取货品的最低销售价
     *
     * @param goodsId     货品ID
     * @param unitId      货品单位ID
     * @param goodsSpecId 货品属性ID
     * @return
     */
    @Query("select g from GoodsPrice g where g.goods.fid=?1 and g.unit.fid=?2 and g.goodsSpec.fid=?3")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsPrice findTopBy(String goodsId, String unitId, String goodsSpecId);

    /**
     * 获取货品的最低销售价
     *
     * @param goodsId 货品ID
     * @param unitId  货品单位ID
     * @return
     */
    @Query("select g from GoodsPrice g where g.goods.fid=?1 and g.unit.fid=?2 and g.goodsSpec is null")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsPrice findTopBy(String goodsId, String unitId);

    /**
     * 获取货品的最低销售价
     *
     * @param goodsId 货品ID
     * @param specId  货品属性ID
     * @return
     */
    @Query("select g from GoodsPrice g where g.goods.fid=?1 and g.goodsSpec.fid=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsPrice findTopByGoodsIdAndSpecId(String goodsId, String specId);

    /**
     * 模糊匹配货品名称和编号
     *
     * @param orgId
     * @param searchKey
     * @return
     */
    @Query("select g from GoodsPrice g where g.org.fid=?1 and (g.goods.code like %?2% or g.goods.name like %?2%)")
    public Page<GoodsPrice> findPageBy(String orgId, String searchKey, Pageable page);

    /**
     * 模糊匹配货品名称和编号
     *
     * @param orgId
     * @param page
     * @return
     */
    @Query("select g from GoodsPrice g where g.org.fid=?1")
    public Page<GoodsPrice> findPageBy(String orgId, Pageable page);

    /**
     * 根据货品和属性统计
     *
     * @param goodsId
     * @param specId
     * @param excludeId
     * @return
     */
    public default Long countByGoodsIdAndSpecId(String goodsId, String specId, String excludeId) {
        return count(new Specification<GoodsPrice>() {

            @Override
            public Predicate toPredicate(Root<GoodsPrice> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("goods").get("fid"), goodsId));

                if (StringUtils.isNotBlank(specId)) {
                    predicates.add(builder.equal(root.<String>get("goodsSpec").get("fid"), specId));
                }
                if (StringUtils.isNotBlank(excludeId)) {
                    predicates.add(builder.notEqual(root.<String>get("fid"), excludeId));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
    }

    /**
     * 根据货品和属性查询/获取货品定价
     *
     * @param goodsId
     * @param specId
     * @return
     */
    public default GoodsPrice findByGoodsIdAndSpecId(String goodsId, String specId) {
        return findTop(new Specification<GoodsPrice>() {

            @Override
            public Predicate toPredicate(Root<GoodsPrice> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));

                if (StringUtils.isNotBlank(specId)) {
                    predicates.add(builder.equal(root.get("goodsSpec").get("fid"), specId));
                } else {
                    predicates.add(builder.isNull(root.get("goodsSpec").get("fid")));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        });
    }

    /**
     * 根据单位ID统计
     *
     * @param unitId
     * @return
     */
    @Query("select count(*) from GoodsPrice p where p.unit.fid=?1")
    public Long countByUnitId(String unitId);

    /**
     * 根据属性IDID统计
     *
     * @param id
     * @return
     */
    @Query("select count(*) from GoodsPrice p where p.goodsSpec.fid=?1")
    public Long countByGoodsSpecId(String id);
}
