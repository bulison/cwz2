package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.UserTemplate;

public interface UserTemplateRepository extends JpaRepository<UserTemplate, String> {

	/**
	 * 分页查询
	 * @param orgId
	 * @param reportId
	 * @param page
	 * @return
	 */
	@Query("select u from UserTemplate u where u.org.fid=?1 and u.report.fid=?2")
	public Page<UserTemplate> findPageBy(String orgId, String reportId, Pageable page);
	
	/**
	 * 列表查询
	 * @param orgId
	 * @param reportId
	 * @param page
	 * @return
	 */
	@Query("select u from UserTemplate u where u.org.fid=?1 and u.report.fid=?2")
	public List<UserTemplate> findBy(String orgId, String reportId, Sort sort);

}
