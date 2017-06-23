package cn.fooltech.fool_ops.domain.sysman.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 用户 </p>
 *
 * @author lxf
 * @version 1.0
 */
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String fid;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 性别
     */
    private Short sex;

    /**
     * 用户地址
     */
    private String faddress;

    /**
     * 联系电话1
     */
    private String phoneOne;

    /**
     * 邮箱
     */
    private String email;

    private String fax;//传真

    private Short isMobileLogin; // 是否允许移动客户端登录,1允许，0不允许

    private Short fisinterface;    //是否部门负责人

    private String postCode;//邮政编码

    private String idCard;//身份证号码

    private String userDesc;//用户描绘

    private String orgId;//用户所属部门ID

    private String oldPsw;//旧密码

    private String newPsw;//新密码

    private boolean selected = false;

    private String inputType;//首选输入法 FIVEPEN:五笔  PINYIN:拼音

    private String searchKey;//搜索关键字

    private Integer searchSize;//搜索结果集大小

    private String securityLevelId; //保密级别ID

    private String securityLevelName; //保密级别名称

    private Integer securityLevel;//保密级别

    private Integer validPrice;//有效报价0--否，1--是，默认1，即有效报价

    /**
     * 本地缓存 0:不使用缓存 1：使用
     */
    private String localCache;

    /**
     * 用户所属部门
     */
    private String deptId;
    private String deptName;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 性别
     *
     * @return
     */
    public Short getSex() {
        return this.sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    /**
     * 地址
     *
     * @return
     */
    public String getFaddress() {
        return this.faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    /**
     * 电话1
     *
     * @return
     */
    public String getPhoneOne() {
        return this.phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    /**
     * EMAIL邮箱
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 传真
     *
     * @return
     */
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 是否移动登陆
     *
     * @return
     */
    public Short getIsMobileLogin() {
        return isMobileLogin;
    }

    public void setIsMobileLogin(Short isMobileLogin) {
        this.isMobileLogin = isMobileLogin;
    }

    /**
     * 是否部门负责人
     *
     * @return
     */
    public Short getFisinterface() {
        return this.fisinterface;
    }

    public void setFisinterface(Short fisinterface) {
        this.fisinterface = fisinterface;
    }

    /**
     * 邮编
     *
     * @return
     */
    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 身份证号码
     *
     * @return
     */
    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 用户描述
     *
     * @return
     */
    public String getUserDesc() {
        return this.userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOldPsw() {
        return oldPsw;
    }

    public void setOldPsw(String oldPsw) {
        this.oldPsw = oldPsw;
    }

    public String getNewPsw() {
        return newPsw;
    }

    public void setNewPsw(String newPsw) {
        this.newPsw = newPsw;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(Integer searchSize) {
        this.searchSize = searchSize;
    }

    public String getSecurityLevelId() {
        return securityLevelId;
    }

    public void setSecurityLevelId(String securityLevelId) {
        this.securityLevelId = securityLevelId;
    }

    public String getSecurityLevelName() {
        return securityLevelName;
    }

    public void setSecurityLevelName(String securityLevelName) {
        this.securityLevelName = securityLevelName;
    }

    public String getLocalCache() {
        return localCache;
    }

    public void setLocalCache(String localCache) {
        this.localCache = localCache;
    }

    public Integer getValidPrice() {
        return validPrice;
    }

    public void setValidPrice(Integer validPrice) {
        this.validPrice = validPrice;
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

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }
}
