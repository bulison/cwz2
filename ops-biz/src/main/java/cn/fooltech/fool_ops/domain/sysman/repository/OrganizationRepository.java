package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.vo.OrganizationVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;

public interface OrganizationRepository extends JpaRepository<Organization, String>,JpaSpecificationExecutor<Organization> {

	/**
	 * 根据机构ID查询用户分页
	 * @param orgId
	 * @return
	 */
	@Query("select o from Organization o where o.orgId=?1 and o.validation=1")
	public List<Organization> findByTopOrgId(final String orgId);
	
	/**
	 * 根据父路径获得所有子节点（不包含自己）
	 * @param org
	 * @return
	 */
	public List<Organization> findByParentIdsStartingWith(String parentIds);

	/**
	 * 根据编码计算个数
	 * @param orgId
	 * @param orgCode
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from Organization o where o.orgId=?1 and o.orgCode=?2 and o.fid!=?3 and o.validation=1")
	public Long countByCode(String orgId, String orgCode, String excludeId);
	
	/**
	 * 根据编码计算个数
	 * @param orgId
	 * @param orgCode
	 * @return
	 */
	@Query("select count(*) from Organization o where o.orgId=?1 and o.orgCode=?2 and o.validation=1")
	public Long countByCode(String orgId, String orgCode);
	/**
	 * 根据名称计算个数
	 * @param orgId
	 * @param orgName
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from Organization o where o.orgId=?1 and o.orgName=?2 and o.fid!=?3 and o.validation=1")
	public Long countByName(String orgId, String orgName, String excludeId);
	
	/**
	 * 根据名称算个数
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	@Query("select count(*) from Organization o where o.orgId=?1 and o.orgName=?2 and o.validation=1")
	public Long countByName(String orgId, String orgName);

	/**
	 * 根据编码计算个数
	 * @param orgCode
	 * @return
	 */
	@Query("select count(*) from Organization o where o.orgCode=?2 and o.validation=1")
	public Long countByCode(String orgCode);

	/**
	 * 查找所有机构
	 * @return
	 */
//	@Query("select o from Organization o where parent.fid is null")
	public default List<Organization> findByOrgIdIsNull(OrganizationVo vo){
		return findAll(new Specification<Organization>() {
			@Override
			public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.isNull(root.get("parent").get("fid")));
				if(!Strings.isNullOrEmpty(vo.getOrgName())){
					String anyLike = PredicateUtils.getAnyLike(vo.getOrgName());
					predicates.add(builder.like(root.get("orgName"), anyLike));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
	}
	/**
	 * 根据编号查找部门
	 * @param orgId
	 * @param orgCode
	 * @return 
	 */
	@Query("select o from Organization o where o.orgId=?1 and o.orgCode=?2")
	public Organization getByCode(String orgId, String orgCode);
	
	@Query("select o from Organization o where o.orgId=?1 and o.orgCode=?2 and o.fid!=?3")
	public Organization getByCode(String orgId, String orgCode, String selfId);

	/**
	 * 根据级别查找
	 * @param orgId
	 * @param level
	 * @return
	 */
	@Query("select o from Organization o where o.orgId=?1 and o.flevel=?2 and validation=1")
	public List<Organization> findByLevel(String orgId, Short level);
	
	
	/**
	 * 查找所有叶子节点
	 * @param orgId
	 * @return
	 */
	@Query(value="select o from smg_torganization o where o.fid in(select a.fid from smg_torganization as a left join smg_torganization as b on a.fid=b.parent_id where b.fid is null and a.org_id=?1)", nativeQuery=true)
	public List<Organization> findAllLeaf(String orgId);
	/**
	 * 修复旧数据,找出所有真结构(没有父类的机构)
	 * @return
	 */
	@Query("select o from Organization o where o.parent is null")
	public List<Organization> queryRootOrganization();
}
