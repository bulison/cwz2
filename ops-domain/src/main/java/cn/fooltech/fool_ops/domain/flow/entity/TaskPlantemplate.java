package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划事件关联模板表
 */
@ApiModel("计划事件关联模板表")
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tflow_task_plantemplate")
public class TaskPlantemplate {

    private static final long serialVersionUID = 1L;


    //主键
    private String id;


    //计划ID
    private Plan plan;


    //计划模板
    private PlanTemplate planTemplate;


    //往来单位ID  关联供应商表或销售商表ID
    private String customerId;


    //发货地ID 关联货运地址表
    private FreightAddress deliveryPlace;


    //收货地ID 关联货运地址表
    private FreightAddress receiptPlace;


    //运输方式ID 关联辅助属性运输方式
    private AuxiliaryAttr transportType;


    //装运方式ID 关联辅助属性装运方式
    private AuxiliaryAttr shipmentType;


    //模板类型，1-采购、2-运输、3-销售
    private Integer templateType;


    //对应计划货品表从表的运输路径ID，当模板类型为2时才记录，可记录多条ID，用逗号隔开
    private String transportId;


    //开始时间
    private Date date;


    //金额
    private BigDecimal amount;


    //创建时间
    private Date createTime;


    //创建人
    private User creator;


    //修改时间戳 初始值为当前时间
    private Date updateTime;


    //组织ID 机构ID
    private Organization org;


    //账套ID
    private FiscalAccount fiscalAccount;

    //运输数量
    private BigDecimal transportQuentity;

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

    @JoinColumn(name = "FPLAN_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @JoinColumn(name = "FPLAN_TEMPLATE_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public PlanTemplate getPlanTemplate() {
        return planTemplate;
    }

    public void setPlanTemplate(PlanTemplate planTemplate) {
        this.planTemplate = planTemplate;
    }

    @Column(name = "FCUSTOMER_ID")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    @Column(name = "FTEMPLATE_TYPE")
    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    @Column(name = "FTRANSPORT_ID")
    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    @Column(name = "FDATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    @Column(name = "FTRANSPORT_QUENTITY")
    public BigDecimal getTransportQuentity() {
        return transportQuentity;
    }

    public void setTransportQuentity(BigDecimal transportQuentity) {
        this.transportQuentity = transportQuentity;
    }
}