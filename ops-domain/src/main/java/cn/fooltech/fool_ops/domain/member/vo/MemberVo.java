package cn.fooltech.fool_ops.domain.member.vo;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.validator.IDCard;
import cn.fooltech.fool_ops.validator.Phone;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * <p>表单传输对象 - 人员</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-09-08 13:55:54
 */
public class MemberVo implements Serializable {

    private static final long serialVersionUID = -1534325833539464556L;
    private String fid;//主键

    @NotBlank(message = "用户名不能为空")
    @Length(max = 100, message = "用户名长度超过100个字符")
    private String username;//用户名

    private Short sex;//性别：1：男；2：女

    @Email(message = "邮箱不符合格式")
    @Length(max = 50, message = "邮箱长度超过50个字符")
    private String email;//邮箱

    @Length(max = 50, message = "邮编长度超过50个字符")
    private String postcode;//邮编

    @Length(max = 100, message = "地址长度超过100个字符")
    private String address;//地址

    @Length(max = 100, message = "传真长度超过100个字符")
    private String fax;//传真

    //演示自定义的身份证号验证
    @IDCard
    private String idCard;//身份证
    @NotNull(message = "是否允许web登录不能为空")
    private Short isWebLogin;//是否允许web登录
    @NotNull(message = "是否允许手机登录不能为空")
    private Short isMobileLogin;//是否允许手机登录

    //演示自定义的手机号验证
    @Phone
    @NotBlank(message = "手机号不能为空")
    private String phoneOne;//手机号

    private String phoneTwo;//电话2

    @Length(max = 500, message = "用户描长度超过500个字符")
    private String userDesc;//用户描述

    private Short isInterface;//是否部门负责人
    @NotBlank(message = "用户编码不能为空")
    @Length(max = 50, message = "用户编码长度超过50个字符")
    private String userCode;//用户编码

    @Length(max = 50, message = "工号长度超过50个字符")
    private String jobNumber;//工号
    private String entryDay;//入职日期

    @Length(max = 50, message = "职位长度超过50个字符")
    private String position;//职位

    private String departureDate;//离职日期

    @Length(max = 200, message = "职位长度超过200个字符")
    private String departureReason;//离职原因
    private Date createTime;//创建时间

    private String updateTime;//修改时间戳
    private Short enable;//状态 0--无效；1--有效
    private String deptId;//部门ID
    private String deptName;//部门名称
    @Length(max = 50, message = "部门编号长度超过50个字符")
    private String deptCode;//部门编号

    private String orgId;//机构ID
    private String orgName;//机构名称

    private String creatorId;//创建人ID
    private String creatorName;//创建人名称

    private String jobStatusId;//工作状态ID
    private String jobStatusName;//工作状态名称
    private String jobStatusCode;

    private String educationId;//学历ID
    private String educationName;//学历名称
    private String educationCode;

    private String loginName;//登录账号,用于搜索
    private String searchKey;//搜索关键字
    private String password;//用户密码

    /**
     * 模糊搜索结果集大小
     */
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Short getSex() {
        return this.sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Short getIsWebLogin() {
        return this.isWebLogin;
    }

    public void setIsWebLogin(Short isWebLogin) {
        this.isWebLogin = isWebLogin;
    }

    public Short getIsMobileLogin() {
        return this.isMobileLogin;
    }

    public void setIsMobileLogin(Short isMobileLogin) {
        this.isMobileLogin = isMobileLogin;
    }

    public String getPhoneOne() {
        return this.phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return this.phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public String getUserDesc() {
        return this.userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Short getIsInterface() {
        return this.isInterface;
    }

    public void setIsInterface(Short isInterface) {
        this.isInterface = isInterface;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getEntryDay() {
        return this.entryDay;
    }

    public void setEntryDay(String entryDay) {
        this.entryDay = entryDay;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartureDate() {
        return this.departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureReason() {
        return this.departureReason;
    }

    public void setDepartureReason(String departureReason) {
        this.departureReason = departureReason;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(String jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public String getJobStatusName() {
        return jobStatusName;
    }

    public void setJobStatusName(String jobStatusName) {
        this.jobStatusName = jobStatusName;
    }

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getJobStatusCode() {
        return jobStatusCode;
    }

    public void setJobStatusCode(String jobStatusCode) {
        this.jobStatusCode = jobStatusCode;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public Integer getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(Integer searchSize) {
        this.searchSize = searchSize;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
