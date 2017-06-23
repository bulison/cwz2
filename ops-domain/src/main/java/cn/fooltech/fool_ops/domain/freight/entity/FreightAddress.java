package cn.fooltech.fool_ops.domain.freight.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 货运地址
 *
 * @author cwz
 *         2016-12-5
 */
@Entity
@Table(name = "tbd_freight_address")
public class FreightAddress extends OpsEntity {
    private static final long serialVersionUID = 1L;

    public static final short ENABLE_Y = 1;
    public static final short ENABLE_N = 0;
    public static final short RECEIPT_Y = 1;
    public static final short RECEIPT_N = 0;
    public static final short TRANSFER_Y = 1;
    public static final short TRANSFER_N = 0;

    private short enable;//状态：0--停用 1--启用
    private FiscalAccount fiscalAccount;    //账套ID
    private String code;//编号
    private Date createTime;//创建时间
    private User creator;//创建人
    private String describe;//描述
    private Short flag;//节点标识：1为子节点，0为父节点
    private String fullParentId;//父路径
    private AuxiliaryAttr ground;///辅助属性场地类型
    private String name;//名称
    private Organization org;//机构ID
    private FreightAddress parent;//父ID
    private short recipientSign;//收货标识：0-否 1-是
    private Date updateTime;//修改时间
    private AuxiliaryAttr warehouse;//辅助属性仓库
    private AuxiliaryAttr transportLoss;//辅助属性损耗地址
    private Short transfer = TRANSFER_N;//是否中转站

    /**
     * 下级节点
     */
    private Set<FreightAddress> childs = new HashSet<FreightAddress>(0);
    /**
     * 子节点
     */
    private List<FreightAddress> children;
    /**
     * 节点层级
     */
    private Integer level;

    public FreightAddress() {
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public List<FreightAddress> getChildren() {
        return children;
    }

    public void setChildren(List<FreightAddress> children) {
        this.children = children;
    }

    /**
     * 获取下级货品
     *
     * @return
     */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<FreightAddress> getChilds() {
        return childs;
    }

    /**
     * 设置下级货品
     *
     * @param childs
     */
    public void setChilds(Set<FreightAddress> childs) {
        this.childs = childs;
    }

    public short getEnable() {
        return enable;
    }

    public void setEnable(short enable) {
        this.enable = enable;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    @JsonIgnore
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Column(name = "FFLAG")
    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    @Column(name = "FFULL_PARENT_ID")
    public String getFullParentId() {
        return fullParentId;
    }

    public void setFullParentId(String fullParentId) {
        this.fullParentId = fullParentId;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGROUND_ID")
    public AuxiliaryAttr getGround() {
        return ground;
    }

    public void setGround(AuxiliaryAttr ground) {
        this.ground = ground;
    }

    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID", updatable = false)
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    @JoinColumn(name = "FPARENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    public FreightAddress getParent() {
        return parent;
    }

    public void setParent(FreightAddress parent) {
        this.parent = parent;
    }

    /**
     * 收货标识：0-否 1-是
     *
     * @return
     */
    @Column(name = "FRECIPIENT_SIGN")
    public short getRecipientSign() {
        return recipientSign;
    }

    public void setRecipientSign(short recipientSign) {
        this.recipientSign = recipientSign;
    }

    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_ID")
    public AuxiliaryAttr getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(AuxiliaryAttr warehouse) {
        this.warehouse = warehouse;
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
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_LOSS_ID")
	public AuxiliaryAttr getTransportLoss() {
		return transportLoss;
	}

	public void setTransportLoss(AuxiliaryAttr transportLoss) {
		this.transportLoss = transportLoss;
	}

    @Column(name = "FTRANSFER")
    public Short getTransfer() {
        return transfer;
    }

    public void setTransfer(Short transfer) {
        this.transfer = transfer;
    }
}