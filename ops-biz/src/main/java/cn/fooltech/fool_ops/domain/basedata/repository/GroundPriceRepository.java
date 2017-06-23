package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPrice;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface GroundPriceRepository extends FoolJpaRepository<GroundPrice, String>, FoolJpaSpecificationExecutor<GroundPrice>{

    /**
     * 统计某个单据类型下的单据数量
     *
     * @param orgId    机构ID
     * @return
     */
    @Query("select count(*) from GroundPrice b where b.orgId=?1")
    public Long countByBillType(String orgId);

    /**
     * 查找分页
     * @param vo
     * @param page
     * @return
     */
    public default Page<GroundPrice> findPageBy(String accId, GroundPriceVo vo, Pageable page){

        return findAll(new Specification<GroundPrice>() {
            @Override
            public Predicate toPredicate(Root<GroundPrice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.get("accId"), accId));

                //单号搜索
                if(StringUtils.isNotBlank(vo.getCode())){
                    predicates.add(builder.like(root.get("code"), PredicateUtils.getAnyLike(vo.getCode())));
                }

                if(StringUtils.isNotBlank(vo.getAddressId())){
                    predicates.add(builder.equal(root.get("addressId"), vo.getAddressId()));
                }

                //单据日期搜索
                if(vo.getStartDay() != null){
                    Date startDate = vo.getStartDay();
                    Date startDatet = DateUtilTools.changeDateTime(startDate, 0, 0, 0,0,0);
                    predicates.add(builder.greaterThanOrEqualTo(root.get("billDate"), startDatet));
                }
                if(vo.getEndDay() != null){
                    Date endDate = vo.getEndDay();
                    Date endDatet = DateUtilTools.changeDateTime(endDate, 0, 0, 0,0,0);
                    predicates.add(builder.lessThanOrEqualTo(root.get("billDate"), endDatet));
                }


                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);
    }

    /**
     * 根据编号统计个数
     * @param code
     * @param accId
     * @return
     */
    @Query("select count(g) from GroundPrice g where g.code=?1 and g.accId=?2")
    public Long countByCode(String code, String accId);

    /**
     * 根据编号统计个数
     * @param code
     * @param excludeId
     * @param accId
     * @return
     */
    @Query("select count(g) from GroundPrice g where g.code=?1 and g.accId=?3 and g.id!=?2")
    public Long countByCode(String code, String excludeId, String accId);

    /**
     * 根据地址ID查询有效数据
     * @param addressId
     * @return
     */
    @Query("select g from GroundPrice g where g.addressId=?1 and g.enable=1 and g.id!=?2 order by billDate desc")
    public List<GroundPrice> findValidByAddressId(String addressId, String excludeId);
    
	/**
	 * 根据场地id查询记录
	 * @param fid
	 * @return
	 */
	@Query("select count(a) from GroundPrice a where addressId=?1")
	public Long queryByAddressCount(String fid);
	
}
