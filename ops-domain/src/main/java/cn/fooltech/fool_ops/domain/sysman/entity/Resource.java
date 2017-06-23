package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 * 资源管理实体PO
 */
@Entity
@Table(name = "SMG_TRESOURCE")
public class Resource extends BasePO implements Serializable {

    /**
     * 查看我(创建人)自已的；
     */
    public static final int FILTER_MY = 1;
    /**
     * 查看本部门的（不包含下属部门）；
     */
    public static final int FILTER_DEPT = 2;
    /**
     * 查看本部门的（包含下属部门）；
     */
    public static final int FILTER_SUB_DEPTS = 3;
    /**
     * 查看其它部门的(默认本部门及本部门的下属部门的也能查看);
     */
    public static final int FILTER_OTHER_DEPTS = 4;
    /**
     * 查看本单位的；
     */
    public static final int FILTER_ORG = 5;
    public static final short RES_TYPE_MENU = 1;
    public static final short RES_TYPE_URL = 2;
    public static final short RES_TYPE_OPERATION = 3;
    public static final Integer SHOW_FALSE=0;
    public static final Integer SHOW_TRUE=1;
    private static final long serialVersionUID = 8010188756078775852L;
    private String fid;
    private Resource parent;//父资源
    private String resName;//资源名称
    private String code;    //资源编码
    private String resString;//资源URL
    private Integer resApp;//资源应用环境 1=web,2=android,3=iphone
    private Short resType;//0:菜单目录;  1：菜单项;  2：URL;  3：操作
    private String smallIcoPath;//资源小图标路径
    private String bigIcoPath;//资源大图标路径
    private Integer rankOrder;//排序字段
    private String resDesc;
    private Short validation;//是否有效
    private Set<Resource> children = new TreeSet<Resource>();     //子资源。级联删除
    private Set<Role> roles = new HashSet<Role>(0);                    //资源对应的角色
    private Set<User> users = new HashSet<User>(0);                    //资源对应的用户
    private Short permType;//资源类型 0：业务资源 1：系统资源
    private Organization relegationOrg; //资源归属ORG, 对于组织私有的资源，需要设置这个属性，设置这个属性以后，只有本组织于下级组织可以看到该资源--无用
    private Integer leaf = LEFA_CHILD;//是否叶子节点
    private Integer show = Constants.SHOW;//是否显示：0不显示，1显示
    /**
     * 数据过滤方式：
     * 1、查看我(创建人)自已的；
     * 2、查看本部门的（不包含下属部门）；
     * 3、查看本部门的（包含下属部门）；
     * 4、 查看其它部门的(默认本部门及本部门的下属部门的也能查看);
     * 5、查看本单位的；
     */
    private Integer dataFilter = FILTER_ORG;

    public Resource() {
    }

    public Resource(String fid, String code, String resName, short resType) {
        this.fid = fid;
        this.code = code;
        this.resName = resName;
        this.resType = resType;
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
     * 资源名称
     *
     * @return
     */
    @Column(name = "FNAME", nullable = false, length = 50)
    public String getResName() {
        return this.resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * 资源编码
     *
     * @return
     */
    @Column(name = "CODE", nullable = false, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 资源链接
     *
     * @return
     */
    @Column(name = "RES_STRING", length = 500)
    public String getResString() {
        return this.resString;
    }

    public void setResString(String resString) {
        this.resString = resString;
    }

    /*
     * 资源应用环境
     */
    @Column(name = "RESAPP")
    public Integer getResApp() {
        return resApp;
    }

    public void setResApp(Integer resApp) {
        this.resApp = resApp;
    }

    /**
     * 资源类型,1.菜单 2.URL 3.操作
     *
     * @return
     */
    @Column(name = "RES_TYPE", nullable = false)
    public Short getResType() {
        return this.resType;
    }

    public void setResType(Short resType) {
        this.resType = resType;
    }

    /**
     * 资源小图标路径
     *
     * @return
     */
    @Column(name = "SMALL_ICO_PATH", length = 200)
    public String getSmallIcoPath() {
        return this.smallIcoPath;
    }

    public void setSmallIcoPath(String smallIcoPath) {
        this.smallIcoPath = smallIcoPath;
    }

    /**
     * 资源大图标路径
     *
     * @return
     */
    @Column(name = "BIG_ICO_PATH", length = 200)
    public String getBigIcoPath() {
        return this.bigIcoPath;
    }

    public void setBigIcoPath(String bigIcoPath) {
        this.bigIcoPath = bigIcoPath;
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
     * 资源描述
     *
     * @return
     */
    @Column(name = "RES_DESC", length = 500)
    public String getResDesc() {
        return this.resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    /**
     * 父资源
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public Resource getParent() {
        return this.parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    /**
     * 子资源
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public Set<Resource> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Resource> children) {
        this.children = children;
    }

    /*
     * 资源对应的角色
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TROLE_AUTH", joinColumns = {@JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", nullable = false, updatable = false)})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /*
     * 资源对应的用户*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TUSER_AUTH", joinColumns = {@JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)})
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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

    @Column(name = "LEAF")
    public Integer isLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }


    /**
     * 权限类型
     *
     * @return
     */
    @Column(name = "PERM_TYPE")
    public Short getPermType() {
        return this.permType;
    }

    public void setPermType(Short permType) {
        this.permType = permType;
    }

    /**
     * 资源关联组织
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public Organization getRelegationOrg() {
        return relegationOrg;
    }

    public void setRelegationOrg(Organization relegationOrg) {
        this.relegationOrg = relegationOrg;
    }

    @Column(name = "DATAFILTER")
    public Integer getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(Integer dataFilter) {
        this.dataFilter = dataFilter;
    }

    @Column(name = "FSHOW")
    public Integer getShow() {return show;}

    public void setShow(Integer show) {this.show = show;}
}
