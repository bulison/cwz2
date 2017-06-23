package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>辅助属性</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月6日
 */
@Entity
@Table(name = "tbd_auxiliary_attr")
public class AuxiliaryAttr extends StorageBaseEntity {

    public static final short LEAF = 1;
    public static final short PARENT = 0;
    public static final short SYSTEM_SIGN_NO = 0;
    public static final short SYSTEM_SIGN_YES = 1;
    private static final long serialVersionUID = 6302582293588216896L;
    /**
     * 关联的目录
     */
    private AuxiliaryAttrType category;

    /**
     * 父节点
     */
    private AuxiliaryAttr parent;

    /**
     * 1为子节点，0为父节点
     */
    private Short flag;

    /**
     * 系统标识
     */
    private Short systemSign = SYSTEM_SIGN_NO;

    /**
     * 财务账套
     *
     * @add by rqh
     */
    private FiscalAccount fiscalAccount;

    /**
     * 所有父亲节点
     */
    private String parentIds;

    /**
     * 节点层级
     */
    private Integer level;

    /**
     * 下级节点
     */
    private Set<AuxiliaryAttr> childs = new HashSet<AuxiliaryAttr>(0);
    /**
     * 换算关系
     */
    private BigDecimal scale;

    @JoinColumn(name = "FCATEGORY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    public AuxiliaryAttrType getCategory() {
        return category;
    }

    public void setCategory(AuxiliaryAttrType category) {
        this.category = category;
    }

    @JoinColumn(name = "FPARENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    public AuxiliaryAttr getParent() {
        return parent;
    }

    public void setParent(AuxiliaryAttr parent) {
        this.parent = parent;
    }

    @Column(name = "FFLAG")
    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    /**
     * 获取系统标识
     *
     * @return
     */
    @Column(name = "FSYS_SIGN")
    public Short getSystemSign() {
        return systemSign;
    }

    /**
     * 设置系统标识
     *
     * @param systemSign
     */
    public void setSystemSign(Short systemSign) {
        this.systemSign = systemSign;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取下级货品
     *
     * @return
     */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<AuxiliaryAttr> getChilds() {
        return childs;
    }

    /**
     * 设置下级货品
     *
     * @param childs
     */
    public void setChilds(Set<AuxiliaryAttr> childs) {
        this.childs = childs;
    }

    /**
     * 父节点S
     *
     * @return
     */
    @Column(name = "FPARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    /**
     * 节点层级
     *
     * @return
     */
    @Column(name = "FLEVEL")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    @Column(name = "FSCALE")
	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

}
