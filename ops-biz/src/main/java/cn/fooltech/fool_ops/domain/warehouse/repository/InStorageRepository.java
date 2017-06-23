package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.warehouse.entity.InStorage;

/**
 * 进仓Repository
 * @author xjh
 *
 */
public interface InStorageRepository extends JpaRepository<InStorage, String>, FoolJpaSpecificationExecutor<InStorage>{

	/**
	 * 根据单据明细ID查询
	 * @return
	 */
	@Query("select i from InStorage i where i.billDetail.fid=?1")
	public List<InStorage> findByBillDetailId(String detailId);
	
	/**
	 * 根据单据明细ID查询
	 * @return
	 */
	@Query("select i from InStorage i where i.billDetail.fid=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public InStorage findTopByBillDetailId(String detailId);
	
	/**
	 * 获取货品的入库记录<br>
	 * 且记录中的记账数量减累计出库数量大于零<br>
	 * @param goods 货品
	 * @param goodsSpec 货品属性
	 * @param fiscalAccountId 账套ID
	 * @return
	 */
	public default List<InStorage> findByGoods(Goods goods, GoodsSpec goodsSpec, String fiscalAccountId){
		return findAll(new Specification<InStorage>() {
			
			@Override
			public Predicate toPredicate(Root<InStorage> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), fiscalAccountId));
				predicates.add(builder.equal(root.<Goods>get("goods"), goods));
				if(goodsSpec!=null){
					predicates.add(builder.equal(root.<GoodsSpec>get("goodsSpec"), goodsSpec));
				}
				predicates.add(builder.greaterThan(root.get("accountQuentity"), root.get("totalOut")));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 获取入库记录
	 * @param billDetailId 单据明细ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public default InStorage findTopBy(String billDetailId, String goodsId, String goodsSpecId){
		return findTop(new Specification<InStorage>() {
			
			@Override
			public Predicate toPredicate(Root<InStorage> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("billDetail").get("fid"), billDetailId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				if(!Strings.isNullOrEmpty(goodsSpecId)){
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), goodsSpecId));
				}else{
					predicates.add(builder.isNull(root.get("goodsSpec")));
				}
				predicates.add(builder.greaterThan(root.get("accountQuentity"), root.get("totalOut")));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	@Query("select a from InStorage a where billDetail.fid=?1")
	public InStorage getByDeatil(String billDetailId);
	/**
	 * 检测仓库单据出库时，库存是否足够(不包括盘点单)
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	@Query(value="select a.FGOODS_ID, ifnull(a.FGOODS_SPEC_ID,'') from " +
		"(select t1.fgoods_id, t1.fgoods_spec_id, sum(abs(t1.faccount_quentity)) as quentity from tsb_warehouse_billdetail t1 left join tbd_goods t3 on " +
		"t1.fgoods_id = t3.fid where t1.fwarehouse_bill_id = ?3 and t3.faccount_flag ="+Goods.ACCOUNT_FLAG_YES+" group by t1.fgoods_id, t1.fgoods_spec_id) as a " +
		"left join " +
		"(select t2.fgoods_id, t2.fspec_id, round(sum(t2.faccount_quentity - t2.ftotal_out), 8) as quentity from tsb_storage_in t2 " + 
		"where t2.forg_id = ?1 and t2.facc_id = ?2 and t2.faccount_quentity - t2.ftotal_out > 0 group by t2.fgoods_id, t2.fspec_id) as b " +
		"on a.fgoods_id = b.fgoods_id and ifnull(a.fgoods_spec_id,'') = ifnull(b.fspec_id,'') where b.quentity is null or a.quentity > b.quentity ", nativeQuery=true)
	public List<Object[]> checkStock(String orgId, String fiscalAccountId, String billId);
	
	
	/**
	 * 针对盘点单，检测仓库单据出库时，库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	@Query(value="select a.FGOODS_ID, ifnull(a.FGOODS_SPEC_ID,'') from " +
		"(select t1.fgoods_id, t1.fgoods_spec_id, sum(abs(t1.faccount_quentity)) as quentity from tsb_warehouse_billdetail t1 left join tbd_goods t3 on " +
		"t1.fgoods_id = t3.fid where t1.fwarehouse_bill_id = ?3 and t1.faccount_quentity < 0 and t3.faccount_flag ="+Goods.ACCOUNT_FLAG_YES+" group by t1.fgoods_id, t1.fgoods_spec_id) as a " +
		"left join " +
		"(select t2.fgoods_id, t2.fspec_id, round(sum(t2.faccount_quentity - t2.ftotal_out), 8) as quentity from tsb_storage_in t2 " + 
		"where t2.forg_id = ?1 and t2.facc_id = ?2 and t2.faccount_quentity - t2.ftotal_out > 0 group by t2.fgoods_id, t2.fspec_id) as b " +
		"on a.fgoods_id = b.fgoods_id and ifnull(a.fgoods_spec_id,'') = ifnull(b.fspec_id,'') where b.quentity is null or a.quentity > b.quentity ", nativeQuery=true)
	public List<Object[]> checkStockByPdd(String orgId, String fiscalAccountId, String billId);
}
