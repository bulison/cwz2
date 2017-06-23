package cn.fooltech.fool_ops.domain.analysis.repository;

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
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;

public interface CostAnalysisBilldetailRepository extends FoolJpaRepository<CostAnalysisBilldetail, String>,
		FoolJpaSpecificationExecutor<CostAnalysisBilldetail> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<CostAnalysisBilldetail> findPageBy(String accId, CostAnalysisBilldetailVo vo,
			Pageable pageable) {
		return findAll(new Specification<CostAnalysisBilldetail>() {
			@Override
			public Predicate toPredicate(Root<CostAnalysisBilldetail> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				if (!Strings.isNullOrEmpty(vo.getBillId())) {
					predicates.add(builder.equal(root.get("bill").get("id"),vo.getBillId()));
				}
				if (!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())) {
					predicates.add(builder.equal(root.get("deliveryPlace").get("fid"),vo.getDeliveryPlaceId()));
				}

				if (!Strings.isNullOrEmpty(vo.getReceiptPlaceId())) {
					predicates.add(builder.equal(root.get("receiptPlace").get("fid"), vo.getReceiptPlaceId()));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
	/**
	 * 查询其他运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		运输公司ID(关联供应商)
	 * @param pageable
	 * @return
	 */
	@Query("select a from CostAnalysisBilldetail a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid!=?6")
	public Page<CostAnalysisBilldetail> findOtherCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,Pageable pageable); 
	
	/**
	 * 查询运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		运输公司ID(关联供应商)
	 * @return
	 */
	@Query("select a from CostAnalysisBilldetail a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid=?6")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public CostAnalysisBilldetail findByCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId); 
	
	/**
	 * 根据单据ID查询明细
	 * @param billId 单据id
	 * @return
	 */
	@Query("select a from CostAnalysisBilldetail a where bill.id=?1")
	public List<CostAnalysisBilldetail> findByBillId(String billId);
	/**
	 * 根据单据ID查询明细,并根据fno从大到小排序
	 * @param billId 单据id
	 * @return	
	 */
	@Query("select a from CostAnalysisBilldetail a where bill.id=?1 order by a.no DESC")
	public List<CostAnalysisBilldetail> findByBillIdOrderFno(String billId);
}
