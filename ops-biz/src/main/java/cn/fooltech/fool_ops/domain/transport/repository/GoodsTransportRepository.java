package cn.fooltech.fool_ops.domain.transport.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.fooltech.fool_ops.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * 货品运输计价换算关系
 * @author cwz
 * @date 2016-12-5
 */
public interface GoodsTransportRepository extends JpaRepository<GoodsTransport, String>, FoolJpaSpecificationExecutor<GoodsTransport> {

	/**
	 *  收货地址查询，模糊查询<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public default Page<GoodsTransport> query(GoodsTransportVo vo,Pageable pageable){
		Page<GoodsTransport> findAll = findAll(new Specification<GoodsTransport>() {
			
			@Override
			public Predicate toPredicate(Root<GoodsTransport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				if(!Strings.isNullOrEmpty(vo.getGoodsId())){
					predicates.add(builder.equal(root.get("goods").get("fid"),vo.getGoodsId()));
				}
				if(!Strings.isNullOrEmpty(vo.getGoodSpecId())){
					predicates.add(builder.equal(root.get("goodSpec").get("fid"),vo.getGoodSpecId()));
				}
//				String searchKey = vo.getSearchKey();
				//关键字搜索
//				if(StringUtils.isNotBlank(searchKey)){
//					predicates.add(
//						builder.or(
//								builder.like(root.get("code"), PredicateUtils.getAnyLike(searchKey)),
//								builder.like(root.get("name"), PredicateUtils.getAnyLike(searchKey))
//						)
//					);
//				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		},pageable);
		return findAll;
	}

	/**
	 *  收货地址查询
	 */
	public default List<GoodsTransport> queryList(String goodsId, String specId){
		return findAll(new Specification<GoodsTransport>() {

			@Override
			public Predicate toPredicate(Root<GoodsTransport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				//predicates.add(builder.equal(root.get("accId").get("fid"), SecurityUtil.getFiscalAccountId()));
				predicates.add(builder.equal(root.get("org").get("fid"), SecurityUtil.getCurrentOrgId()));
				predicates.add(builder.equal(root.get("goods").get("fid"), goodsId));
				if(!Strings.isNullOrEmpty(specId)){
					predicates.add(builder.equal(root.get("goodSpec").get("fid"), specId));
				}else{
					predicates.add(builder.isNull(root.get("goodSpec")));
				}
				Predicate predicate = builder.and(predicates.toArray(new Predicate[] {}));
				return predicate;
			}
		});
	}


	/**
	 * 判断货品ID+货品属性ID+装运方式ID+运输计价单位ID不能重复
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param shipmentTypeId	装运方式ID
	 * @param transportUnitId	运输计价单位ID
	 * @return
	 */
	@Query("select count(a) from GoodsTransport a where goods.fid=?1 and goodSpec.fid=?2 and shipmentType.fid=?3 and transportUnit.fid=?4 and org.fid=?5")
	public Long findRepeat(String goodsId,String goodSpecId,String shipmentTypeId,String transportUnitId,String orgId);
	@Query("select count(a) from GoodsTransport a where goods.fid=?1 and goodSpec.fid=?2 and shipmentType.fid=?3 and transportUnit.fid=?4 and org.fid=?5 and fid !=?6")
	public Long findRepeatById(String goodsId,String goodSpecId,String shipmentTypeId,String transportUnitId,String orgId,String id);
	/**
	 * 判断货品ID+货品属性ID+装运方式ID+运输计价单位ID不能重复
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param shipmentTypeId	装运方式ID
	 * @param transportUnitId	运输计价单位ID
	 * @return
	 */
	@Query("select count(a) from GoodsTransport a where goods.fid=?1  and shipmentType.fid=?2 and transportUnit.fid=?3 and org.fid=?4")
	public Long findRepeat(String goodsId,String shipmentTypeId,String transportUnitId,String orgId);
    @Query("select count(a) from GoodsTransport a where goods.fid=?1  and shipmentType.fid=?2 and transportUnit.fid=?3 and org.fid=?4 and fid !=?5")
    public Long findRepeatById(String goodsId,String shipmentTypeId,String transportUnitId,String orgId,String id);


    /**
	 * 根据货品ID+货品属性ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1  and goodSpec.fid=?2 and transportUnit.fid=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public GoodsTransport findTopBy(String goodsId,String goodSpecId,String transportUnitId);

	/**
	 * 根据货品ID+货品属性ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1  and goodSpec.fid=?2 and transportUnit.fid=?3 and shipmentType.fid=?4")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public GoodsTransport findTopBySpecNotNull(String goodsId,String goodSpecId,String transportUnitId, String shipmentTypeId);

	/**
	 * 根据货品ID+货品属性ID=null+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1 and goodSpec is null and transportUnit.fid=?2 and shipmentType.fid=?3")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public GoodsTransport findTopBySpecIsNull(String goodsId,String transportUnitId, String shipmentTypeId);

	/**
	 * 根据货品ID+运输计价单位ID查询
	 * @param goodsId			货品ID
	 * @param transportUnitId	运输计价单位ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1  and goodSpec.fid is null and transportUnit.fid=?2")
	@QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
	public GoodsTransport findTopBy(String goodsId,String transportUnitId);


	/**
	 * 根据货品ID查询换算关系不为1的记录
	 * @param goodsId	货品ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1  and goodSpec.fid is null and conversionRate<>1")
	public List<GoodsTransport> findByGoods(String goodsId);

	/**
	 * 根据货品ID、属性ID查询换算关系不为1的记录
	 * @param goodsId	货品ID
	 * @param specId	属性ID
	 * @return
	 */
	@Query("select a from GoodsTransport a where goods.fid=?1  and goodSpec.fid=?2 and conversionRate<>1")
	public List<GoodsTransport> findByGoods(String goodsId, String specId);
}	

