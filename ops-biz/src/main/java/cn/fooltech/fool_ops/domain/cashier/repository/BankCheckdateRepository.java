package cn.fooltech.fool_ops.domain.cashier.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.vo.BankCheckdateVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 轧账日期  持久层
 * @author cwz
 *
 */
public interface BankCheckdateRepository extends JpaRepository<BankCheckdate, String>,JpaSpecificationExecutor<BankCheckdate> {
	
	public default Page<BankCheckdate> query(BankCheckdateVo bankCheckdateVo,Pageable pageable){
		Page<BankCheckdate> findAll = findAll(new Specification<BankCheckdate>() {
			
			@Override
			public Predicate toPredicate(Root<BankCheckdate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				if (accountId != null) {
					predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		return findAll;
	}
	
	@Query("select b from BankCheckdate b where fiscalAccount.fid=?1")
	public List<BankCheckdate> getAll(String fiscalAccountId);
	
	
	@Query("select b from BankCheckdate b where fiscalAccount.fid=?1 order by checkDate desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public BankCheckdate getMaxCheckDate(String accountId);

	/**
	 * 统计总数
	 * @return
	 */
	@Query("select count(b) from BankCheckdate b where fiscalAccount.fid=?1")
	public Long countAll(String accountId);
}
