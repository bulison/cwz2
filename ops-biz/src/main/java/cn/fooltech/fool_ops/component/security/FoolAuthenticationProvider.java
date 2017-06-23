package cn.fooltech.fool_ops.component.security;

import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.utils.MD5Util;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;

import java.util.Collection;

import static cn.fooltech.fool_ops.config.Constants.REDIS_SESSION_TIME_OUT;
import static cn.fooltech.fool_ops.config.Constants.REDIS_SESSION_USER_KEY;

public class FoolAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailService;

//    @Autowired
//    private RedisService redisService;
    /**
     * 自定义验证方式
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
//        String key=REDIS_SESSION_USER_KEY+username;
//        SecurityUser user   = redisService.get(key, new TypeReference<SecurityUser>() {});
        UserDetails userDetails=null;
//        if(user==null) {
             userDetails = userDetailService.loadUserByUsername(username);
//             user = (SecurityUser) userDetails;
//             redisService.set(key, user, REDIS_SESSION_TIME_OUT);
//        }

        if (userDetails == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        SecurityUser user = (SecurityUser) userDetails;


        byte passwordByte[] = Base64.decode(password.getBytes());
        String decodePass = new String(passwordByte);

        String md5Password = MD5Util.encrypt(decodePass);
        //加密过程在这里体现
        if (!md5Password.equals(user.getPassword())) {
            throw new BadCredentialsException("用户密码错误");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, md5Password, authorities);
        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public void setUserDetailService(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

}
