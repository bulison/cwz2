package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * 角色权限管理实体PO
 */
@Entity
@Table(name = "SMG_TROLE_AUTH")
public class RoleAuth extends BasePO implements java.io.Serializable {
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
    private static final long serialVersionUID = -3060226692021516461L;
    private String fid;
    private String restrictId;  //资源约束ID
    private Role role;
    private Resource resource;
    /**
     * 数据过滤方式：
     * 1、查看我(创建人)自已的；
     * 2、查看本部门的（不包含下属部门）；
     * 3、查看本部门的（包含下属部门）；
     * 4、 查看其它部门的(默认本部门及本部门的下属部门的也能查看);
     * 5、查看本单位的；
     */
    private Integer dataFilter = FILTER_ORG;

    public RoleAuth() {
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
     * 对应角色
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * 对应资源
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCE_ID", nullable = false)
    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * 资源约束ID
     *
     * @return the restrictId
     */
    @Column(name = "RESTRICT_ID", length = 32)
    public String getRestrictId() {
        return restrictId;
    }

    /**
     * @param restrictId the restrictId to set
     */
    public void setRestrictId(String restrictId) {
        this.restrictId = restrictId;
    }

    @Column(name = "DATAFILTER")
    public Integer getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(Integer dataFilter) {
        this.dataFilter = dataFilter;
    }
}
