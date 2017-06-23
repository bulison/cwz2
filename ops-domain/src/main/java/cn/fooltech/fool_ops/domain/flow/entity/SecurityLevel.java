package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>保密级别</p>
 *
 * @author CYX
 * @version 1.0
 * @date 2016年11月9日
 */
@Entity
@Table(name = "TFLOW_SECURITY_LEVEL")
public class SecurityLevel extends OpsOrgEntity {

    public static final int STATUS_USED = 1;  //已启用
    public static final int STATUS_UNUSE = 0; //未启用
    private static final long serialVersionUID = -8614320956738761370L;
    private String code; //编号
    private String name; //名称
    private Integer level; //保密级别
    private Integer checkoutStatus = STATUS_UNUSE; //事件状态
    private String describe; //描述
    private User creator; //创建人
    private Date createTime; //创建时间
    private Date updateTime; //修改时间

    /*
     * 编号
     */
    @Column(name = "FCODE", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /*
     * 名称
     */
    @Column(name = "FNAME", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * 保密级别
     */
    @Column(name = "FLEVEL", nullable = false)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /*
     * 级别状态
     */
    @Column(name = "FCHECKOUT_STATUS", nullable = false)
    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    /*
     * 描述
     */
    @Column(name = "FDESCRIBE", length = 200)
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /*
     * 创建人
     */
    @ManyToOne
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /*
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /*
     * 修改时间
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
