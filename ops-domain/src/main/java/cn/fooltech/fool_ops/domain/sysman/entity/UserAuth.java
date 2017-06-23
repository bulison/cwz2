package cn.fooltech.fool_ops.domain.sysman.entity;


import cn.fooltech.fool_ops.domain.base.entity.BasePO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * 用户权限管理实体PO
 */
@Entity
@Table(name = "SMG_TUSER_AUTH")
public class UserAuth extends BasePO implements java.io.Serializable {

    private static final long serialVersionUID = 8507990719749829425L;
    private String fid;
    private User user;
    private Resource resource;

    public UserAuth() {
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
     * 对应用户
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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


}
