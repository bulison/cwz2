package cn.fooltech.fool_ops.domain.bom.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.bom.entity.Bom;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface BomRepository extends JpaRepository<Bom, String>, FoolJpaSpecificationExecutor<Bom> {

	/**
	 * 根据货品ID查询
	 * @param accId
	 * @param goodsId
	 * @return
	 */
	@Query("select b from Bom b where b.fiscalAccount.fid=?1 and b.goods.fid=?2")
	public List<Bom> findByGoodsId(String accId, String goodsId);
	
	/**
	 * 查找分页
	 * @param accId
	 * @param goodsId
	 * @param goodsCode
	 * @param goodsName
	 * @param creatorId
	 * @param creatorName
	 * @param enable
	 * @param page
	 * @return
	 */
	public default Page<Bom> findPageBy(String accId, String goodsId, String goodsCode, String goodsName,
			String creatorId, String creatorName, Short enable, Pageable page){
		return findAll(new Specification<Bom>() {
			
			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				if (!Strings.isNullOrEmpty(goodsId)) {
					predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				}else{
					if(!Strings.isNullOrEmpty(goodsCode)){
						String likekey = PredicateUtils.getAnyLike(goodsCode);
						predicates.add(builder.like(root.get("goods").get("code"), likekey));
					}
					if(!Strings.isNullOrEmpty(goodsName)){
						String likekey = PredicateUtils.getAnyLike(goodsName);
						predicates.add(builder.like(root.get("goods").get("name"), likekey));
					}
				}
				
				if(!Strings.isNullOrEmpty(creatorId)){
					predicates.add(builder.equal(root.get("creator").get("fid"), creatorId));
				}else{
					if(!Strings.isNullOrEmpty(creatorName)){
						String likekey = PredicateUtils.getAnyLike(creatorName);
						predicates.add(builder.like(root.get("creator").get("userName"), likekey));
					}
				}
				
				if(enable!=null){
					predicates.add(builder.equal(root.get("enable"), enable));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, page);
	}
	
	
	/**
	 * 根据goodsId、specId查找的物料
	 * @return
	 */
	public default List<Bom> findByMaterial(String accId, String goodsId, String specId, String excludeId){
		return findAll(new Specification<Bom>() {

			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				predicates.add(builder.equal(root.get("enable"), Bom.ENABLE));
				
				if(!Strings.isNullOrEmpty(specId)){
					predicates.add(builder.equal(root.get("spec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("spec")));
				}
				
				if(!Strings.isNullOrEmpty(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	/**
	 * 根据goodsId查找数据
	 * @return
	 */
	public default List<Bom> findByMaterial(String accId, String goodsId, Short fdefault){
		return findAll(new Specification<Bom>() {

			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("enable"), Bom.ENABLE));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				predicates.add(builder.isNull(root.get("spec").get("fid")));
				if (fdefault != null) {
					predicates.add(builder.equal(root.get("fdefault"), fdefault));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}

	public default List<Bom> findByMaterial(String selfId, String accId, String goodsId, String specId, Short fdefault){
		return findAll(new Specification<Bom>() {
			
			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				if(!Strings.isNullOrEmpty(specId)){
					predicates.add(builder.equal(root.get("spec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("spec").get("fid")));
				}
				if (!Strings.isNullOrEmpty(selfId)) {
					predicates.add(builder.notEqual(root.get("fid"), selfId));
					
				}
				predicates.add(builder.equal(root.get("enable"), Bom.ENABLE));
				if (fdefault != null) {
					predicates.add(builder.equal(root.get("fdefault"), fdefault));
				}
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	/**
	 * 根据goodsId、specId查找的物料
	 * @return
	 */
	public default Bom findTopByDefaultMaterial(String accId, String goodsId, String specId){
		return findTop(new Specification<Bom>() {

			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				predicates.add(builder.equal(root.get("enable"), Bom.ENABLE));
				predicates.add(builder.equal(root.get("fdefault"), Bom.DEFAULT));
				
				if(!Strings.isNullOrEmpty(specId)){
					predicates.add(builder.equal(root.get("spec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("spec")));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
	
	/**
	 * 根据goodsId查找物料
	 * @return
	 */
	@Query("select b from Bom b where b.fiscalAccount.fid=?1 and b.goods.fid=?2")
	public List<Bom> findByMaterial(String accId, String goodsId);
	
	/**
	 * 根据goodsId、specId判断bom表存在相同数据个数
	 * @return
	 */
	public default Long countSame(String goodsId, String specId, String accId, String excludeId){
		return count(new Specification<Bom>() {

			@Override
			public Predicate toPredicate(Root<Bom> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accId));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				
				if(!Strings.isNullOrEmpty(specId)){
					predicates.add(builder.equal(root.get("spec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("spec")));
				}
				
				if(!Strings.isNullOrEmpty(excludeId)){
					predicates.add(builder.notEqual(root.get("fid"), excludeId));
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}
	
}
