package cn.fooltech.fool_ops.domain.basedata.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplateDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroundTemplateDetailRepository extends FoolJpaRepository<GroundTemplateDetail, String> {

    /**
     * 查找分页
     *
     * @return
     */
    @Query("select g from GroundTemplateDetail g where g.accId=?1 and g.template.id=?2")
    public Page<GroundTemplateDetail> findPageBy(String accId, String templateId, Pageable page);

    /**
     * 查找列表
     *
     * @return
     */
    @Query("select g from GroundTemplateDetail g where g.accId=?1 and g.template.id=?2")
    public List<GroundTemplateDetail> findBy(String accId, String templateId, Sort sort);

    /**
     * 查找列表
     *
     * @return
     */
    @Query("select g from GroundTemplateDetail g where g.accId=?1 and g.template.ground.fid=?2 order by g.createTime desc")
    public List<GroundTemplateDetail> findBy(String accId, String groundId);
}
