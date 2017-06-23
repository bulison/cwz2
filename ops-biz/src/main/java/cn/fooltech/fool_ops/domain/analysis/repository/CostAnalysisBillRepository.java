package cn.fooltech.fool_ops.domain.analysis.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;

public interface CostAnalysisBillRepository
		extends FoolJpaRepository<CostAnalysisBill, String>, FoolJpaSpecificationExecutor<CostAnalysisBill> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<CostAnalysisBill> findPageBy(String accId, CostAnalysisBillVo vo,
			Pageable pageable) {
		return findAll(new Specification<CostAnalysisBill>() {
			@Override
			public Predicate toPredicate(Root<CostAnalysisBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
//				日期、货品、发货地、收货地；
//				Date date = new Date();
				//默认查询时间段为一个月
//				Date time = DateUtilTools.changeDateTime(date, -1, 0, 0, 0, 0);
				if (!Strings.isNullOrEmpty(vo.getBillDate())) {
					predicates.add(builder.equal(root.get("billDate"), DateUtils.getDateFromString(vo.getBillDate())));
				}
				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}
				if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
					predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
				}
				if (!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())) {
					predicates.add(builder.equal(root.get("deliveryPlace").get("fid"),vo.getDeliveryPlaceId()));
				}
				if (!Strings.isNullOrEmpty(vo.getRoute())) {
					predicates.add(builder.equal(root.get("route"),vo.getRoute()));
				}
				if (!Strings.isNullOrEmpty(vo.getGoodsSpecId())) {
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), vo.getGoodsSpecId()));
				}
				if (!Strings.isNullOrEmpty(vo.getReceiptPlaceId())) {
					predicates.add(builder.equal(root.get("receiptPlace").get("fid"), vo.getReceiptPlaceId()));
				}
				if (!Strings.isNullOrEmpty(vo.getSupplierId())) {
					predicates.add(builder.equal(root.get("supplier").get("fid"),vo.getSupplierId()));
				}
				if(vo.getPurchase()!=null){
					predicates.add(builder.equal(root.get("purchase"), vo.getPurchase()));
				}
				if(vo.getPublish()!=null){
					predicates.add(builder.equal(root.get("publish"), vo.getPublish()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}

	/**
	 * 根据发货地和局部查询线路
	 * @param receiptPlaceId
	 * @param billId
	 * @return
	 */
	@Query(nativeQuery = true, value = "select r1.* from tsb_cost_analysis_bill r1 where r1.FGOODS_ID=?4 and r1.FGOODS_SPEC_ID=?5 and r1.FRECEIPT_PLACE=?1 and r1.FBILL_DATE=?3 and exists (select r0.fid from tsb_cost_analysis_bill r0 where r0.fid=?2 and instr(r1.FROUTE, r0.FROUTE)=1)")
	public List<CostAnalysisBill> findByRoutes(String receiptPlaceId, String billId, String billDate, String goodsId, String specId);


	/**
	 * 根据发货地和局部查询线路
	 * @param receiptPlaceId
	 * @param billId
	 * @return
	 */
	@Query(nativeQuery = true, value = "select r1.* from tsb_cost_analysis_bill r1 where r1.FGOODS_ID=?4 and r1.FRECEIPT_PLACE=?1 and r1.FBILL_DATE=?3 and exists (select r0.fid from tsb_cost_analysis_bill r0 where r0.fid=?2 and instr(r1.FROUTE, r0.FROUTE)=1)")
	public List<CostAnalysisBill> findByRoutes(String receiptPlaceId, String billId, String billDate, String goodsId);

	/**
	 * 根据发货地ID收货地ID查询
	 * @param deliveryPlaceId
	 * @param receiptPlaceId
	 * @return
	 */
	@Query("select c from CostAnalysisBill c where c.deliveryPlace.fid=?1 and c.receiptPlace.fid=?2 and c.billDate=?3")
	public List<CostAnalysisBill> findByDeliveryAndRecept(String deliveryPlaceId, String receiptPlaceId, Date date);

	/**
	 * 折线图
	 * @param accId
	 * @param vo
	 * @return
	 */
	public default List<CostAnalysisBill> findChart(String accId, CostAnalysisBillVo vo) {
		Sort sort = new Sort(Sort.Direction.ASC, "billDate");
		return findAll(new Specification<CostAnalysisBill>() {
			@Override
			public Predicate toPredicate(Root<CostAnalysisBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}
                String startDay = vo.getStartDay();
                String endDay = vo.getEndDay();
                if (!Strings.isNullOrEmpty(startDay)) {
                	predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), DateUtils.getDateFromString(startDay)));
                }
                if (!Strings.isNullOrEmpty(endDay)) {
                	predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), DateUtils.getDateFromString(endDay)));
                }
				if (!Strings.isNullOrEmpty(vo.getRoute())) {
					predicates.add(builder.equal(root.get("route"),vo.getRoute()));
				}
                if (vo.getTotalPrice()!=null) {
                	predicates.add(builder.equal(root.get("totalPrice"), vo.getTotalPrice()));
                }
				if(vo.getPurchase()!=null){
					predicates.add(builder.equal(root.get("purchase"), vo.getPurchase()));
				}
				if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
					predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
				}
				if (!Strings.isNullOrEmpty(vo.getGoodsSpecId())) {
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), vo.getGoodsSpecId()));
				}
				if (!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())) {
					predicates.add(builder.equal(root.get("deliveryPlace").get("fid"),vo.getDeliveryPlaceId()));
				}
				if (!Strings.isNullOrEmpty(vo.getSupplierId())) {
					predicates.add(builder.equal(root.get("supplier").get("fid"),vo.getSupplierId()));
				}

				if (!Strings.isNullOrEmpty(vo.getReceiptPlaceId())) {
					predicates.add(builder.equal(root.get("receiptPlace").get("fid"), vo.getReceiptPlaceId()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
	}
	/**
	 * 发布
	 * @param fid
	 */
	@Query("update CostAnalysisBill c set publish=1 where id=?1")
	@Modifying
	public void issue(String fid);

	/**
	 * 根据采购标识和收货地删除
	 * @param purchase
	 * @param receiptPlaceId
	 * @param date >=date的数据
	 */
	@Query("delete from CostAnalysisBill c where c.purchase=?1 and c.receiptPlace.fid=?2 and c.billDate>=?3")
	@Modifying
	public void deleteByParam(Integer purchase, String receiptPlaceId, Date date);

}
