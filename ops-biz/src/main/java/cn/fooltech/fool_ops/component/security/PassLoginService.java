package cn.fooltech.fool_ops.component.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class PassLoginService {

    private static final Logger logger = LoggerFactory.getLogger(PassLoginService.class);

    private String defaultLoginName = "";

    private String defaultPassword = "";

    private AuthenticationManager authenticationManager;

    public void passLogin() {
        logger.info("===自动登录开始===");
        logger.info("===user:{}===", defaultLoginName);
        logger.info("===password:{}===", defaultPassword);
        byte[] buffer = null;
        try {
            buffer = defaultPassword.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encodePassword = new String(Base64.encode(buffer));
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                defaultLoginName, encodePassword);

        Authentication authenticated = authenticationManager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        logger.info("===自动登录结束===");
    }

    public void setDefaultLoginName(String defaultLoginName) {
        this.defaultLoginName = defaultLoginName;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
