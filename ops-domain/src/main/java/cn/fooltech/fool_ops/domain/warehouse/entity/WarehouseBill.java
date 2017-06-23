package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>仓库单据</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月16日
 */
@Entity
@Table(name = "TSB_WAREHOUSE_BILL")
public class WarehouseBill extends OpsOrgEntity {

    public static final int IN = 1;//入仓
    public static final int OUT = -1;//出仓
    public static final int MOVE_IN = 2;//移入仓
    public static final int MOVE_OUT = -2;//移出仓
    public static final int REVERSE_IN = 3;//反向入
    public static final int REVERSE_OUT = -3;//反向出
    public static final int NOT_STARTED = 0;//--未开工
    public static final int CARRIED_OUT = 1;//1--进行中
    public static final int COMPLETED = 2;//2--已完成
    public static final int SUSPENDED = 3;//3--已暂停
    public static final int DISCONTINUED = 4;//4--已中止
    /**
     * 状态- 未审核
     */
    public static final int STATUS_UNAUDITED = 0;
    /**
     * 状态- 已审核
     */
    public static final int STATUS_AUDITED = 1;
    /**
     * 状态- 已作废
     */
    public static final int STATUS_CANCELED = 2;
    /**
     * 状态- 已勾对
     */
    public static final short CHECKED=1;
    /**
     * 状态- 未勾对
     */
    public static final short NOCHECKED=0;
    private static final long serialVersionUID = 766255994053181260L;
    
    /**
     * 单号
     */
    private String code;
    /**
     * 凭证号
     */
    private String voucherCode;
    /**
     * 进仓仓库(默认使用)
     */
    private AuxiliaryAttr inWareHouse;
    /**
     * 出仓仓库
     */
    private AuxiliaryAttr outWareHouse;
    /**
     * 客户/销售商
     */
    private Customer customer;
    /**
     * 供应商
     */
    private Supplier supplier;
    /**
     * 部门
     */
    private Organization dept;
    /**
     * 创建人所属部门
     */
    private Organization creatorDept;
    /**
     * 描述
     */
    private String describe;
    /**
     * 状态
     */
    private Integer recordStatus = STATUS_UNAUDITED;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 会计期间
     */
    private StockPeriod stockPeriod;

    /**
     * 收货人(默认使用)
     */
    private Member inMember;

    /**
     * 发货人
     */
    private Member outMember;

    /**
     * 单据日期
     */
    private Date billDate;

    /**
     * 计划完成日期
     */
    private Date endDate;

    /**
     * 合计金额
     */
    private BigDecimal totalAmount = new BigDecimal(0);

    /**
     * 免单金额
     */
    private BigDecimal freeAmount = new BigDecimal(0);

    /**
     * 累计收付款金额
     */
    private BigDecimal totalPayAmount = new BigDecimal(0);

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人
     */
    private User auditor;

    /**
     * 作废时间
     */
    private Date cancelTime;

    /**
     * 作废人
     */
    private User cancelor;

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
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 费用金额
     */
    private BigDecimal expenseAmount = BigDecimal.ZERO;

    /**
     * 其他费用
     */
    private BigDecimal otherCharges = new BigDecimal(0);

    /**
     * 生产状况
     */
    private Integer productionStatus;

    /**
     * 计划开工日期
     */
    private Date planStart;

    /**
     * 运输批号
     */
    private String transportNo;

    //发货地ID
    private FreightAddress deliveryPlace;

    //收货地ID
    private FreightAddress receiptPlace;

    //运输方式ID
    private AuxiliaryAttr transportType;

    //装运方式ID
    private AuxiliaryAttr shipmentType;

    //车船号
    private String carNo;

    //司机姓名
    private String driverName;

    //司机电话
    private String driverPhone;

    //扣费金额
    private BigDecimal deductionAmount;
    
    /**
     * 明细
     */
    private List<WarehouseBillDetail> details = new ArrayList<WarehouseBillDetail>(0);
    
    //单据关联事件（计划关联单据使用）
    private Task task;
    private short isCheck=NOCHECKED;//是否勾对
    /**
     * 单据关联事件（计划关联单据使用）
     * @return
     */
    @JoinColumn(name = "FEVENT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Column(name = "FTRANSPORT_NO")
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
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

    @Column(name = "FCAR_NO")
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @Column(name = "FDRIVER_NAME")
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Column(name = "FDRIVER_PHONE")
    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    @Column(name = "FDEDUCTION_AMOUNT")
    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    /**
     * 获取单号
     *
     * @return
     */
    @Column(name = "FCODE", length = 50, nullable = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置单号
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取凭证号
     *
     * @return
     */
    @Column(name = "FVOUCHERCODE", length = 50)
    public String getVoucherCode() {
        return voucherCode;
    }

    /**
     * 设置凭证号
     *
     * @param voucherCode
     */
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
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
     * 获取客户
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCUSTOMER_ID")
    public Customer getCustomer() {
        return customer;
    }

    /**
     * 设置客户
     *
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * 获取供应商
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * 设置供应商
     *
     * @param supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * 获取部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEP_ID")
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
     * 获取创建人所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_DEPT_ID")
    public Organization getCreatorDept() {
        return creatorDept;
    }

    /**
     * 设置获取创建人所属部门
     *
     * @param creatorDept
     */
    public void setCreatorDept(Organization creatorDept) {
        this.creatorDept = creatorDept;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE")
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
     * 获取状态
     *
     * @return
     */
    @Column(name = "RECORD_STATUS", nullable = false)
    public Integer getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置状态
     *
     * @param recordStatus
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE", nullable = false)
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置单据类型
     *
     * @param billType
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取会计期间
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSTOCK_PERIOD_ID", nullable = false)
    public StockPeriod getStockPeriod() {
        return stockPeriod;
    }

    /**
     * 设置会计期间
     *
     * @param stockPeriod
     */
    public void setStockPeriod(StockPeriod stockPeriod) {
        this.stockPeriod = stockPeriod;
    }

    /**
     * 获取收货人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_USER_ID")
    public Member getInMember() {
        return inMember;
    }

    /**
     * 设置收货人
     *
     * @param inMember
     */
    public void setInMember(Member inMember) {
        this.inMember = inMember;
    }

    /**
     * 获取发货人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOUT_USER_ID")
    public Member getOutMember() {
        return outMember;
    }

    /**
     * 设置发货人
     *
     * @param outMember
     */
    public void setOutMember(Member outMember) {
        this.outMember = outMember;
    }

    /**
     * 获取单据日期
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FBILL_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getBillDate() {
        return billDate;
    }

    /**
     * 设置单据日期
     *
     * @param billDate
     */
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    /**
     * 获取计划完成日期
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPLAN_END")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置计划完成日期
     *
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取合计金额
     *
     * @return
     */
    @Column(name = "FTOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置合计金额
     *
     * @param totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获取免单金额
     *
     * @return
     */
    @Column(name = "FFREE_AMOUNT")
    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    /**
     * 设置免单金额
     *
     * @param freeAmount
     */
    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    /**
     * 获取累计收付款金额
     *
     * @return
     */
    @Column(name = "FTOTAL_PAY_AMOUNT")
    public BigDecimal getTotalPayAmount() {
        return totalPayAmount;
    }

    /**
     * 设置累计收付款金额
     *
     * @param totalPayAmount
     */
    public void setTotalPayAmount(BigDecimal totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    /**
     * 获取审核时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 设置审核时间
     *
     * @param auditTime
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 获取审核人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    /**
     * 设置审核人
     *
     * @param auditor
     */
    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    /**
     * 获取取消时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCANCEL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     * 设置取消时间
     *
     * @param cancelTime
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 获取取消人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCANCELOR")
    public User getCancelor() {
        return cancelor;
    }

    /**
     * 设置取消人
     *
     * @param cancelor
     */
    public void setCancelor(User cancelor) {
        this.cancelor = cancelor;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取费用金额
     *
     * @return
     */
    @Column(name = "FEXPENSE_AMOUNT")
    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    /**
     * 设置费用金额
     *
     * @param expenseAmount
     */
    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    /**
     * 获取其他费用
     *
     * @return
     */
    @Column(name = "FOTHER_CHARGES")
    public BigDecimal getOtherCharges() {
        return otherCharges;
    }

    /**
     * 设置其他费用
     *
     * @param otherCharges
     */
    public void setOtherCharges(BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    /**
     * 获取生产状况
     *
     * @return
     */
    @Column(name = "FPRODUCTION_STATUS")
    public Integer getProductionStatus() {
        return productionStatus;
    }

    /**
     * 设置生产状况
     *
     * @param productionStatus
     */
    public void setProductionStatus(Integer productionStatus) {
        this.productionStatus = productionStatus;
    }

    /**
     * 获取计划开工日期
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPLAN_START")
    public Date getPlanStart() {
        return planStart;
    }

    /**
     * 设置计划开工日期
     *
     * @param planStart
     */
    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    /**
     * 获取明细
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "bill")
    public List<WarehouseBillDetail> getDetails() {
        return details;
    }
    /**
     * 获取是否勾对
     * @return
 	*/
    @Column(name="fis_check")
	public short getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(short isCheck) {
		this.isCheck = isCheck;
	}


	/**
     * 设置明细
     *
     * @param details
     */
    public void setDetails(List<WarehouseBillDetail> details) {
        this.details = details;
    }



}
