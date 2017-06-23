package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>人员</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月8日
 */
@Entity
@Table(name = "tbd_member")
public class Member extends OpsEntity {

    public static final short ENABLE_LOGIN = 1;
    public static final short DISABLE_LOGIN = 0;
    public static final short DISABLE = 0;//0--无效
    public static final short ENABLE = 1;//1--有效
    private static final long serialVersionUID = 1514562530546398698L;
    private String username;//用户名
    private Short sex;//性别：1：男；2：女
    private String email;//邮箱
    private String postcode;//邮编
    private String address;//地址
    private String fax;//传真
    private String idCard;//身份证

    private Short isWebLogin;//是否允许web登录
    private Short isMobileLogin;//是否允许手机登录
    private String phoneOne;//手机号
    private String phoneTwo;//电话2
    private String userDesc;//用户描述

    private Organization dept;//部门
    private Short isInterface;//是否部门负责人

    private String userCode;//用户编码
    private String jobNumber;//工号
    private Date entryDay;//入职日期
    private String position;//职位
    private AuxiliaryAttr jobStatus;//工作状态
    private AuxiliaryAttr education;//学历
    //private Attach photo;//照片
    private Date departureDate;//离职日期
    private String departureReason;//离职原因
    private User user;//关联的系统用户

    private Date createTime;//创建时间
    private User creator;//创建人
    private Organization org;//机构
    private Date updateTime;//修改时间戳
    private Short enable;//状态 0--无效；1--有效

    /**
     * 权限过滤的部门
     */
    private Organization dep;
    /**
     * 拼音首字母
     */
    private String pinyin;
    /**
     * 五笔首字母
     */
    private String fivepen;

    @Column(name = "USER_NAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "SEX")
    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "POST_CODE")
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "FADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "FAX")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "ID_CARD")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Column(name = "IS_WEB_LOGIN")
    public Short getIsWebLogin() {
        return isWebLogin;
    }

    public void setIsWebLogin(Short isWebLogin) {
        this.isWebLogin = isWebLogin;
    }

    @Column(name = "IS_MOBILE_LOGIN")
    public Short getIsMobileLogin() {
        return isMobileLogin;
    }

    public void setIsMobileLogin(Short isMobileLogin) {
        this.isMobileLogin = isMobileLogin;
    }

    @Column(name = "PHONE_ONE")
    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    @Column(name = "PHONE_TWO")
    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    @Column(name = "USER_DESC")
    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    @JoinColumn(name = "FDEPT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @Column(name = "FISINTERFACE")
    public Short getIsInterface() {
        return isInterface;
    }

    public void setIsInterface(Short isInterface) {
        this.isInterface = isInterface;
    }

    @Column(name = "USER_CODE")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "FJOB_NUMBER")
    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Column(name = "FENTRY_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEntryDay() {
        return entryDay;
    }

    public void setEntryDay(Date entryDay) {
        this.entryDay = entryDay;
    }

    @Column(name = "FPOSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @JoinColumn(name = "FJOB_STATUS")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(AuxiliaryAttr jobStatus) {
        this.jobStatus = jobStatus;
    }

	/*@JoinColumn(name = "PPHOTO")
    @OneToOne(cascade = {}, fetch = FetchType.LAZY)
	public Attach getPhoto() {
		return photo;
	}
	public void setPhoto(Attach photo) {
		this.photo = photo;
	}*/

    @JoinColumn(name = "FEDUCATION")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getEducation() {
        return education;
    }

    public void setEducation(AuxiliaryAttr education) {
        this.education = education;
    }

    @Column(name = "FDEPARTURE_DATE")
    @Temporal(TemporalType.DATE)
    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Column(name = "FDEPARTURE_REASONS")
    public String getDepartureReason() {
        return departureReason;
    }

    public void setDepartureReason(String departureReason) {
        this.departureReason = departureReason;
    }

    @JoinColumn(name = "FUSER_ID")
    @OneToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     */
    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "ENABLE")
    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEP_ID")
    public Organization getDep() {
        return dep;
    }

    public void setDep(Organization dep) {
        this.dep = dep;
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

