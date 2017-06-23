package cn.fooltech.fool_ops.domain.sysman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAuth;
import org.springframework.data.jpa.repository.Query;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {

	@Query("select u.resource from UserAuth u where u.user.fid=?1")
	public List<Resource> findByUser(String userId);

	public List<UserAuth> findByUser_fid(String userId);
}
