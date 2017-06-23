package cn.fooltech.fool_ops.domain.initialPay.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.initialPay.vo.InitialPayVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.SecurityUtil;

public interface InitialPayRepository extends JpaRepository<WarehouseBill, String>,JpaSpecificationExecutor<WarehouseBill> {


	/**
	 * 查询期初应付列表信息，按照期初应付主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<WarehouseBill> query(InitialPayVo vo,Pageable pageable){
		Page<WarehouseBill> page = findAll(new Specification<WarehouseBill>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseBill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
				predicates.add(builder.equal(root.get("billType"), WarehouseBuilderCodeHelper.qcyf));
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				if(StringUtils.isNotBlank(vo.getSupplierId())){
					predicates.add(builder.equal(root.get("supplier").get("fid"),vo.getSupplierId()));
				}
				if(StringUtils.isNotBlank(vo.getMemberId())){
					predicates.add(builder.equal(root.get("inMember").get("fid"),vo.getMemberId()));
				}
//				Join<WarehouseBill, Supplier> suJoin = root.join(root.getModel().getSingularAttribute("supplier",Supplier.class));
//				Join<WarehouseBill, Member> meJoin = root.join(root.getModel().getSingularAttribute("inMember",Member.class));
				if (StringUtils.isNotBlank(vo.getSearchKey())) {
					predicates.add(builder.or(
//							builder.like(root.get("supplier").get("name"), "%"+ vo.getSearchKey()+"%"),
//							builder.like(root.get("inMember").get("username"), "%"+ vo.getSearchKey()+"%")
//							builder.like(suJoin.get("name"), "%"+ vo.getSearchKey()+"%"),
//							builder.like(meJoin.get("username"), "%"+ vo.getSearchKey()+"%")	
							builder.like(root.get("code"), "%"+ vo.getSearchKey()+"%")
							));
				}

				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, pageable);
		return page;
	}
}
