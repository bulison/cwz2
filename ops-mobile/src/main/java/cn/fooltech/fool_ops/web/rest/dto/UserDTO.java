package cn.fooltech.fool_ops.web.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTO {

    private String login;//登录名
    private String userName;//用户名
    private String phoneOne;//联系电话
    private String deptName;//用户所属部门
    private boolean activated = true;
    private Set<String> authorities;


}
