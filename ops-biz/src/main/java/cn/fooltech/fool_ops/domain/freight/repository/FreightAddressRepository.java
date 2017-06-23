package cn.fooltech.fool_ops.domain.freight.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
/**
 * 收货地址 持久层
 * @author cwz
 * @date 2016-12-5
 */
public interface FreightAddressRepository extends JpaRepository<FreightAddress, String>, FoolJpaSpecificationExecutor<FreightAddress> {

	/**
	 *  收货地址查询，模糊查询<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default List<FreightAddress> query(FreightAddressVo vo){
		Sort sort = new Sort(Direction.ASC, "code");
		List<FreightAddress> findAll = findAll(new Specification<FreightAddress>() {
			
			@Override
			public Predicate toPredicate(Root<FreightAddress> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), SecurityUtil.getFiscalAccountId()));
//				predicates.add(builder.isNull(root.get("parent").get("fid")));
//				predicates.add(builder.equal(root.get("level"),0));
				if(!Strings.isNullOrEmpty(vo.getCode())){
					predicates.add(builder.equal(root.get("code"),vo.getCode()));
				}
				if(!Strings.isNullOrEmpty(vo.getName())){
					predicates.add(builder.equal(root.get("name"),vo.getName()));
				}
				if(!Strings.isNullOrEmpty(vo.getAssetgroundId())){
					predicates.add(builder.equal(root.get("ground").get("fid"),vo.getAssetgroundId()));
				}
				if(vo.getEnable()!=null){
					predicates.add(builder.equal(root.get("enable"),vo.getEnable()));
				}
				if (vo.getFlag() != null) {
					predicates.add(builder.equal(root.get("flag"), vo.getFlag()));
				}
				if(vo.getRecipientSign()!=null){
					predicates.add(builder.equal(root.get("recipientSign"), vo.getRecipientSign()));
				}
				String searchKey = vo.getSearchKey();
				//关键字搜索
				if(StringUtils.isNotBlank(searchKey)){
					predicates.add(
						builder.or(
								builder.like(root.get("code"), PredicateUtils.getAnyLike(searchKey)),
								builder.like(root.get("name"), PredicateUtils.getAnyLike(searchKey))
						)
					);
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},sort);
		return findAll;
	}
	
	@Query("select count(a) from FreightAddress a where code=?1 and fiscalAccount.fid=?2")
	public Long queryByCodeCount(String code,String faccId);
	
	@Query("select count(a) from FreightAddress a where name=?1 and fiscalAccount.fid=?2")
	public Long queryByNameCount(String name,String faccId);
	
	/**
	 * 查找父ID查找子科目 
	 * @return
	 */
	@Query("select count(*) from FreightAddress a where a.parent.fid=?1")
	public Long countByParentId(String fid);
	/**
	 * 查找父ID查找子科目 
	 * @return
	 */
	@Query("select a from FreightAddress a where a.parent.fid=?1 order by code")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FreightAddress queryByParentId(String fid);
	
	@Modifying
	@Query("update FreightAddress a set enable=0 where a.parent.fid=?1")
	public void updateChildsEnable(String fid);


	@Query("select f from FreightAddress f where f.fullParentId like ?1")
	public List<FreightAddress> findByFullParentId(String fullParentId);
	/**
	 * 根据损耗地址ID查找货运地址
	 */
	@Query("select f from FreightAddress f where f.transportLoss.fid=?1")
	public FreightAddress finByLossId(String lossId);
}	

