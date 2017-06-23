package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;


/**
 * <p>财务账套</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月17日
 */
@Entity
@Table(name = "TBD_FISCAL_ACCOUNT")
public class FiscalAccount extends OpsOrgEntity {

    /**
     * 状态- 不可修改
     */
    public static final short ENABLE_NO = 0;
    /**
     * 状态- 可修改
     */
    public static final short ENABLE_YES = 1;
    /**
     * 默认登录标识- 不是
     */
    public static final short FLAG_NO = 0;
    /**
     * 默认登录标识- 是
     */
    public static final short FLAG_YES = 1;
    private static final long serialVersionUID = -7793791070361956562L;
    /**
     * 账套名
     */
    private String name;
    /**
     * 状态
     */
    private Short enable = ENABLE_YES;
    /**
     * 默认登录标识
     */
    private Short defaultFlag = FLAG_NO;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建人
     */
    private User creator;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取账套名
     *
     * @return
     */
    @Column(name = "FNAME", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * 设置账套名
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "FENABLE", nullable = false)
    public Short getEnable() {
        return enable;
    }

    /**
     * 设置状态
     *
     * @param enable
     */
    public void setEnable(Short enable) {
        this.enable = enable;
    }

    /**
     * 获取默认登录标识
     *
     * @return
     */
    @Column(name = "FDEFAULT")
    public Short getDefaultFlag() {
        return defaultFlag;
    }

    /**
     * 设置默认登录标识
     *
     * @param defaultFlag
     */
    public void setDefaultFlag(Short defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIPTION", length = 200)
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
