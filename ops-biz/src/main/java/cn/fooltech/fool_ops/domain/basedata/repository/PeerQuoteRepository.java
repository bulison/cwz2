package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.PeerQuote;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface PeerQuoteRepository extends FoolJpaRepository<PeerQuote, String>,FoolJpaSpecificationExecutor<PeerQuote> {

    /**
     * 统计某个单据类型下的单据数量
     *
     * @param orgId    机构ID
     * @return
     */
    @Query("select count(*) from PeerQuote b where b.orgId=?1")
    public Long countByBillType(String orgId);

    /**
     * 查找分页
     * @param accId
     * @param vo
     * @param pageable
     * @return
     */
    public default Page<PeerQuote> findPageBy(String accId, String creatorId, PeerQuoteVo vo,
                                      Pageable pageable){
        return findAll(new Specification<PeerQuote>() {
            @Override
            public Predicate toPredicate(Root<PeerQuote> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.get("accId"), accId));
                //predicates.add(builder.equal(root.get("creatorId"), creatorId));

                if (!Strings.isNullOrEmpty(vo.getCustomerId())) {
                    predicates.add(builder.equal(root.get("customerId"), vo.getCustomerId()));
                }
                if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
                    predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
                }else if(!Strings.isNullOrEmpty(vo.getGoodsName())){
                    predicates.add(builder.like(root.get("goods").get("name"), PredicateUtils.getAnyLike(vo.getGoodsName())));
                }

                //单据日期搜索
                if(vo.getStartDay() != null){
                    Date startDate = vo.getStartDay();
                    predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
                }
                if(vo.getEndDay() != null){
                    Date endDate = vo.getEndDay();
                    predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }

    /**
     * 根据属性查找最后一次记录
     * @param goodsId
     * @param goodSpecId
     * @param unitId
     * @param receiptPlace
     * @return
     */
    public default PeerQuote findLastRecord(String accId, String creatorId,
                                            String goodsId, String goodSpecId,
                                            String unitId, String receiptPlace){
        Sort sort = new Sort( Sort.Direction.DESC, "billDate");
        return this.findTop(new Specification<PeerQuote>() {
            @Override
            public Predicate toPredicate(Root<PeerQuote> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.get("accId"), accId));
                predicates.add(builder.equal(root.get("creatorId"), creatorId));
                predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));

                if (!Strings.isNullOrEmpty(goodSpecId)) {
                    predicates.add(builder.equal(root.get("goodSpecId"), goodSpecId));
                }else{
                    predicates.add(builder.isNull(root.get("goodSpecId")));
                }

                if (!Strings.isNullOrEmpty(unitId)) {
                    predicates.add(builder.equal(root.get("unitId"), unitId));
                }

                if (!Strings.isNullOrEmpty(receiptPlace)) {
                    predicates.add(builder.equal(root.get("receiptPlace"), receiptPlace));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, sort);
    }
}
