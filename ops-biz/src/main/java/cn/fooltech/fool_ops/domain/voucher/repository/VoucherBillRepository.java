package cn.fooltech.fool_ops.domain.voucher.repository;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;

public interface VoucherBillRepository extends JpaRepository<VoucherBill, String>, JpaSpecificationExecutor<VoucherBill> {


	/**
	 * 根据凭证ID查询
	 * @param voucherId
	 * @return
	 */
	@Query("select v from VoucherBill v where v.voucher.fid=?1")
	public List<VoucherBill> findByVoucherId(String voucherId);
	
	/**
	 * 获取关联记录
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @param fiscalAccountId 财务账套ID
	 * @return
	 */
	@Query("select v from VoucherBill v where v.billId=?1 and v.billType=?2 and v.fiscalAccount.fid=?3")
	public VoucherBill getRecord(String billId, Integer billType, String fiscalAccountId);
	
	/**
	 * 获取单据关联的凭证的记录数目
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @param fiscalAccountId 财务账套ID
	 * @return
	 */
	@Query("select count(*) from VoucherBill v where v.billId in ?1 and v.billType=?2 and v.fiscalAccount.fid=?3")
	public long countVoucher(List<String> billIds, Integer billType, String fiscalAccountId);

	/**
	 * 获取单据关联的凭证的记录数目
	 * @param billId 单据ID
	 * @return
	 */
	@Query("select count(*) from VoucherBill v where v.billId=?1")
	public long countVoucher(String billId);
}
