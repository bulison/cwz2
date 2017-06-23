package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.rest.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class AccountResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    @Inject
    RedisService redisService;
    @Inject
    UserService userService;

    @GetMapping("/account")
    public ResponseEntity<UserDTO> getAccount() {

        SecurityUser userVo = SecurityUtil.getSecurityUser();

        if (userVo != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setLogin(userVo.getUserCode());
            userDTO.setUserName(userVo.getUsername());
            userDTO.setPhoneOne(userVo.getPhoneOne());
            userDTO.setDeptName(userVo.getDeptName());
            userDTO.setAuthorities(userVo.getPermCodes());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @ApiOperation("修改密码")
    @PostMapping(path = "/account/change_password",
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam String password, @RequestParam String oldPassword) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        if (!SecurityUtil.checkUserPwd(oldPassword)) {
            return new ResponseEntity<>("Incorrect old password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= 4 &&
                password.length() <= 50);
    }

}
