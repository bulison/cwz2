/**
 *
 */
package cn.fooltech.fool_ops.domain.basedata.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgBaseDataEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;


/**
 * 货品
 *
 * @author ljb
 * @version 1.0
 * @date 2014年6月11日
 * @update rqh 2015-09-08
 */
@Entity
@Table(name = "TBD_GOODS")
public class Goods extends OpsOrgBaseDataEntity {

    /**
     * 货品标识- 货品组
     */
    public static final int FLAG_GROUP = 0;
    /**
     * 货品标识- 货品
     */
    public static final int FLAG_GOODS = 1;
    /**
     * 记账标识- 货品计算库存
     */
    public static final int ACCOUNT_FLAG_YES = 1;
    /**
     * 记账标识- 不计算库存
     */
    public static final int ACCOUNT_FLAG_NO = 0;
    /**
     * 已使用标识- 未使用
     */
    public static final int USE_FLAG_UNUSED = 0;
    /**
     * 已使用标识- 已使用
     */
    public static final int USE_FLAG_USED = 1;
    private static final long serialVersionUID = -4386921748134520058L;
    /**
     * 规格
     */
    private String spec;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 体积
     */
    private BigDecimal volume;
    /**
     * 仓储期间<br>
     * 货品计划存放天数<br>
     */
    private Integer storagePeriod;
    /**
     * 分类
     */
    private AuxiliaryAttr category;
    /**
     * 货品属性
     */
    private GoodsSpec goodsSpec;
    /**
     * 单位
     */
    private Unit unit;
    /**
     * 单位组
     */
    private Unit unitGroup;
    /**
     * 货品标识
     */
    private Integer flag = FLAG_GROUP;
    /**
     * 记账标识
     */
    private Integer accountFlag = ACCOUNT_FLAG_YES;
    /**
     * 已使用标识
     */
    private Integer useFlag = USE_FLAG_UNUSED;

    /**
     * 上级货品
     */
    private Goods parent;

    /**
     * 子货品
     */
    private Set<Goods> childs;

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
     * 层级
     */
    private Integer level;

    /**
     * 父路径
     */
    private String fullParentId;
    /**
     * 拼音首字母
     */
    private String pinyin;
    /**
     * 五笔首字母
     */
    private String fivepen;
    
	/**
	 * 提成点数【0-100之间】 （新增字段）
	 * @author cwz
	 * @date 2017-6-19
	 */
	private BigDecimal percentage;

    /**
     * 获取规格
     *
     * @return
     */
    @Column(name = "FSPEC", length = 100)
    public String getSpec() {
        return spec;
    }

    /**
     * 设置规格
     *
     * @param spec
     */
    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     * 获取条形码
     *
     * @return
     */
    @Column(name = "FBARCODE", length = 20)
    public String getBarCode() {
        return barCode;
    }

    /**
     * 设置条形码
     *
     * @param barCode
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * 获取重量
     *
     * @return
     */
    @Column(name = "FWEIGHT")
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * 获取体积
     *
     * @return
     */
    @Column(name = "FVOLUME")
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * 设置体积
     *
     * @param volume
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * 获取仓储期间
     *
     * @return
     */
    @Column(name = "FSTORAGE_PERIOD")
    public Integer getStoragePeriod() {
        return storagePeriod;
    }

    /**
     * 设置仓储期间
     *
     * @param storagePeriod
     */
    public void setStoragePeriod(Integer storagePeriod) {
        this.storagePeriod = storagePeriod;
    }

    /**
     * 获取分类
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCATEGORY")
    public AuxiliaryAttr getCategory() {
        return category;
    }

    /**
     * 设置分类
     *
     * @param category
     */
    public void setCategory(AuxiliaryAttr category) {
        this.category = category;
    }

    /**
     * 获取货品属性
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "FGOOD_SPEC_ID")
    public GoodsSpec getGoodsSpec() {
        return goodsSpec;
    }

    /**
     * 设置货品属性
     *
     * @param goodsSpec
     */
    public void setGoodsSpec(GoodsSpec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    /**
     * 获取单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "FUNIT_ID")
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取单位组
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "FUNIT_GROUP_ID")
    public Unit getUnitGroup() {
        return unitGroup;
    }

    /**
     * 设置单位组
     *
     * @param unitGroup
     */
    public void setUnitGroup(Unit unitGroup) {
        this.unitGroup = unitGroup;
    }

    /**
     * 获取货品标识
     *
     * @return
     */
    @Column(name = "FFLAG")
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置货品标识
     *
     * @param flag
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * 获取记账标识
     *
     * @return
     */
    @Column(name = "FACCOUNT_FLAG")
    public Integer getAccountFlag() {
        return accountFlag;
    }

    /**
     * 设置记账标识
     *
     * @param accountFlag
     */
    public void setAccountFlag(Integer accountFlag) {
        this.accountFlag = accountFlag;
    }

    /**
     * 获取已使用标识
     *
     * @return
     */
    @Column(name = "FUSE_FLAG")
    public Integer getUseFlag() {
        return useFlag;
    }

    /**
     * 设置已使用标识
     *
     * @param useFlag
     */
    public void setUseFlag(Integer useFlag) {
        this.useFlag = useFlag;
    }

    /**
     * 获取上级货品
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID")
    public Goods getParent() {
        return parent;
    }

    /**
     * 设置上级货品
     *
     * @param parent
     */
    public void setParent(Goods parent) {
        this.parent = parent;
    }

    /**
     * 获取下级货品
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<Goods> getChilds() {
        return childs;
    }

    /**
     * 设置下级货品
     *
     * @param childs
     */
    public void setChilds(Set<Goods> childs) {
        this.childs = childs;
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

    /**
     * 获取层级
     *
     * @return
     */
    @Column(name = "FLEVEL", nullable = false)
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置层级
     *
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取父路径
     *
     * @return
     */
    @Column(name = "FFULL_PARENT_ID", length = 1000)
    public String getFullParentId() {
        return fullParentId;
    }

    /**
     * 设置父路径
     *
     * @param fullParentId
     */
    public void setFullParentId(String fullParentId) {
        this.fullParentId = fullParentId;
    }

    @Column(name = "FPINYIN")
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Column(name = "FFIVEPEN")
    public String getFivepen() {
        return fivepen;
    }

    public void setFivepen(String fivepen) {
        this.fivepen = fivepen;
    }
    /**
     * 获取提成点数
     * @return
     */
    @Column(name = "FPERCENTAGE")
	public BigDecimal getPercentage() {
		return percentage;
	}
	/**
	 * 设置提成点数
	 * @param percentage
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
    
}
