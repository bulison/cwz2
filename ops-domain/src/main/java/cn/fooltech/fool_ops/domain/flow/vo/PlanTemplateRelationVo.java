package cn.fooltech.fool_ops.domain.flow.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class PlanTemplateRelationVo implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 计划模板ID
	private String planTemplateId;
	private String planTemplateName;

	// 往来单位ID 关联供应商表或销售商表ID
	private String csvId;
	private String csvName;

	// 发货地ID 关联货运地址表
	private String deliveryPlaceId;
	private String deliveryPlaceName;

	// 收货地ID 关联货运地址表
	private String receiptPlaceId;
	private String receiptPlaceName;

	// 运输方式ID 关联辅助属性运输方式
	private String transportTypeId;
	private String transportTypeName;

	// 装运方式ID 关联辅助属性装运方式
	private String shipmentTypeId;
	private String shipmentTypeName;

	// 创建时间
	private Date createTime;

	// 创建人
	private String creatorId;
	private String creatorName;

	// 修改时间戳(初始值为当前时间)
	private Date updateTime;

	// 组织ID(机构ID)
	private String orgId;
	private String orgName;

	// 账套ID
	private String fiscalAccountId;
	private String fiscalAccountName;
	@ApiModelProperty(value = "开始日期，查询用")
	private Date startDay;

	@ApiModelProperty(value = "结束日期，查询用")
	private Date endDay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanTemplateId() {
		return planTemplateId;
	}

	public void setPlanTemplateId(String planTemplateId) {
		this.planTemplateId = planTemplateId;
	}

	public String getPlanTemplateName() {
		return planTemplateName;
	}

	public void setPlanTemplateName(String planTemplateName) {
		this.planTemplateName = planTemplateName;
	}

	public String getCsvId() {
		return csvId;
	}

	public void setCsvId(String csvId) {
		this.csvId = csvId;
	}

	public String getCsvName() {
		return csvName;
	}

	public void setCsvName(String csvName) {
		this.csvName = csvName;
	}

	public String getDeliveryPlaceId() {
		return deliveryPlaceId;
	}

	public void setDeliveryPlaceId(String deliveryPlaceId) {
		this.deliveryPlaceId = deliveryPlaceId;
	}

	public String getDeliveryPlaceName() {
		return deliveryPlaceName;
	}

	public void setDeliveryPlaceName(String deliveryPlaceName) {
		this.deliveryPlaceName = deliveryPlaceName;
	}

	public String getReceiptPlaceId() {
		return receiptPlaceId;
	}

	public void setReceiptPlaceId(String receiptPlaceId) {
		this.receiptPlaceId = receiptPlaceId;
	}

	public String getReceiptPlaceName() {
		return receiptPlaceName;
	}

	public void setReceiptPlaceName(String receiptPlaceName) {
		this.receiptPlaceName = receiptPlaceName;
	}

	public String getTransportTypeId() {
		return transportTypeId;
	}

	public void setTransportTypeId(String transportTypeId) {
		this.transportTypeId = transportTypeId;
	}

	public String getTransportTypeName() {
		return transportTypeName;
	}

	public void setTransportTypeName(String transportTypeName) {
		this.transportTypeName = transportTypeName;
	}

	public String getShipmentTypeId() {
		return shipmentTypeId;
	}

	public void setShipmentTypeId(String shipmentTypeId) {
		this.shipmentTypeId = shipmentTypeId;
	}

	public String getShipmentTypeName() {
		return shipmentTypeName;
	}

	public void setShipmentTypeName(String shipmentTypeName) {
		this.shipmentTypeName = shipmentTypeName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getFiscalAccountId() {
		return fiscalAccountId;
	}

	public void setFiscalAccountId(String fiscalAccountId) {
		this.fiscalAccountId = fiscalAccountId;
	}

	public String getFiscalAccountName() {
		return fiscalAccountName;
	}

	public void setFiscalAccountName(String fiscalAccountName) {
		this.fiscalAccountName = fiscalAccountName;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}
	
	 

}