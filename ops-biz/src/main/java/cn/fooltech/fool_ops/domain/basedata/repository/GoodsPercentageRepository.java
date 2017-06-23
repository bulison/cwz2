package cn.fooltech.fool_ops.domain.basedata.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPercentage;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPercentageVo;

/**
 * 
 * <p>货品提成持久类</p>  
 * @author cwz
 * @date 2017年6月19日
 */
public interface GoodsPercentageRepository
		extends FoolJpaRepository<GoodsPercentage, String>, FoolJpaSpecificationExecutor<GoodsPercentage> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<GoodsPercentage> findPageBy(String accId,GoodsPercentageVo vo,Pageable pageable) {
		return findAll(new Specification<GoodsPercentage>() {
			@Override
			public Predicate toPredicate(Root<GoodsPercentage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
//				predicates.add(builder.equal(root.get("creatorId"), creatorId));

				if (!Strings.isNullOrEmpty(vo.getFid())) {
					predicates.add(builder.equal(root.get("id"), vo.getFid()));
				}

				if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
					predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
				}

				if (vo.getPercentage()!=null) {
					predicates.add(builder.equal(root.get("percentage"), vo.getPercentage()));
				}

				if (vo.getIsLast()!=null) {
					predicates.add(builder.equal(root.get("isLast"), vo.getIsLast()));
				}
				// 单据日期搜索
				if (vo.getStartDay() != null) {
					Date startDate = vo.getStartDay();
					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
				}
				if (vo.getEndDay() != null) {
					Date endDate = vo.getEndDay();
					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
	
    @Query("select g from GoodsPercentage g where g.goods.fid=?1 and percentage=?2 and org.fid=?3 and fiscalAccount.fid=?4")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsPercentage findTopBy(String goodsId,BigDecimal percentage,String orgId, String accId);
}
