package cn.fooltech.fool_ops.domain.basedata.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportLoss;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportLossVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 损耗地址持久化类
 * @author hjr
 * @date 2017/03/13
 */
public interface TransportLossRepository extends JpaRepository<TransportLoss,String>,FoolJpaSpecificationExecutor<TransportLoss> {
	/**
	 * 分页查询
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<TransportLoss> findPageBy(TransportLossVo vo,Pageable pageable){
		return findAll(new Specification<TransportLoss>(){
			@Override
			public Predicate toPredicate(Root<TransportLoss> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				 List<Predicate> predicateList = Lists.newArrayList();
			//	 predicateList.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				 String accId=SecurityUtil.getFiscalAccountId();
				 predicateList.add(builder.equal(root.get("fiscalAccount").get("fid"), accId)); 
				 String searchKey=vo.getSearchKey();
				 String goodsName=vo.getGoods();
				 String goodsSpec=vo.getGoodsSpec();
				 String delivery=vo.getDelivery();
				 String shipment=vo.getShipment();
				 String receipt=vo.getReceipt();
				 if (!Strings.isNullOrEmpty(goodsName)){
					 predicateList.add(builder.equal(root.get("goods").get("name"),goodsName));
				 }
				 if(!Strings.isNullOrEmpty(goodsSpec)){
					 predicateList.add(builder.equal(root.get("goodsSpec").get("name"),goodsSpec));
				 }
				 if(!Strings.isNullOrEmpty(delivery)){
					 predicateList.add(builder.equal(root.get("delivery").get("name"),delivery));
				 } 
				 if(!Strings.isNullOrEmpty(shipment)){
					 predicateList.add(builder.equal(root.get("shipment").get("name"),shipment));
				 }
				 if(!Strings.isNullOrEmpty(receipt)){
					 predicateList.add(builder.equal(root.get("receipt").get("name"),receipt));
				 }
				 if(!Strings.isNullOrEmpty(searchKey)){
					// 暂不知道有什么需要searchkey
					 predicateList.add(builder.or(
							 builder.like(root.get("goods").get("name"), "%"+searchKey+"%"))
							 );
				 }
				 Predicate predicate=builder.and(predicateList.toArray(new Predicate[]{}));
				 return predicate;
			}
		},pageable);
	}
	
	/**
	 * 根据成本分析表的字段查找出唯一的损耗地址(没有货品属性的情况)
	 */
	@Query("select t from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 and t.goodsSpec.fid is null")
	public TransportLoss findLossByCostBill(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId);
	/**
	 * 根据成本分析表的字段查找出唯一的损耗地址(有货品属性的情况)
	 */
	@Query("select t from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 and t.goodsSpec.fid=?6")
	public TransportLoss findLossByCostBill(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId,String goodsSpec);
	/**
	 * 判断是否有相同记录
	 */
	@Query("select t from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 and t.goodsSpec.fid=?6 and t.fid!=?7")
	public TransportLoss findLossByCostBillandId(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId,String goodsSpec,String fid);
	@Query("select t from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 and t.fid!=?6 and t.goodsSpec.fid is null")
	public TransportLoss findLossByCostBillandId(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId,String fid);
	/*	*//**
	 * 查找是否已经有了损耗地址(没有货品属性的情况)
	 *//*

	@Query("select count(*) from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 ")
	public Long countLossByCostBill(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId);
	*//**
	 * 根据成本分析表的字段查找出唯一的损耗地址(有货品属性的情况)
	 *//*
	@Query("select count(*) from TransportLoss t where t.fiscalAccount.fid=?1 and t.delivery.fid=?2 and t.receipt.fid=?3 and t.shipment.fid=?4 and t.goods.fid=?5 and t.goodsSpec.fid=?6 ")
	public Long countLossByCostBill(String accId,String deliveryId,String receiptId,String attrShipmentId,String goodsId,String goodsSpec);
*/
	
}
