package cn.fooltech.fool_ops.domain.voucher.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.voucher.entity.WageVoucher;

public interface WageVoucherRepository extends JpaRepository<WageVoucher, String>, JpaSpecificationExecutor<WageVoucher> {

	/**
	 * 根据账套查找分页
	 * @param accId
	 * @param pageRequest
	 * @return
	 */
	@Query("select w from WageVoucher w where w.fiscalAccount.fid=?1")
	public Page<WageVoucher> findPageByAccId(String accId, Pageable page);
	
	/**
	 * 根据账套查找列表
	 * @param accId
	 * @param pageRequest
	 * @return
	 */
	@Query("select w from WageVoucher w where w.fiscalAccount.fid=?1")
	public List<WageVoucher> findByAccId(String accId);
	
	/**
	 * 根据科目ID统计引用个数
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from WageVoucher w where w.expenseSubject.fid=?1 or w.wageSubject.fid=?1")
	public Long countBySubjectId(String subjectId);


	/**
	 * 根据工资项目ID统计引用个数
	 * @param wageColmnId
	 * @return
	 */
	@Query("select count(*) from WageVoucher w where w.wageFormula.fid=?1")
	public Long countByWageColumnId(String wageColmnId);
}
