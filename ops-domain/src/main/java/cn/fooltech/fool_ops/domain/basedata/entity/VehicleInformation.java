package cn.fooltech.fool_ops.domain.basedata.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@ApiModel("")
@Entity
@Table(name = "tbd_vehicle_information")
public class VehicleInformation {
    public static final String STATUS_SNU = "SNU";//无效数据
    public static final String STATUS_SAC = "SAC";//有效数据
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //车牌号
    @ApiModelProperty(value = "车牌号")

    @Column(name = "flicense_number")
    private String licenseNumber;


    //行驶证号
    @ApiModelProperty(value = "行驶证号")

    @Column(name = "fdriving_licese")
    private String drivingLicese;


    //车主姓名
    @ApiModelProperty(value = "车主姓名")

    @Column(name = "fowner_name")
    private String ownerName;


    //车主身份证
    @ApiModelProperty(value = "车主身份证")

    @Column(name = "fowner_id_card")
    private String ownerIdCard;


    //联系电话
    @ApiModelProperty(value = "联系电话")

    @Column(name = "fcontact_phone")
    private String contactPhone;


    //描述
    @ApiModelProperty(value = "描述")

    @Column(name = "fdescribe")
    private String fdescribe;


    //创建时间
    @ApiModelProperty(value = "创建时间")

    @Column(name = "fcreate_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")

    @Column(name = "fcreator_id")
    private String creatorId;


    //修改时间戳
    @ApiModelProperty(value = "修改时间戳")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fupdate_time")
    private Date updateTime;


    //组织id
    @ApiModelProperty(value = "组织id")

    @Column(name = "forg_id")
    private String orgId;


    //账套id
    @ApiModelProperty(value = "账套id")

    @Column(name = "facc_id")
    private String accId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getDrivingLicese() {
        return drivingLicese;
    }

    public void setDrivingLicese(String drivingLicese) {
        this.drivingLicese = drivingLicese;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerIdCard() {
        return ownerIdCard;
    }

    public void setOwnerIdCard(String ownerIdCard) {
        this.ownerIdCard = ownerIdCard;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getFdescribe() {
        return fdescribe;
    }

    public void setFdescribe(String fdescribe) {
        this.fdescribe = fdescribe;
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

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}