package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划货品表从表
 */
@ApiModel("计划货品表从表")
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tflow_plan_goods_detail")
public class PlanGoodsDetail {

    private static final long serialVersionUID = 1L;

    // 主键
    @ApiModelProperty(value = "主键")
    private String id;

    // 单据ID
    @ApiModelProperty(value = "单据ID")
    private PlanGoods bill;

    // 序号
    @ApiModelProperty(value = "序号")
    private Integer no;

    // 运输报价ID
    @ApiModelProperty(value = "运输报价ID")
    private TransportPrice transportBill;

    //报价日期
    @ApiModelProperty(value = "报价日期")
    private Date billDate;

    // 运输公司  关联供应商
    @ApiModelProperty(value = "运输公司  关联供应商")
    private Supplier supplier;

    // 发货地ID 关联场地表
    @ApiModelProperty(value = "发货地ID  关联场地表")
    private FreightAddress deliveryPlace;

    // 收货地ID 关联场地表
    @ApiModelProperty(value = "收货地ID  关联场地表")
    private FreightAddress receiptPlace;

    // 运输方式ID(关联辅助属性运输方式)
    @ApiModelProperty(value = "运输方式ID(关联辅助属性运输方式)")
    private AuxiliaryAttr transportType;

    // 装运方式ID(关联辅助属性装运方式)
    @ApiModelProperty(value = "装运方式ID(关联辅助属性装运方式)")
    private AuxiliaryAttr shipmentType;

    //运输计价单位ID  关联辅助属性运输费计价单位
    @ApiModelProperty(value = "运输计价单位ID  关联辅助属性运输费计价单位")
    private AuxiliaryAttr transportUnit;

    // 运输费用
    @ApiModelProperty(value = "运输费用")
    private BigDecimal freightPrice = BigDecimal.ZERO;

    // 调整运输费用
    @ApiModelProperty(value = "调整运输费用")
    private BigDecimal publishFreightPrice;

    // 换算关系  运输单位与货品基本单位的换算关系
    @ApiModelProperty(value = "换算关系  运输单位与货品基本单位的换算关系")
    private BigDecimal conversionRate;

    // 折算运输单价
    @ApiModelProperty(value = "折算运输单价")
    private BigDecimal basePrice = BigDecimal.ZERO;

    // 折算运输单价
    @ApiModelProperty(value = "调整折算运输单价")
    private BigDecimal publishBasePrice;

    // 可执行标识(1-可执行 2-难执行 3-无法执行)
    @ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
    private Integer executeSign = 1;

    // 预计天数
    @ApiModelProperty(value = "预计天数")
    private Integer expectedDays = 1;

    // 场地费用单价
    @ApiModelProperty(value = "场地费用单价")
    private BigDecimal groundCostPrice = BigDecimal.ZERO;

    // 备注
    @ApiModelProperty(value = "备注")
    private String remark;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    // 创建人
    @ApiModelProperty(value = "创建人")
    private User creator;

    // 修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    private Date updateTime;

    // 组织ID,机构ID
    @ApiModelProperty(value = "组织ID,机构ID")
    private Organization org;

    // 账套ID
    @ApiModelProperty(value = "账套ID")
    private FiscalAccount fiscalAccount;

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

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBILL_ID")
    public PlanGoods getBill() {
        return bill;
    }

    public void setBill(PlanGoods bill) {
        this.bill = bill;
    }

    @Column(name = "FNO")
    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_BILL_ID")
    public TransportPrice getTransportBill() {
        return transportBill;
    }

    public void setTransportBill(TransportPrice transportBill) {
        this.transportBill = transportBill;
    }

    @Column(name = "FBILL_DATE")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDELIVERY_PLACE")
    public FreightAddress getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(FreightAddress deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRECEIPT_PLACE")
    public FreightAddress getReceiptPlace() {
        return receiptPlace;
    }

    public void setReceiptPlace(FreightAddress receiptPlace) {
        this.receiptPlace = receiptPlace;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_TYPE_ID")
    public AuxiliaryAttr getTransportType() {
        return transportType;
    }

    public void setTransportType(AuxiliaryAttr transportType) {
        this.transportType = transportType;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    public AuxiliaryAttr getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(AuxiliaryAttr shipmentType) {
        this.shipmentType = shipmentType;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_UNIT_ID")
    public AuxiliaryAttr getTransportUnit() {
        return transportUnit;
    }

    public void setTransportUnit(AuxiliaryAttr transportUnit) {
        this.transportUnit = transportUnit;
    }

    @Column(name = "FFREIGHT_PRICE")
    public BigDecimal getFreightPrice() {
        return freightPrice;
    }


    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    @Column(name = "FPUBLISH_FREIGHT_PRICE")
    public BigDecimal getPublishFreightPrice() {
        return publishFreightPrice;
    }

    public void setPublishFreightPrice(BigDecimal publishFreightPrice) {
        this.publishFreightPrice = publishFreightPrice;
    }

    @Column(name = "FCONVERSION_RATE")
    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    @Column(name = "FBASE_PRICE")
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Column(name = "FPUBLISH_BASE_PRICE")
    public BigDecimal getPublishBasePrice() {
        return publishBasePrice;
    }

    public void setPublishBasePrice(BigDecimal publishBasePrice) {
        this.publishBasePrice = publishBasePrice;
    }

    @Column(name = "FEXECUTE_SIGN")
    public Integer getExecuteSign() {
        return executeSign;
    }

    public void setExecuteSign(Integer executeSign) {
        this.executeSign = executeSign;
    }

    @Column(name = "FEXPECTED_DAYS")
    public Integer getExpectedDays() {
        return expectedDays;
    }

    public void setExpectedDays(Integer expectedDays) {
        this.expectedDays = expectedDays;
    }

    @Column(name = "FGROUND_COST_PRICE")
    public BigDecimal getGroundCostPrice() {
        return groundCostPrice;
    }

    public void setGroundCostPrice(BigDecimal groundCostPrice) {
        this.groundCostPrice = groundCostPrice;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }
}