package cn.fooltech.fool_ops.component.security;


import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtil.getCurrentUserId();
        return (userName != null ? userName : "system");
    }
}
