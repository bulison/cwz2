package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.PurchasePrice;
import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface PurchasePriceRepository extends FoolJpaRepository<PurchasePrice, String>,FoolJpaSpecificationExecutor<PurchasePrice> {

    /**
     * 查找分页
     * @param accId
     * @param vo
     * @param pageable
     * @return
     */
    public default Page<PurchasePrice> findPageBy(String accId, String creatorId, PurchasePriceVo vo,
                                                  Pageable pageable){
        return findAll(new Specification<PurchasePrice>() {
            @Override
            public Predicate toPredicate(Root<PurchasePrice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.get("accId"), accId));
                //predicates.add(builder.equal(root.get("creatorId"), creatorId));


                if (!Strings.isNullOrEmpty(vo.getGoodsId())) {
                    predicates.add(builder.equal(root.get("goods").get("fid"), vo.getGoodsId()));
                }

                if (!Strings.isNullOrEmpty(vo.getId())) {
                    predicates.add(builder.equal(root.get("id"), vo.getId()));
                }

                if (!Strings.isNullOrEmpty(vo.getCode())) {
                    predicates.add(builder.equal(root.get("code"), vo.getCode()));
                }
                if (!Strings.isNullOrEmpty(vo.getSupplierId())) {
                    predicates.add(builder.equal(root.get("supplierId"), vo.getSupplierId()));
                }

                if (!Strings.isNullOrEmpty(vo.getGoodSpecId())) {
                    predicates.add(builder.equal(root.get("goodSpecId"), vo.getGoodSpecId()));
                }

                if (!Strings.isNullOrEmpty(vo.getUnitId())) {
                    predicates.add(builder.equal(root.get("unitId"), vo.getUnitId()));
                }

                if ((vo.getFactoryPrice()!=null)) {
                    predicates.add(builder.equal(root.get("factoryPrice"), vo.getFactoryPrice()));
                }

                if (vo.getTaxPoint()!=null) {
                    predicates.add(builder.equal(root.get("taxPoint"), vo.getTaxPoint()));
                }
//
                if ((vo.getAfterTaxPrice()!=null)) {
                    predicates.add(builder.equal(root.get("afterTaxPrice"), vo.getAfterTaxPrice()));
                }
//
                if (vo.getPickUpCharge()!=null) {
                    predicates.add(builder.equal(root.get("pickUpCharge"), vo.getPickUpCharge()));
                }
                if (vo.getDeliveryPrice()!=null) {
                    predicates.add(builder.equal(root.get("deliveryPrice"), vo.getDeliveryPrice()));
                }

                if (!Strings.isNullOrEmpty(vo.getDeliveryPlace())) {
                    predicates.add(builder.equal(root.get("deliveryPlace"), vo.getDeliveryPlace()));
                }

                if (!Strings.isNullOrEmpty(vo.getCreatorId())) {
                    predicates.add(builder.equal(root.get("creatorId"), vo.getCreatorId()));
                }
                if (!Strings.isNullOrEmpty(vo.getOrgId())) {
                    predicates.add(builder.equal(root.get("orgId"), vo.getOrgId()));
                }

                if (!Strings.isNullOrEmpty(vo.getAccId())) {
                    predicates.add(builder.equal(root.get("accId"), vo.getAccId()));
                }

                //单据日期搜索

                if(vo.getStartDay() != null){
                    Date startDate = vo.getStartDay();
                    predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
                }
                if(vo.getEndDay() != null){
                    Date endDate = vo.getEndDay();
                    predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
                }

                //日期不传则默认显示一周内的数据
                if(vo.getStartDay() == null && vo.getEndDay() == null){
                    Date beforeWeek = DateUtilTools.addDateTime(DateUtilTools.now(), -7, 0, 0, 0);
                    predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), beforeWeek));
                }

                if (StringUtils.isNotBlank(vo.getSearchKey())) {
                    String likeKey = vo.getSearchKey().trim();
                    likeKey = PredicateUtils.getAnyLike(likeKey);
                    predicates.add(builder.or(
                            builder.like(root.get("goods").get("name"), likeKey),
                            builder.like(root.get("code"), likeKey)
                    ));
                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }
    /**
     * 可按时间段、供应商、货品等进行查询
     *
     * @param orgId
     * @param searchKey
     * @param maxSize
     * @return
     */
    public default List<PurchasePrice> vagueSearch(String orgId, String userId, String searchKey,
                                                        int maxSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "billDate");
        return findAll(new Specification<PurchasePrice>() {

            @Override
            public Predicate toPredicate(Root<PurchasePrice> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));



                if (cn.fooltech.fool_ops.utils.StringUtils.isNotBlank(searchKey)) {
                    String likeKey = searchKey.trim();
                    likeKey = PredicateUtils.getAnyLike(likeKey);
                    predicates.add(builder.or(
                            builder.like(root.get("supplierId"), likeKey)
//                            builder.like(root.get("drivingLicese"), likeKey),
//                            builder.like(root.get("ownerName"), likeKey),
//                            builder.like(root.get("ownerIdCard"), likeKey),
//                            builder.like(root.get("contactPhone"), likeKey)
                    ));
//                    }
                }
                //单据日期搜索

//                if(vo.getStartDay() != null){
//                    Date startDate = vo.getStartDay();
//                    predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDate));
//                }
//                if(vo.getEndDay() != null){
//                    Date endDate = vo.getEndDay();
//                    predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDate));
//                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, sort, maxSize);
    }
	/**
	 * 根据发货地查询记录
	 * @param fid	发货地id
	 * @return
	 */
	@Query("select count(a) from PurchasePrice a where a.deliveryPlace=?1")
	public Long queryByDeliveryPlaceCount(String fid);

	/**
	 * 保存时检索相同供应商、货品ID、货品属性ID、发货地且状态=有效的记录
	 * @param supplierId 	供应商
	 * @param goodsId	 	货品
	 * @param deliveryPrice	发货地
 	 * @param accId			账套
	 * @return
	 */
	@Query(nativeQuery=true,value="SELECT a.* from tsb_purchase_price a where 1=1 and a.fenable=1 and  a.fsupplier_id=?1 and a.fgoods_id=?2 and a.fdelivery_place=?3 and a.facc_id=?4  and a.fgood_spec_id is null ORDER BY a.fbill_date desc,a.fupdate_time desc ")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PurchasePrice queryBySupplier(String supplierId,String goodsId,String deliveryPlace,String accId);
	/**
	 * 保存时检索相同供应商、货品ID、货品属性ID、发货地且状态=有效的记录
	 * @param supplierId		供应商	
	 * @param goodsId			货品
	 * @param goodSpecId		货品属性
	 * @param deliveryPlace		发货地
	 * @param accId				账套
	 * @return
	 */
	@Query(nativeQuery=true,value="SELECT a.* from tsb_purchase_price a where 1=1  and a.fenable=1 and  a.fsupplier_id=?1 and a.fgoods_id=?2 and a.fgood_spec_id=?3 and a.fdelivery_place=?4 and a.facc_id=?5 ORDER BY a.fbill_date desc,a.fupdate_time desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PurchasePrice queryBySupplier(String supplierId,String goodsId,String goodSpecId,String deliveryPlace,String accId);

	/**
	 * 设置状态为失效，判断条件不为id的数据
	 * @param id
	 */
	@Query("update PurchasePrice set enable=0 where id !=?1 and accId=?2 and supplierId=?3 and goods.fid=?4  and deliveryPlace=?5")
	@Modifying
	public void updateById(String id,String accId,String supplierId,String goodsId,String deliveryPlace);
	
	@Query("update PurchasePrice set enable=0 where id !=?1 and accId=?2 and supplierId=?3 and goods.fid=?4 and goodSpecId=?5 and deliveryPlace=?6")
	@Modifying
	public void updateById(String id,String accId,String supplierId,String goodsId,String goodSpecId,String deliveryPlace);

	/**
	 * 设置日状体为失效，判断条件不为id的数据
	 * @param accId
	 * @param fid		
	 * @param billDate 单据时间
	 */
	@Query(nativeQuery=true,value="update tsb_purchase_price set fenable_date=0 where facc_id=?1  and date_format(fbill_date,'%Y-%m-%d')=?3 and fid !=?2 and fsupplier_id=?4 and fgoods_id=?5 and fgood_spec_id=?6 and fdelivery_place=?7")
	@Modifying
	public void updateEnableDate(String accId,String fid,String billDate,String supplierId,String goodsId,String goodSpecId,String deliveryPlace);
	/**
	 * 设置日状体为失效，判断条件不为id的数据
	 * @param accId
	 * @param fid		
	 * @param billDate 单据时间
	 */
	@Query(nativeQuery=true,value="update tsb_purchase_price set fenable_date=0 where facc_id=?1  and date_format(fbill_date,'%Y-%m-%d')=?3 and fid !=?2 and fsupplier_id=?4 and fgoods_id=?5 and fdelivery_place=?6 and fgood_spec_id is null")
	@Modifying
	public void updateEnableDate(String accId,String fid,String billDate,String supplierId,String goodsId,String deliveryPlace);

    /**
     * 根据发货地查询记录
     * @param deliveryPlaceId	发货地id
     * @return
     */
    @Query("select a from PurchasePrice a where a.deliveryPlace = ?1 and a.enable=1")
    public List<PurchasePrice> findByDeliveryPlace(String deliveryPlaceId);

    /**
     * 根据属性ID统计
     * @param specId
     * @return
     */
    @Query("select count(*) from PurchasePrice d where d.goodSpecId=?1")
    public Long countByGoodsSpecId(String specId);
    /**
     * 根据有效日期统计
     */
    @Query("select d from PurchasePrice d where d.effectiveDate >=?1 and d.effectiveDate<?2")
    public List<PurchasePrice> findByEffectiveDate(Date yesterday,Date today);
}
