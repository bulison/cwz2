package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface SysReportRepository extends JpaRepository<SysReport, String>, JpaSpecificationExecutor<SysReport> {

	/**
	 * 查询分页
	 * @param code
	 * @param parentId
	 * @param reportName
	 * @param page
	 * @return
	 */
	public default Page<SysReport> findPageBy(Integer code, String parentId, String reportName, Pageable page){
		return findAll(new Specification<SysReport>() {
			
			@Override
			public Predicate toPredicate(Root<SysReport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				
				if(code!= null){
					predicates.add(builder.equal(root.get("code"), code));
				}
				if(StringUtils.isNotBlank(parentId)){
					predicates.add(builder.equal(root.get("parent").get("fid"), parentId));
				}
				if(StringUtils.isNotBlank(reportName)){
					String likekey = PredicateUtils.getAnyLike(reportName);
					predicates.add(builder.like(root.get("reportName"), likekey));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	/**
	 * 查询列表
	 * @param code
	 * @param parentId
	 * @param reportName
	 * @param page
	 * @return
	 */
	public default List<SysReport> findBy(Integer code, String parentId, String reportName, Sort sort){
		return findAll(new Specification<SysReport>() {
			
			@Override
			public Predicate toPredicate(Root<SysReport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				
				if(code!= null){
					predicates.add(builder.equal(root.get("code"), code));
				}
				if(StringUtils.isNotBlank(parentId)){
					predicates.add(builder.equal(root.get("parent").get("fid"), parentId));
				}
				if(StringUtils.isNotBlank(reportName)){
					String likekey = PredicateUtils.getAnyLike(reportName);
					predicates.add(builder.like(root.get("reportName"), likekey));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
	}
	
	@Query("select s from SysReport s where s.parent is null")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public SysReport findRoot();

	@Query("select count(*) from SysReport s where s.parent.fid=?1 and s.code=?2")
	public Long countByCode(String parentId, Integer code);
	
	@Query("select count(*) from SysReport s where s.parent.fid=?1 and s.code=?2 and s.fid!=?3")
	public Long countByCode(String parentId, Integer code, String excludeId);
}
