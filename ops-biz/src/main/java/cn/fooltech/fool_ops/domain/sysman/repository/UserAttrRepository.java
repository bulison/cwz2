package cn.fooltech.fool_ops.domain.sysman.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;

import java.util.List;

public interface UserAttrRepository extends JpaRepository<UserAttr, String> {

	/**
	 * 根据用户ID和KEY获取用户属性(只取一个)
	 * @param userId
	 * @param key
	 * @return
	 */
	@Query("select u from UserAttr u where u.userID=?1 and key=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public UserAttr findTopBy(String userId, String key);

	/**
	 * 根据用户ID和KEY获取用户属性
	 * @param userId
	 * @param key
	 * @return
	 */
	@Query("select u from UserAttr u where u.userID=?1 and u.key=?2")
	public List<UserAttr> findByIdAndKey(String userId, String key);
}
