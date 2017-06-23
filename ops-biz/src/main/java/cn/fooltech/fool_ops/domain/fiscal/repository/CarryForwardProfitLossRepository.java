package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.fiscal.entity.CarryForwardProfitLoss;

public interface CarryForwardProfitLossRepository extends FoolJpaRepository<CarryForwardProfitLoss, String>, 
	FoolJpaSpecificationExecutor<CarryForwardProfitLoss> {

	/**
	 * 根据类型和账套ID查询
	 * @param type
	 * @param accId
	 * @return
	 */
	@Query("select c from CarryForwardProfitLoss c where c.type=?1 and c.fiscalAccount.fid=?2")
	public List<CarryForwardProfitLoss> findByTypeAndAccId(int type, String accId);

	/**
	 * 制作凭证
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param fiscalPeriodId 会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @param creatorId 创建人ID
	 * @param deptId 部门ID
	 * return 凭证字号
	 */
	public default String saveVoucher(String orgId, String fiscalAccountId, String fiscalPeriodId, String voucherWordId,
			String creatorId, String deptId, Integer type, Date voucherDate, String resume){
		String sql = "call carry_forward_profit_loss(:orgId, :accountId, :periodId, :voucherWordId, :creatorId, :deptId, :type, :voucherDate, :resume)";
		javax.persistence.Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("orgId", orgId);
		query.setParameter("accountId", fiscalAccountId);
		query.setParameter("periodId", fiscalPeriodId);
		query.setParameter("voucherWordId", voucherWordId);
		query.setParameter("creatorId", creatorId);
		query.setParameter("deptId", deptId);
		query.setParameter("type", type);
		query.setParameter("voucherDate", voucherDate);
		query.setParameter("resume", resume);
		Object data = query.getSingleResult();
		if(data!=null){
			return data.toString();
		}
		return " ";
	}

	/**
	 * 根据账套和类型查询分页
	 * @param accId
	 * @param type
	 * @param page
	 * @return
	 */
	@Query("select c from CarryForwardProfitLoss c where c.type=?2 and c.fiscalAccount.fid=?1")
	public Page<CarryForwardProfitLoss> findPageBy(String accId, int type, Pageable page);
	
	/**
	 * 根据参数统计记录
	 * @param fiscalAccountId 财务账套ID
	 * @param inSubjectId 转入科目ID
	 * @param outSubjectId 转出科目ID
	 * @return
	 */
	@Query("select count(*) from CarryForwardProfitLoss c where c.fiscalAccount.fid=?1 and c.inSubject.fid=?2 and c.outSubject.fid=?3")
	public Long countBy(String fiscalAccountId, String inSubjectId, String outSubjectId);

	/**
	 * 统计某个科目被引用的次数
	 * @param subjectId 科目ID
	 * @param type 类别:1--结转损益；2--结转制造费用
	 * @return
	 */
	@Query("select count(*) from CarryForwardProfitLoss c where (c.outSubject.fid=?1 or c.inSubject.fid=?1) and c.type=?2")
	public Long countBySubjectIdAndType(String subjectId, Integer type);
}
