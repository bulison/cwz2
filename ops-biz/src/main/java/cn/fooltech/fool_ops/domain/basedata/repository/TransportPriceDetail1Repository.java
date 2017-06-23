package cn.fooltech.fool_ops.domain.basedata.repository;


import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail1;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface TransportPriceDetail1Repository extends FoolJpaRepository<TransportPriceDetail1, String>,FoolJpaSpecificationExecutor<TransportPriceDetail1> {

	public default Page<TransportPriceDetail1> findByBillId(String billId, Pageable pageable){
        return findAll(new Specification<TransportPriceDetail1>() {
            @Override
            public Predicate toPredicate(Root<TransportPriceDetail1> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                String accId = SecurityUtil.getFiscalAccountId();
                predicateList.add(criteriaBuilder.equal(root.get("fiscalAccount").get("fid"), accId));
                if(!Strings.isNullOrEmpty(billId)){
                	 predicateList.add(criteriaBuilder.equal(root.get("bill").get("id"), billId));
                }
                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
	}

	/**
	 * 根据运输报价id删除
	 * @param tempId
	 */
	@Modifying
	@Query("delete from TransportPriceDetail1 a where bill.id=?1")
	public void delByTempId(String tempId);
	/**
	 * 根据id，账套，关联id查询从1记录
	 * @param accId
	 * @param templateId
	 * @param id
	 * @return
	 */
    @Query("select a from TransportPriceDetail1 a where fiscalAccount.fid=?1 and bill.id=?2 and id=?3")
	public TransportPriceDetail1 queryByFid(String accId,String templateId,String id);
	
}
