package cn.fooltech.fool_ops.domain.capital.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanBill;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 
 * @Description: 资金计划关联单据 持久层
 * @author cwz
 * @date 2017年3月1日 上午9:08:38
 */
public interface CapitalPlanBillRepository
		extends FoolJpaRepository<CapitalPlanBill, String>, FoolJpaSpecificationExecutor<CapitalPlanBill> {

	/**
	 * 统计某个单据类型下的单据数量
	 *
	 * @param orgId    机构ID
	 * @return
	 */
	@Query("select count(*) from CapitalPlanBill b where b.org.fid=?1")
	public Long countByBillType(String orgId);

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<CapitalPlanBill> findPageBy(CapitalPlanBillVo vo, Pageable pageable) {
		return findAll(new Specification<CapitalPlanBill>() {
			@Override
			public Predicate toPredicate(Root<CapitalPlanBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getDetailId())) {
					predicates.add(builder.equal(root.get("detail").get("id"), vo.getDetailId()));
				}

				if (vo.getBindType()!=null) {
					predicates.add(builder.equal(root.get("bindType"), vo.getBindType()));
				}
				//
				// if (!Strings.isNullOrEmpty(vo.getRelationSign())) {
				// predicates.add(builder.equal(root.get("relationSign"),
				// vo.getRelationSign()));
				// }

				if (!Strings.isNullOrEmpty(vo.getRelationId())) {
					predicates.add(builder.equal(root.get("relationId"), vo.getRelationId()));
				}
				//
				// if (!Strings.isNullOrEmpty(vo.getBillAmount())) {
				// predicates.add(builder.equal(root.get("billAmount"),
				// vo.getBillAmount()));
				// }

				// if (!Strings.isNullOrEmpty(vo.getBindAmount())) {
				// predicates.add(builder.equal(root.get("bindAmount"),
				// vo.getBindAmount()));
				// }

				// if (!Strings.isNullOrEmpty(vo.getCreateTime())) {
				// predicates.add(builder.equal(root.get("createTime"),
				// vo.getCreateTime()));
				// }

				if (!Strings.isNullOrEmpty(vo.getCreatorId())) {
					predicates.add(builder.equal(root.get("creatorId"), vo.getCreatorId()));
				}
				//
				// if (!Strings.isNullOrEmpty(vo.getUpdateTime())) {
				// predicates.add(builder.equal(root.get("updateTime"),
				// vo.getUpdateTime()));
				// }

				if (!Strings.isNullOrEmpty(vo.getOrgId())) {
					predicates.add(builder.equal(root.get("orgId"), vo.getOrgId()));
				}

				// if (!Strings.isNullOrEmpty(vo.getAccId())) {
				// predicates.add(builder.equal(root.get("accId"),
				// vo.getAccId()));
				// }

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
	 * 根据明细id查询关联单据
	 * @param detailId
	 * @return
	 */
	@Query("select a from CapitalPlanBill a where detail.id=?1")
	public List<CapitalPlanBill> queryByDetail(String detailId);
	
	/**
	 * 根据单据id查询关联单据
	 * @param relationId
	 * @return
	 */
	@Query("select a from CapitalPlanBill a where relationId=?1")
	public List<CapitalPlanBill> queryByRelationId(String relationId);
}
