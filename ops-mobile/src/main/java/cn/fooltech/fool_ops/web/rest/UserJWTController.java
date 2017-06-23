package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fool_ops.component.security.FoolAuthenticationProvider;
import cn.fooltech.fool_ops.security.jwt.JWTConfigurer;
import cn.fooltech.fool_ops.security.jwt.TokenProvider;
import cn.fooltech.fool_ops.utils.StringUtils;
import cn.fooltech.fool_ops.web.rest.jwt.JWTToken;
import cn.fooltech.fool_ops.web.rest.jwt.LoginVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

//import cn.fooltech.log.FileLogger;

@RestController
@RequestMapping("/api")
public class UserJWTController {
    //    FileLogger fileLogger = new FileLogger("./log2//");
    @Inject
    FoolAuthenticationProvider foolAuthenticationProvider;
    @Inject
    private TokenProvider tokenProvider;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {
        byte[] buffer = null;
        try {
            buffer = StringUtils.trimToEmpty(loginVM.getPassword()).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encodePassword = new String(org.springframework.security.crypto.codec.Base64.encode(buffer));


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), encodePassword);


        try {

            Authentication authentication = this.foolAuthenticationProvider.authenticate(authenticationToken);


            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException", exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
