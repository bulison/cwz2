package cn.fooltech.fool_ops.domain.voucher.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;

public interface VoucherDetailRepository extends FoolJpaRepository<VoucherDetail, String>, JpaSpecificationExecutor<VoucherDetail> {

	/**
	 * 根据凭证ID查询
	 * @param voucherId
	 * @return
	 */
	@Query("select v from VoucherDetail v where v.voucher.fid=?1")
	public List<VoucherDetail> findDetailsByVoucherId(String voucherId);

	/**
	 * 根据凭证ID合计金额
	 * @param voucherId
	 * @return
	 */
	@Query("select sum(v.debitAmount) from VoucherDetail v where v.voucher.fid=?1")
	public BigDecimal sumAmountByVoucherId(String voucherId);
	
	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from VoucherDetail v where v.accountingSubject.fid=?1")
	public Long countBySubjectId(String subjectId);
	
	/**
	 * 根据科目ID统计
	 * @param subjectId
	 * @return
	 */
	@Query("select count(*) from VoucherDetail v where v.accountingSubject.fid=?1 and v.voucher.voucherDate>=?2 and v.voucher.voucherDate<=?3")
	public Long countBySubjectId(String subjectId, Date start, Date end);

	/**
	 * 获取凭证里的第一条凭证明细
	 * @param voucherId
	 * @return
	 */
	@Query("select v from VoucherDetail v where v.voucher.fid=?1 order by v.createTime asc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public VoucherDetail getFirstDetailOfVoucher(String voucherId);
	
	/**
	 * 根据科目ID查询凭证结余金额
	 * @param code
	 * @param accId
	 */
	public default BigDecimal getAmountBySubjectId(String subjectId, String accId, 
			List<Integer> statusList){
		if(Strings.isNullOrEmpty(subjectId))return BigDecimal.ZERO;
		
		Joiner joiner = Joiner.on(",");
		String statusStr = joiner.join(statusList);
		
		String sql = "select sum(coalesce(FDEBIT_AMOUNT,0)-coalesce(FCREDIT_AMOUNT,0)) from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid ";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in ("+statusStr+") and b.fid=:subjectId and a.facc_id=:accId";
		
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("accId", accId);
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	/**
	 * 根据科目ID查询凭证结余金额
	 * @param code
	 * @param accId
	 */
	public default BigDecimal getAmountBySubjectId(String subjectId, Date startDate, Date endDate, 
			String accId, List<Integer> statusList){
		if(Strings.isNullOrEmpty(subjectId))return BigDecimal.ZERO;
		
		Joiner joiner = Joiner.on(",");
		String statusStr = joiner.join(statusList);
		
		String sql = "select sum(coalesce(FDEBIT_AMOUNT,0)-coalesce(FCREDIT_AMOUNT,0)) from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid ";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in ("+statusStr+") and b.fid=:subjectId and a.facc_id=:accId and c.FVOUCHER_DATE >=:startDate and c.FVOUCHER_DATE <=:endDate";
		
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("accId", accId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}

	/**
	 * 获得多个code的查询条件
	 * @param code
	 * @param condition
	 * @return
	 */
	default String getCodeCondition(String code, String condition){
		String codeCondition = condition+" like '"+code+"%'";
		return codeCondition;
	}
	/**
	 * 根据科目编号查询金额(资产负债AA)
	 * （借方科目）金额=借-贷
	 * （贷方科目）金额=贷-借
	 * @param code
	 * @param accId
	 */
	public default BigDecimal getAA(String code, Date date, String accId){
		if(Strings.isNullOrEmpty(code))return BigDecimal.ZERO;
		String codeStr = getCodeCondition(code, "b.fcode");
		String sql = "select sum(case when b.fdirection=1 then coalesce(FDEBIT_AMOUNT,0)-coalesce(FCREDIT_AMOUNT,0)";
		sql += " when b.fdirection=-1 then coalesce(FCREDIT_AMOUNT,0)-coalesce(FDEBIT_AMOUNT,0) end";
		sql += ") from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid ";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in (1,3) and a.facc_id=:accId and c.FVOUCHER_DATE <=:date";
		sql += " and (" +codeStr+")";
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("accId", accId);
		query.setParameter("date", date);
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	

	/**
	 * 汇总多个科目数据，取科目设置中余额方向的发生额(资产负债AS)
	 * @param code
	 * @param accId
	 * @return
	 */
	public default BigDecimal getAS(String code, Date date, String accId) {
		if(Strings.isNullOrEmpty(code))return BigDecimal.ZERO;
		String codeStr = getCodeCondition(code, "b.fcode");
		String sql = "select sum(case when b.fdirection=1 then coalesce(FDEBIT_AMOUNT,0)";
		sql += " when b.fdirection=-1 then coalesce(FCREDIT_AMOUNT,0) end";
		sql += ") from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in (1,3) and a.facc_id=:accId and c.FVOUCHER_DATE <=:date";
		sql += " and (" +codeStr+")";
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("accId", accId);
		query.setParameter("date", date);
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	
	//////////////////////////////////////////////////
	
	
	
	/**
	 * 根据科目编号查询金额（利润AA）
	 * （借方科目）金额=借-贷
	 * （贷方科目）金额=贷-借
	 * @param code
	 * @param accId
	 */
	public default BigDecimal getAAPeriod(String code, Date startDate, Date endDate, String accId){
		if(Strings.isNullOrEmpty(code))return BigDecimal.ZERO;
		String codeStr = getCodeCondition(code, "b.fcode");
		String sql = "select sum(case when b.fdirection=1 then coalesce(FDEBIT_AMOUNT,0)-coalesce(FCREDIT_AMOUNT,0)";
		sql += " when b.fdirection=-1 then coalesce(FCREDIT_AMOUNT,0)-coalesce(FDEBIT_AMOUNT,0) end";
		sql += ") from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid ";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in (1,3) and a.facc_id=:accId and c.FVOUCHER_DATE >=:startDate and c.FVOUCHER_DATE <=:endDate";
		sql += " and (" +codeStr+")";
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("accId", accId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	/**
	 * 汇总多个科目数据，取科目设置中余额方向的发生额（利润AS）
	 * @param code
	 * @param accId
	 * @return
	 */
	public default BigDecimal getASPeriod(String code, Date startDate, Date endDate, String accId) {
		if(Strings.isNullOrEmpty(code))return BigDecimal.ZERO;
		String codeStr = getCodeCondition(code, "b.fcode");
		String sql = "select sum(case when b.fdirection=1 then coalesce(FDEBIT_AMOUNT,0)";
		sql += " when b.fdirection=-1 then coalesce(FCREDIT_AMOUNT,0) end";
		sql += ") from tbd_voucher_detail a left join tbd_fiscal_accounting_subject b on a.fsubjectid=b.fid";
		sql += " left join tbd_voucher c on a.FVOUCHERID=c.fid where c.RECORD_STATUS in (1,3) and a.facc_id=:accId and  c.FVOUCHER_DATE >=:startDate and c.FVOUCHER_DATE <=:endDate";
		sql += " and (" +codeStr+")";
		javax.persistence.Query query = this.getEntityManager().createNativeQuery(sql);
		query.setParameter("accId", accId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		Object data = query.getSingleResult();
		if(data!=null)return (BigDecimal)data;
		return BigDecimal.ZERO;
	}
	
	/**
	 * 资产负债表AD、AC（科目）：汇总科目数据，取科目的所有借方，贷方的发生额；
	 * @param code 科目编号,多个用英文逗号隔开
	 * @param direction 余额方向
	 * @param accId 账套ID
	 * @return
	 */
	public default BigDecimal getADAC(String code, Date date, Integer direction, String accId) {
		
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> codes = splitter.splitToList(code);
		
		List<String> codeOrList = Lists.newArrayList();
		if(codes.size()<1){
			return BigDecimal.ZERO;
		}else{
			for(String temp:codes){
				codeOrList.add(" v.accountingSubject.code like %"+temp +" ");
			}
			Joiner joinner = Joiner.on("or").skipNulls();
			String codelikeStr = joinner.join(codeOrList);
			
			StringBuffer buffer = new StringBuffer();
			if(direction==FiscalAccountingSubject.DIRECTION_BORROW){
				buffer.append("select sum(v.debitAmount) from VoucherDetail v");
			}else{
				buffer.append("select sum(v.creditAmount) from VoucherDetail v ");
			}
			buffer.append("where v.fiscalAccount.fid=:accId ");
			buffer.append("and v.voucher.voucherDate<=:date ");
			buffer.append("and v.voucher.recordStatus in :status ");
			buffer.append("and (");
			buffer.append(codelikeStr);
			buffer.append(")");
			
			javax.persistence.Query query = getEntityManager().createQuery(buffer.toString());
			query.setParameter("accId", accId);
			query.setParameter("date", date);
			query.setParameter("status", Lists.newArrayList(Voucher.STATUS_AUDITED, Voucher.STATUS_POSTED));

			Object data = query.getSingleResult();
			if(data!=null)return (BigDecimal)data;
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * 利润表 AD/AC（科目）：汇总科目数据，取科目的所有借方，贷方的发生额；
	 * @param code 科目编号,多个用英文逗号隔开
	 * @param direction 余额方向
	 * @param accId 账套ID
	 * @return
	 */
	public default BigDecimal getADACPeriod(String code, Date startDate, Date endDate, Integer direction, String accId) {
		
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> codes = splitter.splitToList(code);
		
		List<String> codeOrList = Lists.newArrayList();
		if(codes.size()<1){
			return BigDecimal.ZERO;
		}else{
			for(String temp:codes){
				codeOrList.add(" v.accountingSubject.code like %"+temp +" ");
			}
			Joiner joinner = Joiner.on("or").skipNulls();
			String codelikeStr = joinner.join(codeOrList);
			
			StringBuffer buffer = new StringBuffer();
			if(direction==FiscalAccountingSubject.DIRECTION_BORROW){
				buffer.append("select sum(v.debitAmount) from VoucherDetail v");
			}else{
				buffer.append("select sum(v.creditAmount) from VoucherDetail v ");
			}
			buffer.append("where v.fiscalAccount.fid=:accId ");
			buffer.append("and v.voucher.voucherDate>=:startDate ");
			buffer.append("and v.voucher.voucherDate>=:endDate ");
			buffer.append("and v.voucher.recordStatus in :status ");
			buffer.append("and (");
			buffer.append(codelikeStr);
			buffer.append(")");
			
			javax.persistence.Query query = getEntityManager().createQuery(buffer.toString());
			query.setParameter("accId", accId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("status", Lists.newArrayList(Voucher.STATUS_AUDITED, Voucher.STATUS_POSTED));

			Object data = query.getSingleResult();
			if(data!=null)return (BigDecimal)data;
			return BigDecimal.ZERO;
		}
	}

}
