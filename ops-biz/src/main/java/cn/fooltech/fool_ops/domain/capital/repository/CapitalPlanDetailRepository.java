
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
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 
 * @Description: 资金计划明细 持久层
 * @author cwz
 * @date 2017年3月1日 上午9:07:14
 */
public interface CapitalPlanDetailRepository
		extends FoolJpaRepository<CapitalPlanDetail, String>, FoolJpaSpecificationExecutor<CapitalPlanDetail> {

	/**
	 * 查找分页
	 * 
	 * @param accId
	 * @param vo
	 * @param pageable
	 * @return
	 */
	public default Page<CapitalPlanDetail> findPageBy(CapitalPlanDetailVo vo, Pageable pageable) {
		return findAll(new Specification<CapitalPlanDetail>() {
			@Override
			public Predicate toPredicate(Root<CapitalPlanDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {

				List<Predicate> predicates = Lists.newArrayList();

				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				
				
				if (!Strings.isNullOrEmpty(vo.getId())) {
					predicates.add(builder.equal(root.get("id"), vo.getId()));
				}
				if (vo.getCalculation()!=null) {
					predicates.add(builder.equal(root.get("capital").get("calculation"), vo.getCalculation()));
				}
				if (!Strings.isNullOrEmpty(vo.getCapitalId())) {
					predicates.add(builder.equal(root.get("capital").get("id"), vo.getCapitalId()));
				}
				if (!Strings.isNullOrEmpty(vo.getExplain())) {
					predicates.add(builder.like(root.get("explain"), PredicateUtils.getAnyLike(vo.getExplain())));
				}

				if (!Strings.isNullOrEmpty(vo.getRelationId())) {
					predicates.add(builder.equal(root.get("relationId"), vo.getRelationId()));
				}

				if (!Strings.isNullOrEmpty(vo.getRemark())) {
					predicates.add(builder.equal(root.get("remark"), vo.getRemark()));
				}
				if (!Strings.isNullOrEmpty(vo.getCode())) {
					predicates.add(builder.like(root.get("capital").get("code"), PredicateUtils.getAnyLike(vo.getCode())));
				}
				
				if (vo.getRecordStatus() != null) {
					predicates.add(builder.equal(root.get("recordStatus"), vo.getRecordStatus()));
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
	@Query("select c from CapitalPlanDetail c where id=?1 and org.fid=?2")
	public CapitalPlanDetail querybyfid(String id,String orgId);
	/**
	 * 根据计划id删除明细
	 * @param capitalId
	 */
	@Modifying
	@Query("delete from CapitalPlanDetail where capital.id=?1")
	public void delByCapitalId(String capitalId);
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param capitalId
	 * @param recordStatus
	 */
	@Modifying
	@Query("update from CapitalPlanDetail set updateTime=NOW(),recordStatus=?2 where capital.id=?1")
	public void changeByCapitalId(String capitalId,int recordStatus);
	
	/**
	 * 根据计划id查询明细
	 * @param capitalId
	 * @return
	 */
	@Query("select c from CapitalPlanDetail c where capital.id=?1 order by paymentDate ")
	public List<CapitalPlanDetail> queryByCapitalId(String capitalId);
	/**
	 * 根据关联id查询资金计划
	 * @param relationId	关联id
	 * @return
	 */
	@Query("select a from CapitalPlanDetail a where relationId=?1")
	public List<CapitalPlanDetail> queryByRelation(String relationId);
}

