package cn.fooltech.fool_ops.domain.flow.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.TaskTemplate;
import cn.fooltech.fool_ops.domain.flow.vo.TaskTemplateVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, String>, FoolJpaSpecificationExecutor<TaskTemplate> {

	/**
	 * 根据编号或名称查询企业事件类型
	 * @param orgId
	 * @param searchKey
	 * @param limit
	 * @return List<TaskType>
	 */
	public default List<TaskTemplate> findBySearchKey(final String orgId, final String searchKey, final Integer limit) {

		Order[] orders = { new Order(Direction.ASC, "code"), new Order(Direction.ASC, "name") };
		Sort sort = new Sort(orders);

		return findAll(new Specification<TaskTemplate>() {

			@Override
			public Predicate toPredicate(Root<TaskTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));

				if (StringUtils.isNotBlank(searchKey)) {
					String key = PredicateUtils.getAnyLike(searchKey);
					predicates.add(builder.or(
							builder.like(root.get("code"), key),
							builder.like(root.get("name"), key)
						));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, sort, limit);
	}
	/**
	 * 查询事件模板列表信息，按照事件模板主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<TaskTemplate> query(TaskTemplateVo vo,Pageable pageable){
		Page<TaskTemplate> findAll = findAll(new Specification<TaskTemplate>() {
			
			@Override
			public Predicate toPredicate(Root<TaskTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				String searchKey = vo.getSearchKey();
				if(!Strings.isNullOrEmpty(searchKey)){
					predicates.add(builder.or
							(
									builder.like(root.get("code"), PredicateUtils.getAnyLike(searchKey)),
									builder.like(root.get("name"), PredicateUtils.getAnyLike(searchKey))
							));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}
	/**
	 * 查找所有
	 * @return
	 */
	public default List<TaskTemplate> queryAll(TaskTemplateVo vo,String taskTypeId) {
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<TaskTemplate> findAll = findAll(new Specification<TaskTemplate>() {
			@Override
			public Predicate toPredicate(Root<TaskTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				if(!Strings.isNullOrEmpty(taskTypeId)){
					predicates.add(builder.equal(root.get("taskType").get("fid"), taskTypeId));
				}

				String searchKey = vo.getSearchKey();
				if(!Strings.isNullOrEmpty(searchKey)){
					predicates.add(builder.or
							(
									builder.like(root.get("code"), "'%"+searchKey+"%'"),
									builder.like(root.get("name"), "'%"+searchKey+"%'")
							));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
		return findAll;
	}
	/**
	 * 查找是否有重复编号
	 * @param selfId
	 * @param orgId
	 * @param code
	 * @return
	 */
	public default Long countSameCode(String selfId, String orgId, String code) {
		long count = this.count(new Specification<TaskTemplate>() {
			public Predicate toPredicate(Root<TaskTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
	 * 查找是否与计划模板有所关联
	 */
/*	@Query("select count(*) from TaskPlantemplate t where t.planTemplate.fid=?1")
	public Long countPlantemplateId(String id);*/
}	

