package cn.fooltech.fool_ops.domain.basedata.repository;


import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplate;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateVo;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
/**
 * 运输费报价模板 持久层
 * @author cwz
 * @date 2016-12-8
 */
public interface TransportTemplateRepository extends FoolJpaRepository<TransportTemplate, String> ,FoolJpaSpecificationExecutor<TransportTemplate> {
	
    /**
     * 根据参数查找分页
     *
     * @param pageable
     * @return
     */
    public default Page<TransportTemplate> findPageBy(TransportTemplateVo vo,Pageable pageable) {
        return findAll(new Specification<TransportTemplate>() {
            @Override
            public Predicate toPredicate(Root<TransportTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                String accId = SecurityUtil.getFiscalAccountId();
                predicateList.add(criteriaBuilder.equal(root.get("fiscalAccount").get("fid"), accId));
                String code = vo.getCode();
                String name = vo.getName();
                String transportTypeName = vo.getTransportTypeName();
                String shipmentTypeName = vo.getShipmentTypeName();
                String searchKey = vo.getSearchKey();
                if (!Strings.isNullOrEmpty(code)) {
                    predicateList.add(criteriaBuilder.equal(root.get("code"), code));
                }
                if (!Strings.isNullOrEmpty(name)) {
                	predicateList.add(criteriaBuilder.equal(root.get("name"), name));
                }
                if (!Strings.isNullOrEmpty(transportTypeName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("transportType").get("name"), transportTypeName));
                }
                if (!Strings.isNullOrEmpty(shipmentTypeName)) {
                	predicateList.add(criteriaBuilder.equal(root.get("shipmentType").get("name"), shipmentTypeName));
                }
                if (StringUtils.isNotBlank(searchKey)) {
                    String key = PredicateUtils.getAnyLike(searchKey);
                    predicateList.add(criteriaBuilder.or(
                    		criteriaBuilder.like(root.<String>get("code"), key),
                    		criteriaBuilder.like(root.<String>get("name"), key)
                    ));
                }

                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }
	@Query("select count(a) from TransportTemplate a where code=?1 and fiscalAccount.fid=?2")
	public Long queryByCodeCount(String code,String faccId);
	
	@Query("select count(a) from TransportTemplate a where name=?1 and fiscalAccount.fid=?2")
	public Long queryByNameCount(String name,String faccId);
	/**
	 * 判断主表的运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用；
	 * @param transportTypeId
	 * @param shipmentTypeId
	 * @return
	 */
	@Query("select count(a) from TransportTemplate a where transportType.fid=?1 and shipmentType.fid=?2 and deliveryPlace.fid=?3 and receiptPlace.fid=?4 and enable=1 and fiscalAccount.fid=?5")
	public Long queryByTypeCount(String transportTypeId,String shipmentTypeId,String deliveryPlaceId,String receiptPlaceId,String faccId);
	/**
	 * 判断主表的运输方式+装运方式+发货地+收货地，只允许一个模板是有效启用；
	 * @param transportTypeId
	 * @param shipmentTypeId
	 * @return
	 */
	@Query("select count(a) from TransportTemplate a where transportType.fid=?1 and shipmentType.fid=?2 and deliveryPlace.fid=?3 and receiptPlace.fid=?4 and enable=1 and fiscalAccount.fid=?5 and id !=?6")
	public Long queryByTypeCount(String transportTypeId,String shipmentTypeId,String deliveryPlaceId,String receiptPlaceId,String faccId,String id);
	
	@Modifying
	@Query("update TransportTemplate set enable=?1 where id=?2")
	public void updateEnable(int enable,String fid);
	
    
	/**
	 * 根据用户选择运输方式和装运方式，查找运输费报价模板
	 * @param transportTypeId 	运输方式
	 * @param shipmentTypeId  	装运方式
	 * @param deliveryPlaceId	发货地	
	 * @param receiptPlaceId	收货地
	 * @return
	 */
	@Query("select a from TransportTemplate a where transportType.fid=?1 and shipmentType.fid=?2 and deliveryPlace.fid=?3 and receiptPlace.fid=?4  and enable=1 and fiscalAccount.fid=?5")
    public List<TransportTemplate> findByTemp(String transportTypeId,String shipmentTypeId,String deliveryPlaceId,String receiptPlaceId,String faccId);
	
	/**
	 * 根据发货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from TransportTemplate a where deliveryPlace.fid=?1 and fiscalAccount.fid=?2")
	public Long queryByDeliveryPlaceCount(String fid,String faccId);
	
	/**
	 * 根据收货地查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from TransportTemplate a where receiptPlace.fid=?1 and fiscalAccount.fid=?2")
	public Long queryByReceiptPlaceCount(String fid,String faccId);
}
