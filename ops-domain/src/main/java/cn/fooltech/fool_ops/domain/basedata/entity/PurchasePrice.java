package cn.fooltech.fool_ops.domain.basedata.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 货品价格报价
 */
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_purchase_price")
public class PurchasePrice {

    private static final long serialVersionUID = 1L;

    //主键
    private String id;

    //单号
    private String code;

    //单据日期
    private Date billDate;

    //供应商
    private String supplierId;

    //货品ID
    private Goods goods;

    //货品属性ID
    private String goodSpecId;

    //货品单位
    private String unitId;

    //厂价
    private BigDecimal factoryPrice;

    //税点
    private BigDecimal taxPoint;

    //税后价
    private BigDecimal afterTaxPrice;

    //提货费
    private BigDecimal pickUpCharge;


    //交货总价
    private BigDecimal deliveryPrice;


    //有效日期
    private Date effectiveDate;


    //发货地ID
    private String deliveryPlace;

    //描述
    private String fdescribe;


    //创建时间
    private Date createTime;

    //创建人
    private String creatorId;

    //修改时间戳
    private Date updateTime;

    //组织ID
    private String orgId;

    //账套ID
    private String accId;


    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "FBILL_DATE")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @Column(name = "FSUPPLIER_ID")
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "FGOOD_SPEC_ID")
    public String getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(String goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    @Column(name = "FUNIT_ID")
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Column(name = "FFACTORY_PRICE")
    public BigDecimal getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(BigDecimal factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    @Column(name = "FTAX_POINT")
    public BigDecimal getTaxPoint() {
        return taxPoint;
    }

    public void setTaxPoint(BigDecimal taxPoint) {
        this.taxPoint = taxPoint;
    }

    @Column(name = "FAFTER_TAX_PRICE")
    public BigDecimal getAfterTaxPrice() {
        return afterTaxPrice;
    }

    public void setAfterTaxPrice(BigDecimal afterTaxPrice) {
        this.afterTaxPrice = afterTaxPrice;
    }

    @Column(name = "FPICK_UP_CHARGE")
    public BigDecimal getPickUpCharge() {
        return pickUpCharge;
    }

    public void setPickUpCharge(BigDecimal pickUpCharge) {
        this.pickUpCharge = pickUpCharge;
    }

    @Column(name = "FDELIVERY_PRICE")
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Column(name = "FEFFECTIVE_DATE")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "FDELIVERY_PLACE")
    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    @Column(name = "FDESCRIBE")
    public String getFdescribe() {
        return fdescribe;
    }

    public void setFdescribe(String fdescribe) {
        this.fdescribe = fdescribe;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "FCREATOR_ID")
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FORG_ID")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Column(name = "FACC_ID")
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
    //状态,0-失效 1-有效
    @ApiModelProperty(value = "状态,0-失效 1-有效")
    private Integer enable;

    //状态,0-失效 1-有效
    @ApiModelProperty(value = "日状态,0-失效 1-有效（每天只要一条有效数据）")
    private Integer dayEnable;

   
    @Column(name = "FENABLE_DATE")
	public Integer getDayEnable() {
		return dayEnable;
	}

	public void setDayEnable(Integer dayEnable) {
		this.dayEnable = dayEnable;
	}
	@Column(name = "FENABLE")
	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}
}