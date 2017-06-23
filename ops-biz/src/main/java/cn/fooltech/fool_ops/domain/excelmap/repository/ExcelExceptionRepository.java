package cn.fooltech.fool_ops.domain.excelmap.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.excelmap.entity.ExcelException;

public interface ExcelExceptionRepository extends JpaRepository<ExcelException, String> {

	/**
	 * 根据orgId查询和code查询
	 * @param code
	 * @param orgId
	 * @param request
	 * @return
	 */
	@Query("select e from ExcelException e where e.code like ?1 and e.org.fid=?2")
	public Page<ExcelException> findPageByOrgAndCode(String code, String orgId, Pageable pageable);

	/**
	 * 根据orgId查询
	 * @param orgId
	 * @param request
	 * @return
	 */
	@Query("select e from ExcelException e where e.org.fid=?1")
	public Page<ExcelException> findPageByOrg(String orgId, Pageable pageable);
	
}
