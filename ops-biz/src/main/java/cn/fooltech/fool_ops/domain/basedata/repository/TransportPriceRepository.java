package cn.fooltech.fool_ops.domain.basedata.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
/**
 * 运输费报价 持久层
 * @author cwz
 * @date   2016-12-12
 */
public interface TransportPriceRepository extends FoolJpaRepository<TransportPrice, String>,FoolJpaSpecificationExecutor<TransportPrice> {


	/**
	 * 统计某个单据类型下的单据数量
	 *
	 * @param orgId    机构ID
	 * @return
	 */
	@Query("select count(*) from TransportPrice b where b.org.fid=?1")
	public Long countByBillType(String orgId);

	/**
     * 根据参数查找分页
     *
     * @param pageable
     * @return
     */
    public default Page<TransportPrice> findPageBy(TransportPriceVo vo,Pageable pageable) {
        return findAll(new Specification<TransportPrice>() {
//        	查询条件：可按时间段、单号、供应商、发货地、收货地、运输方式、装运方式、报价人进行查询；单号可模糊查询；
            @Override
            public Predicate toPredicate(Root<TransportPrice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                String accId = SecurityUtil.getFiscalAccountId();
                predicateList.add(criteriaBuilder.equal(root.get("fiscalAccount").get("fid"), accId));
                String code = vo.getCode();
                //运输方式名称
                String transportTypeName = vo.getTransportTypeName();
                //装运方式名称
                String shipmentTypeName = vo.getShipmentTypeName();
                //发货地
                String deliveryPlaceName = vo.getDeliveryPlaceName();
                //收货地
                String receiptPlaceName = vo.getReceiptPlaceName();
                //承运单位
                String supplierName = vo.getSupplierName();
                //报价单位
                String transportUnitName = vo.getTransportUnitName();
                //报价人
                String creatorName = vo.getCreatorName();
                String searchKey = vo.getSearchKey();
                String startDay = vo.getStartDay();
                String endDay = vo.getEndDay();
                if (!Strings.isNullOrEmpty(startDay)) {
                	predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("billDate"), DateUtils.getDateFromString(startDay)));
                }
                if (!Strings.isNullOrEmpty(endDay)) {
                	predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("billDate"), DateUtils.getDateFromString(endDay)));
                }
                if(vo.getBillDate()!=null){
                	predicateList.add(criteriaBuilder.equal(root.get("billDate"), DateUtils.getDateFromString(vo.getBillDate())));
                }
                if (!Strings.isNullOrEmpty(code)) {
                    predicateList.add(criteriaBuilder.equal(root.get("code"), code));
                }
                if (!Strings.isNullOrEmpty(supplierName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("supplier").get("name"), supplierName));
                }
                if (!Strings.isNullOrEmpty(transportUnitName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("priceUnit").get("name"), transportUnitName));
                }
                if (!Strings.isNullOrEmpty(deliveryPlaceName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("deliveryPlace").get("name"), deliveryPlaceName));
                }
                if (!Strings.isNullOrEmpty(receiptPlaceName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("receiptPlace").get("name"), receiptPlaceName));
                }
                if (!Strings.isNullOrEmpty(transportTypeName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("transportType").get("name"), transportTypeName));
                }
                if (!Strings.isNullOrEmpty(shipmentTypeName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("shipmentType").get("name"), shipmentTypeName));
                }
                if (!Strings.isNullOrEmpty(creatorName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("creator").get("name"), creatorName));
                }
                if (vo.getDayEnable()!=null) {
                	predicateList.add(criteriaBuilder.equal(root.get("dayEnable"), vo.getDayEnable()));
                }
				if (vo.getEnable()!=null) {
					predicateList.add(criteriaBuilder.equal(root.get("enable"), vo.getEnable()));
				}
                if (StringUtils.isNotBlank(searchKey)) {
                    String key = PredicateUtils.getAnyLike(searchKey);
                    predicateList.add(criteriaBuilder.like(root.<String>get("code"), key));
                }

                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }
    /**
	 * 查询运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		运输公司ID(关联供应商)
	 * @return
	 */
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid=?6 and transportUnit.fid=?7 and enable=1 order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findByCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String transportUnitId); 
	
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid=?6 and transportUnit.fid=?7  order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findByCompanyByDel(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String transportUnitId); 
    /**
	 * 查询运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		承运单位ID(关联供应商)
	 * @param priceUnitId		报价单位ID(关联供应商)
	 * @return
	 */
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid=?6 and priceUnit.fid=?7  and transportUnit.fid=?8 and enable=1 order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findByCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String priceUnitId,String transportUnitId); 

	/**
	 * 查询有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @return
	 */
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and transportUnit.fid=?6 and enable=1 order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findByCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String transportUnitId); 
	
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and transportUnit.fid=?6 order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findByCompanyByDel(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String transportUnitId); 

	
	/**
	 * 设置状态为失效，判断条件不为id的数据
	 * @param id
	 */
	@Query("update TransportPrice set enable=0 where id !=?7 and fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and supplier.fid=?6")
	@Modifying
	public void updateById(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String id);
	/**
	 * 设置状态为失效，判断条件不为id的数据
	 * @param id
	 */
	@Query("update TransportPrice set enable=0 where id !=?6 and fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5")
	@Modifying
	public void updateById(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String id);
	/**
	 * 设置日状体为失效，判断条件不为id的数据
	 * @param accId
	 * @param fid		
	 * @param billDate 单据时间
	 */
	@Query(nativeQuery=true,value="update tsb_transport_price set fenable_date=0 where facc_id=?1  and date_format(fbill_date,'%Y-%m-%d')=?3 and fid !=?2 and fsupplier_id=?4 and fdelivery_place=?5 and freceipt_place=?6 and ftransport_type_id=?7 and fshipment_type_id=?8")
	@Modifying
	public void updateEnableDate(String accId,String fid,String billDate,String supplierId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId);
	/**
	 * 根据发货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from TransportPrice a where deliveryPlace.fid=?1")
	public Long queryByDeliveryPlaceCount(String fid);
	
	/**
	 * 根据收货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from TransportPrice a where receiptPlace.fid=?1")
	public Long queryByReceiptPlaceCount(String fid);

	/**
	 * 查询有效期内的报价记录
	 * @param date				日期 小于等于date
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @return
	 */
	@Query("select a from TransportPrice a where a.billDate<=?1 and a.fiscalAccount.fid=?2 and a.deliveryPlace.fid=?3 and a.receiptPlace.fid=?4 and a.transportType.fid=?5 and shipmentType.fid=?6 order by billDate desc,updateTime desc")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public TransportPrice findYesterdayRecord(Date date, String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId);
	/**
	 * 查询其他运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		运输公司ID(关联供应商)
	 * @param pageable
	 * @return
	 */
	@Query("select a from TransportPrice a where fiscalAccount.fid=?1 and deliveryPlace.fid=?2 and receiptPlace.fid=?3 and transportType.fid=?4 and shipmentType.fid=?5 and enable=1")
	public Page<TransportPrice> findOtherCompany(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,Pageable pageable);
	/**
	 * 根据有效日期统计
	 */
	@Query("select a from TransportPrice a where a.effectiveDate>=?1 and a.effectiveDate<?2")
	public List<TransportPrice> findByEffectiveDate(Date yesterday,Date today);
}
