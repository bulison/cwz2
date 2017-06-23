package cn.fooltech.fool_ops.domain.basedata.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;

import org.hibernate.annotations.GenericGenerator;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
/**
 * 
 * @author hjr
 *2017/3/13
 */
@Entity
@Table(name="tsb_transport_loss")
public class TransportLoss {
	//主键
	private String fid;
	//货品ID，关联货品表
	private Goods goods;
	//货品属性，关联货品属性表
	private GoodsSpec goodsSpec;
	//损耗发货地ID  关联辅助属性表损耗
	private AuxiliaryAttr delivery;
	//损耗收货地ID  关联辅助属性表损耗
	private AuxiliaryAttr receipt;
	//装运方式ID  关联辅助属性装运方式
	private AuxiliaryAttr shipment;
	//损耗百分比 不超过100
	private BigDecimal paymentAmonut;
	//备注
	private String remark;
	//创建时间
	private Date createTime;
	//创建人
	private User create;
	//修改时间戳,初始值为当前时间
	private Date updateTime;
	//组织ID,机构ID
	private Organization org;
	//账套ID
	private FiscalAccount fiscalAccount;
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	
	//获取货品ID，关联货品表
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FGOODS_ID")
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	//获取货品属性，关联货品属性表
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FGOODS_SPEC_ID")
	public GoodsSpec getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(GoodsSpec goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	//获取损耗发货地ID  关联辅助属性表损耗
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FDELIVERY_ID")
	public AuxiliaryAttr getDelivery() {
		return delivery;
	}
	public void setDelivery(AuxiliaryAttr delivery) {
		this.delivery = delivery;
	}
	//获取损耗收货地ID  关联辅助属性表损耗
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FRECEIPT_ID")
	public AuxiliaryAttr getReceipt() {
		return receipt;
	}
	public void setReceipt(AuxiliaryAttr receipt) {
		this.receipt = receipt;
	}
	//获取装运方式ID  关联辅助属性装运方式
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FSHIPMENT_TYPE_ID")
	public AuxiliaryAttr getShipment() {
		return shipment;
	}
	public void setShipment(AuxiliaryAttr shipment) {
		this.shipment = shipment;
	}
	//获取损耗百分比 不超过100
	@Column(name="FPAYMENT_AMOUNT")
	@Max(value=100)
	public BigDecimal getPaymentAmonut() {
		return paymentAmonut;
	}






	public void setPaymentAmonut(BigDecimal paymentAmonut) {
		this.paymentAmonut = paymentAmonut;
	}
	//获取备注
	@Column(name="FREMARK",length=200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	//获取创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//获取创建人
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FCREATOR_ID", nullable = false)
	public User getCreate() {
		return create;
	}
	public void setCreate(User create) {
		this.create = create;
	}
	//获取更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	//获取机构ID
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	//获取账套ID
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}
	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
	
}
