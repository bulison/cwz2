package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.SysFilter;

public interface SysFilterRepository extends JpaRepository<SysFilter, String> {

	/**
	 * 根据报表ID查询条件
	 * @param reportId
	 * @param sort
	 * @return
	 */
	@Query("select f from SysFilter f where f.sysReport.fid=?1")
	public List<SysFilter> findByReportId(String reportId, Sort sort);

	/**
	 * 根据报表ID查询分页
	 * @param reportId
	 * @param page
	 * @return
	 */
	@Query("select f from SysFilter f where f.sysReport.fid=?1")
	public Page<SysFilter> findPageByReportId(String reportId, Pageable page);

	@Query("select count(*) from SysFilter s where s.sysReport.fid=?1")
	public Long countByReportId(String reportId);

}
