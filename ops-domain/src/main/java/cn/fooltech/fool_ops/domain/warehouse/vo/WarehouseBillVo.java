package cn.fooltech.fool_ops.domain.warehouse.vo;
import cn.fooltech.fool_ops.validator.bill.*;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>表单传输对象- 仓库单据记录明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
@ToString
public class WarehouseBillVo implements Serializable {

    private static final long serialVersionUID = 3032473960354236837L;

    /**
     * ID
     */
    private String fid;

    /**
     * 单号
     */
    @NotEmpty(message = "单号必填", groups = {Qckc.class, Cgsqd.class, Cgxjd.class,
            Xsfp.class, Cgdd.class, Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scll.class, Sctl.class, Cprk.class, Cptk.class,
            Dcd.class, Bsd.class, Pdd.class, Cgfp.class, Xsfp.class, Qcyf.class})
    private String code;

    /**
     * 单据日期
     */
    @NotNull(message = "单据日期必填", groups = {Cgxjd.class, Xsfp.class, Cgdd.class,
            Cgrk.class, Cgsqd.class, Cgth.class, Xsdd.class, Xsch.class, Xsth.class,
            Scjhd.class, Scll.class, Sctl.class, Cprk.class, Cptk.class, Dcd.class,
            Bsd.class, Pdd.class, Cgfp.class, Fhd.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    /**
     * 计划完成日期
     */
    @NotNull(message = "计划完成日期必填", groups = {Cgdd.class, Cgrk.class, Cgsqd.class,
            Cgth.class, Xsdd.class, Xsch.class, Xsth.class, Scjhd.class, Scll.class,
            Sctl.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 客户ID
     */
    @NotEmpty(message = "客户必填", groups = {Xsfp.class, Xsbj.class, Xsdd.class, Xsch.class, Xsth.class, Scjhd.class})
    private String customerId;

    /**
     * 供应商ID
     */
    @NotEmpty(message = "供应商必填", groups = {Cgxjd.class, Cgdd.class, Cgrk.class, Cgth.class, Cgfp.class})
    private String supplierId;

    /**
     * 部门ID
     */
    @NotEmpty(message = "部门必填", groups = {Cgxjd.class, Xsfp.class, Cgdd.class,
            Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scjhd.class, Scll.class, Sctl.class, Cprk.class, Cptk.class, Cgfp.class})
    private String deptId;

    /**
     * 收货人ID
     */
    @NotEmpty(message = "人员必填", groups = {Cgxjd.class, Xsfp.class, Cgdd.class,
            Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scll.class, Sctl.class, Cprk.class, Cptk.class,
            Dcd.class, Bsd.class, Pdd.class, Cgfp.class})
    private String inMemberId;

    /**
     * 凭证号
     */
    @Length(max = 50, message = "原始单号不能超过{max}个字符", groups = {Qckc.class, Cgsqd.class, Cgxjd.class,
            Xsfp.class, Cgdd.class, Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scll.class, Sctl.class, Cprk.class, Cptk.class,
            Dcd.class, Bsd.class, Pdd.class, Cgfp.class, Xsfp.class})
    private String voucherCode;

    /**
     * 进仓仓库ID
     */
    @NotEmpty(message = "进仓仓库必填", groups = {Dcd.class, Pdd.class})
    private String inWareHouseId;

    /**
     * 进仓仓库名称
     */
    private String inWareHouseName;

    /**
     * 出仓仓库ID
     */
    @NotEmpty(message = "出仓仓库必填", groups = {Dcd.class})
    private String outWareHouseId;

    /**
     * 出仓仓库名称
     */
    private String outWareHouseName;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户地址
     */
    private String customerAddress;

    /**
     * 客户联系人
     */
    private String customerContact;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商电话
     */
    private String supplierPhone;

    /**
     * 供应商地址
     */
    private String supplierAddress;

    /**
     * 供应商联系人
     */
    private String supplierContact;

    /**
     * 开单人名称
     */
    private String userName;


    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 描述
     */
    @Length(max = 200, message = "备注不能超过{max}个字符", groups = {Qckc.class, Cgsqd.class, Cgxjd.class,
            Xsfp.class, Cgdd.class, Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scll.class, Sctl.class, Cprk.class, Cptk.class,
            Dcd.class, Bsd.class, Pdd.class, Cgfp.class, Xsfp.class, Qcys.class, Qcyf.class})
    private String describe;

    /**
     * 状态
     */
    private Integer recordStatus;

    /**
     * 单据类型
     */
    @NotNull(message = "单据类型必填", groups = {Qckc.class, Cgsqd.class, Cgxjd.class,
            Xsfp.class, Cgdd.class, Cgrk.class, Cgsqd.class, Cgth.class, Xsbj.class, Xsdd.class, Xsch.class,
            Xsth.class, Scll.class, Sctl.class, Cprk.class, Cptk.class,
            Dcd.class, Bsd.class, Pdd.class, Cgfp.class, Xsfp.class, Qcys.class, Qcyf.class})
    private Integer billType;

    /**
     * 会计期间ID
     */
    private String stockPeriodId;

    /**
     * 会计期间详细
     */
    private String stockPeriodDetail;

    /**
     * 收货人名称
     */
    private String inMemberName;

    /**
     * 发货人ID
     */
    @NotEmpty(message = "人员必填", groups = {Dcd.class})
    private String outMemberId;

    /**
     * 发货人名称
     */
    private String outMemberName;


    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 合计金额
     */
    private String totalAmount;

    /**
     * 免单金额
     */
    private String freeAmount;

    /**
     * 累计收付款金额
     */
    private String totalPayAmount;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 审核人名称
     */
    private String auditorId;

    /**
     * 审核人名称
     */
    private String auditorName;

    /**
     * 作废时间
     */
    private String cancelTime;

    /**
     * 作废人名称
     */
    private String cancelorName;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 页面搜索关键字- 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    /**
     * 页面搜索关键字- 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 页面搜索关键字- 货品ID
     */
    private String goodsId;

    /**
     * 前台传过来的单据明细集合(JSON数组格式)
     */
    private String transportDetails;
    /**
     * 前台传过来的单据明细集合(JSON数组格式)
     */
    private String details;
    /**
     * 单据关联ID
     */
    private String relationId;
    /**
     * 关联单据的名称
     */
    private String relationName;

    /**
     * 关联单据的单号
     */
    private String relationCode;

    /**
     * 查找未勾对收付款单据时用
     */
    private String paymentBillId;
    /**
     * 查找未勾对费用单据时用
     */
    private String costBillId;
    /**
     * 客户供应商合并显示
     */
    private String csvId;
    private String csvName;
    private Integer csvType;
    /**
     * 勾兑时候用的标识
     */
    private String detal = "1";
    /**
     * 单号或凭证号
     */
    private String codeOrVoucherCode;
    /**
     * 生产状况
     */
    private Integer productionStatus;
    /**
     * 其他费用
     */
    private String otherCharges;
    /**
     * 计划开工日期
     */
    @NotNull(message = "计划开始日期必填", groups = {Scjhd.class})
    @JSONField(format = "yyyy-MM-dd")
    private Date planStart;
    /**
     * 申请单号Id
     */
    private String applyIds;
    /**
     * 申请单号名称
     */
    private String applyCodes;
    /**
     * 费用金额
     */
    private String expenseAmount;
    /**
     * 运输批号
     */
    @NotEmpty(message = "运输批号必填", groups = {Fhd.class})
    private String transportNo;
    //发货地ID
    @NotEmpty(message = "发货地必填", groups = {Fhd.class})
    private String deliveryPlaceId;
    private String deliveryPlaceName;
    //收货地ID
    @NotEmpty(message = "收货地必填", groups = {Fhd.class})
    private String receiptPlaceId;
    private String receiptPlaceName;
    //运输方式ID
    @NotEmpty(message = "运输方式必填", groups = {Fhd.class})
    private String transportTypeId;
    private String transportTypeName;
    //装运方式ID
    @NotEmpty(message = "装运方式必填", groups = {Fhd.class})
    private String shipmentTypeId;
    private String shipmentTypeName;
    //车船号
    private String carNo;
    //司机姓名
    private String driverName;
    //司机电话
    private String driverPhone;
    //扣费金额
    private BigDecimal deductionAmount;

    //箱号
    private String containerNumber;

    //封号
    private String sealingNumber;
    
    //单据关联事件id（计划关联单据使用）
    private String taskId;
    
   //单据关联事件名称（计划关联单据使用）
    private String taskName;
    //关联事件id过滤查询。等于1才处理
    private Integer taskFilter;
    private short ischeck;
    /**
     * 单据关联事件id（计划关联单据使用）
     * @return
     */
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 单据关联事件名称（计划关联单据使用）
	 * @return
	 */
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

    public String getTransportDetails() {
        return transportDetails;
    }

    public void setTransportDetails(String transportDetails) {
        this.transportDetails = transportDetails;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getInWareHouseId() {
        return inWareHouseId;
    }

    public void setInWareHouseId(String inWareHouseId) {
        this.inWareHouseId = inWareHouseId;
    }

    public String getInWareHouseName() {
        return inWareHouseName;
    }

    public void setInWareHouseName(String inWareHouseName) {
        this.inWareHouseName = inWareHouseName;
    }

    public String getOutWareHouseId() {
        return outWareHouseId;
    }

    public void setOutWareHouseId(String outWareHouseId) {
        this.outWareHouseId = outWareHouseId;
    }

    public String getOutWareHouseName() {
        return outWareHouseName;
    }

    public void setOutWareHouseName(String outWareHouseName) {
        this.outWareHouseName = outWareHouseName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getStockPeriodId() {
        return stockPeriodId;
    }

    public void setStockPeriodId(String stockPeriodId) {
        this.stockPeriodId = stockPeriodId;
    }

    public String getStockPeriodDetail() {
        return stockPeriodDetail;
    }

    public void setStockPeriodDetail(String stockPeriodDetail) {
        this.stockPeriodDetail = stockPeriodDetail;
    }

    public String getInMemberId() {
        return inMemberId;
    }

    public void setInMemberId(String inMemberId) {
        this.inMemberId = inMemberId;
    }

    public String getInMemberName() {
        return inMemberName;
    }

    public void setInMemberName(String inMemberName) {
        this.inMemberName = inMemberName;
    }

    public String getOutMemberId() {
        return outMemberId;
    }

    public void setOutMemberId(String outMemberId) {
        this.outMemberId = outMemberId;
    }

    public String getOutMemberName() {
        return outMemberName;
    }

    public void setOutMemberName(String outMemberName) {
        this.outMemberName = outMemberName;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(String totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelorName() {
        return cancelorName;
    }

    public void setCancelorName(String cancelorName) {
        this.cancelorName = cancelorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * 获取前台表单传过来的单据明细集合
     */
    @SuppressWarnings("rawtypes")
    @JsonIgnore
    public List<WarehouseBillDetailVo> getDetailList() {
        List<WarehouseBillDetailVo> list = new ArrayList<WarehouseBillDetailVo>();
        if (StringUtils.isNotBlank(this.details)) {
            JSONArray array = JSONArray.fromObject(this.details);
            List details = (List) JSONArray.toCollection(array, WarehouseBillDetailVo.class);
            Iterator iterator = details.iterator();
            while (iterator.hasNext()) {
                WarehouseBillDetailVo detail = (WarehouseBillDetailVo) iterator.next();
                detail.setBillType(billType);
                list.add(detail);
            }
        }
        return list;
    }

    /**
     * 获取前台表单传过来的单据明细集合
     */
    @SuppressWarnings("rawtypes")
    @JsonIgnore
    public List<TransportBilldetailVo> getTransportBilldetailList() {
        List<TransportBilldetailVo> list = new ArrayList<TransportBilldetailVo>();
        if (StringUtils.isNotBlank(this.transportDetails)) {
            JSONArray array = JSONArray.fromObject(this.transportDetails);
            List details = (List) JSONArray.toCollection(array, TransportBilldetailVo.class);
            Iterator iterator = details.iterator();
            while (iterator.hasNext()) {
                TransportBilldetailVo detail = (TransportBilldetailVo) iterator.next();
                list.add(detail);
            }
        }
        return list;
    }

    public String getPaymentBillId() {
        return paymentBillId;
    }

    public void setPaymentBillId(String paymentBillId) {
        this.paymentBillId = paymentBillId;
    }

    public String getCostBillId() {
        return costBillId;
    }

    public void setCostBillId(String costBillId) {
        this.costBillId = costBillId;
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

    public Integer getCsvType() {
        return csvType;
    }

    public void setCsvType(Integer csvType) {
        this.csvType = csvType;
    }

    public String getDetal() {
        return detal;
    }

    public void setDetal(String detal) {
        this.detal = detal;
    }

    public String getCodeOrVoucherCode() {
        return codeOrVoucherCode;
    }

    public void setCodeOrVoucherCode(String codeOrVoucherCode) {
        this.codeOrVoucherCode = codeOrVoucherCode;
    }

    public Integer getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(Integer productionStatus) {
        this.productionStatus = productionStatus;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        this.otherCharges = otherCharges;
    }

    public Date getPlanStart() {
        return planStart;
    }

    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    public String getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(String applyIds) {
        this.applyIds = applyIds;
    }

    public String getApplyCodes() {
        return applyCodes;
    }

    public void setApplyCodes(String applyCodes) {
        this.applyCodes = applyCodes;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getSealingNumber() {
        return sealingNumber;
    }

    public void setSealingNumber(String sealingNumber) {
        this.sealingNumber = sealingNumber;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

	public Integer getTaskFilter() {
		return taskFilter;
	}

	public void setTaskFilter(Integer taskFilter) {
		this.taskFilter = taskFilter;
	}

	public short getIscheck() {
		return ischeck;
	}

	public void setIscheck(short ischeck) {
		this.ischeck = ischeck;
	}


}
