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
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail1Vo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 运输费报价模板（从1表） 持久层
 * @author cwz
 * @date 2016-12-8
 */
public interface TransportTemplateDetail1Repository extends FoolJpaRepository<TransportTemplateDetail1, String>,FoolJpaSpecificationExecutor<TransportTemplateDetail1> {

    /**
     * 根据参数查找分页
     *
     * @param pageable
     * @return
     */
    public default Page<TransportTemplateDetail1> findPageBy(TransportTemplateDetail1Vo vo,Pageable pageable) {
        return findAll(new Specification<TransportTemplateDetail1>() {
            @Override
            public Predicate toPredicate(Root<TransportTemplateDetail1> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                String accId = SecurityUtil.getFiscalAccountId();
                predicateList.add(criteriaBuilder.equal(root.get("fiscalAccount").get("fid"), accId));
                if(!Strings.isNullOrEmpty(vo.getTemplateFid())){
                	 predicateList.add(criteriaBuilder.equal(root.get("template").get("id"), vo.getTemplateFid()));
                }
                Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
                return predicate;
            }
        }, pageable);
    }
    @Query("select a from TransportTemplateDetail1 a where fiscalAccount.fid=?1 and template.id=?2 and id=?3")
	public TransportTemplateDetail1 queryByFid(String accId,String templateId,String id);
    /**
     * 根据模版id获取列表
     * @param accId
     * @param templateId
     * @return
     */
    @Query("select a from TransportTemplateDetail1 a where fiscalAccount.fid=?1 and template.id=?2 order by code")
    public List<TransportTemplateDetail1> queryByFid(String accId,String templateId);
    
    @Modifying
    @Query("delete from TransportTemplateDetail1 t where t.template.id=?1")
    public void delByTempId(String tempId);
}
