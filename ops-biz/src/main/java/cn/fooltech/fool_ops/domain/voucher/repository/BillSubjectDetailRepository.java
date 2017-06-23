package cn.fooltech.fool_ops.domain.voucher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;

public interface BillSubjectDetailRepository extends JpaRepository<BillSubjectDetail, String>, JpaSpecificationExecutor<BillSubjectDetail> {

	
	/**
	 * 根据模板ID获取
	 * @param orgId
	 * @param templateId
	 * @return
	 */
	@Query("select b from BillSubjectDetail b where b.org.fid=?1 and b.billSubject.fid=?2 order by b.createTime asc")
	public List<BillSubjectDetail> getByTemplateId(String orgId, String templateId);
	
	/**
	 * 统计某个科目被引用的次数
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from BillSubjectDetail b where b.subject.fid=?1") 
	public Long countBySubjectId(String subjectId);
}
