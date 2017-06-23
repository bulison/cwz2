package cn.fooltech.fool_ops.domain.asset.repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.entity.AssetDetail;
import cn.fooltech.fool_ops.domain.asset.vo.AssetDetailVo;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 固定资产卡片明细  持久层
 * @author cwz
 *
 */
public interface AssetDetailRepository extends JpaRepository<AssetDetail, String>,JpaSpecificationExecutor<AssetDetail> {
	
	public default List<AssetDetail> query(String assetId){
		if(StringUtils.isBlank(assetId)){
			return Collections.emptyList();
		}
		Sort sort = new Sort(Direction.ASC, "type","date","createTime");
		List<AssetDetail> findAll = findAll(new Specification<AssetDetail>() {
			@Override
			public Predicate toPredicate(Root<AssetDetail> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
					predicates.add(builder.equal(root.get("asset").get("fid"), assetId));
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		}, sort);
		return findAll;
	}
	/**
	 * 分页查询所有固定资产卡片
	 * @param vo
	 * @param pageable
	 * @author rqh
	 */
	public default Page<AssetDetail> queryByPage(AssetDetailVo vo , Pageable pageable){
		return findAll(new Specification<AssetDetail>() {
			@Override
			public Predicate toPredicate(Root<AssetDetail> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				String accountId = SecurityUtil.getFiscalAccountId();
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accountId));
				//类型
				if(vo.getType() != null){
					predicates.add(builder.equal(root.get("type"), vo.getType()));
				}
				//日期
				if(!Strings.isNullOrEmpty(vo.getStartDay())){
					predicates.add(builder.greaterThanOrEqualTo(root.get("date"), DateUtils.getDateFromString(vo.getStartDay())));
				}
				if(!Strings.isNullOrEmpty(vo.getEndDay())){
					predicates.add(builder.lessThanOrEqualTo(root.get("date"), DateUtils.getDateFromString(vo.getEndDay())));
				}
				//固定资产编号
				if(StringUtils.isNotBlank(vo.getAssetCode())){
					String anyLike = PredicateUtils.getAnyLike(vo.getAssetCode());
					predicates.add(builder.like(root.get("asset").get("assetCode"), anyLike));
				}
				//固定资产名称
				if(StringUtils.isNotBlank(vo.getAssetName())){
					String anyLike = PredicateUtils.getAnyLike(vo.getAssetName());
					predicates.add(builder.like(root.get("asset").get("assetName"), anyLike));
				}

				//过滤已生成过凭证的记录
				if(vo.getShowGened()!=null){
					Subquery<AssetDetail> subquery = query.subquery(AssetDetail.class);
					Root<VoucherBill> fromProject = subquery.from(VoucherBill.class);
					subquery.select(fromProject.get("fid"));
					subquery.where(
							builder.equal(fromProject.get("billId"), root.get("fid"))
					);
					if(vo.getShowGened()== Constants.SHOW){
						predicates.add(builder.exists(subquery));
					} if(vo.getShowGened()== Constants.NOTSHOW){
						predicates.add(builder.not(builder.exists(subquery)));
					}

				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
//				query.select(root.join("asset",JoinType.INNER));
//				query.where(predicate);
				return predicate;
				
			}
		} , pageable);
	}
	/**
	 * 根据类型统计
	 * @param assetId
	 * @param type
	 * @return
	 */
	@Query("select count(a) from AssetDetail a where asset.fid=?1 and type=?2")
	public Long countByType(String assetId, int type);
	
	/**
	 * 根据类型统计
	 * @param assetId
	 * @param type
	 * @return
	 */
	@Query("select max(date) from AssetDetail a where asset.fid=?1 and type=?2")
	public Date getMaxDate(String assetId, int type);
	
	/**
	 * 获取固定资产的资产购入金额
	 * @param assetId 固定资产ID
	 * @return
	 */
	@Query("select sum(amount) from AssetDetail a where asset.fid=?1 and type=?2")
	public BigDecimal getAssetAmount(String assetId, int detailType); 

}
