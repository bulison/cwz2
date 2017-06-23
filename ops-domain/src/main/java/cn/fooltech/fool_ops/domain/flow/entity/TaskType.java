package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 事件类别PO
 *
 * @author CYX
 * @version 1.0
 * @date 2016年10月28日
 */
@Entity
@Table(name = "TFLOW_TASK_TYPE")
public class TaskType extends BasePO implements java.io.Serializable {

    private static final long serialVersionUID = 1559142187875495061L;

    private String fid; //id
    private String code; //编号
    private String name; //名称
    private String describe; //描述
    private Organization org; //机构
    private User creator; //创建人
    private Date createTime; //创建时间
    private Date updateTime; //修改时间

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FID", unique = true, nullable = false, length = 32)
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 编号
     *
     * @return
     */
    @Column(name = "FCODE", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 名称
     *
     * @return
     */
    @Column(name = "FNAME", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 机构
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 创建人
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
