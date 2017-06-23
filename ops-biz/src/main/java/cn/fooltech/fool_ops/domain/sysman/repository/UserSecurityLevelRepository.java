package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.sysman.entity.UserSecurityLevel;

public interface UserSecurityLevelRepository extends JpaRepository<UserSecurityLevel, String>,FoolJpaSpecificationExecutor<UserSecurityLevel> {

//	@Query("select u from UserAttr u where u.userID=?1 and key=?2")
//	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
//	public UserAttr findTopBy(String userId, String key);
	/**
	 * 获取用户的保密级别
	 * @param userId
	 * @return
	 */
	@Query("select a from UserSecurityLevel a where user.fid=?1")
	public List<UserSecurityLevel> getByUserId(String userId);
	
	/**
	 * 统计保密级别被引用的次数
	 * @param securityLevelId 保密级别ID
	 * @return
	 */
	@Query("select count(a) from UserSecurityLevel a where securityLevel.fid=?1")
	public long countBySecurityLevel(String securityLevelId);
}
