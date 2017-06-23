package cn.fooltech.fool_ops.domain.basedata.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>基础属性类别：地区，客户类别，征信级别，货品类别，在职状况，学历，仓库</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月6日
 */
public class AuxiliaryAttrTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fid;
    @NotBlank(message = "编号不能为空")
    @Length(max = 50, message = "编号长度超过50个字符")
    private String code;//编号
    @NotBlank(message = "名称不能为空")
    @Length(max = 50, message = "名称长度超过50个字符")
    private String name;//名称
    private String describe;//描述
    private String createTime;
    @NotNull(message = "是否有效不能为空")
    private Short enable;//1：有效 0：无效
    private String createName;
    private Short isAccount;//是否设置财务账套0:不设置 1：设置
    /**
     * 财务账套ID
     */
    private String fiscalAccountId;
    /**
     * 财务账套名称
     */
    private String fiscalAccountName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Short getIsAccount() {
        return isAccount;
    }

    public void setIsAccount(Short isAccount) {
        this.isAccount = isAccount;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }


}
