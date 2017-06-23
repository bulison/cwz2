/**
 *
 */
package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgBaseDataEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 客户
 *
 * @author ljb
 * @version 1.0
 * @date 2014年6月11日
 * @update rqh 2015-09-14
 */
@Entity
@Table(name = "TBD_CUSTOMER")
public class Customer extends OpsOrgBaseDataEntity {

    private static final long serialVersionUID = -909006646293234174L;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 地区
     */
    private AuxiliaryAttr area;

    /**
     * 类别
     */
    private AuxiliaryAttr category;

    /**
     * 用户配置信用额度
     */
    private BigDecimal creditLineUser;

    /**
     * 系统计算信用额度
     */
    private BigDecimal creditLineSys;

    /**
     * 业务联系人
     */
    private String businessContact;

    /**
     * 业务联系人传真
     */
    private String fax;

    /**
     * 业务联系人移动电话
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String tel;

    /**
     * 业务员
     */
    private Member member;

    /**
     * 法人代表
     */
    private String principal;

    /**
     * 法人联系电话
     */
    private String principalPhone;

    /**
     * 征信级别
     */
    private AuxiliaryAttr creditRating;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 注册资金
     */
    private String registedCapital;

    /**
     * 在业人数
     */
    private Integer staffNum;

    /**
     * 经营范围
     */
    private String bussinessRange;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 帐号
     */
    private String account;

    /**
     * 国税号
     */
    private String nationTax;

    /**
     * 地税号
     */
    private String landTax;

    /**
     * 成立日期
     */
    private Date registerDate;

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
     * 记录状态
     */
    private String recordStatus = STATUS_SAC;

    /**
     * 部门
     */
    private Organization dept;
    /**
     * 拼音首字母
     */
    private String pinyin;
    /**
     * 五笔首字母
     */
    private String fivepen;

    /**
     * 获取简称
     *
     * @return
     */
    @Column(name = "FSHORT_NAME", nullable = false, length = 100)
    public String getShortName() {
        return shortName;
    }

    /**
     * 设置简称
     *
     * @param shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
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
     * 获取用户配置信用额度
     *
     * @return
     */
    @Column(name = "FCREDIT_LINE_USER")
    public BigDecimal getCreditLineUser() {
        return creditLineUser;
    }

    /**
     * 设置用户配置信用额度
     *
     * @param creditLineUser
     */
    public void setCreditLineUser(BigDecimal creditLineUser) {
        this.creditLineUser = creditLineUser;
    }

    /**
     * 获取系统计算信用额度
     *
     * @return
     */
    @Column(name = "FCREDIT_LINE_SYS")
    public BigDecimal getCreditLineSys() {
        return creditLineSys;
    }

    /**
     * 设置系统计算信用额度
     *
     * @param creditLineSys
     */
    public void setCreditLineSys(BigDecimal creditLineSys) {
        this.creditLineSys = creditLineSys;
    }

    /**
     * 获取业务联系人
     *
     * @return
     */
    @Column(name = "FBUSINESS_CONTACT", length = 50)
    public String getBusinessContact() {
        return businessContact;
    }

    /**
     * 设置业务联系人
     *
     * @param businessContact
     */
    public void setBusinessContact(String businessContact) {
        this.businessContact = businessContact;
    }

    /**
     * 获取业务联系人传真
     *
     * @return
     */
    @Column(name = "FFAX", length = 50)
    public String getFax() {
        return fax;
    }

    /**
     * 设置业务联系人传真
     *
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
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

    /**
     * 获取邮箱地址
     *
     * @return
     */
    @Column(name = "FEMAIL", length = 128)
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱地址
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取地址
     *
     * @return
     */
    @Column(name = "FADDRESS", length = 200)
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取电话
     *
     * @return
     */
    @Column(name = "FTEL", length = 50)
    public String getTel() {
        return tel;
    }

    /**
     * 设置电话
     *
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取业务员
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMEMBER_ID")
    public Member getMember() {
        return member;
    }

    /**
     * 设置业务员
     *
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取法人代表
     *
     * @return
     */
    @Column(name = "PRINCIPAL", length = 50)
    public String getPrincipal() {
        return principal;
    }

    /**
     * 设置法人代表
     *
     * @param principal
     */
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    /**
     * 获取法人联系电话
     *
     * @return
     */
    @Column(name = "PRINCIPAL_PHONE", length = 20)
    public String getPrincipalPhone() {
        return principalPhone;
    }

    /**
     * 设置法人联系电话
     *
     * @param principalPhone
     */
    public void setPrincipalPhone(String principalPhone) {
        this.principalPhone = principalPhone;
    }

    /**
     * 获取征信级别
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREDIT_RATING")
    public AuxiliaryAttr getCreditRating() {
        return creditRating;
    }

    /**
     * 设置征信级别
     *
     * @param creditRating
     */
    public void setCreditRating(AuxiliaryAttr creditRating) {
        this.creditRating = creditRating;
    }

    /**
     * 获取邮编
     *
     * @return
     */
    @Column(name = "FPOST_CODE", length = 16)
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮编
     *
     * @param postCode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 获取注册资金
     *
     * @return
     */
    @Column(name = "FREGISTED_CAPITAL", length = 50)
    public String getRegistedCapital() {
        return registedCapital;
    }

    /**
     * 设置注册资金
     *
     * @param registedCapital
     */
    public void setRegistedCapital(String registedCapital) {
        this.registedCapital = registedCapital;
    }

    /**
     * 获取在业人数
     *
     * @return
     */
    @Column(name = "FSTAFF_NUM")
    public Integer getStaffNum() {
        return staffNum;
    }

    /**
     * 设置在业人数
     *
     * @param staffNum
     */
    public void setStaffNum(Integer staffNum) {
        this.staffNum = staffNum;
    }

    /**
     * 获取经营范围
     *
     * @return
     */
    @Column(name = "FBUSSINESS_RANGE", length = 50)
    public String getBussinessRange() {
        return bussinessRange;
    }

    /**
     * 设置经营范围
     *
     * @param bussinessRange
     */
    public void setBussinessRange(String bussinessRange) {
        this.bussinessRange = bussinessRange;
    }

    /**
     * 获取开户银行
     *
     * @return
     */
    @Column(name = "BANK", length = 50)
    public String getBank() {
        return bank;
    }

    /**
     * 设置开户银行
     *
     * @param bank
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 设置帐号
     *
     * @return
     */
    @Column(name = "ACCOUNT", length = 50)
    public String getAccount() {
        return account;
    }

    /**
     * 获取帐号
     *
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取国税号
     *
     * @return
     */
    @Column(name = "NATION_TAX", length = 50)
    public String getNationTax() {
        return nationTax;
    }

    /**
     * 设置国税号
     *
     * @param nationTax
     */
    public void setNationTax(String nationTax) {
        this.nationTax = nationTax;
    }

    /**
     * 获取地税号
     *
     * @return
     */
    @Column(name = "LAND_TAX", length = 50)
    public String getLandTax() {
        return landTax;
    }

    /**
     * 设置地税号
     *
     * @param landTax
     */
    public void setLandTax(String landTax) {
        this.landTax = landTax;
    }

    /**
     * 获取成立日期
     *
     * @return
     */
    @Column(name = "FREGISTER_DATE")
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * 设置成立日期
     *
     * @param registerDate
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
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
     * 获取记录状态
     *
     * @return
     */
    @Column(name = "RECORD_STATUS", length = 3)
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置记录状态
     *
     * @param recordStatus
     */
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
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

    @Column(name = "FPINYIN")
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Column(name = "FFIVEPEN")
    public String getFivepen() {
        return fivepen;
    }

    public void setFivepen(String fivepen) {
        this.fivepen = fivepen;
    }
}
