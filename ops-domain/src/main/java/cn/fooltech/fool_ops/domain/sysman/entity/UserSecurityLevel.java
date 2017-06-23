package cn.fooltech.fool_ops.domain.sysman.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;

import javax.persistence.*;


/**
 * <p>用户保密级别</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年6月30日
 */
@Entity
@Table(name = "smg_user_security_level")
public class UserSecurityLevel extends OpsEntity {

    private static final long serialVersionUID = 1496170432449442194L;

    /**
     * 用户
     */
    private User user;

    /**
     * 保密级别
     */
    private SecurityLevel securityLevel;

    public UserSecurityLevel() {

    }

    public UserSecurityLevel(User user, SecurityLevel securityLevel) {
        super();
        this.user = user;
        this.securityLevel = securityLevel;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUSER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSECURITY_LEVEL_ID")
    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }

}
