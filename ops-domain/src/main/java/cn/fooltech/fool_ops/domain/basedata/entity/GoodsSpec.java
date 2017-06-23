package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgBaseDataEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * <p>货品属性</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月8日
 */
@Entity
@Table(name = "TBD_GOOD_SPEC")
public class GoodsSpec extends OpsOrgBaseDataEntity {

    /**
     * 属性组标- 组
     */
    public static final int FLAG_GROUP = 0;
    /**
     * 属性组标- 属性
     */
    public static final int FLAG_SPEC = 1;
    private static final long serialVersionUID = 7599766014252361716L;
    /**
     * 父属性
     */
    private GoodsSpec parent;
    /**
     * 子属性
     */
    private Set<GoodsSpec> childs;
    /**
     * 属性组标
     */
    private Integer flag = FLAG_GROUP;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 记录状态
     */
    private String recordStatus = STATUS_SAC;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取父属性
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID")
    public GoodsSpec getParent() {
        return parent;
    }

    /**
     * 设置父属性
     *
     * @param parent
     */
    public void setParent(GoodsSpec parent) {
        this.parent = parent;
    }

    /**
     * 获取子属性
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<GoodsSpec> getChilds() {
        return childs;
    }

    /**
     * 设置组属性
     *
     * @param childs
     */
    public void setChilds(Set<GoodsSpec> childs) {
        this.childs = childs;
    }

    /**
     * 获取属性组标
     *
     * @return
     */
    @Column(name = "FFLAG")
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置属性组标
     *
     * @param flag
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
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

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取记录状态
     *
     * @return
     */
    @Column(name = "RECORD_STATUS", length = 3)
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置记录状态
     *
     * @param recordStatus
     */
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

}
