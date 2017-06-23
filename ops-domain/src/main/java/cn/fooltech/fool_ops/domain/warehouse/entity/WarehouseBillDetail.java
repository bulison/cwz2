package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>仓库单据记录明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月16日
 */
@Entity
@Table(name = "TSB_WAREHOUSE_BILLDETAIL")
public class WarehouseBillDetail extends OpsOrgEntity {

    public static final int DETAIL_TYPE_PRODUCT = 0;//产品
    public static final int DETAIL_TYPE_METERIAL = 1;//材料
    private static final long serialVersionUID = -2630237287451654465L;
    /**
     * 单据
     */
    private WarehouseBill bill;
    /**
     * 货品
     */
    private Goods goods;
    /**
     * 货品属性
     */
    private GoodsSpec goodsSpec;
    /**
     * 货品单位(开单时的单位)
     */
    private Unit unit;
    /**
     * 货品数量(开单时的数量)
     */
    private BigDecimal quentity;
    /**
     * 成本价
     */
    private BigDecimal costPrice = BigDecimal.ZERO;
    /**
     * 核算成本单价
     */
    private BigDecimal costUnitPrice = BigDecimal.ZERO;

	/**
     * 单价
     */
    private BigDecimal unitPrice = BigDecimal.ZERO;
    /**
     * 记账金额
     */
    private BigDecimal type = BigDecimal.ZERO;
    /**
     * 单位换算
     */
    private BigDecimal scale;
    /**
     * 记账数量
     */
    private BigDecimal accountQuentity;
    /**
     * 记账单位(最小单位)
     */
    private Unit accountUint;
    /**
     * 记账单价
     */
    private BigDecimal accountUintPrice;
    /**
     * 进仓仓库(默认使用)
     */
    private AuxiliaryAttr inWareHouse;
    /**
     * 出仓仓库
     */
    private AuxiliaryAttr outWareHouse;
    /**
     * 描述
     */
    private String describe;
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
     * 明细类型
     * 0--产品明细
     * 1--材料明细
     */
    private Integer detailType = DETAIL_TYPE_PRODUCT;
    /**
     * 出库记录
     */
    private Set<OutStorage> outStorages = new HashSet<OutStorage>(0);

    /**
     * 税率
     */
    private BigDecimal taxRate = BigDecimal.ZERO;

    /**
     * 税金
     */
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * 税后金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 引用的单据明细ID
     */
    private String refDetailId;


    //实收数量
    private BigDecimal receivedQuantity;


    //亏损数量
    private BigDecimal loseQuantity;


    //亏损金额
    private BigDecimal loseAmount;


    //发货成本单价
    private BigDecimal deliveryCostPrice;


    //发货成本金额
    private BigDecimal deliveryCostAmount;


    //成本金额
    private BigDecimal costAmount;


    //运输单位ID
    private AuxiliaryAttr transportUint;


    //运输数量
    private BigDecimal transportQuentity;


    //运输单价
    private BigDecimal transportPrice;


    //运输金额
    private BigDecimal transportAmount;


    //扣费金额
    private BigDecimal deductionAmount;


    //运输单位换算
    private BigDecimal transprotScale;

    //账套
    private FiscalAccount fiscalAccount;
    //提成点数
    private BigDecimal percentage;
    //提成金额
    private BigDecimal percentageAmount;
    @Column(name = "FRECEIVED_QUANTITY")
    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    @Column(name = "FLOSE_QUANTITY")
    public BigDecimal getLoseQuantity() {
        return loseQuantity;
    }

    public void setLoseQuantity(BigDecimal loseQuantity) {
        this.loseQuantity = loseQuantity;
    }

    @Column(name = "FLOSE_AMOUNT")
    public BigDecimal getLoseAmount() {
        return loseAmount;
    }

    public void setLoseAmount(BigDecimal loseAmount) {
        this.loseAmount = loseAmount;
    }

    @Column(name = "FDELIVERY_COST_PRICE")
    public BigDecimal getDeliveryCostPrice() {
        return deliveryCostPrice;
    }

    public void setDeliveryCostPrice(BigDecimal deliveryCostPrice) {
        this.deliveryCostPrice = deliveryCostPrice;
    }

    @Column(name = "FDELIVERY_COST_AMOUNT")
    public BigDecimal getDeliveryCostAmount() {
        return deliveryCostAmount;
    }

    public void setDeliveryCostAmount(BigDecimal deliveryCostAmount) {
        this.deliveryCostAmount = deliveryCostAmount;
    }

    @Column(name = "FCOST_AMOUNT")
    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    @JoinColumn(name = "FTRANSPORT_UINT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getTransportUint() {
        return transportUint;
    }

    public void setTransportUint(AuxiliaryAttr transportUint) {
        this.transportUint = transportUint;
    }

    @Column(name = "FTRANSPROT_QUENTITY")
    public BigDecimal getTransportQuentity() {
        return transportQuentity;
    }

    public void setTransportQuentity(BigDecimal transportQuentity) {
        this.transportQuentity = transportQuentity;
    }

    @Column(name = "FTRANSPORT_PRICE")
    public BigDecimal getTransportPrice() {
        return transportPrice;
    }

    public void setTransportPrice(BigDecimal transportPrice) {
        this.transportPrice = transportPrice;
    }

    @Column(name = "FTRANSPORT_AMOUNT")
    public BigDecimal getTransportAmount() {
        return transportAmount;
    }

    public void setTransportAmount(BigDecimal transportAmount) {
        this.transportAmount = transportAmount;
    }

    @Column(name = "FDEDUCTION_AMOUNT")
    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    @Column(name = "FTRANSPROT_SCALE")
    public BigDecimal getTransprotScale() {
        return transprotScale;
    }

    public void setTransprotScale(BigDecimal transprotScale) {
        this.transprotScale = transprotScale;
    }

    /**
     * 获取单据
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_BILL_ID", nullable = false)
    public WarehouseBill getBill() {
        return bill;
    }

    /**
     * 设置单据
     *
     * @param bill
     */
    public void setBill(WarehouseBill bill) {
        this.bill = bill;
    }

    /**
     * 获取货品
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置货品
     *
     * @param goods
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 获取货品属性
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_SPEC_ID")
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
     * 获取货品单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUINT_ID", nullable = false)
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置货品单位
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取货品数量
     *
     * @return
     */
    @Column(name = "FQUENTITY", nullable = false)
    public BigDecimal getQuentity() {
        return quentity;
    }

    /**
     * 设置货品数量
     *
     * @param quentity
     */
    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    /**
     * 获取成本价
     *
     * @return
     */
    @Column(name = "FCOST_PRICE")
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    /**
     * 设置成本价
     *
     * @param costPrice
     */
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * 获取单价
     *
     * @return
     */
    @Column(name = "FUNIT_PRICE", nullable = false)
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置单价
     *
     * @param unitPrice
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 获取记账金额
     *
     * @return
     */
    @Column(name = "FACCOUNT_AMOUNT", nullable = false)
    public BigDecimal getType() {
        return type;
    }

    /**
     * 设置记账金额
     *
     * @param type
     */
    public void setType(BigDecimal type) {
        this.type = type;
    }

    /**
     * 获取单位换算
     *
     * @return
     */
    @Column(name = "FSCALE", nullable = false)
    public BigDecimal getScale() {
        return scale;
    }

    /**
     * 设置单位换算
     *
     * @param scale
     */
    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    /**
     * 获取记账数量
     *
     * @return
     */
    @Column(name = "FACCOUNT_QUENTITY")
    public BigDecimal getAccountQuentity() {
        return accountQuentity;
    }

    /**
     * 设置记账数量
     *
     * @param accountQuentity
     */
    public void setAccountQuentity(BigDecimal accountQuentity) {
        this.accountQuentity = accountQuentity;
    }

    /**
     * 获取记账单位
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACCOUNT_UINT_ID")
    public Unit getAccountUint() {
        return accountUint;
    }

    /**
     * 设置记账单位
     *
     * @param accountUint
     */
    public void setAccountUint(Unit accountUint) {
        this.accountUint = accountUint;
    }

    /**
     * 获取记账单价
     *
     * @return
     */
    @Column(name = "FACCOUNT_UINT_PRICE")
    public BigDecimal getAccountUintPrice() {
        return accountUintPrice;
    }

    /**
     * 设置记账单价
     *
     * @param accountUintPrice
     */
    public void setAccountUintPrice(BigDecimal accountUintPrice) {
        this.accountUintPrice = accountUintPrice;
    }

    /**
     * 获取进仓仓库
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_WAREHOUSE_ID")
    public AuxiliaryAttr getInWareHouse() {
        return inWareHouse;
    }

    /**
     * 设置进仓仓库
     *
     * @param inWareHouse
     */
    public void setInWareHouse(AuxiliaryAttr inWareHouse) {
        this.inWareHouse = inWareHouse;
    }

    /**
     * 获取出仓仓库
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOUT_WAREHOUSE_ID")
    public AuxiliaryAttr getOutWareHouse() {
        return outWareHouse;
    }

    /**
     * 设置出仓仓库
     *
     * @param outWareHouse
     */
    public void setOutWareHouse(AuxiliaryAttr outWareHouse) {
        this.outWareHouse = outWareHouse;
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
     * 获取出库记录
     *
     * @return
     */
    @ManyToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "billDetailOut")
    public Set<OutStorage> getOutStorages() {
        return outStorages;
    }

    /**
     * 设置出库记录
     *
     * @param outStorages
     */
    public void setOutStorages(Set<OutStorage> outStorages) {
        this.outStorages = outStorages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fid == null) ? 0 : fid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WarehouseBillDetail other = (WarehouseBillDetail) obj;
        if (fid == null) {
            if (other.fid != null)
                return false;
        } else if (!fid.equals(other.fid))
            return false;
        return true;
    }

    /**
     * 获取明细类型
     *
     * @return
     */
    @Column(name = "FDETAIL_TYPE")
    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    @Column(name = "FTAX_RATE")
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Column(name = "FTAX_AMOUNT")
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Column(name = "FTOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "FREF_DETAIL_ID")
    public String getRefDetailId() {
        return refDetailId;
    }

    public void setRefDetailId(String refDetailId) {
        this.refDetailId = refDetailId;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }
    /**
     * 获取核算成本单价
     * @return
     */
    @Column(name = "FCOST_UNIT_PRICE")
    public BigDecimal getCostUnitPrice() {
		return costUnitPrice;
	}

	public void setCostUnitPrice(BigDecimal costUnitPrice) {
		this.costUnitPrice = costUnitPrice;
	}
	@Column(name="FPERCENTAGE")
	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	@Column(name="FPERCENTAGE_AMOUNT")
	public BigDecimal getPercentageAmount() {
		return percentageAmount;
	}

	public void setPercentageAmount(BigDecimal percentageAmount) {
		this.percentageAmount = percentageAmount;
	}
	
}
