package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * 组织管理实体PO
 */
@Entity
@Table(name = "SMG_TORGANIZATION")
public class Organization extends BasePO implements java.io.Serializable {

    private static final long serialVersionUID = -4029333861571086529L;
    private String fid;
    private Organization parent;//父机构
    private String orgName;//机构名称
    private String orgAlias;//机构别名
    private String orgCode;//机构代码（国标）
    private String postCode;//邮编
    private String faddress;//地址
    private String fax;//传真
    private String email;//电子邮箱
    private String phoneOne;//联系电话
    private String orgDesc;//组织描述
    private String homePage;//主页
    private Short validation;//无效：0 有效：1
    private User principal;//负责人
    private Integer rankOrder;//排序字段
    private User createUser;//创建人
    //ORG_ID 组织机构ID(SMG_TORGANIZATION.FID),对应原部门表--新增
    private String orgId;
    private String customCode;//对应原部门表中的FIELD1
    private String districtCode;//对应原部门表中的FIELD2
    //	设置层次级别(顶节点从1开始)
    private Short flevel;

    /**
     * 所有父亲节点
     */
    private String parentIds = "";

    //子机构
    private Set<Organization> childs = new HashSet<Organization>(0);

    private Set<User> orgUsers = new HashSet<User>(0);//组织下的用户

    private Set<Role> roles = new HashSet<Role>(0);

    public Organization() {
    }

    public Organization(String fid) {
        this.fid = fid;
    }

    public Organization(String fid, String orgCode,
                        String orgName) {
        this.fid = fid;
        this.orgCode = orgCode;
        this.orgName = orgName;
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
     * 父组织
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public Organization getParent() {
        return this.parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    /**
     * 组织名称
     *
     * @return
     */
    @Column(name = "FNAME", nullable = false, length = 200)
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 组织别名
     *
     * @return
     */
    @Column(name = "ORG_ALIAS", length = 200)
    public String getOrgAlias() {
        return this.orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    /**
     * 组织编码
     *
     * @return
     */
    @Column(name = "ORG_CODE", nullable = false, length = 200)
    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
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
    @Column(name = "FADDRESS", length = 50)
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
     * 电子邮箱
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
     * 组织描述
     *
     * @return
     */
    @Column(name = "ORG_DESC", length = 500)
    public String getOrgDesc() {
        return this.orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    /**
     * 主页
     *
     * @return
     */
    @Column(name = "HOME_PAGE", length = 200)
    public String getHomePage() {
        return this.homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
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
     * 排序字段
     *
     * @return
     */
    @Column(name = "RANK_ORDER")
    public Integer getRankOrder() {
        return this.rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

    /**
     * 创建人
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_USER")
    public User getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    /**
     * 对应原部门表中的FIELD1
     *
     * @return
     */
    @Column(name = "CUSTOM_CODE", length = 200)
    public String getCustomCode() {
        return this.customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    /**
     * 对应原部门表中的FIELD2
     *
     * @return
     */
    @Column(name = "DISTRICT_CODE", length = 200)
    public String getDistrictCode() {
        return this.districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    /*
     * 部门所在的单位FID,与GDPr的设计靠拢
     * lixiaolin
     * 2015-05-06
     */
    @Column(name = "ORG_ID", length = 200)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * 子组织集合
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<Organization> getChilds() {
        return this.childs;
    }

    public void setChilds(Set<Organization> childs) {
        this.childs = childs;
    }

    @Column(name = "FPARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    /**
     * 组织树形级别
     *
     * @return
     */
    @Column(name = "FLEVEL")
    public Short getFlevel() {
        return this.flevel;
    }

    public void setFlevel(Short flevel) {
        this.flevel = flevel;
    }
}
