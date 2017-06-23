package cn.fooltech.fool_ops.domain.fiscal.vo;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>表单传输对象 - 财务账套</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-23 15:28:26
 */
public class FiscalAccountVo implements Serializable {

    public static final String SESSION_KEY_ID = "FiscalAccountId";
    public static final String SESSION_KEY_NAME = "FiscalAccountName";
    private static final long serialVersionUID = -1751711611941610052L;
    /**
     * 账套名
     */
    @NotBlank(message = "账套名必填")
    @Length(max = 50, message = "账套名长度超过{max}个字符")
    private String name;

    /**
     * 状态0：不可修改	1：可修改
     */
    @NotNull(message = "可修改数据必填")
    private Short enable;

    /**
     * 默认登录标识
     */
    private Short defaultFlag = FiscalAccount.FLAG_NO;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String description;

    private String fid;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public Short getDefaultFlag() {
        return this.defaultFlag;
    }

    public void setDefaultFlag(Short defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
