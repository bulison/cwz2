package cn.fooltech.fool_ops.domain.sysman.vo;


/**
 * <p>表单传输对象 - 角色 </p>
 *
 * @author lzf
 * @version 1.0
 */
public class RoleVo {
    private String fid;
    private String roleName;//角色名称
    private String roleCode;//角色编码
    private String roleDesc;//角色描述
    private Short validation;//是否有效，0否1是

    /**
     * fid
     *
     * @return
     */
    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 角色名称
     *
     * @return
     */
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 角色描述
     *
     * @return
     */
    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    /**
     * 角色编码
     *
     * @return
     */
    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 是否有效
     *
     * @return
     */
    public Short getValidation() {
        return this.validation;
    }

    public void setValidation(Short validation) {
        this.validation = validation;
    }
}
