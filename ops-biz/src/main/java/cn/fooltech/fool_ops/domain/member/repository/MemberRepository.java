package cn.fooltech.fool_ops.domain.member.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.utils.PredicateUtils;
/**
 * 企业人员 持久层
 * @author cwz
 * @date   2016-10-27
 */
public interface MemberRepository extends JpaRepository<Member, String>,FoolJpaSpecificationExecutor<Member> {

	/**
	 * 查询人员列表信息，按照人员主键降序排列
	 * @param memberVo
	 * @param orgId
	 * @param pageable
	 * @return
	 */
	public default Page<Member> query(MemberVo memberVo,String orgId,Pageable pageable){
		return findAll(new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("org").get("fid"),orgId));
				predicates.add(builder.equal(root.<String>get("enable"),StorageBaseEntity.STATUS_ENABLE));
			    root.join("user",JoinType.LEFT); 
			    String searchKey = memberVo.getSearchKey()==null?"":memberVo.getSearchKey().trim();
			    String likeKey = "%"+searchKey+"%";
				if (StringUtils.isNotBlank(likeKey)) {
					predicates.add(builder.or(builder.like(root.<String>get("jobNumber"), likeKey),
							builder.like(root.get("username"), likeKey),
							builder.like(root.get("userCode"), likeKey),
							builder.like(root.get("user").get("accountName"), likeKey),
							builder.like(root.get("phoneOne"), likeKey)));
				}
				if(StringUtils.isNotBlank(memberVo.getJobNumber())){
					predicates.add(builder.like(root.get("jobNumber"),"%"+ memberVo.getJobNumber().trim()+"%"));
				}
				if(StringUtils.isNotBlank(memberVo.getDeptId())){
					predicates.add(builder.equal(root.get("dept").get("fid"),memberVo.getDeptId()));
				}
				
				if(StringUtils.isNotBlank(memberVo.getUsername())){
					predicates.add(builder.like(root.get("username"),"%"+ memberVo.getUsername().trim()+"%"));
				}
				
				if(StringUtils.isNotBlank(memberVo.getUserCode())){
					predicates.add(builder.like(root.get("userCode"),"%"+ memberVo.getUserCode().trim()+"%"));
				}
				
				if(StringUtils.isNotBlank(memberVo.getLoginName())){
					predicates.add(builder.like(root.get("user").get("accountName"),"%"+ memberVo.getLoginName().trim()+"%"));
				}
				
				if(StringUtils.isNotBlank(memberVo.getPhoneOne())){
					predicates.add(builder.like(root.get("phoneOne"),"%"+ memberVo.getPhoneOne().trim()+"%"));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		
	}
	/**
	 * 检查是否统一机构下是否有相同的用户编号
	 * 存在则返回false,否则返回true
	 */
	public default boolean checkUserCode(String orgId, String userCode, String memberId) {
		 long count = count(new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("org").get("fid"),orgId));
				predicates.add(builder.equal(root.<String>get("userCode"),userCode));
				if (StringUtils.isNotBlank(memberId)) {
					predicates.add(builder.notEqual(root.<String>get("fid"), memberId));// 排除自己
				}
				predicates.add(builder.equal(root.<String>get("enable"), StorageBaseEntity.STATUS_ENABLE));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return count > 0 ? false : true;
	}
	

	/**
	 * 检查手机号唯一
	 * @param phoneOne
	 * @param fid
	 * @return
	 */
	public default boolean checkPhoneOne(String phoneOne, String fid, String orgId) {
		 long count = count(new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				predicates.add(builder.equal(root.<String>get("org").get("fid"),orgId));
				predicates.add(builder.equal(root.<String>get("phoneOne"),phoneOne));
				if (StringUtils.isNotBlank(fid)) {
					predicates.add(builder.notEqual(root.<String>get("fid"), fid));// 排除自己
				}
				predicates.add(builder.equal(root.<String>get("enable"), StorageBaseEntity.STATUS_ENABLE));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return count > 0 ? false : true;
	}
	/**
	 * 根据部门查找人员
	 */
	@Query("select a from Member a where dept.fid=?1 and enable=1")
	public List<Member> findByDeptIdOrderByCreateTimeAsc(String deptId);
	
	/**
	 * 模糊查询(根据人员编号、人员名称)
	 * @param orgId
	 * @param searchKey
	 * @param maxSize
	 * @param userId
	 * @return
	 */
	public default List<Member> vagueSearch(String orgId, String searchKey, int maxSize,String userId,String inputType){

		Sort sort = new Sort(Direction.ASC, "userCode","username");
		return findAll(new Specification<Member>() {
			
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList(); 
				
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("enable"), StorageBaseEntity.STATUS_ENABLE));
				
				if(StringUtils.isNotBlank(searchKey)){
					String likeKey = searchKey.trim();
					likeKey = PredicateUtils.getAnyLike(likeKey);
					if(UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)){
						predicates.add(builder.or(
								builder.like(root.get("userCode"), likeKey),
								builder.like(root.get("username"), likeKey),
								builder.like(root.get("fivepen"), likeKey)
							));
					}else{
						predicates.add(builder.or(
								builder.like(root.get("userCode"), likeKey),
								builder.like(root.get("username"), likeKey),
								builder.like(root.get("pinyin"), likeKey)
							));
					}
				}
				
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort, maxSize);
		
	}
	/**
	 * 根据编号获取
	 * @param orgId    机构id
	 * @param userCode 用户编号
	 * @return
	 */
	@Query("select a from Member a where org.fid=?1 and userCode=?2 and enable=1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public Member getByCode(String orgId, String userCode);
	
	
	/**
	 * 根据部门查找在职人员+本月离职人员
	 */
	public default List<Member> findNotDepartureByDeptId(String deptId, Date departureDate) {
		Sort sort = new Sort(Direction.ASC, "createTime");
		List<Member> findAll = findAll(new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<String>get("dept").get("fid"), deptId));
				predicates.add(builder.equal(root.<String>get("enable"), StorageBaseEntity.STATUS_ENABLE));
//				ParameterExpression<Date> param = builder.parameter(Date.class, "departureDate");
				if (departureDate != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(departureDate);
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					calendar.set(Calendar.HOUR, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					predicates.add(builder.or(
							builder.isNull(root.get("departureDate")),
							builder.lessThanOrEqualTo(root.get("departureDate"), calendar.getTime())
							));
				} else {
					predicates.add(builder.isNull(root.get("departureDate")));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
		return findAll;
//		DetachedCriteria dc = DetachedCriteria.forClass(Member.class);
//		dc.add(Restrictions.eq("dept.fid", deptId));
//		dc.add(Restrictions.eq("enable", StorageBaseEntity.STATUS_ENABLE));
//		
//		if(departureDate!=null){
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(departureDate);
//			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//			calendar.set(Calendar.HOUR, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.MILLISECOND, 0);
//			dc.add(Restrictions.or(Restrictions.isNull("departureDate"),
//					Restrictions.le("departureDate", calendar.getTime())));
//		}else{
//			dc.add(Restrictions.isNull("departureDate"));
//		}
//		dc.addOrder(Order.asc("createTime"));
//		return queryByCriteria(dc);
	}
	
	/**
	 * 根据机构ID和状态查询
	 * @param orgId
	 * @param status
	 * @return
	 */
	@Query("select a from Member a where a.org.fid=?1 and enable=?2")
	public List<Member> findByEnableAndOrgId(String orgId, short status);
}
