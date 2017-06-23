package cn.fooltech.fool_ops.security.jwt;

import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.component.security.SecurityUser;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;


//@Component
@Service
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_KEY = "Authentication";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private String secretKey;
    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;

    @Autowired
    RedisService redisService;

    @PostConstruct
    public void init() {
        this.secretKey = "042e7d536354d3cdfab490925c55022efcb42bae";

        this.tokenValidityInMilliseconds = 1000 * 60 * 60 * 24 * 12;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * 60 * 60 * 24 * 12;
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {

        String authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        SecurityUser user = (SecurityUser) authentication.getDetails();
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("id", user.getId())
                .claim("userName", user.getUsername())
//                .claim("sex", user.getSex())
                .claim("accountName", user.getAccountName())
                .claim("password", user.getPassword())
                .claim("email", user.getEmail())
//                .claim("isMobileLogin", user.getIsMobileLogin())
                .claim("phoneOnd", user.getPhoneOne())
                .claim("deptId", user.getDeptId())
                .claim("deptName", user.getDeptName())
                .claim("userCode", user.getUserCode())
                .claim("headPortRait", user.getHeadPortRait())
                .claim("orgId", user.getOrgId())
                .claim("orgName", user.getOrgName())
//                .claim("isAdmin", user.getIsMobileLogin())
                .claim("fiscalAccountId", user.getFiscalAccountId())
                .claim("fiscalAccountName", user.getFiscalAccountName())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity)
                .compact();

        redisService.setm(TOKEN_KEY, token, user);
        //log.info("createToken:"+token);
        return token;
    }

    public Authentication getAuthentication(String token) {

        //log.info("getAuthentication:"+token);
        SecurityUser securityUser = redisService.getm(TOKEN_KEY, token, new TypeReference<SecurityUser>(){});

        if(securityUser==null)return null;

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(securityUser);
        return usernamePasswordAuthenticationToken;


    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }
}
