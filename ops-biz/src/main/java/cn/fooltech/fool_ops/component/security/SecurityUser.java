package cn.fooltech.fool_ops.component.security;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
//@ToString
public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = -675129699934490295L;

    private Set<String> permCodes;
    private String id;
    private String userName;//用户名
    private Short sex;//性别
    private String accountName;//帐号
    private String password;
    private String email;//邮件
    private Short isMobileLogin; // 是否允许移动客户端登录,1允许，0不允许
    private String phoneOne;//联系电话1
    private String deptId;//用户所属部门ID
    private String deptName;
    private String userCode;//用户编号
    private String headPortRait;//HEAD_PORTRAIT头像连接
    private String orgId;//用户所属机构ID
    private String orgName;
    private boolean isAdmin;//是否是管理员, 系统保留用户,false:普通用户  true:用户最高级别管理员
    private String fiscalAccountId;//账套
    private String fiscalAccountName;

    public SecurityUser(String id, String userName, Short sex, String accountName, String password, String email, Short isMobileLogin, String phoneOne, String deptId, String deptName, String userCode, String headPortRait, String orgId, String orgName, boolean isAdmin, String fiscalAccountId, String fiscalAccountName,Set<String> permCodes) {
        this.id = id;
        this.userName = userName;
        this.sex = sex;
        this.accountName = accountName;
        this.password = password;
        this.email = email;
        this.isMobileLogin = isMobileLogin;
        this.phoneOne = phoneOne;
        this.deptId = deptId;
        this.deptName = deptName;
        this.userCode = userCode;
        this.headPortRait = headPortRait;
        this.orgId = orgId;
        this.orgName = orgName;
        this.isAdmin = isAdmin;
        this.fiscalAccountId = fiscalAccountId;
        this.fiscalAccountName = fiscalAccountName;
        this.permCodes = permCodes;
    }

    public SecurityUser(String userName, Set<String> permCodes) {
        this.userName = userName;
        this.permCodes = permCodes;
    }
    public SecurityUser(User user, Set<String> permCodes) {
        initUserAttr(user);
        this.permCodes = permCodes;
    }

    public SecurityUser() {
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authoritys = Lists.newArrayList();
        for (String perm : permCodes) {
            SimpleGrantedAuthority simpleAuthority = new SimpleGrantedAuthority(perm);
            authoritys.add(simpleAuthority);
        }

        return authoritys;
    }



    private void initUserAttr(User user) {
        this.id = user.getFid();
        this.accountName = user.getAccountName();
        this.email = user.getEmail();
        this.headPortRait = user.getHeadPortRait();
        this.isAdmin = user.getIsAdmin() != null && user.getIsAdmin() == 1;
        this.isMobileLogin = user.getIsMobileLogin();
        this.password = user.getPassWord();
        this.phoneOne = user.getPhoneOne();
        this.sex = user.getSex();
        this.userCode = user.getUserCode();
        this.userName = user.getUserName();

        Organization dept = user.getOrgId();
        if (dept != null) {
            this.deptId = dept.getFid();
            this.deptName = dept.getOrgName();
        }
        Organization org = user.getTopOrg();
        if (org != null) {
            this.orgId = org.getFid();
            this.orgName = org.getOrgName();
        }
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }




    @Override
    public String toString() {
        return "SecurityUser{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", accountName='" + accountName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isMobileLogin=" + isMobileLogin +
                ", phoneOne='" + phoneOne + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", userCode='" + userCode + '\'' +
                ", headPortRait='" + headPortRait + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", isAdmin=" + isAdmin +
                ", fiscalAccountId='" + fiscalAccountId + '\'' +
                ", fiscalAccountName='" + fiscalAccountName + '\'' +
                '}';
    }
}
