package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.TaskTemplate;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
public interface PlanTemplateRepository extends JpaRepository<PlanTemplate, String>, FoolJpaSpecificationExecutor<PlanTemplate> {

	/**
	 * 查询计划模板列表信息，按照计划模板主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<PlanTemplate> query(PlanTemplateVo vo,Pageable pageable){
		Page<PlanTemplate> findAll = findAll(new Specification<PlanTemplate>() {
			
			@Override
			public Predicate toPredicate(Root<PlanTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				String searchKey = vo.getSearchKey();
				if(!Strings.isNullOrEmpty(searchKey)){
					String key = PredicateUtils.getAnyLike(searchKey);
					predicates.add(builder.or(
							builder.like(root.get("name"), key ),
							builder.like(root.get("code"), key),
							builder.like(root.get("describe"), key)
							));
				}
				if(vo.getStatus()!=null){
					predicates.add(builder.equal(root.get("status"), vo.getStatus()));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	@Query("select a from PlanTemplate a where status=1 and org.fid=?1 ")
	public List<PlanTemplate> queryAll(String orgId,Sort sort);
	/**
	 * 计算同一企业下相同的编号数据个数
	 * @param selfId
	 * @param orgId
	 * @param code
	 * @return
	 */
	public default Long countSameCode(String selfId, String orgId, String code) {
		long count = this.count(new Specification<PlanTemplate>() {
			public Predicate toPredicate(Root<PlanTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if(!Strings.isNullOrEmpty(selfId)){
					predicates.add(builder.notEqual(root.get("fid"), selfId));
				}
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("code"), code));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return count;
	}
	/**
	 * 计算统一企业下相同的名称的数据个数
	 * @param selfId
	 * @param orgId
	 * @param name
	 * @return
	 */
	public default Long countSameName(String selfId, String orgId, String name) {
		long count = this.count(new Specification<PlanTemplate>() {
			public Predicate toPredicate(Root<PlanTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if(!Strings.isNullOrEmpty(selfId)){
					predicates.add(builder.notEqual(root.get("fid"), selfId));
				}
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("name"), name));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return count;
	}
	
}	

