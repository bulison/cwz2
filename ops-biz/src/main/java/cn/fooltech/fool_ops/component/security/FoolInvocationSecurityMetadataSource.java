package cn.fooltech.fool_ops.component.security;

import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.repository.ResourceRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FoolInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(FoolInvocationSecurityMetadataSource.class);
    private static Map<String, Collection<ConfigAttribute>> authMap = Maps.newHashMap();
    private PathMatcher matcher = new AntPathMatcher();
    private ResourceRepository resRepo;
    private PassLoginService passLoginService;
    private boolean passLogin = false;

    /**
     * 提前加载角色与权限的关系
     */
    @PostConstruct
    public void loadAuthMap() {
        List<Resource> resList = resRepo.findByValidation((short) 1);
        for (Resource res : resList) {
            if (!Strings.isNullOrEmpty(res.getResString())) {
                ConfigAttribute ca = new SecurityConfig(res.getCode());
                authMap.put(res.getResString(), Lists.newArrayList(ca));
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Collection<ConfigAttribute> all = Lists.newArrayList();
        for (Collection<ConfigAttribute> iter : authMap.values()) {
            all.addAll(iter);
        }

        return all;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        boolean isAuthenticated = SecurityUtil.getSecurityUser() == null ? false : true;
//        logger.info("=============isAuthenticated==================" + isAuthenticated);
        if (!isAuthenticated && passLogin) {
//            logger.info("===============================");
            //自动登录的用户
            //session 的处理...
            passLoginService.passLogin();
        }

        String url = ((FilterInvocation) object).getRequestUrl();
        Iterator<String> ite = authMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            String authUrlPattern = resURL + "*";//数据库数据没有填写正则表达式，此处临时处理
            if (matcher.matchStart(authUrlPattern, url)) {
                return authMap.get(resURL);
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return paramClass.equals(FilterInvocation.class);
    }

    public void setResRepo(ResourceRepository resRepo) {
        this.resRepo = resRepo;
    }

    public void setPassLoginService(PassLoginService passLoginService) {
        this.passLoginService = passLoginService;
    }

    public void setPassLogin(boolean passLogin) {
        this.passLogin = passLogin;
    }
}
