package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 单位
 *
 * @author lzf
 * @version 1.0
 * @date 2015年6月26日
 * @update rqh 2015-09-06
 */
@Entity
@Table(name = "TBD_UNIT")
public class Unit extends OpsOrgEntity {

    /**
     * 单位标志- 单位组
     */
    public static final int FLAG_GROUP = 0;
    /**
     * 单位标志- 单位
     */
    public static final int FLAG_UNIT = 1;
    /**
     * 状态- 有效
     */
    public static final int ENABLE = 1;
    /**
     * 状态- 无效
     */
    public static final int UNABLE = 0;
    private static final long serialVersionUID = 8777660328394257414L;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 换算关系
     */
    private BigDecimal scale = new BigDecimal(1);
    /**
     * 单位标志
     */
    private Integer flag = FLAG_GROUP;
    /**
     * 描述
     */
    private String describe;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private Integer enable = ENABLE;

    /**
     * 组
     */
    private Unit parent;

    /**
     * 子单位
     */
    private Set<Unit> childs = new HashSet<Unit>(0);

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取编号
     *
     * @return
     */
    @Column(name = "FCODE", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取换算单位
     *
     * @return
     */
    @Column(name = "FSCALE", nullable = false)
    public BigDecimal getScale() {
        return scale;
    }

    /**
     * 设置换算单位
     *
     * @param scale
     */
    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    /**
     * 获取单位标志
     *
     * @return
     */
    @Column(name = "FFLAG")
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置单位标志
     *
     * @param flag
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE", length = 200)
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     *
     * @param describe
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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
     * 获取修改时间
     *
     * @return
     */
    @Column(name = "FUPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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
     * 获取状态
     *
     * @return
     */
    @Column(name = "FENABLE", nullable = false)
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置状态
     *
     * @param enable
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * 获取组
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID")
    public Unit getParent() {
        return parent;
    }

    /**
     * 设置组
     *
     * @param parent
     */
    public void setParent(Unit parent) {
        this.parent = parent;
    }

    /**
     * 获取子单位
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<Unit> getChilds() {
        return childs;
    }

    /**
     * 设置子单位
     *
     * @param childs
     */
    public void setChilds(Set<Unit> childs) {
        this.childs = childs;
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

