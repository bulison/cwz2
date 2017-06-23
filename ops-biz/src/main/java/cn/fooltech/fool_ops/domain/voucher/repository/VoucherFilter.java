package cn.fooltech.fool_ops.domain.voucher.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface VoucherFilter {

	/**
	 * 过滤与凭证相关的单据
	 * @param criteria
	 * @param vo
	 */
	public default Predicate addVoucherFilter(Root<?> root, Class<?> clazz,CriteriaQuery<?> query, CriteriaBuilder builder, VoucherMakeVo vo){
		Integer flag = vo.getVoucherTag();
		String voucherWordNumber = vo.getVoucherWordNumber();
		List<Predicate> predicates = Lists.newArrayList();
		if(!(flag == null && StringUtils.isBlank(voucherWordNumber))){
			String accId = SecurityUtil.getFiscalAccountId();
			
			//过滤未生成凭证的单据
			if(flag != null && flag == 0){
				Predicate predicate = getExistVoucherPredicate(root, clazz, query, builder, vo, accId);
		        predicates.add(builder.not(predicate));
			}
			//过滤已生成凭证的单据
			else if(flag != null && flag == 1 && StringUtils.isBlank(voucherWordNumber)){
				Predicate predicate = getExistVoucherPredicate(root, clazz, query, builder, vo, accId);
		        predicates.add(predicate);
			}
			//过滤已生成凭证的单据，并匹配凭证字号
			else if(StringUtils.isNotBlank(voucherWordNumber)){
				Predicate predicate = getExistVoucherAndMatchPredicate(root, clazz, query, builder, vo, accId);
		        predicates.add(predicate);
			}
		}
		return builder.and(predicates.toArray(new Predicate[] {}));
	}
	
	/**
	 * 获取已生成凭证,并匹配凭证字号的单据的条件
	 * @param root
	 * @param clazz
	 * @param query
	 * @param builder
	 * @param vo
	 * @param accId
	 * @return
	 */
	public default Predicate getExistVoucherAndMatchPredicate(Root<?> root, Class<?> clazz,CriteriaQuery<?> query, CriteriaBuilder builder, 
			VoucherMakeVo vo, String accId){
		String voucherWordNumber = vo.getVoucherWordNumber();
		Subquery<?> subquery = query.subquery(clazz);
		Root<VoucherBill> fromProject = subquery.from(VoucherBill.class);
        subquery.select(fromProject.get("fid"));
        String like = PredicateUtils.getAnyLike(voucherWordNumber);
        subquery.where(
        		builder.equal(fromProject.get("billType"), vo.getBillType()),
        		builder.equal(fromProject.get("billId"), root.get("fid")),
        		builder.equal(fromProject.get("voucher").get("voucherWordNumber"), like),
        		builder.equal(fromProject.get("fiscalAccount").get("fid"), accId)
        	);
        return builder.exists(subquery);
	}
	
	/**
	 * 获取已生成凭证的单据的条件
	 * @param root
	 * @param clazz
	 * @param query
	 * @param builder
	 * @param vo
	 * @param accId
	 * @return
	 */
	public default Predicate getExistVoucherPredicate(Root<?> root, Class<?> clazz,CriteriaQuery<?> query, CriteriaBuilder builder, 
			VoucherMakeVo vo, String accId){
		Subquery<?> subquery = query.subquery(clazz);
		Root<VoucherBill> fromProject = subquery.from(VoucherBill.class);  
        subquery.select(fromProject.get("fid"));
        subquery.where(
        		builder.equal(fromProject.get("billType"), vo.getBillType()),
        		builder.equal(fromProject.get("billId"), root.get("fid")),
        		builder.equal(fromProject.get("fiscalAccount").get("fid"), accId)
        	);
        return builder.exists(subquery);
	}
}
