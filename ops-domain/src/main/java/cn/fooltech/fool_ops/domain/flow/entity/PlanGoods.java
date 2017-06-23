package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 计划货品表
 * @author cwz
 * @date 2017-2-8
 */
@ApiModel("计划货品表")
@Entity
@Table(name = "tflow_plan_goods")
public class PlanGoods {

	// 主键
	@ApiModelProperty(value = "主键")
	private String id;

	// 计划ID
	@ApiModelProperty(value = "计划ID")
	private Plan plan;

	// 单据日期
	@ApiModelProperty(value = "单据日期")
	private Date billDate;

	// 线路路径
	@ApiModelProperty(value = "线路路径")
	private String route;

	// 采购公司 关联供应商
	@ApiModelProperty(value = "采购公司  关联供应商")
	private Supplier supplier;

	// 销售客户ID  关联客户，根据收货地得出
	@ApiModelProperty(value = "客户ID")
	private Customer customer;

	// 货品报价单ID 关联货品价格报价表
	@ApiModelProperty(value = "货品报价单ID 关联货品价格报价表")
	private PurchasePrice purchasePrice;

	// 货品ID
	@ApiModelProperty(value = "货品ID")
	private Goods goods;

	// 货品属性ID
	@ApiModelProperty(value = "货品属性ID")
	private GoodsSpec goodsSpec;

	// 货品单位ID 货品记账单位
	@ApiModelProperty(value = "货品单位ID  货品记账单位")
	private Unit unit;

	// 发货地ID 关联场地表
	@ApiModelProperty(value = "发货地ID  关联场地表")
	private FreightAddress deliveryPlace;

	// 收货地ID 关联场地表
	@ApiModelProperty(value = "收货地ID  关联场地表")
	private FreightAddress receiptPlace;

	// 出厂价
	@ApiModelProperty(value = "出厂价")
	private BigDecimal factoryPrice = BigDecimal.ZERO;

	// 调整出厂价
	@ApiModelProperty(value = "调整出厂价")
	private BigDecimal publishFactoryPrice = BigDecimal.ZERO;

	// 运输费用
	@ApiModelProperty(value = "运输费用")
	private BigDecimal freightPrice = BigDecimal.ZERO;

	// 调整运输费用
	@ApiModelProperty(value = "调整运输费用")
	private BigDecimal publishFreightPrice = BigDecimal.ZERO;

	// 成本总价
	@ApiModelProperty(value = "成本总价")
	private BigDecimal totalPrice = BigDecimal.ZERO;

	// 调整成本总价
	@ApiModelProperty(value = "调整成本总价")
	private BigDecimal publishTotalPrice = BigDecimal.ZERO;

	// 可执行标识(1-可执行 2-难执行 3-无法执行)
	@ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
	private Integer executeSign = 1;

	// 预计天数
	@ApiModelProperty(value = "预计天数")
	private Integer expectedDays = 1;

	// 备注
	@ApiModelProperty(value = "备注")
	private String remark;

	// 发布 0-不发布 1-发布
	@ApiModelProperty(value = "发布 0-不发布 1-发布")
	private Integer publish = 0;

	// 货品数量
	@ApiModelProperty(value = "货品数量")
	private BigDecimal goodsQuentity;

	// 销售单价
	@ApiModelProperty(value = "销售单价")
	private BigDecimal salePrice;

	// 销售金额
	@ApiModelProperty(value = "销售金额")
	private BigDecimal saleAmount;

	// 运输日期
	@ApiModelProperty(value = "运输日期")
	private Date transportDate;

	// 状态（计划状态，计划状态改变时改变）
	@ApiModelProperty(value = "状态（计划状态，计划状态改变时改变）")
	private Integer status;

	// 是否采购 0-仓库 1-采购
	@ApiModelProperty(value = "是否采购 0-仓库 1-采购")
	private Integer purchase;

	public static final int WAREHOUSE = 0;
	public static final int PURCHSE = 1;

	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	// 创建人
	@ApiModelProperty(value = "创建人")
	private User creator;

	// 修改时间戳(初始值为当前时间)
	@ApiModelProperty(value = "修改时间戳(初始值为当前时间)")
	private Date updateTime;

	// 组织ID(机构ID)
	@ApiModelProperty(value = "组织ID(机构ID)")
	private Organization org;

	// 账套ID
	@ApiModelProperty(value = "账套ID")
	private FiscalAccount fiscalAccount;

	//明细s
	private List<PlanGoodsDetail> details = Lists.newArrayList();

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FPLAN_ID")
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_SPEC_ID")
	public GoodsSpec getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(GoodsSpec goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	@Column(name = "FGOODS_QUENTITY")
	public BigDecimal getGoodsQuentity() {
		return goodsQuentity;
	}

	public void setGoodsQuentity(BigDecimal goodsQuentity) {
		this.goodsQuentity = goodsQuentity;
	}

	@Column(name = "FSALE_PRICE")
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Column(name = "FPURCHASE")
	public Integer getPurchase() {
		return purchase;
	}

	public void setPurchase(Integer purchase) {
		this.purchase = purchase;
	}

	@Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
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

	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}

	@Column(name = "FBILL_DATE")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Column(name = "FROUTE")
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
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
	@JoinColumn(name = "FCUSTOMER_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FPURCHASE_ID")
	public PurchasePrice getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(PurchasePrice purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FGOODS_UINT_ID")
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
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

	@Column(name = "FFACTORY_PRICE")
	public BigDecimal getFactoryPrice() {
		return factoryPrice;
	}
	public void setFactoryPrice(BigDecimal factoryPrice) {
		this.factoryPrice = factoryPrice;
	}

	@Column(name = "FPUBLISH_FACTORY_PRICE")
	public BigDecimal getPublishFactoryPrice() {
		return publishFactoryPrice;
	}

	public void setPublishFactoryPrice(BigDecimal publishFactoryPrice) {
		this.publishFactoryPrice = publishFactoryPrice;
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

	@Column(name = "FTOTAL_PRICE")
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "FPUBLISH_TOTAL_PRICE")
	public BigDecimal getPublishTotalPrice() {
		return publishTotalPrice;
	}

	public void setPublishTotalPrice(BigDecimal publishTotalPrice) {
		this.publishTotalPrice = publishTotalPrice;
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

	@Column(name = "FREMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "FPUBLISH")
	public Integer getPublish() {
		return publish;
	}

	public void setPublish(Integer publish) {
		this.publish = publish;
	}

	@Column(name = "FSALE_AMOUNT")
	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	@Column(name = "FTRANSPORT_DATE")
	public Date getTransportDate() {
		return transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	@Column(name = "FSTATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bill")
	public List<PlanGoodsDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PlanGoodsDetail> details) {
		this.details = details;
	}
}