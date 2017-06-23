package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodStockAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;

/**
 * 期间分仓库存金额Repository
 * @author xjh
 *
 */
public interface PeriodStockAmountRepository extends JpaRepository<PeriodStockAmount, String>, 
	FoolJpaSpecificationExecutor<PeriodStockAmount>{

	/**
	 * 根据会计期间ID，货品ID，属性ID查询期间总金额
	 * @param periodId
	 * @param warehouseId
	 * @param goodsId
	 * @param specId
	 * @return
	 */
	public default PeriodStockAmount findTopBy(String periodId, String warehouseId, String goodsId, String specId){
		return findTop(new Specification<PeriodStockAmount>() {
			
			@Override
			public Predicate toPredicate(Root<PeriodStockAmount> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("stockPeriod").get("fid"), periodId));
				predicates.add(builder.equal(root.get("warehouse").get("fid"), warehouseId));
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
	public default List<PeriodStockAmount> findBy(WarehouseBill bill,WarehouseBillDetail detail){
		Sort sort = new Sort(Direction.DESC, "stockPeriod.startDate");
		
		return findAll(new Specification<PeriodStockAmount>() {
			@Override
			public Predicate toPredicate(Root<PeriodStockAmount> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.lessThan(root.get("stockPeriod").get("startDate"),bill.getBillDate()));
				predicates.add(builder.equal(root.get("goods").get("fid"), detail.getGoods().getFid()));
				if (bill.getBillType() == 21) {
					predicates.add(builder.equal(root.get("warehouse").get("fid"), detail.getOutWareHouse().getFid()));
				} else {
					predicates.add(builder.equal(root.get("warehouse").get("fid"), detail.getInWareHouse().getFid()));
				}
				if (detail.getGoodsSpec() != null) {
					
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), detail.getGoodsSpec().getFid()));
				} else {
					predicates.add(builder.isNull(root.get("goodsSpec").get("fid")));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
	}
	/**
	 * 根据仓库id+货品id查询货品库存
	 * @return
	 */
	@Query("select s from PeriodStockAmount s where warehouse.fid=?1 and goods.fid=?2 order by stockPeriod.period desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PeriodStockAmount queryLastStock(String warehouseId,String goodsId);
	/**
	 * 根据仓库id+货品id+货品属性id查询货品库存
	 * @return
	 */
	@Query("select s from PeriodStockAmount s where warehouse.fid=?1 and goods.fid=?2 and goodsSpec.fid=?3 order by stockPeriod.period desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PeriodStockAmount queryLastStock(String warehouseId,String goodsId,String goodsSpecId);


	/**
	 * 根据仓库id+货品id+货品属性id查询货品库存
	 * @return
	 */
	public default Page<PeriodStockAmount> queryBy(PeriodStockAmountVo vo, Pageable pageable){
		return findAll(new Specification<PeriodStockAmount>() {

			@Override
			public Predicate toPredicate(Root<PeriodStockAmount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
						builder.equal(root.get("transportInQuantity"), BigDecimal.ZERO)
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

				if (StringUtils.isNotBlank(vo.getWarehouseId())) {
					predicates.add(builder.equal(root.get("warehouse").get("fid"), vo.getWarehouseId()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
				return predicate;
			}
		}, pageable);
	}
}
