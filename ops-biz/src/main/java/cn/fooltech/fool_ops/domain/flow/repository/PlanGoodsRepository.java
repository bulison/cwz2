package cn.fooltech.fool_ops.domain.flow.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoods;
import cn.fooltech.fool_ops.domain.flow.vo.PlanGoodsVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface PlanGoodsRepository
		extends FoolJpaRepository<PlanGoods, String>, FoolJpaSpecificationExecutor<PlanGoods> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<PlanGoods> findPageBy(PlanGoodsVo vo, Pageable pageable) {
		return findAll(new Specification<PlanGoods>() {
			@Override
			public Predicate toPredicate(Root<PlanGoods> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
//				predicates.add(builder.equal(root.get("creatorId"), creatorId));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getPlanId())) {
					predicates.add(builder.equal(root.get("plan").get("fid"), vo.getPlanId()));
				}

				if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
					predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
				}

				if (!Strings.isNullOrEmpty(vo.getGoodsSpecId())) {
					predicates.add(builder.equal(root.get("goodsSpec").get("fid"), vo.getGoodsSpecId()));
				}

				if (vo.getPurchase()!=null) {
					predicates.add(builder.equal(root.get("purchase"), vo.getPurchase()));
				}

				// 单据日期搜索
//				if (vo.getStartDay() != null) {
//					Date startDate = vo.getStartDay();
//					predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
//				}
//				if (vo.getEndDay() != null) {
//					Date endDate = vo.getEndDay();
//					predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
//				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}

	/**
	 * 查找计划未完成的货品
	 * @return
	 */
	@Query("select p from PlanGoods p where p.plan.status not in(104,105) and p.purchase=?1")
	public List<PlanGoods> queryNotComplate(int purchase);
	
	@Query("select p from PlanGoods p where p.plan.id=?1")
	public List<PlanGoods> queryByPlanId(String planId);
}
