package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.fooltech.fool_ops.config.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateRelation;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateRelationVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface PlanTemplateRelationRepository
		extends FoolJpaRepository<PlanTemplateRelation, String>, FoolJpaSpecificationExecutor<PlanTemplateRelation> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<PlanTemplateRelation> findPageBy(PlanTemplateRelationVo vo, Pageable pageable) {
		return findAll(new Specification<PlanTemplateRelation>() {
			@Override
			public Predicate toPredicate(Root<PlanTemplateRelation> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getPlanTemplateId())) {
					predicates.add(builder.equal(root.get("planTemplate").get("fid"), vo.getPlanTemplateId()));
				}

				if (!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())) {
					predicates.add(builder.equal(root.get("deliveryPlace").get("fid"), vo.getDeliveryPlaceId()));
				}

				if (!Strings.isNullOrEmpty(vo.getReceiptPlaceId())) {
					predicates.add(builder.equal(root.get("receiptPlace").get("fid"), vo.getReceiptPlaceId()));
				}

				if (!Strings.isNullOrEmpty(vo.getTransportTypeId())) {
					predicates.add(builder.equal(root.get("transportType").get("fid"), vo.getTransportTypeId()));
				}

				if (!Strings.isNullOrEmpty(vo.getShipmentTypeId())) {
					predicates.add(builder.equal(root.get("shipmentType").get("fid"), vo.getShipmentTypeId()));
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

	/**
	 * 根据发货地ID，收货地ID，运输方式ID，装运方式ID查找计划模板
	 * @param deliveryPlaceId
	 * @param receiptPlaceId
	 * @param transportTypeId
	 * @param shipmentTypeId
	 * @return
	 */
	@QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
	@Query("select p from PlanTemplateRelation p where p.deliveryPlace.fid=?1 and p.receiptPlace.fid=?2 and p.transportType.fid=?3 and p.shipmentType.fid=?4 and p.templateType=?5")
	public PlanTemplateRelation findBy(String deliveryPlaceId, String receiptPlaceId,
									   String transportTypeId, String shipmentTypeId, int templateType);

	/**
	 * 根据供应商ID或客户查找计划模板
	 * @param csvId
	 * @return
	 */
	@QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
	@Query("select p from PlanTemplateRelation p where p.csv.fid=?1 and p.templateType=?2")
	public PlanTemplateRelation findBy(String csvId, int templateType);

	/**
	 * 根据模板删除关联
	 * @param templateId
	 * @return
	 */
	@Query("delete from PlanTemplateRelation p where p.planTemplate.fid=?1")
	@Modifying
	public void deleteByTemplateId(String templateId);
}
