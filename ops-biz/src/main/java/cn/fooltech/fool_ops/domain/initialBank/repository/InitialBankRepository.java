package cn.fooltech.fool_ops.domain.initialBank.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalInitBalance;
import cn.fooltech.fool_ops.domain.initialBank.entity.InitialBank;
import cn.fooltech.fool_ops.domain.initialBank.vo.InitialBankVo;
import cn.fooltech.fool_ops.utils.StringUtils;

/**
 * 现金银行期初 持久层
 * 
 * @author cwz
 * @date 2016-11-3
 */
public interface InitialBankRepository
		extends JpaRepository<InitialBank, String>, JpaSpecificationExecutor<InitialBank> {

	/**
	 * 判断现金银行期初账是否存在
	 * 
	 * @param accountId
	 *            财务账套ID
	 * @param bankId
	 * @param selfId
	 * @return
	 */
	public default boolean existInitialBank(String accountId, String bankId, String selfId) {
		List<InitialBank> list = findAll(new Specification<InitialBank>() {
			@Override
			public Predicate toPredicate(Root<InitialBank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (StringUtils.isNotBlank(selfId)) {
					predicates.add(builder.notEqual(root.<String>get("fid"), selfId));
				}
				predicates.add(builder.equal(root.<String>get("bank").get("fid"), bankId));
				predicates.add(builder.equal(root.<String>get("fiscalAccount").get("fid"), accountId));
				predicates.add(builder.equal(root.<String>get("bank").get("fid"), bankId));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				
				return predicate;

			}
		});
		return list.size() > 0;
	}

	/**
	 * 查询现金银行期初列表信息<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<InitialBank> query(InitialBankVo initialBankVo,Pageable pageable,String orgId,String faccId ){
		return findAll(new Specification<InitialBank>() {
			
			@Override
			public Predicate toPredicate(Root<InitialBank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), orgId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), faccId));
				root.join("bank",JoinType.LEFT);
				if(StringUtils.isNotBlank(initialBankVo.getSearchKey())){
					predicates.add(builder.or(
							builder.like(root.get("bank").get("code"), "%"+ initialBankVo.getSearchKey()+"%"),
							builder.like(root.get("bank").get("name"), "%"+ initialBankVo.getSearchKey()+"%"),
							builder.like(root.get("bank").get("account"), "%"+ initialBankVo.getSearchKey()+"%")
							));
			}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
				return predicate;
			}
		}, pageable);
	}
	
	/**
	 * 根据银行ID统计现金银行期初的总金额
	 * @param bankId 银行ID
	 * @param accountId 财务账套ID
	 * @return
	 */
	@Query("select sum(b.amount) from InitialBank b where b.fiscalAccount.fid=?2 and b.bank.fid=?1")
	public BigDecimal sumByBank(String bankId, String accId);
	
	/**
	 * 根据银行ID统计
	 * @param bankId
	 * @return
	 */
	@Query("select count(*) from InitialBank b where b.bank.fid=?1")
	public Long countByBankId(String bankId);
}
