package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import com.google.common.collect.Lists;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


/**
 * 角色管理实体PO
 */
@Entity
@Table(name = "SMG_TROLE")
public class Role extends BasePO implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6528097481647325139L;
    private String fid;
    private String roleName;//角色名称
    private String roleCode;//角色编码
    private String roleDesc;
    private Organization orgId;//角色所属单位ID
    private Short validation;//0否1是

    private List<User> users = Lists.newArrayList();
    private List<Resource> resources = Lists.newArrayList();//用户对应的资源

    public Role() {
    }

    public Role(String fid, String roleName) {
        this.fid = fid;
        this.roleName = roleName;
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
     * 角色名称
     *
     * @return
     */
    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 角色描述
     *
     * @return
     */
    @Column(name = "ROLE_DESC", length = 500)
    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    /**
     * 角色编码
     *
     * @return
     */
    @Column(name = "CODE", length = 32)
    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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

    /*
     * 角色所属单位ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID")
    public Organization getOrgId() {
        return orgId;
    }

    public void setOrgId(Organization orgId) {
        this.orgId = orgId;
    }

    /**
     * 角色用户
     *
     * @return
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TUSER_ROLE", joinColumns = {@JoinColumn(name = "ROLE_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)})
    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * 角色对应的资源
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SMG_TROLE_AUTH", joinColumns = {@JoinColumn(name = "ROLE_ID", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false)})
    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}
