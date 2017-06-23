package cn.fooltech.fool_ops.domain.basedata.vo;

import cn.fooltech.fool_ops.config.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;


/**
 * 表单传输对象-供应商
 *
 * @author lzf
 * @version 1.0
 * @date 2015年4月15日
 * @update rqh 2015-09-11
 */
@ApiModel("供应商")
public class SupplierVo implements Serializable {


    public static final int SHOW = 1;//显示
    public static final int NOT_SHOW = 0;//不显示
    private static final long serialVersionUID = -2452880076958483094L;
    /**
     * ID
     */
    private String fid;
    /**
     * 组织机构ID
     */
    @Length(max = 32, message = "机构长度超过{max}个字符")
    @ApiModelProperty(value = "组织机构ID")
    private String orgId;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @NotBlank(message = "编号不能为空")
    @Length(max = 50, message = "编号长度超过50个字符")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 50, message = "名称长度超过50个字符")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 500, message = "描述长度超过500个字符")
    private String describe;
    /**
     * 简称
     */
    @ApiModelProperty(value = "简称")
    @Length(max = 50, message = "简称长度超过50个字符")
    private String shortName;
    /**
     * 地区ID
     */
    @ApiModelProperty(value = "地区ID")
    @Length(max = 32, message = "地区长度超过32个字符")
    private String areaId;
    /**
     * 地区名称
     */
    @ApiModelProperty(value = "地区名称")
    private String areaName;
    /**
     * 地区编号
     */
    @ApiModelProperty(value = "地区编号")
    private String areaCode;
    /**
     * 类别ID
     */
    @ApiModelProperty(value = "类别ID")
    @Length(max = 32, message = "类别长度超过32个字符")
    private String categoryId;
    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String categoryName;
    /**
     * 类别编号
     */
    @ApiModelProperty(value = "类别编号")
    private String categoryCode;
    /**
     * 用户配置信用额度
     */
    @ApiModelProperty(value = "用户配置信用额度")
    @Length(max = 10, message = "信用额度长度超过10位")
    private String creditLineUser;
    /**
     * 系统计算信用额度
     */
    @ApiModelProperty(value = "系统计算信用额度")
    private String creditLineSys;
    /**
     * 业务联系人
     */
    @ApiModelProperty(value = "业务联系人")
    @Length(max = 10, message = "业务联系人长度超过10字符")
    private String businessContact;
    /**
     * 业务联系人传真
     */
    @ApiModelProperty(value = "业务联系人传真")
//	@Pattern(regexp = "^(\\d{3,4}-)?\\d{7,8}$", message = "传真格式为:XXX-12345678或XXXX-1234567或XXXX-12345678")
    private String fax;
    /**
     * 业务联系人移动电话
     */
//	@Phone
    @ApiModelProperty(value = "业务联系人移动电话")
    private String phone;
    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱地址")
//	@Email(regexp="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",message="邮箱格式有误")
    private String email;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    @Length(max = 200, message = "地址长度超过200字符")
    private String address;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
//	@Pattern(regexp = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}", message = "电话格式有误")
    private String tel;
    /**
     * 业务员ID
     */
    @ApiModelProperty(value = "业务员ID")
    @Length(max = 32, message = "业务员长度超过32字符")
    private String memberId;
    /**
     * 业务人员名称
     */
    @ApiModelProperty(value = "业务人员名称")
    private String memberName;
    /**
     * 业务员编号
     */
    @ApiModelProperty(value = "业务员编号")
    private String memberCode;
    /**
     * 法人代表
     */
    @ApiModelProperty(value = "法人代表")
    @Length(max = 32, message = "法人代表长度超过32字符")
    private String principal;
    /**
     * 法人联系电话
     */
    @ApiModelProperty(value = "法人联系电话")
//	@Pattern(regexp = "((\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}|[1][3,4,5,8][0-9]{9}$)", message = "法人联系电话格式有误")
    @Length(max = 32, message = "法人联系电话长度超过32字符")
    private String principalPhone;
    /**
     * 征信级别ID
     */
    @ApiModelProperty(value = "征信级别ID")
    @Length(max = 32, message = "征信级别长度超过32字符")
    private String creditRatingId;
    /**
     * 征信级别名称
     */
    @ApiModelProperty(value = "征信级别名称")
    private String creditRatingName;
    /**
     * 征信级别编号
     */
    @ApiModelProperty(value = "征信级别编号")
    private String creditRatingCode;
    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编")
//	@Pattern(regexp = "[1-9]{1}(\\d+){5}", message = "邮编格式有误")
    private String postCode;
    /**
     * 注册资金
     */
    @ApiModelProperty(value = "注册资金")
    @Length(max = 50, message = "注册资金长度超过15位")
    private String registedCapital;
    /**
     * 在业人数
     */
    @ApiModelProperty(value = "在业人数")
    @Min(value = 0, message = "在业人数不能低于{value}")
    @Max(value = 2000000000, message = "在业人数不能超过{value}")
    private Integer staffNum = 0;
    /**
     * 经营范围
     */
    @ApiModelProperty(value = "经营范围")
    @Length(max = 50, message = "经营范围长度超过50位")
    private String bussinessRange;
    /**
     * 开户银行
     */
    @ApiModelProperty(value = "开户银行")
    @Length(max = 50, message = "开户银行长度超过50位")
    private String bank;
    /**
     * 帐号
     */
    @ApiModelProperty(value = "帐号")
//	@Pattern(regexp = "^(\\d{16}|\\d{19})$", message = "帐号格式有误")
    private String account;
    /**
     * 国税号
     */
    @ApiModelProperty(value = "国税号")
    @Length(max = 50, message = "国税号长度超过50位")
    private String nationTax;
    /**
     * 地税号
     */
    @ApiModelProperty(value = "地税号")
    @Length(max = 50, message = "地税号长度超过50位")
    private String landTax;
    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期")
//	@Pattern(regexp = "\\d{4}-\\d{1,2}-\\d{1,2}?$", message = "成立日期式有误")
    private String registerDate;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private String creatorId;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    /**
     * 记录状态
     */
    @ApiModelProperty(value = "记录状态")
    private String recordStatus;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private String deptId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;
    /**
     * 模糊搜索结果集大小
     */
    @ApiModelProperty(value = "模糊搜索结果集大小")
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;
    /**
     * 是否显示无效数据
     */
    @ApiModelProperty(value = "是否显示无效数据")
    private Integer showDisable = NOT_SHOW;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreditLineUser() {
        return creditLineUser;
    }

    public void setCreditLineUser(String creditLineUser) {
        this.creditLineUser = creditLineUser;
    }

    public String getCreditLineSys() {
        return creditLineSys;
    }

    public void setCreditLineSys(String creditLineSys) {
        this.creditLineSys = creditLineSys;
    }

    public String getBusinessContact() {
        return businessContact;
    }

    public void setBusinessContact(String businessContact) {
        this.businessContact = businessContact;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipalPhone() {
        return principalPhone;
    }

    public void setPrincipalPhone(String principalPhone) {
        this.principalPhone = principalPhone;
    }

    public String getCreditRatingId() {
        return creditRatingId;
    }

    public void setCreditRatingId(String creditRatingId) {
        this.creditRatingId = creditRatingId;
    }

    public String getCreditRatingName() {
        return creditRatingName;
    }

    public void setCreditRatingName(String creditRatingName) {
        this.creditRatingName = creditRatingName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getRegistedCapital() {
        return registedCapital;
    }

    public void setRegistedCapital(String registedCapital) {
        this.registedCapital = registedCapital;
    }

    public Integer getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(Integer staffNum) {
        this.staffNum = staffNum;
    }

    public String getBussinessRange() {
        return bussinessRange;
    }

    public void setBussinessRange(String bussinessRange) {
        this.bussinessRange = bussinessRange;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNationTax() {
        return nationTax;
    }

    public void setNationTax(String nationTax) {
        this.nationTax = nationTax;
    }

    public String getLandTax() {
        return landTax;
    }

    public void setLandTax(String landTax) {
        this.landTax = landTax;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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

    public String getCreditRatingCode() {
        return creditRatingCode;
    }

    public void setCreditRatingCode(String creditRatingCode) {
        this.creditRatingCode = creditRatingCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getShowDisable() {
        return showDisable;
    }

    public void setShowDisable(Integer showDisable) {
        this.showDisable = showDisable;
    }
}
