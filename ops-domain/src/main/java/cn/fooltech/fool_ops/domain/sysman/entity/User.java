package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import com.google.common.collect.Lists;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * 用户管理实体PO
 */
@Entity
@Table(name = "SMG_TUSER")
@ToString
public class User extends BasePO implements java.io.Serializable {
    private static final long serialVersionUID = -3068226622021315463L;

    private String fid;
    private String userName;//用户名
    private Short sex;//性别
    private String accountName;//帐号
    private String passWord;
    private String email;//邮件
    private String postCode;//邮政编码
    private String faddress;//用户地址
    private String fax;//传真
    private String idCard;//身份证号码
    private Short isMobileLogin; // 是否允许移动客户端登录,1允许，0不允许
    private String phoneOne;//联系电话1
    private String phoneTwo;//联系电话2
    private String userDesc;//用户描绘
    private Short fisinterface;    //是否部门负责人
    private Short validation;//0:失效,1:有效
    private Organization orgId;//用户所属部门ID
    private User userFid;//用户跨部门的情况
    private String userCode;//用户编号
    private String headPortRait;//HEAD_PORTRAIT头像连接
    private Date createDate;//创建日期--无用
    private Short isAdmin;//是否是管理员, 系统保留用户,0:普通用户   1:用户最高级别管理员
    private Integer validPrice;//有效报价0--否，1--是，默认1，即有效报价


    private List<Role> roles = Lists.newArrayList();    //用户所属角色

    private List<Resource> resources = Lists.newArrayList();//用户对应的资源
    private Organization topOrg;//部门

    public User() {
    }

    public User(String fid, String userCode, String userName,
                String accountName) {
        this.fid = fid;
        this.userCode = userCode;
        this.userName = userName;
        this.accountName = accountName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FID", unique = true, nullable = false, length = 32)
    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 用户名称
     *
     * @return
     */
    @Column(name = "USER_NAME", nullable = false, length = 50)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
     * 用户跨部门的情况
     * lixiaolin
     * 2015-05-06
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUSER_FID")
    public User getUserFid() {
        return userFid;
    }

    public void setUserFid(User userFid) {
        this.userFid = userFid;
    }

    /*
     * 用户所在部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public Organization getOrgId() {
        return orgId;
    }

    public void setOrgId(Organization orgId) {
        this.orgId = orgId;
    }

    /**
     * 性别
     *
     * @return
     */
    @Column(name = "SEX")
    public Short getSex() {
        return this.sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    /**
     * 账号
     *
     * @return
     */
    @Column(name = "ACCOUNT_NAME", length = 32)
    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 用户密码
     *
     * @return
     */
    @Column(name = "PASS_WORD", length = 32)
    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * EMAIL邮箱
     *
     * @return
     */
    @Column(name = "EMAIL", length = 50)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 邮编
     *
     * @return
     */
    @Column(name = "POST_CODE", length = 50)
    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 地址
     *
     * @return
     */
    @Column(name = "FADDRESS", length = 100)
    public String getFaddress() {
        return this.faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    /**
     * 传真
     *
     * @return
     */
    @Column(name = "FAX", length = 50)
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 身份证号码
     *
     * @return
     */
    @Column(name = "ID_CARD", length = 40)
    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Column(name = "IS_MOBILE_LOGIN")
    public Short getIsMobileLogin() {
        return isMobileLogin;
    }

    public void setIsMobileLogin(Short isMobileLogin) {
        this.isMobileLogin = isMobileLogin;
    }

    /**
     * 电话1
     *
     * @return
     */
    @Column(name = "PHONE_ONE", length = 50)
    public String getPhoneOne() {
        return this.phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    /**
     * 电话2
     *
     * @return
     */
    @Column(name = "PHONE_TWO", length = 50)
    public String getPhoneTwo() {
        return this.phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    /**
     * 用户描述
     *
     * @return
     */
    @Column(name = "USER_DESC", length = 500)
    public String getUserDesc() {
        return this.userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    /**
     * 是否部门负责人
     *
     * @return
     */
    @Column(name = "FISINTERFACE")
    public Short getFisinterface() {
        return this.fisinterface;
    }

    public void setFisinterface(Short fisinterface) {
        this.fisinterface = fisinterface;
    }

    /**
     * 是否有效
     *
     * @return
     */
    @Column(name = "VALIDATION")
    public Short getValidation() {
        return this.validation;
    }

    public void setValidation(Short validation) {
        this.validation = validation;
    }

    /**
     * 用户编码
     *
     * @return
     */
    @Column(name = "USER_CODE", nullable = false, length = 40)
    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "HEAD_PORTRAIT", nullable = false, length = 250)
    public String getHeadPortRait() {
        return headPortRait;
    }

    public void setHeadPortRait(String headPortRait) {
        this.headPortRait = headPortRait;
    }

    /**
     * 用户角色
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TUSER_ROLE", joinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", nullable = false, updatable = false)})
    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /*
     * 用户对应的资源
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TUSER_AUTH", joinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false)})
    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    /**
     * 是否管理员用户,1：admin、9：GeverAdmin、0: 普通用户
     *
     * @return
     */
    @Column(name = "IS_ADMIN")
    public Short getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Short isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Transient
    public boolean isAdminUser() {
        return getIsAdmin() != null && getIsAdmin().intValue() == 1;
    }

    @Transient
    public boolean isValidUser() {
        if (getValidation() == null || getValidation().intValue() != 1) {
            return false;
        }
        return true;
    }

    /**
     * 机构
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOPORG_ID")
    public Organization getTopOrg() {
        return topOrg;
    }

    public void setTopOrg(Organization topOrg) {
        this.topOrg = topOrg;
    }

    @Column(name = "FVALID_PRICE")
    public Integer getValidPrice() {
        return validPrice;
    }

    public void setValidPrice(Integer validPrice) {
        this.validPrice = validPrice;
    }

}
