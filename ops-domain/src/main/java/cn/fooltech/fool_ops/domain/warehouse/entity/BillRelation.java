package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * <p>单据关联表</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月21日
 */

@Entity
@Table(name = "tsb_bill_relation")
public class BillRelation extends OpsEntity {

    public static final short BILL = 0;
    public static final short DETAIL = 1;

    public static final int UN_AUTH = 0;
    public static final int AUTH = 1;
    public static final int CALCLE = 2;

    private static final long serialVersionUID = 5610037055037773840L;
    private Integer billType;//单据类型
    private Short isDetail;//单据是否主表ID  0—单据表ID;1—单据明细表ID
    private String billId;//单据ID

    private Integer refBillType;//关联单据类型
    private Short refIsDetail;//单据是否主表ID  0—单据表ID;1—单据明细表ID
    private String refBillId;//关联单据ID

    private Integer recordStatus = UN_AUTH;//状态:0—未审核;1—审核;2—作废
    private String orgId;//机构ID

    @Column(name = "FBILL_TYPE")
    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    @Column(name = "FIS_DETAIL")
    public Short getIsDetail() {
        return isDetail;
    }

    public void setIsDetail(Short isDetail) {
        this.isDetail = isDetail;
    }

    @Column(name = "FBILL_ID")
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Column(name = "FREF_BILL_TYPE")
    public Integer getRefBillType() {
        return refBillType;
    }

    public void setRefBillType(Integer refBillType) {
        this.refBillType = refBillType;
    }

    @Column(name = "FREF_IS_DETAIL")
    public Short getRefIsDetail() {
        return refIsDetail;
    }

    public void setRefIsDetail(Short refIsDetail) {
        this.refIsDetail = refIsDetail;
    }

    @Column(name = "FREF_BILL_ID")
    public String getRefBillId() {
        return refBillId;
    }

    public void setRefBillId(String refBillId) {
        this.refBillId = refBillId;
    }

    @Column(name = "RECORD_STATUS")
    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    @Column(name = "FORG_ID")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
