package cn.fooltech.fool_ops.domain.warehouse.repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.*;

import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;

import static cn.fooltech.fool_ops.domain.flow.entity.Task.*;

/**
 * 仓储单据明细Repository
 * @author xjh
 *
 */
public interface WarehouseBillDetailRepository extends FoolJpaRepository<WarehouseBillDetail, String>,
	FoolJpaSpecificationExecutor<WarehouseBillDetail>{

	/**
	 * 获取单据明细
	 * @param billTypes 仓库单据类型
	 * @param statusAudited 仓库单据状态
	 * @param customerId 客户ID
	 * @param supplierId 供应商ID
	 * @param goodsId 货品ID
	 * @param unitId 货品单位ID
	 * @param goodsSpecId 货品属性ID 
	 * @return
	 */
	public default WarehouseBillDetail findTopBy(List<Integer> billTypes, int statusAudited, String customerId, String supplierId,
			String goodsSpecId, String goodsId, String unitId){
		
		Sort sort = new Sort(Direction.DESC, "bill.billDate");
		
		return findTop(new Specification<WarehouseBillDetail>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseBillDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(root.<Integer>get("bill").get("billType").in(billTypes));
				predicates.add(builder.equal(root.get("bill").get("recordStatus"), statusAudited));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				
				//客户
				if(StringUtils.isNotBlank(customerId)){
					predicates.add(builder.equal(root.get("bill").get("customer").get("fid"), customerId));
				}
				//供应商
				if(StringUtils.isNotBlank(supplierId)){
					predicates.add(builder.equal(root.get("bill").get("supplier").get("fid"), supplierId));
				}
				//货品属性
				if(StringUtils.isBlank(goodsSpecId)){
					predicates.add(builder.isNull(root.get("goodsSpec")));
				}else{
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), goodsSpecId));
				}
				if(StringUtils.isNotBlank(unitId)){
					predicates.add(builder.equal(root.get("unit").get("fid"), unitId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	/**
	 * 根据仓储单据ID查询明细
	 * @param billId
	 * @return
	 */
	@Query("select d from WarehouseBillDetail d where d.bill.fid=?1")
	public List<WarehouseBillDetail> findByBillId(String billId);

	/**
	 * 根据仓储单据IDs查询明细
	 * @param billIds
	 * @return
	 */
	@Query("select d from WarehouseBillDetail d where d.bill.fid in ?1")
	public List<WarehouseBillDetail> findByBillIds(List<String> billIds);
	
	/**
	 * 获取单据的成本金额
	 * @param billId 单据ID
	 * @return
	 */
	@Query("select sum(d.type) from WarehouseBillDetail d where d.bill.fid=?1")
	public BigDecimal sumBillInCostAmount(String billId);
	
	/**
	 * 获取税额
	 * @param billId 单据ID
	 * @return
	 */
	@Query("select sum(d.taxAmount) from WarehouseBillDetail d where d.bill.fid=?1")
	public BigDecimal sumTaxAmount(String billId);
	
	/**
	 * 获取含税金额
	 * @param billId 单据ID
	 * @return
	 */
	@Query("select sum(d.totalAmount) from WarehouseBillDetail d where d.bill.fid=?1")
	public BigDecimal sumTotalAmount(String billId);
	
	/**
	 * 通过分组查询，获取仓库单据明细信息
	 * @param billId 单据ID
	 * @param groupType 分组查询类型，0 货品   1 仓库   2  货品+仓库
	 * @param amountSource 金额来源: 1-单据金额（不含税金额）,2-成本金额, 3--税额, 4-含税金额, 5-利润
	 * @return 明细ID、金额
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public default List<Object[]> getDetailsByGroup(String billId, int groupType, int amountSource){

		String sql = "select fid, case :amountSource " +
		"when 1 then sum(ifnull(faccount_amount, 0)) " +
		"when 2 then sum(ifnull(fcost_price, 0)) " +
		"when 3 then sum(ifnull(ftax_amount, 0)) " +
		"when 4 then sum(ifnull(ftotal_amount, 0)) " +
		"when 5 then sum(ifnull(faccount_amount, 0)) - sum(ifnull(fcost_price, 0)) else 0 end " +    
		"from tsb_warehouse_billdetail where fwarehouse_bill_id = :billId ";
		
		switch(groupType){
			 case 0:
				 sql = sql + "group by fgoods_id";
				 break;
			 case 1:
				 sql = sql + "group by fin_warehouse_id";
				 break;
			 case 2:
				 sql = sql + "group by fgoods_id, fin_warehouse_id";
				 break;
			default:
				break;
		}
		
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("billId", billId);
		query.setParameter("amountSource", amountSource);
		List datas = query.getResultList();
		if(datas != null){
			return (List<Object[]>) datas;
		}
		return Collections.emptyList();
	}
	
	/**
	 * 根据单位ID统计
	 * @param unitId
	 * @return
	 */
	@Query("select count(*) from WarehouseBillDetail d where d.accountUint.fid=?1 or d.unit.fid=?1")
	public Long countByUnitId(String unitId);

	/**
	 * 根据属性ID统计
	 * @param specId
	 * @return
	 */
	@Query("select count(*) from WarehouseBillDetail d where d.goodsSpec.fid=?1")
	public Long countByGoodsSpecId(String specId);


	/**
	 * 根据仓库ID、货品ID，属性ID查询仓储单据明细记录
	 * @param billType
	 * @return
	 */
	@Query(value = "select b.* from tsb_warehouse_billdetail d left join tsb_warehouse_bill b on d.FWAREHOUSE_BILL_ID=d.FID where b.RECORD_STATUS=1 and b.FBILL_TYPE=?1 and exists (select count(*) from tflow_task_bill tb left join tflow_task t on tb.ftask_id=t.fid where tb.FBILL_ID=d.FWAREHOUSE_BILL_ID and t.FSTATUS in (1,2,4,5) limit 1)" ,nativeQuery = true)
	public List<WarehouseBillDetail> findNotCompleteBy(int billType);
	
	@Query("select d from WarehouseBillDetail d where d.inWareHouse.fid=?1 and  d.goods.fid=?2")
	public List<WarehouseBillDetail> findGoodsAndWarehouse(String inWareHouseId,String goodsId);
	
	@Query("select d from WarehouseBillDetail d where d.inWareHouse.fid=?1 and  d.goods.fid=?2 and d.goodsSpec.fid=?3")
	public List<WarehouseBillDetail> findGoodsAndWarehouse(String inWareHouseId,String goodsId,String goodsSpecId);
	/**
	 * 获取关联的单据ID的成本单价
	 */
	@Query("select sum(d.costPrice) from WarehouseBillDetail d where d.fid=?1")
	public BigDecimal findDetailbyDetailId(String refDetailId);
	
	
	@Query(nativeQuery=true,value="select b.* from tflow_task_bill t "
			+ " LEFT JOIN tflow_plan d on d.FID=t.FPLAN_ID "
			+ " LEFT JOIN tflow_plan_goods e on e.FPLAN_ID=d.FID  "
			+ " LEFT JOIN tsb_warehouse_bill a on  a.fid=t.FBILL_ID "
			+ " LEFT JOIN tsb_warehouse_billdetail b on a.FID=b.FWAREHOUSE_BILL_ID "
			+ " LEFT JOIN tbd_freight_address c on c.FID=a.FDELIVERY_PLACE "
			+ " where t.FPLAN_ID=?1 "
			+ " and t.FBILL_SIGN in('11','12','22','23','24','41','42','20') "
			+ " and a.RECORD_STATUS=1 "
			+ " and b.FIN_WAREHOUSE_ID=?2 "
			+ " and b.FGOODS_ID=?3 GROUP BY a.FCODE")
	public List<WarehouseBillDetail> findGoodsAndWarehouseByPlanGoods(String planId,String inWareHouseId,String goodsId);
	
	@Query(nativeQuery=true,value="select b.* from tflow_task_bill t "
			+ " LEFT JOIN tflow_plan d on d.FID=t.FPLAN_ID "
			+ " LEFT JOIN tflow_plan_goods e on e.FPLAN_ID=d.FID  "
			+ " LEFT JOIN tsb_warehouse_bill a on  a.fid=t.FBILL_ID "
			+ " LEFT JOIN tsb_warehouse_billdetail b on a.FID=b.FWAREHOUSE_BILL_ID "
			+ " LEFT JOIN tbd_freight_address c on c.FID=a.FDELIVERY_PLACE "
			+ " where t.FPLAN_ID=?1 "
			+ " and t.FBILL_SIGN in('11','12','22','23','24','41','42','20') "
			+ " and a.RECORD_STATUS=1 "
			+ " and b.FIN_WAREHOUSE_ID=?2 "
			+ " and b.FGOODS_ID=?3 "
			+ " and b.FGOODS_SPEC_ID=?4 GROUP BY a.FCODE,b.FGOODS_ID,b.FGOODS_SPEC_ID,b.FIN_WAREHOUSE_ID")
	public List<WarehouseBillDetail> findGoodsAndWarehouseByPlanGoods(String planId,String inWareHouseId,String goodsId,String goodsSpecId);
	
	
	
	@Query(nativeQuery=true,value="select b.* from tflow_task_bill t "
			+ " LEFT JOIN tflow_plan d on d.FID=t.FPLAN_ID "
			+ " LEFT JOIN tflow_plan_goods e on e.FPLAN_ID=d.FID  "
			+ " LEFT JOIN tsb_warehouse_bill a on  a.fid=t.FBILL_ID "
			+ " LEFT JOIN tsb_warehouse_billdetail b on a.FID=b.FWAREHOUSE_BILL_ID "
			+ " LEFT JOIN tbd_freight_address c on c.FID=a.FDELIVERY_PLACE "
			+ " where t.FPLAN_ID=?1 "
			+ " and t.FBILL_SIGN in('11','12','22','23','24','41','42','20') "
			+ " and a.RECORD_STATUS=1 GROUP BY a.FCODE,b.FGOODS_ID,b.FGOODS_SPEC_ID,b.FIN_WAREHOUSE_ID ")
	public List<WarehouseBillDetail> findGoodsByPlan(String planId);
	/**
	 * 根据用户获取单据明细
	 * @param memberId
	 * @return
	 */
	@Query("select d from WarehouseBillDetail d where d.bill.inMember.fid=?1 and d.org.fid=?2 and d.fiscalAccount.fid=?3 and (d.bill.billType=42 or d.bill.billType=41)")
	public List<WarehouseBillDetail> getPercentageDetailsByMemberId(String memberId,String orgId,String accId);
	
}
