package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumnDetail;

public interface MultiColumnDetailRepository extends JpaRepository<FiscalMultiColumnDetail, String>, 
	JpaSpecificationExecutor<FiscalMultiColumnDetail> {

	/**
	 * 根据多栏明细ID和科目方向查询
	 * @param columnId
	 * @param direction
	 * @return
	 */
	@Query("select d from FiscalMultiColumnDetail d where d.fiscalMultiColumn.fid=?1 and d.direction=?2 order by d.direction asc,d.subject.code asc,d.auxiliaryAttrId asc")
	public List<FiscalMultiColumnDetail> findByColumnIdAndDirection(String columnId, int direction);
}
