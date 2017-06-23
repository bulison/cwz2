package cn.fooltech.fool_ops.domain.basedata.vo;

import java.math.BigDecimal;
/**
 * 
 * @author hjr	
 * 2017/3/13
 */
public class TransportLossVo {
	//主键
	private String fid;
	//货品ID，关联货品表
	private String goodsId;
	//货品名称
	private String goods;

	//货品属性，关联货品属性表
	private String goodsSpecId;
	//货品属性名称
	private String goodsSpec;
	//损耗发货地ID  关联辅助属性表损耗
	private String deliveryId;
	//发货地名称
	private String delivery;
	//损耗收货地ID  关联辅助属性表损耗
	private String receiptId;
	//收货地名称
	private String receipt;
	//装运方式ID  关联辅助属性装运方式
	private String shipmentId;
	//装运方式名称
	private String shipment;
	//损耗百分比 不超过100
	private BigDecimal paymentAmonut;
	//备注
	private String remark;
	//创建时间
	private String createTime;
	//创建人
	private String createId;
	//创建人
	private String create;
	//修改时间戳,初始值为当前时间
	private String updateTime;
	//组织ID,机构ID
	private String orgId;
	//账套ID
	private String fiscalAccountId;
	//搜索key
	private String searchKey;
	public String getFid() {
		return fid;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public String getGoods() {
		return goods;
	}
	public String getGoodsSpecId() {
		return goodsSpecId;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public String getDeliveryId() {
		return deliveryId;
	}
	public String getDelivery() {
		return delivery;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public String getReceipt() {
		return receipt;
	}
	public String getShipmentId() {
		return shipmentId;
	}

	public BigDecimal getPaymentAmonut() {
		return paymentAmonut;
	}
	public String getRemark() {
		return remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getCreateId() {
		return createId;
	}
	public String getCreate() {
		return create;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public String getOrgId() {
		return orgId;
	}
	public String getFiscalAccountId() {
		return fiscalAccountId;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public void setGoodsSpecId(String goodsSpecId) {
		this.goodsSpecId = goodsSpecId;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	public void setPaymentAmonut(BigDecimal paymentAmonut) {
		this.paymentAmonut = paymentAmonut;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public void setCreate(String create) {
		this.create = create;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setFiscalAccountId(String fiscalAccountId) {
		this.fiscalAccountId = fiscalAccountId;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	
	
}
