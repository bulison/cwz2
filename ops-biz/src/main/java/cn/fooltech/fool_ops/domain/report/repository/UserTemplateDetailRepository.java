package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;

public interface UserTemplateDetailRepository extends JpaRepository<UserTemplateDetail, String> {

	/**
	 * 根据模板ID查询
	 * @param templateId
	 * @return
	 */
	@Query("select u from UserTemplateDetail u where u.template.fid=?1")
	public List<UserTemplateDetail> findByUserTemplateId(String templateId);

	@Query("select count(*) from UserTemplateDetail s where s.report.fid=?1")
	public Long countByReportId(String reportId);

}
