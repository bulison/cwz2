package cn.fooltech.fool_ops.domain.register.vo;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.MD5Util;

import java.io.Serializable;


public class UserAndOrgInfoVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

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

    private String orgName;//机构名称
    private String orgAlias;//机构别名
    private String orgCode;//机构代码（国标）

    private String orgDesc;//组织描述
    private String homePage;//主页

    private String secCode;
    private User principal;//负责人

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = MD5Util.encrypt(passWord);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getFaddress() {
        return faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Short getIsMobileLogin() {
        return isMobileLogin;
    }

    public void setIsMobileLogin(Short isMobileLogin) {
        this.isMobileLogin = isMobileLogin;
    }

    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Short getFisinterface() {
        return fisinterface;
    }

    public void setFisinterface(Short fisinterface) {
        this.fisinterface = fisinterface;
    }

    public Short getValidation() {
        return validation;
    }

    public void setValidation(Short validation) {
        this.validation = validation;
    }

    public Organization getOrgId() {
        return orgId;
    }

    public void setOrgId(Organization orgId) {
        this.orgId = orgId;
    }

    public User getUserFid() {
        return userFid;
    }

    public void setUserFid(User userFid) {
        this.userFid = userFid;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getHeadPortRait() {
        return headPortRait;
    }

    public void setHeadPortRait(String headPortRait) {
        this.headPortRait = headPortRait;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public User getPrincipal() {
        return principal;
    }

    public void setPrincipal(User principal) {
        this.principal = principal;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
    }


}
