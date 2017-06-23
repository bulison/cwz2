package cn.fooltech.fool_ops.domain.capital.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 资金计划 持久层
 * 
 * @Description:
 * @author cwz
 * @date 2017年3月1日 上午9:06:30
 */
public interface CapitalPlanRepository
		extends FoolJpaRepository<CapitalPlan, String>, FoolJpaSpecificationExecutor<CapitalPlan> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<CapitalPlan> findPageBy(CapitalPlanVo vo, Pageable pageable) {
		return findAll(new Specification<CapitalPlan>() {
			@Override
			public Predicate toPredicate(Root<CapitalPlan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));

				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}

				if (!Strings.isNullOrEmpty(vo.getCode())) {
					predicates.add(builder.equal(root.get("code"), vo.getCode()));
				}

				if (!Strings.isNullOrEmpty(vo.getExplain())) {
					predicates.add(builder.equal(root.get("explain"), vo.getExplain()));
				}

				if (!Strings.isNullOrEmpty(vo.getRelationId())) {
					predicates.add(builder.equal(root.get("relationId"), vo.getRelationId()));
				}
				if (!Strings.isNullOrEmpty(vo.getRemark())) {
					predicates.add(builder.equal(root.get("remark"), vo.getRemark()));
				}
				// 单据日期搜索
				if (vo.getStartDay() != null) {
					Date startDate = vo.getStartDay();
					predicates.add(builder.greaterThanOrEqualTo(root.get("paymentDate"), startDate));
				}
				if (vo.getEndDay() != null) {
					Date endDate = vo.getEndDay();
					predicates.add(builder.lessThanOrEqualTo(root.get("paymentDate"), endDate));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
	}
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param capitalId
	 * @param recordStatus
	 */
	@Modifying
	@Query("update from CapitalPlan set updateTime=NOW(),recordStatus=?2 where id=?1")
	public void changeByCapitalId(String id,int recordStatus);
	
	
	/**
	 * 根据关联id查询资金计划
	 * @param relationId	关联id
	 * @return
	 */
	@Query("select a from CapitalPlan a where relationId=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public CapitalPlan queryByRelation(String relationId);
	/**
	 * 根据关联id查询资金计划
	 * @param relationId	关联id
	 * @return
	 */
	@Query("select a from CapitalPlan a where relationId=?1")
	public List<CapitalPlan> queryByWarehouseId(String relationId);
}
