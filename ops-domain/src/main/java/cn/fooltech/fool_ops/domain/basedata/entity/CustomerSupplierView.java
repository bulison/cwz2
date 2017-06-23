package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgBaseDataEntity;

import javax.persistence.*;

/**
 * <p>客户供应商的组合视图映射</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月6日
 */
@Entity
@Table(name = "tbd_customer_supplier_view")
public class CustomerSupplierView extends OpsOrgBaseDataEntity {

    public static final int TYPE_CUSTOMER = 1;//客户
    public static final int TYPE_SUPPLIER = 2;//供应商

    private static final long serialVersionUID = 4602712775625093644L;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 类型 1：客户；2：供应商
     */
    private Integer type = TYPE_CUSTOMER;

    /**
     * 记录状态
     */
    private String recordStatus = STATUS_SAC;

    /**
     * 地区
     */
    private AuxiliaryAttr area;

    /**
     * 类别
     */
    private AuxiliaryAttr category;

    /**
     * 业务联系人移动电话
     */
    private String phone;

    @Column(name = "FSHORT_NAME", nullable = false, length = 100)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "RECORD_STATUS", length = 3)
    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取地区
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAREA")
    public AuxiliaryAttr getArea() {
        return area;
    }

    /**
     * 设置地区
     *
     * @param area
     */
    public void setArea(AuxiliaryAttr area) {
        this.area = area;
    }

    /**
     * 获取分类
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCATEGORY")
    public AuxiliaryAttr getCategory() {
        return category;
    }

    /**
     * 设置分类
     *
     * @param category
     */
    public void setCategory(AuxiliaryAttr category) {
        this.category = category;
    }

    /**
     * 获取业务联系人移动电话
     *
     * @return
     */
    @Column(name = "FPHONE", length = 50)
    public String getPhone() {
        return phone;
    }

    /**
     * 设置业务联系人移动电话
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
