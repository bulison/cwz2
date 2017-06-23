package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.VehicleInformation;
import cn.fooltech.fool_ops.domain.basedata.vo.VehicleInformationVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import cn.fooltech.fool_ops.utils.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface VehicleInformationRepository extends FoolJpaRepository<VehicleInformation, String>,FoolJpaSpecificationExecutor<VehicleInformation> {

    /**
     * 查找分页
     * @param accId
     * @param vo
     * @param pageable
     * @return
     */
    public default Page<VehicleInformation> findPageBy(String accId, String creatorId, VehicleInformationVo vo,
                                                       Pageable pageable){
        return findAll(new Specification<VehicleInformation>() {
            @Override
            public Predicate toPredicate(Root<VehicleInformation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                System.out.println("==============================="+vo.toString());
                List<Predicate> predicates = Lists.newArrayList();
                if (!Strings.isNullOrEmpty(accId)) {
                    predicates.add(builder.equal(root.get("accId"), accId));
                }
                if (!Strings.isNullOrEmpty(creatorId)) {
                predicates.add(builder.equal(root.get("creatorId"), creatorId));
                }

                if (!Strings.isNullOrEmpty(vo.getId())) {
                    predicates.add(builder.equal(root.get("id"), vo.getId()));
                }

                if (!Strings.isNullOrEmpty(vo.getLicenseNumber())) {
                    predicates.add(builder.equal(root.get("licenseNumber"), vo.getLicenseNumber()));
                }

                if (!Strings.isNullOrEmpty(vo.getDrivingLicese())) {
                    predicates.add(builder.equal(root.get("drivingLicese"), vo.getDrivingLicese()));
                }

                if (!Strings.isNullOrEmpty(vo.getOwnerName())) {
                    predicates.add(builder.equal(root.get("ownerName"), vo.getOwnerName()));
                }

                if (!Strings.isNullOrEmpty(vo.getOwnerIdCard())) {
                    predicates.add(builder.equal(root.get("ownerIdCard"), vo.getOwnerIdCard()));
                }

                if (!Strings.isNullOrEmpty(vo.getContactPhone())) {
                    predicates.add(builder.equal(root.get("contactPhone"), vo.getContactPhone()));
                }

                if (!Strings.isNullOrEmpty(vo.getFdescribe())) {
                    predicates.add(builder.equal(root.get("describe"), vo.getFdescribe()));
                }


                //单据日期搜索

//                if(vo.getStartDay() != null){
//                    Date startDate = vo.getStartDay();
//                    predicates.add(builder.greaterThanOrEqualTo(root.get("createTime"), startDate));
//                }
//                if(vo.getEndDay() != null){
//                    Date endDate = vo.getEndDay();
//                    predicates.add(builder.lessThanOrEqualTo(root.get("createTime"), endDate));
//                }

                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }


    /**
     * 模糊查询(根据客户编号、客户名称)
     *
     * @param accId
     * @param searchKey
     * @param maxSize
     * @return
     */
    public default List<VehicleInformation> vagueSearch(String accId, String userId, String searchKey,
                                              int maxSize ,Date startDay,Date endDay) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return findAll(new Specification<VehicleInformation>() {

            @Override
            public Predicate toPredicate(Root<VehicleInformation> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(builder.equal(root.<String>get("accId"), accId));

//                if (Strings.isNullOrEmpty(recordStatus)) {
//                    predicates.add(builder.equal(root.get("recordStatus"), VehicleInformation.STATUS_SAC));
//                } else {
//                    predicates.add(builder.equal(root.get("recordStatus"), recordStatus));
//                }//(根据可按车牌号、行驶证号、车主姓名、车主身份证、联系电话等进行模糊查询)

                if (StringUtils.isNotBlank(searchKey)) {
                    String likeKey = searchKey.trim();
                    likeKey = PredicateUtils.getAnyLike(likeKey);
//                    if (UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)) {
//                        predicates.add(builder.or(
//                                builder.like(root.get("licenseNumber"), likeKey),
//                                builder.like(root.get("drivingLicese"), likeKey),
//                                builder.like(root.get("ownerName"), likeKey),
//                                builder.like(root.get("ownerIdCard"), likeKey),
//                                builder.like(root.get("contactPhone"), likeKey)
//                        ));
//                    } else {
                        predicates.add(builder.or(
                                builder.like(root.get("licenseNumber"), likeKey),
                                builder.like(root.get("drivingLicese"), likeKey),
                                builder.like(root.get("ownerName"), likeKey),
                                builder.like(root.get("ownerIdCard"), likeKey),
                                builder.like(root.get("contactPhone"), likeKey)
                        ));
//                    }
                }
                if(startDay != null){
                    Date startDayt = DateUtilTools.changeDateTime(startDay, 0 ,0, 0, 0, 0);
                    predicates.add(builder.greaterThanOrEqualTo(root.get("createTime"), startDayt));
                }
                if(endDay != null){
                    Date endDayt = DateUtilTools.changeDateTime(endDay, 0 ,0, 23, 59, 59);
                    predicates.add(builder.lessThanOrEqualTo(root.get("createTime"), endDayt));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, sort, maxSize);
    }
    /**
     * 根据账套查询车牌号
     * @param faccId
     * @param licenseNumber
     * @return
     */
    @Query("select count(a) from VehicleInformation a where  accId=?1 and licenseNumber=?2")
    public Long findByNum(String faccId,String licenseNumber);
}
