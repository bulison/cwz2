package cn.fooltech.fool_ops.domain.base.entity;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>新增仓储的抽象类，不再使用多重继承，直接抽取要用到的属性进行抽象</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月6日
 */
@MappedSuperclass
public class StorageBaseEntity extends BasePO implements Serializable, Cloneable {

    public static final short STATUS_DISABLE = 0;//无效数据
    public static final short STATUS_ENABLE = 1;//有效数据
    private static final long serialVersionUID = -4351313473076779882L;
    /**
     * 主键
     */
    protected String fid;

    /**
     * 所属企业
     */
    protected Organization org;

    /**
     * 编码，相当于助记符 一般控制有效的记录中编码不可重复
     */
    protected String code;

    /**
     * 名称
     */
    protected String name;

    /**
     * 描述
     */
    protected String describe;

    /**
     * 创建人
     */
    protected User creator;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /**
     * 数据是否有效
     */
    protected Short enable;

    /**
     * 获取主键
     */
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getFid() {
        return fid;
    }

    /**
     * 设置主键
     */
    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 获取所属企业
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    @JsonIgnore
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取编码
     *
     * @return
     */
    @Column(name = "FCODE", length = 50, nullable = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param theCode
     */
    public void setCode(String theCode) {
        code = theCode;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param theName
     */
    public void setName(String theName) {
        name = theName;
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
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     */
    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间戳
     */
    @Column(name = "FUPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "ENABLE")
    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        StorageBaseEntity entity = (StorageBaseEntity) clone;
        entity.setFid(null);
        return entity;
    }
}
