package cn.fooltech.fool_ops.utils;

import cn.fooltech.fool_ops.component.core.SpringBeanUtils;
import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.sysman.service.ResourceService;
import com.google.common.base.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;

/**
 * @author : xjh
 * @description:安全 工具类
 */
public class SecurityUtil {

    /**
     * 判断当前用户是否有权限
     *
     * @return
     */
    public static boolean isPermit(String userId, String permitCode) {

        ResourceService resourceService = (ResourceService) SpringBeanUtils.getBean(ResourceService.class);
        Set<String> authCodes = resourceService.queryAuthCode(userId);
        if (authCodes.contains(permitCode)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户是否有权限
     *
     * @return
     */
    public static Set<String> getPermitCodes(String userId) {

        ResourceService resourceService = (ResourceService) SpringBeanUtils.getBean(ResourceService.class);
        return resourceService.queryAuthCode(userId);
    }

    /**
     * 判断当前用户是否有权限
     *
     * @return
     */
    public static boolean isPermit(String permitCode) {

        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null) return false;
        Authentication auth = context.getAuthentication();
        if (!auth.isAuthenticated()) return false;
        Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
        for (GrantedAuthority gauth : auths) {
            if (gauth.getAuthority().equals(permitCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static SecurityUser getSecurityUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if(authentication != null) {
            if(authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails)authentication.getPrincipal();
                SecurityUser securityUser = (SecurityUser) springSecurityUser;
                return securityUser;
        }else if(authentication.getPrincipal() instanceof String) {
             String   userName = (String)authentication.getPrincipal();
            }
    }
    return null;
    }

    /**
     * 改变账套
     *
     * @param accountId
     * @param accountName
     */
    public static void changeFiscalAccount(String accountId, String accountName) {
        Assert.notNull(accountId);
        Assert.notNull(accountName);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if(authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                SecurityUser securityUser = (SecurityUser) springSecurityUser;

                securityUser.setFiscalAccountId(accountId);
                securityUser.setFiscalAccountName(accountName);

                final Authentication newAuth = new UsernamePasswordAuthenticationToken(securityUser,
                        authentication.getCredentials(),
                        authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
    }


    /**
     * 获取当前登录用户ID
     *
     * @return
     */
    public static String getCurrentUserId() {
        SecurityUser user = getSecurityUser();
        if (user != null) return user.getId();
        return null;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static User getCurrentUser() {
        SecurityUser user = getSecurityUser();
        if (user != null) {
            UserRepository userRepo = (UserRepository)
                    SpringBeanUtils.getBean(UserRepository.class);
            return userRepo.findOne(user.getId());
        }
        return null;
    }

    /**
     * 获取当前登录用户机构ID
     *
     * @return
     */
    public static String getCurrentOrgId() {
        SecurityUser user = getSecurityUser();
        if (user != null) return user.getOrgId();
        return null;
    }

    /**
     * 获取当前登录用户机构
     *
     * @return
     */
    public static Organization getCurrentOrg() {
        SecurityUser user = getSecurityUser();
        if (user != null) {
            OrganizationRepository orgRepo = (OrganizationRepository)
                    SpringBeanUtils.getBean(OrganizationRepository.class);
            return orgRepo.findOne(user.getOrgId());
        }
        return null;
    }

    /**
     * 获取当前登录用户部门ID
     *
     * @return
     */
    public static Organization getCurrentDept() {
        SecurityUser user = getSecurityUser();
        if (user != null) {
            OrganizationRepository orgRepo = (OrganizationRepository)
                    SpringBeanUtils.getBean(OrganizationRepository.class);
            return orgRepo.findOne(user.getDeptId());
        }
        return null;
    }

    /**
     * 获取当前登录用户部门ID
     *
     * @return
     */
    public static String getCurrentDeptId() {
        SecurityUser user = getSecurityUser();
        if (user != null) return user.getDeptId();
        return null;
    }

    /**
     * 获取当前登录账套ID
     */
    public static String getFiscalAccountId() {
        SecurityUser user = getSecurityUser();
        if (user != null) return user.getFiscalAccountId();
        return null;
    }

    /**
     * 获取当前登录账套
     */
    public static FiscalAccount getFiscalAccount() {
        SecurityUser user = getSecurityUser();
        if (user != null) {
            FiscalAccountRepository accountRepo = (FiscalAccountRepository)
                    SpringBeanUtils.getBean(FiscalAccountRepository.class);
            return accountRepo.findOne(user.getFiscalAccountId());
        }
        return null;
    }



    /**
     * 验证密码输入
     *
     * @param pwd 密码
     * @return
     */
    public static boolean checkUserPwd(String pwd) {
        if (Strings.isNullOrEmpty(pwd)) {
            return false;
        }
        SecurityUser suer = getSecurityUser();
        if (suer == null) return false;

        String md5Password = MD5Util.encrypt(pwd);

        if (md5Password.equals(suer.getPassword())) return true;
        return false;
    }

    /**
     * 验证密码输入
     *
     * @param pwd 密码
     * @return
     */
    public static boolean checkUserPwd(String userCode, String pwd) {
        if (Strings.isNullOrEmpty(pwd)) {
            return false;
        }
        UserRepository userRepo = (UserRepository) SpringBeanUtils.getBean(UserRepository.class);
        User user = userRepo.findOneByUserCode(userCode);
        if (user == null) return false;
        String md5Password = MD5Util.encrypt(pwd);

        if (md5Password.equals(user.getPassWord())) return true;
        return false;
    }

    /**
     * 根据用户编号和密码查询用户
     *
     * @param userCode 用户编号
     * @param pwd 密码
     * @return
     */
    public static User getUser(String userCode, String pwd) {
        if (Strings.isNullOrEmpty(pwd)) {
            return null;
        }
        UserRepository userRepo = (UserRepository) SpringBeanUtils.getBean(UserRepository.class);
        User user = userRepo.findOneByUserCode(userCode);
        if (user == null) return null;
        String md5Password = MD5Util.encrypt(pwd);

        if (md5Password.equals(user.getPassWord())) return user;
        return null;
    }
}
