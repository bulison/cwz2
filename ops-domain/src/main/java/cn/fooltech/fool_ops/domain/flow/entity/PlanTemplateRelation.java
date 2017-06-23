package cn.fooltech.fool_ops.domain.flow.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

/**
 * 计划模板关联
 * 
 * @author cwz
 * @date 2017-2-8
 */
@Entity
@Table(name = "tflow_plan_template_relation")
public class PlanTemplateRelation implements Serializable{

	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 计划模板ID
	private PlanTemplate planTemplate;

	// 往来单位ID 关联供应商表或销售商表ID
	private CustomerSupplierView csv;

	// 发货地ID 关联货运地址表
	private FreightAddress deliveryPlace;

	// 收货地ID 关联货运地址表
	private FreightAddress receiptPlace;

	// 运输方式ID 关联辅助属性运输方式
	private AuxiliaryAttr transportType;

	// 装运方式ID 关联辅助属性装运方式
	private AuxiliaryAttr shipmentType;

	// 创建时间
	private Date createTime;

	// 创建人
	private User creator;

	// 修改时间戳(初始值为当前时间)
	private Date updateTime;

	// 组织ID(机构ID)
	private Organization org;

	// 账套ID
	private FiscalAccount fiscalAccount;

	//模板类型（1-采购、2-运输、3-销售）
	private Integer templateType;

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
	@JoinColumn(name = "FPLAN_TEMPLATE_ID")
	public PlanTemplate getPlanTemplate() {
		return planTemplate;
	}

	public void setPlanTemplate(PlanTemplate planTemplate) {
		this.planTemplate = planTemplate;
	}

	@JoinColumn(name = "FCUSTOMER_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public CustomerSupplierView getCsv() {
		return csv;
	}

	public void setCsv(CustomerSupplierView csv) {
		this.csv = csv;
	}

	@JoinColumn(name = "FDELIVERY_PLACE")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public FreightAddress getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(FreightAddress deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	@JoinColumn(name = "FRECEIPT_PLACE")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public FreightAddress getReceiptPlace() {
		return receiptPlace;
	}

	public void setReceiptPlace(FreightAddress receiptPlace) {
		this.receiptPlace = receiptPlace;
	}

	@JoinColumn(name = "FTRANSPORT_TYPE_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public AuxiliaryAttr getTransportType() {
		return transportType;
	}

	public void setTransportType(AuxiliaryAttr transportType) {
		this.transportType = transportType;
	}

	@JoinColumn(name = "FSHIPMENT_TYPE_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public AuxiliaryAttr getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(AuxiliaryAttr shipmentType) {
		this.shipmentType = shipmentType;
	}

	@Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JoinColumn(name = "FCREATOR_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
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

	@JoinColumn(name = "FORG_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	@JoinColumn(name = "FACC_ID")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}

	@Column(name = "FTEMPLATE_TYPE")
	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
}