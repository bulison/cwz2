package cn.fooltech.fool_ops.domain.basedata.repository;


import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail2Vo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 运输费报价模板（从2表） 持久层
 * @author cwz
 * @date 2016-12-8
 */
public interface TransportTemplateDetail2Repository extends FoolJpaRepository<TransportTemplateDetail2, String>,FoolJpaSpecificationExecutor<TransportTemplateDetail2> {

    /**
     * 根据参数查找分页
     *
     * @param pageable
     * @return
     */
    public default Page<TransportTemplateDetail2> findPageBy(TransportTemplateDetail2Vo vo,Pageable pageable) {
        return findAll(new Specification<TransportTemplateDetail2>() {
            @Override
            public Predicate toPredicate(Root<TransportTemplateDetail2> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                String accId = SecurityUtil.getFiscalAccountId();
                predicateList.add(criteriaBuilder.equal(root.get("fiscalAccount").get("fid"), accId));
                if(!Strings.isNullOrEmpty(vo.getTemplateFid())){
               	 predicateList.add(criteriaBuilder.equal(root.get("template").get("id"), vo.getTemplateFid()));
               }
                if(!Strings.isNullOrEmpty(vo.getDetail1Fid())){
                	predicateList.add(criteriaBuilder.equal(root.get("detail1").get("id"), vo.getDetail1Fid()));
                }
                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }
    /**
     * 根据从1表id删除
     * @param detailId
     */
    @Query("delete from TransportTemplateDetail2 a where detail1.id=?1")
    @Modifying
	public void delByDetail1(String detailId);
    
    @Query("select a from TransportTemplateDetail2 a where fiscalAccount.fid=?1 and template.id=?2 and id=?3")
	public TransportTemplateDetail2 queryByFid(String accId,String templateId,String id);
    /**
     * 根据模版id获取列表
     * @param accId
     * @param templateId
     * @return
     */
    @Query("select a from TransportTemplateDetail2 a where fiscalAccount.fid=?1 and template.id=?2 and detail1.id=?3")
    public List<TransportTemplateDetail2> queryByTemplateId(String accId,String templateId,String detail1Id);
    /**
     * 根据模版id获取列表
     * @param accId
     * @param templateId
     * @return
     */
    @Query("select a from TransportTemplateDetail2 a where fiscalAccount.fid=?1 and template.id=?2 order by code")
    public List<TransportTemplateDetail2> queryByTemplateId(String accId,String templateId);
    
    @Modifying
    @Query("delete from TransportTemplateDetail2 t where t.template.id=?1")
    public void delByTempId(String tempId);
}
