package cn.fooltech.fool_ops.domain.cashier.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.cashier.entity.BankInit;
import cn.fooltech.fool_ops.domain.cashier.vo.BankInitVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 出纳-初始银行设置  持久层
 * @author cwz
 *
 */
public interface BankInitRepository extends JpaRepository<BankInit, String>,JpaSpecificationExecutor<BankInit> {

	/**
	 * 获取记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @return
	 * @author rqh
	 */
	@Query("select b from BankInit b where fiscalAccount.fid=?1 and subject.fid=?2")
	public BankInit getRecord(String fiscalAccountId, String subjectId);
	
	/**
	 * 统计有多少已启用的数据
	 * @return
	 */
	@Query("select count(b) from BankInit b where fiscalAccount.fid=?1 and start=1")
	public Long countStart(String accountId);
	
	/**
	 * 根据账套获取所有数据
	 * @param fiscalAccountId
	 * @return
	 */
	@Query("select b from BankInit b where fiscalAccount.fid=?1")
	public List<BankInit> getAll(String fiscalAccountId);
	
	/**
	 * 根据科目ID统计
	 * @return
	 */
	@Query("select count(b) from BankInit b where subject.fid=?1")
	public long countBySubjectId(String subjectId);
	
	public default Page<BankInit> query(BankInitVo bankInitVo,Pageable pageable){
		return findAll(new Specification<BankInit>() {
			@Override
			public Predicate toPredicate(Root<BankInit> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.get("fiscalAccount").get("fid"),accountId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
	}
	/**
	 * 根据科目获取记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @return
	 * @author rqh
	 */
	@Query("select b from BankInit b where fiscalAccount.fid=?1 and subject.fid=?2")
	public List<BankInit> getBySubjectRecord(String fiscalAccountId, String subjectId);
}
