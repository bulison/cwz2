package cn.fooltech.fool_ops.domain.excelmap.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.excelmap.entity.ExcelMap;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface ExcelMapRepository extends JpaRepository<ExcelMap, String>, 
	JpaSpecificationExecutor<ExcelMap> {

	/**
	 * 查询分页
	 * @param clazz
	 * @param cnName
	 * @param field
	 * @param type
	 * @param request
	 * @return
	 */
	public default Page<ExcelMap> findPageBy(String clazz, String cnName, String field, 
			Integer type, PageRequest request){
		return findAll(new Specification<ExcelMap>() {
			
			@Override
			public Predicate toPredicate(Root<ExcelMap> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				if(StringUtils.isNotBlank(clazz)){
					predicates.add(builder.like(root.<String>get("clazz"), PredicateUtils.getAnyLike(clazz)));
				}
				if(StringUtils.isNotBlank(cnName)){
					predicates.add(builder.like(root.<String>get("cnName"), PredicateUtils.getAnyLike(cnName)));
				}
				if(StringUtils.isNotBlank(field)){
					predicates.add(builder.equal(root.<String>get("field"), field));
				}
				if(type!=null){
					predicates.add(builder.equal(root.<String>get("type"), type));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		}, request);
	}
	
	
	/**
	 * 查询分页
	 * @param clazz
	 * @param cnName
	 * @param field
	 * @param type
	 * @param request
	 * @return
	 */
	public default List<ExcelMap> findBy(String clazz, Integer type, boolean fexport, boolean fimport, Sort sort){
		return findAll(new Specification<ExcelMap>() {
			
			@Override
			public Predicate toPredicate(Root<ExcelMap> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				
				if(StringUtils.isNotBlank(clazz)){
					predicates.add(builder.equal(root.<String>get("clazz"), clazz));
				}
				if(type!=null){
					predicates.add(builder.equal(root.<String>get("type"), type));
				}
				if(fexport){
					predicates.add(builder.equal(root.<Boolean>get("fexport"), fexport));
				}
				if(fimport){
					predicates.add(builder.equal(root.<Boolean>get("fimport"), fimport));
				}
				predicates.add(builder.equal(root.get("validation"), true));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		}, sort);
	}
}
