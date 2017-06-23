package cn.fooltech.fool_ops.domain.wage.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.wage.entity.WageDetail;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;

/**
 * 工资明细持久层
 * 
 * @author cwz
 * @date 2016-10-27
 */
public interface WageDetailRepository extends FoolJpaRepository<WageDetail, String>, JpaSpecificationExecutor<WageDetail> {

	@Query("select count(w) from WageDetail w where formula.fid=?1")
	public Long countByColumn(String fid);

	@Query("select w from WageDetail w where wage.fid=?1")
	public List<WageDetail> findByWageId(String wageId);

	/**
	 * 根据工资单号查询和人员ID查询
	 * 
	 * @param wageId
	 * @param memberId
	 * @return
	 */
	public default List<WageDetail> getByWageMember(String wageId, String memberId) {
		List<WageDetail> list = findAll(new Specification<WageDetail>() {
			@Override
			public Predicate toPredicate(Root<WageDetail> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.<String>get("wage").get("fid"), wageId));
				if (!Strings.isNullOrEmpty(memberId)) {
					predicates.add(builder.equal(root.<String>get("member").get("fid"), memberId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
		return list;

	}
	/**
	 * 获取存在的工资列
	 * @param fid
	 * @return
	 */
	@Query("select DISTINCT(w.formula.fid) from WageDetail w where wage.fid=?1")
	public List<String> getExistFormula(String wageId);

	/**
	 * 根据工资公式fid查询
	 * @param fid
	 * @return
	 */
	@Query("select w from WageFormula w where fid in(?1)")
	public  List<WageFormula> getByWageFormula(List<String> fid);


	/**
	 * 根据账套id删除工资明细
	 * @param fiscalAccountId 账套id
	 */
	@Query("delete from WageDetail a where a.wage.fid=(select b.fid from Wage b where b.fiscalAccount=?1) ")
	@Modifying
	public void delByFiscalAccountId(String fiscalAccountId);

	/**
	 * 根据部门、工资项目，获取某个月的工资单ID<br>
	 * 过滤已生成凭证和未审核的单据<br>
	 * @param fiscalAccountId 财务账套ID
	 * @param month 月份(年+月)
	 * @param formulaId 工资项目ID
	 * @param deptId 部门ID
	 * @return
	 * @author rqh
	 */
	@SuppressWarnings("unchecked")
	public default List<String> getWageIds(String fiscalAccountId, Date month, String formulaId, String deptId){
		String sql = "select distinct(wage.fid) from tbd_wage_detail detail inner join tbd_wage wage on detail.fwage_id = wage.fid " + 
				 "where wage.facc_id = :fiscalAccountId and wage.fdep_id = :deptId and date_format(wage.fdate, '%Y%m') = :month and detail.fcolumn_id = :formulaId " +
				 "and wage.fauditor is not null and not exists (select * from tbd_voucher_bill where facc_id = :fiscalAccountId and fbill_type = :billType and fbill_id = wage.fid)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("formulaId", formulaId);
		query.setParameter("fiscalAccountId", fiscalAccountId);
		query.setParameter("deptId", deptId);
		query.setParameter("billType", WarehouseBuilderCodeHelper.gzd);
		query.setParameter("month", DateUtilTools.date2String(month, DateUtilTools.DATE_PATTERN_YYYYMM));
		return query.getResultList();
	}
	
	/**
	 * 根据工资项目，获取某工资单下的工资明细<br>
	 * 过滤已生成凭证和未审核的单据<br>
	 * @param fiscalAccountId 财务账套ID
	 * @param wageId 工资单ID
	 * @param formulaId 工资项目ID
	 * @return
	 * @author rqh
	 */
	@SuppressWarnings("unchecked")
	public default List<String> getDetailIds(String fiscalAccountId, String wageId, String formulaId){
		String sql = "select detail.fid from tbd_wage_detail detail inner join tbd_wage wage on detail.fwage_id = wage.fid " + 
				 "where wage.facc_id = :fiscalAccountId and wage.fid = :wageId and detail.fcolumn_id = :formulaId " +
				 "and wage.fauditor is not null and not exists (select * from tbd_voucher_bill where facc_id = :fiscalAccountId and fbill_type = :billType and fbill_id = wage.fid)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("formulaId", formulaId);
		query.setParameter("wageId", wageId);
		query.setParameter("fiscalAccountId", fiscalAccountId);
		query.setParameter("billType", WarehouseBuilderCodeHelper.gzd);
		return query.getResultList();
	}
	
	/**
	 * 获取某工资单的金额总数
	 * @param wageId 工资ID
	 * @param formulaId 工资项目ID
	 */
	@Query("select sum(w.value) from WageDetail w where w.wage.fid=?1 and w.formula.fid=?2")
	public BigDecimal sumAmountByWageIdAndFormulaId(String wageId, String formulaId);
	
	/**
	 * 获取某工资单的金额总数
	 * @param wageId 工资ID
	 */
	@Query("select sum(w.value) from WageDetail w where w.wage.fid=?1")
	public BigDecimal sumAmountByWageId(String wageId);
}
