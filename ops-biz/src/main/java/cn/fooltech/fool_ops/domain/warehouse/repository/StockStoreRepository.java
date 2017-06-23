package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodStockAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.StockStore;

/**
 * 即时分仓库存Repository
 * @author xjh
 *
 */
public interface StockStoreRepository extends JpaRepository<StockStore, String>, JpaSpecificationExecutor<StockStore>{

	/**
	 * 根据账套ID查询
	 * @param accId
	 * @return
	 */
	@Query("select s from StockStore s where s.fiscalAccount.fid=?1")
	public List<StockStore> findByAccId(String accId);
	
	/**
	 * 查询即时库存列表
	 * @param warehouseId
	 * @param goodsId
	 * @param specId
	 * @return
	 */
	public default List<StockStore> findBy(String accId, String warehouseId, String goodsId, String specId){
		Sort sort = new Sort(Direction.ASC, "warehouse.name");
		return findAll(new Specification<StockStore>() {
			
			@Override
			public Predicate toPredicate(Root<StockStore> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				if(StringUtils.isNotBlank(warehouseId)){
					predicates.add(builder.equal(root.get("warehouse").get("fid"), warehouseId));
				}
				if(StringUtils.isNotBlank(goodsId)){
					predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				}
				if(StringUtils.isNotBlank(specId)){
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), specId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 查找库存
	 * @param fiscalAccountId
	 * @param warehouseId
	 * @param goodsId
	 * @return
	 */
	@Query("select s from StockStore s where s.fiscalAccount.fid=?1 and s.warehouse.fid=?2 and s.goods.fid=?3 and s.goodsSpec is null")
	public StockStore findTopBy(String fiscalAccountId, String warehouseId, String goodsId);
	
	/**
	 * 查找库存
	 * @param fiscalAccountId
	 * @param warehouseId
	 * @param goodsId
	 * @return
	 */
	@Query("select s from StockStore s where s.fiscalAccount.fid=?1 and s.warehouse.fid=?2 and s.goods.fid=?3 and s.goodsSpec.fid=?4")
	public StockStore findTopBy(String fiscalAccountId, String warehouseId, String goodsId, String specId);
	
	/**
	 * 检查即时分仓库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	@Query(value="select a.fgoods_id, ifnull(a.fgoods_spec_id, '') from " +
		"(select stock.fid as fwarehouse_id, detail.fgoods_id, detail.fgoods_spec_id, sum(abs(detail.faccount_quentity)) as quentity from tsb_warehouse_billdetail detail " +
		"left join tbd_goods goods on goods.fid = detail.fgoods_id left join tbd_auxiliary_attr stock on stock.fid = detail.fin_warehouse_id " +
		"where detail.fwarehouse_bill_id = ?3 and goods.faccount_flag = "+Goods.ACCOUNT_FLAG_YES+" group by detail.fin_warehouse_id, detail.fgoods_id, detail.fgoods_spec_id " +
		") as a left join (select stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id, stockStore.faccount_quentity as quentity " +
		"from tsb_stock_store stockStore where stockStore.forg_id = ?1 and stockStore.facc_id = ?2 group by stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id " +
		") as b on a.fgoods_id = b.fgoods_id and ifnull(a.fwarehouse_id,'') = ifnull(b.fwarehouse_id,'') and ifnull(a.fgoods_spec_id,'') = ifnull(b.fspec_id,'') " +
		"where b.quentity is null or a.quentity > b.quentity", nativeQuery=true)
	public List<Object[]> checkStock(String orgId, String fiscalAccountId, String billId);
	
	/**
	 * 针对调仓单，检查即时分仓库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	@Query(value="select a.fgoods_id, ifnull(a.fgoods_spec_id, '') from " +
		"(select stock.fid as fwarehouse_id, detail.fgoods_id, detail.fgoods_spec_id, sum(abs(detail.faccount_quentity)) as quentity from tsb_warehouse_billdetail detail " +
		"left join tbd_goods goods on goods.fid = detail.fgoods_id left join tbd_auxiliary_attr stock on stock.fid = detail.fout_warehouse_id " +
		"where detail.fwarehouse_bill_id = ? and goods.faccount_flag = "+Goods.ACCOUNT_FLAG_YES+" group by detail.fin_warehouse_id, detail.fgoods_id, detail.fgoods_spec_id " +
		") as a left join (select stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id, stockStore.faccount_quentity as quentity " +
		"from tsb_stock_store stockStore where stockStore.forg_id = ? and stockStore.facc_id = ? group by stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id " +
		") as b on a.fgoods_id = b.fgoods_id and ifnull(a.fwarehouse_id,'') = ifnull(b.fwarehouse_id,'') and ifnull(a.fgoods_spec_id,'') = ifnull(b.fspec_id,'') " +
		"where b.quentity is null or a.quentity > b.quentity", nativeQuery=true)
	public List<Object[]> checkStockByDcd(String orgId, String fiscalAccountId, String billId);
	
	/**
	 * 针对盘点单，检查即时分仓库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	@Query(value="select a.fgoods_id, ifnull(a.fgoods_spec_id, '') from " +
		"(select stock.fid as fwarehouse_id, detail.fgoods_id, detail.fgoods_spec_id, sum(abs(detail.faccount_quentity)) as quentity from tsb_warehouse_billdetail detail " +
		"left join tbd_goods goods on goods.fid = detail.fgoods_id left join tbd_auxiliary_attr stock on stock.fid = detail.fin_warehouse_id " +
		"where detail.fwarehouse_bill_id = ? and goods.faccount_flag = "+Goods.ACCOUNT_FLAG_YES+" and detail.faccount_quentity < 0 group by detail.fin_warehouse_id, detail.fgoods_id, detail.fgoods_spec_id " +
		") as a left join (select stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id, stockStore.faccount_quentity as quentity " +
		"from tsb_stock_store stockStore where stockStore.forg_id = ? and stockStore.facc_id = ? group by stockStore.fwarehouse_id, stockStore.fgoods_id, stockStore.fspec_id " +
		") as b on a.fgoods_id = b.fgoods_id and ifnull(a.fwarehouse_id,'') = ifnull(b.fwarehouse_id,'') and ifnull(a.fgoods_spec_id,'') = ifnull(b.fspec_id,'') " +
		"where b.quentity is null or a.quentity > b.quentity", nativeQuery=true)
	public List<Object[]> checkStockByPdd(String orgId, String fiscalAccountId, String billId);
	
	/**
	 * 根据仓库id+货品id查询即时分仓库存
	 * @return
	 */
	@Query("select s from StockStore s where fiscalAccount.fid=?1 and warehouse.fid=?2 and goods.fid=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockStore queryLastStock(String accId,String warehouseId,String goodsId);
	/**
	 * 根据仓库id+货品id+货品属性id查询即时分仓库存
	 * @return
	 */
	@Query("select s from StockStore s where fiscalAccount.fid=?1 and warehouse.fid=?2 and goods.fid=?3 and goodsSpec.fid=?4")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public StockStore queryLastStock(String accId,String warehouseId,String goodsId,String goodsSpecId);
}
