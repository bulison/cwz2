package cn.fooltech.fool_ops.domain.sysman.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>用户常用模块</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月29日
 */
@Entity
@Table(name = "SMG_COMMON_USE_MODULE")
public class CommonUseModule extends OpsEntity {

    private static final long serialVersionUID = 727313798335004784L;

    /**
     * 资源
     */
    private Resource resource;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 获取资源
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRESOURCE_ID")
    public Resource getResource() {
        return resource;
    }

    /**
     * 设置资源
     *
     * @param resource
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
