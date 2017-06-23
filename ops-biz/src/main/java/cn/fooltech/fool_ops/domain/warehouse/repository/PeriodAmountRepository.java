package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodStockAmount;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodAmountVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodAmount;

/**
 * 期间总库存金额Repository
 * @author xjh
 *
 */
public interface PeriodAmountRepository extends JpaRepository<PeriodAmount, String>, FoolJpaSpecificationExecutor<PeriodAmount>{

	
	/**
	 * 根据会计期间ID，货品ID，属性ID查询期间总金额
	 * @param periodId
	 * @param goodsId
	 * @param specId
	 * @return
	 */
	public default PeriodAmount findTopBy(String periodId, String goodsId, String specId){
		return findTop(new Specification<PeriodAmount>() {
			
			@Override
			public Predicate toPredicate(Root<PeriodAmount> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("stockPeriod").get("fid"), periodId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				
				if(StringUtils.isNotBlank(specId)){
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("goodsSpec")));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	/**
	 * 根据仓库id+货品id+货品属性id查询货品库存
	 * @return
	 */
	public default Page<PeriodAmount> queryBy(PeriodAmountVo vo, Pageable pageable){
		return findAll(new Specification<PeriodAmount>() {

			@Override
			public Predicate toPredicate(Root<PeriodAmount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("stockPeriod").get("fid"), vo.getPeriodId()));

				predicates.add(builder.not(builder.and(
						builder.equal(root.get("preQuentity"), BigDecimal.ZERO),
						builder.equal(root.get("inQuentity"), BigDecimal.ZERO),
						builder.equal(root.get("outQuentity"), BigDecimal.ZERO),
						builder.equal(root.get("profitQuentity"), BigDecimal.ZERO),
						builder.equal(root.get("accountQuentity"), BigDecimal.ZERO),
						builder.equal(root.get("purchaseQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("purchaseReturnQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("materialsQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("materialsReturnQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("productQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("productReturnQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("saleQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("saleReturnQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("lossQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("transportOutQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("transportInQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("moveOutQuantity"), BigDecimal.ZERO),
						builder.equal(root.get("moveInQuantity"), BigDecimal.ZERO)
				)));

				if (StringUtils.isNotBlank(vo.getGoodsId())) {
					Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
					List<String> goodIds = splitter.splitToList(vo.getGoodsId());
					if(goodIds.size()>0){
						predicates.add(root.get("goods").get("fid").in(goodIds));
					}
				}

				if (StringUtils.isNotBlank(vo.getGoodsSpecId())) {
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), vo.getGoodsSpecId()));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
				return predicate;
			}
		}, pageable);
	}
}