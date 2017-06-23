package cn.fooltech.fool_ops.domain.sysman.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, FoolJpaSpecificationExecutor<User>{

	@Query("select u from User u where u.userCode=?1 and u.validation=1")
	@QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
	public User findOneByUserCode(String userCode);

	/**
	 * 根据用户名或编号查询企业下的用户
	 * @param orgId
	 * @param searchKey
	 * @param limit
	 * @return
	 */
	public default List<User> findBySearchKey(final String orgId, final String searchKey, final Integer limit){
		
		Order[] orders = {new Order(Direction.ASC, "userCode"), new Order(Direction.ASC, "userName")};
		Sort sort = new Sort(orders);
		
		 List<User> findAll = findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<String>get("topOrg").get("fid"), orgId));
				predicates.add(builder.equal(root.<Short>get("validation"), (short)1));
				predicates.add(builder.equal(root.<Short>get("isAdmin"), (short)0));
				
				if(StringUtils.isNotBlank(searchKey)){
					String likeKey = PredicateUtils.getAnyLike(searchKey.trim());
					predicates.add(builder.or(builder.like(root.get("userCode").as(String.class), likeKey),
							builder.like(root.get("userName").as(String.class), likeKey)));
				}
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		}, sort, limit );
		 return findAll;
	}
	
	/**
	 * 根据机构ID查询用户分页
	 * @param orgId
	 * @param pageable
	 * @return
	 */
	@Query("select u from User u where u.topOrg.fid=?1 and u.validation=1")
	public Page<User> findPageByTopOrgId(final String orgId, Pageable pageable);
	/**
	 * 根据机构ID查询用户不分页
	 * @param orgId
	 * @param pageable
	 * @return
	 */
	@Query("select u from User u where u.topOrg.fid=?1 and u.validation=1")
	public List<User> findListByTopOrgId(final String orgId);
	
	/**
	 * 根据部门ID查询用户分页
	 * @param deptId
	 * @param pageable
	 * @return
	 */
	@Query("select u from User u where u.orgId.fid=?1 and u.validation=1")
	public Page<User> findPageByDeptId(final String deptId, Pageable pageable);
	
	/**
	 * 根据部门s查询用户分页
	 * @param depts
	 * @param pageable
	 * @return
	 */
	@Query("select u from User u where u.orgId in ?1 and u.validation=1")
	public Page<User> findPageByDepts(final List<Organization> depts, Pageable pageable);
	
	/**
	 * 判断用户的手机号码是否已存在 
	 * @param orgId
	 * @param phone
	 * @return
	 */
	@Query("select count(*) from User u where u.topOrg.fid = ?1 and u.validation=1 and u.phoneOne=?2")
	public Long countByPhone(final String orgId, final String phone);
	
	/**
	 * 判断用户的手机号码是否已存在 
	 * @param orgId
	 * @param phone
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from User u where u.topOrg.fid = ?1 and u.validation=1 and u.phoneOne=?2 and u.fid!=?3")
	public Long countByPhone(final String orgId, final String phone, final String excludeId);
	
	/**
	 * 判断用户的编号是否已存在 
	 * @param userCode
	 * @return
	 */
	@Query("select count(*) from User u where u.validation=1 and u.userCode=?1")
	public Long countByUserCode(final String userCode);
	
	/**
	 * 判断用户的编号是否已存在 
	 * @param userCode
	 * @param excludeId
	 * @return
	 */
	@Query("select count(*) from User u where u.validation=1 and u.userCode=?1 and u.fid!=?2")
	public Long countByUserCode(final String userCode, final String excludeId);
	
	/**
	 * 判断用户编号是否存在
	 * @param userId 用户ID
	 * @param userCode 用户编码
	 * @return 不存在：false 存在 true
	 */
	public default boolean checkCodeIsExist(String userId, String userCode) {
		 long count = count(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("userCode"), StringUtils.trimToNull(userCode)));
				predicates.add(builder.equal(root.get("validation"), (short)1));
				if(!StringUtils.isBlank(userId)){
					predicates.add(builder.notEqual(root.<String>get("fid"), userId));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
		 return count!= 0;
	}
	/**
	 * 通过orgId查找企业的超级管理员,用于数据初始化,注释
	 */
	@Query("select u from User u where u.topOrg.fid=?1 and u.isAdmin=1")
	public List<User> findAdminByOrgId(String orgId);
}
